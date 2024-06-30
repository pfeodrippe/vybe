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

(vp/defcomp Aabb
  [[:min Vector3]
   [:max Vector3]])

(vp/defcomp Transform
  [[:m0 :float]
   [:m4 :float]
   [:m8 :float]
   [:m12 :float]
   [:m1 :float]
   [:m5 :float]
   [:m9 :float]
   [:m13 :float]
   [:m2 :float]
   [:m6 :float]
   [:m10 :float]
   [:m14 :float]
   [:m3 :float]
   [:m7 :float]
   [:m11 :float]
   [:m15 :float]])

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

(vp/defcomp VyBody
  [[:body-interface :pointer]
   [:id :long]])

(defonce ^:private *state (atom {}))

(defmacro debug
  ""
  [& strs]
  `(println (str "[Vybe] - " ~@strs)))

;; We can only initialize this once.
(defn init
  "This will be initialized only once. Following calls will have no effect."
  ([]
   (init {}))
  ([{:keys [num-of-threads]
     :or {num-of-threads (.availableProcessors (Runtime/getRuntime))}}]
   (or (:job-system @*state)
       (let [_ (do (vj.c/jpc-register-default-allocator)
                   (vj.c/jpc-create-factory)
                   (vj.c/jpc-register-types))

             num-of-threads (min 16 num-of-threads)

             job-system (vj.c/jpc-job-system-create
                         (jolt/JPC_MAX_PHYSICS_JOBS)
                         (jolt/JPC_MAX_PHYSICS_BARRIERS)
                         num-of-threads)]
         (debug "Starting physics job system with " num-of-threads " thread"
                (if (> num-of-threads 1)
                  "s"
                  "")
                " (max of 16 threads)."
                " Following calls to `vybe.jolt/init` will return the cached job system.")
         (swap! *state assoc :job-system job-system)
         job-system))))

;; See https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44

;; -- Physics system.
(defn physics-system
  "Gets a new physics system, calls `init` if not already done."
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
   (vj.c/jpc-physics-system-get-body-interface-no-lock phys)))

(defn optimize-broad-phase
  [phys]
  (vj.c/jpc-physics-system-optimize-broad-phase phys))

(defn -bodies
  "[AVOID this function in PRD]. See `-body-get` docs."
  [phys]
  (let [bodies-count (vj.c/jpc-physics-system-get-num-bodies phys)
        out-bodies (vp/arr bodies-count :pointer)]
    (vj.c/jpc-physics-system-get-bodies phys out-bodies)
    (vp/arr (vp/mem out-bodies) bodies-count [:pointer Body])))

(defn -bodies-unsafe
  "[AVOID this function in PRD]. See `-body-get` docs."
  [phys]
  (-> (vj.c/jpc-physics-system-get-bodies-unsafe phys)
      (vp/arr (vj.c/jpc-physics-system-get-num-bodies phys) [:pointer Body])))

(defn -body-get
  "[AVOID this function in PRD]. Use `bodies` instead.

  Returns the body pointer. But it's not recommended to use this one as there
  is some weirness in Jolt when you read from this pointer, nos sure what's
  happening. "
  [phys body-id]
  (let [body (-> (-bodies-unsafe phys)
                 (get (bit-and body-id (jolt/JPC_BODY_ID_INDEX_BITS))))]
    (when (and body (zero? (bit-and (vp/address body) (jolt/_JPC_IS_FREED_BODY_BIT))))
      body)))

(defn bodies
  "Returns a seq of `VyBody`s or `nil` if bodies count is 0."
  [phys]
  (let [bodies-count (vj.c/jpc-physics-system-get-num-bodies phys)]
    (when (pos? bodies-count)
      (let [out-body-ids (vp/arr bodies-count :int)
            body-i (body-interface phys)]
        (vj.c/jpc-physics-system-get-body-i-ds phys bodies-count (vp/int* 0) out-body-ids)
        (map (fn [id]
               (VyBody {:id id :body-interface body-i}))
             out-body-ids)))))

(defn bodies-active
  "Returns a seq of `VyBody`s which are active or `nil` if active bodies count is 0."
  [phys]
  (let [bodies-count (vj.c/jpc-physics-system-get-num-active-bodies phys)]
    (when (pos? bodies-count)
      (let [out-body-ids (vp/arr bodies-count :int)
            body-i (body-interface phys)]
        (vj.c/jpc-physics-system-get-active-body-i-ds phys bodies-count (vp/int* 0) out-body-ids)
        (map (fn [id]
               (VyBody {:id id :body-interface body-i}))
             out-body-ids)))))

(defn narrow-phase-query
  [phys]
  (NarrowPhaseQuery
   (vj.c/jpc-physics-system-get-narrow-phase-query-no-lock phys)))

;; -- Query.
(defn cast-ray
  "Returns the VyBody of the affected body (or sub shape id), `nil` otherwise."
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
         (VyBody {:id (:body_id hit)
                  :body-interface (body-interface phys)}))))))

;; -- Shape.
(defn box-settings
  [half-extent]
  (ShapeSettings
   (vj.c/jpc-box-shape-settings-create half-extent)))

(defn shape-scale
  [shape-settings scale]
  (vj.c/jpc-scaled-shape-settings-create shape-settings scale))

(defn make-shape
  [settings]
  (Shape
   (vj.c/jpc-shape-settings-create-shape settings)))

(defn box
  ([half-extent]
   (box half-extent nil))
  ([half-extent scale]
   (cond-> (box-settings half-extent)
     scale (shape-scale scale)
     true make-shape)))

;; -- Body interface
(defn body-add
  "Creates and adds a body, returns a VyBody."
  ([phys body-settings]
   (body-add phys body-settings (jolt/JPC_ACTIVATION_ACTIVATE)))
  ([phys body-settings activation]
   (let [body-i (body-interface phys)
         id (vj.c/jpc-body-interface-create-and-add-body body-i body-settings activation)]
     (VyBody {:id id
              :body-interface (body-interface phys)}))))

;; -- Body
(defn remove*
  "Removes and destroys the body."
  [vy-body]
  (let [body-i (:body-interface vy-body)
        id (:id vy-body)]
    (vj.c/jpc-body-interface-remove-body body-i id)
    (vj.c/jpc-body-interface-destroy-body body-i id)))

(defn activate
  [vy-body]
  (let [body-i (:body-interface vy-body)
        id (:id vy-body)]
    (vj.c/jpc-body-interface-activate-body body-i id)))

(defn active?
  "Is body active?"
  [vy-body]
  (vj.c/jpc-body-interface-is-active (:body-interface vy-body) (:id vy-body)))

(defn move
  "Move kinematic body.

  `position` should be a vec3 (Translation).
  `rotation` should be a normalized vec4 (Rotation)."
  ([vy-body position delta]
   (move vy-body position (Vector4 [0 0 0 1]) delta))
  ([vy-body position rotation delta]
   (vj.c/jpc-body-interface-move-kinematic (:body-interface vy-body) (:id vy-body) position rotation (float delta))))

(defn added?
  "Check if body is added."
  [vy-body]
  (vj.c/jpc-body-interface-is-added (:body-interface vy-body) (:id vy-body)))

(defn position
  "Get/set position."
  ([vy-body]
   (when vy-body
     (let [pos (Vector3)]
       (vj.c/jpc-body-interface-get-position (:body-interface vy-body) (:id vy-body) pos)
       pos)))
  ([vy-body pos]
   (vj.c/jpc-body-interface-set-position
    (:body-interface vy-body) (:id vy-body) pos (jolt/JPC_ACTIVATION_ACTIVATE))))

(defn rotation
  "Get/set rotation."
  ([vy-body]
   (when vy-body
     (let [rot (Vector4)]
       (vj.c/jpc-body-interface-get-rotation (:body-interface vy-body) (:id vy-body) rot)
       rot)))
  ([vy-body rot]
   (vj.c/jpc-body-interface-set-rotation
    (:body-interface vy-body) (:id vy-body) rot (jolt/JPC_ACTIVATION_ACTIVATE))))

(defn linear-velocity
  "Get/set linear velocity."
  ([vy-body]
   (when vy-body
     (let [vel (Vector3)]
       (vj.c/jpc-body-interface-get-linear-velocity (:body-interface vy-body) (:id vy-body) vel)
       vel)))
  ([vy-body vel]
   (when vy-body
     (vj.c/jpc-body-interface-set-linear-velocity (:body-interface vy-body) (:id vy-body) vel))))

(defn angular-velocity
  "Get/set angular velocity."
  ([vy-body]
   (when vy-body
     (let [vel (Vector3)]
       (vj.c/jpc-body-interface-get-angular-velocity (:body-interface vy-body) (:id vy-body) vel)
       vel)))
  ([vy-body vel]
   (when vy-body
     (vj.c/jpc-body-interface-set-angular-velocity (:body-interface vy-body) (:id vy-body) vel))))

(defn shape
  "Get body shape."
  [vy-body]
  (vj.c/jpc-body-interface-get-shape (:body-interface vy-body) (:id vy-body)))

(defn local-bounds
  "Get body local bounds (returns an Aabb)."
  [vy-body]
  (when vy-body
    (let [min-v (Vector3)
          max-v (Vector3)]
      (vj.c/jpc-shape-get-local-bounds (shape vy-body) min-v max-v)
      (Aabb {:min min-v
             :max max-v}))))

(defn center-of-mass-transform
  "Get body center of mass transform."
  [vy-body]
  (let [transform (Transform)]
    (vj.c/jpc-body-interface-get-center-of-mass-transform (:body-interface vy-body) (:id vy-body) transform)
    transform))

(defn world-bounds
  "Get body world bounds (returns an Aabb)."
  [vy-body]
  (when vy-body
    (let [min-v (Vector3)
          max-v (Vector3)]
      (vj.c/jpc-shape-get-world-space-bounds (shape vy-body)
                                             (center-of-mass-transform vy-body)
                                             (Vector3 [1 1 1])
                                             min-v
                                             max-v)
      (Aabb {:min min-v
             :max max-v}))))

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
