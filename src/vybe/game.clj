(ns vybe.game
  "Namespace for game stuff."
  (:require
   [clojure.walk :as walk]
   [nextjournal.beholder :as beholder]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure2d.color :as c]
   [clojure.set :as set]
   [potemkin :refer [def-map-type]]
   [vybe.protocol :as proto]
   [clojure.math.combinatorics :as combo]
   [vybe.component :as vy.c]
   [vy.visibility :as-alias vi]
   [clj-java-decompiler.core :refer [decompile disassemble]]
   [vybe.api :as vy]
   [vybe.util :as vy.u]
   [jsonista.core :as json]
   [vybe.raylib :as vr])
  (:import
   (java.util Iterator)
   (clojure.lang IAtom2)
   (org.raylib Font Texture2D raylib_h Rectangle)
   (java.lang.foreign MemorySegment ValueLayout))
  #_(:import
     (com.badlogic.gdx.graphics.glutils ShaderProgram FrameBuffer)
     (com.badlogic.gdx Game Gdx Graphics Screen InputAdapter)
     (com.badlogic.gdx.graphics.g2d.freetype FreeTypeFontGenerator FreeTypeFontGenerator$FreeTypeFontParameter)
     (com.badlogic.gdx.graphics Color GL20 OrthographicCamera Texture Mesh VertexAttribute VertexAttributes
                                Cursor$SystemCursor
                                VertexAttributes VertexAttribute VertexAttributes$Usage
                                Pixmap$Format)
     (com.badlogic.gdx.graphics.g2d BitmapFont GlyphLayout CustomSpriteBatch Batch TextureRegion Animation)
     (java.util Iterator)
     (clojure.lang IAtom2)))

(set! *warn-on-reflection* true)
#_(set! *unchecked-math* :warn-on-boxed)

;; ------------- Utils ---------------

(defonce *resources (atom {}))

(defmacro with-managed
  [game-id resource-path builder opts & body]
  `(let [resource# (do ~@body)
         canonical-path# (when ~resource-path
                           (.getPath (io/resource ~resource-path)))]
     (swap! *resources update-in [~game-id (or canonical-path#
                                               (gensym))]
            conj (merge {:resource-path canonical-path#
                         :resource resource#
                         :builder ~builder}
                        ~opts))
     resource#))

;; Used for `env`, this acts as a persistent (immutable) map if you only
;; use the usual persistent functions (`get`, `assoc`, `update` etc), while
;; it will change the underlying atom if you use `IAtom` (and `IAtom2`) functions
;; like `swap!`, `reset!` etc.
(def-map-type MutableMap [^IAtom2 *m ^IAtom2 *temp-m]
  (get [_ k default-value]
       (if (contains? @*temp-m k)
         (get @*temp-m k default-value)
         (get @*m k default-value)))
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
  []
  (->MutableMap (atom {}) (atom {})))

(comment

  (let [a (make-env)]
    (swap! a assoc :a 4)
    [(assoc a :a 5 :b 55)
     (keys (assoc a :a 5 :b 55))
     (keys a)
     a
     (:a a)])

  ())

;; -------------- COLOR -----------------

(defn ->color
  "Converts a color from Clojure to a map (:r :g :b :a)."
  [color]
  (zipmap [:r :g :b :a] (c/scale-down color true)))

(defn set-color
  [env color]
  (let [color (or color (->color :white))]
    (assoc env ::color color)))

(defn- adapt-color
  [color]
  (->> ((juxt :r :g :b :a) color)
        (mapv #(* % 255))
        vr/color))

;; ------------- SHAPE ----------------------

(defn rect
  "Draw a rectangle."
  [{::keys [_screen-size color] :as env}
   {:keys [x y]}
   {:keys [width height]}]
  (let [#_ #_ [_screen-width _screen-height] screen-size
        color (or color (->color :white))]
    (vr/draw-rectangle! x y
                        width height
                        (adapt-color color)))
  env)

(defn rect-lines
  "Draw rectangle lines."
  [{::keys [_screen-size color] :as env}
   {:keys [x y]}
   {:keys [width height]}
   line-thick]
  (let [#_ #_ [_screen-width _screen-height] screen-size
        color (or color (->color :white))]
    (vr/draw-rectangle-lines-ex! (vr/rectangle x y width height)
                                 line-thick
                                 (adapt-color color)))
  env)

;; --------------- FONT ------------------

(defn font
  "Managed. Generate a font with some params (derived from a TTF file in the classpath)."
  [game-id resource-path {:keys [size space-y]
                          :or {size 12
                               space-y 48}
                          :as params}]
  (with-managed game-id resource-path #(font game-id resource-path params) {}
    (vr/set-text-line-spacing! space-y)
    (vr/load-font-ex (.getPath (io/resource resource-path))
                     size MemorySegment/NULL 0)))

(defn text
  "Writes some text to the screen."
  [{::keys [#_screen-size ^Font font color font-scale]
    :or {font-scale 1.0}
    :as env}
   ^String text
   position]
  (let [[x y] position
        #_ #_ [_screen-width screen-height] screen-size
        color (or color (->color :white))]
    (vr/draw-text-ex! font
                      text
                      (vr/vec2 [x y])
                      (* (Font/baseSize font)
                         (float font-scale))
                      (/ (Font/baseSize font)
                         30.0)
                      (adapt-color color)))
  env)

;; --------------- HELPER --------------

(defn fps
  "Return current frames per second."
  []
  (vr/get-fps))

(defn clear-color
  "Clear color (clear background)."
  [color]
  (vr/clear-background! (adapt-color color)))

;; --------------- SHADER --------------

(defn builtin-path
  "Build the path for a built-in resource."
  [res-path]
  (str "com/pfeodrippe/vybe/" res-path))

(defn- pre-process-shader
  [shader-res-path]
  (let [file (or (io/file (io/resource shader-res-path))
                 (io/file (io/resource (builtin-path shader-res-path)))
                 (io/file shader-res-path))
        folder-name (.getParent file)]
    (->> (slurp file)
         str/split-lines
         (mapv (fn [line]
                 (if-let [dep-relative-path (-> (re-matches #"#include \"(.*)\"" line)
                                                last)]
                   (pre-process-shader (-> (or (io/file (io/resource dep-relative-path))
                                               (io/file (io/resource (builtin-path (str "shaders/" dep-relative-path))))
                                               (io/file folder-name dep-relative-path))
                                           .toPath
                                           ;; Normalize so we get rid of any `../`
                                           .normalize
                                           str))
                   line)))
         (str/join "\n"))))
#_(pre-process-shader "shaders/main.fs")

(defonce *shaders-cache (atom {}))

(defn shader-program
  "Create a shader-program."
  ([]
   (shader-program (builtin-path "shaders/default.vs") (builtin-path "shaders/default.fs")))
  ([frag-res-path]
   (shader-program (builtin-path "shaders/default.vs") frag-res-path))
  ([vertex-res-path frag-res-path]
   ;; This first should be the one used in PRD.
   #_(or (get @*shaders-cache [vertex-res-path frag-res-path])
         (let [vertex-shader-str (pre-process-shader vertex-res-path)
               frag-shader-str (pre-process-shader frag-res-path)
               shader (ShaderProgram. ^String vertex-shader-str ^String frag-shader-str)]
           (when-not (.isCompiled shader)
             (throw (ex-info "Error when compiling shader" {:vertex-res-path vertex-res-path
                                                            :frag-res-path frag-res-path
                                                            :log (.getLog shader)})))
           (swap! *shaders-cache assoc [vertex-res-path frag-res-path] shader)
           shader))
   (let [vertex-shader-str (pre-process-shader vertex-res-path)
         frag-shader-str (pre-process-shader frag-res-path)]
     (or (get @*shaders-cache [vertex-shader-str frag-shader-str])
         (let [shader (vr/load-shader-from-memory vertex-shader-str frag-shader-str)]
           (when (< (org.raylib.Shader/id shader) 4)
             (throw (ex-info "Error when compiling shader" {:vertex-res-path vertex-res-path
                                                            :frag-res-path frag-res-path
                                                            :shader-id (org.raylib.Shader/id shader)
                                                            #_ #_:log (.getLog shader)})))
           (swap! *shaders-cache assoc [vertex-shader-str frag-shader-str] shader)
           shader)))))
#_(shader-program)
#_(shader-program "shaders/main.fs")
#_(shader-program "shaders/cursor.fs")
#_(shader-program "shaders/light.fs")
#_(shader-program "shaders/shadowmap.vs" "shaders/shadowmap.fs")
#_ (-> (shader-program "shaders/rect.fs")
       (vr/get-shader-location-attrib "vertexColosr"))

(comment

  (vr/rl-load-vertex-array)

  ())

(defn -adapt-shader
  [shader]
  (if (instance? java.lang.foreign.MemorySegment shader)
    shader
    (shader-program (or (::shader.vert shader)
                        (builtin-path "shaders/default.vs"))
                    (or (::shader.frag shader)
                        (builtin-path "shaders/default.fs")))))

#_(-adapt-shader "shaders/cursor.fs")

(defn set-uniform
  [env shader uniform value]
  (let [sp (-adapt-shader shader)]
    (if (vector? value)
      (vr/set-shader-value! sp (vr/get-shader-location sp (name uniform))
                            (vr/vec2 value)
                            (raylib_h/SHADER_UNIFORM_VEC2))
      (vr/set-shader-value! sp (vr/get-shader-location sp (name uniform))
                            (vr/float* value)
                            (raylib_h/SHADER_UNIFORM_FLOAT))))
  env)

(defonce ^:dynamic *custom-attrs* (atom {}))

(defmacro with-shader
  [shader & body]
  `(try
     (vr/begin-shader-mode! (-adapt-shader ~shader))
     ~@body
     (finally
       (vr/end-shader-mode!))))

(defn set-custom
  "Set custom vertex atribute.

  E.g. `(set-custom :a_stroke 1)"
  [env attr v]
  (swap! *custom-attrs* assoc attr v)
  env)

;; ----------------------- TEXTURE/ANIMATION ---------------------

(defn texture
  "Managed, returns a texture."
  (^Texture2D [game-id resource-path]
   (texture game-id resource-path {}))
  (^Texture2D [game-id resource-path {:keys [managed-opts]}]
   (with-managed game-id resource-path #(texture game-id resource-path managed-opts) managed-opts
     (vr/load-texture (.getPath (io/resource resource-path))))))

;; FIXME Animation
#_(defn animation
    "Create a libgdx animation, texture created from `resource-path` will be
  managed.

  `json-path` should be in the aseprite format (array, not hash!).

  E.g. of the required keys (JSON parsed to EDN format)
  {:frames [{:frame {:x 10 :y 10 :w 30 :h 40}}]}"
    [game-id resource-path json-path]
    (with-managed game-id resource-path #(animation game-id resource-path json-path) {:dispose (constantly nil)}
      (let [tex (texture game-id resource-path {:managed-opts {:builder (constantly nil)}})
            {:keys [frames]} (vy.u/parse-json (io/resource json-path))
            ^{:tag "[Ljava.lang.Object;"} frame-arr (->> frames
                                                         (mapv (fn [{:keys [frame]}]
                                                                 (let [{:keys [x y w h]} frame]
                                                                   (TextureRegion. tex
                                                                                   ^int x
                                                                                   ^int y
                                                                                   ^int w
                                                                                   ^int h))))
                                                         (into-array Object))]
        (Animation. ^float (float 0.1) frame-arr))))

#_(defn draw-animation
  [{::keys [screen-size total-time ^Batch batch] :as env}
   texture-region
   {:keys [x y]}
   {:keys [width height]}]
  (let [[_screen-width screen-height] screen-size
        frame-tex ^TextureRegion (.getKeyFrame ^Animation texture-region total-time true)]
    (doto batch
      (.draw frame-tex
             (float x) (float (- ^long screen-height (float y)))
             (float width) (float height))))
  env)

;; ----------------------- FBO -------------------------

;; FIXME FBO
#_(defn fbo
  "Create a FBO."
  [game-id]
  (with-managed game-id nil #(fbo game-id) {}
    (let [graphics ^com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics (Gdx/graphics)]
      (FrameBuffer. Pixmap$Format/RGBA8888
                    (.getBackBufferWidth graphics)
                    (.getBackBufferHeight graphics)
                    false))))

;; TODO Fix the FBO parameters (it's using an specific shader, but this should
;; come from config)
#_(defn draw-fbo
  "Draw frame buffer object."
  ([env position size]
   (draw-fbo env position size {}))
  ([{::keys [^FrameBuffer fbo ^Batch fbo-batch screen-size] :as env} position size
    {:keys [shader]}]
   (let [[x y] position
         [width height] size
         [_screen-width screen-height] screen-size
         draw (fn []
                (.draw fbo-batch
                       ^Texture (.getColorBufferTexture fbo)
                       (float x) (float (- screen-height y))
                       (float width) (float (- height))))]
     (if (.isDrawing fbo-batch)
       (draw)
       (with-batch (cond-> (assoc env ::batch fbo-batch)
                     shader
                     (-> (set-shader shader)
                         (set-uniform {::shader.frag "shaders/grain.fs"} "u_time" (* (::total-time env) 0.1))))
         (draw))))))

(defmacro with-fbo
  "Bind FBO at `::vg/fbo`."
  [env & body]
  `(try
     (.begin ^FrameBuffer (::fbo ~env))
     (doto ^GL20 (gl)
       (.glClear GL20/GL_COLOR_BUFFER_BIT)
       (.glEnable GL20/GL_TEXTURE_2D))
     ~@body
     (finally
       (.end ^FrameBuffer (::fbo ~env)))))

(defmacro with-fx
  "Apply a special effect (shader) to the entire screen.

  E.g.

  (with-fx env {::vg/shader.fs \"...\"}
    ...
    ...)"
  [env shader & body]
  `(do
     (with-fbo ~env
       ~@body)

     (let [[width# height#] (::screen-size ~env)]
       (draw-fbo ~env [0 0] [width# height#] {:shader ~shader}))))

;; ----------------------- HELPER -------------------------

(defn window-pos
  "Set window position."
  [x y]
  (vr/set-window-position! x y))

(defn close-app
  []
  (vr/close-window!))

(defn dispose-resources
  "Dispose resources for a game."
  ([game-id]
   (dispose-resources game-id :all))
  ([game-id resources-paths]
   (->> (get @*resources game-id)
        (filter (if (= resources-paths :all)
                  (constantly true)
                  (comp (set resources-paths) key)))
        (mapv (fn [[id coll]]
                (mapv (fn [{:keys [resource dispose] :as res-params}]
                        #_(println "Disposing" resource "for" id)
                        (try
                          (if dispose
                            (dispose)
                            ;; FIXME
                            #_(.dispose ^com.badlogic.gdx.utils.Disposable resource))
                          (swap! *resources update-in [game-id id] #(remove #{res-params} %))
                          (catch Exception e
                            (println e))))
                      coll))))
   (when (= resources-paths :all)
     (swap! *resources dissoc game-id))))

(defn recreate-resources
  "Recreate (possibly) modified resources for a game."
  [env game-id resources-paths]
  #_ (def game-id (ffirst @*resources))
  #_(println "Recreating resources" {:resources-paths resources-paths})
  (let [resources (get @*resources game-id)
        resources-group (->> resources
                             (filter (comp (set resources-paths) key))
                             vals
                             (apply concat)
                             (mapv (juxt :resource identity))
                             (into {}))
        reset (fn []
                (reset! env (walk/prewalk (fn [v]
                                            (if-let [r-params (get resources-group v)]
                                              ((:builder r-params))
                                              v))
                                          env)))]
    (dispose-resources game-id resources-paths)
    (try
      (reset)
      (catch Exception _
        ;; Try again in case some resource is in an invalid format.
        (reset)))))

;; ------------------------- SYSTEMS ------------------------

(defn dev-resources-watcher
  "To be used with `dev-system`.

  Initiates resources watcher, default path is `resources`."
  ([world]
   (dev-resources-watcher world {}))
  ([world {:keys [path]
           :or {path "resources"}}]
   (future
     (beholder/watch
      (fn [{:keys [type path]}]
        (try
          (when (contains? #{:create :modify :overflow} type)
            (vy/add-c world (vy.c/ResourceChanged {:path (str path)})))
          (catch Exception e
            (println e))))
      path))))

(defn dev-system
  "This system runs on Flec's OnLoad phase and checks if any resources or vars
  needs to be reloaded.

  It should only be used in development so you can have propery hot reload!"
  {:vy/query [vy.c/ResourceChanged]
   :vy/phase :vy.b/EcsOnLoad}
  [{::keys [game] :as env} iter]
  (vy/with-changed iter
    (vy/with-each iter [res vy.c/ResourceChanged
                        entity :vy/entity]
      (let [path (vy/pget res :path)
            world (vy/iter-world iter)]
        (recreate-resources env game #{path})
        (vy/delete world entity)))))
