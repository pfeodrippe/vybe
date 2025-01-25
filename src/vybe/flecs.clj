(ns vybe.flecs
  {:clj-kondo/ignore [:unused-value]}
  (:refer-clojure :exclude [ref])
  (:require
   [clojure.pprint :as pp]
   [clojure.set :as set]
   [clojure.string :as str]
   [meta-merge.core :as meta-merge]
   [potemkin :refer [def-map-type deftype+]]
   [vybe.c :as vc]
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.panama :as vp]
   [vybe.util :as vy.u])
  (:import
   (vybe.panama VybeComponent VybePMap IVybeWithComponent IVybeWithPMap IVybeMemorySegment)
   (org.vybe.flecs flecs ecs_entity_desc_t ecs_component_desc_t ecs_type_info_t
                   ecs_iter_t ecs_query_desc_t ecs_app_desc_t EcsRest EcsDocDescription
                   ecs_iter_action_t ecs_iter_action_t$Function ecs_event_desc_t
                   ecs_os_api_log_t ecs_os_api_log_t$Function
                   ecs_os_api_abort_t ecs_os_api_abort_t$Function
                   ecs_os_api_t ecs_ref_t ecs_system_t ecs_query_t
                   ecs_system_stats_t ecs_query_stats_t ecs_system_desc_t
                   ecs_observer_t)
   (java.lang.foreign AddressLayout MemoryLayout$PathElement MemoryLayout
                      ValueLayout ValueLayout$OfDouble ValueLayout$OfLong
                      ValueLayout$OfInt ValueLayout$OfBoolean ValueLayout$OfFloat
                      ValueLayout$OfByte ValueLayout$OfShort
                      StructLayout MemorySegment PaddingLayout SequenceLayout
                      UnionLayout FunctionDescriptor Linker SegmentAllocator)
   (java.lang.invoke MethodHandles MethodHandle)))

;; -- Flecs types
(vp/defcomp ecs_type_t (org.vybe.flecs.ecs_type_t/layout))
(vp/defcomp ecs_observer_desc_t (org.vybe.flecs.ecs_observer_desc_t/layout))

(vp/defcomp observer_t (ecs_observer_t/layout))
(vp/defcomp iter_t (ecs_iter_t/layout))
(vp/defcomp query_desc_t (ecs_query_desc_t/layout))
(vp/defcomp app_desc_t (ecs_app_desc_t/layout))
(vp/defcomp event_desc_t (ecs_event_desc_t/layout))
(vp/defcomp system_t (ecs_system_t/layout))
(vp/defcomp system_stats_t (ecs_system_stats_t/layout))
(vp/defcomp query_t (ecs_query_t/layout))
(vp/defcomp ref_t (ecs_ref_t/layout))
(vp/defcomp os_api_t (ecs_os_api_t/layout))
(vp/defcomp system_desc_t (ecs_system_desc_t/layout))
(vp/defcomp entity_desc_t (ecs_entity_desc_t/layout))

(vp/defcomp DocDescription (EcsDocDescription/layout))
(vp/defcomp Identifier (org.vybe.flecs.EcsIdentifier/layout))
(vp/defcomp Rest (EcsRest/layout))

(set! *warn-on-reflection* true)

(defprotocol IVybeName
  (vybe-name [e]))

(defn long-callback
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

    :vf/union
    {:doc "Maps to EcsUnion"}

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
   :vf/union (flecs/EcsUnion)
   :vf/exclusive (flecs/EcsExclusive)
   :vf/trait (flecs/EcsTrait)
   :vf/disabled (flecs/EcsDisabled)
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

(defn- -entity-components
  [wptr e-id]
  (let [{:keys [array count]} (-> (vf.c/ecs-get-type wptr e-id)
                                  (vp/p->map ecs_type_t))]
    (->> (range count)
         (keep (fn [^long idx]
                 (let [c-id (.getAtIndex ^MemorySegment array ValueLayout/JAVA_LONG idx)
                       *c-cache (delay (->comp-rep wptr c-id))]
                   (cond
                     ;; Exclude from printing.
                     (contains? #{on-instantiate-inherit-id doc-description-name-id} c-id)
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
                     (-get-c wptr e-id  @*c-cache)

                     :else
                     @*c-cache))))
         (remove nil?)
         set)))
#_ (let [wptr (vf/-init)
         c Position]
     (vf/-set-c wptr :bob [:walking
                           (Position {:x 10512 :y -4})
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
       (if (some? e)
         (cond
           (vector? e)
           (if (and (not= (vf.c/ecs-lookup-symbol this (vybe-name (first e)) true false)  0)
                    (not= (vf.c/ecs-lookup-symbol this (vybe-name (second e)) true false) 0)
                    (not= (vf.c/ecs-is-valid this (-ecs-pair
                                                   (eid this (first e))
                                                   (eid this (second e))))
                          0))
             (ent this e)
             default-value)

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
    (VybeFlecsWorldMap. (.mem_segment this) mta))

  IVybeMemorySegment
  (mem_segment [_] -wptr)

  clojure.lang.IPersistentCollection
  (equiv [this x]
         (and (instance? VybeFlecsWorldMap x)
              (= (.mem_segment this) (.mem_segment ^VybeFlecsWorldMap x))))

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
  "It will start and stop a world. Useful for tests."
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
  "It will start and stop a world without setup. Useful for tests."
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
  "Returns an VybeFlecsEntitySet.

  To get the ID, see `eid`."
  ^VybeFlecsEntitySet [w e]
  (VybeFlecsEntitySet. w (eid w e)))
#_ (vf/make-entity (vf/make-world) :a)

(defn entity?
  "Check if value is a VybeFlecsEntitySet"
  [v]
  (instance? VybeFlecsEntitySet v))

(defn entity-get-id
  [^VybeFlecsEntitySet v]
  (.id v))

(defn pair?
  "Check if entity is pair.

  `e` can be an id or a VybeFlecsEntitySet."
  [e]
  (if (entity? e)
    (vf.c/ecs-id-is-pair (entity-get-id e))
    (vf.c/ecs-id-is-pair e)))

(defn pair-first
  "Get relationship entity."
  ([^VybeFlecsEntitySet em]
   (pair-first (.w em) (.id em)))
  ([w e]
   (vf.c/vybe-pair-first w e)))

(defn pair-second
  "Get target entity."
  ([^VybeFlecsEntitySet em]
   (pair-second (.w em) (.id em)))
  ([w e]
   (vf.c/vybe-pair-second w e)))

(defn target
  "Get target data for entity."
  ([^VybeFlecsEntitySet em rel]
   (target em rel 0))
  ([^VybeFlecsEntitySet em rel idx]
   (target (.w em) (.id em) rel idx))
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
    #_(println :Sss k)
    (or (get @*name-cache k)
        (let [s (-> (symbol k)
                    str
                    (str/replace #"\." "_DOT_")
                    (str/replace #"/" "_SLASH_")
                    (str/replace #"-" "_DASH_"))]
          (swap! *name-cache assoc k s)
          s)))

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
  "Check if entity is still valid."
  ([^VybeFlecsEntitySet em]
   (valid? (.w em) (.id em)))
  ([w e]
   (vf.c/ecs-is-valid w (vf/eid w e {:create-entity false}))))

(defn alive?
  "Check if entity is still alive."
  ([^VybeFlecsEntitySet em]
   (alive? (.w em) (.id em)))
  ([w e]
   (vf.c/ecs-is-alive w (vf/eid w e {:create-entity false}))))

(defn eid
  "Creates or refers an entity. Returns the ID of the entity.

  For the VybeFlecsEntitySet instance, see `ent`."
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
           (let [id (-ecs-pair (eid wptr (first e) opts)
                               (eid wptr (second e) opts))]
             id)

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
                              (or (get builtin-entities e)
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
     (when (zero? id)
       (throw (ex-info "`eid` would return `0`"
                       {:e e
                        :opts opts})))
     id)))
#_ (let [wptr (vf/-init)]
     [(vf/eid wptr :a)
      (vf/eid wptr :b)
      (Position {:x 10})])

;; -- Low-level only.
(defn -override
  [wptr e]
  (bit-or (flecs/ECS_AUTO_OVERRIDE) (eid wptr e)))

(declare path)

(defn -set-c
  [wptr e coll]
  (let [e-id (eid wptr e)]
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
  (let [e-id (eid wptr e)]
    (mapv (fn [v]
            (vf.c/ecs-remove-id wptr e-id (eid wptr v)))
          (if (sequential? coll)
            coll
            [coll]))))

(defn -get-c
  [w e c]
  (let [e-id (eid w e)
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
  "Data-driven op for making a component overridable,
  see https://www.flecs.dev/flecs/md_docs_2Manual.html#automatic-overriding

  Use like

    (vf/override (Position {:x 10}))"
  [e]
  {:vf.op/override e})

(defn del
  "Data-driven component removal for an entity or for the entity itself.

  You can use like

     ;; Deletes the component.
     {(vg/body-path body) (vf/del :my-component)}

  or

     ;; Deletes the entity itself.
     {(vg/body-path body) (vf/del)}"
  ([]
   :vf.op/del)
  ([c]
   {:vf.op/del c}))

(defn sym
  "Data-driven setting of a symbol for an entity"
  [c]
  {:vf.op/sym c})

(defn is-a
  "See https://www.flecs.dev/flecs/md_docs_2Manual.html#inheritance

  E.g.

     (vf/is-a :spaceship)"
  [e]
  [:vf/is-a e])

(defn child-of
  "E.g.

     (vf/child-of :spaceship)"
  [e]
  [:vf/child-of e])

(defn slot-of
  "E.g.

     (vf/slot-of :spaceship)"
  [e]
  [:vf/slot-of e])

#_(declare vybe-flecs-ref-rep)

(defn modified!
  "Mark a component entity as modified. Useful, for example, when you are
  mutating pointers and Flecs does not have any hint that it's being modified."
  ([^VybeFlecsEntitySet em c]
   (vf.c/ecs-modified-id (.w em) (.id em) c))
  ([w e c]
   (vf.c/ecs-modified-id w (vf/eid w e) (vf/eid w c))))

(defn ref-get
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
  "Creates a cached reference (check Flecs's ecs_ref_init_id) to an component in
  an entity. Useful for components that need to be read/written often (e.g. for
  an animation system)."
  ([^VybeFlecsEntitySet em c]
   (ref (.w em) (.id em) c))
  ([w e c]
   (Ref {:flecs_ref (vf.c/ecs-ref-init-id w (vf/eid w e) (vf/eid w c))
         :vybe_component_id (:id (-get-c w c VybeComponentId))
         :w w})))

(defn get-internal-name
  "Retrieves flecs internal name."
  ([^VybeFlecsEntitySet em]
   (get-internal-name (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-get-name wptr (eid wptr e))
       vp/->string)))

(defn get-internal-path
  "Retrieves flecs internal path."
  ([^VybeFlecsEntitySet em]
   (get-internal-path (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-get-path-w-sep wptr 0 (eid wptr e) "." (flecs/NULL))
       vp/->string)))

(defn get-name
  "Retrieves entity name, returns a string."
  ([^VybeFlecsEntitySet em]
   (get-name (.w em) (.id em)))
  ([w e]
   (get-internal-path w e)))

(defn get-rep
  "Retrieves vybe representation.

  It returns a keyword if the entity has no parent; returns an expression
  of the form `(vybe.flecs/path [...])` if it has a parent."
  ([^VybeFlecsEntitySet em]
   (when em
     (get-rep (.w em) (.id em))))
  ([w e]
   (when e
     (-> (get-internal-path w e)
         -flecs->vybe))))

(defn get-symbol
  ([^VybeFlecsEntitySet em]
   (get-symbol (.w em) (.id em)))
  ([w e]
   (vp/->string (vf.c/ecs-get-symbol w (vf/eid w e)))))

(defn lookup-symbol
  "Returns an entity id (or nil)."
  [w s]
  (let [e-id (vf.c/ecs-lookup-symbol w s false false)]
    (when (pos? e-id)
      e-id)))

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

(defn type-str
  "Get the type of an entity in Flecs string format."
  ([^VybeFlecsEntitySet em]
   (type-str (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-type-str wptr (vf.c/ecs-get-type wptr (eid wptr e)))
       vp/->string)))

(defn children-ids
  "Get children of an entity."
  ([^VybeFlecsEntitySet em]
   (children-ids (.w em) (.id em)))
  ([w e]
   (let [it (vf.c/ecs-children w (eid w e))]
     (loop [acc []
            has-next? (vf.c/ecs-children-next it)]
       (if has-next?
         (recur (concat acc (mapv #(.getAtIndex ^MemorySegment (:entities it)
                                                ValueLayout/JAVA_LONG
                                                ^long %)
                                  (range (:count it))))
                (vf.c/ecs-children-next it))
         acc)))))

(defn children
  "Get children of an entity."
  ([^VybeFlecsEntitySet em]
   (children (.w em) (.id em)))
  ([w e]
   (->> (children-ids w e)
        (mapv #(ent w %)))))

(defn parent-id
  "Get parent ID of an entity."
  ([^VybeFlecsEntitySet em]
   (parent-id (.w em) (.id em)))
  ([w e]
   (when-let [e-id (eid w e {:create-entity false})]
     (let [id (vf.c/ecs-get-parent w e-id)]
       (when-not (zero? id)
         id)))))

(defn parent
  "Get parent of an entity."
  ([^VybeFlecsEntitySet em]
   (parent (.w em) (.id em)))
  ([w e]
   (some->> (parent-id w e) (ent w))))

(defn hierarchy
  "Get hierarchy (children and nested children without the components) of an entity."
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
  "Get hierarchy without showing entire children path, useful for debugging."
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

(defn get-world
  "`poly` can be a world, a stage or a query

  Returns a VyveFlecsWordMap."
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
  "Internal function to parse a query expr to a filter terms + additional info for the
  query/filter/rule descriptor. "
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
  "Parse a query expr into a query description (`ecs_query_desc_t`)."
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

(defn- -each-bindings-adapter
  [^VybeFlecsWorldMap w bindings+opts]
  (let [bindings (->> bindings+opts (remove (comp keyword? first)))
        query-expr (->> bindings
                        (mapv last)
                        (remove #{:vf/entity :vf/eid :vf/iter :vf/world :vf/event})
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
  "Runs operations in deferred mode."
  [w & body]
  `(try
     (vf.c/ecs-defer-begin ~w)
     ~@body
     (finally
       (vf.c/ecs-defer-end ~w))))

(defn iter-skip
  "Skip an iteration. It will prevent components of being marked as modified."
  [it]
  (vf.c/ecs-iter-skip it))

(defn iter-changed
  "Check if iter was modified."
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
  "Receives the world + some bindings (as in a `let`) for the
  components.

  -- Example --

  (let [w (vf/make-world #_{:debug true})
        {:syms [Position ImpulseSpeed]} (vp/make-components
                                         '{ImpulseSpeed [[:value :double]]
                                           Position [[:x :double] [:y :double]]})]
    (merge w {:a [(Position {:x -105.1}) :aaa]
              :b [(Position {:x 333.1}) (ImpulseSpeed 311)]
              :c [(Position {:x 0.1}) (ImpulseSpeed -43)]})
    (vf/with-query w [speed ImpulseSpeed
                     {:keys [x] :as pos} Position
                     e :vf/entity]
      [e (update pos :x dec) x (update speed :value inc)]))"
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
                                 :query (parse-query-expr w query-expr)}
                                (if always
                                  {:callback (-system-callback
                                              (fn [it-p]
                                                (let [it (vp/jx-p->map it-p ecs_iter_t)
                                                      f-idx (mapv (fn [f] (f it)) f-arr)]
                                                  (doseq [idx (range (ecs_iter_t/count it-p))]
                                                    (each-handler (mapv (fn [f] (f idx)) f-idx))))))}
                                  {:run (-system-callback
                                         (fn [it-p]
                                           #_(println :AAAA (:vf/name opts))
                                           (when (vf.c/ecs-query-changed (ecs_iter_t/query it-p))
                                             (while (vf.c/ecs-query-next it-p)
                                               (if (vf.c/ecs-iter-changed it-p)
                                                 (let [it (vp/jx-p->map it-p ecs_iter_t)
                                                       f-idx (mapv (fn [f] (f it)) f-arr)]
                                                   (doseq [idx (range (ecs_iter_t/count it-p))]
                                                     (each-handler (mapv (fn [f] (f idx)) f-idx))))
                                                 (vf.c/ecs-iter-skip it-p))))))}))))
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
  "Similar to `with-query`, see its documentation.

  The differences are that `with-system` requires a
  :vf/name (you put it in the bindings, see example) and it won't
  run the code in place, it will build a Flecs system instead that can be run
  with `system-run`.

  Required params are:
    - `:vf/name`, should be unique for this world, it can be a keyword, a string
      (`vf/path` outputs a string)

  Optional params are:
    - `:vf/phase`, pipeline phase, see https://github.com/SanderMertens/flecs/blob/v4/docs/Systems.md#builtin-pipeline
    - `:vf/always`, boolean, systems run only when its input are modified, if you
      need it to run for every `vf/system-run` (or `vf/progress`), set it to `true`

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
  "Run a system (which is just an entity)."
  [^VybeFlecsWorldMap w e]
  (vf.c/ecs-run w (eid w e) 0 vp/null))

(defn system-query-str
  ([^VybeFlecsEntitySet em]
   (system-query-str (.w em) (.id em)))
  ([w e]
   (-> (vf.c/ecs-system-get w (eid w e))
       (vp/p->map vf/system_t)
       :query
       vf.c/ecs-query-str
       vp/->string)))

(defn progress
  "Progress the world by running the systems."
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
                                         (let [it (vp/jx-p->map it ecs_iter_t)
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
  "Similar to `with-system`, but creates a Observer.

  `:vf/name` is required and `:vf/events` is optional.

  If the `bindings` contain a `[:event ...]`, it will list to the data from that event
  regardless of the associated component or entity.

  `:vf/events` can be any entity or a set of one or more of:

  - :add (maps to EcsOnAdd)
  - :set (maps to EcsOnSet)
  - :remove (maps to  EcsOnRemove)
  "
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
  "Define a var query.

  Also see `with-query`."
  [name w bindings & body]
  `(defn ~name [w#]
     (let [~w w#]
       (vf/with-query ~w ~bindings
         ~@body))))

(defmacro defobserver
  "Define a observer.

  Call it with `(my-observer w)`.

  Also see `with-observer`."
  [name w bindings & body]
  `(defn ~name [w#]
     (let [~w w#]
       (vf/with-observer ~w ~(concat [:vf/name (keyword (str *ns* "/" name))]
                                     bindings)
         ~@body))))

(defmacro defsystem
  "Define a system.

  Call it with `(my-system w)`.

  Also see `with-system`."
  [name w bindings & body]
  `(defn ~name [w#]
     (let [~w w#]
       (vf/with-system ~w ~(concat [:vf/name (keyword (str *ns* "/" name))]
                                   bindings)
         ~@body))))

(defmacro ^:private -field
  [it c idx]
  `(-> (vf.c/ecs-field-w-size ~it (vp/sizeof ~c) ~idx)
       (vp/as [:* ~c])))

(def c-w
  "C symbol for the world inside a C system."
  'w--)

(defmacro defsystem-c
  "Like `defsystem`, but will use the VybeC compiler (clang
  is necessary for the first compilation, a dynamic lib will be created).

  VybeC is a subset of Clojure to be compiled to C as direct as possible."
  {:clj-kondo/ignore [:type-mismatch]}
  [sys-name w bindings & body]
  (let [bindings (mapv (fn [[k v]]
                         [k v])
                       (partition 2 bindings))
        bindings-only-valid (->> bindings
                                 (remove (comp keyword? first))
                                 vec)
        i 'vybe_c_i
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
                    flags @*flags]
                ;; We return a vector of vectors because of the
                ;; destructuring (note that we use `(apply concat)` below.
                (cond
                  (:vf/entity flags)
                  ;; TODO maybe
                  [(with-meta [(symbol (str k "--arr"))
                               ;; TODO support `:maybe`
                               `(vf.c/ecs-field-src ~it ~idx)]
                     {:idx idx
                      :type nil
                      :sym k
                      :flags flags})]

                  (and (vector? v)
                       #_(some #{:* :_} v))
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
                                                        `(vf.c/vybe-pair-second ~w (vf.c/ecs-field-id ~it ~idx))
                                                        `(vf.c/vybe-pair-first ~w (vf.c/ecs-field-id ~it ~idx)))]
                                            {:idx idx
                                             :type nil
                                             :sym k-each
                                             :flags flags}))))
                         (remove nil?)
                         vec)
                    [(with-meta [(symbol (str k "--arr"))
                                 ;; TODO support `:maybe`
                                 `(vf.c/ecs-field-id ~it ~idx)]
                       {:idx idx
                        :type nil
                        :sym k
                        :flags flags})])

                  ;; Tag branch.
                  (keyword? v)
                  [(with-meta [(symbol (str k "--arr"))
                               ;; TODO support `:maybe`
                               `(vf.c/ecs-field-id ~it ~idx)]
                     {:idx idx
                      :type nil
                      :sym k
                      :flags flags})]

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
                       {:idx idx
                        :type v
                        :sym k-sym
                        :binding-form k
                        :flags flags})])))))

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
         (let [~w (:world @~it)
               ~c-w ~w]
           (let ~ (->> bindings-processed
                       (apply concat)
                       vec)
             (doseq [~i (range (:count @~it))]
               (let ~ (->> bindings-processed
                           (mapv (fn [k-and-v]
                                   (let [{:keys [sym flags binding-form type]} (meta k-and-v)
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
                                                    `(if (= ~k vp/null)
                                                       (vp/as vp/null [:* ~type])
                                                       (vp/& (nth ~k ~i)))
                                                    `(vp/& (nth ~k ~i)))]
                                         (if (map? binding-form)
                                           ;; Map destructuring.
                                           (vec (concat ['res-internal-- form]
                                                        (->> (:keys binding-form)
                                                             (mapcat #(vector % (list (keyword %)
                                                                                      '@res-internal--))))))
                                           ;; No destructuring.
                                           [sym form]))
                                       ;; Not a component branch.
                                       ;; TODO support `:maybe`
                                       [sym k]))))
                           (apply concat)
                           vec)
                 ~@body)))))

       ;; Defined system builder.
       (defn ~sys-name
         [w#]
         (def ~'w w#)
         #_ w
         (let [q# (vf/parse-query-expr w# ~(mapv second bindings-only-valid))

               e# (vf.c/ecs-entity-init w# (vf/entity_desc_t
                                            {:name (vf/vybe-name (keyword (symbol #'~sys-name)))
                                             :add (-> [(vf.c/vybe-pair (flecs/EcsDependsOn)
                                                                       (flecs/EcsOnUpdate))
                                                       ;; This `0` is important so Flecs can know
                                                       ;; the end of the array.
                                                       0]
                                                      (vp/arr :long-long))}))
               system-desc# (vf/system_desc_t
                             {:entity e#
                              :callback (vp/mem ~(symbol (str sys-name "--internal")))
                              :query q#})]
           (vf/ent w# (vf.c/ecs-system-init w# system-desc#)))))))

(defonce ^:private lock (Object.))

(defn event!
  "Enqueue an event for an entity (entity set or id).
  For the event to be useful for your game, it should be listened by a
  observer."
  ([w-or-em event]
   (if (instance? VybeFlecsEntitySet w-or-em)
     (let [^VybeFlecsEntitySet em w-or-em]
       (event! (.w em) (.id em) event))
     ;; Event with no entity.
     (event! w-or-em :vf/_ event)))
  ([w e event]
   ;; We don't want to emit events in parallel (or any other Flecs operation).
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
  "Enable a component for an entity or enable an entity."
  ([^VybeFlecsEntitySet em]
   (disj em :vf/disabled))
  ([^VybeFlecsEntitySet em c]
   (enable (.w em) (.id em) c))
  ([w e c]
   (vf.c/ecs-enable-id w (eid w e) (eid w c) true)))

(defn disable
  "Disable a component for an entity or disable an entity."
  ([^VybeFlecsEntitySet em]
   (conj em :vf/disabled))
  ([^VybeFlecsEntitySet em c]
   (disable (.w em) (.id em) c))
  ([w e c]
   (vf.c/ecs-enable-id w (eid w e) (eid w c) false)))

(defn rest-enable!
  "Enable rest API.

  Check https://www.flecs.dev/explorer."
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
  "Used for creating anonymous entities."
  []
  (keyword "vf" (str (gensym "ANOM_"))))

(defn debug-level!
  "`n` goes from -1 to 3."
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
  "Retrieve information about the systems (systems and observers).

  This creates a new query."
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
  "Retrieve information about an entity, e.g. which systems/observers are
  using it."
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
