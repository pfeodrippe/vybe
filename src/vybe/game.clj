(ns vybe.game
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.math :as math]
   [clojure.string :as str]
   [jsonista.core :as json]
   [lambdaisland.deep-diff2 :as ddiff]
   [nextjournal.beholder :as beholder]
   [potemkin :refer [def-map-type]]
   [vybe.c :as vc]
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.game :as vg]
   [vybe.game.system :as vg.s]
   [vybe.jolt :as vj]
   [vybe.jolt.c :as vj.c]
   [vybe.math :as vm]
   [vybe.panama :as vp]
   [vybe.raylib :as vr]
   [vybe.raylib.c :as vr.c]
   [vybe.type :as vt]
   [vybe.util :as vy.u])
  (:import
   (java.lang.foreign Arena ValueLayout MemorySegment)
   (org.vybe.raylib raylib)
   (org.vybe.flecs flecs)
   (org.vybe.jolt jolt)
   (vybe.flecs VybeFlecsWorldMap VybeFlecsEntitySet)
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

(defn- -shader-find
  [shader-res-path]
  (or (when-let [r (io/resource shader-res-path)]
                [r shader-res-path])
              (when-let [r (io/resource (builtin-path shader-res-path))]
                [r (builtin-path shader-res-path)])
              (when-let [r (io/resource (builtin-path (str "shaders/" shader-res-path)))]
                [r (builtin-path (str "shaders/" shader-res-path))])))

(defn- pre-process-shader
  [shader-res-path]
  (try
    (let [[res path] (-shader-find shader-res-path)]
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

(declare set-uniform)
(defn shader-program
  "Loads a shader program.

  The map in `frag-res-path-or-map` can receive

    - `:vert`, path to a vertex shader, defaults to the default vertex shader
    - `:frag`, path to a fragment shader, defaults to the default fragment shader
    - `:uniforms`, map of uniform to values to be set for this shader"
  ([w game-id]
   (shader-program w game-id {}))
  ([w game-id frag-res-path-or-map]
   (if (map? frag-res-path-or-map)
     (let [shader-map frag-res-path-or-map]
       (shader-program w
                       game-id
                       (or (:vert shader-map)
                           (builtin-path "shaders/default.vs"))
                       (or (:frag shader-map)
                           (builtin-path "shaders/default.fs"))
                       shader-map))
     (shader-program w game-id (builtin-path "shaders/default.vs") frag-res-path-or-map)))
  ([w game-id vertex-res-path frag-res-path]
   (shader-program w game-id vertex-res-path frag-res-path {}))
  ([w game-id vertex-res-path frag-res-path {:keys [uniforms]}]
   (let [[_ vertex-res-path] (-shader-find vertex-res-path)
         [_ frag-res-path] (-shader-find frag-res-path)]
     (reloadable {:game-id game-id :resource-paths [vertex-res-path frag-res-path]}
       (let [shader (-shader-program game-id vertex-res-path frag-res-path)]
         (set-uniform shader uniforms)
         (merge w {game-id [(vt/Shader shader)]}))))))
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
    vt/Vector4 (raylib/SHADER_UNIFORM_VEC4)
    vt/Vector2 (raylib/SHADER_UNIFORM_VEC2)))
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

       (= c vr/Color)
       (set-uniform shader uniform-name (->> value
                                             ((juxt :r :g :b :a))
                                             (mapv (fn [v]
                                                     (/ (if (neg? v)
                                                          (+ 255 (inc v))
                                                          v)
                                                        255.0)))
                                             vt/Vector4))

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
  "Render body to render texture."
  [render-texture-2d & body]
  `(try
     (vr.c/begin-texture-mode ~render-texture-2d)
     (vr.c/clear-background (vr/Color [20 20 20 0]))
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
                   (vr.c/clear-background (vr/Color [20 20 20 0]))
                   (vr.c/draw-texture-rec (:texture t1) rect (vr/Vector2 [0 0]) color-white))))
             shaders)))

(defonce *textures-cache (atom {}))

(defmacro with-fx
  "Apply shaders.

  - `rt` is a RenderTexture
  - `opts` is a map
      - `:shaders`, a list of list of shaders with its params
      - `:rect`, render size, a `vr/Rectangle`
      - `:flip-y`, boolean that tell us if the result should be, useful for when
        you want to use the render texture in a shader

  E.g.

    (vg/with-fx (get render-texture vr/RenderTexture2D) {:shaders
                                                          [[(get noise-blur-shader vt/Shader)
                                                            {:u_radius (+ 1.0
                                                                          #_(* (vr.c/vector-3-length velocity) 0.1)
                                                                          (rand 1))}]

                                                          [(get dither-shader vt/Shader)
                                                            {:u_offsets (vt/Vector3 (mapv #(* % (+ 0.6
                                                                                                   (wobble 0.3)))
                                                                                          [0.02 (+ 0.016 (wobble 0.01))
                                                                                           (+ 0.040 (wobble 0.01))]))}]]}
      (vr.c/clear-background (vr/Color \"#A98B39\"))
      (vg/with-camera camera
        (draw-scene w)))"
  [rt opts & body]
  `(let[{shaders# :shaders
         rect# :rect
         flip-y# :flip-y}
        ~opts

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
                                  (if flip-y#
                                    (update rect# :height -)
                                    rect#)
                                  (vr/Vector2 [0 0])
                                  vg/color-white))

         rt#)))

(defmacro with-fx-default
  "Like `with-fx`, but you don't need to pass rt.

  `opts` receives:

      - `:rt`, render texture to be used instead of the default one, a `vr/RenderTexture2D`
      - see `with-fx` for more parameters"
  [w opts & body]
  `(let [opts# ~opts
         rt# (or (:rt opts#)
                 (get (::render-texture ~w) vr/RenderTexture2D))]
     (vg/with-fx rt# opts#
       ~@body)

     rt#))

(defmacro with-target
  "Render to target entity (e.g. render a scene into a plane so you can present
  it as a TV screen).

  `opts` receives:

      - `:target`, target Flecs entity
      - `:rt`, render texture to be used instead of the default one, a `vr/RenderTexture2D`
      - see `with-fx` for more parameters"
  [opts & body]
  `(let [opts# ~opts
         target# (:target opts#)
         w# (VybeFlecsEntitySet/.w target#)
         rt# (or (:rt opts#)
                 (get (::render-texture w#) vr/RenderTexture2D))]
     ;; Set target as the material.
     (-> (w# (vf/path [(vf/get-name target#) :vg.gltf.mesh/data]))
         (get vr/Material)
         (vr/material-get (raylib/MATERIAL_MAP_DIFFUSE))
         (assoc-in [:texture] (:texture rt#)))

     (vg/with-fx rt# (merge {:flip-y true} opts#)
       ~@body)))

(defn wobble
  "Wobble some value (based on time)."
  ([v]
   (wobble v 1.0))
  ([v freq]
   (* v (math/sin (* (vr.c/get-time) freq)))))

(defn wobble-rand
  "Wobble some value (random-like)."
  ([v]
   (wobble-rand v 1.0))
  ([v freq]
   (let [f #(wobble v (* % freq))]
     (+ (f 2) (* (f 3) (f 4.5))))))

(defn fx-painting
  "Painting-like effect (using shaders). Ready to be used with
  `with-drawing-fx` or `with-fx`."
  ([w]
   (fx-painting w {}))
  ([w {:keys [dither-radius]
       :or {dither-radius 0.5}}]
   [[(get (::shader-noise-blur w) vt/Shader)
     {:u_radius (+ 1.0 (rand 1))}]

    [(get (::shader-dither w) vt/Shader)
     {:u_offsets (vt/Vector3 (mapv #(* % (+ 0.6
                                            (wobble 0.3)))
                                   [0.02 (+ 0.016 (wobble 0.01))
                                    (+ 0.040 (wobble 0.01))]))
      :u_radius dither-radius}]]))

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
  "Drawing context. Call it only once per loop."
  [& body]
  `(try
     (vr.c/begin-drawing)
     ~@body
     (finally
       (vr.c/end-drawing))))

(defmacro with-drawing-fx
  "Draw with effects. Use this inside `with-drawing`.

  Check `with-fx` for the map options of `fx-or-map`.

  E.g.

  (vg/with-drawing
    (vg/with-drawing-fx w (vg/fx-painting w)
      (vr.c/clear-background (vr/Color [255 20 100 255]))

      (vf/with-query w [_ :vg/camera-active
                        camera vt/Camera]
        (vg/with-camera camera
          (vg/draw-scene w)))

      (vr.c/draw-fps 510 570)))"
  [w fx-or-map & body]
  `(let [fx# ~fx-or-map
         rt# (or (:rt fx#)
                 (get (::render-texture ~w) vr/RenderTexture2D))
         width# (:width (:texture rt#))
         height# (:height (:texture rt#))]
     (vg/with-fx rt# (if (map? fx#)
                       fx#
                       {:shaders fx#})
       ~@body)

     (vr.c/draw-texture-pro
      (:texture rt#)
      (vr/Rectangle [0 0 width# (- height#)])
      (vr/Rectangle [0 0 width# height#])
      (vr/Vector2 [0 0]) 0 vg/color-white)

     rt#))

(defonce ^:private -resources-cache (atom {}))

;; -- Model.
(defn- file->bytes [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))

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
                (update :nodes #(vec (map-indexed (fn [idx m] (assoc m :_idx idx)) %))))
        edn (into (sorted-map) edn)]

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
#_ (-gltf-json "/Users/pfeodrippe/dev/vybe-games/resources/models.glb")
#_ (-gltf-json "/Users/pfeodrippe/dev/vybe-games/resources/models2.glb")
#_ (-gltf-json "/Users/pfeodrippe/dev/vybe-games/resources/noel.glb")
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
  (apply vg.s/root ks))

(defn- -safe-eval-model-meta
  [node v]
  (cond
    (nil? v)
    v

    (vector? v)
    (mapv #(-safe-eval-model-meta node %) v)

    (keyword? v)
    v

    ;; A call (component instance).
    (list? v)
    (try
      (let [[c-sym data] v
            c @(resolve c-sym)]
        (when-not (vp/component? c)
          (throw (ex-info "Not a component" {:c c})))
        (c data))
      (catch Exception ex
        (throw (ex-info "Unsupported GLB metadata (extra)"
                        {:v v
                         :node node
                         :ex ex}))))

    :else
    (throw (ex-info "Unsupported GLB metadata (extra)"
                    {:v v
                     :node node}))))

(defn model-nodes
  "Debug nodes from a blender model."
  [resource-path]
  (let [{:keys [nodes]} (-gltf-json resource-path)
        vybe-keys (mapv #(keyword (str "vybe_" %)) (range 20))]
    (->> nodes
         (mapv (fn [node]
                 (-> node
                     (update :extras (apply juxt vybe-keys))
                     (update :extras (fn [extras]
                                       (->> (remove nil? extras)
                                            (mapv (comp #(-safe-eval-model-meta node %)
                                                        edn/read-string)))))))))))
#_ (model-nodes "resources/models.glb")

(declare setup!)

#_(def aaa
    (vr/Model
     {:transform {:m0 1.0,
                  :m4 0.0,
                  :m8 0.0,
                  :m12 0.0,
                  :m1 0.0,
                  :m5 1.0,
                  :m9 0.0,
                  :m13 0.0,
                  :m2 0.0,
                  :m6 0.0,
                  :m10 1.0,
                  :m14 0.0,
                  :m3 0.0,
                  :m7 0.0,
                  :m11 0.0,
                  :m15 1.0},
      :meshCount 1
      :materialCount 2
      :boneCount 0
      :materials (vp/arr [(vr/Material {:shader {:id 3}})
                          (vr/Material {:shader {:id 3}})])
      :meshes (vp/arr [(vr/Mesh
                        {:vertexCount 24,
                         :triangleCount 12,
                         :vaoId 2})])
      :meshMaterial (vp/arr [1] :int)}))
#_ (do model-loaded)

(defn ^:dynamic *load-model*
  "DON'T use this function directly, use `model` instead.

  Dynamic var to be patched when testing so we can avoid
  using raylib during testing."
  [resource-path]
  (vr.c/load-model resource-path))

(defonce ^:private *color-identifiers (atom #{}))
(defonce ^:private *color-identifier->entity-sym (atom {}))
(defonce ^:private *identifier->color-identifier (atom {}))

(defn color-identifier
  "Generates a unique color identifier or returns the existing one
  (for the same world + identifier)."
  [w identifier]
  ;; We use `255` for the random values instead of `256` so we don't have
  ;; conflicts in our shaders.
  (or (get-in @*identifier->color-identifier [(vp/mem (vf/get-world w)) identifier])
      (let [color (vp/with-arena-root
                    (vr/Color [(rand-int 255)
                               (rand-int 255)
                               (rand-int 255)
                               255]))
            colors-count (count @*color-identifiers)
            color-id (if (= colors-count (count (swap! *color-identifiers conj color)))
                       (color-identifier w identifier)
                       (do (swap! *color-identifier->entity-sym assoc-in [(vp/mem (vf/get-world w)) color] identifier)
                           color))]
        (swap! *identifier->color-identifier assoc-in [(vp/mem (vf/get-world w)) identifier] color-id)
        color-id)))

(defn color-identifier->entity
  [w color-identifier]
  (when-let [sym (get-in @*color-identifier->entity-sym [(vp/mem (vf/get-world w)) color-identifier])]
    (vf/lookup-symbol w sym)))

;; https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html
(defn -gltf->flecs
  [w parent resource-path]
  (setup! w)
  (let [{:keys [nodes cameras meshes scenes _scene
                extensions animations accessors
                _buffers bufferViews skins]}
        (-gltf-json resource-path)

        buffer-0 (-gltf-buffer-0 resource-path)
        node->name-raw #(get-in nodes [% :name])
        node->name #(keyword "vg.gltf" (get-in nodes [% :name]))
        node->sym #(str (symbol parent) "__node__" (get-in nodes [% :name]) "__" % "__")
        {:keys [_lights]} (:KHR_lights_punctual extensions)
        vybe-keys (mapv #(keyword (str "vybe_" %)) (range 20))
        adapted-nodes (->> nodes
                           (mapv (fn [node]
                                   (-> node
                                       (update :extras (apply juxt vybe-keys))
                                       (update :extras (fn [extras]
                                                         (->> (remove nil? extras)
                                                              (mapv (comp #(-safe-eval-model-meta node %)
                                                                          edn/read-string)))))))))

        model-loaded (*load-model* resource-path)
        model-materials (vp/arr (:materials model-loaded) (:materialCount model-loaded) vr/Material)
        model-meshes (vp/arr (:meshes model-loaded) (:meshCount model-loaded) vr/Mesh)
        model-mesh-materials (vp/arr (:meshMaterial model-loaded) (:meshCount model-loaded) :int)
        ;; TODO We could have more than 1 skin.
        inverse-bind-matrices (when skins
                                (-> (get accessors (:inverseBindMatrices (first skins)))
                                    (-gltf-accessor->data buffer-0 bufferViews)))]

    (vy.u/debug ::-gltf->flecs
                {:parent parent
                 :resource-path resource-path
                 :materials-pointer (vp/mem model-materials)
                 :meshes-pointer (vp/mem model-meshes)
                 :mesh-material-pointer (vp/mem model-mesh-materials)
                 :model-mesh-materials model-mesh-materials
                 :model-materials-count (count model-materials)
                 :model-meshes-count (count model-meshes)})

    #_(do (def skins skins)
          (def scenes scenes)
          (def vybe-keys vybe-keys)
          (def model-loaded model-loaded)
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
    (dissoc w :vg.internal/camera-move!)

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
              [(vt/Model {:model model-loaded})
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
                             params (cond-> (conj extras pos rot scale vt/Velocity
                                                  [vt/Transform :global] [vt/Transform :initial]
                                                  vt/Transform [(vt/Index idx) :node]
                                                  (vt/EntityName (node->name-raw idx))
                                                  [(color-identifier w (node->sym idx)) :color-identifier])
                                      (str/includes? (node->name-raw idx) "__collider")
                                      (conj :vg/kinematic :vg/collider)

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
                                                                 vm/rad->degree)}
                                              :rotation rot}))

                                      light
                                      (conj :vf/light :vg/light
                                            (vt/Camera
                                             {:camera {:position pos
                                                       :fovy 10
                                                       :projection (raylib/CAMERA_ORTHOGRAPHIC)}
                                              :rotation rot})))

                             ;; If it's a mesh, add the primitives as children.
                             mesh-children
                             (when-let [{:keys [primitives]} (get meshes mesh)]
                               (->> primitives
                                    (mapv (fn [{:keys [attributes]}]
                                            (let [[_mesh-idx _] (swap-vals! *mesh-idx inc)
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
                                                                                   "rotation" vt/Rotation)]
                                                            (when (nil? target-node)
                                                              (throw (ex-info "Lookup symbol shouldn't be `nil`"
                                                                              {:node node
                                                                               :node-target (:node target)
                                                                               :target target})))
                                                            {(vf/_)
                                                             [:vg/channel
                                                              (vt/AnimationChannel
                                                               {:kind ({vt/Translation 0
                                                                        vt/Scale 1
                                                                        vt/Rotation 2}
                                                                       target-component)
                                                                :timeline (-> (get accessors input)
                                                                              (-gltf-accessor->data buffer-0 bufferViews)
                                                                              (vp/arr :float))
                                                                :values (-> (get accessors output)
                                                                            (-gltf-accessor->data buffer-0 bufferViews)
                                                                            vp/arr)})
                                                              [:vg.anim/target-node target-node]
                                                              [:vg.anim/target-component target-component]
                                                              (vf/ref w target-node target-component)]})))))
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

      (merge transform-initial (cond-> (vm/matrix-transform pos rot scale)
                                 transform-parent
                                 (vr.c/matrix-multiply transform-parent)))
      (merge transform (cond-> (vm/matrix-transform pos rot scale)
                         transform-parent
                         (vr.c/matrix-multiply transform-parent))))

    ;; Returns world.
    w))
#_ (::uncached (-> (vf/make-world)
                   #_(-gltf->flecs ::uncached
                                   "/Users/pfeodrippe/dev/games/resources/models.glb")
                   #_(-gltf->flecs ::uncached
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

(defn resource
  "Get application resource path, extracting if necessary."
  ([resource-relative-path]
   (resource resource-relative-path {}))
  ([resource-relative-path params]
   (vy.u/app-resource resource-relative-path params)))

(defn model
  "Load model.

  E.g.

    (vg/model w :my/model (vg/resource \"com/pfeodrippe/vybe/model/minimal.glb\"))"
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
  [w phys body-1 body-2 contact-manifold _contact-settings]
  (let [{body-1-id :id} (vp/p->map body-1 vj/Body)
        {body-2-id :id} (vp/p->map body-2 vj/Body)]
    (vf/event! w (vj/OnContactAdded
                  {:body-1 (vj/body phys body-1-id)
                   :body-2 (vj/body phys body-2-id)
                   :contact-manifold contact-manifold
                   #_ #_:contact-settings contact-settings}))))

(defn on-contact-persisted
  [w phys body-1 body-2 contact-manifold _contact-settings]
  (let [{body-1-id :id} (vp/p->map body-1 vj/Body)
        {body-2-id :id} (vp/p->map body-2 vj/Body)]
    (vf/event! w (vj/OnContactPersisted
                  {:body-1 (vj/body phys body-1-id)
                   :body-2 (vj/body phys body-2-id)
                   :contact-manifold contact-manifold
                   #_ #_:contact-settings contact-settings}))))

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
                         {:on-contact-added (fn [body-1 body-2 contact-manifold contact-settings]
                                              (#'on-contact-added w phys body-1 body-2 contact-manifold contact-settings))
                          :on-contact-persisted (fn [body-1 body-2 contact-manifold contact-settings]
                                                  (#'on-contact-persisted w phys body-1 body-2 contact-manifold contact-settings))
                          #_ #_:on-contact-validate (fn [_ _ _ _]
                                                      (jolt/JPC_VALIDATE_RESULT_ACCEPT_ALL_CONTACTS))
                          #_ #_:on-contact-removed (fn [_]
                                                     (println :REMOVED))})))
#_ (setup! w)

(defn body-path
  [body]
  (vg.s/body-path body))

(defn gen-cube
  "Returns a hash map with `:mesh` and `:material`.

  `idx` is used just to choose some color.
  "
  ([params]
   (vg.s/gen-cube params))
  ([params idx]
   (vg.s/gen-cube params idx)))

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
       (let [local (vm/matrix-transform pos rot scale)]
         (merge transform-local local)
         (merge transform-global (cond-> local
                                   transform-parent
                                   (vr.c/matrix-multiply transform-parent)))))

   ;; Observers.
   (vg.s/body-removed w)
   (vg.s/on-close w)

   ;; Systems.
   (vg.s/input-handler w)

   (vg.s/vybe-transform w)

   (vg.s/update-model-meshes w)
   (vg.s/update-physics w)
   (vg.s/update-physics-ongoing w)

   (vg.s/animation-loop w)
   (vg.s/animation-controller w)
   (vg.s/animation-node-player w)

   (vg.s/update-camera w)

   #_(vg.s/update-sound-sources w)])

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
  "Draw scene using all the available meshes.

    - `:colliders`, boolean to show colliders
    - `:scene`, scene name (e.g. `:vg.gltf.scene/Scene`)
    - `:entities`, if set, will only draw the entities NAMES in this set
    - `:entities-exclude`, inverse of `:entities`
    - `:use-color-ids`, set `colDiffuse` in shaders to the color identifiers"
  ([w]
   (draw-scene w {}))
  ([w {:keys [debug scene colliders entities entities-exclude use-color-ids]}]
   (vf/with-query w [transform-global [:meta {:flags #{:up}} [vt/Transform :global]]
                     material [:inout vr/Material], mesh vr/Mesh
                     vbo-joint [:maybe [vt/VBO :joint]], vbo-weight [:maybe [vt/VBO :weight]]
                     color [:maybe {:flags #{:up}} [vr/Color :color-identifier]]
                     _no-disabled [:not {:flags #{:up}}
                                   :vf/disabled]
                     _no-collider (if colliders
                                    [:maybe {:flags #{:up}} :vg/collider]
                                    [:not {:flags #{:up}} :vg/collider])
                     _ (if debug
                         :vg/debug
                         [:not :vg/debug])
                     _ (or scene :_)
                     _e :vf/entity
                     parent-e [:vf/entity [:maybe {:flags #{:up}} vt/EntityName]]]
     #_(when (= (vf/get-name (vf/parent e))
                '(vybe.flecs/path [:my/model :vg.gltf/Sphere]))
         (println :BBB (vm/matrix->translation transform-global)))
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

       (vr.c/rl-enable-shader (:id (:shader material)))
       (vr.c/rl-enable-vertex-array (:vaoId mesh))

       (vr.c/rl-enable-vertex-buffer (:id vbo-joint))
       (vr.c/rl-set-vertex-attribute 6 4 (raylib/RL_FLOAT) false 0 0)
       (vr.c/rl-enable-vertex-attribute 6)

       (vr.c/rl-enable-vertex-buffer (:id vbo-weight))
       (vr.c/rl-set-vertex-attribute 7 4 (raylib/RL_FLOAT) false 0 0)
       (vr.c/rl-enable-vertex-attribute 7))

     #_(vr.c/draw-mesh-instanced mesh material transform-global 1)
     (when (and (or (empty? entities)
                    (contains? entities (vf/get-name parent-e)))
                (or (empty? entities-exclude)
                    (not (contains? entities-exclude (vf/get-name parent-e)))))
       (if (and use-color-ids color)
         (let [diffuse (vr/material-get material (raylib/MATERIAL_MAP_DIFFUSE))
               original-shader (vp/clone (:shader material))
               original-color (vp/clone (get diffuse :color))]
           (try
             (assoc material :shader (get (::shader-diffuse w) vt/Shader))
             (assoc diffuse :color color)
             (vr.c/draw-mesh mesh material transform-global)
             (finally
               (assoc diffuse :color original-color)
               (assoc material :shader original-shader))))
         (vr.c/draw-mesh mesh material transform-global))))))

(defn draw-debug
  "Draw debug information (e.g. lights)."
  ([w]
   (draw-debug w {}))
  ([w {:keys [animation] :as params}]
   (vf/with-query w [transform-global [vt/Transform :global]
                     _ :vg/light]
     ;; TRS from a matrix https://stackoverflow.com/a/27660632
     (let [v (vm/matrix->translation transform-global)]
       (vr.c/draw-sphere v 0.2 (vr/Color [0 185 155 255]))
       (vr.c/draw-line-3-d v
                           (-> (vt/Vector3 [0 0 -40])
                               (vr.c/vector-3-transform transform-global))
                           (vr/Color [0 185 255 255]))))

   (draw-scene w (merge {:debug true} params))

   (when animation
     (vf/with-query w [_ :vg.anim/joint
                       transform-global [vt/Transform :global]
                       #_ #__joint-transform [vt/Transform :joint]]
       (let [v (vm/matrix->translation transform-global)]
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
  ([w]
   (draw-lights w {}))
  ([w {:keys [shader scene shader-params draw]
       :or {shader (get (::shader-shadowmap w) vt/Shader)
            draw draw-scene}}]
   (let [depth-rts (-get-depth-rts w)
         cull-near (vr.c/rl-get-cull-distance-near)
         cull-far (vr.c/rl-get-cull-distance-far)]
     (try
       ;; We set the cull box so we can have a sun-like light.
       #_(vr.c/rl-set-clip-planes -1000 1000)

       (vf/with-query w [material [:mut vr/Material]
                         _ (or scene :_)]
         (assoc material :shader shader))

       (.set ^MemorySegment (:locs shader)
             ValueLayout/JAVA_INT
             (* 4 (raylib/SHADER_LOC_VECTOR_VIEW))
             (int (vr.c/get-shader-location shader "viewPos")))

       (vg/set-uniform shader
                       (merge {:u_light_color (vr.c/color-normalize #_(vr/Color [10 40 50 255])
                                                                    (vr/Color [255 255 255 255]))
                               :u_ambient (vr.c/color-normalize (vr/Color [255 255 255 255])
                                                                #_(vr/Color [255 200 224 255]))
                               :shadowMapResolution (:width (:depth (first depth-rts)))}
                              shader-params))

       (if-let [[light-cams light-dirs] (->> (vf/with-query w [_ :vg/light,
                                                               mat [vt/Transform :global]
                                                               cam vt/Camera
                                                               _ (or scene :_)]
                                               [cam (-> (vt/Vector3 [0 0 -1])
                                                        (vr.c/vector-3-rotate-by-quaternion
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
                                       (draw w)
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

         (vg/set-uniform shader {:lightsCount 0}))

       (first depth-rts)

       (finally
         (vr.c/rl-set-clip-planes cull-near cull-far))))))

(vc/defn* matrix-view :- vt/Transform
  [vy-camera :- vt/Camera]
  (let [{:keys [camera rotation]} vy-camera
        quat (vr.c/quaternion-invert (vc/cast* rotation vt/Vector4))
        {:keys [x y z]} (:position camera)]
    (vr.c/matrix-multiply (vr.c/matrix-translate (- x) (- y) (- z))
                          (vr.c/matrix-rotate
                           (vr.c/vy-quaternion-to-axis-vector quat)
                           (vr.c/vy-quaternion-to-axis-angle quat)))))

(defn draw-billboard
  "Draw billboard (a texture that always faces the camera)."
  ([camera-ent texture position]
   (draw-billboard camera-ent texture position {}))
  ([camera-ent {:keys [width height] :as texture} position {:keys [scale]
                                                            :or {scale 8}}]
   (let [vy-camera (get camera-ent vt/Camera)
         source (vr/Rectangle [0 0 width height])
         size (vt/Vector2 [scale scale])]
     (vr.c/draw-billboard-pro (:camera vy-camera)
                              texture
                              source
                              position
                              (let [{:keys [m1 m5 m9]} (matrix-view vy-camera)]
                                (vt/Vector3 [m1 m5 m9]))
                              size
                              (vr.c/vector-2-zero)
                              0.0
                              (vr/Color [255 255 255 255])))))

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

    (vf/rest-enable! w))

  w)

(defn phys
  "Get the physics object."
  [w]
  (get-in w [(vg/root) vj/PhysicsSystem]))

(defn physics-update!
  "Update physics."
  [w delta-time]
  (vf/with-deferred w
    (vj/update! (phys w) delta-time)))

(defmacro key-down?
  "Receives a keyword (e.g. `:k`) or some key like `raylib/KEY_K`."
  [k]
  `(vr.c/is-key-down ~(if (keyword? k)
                        (list (symbol (str `raylib) (str "KEY_" (str/upper-case (name k)))))
                        k)))

(defmacro key-pressed?
  "Receives a keyword (e.g. `:k`) or some key like `raylib/KEY_K`."
  [k]
  `(vr.c/is-key-pressed ~(if (keyword? k)
                           (list (symbol (str `raylib) (str "KEY_" (str/upper-case (name k)))))
                           k)))

(defn get-delta-time
  []
  (vr.c/get-frame-time))

(def ^:private unit-z
  (vt/Vector3 [0 0 -1]))

(def ^:private unit-y
  (vt/Vector3 [0 1 0]))

(def ^:private unit-x
  (vt/Vector3 [1 0 0]))

(vp/defcomp Euler
  [[:yaw :float]
   [:pitch :float]
   [:roll :float]
   [:quaternion_initial vt/Rotation]])

(defn camera-move!
  "Update curent active camera with mouse (for rotation) + keyboard (for translation).

  Use the WASD keys."
  ([w]
   (camera-move! w {}))
  ([w {:keys [sensitivity rot-sensitivity rot-pitch-limit entity-tag mouse-continuous]
       :or {sensitivity 0.5
            rot-sensitivity 1.0
            rot-pitch-limit (/ Math/PI 4.0)
            entity-tag :vg/camera-active
            mouse-continuous true}}]
   (vf/with-query w [_ entity-tag
                     translation [:mut vt/Translation]
                     rotation [:mut vt/Rotation]
                     vel vt/Velocity
                     transform vt/Transform
                     {:keys [quaternion_initial] :as euler} [:inout [:maybe Euler]]
                     {:keys [width height]} [:src :vg/root vt/ScreenSize]
                     e :vf/entity]
     (cond
       (not (vr.c/is-window-focused))
       false

       ;; Initialize.
       (not euler)
       (merge w {e [(Euler {:quaternion_initial rotation})]})

       ;; Hack so we don't have artifacts during the initialization.
       (and (not (:vg.internal/camera-move! w))
            (= vel (vt/Velocity)))
       (merge w {:vg.internal/camera-move! []})

       (:vg.internal/camera-move! w)
       (let [delta-time (get-delta-time)
             *move-forward (delay
                             (fn [pos v]
                               (vr.c/vector-3-add pos
                                                  (-> (vr.c/vector-3-transform unit-z transform)
                                                      (vr.c/vector-3-subtract translation)
                                                      (vr.c/vector-3-scale v)))))
             *move-right (delay
                           (fn [pos v]
                             (vr.c/vector-3-add pos
                                                (-> (vr.c/vector-3-transform unit-z transform)
                                                    (vr.c/vector-3-subtract translation)
                                                    (vr.c/vector-3-scale v)
                                                    (vr.c/vector-3-cross-product unit-y)))))
             limit 10
             decrease 0.8
             c 130
             v0 (vt/Velocity [(let [vv (* (:x vel) decrease)]
                                (cond
                                  (< (abs vv) 0.001) 0.0
                                  (> vv limit) limit
                                  (< vv (- limit)) (- limit)
                                  :else vv))
                              0
                              (let [vv (* (:z vel) decrease)]
                                (cond
                                  (< (abs vv) 0.001) 0.0
                                  (> vv limit) limit
                                  (< vv (- limit)) (- limit)
                                  :else vv))])
             new-translation (cond-> (vt/Translation [0 0 0])
                               (key-down? :w) (@*move-forward (* c sensitivity))
                               (key-down? :s) (@*move-forward (* (- c) sensitivity))
                               (key-down? :d) (@*move-right (* c sensitivity))
                               (key-down? :a) (@*move-right (* (- c) sensitivity)))
             {mouse-x :x mouse-y :y} (vr.c/get-mouse-position)]

         #_(when (not= vel (vt/Velocity))
             (println "============================")
             (println ((juxt :x :y :z) vel))
             (println ((juxt :x :y :z) new-translation)))

         (merge translation (-> new-translation
                                ;; FIXME Maybe it's better to use the up vector
                                ;; instead of hardcoding it to `y`?
                                (assoc :y 0)
                                ;; Acceleration.
                                (vr.c/vector-3-scale (* (/ (* delta-time delta-time)
                                                           2)
                                                        (if (and (realized? *move-forward)
                                                                 (realized? *move-right))
                                                          ;; Compensate for diagonal moves so
                                                          ;; we have the same speed.
                                                          (/ 1 (Math/pow 2 0.5))
                                                          1)))
                                ;; Initial velocity (v0).
                                (vr.c/vector-3-add (vr.c/vector-3-scale v0 delta-time))
                                ;; Initial position (x0).
                                (vr.c/vector-3-add translation)))

         (when (and (< 0 mouse-x width)
                    (< 0 mouse-y height))
           (let [{delta-x :x delta-y :y} (vr.c/get-mouse-delta)]
             (when mouse-continuous
               (when (< mouse-x 30)
                 (vr.c/set-mouse-position (- width 50) mouse-y))
               (when (> mouse-x (- width 30))
                 (vr.c/set-mouse-position 50 mouse-y))
               (when (< mouse-y 30)
                 (vr.c/set-mouse-position mouse-x (- height 50)))
               (when (> mouse-y (- height 30))
                 (vr.c/set-mouse-position mouse-x 50)))

             ;; To avoid big jumps.
             (when (or (< 0 (abs delta-x) 60)
                       (< 0 (abs delta-y) 60))
               (let [axis (vt/Vector3)
                     angle (vp/float* 0)
                     _ (vr.c/quaternion-to-axis-angle rotation axis angle)]

                 ;; Update euler rot.
                 (-> euler
                     (update :yaw + (* delta-x
                                       (* -2.0 sensitivity rot-sensitivity)
                                       delta-time))
                     (update :pitch (fn [v]
                                      (let [v (+ v (* delta-y
                                                      (* -0.6 sensitivity rot-sensitivity)
                                                      delta-time))]
                                        (cond
                                          (< v (- rot-pitch-limit))
                                          (- rot-pitch-limit)

                                          (> v rot-pitch-limit)
                                          rot-pitch-limit

                                          :else
                                          v)))))

                 (merge rotation
                        (-> quaternion_initial
                            (vr.c/quaternion-multiply
                             (vr.c/quaternion-normalize
                              (-> (vr.c/quaternion-from-axis-angle (vt/Vector3 [0 1 0])
                                                                   (:yaw euler))
                                  (vr.c/quaternion-multiply
                                   (vr.c/quaternion-from-axis-angle (vt/Vector3 [1 0 0])
                                                                    (:pitch euler))))))
                            vr.c/quaternion-normalize))))))

         ;; Unroll.
         #_(let [{:keys [x]} (vr.c/quaternion-to-euler rotation)
                 #_ #_ #_ #_ #_ #_ #_ #_axis (vt/Vector3)
                 angle (vp/float* 0)
                 _ (vr.c/quaternion-to-axis-angle rotation axis angle)
                 pitch-yaw-axis (vr.c/vector-3-normalize (vt/Vector3 [(:x axis) (:y axis) 0]))]
             (println :ROLL x
                      "\n  " rotation)

             (cond
               #_(and (> (abs x) 0.1)
                      #_(not (< 3.1415 (abs x) 3.2))
                      (not (> (- Math/PI 0.1) (abs x))))
               true
               (let [c 100.0
                     roll (if (> (abs x) (/ Math/PI 2))
                            (if (neg? x)
                              (/ (+ (/ Math/PI 1) x)
                                 c)
                              (/ (- (+ (/ Math/PI 1) x))
                                 c))
                            (/ (- x) c))]
                 (merge rotation (-> rotation
                                     (vr.c/quaternion-multiply
                                      (#_ vr.c/quaternion-normalize identity
                                          (vr.c/quaternion-from-euler 0 0 roll)))
                                     vr.c/quaternion-normalize))))))))))

(defn body->entity
  "Get Flecs entity from Jolt body."
  [w body]
  (w (get-in w [(vg/body-path body) vt/Eid :id])))

(defn render-texture
  "Create and load a render texture."
  [w game-id width height]
  (merge w {game-id [(vr/RenderTexture2D (vr.c/load-render-texture width height))]}))

(defn try-requiring-flow-storm!
  "Check if we have the flow storm debugger available as a dependency
  and require it if we do so we have its side-effects available."
  []
  (try (requiring-resolve 'vybe.debug.flow-storm/start-debugger)
       true
       (catch Exception _ false)))

(defn start!
  "Start game.

  `w` is `world` from `vybe.flecs/make-world`.

  `draw-var` receives `w` and `delta-time` as its arguments, the function
  will be wrapped with `vp/with-arena` so we don't have memory leaks.

  `init-fn` receives `w` as its argument.
  Don't use functions that creates new threads in `init-fn` (e.g. `pmap`).

  `screen-loader` is a function that will be called just after we initialize the
  graphics, don't assume we have the Flecs `w` ready!"
  ([w screen-width screen-height draw-var init-fn]
   (start! w {:screen-size [screen-width screen-height]
              :draw-var draw-var
              :init-fn init-fn}))
  ([w {:keys [fps window-name window-position screen-loader
              screen-size draw-var init-fn full-screen]
       :or {fps 60
            window-name "Untitled Game"
            window-position [1120 200]
            full-screen false}}]
   (when-not (var? draw-var)
     (throw (ex-info "`draw-var` should be a var" {})))

   ;; Init raylib.
   (when-not (vr.c/is-window-ready)
     (vr.c/set-config-flags (raylib/FLAG_MSAA_4X_HINT))
     (if screen-size
       (vr.c/init-window (first screen-size) (second screen-size) window-name)
       (vr.c/init-window 0 0 window-name))
     (vr.c/set-window-state (raylib/FLAG_WINDOW_UNFOCUSED))
     (vr.c/set-target-fps fps)
     (vr.c/set-window-position (first window-position)
                               (second window-position))
     (when full-screen
       (vr.c/toggle-borderless-windowed))

     (when screen-loader
       (screen-loader)))

   ;; Setup world.
   (setup! w)
   (merge w {:vg/root [(vt/ScreenSize [(vr.c/get-screen-width) (vr.c/get-screen-height)])]})

   ;; Default shaders.
   (let [resolution (vt/Vector2 [(vr.c/get-screen-width) (vr.c/get-screen-height)])]
     (-> w
         (vg/shader-program ::shader-default)
         (vg/shader-program ::shader-shadowmap "shaders/shadowmap.vs" "shaders/shadowmap.fs")
         (vg/shader-program ::shader-dither {:frag "shaders/dither.fs"
                                             :uniforms {:u_resolution resolution}})
         (vg/shader-program ::shader-diffuse "shaders/diffuse.fs")
         (vg/shader-program ::shader-solid "solid.fs")
         (vg/shader-program ::shader-noise-blur "shaders/noise_blur_2d.fs")
         (vg/shader-program ::shader-edge-2d {:frag "shaders/edge_2d.fs"
                                              :uniforms {:u_resolution resolution}})
         (vg/shader-program ::shader-mixer "shaders/mixer.fs")

         ;; Render texture.
         (vg/render-texture ::render-texture (vr.c/get-screen-width) (vr.c/get-screen-height))
         (vg/render-texture ::rt-1-by-1 1 1)))

   ;; Setup C systems.
   #_(do (vf/eid w vt/Translation)
         (vf/eid w vt/Rotation)
         (vf/eid w vt/Scale)
         (vf/eid w vt/Transform)
         (vf/eid w :global)
         (vf.c/vybe-default-systems-c w))

   ;; Setup default systems.
   (default-systems w)

   ;; `vr/t` is used so we run the command in the main thread.
   (vr/t (init-fn w))
   (alter-var-root #'vr/draw
                   (constantly
                    (fn []
                      (vp/with-arena _
                        (run-commands!)
                        (draw-var w (vr.c/get-frame-time))))))))
