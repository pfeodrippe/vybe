(ns vybe.jolt
  "Some functions are based on zphysics.zig.

  See a sample at https://github.com/zig-gamedev/zig-gamedev/blob/main/samples/physics_test_wgpu/src/physics_test_wgpu.zig#L321

  Also see https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44"
  (:require
   [vybe.jolt.c :as vj.c]
   [vybe.panama :as vp])
  (:import
   (org.vybe.jolt jolt
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

(vp/defcomp VTable
  {:constructor (fn [v]
                  {:vtable (vp/mem v)})}
  [[:vtable :pointer]])

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

  (init)

  ;; See https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44
  (do
    (def v1
      (let [vtable (BroadPhaseLayerInterfaceVTable)]
        (-> vtable
            (assoc :GetNumBroadPhaseLayers
                   (-> (reify JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers$Function
                         (apply [_ _]
                           2))
                       (JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers/allocate (vp/default-arena)))
                   :GetBroadPhaseLayer
                   (-> (reify JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer$Function
                         (apply [_ _ layer]
                           (byte layer)))
                       (JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer/allocate (vp/default-arena)))))))

    (def v2
      (let [vtable (ObjectVsBroadPhaseLayerFilterVTable)]
        (rr vtable)
        (-> vtable
            (assoc :ShouldCollide
                   (-> (reify JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide$Function
                         (apply [_ _ layer1 layer2]
                           (case layer1
                             0 (= layer2 1)
                             1 true
                             false)))
                       (JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide/allocate (vp/default-arena)))))))

    (def v3
      (let [vtable (ObjectLayerPairFilterVTable)]
        (rr vtable)
        (-> vtable
            (assoc :ShouldCollide
                   (-> (reify JPC_ObjectLayerPairFilterVTable$ShouldCollide$Function
                         (apply [_ _ layer1 layer2]
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
       (VTable v3))))

  ())
