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

                  JPC_RRayCast
                  JPC_RayCastResult
                  JPC_RayCastSettings

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

#_(import 'org.vybe.jolt.JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer)

(def layer->int
  {:vj.layer/non-moving 0
   :vj.layer/moving 1})

(def int->layer
  (set/map-invert layer->int))

(vp/defcomp VTable
  {:constructor (fn [v]
                  {:vtable (vp/mem v)})}
  [[:vtable :pointer]])

(vp/defcomp HalfExtent
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Vector2
  [[:x :float]
   [:y :float]])

(vp/defcomp Vector3
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Vector4
  [[:x :float]
   [:y :float]
   [:z :float]
   [:w :float]])

(vp/defopaques PhysicsSystem Shape BodyInterface NarrowPhaseQuery ShapeSettings)

(vp/defcomp BroadPhaseLayerInterfaceVTable (JPC_BroadPhaseLayerInterfaceVTable/layout))
(vp/defcomp ObjectVsBroadPhaseLayerFilterVTable (JPC_ObjectVsBroadPhaseLayerFilterVTable/layout))
(vp/defcomp ObjectLayerPairFilterVTable (JPC_ObjectLayerPairFilterVTable/layout))
(vp/defcomp Body (JPC_Body/layout))
(vp/defcomp RayCast (JPC_RRayCast/layout))
(vp/defcomp RayCastSettings (JPC_RayCastSettings/layout))

(vp/defcomp RayCastResult
  {:constructor (fn [m]
                  (merge {:body_id (jolt/JPC_BODY_ID_INVALID)
                          :fraction (+ 1 (jolt/JPC_FLT_EPSILON))}
                         m))}
  (JPC_RayCastResult/layout))

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
      #_(min 16 (.availableProcessors (Runtime/getRuntime)))
      1))))

;; See https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44

;; -- Physics system.
(defn physics-system
  []
  (init)

  (let [broad-phase-layer-interface
        (-> (BroadPhaseLayerInterfaceVTable)
            (assoc :GetNumBroadPhaseLayers
                   (vp/with-apply JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers
                     [_ _]
                     2)

                   :GetBroadPhaseLayer
                   (vp/with-apply JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer
                     [_ _ layer]
                     (byte layer)))
            VTable)

        object-vs-broad-phase-layer-interface
        (-> (ObjectVsBroadPhaseLayerFilterVTable)
            (assoc :ShouldCollide
                   (vp/with-apply JPC_ObjectVsBroadPhaseLayerFilterVTable$ShouldCollide
                     [_ _ layer1 layer2]
                     (case (int->layer layer1)
                       :vj.layer/non-moving (= (int->layer layer2) :vj.layer/moving)
                       :vj.layer/moving true
                       false)))
            VTable)

        object-layer-pair-filter-interface
        (-> (ObjectLayerPairFilterVTable)
            (assoc :ShouldCollide
                   (vp/with-apply JPC_ObjectLayerPairFilterVTable$ShouldCollide
                     [_ _ layer1 layer2]
                     (case (int->layer layer1)
                       :vj.layer/non-moving (= (int->layer layer2) :vj.layer/moving)
                       :vj.layer/moving true
                       false)))
            VTable)]

    (PhysicsSystem
     (vj.c/jpc-physics-system-create
      1024 0 1024 1024
      broad-phase-layer-interface
      object-vs-broad-phase-layer-interface
      object-layer-pair-filter-interface))))

(defn body-interface
  [phys]
  (BodyInterface
   (vj.c/jpc-physics-system-get-body-interface phys)))

(defn optimize-broad-phase
  [phys]
  (vj.c/jpc-physics-system-optimize-broad-phase phys))

(defn bodies
  [phys]
  (let [bodies-count (vj.c/jpc-physics-system-get-num-bodies phys)
        out-bodies (vp/arr bodies-count :pointer)]
    (vj.c/jpc-physics-system-get-bodies phys out-bodies)
    (vp/arr (vp/mem out-bodies) bodies-count [:pointer Body])))

(defn bodies-unsafe
  [phys]
  (-> (vj.c/jpc-physics-system-get-bodies-unsafe phys)
      (vp/arr (vj.c/jpc-physics-system-get-num-bodies phys) [:pointer Body])))

(defn body-ids
  [phys]
  (let [bodies-count (vj.c/jpc-physics-system-get-num-bodies phys)
        out-body-ids (vp/arr bodies-count :int)]
    (vj.c/jpc-physics-system-get-body-i-ds phys bodies-count (vp/int* 0) out-body-ids)
    out-body-ids))

(defn narrow-phase-query
  [phys]
  (NarrowPhaseQuery
   (vj.c/jpc-physics-system-get-narrow-phase-query phys)))

(defn body-get
  [phys body-id]
  (-> (bodies-unsafe phys)
      (get (bit-and body-id (jolt/JPC_BODY_ID_INDEX_BITS)))))

;; -- Query.
(defn cast-ray
  ([phys origin-vec3 direction-vec3]
   (cast-ray phys origin-vec3 direction-vec3 {}))
  ([phys origin-vec3 direction-vec3 {:keys [original]}]
   (let [ray-cast (vj/RayCast
                   {:origin (assoc (Vector4 origin-vec3) :w 1)
                    :direction (assoc (Vector4 direction-vec3) :w 0)})
         hit (RayCastResult)
         has-hit (vj.c/jpc-narrow-phase-query-cast-ray (narrow-phase-query phys) ray-cast hit vp/null vp/null vp/null)]
     (when has-hit
       (if original
         hit
         (body-get phys (:body_id hit)))))))

;; -- Shape.
(defn box-settings
  [half-extent]
  (ShapeSettings
   (vj.c/jpc-box-shape-settings-create half-extent)))

(defn shape-scale
  [shape-settings scale]
  (vj.c/jpc-scaled-shape-settings-create shape-settings scale))

(defn shape
  [settings]
  (Shape
   (vj.c/jpc-shape-settings-create-shape settings)))

(defn box
  ([half-extent]
   (box half-extent nil))
  ([half-extent scale]
   (cond-> (box-settings half-extent)
     scale (shape-scale scale)
     true shape)))

;; -- Body interface
(defn body-add
  ([phys body-settings]
   (body-add phys body-settings (jolt/JPC_ACTIVATION_ACTIVATE)))
  ([phys body-settings activation]
   (vj.c/jpc-body-interface-create-and-add-body (vj/body-interface phys) body-settings activation)))

(defn body-remove
  "Will remove and destroy the body."
  [phys body-id]
  (let [body-i (vj/body-interface phys)]
    (vj.c/jpc-body-interface-remove-body body-i body-id)
    (vj.c/jpc-body-interface-destroy-body body-i body-id)))

(defn body-activate
  [phys body-id]
  (let [body-i (vj/body-interface phys)]
    (vj.c/jpc-body-interface-activate-body body-i body-id)))

;; -- Body
(defn body-active?
  [body]
  (vj.c/jpc-body-is-active body))

;; -- Misc
(defonce *temp-allocator
  (delay (vj.c/jpc-temp-allocator-create (* 16 1024 1024))))

(defn update!
  ([phys delta-time]
   (update! (init) phys delta-time 1 1 @*temp-allocator))
  ([job-system phys delta-time]
   (update! job-system phys delta-time 1 1 @*temp-allocator))
  ([job-system phys delta-time collision-steps integration-sub-steps allocator]
   (let [res (vj.c/jpc-physics-system-update phys
                                             delta-time
                                             collision-steps
                                             integration-sub-steps
                                             allocator
                                             job-system)]
     (if (= res (jolt/JPC_PHYSICS_UPDATE_NO_ERROR))
       res
       (throw (ex-info "An error while update Physics was running"
                       {:res res}))))))

#_(vp/mem (BodyInterface))

(comment

  (do

    (def job-system (vj/init))
    (def phys (vj/physics-system))

    ;; Body interface.
    (vj/body-add phys (vj/BodyCreationSettings
                       {:position (vj/Vector4 [0 -1 0 1])
                        :rotation (vj/Vector4 [0 0 0 1])
                        :shape (vj/box (vj/HalfExtent [100 1 100]))}))

    (->> (range 16)
         (mapv (fn [idx]
                 (vj/body-add phys (vj/BodyCreationSettings
                                    {:position (vj/Vector4 [0 (+ 8 (* idx 1.2)) 8 1])
                                     :rotation (vj/Vector4 [0 0 0 1])
                                     :shape (vj/box (vj/HalfExtent [0.5 0.5 0.5]))
                                     :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                                     :object_layer :vj.layer/moving})))))

    ;; Update.
    (update! phys (/ 1.0 60))

    (let [bodies (vj/bodies phys)]
      (mapv :position bodies)))

  ())
