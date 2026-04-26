(ns vybe.jolt.wasm-c
  (:require
   [clojure.string :as str]
   [camel-snake-kebab.core :as csk]
   [vybe.jolt.abi :as abi]
   [vybe.jolt.wasm :as jolt-wasm]
   [vybe.panama :as panama]
   [vybe.wasm :as vw])
  (:import
   (java.lang.foreign MemorySegment)
   (vybe.panama VybeComponent VybePMap)))

(defonce module*
  (delay
    (jolt-wasm/set-contact-callback-handler!
     (fn [event args]
       (let [callback-id (aget args 0)]
         (if-let [f (vw/callback callback-id)]
           (case event
             :validate
             (long-array [(long (or (f nil nil
                                       (aget args 1)
                                       (aget args 2)
                                       (aget args 3)
                                       (aget args 4))
                                     0))])

             :added
             (do (f nil nil
                    (aget args 1)
                    (aget args 2)
                    (aget args 3)
                    (aget args 4))
                 (vw/empty-result))

             :persisted
             (do (f nil nil
                    (aget args 1)
                    (aget args 2)
                    (aget args 3)
                    (aget args 4))
                 (vw/empty-result))

             :removed
             (do (f nil nil (aget args 1))
                 (vw/empty-result)))
           (throw (ex-info "No registered Jolt Wasm callback"
                           {:event event
                            :callback-id callback-id}))))))
    (jolt-wasm/load-module)))
(defn module [] @module*)
(defn raw-call [name & args] (apply vw/call @module* name args))
(defn alloc [size] (vw/malloc @module* size))
(defn free [ptr] (vw/free @module* ptr))
(defn zero! [ptr size] (vw/zero! @module* ptr size))

(defn- align-ptr
  [ptr alignment]
  (let [rem (mod ptr alignment)]
    (if (zero? rem)
      ptr
      (+ ptr (- alignment rem)))))

(defn- alloc-aligned
  [size alignment]
  (let [base (alloc (+ size alignment))
        ptr (align-ptr base alignment)]
    {:base base :ptr ptr}))

(defn- mem
  [v]
  (cond
    (nil? v) 0
    (number? v) (long v)
    (vw/mem? v) (vw/mem v)
    (instance? MemorySegment v) (.address ^MemorySegment v)
    :else (let [p (panama/mem v)]
            (if (instance? MemorySegment p)
              (.address ^MemorySegment p)
              p))))

(defn- ret-value
  [schema v]
  (case schema
    :void nil
    :boolean (not (zero? (long v)))
    :float (vw/raw-i32->float v)
    :double (vw/raw-i64->double v)
    :uint (Integer/toUnsignedLong (int v))
    v))

(defn- arg-value
  [schema v]
  (case schema
    :float (Float/floatToRawIntBits (float v))
    :double (Double/doubleToRawLongBits (double v))
    :boolean (if v 1 0)
    (:byte :short :int :uint) (long (or v 0))
    (mem v)))

(defn- generated-call
  [c-name args]
  (let [desc (abi/function-data c-name)
        ret-schema (get-in desc [:ret :schema])
        arg-schemas (map :schema (:args desc))]
    (ret-value ret-schema
               (apply raw-call c-name (map arg-value arg-schemas args)))))

(doseq [[c-name _] (:functions (abi/abi))
        :let [clj-name (csk/->kebab-case-symbol c-name)]
        :when (not (ns-resolve *ns* clj-name))]
  (let [v (intern *ns* clj-name (fn [& args] (generated-call c-name args)))
        fn-desc (abi/function-desc c-name)]
    (alter-meta! v assoc
                 :vybe/c-name c-name
                 :vybe/wasm-fn fn-desc
                 :vybe/fn-meta {:fn-desc fn-desc
                                :fn-address 0})))

(defn- vector-ptr
  [v n]
  (let [p (alloc (* n 4))
        values (cond
                 (sequential? v) v
                 (map? v) (map v [:x :y :z :w])
                 :else [])]
    (doseq [idx (range n)]
      (vw/write-f32! @module* (+ p (* idx 4)) (float (or (nth values idx 0) 0.0))))
    p))

(defn- with-vector-ptr
  [v n f]
  (let [p (vector-ptr v n)]
    (try
      (f p)
      (finally
        (free p)))))

(defn- component->wasm
  [pmap]
  (let [component (vw/component pmap)
        ptr (alloc (vw/sizeof component))]
    (zero! ptr (vw/sizeof component))
    (vw/write-component! @module* component ptr (into {} pmap))
    ptr))

(defn- with-component-ptr
  [v f]
  (if (vw/pmap? v)
    (let [p (component->wasm v)]
      (try
        (f p)
        (finally
          (free p))))
    (f (mem v))))

(defn- write-pmap-fields!
  [pmap values]
  (if (instance? VybePMap pmap)
    (let [mem-segment (panama/mem pmap)
          component (vw/component pmap)]
      (doseq [[field value] values
              :let [builder (:builder (get (.fields ^VybeComponent component) field))]
              :when builder]
        (builder mem-segment value))
      pmap)
    (reduce-kv assoc pmap values)))

(defn jpc-box-shape-settings-create
  [half-extent]
  (with-vector-ptr half-extent 3
    #(raw-call "JPC_BoxShapeSettings_Create" %)))

(defn jpc-scaled-shape-settings-create
  [inner-shape-settings scale]
  (with-vector-ptr scale 3
    #(raw-call "JPC_ScaledShapeSettings_Create" (mem inner-shape-settings) %)))

(defn jpc-rotated-translated-shape-settings-create
  [inner-shape-settings rotation translation]
  (with-vector-ptr rotation 4
    (fn [rotation-p]
      (with-vector-ptr translation 3
        (fn [translation-p]
          (raw-call "JPC_RotatedTranslatedShapeSettings_Create"
                    (mem inner-shape-settings)
                    rotation-p
                    translation-p))))))

(defn jpc-physics-system-create
  [max-bodies num-body-mutexes max-body-pairs max-contact-constraints & _filters]
  (raw-call "vybe_jolt_physics_system_create_default"
            max-bodies num-body-mutexes max-body-pairs max-contact-constraints))

(defn jpc-job-system-create
  [max-jobs _max-barriers _num-threads]
  (raw-call "vybe_jolt_job_system_create_single_threaded" max-jobs))

(defn jpc-physics-system-set-contact-listener
  [phys listener]
  (if (vw/null? listener)
    (raw-call "JPC_PhysicsSystem_SetContactListener"
              (mem phys)
              0)
    (let [callbacks (into {} listener)
          listener-p (raw-call "vybe_jolt_contact_listener_create"
                               (int (or (:OnContactValidate callbacks) 0))
                               (int (or (:OnContactAdded callbacks) 0))
                               (int (or (:OnContactPersisted callbacks) 0))
                               (int (or (:OnContactRemoved callbacks) 0)))]
      (raw-call "JPC_PhysicsSystem_SetContactListener"
                (mem phys)
                listener-p))))

(defn jpc-body-interface-create-and-add-body
  [body-interface body-settings activation]
  (with-component-ptr body-settings
    #(raw-call "JPC_BodyInterface_CreateAndAddBody" (mem body-interface) % activation)))

(defn jpc-body-interface-set-linear-velocity
  [body-interface body-id vel]
  (with-vector-ptr vel 3
    #(raw-call "JPC_BodyInterface_SetLinearVelocity" (mem body-interface) body-id %)))

(defn jpc-body-interface-get-linear-velocity
  [body-interface body-id out-vel]
  (with-vector-ptr [0 0 0] 3
    (fn [p]
      (raw-call "JPC_BodyInterface_GetLinearVelocity" (mem body-interface) body-id p)
      (merge out-vel {:x (vw/read-f32 @module* p)
                      :y (vw/read-f32 @module* (+ p 4))
                      :z (vw/read-f32 @module* (+ p 8))}))))

(defn jpc-body-interface-get-position
  [body-interface body-id out-pos]
  (with-vector-ptr [0 0 0] 3
    (fn [p]
      (raw-call "JPC_BodyInterface_GetPosition" (mem body-interface) body-id p)
      (merge out-pos {:x (vw/read-f32 @module* p)
                      :y (vw/read-f32 @module* (+ p 4))
                      :z (vw/read-f32 @module* (+ p 8))}))))

(defn jpc-body-interface-set-position
  [body-interface body-id pos activation]
  (with-vector-ptr pos 3
    #(raw-call "JPC_BodyInterface_SetPosition" (mem body-interface) body-id % activation)))

(defn jpc-body-interface-get-rotation
  [body-interface body-id out-rot]
  (with-vector-ptr [0 0 0 1] 4
    (fn [p]
      (raw-call "JPC_BodyInterface_GetRotation" (mem body-interface) body-id p)
      (merge out-rot {:x (vw/read-f32 @module* p)
                      :y (vw/read-f32 @module* (+ p 4))
                      :z (vw/read-f32 @module* (+ p 8))
                      :w (vw/read-f32 @module* (+ p 12))}))))

(defn jpc-body-interface-set-rotation
  [body-interface body-id rot activation]
  (with-vector-ptr rot 4
    #(raw-call "JPC_BodyInterface_SetRotation" (mem body-interface) body-id % activation)))

(defn jpc-physics-system-get-body-i-ds
  [phys max-body-ids out-num-body-ids out-body-ids]
  (raw-call "JPC_PhysicsSystem_GetBodyIDs" (mem phys) max-body-ids out-num-body-ids out-body-ids))

(defn jpc-physics-system-get-active-body-i-ds
  [phys max-body-ids out-num-body-ids out-body-ids]
  (raw-call "JPC_PhysicsSystem_GetActiveBodyIDs" (mem phys) max-body-ids out-num-body-ids out-body-ids))

(defn jpc-narrow-phase-query-cast-ray
  [query ray hit broad-phase-filter object-layer-filter body-filter]
  (let [ray-component (vw/component ray)
        hit-component (vw/component hit)
        ray-allocation (alloc-aligned (vw/sizeof ray-component) 16)
        hit-allocation (alloc-aligned (vw/sizeof hit-component) 16)
        ray-p (:ptr ray-allocation)
        hit-p (:ptr hit-allocation)]
    (try
      (zero! ray-p (vw/sizeof ray-component))
      (zero! hit-p (vw/sizeof hit-component))
      (vw/write-component! @module* ray-component ray-p (into {} ray))
      (vw/write-component! @module* hit-component hit-p (into {} hit))
      (let [res (not (zero? (raw-call "JPC_NarrowPhaseQuery_CastRay"
                                      (mem query) ray-p hit-p
                                      (mem broad-phase-filter)
                                      (mem object-layer-filter)
                                      (mem body-filter))))]
        (when (vw/pmap? hit)
          (write-pmap-fields! hit
                              (vw/read-component @module* hit-component hit-p)))
        res)
      (finally
        (free (:base ray-allocation))
        (free (:base hit-allocation))))))
