(ns vybe.game
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]
   [vybe.type :as vt]
   [vybe.jolt :as vj]
   [vybe.jolt.c :as vj.c]
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.panama :as vp]
   [vybe.raylib :as vr]
   [vybe.util :as vy.u]
   [vybe.raylib.c :as vr.c]
   [potemkin :refer [def-map-type]]
   [vybe.game :as vg]
   [nextjournal.beholder :as beholder]
   [jsonista.core :as json]
   [clojure.edn :as edn]
   [lambdaisland.deep-diff2 :as ddiff])
  (:import
   (java.lang.foreign Arena ValueLayout MemorySegment)
   (org.vybe.raylib raylib)
   (org.vybe.flecs flecs)
   (org.vybe.jolt jolt)
   (vybe.flecs VybeFlecsWorldMap)
   [vybe.panama VybePMap]))

(set! *warn-on-reflection* true)

(defonce *resources (atom {}))

(defn enqueue-command!
  "Receives a zero-arity function that will be run before the next draw
  call."
  [f]
  (swap! vy.u/*commands conj f))

(defn -watch-reload!
  [game-id canonical-paths builder]
  #_(println :watch-reload! game-id canonical-paths)
  (when-not (some #(str/includes? % "jar!") canonical-paths)
    (apply
     beholder/watch
     (fn [{:keys [type path] :as _x}]
       (try
         (when (contains? #{:create :modify :overflow} type)
           (enqueue-command! (fn []
                               (println :reloading game-id path)
                               (try
                                 (builder)
                                 (catch Exception e
                                   (println e)
                                   (throw e))))))
         (catch Exception e
           (println e))))
     canonical-paths)))

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
  "Loads a shader program."
  ([w game-id]
   (shader-program w game-id {}))
  ([w game-id frag-res-path-or-map]
   (if (map? frag-res-path-or-map)
     (let [shader-map frag-res-path-or-map]
       (shader-program w
                       game-id
                       (or (::shader.vert shader-map)
                           (builtin-path "shaders/default.vs"))
                       (or (::shader.frag shader-map)
                           (builtin-path "shaders/default.fs"))))
     (shader-program w game-id (builtin-path "shaders/default.vs") frag-res-path-or-map)))
  ([w game-id vertex-res-path frag-res-path]
   (reloadable {:game-id game-id :resource-paths [vertex-res-path frag-res-path]}
     (let [shader (-shader-program game-id vertex-res-path frag-res-path)]
       (merge w {game-id [(vt/Shader shader)]})))))
#_(shader-program (vf/make-world) :eita)
#_(shader-program :a "shaders/main.fs")
#_(shader-program (vf/make-world) :b "shaders/cursor.fs")
#_(shader-program (vf/make-world) :c "shaders/dither.fs")
#_(shader-program :d "shaders/noise_blur_2d.fs")
#_(shader-program :e "shaders/edge_2d.fs")
#_(shader-program :f "shaders/dof.fs")
#_(shader-program (vf/make-world) :g "shaders/shadowmap.vs" "shaders/shadowmap.fs")

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
    vt/Translation (raylib/SHADER_UNIFORM_VEC3)
    vt/Vector4 (raylib/SHADER_UNIFORM_VEC4)))
#_(component->uniform-type vt/Vector3)

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

       (vp/layout-equal? c vt/Transform)
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
  `(let[{shaders# :shaders
         rect# :rect} ~opts
        rt# ~rt
        width# (:width (:texture rt#))
        height# (:height (:texture rt#))
        rect# (or rect# (vr/Rectangle [0 0 width# (- height#)]))
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
        mat-rotation (vr.c/quaternion-to-matrix (or rotation (vt/Rotation {:x 0 :y 0 :z 0 :w 1})))
        mat-translation (if translation
                          (vr.c/matrix-translate (:x translation) (:y translation) (:z translation))
                          (vr.c/matrix-translate 0 0 0))]
    (vr.c/matrix-multiply (vr.c/matrix-multiply mat-scale mat-rotation) mat-translation)))
#_(vg/matrix-transform
   (vt/Translation [0 0 0])
   (vt/Rotation [0 0 0 1])
   #_(vr.c/matrix-identity)
   (vt/Scale [1 1 1]))

(defn matrix->translation
  [matrix]
  (vt/Translation ((juxt :m12 :m13 :m14) matrix)))

#_(defn matrix->scale
  [matrix]
  (vt/Translation ((juxt :m12 :m13 :m14) matrix)))

(defn matrix->rotation
  [matrix]
  (-> (vr.c/quaternion-from-matrix matrix)
      vj/normalize))

;; -- vt/Model
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
    #_(when-let [previous-edn (get @-resources-cache resource-path)]
        (future
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
#_ (-gltf-json "/Users/pfeodrippe/dev/games/resources/models.glb")
#_ (-gltf-json "/Users/pfeodrippe/Library/Mobile Documents/com~apple~CloudDocs/Nomad/Project.glb")

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
                                          "VEC2" [vt/Vector2 2]
                                          "VEC3" [vt/Vector3 3]
                                          "VEC4" [vt/Vector4 4]
                                          "MAT2" [:mat2 4]
                                          "MAT3" [:mat3 9]
                                          "MAT4" [vt/Transform 16]}
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
               (mapv (comp (if (= container-type vt/Vector4)
                             vt/Vector4
                             container-type)
                           vec)))
          (vec (mapv float bytes)))))))

(defn- d
  ([msg]
   (println msg))
  ([v msg]
   (println msg)
   v))

(defn root
  "Get path to vybe.game flecs parent."
  [& ks]
  (vf/path (concat [:vg/root] ks)))

(defn gen-cube
  "Returns a hash map with `:mesh` and `:material`.

  `idx` is used just to choose some color.
  "
  ([params]
   (gen-cube params (rand-int 5)))
  ([{:keys [x y z]
     :or {x 1 y 1 z 1}
     :as _size}
    idx]
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
      :material model-material})))

(declare setup!)

;; https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html
(defn- -gltf->flecs
  [w parent resource-path]
  (setup! w)
  (let [{:keys [nodes cameras meshes scenes scene
                extensions animations accessors
                _buffers bufferViews skins]}
        (-gltf-json resource-path)

        buffer-0 (-gltf-buffer-0 resource-path)
        node->name #(keyword "vg.gltf" (get-in nodes [% :name]))
        node->sym #(str (symbol parent) "__node__" (get-in nodes [% :name]) "__" % "__")
        {:keys [_lights]} (:KHR_lights_punctual extensions)
        vybe-keys (mapv #(keyword (str "vybe_" %)) (range 20))
        adapted-nodes (->> nodes
                           (mapv (fn [v]
                                   (-> v
                                       (update :extras (apply juxt vybe-keys))
                                       (update :extras #(->> (remove nil? %)
                                                             (mapv (comp eval edn/read-string))))))))

        model (vr.c/load-model resource-path)
        model-materials (vp/arr (:materials model) (:materialCount model) vr/Material)
        model-meshes (vp/arr (:meshes model) (:meshCount model) vr/Mesh)
        model-mesh-materials (vp/arr (:meshMaterial model) (:meshCount model) :int)
        ;; TODO We could have more than 1 skin.
        inverse-bind-matrices (when skins
                                (-> (get accessors (:inverseBindMatrices (first skins)))
                                    (-gltf-accessor->data buffer-0 bufferViews)))]


    #_(do (def skins skins)
          (def scenes scenes)
          (def vybe-keys vybe-keys)
          (def model-meshes model-meshes)
          (def model-materials model-materials)
          (def model-mesh-materials model-mesh-materials)
          (def animations animations)
          (def accessors accessors)
          (def buffers buffers)
          (def nodes nodes)
          (def bufferViews bufferViews)
          (def node->name node->name)
          (def node->sym node->sym)
          (def buffer-0 buffer-0)
          (def w w)
          (def inverse-bind-matrices inverse-bind-matrices)
          (def parent parent)
          (def adapted-nodes adapted-nodes)
          (def cameras cameras)
          (def meshes meshes)
          (def root-nodes root-nodes))

    ;; When we remove a parent (in this case, the model identifier),
    ;; Flecs remove all the children as well (recursively!).
    (dissoc w parent)

    ;; Iterate over each scene.
    (doseq [[main-scene _scene-idx] (mapv vector scenes (range))
            :let [root-nodes (set (:nodes main-scene))
                  scene-name (:name main-scene)]]
      (-> w

          ;; Add symbols to all the nodes so we can reuse them later.
          (merge ;; The root nodes will be direct children of `parent`.
           {parent
            [(->> adapted-nodes
                  (map-indexed vector)
                  (filter (comp root-nodes first))
                  (mapv
                   (fn iter
                     [[idx {:keys [children]}]]
                     ;; Besides the symbol, we also add the scene name so we can
                     ;; use it as a filter when drawing the models.
                     (let [params (cond-> [(vf/sym (node->sym idx))
                                           (keyword "vg.gltf.scene" scene-name)]
                                    (seq children)
                                    (conj (->> children
                                               (mapv (fn [c-idx]
                                                       (iter [c-idx (get adapted-nodes c-idx)])))
                                               (into {}))))]
                       {(node->name idx) params})))
                  (into {}))]})

          ;; Merge rest of the stuff.
          (merge
           {parent
            (let [ ;; Used to refer from the raylib model.
                  *mesh-idx (atom 0)]
              [(vt/Model {:model model})
               (->> adapted-nodes
                    (map-indexed vector)
                    (filter (comp root-nodes first))
                    (mapv
                     (fn iter
                       [[idx {:keys [_name extras children translation rotation scale camera
                                     mesh extensions]
                              :or {translation [0 0 0]
                                   rotation [0 0 0 1]
                                   scale [1 1 1]}}]]
                       ;; TODO Joint based on first skin, but we may have more
                       (let [joint-idx (when skins
                                         (.indexOf ^clojure.lang.PersistentVector
                                                   (:joints (first skins))
                                                   idx))
                             joint? (when skins (when (>= joint-idx 0) joint-idx))
                             pos (vt/Translation translation)
                             rot (vt/Rotation rotation)
                             scale (vt/Scale scale)
                             {:keys [light]} (:KHR_lights_punctual extensions)
                             light (or light
                                       ;; Return some arbitrary index, this is probably
                                       ;; an area light from Blender.
                                       (when (or (:vf/light (set extras))
                                                 (:vg/light (set extras)))
                                         -1))
                             params (cond-> (conj extras pos rot scale [vt/Transform :global] [vt/Transform :initial]
                                                  vt/Transform [(vt/Index idx) :node])
                                      joint?
                                      (conj :vg.anim/joint
                                            [(vt/Transform (vr.c/matrix-transpose (get inverse-bind-matrices joint-idx)))
                                             :joint]
                                            [(vt/Index joint-idx) :joint]
                                            [(node->sym (first (:joints (first skins)))) :root-joint])

                                      (seq children)
                                      (conj (->> children
                                                 (mapv (fn [c-idx]
                                                         (iter [c-idx (get adapted-nodes c-idx)])))
                                                 (into {})))

                                      camera
                                      ;; Build a vt/Camera, see https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html#accessors
                                      (conj :vf/camera :vg/camera
                                            (vt/Camera
                                             {:camera {:position pos
                                                       :fovy (-> (or (get-in cameras [camera :perspective :yfov])
                                                                     0.5)
                                                                 rad->degree)}
                                              :rotation rot}))

                                      light
                                      (conj :vf/light :vg/light
                                            (vt/Camera
                                             {:camera {:position pos
                                                       :fovy 90
                                                       #_ #_:projection (raylib/CAMERA_ORTHOGRAPHIC)}
                                              :rotation rot})))

                             ;; If it's a mesh, add the primitives as children.
                             mesh-children
                             (when-let [{:keys [primitives]} (get meshes mesh)]
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
                                              {:vg.gltf.mesh/data
                                               (-> [(vt/Translation) (vt/Scale [1 1 1]) (vt/Rotation [0 0 0 1])
                                                    [vt/Transform :global] [vt/Transform :initial] vt/Transform
                                                    (nth model-materials (nth model-mesh-materials mesh))
                                                    ;; Also add scene that it participates here.
                                                    (keyword "vg.gltf.scene" scene-name)
                                                    (nth model-meshes mesh)
                                                    (when joints
                                                      [(vt/VBO (vr.c/rl-load-vertex-buffer
                                                                joints
                                                                (* (count joints) 4 4)
                                                                true))
                                                       :joint])
                                                    (when weights
                                                      [(vt/VBO (vr.c/rl-load-vertex-buffer
                                                                weights
                                                                (* (count weights) 4 4)
                                                                true))
                                                       :weight])]
                                                   ;; Store aabb as meta so we can use it
                                                   ;; to calculate the node aabb.
                                                   (with-meta {:aabb aabb}))})))
                                    (into {})))]
                         {(node->name idx) (cond-> params
                                             mesh-children
                                             (conj mesh-children
                                                   (vt/Aabb (->> (vals mesh-children)
                                                                 (mapv (comp :aabb meta))
                                                                 (reduce (fn [{acc-min :min acc-max :max :as acc}
                                                                              {aabb-min :min aabb-max :max :as aabb}]
                                                                           (if acc
                                                                             {:min (mapv min acc-min aabb-min)
                                                                              :max (mapv max acc-max aabb-max)}
                                                                             aabb))
                                                                         nil)))))})))
                    (into {}))])})))

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
                                                          (let [target-node (vf/lookup-symbol w (node->sym (:node target)))
                                                                target-component (case (:path target)
                                                                                   "translation" vt/Translation
                                                                                   "scale" vt/Scale
                                                                                   "rotation" vt/Rotation)
                                                                sym (vf/lookup-symbol w (node->sym (:node target)))]
                                                            (when (nil? sym)
                                                              (throw (ex-info "Lookup symbol shouldn't be `nil`"
                                                                              {:node node
                                                                               :node-target (:node target)
                                                                               :target target})))
                                                            {(vf/_)
                                                             [:vg/channel
                                                              (vt/AnimationChannel
                                                               {:timeline (-> (get accessors input)
                                                                              (-gltf-accessor->data buffer-0 bufferViews)
                                                                              (vp/arr :float))
                                                                :values (-> (get accessors output)
                                                                            (-gltf-accessor->data buffer-0 bufferViews)
                                                                            vp/arr)})
                                                              [:vg.anim/target-node target-node]
                                                              [:vg.anim/target-component target-component]
                                                              (vf/ref w sym target-component)]})))))
                                              vec)]
                                     (when (seq processed-channels)
                                       {(keyword "vg.gltf.anim" name)
                                        (-> processed-channels
                                            (conj (vt/AnimationPlayer) :vg/animation
                                                  ;; We put the animation as a tag as well
                                                  ;; so it's easy to trigger the same animation.
                                                  (keyword "vg.gltf.anim" name)))}))))
                           vec)})))))

    ;; Choose one camera to be active (if no camera has this tag already).
    (let [cams (->> (vf/with-query w [_ :vg/camera, e :vf/entity]
                      (when-not (= (vf/ent w :vg/camera) e)
                        e))
                    (remove nil?))]
      (when-not (some :vg/active cams)
        (conj (first cams) :vg/active))
      (vf/with-query w [_ :vg/camera, _ :vg/active, e :vf/entity]
        (assoc w e [:vg/camera-active])))

    ;; Add initial transforms so we can use it to correctly animate skins.
    (vf/with-query w [pos vt/Translation, rot vt/Rotation, scale vt/Scale
                      transform-initial [:mut [vt/Transform :initial]]
                      transform [:mut [vt/Transform :global]]
                      transform-parent [:maybe {:flags #{:up :cascade}}
                                        [vt/Transform :global]]]

      (merge transform-initial (cond-> (matrix-transform pos rot scale)
                                 transform-parent
                                 (vr.c/matrix-multiply transform-parent)))
      (merge transform (cond-> (matrix-transform pos rot scale)
                         transform-parent
                         (vr.c/matrix-multiply transform-parent))))

    ;; Returns world.
    w))
#_ (::uncached (-> (vf/make-world)
                   #_(-gltf->flecs ::uncached
                                 "/Users/pfeodrippe/dev/games/resources/models.glb")
                   (-gltf->flecs ::uncached
                                 "/Users/pfeodrippe/Library/Mobile Documents/com~apple~CloudDocs/Nomad/Project.glb")
                   #_(-gltf->flecs ::uncached
                                   "/Users/pfeodrippe/Downloads/a.glb")))

(comment

  (let [w (vf/make-world)]
    (merge w {:a [(vt/Translation [0 10])]})
    (merge w {:c [(vf/is-a :a)]})
    (update-in w [:a vt/Translation :x] inc)
    [(get (:a w) vt/Translation)
     (get (:c w) vt/Translation)])

  (let [w (vf/make-world)]
    (assoc w
           :a [(vf/override (vt/Translation [0 10]))]
           :c [(vf/is-a :a)])
    (update-in w [:a vt/Translation :x] inc)
    [(get (:a w) vt/Translation)
     (get (:c w) vt/Translation)])

  (let [w (vf/make-world)]
    (assoc w
           :c [(vf/is-a :a)]
           :a [[(vt/Translation [0 10]) :global] :ccc])
    (-> (get (:a w) [vt/Translation :global])
        (update :x inc))
    [(get (:a w) [vt/Translation :global])
     (get (:c w) [vt/Translation :global])
     (get (:c w) :ccc)])

  ())

(defn model
  "Load model."
  [w game-id resource-path]
  ;; `vg/reloadable` is a macro that will wrap the code
  ;; in a function that will be retrigged whenever
  ;; `path` is modified. In the case here, it will
  ;; watch the GLTF file (.glb is just its binary version)
  ;; and call `vg/model` again with the same arguments.
  ;; You can re-trigger any code you need!
  (reloadable {:game-id :my/model :resource-paths [resource-path]}
    (-gltf->flecs w game-id resource-path)))

(defn run-commands!
  []
  (vp/with-arena-root
    (let [[commands _] (reset-vals! vy.u/*commands [])]
      (mapv #(%) commands))))

(defn on-contact-added
  [w phys body-1 body-2]
  (let [{body-1-id :id} (vp/p->map body-1 vj/Body)
        {body-2-id :id} (vp/p->map body-2 vj/Body)]
    (vf/event! w (vj/OnContactAdded {:body-1 (vj/body phys body-1-id)
                                     :body-2 (vj/body phys body-2-id)}))))

(defn setup!
  "Setup components, it will be called by `start!`."
  [w]
  ;; Setup world.
  (merge w {:vg/raycast [:vf/exclusive]
            :vg/raycast-body [:vf/exclusive]
            :vg/camera-active [:vf/unique]})

  (when-not (get-in w [(root) vj/PhysicsSystem])
    (let [phys (vj/physics-system)]
      (merge w {(root) [phys]})))

  (let [phys (get-in w [(root) vj/PhysicsSystem])]
    (vj/contact-listener phys
                         {:on-contact-added (fn [body-1 body-2 _ _]
                                              (#'on-contact-added w phys body-1 body-2))
                          #_ #_:on-contact-validate (fn [_ _ _ _]
                                                      (jolt/JPC_VALIDATE_RESULT_ACCEPT_ALL_CONTACTS))
                          #_ #_:on-contact-persisted (fn [_ _ _ _]
                                                       (println :PERSISTED))
                          #_ #_:on-contact-removed (fn [_]
                                                     (println :REMOVED))})))
#_ (setup! w)

(defn body-path
  [body]
  (vf/path [(root) (keyword (str "vj-" (:id body)))]))

(defn input-system
  [w]
  (vf/with-system w [:vf/name :vf.system/input
                     :vf/always true
                     _ :vg/camera-active
                     camera vt/Camera
                     phys [:src (root) vj/PhysicsSystem]
                     [_ last-body-entity] [:maybe [:src :vg/raycast [:vg/raycast-body :_]]]]
    ;; -- Window.
    (when (vr.c/window-should-close)
      (vf/event! w :vg.window/on-close))

    ;; -- Raycast.
    (let [{:keys [position direction]} (-> (vr.c/get-mouse-position)
                                           (vr.c/vy-get-screen-to-world-ray camera))
          direction (mapv #(* % 10000) (vals direction))
          body (vj/cast-ray phys position direction)
          path (body-path body)]
      (if (and body (get-in w [(get-in w [path vt/Eid :id])
                               [:vg/raycast :vg/enabled]]))
        ;; Only trigger hover is not the same body as before.
        (let [same-body? (get-in w [:vg/raycast [:vg/raycast-body path]])]
          (when-not same-body?
            (assoc w :vg/raycast [[:vg/raycast-body path]]))
          (if (vr.c/is-mouse-button-pressed (raylib/MOUSE_BUTTON_LEFT))
            (vf/event! w path :vg.raycast/on-click)
            (do (when-not same-body?
                  (vf/event! w path :vg.raycast/on-enter))
                (vf/event! w path :vg.raycast/on-hover))))
        (when last-body-entity
          (update w :vg/raycast disj [:vg/raycast-body last-body-entity])
          (vf/event! w :vg.raycast/on-leave))))))

;; -- Systems + Observers
(defn default-systems
  [w]
  #_(def w w)
  [#_(vf/with-system w [:vf/name :vf.system/transform
                        pos vt/Translation, rot vt/Rotation, scale vt/Scale
                        transform-global [:out [vt/Transform :global]]
                        transform-local [:out vt/Transform]
                        transform-parent [:maybe {:flags #{:up :cascade}}
                                          [vt/Transform :global]]]
       (let [local (matrix-transform pos rot scale)]
         (merge transform-local local)
         (merge transform-global (cond-> local
                                   transform-parent
                                   (vr.c/matrix-multiply transform-parent)))))

   (vf/with-system w [:vf/name :vf.system/update-physics
                      ;; TODO Derive it from transform-global.
                      scale vt/Scale
                      {aabb-min :min aabb-max :max} vt/Aabb
                      vy-body [:maybe vj/VyBody]
                      transform-global [vt/Transform :global]
                      kinematic [:maybe :vg/kinematic]
                      dynamic [:maybe :vg/dynamic]
                      sensor [:maybe :vg/sensor]
                      ;; Used to find if we are setting `[:vg/raycast :vg/disabled]`
                      ;; in Blender.
                      raycast [:maybe {:flags #{:up :self}}
                               [:vg/raycast :*]]
                      phys [:src (root) vj/PhysicsSystem]
                      e :vf/entity
                      it :vf/iter]
     #_(println :e (vf/get-name e) :kin kinematic)
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
           body (if vy-body
                  (do (when kinematic
                        #_(println :KINEMATIC (matrix->rotation transform-global))
                        (vj/move vy-body (vt/Vector3 [x y z]) (matrix->rotation transform-global) (:delta_time it)))
                      vy-body)
                  (let [body (vj/body-add phys (vj/BodyCreationSettings
                                                (cond-> {:position #_(vt/Vector4 [0 0 0 1])
                                                         (vt/Vector4 [x y z 1])
                                                         :rotation #_(vt/Rotation [0 0 0 1])
                                                         (matrix->rotation transform-global)
                                                         :shape (vj/box (vj/HalfExtent [(half :x) (half :y) (half :z)])
                                                                        scale
                                                                        #_(vt/Vector4 [x y z 1])
                                                                        #_(vt/Translation [0 0 0])
                                                                        #_(matrix->rotation transform-global))}
                                                  kinematic
                                                  (assoc :motion_type (jolt/JPC_MOTION_TYPE_KINEMATIC))

                                                  sensor
                                                  (assoc :is_sensor true)

                                                  dynamic
                                                  (assoc :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                                                         :object_layer :vj.layer/moving))))]
                    (when (= (vf/get-name e) (vf/path [:my/model :vg.gltf/my-cube]))
                      #_(clojure.pprint/pprint (-> (vj/-body-get phys (:id body))
                                                   :motion_properties
                                                   #_(vp/p->map vj/MotionProperties))))
                    body))
           {:keys [mesh material]} (when-not vy-body
                                     (gen-cube {:x (scaled :x) :y (scaled :y) :z (scaled :z)}
                                               (rand-int 10)))]
       (merge w {(body-path body)
                 [:vg/debug mesh material phys body
                  (vt/Eid e)]

                 e [phys body
                    (when-not raycast
                      [:vg/raycast :vg/enabled])]})))

   (input-system w)

   (vf/with-observer w [:vf/name :vf.observer/body-removed
                        :vf/events #{:remove}
                        body vj/VyBody
                        {:keys [id]} [:maybe vt/Eid]]
     #_(println :REMOVING body :mesh-entity mesh-entity)
     (when (vj/added? body)
       (vj/remove* body))
     (dissoc w (body-path body) id))])

(defn- transpose [m]
  (if (seq m)
    (apply mapv vector m)
    m))

;; -- Drawing
(defn shadowmap-render-texture
  "Creates a shadow map render texture, see
  https://github.com/raysan5/raylib/blob/master/examples/shaders/shaders_shadowmap.c#L202."
  [width height]
  (let [rt (vr/RenderTexture2D)
        id (vr.c/rl-load-framebuffer)
        _ (assoc rt :id id)
        _ (vr.c/rl-enable-framebuffer id)
        tex-depth-id (vr.c/rl-load-texture-depth width height false)]
    (merge rt {:texture {:width width, :height height}
               :depth {:id tex-depth-id, :width width, :height height,
                       :format 19, :mipmaps 1}})
    (vr.c/rl-framebuffer-attach id tex-depth-id (raylib/RL_ATTACHMENT_DEPTH) (raylib/RL_ATTACHMENT_TEXTURE2D) 0)
    (when-not (vr.c/rl-framebuffer-complete id)
      (throw (ex-info "Couldn't create frame buffer" {})))
    (vr.c/rl-disable-framebuffer)

    rt))
#_(shadowmap-render-texture 600 600)

(defn draw-scene
  "Draw scene using all the available meshes."
  ([w]
   (draw-scene w {}))
  ([w {:keys [debug scene]}]
   (vf/with-query w [transform-global [vt/Transform :global]
                     material vr/Material, mesh vr/Mesh
                     vbo-joint [:maybe [vt/VBO :joint]], vbo-weight [:maybe [vt/VBO :weight]]
                     _ (if debug
                         :vg/debug
                         [:not :vg/debug])
                     _ (or scene :_)
                     e :vf/entity]
     #_(when (= (vf/get-name (vf/parent e))
                '(vybe.flecs/path [:my/model :vg.gltf/Sphere]))
         (println :BBB (matrix->translation transform-global)))
     ;; Bones (if any).
     (when (and vbo-joint vbo-weight)
       ;; TODO This uniform really needs to be set only once.
       (set-uniform (:shader material)
                    {:u_jointMat
                     (mapv first (sort-by last
                                          (vf/with-query w [_ :vg.anim/joint
                                                            transform-global [vt/Transform :global]
                                                            inverse-transform [vt/Transform :joint]
                                                            [root-joint _] [:* :root-joint]
                                                            {:keys [index]} [vt/Index :joint]]
                                            [(-> (vr.c/matrix-multiply inverse-transform transform-global)
                                                 (vr.c/matrix-multiply
                                                  (vr.c/matrix-invert
                                                   (get-in w [root-joint [vt/Transform :initial]]))))
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

     #_(vr.c/draw-mesh-instanced mesh material transform-global 1)
     (vr.c/draw-mesh mesh material transform-global))))

(defn draw-debug
  "Draw debug information (e.g. lights)."
  ([w]
   (draw-debug w {}))
  ([w {:keys [animation]}]
   (vf/with-query w [transform-global [vt/Transform :global]
                    _ :vg/light]
     ;; TRS from a matrix https://stackoverflow.com/a/27660632
     (let [v (matrix->translation transform-global)]
       (vr.c/draw-sphere v 0.05 (vr/Color [0 185 155 255]))
       (vr.c/draw-line-3-d v
                           (-> (vt/Vector3 [0 0 -40])
                               (vr.c/vector-3-transform transform-global))
                           (vr/Color [0 185 255 255]))))

   (draw-scene w {:debug true})

   (when animation
     (vf/with-query w [_ :vg.anim/joint
                      transform-global [vt/Transform :global]
                      #_ #__joint-transform [vt/Transform :joint]]
       (let [v (matrix->translation transform-global)]
         (vr.c/draw-sphere v 0.2 (vr/Color [200 85 155 255])))))))

(defn- -get-depth-rts
  [w]
  (let [depth-rts (vf/with-query w [rt [:mut [vr/RenderTexture2D :depth-render-texture]]]
                    rt)]
    (if (seq depth-rts)
      depth-rts
      (let [{:keys [width height]} (get-in w [:vg/root vt/ScreenSize])]
        (merge w (->> (range 10)
                      (mapv (fn [_]
                              [(root (vf/_)) [[(shadowmap-render-texture width height)
                                               :depth-render-texture]]]))
                      (into {})))
        (vf/with-query w [rt [:mut [vr/RenderTexture2D :depth-render-texture]]]
          rt)))))

#_(def w (vf/make-world))

(defn draw-lights
  ([w shader]
   (draw-lights w shader draw-scene))
  ([w shader draw-fn]
   (draw-lights w shader draw-fn {}))
  ([w shader draw-fn {:keys [scene]}]
   (let [depth-rts (-get-depth-rts w)]
     (vf/with-query w [material [:mut vr/Material]
                       _ (or scene :_)]
       (assoc material :shader shader))

     (.set ^MemorySegment (:locs shader)
           ValueLayout/JAVA_INT
           (* 4 (raylib/SHADER_LOC_VECTOR_VIEW))
           (int (vr.c/get-shader-location shader "viewPos")))

     (vg/set-uniform shader
                     {:lightColor (vr.c/color-normalize (vr/Color [255 255 255 255]))
                      :ambient (vr.c/color-normalize (vr/Color [255 200 224 255]))
                      :shadowMapResolution (:width (:depth (first depth-rts)))})

     (if-let [[light-cams light-dirs] (->> (vf/with-query w [_ :vg/light, mat [vt/Transform :global], cam vt/Camera
                                                             _ (or scene :_)]
                                             [cam (-> (vr.c/vector-3-rotate-by-quaternion
                                                       (vt/Vector3 [0 0 -1])
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
         (vr.c/rl-enable-shader (:id shader))
         (vg/set-uniform shader
                         {:lightsCount (count light-dirs)
                          :lightDirs light-dirs
                          :lightVPs light-vps
                          :shadowMaps shadow-map-ints
                          :u_time (vr.c/get-time)}))
       (vg/set-uniform shader
                       {:lightsCount 0})))))

(defn debug-init!
  "Initiate debug mode (call this before caling `start!`, debug ise only setup in non PROD modes
  (e.g. if you don't have `VYBE_PROD=true` as an env var or JVM property.

  It will print messages to the console, start clerk (access it in http://localhost:7777/'vybe.clerk)
  and enable rest (access it in https://www.flecs.dev/explorer)."
  [w]
  (when-not vy.u/prd?
    (vy.u/debug-set! true)

    (vy.u/debug "Initiating debug mode...")

    ((requiring-resolve 'vybe.clerk/init!) {})
    (eval `(swap! vybe.clerk/*docs merge vf/docs vt/docs))

    #_(vf/rest-enable! w))

  w)

(defn start!
  "Start game.

  `w` is `world` from `vybe.flecs/make-world`.

  `draw-fn-var` receives `w` and `delta-time` as its arguments, the function
  will be wrapped with `vp/with-arena` so we don't have memory leaks.

  `init-fn` receives `w` as its argument.
  Don't use functions that creates new threads in `init-fn` (e.g. `pmap`)."
  [w screen-width screen-height draw-fn-var init-fn]
  (when-not (var? draw-fn-var)
    (throw (ex-info "`draw-fn-var` should be a var" {})))

  (setup! w)
  (merge w {:vg/root [(vt/ScreenSize [screen-width screen-height])]})

  (vf/eid w vt/Translation)
  (vf/eid w vt/Rotation)
  (vf/eid w vt/Scale)
  (vf/eid w vt/Transform)
  (vf/eid w :global)
  (vf.c/vybe-default-systems-c w)

  ;; `vr/t` is used so we run the command in the main thread.
  (vr/t (init-fn w))
  (alter-var-root #'vr/draw
                  (constantly
                   (fn []
                     (vp/with-arena _
                       (run-commands!)
                       (draw-fn-var w (vr.c/get-frame-time)))))))
