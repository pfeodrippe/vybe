(ns vybe.jolt
  "Some functions are based on zphysics.zig.

  See a sample at https://github.com/zig-gamedev/zig-gamedev/blob/main/samples/physics_test_wgpu/src/physics_test_wgpu.zig#L321

  Also see https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44"
  (:require
   [vybe.jolt.c :as vj.c]
   [vybe.panama :as vp])
  (:import
   (org.vybe.jolt jolt
                  JPC_BodyCreationSettings
                  JPC_Body

                  JPC_BroadPhaseLayerInterfaceVTable
                  JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer
                  JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer$Function
                  JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers
                  JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers$Function

                  JPC_ObjectVsBroadPhaseLayerFilterVTable
                  JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide
                  JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide$Function

                  JPC_ObjectLayerPairFilterVTable
                  JPC_ObjectLayerPairFilterVTable$ShouldCollide
                  JPC_ObjectLayerPairFilterVTable$ShouldCollide$Function)))

(vp/defcomp BroadPhaseLayerInterfaceVTable (JPC_BroadPhaseLayerInterfaceVTable/layout))
(vp/defcomp ObjectVsBroadPhaseLayerFilterVTable (JPC_ObjectVsBroadPhaseLayerFilterVTable/layout))
(vp/defcomp ObjectLayerPairFilterVTable (JPC_ObjectLayerPairFilterVTable/layout))
(vp/defcomp BodyCreationSettings (JPC_BodyCreationSettings/layout))
(vp/defcomp Body (JPC_Body/layout))

(vp/defcomp VTable
  {:constructor (fn [v]
                  {:vtable (vp/mem v)})}
  [[:vtable :pointer]])

(vp/defcomp HalfExtent
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Vector3
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Vector4
  [[:x :float]
   [:y :float]
   [:z :float]
   [:w :float]])

(defn init
  []

  (vj.c/jpc-register-default-allocator)
  (vj.c/jpc-create-factory)
  (vj.c/jpc-register-types)

  (vj.c/jpc-job-system-create
   (jolt/JPC_MAX_PHYSICS_JOBS)
   (jolt/JPC_MAX_PHYSICS_BARRIERS)
   (min 16 (.availableProcessors (Runtime/getRuntime)))))

(comment

  (def job-system (init))

  ;; See https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44

  ;; Physics system.
  (do
    (def v1
      (let [vtable (BroadPhaseLayerInterfaceVTable)]
        (-> vtable
            (assoc :GetNumBroadPhaseLayers
                   (-> (reify JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers$Function
                         (apply [_ _]
                           (println :aaaa)
                           2))
                       (JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers/allocate (vp/default-arena)))
                   :GetBroadPhaseLayer
                   (-> (reify JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer$Function
                         (apply [_ _ layer]
                           (println :broadphaselayer layer)
                           (byte layer)))
                       (JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer/allocate (vp/default-arena)))))))

    (def v2
      (let [vtable (ObjectVsBroadPhaseLayerFilterVTable)]
        (-> vtable
            (assoc :ShouldCollide
                   (-> (reify JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide$Function
                         (apply [_ _ layer1 layer2]
                           (println :shouldcollide-1 layer1 layer2)
                           (case layer1
                             0 (= layer2 1)
                             1 true
                             false)))
                       (JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide/allocate (vp/default-arena)))))))

    (def v3
      (let [vtable (ObjectLayerPairFilterVTable)]
        (-> vtable
            (assoc :ShouldCollide
                   (-> (reify JPC_ObjectLayerPairFilterVTable$ShouldCollide$Function
                         (apply [_ _ layer1 layer2]
                           (println :shouldcollide-2 layer1 layer2)
                           (case layer1
                             0 (= layer2 1)
                             1 true
                             false)))
                       (JPC_ObjectLayerPairFilterVTable$ShouldCollide/allocate (vp/default-arena)))))))

    (def physics-system
      (vj.c/jpc-physics-system-create
       1024 0 1024 1024
       (VTable v1)
       (VTable v2)
       (VTable v3)))

    (comment

      (let [v (Vector3 {:y -10})]
        (vj.c/jpc-physics-system-set-gravity physics-system v)
        (vj.c/jpc-physics-system-get-gravity physics-system v)
        v)

      ()))

  ;; Body interface.
  (do
    (def body-interface (vj.c/jpc-physics-system-get-body-interface physics-system))

    (def floor-shape-settings (vj.c/jpc-box-shape-settings-create (HalfExtent [100 1 100])))
    (def floor-shape (vj.c/jpc-shape-settings-create-shape floor-shape-settings))

    (def floor
      (vj.c/jpc-body-interface-create-and-add-body
       body-interface
       (BodyCreationSettings
        {:position (Vector4 [0 -1 0 1])
         :rotation (Vector4 [0 0 0 1])
         :shape floor-shape
         :motion_type (jolt/JPC_MOTION_TYPE_STATIC)
         :object_layer 0

         :friction 0.2
         :is_sensor false
         :allow_sleeping true
         :allow_dynamic_or_kinematic false
         :use_manifold_reduction true
         :linear_damping 0.05
         :angular_damping 0.05
         :max_linear_velocity 500
         :max_angular_velocity (* 0.25 60 3.14)
         :gravity_factor 1
         :inertia_multiplier 1})
       (jolt/JPC_ACTIVATION_ACTIVATE)))

    (def box-shape-settings (vj.c/jpc-box-shape-settings-create (HalfExtent [0.5 0.5 0.5])))
    (def box-shape (vj.c/jpc-shape-settings-create-shape box-shape-settings))

    (def bodies
      (mapv (fn [idx]
              (vj.c/jpc-body-interface-create-and-add-body
               body-interface
               (BodyCreationSettings
                {:position (Vector4 [0 (+ 8 (* idx 1.2)) 8 1])
                 :rotation (Vector4 [0 0 0 1])
                 :shape box-shape
                 :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                 :object_layer 1
                 :angular_velocity (Vector4)

                 :friction 0.2
                 :is_sensor false
                 :allow_sleeping true
                 :allow_dynamic_or_kinematic false
                 :use_manifold_reduction true
                 :linear_damping 0.05
                 :angular_damping 0.05
                 :max_linear_velocity 500
                 :max_angular_velocity (* 0.25 60 3.14)
                 :gravity_factor 1
                 :inertia_multiplier 1})
               (jolt/JPC_ACTIVATION_ACTIVATE)))
            (range 16)))

    (vj.c/jpc-physics-system-optimize-broad-phase physics-system)

    (comment

      (vj.c/jpc-body-get-motion-type)
      (vj.c/jpc-body-interface-get-motion-type body-interface floor)

      (let [pos (Vector4)]
        (vj.c/jpc-body-interface-get-position body-interface (last bodies) pos)
        pos)

      (let [pos (Vector4)]
        (vj.c/jpc-body-get-position (last bb) pos)
        pos)

      ()))

  (def temp-allocator
    (vj.c/jpc-temp-allocator-create (* 16 1024 1024)))

  ;; Update
  (do
    (vj.c/jpc-physics-system-update physics-system
                                    (/ 1.0 60)
                                    1
                                    1
                                    temp-allocator
                                    job-system)

    (jolt/JPC_PHYSICS_UPDATE_NO_ERROR)

    #_(vj.c/jpc-body-interface-is-active body-interface (first bodies))

    (let [bodies (vp/arr (vj.c/jpc-physics-system-get-bodies-unsafe physics-system)
                         (vj.c/jpc-physics-system-get-num-bodies physics-system)
                         [:pointer Body])]
      (mapv :position bodies)))

  ())
