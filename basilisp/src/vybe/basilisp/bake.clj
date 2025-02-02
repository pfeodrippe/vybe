(ns vybe.basilisp.bake
  "Creates a torus pattern with randomly colored materials."
  (:import bpy
           math))

(defn clear-mesh-objects
  []
  (.select-all     bpy.ops/object ** :action "DESELECT")
  (.select-by-type bpy.ops/object ** :type "MESH")
  (.delete         bpy.ops/object))

(def obj bpy.ops/object)
(def objs bpy.ops/objects)

#_(clear-mesh-objects)

(comment

  ;; Refs
  ;; - https://blenderartists.org/t/how-to-generate-textures-without-rendering-to-file/1560362/4

  ;; https://docs.blender.org/api/current/bpy.ops.object.html#bpy.ops.object.bake
  (.bake obj **
         :type "COMBINED"
         :pass_filter (lisp->py
                       #{"EMIT" "DIRECT" "INDIRECT"
                         "COLOR" "DIFFUSE" "GLOSSY"
                         "TRANSMISSION"})
         :width 4096
         :height 4096
         :use_selected_to_active true
         :cage_extrusion 0.02
         :use_clear true)

  #_(.select-pattern obj **
                   :pattern "*SSS*"
                   :extend false)

  (for [o bpy.data/objects]
    (when (or (= (.-name o) "SSS")
              (= (.-name o) "SSS.bake"))
      (.select_set o true)
      (set! bpy.context.view_layer.objects/active o)))

  ())

(defn create-random-material []
  (let [mat  (.new bpy.data/materials ** :name "RandomMaterial")
        _    (set! (.-use-nodes mat) true)
        bsdf (aget (.. mat -node-tree -nodes) "Principled BSDF")]

    (set! (-> bsdf .-inputs (aget "Base Color") .-default-value)
          [(rand) (rand) (rand) 1])
    mat))

(defn create-torus [radius tube-radius location segments]
  (.primitive-torus-add bpy.ops/mesh **
                        :major-radius radius
                        :minor-radius tube-radius
                        :location location
                        :major-segments segments
                        :minor-segments segments)
  (let [material (create-random-material)]
    (-> bpy.context/object .-data .-materials (.append material))))

#_(create-torus 5, 5, [0 0 0] 48)

(defn create-pattern [{:keys [layers-num radius tube-radius]
                       :or {layers-num 2
                            radius 2
                            tube-radius 0.2}}]
  (let [angle-step (/ math/pi 4)]
    (dotimes [i layers-num]
      (let [layer-radius (* radius (inc i))
            objects-num (* 12 (inc i))]
        (dotimes [j objects-num]
          (let [angle (* j angle-step)
                x (* layer-radius (math/cos angle))
                y (* layer-radius (math/sin angle))
                z (* i 0.5)]
            (create-torus (/ radius 2) tube-radius [x y z] 48)))))))

#_(create-pattern {:layers-num 5})
