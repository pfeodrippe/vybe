(ns vybe.raylib.lwjgl.smoke
  (:require
   [vybe.raylib.lwjgl.host :as host]))

(defn -main
  [& _]
  (println :raylib-wasm-import-summary (host/import-summary))
  (host/init-window! {:width 640
                      :height 360
                      :title "Vybe Raylib Wasm/LWJGL Host Smoke"})
  (let [deadline (+ (System/nanoTime) (* 2 1000000000))]
    (while (and (< (System/nanoTime) deadline)
                (not (host/should-close?)))
      (host/poll-events!)
      (host/clear! 0.06 0.08 0.12 1.0)
      (host/swap-buffers!)))
  (host/destroy!))
