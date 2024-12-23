(ns vybe.jolt
  "Some functions are based on zphysics.zig.

  See a sample at https://github.com/zig-gamedev/zig-gamedev/blob/main/samples/physics_test_wgpu/src/physics_test_wgpu.zig#L321

  Also see https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java#L44"
  (:require
   [vybe.jolt.c :as vj.c]
   [vybe.panama :as vp]
   [vybe.type :as vt]
   [vybe.jolt :as vj]
   [clojure.set :as set]
   [vybe.util :as vy.u])
  (:import
   (org.vybe.jolt jolt
                  JPC_BodyCreationSettings
                  JPC_Body

                  JPC_RRayCast
                  JPC_RayCastResult
                  JPC_RayCastSettings

                  JPC_MotionProperties

                  JPC_ContactListenerVTable
                  JPC_ContactListenerVTable$OnContactValidate
                  JPC_ContactListenerVTable$OnContactValidate$Function
                  JPC_ContactListenerVTable$OnContactAdded
                  JPC_ContactListenerVTable$OnContactAdded$Function
                  JPC_ContactListenerVTable$OnContactRemoved
                  JPC_ContactListenerVTable$OnContactRemoved$Function
                  JPC_ContactListenerVTable$OnContactPersisted
                  JPC_ContactListenerVTable$OnContactPersisted$Function

                  JPC_ContactManifold

                  JPC_CharacterContactListenerVTable

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

;; -- Types.
(vp/defcomp VTable
  {:constructor (fn [v]
                  {:vtable (vp/mem v)})}
  [[:vtable :pointer]])

(vp/defcomp HalfExtent
  vt/Vector3)

(vp/defopaques PhysicsSystem Shape BodyInterface NarrowPhaseQuery ShapeSettings)

(vp/defcomp ContactListenerVTable (JPC_ContactListenerVTable/layout))
(vp/defcomp BroadPhaseLayerInterfaceVTable (JPC_BroadPhaseLayerInterfaceVTable/layout))
(vp/defcomp ObjectVsBroadPhaseLayerFilterVTable (JPC_ObjectVsBroadPhaseLayerFilterVTable/layout))
(vp/defcomp ObjectLayerPairFilterVTable (JPC_ObjectLayerPairFilterVTable/layout))
(vp/defcomp Body (JPC_Body/layout))
(vp/defcomp MotionProperties (JPC_MotionProperties/layout))
(vp/defcomp RayCast (JPC_RRayCast/layout))
(vp/defcomp RayCastSettings (JPC_RayCastSettings/layout))
(vp/defcomp ContactManifold (JPC_ContactManifold/layout))

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
                          :angular_velocity (vt/Vector4)
                          :object_layer (cond
                                          (not object_layer) 0
                                          (keyword? object_layer) (layer->int object_layer)
                                          :else object_layer)
                          :motion_type (jolt/JPC_MOTION_TYPE_STATIC)
                          :allowed_dofs (jolt/JPC_ALLOWED_DOFS_ALL)}
                         (dissoc m :object_layer)))}
  (JPC_BodyCreationSettings/layout))

(vp/defcomp Byte*
  [[:v :byte]])

(vp/defcomp VyBody
  [[:body-interface :pointer]
   [:id :long]])

(vp/defcomp OnContactAdded
  [[:body-1 vj/VyBody]
   [:body-2 vj/VyBody]
   [:contact-manifold vj/ContactManifold]])

;; -- Everything else.
(defonce ^:private *state (atom {}))

;; We can only initialize this once.
(defn init
  "This will be initialized only once. Following calls will have no effect.

  `num-of-threads` will be `(.availableProcessors (Runtime/getRuntime))` if this
  argument is not defined by the caller."
  ([]
   (init {}))
  ([{:keys [num-of-threads]}]
   (or (:job-system @*state)
       (let [_ (do (vj.c/jpc-register-default-allocator)
                   (vj.c/jpc-create-factory)
                   (vj.c/jpc-register-types))

             num-of-threads (min 16 (or num-of-threads (.availableProcessors (Runtime/getRuntime))))

             job-system (vj.c/jpc-job-system-create
                         (jolt/JPC_MAX_PHYSICS_JOBS)
                         (jolt/JPC_MAX_PHYSICS_BARRIERS)
                         num-of-threads)]
         (vy.u/debug "Starting physics job system with " num-of-threads " thread"
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
                   (vp/if-windows?
                     (vp/with-apply JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer
                       [_ _ out-layer layer]
                       (-> (vp/p->map out-layer Byte*)
                           (assoc :v (byte layer))
                           vp/mem))
                     (vp/with-apply JPC_BroadPhaseLayerInterfaceVTable$GetBroadPhaseLayer
                       [_ _ layer]
                       (byte layer))))
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
      ;; Values from https://github.com/jrouwe/JoltPhysics/blob/28783b7cbc85fa7a3472247c3d58b654ef0e1335/HelloWorld/HelloWorld.cpp#L257C109-L257C114.
      65536 0 65536 10240
      broad-phase-layer-interface
      object-vs-broad-phase-layer-interface
      object-layer-pair-filter-interface))))

(defn contact-listener
  "Build and set a contact listener for a physics system.

  See https://jrouwe.github.io/JoltPhysics/class_contact_listener.html
  for the arguments of each `on-...` function.

  Hello world example at https://github.com/jrouwe/JoltPhysics/blob/28783b7cbc85fa7a3472247c3d58b654ef0e1335/HelloWorld/HelloWorld.cpp#L167"
  [phys {:keys [on-contact-validate on-contact-added on-contact-persisted on-contact-removed]}]
  (vp/with-arena-root
    (->> (VTable
          (cond-> (ContactListenerVTable)
            on-contact-validate
            (assoc :OnContactValidate
                   (vp/with-apply JPC_ContactListenerVTable$OnContactValidate
                     [_ _ body-1 body-2 base-offset collision-result]
                     (on-contact-validate body-1 body-2 base-offset collision-result)))

            on-contact-added
            (assoc :OnContactAdded
                   (vp/with-apply JPC_ContactListenerVTable$OnContactAdded
                     [_ _ body-1 body-2 contact-manifold contact-settings]
                     (on-contact-added body-1 body-2 contact-manifold contact-settings)))

            on-contact-persisted
            (assoc :OnContactPersisted
                   (vp/with-apply JPC_ContactListenerVTable$OnContactPersisted
                     [_ _ body-1 body-2 contact-manifold contact-settings]
                     (on-contact-persisted body-1 body-2 contact-manifold contact-settings)))

            on-contact-removed
            (assoc :OnContactRemoved
                   (vp/with-apply JPC_ContactListenerVTable$OnContactRemoved
                     [_ _ sub-shape-pair]
                     (on-contact-removed sub-shape-pair)))))
         (vj.c/jpc-physics-system-set-contact-listener phys))))

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

(defn body-p-valid?
  "Check that body pointer is valid (not freed)."
  [body-p]
  (zero? (bit-and (vp/address body-p) (jolt/_JPC_IS_FREED_BODY_BIT))))

(defn -body-get
  "[AVOID this function in PRD]. Use `bodies` instead.

  Returns the body pointer. But it's not recommended to use this one as there
  is some weirness in Jolt when you read from this pointer, nos sure what's
  happening. "
  [phys body-id]
  (let [body-p (-> (-bodies-unsafe phys)
                 (get (bit-and body-id (jolt/JPC_BODY_ID_INDEX_BITS))))]
    (when (and body-p (body-p-valid? body-p))
      body-p)))

(defn body
  "Constructs a `VyBody`."
  [phys body-id]
  (VyBody {:id body-id :body-interface (body-interface phys)}))

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
                   {:origin (assoc (vt/Vector4 origin-vec3) :w 1)
                    :direction (assoc (vt/Vector4 direction-vec3) :w 0)})
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

(defn shape-settings-params
  "Sets scale, rotation and or translation for a shape settings."
  ([shape-settings scale]
   (vj.c/jpc-scaled-shape-settings-create shape-settings scale))
  ([shape-settings scale translation]
   (-> shape-settings
       (vj.c/jpc-rotated-translated-shape-settings-create (vt/Rotation [0 0 0 1]) translation)
       (vj.c/jpc-scaled-shape-settings-create scale)))
  ([shape-settings scale translation rotation]
   (-> shape-settings
       (vj.c/jpc-rotated-translated-shape-settings-create rotation translation)
       (vj.c/jpc-scaled-shape-settings-create scale))))

(defn make-shape
  [settings]
  (Shape
   (vj.c/jpc-shape-settings-create-shape settings)))

(defn box
  ([half-extent]
   (box half-extent nil))
  ([half-extent scale]
   (let [bs (box-settings half-extent)]
     (if scale
       (make-shape (shape-settings-params bs scale))
       (make-shape bs))))
  ([half-extent scale translation]
   (let [bs (box-settings half-extent)]
     (make-shape (shape-settings-params bs scale translation))))
  ([half-extent scale translation rotation]
   (let [bs (box-settings half-extent)]
     (make-shape (shape-settings-params bs scale translation rotation)))))

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
   (move vy-body position (vt/Vector4 [0 0 0 1]) delta))
  ([vy-body position rotation delta]
   (vj.c/jpc-body-interface-move-kinematic (:body-interface vy-body) (:id vy-body) position rotation (float delta))))

(defn added?
  "Check if body is added."
  [vy-body]
  (vj.c/jpc-body-interface-is-added (:body-interface vy-body) (:id vy-body)))

(defn motion-type
  "Get/set body's motion type."
  ([vy-body]
   (when vy-body
     (vj.c/jpc-body-interface-get-motion-type (:body-interface vy-body) (:id vy-body))))
  ([vy-body motion-type]
   (when vy-body
     (vj.c/jpc-body-interface-set-motion-type (:body-interface vy-body) (:id vy-body)
                                              motion-type
                                              (jolt/JPC_ACTIVATION_ACTIVATE)))))

(defn position
  "Get/set position."
  ([vy-body]
   (when vy-body
     (let [pos (vt/Translation)]
       (vj.c/jpc-body-interface-get-position (:body-interface vy-body) (:id vy-body) pos)
       pos)))
  ([vy-body pos]
   (vj.c/jpc-body-interface-set-position
    (:body-interface vy-body) (:id vy-body) pos (jolt/JPC_ACTIVATION_ACTIVATE))))

(defn rotation
  "Get/set rotation."
  ([vy-body]
   (when vy-body
     (let [rot (vt/Rotation)]
       (vj.c/jpc-body-interface-get-rotation (:body-interface vy-body) (:id vy-body) rot)
       rot)))
  ([vy-body rot]
   (vj.c/jpc-body-interface-set-rotation
    (:body-interface vy-body) (:id vy-body) rot (jolt/JPC_ACTIVATION_ACTIVATE))))

(defn linear-velocity
  "Get/set linear velocity."
  ([vy-body]
   (when vy-body
     (let [vel (vt/Velocity)]
       (vj.c/jpc-body-interface-get-linear-velocity (:body-interface vy-body) (:id vy-body) vel)
       vel)))
  ([vy-body vel]
   (when vy-body
     (vj.c/jpc-body-interface-set-linear-velocity (:body-interface vy-body) (:id vy-body) vel))))

(defn angular-velocity
  "Get/set angular velocity."
  ([vy-body]
   (when vy-body
     (let [vel (vt/Velocity)]
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
  "Get body local bounds (returns a vt/Aabb)."
  [vy-body]
  (when vy-body
    (let [min-v (vt/Vector3)
          max-v (vt/Vector3)]
      (vj.c/jpc-shape-get-local-bounds (shape vy-body) min-v max-v)
      (vt/Aabb {:min min-v
                :max max-v}))))

(defn center-of-mass-transform
  "Get body center of mass transform."
  [vy-body]
  (let [transform (vt/Transform)]
    (vj.c/jpc-body-interface-get-center-of-mass-transform (:body-interface vy-body) (:id vy-body) transform)
    transform))

(defn world-bounds
  "Get body world bounds (returns an vt/Aabb)."
  [vy-body]
  (when vy-body
    (let [min-v (vt/Vector3)
          max-v (vt/Vector3)]
      (vj.c/jpc-shape-get-world-space-bounds (shape vy-body)
                                             (center-of-mass-transform vy-body)
                                             (vt/Vector3 [1 1 1])
                                             min-v
                                             max-v)
      (vt/Aabb {:min min-v
                :max max-v}))))

;; -- Math.
(defn normalize
  "Normalize rotation (quaternion)."
  [v]
  (let [out (vt/Rotation)]
    (vj.c/jpc-vec-4-normalize v out)
    out))

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
