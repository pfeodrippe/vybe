(ns vybe.flecs
  {:clj-kondo/ignore [:unused-value]}
  (:refer-clojure :exclude [ref alias])
  (:require
   [clojure.pprint :as pp]
   [clojure.set :as set]
   [clojure.string :as str]
   [meta-merge.core :as meta-merge]
   [potemkin :refer [def-map-type deftype+]]
   [vybe.c :as vc]
   [vybe.flecs :as vf]
   [vybe.flecs.abi :as abi]
   [vybe.flecs.c :as vf.c]
   [vybe.flecs.ids :as flecs]
   [vybe.wasm :as vp]
   [vybe.util :as vy.u])
  (:import
   (vybe.panama VybeComponent VybePMap IVybeWithComponent IVybeWithPMap IVybeMemorySegment)
   (java.lang.foreign AddressLayout MemoryLayout$PathElement MemoryLayout
                      ValueLayout ValueLayout$OfDouble ValueLayout$OfLong
                      ValueLayout$OfInt ValueLayout$OfBoolean ValueLayout$OfFloat
                      ValueLayout$OfByte ValueLayout$OfShort
                      StructLayout MemorySegment PaddingLayout SequenceLayout
                      UnionLayout FunctionDescriptor Linker SegmentAllocator)
   (java.lang.invoke MethodHandles MethodHandle)))

;; -- Flecs types
(vp/defcomp ecs_type_t (abi/layout :ecs_type_t))
(vp/defcomp ecs_type_info_t (abi/layout :ecs_type_info_t))
(vp/defcomp ecs_observer_desc_t (abi/layout :ecs_observer_desc_t))

(vp/defcomp observer_t (abi/layout :ecs_observer_t))
(vp/defcomp iter_t (abi/layout :ecs_iter_t))
(vp/defcomp query_desc_t (abi/layout :ecs_query_desc_t))
(vp/defcomp app_desc_t (abi/layout :ecs_app_desc_t))
(vp/defcomp event_desc_t (abi/layout :ecs_event_desc_t))
(vp/defcomp system_t (abi/layout :ecs_system_t))
(vp/defcomp system_stats_t (abi/layout :ecs_system_stats_t))
(vp/defcomp query_t (abi/layout :ecs_query_t))
(vp/defcomp ref_t (abi/layout :ecs_ref_t))
(vp/defcomp os_api_t (abi/layout :ecs_os_api_t))
(vp/defcomp system_desc_t (abi/layout :ecs_system_desc_t))
(vp/defcomp entity_desc_t (abi/layout :ecs_entity_desc_t))

(vp/defcomp DocDescription (abi/layout :EcsDocDescription))
(vp/defcomp Identifier (abi/layout :EcsIdentifier))
(vp/defcomp Rest (abi/layout :EcsRest))
(vp/defcomp EcsComponent (abi/layout :EcsComponent))

(def ecs_iter_action_t :ecs_iter_action_t)
(def ecs_os_api_log_t :ecs_os_api_log_t)
(def ecs_os_api_abort_t :ecs_os_api_abort_t)

(set! *warn-on-reflection* true)

(defprotocol IVybeName
  (vybe-name [e]))

(defn- -vybe-name
  [e]
  (let [v (if (or (int? e)
                  (keyword? e))
            e
            (vybe-name e))]
    v))

(def path
  "Builds path of entities (usually keywords), returns a string.

  E.g.

     (vf/path [:my/model :vg.gltf/alphabet :vg.gltf/A])"
  (memoize
   (fn [ks]
     (->> ks
          (mapv (fn [v]
                  (vybe-name v)))
          (str/join ".")))))

(defn long-callback
  "Create a native upcall stub for a zero-arity function that returns a long.

  Arguments:
    - f : a zero-arity Clojure function returning a number that fits in a
      Java long.

  Behaviour:
    - Allocates a foreign upcall stub in the default arena and returns the
      `MemorySegment` pointing at the upcall stub. The returned segment may be
      stored in native structures and invoked from C (Flecs) code.

  Safety notes:
    - The provided function will be reified into an interface and bound via
      MethodHandles; avoid capturing large mutable state in hot paths.
    - The memory segment is allocated in the default arena — callers must
      ensure the arena/lifetime semantics match native usage to avoid use-
      after-free.
  "
  [f]
  (let [desc (FunctionDescriptor/of
              ValueLayout/JAVA_LONG
              (into-array MemoryLayout []))
        linker (Linker/nativeLinker)
        f (do (definterface LongCallback2
                (^long apply []))
              (eval `(reify ~'LongCallback2
                       (apply [_]
                         (~f)))))
        mh (-> (MethodHandles/lookup)
               (.findVirtual (class f) "apply" (.toMethodType desc)))
        linker-options (into-array java.lang.foreign.Linker$Option [])
        mem-segment (.upcallStub linker (.bindTo mh f) desc (vp/default-arena) linker-options)]
    mem-segment
    #_(-> (.downcallHandle linker desc linker-options)
          (.invokeWithArguments (into-array Object [mem-segment]))
          ;; TODO https://github.com/IGJoshua/coffi/blob/master/src/clj/coffi/ffi.clj#L176
          #_(.invokeExact ^MemorySegment* (into-array MemorySegment [mem-segment])))))
#_ (long-callback (fn [] 410))

(defn run-long-callback
  "Invoke a `MemorySegment` upcall stub previously created by `long-callback`.

  Arguments:
    - mem-segment : a `MemorySegment` returned from `long-callback`.

  Returns the 64-bit integer result produced by the native upcall.

  Note: this is a thin convenience wrapper around the low-level downcall
  handle creation; prefer memoizing/keeping a downcall handle for hot paths
  instead of recreating it on each invocation." 
  [^MemorySegment mem-segment]
  (let [desc (FunctionDescriptor/of
              ValueLayout/JAVA_LONG
              (into-array MemoryLayout []))
        linker (Linker/nativeLinker)
        linker-options (into-array java.lang.foreign.Linker$Option [])]
    (-> (.downcallHandle linker desc linker-options)
        (.invokeWithArguments ^"[Ljava.lang.Object;" (into-array Object [mem-segment]))
        ;; TODO https://github.com/IGJoshua/coffi/blob/master/src/clj/coffi/ffi.clj#L176
        #_(.invokeExact ^MemorySegment* (into-array MemorySegment [mem-segment])))))
#_(run-long-callback (long-callback (fn [] 410)))

#_ (-to-c (ecs_entity_desc_t/layout))
#_ (-to-c (ecs_iter_t/layout) {:world {:constructor identity}})

(defn- -adapt-name
  [v]
  (or ({:ChildOf :vf/child-of}
       v)
      v))

(def -flecs->vybe
  (memoize
   (fn [s]
     (if (keyword? s)
       s
       (if (str/includes? s ".")
         (list `path
               (->> (str/split s #"\.")
                    (mapv -flecs->vybe)))
         (let [parsed (-> s
                          (str/replace #"_DOT_" ".")
                          (str/replace #"_SLASH_" "/")
                          (str/replace #"_DASH_" "-"))]
           (if (str/starts-with? s "C_")
             (-> (subs parsed 2)
                 symbol)
             (-> parsed
                 keyword
                 -adapt-name))))))))
#_ (-flecs->vybe "s_DOT_f_DASH_f.al")
#_ (-flecs->vybe (vybe-name Position))

(declare eid)

#_(vp/defcomp Position
  [[:x :double]
   [:y :double]])

#_ (ex-1)

(def docs
  "Builtin documentation."
  {:flecs/tags
   {:vf/child-of
    {:doc "Maps to EcsChildOf"}

    :vf/is-a
    {:doc "Maps to EcsIsA"}

    :vf/prefab
    {:doc "Maps to EcsPrefab"}

    :vf/trait
    {:doc "Maps to EcsTrait"}

    :vf/exclusive
    {:doc "Maps to EcsExclusive"}

    :vf/disabled
    {:doc "Maps to EcsDisabled"}

    :*
    {:doc "Maps to EcsWildcard, the `*` symbol that allows you to query for all of the elements in a pair, for example"}

    :_
    {:doc "Maps to EcsAny, the `_` symbol that allows you to query for only one element in a pair (in contrast with `*`), for example"}

    :vf/unique
    {:doc "Component trait that lets will force the usage of a component to only one entity"}

    :vf/print-disabled
    {:doc "Disables print of this component when printing an entity"}}

   :flecs/query-config
   {:vf/name
    {:doc "Required in systems or observers, it may be a keyword or string (including the result of `vf/path`)"
     :examples '[[:vf/name :my-system]]}

    :vf/disabled
    {:doc "Disables a system or observer if set to true"
     :examples '[[:vf/name :my-observer
                  :vf/disabled true]]}

    :vf/always
    {:doc "Only for systems, it will make the system run every time, independently if there was a change or not"
     :examples '[[:vf/name :my-system
                  :vf/always true]]}

    :vf/phase
    {:doc "Only for systems, the phase of the system, it defaults to EcsOnUpdate"
     :examples '[[:vf/name :my-system
                  :vf/always true]]}

    :vf/immediate
    {:doc "Only for systems, if true, it ensures that system will not be in readonly mode"
     :examples '[[:vf/name :my-system
                  :vf/immediate true]]}

    :vf/events
    {:doc "Only for observers, you can pass a list of built-in (`:add`, `:set`, `:remove`) or custom events"
     :examples '[[:vf/name :my-observer
                  :vf/events #{:set}]]}

    :vf/yield-existing
    {:doc "Only for observers, will cause entities that match the query to be triggered (take care with this one in the REPL as re-creation will trigger the observer!)"
     :examples '[[:vf/name :my-observer
                  :vf/yield-existing true]]}}

   :flecs/query-special
   {:vf/entity
    {:doc "Fetches the entity (`VybeFlecsEntitySet`) associated with the match"
     :examples '[[e :vf/entity]
                 [e [:vf/entity :c]]]}

    :vf/eid
    {:doc "Fetches the entity id (a long) associated with the match"
     :examples '[[e :vf/eid]
                 [e [:vf/eid :c]]]}

    :vf/iter
    {:doc "Fetches the iter (`iter_t`) associated with the match"
     :examples '[[it :vf/iter]]}

    :vf/event
    {:doc "Only for observers, fetches the event associated with the match"
     :examples '[[ev :vf/event]]}}

   :flecs/query-terms
   {:or
    {:doc "Maps to EcsOr"
     :examples '[[c [:or :c1 :c2]]]}

    :not
    {:doc "Maps to EcsNot"
     :examples '[[c [:not :c]]]}

    :maybe
    {:doc "Maps to EcsOptional"
     :examples '[[c [:maybe :c]]]}

    :meta
    {:doc "Use this so you can set flags directly in Flecs, it's low-level"
     :examples '[[c [:meta {:term {:src {:id 521}}} :c]]]}

    :in
    {:doc "Maps to EcsIn, by default, all components are input, so you don't necessarily need to use this"
     :examples '[[c [:in my-component]]]}

    :out
    {:doc "Maps to EcsOut"
     :examples '[[c [:out my-component]]]}

    :inout
    {:doc "Maps to EcsInOut"
     :examples '[[c [:inout my-component]]]}

    :inout-filter
    {:doc "Maps to EcsInOutFilter"
     :examples '[[c [:inout-filter my-component]]]}

    :filter
    {:doc "Maps to EcsInOutFilter, same as `:inout-filter`"
     :examples '[[c [:filter my-component]]]}

    :none
    {:doc "Maps to EcsNone"
     :examples '[[c [:none my-component]]]}

    :mut
    {:doc "Maps to EcsInOut, same as `:inout`"
     :examples '[[c [:mut my-component]]]}

    :src
    {:doc "Receives a fixed or variable source, you can match anything with it"
     :examples '[[c [:src :my-entity my-component]]
                 [c1 [:src '?e my-component-1]
                  c2 [:src '?e my-component-2]]]}}})

;; -- Main API.
(declare -set-c)
(declare -remove-c)
(declare -init)
(declare -get-c)

#_(defcomp VybeFlecsLongCallback
    [[:callback {:constructor long-callback} :pointer]])

(defn -init
  "Init a new world."
  []
  (vf.c/ecs-init))

(vp/defcomp VybeComponentId
  [[:id :long]])

(def builtin-entities
  {:vf/child-of (flecs/EcsChildOf)
   :vf/slot-of (flecs/EcsSlotOf)
   :vf/is-a (flecs/EcsIsA)
   :vf/prefab (flecs/EcsPrefab)
   :vf/union :vf.unsupported/union
   :vf/exclusive (flecs/EcsExclusive)
   :vf/trait (flecs/EcsTrait)
   :vf/disabled (flecs/EcsDisabled)
   :vf/component (flecs/FLECS_IDEcsComponentID_)
   :* (flecs/EcsWildcard)
   :_ (flecs/EcsAny)})

(def builtin-entities-rev
  (set/map-invert builtin-entities))

(defn- -world-entities
  [wptr]
  (let [e ::entity
        it (vf.c/ecs-each-id wptr (eid wptr e))
        *acc (transient [])]
    (while (vf.c/ecs-each-next it)
      (mapv (fn [^long idx]
              (let [e-id (.getAtIndex ^MemorySegment (:entities it)
                                      ValueLayout/JAVA_LONG
                                      idx)]
                (when-let [v (vp/comp-cache (:id (-get-c wptr e-id VybeComponentId)))]
                  (conj! *acc v))))
            (range (:count it))))
    (persistent! *acc)))
#_(let [wptr (vf/-init)]
    (vf/-set-c wptr :bob [:walking (Position {:x 10512 :y -4}) [:vf/child-of :a]])
    (-world-entities wptr))

(declare get-internal-path)

(def ^:private skip-meta
  #{::entity
    :vybe.flecs.type/component
    :vybe.flecs.type/keyword
    :vybe.flecs.type/pair
    VybeComponentId
    `VybeComponentId})

(defn- ->comp-rep
  [wptr v]
  (or (get builtin-entities-rev v)
      (vp/comp-cache (:id (-get-c wptr v VybeComponentId)))
      (let [n (-flecs->vybe (get-internal-path wptr (eid wptr v)))]
        (when-not (contains? (conj skip-meta :Identifier :Name :Symbol :Component)
                             n)
          n))))

(def on-instantiate-inherit-id
  (vf.c/vybe-pair (flecs/EcsOnInstantiate) (flecs/EcsInherit)))

(def ^:private doc-description-name-id
  (vf.c/vybe-pair (flecs/FLECS_IDEcsDocDescriptionID_) (flecs/EcsName)))

(def ^:private ecs-component-id (flecs/FLECS_IDEcsComponentID_))

(defn- -entity-components
  [wptr e-id]
  (let [type-ptr (vf.c/ecs-get-type wptr e-id)
        {:keys [count]} (vp/p->map type-ptr ecs_type_t)]
    (->> (range count)
         (keep (fn [^long idx]
                 (let [c-id (vf.c/ecs-type-id-at type-ptr idx)
                       *c-cache (delay (->comp-rep wptr c-id))]
                   (cond
                     ;; Exclude from printing.
                     (contains? #{on-instantiate-inherit-id doc-description-name-id
                                  ecs-component-id}
                                c-id)
                     nil

                     (vf.c/ecs-id-is-pair c-id)
                     (let [[a b] [(->comp-rep wptr (vf.c/vybe-pair-first wptr c-id))
                                  (->comp-rep wptr (vf.c/vybe-pair-second wptr c-id))]]
                       (when (some some? [a b])
                         [(or a (vf.c/vybe-pair-first wptr c-id))
                          (or b (vf.c/vybe-pair-second wptr c-id)) ]))

                     #_(instance? IVybeWithComponent @*c-cache)
                     #_(-get-c wptr e-id @*c-cache)

                     (instance? VybeComponent @*c-cache)
                     (-get-c wptr e-id @*c-cache)

                     :else
                     @*c-cache))))
         (remove nil?)
         set)))
#_ (let [wptr (vf/-init)
         c vybe.type/Translation]
     (vf/-set-c wptr :bob [:walking
                           (c {:x 10512 :y -4})
                           [:a :b]
                           [:vf/child-of :x]])
     (-entity-components wptr (eid wptr :bob)))

(defn- -ecs-pair
  "Receives two ids (numbers) and returns the id of the pair."
  [id-1 id-2]
  (vf.c/vybe-pair id-1 id-2))

;; -- Types.
(definterface IVybeFlecsEntityMap
  (^long id []))

(declare vybe-flecs-world-map-rep)
(declare ent)

(def-map-type VybeFlecsWorldMap [-wptr mta]
  (get [this e default-value]
       (or (cond
             (nil? e)
             default-value

             (int? e)
             (ent this e)

             :else
             (let [e-id (vf.c/ecs-lookup-symbol this (vybe-name e) true false)]
               (if (not= e-id 0)
                 (ent this e-id)
                 default-value)))
           default-value))
  (assoc [this k v]
         #_(println :WORLD k v)
         (if (= v :vf.op/del)
           (dissoc this k)
           (-set-c this k v))
         this)
  (dissoc [this k]
          (when (get this k)
            (vf.c/ecs-delete this (eid this k)))
          this)
  (keys [this] (-world-entities this))
  (keySet [this] (set (potemkin.collections/keys* this)))
  (meta [_] mta)
  (with-meta [this mta]
    (VybeFlecsWorldMap. -wptr mta))

  IVybeMemorySegment
  (mem_segment [_] (MemorySegment/ofAddress (long -wptr)))

  clojure.lang.IPersistentCollection
  (equiv [this x]
         (and (instance? VybeFlecsWorldMap x)
              (= -wptr (vp/mem x))))

  Object
  (toString [this] (str (vybe-flecs-world-map-rep this))))
#_ (let [w (vf/make-world)]
     (assoc w :b :fff)
     (merge w {:af [[:a :b]]})
     w

     #_(-> (vf.c/ecs-type-str w
                              (vf.c/ecs-get-type w (.id (get w [:a :b]))))
           vp/->string)

     #_(-> w
           (assoc :b :cccc)
           (merge {:fff [:gg]
                   ;; DONE Here we are having that [:b :a] has :b...
                   ;;      It's just the way that flecs is.
                   :g [[:b :a]]})
           #_(dissoc :fff)
           (assoc :fff [:ffa (Position {:x -1 :y 13})])
           #_(dissoc :ffa)
           #_(assoc-in [:fff :ffa] (Position {:x 910 :y 911}))
           #_(get-in [:fff Position])
           #_(get-in [:fff Position])))
#_(reset! pt/type-bodies {})

(defn- vybe-flecs-world-map-rep
  [^VybeFlecsWorldMap this]
  ;; Remove empty entities so we don't clutter the output.
  (->> this
       (remove (if (-> this meta :show-all)
                 (constantly false)
                 (comp (fn [e]
                         (or (not (seq e))
                             (:vf/print-disabled e)))
                       val)))
       (into {})))

(defmethod print-method VybeFlecsWorldMap
  [^VybeFlecsWorldMap o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybeFlecsWorldMap
  [^VybeFlecsWorldMap o]
  (pp/simple-dispatch (vybe-flecs-world-map-rep o)))

(declare -setup-world)

(defn make-world
  "Create a fully initialized `VybeFlecsWorldMap`.

  Arity notes:
    - [] : create a fresh world and run `-setup-world`.
    - [mta] : attach `mta` as metadata to the world map prior to setup.
    - [wptr mta] : wrap an existing Flecs world pointer and still run
      `-setup-world` so helper observers/components are registered.

  Metadata options (keys users commonly set on `mta`):
    - :debug - truthy or a function used when pretty-printing entities; if a
      function it's applied as an adapter to printed representations.
    - :show-all - when true the pretty-printer includes empty entities in
      outputs; otherwise empty entities are filtered from printing.

  The metadata is stored as map metadata on the returned `VybeFlecsWorldMap`
  and used for diagnostic / pretty-printing behaviour."
  (^VybeFlecsWorldMap []
   (make-world {}))
  (^VybeFlecsWorldMap [mta]
   (-setup-world
    (VybeFlecsWorldMap. (-init) mta)))
  (^VybeFlecsWorldMap [wptr mta]
   (-setup-world
    (VybeFlecsWorldMap. wptr mta))))
#_ (vf/make-world)

(defmacro with-world
  "Bind a short-lived Flecs world to the symbol `w-sym` for the scope of `body`.

  Arguments:
    - w-sym : a symbol that will be bound to a fresh `VybeFlecsWorldMap` for
      the duration of `body`.
    - body  : one or more forms evaluated with `w-sym` in scope.

  Behavior:
    - Calls `make-world` and binds the result to `w-sym`.
    - Ensures `vf.c/ecs-fini` is invoked in a `finally` block so native
      resources are released even if `body` throws.

  Typical usage is in tests or REPL experiments where you need an isolated
  world. Returns whatever `body` returns. This macro does not run any
  additional setup beyond what `make-world` performs."
  [w-sym & body]
  `(let [~w-sym (make-world)]
     (try
       ~@body
       (finally
         (vf.c/ecs-fini ~w-sym)))))

;; Used internally for iterations.
(defn -make-world
  (^VybeFlecsWorldMap []
   (-make-world {}))
  (^VybeFlecsWorldMap [mta]
   (VybeFlecsWorldMap. (-init) mta))
  (^VybeFlecsWorldMap [wptr mta]
   (VybeFlecsWorldMap. wptr mta)))

(defmacro -with-world
  "Like `with-world` but uses an uninitialized world created by
  `-make-world` (no additional `-setup-world` registration).

  Arguments:
    - w-sym : symbol bound to an uninitialized world (`-make-world`).
    - body  : forms executed while the world is bound.

  Use this when you want a very minimal Flecs world for unit tests or when
  you want full control over registration and initialization order. As with
  `with-world`, `vf.c/ecs-fini` is guaranteed to be called in a `finally`
  block. Returns the value of `body`."
  [w-sym & body]
  `(let [~w-sym (-make-world)]
     (try
       ~@body
       (finally
         (vf.c/ecs-fini ~w-sym)))))

(definterface IVybeFlecsWorldMap
  (^vybe.flecs.VybeFlecsWorldMap w []))

(declare vybe-flecs-entity-set-rep)
(declare get-name)

(deftype+ VybeFlecsEntitySet [-w -id]
  clojure.lang.IPersistentSet
  (seq [this]
    (seq (-entity-components (.w this) (.id this))))
  (cons [this c]
    (assoc (.w this) this [c])
    this)
  (get [this c]
    (if (= c :vf/id)
      (.id this)
      (-get-c (.w this) (.id this) c)))
  (disjoin [this c]
    (let [w (.w this)]
      (vf.c/ecs-remove-id w (.id this) (eid w c)))
    this)
  #_(next [_] (CustomSeq. (next s)))
  #_(more [this]
      (let [n (next this)]
        (if (empty? n)
          '()
          n)))

  clojure.lang.Counted
  (count [this]
    (:count (vp/p->value
             (vf.c/ecs-get-type (.w this) (.id this))
             ecs_type_t)))

  clojure.lang.Associative
  (assoc [this c v]
    (assoc (.w this) this (c v))
    this)
  (entryAt [this c]
    (.get this c))
  (containsKey [this k]
    (boolean (get this k)))

  clojure.lang.IFn
  (invoke [this c]
    (get this c))

  clojure.lang.ILookup
  (valAt [this c] (.get this c))
  (valAt [this c default] (potemkin.collections/get* this c default))

  clojure.lang.IPersistentCollection
  (equiv [this x]
    (and (instance? VybeFlecsEntitySet x)
         (= (.w this) (.w ^VybeFlecsEntitySet x))
         (= (.id this) (.id ^VybeFlecsEntitySet x))))

  IVybeFlecsEntityMap
  (id [_] -id)

  IVybeFlecsWorldMap
  (w [this] -w)

  Object
  (toString [this] (str (vybe-flecs-entity-set-rep this))))
#_ (let [w (vf/make-world)
         aaa (:csa w)]
     (-> (VybeFlecsEntitySet. w (eid w :vvv))
         (conj :a)
         (disj :a)
         (conj (Position {:x 11}))
         #_(disj Position))
     (get-in w [:vvv Position])
     #_(update w :vvv disj Position)
     #_(conj aaa (Position {:x 10}))
     w)

(declare children)
(declare get-internal-name)

(defn- vybe-flecs-entity-set-rep
  [^VybeFlecsEntitySet this]
  (if-let [debug (:debug (meta (.w this)))]
    (let [adapter (if (fn? debug)
                    debug
                    identity)]
      (adapter
       (merge {:vf/id (.id this)
               :vf/name (get-internal-path (.w this) (.id this))
               :vf/value (-entity-components (.w this) (.id this))}
              (when-let [e-children (seq (children this))]
                {:vf/children (vec e-children)}))))
    (cond-> (-entity-components (.w this) (.id this))
      (seq (children this))
      (conj (->> (children this)
                 (map (fn [^VybeFlecsEntitySet v]
                        [(-flecs->vybe (get-internal-name v)) v]))
                 (into {}))))))

(defmethod print-method VybeFlecsEntitySet
  [^VybeFlecsEntitySet o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybeFlecsEntitySet
  [^VybeFlecsEntitySet o]
  (pp/simple-dispatch (vybe-flecs-entity-set-rep o)))

(defn ent
  "Wrap an entity designator in a `VybeFlecsEntitySet`.

  Accepts a `w` value (either a `VybeFlecsWorldMap` or a raw world pointer
  wrapper) and an entity designator `e`. The `e` argument supports the same
  forms `eid` accepts, including:
    - keywords (e.g. `:player`)
    - strings (Flecs symbol names)
    - numeric ids (raw long)
    - vectors describing pairs/paths (e.g. `[:parent :child]`)
    - component references / component instances (types wrapped by Vybe)
    - existing `VybeFlecsEntitySet` instances

  The function resolves `e` to a numeric Flecs id using `eid` and returns a
  set-like wrapper that supports lookup and associative operations used by the
  rest of the API." 
  ^VybeFlecsEntitySet [w e]
  (VybeFlecsEntitySet. w (eid w e)))
#_ (vf/make-entity (vf/make-world) :a)

(defn entity?
  "Return true when `v` is already a `VybeFlecsEntitySet` wrapper.

  Helpful for guarding code paths that can accept either raw ids or fully
  wrapped entity handles."
  [v]
  (instance? VybeFlecsEntitySet v))

(defn entity-get-id
  "Extract the raw Flecs id from a `VybeFlecsEntitySet` without additional lookups."
  [^VybeFlecsEntitySet v]
  (.id v))

(defn pair?
  "Return true when the designator resolves to a Flecs pair id.

  Accepts either a `VybeFlecsEntitySet` or a numeric id and delegates to
  `ecs-id-is-pair`."
  [e]
  (if (entity? e)
    (vf.c/ecs-id-is-pair (entity-get-id e))
    (vf.c/ecs-id-is-pair e)))

(defn pair-first
  "Fetch the relationship (left) entity for a pair."
  ([^VybeFlecsEntitySet em]
   (pair-first (.w em) (.id em)))
  ([w e]
   (vf/ent w (vf.c/vybe-pair-first w e))))

(defn pair-second
  "Fetch the target (right) entity for a pair."
  ([^VybeFlecsEntitySet em]
   (pair-second (.w em) (.id em)))
  ([w e]
   (vf/ent w (vf.c/vybe-pair-second w e))))

(defn target
  "Lookup the `idx`th target entity reached from `e` through relationship `rel`.

  Supports both the `VybeFlecsEntitySet` arity and the raw world/id arity. When
  the relationship lookup succeeds, returns a wrapped entity via `ent`; returns
  nil when Flecs reports no matching target."
  ([^VybeFlecsEntitySet em rel]
   (target em rel 0))
  ([^VybeFlecsEntitySet em rel idx]
   (when em
     (target (.w em) (.id em) rel idx)))
  ([w e rel idx]
   (let [id (vf.c/ecs-get-target w (vf/eid w e) (vf/eid w rel) idx)]
     (when (pos? id)
       (vf/ent w id)))))

(defonce *name-cache (atom {}))

(extend-protocol IVybeName
  #_ #_clojure.lang.Var
  (vybe-name [v]
    (str "V_" (-> v
                  symbol
                  str
                  (str/replace #"\." "_"))))
  Long
  (vybe-name [v]
    (str "#" (-> (unchecked-int v)
                 (bit-shift-left 32)
                 (unsigned-bit-shift-right 32))))

  clojure.lang.Keyword
  (vybe-name [k]
    (or (get @*name-cache k)
        (let [s (-> (symbol k)
                    str
                    (str/replace #"\." "_DOT_")
                    (str/replace #"/" "_SLASH_")
                    (str/replace #"-" "_DASH_"))]
          (swap! *name-cache assoc k s)
          s)))

  clojure.lang.PersistentVector
  (vybe-name [v]
    (path v))

  VybeComponent
  (vybe-name [v]
    (name v))

  IVybeWithComponent
  (vybe-name [v]
    (vybe-name (.component v)))

  String
  (vybe-name [s]
    s)

  VybeFlecsEntitySet
  (vybe-name [s]
    (vybe-name (.id s)))

  #_ #_clojure.lang.Symbol
  (vybe-name [sym]
    (str "S_" (-> sym
                  (str/replace #"\." "_")))))

;; -- Operators.
(defn- -add-meta
  [wptr e e-id type-k]
  (when-not (skip-meta e)
    (let [comp-id (vp/comp-cache e)]
      (when (nil? comp-id)
        (throw (ex-info "Component is not in the cache, this shouldn't be happening!!"
                        {:e e
                         :e-id e-id})))
      (-set-c wptr e-id [type-k (VybeComponentId comp-id)]))))

(defonce ^:private *ent-cache (atom {}))

(defn- -cache-entity
  [wptr e e-id]
  (let [wptr (vp/mem wptr)]
    (swap! *ent-cache
           (fn [v]
             (-> v
                 (assoc-in [wptr e] e-id)
                 (assoc-in [wptr e-id] e))))))

(defonce ^:private *world->cache (atom {}))

(defn valid?
  "Return truthy when the entity designator still resolves to a valid handle.

  Accepts either a `VybeFlecsEntitySet` or anything `eid` can resolve and uses
  `ecs-is-valid` without creating missing entities. Useful for guard clauses
  around cached ids."
  ([^VybeFlecsEntitySet em]
   (valid? (.w em) (.id em)))
  ([w e]
   (some->> (vf/eid w e {:create-entity false}) (vf.c/ecs-is-valid w ))))

(defn alive?
  "Return truthy when the entity has not been deleted and is considered alive by Flecs.

  Similar to `valid?`, but delegates to `ecs-is-alive`, ensuring the id refers to
  a living entity (not a tombstone)."
  ([^VybeFlecsEntitySet em]
   (alive? (.w em) (.id em)))
  ([w e]
   (some->> (vf/eid w e {:create-entity false}) (vf.c/ecs-is-alive w))))

(defn eid
  "Resolve an entity designator into its numeric id, optionally creating the entity.

  Handles keywords, strings, components, pairs, raw longs, and `VybeFlecsEntitySet`
  instances. The `create-entity` option (default true) controls whether missing
  entities are created on the fly. Returns the raw long Flecs id; use `ent` when
  you need the richer wrapper."
  ([^VybeFlecsEntitySet em]
   (.id em))
  ([wptr e]
   (eid wptr e {}))
  ([wptr e {:keys [create-entity]
            :or {create-entity true}
            :as opts}]
   #_ (when-not (int? e)
        (println :e3 e))
   (let [id
         (cond
           (int? e)
           e

           (instance? VybeFlecsEntitySet e)
           (.id ^VybeFlecsEntitySet e)

           (instance? IVybeWithComponent e)
           (eid wptr (.component ^IVybeWithComponent e) opts)

           (vector? e)
           (-ecs-pair (eid wptr (first e) opts)
                      (eid wptr (second e) opts))

           :else
           (or (if (or (keyword? e) (string? e))
                 (let [e-id (vf.c/ecs-lookup-symbol wptr (vybe-name e) true false)]
                   (when-not (zero? e-id)
                     e-id))

                 (when-let [id (get-in @*world->cache [(vp/mem wptr) e])]
                   (when (alive? wptr id)
                     id)))

               (when create-entity
                 (let [#_ #__ (println :___ENT e)
                       e-id (cond
                              (instance? VybeComponent e)
                              (let [^MemoryLayout layout (.layout ^VybeComponent e)
                                    name (vybe-name e)
                                    edesc (vp/jx-i {:id 0
                                                    :name name
                                                    :symbol name
                                                    :use_low_id true}
                                                   ecs_entity_desc_t)
                                    e-id (vf.c/ecs-entity-init wptr edesc)
                                    desc (vp/jx-i {:entity e-id
                                                   :type (vp/jx-i {:size (.byteSize layout)
                                                                   :alignment (.byteAlignment layout)}
                                                                  ecs_type_info_t)}
                                                  ecs_component_desc_t)
                                    _id (vf.c/ecs-component-init wptr desc)]
                                (-add-meta wptr e e-id :vybe.flecs.type/component)
                                (-cache-entity wptr e e-id)
                                (vf.c/ecs-doc-set-name wptr e-id (vp/comp-name e))
                                #_(-set-c wptr e-id [on-instantiate-inherit-id])
                                e-id)

                              (string? e)
                              (let [sym (vybe-name e)
                                    e-id (vf.c/ecs-lookup-symbol wptr sym true false)]
                                (if (not= e-id 0)
                                  e-id
                                  (let [id (vf.c/ecs-set-name wptr 0 sym)]
                                    (when (zero? id)
                                      (throw (ex-info "`eid` would return `0` (from string ecs-set-name)"
                                                      {:e e
                                                       :opts opts})))
                                    #_(vf.c/ecs-set-symbol wptr id sym)
                                    #_(vp/cache-comp e)
                                    #_(-add-meta wptr e id :vybe.flecs.type/keyword)
                                    (-cache-entity wptr e id)
                                    #_(-set-c wptr id [on-instantiate-inherit-id])
                                    id)))

                              (keyword? e)
                              (or (let [v (get builtin-entities e)]
                                    (when (keyword? v)
                                      (throw (ex-info "Attribute not supported anymore, check Flecs breaking changes at https://github.com/SanderMertens/flecs/discussions/466"
                                                      {:e e})))
                                    v)
                                  (let [sym (vybe-name e)
                                        e-id (vf.c/ecs-lookup-symbol wptr sym true false)]
                                    (if (not= e-id 0)
                                      e-id
                                      (let [id (vf.c/ecs-set-name wptr 0 sym)]
                                        (vf.c/ecs-doc-set-name wptr id (str e))
                                        (when (zero? id)
                                          (throw (ex-info "`eid` would return `0` (from keyword ecs-set-name)"
                                                          {:e e
                                                           :opts opts})))
                                        #_(vf.c/ecs-set-symbol wptr id sym)
                                        (vp/cache-comp e)
                                        (-add-meta wptr e id :vybe.flecs.type/keyword)
                                        (-cache-entity wptr e id)
                                        #_(-set-c wptr id [on-instantiate-inherit-id])
                                        id))))

                              :else
                              (throw (ex-info "Unsupported entity type" {:type (type e)
                                                                         :value e})))]
                   (when-not (skip-meta e)
                     (vf.c/ecs-add-id wptr e-id (eid wptr ::entity)))
                   (swap! *world->cache assoc-in [(vp/mem wptr) e] e-id)
                   e-id))))]
     (if create-entity
       (do (when (zero? id)
             (throw (ex-info "`eid` would return `0`"
                             {:e e
                              :opts opts})))
           id)
       id))))
#_ (let [wptr (vf/-init)]
     [(vf/eid wptr :a)
      (vf/eid wptr :b)
      (Position {:x 10})])

(defn alias!
  "Assign or update the Flecs alias for an entity.

  Usage forms:
    - (alias! em n)    ; em is a `VybeFlecsEntitySet`
    - (alias! w e n)   ; w is the world, e an entity designator

  The alias `n` is converted via `vybe-name` (so keywords, strings, and
  component-like symbols are supported) and stored in Flecs as the entity's
  alias. This is the imperative equivalent of the data-driven `(alias n)`
  descriptor used when merging maps into a world." 
  ([^VybeFlecsEntitySet em n]
   (alias! (.w em) (.id em) n))
  ([w e n]
   (vybe.flecs.c/ecs-set-alias w (vf/eid w e) (vybe-name n))))

;; -- Low-level only.
(defn -override
  [wptr e]
  (bit-or (flecs/ECS_AUTO_OVERRIDE) (eid wptr (-vybe-name e))))

(defn -set-c
  [wptr e coll]
  (let [e-id (eid wptr (-vybe-name e))]
    (mapv (fn [v]
            (cond
              (instance? VybePMap v)
              (let [^VybePMap v v
                    ^MemorySegment mem-segment (.mem_segment v)]
                (vf.c/ecs-set-id wptr e-id (eid wptr (.component v))
                                 (.byteSize mem-segment)
                                 mem-segment))

              (instance? IVybeWithPMap v)
              (let [#_ #__ (def v v)
                    ^VybePMap v (.pmap ^IVybeWithPMap v)
                    ^MemorySegment mem-segment (.mem_segment v)]
                (vf.c/ecs-set-id wptr e-id (eid wptr (.component v))
                                 (.byteSize mem-segment)
                                 mem-segment))

              (and (vector? v) (vp/pmap? (first v)))
              (let [^VybePMap v' (first v)
                    ^MemorySegment mem-segment (.mem_segment v')]
                (vf.c/ecs-set-id wptr e-id (eid wptr v)
                                 (.byteSize mem-segment)
                                 mem-segment))

              (and (vector? v) (vp/pmap? (peek v)))
              (let [^VybePMap v (peek v)
                    ^MemorySegment mem-segment (.mem_segment v)]
                (vf.c/ecs-set-id wptr e-id (eid wptr v)
                                 (.byteSize mem-segment)
                                 mem-segment))

              (map? v)
              (cond
                (:vf.op/override v)
                (do (-set-c wptr e-id (-override wptr (:vf.op/override v)))
                    (-set-c wptr e-id (:vf.op/override v)))

                #_(:vf.op/ref v)
                #_(let [c (:component v)]
                    (-set-c wptr e-id
                            (if (vector? c)
                              ;; TODO Handle other cases.
                              [(vp/clone (get-in wptr [(:vf.op/ref v) c])) (last c)]
                              (vp/clone (get-in wptr [(:vf.op/ref v) c])))))

                (:vf.op/del v)
                (-remove-c wptr e-id [(:vf.op/del v)])

                (:vf.op/sym v)
                (vf.c/ecs-set-symbol wptr e-id (:vf.op/sym v))

                (:vf.op/alias v)
                (alias! wptr e-id (:vf.op/alias v))

                :else
                ;; Child of hash map syntax.
                (mapv (fn [[nested-entity nested-components]]
                        (-set-c wptr (path [e-id nested-entity])
                                (conj (vec (cond
                                             (nil? nested-components)
                                             []

                                             (sequential? nested-components)
                                             nested-components

                                             :else
                                             [nested-components])))))
                      v))

              :else
              (let [c-id (eid wptr v)]
                (vf.c/ecs-add-id wptr e-id c-id)
                c-id)))
          (->> (if (sequential? coll)
                 coll
                 [coll])
               (remove nil?)))))

(defn -remove-c
  [wptr e coll]
  (let [e-id (eid wptr (-vybe-name e))]
    (mapv (fn [v]
            (vf.c/ecs-remove-id wptr e-id (eid wptr v)))
          (if (sequential? coll)
            coll
            [coll]))))

(defn -get-c
  [w e c]
  (let [e-id (eid w (-vybe-name e))
        c-id (eid w c)]
    (when (vf.c/ecs-has-id w e-id c-id)
      (cond
        (vf.c/ecs-id-is-wildcard c-id)
        (let [table (vf.c/ecs-get-table w e-id)
              table-type (-> (vf.c/ecs-table-get-type table)
                             (vp/p->map ecs_type_t))
              ids (vp/arr (:array table-type) (:count table-type) :long)]
          (loop [cur (vf.c/ecs-search-offset w table 0 c-id vp/null)
                 acc []]
            (if (not= cur -1)
              (let [n-id (nth ids cur)]
                (recur (vf.c/ecs-search-offset w table (inc cur) c-id vp/null)
                       (conj acc (if (vf.c/ecs-id-is-tag w n-id)
                                   [(vf.c/vybe-pair-first w n-id)
                                    (vf.c/vybe-pair-second w n-id)]
                                   (-get-c w e-id n-id)))))
              acc)))

        (vf.c/ecs-id-is-tag w c-id)
        c

        :else
        (-> (vf.c/ecs-get-id w e-id c-id)
            (vp/p->map (if (vector? c)
                         (if (instance? VybeComponent (first c))
                           (first c)
                           (last c))
                         (cond
                           (instance? IVybeWithComponent c)
                           (.component ^IVybeWithComponent c)

                           (instance? VybeComponent c)
                           c

                           (vf.c/ecs-id-is-pair c-id)
                           (vp/comp-cache
                            (:id (-get-c w (vf.c/vybe-pair-first w c-id) VybeComponentId)))

                           :else
                           (vp/comp-cache c-id))))
            vp/->with-pmap)))))

(comment

  (let [w (vf/make-world)]
    (vf/-set-c w :bob [:walking
                       (Position {:x 10512 :y -4})
                       [(Position {:x 10512 :y -10}) :global]
                       [(Position {:x 555 :y -40}) :global-2]
                       [:a :b]
                       [:a :c]])
    [(vf/-get-c w :bob Position)
     (vf/-get-c w :bob [Position :global])

     (vf/-get-c w :bob [Position :*])
     (get-in w [:bob [Position :*]])

     (vf/-get-c w :bob [:a :*])
     (get-in w [:bob [:a :*]])
     (get-in w [:bob [:a :_]])])

  ())

;; -- High-level.
(defn override
  "Mark a component value so child entities auto-override it when instanced.

  Returns the data-driven descriptor understood by `merge`/`assoc` world
  updates. See Flecs automatic overriding docs:
  https://www.flecs.dev/flecs/md_docs_2Manual.html#automatic-overriding."
  [c]
  {:vf.op/override c})

(defn del
  "Return a data-driven deletion descriptor.

  With no args, signals that the whole entity should be removed when the map is
  merged. With one arg, removes the specific component id/tag from the target
  entity."
  ([]
   :vf.op/del)
  ([c]
   {:vf.op/del c}))

(defn sym
  "Return a descriptor that sets the low-level symbol for an entity.

  Useful when interoperating with C code expecting a stable symbol distinct
  from the human-friendly name."
  [n]
  {:vf.op/sym n})

(defn alias
  "Return a descriptor that sets an alias for an entity via world merges.

  Acts as the data equivalent of `alias!`, handy when building up entity data
  literals."
  [n]
  {:vf.op/alias n})

(defn is-a
  "Construct an inheritance pair using `:vf/is-a` and the relationship target.

  The target is normalized via `-vybe-name`, matching Flecs expectations."
  [e]
  [:vf/is-a (-vybe-name e)])

(defn child-of
  "Construct a `:vf/child-of` pair pointing to the given parent entity."
  [e]
  [:vf/child-of (-vybe-name e)])

(defn slot-of
  "Construct a `:vf/slot-of` pair for slot relationships."
  [e]
  [:vf/slot-of (-vybe-name e)])

#_(declare vybe-flecs-ref-rep)

(defn modified!
  "Notify Flecs that a component on `e` changed out-of-band.

  Call this after mutating memory returned by `ref`/`vp/p->map` so systems that
  watch for changes are triggered. No-op if the entity does not currently own
  the component."
  ([^VybeFlecsEntitySet em c]
   (modified! (.w em) (.id em) c))
  ([w e c]
   (let [e-id (vf/eid w e)
         c-id (vf/eid w c)]
     (when (vf.c/ecs-has-id w e-id c-id)
       (vf.c/ecs-modified-id w e-id c-id)))))

(defn ref-get
  "Resolve an `ecs_ref_t` into the current component value as a Vybe map/struct.

  Arguments:
    - w : world pointer (`VybeFlecsWorldMap` or native world)
    - ref-pmap : the `Ref`-style wrapper returned by `ref` (contains the native
      `ecs_ref_t` instance)
    - c : the component type (Vybe component descriptor used by `vp/p->map`)

  Returns the materialized component data (via `vp/p->map`). This is a read
  helper; if you mutate the returned structure in-place call `modified!` to
  notify Flecs about the change so listeners/systems will see the update." 
  [w ref-pmap c]
  (-> (vf.c/ecs-ref-get-id w ref-pmap (:id ref-pmap))
      (vp/p->map c)))

(vp/defcomp Ref
  {:vp/deref (fn [{:keys [w flecs_ref vybe_component_id]}]
               (ref-get w flecs_ref (vp/comp-cache vybe_component_id)))}
  [[:flecs_ref ref_t]
   [:vybe_component_id :long]
   [:w :pointer]])

(defn ref
  "Create a cached `ecs_ref_t` for repeatedly reading or mutating a component.

  Forms:
    - (ref em c)      ; em is a `VybeFlecsEntitySet`
    - (ref w e c)     ; w is a world and e an entity designator

  Returns a `Ref` record whose deref returns the current component value (as
  a Vybe struct/map). The `Ref` caches the low-level `ecs_ref_t` so repeated
  accesses avoid a full lookup. If you mutate memory obtained via `ref` or
  `vp/p->map`, call `modified!` afterwards to notify Flecs of changes so
  dependent systems will be triggered." 
  ([^VybeFlecsEntitySet em c]
   (ref (.w em) (.id em) c))
  ([w e c]
   (Ref {:flecs_ref (vf.c/ecs-ref-init-id w (vf/eid w e) (vf/eid w c))
         :vybe_component_id (:id (-get-c w c VybeComponentId))
         :w w})))

(defn get-internal-name
  "Return the Flecs-internal short name for `e` (no hierarchy)."
  ([^VybeFlecsEntitySet em]
   (get-internal-name (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-get-name wptr (eid wptr e))
       vp/->string)))

(defn get-internal-path
  "Return the fully qualified Flecs path string for `e` using dot separators."
  ([^VybeFlecsEntitySet em]
   (when em
     (get-internal-path (.w em) (.id em))))
  ([wptr e]
   (-> (vf.c/ecs-get-path-w-sep wptr 0 (eid wptr e) "." (flecs/NULL))
       vp/->string)))

(defn get-name
  "Return the human-friendly name for an entity, falling back to the path."
  ([^VybeFlecsEntitySet em]
   (when em
     (get-name (.w em) (.id em))))
  ([w e]
   (get-internal-path w e)))

(defn get-rep
  "Return the canonical Vybe representation for an entity.

  Single-level names become keywords; hierarchical entities produce the
  `(vybe.flecs/path [...])` form that can be eval'd back into a path."
  ([^VybeFlecsEntitySet em]
   (when em
     (get-rep (.w em) (.id em))))
  ([w e]
   (when e
     (-> (get-internal-path w e)
         -flecs->vybe))))

(defn get-path
  "Return the Vybe path vector that identifies the entity within its world."
  ([^VybeFlecsEntitySet em]
   (when em
     (get-path (.w em) (.id em))))
  ([w e]
   (when e
     (-> (get-internal-path w e)
         -flecs->vybe
         last))))

(defn get-symbol
  "Retrieve the raw Flecs symbol associated with an entity."
  ([^VybeFlecsEntitySet em]
   (get-symbol (.w em) (.id em)))
  ([w e]
   (vp/->string (vf.c/ecs-get-symbol w (vf/eid w e)))))

(defn lookup-symbol
  "Lookup an entity by its exact Flecs symbol name.

  Returns a `VybeFlecsEntitySet` when present or nil otherwise."
  [w n]
  (let [e-id (vf.c/ecs-lookup-symbol w n false false)]
    (when (pos? e-id)
      (vf/ent w e-id))))

(defn type-str
  "Return the Flecs canonical type string for the entity.

  Forms:
    - (type-str em)   ; em is a `VybeFlecsEntitySet`
    - (type-str w e)  ; w is a world, e an entity designator

  The returned string reflects the native Flecs type (the component ids
  present on the entity) and is useful for debugging or constructing
  low-level queries." 
  ([^VybeFlecsEntitySet em]
   (type-str (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-type-str wptr (vf.c/ecs-get-type wptr (eid wptr e)))
       vp/->string)))

(defn children-ids
  "Return a vector of numeric child entity ids for `e`.

  Forms:
    - (children-ids em)  ; em is a `VybeFlecsEntitySet`
    - (children-ids w e) ; w is a world, e an entity designator

  The function iterates the Flecs children iterator and returns a vector of
  raw long ids. Use `children` to receive wrapped `VybeFlecsEntitySet`
  instances instead." 
  ([^VybeFlecsEntitySet em]
   (when em
     (children-ids (.w em) (.id em))))
  ([w e]
   (when e
     (let [it (vf.c/ecs-children w (eid w e))]
       (loop [acc []
              has-next? (vf.c/ecs-children-next it)]
         (if has-next?
           (recur (concat acc (mapv #(.getAtIndex ^MemorySegment (:entities it)
                                                  ValueLayout/JAVA_LONG
                                                  ^long %)
                                    (range (:count it))))
                  (vf.c/ecs-children-next it))
           acc))))))

(defn children
  "Return child entities of `e` as `VybeFlecsEntitySet` wrappers.

  Forms:
    - (children em)
    - (children w e)

  This is a convenience over `children-ids` that wraps each numeric id into an
  `VybeFlecsEntitySet` via `ent`. Useful when you want to inspect components
  or use the entity in higher-level APIs." 
  ([^VybeFlecsEntitySet em]
   (children (.w em) (.id em)))
  ([w e]
   (->> (children-ids w e)
        (mapv #(ent w %)))))

(defn parent-id
  "Return the numeric parent id for `e`, or nil when `e` is root.

  Forms:
    - (parent-id em)
    - (parent-id w e)

  The function avoids creating missing entities (`:create-entity false`) when
  resolving `e` and returns `nil` for root nodes." 
  ([^VybeFlecsEntitySet em]
   (parent-id (.w em) (.id em)))
  ([w e]
   (when-let [e-id (eid w e {:create-entity false})]
     (let [id (vf.c/ecs-get-parent w e-id)]
       (when-not (zero? id)
         id)))))

(defn parent
  "Return the parent entity for `e` as a `VybeFlecsEntitySet`, if any.

  Forms:
    - (parent em)
    - (parent w e)

  Returns `nil` when `e` is root or the parent does not exist." 
  ([^VybeFlecsEntitySet em]
   (parent (.w em) (.id em)))
  ([w e]
   (some->> (parent-id w e) (ent w))))

(defn hierarchy
  "Return a nested map describing the entity's descendant hierarchy.

  The returned map has Vybe representations (`get-rep`) as keys and nested
  subtrees as values. Useful for inspection and REPL debugging when you need a
  programmatic view of the entity tree.

  Forms:
    - (hierarchy em)
    - (hierarchy w e)

  Note: component data is not included; this function only reflects the
  parent/child relationships." 
  ([^VybeFlecsEntitySet em]
   (hierarchy (.w em) (.id em)))
  ([w e]
   (let [cs (children w (eid w e))]
     (->> (mapv (fn [e]
                  (let [h (hierarchy e)
                        n (get-rep e)]
                    [n h]))
                cs)
          (into {})))))

(defn hierarchy-no-path
  "Like `hierarchy` but use local (short) names as keys.

  This is a convenience for REPL inspection where full dotted paths are too
  verbose; keys in the result are the local names produced by
  `(-> e get-internal-name -flecs->vybe)`.

  Forms:
    - (hierarchy-no-path em)
    - (hierarchy-no-path w e)
  "
  ([^VybeFlecsEntitySet em]
   (hierarchy-no-path (.w em) (.id em)))
  ([w e]
   (let [cs (children w (eid w e))]
     (->> (mapv (fn [e]
                  (let [h (hierarchy-no-path e)
                        n (-> e get-internal-name -flecs->vybe)]
                    [n h]))
                cs)
          (into {})))))

(defn singleton!
  "Register a component value as a world-level singleton.

  Forms:
    - (singleton! w v)

  The function merges the component value into the world under its component
  key so it can be retrieved via `singleton`. This is a convenience to express
  intent when a single, global instance of a component is required." 
  [w v]
  (merge w {(vp/component v) [v]}))

(defn singleton
  "Retrieve the singleton instance for component `c` from the world map.

  Returns the registered value or nil when none is present. This expects the
  singleton was registered with `singleton!` (or merged directly into the
  world as `{c [value]}`)." 
  [w c]
  (get-in w [c c]))

(defn get-world
  "Construct a `VybeFlecsWorldMap` from a native world-like pointer (world,
  stage, or query).

  This is useful when working with APIs that return low-level pointers and you
  want the higher-level Vybe wrapper that supports map-like operations and
  pretty-printing.

  Example:
    (get-world some-native-stage)
  "
  ^VybeFlecsWorldMap [poly]
  (-make-world (vf.c/ecs-get-world poly) {}))

(def -parser-special-keywords
  #{:or :not :maybe :pair :meta :entity :query
    :in :out :inout :none :inout-filter :filter :mut :vf/entity
    :notify :sync :src})

(defn -pair-id
  "Get id of the pair."
  [wptr c1 c2]
  (eid wptr [c1 c2]))

(defn -parse-query-expr
  "Internal: parse a high-level query expression into the low-level term list
  and auxiliary metadata used to build Flecs query/filter/rule descriptors.

  Supported expression shapes and special keywords:
    - Component symbols (e.g. `Translation`) or pair vectors (`[Position :global]`).
    - :or, :not — boolean combinators for grouping clauses.
    - :maybe — marks a term as optional (maps to EcsOptional).
    - :pair — explicit pair constructor when needed.
    - :meta — embed metadata for a term (used for :src and other custom options).
    - :src — specify a source entity for a term (can be a symbol or entity id).
    - :scope — open/close a query scope (translates to EcsScopeOpen/EcsScopeClose).
    - Access modifiers: :in, :out, :inout, :inout-filter, :filter, :mut, :none
      (mapped to Flecs inout flags).
    - Special rvalues ignored by user-facing queries: :vf/entity :vf/eid

  The function returns a map with :terms (a vector of term maps suitable for
  `ecs_query_desc_t`) and updates an internal additional-info atom for other
  directives (filters, order_by, query-level metadata).

  This API is intentionally permissive — it accepts nested forms (vectors and
  maps) that carry flags and destructuring info and normalizes them into the
  flat term representation Flecs expects." 
  [wptr query-expr]
  (let [*additional-info (atom {})]
    {:terms
     (->> (if (and (sequential? query-expr)
                   (not (contains? -parser-special-keywords (first query-expr))))
            query-expr
            [query-expr])
          (mapcat (fn parse-one-expr [c]
                    (if (not (sequential? c))
                      [{:id (eid wptr
                                 (case c
                                   (:* *) (flecs/EcsWildcard)
                                   (:_ _) (flecs/EcsAny)
                                   c))
                        :inout (flecs/EcsIn)}]
                      (let [{:keys [flags inout term src]
                             :or {inout :in}
                             :as metadata}
                            (some (fn [v]
                                    (when (and (map? v)
                                               (not (instance? VybeComponent v)))
                                      v))
                                  c)

                            c (remove (fn [v]
                                        (and (map? v)
                                             (not (instance? VybeComponent v))))
                                      c)
                            args (rest c)
                            result (case (first c)
                                     :or
                                     (vec
                                      (concat (->> (:terms (-parse-query-expr wptr (drop-last args)))
                                                   (mapv (fn [term]
                                                           (assoc term :oper (flecs/EcsOr)))))
                                              ;; We put EcsOr only to the first arguments above.
                                              ;; See https://www.flecs.dev/flecs/md_docs_Queries.html#autotoc_md205.
                                              (parse-one-expr (last args))))


                                     :not
                                     (assoc-in (parse-one-expr (last args))
                                               [0 :oper]
                                               (flecs/EcsNot))


                                     ;; Used as a marker for queries so the term can
                                     ;; return the entity source.
                                     (:vf/entity :vf/eid)
                                     (parse-one-expr (last args))

                                     :maybe
                                     [(assoc (first (parse-one-expr (last args)))
                                             :oper (flecs/EcsOptional))]

                                     ;; Force a sync point for this component.
                                     :sync
                                     (parse-one-expr (into [:in {:flags #{:is-entity}}]
                                                           args))

                                     ;; Notify other systems that they should sync
                                     ;; for this component.
                                     :notify
                                     (parse-one-expr (into [:out {:flags #{:is-entity}}]
                                                           args))

                                     :meta
                                     (:terms (-parse-query-expr wptr args))

                                     :query
                                     (do (swap! *additional-info update :query meta-merge/meta-merge metadata)
                                         nil)

                                     :src
                                     (let [src-entity (first args)]
                                       (parse-one-expr
                                        [:meta {:term
                                                {:src (if (and (symbol? src-entity)
                                                               (str/starts-with? (name src-entity) "?"))
                                                        {:name (str "$" (subs (name src-entity)
                                                                              1))}
                                                        {:id (eid wptr src-entity)})}}
                                         (last args)]))

                                     ;; Query scope.
                                     :scope
                                     (vec
                                      (concat [{:id (flecs/EcsScopeOpen) :src {:id (flecs/EcsIsEntity)}}]
                                              (:terms (-parse-query-expr wptr args))
                                              ;; We put EcsOr only to the first arguments above.
                                              ;; See https://www.flecs.dev/flecs/md_docs_Queries.html#autotoc_md205.
                                              [{:id (flecs/EcsScopeClose) :src {:id (flecs/EcsIsEntity)}}]))


                                     ;; Inout(s), see Access Modifiers in the Flecs manual.
                                     (:in :out :inout :inout-filter :filter :none :mut)
                                     (parse-one-expr (into [:meta {:inout (first c)}]
                                                           args))

                                     ;; Pair.
                                     (let [adapt #(case %
                                                    (:* *) (flecs/EcsWildcard)
                                                    (:_ _) (flecs/EcsAny)
                                                    %)
                                           [rel target] (if (= (first c) :pair)
                                                          [(adapt (first args))
                                                           (adapt (last args))]
                                                          [(adapt (first c))
                                                           (adapt (last args))])]
                                       [{:first (if (and (symbol? rel)
                                                         (str/starts-with? (name rel) "?"))
                                                  {:name (str "$" (subs (name rel)
                                                                        1))}
                                                  {:id (eid wptr rel)})
                                         :second (if (and (symbol? target)
                                                          (str/starts-with? (name target) "?"))
                                                   {:name (str "$" (subs (name target)
                                                                         1))}
                                                   {:id (eid wptr target)})}]))]
                        (when result
                          #_(println result inout)
                          (cond-> (-> result
                                      (update 0 meta-merge/meta-merge term))
                            flags
                            (assoc-in [0 :src :id] (->> flags
                                                        (mapv {:up (flecs/EcsUp)
                                                               :cascade (flecs/EcsCascade)
                                                               :is-entity (flecs/EcsIsEntity)
                                                               :self (flecs/EcsSelf)
                                                               :variable (flecs/EcsIsVariable)
                                                               :trav (flecs/EcsTrav)})
                                                        (apply (partial bit-or 0))))

                            src
                            (assoc-in [0 :src] (if (and (symbol? src)
                                                        (str/starts-with? (name src) "?"))
                                                 {:name (str "$" (subs (name src) 1))}
                                                 {:id (eid wptr src)}))

                            (and inout (or (not (get-in result [0 :inout]))
                                           (= (get-in result [0 :inout])
                                              (flecs/EcsIn))))
                            (assoc-in [0 :inout] ({:in (flecs/EcsIn)
                                                   :out (flecs/EcsOut)
                                                   :inout (flecs/EcsInOut)
                                                   :mut (flecs/EcsInOut)
                                                   :inout-filter (flecs/EcsInOutFilter)
                                                   :filter (flecs/EcsInOutFilter)
                                                   :none (flecs/EcsInOutNone)}
                                                  inout))))))))
          vec)

     :additional-info @*additional-info}))
#_(let [Translation (vp/make-component 'Translation [[:x :double] [:y :double]])]
    (->> [[:src '?my-ent Translation]
          [Translation '?my-ent]
          [:maybe {:flags #{:up :cascade}}
           [Translation '?my-ent]]
          [:not
           [:scope
            [:src '?my-ent Translation]
            [Translation '?my-ent]]]]
         (parse-query-expr (-init))))

(defn parse-query-expr
  "Translate a high-level query expression into the map used for
  `ecs_query_desc_t`.

  This function normalizes a user-friendly query expression into the low-level
  shape expected by the Flecs runtime (terms vector plus optional
  `:filter`/`:order_by_component` metadata). It resolves component symbols,
  pairs, and the special keywords documented in `-parse-query-expr`.

  Returns a map ready for `vp/jx-i` with keys such as:
    - :terms           => vector of term maps
    - :filter (opt)    => additional filter metadata
    - :order_by_component (opt) => component to order by (translated to id)

  Example input shapes:
    - [Position]
    - [[Position :global]]
    - [:maybe {:flags #{:up :cascade}} [Position :global]]
    - [:or [A] [B]]

  Use `parse-query-expr` when building programmatic queries or when writing
  systems that need the low-level `ecs_query_desc_t` representation." 
  [wptr query-expr]
  (let [{:keys [terms additional-info]} (-parse-query-expr wptr query-expr)
        query (:query additional-info)]
    (meta-merge/meta-merge
     (meta-merge/meta-merge {:terms terms}
                            (:filter additional-info))
     (cond-> query
       (:order_by_component query)
       (update :order_by_component #(eid wptr %))))))
#_ (let [Translation (vp/make-component 'Translation [[:x :double] [:y :double]])]
     (->> [Translation
           [Translation :global]
           [:maybe {:flags #{:up :cascade}}
            [Translation :global]]]
          (parse-query-expr (-init))))
#_ (->> [[:a :*]]
        (parse-query-expr (-init)))
#_ (->> [[:meta {:term {:src {:id 33}}} [:a :*]]]
        (parse-query-expr (-init)))
#_(->> [:aa]
        (parse-query-expr (-init)))
#_(->> [:meta {:term {:src {:id 521}}}
        vybe.type/Translation]
       (parse-query-expr (-init)))

(defn- -query
  "Creates a query (it can be cached or uncached)."
  [wptr query-expr]
  (->> (vp/jx-i (assoc (parse-query-expr wptr query-expr)
                       :cache_kind (flecs/EcsQueryCacheAuto))
                ecs_query_desc_t)
       (vf.c/ecs-query-init wptr)))
;; Children query.
#_(let [Translation (vp/make-component 'Translation [[:x :double] [:y :double]])]
    (->> [Translation
          [:pair Translation :global]
          [:maybe {:flags #{:up :cascade}}
           [:pair Translation :global]]]
         (-query (-init))))

(defn- ->comp
  [wptr v]
  (or (get builtin-entities-rev v)
      (vp/comp-cache (:id (-get-c wptr v VybeComponentId)))
      (eid wptr v)))

(def ^:private -rvalues-to-be-ignored-by-query
  #{:vf/entity :vf/eid :vf/iter :vf/world :vf/event})

(defn- -each-bindings-adapter
  [^VybeFlecsWorldMap w bindings+opts]
  (let [bindings (->> bindings+opts (remove (comp keyword? first)))
        query-expr (->> bindings
                        (mapv last)
                        (remove -rvalues-to-be-ignored-by-query)
                        vec)
        terms (:terms (parse-query-expr w query-expr))
        inouts (->> terms
                    (mapv :inout)
                    (mapv {(flecs/EcsIn) :in
                           (flecs/EcsInOut) :inout
                           (flecs/EcsOut) :out
                           (flecs/EcsInOutFilter) :inout-filter
                           (flecs/EcsInOutNone) :none}))
        f-arr
        (->> (mapv last bindings)
             (reduce (fn [{:keys [field-idx] :as acc} c]
                       (let [special-keyword (when-let [k (and (vector? c) (first c))]
                                               (when (contains? #{:vf/entity :vf/eid} k)
                                                 k))
                             c (loop [c c]
                                 (if (and (vector? c) (contains? -parser-special-keywords (first c)))
                                   (recur (last c))
                                   c))
                             in? (not (contains? #{:inout :out :inout-filter} (get inouts field-idx)))
                             c (cond
                                 (instance? IVybeWithComponent c)
                                 (.component ^IVybeWithComponent c)

                                 (vector? c)
                                 (mapv (fn [v]
                                         (if (instance? IVybeWithComponent v)
                                           (.component ^IVybeWithComponent v)
                                           v))
                                       c)

                                 (and (int? c) (vf.c/ecs-id-is-pair c))
                                 (or (vp/comp-cache
                                      (:id (-get-c w (vf.c/vybe-pair-first w c) VybeComponentId)))
                                     c)

                                 (int? c)
                                 (or (vp/comp-cache (:id (-get-c w c VybeComponentId)))
                                     c)

                                 :else
                                 c)]
                         (cond
                           (= special-keyword :vf/entity)
                           (-> acc
                               (update :coll conj
                                       (fn [^VybePMap it]
                                         (let [is-set (vf.c/ecs-field-is-set it field-idx)
                                               e-id (when is-set (vf.c/ecs-field-src it field-idx))]
                                           (fn [^long _idx]
                                             (when e-id
                                               (ent w e-id))))))
                               (update :field-idx inc))

                           (= special-keyword :vf/eid)
                           (-> acc
                               (update :coll conj
                                       (fn [^VybePMap it]
                                         (let [is-set (vf.c/ecs-field-is-set it field-idx)
                                               e-id (when is-set (vf.c/ecs-field-src it field-idx))]
                                           (fn [^long _idx]
                                             e-id))))
                               (update :field-idx inc))

                           (or (instance? VybeComponent c)
                               ;; Pair.
                               (and (vector? c)
                                    (some #(instance? VybeComponent %) c)))
                           (let [^VybeComponent c (cond
                                                    (instance? VybeComponent c)         c
                                                    (instance? VybeComponent (first c)) (first c)
                                                    (instance? VybeComponent (peek c))  (peek c))
                                 layout (.layout c)
                                 byte-size (.byteSize layout)]
                             (-> acc
                                 (update :coll conj
                                         ;; If it's marked as an input, add :vp/const. This will
                                         ;; throw if we try to write to it.
                                         (if in?
                                           (fn [^VybePMap it]
                                             (let [p-arr (vf.c/ecs-field-w-size it byte-size field-idx)]
                                               (if (vf.c/ecs-field-is-self it field-idx)
                                                 (fn [^long idx]
                                                   (when-not (vp/null? p-arr)
                                                     (-> (.asSlice ^MemorySegment p-arr (* idx byte-size) layout)
                                                         (vp/p->map c)
                                                         (vary-meta assoc :vp/const true)
                                                         vp/->with-pmap)))
                                                 (fn [^long _idx]
                                                   (when-not (vp/null? p-arr)
                                                     (-> p-arr
                                                         (vp/p->map c)
                                                         (vary-meta assoc :vp/const true)
                                                         vp/->with-pmap))))))
                                           (fn [^VybePMap it]
                                             (let [p-arr (vf.c/ecs-field-w-size it byte-size field-idx)]
                                               (if (vf.c/ecs-field-is-self it field-idx)
                                                 (fn [^long idx]
                                                   (when-not (vp/null? p-arr)
                                                     (-> (.asSlice ^MemorySegment p-arr (* idx byte-size) layout)
                                                         (vp/p->map c)
                                                         vp/->with-pmap)))
                                                 (fn [^long _idx]
                                                   (when-not (vp/null? p-arr)
                                                     (-> p-arr
                                                         (vp/p->map c)
                                                         vp/->with-pmap))))))))
                                 (update :field-idx inc)))

                           ;; Pair (tag).
                           (and (vector? c)
                                (some #{:* :_} c))
                           (-> acc
                               (update :coll conj
                                       (fn [^VybePMap it]
                                         (let [p-arr (vf.c/ecs-field-id it field-idx)
                                               [rel target] (when-not (vp/null? p-arr)
                                                              [(vf.c/vybe-pair-first w p-arr)
                                                               (vf.c/vybe-pair-second w p-arr)])
                                               is-set (vf.c/ecs-field-is-set it field-idx)]
                                           (fn [^long _idx]
                                             (when-not (vp/null? p-arr)
                                               (when is-set
                                                 (mapv #(->comp w %) [rel target])))))))
                               (update :field-idx inc))

                           :else
                           (-> acc
                               (update :coll conj
                                       (case c
                                         :vf/entity
                                         (fn [it]
                                           (let [^MemorySegment entities-arr (:entities it)]
                                             (fn [^long idx]
                                               (ent w (.getAtIndex entities-arr ValueLayout/JAVA_LONG idx)))))

                                         :vf/eid
                                         (fn [it]
                                           (let [^MemorySegment entities-arr (:entities it)]
                                             (fn [^long idx]
                                               (.getAtIndex entities-arr ValueLayout/JAVA_LONG idx))))

                                         :vf/iter
                                         (fn [it]
                                           (fn [^long _idx]
                                             it))

                                         :vf/world
                                         (fn [it]
                                           (fn [^long _idx]
                                             (-make-world (:world it) {})))

                                         ;; Used in observers.
                                         :vf/event
                                         (fn [it]
                                           (fn [^long _idx]
                                             (condp = (:event it)
                                               (flecs/EcsOnAdd) :add
                                               (flecs/EcsOnSet) :set
                                               (flecs/EcsOnRemove) :remove
                                               nil)))

                                         (fn [it]
                                           (let [is-set (vf.c/ecs-field-is-set it field-idx)]
                                             (fn [_idx]
                                               (when is-set
                                                 c))))))
                               (update :field-idx (if (contains? #{:vf/iter :vf/entity :vf/eid :vf/world :vf/event} c)
                                                    identity
                                                    inc))))))
                     {:field-idx 0 :coll []})
             :coll)]
    {:opts (->> bindings+opts (filter (comp keyword? first)) (into {}))
     :f-arr f-arr
     :query-expr query-expr}))

(defonce *-each-cache (atom {}))

(defmacro with-deferred
  "Run `body` with Flecs deferred mode enabled for world `w`.

  Arguments:
    - w : world (VybeFlecsWorldMap or raw Flecs world pointer).
    - body : forms executed while Flecs defer mode is active.

  Behavior:
    - Calls `ecs-defer-begin` on `w` before evaluating `body` and ensures
      `ecs-defer-end` is called in a `finally` block.
    - Changes enqueued during the deferred block are applied when `ecs-defer-end`
      runs; code inside the block will not observe those modifications.

  Use to batch many updates safely during iteration and avoid iterator
  invalidation."
  [w & body]
  `(try
     (vf.c/ecs-defer-begin ~w)
     ~@body
     (finally
       (vf.c/ecs-defer-end ~w))))

(defn iter-skip
  "Skip the current iterator chunk so Flecs does not mark components as modified."
  [it]
  (vf.c/ecs-iter-skip it))

(defn iter-changed
  "Return true when the iterator reports data changes during the current step."
  [it]
  (vf.c/ecs-iter-changed it))

(defn -query-internal
  [^VybeFlecsWorldMap w bindings+opts]
  (let [{:keys [_opts f-arr query-expr]} (-each-bindings-adapter w bindings+opts)
        wptr w
        q (vp/with-arena-root (-query wptr query-expr))]
    (vy.u/debug :creating-query)
    (fn [each-handler]
      (let [it (vf.c/ecs-query-iter wptr q)
            *acc (atom [])]
        (with-deferred w
          (while (vf.c/ecs-query-next it)
            (let [f-idx (mapv (fn [f] (f it)) f-arr)]
              (swap! *acc conj (mapv (fn [idx]
                                       (each-handler (mapv (fn [f] (f idx)) f-idx)))
                                     (range (:count it)))))))
        (vec (apply concat @*acc))))))

(comment

  ;; Wildcard query.
  (let [w (vf/make-world)
        A (vp/make-component 'A [[:x :double]])]
    (merge w {:b [(A {:x 34})
                  [:a :c]
                  [:a :d]]})
    (vf/with-query w [a A
                      v [:a :*]]
      [a v])
    #_(-get-c w :b [:a :*]))

  ;; Children query.
  (let [w (vf/make-world #_{:debug true})
        Translation (vp/make-component 'Translation [[:x :double] [:y :double]])]
    (merge w {:b [(Translation {:x -205.1})
                  [(Translation {:x -206.1}) :global]
                  {:a [(Translation {:x -105.1})
                       [(Translation {:x -106.1}) :global]
                       :aaa]}]})
    [(vf/with-query w [pos Translation
                      pos-global [Translation :global]
                      pos-parent [:maybe {:flags #{:up :cascade}}
                                  [Translation :global]]]
       (println :aaa)
       [pos pos-global pos-parent])

     (do (merge w {:b [(Translation {:x -400.1})]})
         (vf/with-query w [pos Translation
                          pos-global [Translation :global]
                          pos-parent [:maybe {:flags #{:up :cascade}}
                                      [Translation :global]]]
           (println :aaa)
           [pos pos-global pos-parent]))])

  ())

(defmacro with-query
  "Query iteration macro.

  Arguments:
    - w        : world (VybeFlecsWorldMap or native world pointer).
    - bindings : a `let`-style binding vector describing which components or
                 special values to extract for each tuple. Bindings are
                 provided as pairs: `[sym spec sym spec ...]` where `spec`
                 can be a component symbol, a pair vector (e.g. `[Comp :tag]`),
                 or a special keyword binding such as `:vf/entity`, `:vf/eid`,
                 `:vf/iter`, `:vf/world`, `:vf/event`.
    - body     : forms executed for each matched tuple. The body may return
                 any value; `with-query` concatenates the per-tuple results
                 into a flat vector which it returns.

  Notes on `bindings` shapes:
    - Component symbol: `Position` — provides the component value as a Vybe
      map/struct.
    - Destructuring: `{:keys [x] :as pos} Position` — destructure the mapped
      component value into `pos`.
    - Pair/vector: `v [:a :*]` or `[Translation :global]` — supports Flecs
      pair queries and wildcard/tag uses.

  Use `with-query-one` to return only the first match."
  [w bindings & body]
  (let [bindings (mapv (fn [[k v]]
                         [k v])
                       (concat (partition 2 bindings)
                               (list (list w :vf/world))))
        code `(-query-internal ~w ~(mapv (fn [[k v]] [`(quote ~k) v]) bindings))
        hashed (hash code)]
    `(let [hashed# (hash ~(mapv last bindings))
           f# (or (get-in @*-each-cache [(vp/mem ~w) [~hashed hashed#]])
                  (let [res# ~code]
                    (swap! *-each-cache assoc-in [(vp/mem ~w) [~hashed hashed#]] res#)
                    res#))]
       (f# (fn [~(vec (remove keyword? (mapv first bindings)))]
             (try
               (vy.u/if-prd
                (do ~@body)
                (do (vy.u/counter! (str "QUERY_" ~*ns* "_"
                                        ~(select-keys (meta &form) [:line :column])
                                        "_" ~hashed "_" hashed#))
                    ~@body))
               (catch Throwable e#
                 (println e#))))))))

(defmacro with-query-one
  "Like `with-query`, but returns only the first matched result.

  Arguments are identical to `with-query`:
    - w : world
    - bindings : the binding vector
    - body : forms producing the per-tuple result

  Example:

    (vf/with-query-one w [_ :vg/camera-active
                          camera vt/Camera]
      camera)"
  [w bindings & body]
  `(first (vf/with-query ~w ~bindings ~@body)))

(comment

  ;; Simple query.
  (let [w (vf/make-world #_{:debug true})
        {:syms [Position ImpulseSpeed]} (vp/make-components
                                         '{ImpulseSpeed [[:value :double]]
                                           Position [[:x :double] [:y :double]]})
        xx :vf/entity]
    (merge w {:a [(Position {:x -105.1}) :aaa]
              :b [(Position {:x 333.1}) (ImpulseSpeed 311)]
              :c [(Position {:x 0.1}) (ImpulseSpeed -43)]})
    (vf/with-query w [speed ImpulseSpeed
                      {:keys [x] :as pos} Position
                      e xx]
      [e (update pos :x dec) x (update speed :value inc)]))

  ())

(defn -system-callback
  [f]
  (-> (vp/with-apply ecs_iter_action_t
        [_ it]
        (f it))))

(defn- -ecs-log-init
  []
  (vf.c/ecs-os-set-api-defaults)
  (let [os-api (vf.c/ecs-os-get-api)]
    (assoc os-api
           :log_ (vp/with-apply ecs_os_api_log_t
                   [_ level file line msg]
                   (println :level level
                            :file (vp/->string file)
                            :line line
                            :msg (vp/->string msg)))
           #_ #_:abort_ (vp/with-apply ecs_os_api_abort_t
                          [_]
                          (println :!!!!!!!!ABORT!!!!!!)))
    (vf.c/ecs-os-set-api os-api)
    (vf.c/ecs-os-init)))
#_ (-ecs-log-init)

(defn -system
  [^VybeFlecsWorldMap w bindings+opts each-handler]
  (vp/with-arena-root
    (let [{:keys [opts f-arr query-expr]} (-each-bindings-adapter w bindings+opts)
          _ (vy.u/debug :creating-system (:vf/name opts))
          e (eid w (:vf/name opts))
          ;; Delete entity if it's a system already and recreate it.
          e (if (vf.c/ecs-has-id w e (flecs/EcsSystem))
              (do (vf.c/ecs-delete w e)
                  (eid w (:vf/name opts)))
              e)

          {:vf/keys [phase always disabled immediate]
           :or {immediate false}}
          opts

          _system-id (vf.c/ecs-system-init
                      w (system_desc_t
                         (merge {:entity e
                                 :immediate immediate
                                 ;; Add query detection flaqg because of
                                 ;; https://github.com/SanderMertens/flecs/discussions/466#discussioncomment-13249013
                                 :query (merge (parse-query-expr w query-expr)
                                               {:flags (flecs/EcsQueryDetectChanges)})}
                                (if always
                                  {:callback (-system-callback
                                              (fn [it-p]
                                                (let [it (vp/jx-p->map it-p iter_t)
                                                      f-idx (mapv (fn [f] (f it)) f-arr)]
                                                  (doseq [idx (range (:count it))]
                                                    (each-handler (mapv (fn [f] (f idx)) f-idx))))))}
                                  {:run (-system-callback
                                         (fn [it-p]
                                           (let [it (vp/jx-p->map it-p iter_t)]
                                             (when (vf.c/ecs-query-changed (:query it))
                                             (while (vf.c/ecs-query-next it-p)
                                               (if (vf.c/ecs-iter-changed it-p)
                                                 (let [it (vp/jx-p->map it-p iter_t)
                                                       f-idx (mapv (fn [f] (f it)) f-arr)]
                                                   (doseq [idx (range (:count it))]
                                                     (each-handler (mapv (fn [f] (f idx)) f-idx))))
                                                 (vf.c/ecs-iter-skip it-p)))))))}))))
          depends-on [(flecs/EcsDependsOn) (or phase (flecs/EcsOnUpdate))]]
      (assoc w e (cond-> [depends-on]
                   disabled
                   (conj :vf/disabled)))
      (ent w e))))

(comment

  (let [w (vf/make-world #_{:debug true})
        {:syms [Position ImpulseSpeed]} (vp/make-components
                                         '{ImpulseSpeed [[:value :double]]
                                           Position [[:x :double] [:y :double]]})]
    (merge w {:a [(Position {:x -105.1}) :aaa]
              :b [(Position {:x 333.1}) (ImpulseSpeed 311)]
              :c [(Position {:x 0.1}) (ImpulseSpeed -43)]})

    (vf/with-system w [:vf/name :my-system-with-a-big-name
                       speed ImpulseSpeed
                       {:keys [x] :as pos} Position
                       e :vf/entity]
      (println :bbbb)
      [e (update pos :x dec) x (update speed :value inc)])

    (vf/system-run w :my-system-with-a-big-name)
    (vf/system-run w :my-system-with-a-big-name)
    w)

  ())

(defmacro with-system
  "Define/register a system entity.

  Arguments:
    - w        : world (VybeFlecsWorldMap or native pointer)
    - bindings : a `let`-style binding vector that must include the
                 `:vf/name` entry and describes the system inputs (components
                 or special bindings) in the same shape as `with-query`.
    - body     : forms executed by the system for each matched tuple (the
                 system body is compiled into a callback and registered with
                 Flecs).

  Required binding keys:
    - `:vf/name` : unique identifier for the system in the world (keyword or
      string). The macro uses this to create/lookup the underlying system
      entity.

  Optional binding keys:
    - `:vf/phase`  : pipeline phase
    - `:vf/always` : when true, system runs every progress step regardless of
      change detection

  Behavior: `with-system` registers a system entity that can be invoked via
  `system-run` or by advancing the world pipeline with `progress`.

  -- Example --

  (let [w (vf/make-world #_{:debug true})
        {:syms [Position ImpulseSpeed]} (vp/make-components
                                         '{ImpulseSpeed [[:value :double]]
                                           Position [[:x :double] [:y :double]]})]
    (merge w {:a [(Position {:x -105.1}) :aaa]
              :b [(Position {:x 333.1}) (ImpulseSpeed 311)]
              :c [(Position {:x 0.1}) (ImpulseSpeed -43)]})

    (vf/with-system w [:vf/name :my-system-with-a-big-name
                       speed ImpulseSpeed
                       {:keys [x] :as pos} Position
                       e :vf/entity]
      [e (update pos :x dec) x (update speed :value inc)])

    (vf/system-run w :my-system-with-a-big-name)
    w)"
  [w bindings & body]
  (let [bindings (mapv (fn [[k v]]
                         [k v])
                       (concat (partition 2 bindings)
                               (list (list w :vf/world))))
        bindings-map (into {} bindings)
        _ (when-not (:vf/name bindings-map)
            (throw (ex-info "`with-system` requires a :vf/name" {:bindings bindings
                                                                 :body body})))
        code `(let [identifier# ~(:vf/name bindings-map)]
                (-system ~w ~(mapv (fn [[k v]] [`(quote ~k) v]) bindings)
                         (fn #_~(symbol (str/replace (str "___" (symbol (:vf/name bindings-map)))
                                                     #"/" "__"))
                           [~(vec (remove keyword? (mapv first bindings)))]
                           (try
                             (vy.u/if-prd
                              (do ~@body)
                              (do (vy.u/counter! identifier#)
                                  ~@body))
                             (catch Throwable e#
                               (println e#))))))
        hashed (hash code)]
    `(let [hashed# (hash ~(mapv last bindings))]
       (or (when-let [e# (get-in @*-each-cache [(vp/mem ~w) [~hashed hashed#]])]
             (when (alive? ~w e#)
               e#))
           (let [res# ~code]
             (swap! *-each-cache assoc-in [(vp/mem ~w) [~hashed hashed#]] res#)
             res#)))))

(defn system-run
  "Execute a system entity once with a zero delta-time.

  Forms:
    - (system-run em)    ; em is a `VybeFlecsEntitySet`
    - (system-run w e)   ; w is a world, e an entity designator or id

  This wraps `ecs-run` and runs the system immediately with a delta-time of
  0. The function returns whatever the native call returns (typically nil).
  Use `vf/progress` to advance the world's pipeline and run all systems for a
  given delta-time." 
  ([^VybeFlecsEntitySet em]
   (system-run (.w em) (.id em)))
  ([^VybeFlecsWorldMap w e]
   (vf.c/ecs-run w (eid w e) 0 vp/null)))

(defn system-query-str
  "Return the Flecs DSL query string for the given system entity.

  Forms:
    - (system-query-str em)  ; em is a `VybeFlecsEntitySet`
    - (system-query-str w e) ; w is a world, e an entity designator

  The returned string is suitable for debugging and mirrors the native Flecs
  query definition backing the system." 
  ([^VybeFlecsEntitySet em]
   (system-query-str (.w em) (.id em)))
  ([w e]
   (-> (vf.c/ecs-system-get w (eid w e))
       (vp/p->map vf/system_t)
       :query
       vf.c/ecs-query-str
       vp/->string)))

(defn progress
  "Advance the world's pipeline, running systems for `delta-time`.

  Forms:
    - (progress w)           ; advances with delta-time 0
    - (progress w delta-time)

  `delta-time` is a number in seconds (or the unit used by your systems). This
  calls the native `ecs-progress` which runs scheduled systems for the world." 
  ([^VybeFlecsWorldMap w]
   (progress w 0))
  ([^VybeFlecsWorldMap w delta-time]
   (vf.c/ecs-progress w delta-time)))

(defn -observer
  [^VybeFlecsWorldMap w bindings+opts each-handler]
  (vp/with-arena-root
    (let [{:keys [opts f-arr query-expr]} (-each-bindings-adapter w bindings+opts)
          _ (vy.u/debug :creating-observer (:vf/name opts))
          e (eid w (:vf/name opts))
          ;; Delete entity if it's a observer already and recreate it.
          [existing? e] (if (vf.c/ecs-has-id w e (flecs/EcsObserver))
                          [true (do (vf.c/ecs-delete w e)
                                    #_(vy.u/debug :is-alive e (vf.c/ecs-is-alive w e))
                                    (eid w (:vf/name opts)))]
                          [false e])
          _ (when existing?
              (merge w {e [:vf/existing]}))
          {:vf/keys [events yield-existing disabled]
           :or {yield-existing false}}
          opts

          _observer-id (vf.c/ecs-observer-init
                        w (ecs_observer_desc_t
                           {:entity e
                            :query (parse-query-expr w query-expr)
                            :events (-> (cond-> (->> events
                                                     (remove #{:add :set :remove})
                                                     (mapv #(eid w %)))
                                          (contains? events :add) (conj (flecs/EcsOnAdd))
                                          (contains? events :set)(conj (flecs/EcsOnSet))
                                          (contains? events :remove) (conj (flecs/EcsOnRemove)))
                                        seq
                                        (vp/arr :long))
                            :yield_existing yield-existing
                            :callback (-system-callback
                                       (fn [it]
                                         (let [it (vp/jx-p->map it iter_t)
                                               f-idx (mapv (fn [f] (f it)) f-arr)]
                                           (doseq [idx (range (:count it))]
                                             (each-handler (mapv (fn [f] (f idx)) f-idx))))))}))]
      (assoc w e (cond-> []
                   disabled
                   (conj :vf/disabled)))
      (ent w e))))

(comment

  (let [w (vf/make-world #_{:debug true})
        {:syms [Position ImpulseSpeed]} (vp/make-components
                                         '{ImpulseSpeed [[:value :double]]
                                           Position [[:x :double] [:y :double]]})]

    (vf/with-observer w [:vf/name :my-observer-with-a-big-name
                         speed ImpulseSpeed
                         {:keys [x] :as pos} Position
                         e :vf/entity
                         event :vf/event]
      (println event)
      (println :speed speed)
      (println :x x)
      #_[e (update pos :x dec) x (update speed :value inc)])

    (merge w {:a [(Position {:x -105.1}) :aaa]
              :b [(Position {:x 333.1}) (ImpulseSpeed 311)]
              #_ #_:c [(Position {:x 0.1}) (ImpulseSpeed -43)]})

    w)

  ())

(defmacro with-observer
  "Create and register an observer for `w` using a `with-query`-style binding
  vector and a handler body.

  Arguments:
    - w        : world
    - bindings : `let`-style binding vector. Must include `:vf/name`. May
                 include `:vf/events` to restrict which events trigger the
                 observer. Bindings may contain a special `[:event <sym>]`
                 entry to indicate an event payload binding.
    - body     : handler body executed when the observer triggers.

  Behavior:
    - Registers an observer entity with the given `:vf/name` and callback
      wiring described by `bindings`.
    - When a `[:event <event-sym>]` binding is present, the corresponding
      parameter is populated with the event payload in the handler.

  Returns the entity id of the created observer (same caching behaviour as
  `with-system`)."
  [w bindings & body]
  (let [bindings (->> (concat (partition 2 bindings) (list (list w :vf/world)))
                      (mapcat (fn [[k v]]
                                (if (and (vector? v)
                                         (= (first v) :event))
                                  [[:vf/events #{(last v)}]
                                   ['-vybe_ :_]
                                   [(with-meta '-vybe-it
                                      {:binding k
                                       :event (last v)})
                                    :vf/iter]]
                                  [[k v]])))
                      vec)
        bindings-map (into {} bindings)
        _ (when-not (:vf/name bindings-map)
            (throw (ex-info "`with-observer` requires a :vf/name" {:bindings bindings
                                                                   :body body})))
        identifier-sym (gensym)
        code `(let [~identifier-sym ~(:vf/name bindings-map)]
                (-observer ~w ~(mapv (fn [[k v]] [`(quote ~k) v]) bindings)
                           (fn #_~(symbol (str/replace (str "___" (symbol (:vf/name bindings-map)))
                                                       #"/" "__"))
                             [~(vec (remove keyword? (mapv first bindings)))]
                             (try
                               ~(if-let [{:keys [binding event]} (meta (first (filter #{'-vybe-it} (keys bindings-map))))]
                                  `(let [~binding (if (vp/component? ~event)
                                                    (vp/p->map (:param ~'-vybe-it) ~event)
                                                    ~event)]
                                     (vy.u/if-prd
                                      (do ~@body)
                                      (do (vy.u/counter! ~identifier-sym)
                                          ~@body)))
                                  `(vy.u/if-prd
                                    (do ~@body)
                                    (do (vy.u/counter! ~identifier-sym)
                                        ~@body)))
                               (catch Throwable e#
                                 (println e#))))))
        hashed (hash code)]
    (when-not (contains? (set (mapv first bindings)) :vf/name)
      (throw (ex-info "`with-observer` requires a :vf/name" {:bindings bindings
                                                             :body body})))
    `(let [hashed# (hash ~(mapv last bindings))]
       (or (when-let [e# (get-in @*-each-cache [(vp/mem ~w) [~hashed hashed#]])]
             (when (alive? ~w e#)
               e#))
           (let [res# ~code]
             #_(vy.u/debug :new-observer ~(:vf/name bindings-map))
             (swap! *-each-cache assoc-in [(vp/mem ~w) [~hashed hashed#]] res#)
             res#)))))
#_(macroexpand-1
   '(vf/with-observer w [:vf/name :observer/on-contact-added
                         {:keys [body-1 body-2]} [:event vg/OnContactAdded]]
      (println [(:id body-1) (:id body-2)])))

(defmacro defquery
  "Define a reusable, named query helper.

  Arguments:
    - name     : symbol for the generated function
    - w        : world parameter name used in the generated function
    - bindings : the `with-query` binding vector
    - body     : the body evaluated for each match; the generated function
                 returns the results same as `with-query`.

  Expands into a single-argument function `(fn [w])` that invokes
  `with-query` with the supplied bindings and body. Useful to create
  composable query helpers.

  Example:

    (vf/defquery all-positions w [p Position]
      p)

    (all-positions w)"
  [name w bindings & body]
  `(defn ~name [w#]
     (let [~w w#]
       (vf/with-query ~w ~bindings
         ~@body))))

(defmacro defobserver
  "Define a named observer registration function.

  Arguments:
    - name     : symbol for the generated function
    - w        : world parameter name used in the generated function
    - bindings : `with-observer` binding vector (the macro will inject a
                 generated `:vf/name` value based on namespace/name)
    - body     : handler body executed when the observer fires

  Expands to a one-argument function that registers the observer using
  `with-observer` with a generated `:vf/name` of `:<ns>/<name>`.
  Useful for packaging observer logic for initialization.

  Example:

    (vf/defobserver body-removed w
      [:vf/events #{:remove}
       body vj/VyBody]
      (when (vj/added? body)
        (vj/remove* body)))

    (body-removed w)"
  [name w bindings & body]
  `(defn ~name [w#]
     (let [~w w#]
       (vf/with-observer ~w ~(concat [:vf/name (keyword (str *ns* "/" name))]
                                     bindings)
         ~@body))))

(defmacro defsystem
  "Define a named system registration helper.

  Arguments:
    - name     : symbol for the generated function
    - w        : world parameter name used in the generated function
    - bindings : `with-system` binding vector (the macro injects a generated
                 `:vf/name` of `:<ns>/<name>` if none is provided)
    - body     : system body executed per match

  Expands to a one-argument function that registers (or reuses) a Flecs
  system entity using `with-system`. The helper caches by binding shape to
  avoid duplicate system creation.

  Example:

    (vf/defsystem move-system w
      [:vf/name :move
       pos Position]
      (move-entities pos))

    (move-system w)"
  [name w bindings & body]
  `(defn ~name [w#]
     (let [~w w#]
       (vf/with-system ~w ~(concat [:vf/name (keyword (str *ns* "/" name))]
                                   bindings)
         ~@body))))

(defmacro ^:private -field
  "Internal helper: return a typed pointer expression for field `idx` on
  iterator `it` with C type `c`.

  Arguments:
    - it  : iterator local (pointer wrapper used by generated C callback)
    - c   : C/Flecs component type (a Vybe component descriptor)
    - idx : numeric field index inside the iterator

  This macro is private and intended only for use by the C-backed system
  generator (`defsystem-c`) and related machinery. It wraps the call to
  `vf.c/ecs-field-w-size` and casts the result to the expected pointer type
  using `vp/as` so generated C interop code can access the field data
  directly." 
  [it c idx]
  `(-> (vf.c/ecs-field-w-size ~it (vp/sizeof ~c) ~idx)
       (vp/as [:* ~c])))

(def c-w
  "C symbol for the world inside a C system."
  'w--)

(defonce -c-sys-cache (atom {}))

(defmacro defsystem-c
  "Define a system using the VybeC subset: the body will be compiled to C and
  linked as a dynamic library to run the system with lower overhead.

  Arguments:
    - sys-name : symbol for the generated system registration function
    - w        : world parameter name used by the generated function
    - bindings : binding vector describing inputs (same shapes as
                 `with-system`), limited to constructs supported by VybeC
    - body     : the system body expressed in the VybeC subset (lowerable to
                 straightforward C)

  Requirements and notes:
    - `clang` (or a compatible C toolchain) must be available to perform the
      first compilation.
    - The VybeC subset restricts Clojure forms to those that can be lowered to
      simple C constructs; complex runtime features are not supported.
    - The macro generates glue that exposes a C entry point used by the
      Flecs system callback and handles marshaling of iterator/field data.

  Use `defsystem-c` when performance matters and when your system body can be
  expressed using the supported VybeC subset. See the codebase `vybe_c` and
  `bin/jextract-libs.sh` helpers for the build and linking workflow."
  {:clj-kondo/ignore [:type-mismatch]}
  [sys-name w bindings & body]
  (let [bindings (mapv (fn [[k v]]
                         [k v])
                       (partition 2 bindings))
        bindings-only-valid (->> bindings
                                 (remove (comp keyword? first))
                                 vec)
        i 'vybe_c_idx
        it 'vybe_c_it

        f (fn [idx [k v]]
            (when-not (and (symbol? k)
                           (= (first (name k)) \_))
              (let [ ;; Collect some flags (e.g. :maybe, :up)
                    *flags (atom #{})
                    v (loop [c v]
                        (if (and (vector? c)
                                 (contains? vf/-parser-special-keywords
                                            (first c)))
                          (do
                            (when-let [{:keys [flags]} (and (= (count c) 3)
                                                            (second c))]
                              (swap! *flags set/union flags))

                            (when (= (first c) :maybe)
                              (swap! *flags set/union #{:maybe}))
                            (when (= (first c) :vf/entity)
                              (swap! *flags set/union #{:vf/entity}))
                            (when (= (first c) :src)
                              (swap! *flags set/union #{:src}))

                            (recur (last c)))
                          c))

                    ;; If a symbol, we assume it's a component.
                    v (cond
                        (and (vector? v)
                             (symbol? (first v)))
                        (first v)

                        (and (vector? v)
                             (symbol? (second v)))
                        (second v)

                        :else
                        v)
                    flags @*flags
                    meta-default {:idx idx
                                  :sym k
                                  :flags flags
                                  :rvalue v}]
                ;; We return a vector of vectors because of the
                ;; destructuring (note that we use `(apply concat)` below.
                (cond
                  (:vf/entity flags)
                  ;; TODO maybe
                  [(with-meta [(symbol (str k "--arr"))
                               ;; TODO support `:maybe`
                               `(vf.c/ecs-field-src ~it ~idx)]
                     meta-default)]

                  ;; Get root entity.
                  (= v :vf/entity)
                  [(with-meta [(symbol (str k "--arr"))
                               `(vp/as (:entities @~it) [:* :long])]
                      meta-default )]

                  ;; Get iter pointer.
                  (= v :vf/iter)
                  [(with-meta [(symbol (str k "--arr"))
                               it]
                     meta-default)]

                  (vector? v)
                  (if (vector? k)
                    (->> k
                         ;; Index for the vector destructuring.
                         (map-indexed (fn [idx-destructuring k-each]
                                        (when-not (= (first (name k-each)) \_)
                                          (with-meta [(symbol (str k-each "--arr"))
                                                      ;; TODO support `:maybe`
                                                      ;; TODO OPTM The result of  `ecs-field-id` can be
                                                      ;; associated to its own variable.
                                                      (if (= idx-destructuring 1)
                                                        `(vf.c/vybe-pair-second ~c-w (vf.c/ecs-field-id ~it ~idx))
                                                        `(vf.c/vybe-pair-first ~c-w (vf.c/ecs-field-id ~it ~idx)))]
                                            {:idx idx
                                             :type nil
                                             :sym k-each
                                             :flags flags}))))
                         (remove nil?)
                         vec)
                    [(with-meta [(symbol (str k "--arr"))
                                 (if (contains? flags :maybe)
                                   `(when (vf.c/ecs-field-is-set ~it ~idx)
                                      (vf.c/ecs-field-id ~it ~idx))
                                   `(vf.c/ecs-field-id ~it ~idx))]
                       meta-default)])

                  ;; Tag branch.
                  (keyword? v)
                  [(with-meta [(symbol (str k "--arr"))
                               (if (contains? flags :maybe)
                                 `(when (vf.c/ecs-field-is-set ~it ~idx)
                                    (vf.c/ecs-field-id ~it ~idx))
                                 `(vf.c/ecs-field-id ~it ~idx))]
                     meta-default)]

                  ;; Component branch.
                  :else
                  (let [k-sym (if (map? k)
                                (symbol (str "SYM--internal-" idx))
                                k)]
                    [(with-meta [(symbol (str k-sym "--arr"))
                                 (if (contains? flags :maybe)
                                   `(if (vf.c/ecs-field-is-set ~it ~idx)
                                      (-field ~it ~v ~idx)
                                      (vp/as vp/null [:* ~v]))
                                   `(-field ~it ~v ~idx))]
                       (merge meta-default
                              {:type v
                               :sym k-sym
                               :binding-form k}))])))))

        bindings-processed (->> bindings-only-valid
                                (map-indexed f)
                                (apply concat))]
    #_(do (def bindings bindings)
          (def bindings-only-valid bindings-only-valid)
          (def bindings-processed bindings-processed)
          (def f f)
          (mapv meta bindings-processed))
    `(do
       ;; Define iterator.
       (vc/defn* ~(with-meta (symbol (str sys-name "--internal"))
                    (merge (meta sys-name)
                           {:private true}))
         :- :void
         [~it :- [:* vf/iter_t]]
         ~(-> `(let [~w (:world @~it)
                     ~c-w (:world @~it)]
                 (let ~ (->> bindings-processed
                             (apply concat)
                             vec)
                   (doseq [~i (range (:count @~it))]
                     (let ~ (->> bindings-processed
                                 (mapv (fn [k-and-v]
                                         (let [{:keys [sym flags binding-form type idx rvalue]} (meta k-and-v)
                                               [k _v] k-and-v
                                               ;; If we are up, it means we are self.
                                               ;; TODO Use `is-self` so we can cover up
                                               ;; all the possibilities (e.g. Prefabs).
                                               i (if (or (contains? flags :up)
                                                         (contains? flags :src))
                                                   0
                                                   i)]
                                           (if type
                                             ;; Component branch.
                                             (let [form (if (contains? flags :maybe)
                                                          `(if ~k
                                                             (vp/& (nth ~k ~i))
                                                             (vp/as vp/null [:* ~type]))
                                                          `(vp/& (nth ~k ~i)))
                                                   res-internal-sym (symbol (str "res-internal--" idx))
                                                   rvalue-form (if (contains? flags :maybe)
                                                                 `(if ~res-internal-sym
                                                                    (deref ~res-internal-sym)
                                                                    (vc/cast* 0 ~type))
                                                                 `(deref ~res-internal-sym))]
                                               (if (map? binding-form)
                                                 ;; Map destructuring.
                                                 (vec (concat [res-internal-sym form]
                                                              ;; :keys destructuring
                                                              (->> (:keys binding-form)
                                                                   (mapcat #(vector %
                                                                                    (list (keyword %)
                                                                                          rvalue-form))))
                                                              ;; {aabb-min :min aabb-max :max} destructuring
                                                              (->> (dissoc binding-form :keys)
                                                                   (mapcat #(vector (first %)
                                                                                    (list (keyword (second %))
                                                                                          rvalue-form))))))
                                                 ;; No destructuring.
                                                 [sym form]))
                                             ;; Not a component branch.
                                             ;; TODO support `:maybe`
                                             [sym
                                              (case rvalue
                                                :vf/entity
                                                `(nth ~k ~i)
                                                k)]))))
                                 (apply concat)
                                 vec)
                       ~@ (mapv #(vary-meta % merge {:void true})
                                body)))))
              (with-meta (meta &form))))

       ;; Define system builder.
       (defn ~sys-name
         [w#]
         #_ (def ~'w w#)
         (let [k-name# (keyword (symbol #'~sys-name))
               c-sys# ~(symbol (str sys-name "--internal"))
               lib-full-path# (:lib-full-path c-sys#)
               w-addr# (vp/& (vf.c/ecs-get-world w#))]
           (or (get-in @-c-sys-cache [w-addr# k-name# lib-full-path#])
               (let [_# (swap! -c-sys-cache update w-addr# dissoc k-name#)
                     e# (eid w# k-name#)
                     ;; Delete entity if it's a system already and recreate it.
                     e# (if (vf.c/ecs-has-id w# e# (flecs/EcsSystem))
                          (do (vf.c/ecs-delete w# e#)
                              (eid w# k-name#))
                          e#)

                     q# (vf/parse-query-expr w# ~(->> (mapv second bindings-only-valid)
                                                      (remove -rvalues-to-be-ignored-by-query)
                                                      vec))
                     system-desc# (system_desc_t
                                   {:entity e#
                                    :callback (vp/mem c-sys#)
                                    :query q#})

                     depends-on# [(flecs/EcsDependsOn) (flecs/EcsOnUpdate)
                                  #_(or phase (flecs/EcsOnUpdate))]
                     sys# (vf/ent w# (vf.c/ecs-system-init w# system-desc#))]

                 (vy.u/debug :creating-system k-name#)
                 (assoc w# e# [depends-on#] #_(cond-> [depends-on#]
                                                disabled
                                                (conj :vf/disabled)))
                 (swap! -c-sys-cache assoc-in [w-addr# k-name# lib-full-path#] sys#)
                 sys#)))))))

(defonce ^:private lock (Object.))

(defn event!
  "Enqueue a Flecs event, optionally associated with an entity.

  Forms:
    - (event! em event)      ; em is a `VybeFlecsEntitySet`, `event` is an entity/designator
    - (event! w e event)      ; w is a world, e an entity designator

  The `event` argument may be:
    - a component/entity designator (keyword, symbol, id) that resolves to the
      Flecs event id to enqueue, or
    - an `IVybeMemorySegment` wrapper whose underlying memory segment is used
      as the event payload (the memory is attached to the native event
      descriptor via :param).

  Note: events are enqueued under a global lock to avoid concurrent enqueue
  races; the function will return quickly after scheduling the native event."
  ([w-or-em event]
   (if (instance? VybeFlecsEntitySet w-or-em)
     (let [^VybeFlecsEntitySet em w-or-em]
       (event! (.w em) (.id em) event))
     ;; Event with no entity.
     (event! w-or-em :vf/_ event)))
  ([w e event]
   ;; We don't want to emit events in parallel.
   ;; See https://discord.com/channels/633826290415435777/1258103334255067267.
   (locking lock
     (let [event-desc (vf/event_desc_t
                       (cond-> {:event (vf/eid w event)
                                :entity (vf/eid w e)}
                         (instance? IVybeMemorySegment event)
                         (assoc :param (.mem_segment ^IVybeMemorySegment event))))]
       (vf.c/ecs-enqueue w event-desc)))))

;; We put `-setup-world` here because it uses some of the macros.
(defn -setup-world
  [w]
  (vy.u/debug :setting-up-world)

  #_(vf.c/vybe-setup-allocator)

  ;; Watch for :vf/unique adds.
  (with-observer w [:vf/name :vf.observer/unique
                    :vf/events #{:add}
                    _ :vf/unique
                    e1 :vf/entity]
    (when-not (= e1 (ent w :vf/unique))
      (do
        (merge w {e1 [(flecs/EcsCanToggle)]})
        #_(vy.u/debug :setup-world-obs :e1 (get-rep e1))
        ;; Then watch for the entities that uses :vf/unique so we can
        ;; remove it from other entities.
        (with-observer w [:vf/name (vf/path [:vf.observer/unique (str "obs-" (vf/eid w e1))])
                          :vf/events #{:add}
                          _ e1
                          e2 :vf/entity]
          (if (= e1 e2)
            (do #_(vy.u/debug :setup-world-obs :disable (get-rep e1) (get-rep e2) (eid w e1)  (eid w e2))
                (vf.c/ecs-enable-id w (eid w e1) (eid w e2) false))
            (do
              (vy.u/debug :setup-world-obs :e1 (get-rep e1) :e2 (get-rep e2))
              (with-query w [_ e1
                             e3 :vf/entity]
                (when-not (= e3 e2)
                  (vy.u/debug :setup-world-obs-remove-e1-from-e3
                              :e1 (get-rep e1)
                              :e2 (get-rep e2)
                              :e3 (get-rep e3))
                  (disj e3 e1)))))))))

  (-> w
      (assoc :vf/unique [:vf/trait :vf/print-disabled]
             :vf.observer/unique [:vf/print-disabled]))

  w)

(defn enable
  "Enable an entity or one of its components.

  Forms:
    - (enable em)        ; remove :vf/disabled flag from the entity wrapper
    - (enable em c)      ; enable a specific component on the entity
    - (enable w e c)     ; world + entity designator + component designator

  The function ultimately calls `ecs-enable-id` with `true` to enable the
  entity/component in Flecs." 
  ([^VybeFlecsEntitySet em]
   (disj em :vf/disabled))
  ([^VybeFlecsEntitySet em c]
   (enable (.w em) (.id em) c))
  ([w e c]
   (vf.c/ecs-enable-id w (eid w e) (eid w c) true)))

(defn disable
  "Disable an entity or one of its components.

  Forms:
    - (disable em)        ; mark the wrapper with :vf/disabled
    - (disable em c)      ; disable a specific component on the entity
    - (disable w e c)     ; world + entity designator + component designator

  Calls `ecs-enable-id` with `false` to disable the entity/component in
  Flecs." 
  ([^VybeFlecsEntitySet em]
   (conj em :vf/disabled))
  ([^VybeFlecsEntitySet em c]
   (disable (.w em) (.id em) c))
  ([w e c]
   (vf.c/ecs-enable-id w (eid w e) (eid w c) false)))

(defn rest-enable!
  "Import and configure Flecs REST/Stats modules and activate the REST endpoint.

  This helper imports the required Flecs native modules and sets up the REST
  ID used by the Flecs explorer. Call it when you want to expose a running
  world's runtime information to the Flecs web-based inspector." 
  [w]
  (doto w
    (vf.c/ecs-import-c (flecs/FlecsRestImport$address) "FlecsRest")
    (vf.c/ecs-import-c (flecs/FlecsStatsImport$address) "FlecsStats"))
  (vf.c/ecs-set-id w
                   (flecs/FLECS_IDEcsRestID_)
                   (flecs/FLECS_IDEcsRestID_)
                   (.byteSize (.layout vf/Rest))
                   (vf/Rest))
  #_(vf.c/vybe-rest-enable w)
  w)

(defn _
  "Generate a fresh anonymous keyword under the `vf` namespace."
  []
  (keyword "vf" (str (gensym "ANOM_"))))

(defn debug-level!
  "Set the global Flecs log verbosity level (-1 through 3)."
  [n]
  (vf.c/ecs-log-set-level n))

(defn- -term->components
  [w {:keys [id]}]
  (set
   (if (vf/pair? id)
     [(vf/->comp w (vf/pair-first w id))
      (vf/->comp w (vf/pair-second w id))
      (vf/->comp w id)]
     [(vf/->comp w id)])))

(defn- -adapt-term
  [w]
  (fn [{:keys [id] :as term}]
    {:entities (-term->components w term)
     :pair? (vf/pair? id)
     :term-str (vp/->string (vf.c/ecs-term-str w term))}))

(defn systems-debug
  "Gather a map summarizing all user-defined systems, observers, and queries.

  Builds temporary queries to extract each entity's terms and timing metadata."
  [w]
  (->> (vf/with-query w [_ (flecs/EcsSystem)
                         e :vf/entity]
         (when-not (str/starts-with? (vf/get-name e) "flecs")
           (let [{:keys [time_spent query]} (-> (vf.c/ecs-system-get w (VybeFlecsEntitySet/.id e))
                                                (vp/as vf/system_t))]
             [(vf/get-rep e)
              {:type :system
               :time-spent time_spent
               :terms (->> (filter #(vf.c/ecs-term-ref-is-set %)
                                   (-> query
                                       (vp/as vf/query_t)
                                       :terms))
                           (mapv (-adapt-term w)))}])))
       (concat (vf/with-query w [_ (flecs/EcsObserver)
                                 _ [:not {:flags #{:up :self}}
                                    [:vf/child-of (flecs/EcsFlecsCore)]]
                                 e :vf/entity]
                 (when-not (str/starts-with? (vf/get-name e) "flecs")
                   (let [{:keys [query]} (-> (vf.c/ecs-observer-get w (VybeFlecsEntitySet/.id e))
                                             (vp/as vf/observer_t))]

                     [(vf/get-rep e)
                      {:type :observer
                       :terms (->> (filter #(vf.c/ecs-term-ref-is-set %)
                                           (-> query
                                               (vp/as vf/query_t)
                                               :terms))
                                   (mapv (-adapt-term w)))}])))
               (vf/with-query w [_ (flecs/EcsQuery)
                                 _ [:not (flecs/EcsSystem)]
                                 _ [:not (flecs/EcsObserver)]
                                 _ [:not {:flags #{:up :self}}
                                    [:vf/child-of (flecs/EcsFlecsCore)]]
                                 e :vf/entity]
                 (when-not (str/starts-with? (vf/get-name e) "flecs")
                   (let [{:keys [terms]} (-> (vf.c/ecs-query-get w (VybeFlecsEntitySet/.id e))
                                             (vp/as vf/query_t))]

                     [(vf/get-rep e)
                      {:type :query
                       :terms (->> (filter #(vf.c/ecs-term-ref-is-set %)
                                           terms)
                                   (mapv (-adapt-term w)))}]))))
       (remove nil?)
       (into {})))

#_ (let [stats (system_stats_t)]
     (vf.c/ecs-system-stats-get w (.id e) stats)
     stats)
#_ (do (require '[vybe.raylib :as vr])
       (def w noel/w))
#_ (vr/t (vf/systems-debug w))

(defn entity-debug
  "Return which systems/observers reference the supplied entity.

  Wraps `systems-debug` and filters the `:used-by` relations for the entity's id."
  ([^VybeFlecsEntitySet em]
   (when (some? em)
     (entity-debug (.w em) (.id em))))
  ([w e]
   (when (some? e)
     (let [m (vf/systems-debug w)]
       {:used-by
        (->> m
             (reduce (fn [acc [system-n {:keys [terms] :as _info}]]
                       (let [entities-set (set (map #(vf/eid w %)
                                                    (mapcat :entities terms)))]
                         (if (contains? entities-set (vf/eid w e))
                           (conj acc system-n)
                           acc)))
                     []))}))))
#_ (do (require '[vybe.raylib :as vr])
       (def w noel/w))
#_ (vr/t (vf/entity-debug (w vybe.type/AnimationPlayer)))
#_ (vr/t (vf/entity-debug (w vybe.jolt/VyBody)))
#_ (vr/t (vf/entity-debug (w :vg/camera)))
