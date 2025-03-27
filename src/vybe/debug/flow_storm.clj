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
   vybe.flecs)
  (:import
   (vybe.flecs VybeFlecsWorldMap)
   (vybe.panama VybePMap)
   (org.vybe.raylib raylib)
   (javafx.scene.image Image ImageView PixelWriter WritableImage PixelFormat)
   (javafx.scene.paint Color)))

(extend-protocol fs.v/SnapshotP
  VybeFlecsWorldMap
  (snapshot-value [_wm]
    "WORLD"))

#_(defn- texture->fx-image
  ([texture]
   (texture->fx-image texture) nil)
  ([{:keys [id width height]} pixels-byte-array]
   (let [pixels (or pixels-byte-array
                    (-> (vr.c/rl-read-texture-pixels id width height
                                                     (raylib/RL_PIXELFORMAT_UNCOMPRESSED_R8G8B8A8))
                        (vp/arr (* width height 4) :byte)
                        byte-array))
         image (WritableImage. width height)
         pw (.getPixelWriter image)]
     (.setPixels pw 0 0 width height
                 (PixelFormat/getByteRgbInstance) pixels
                 0 width)
     image)))
(defn- texture->fx-image
  ([texture]
   (texture->fx-image texture nil))
  ([{:keys [id width height]} pixels]
   (let [pixels (or pixels
                    (-> (vr.c/rl-read-texture-pixels id width height
                                                     (raylib/RL_PIXELFORMAT_UNCOMPRESSED_R8G8B8A8))
                        (vp/arr (* width height) vr/Color)))
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
         (.setColor pw x y (Color. r g b 1.0))))
     image)))
#_ (texture->fx-image (:texture user/rt))

(extend-protocol fs.v/SnapshotP
  VybePMap
  (snapshot-value [v]
    (let [c (vp/component v)]
      (with-meta (merge (into {} v)
                        ;; TODO Close the vybepmap.
                        (when (= c vr/RenderTexture2D)
                          {::meta
                           (let [{:keys [id width height]} (:texture v)]
                             {:texture-pointer
                              (vr.c/rl-read-texture-pixels
                               id width height (raylib/RL_PIXELFORMAT_UNCOMPRESSED_R8G8B8A8))})}))
        {::vp/VybePMap {:component (symbol (vp/comp-name c))}}))))

(fs.v/register-data-aspect-extractor
 {:id ::rt
  :pred (fn [v _]
          (:texture-pointer (::meta v)))
  :extractor (fn [{:keys [texture] :as rt} _]
               (let [{:keys [width height]} texture]
                 {:texture (into {} texture)
                  :texture-pixels (->> (-> (:texture-pointer (::meta rt))
                                           (vp/arr (* width height) vr/Color))
                                       (mapv #(into {} %)))}))})
#_ (fs.v/extract-data-aspects user/rt nil)

(viz/add-default-visualizer
 (fn [v]
   (contains? (::fs.v/kinds v) ::rt))
 ::texture)

(viz/register-visualizer
 {:id ::texture
  :pred (fn [v]
          (contains? (::fs.v/kinds v) ::rt))
  :on-create (fn [{:keys [texture-pixels texture]}]
               {:fx/node
                (doto (ImageView. (texture->fx-image texture texture-pixels))
                  (.setFitWidth 300)
                  (.setPreserveRatio true))})
  ;; OPTIONALLY
  #_ #_:on-update (fn [val created-ctx-map {:keys [new-val]}] )
  #_ #_:on-destroy (fn [created-ctx-map] )})

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
