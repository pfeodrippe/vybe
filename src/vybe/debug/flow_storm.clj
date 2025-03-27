(ns vybe.debug.flow-storm
  "See https://www.flow-storm.org/

  You should have Flow Storm as one of your dependencies
  before calling this ns."
  (:require
   [vybe.panama :as vp]
   [flow-storm.runtime.values :as fs.v]
   [flow-storm.debugger.ui.data-windows.visualizers :as viz]
   [flow-storm.debugger.main :as fs.dbg]
   [nrepl.server :refer [start-server]]
   [vybe.type :as vt]
   [vybe.raylib :as vr]
   [vybe.raylib.c :as vr.c]
   [cider.nrepl :refer [cider-nrepl-handler]]
   [cider.nrepl.middleware :as mw]
   [clojure.java.io :as io]
   [vybe.c :as vc]
   [com.climate.claypoole :as cp]
   vybe.flecs)
  (:import
   (vybe.flecs VybeFlecsWorldMap)
   (vybe.panama VybePMap)
   (org.vybe.raylib raylib)
   (javafx.scene.image Image ImageView PixelWriter WritableImage PixelFormat)
   (javafx.scene.paint Color)
   (javafx.embed.swing SwingFXUtils)
   (javax.imageio ImageIO)
   (java.io File)
   (java.lang.foreign Arena)))

(set! *warn-on-reflection* true)

(vc/defn* free :- :void
  [ptr :- :*]
  (vc/free ptr))

;; TODO Maybe use something else as this is TOO slow right now.
(defn- texture->fx-image
  ([texture]
   (texture->fx-image texture nil))
  ([{:keys [width height]} texture-pointer]
   (let [pixels (-> texture-pointer
                    (vp/arr (* width height) vr/Color))
         image (WritableImage. width height)
         pw (.getPixelWriter image)]
     (doseq [x (range width)
             y (range height)]
       (let [[r g b a] (->> (nth pixels (+ x (* y width)))
                            ((juxt :r :g :b :a))
                            (mapv (fn [v]
                                    (/ (if (neg? v)
                                         (+ 255 (inc v))
                                         v)
                                       255.0))))]
         (.setColor pw x y (Color. r g b a))))
     image)))
#_ (texture->fx-image (:texture user/rt)
                      (:texture-pointer (meta user/rt)))

(defn- texture->file-path
  [texture texture-pointer]
  (let [image (texture->fx-image texture texture-pointer)
        file (File/createTempFile "vy-texture" ".png")
        file-path (str file)]
    (-> (SwingFXUtils/fromFXImage image nil)
        (ImageIO/write "png" file))
    file-path))

(extend-protocol fs.v/SnapshotP
  VybeFlecsWorldMap
  (snapshot-value [_wm]
    "WORLD"))

(defonce pool (cp/threadpool 12))

(extend-protocol fs.v/SnapshotP
  VybePMap
  (snapshot-value [v]
    (let [c (vp/component v)
          rt? (= c vr/RenderTexture2D)
          t (vr.c/get-time)
          meta-extra (when (and rt?
                                (zero? (mod (int t) 2))
                                (<= (- t (int t))
                                    1/30))
                       (let [arena (Arena/ofShared)]
                         (binding [vp/*dyn-arena* arena]
                           (let [texture (into {} (:texture v))
                                 {:keys [id width height]} texture
                                 texture-pointer (vr.c/rl-read-texture-pixels
                                                  id width height (raylib/RL_PIXELFORMAT_UNCOMPRESSED_R8G8B8A8))]
                             {:*texture-path
                              (cp/future pool
                                         (binding [vp/*dyn-arena* arena]
                                           (try
                                             (let [file-path (texture->file-path texture texture-pointer)]
                                               #_(println :SAVING file-path)
                                               file-path)
                                             (finally
                                               (try
                                                 (free texture-pointer)
                                                 (.close arena)
                                                 (catch Exception e
                                                   (println e)))))))}))))]
      (when (or (not rt?) meta-extra)
        (with-meta (into {} v)
          (merge {::vp/VybePMap {:component (symbol (vp/comp-name c))}}
                 meta-extra))))))

(fs.v/register-data-aspect-extractor
 {:id ::rt
  :pred (fn [v _]
          (:*texture-path (meta v)))
  :extractor (fn [rt _]
               {:texture-path @(:*texture-path (meta rt))})})
#_ (fs.v/extract-data-aspects user/rt nil)

;; --- UI
(viz/add-default-visualizer
 (fn [v]
   (contains? (::fs.v/kinds v) ::rt))
 ::texture)

(def ^:private texture-path->image
  (memoize
   (fn [texture-path]
     (println :LOADING_TEXTURE texture-path)
     (doto (ImageView. (Image. (str "file:" texture-path)))
       (.setFitWidth 300)
       (.setPreserveRatio true)))))

(viz/register-visualizer
 {:id ::texture
  :pred (fn [v]
          (contains? (::fs.v/kinds v) ::rt))
  :on-create (fn [{:keys [texture-path]}]
               {:fx/node (texture-path->image texture-path)})
  ;; OPTIONALLY
  #_ #_:on-update (fn [val created-ctx-map {:keys [new-val]}] )
  #_ #_:on-destroy (fn [created-ctx-map] )})

#_ (flow-storm.runtime.debuggers-api/set-recording true)
#_ (flow-storm.runtime.debuggers-api/set-recording false)

;; --- nREPL
;; We run the debugger in another JVM because of the OSX's first thread
;; issue, so we start a nREPL server for this new process as well.
(defn start-nrepl!
  []
  (let [port (Long/parseLong
              (or (System/getProperty "VYBE_NREPL_PORT")
                  (System/getenv "VYBE_NREPL_PORT")
                  "7988"))
        handler (apply nrepl.server/default-handler
                       (map #'cider.nrepl/resolve-or-fail mw/cider-middleware))]
    (try
      (start-server :port port
                    :handler handler)
      (catch Exception e
        (println :nrepl-connection/error "\n" e))
      (finally
        (println :nrepl-connection :port port)))))

(defn start-debugger
  [m]
  (start-nrepl!)
  (fs.dbg/start-debugger m))
