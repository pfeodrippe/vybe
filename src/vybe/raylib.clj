(ns vybe.raylib
  "Raylib stuff.

  Java bindings
  https://github.com/electronstudio/jaylib

  Clojure bindings
  https://github.com/kiranshila/raylib-clj

  Cheatsheet
  https://www.raylib.com/cheatsheet/cheatsheet.html"
  (:require
   #_[vybe.nrepl]
   [vybe.raylib :as vr]
   [vybe.raylib.c :as vr.c]
   [vybe.panama :as vp]
   [vybe.type :as vt]
   [clojure.string :as str]))

(def ^:private raylib-constants
  {:CAMERA_ORTHOGRAPHIC 1
   :FLAG_MSAA_4X_HINT 32
   :FLAG_WINDOW_UNFOCUSED 2048
   :MATERIAL_MAP_ALBEDO 0
   :MATERIAL_MAP_DIFFUSE 0
   :MOUSE_BUTTON_LEFT 0
   :RL_ATTACHMENT_DEPTH 100
   :RL_ATTACHMENT_TEXTURE2D 100
   :RL_FLOAT 5126
   :RL_PIXELFORMAT_UNCOMPRESSED_R8G8B8A8 7
   :SHADER_LOC_VECTOR_VIEW 11
   :SHADER_UNIFORM_FLOAT 0
   :SHADER_UNIFORM_INT 4
   :SHADER_UNIFORM_VEC2 1
   :SHADER_UNIFORM_VEC3 2
   :SHADER_UNIFORM_VEC4 3})

(defn raylib-constant
  [constant]
  (or (get raylib-constants constant)
      (when-let [[_ key-name] (re-matches #"KEY_([A-Z])" (name constant))]
        (int (first key-name)))
      (throw (ex-info "Raylib constant is not available"
                      {:constant constant}))))

(defn raylib-call
  [_method-name]
  nil)

;; -- Raylib types
(vp/defcomp Texture [[:id :int]
                     [:width :int]
                     [:height :int]
                     [:mipmaps :int]
                     [:format :int]])
(vp/defcomp RenderTexture2D [[:id :int]
                             [:texture Texture]
                             [:depth Texture]])
(vp/defcomp Matrix vt/Matrix)
(vp/defcomp Vector2 vt/Vector2)
(vp/defcomp Vector3 vt/Vector3)
(vp/defcomp Camera [[:position vt/Vector3]
                    [:target vt/Vector3]
                    [:up vt/Vector3]
                    [:fovy :float]
                    [:projection :int]])
(vp/defcomp Camera2D [[:offset vt/Vector2]
                      [:target vt/Vector2]
                      [:rotation :float]
                      [:zoom :float]])
(vp/defcomp Rectangle [[:x :float]
                       [:y :float]
                       [:width :float]
                       [:height :float]])
(vp/defcomp Shader [[:id :int]])
(vp/defcomp Mesh [[:vertexCount :int]
                  [:triangleCount :int]
                  [:vaoId :int]])
(vp/defcomp MaterialMap [[:texture Texture]
                         [:color :int]
                         [:value :float]])
(vp/defcomp Material [[:shader Shader]
                      [:_padding :int]
                      [:maps :pointer]])
(vp/defcomp Model [[:transform vt/Transform]
                   [:meshCount :int]
                   [:materialCount :int]
                   [:boneCount :int]
                   [:_padding :int]
                   [:meshes :pointer]
                   [:materials :pointer]
                   [:meshMaterial :pointer]])
(vp/defcomp VyModelMeta [])
(vp/defcomp VyModel [[:model Model]
                     [:metaCount :int]
                     [:_padding :int]
                     [:meta :pointer]])
(vp/defcomp Image [[:data :pointer]
                   [:width :int]
                   [:height :int]
                   [:mipmaps :int]
                   [:format :int]])

(def ^:private char->value
  (->> (mapv (fn [n]
               [(first (str n)) n])
             (range 10))
       (concat (mapv (fn [c n]
                       [c n])
                     [\a \b \c \d \e \f]
                     (range 10 16)))
       (concat (mapv (fn [c n]
                       [c n])
                     [\A \B \C \D \E \F]
                     (range 10 16)))
       (into {})))

(def ^:private color-opts
  {:constructor (fn [v]
                  (if (string? v)
                    (let [[r g b a] (->> (last (str/split v #"#"))
                                         (partition-all 2 2)
                                         (mapv #(let [[h l] (mapv char->value %)]
                                                  (+ (* h 16) l))))]
                      [r g b (or a 255)])
                    v))})

(vp/defcomp Color color-opts
  [[:r :byte] [:g :byte] [:b :byte] [:a :byte]])

(defmacro t
  "Runs command (delayed) in the main thread.

  Useful for REPL testing as it will block and return
  the result from the command."
  [& body]
  `(do ~@body))

(defonce ^:private *state
  (atom {:buf-general []
         :buf1 []
         :buf2 []
         :front-buf? true}))

(defn- impl-state
  []
  *state)

;; -- Custom VY types.
(defn vy-model
  [model]
  (let [mesh-count (:meshCount model)
        meta-arr (vp/arr mesh-count VyModelMeta)]
    #_(assoc-in meta-arr [3 :drawingDisabled] 1)
    (VyModel {:model model, :metaCount mesh-count, :meta meta-arr})))

;; -- Helpers.
(defn material-get
  "For `prop` options, use `raylib-constant` with `:MATERIAL_MAP...`,
  e.g.

  `(raylib-constant :MATERIAL_MAP_DIFFUSE)`"
  [material prop]
  (let [maps (-> material :maps)]
    (when-not (vp/null? maps)
      (-> maps
          (vp/arr 99 vr/MaterialMap)
          (nth prop)))))

;; ------- Misc
(defn- run-buf-general-cmds
  []
  (let [state* (impl-state)
        {:keys [buf-general]} @state*]
    (locking state*
      (try
        (run! (fn [{:keys [cmd prom]}]
                (try
                  (let [res (cmd)]
                    (some-> prom (deliver res))
                    res)
                  (catch Exception e
                    (some-> prom (deliver {:error e}))
                    (println e)
                    (vr.c/draw-text "!! ERROR !!" 200 200 20
                                    (Color [255 0 0 255])))))
              buf-general)
        (finally
          (swap! state* (fn [state]
                          (-> state
                              (assoc :buf-general [])))))))))

(defn- run-buf-1-2-cmds
  []
  (let [state* (impl-state)
        {:keys [buf1 buf2 front-buf?]} @state*
        buf (if front-buf? buf1 buf2)]
    (locking state*
      (try
        (run! (fn [{:keys [cmd prom]}]
                (try
                  (let [res (cmd)]
                    (some-> prom (deliver res))
                    res)
                  (catch Exception e
                    (some-> prom (deliver {:error e}))
                    (println {:form (:form (meta cmd))
                              :exception e})
                    (vr.c/draw-text "!! ERROR !!" 200 200 20
                                    (Color [255 0 0 255])))))
              (concat buf))
        (finally
          (swap! state* (fn [state]
                          (-> state
                              (assoc (if front-buf? :buf1 :buf2) [])
                              (update :front-buf? not)))))))))

(defonce draw (fn []))
(defonce original-draw @#'draw)

(defn -main-loop
  []
  (run-buf-general-cmds)
  (when (vr.c/is-window-ready)
    ;; TODO Let the user control begin/end of drawing for `draw`.
    (try
      (draw)
      (catch Exception e
        (println e)))
    (when (or (= draw original-draw)
              (seq (:buf1 @(impl-state)))
              (seq (:buf2 @(impl-state))))
      (raylib-call :BeginDrawing)
      (run-buf-1-2-cmds)
      (raylib-call :EndDrawing)

      (raylib-call :BeginDrawing)
      (run-buf-1-2-cmds)
      (raylib-call :EndDrawing))))

(defn start-nrepl!
  []
  (let [port (Long/parseLong
              (or (System/getProperty "VYBE_NREPL_PORT")
                  (System/getenv "VYBE_NREPL_PORT")
                  "7888"))
        default-handler @(requiring-resolve 'nrepl.server/default-handler)
        cider-resolve @(requiring-resolve 'cider.nrepl/resolve-or-fail)
        cider-middleware @(requiring-resolve 'cider.nrepl.middleware/cider-middleware)
        start-server @(requiring-resolve 'nrepl.server/start-server)
        handler (apply default-handler (map cider-resolve cider-middleware))]
    (try
      (start-server :port port
                    :handler handler)
      (catch Exception e
        (println :nrepl-connection/error "\n" e))
      (finally
        (println :nrepl-connection :port port)))))

(defn -main
  []
  ;; Start server as we need to be on the main thread, see
  ;; https://medium.com/@kadirmalak/interactive-opengl-development-with-clojure-and-lwjgl-2066e9e48b52
  (start-nrepl!)

  (while (empty? (:buf-general @(impl-state)))
    (Thread/sleep 30))

  (while true
    (-main-loop)))

;; -- Raygui.
(defn gui-icon
  ([icon]
   (str "#" icon "#"))
  ([icon text]
   (str "#" icon "#" text)))

(defn gui-text-input-box
  "Draws a RayGui text-mem input box. Returns the created text memory (text-mem).

  `on-close`: (fn [text-mem])
  `buttons`:  [{:label \"SOME_LABEL\" :on-click (fn [text-mem]}
             {:label \"SOME_OTHER_LABEL\" :on-click (fn [text-mem]}]"
  [identifier {:keys [text text-size rect title message on-close buttons]
               :or {text ""
                    text-size 50
                    rect [10 10 200 140]
                    title "Some title"
                    message "Some message"
                    on-close (fn [_mem] (println ::gui-text-input-box identifier :ON_CLOSE))
                    buttons [{:label "Button 1"
                              :on-click (fn [msg]
                                          (println ::gui-text-input-box identifier :btn-1 msg))}
                             {:label "Button 2"
                              :on-click (fn [msg]
                                          (println ::gui-text-input-box identifier :btn-2 msg))}]}}]
  (let [text-mem (vp/mem identifier text text-size)
        res (vr.c/gui-text-input-box (vr/Rectangle rect)
                                     title message
                                     (->> buttons
                                          (mapv :label)
                                          (str/join ";"))
                                     text-mem text-size vp/null)]
    (when-not (neg? res)
      (if (zero? res)
        (on-close text-mem)
        ((:on-click (nth buttons (dec res))) text-mem)))
    text-mem))

;; jextract, https://jdk.java.net/jextract/
;; sudo xattr -r -d com.apple.quarantine ~/Downloads/jextract-22
;; ... refs
;; calling jextract, https://docs.oracle.com/en/java/javase/21/core/call-native-functions-jextract.html#GUID-480A7E64-531A-4C88-800F-810FF87F24A1

;; jdk22, https://jdk.java.net/22/
;; sdk install java jdk-22 /Users/pfeodrippe/Downloads/jdk-22.jdk/Contents/Home

;; ... panama docs
;; javadocs, https://cr.openjdk.org/~mcimadamore/jdk/FFM_22_PR/javadoc/java.base/java/lang/foreign/Arena.html
;; native memory, https://community.sap.com/t5/technology-blogs-by-sap/from-c-to-java-code-using-panama/ba-p/13578395
;; memory access, https://github.com/openjdk/panama-foreign/blob/foreign-memaccess%2Babi/doc/panama_memaccess.md
;; struct layout, https://www.baeldung.com/java-project-panama#2-foreign-memory-manipulation
