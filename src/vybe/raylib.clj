(ns vybe.raylib
  "Raylib wasm/LWJGL bindings."
  (:require
   [portal.nrepl]
   [cider.nrepl :refer [cider-nrepl-handler]]
   [cider.nrepl.middleware :as mw]
   [nrepl.server :refer [start-server]]
   [vybe.raylib.abi :as abi]
   [vybe.raylib :as vr]
   [vybe.raylib.impl :as vr.impl]
   [vybe.raylib.c :as vr.c]
   [vybe.wasm :as vp]
   [clojure.string :as str]))

;; -- Raylib types
(vp/defcomp RenderTexture2D (abi/layout :RenderTexture2D))
(vp/defcomp Texture (abi/layout :Texture))
(vp/defcomp VyModel (abi/layout :VyModel))
(vp/defcomp VyModelMeta (abi/layout :VyModelMeta))
(vp/defcomp Mesh (abi/layout :Mesh))
(vp/defcomp Material (abi/layout :Material))
(vp/defcomp MaterialMap (abi/layout :MaterialMap))
(vp/defcomp Matrix (abi/layout :Matrix))
(vp/defcomp Vector2 (abi/layout :Vector2))
(vp/defcomp Vector3 (abi/layout :Vector3))
(vp/defcomp Vector4 (abi/layout :Vector4))
(vp/defcomp Camera (abi/layout :Camera))
(vp/defcomp Camera2D (abi/layout :Camera2D))
(vp/defcomp Rectangle (abi/layout :Rectangle))
(vp/defcomp Model (abi/layout :Model))
(vp/defcomp Image (abi/layout :Image))
(vp/defcomp Shader (abi/layout :Shader))

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

(vp/defcomp Color
  {:constructor (fn [v]
                  (if (string? v)
                    (let [[r g b a] (->> (last (str/split v #"#"))
                                         (partition-all 2 2)
                                         (mapv #(let [[h l] (mapv char->value %)]
                                                  (+ (* h 16) l))))]
                      [r g b (or a 255)])
                    v))}
  (abi/layout :Color))

(defn raylib-constant
  [k]
  (abi/constant k))

(defmacro t
  "Runs command (delayed) in the main thread.

  Useful for REPL testing as it will block and return
  the result from the command."
  [& body]
  `(vr.impl/t ~@body))

;; -- Custom VY types.
(defn vy-model
  [model]
  (let [mesh-count (:meshCount model)
        meta-arr (vp/arr mesh-count VyModelMeta)]
    #_(assoc-in meta-arr [3 :drawingDisabled] 1)
    (VyModel {:model model, :metaCount mesh-count, :meta meta-arr})))

;; -- Helpers.
(defn material-get
  "For `prop` options, check `org.vybe.raylib.raylib/MATERIAL_MAP...`,
  e.g.

  org.vybe.raylib.raylib/MATERIAL_MAP_DIFFUSE"
  [material prop]
  (let [maps (-> material :maps)]
    (when-not (vp/null? maps)
      (-> maps
          (vp/arr 99 vr/MaterialMap)
          (nth prop)))))

;; ------- Misc
(defn- run-buf-general-cmds
  []
  (let [{:keys [buf-general]} @vr.impl/*state]
    (locking vr.impl/*state
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
          (swap! vr.impl/*state (fn [state]
                                  (-> state
                                      (assoc :buf-general [])))))))))

(defn- run-buf-1-2-cmds
  []
  (let [{:keys [buf1 buf2 front-buf?]} @vr.impl/*state
        buf (if front-buf? buf1 buf2)]
    (locking vr.impl/*state
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
          (swap! vr.impl/*state (fn [state]
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
              (seq (:buf1 @vr.impl/*state))
              (seq (:buf2 @vr.impl/*state)))
      (vr.c/begin-drawing)
      (run-buf-1-2-cmds)
      (vr.c/end-drawing)

      (vr.c/begin-drawing)
      (run-buf-1-2-cmds)
      (vr.c/end-drawing))))

(defn start-nrepl!
  []
  (let [port (Long/parseLong
              (or (System/getProperty "VYBE_NREPL_PORT")
                  (System/getenv "VYBE_NREPL_PORT")
                  "7888"))
        handler (apply nrepl.server/default-handler
                       (concat (map #'cider.nrepl/resolve-or-fail mw/cider-middleware)
                               #_portal.nrepl/middleware
                               #_vybe.nrepl/middleware))]
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

  (while (empty? (:buf-general @vr.impl/*state))
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
