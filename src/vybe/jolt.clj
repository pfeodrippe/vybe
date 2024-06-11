(ns vybe.jolt
  "Some functions are based on zphysics.zig.

  See a sample at https://github.com/zig-gamedev/zig-gamedev/blob/main/samples/physics_test_wgpu/src/physics_test_wgpu.zig#L321

  Also see https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44"
  (:require
   [vybe.jolt.c :as vj.c]
   [vybe.panama :as vp]
   [vybe.jolt :as vj]
   [clojure.set :as set])
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

(vp/defcomp BroadPhaseLayerInterfaceVTable (JPC_BroadPhaseLayerInterfaceVTable/layout))
(vp/defcomp ObjectVsBroadPhaseLayerFilterVTable (JPC_ObjectVsBroadPhaseLayerFilterVTable/layout))
(vp/defcomp ObjectLayerPairFilterVTable (JPC_ObjectLayerPairFilterVTable/layout))
(vp/defcomp Body (JPC_Body/layout))

(def layer->int
  {:vj.layer/non-moving 0
   :vj.layer/moving 1})

(def int->layer
  (set/map-invert layer->int))

(vp/defcomp BodyCreationSettings
  {:constructor (fn [{:keys [object_layer] :as m}]
                  (merge {:friction 0.2
                          :is_sensor false
                          :allow_sleeping true
                          :allow_dynamic_or_kinematic false
                          :use_manifold_reduction true
                          :linear_damping 0.05
                          :angular_damping 0.05
                          :max_linear_velocity 500
                          :max_angular_velocity (* 0.25 60 3.14)
                          :gravity_factor 1
                          :inertia_multiplier 1
                          :angular_velocity (Vector4)
                          :object_layer (cond
                                          (not object_layer) 0
                                          (keyword? object_layer) (layer->int object_layer)
                                          :else object_layer)
                          :motion_type (jolt/JPC_MOTION_TYPE_STATIC)}
                         (dissoc m :object_layer)))}
  (JPC_BodyCreationSettings/layout))

;; We can only initialize this once.
(defonce init
  (memoize
   (fn []
     (vj.c/jpc-register-default-allocator)
     (vj.c/jpc-create-factory)
     (vj.c/jpc-register-types)

     (vj.c/jpc-job-system-create
      (jolt/JPC_MAX_PHYSICS_JOBS)
      (jolt/JPC_MAX_PHYSICS_BARRIERS)
      (min 16 (.availableProcessors (Runtime/getRuntime)))))))

;; See https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44
(defn default-physics-system
  []
  (let [broad-phase-layer-interface
        (-> (BroadPhaseLayerInterfaceVTable)
            (assoc :GetNumBroadPhaseLayers
                   (-> (reify JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers$Function
                         (apply [_ _]
                           2))
                       (JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers/allocate (vp/default-arena)))
                   :GetBroadPhaseLayer
                   (-> (reify JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer$Function
                         (apply [_ _ layer]
                           (byte layer)))
                       (JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer/allocate (vp/default-arena))))
            VTable)

        object-vs-broad-phase-layer-interface
        (-> (ObjectVsBroadPhaseLayerFilterVTable)
            (assoc :ShouldCollide
                   (-> (reify JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide$Function
                         (apply [_ _ layer1 layer2]
                           (case (layer->int layer1)
                             :vj.layer/non-moving (= (layer->int layer2) :vj.layer/moving)
                             :vj.layer/moving true
                             false)))
                       (JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide/allocate (vp/default-arena))))
            VTable)

        object-layer-pair-filter-interface
        (-> (ObjectLayerPairFilterVTable)
            (assoc :ShouldCollide
                   (-> (reify JPC_ObjectLayerPairFilterVTable$ShouldCollide$Function
                         (apply [_ _ layer1 layer2]
                           (case (layer->int layer1)
                             :vj.layer/non-moving (= (layer->int layer2) :vj.layer/moving)
                             :vj.layer/moving true
                             false)))
                       (JPC_ObjectLayerPairFilterVTable$ShouldCollide/allocate (vp/default-arena))))
            VTable)]

    (vj.c/jpc-physics-system-create
     1024 0 1024 1024
     broad-phase-layer-interface
     object-vs-broad-phase-layer-interface
     object-layer-pair-filter-interface)))

(defn body-interface
  [physics-system]
  (vj.c/jpc-physics-system-get-body-interface physics-system))

(defn box-settings
  [half-extent]
  (vj.c/jpc-box-shape-settings-create half-extent))

(defn shape
  [settings]
  (vj.c/jpc-shape-settings-create-shape settings))

(defn add-body
  ([body-i body-settings]
   (add-body body-i body-settings (jolt/JPC_ACTIVATION_ACTIVATE)))
  ([body-i body-settings activation]
   (vj.c/jpc-body-interface-create-and-add-body body-i body-settings activation)))

(defn optimize-broad-phase
  [physics-system]
  (vj.c/jpc-physics-system-optimize-broad-phase physics-system))

(defonce *temp-allocator
  (delay (vj.c/jpc-temp-allocator-create (* 16 1024 1024))))

(defn update!
  ([job-system physics-system delta-time]
   (update! job-system physics-system delta-time 1 1 @*temp-allocator))
  ([job-system physics-system delta-time collision-steps integration-sub-steps allocator]
   (let [res (vj.c/jpc-physics-system-update physics-system
                                             delta-time
                                             collision-steps
                                             integration-sub-steps
                                             allocator
                                             job-system)]
     (if (= res (jolt/JPC_PHYSICS_UPDATE_NO_ERROR))
       res
       (throw (ex-info "An error whily update Physics has happened"
                       {:res res}))))))

(defn get-bodies
  [physics-system]
  (vp/arr (vj.c/jpc-physics-system-get-bodies-unsafe physics-system)
          (vj.c/jpc-physics-system-get-num-bodies physics-system)
          [:pointer Body]))

(comment

  (do

    (def job-system (vj/init))
    (def physics-system (vj/default-physics-system))
    (def body-i (vj/body-interface physics-system))

    ;; Body interface.
    (def floor
      (vj/add-body body-i (vj/BodyCreationSettings
                           {:position (vj/Vector4 [0 -1 0 1])
                            :rotation (vj/Vector4 [0 0 0 1])
                            :shape (-> (vj/box-settings (vj/HalfExtent [100 1 100]))
                                       vj/shape)})))

    (def bodies
      (->> (range 16)
           (mapv (fn [idx]
                   (vj/add-body body-i (vj/BodyCreationSettings
                                        {:position (vj/Vector4 [0 (+ 8 (* idx 1.2)) 8 1])
                                         :rotation (vj/Vector4 [0 0 0 1])
                                         :shape (-> (vj/box-settings (vj/HalfExtent [0.5 0.5 0.5]))
                                                    vj/shape)
                                         :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                                         :object_layer :vj.layer/moving}))))))

    ;; Update.
    (update! job-system physics-system (/ 1.0 60))

    (let [bodies (vj/get-bodies physics-system)]
      (mapv :position bodies)))

  ())
