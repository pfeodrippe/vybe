(ns vybe.game
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]
   [vybe.jolt :as vj]
   [vybe.jolt.c :as vj.c]
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.panama :as vp]
   [vybe.raylib :as vr]
   [vybe.raylib.c :as vr.c]
   [potemkin :refer [def-map-type]]
   [vybe.game :as vg]
   [nextjournal.beholder :as beholder]
   [jsonista.core :as json]
   [clojure.edn :as edn]
   [lambdaisland.deep-diff2 :as ddiff])
  (:import
   (clojure.lang IAtom2)
   (java.lang.foreign Arena ValueLayout MemorySegment)
   (org.vybe.raylib raylib)
   (org.vybe.flecs flecs)
   (org.vybe.jolt jolt)
   (vybe.flecs VybeFlecsWorldMap)))

(set! *warn-on-reflection* true)

;; -- Built-in components
(vp/defcomp Camera (org.vybe.raylib.VyCamera/layout))
(vp/defcomp Model (org.vybe.raylib.VyModel/layout))
(vp/defcomp Vector2 (org.vybe.raylib.Vector2/layout))
(vp/defcomp Vector3 (org.vybe.raylib.Vector3/layout))
(vp/defcomp Vector4 (org.vybe.raylib.Vector4/layout))
(vp/defcomp BoundingBox (org.vybe.raylib.BoundingBox/layout))

(vp/defcomp Transform vr/Matrix)

(vp/defcomp Vector4Byte
  [[:x :byte]
   [:y :byte]
   [:z :byte]
   [:w :byte]])

(vp/defcomp Shader (org.vybe.raylib.Shader/layout))
(defmethod vp/pmap-metadata Shader
  [v]
  (when-not (zero? (:id v))
    (->> (vr.c/vy-gl-get-active-parameters (:id v))
         (mapv #(into % {}))
         (into {})
         ((fn [params]
            (-> params
                (update :attributes (fn [coll]
                                      (->> (take (:attributesCount params) coll)
                                           (mapv #(update (into {} %) :name vp/->string)))))
                (update :uniforms (fn [coll]
                                    (->> (take (:uniformsCount params) coll)
                                         (mapv #(update (into {} %) :name vp/->string))))))))
         (into {}))))

(vp/defcomp Translation
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Rotation
  [[:x :float]
   [:y :float]
   [:z :float]
   [:w :float]])

(vp/defcomp Scale
  [[:x :float]
   [:y :float]
   [:z :float]])

#_(mapv #(ns-unmap *ns* %) ['Vector3 'Vector2])

;; Used for `env`, this acts as a persistent (immutable) map if you only
;; use the usual persistent functions (`get`, `assoc`, `update` etc), while
;; it will change the underlying atom if you use `IAtom` (and `IAtom2`) functions
;; like `swap!`, `reset!` etc.
;; Also, if a value is deferrable, then it will be deferred.
(def-map-type MutableMap [^IAtom2 *m ^IAtom2 *temp-m]
  (get [_ k default-value]
       (let [v (if (contains? @*temp-m k)
                 (get @*temp-m k default-value)
                 (get @*m k default-value))]
         (if (instance? clojure.lang.IDeref v)
           @v
           v)))
  (assoc [_ k v]
         (MutableMap. *m (atom (assoc @*temp-m k v))))
  (dissoc [_ k]
          (cond
            (contains? @*temp-m k)
            (MutableMap. *m (atom (dissoc @*temp-m k)))

            (contains? @*m k)
            (throw (ex-info (str "Can't dissoc a MutableMap, use `swap!` "
                                 "instead if you want to change it globally")
                            {:k k}))))
  (keys [_]
        (distinct (concat (keys @*temp-m)
                          (keys @*m))))
  (meta [_]
        (meta @*m))
  (with-meta [_ metadata]
    (MutableMap. (swap! *m with-meta metadata) *temp-m))

  IAtom2
  (swap [_ f]
        (.swap *m f))
  (swap [_ f a1]
        (.swap *m f a1))
  (swap [_ f a1 a2]
        (.swap *m f a1 a2))
  (swap [_ f a1 a2 a-seq]
        (.swap *m f a1 a2 a-seq))
  (compareAndSet [_ old new]
                 (.compareAndSet *m old new))
  (reset [_ v]
         (.reset *m v))

  (swapVals [_ f]
            (.swapVals *m f))
  (swapVals [_ f a1]
            (.swapVals *m f a1))
  (swapVals [_ f a1 a2]
            (.swapVals *m f a1 a2))
  (swapVals [_ f a1 a2 a-seq]
            (.swapVals *m f a1 a2 a-seq))
  (resetVals [_ v]
             (.resetVals *m v)))

(defn make-env
  "Mutable env."
  ([]
   (make-env {}))
  ([m]
   (->MutableMap (atom m) (atom {}))))

(comment

  (let [a (make-env)]
    (swap! a assoc :a 4)
    [(assoc a :a 5 :b 55)
     (keys (assoc a :a 5 :b 55))
     (keys a)
     a
     (:a a)])

  ())

(defonce *resources (atom {}))

(defonce ^:private *reloadable-commands (atom []))

(defn -watch-reload!
  [game-id canonical-paths builder]
  #_(println :watch-reload! game-id canonical-paths)
  (apply
   beholder/watch
   (fn [{:keys [type path] :as _x}]
     (try
       (when (contains? #{:create :modify :overflow} type)
         (swap! *reloadable-commands conj
                (fn []
                  (println :reloading game-id path)
                  (builder))))
       (catch Exception e
         (println e))))
   canonical-paths))

(defmacro reloadable
  "Make resources reloadable, useful for local dev.
  The entire `body` will re-run when the resource is modified.

  `opts` is a map that receives a `:game-id` or `:resource-paths`.

  E.g.

    (vg/reloadable {:game-id :my-id :resource-paths []}
      ...)"
  [opts & body]
  `(let [{game-id# :game-id
          resource-paths# :resource-paths
          use-atom# :use-atom} ~opts]
     (or (:resource (get @*resources game-id#))
         (let [game-id# (if (= game-id# ::uncached)
                          (keyword (gensym))
                          game-id#)
               resource-paths# (cond
                                 (seq resource-paths#) resource-paths#
                                 resource-paths# [resource-paths#]
                                 (seq game-id#) game-id#
                                 :else [game-id#])
               *atom# (atom nil)
               builder# (fn []
                          (let [res# (do ~@body)]
                            (if use-atom#
                              (do (reset! *atom# res#)
                                  *atom#)
                              res#)))
               resource# (builder#)
               canonical-paths# (->> resource-paths#
                                     (mapv #(or (some-> (io/resource %) .getPath)
                                                (-> (io/file %) .getAbsolutePath))))]
           (swap! *resources assoc game-id#
                  {:resource-path canonical-paths#
                   :resource resource#
                   :builder builder#})
           (-watch-reload! game-id# canonical-paths# builder#)
           resource#))))

;; -- Color.
(defonce color-white
  (vr/Color [255 255 255 255]))

;; -- Shader
(defn builtin-path
  "Build the path for a built-in resource."
  [res-path]
  (str "com/pfeodrippe/vybe/" res-path))

(defn- pre-process-shader
  [shader-res-path]
  (try
    (let [[res path]
          (or (when-let [r (io/resource shader-res-path)]
                [r shader-res-path])
              (when-let [r (io/resource (builtin-path shader-res-path))]
                [r (builtin-path shader-res-path)])
              (when-let [r (io/resource (builtin-path (str "shaders/" shader-res-path)))]
                [r (builtin-path (str "shaders/" shader-res-path))]))]
      (->> (slurp res)
           str/split-lines
           (mapv (fn [line]
                   (if-let [dep-relative-path (-> (re-matches #"#include \"(.*)\"" line)
                                                  last)]
                     (pre-process-shader (if (str/includes? path "com/pfeodrippe")
                                           (->> (str/split (str (str/join "/" (drop-last (str/split path #"/")))
                                                                "/" dep-relative-path) #"/")
                                                (reduce (fn [acc v]
                                                          (cond
                                                            (= v "..")
                                                            (vec (drop-last acc))

                                                            :else
                                                            (conj acc v)))
                                                        [])
                                                (str/join "/"))
                                           dep-relative-path))
                     line)))
           (str/join "\n")))
    (catch Exception e
      (throw (ex-info "Can't find shader file" {:shader-res-path shader-res-path} e)))))
#_(pre-process-shader-2 "shaders/main.fs")

(defonce *shaders-cache (atom {}))

(defn -shader-program
  [game-id vertex-res-path frag-res-path]
  (let [vertex-shader-str (pre-process-shader vertex-res-path)
        frag-shader-str (pre-process-shader frag-res-path)]
    (or (get @*shaders-cache [game-id vertex-shader-str frag-shader-str])
        (let [shader (vr.c/load-shader-from-memory vertex-shader-str frag-shader-str)]
          (when (< (:id shader) 4)
            (throw (ex-info "Error when compiling shader" {:vertex-res-path vertex-res-path
                                                           :frag-res-path frag-res-path
                                                           :shader-id (:id shader)
                                                           #_ #_:log (.getLog shader)})))
          (swap! *shaders-cache assoc [game-id vertex-res-path frag-res-path] shader)
          (swap! *shaders-cache assoc [game-id vertex-shader-str frag-shader-str] shader)
          shader))))

(defn shader-program
  "Create a shader program."
  ([game-id frag-res-path-or-map]
   (if (map? frag-res-path-or-map)
     (let [shader-map frag-res-path-or-map]
       (shader-program game-id
                       (or (::shader.vert shader-map)
                           (builtin-path "shaders/default.vs"))
                       (or (::shader.frag shader-map)
                           (builtin-path "shaders/default.fs"))))
     (shader-program game-id (builtin-path "shaders/default.vs") frag-res-path-or-map)))
  ([game-id vertex-res-path frag-res-path]
   (reloadable {:game-id game-id :resource-paths [vertex-res-path frag-res-path]
                ;; FIXME This`use-atom` will be removed in the future when we use `w` only.
                :use-atom true}
     (-shader-program game-id vertex-res-path frag-res-path))))
#_(shader-program :a "shaders/main.fs")
#_(shader-program :b "shaders/cursor.fs")
#_(shader-program :c "shaders/dither.fs")
#_(shader-program :d "shaders/noise_blur_2d.fs")
#_(shader-program :e "shaders/edge_2d.fs")
#_(shader-program :f "shaders/dof.fs")
#_(shader-program :g "shaders/shadowmap.vs" "shaders/shadowmap.fs")
#_(shader-program :h {::vg/shader.vert "shaders/shadowmap.vs"
                      ::vg/shader.frag "shaders/shadowmap.fs"})

(defn -adapt-shader
  [shader]
  shader
  #_@shader
  #_(cond
      (instance? java.lang.foreign.MemorySegment shader) shader
      (instance? clojure.lang.Atom shader) @shader
      :else (-adapt-shader (shader-program shader))))

#_(-adapt-shader "shaders/cursor.fs")

(defn component->uniform-type
  [c]
  (condp vp/layout-equal? c
    Translation (raylib/SHADER_UNIFORM_VEC3)
    Vector4 (raylib/SHADER_UNIFORM_VEC4)))
#_(component->uniform-type Vector3)

(defn set-uniform
  ([shader uniforms-map]
   (mapv (fn [[k v]]
           (set-uniform shader k v))
         uniforms-map))
  ([shader uniform value]
   (set-uniform shader uniform value {}))
  ([shader uniform value {:keys [type]}]
   (let [sp (-adapt-shader shader)
         uniform-name (name uniform)
         loc (vr.c/get-shader-location sp uniform-name)
         c (vp/component value)]
     (cond
       (instance? MemorySegment value)
       (vr.c/set-shader-value sp loc value
                              (case type
                                :vec3 (raylib/SHADER_UNIFORM_VEC3)
                                (raylib/SHADER_UNIFORM_INT)))

       (vp/layout-equal? c Transform)
       (vr.c/set-shader-value-matrix sp loc value)

       (or (vp/arr? value) (sequential? value))
       (mapv (fn [v idx]
               (set-uniform shader (str uniform-name "[" idx "]") v))
             value
             (range))

       (vp/component? c)
       (vr.c/set-shader-value sp loc value (component->uniform-type c))

       :else
       (let [t (class value)]
         (condp = t
           Integer
           (vr.c/set-shader-value sp loc (vp/int* value) (raylib/SHADER_UNIFORM_INT))

           Long
           (vr.c/set-shader-value sp loc (vp/int* value) (raylib/SHADER_UNIFORM_INT))

           Float
           (vr.c/set-shader-value sp loc (vp/float* value) (raylib/SHADER_UNIFORM_FLOAT))

           Double
           (vr.c/set-shader-value sp loc (vp/float* value) (raylib/SHADER_UNIFORM_FLOAT))

           :else
           (throw (ex-info "Type not supported (yet)" {:value value}))))))))

(defn set-uniforms
  [shader params]
  (->> params
       (mapv (fn [p]
               (set-uniform shader (first p) (second p))))))

(defmacro with-shader
  [shader-opts & body]
  `(let [opts# ~shader-opts
         [shader# params#] (if (vector? opts#)
                             opts#
                             [opts#])]
     (if shader#
       (try
         (set-uniforms shader# params#)
         (vr.c/begin-shader-mode (-adapt-shader shader#))
         ~@body
         (finally
           (vr.c/end-shader-mode)))
       (do
         ~@body))))

(defmacro with-render-texture
  [render-texture-2d & body]
  `(try
     (vr.c/begin-texture-mode ~render-texture-2d)
     ~@body
     (finally
       (vr.c/end-texture-mode))))

(defn -apply-multipass
  [shaders rect temp-1 temp-2]
  (->> (cycle [temp-1 temp-2])
       (partition-all 2 1)
       (mapv (fn [shader [t1 t2]]
               (with-render-texture t2
                 (with-shader shader
                   (vr.c/draw-texture-rec (:texture t1) rect (vr/Vector2 [0 0]) color-white))))
             shaders)))

(defonce *textures-cache (atom {}))

(defmacro with-multipass
  "Multiple shaders applied (2d only)."
  [rt opts & body]
  `(let[{shaders# :shaders} ~opts
        rt# ~rt
        width# (:width (:texture rt#))
        height# (:height (:texture rt#))
        rect# (vr/Rectangle [0 0 width# (- height#)])
        k-1# [:temp-1 width# height#]
        k-2# [:temp-2 width# height#]
        temp-1# (or (get @*textures-cache k-1#)
                    (do (swap! *textures-cache assoc k-1# (vp/with-arena-root (vr.c/load-render-texture width# height#)))
                        (get @*textures-cache k-1#)))
        temp-2# (or (get @*textures-cache k-2#)
                    (do (swap! *textures-cache assoc k-2# (vp/with-arena-root (vr.c/load-render-texture width# height#)))
                        (get @*textures-cache k-2#)))]
     (do (vg/with-render-texture temp-1#
           ~@body)

         (-apply-multipass shaders# rect# temp-1# temp-2#)

         (vg/with-render-texture rt#
           (vr.c/draw-texture-rec (:texture (if (odd? (count shaders#))
                                              temp-2#
                                              temp-1#))
                                  rect# (vr/Vector2 [0 0]) vg/color-white))

         rt#)))

;; -- Misc
(defmacro with-camera
  "3d."
  [camera & body]
  `(try
     (let [cam# ~camera]
       (vr.c/vy-begin-mode-3-d cam#))
     ~@body
     (finally
       (vr.c/end-mode-3-d))))

(defmacro with-drawing
  [& body]
  `(try
     (vr.c/begin-drawing)
     ~@body
     (finally
       (vr.c/end-drawing))))

;; -- Math
(defn matrix-transform
  [translation rotation scale]
  (let [mat-scale (if scale
                    (vr.c/matrix-scale (:x scale) (:y scale) (:z scale))
                    (vr.c/matrix-scale 1 1 1))
        mat-rotation (vr.c/quaternion-to-matrix (or rotation (Rotation {:x 0 :y 0 :z 0 :w 1})))
        mat-translation (if translation
                          (vr.c/matrix-translate (:x translation) (:y translation) (:z translation))
                          (vr.c/matrix-translate 0 0 0))]
    (vr.c/matrix-multiply (vr.c/matrix-multiply mat-scale mat-rotation) mat-translation)))

(defn matrix->translation
  [matrix]
  (Translation ((juxt :m12 :m13 :m14) matrix)))

;; -- Model
(defn- file->bytes [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))

(defn rad->degree
  [v]
  (* v (raylib/RAD2DEG)))

(defonce ^:private -resources-cache (atom {}))

;; https://wirewhiz.com/read-gltf-files/
;; https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html#gltf-file-format-specification
;; it's little endian
(defn -gltf-json
  "Read GLTF json data."
  [resource-path]
  (let [resource-bytes (file->bytes resource-path)
        sum (->> resource-bytes
                 (drop 12)
                 (take 4)
                 (reduce (fn [{:keys [mult] :as acc} v]
                           (-> acc
                               (update :sum + (* mult (bit-and 0xFF v)))
                               (update :mult * 256)))
                         {:mult 1 :sum 0})
                 :sum)
        edn (-> (->> resource-bytes
                     (drop 12)
                     (drop 8)
                     (take sum)
                     (mapv char)
                     str/join)
                (json/read-value (json/object-mapper {:decode-key-fn true}))
                (update :nodes #(vec (map-indexed (fn [idx m] (assoc m :_idx idx)) %))))]

    ;; Print diff to last model.
    (future
      (when-let [previous-edn (get @-resources-cache resource-path)]
        (let [adapter #(-> %
                           (select-keys [:scenes :nodes :cameras :extensions :accessors :meshes :materials :skins
                                         :animations])
                           (update-in [:scenes 0] dissoc :extras))
              diff (ddiff/diff (adapter previous-edn) (adapter edn))]
          (when (seq (ddiff/minimize diff))
            (println :diff resource-path)
            (ddiff/pretty-print diff)))))
    (swap! -resources-cache assoc resource-path edn)

    edn))
#_ (-> (-gltf-json "/Users/pfeodrippe/dev/games/resources/models.glb")
       #_(-gltf-json "/Users/pfeodrippe/Downloads/models.glb")
       (select-keys [:scenes :nodes :cameras :extensions :accessors :meshes :materials :skins
                     :animations])
       (update-in [:scenes 0] dissoc :extras))

(defn -gltf-buffer-0
  "Read GLTF buffer 0 data."
  [resource-path]
  (let [resource-bytes (file->bytes resource-path)
        json-bytes-count (->> resource-bytes
                              (drop 12)
                              (take 4)
                              (reduce (fn [{:keys [mult] :as acc} v]
                                        (-> acc
                                            (update :sum + (* mult (bit-and 0xFF v)))
                                            (update :mult * 256)))
                                      {:mult 1 :sum 0})
                              :sum)
        bin-with-count (->> resource-bytes
                            ;; Drop JSON data.
                            (drop 12)
                            (drop 8)
                            (drop json-bytes-count))
        bin-bytes-count (->> (take 4 bin-with-count)
                             (reduce (fn [{:keys [mult] :as acc} v]
                                       (-> acc
                                           (update :sum + (* mult (bit-and 0xFF v)))
                                           (update :mult * 256)))
                                     {:mult 1 :sum 0})
                             :sum)
        bin (->> bin-with-count
                 (drop 8)
                 (take bin-bytes-count))]

    (vec bin)))
#_ (count (-gltf-buffer-0 "/Users/pfeodrippe/dev/games/resources/models.glb"))

;; https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html#accessor-data-types
(defn- -gltf-accessor->data
  [accessor buffer buffer-views]
  (let [{:keys [type componentType bufferView count]} (update accessor :bufferView #(get buffer-views %))
        [container-type container-size] ({"SCALAR" [:scalar 1]
                                          "VEC2" [Vector2 2]
                                          "VEC3" [Vector3 3]
                                          "VEC4" [Vector4 4]
                                          "MAT2" [:mat2 4]
                                          "MAT3" [:mat3 9]
                                          "MAT4" [Transform 16]}
                                         type)
        [component-type component-type-size] ({5120 [:signed-byte 1]
                                               5121 [:unsigned-byte 1]
                                               5122 [:signed-short 2]
                                               5123 [:unsigned-short 2]
                                               5125 [:unsigned-int 4]
                                               5126 [:float 4]}
                                              componentType)
        data-size (* count component-type-size container-size)]
    (case component-type
      :float
      (let [floats (float-array (/ data-size component-type-size))]
        (-> (java.nio.ByteBuffer/wrap (byte-array (subvec (vec buffer)
                                                          (:byteOffset bufferView)
                                                          (+ (:byteOffset bufferView) data-size))))
            (.order java.nio.ByteOrder/LITTLE_ENDIAN)
            .asFloatBuffer
            (.get floats))
        (if (> container-size 1)
          (->> (partition-all container-size container-size floats)
               (mapv (comp container-type vec)))
          (vec floats)))

      (:byte :unsigned-byte)
      (let [bytes (byte-array (/ data-size component-type-size))]
        (-> (java.nio.ByteBuffer/wrap (byte-array (subvec (vec buffer)
                                                          (:byteOffset bufferView)
                                                          (+ (:byteOffset bufferView) data-size))))
            (.order java.nio.ByteOrder/LITTLE_ENDIAN)
            (.get bytes))
        (if (> container-size 1)
          (->> (partition-all container-size container-size (mapv float bytes))
               (mapv (comp (if (= container-type Vector4)
                             Vector4
                             container-type)
                           vec)))
          (vec (mapv float bytes)))))))

(vp/defcomp AnimationChannel
  {:constructor (fn [v]
                  (if (:timeline_count v)
                    v
                    (assoc v :timeline_count (count (:timeline v)))))}
  [[:timeline_count :long]
   [:values :pointer]
   [:timeline :pointer]])

(vp/defcomp AnimationPlayer
  [[:current_time :float]])

(vp/defcomp Index
  [[:index :int]])

(vp/defcomp VBO
  [[:id :int]])

(vp/defcomp Aabb
  [[:min Vector3]
   [:max Vector3]])

(defn- d
  ([msg]
   (println msg))
  ([v msg]
   (println msg)
   v))

(defn init!
  [w]
  (merge w {:vg/raycast [:vf/exclusive]})

  (when-not (:vg/phys w)
    (merge w {:vg/phys (vj/physics-system)})))

(defn gen-cube
  "Returns a hash map with `:mesh` and `:material`.

  `idx` is used just to choose some color.
  "
  [{:keys [x y z] :as _size} idx]
  (let [model (vr.c/load-model-from-mesh (vr.c/gen-mesh-cube x y z))
        model-material (first (vp/arr (:materials model) (:materialCount model) vr/Material))
        model-mesh (first (vp/arr (:meshes model) (:meshCount model) vr/Mesh))]
    ;; Set material color so we can have a better constrast.
    (-> (vr/material-get model-material (raylib/MATERIAL_MAP_ALBEDO))
        (assoc :color (vr/Color (nth [[200 155 255 255.0]
                                      [100 255 255 255.0]
                                      [240 155 155 255.0]
                                      [10 20 200 255.0]
                                      [10 255 24 255.0]]
                                     (mod idx 5)))))
    {:mesh model-mesh
     :material model-material}))

(defn- -gltf->flecs
  [w parent resource-path]
  (init! w)
  (let [{:keys [nodes cameras meshes scenes extensions animations accessors
                buffers bufferViews skins]}
        (-gltf-json resource-path)

        buffer-0 (-gltf-buffer-0 resource-path)
        node->name #(keyword "vg.gltf" (get-in nodes [% :name]))
        node->sym #(str (symbol parent) "|node|" (get-in nodes [% :name]) "|" % "|")
        ;; TODO We will support only one scene for now.
        main-scene (first scenes)
        root-nodes (set (:nodes main-scene))
        {:keys [_lights]} (:KHR_lights_punctual extensions)
        vybe-keys (mapv #(keyword (str "vybe_" %)) (range 20))
        adapted-nodes (->> nodes
                           (mapv (fn [v]
                                   (-> v
                                       (update :extras (apply juxt vybe-keys))
                                       (update :extras #(->> (remove nil? %)
                                                             (mapv edn/read-string)))))))
        model (vr.c/load-model resource-path)
        model-materials (vp/arr (:materials model) (:materialCount model) vr/Material)
        model-meshes (vp/arr (:meshes model) (:meshCount model) vr/Mesh)
        model-mesh-materials (vp/arr (:meshMaterial model) (:meshCount model) :int)
        ;; TODO We could have more than 1 skin.
        inverse-bind-matrices (when skins
                                (-> (get accessors (:inverseBindMatrices (first skins)))
                                    (-gltf-accessor->data buffer-0 bufferViews)))
        ;; Used to refer from the raylib model.
        *mesh-idx (atom 0)]

    #_(do (def model model)
          (def skins skins)
          (def model-meshes model-meshes)
          (def model-mesh-materials model-mesh-materials)
          (def animations animations)
          (def accessors accessors)
          (def buffers buffers)
          (def nodes nodes)
          (def bufferViews bufferViews)
          (def node->name node->name)
          (def node->sym node->sym)
          (def buffer-0 buffer-0)
          (def w w))

    (-> w
        (dissoc parent)

        ;; Merge rest of the stuff.
        (merge ;; The root nodes will be direct children of `parent`.
         {parent
          [(Model {:model model})
           (->> adapted-nodes
                (map-indexed vector)
                (filter (comp root-nodes first))
                (mapv
                 (fn iter
                   [[idx {:keys [_name extras translation rotation scale camera
                                 mesh children extensions]
                          :or {translation [0 0 0]
                               rotation [0 0 0 1]
                               scale [1 1 1]}}]]
                   ;; TODO Joint based on first skin, but we may have more
                   (let [joint-idx (when skins (.indexOf ^clojure.lang.PersistentVector (:joints (first skins)) idx))
                         joint? (when skins (when (>= joint-idx 0) joint-idx))
                         pos (Translation translation)
                         rot (Rotation rotation)
                         scale (Scale scale)
                         {:keys [light]} (:KHR_lights_punctual extensions)
                         light (or light
                                   ;; Return some arbitrary index, this is probably
                                   ;; an area light from Blender.
                                   (when (or (:vf/light (set extras))
                                             (:vg/light (set extras)))
                                     -1))
                         params (cond-> (conj extras pos rot scale [Transform :global] [Transform :initial]
                                              Transform [(Index idx) :node]
                                              (vf/sym (node->sym idx)))
                                  joint?
                                  (conj :vg.anim/joint
                                        [(Transform (vr.c/matrix-transpose (get inverse-bind-matrices joint-idx)))
                                         :joint]
                                        [(Index joint-idx) :joint]
                                        [(node->sym (first (:joints (first skins)))) :root-joint])

                                  (seq children)
                                  (conj (->> children
                                             (mapv (fn [c-idx]
                                                     ;; Add children as... children in Flecs.
                                                     #_[(node->name c-idx) []]
                                                     (iter [c-idx (get adapted-nodes c-idx)])))
                                             (into {})))

                                  ;; If it's a mesh, add the primitives to the main scene.
                                  ;; Raylib maps a primitive to a mesh, so we do this here as well.
                                  mesh
                                  (#(conj %
                                          (let [{:keys [primitives]} (get meshes mesh)]
                                            (->> primitives
                                                 (mapv (fn [{:keys [attributes]}]
                                                         (let [[mesh-idx _] (swap-vals! *mesh-idx inc)
                                                               {:keys [JOINTS_0 WEIGHTS_0 POSITION]} attributes
                                                               aabb (select-keys (get accessors POSITION) [:min :max])
                                                               joints (some-> (get accessors JOINTS_0)
                                                                              (-gltf-accessor->data buffer-0 bufferViews)
                                                                              vp/arr)
                                                               weights (some-> (get accessors WEIGHTS_0)
                                                                               (-gltf-accessor->data buffer-0 bufferViews)
                                                                               vp/arr)]
                                                           {(vf/_)
                                                            [(nth model-materials (nth model-mesh-materials mesh-idx))
                                                             (nth model-meshes mesh-idx)
                                                             (Aabb aabb)
                                                             (when joints
                                                               [(VBO (vr.c/rl-load-vertex-buffer
                                                                      joints
                                                                      (* (count joints) 4 4)
                                                                      true))
                                                                :joint])
                                                             (when weights
                                                               [(VBO (vr.c/rl-load-vertex-buffer
                                                                      weights
                                                                      (* (count weights) 4 4)
                                                                      true))
                                                                :weight])]})))
                                                 (into {})))))

                                  camera
                                  ;; Build a Camera, see https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html#accessors
                                  (conj :vf/camera :vg/camera
                                        (Camera
                                         {:camera {:position pos
                                                   :fovy (-> (or (get-in cameras [camera :perspective :yfov])
                                                                 0.5)
                                                             rad->degree)}
                                          :rotation rot}))

                                  light
                                  (conj :vf/light :vg/light
                                        (Camera
                                         {:camera {:position pos
                                                   :fovy 90
                                                   #_ #_:projection (raylib/CAMERA_ORTHOGRAPHIC)}
                                          :rotation rot})))]
                     {(node->name idx) params})))
                (into {}))]}))

    ;; Animation.
    (->> (mapcat :channels animations)
         (mapv (comp :node :target))
         distinct
         (mapv
          (fn [node]
            (let [e (vf/lookup-symbol w (node->sym node))
                  armature (when-let [root-joint (ffirst (get-in w [e [:* :root-joint]]))]
                             (vf/parent w root-joint))]
              (merge w
                     {(or armature e)
                      (->> animations
                           (keep (fn [anim]
                                   (let [{:keys [channels samplers name]} anim
                                         processed-channels
                                         (->> channels
                                              (keep (fn [{:keys [sampler target]}]
                                                      (let [{:keys [_interpolation output input]} (get samplers sampler)]
                                                        (when (= (:node target) node)
                                                          {(vf/_)
                                                           [:vg/channel
                                                            (AnimationChannel
                                                             {:timeline (-> (get accessors input)
                                                                            (-gltf-accessor->data buffer-0 bufferViews)
                                                                            (vp/arr :float))
                                                              :values (-> (get accessors output)
                                                                          (-gltf-accessor->data buffer-0 bufferViews)
                                                                          vp/arr)})
                                                            [:vg.anim/target-node (vf/lookup-symbol w (node->sym (:node target)))]
                                                            [:vg.anim/target-component (case (:path target)
                                                                                         "translation" Translation
                                                                                         "scale" Scale
                                                                                         "rotation" Rotation)]]}))))
                                              vec)]
                                     (when (seq processed-channels)
                                       {(keyword "vg.gltf.anim" name)
                                        (-> processed-channels
                                            (conj (AnimationPlayer) :vg/animation))}))))
                           vec)})))))

    ;; Choose one camera to be active (if no camera has this tag already).
    (let [cams (vf/with-each w [_ :vg/camera, e :vf/entity] e)]
      (when-not (some :vg/active cams)
        (conj (first cams) :vg/active))
      (vf/with-each w [_ :vg/camera, _ :vg/active, e :vf/entity]
        (assoc w parent [{:vg/camera-active [(vf/is-a e)]}])))

    ;; Add initial transforms so we can use it to correctly animate skins.
    (vf/with-each w [pos Translation, rot Rotation, scale Scale
                     transform-initial [Transform :initial]
                     transform [Transform :global]
                     transform-parent [:maybe {:flags #{:up :cascade}}
                                       [Transform :initial]]]
      (merge transform-initial (cond-> (matrix-transform pos rot scale)
                                 transform-parent
                                 (vr.c/matrix-multiply transform-parent)))
      (merge transform (cond-> (matrix-transform pos rot scale)
                         transform-parent
                         (vr.c/matrix-multiply transform-parent))))

    ;; Return world.
    w))
#_ (::uncached (-> (vf/make-world #_{:debug (fn [entity] (select-keys entity [:vf/id]))
                                     :show-all true})
                   (gltf->flecs ::uncached
                                "/Users/pfeodrippe/dev/games/resources/models.glb")
                   deref
                   #_keys))

(defn load-model
  [w game-id resource-path]
  (-gltf->flecs w game-id resource-path))

;; -- Drawing
(defn run-reloadable-commands!
  []
  (vp/with-arena-root
    (let [[commands _] (reset-vals! *reloadable-commands [])]
      (mapv #(%) commands))))

(vp/defcomp Int [[:i :long]])

(comment

     (vf/alive? w :vf.observer/update-physics)
     (vf.c/ecs-is-alive w :vf.observer/update-physics)

     (def q (-> (vf.c/ecs-observer-get w (vf/ent w :vf.observer/update-physics))
                (vp/p->map vf/observer_t)
                :query))

     (vf.c/ecs-query-find-var q "e")

     (def it (vf.c/ecs-query-iter w q))
     (vf/get-name w (vf.c/ecs-iter-get-var it 1))

     (vf.c/ecs-iter-set-var it 1 (vf/ent w :gggg))

     (vp/address (get (:vg/phys w) vj/PhysicsSystem))

     (merge w {:aaa [:vg/refers :gggg]})

     ())

;; -- Systems + Observers
(defn default-systems
  [w]
  (def w w)
  [(vf/with-system w [:vf/name :vf.system/transform
                      pos Translation, rot Rotation, scale Scale
                      transform-global [Transform :global]
                      transform-local Transform
                      transform-parent [:maybe {:flags #{:up :cascade}}
                                        [Transform :global]]]
     #_(println :AAbbbb)
     (merge transform-local (matrix-transform pos rot scale))
     (merge transform-global (cond-> transform-local
                               transform-parent
                               (vr.c/matrix-multiply transform-parent))))

   (vf/with-system w [:vf/name :vf.system/update-physics
                      {aabb-min :min aabb-max :max} vg/Aabb
                      transform-global [:meta {:flags #{:up}} [vg/Transform :global]]
                      ;; TODO Derive it from transform-global.
                      scale [:meta {:flags #{:up}} vg/Scale]
                      kinematic [:maybe {:flags #{:up}} :vj/kinematic]
                      {existing-id :i} [:maybe [Int :vj/body-id]]
                      raycast [:maybe {:flags #{:up}} [:vg/raycast :*]]
                      phys [:src :vg/phys vj/PhysicsSystem]
                      e :vf/entity]
     #_(println :existing-id existing-id :e (vf/get-name e) :phys (vp/address phys))
     (let [half #(max (/ (- (% aabb-max)
                            (% aabb-min))
                         2.0)
                      0.1)
           center #(+ (* (/ (+ (% aabb-max)
                               (% aabb-min))
                            2.0)))
           scaled #(* (half %) 2 (scale %))
           {:keys [x y z]} (vg/matrix->translation
                            (-> (vr.c/matrix-translate (center :x) (center :y) (center :z))
                                (vr.c/matrix-multiply transform-global)))
           id (if existing-id
                (when kinematic
                  #_(println :KINEMATIC existing-id)
                  (vj/body-move phys existing-id (vg/Vector3 [x y z]) 1/60)
                  existing-id)
                (vj/body-add phys (vj/BodyCreationSettings
                                   (merge {:position (vj/Vector4 [x y z 1])
                                           :rotation (vj/Vector4 [0 0 0 1])
                                           :shape (vj/box (vj/HalfExtent [(half :x) (half :y) (half :z)])
                                                          scale)}
                                          (when kinematic
                                            {:motion_type (jolt/JPC_MOTION_TYPE_KINEMATIC)})))))
           {:keys [mesh material]} (when-not existing-id
                                     (gen-cube {:x (scaled :x) :y (scaled :y) :z (scaled :z)}
                                               (rand-int 10)))]
       #_(println :---------pos [(half :x) (half :y) (half :z)])
       #_(println "\n")
       (merge w {phys
                 [{(keyword (str "vj-" id))
                   [[:vg/refers e] :vg/debug mesh material
                    (when-not existing-id
                      [(Int id) :vj/body-id])]}]

                 e [(when-not raycast
                      [:vg/raycast :vg/enabled])
                    (when-not existing-id
                      [(Int id) :vj/body-id])]})))

   (vf/with-observer w [:vf/name :vf.observer/body-removed
                        :vf/events #{:remove}
                        {id :i} [Int :vj/body-id]
                        phys [:src :vg/phys vj/PhysicsSystem]
                        e :vf/entity]
     #_(println :REMOVING id :e (vf/get-name e))
     (when (vj/body-added? phys id)
       (vj/body-remove phys id))
     (dissoc w (vf/path [phys (keyword (str "vj-" id))])))])

(comment

  (def aa (vj/physics-system))
  (def w (vf/make-world))
  (merge w {aa [{:fafa [:aa]}]})

  (get w aa)

  ())

(defn- transpose [m]
  (if (seq m)
    (apply mapv vector m)
    m))

(defn draw-scene
  "Draw scene using all the available meshes."
  ([w]
   (draw-scene w {}))
  ([w {:keys [debug]}]
   (vf/with-each w [transform-global [:maybe [vg/Transform :global]]
                    transform-global-parent [:maybe {:flags #{:up :cascade}} [vg/Transform :global]]
                    material vr/Material, mesh vr/Mesh
                    vbo-joint [:maybe [VBO :joint]], vbo-weight [:maybe [VBO :weight]]
                    _ (if debug
                        :vg/debug
                        [:not :vg/debug])]

     (when-let [transform-global (or transform-global transform-global-parent)]

       ;; Bones (if any).
       (when (and vbo-joint vbo-weight)
         ;; TODO This uniform really needs to be set only once.
         (set-uniform (:shader material)
                      {:u_jointMat
                       (mapv first (sort-by last
                                            (vf/with-each w [_ :vg.anim/joint
                                                             transform-global [vg/Transform :global]
                                                             inverse-transform [vg/Transform :joint]
                                                             [root-joint _] [:* :root-joint]
                                                             {:keys [index]} [Index :joint]]
                                              [(-> (vr.c/matrix-multiply inverse-transform transform-global)
                                                   (vr.c/matrix-multiply
                                                    (vr.c/matrix-invert
                                                     (get-in w [root-joint [vg/Transform :initial]]))))
                                               index])))})


         ;; rlEnableVertexArray(mesh.vaoId)

         (vr.c/rl-enable-shader (:id (:shader material)))
         (vr.c/rl-enable-vertex-array (:vaoId mesh))

         (vr.c/rl-enable-vertex-buffer (:id vbo-joint))
         (vr.c/rl-set-vertex-attribute 6 4 (raylib/RL_FLOAT) false 0 0)
         (vr.c/rl-enable-vertex-attribute 6)

         (vr.c/rl-enable-vertex-buffer (:id vbo-weight))
         (vr.c/rl-set-vertex-attribute 7 4 (raylib/RL_FLOAT) false 0 0)
         (vr.c/rl-enable-vertex-attribute 7))

       (vr.c/draw-mesh mesh material transform-global)))))

(defn draw-debug
  "Draw debug information (e.g. lights)."
  ([w]
   (draw-debug w {}))
  ([w {:keys [animation]}]
   (vf/with-each w [:vf/name :vf.system/draw-lights
                    :vf/phase (flecs/EcsOnStore)
                    transform-global [vg/Transform :global]
                    _ :vg/light]
     ;; TRS from a matrix https://stackoverflow.com/a/27660632
     (let [v (matrix->translation transform-global)]
       (vr.c/draw-sphere v 0.05 (vr/Color [0 185 155 255]))
       (vr.c/draw-line-3-d v
                           (-> (vg/Vector3 [0 0 -40])
                               (vr.c/vector-3-transform transform-global))
                           (vr/Color [0 185 255 255]))))

   (draw-scene w {:debug true})

   (when animation
     (vf/with-each w [_ :vg.anim/joint
                      transform-global [vg/Transform :global]
                      #_ #__joint-transform [vg/Transform :joint]]
       (let [v (matrix->translation transform-global)]
         (vr.c/draw-sphere v 0.2 (vr/Color [200 85 155 255])))))))

(defn draw-lights
  ([w shadowmap-shader depth-rts]
   (draw-lights w shadowmap-shader depth-rts draw-scene))
  ([w shadowmap-shader depth-rts draw-fn]
   (->> (vf/with-each w [material vr/Material]
          material)
        (mapv #(assoc % :shader shadowmap-shader)))

   (.set ^MemorySegment (:locs shadowmap-shader)
         ValueLayout/JAVA_INT
         (* 4 (raylib/SHADER_LOC_VECTOR_VIEW))
         (int (vr.c/get-shader-location shadowmap-shader "viewPos")))

   (vg/set-uniform shadowmap-shader
                   {:lightColor (vr.c/color-normalize (vr/Color [255 255 255 255]))
                    :ambient (vr.c/color-normalize (vr/Color [255 200 224 255]))
                    :shadowMapResolution (:width (:depth (first depth-rts)))})

   (if-let [[light-cams light-dirs] (->> (vf/with-each w [_ :vg/light, mat [vg/Transform :global], cam vg/Camera]
                                           [cam (-> (vr.c/vector-3-rotate-by-quaternion
                                                     (vg/Vector3 [0 0 -1])
                                                     (vr.c/quaternion-from-matrix mat))
                                                    vr.c/vector-3-normalize)])
                                         transpose
                                         seq)]
     (let [shadow-map-ints (range 10 (+ 10 (count light-cams)))
           _ (mapv (fn [i rt]
                     (vr.c/rl-active-texture-slot i)
                     (vr.c/rl-enable-texture (:id (:depth rt))))
                   shadow-map-ints
                   depth-rts)
           light-vps (mapv (fn [shadowmap cam]
                             (vg/with-render-texture shadowmap
                               (vr.c/clear-background (vr/Color [255 255 255 255]))
                               (vg/with-camera cam
                                 (let [light-view-proj (-> (vr.c/rl-get-matrix-modelview)
                                                           (vr.c/matrix-multiply (vr.c/rl-get-matrix-projection)))]
                                   (draw-fn w)
                                   light-view-proj))))
                           depth-rts
                           light-cams)]
       (vr.c/rl-enable-shader (:id shadowmap-shader))
       (vg/set-uniform shadowmap-shader
                       {:lightsCount (count light-dirs)
                        :lightDirs light-dirs
                        :lightVPs light-vps
                        :shadowMaps shadow-map-ints
                        :u_time (vr.c/get-time)}))
     (vg/set-uniform shadowmap-shader
                     {:lightsCount 0}))))
