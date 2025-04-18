[![Clojars Project](https://img.shields.io/clojars/v/io.github.pfeodrippe/vybe.svg)](https://clojars.org/io.github.pfeodrippe/vybe)

# Vybe

A Clojure framework for game dev (alpha).

Check <https://vybegame.dev> for documentation o/

## Getting Started

This is the same example as we have at <https://vybegame.dev/getting-started/>.
You can also check <https://github.com/pfeodrippe/vybe-games> for more examples.

``` clojure
(ns vybe.example.minimal
  "Example with minimal setup, it will load a builtin GLTF (.glb) model with
  which contains a cube."
  (:require
   [vybe.flecs :as vf]
   [vybe.game :as vg]
   [vybe.raylib.c :as vr.c]
   [vybe.raylib :as vr]
   [vybe.type :as vt]))

(defn draw
  [w delta-time]
  ;; For debugging
  #_(def w w)

  ;; --8<-- [start:flecs_physics]
  ;; Progress the systems (using Flecs).
  (vf/progress w delta-time)

  ;; Update physics (using Jolt).
  (vg/physics-update! w delta-time)
  ;; --8<-- [end:flecs_physics]

  ;; --8<-- [start:rendering]
  ;; Add some lights (from the blender model).
  (vg/draw-lights w)

  ;; Render stuff into the screen (using Raylib) using a built-in effect.
  (vg/with-drawing
    (vg/with-fx w {:drawing true
                   :shaders (vg/fx-painting w)}
      (vr.c/clear-background (vr/Color [255 20 100 255]))

      ;; Here we do a query for the active camera (it's setup when loading the model).
      (vf/with-query w [_ :vg/camera-active
                        camera vt/Camera]
        (vg/with-camera camera
          (vg/draw-scene w)))

      (vr.c/draw-fps 510 570))))
;; --8<-- [end:rendering]

(defn init
  []
  (let [w (vf/make-world)]
    ;; If you want to enable debugging (debug messages + clerk + flecs explorer),
    ;; uncomment line below.
    #_(vg/debug-init! w)

    (vg/start! w 600 600 #'draw
               (fn [w]
                 (-> w
                     ;; Load model (as a resource).
                     ;; We are going to load a bultin model, but you can use any .glb
                     ;; resource you have.
                     (vg/model :my/model (vg/resource "com/pfeodrippe/vybe/model/minimal.glb")))))))

#_ (init)
```

## Flecs

One of the libraries used by Vybe is Flecs, it's a powerful and innovative ECS library,
check <https://www.flecs.dev/flecs/md_docs_2Quickstart.html>.

We have a standalone vybe-flecs dependency that is concerned only with the Flecs functionality,
so it won't have Raylib, Jolt etc, you can see it on Clojars below.

[![Clojars Project](https://img.shields.io/clojars/v/io.github.pfeodrippe/vybe-flecs.svg)](https://clojars.org/io.github.pfeodrippe/vybe-flecs)

```clojure
(require '[vybe.flecs :as vf])
(require '[vybe.flecs.c :as vf.c])
(import '(org.vybe.flecs flecs))

(def w (vf/make-world))

(merge w {:bob []})
(vf/get-name (:bob w))
;; => "bob"

;; You have access to systems, queries, observers, relationships etc,
;; all of these concepts are described at https://www.flecs.dev/flecs/md_docs_2Quickstart.html
```
