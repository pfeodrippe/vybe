(ns vybe.raylib.lwjgl.wasm-smoke
  (:require
   [vybe.raylib.lwjgl.host :as host]
   [vybe.raylib.lwjgl.wasm :as raylib-wasm]
   [vybe.wasm :as vw]))

(defn- write-color!
  [module ptr [r g b a]]
  (vw/write-bytes! module ptr
                   (byte-array (map unchecked-byte [r g b (or a 255)])))
  ptr)

(defn -main
  [& _]
  (try
    (let [module (raylib-wasm/module)]
      (vw/with-c-string* module "Vybe Raylib Wasm on LWJGL"
        (fn [title]
          (raylib-wasm/call "InitWindow" 640 360 title)))
      (vw/with-alloc* module 4
        (fn [color]
          (write-color! module color [18 28 42 255])
          (let [deadline (+ (System/nanoTime) (* 2 1000000000))]
            (while (and (< (System/nanoTime) deadline)
                        (zero? (long (raylib-wasm/call "WindowShouldClose"))))
              (host/poll-events!)
              (raylib-wasm/call "BeginDrawing")
              (raylib-wasm/call "ClearBackground" color)
              (raylib-wasm/call "EndDrawing"))))))
    (finally
      (host/destroy!))))
