(ns vybe.flecs
  {:clj-kondo/ignore [:unused-value :missing-test-assertion]}
  (:require
   [clojure.edn :as edn]
   [vybe.flecs.c :as vf.c]
   [vybe.flecs :as vf]
   [vybe.type :as vt]
   [vybe.panama :as vp]
   [clojure.string :as str]
   [potemkin :refer [def-map-type deftype+]]
   [clojure.pprint :as pp]
   [clojure.set :as set]
   #_[clj-java-decompiler.core :refer [decompile disassemble]]
   [meta-merge.core :as meta-merge]

   [clojure.test :refer [deftest is testing]])
  (:import
   (vybe.panama VybeComponent VybePMap IVybeWithComponent IVybeMemorySegment)
   (org.vybe.flecs flecs ecs_entity_desc_t ecs_component_desc_t ecs_type_info_t
                   ecs_iter_t ecs_query_desc_t
                   ecs_iter_action_t ecs_iter_action_t$Function)
   (java.lang.foreign AddressLayout MemoryLayout$PathElement MemoryLayout
                      ValueLayout ValueLayout$OfDouble ValueLayout$OfLong
                      ValueLayout$OfInt ValueLayout$OfBoolean ValueLayout$OfFloat
                      ValueLayout$OfByte ValueLayout$OfShort
                      StructLayout MemorySegment PaddingLayout SequenceLayout
                      UnionLayout FunctionDescriptor Linker SegmentAllocator)
   (java.lang.invoke MethodHandles MethodHandle)))

;; -- Flecs types
(vp/defcomp ecs_type_t (org.vybe.flecs.ecs_type_t/layout))
(vp/defcomp ecs_system_desc_t (org.vybe.flecs.ecs_system_desc_t/layout))

(set! *warn-on-reflection* true)

(definterface LongCallback
  (^long apply []))

(defn long-callback
  [f]
  (let [desc (FunctionDescriptor/of
              ValueLayout/JAVA_LONG
              (into-array MemoryLayout []))
        linker (Linker/nativeLinker)
        f (reify LongCallback
            (apply [_]
              (f)))
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

(extend-protocol vt/IVybeName
  #_ #_clojure.lang.Var
  (vybe-name [v]
    (str "V_" (-> v
                  symbol
                  str
                  (str/replace #"\." "_"))))

  VybeComponent
  (vybe-name [v]
    (str "C_" (-> (.get (.name ^MemoryLayout (.layout v)))
                  (str/replace #"\." "_"))))

  clojure.lang.Keyword
  (vybe-name [k]
    (-> (symbol k)
        str
        (str/replace #"\." "_")))

  #_ #_clojure.lang.Symbol
  (vybe-name [sym]
    (str "S_" (-> sym
                  (str/replace #"\." "_")))))

(declare ent)

(vp/defcomp Position
  [[:x :double]
   [:y :double]])

#_ (ex-1)

;; -- Main API.
(declare -set-c)
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
   :vf/is-a (flecs/EcsIsA)
   :vf/prefab (flecs/EcsPrefab)
   :* (flecs/EcsWildcard)
   :_ (flecs/EcsAny)})

(def builtin-entities-rev
  (set/map-invert builtin-entities))

(defn- -world-entities
  [wptr]
  (let [e ::entity
        it (vf.c/ecs-each-id wptr (ent wptr e))
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

(defn- ->comp
  [wptr v]
  (or (get builtin-entities-rev v)
      (vp/comp-cache (:id (-get-c wptr v VybeComponentId)))))

(defn- -entity-components
  [wptr e-id]
  (let [{:keys [array count]} (-> (vf.c/ecs-get-type wptr e-id)
                                  (vp/p->map ecs_type_t))]
    (->> (range count)
         (keep (fn [^long idx]
                 (let [c-id (.getAtIndex ^MemorySegment array ValueLayout/JAVA_LONG idx)
                       *c-cache (delay (->comp wptr c-id))]
                   (cond
                     (vf.c/ecs-id-is-pair c-id)
                     (let [[a b] [(->comp wptr (vf.c/vybe-pair-first wptr c-id))
                                  (->comp wptr (vf.c/vybe-pair-second wptr c-id))]]
                       (when (some some? [a b])
                         [(or a (vf.c/vybe-pair-first wptr c-id))
                          (or b (vf.c/vybe-pair-second wptr c-id)) ]))

                     (instance? VybeComponent @*c-cache)
                     (-get-c wptr e-id (->comp wptr c-id))

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
     (-entity-components wptr (ent wptr :bob)))

(defn- -ecs-pair
  "Receives two ids (numbers) and returns the id of the pair."
  [id-1 id-2]
  (vf.c/vybe-pair id-1 id-2))

(defn- -get-path
  "Retrieve flecs internal path."
  [wptr e]
  (-> (vf.c/ecs-get-path-w-sep wptr 0 (ent wptr e) "." (flecs/NULL))
      vp/->string))

;; -- Types.
(definterface IVybeFlecsEntityMap
  (^long id []))

(declare vybe-flecs-world-map-rep)
(declare make-entity)

(def-map-type VybeFlecsWorldMap [-wptr mta]
  (get [this c default-value]
       (if (some? c)
         (if (vector? c)
           (if (and (not= (vf.c/ecs-lookup-symbol this (vt/vybe-name (first c)) false false)  0)
                    (not= (vf.c/ecs-lookup-symbol this (vt/vybe-name (second c)) false false) 0)
                    (not= (vf.c/ecs-is-valid this (-ecs-pair
                                                   (ent this (first c))
                                                   (ent this (second c))))
                          0))
             (make-entity this c)
             default-value)

           (let [e-id (vf.c/ecs-lookup-symbol this (vt/vybe-name c) false false)]
             (if (not= e-id 0)
               (make-entity this e-id)
               default-value)))
         default-value))
  (assoc [this k v]
         #_(println :WORLD k v)
         (-set-c this k v)
         this)
  (dissoc [this k]
          (vf.c/ecs-delete this (ent this k))
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
       (remove (if (-> this meta :show-all) (constantly false) (comp empty? val)))
       (into {})))

(defmethod print-method VybeFlecsWorldMap
  [^VybeFlecsWorldMap o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybeFlecsWorldMap
  [^VybeFlecsWorldMap o]
  (pp/simple-dispatch (vybe-flecs-world-map-rep o)))

(defn make-world
  (^VybeFlecsWorldMap []
   (make-world {}))
  (^VybeFlecsWorldMap [mta]
   (VybeFlecsWorldMap. (-init) mta)))
#_ (vf/make-world)

(definterface IVybeFlecsWorldMap
  (^vybe.flecs.VybeFlecsWorldMap w []))

(declare vybe-flecs-entity-set-rep)

(deftype+ VybeFlecsEntitySet [-w -id]
  clojure.lang.IPersistentSet
  (seq [this]
    (seq (-entity-components (.w this) (.id this))))
  (cons [this c]
    (assoc (.w this) this [c])
    this)
  (get [this c]
    (if (= c :vf/id)
      -id
      (-get-c (.w this) (.id this) c)))
  (disjoin [this c]
    (let [w (.w this)]
      (vf.c/ecs-remove-id w (.id this) (ent w c)))
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
     (-> (VybeFlecsEntitySet. w (ent w :vvv))
         (conj :a)
         (disj :a)
         (conj (Position {:x 11}))
         #_(disj Position))
     (get-in w [:vvv Position])
     #_(update w :vvv disj Position)
     #_(conj aaa (Position {:x 10}))
     w)

(declare children)
(defn- vybe-flecs-entity-set-rep
  [^VybeFlecsEntitySet this]
  (if-let [debug (:debug (meta (.w this)))]
    (let [adapter (if (fn? debug)
                    debug
                    identity)]
      (adapter
       (merge {:vf/id (.id this)
               :vf/name (-get-path (.w this) (.id this))
               :vf/value (-entity-components (.w this) (.id this))}
              (when-let [e-children (seq (children this))]
                {:vf/children (vec e-children)}))))
    (some-> (-entity-components (.w this) (.id this))
            #_(conj {:vf/id (.id this)}))))

(defmethod print-method VybeFlecsEntitySet
  [^VybeFlecsEntitySet o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybeFlecsEntitySet
  [^VybeFlecsEntitySet o]
  (pp/simple-dispatch (vybe-flecs-entity-set-rep o)))

(defn make-entity
  ^VybeFlecsEntitySet [w e]
  (VybeFlecsEntitySet. w (ent w e)))
#_ (vf/make-entity (vf/make-world) :a)

;; -- Operators.
(def ^:private skip-meta
  #{::entity
    :vybe.flecs.type/component
    :vybe.flecs.type/keyword
    :vybe.flecs.type/pair
    VybeComponentId})

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

(defn ent
  "Creates or refers an entity.

  Returns the ID of the entity."
  [wptr e]
  #_ (when-not (int? e)
       (println :e e))
  (cond
    (int? e)
    e

    (instance? VybeFlecsEntitySet e)
    (.id ^VybeFlecsEntitySet e)

    (instance? IVybeWithComponent e)
    (ent wptr (.component ^IVybeWithComponent e))

    :else
    (or (when-let [id (get-in @*world->cache [(vp/mem wptr) e])]
          (when (vf.c/ecs-is-valid wptr id)
            id))

        (let [#_ #__ (println :___ENT e)
              e-id (cond
                     (instance? VybeComponent e)
                     (let [^MemoryLayout layout (.layout ^VybeComponent e)
                           name (vt/vybe-name e)
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
                       e-id)

                     (vector? e)
                     (let [id (-ecs-pair (ent wptr (first e))
                                         (ent wptr (second e)))]
                       #_(vp/cache-comp e)
                       #_(-add-meta wptr e id :vybe.flecs.type/pair)
                       #_(-cache-entity wptr e id)
                       id)

                     (keyword? e)
                     (or (get builtin-entities e)
                         (let [sym (vt/vybe-name e)
                               e-id (vf.c/ecs-lookup-symbol wptr sym false false)]
                           (if (not= e-id 0)
                             e-id
                             (let [id (vf.c/ecs-set-name wptr 0 sym)]
                               (vf.c/ecs-set-symbol wptr id sym)
                               (vp/cache-comp e)
                               (-add-meta wptr e id :vybe.flecs.type/keyword)
                               (-cache-entity wptr e id)
                               id))))

                     :else
                     (throw (ex-info "Unsupported entity type" {:type (type e)
                                                                :value e})))]
          (when-not (skip-meta e)
            (vf.c/ecs-add-id wptr e-id (ent wptr ::entity)))
          (swap! *world->cache assoc-in [(vp/mem wptr) e] e-id)
          e-id))))
#_ (let [wptr (vf/-init)]
     [(vf/ent wptr :a)
      (vf/ent wptr :b)
      (Position {:x 10})])

;; -- Low-level only.
(defn -override
  [wptr e]
  (bit-or (flecs/ECS_OVERRIDE) (ent wptr e)))

(defn -set-c
  [wptr e coll]
  #_(println :set-c e coll)
  (mapv (fn [v]
          (cond
            (instance? VybePMap v)
            (let [^VybePMap v v
                  ^MemorySegment mem-segment (.mem_segment v)]
              (vf.c/ecs-set-id wptr (ent wptr e) (ent wptr (.component v))
                               (.byteSize mem-segment)
                               mem-segment))

            (and (vector? v) (vp/pmap? (first v)))
            (let [^VybePMap v' (first v)
                  ^MemorySegment mem-segment (.mem_segment v')]
              (vf.c/ecs-set-id wptr (ent wptr e) (ent wptr v)
                               (.byteSize mem-segment)
                               mem-segment))

            (and (vector? v) (vp/pmap? (peek v)))
            (let [^VybePMap v (peek v)
                  ^MemorySegment mem-segment (.mem_segment v)]
              (vf.c/ecs-set-id wptr (ent wptr e) (ent wptr v)
                               (.byteSize mem-segment)
                               mem-segment))

            (map? v)
            (cond
              ('vf/override v)
              (do (-set-c wptr e (-override wptr ('vf/override v)))
                  (-set-c wptr e ('vf/override v)))

              :else
              ;; Child of hash map syntax.
              (mapv (fn [[nested-entity nested-components]]
                      (-set-c wptr nested-entity
                              (conj (vec (cond
                                           (nil? nested-components)
                                           []

                                           (sequential? nested-components)
                                           nested-components

                                           :else
                                           [nested-components]))
                                    [:vf/child-of e])))
                    v))

            :else
            (let [c-id (ent wptr v)]
              (vf.c/ecs-add-id wptr (ent wptr e) c-id)
              c-id)))
        (->> (if (sequential? coll)
               coll
               [coll])
             (remove nil?))))

(defn -remove-c
  [wptr e coll]
  (mapv (fn [v]
          (vf.c/ecs-remove-id wptr (ent wptr e) (ent wptr v)))
        (if (sequential? coll)
          coll
          [coll])))

(defn -get-c
  [wptr e c]
  (let [e-id (ent wptr e)
        c-id (ent wptr c)]
    (when (vf.c/ecs-has-id wptr e-id c-id)
      (if (vf.c/ecs-id-is-tag wptr c-id)
        c
        (vp/p->map (vf.c/ecs-get-id wptr e-id c-id)
                   (if (vector? c)
                     (if (instance? VybeComponent (first c))
                       (first c)
                       (last c))
                     c))))))
#_ (let [wptr (vf/-init)]
     (vf/-set-c wptr :bob [:walking
                           (Position {:x 10512 :y -4})
                           [(Position {:x 10512 :y -4}) :global]])
     (vp/defcomp Vel [[:a :double]])
     [(vf/-get-c wptr :bob Position)
      (vf/-get-c wptr :bob [ :global Position])])

;; -- High-level.
(defn override
  "Make a component overridable, usually used in prefabs,
  see https://www.flecs.dev/flecs/md_docs_2Manual.html#automatic-overriding

  Use like

    (vf/override (Position {:x 10}))"
  [e]
  {'vf/override e})

(defn is-a
  "See https://www.flecs.dev/flecs/md_docs_2Manual.html#inheritance

  E.g.

     (vf/is-a :spaceship)"
  [e]
  [:vf/is-a e])

(defn get-name
  "Retrieve flecs internal name."
  ([^VybeFlecsEntitySet em]
   (get-name (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-get-name wptr (ent wptr e))
       vp/->string)))

(defn type-str
  "Get the type of an entity in Flecs string format."
  ([^VybeFlecsEntitySet em]
   (type-str (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-type-str wptr (vf.c/ecs-get-type wptr (ent wptr e)))
       vp/->string)))

(defn children
  "Get children of an entity."
  ([^VybeFlecsEntitySet em]
   (->> (children (.w em) (.id em))
        (mapv #(make-entity (.w em) %))))
  ([wptr e]
   (let [it (vf.c/ecs-children wptr (ent wptr e))]
     (loop [acc []
            has-next? (vf.c/ecs-children-next it)]
       (if has-next?
         (recur (concat acc (mapv #(.getAtIndex ^MemorySegment (:entities it)
                                                ValueLayout/JAVA_LONG
                                                ^long %)
                                  (range (:count it))))
                (vf.c/ecs-children-next it))
         acc)))))

(def -parser-special-keywords
  #{:or :not :maybe :pair :meta :entity
    :filter :query
    :in :out :inout :none
    :notify :sync})

(defn -pair-id
  "Get id of the pair."
  [wptr c1 c2]
  (ent wptr [c1 c2]))

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
                      [{:id (ent wptr
                                 (case c
                                   (:* *) (flecs/EcsWildcard)
                                   (:_ _) (flecs/EcsAny)
                                   c))
                        :inout (flecs/EcsIn)}]
                      (let [{:keys [flags inout term]
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
                                     [(assoc (first (parse-one-expr (last args)))
                                             :oper (flecs/EcsNot))]

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

                                     :entity
                                     (do (swap! *additional-info assoc-in [:filter :entity] (ent wptr (last args)))
                                         nil)

                                     :filter
                                     (do (swap! *additional-info update :filter meta-merge/meta-merge metadata)
                                         nil)

                                     :query
                                     (do (swap! *additional-info update :query meta-merge/meta-merge metadata)
                                         nil)

                                     ;; Inout(s), see Access Modifiers in the Flecs manual.
                                     (:in :out :inout :none)
                                     (parse-one-expr (into [:meta {:inout (first c)}]
                                                           args))

                                     ;; Pair.
                                     (let [adapt #(case %
                                                     (:* *) (flecs/EcsWildcard)
                                                     (:_ _) (flecs/EcsAny)
                                                     %)]
                                       (if (= (first c) :pair)
                                         [{:id (-pair-id wptr
                                                         (adapt (first args))
                                                         (adapt (last args)))}]
                                         [{:id (-pair-id wptr
                                                         (adapt (first c))
                                                         (adapt (last args)))}])))]
                        (when result
                          #_(println result inout)
                          (cond-> (-> result
                                      (update 0 meta-merge/meta-merge term))
                            flags
                            (assoc-in [0 :src :id] (->> flags
                                                        (mapv {:up (flecs/EcsUp)
                                                               :cascade (flecs/EcsCascade)
                                                               :is-entity (flecs/EcsIsEntity)})
                                                        (apply (partial bit-or 0))))

                            (and inout (or (not (get-in result [0 :inout]))
                                           (= (get-in result [0 :inout])
                                              (flecs/EcsIn))))
                            (assoc-in [0 :inout] ({:in (flecs/EcsIn)
                                                   :out (flecs/EcsOut)
                                                   :inout (flecs/EcsInOut)
                                                   :none (flecs/EcsInOutNone)}
                                                  inout))))))))
          vec)

     :additional-info @*additional-info}))

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
       (update :order_by_component #(ent wptr %))))))
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

(defn- -each-bindings-adapter
  [^VybeFlecsWorldMap w bindings+opts]
  (let [bindings (->> bindings+opts (remove (comp keyword? first)))
        f-arr
        (->> (mapv last bindings)
             (reduce (fn [{:keys [idx] :as acc} c]
                       (let [c (if (and (vector? c) (contains? -parser-special-keywords (first c)))
                                 (case (first c)
                                   (:maybe :meta) (last c))
                                 c)]
                         (cond
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
                                         (fn [^VybePMap it]
                                           (let [p-arr (vf.c/ecs-field-w-size it byte-size idx)]
                                             (fn [^long idx]
                                               (when-not (vp/null? p-arr)
                                                 (-> (.asSlice ^MemorySegment p-arr (* idx byte-size) layout)
                                                     (vp/p->map c)))))))
                                 (update :idx inc)))

                           ;; Pair (tag).
                           (and (vector? c)
                                (some #{:* :_} c))
                           (-> acc
                               (update :coll conj
                                       (fn [^VybePMap it]
                                         (let [p-arr (vf.c/ecs-field-id it idx)
                                               [rel target] (when-not (vp/null? p-arr)
                                                              [(vf.c/vybe-pair-first w p-arr)
                                                               (vf.c/vybe-pair-second w p-arr)])]
                                           (fn [^long _idx]
                                             (when-not (vp/null? p-arr)
                                               (mapv #(->comp w %) [rel target]))))))
                               (update :idx inc))

                           :else
                           (-> acc
                               (update :coll conj
                                       (case c
                                         :vf/entity
                                         (fn [it]
                                           (let [^MemorySegment entities-arr (:entities it)]
                                             (fn [^long idx]
                                               (make-entity w (.getAtIndex entities-arr ValueLayout/JAVA_LONG idx)))))

                                         ;; TODO Need to check that the component exists
                                         ;; (as it could have came from a `:maybe`, for example.
                                         (fn [_it-m]
                                           (fn [_idx]
                                             c))))
                               (update :idx (if (= c :vf/entity)
                                              identity
                                              inc))))))
                     {:idx 0 :coll []})
             :coll)]
    {:opts (->> bindings+opts (filter (comp keyword? first)) (into {}))
     :f-arr f-arr
     :query-expr (->> bindings
                      (mapv last)
                      (remove #{:vf/entity})
                      vec)}))

(defonce *-each-cache (atom {}))

(defn -each
  [^VybeFlecsWorldMap w bindings+opts each-handler]
  (let [{:keys [_opts f-arr query-expr]} (-each-bindings-adapter w bindings+opts)
        wptr w
        q (-query wptr query-expr)
        *last-value (atom [])]
    (fn []
      (let [it (vf.c/ecs-query-iter wptr q)
            *acc (atom [])
            *idx (atom 0)]
        (while (vf.c/ecs-query-next it)
          (if #_(vf.c/ecs-iter-changed it) true
            (let [f-idx (mapv (fn [f] (f it)) f-arr)]
              (swap! *acc conj (mapv (fn [idx]
                                       (each-handler (mapv (fn [f] (f idx)) f-idx)))
                                     (range (:count it)))))
            (do (vf.c/ecs-iter-skip it)
                (swap! *acc assoc @*idx (get @*last-value @*idx))))
          (swap! *idx inc))
        (reset! *last-value @*acc)
        (vec (apply concat @*acc))))))

(comment

  ;; Wildcard query.
  (let [w (vf/make-world)
        A (vp/make-component 'A [[:x :double]])]
    (merge w {:b [(A {:x 34})
                  [:a :c]
                  [:a :d]]})
    (vf/with-each w [a A
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
    [(vf/with-each w [pos Translation
                      pos-global [Translation :global]
                      pos-parent [:maybe {:flags #{:up :cascade}}
                                  [Translation :global]]]
       (println :aaa)
       [pos pos-global pos-parent])

     (do (merge w {:b [(Translation {:x -400.1})]})
         (vf/with-each w [pos Translation
                          pos-global [Translation :global]
                          pos-parent [:maybe {:flags #{:up :cascade}}
                                      [Translation :global]]]
           (println :aaa)
           [pos pos-global pos-parent]))])

  ())

(defmacro with-each
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
    (vf/with-each w [speed ImpulseSpeed
                     {:keys [x] :as pos} Position
                     e :vf/entity]
      [e (update pos :x dec) x (update speed :value inc)]))"
  [w bindings & body]
  (let [bindings (mapv (fn [[k v]]
                         [k v])
                       (partition 2 bindings))
        code `(-each ~w ~(mapv (fn [[k v]] [`(quote ~k) v]) bindings)
                     (fn [~(vec (remove keyword? (mapv first bindings)))]
                       (try
                         ~@body
                         (catch Throwable e#
                           (println e#)))))
        hashed (hash code)]
    `((or (get-in @*-each-cache [(vp/mem ~w) ~hashed])
          (let [res# ~code]
            (swap! *-each-cache assoc-in [(vp/mem ~w) ~hashed] res#)
            res#)))))

(comment

  ;; Simple query.
  (let [w (vf/make-world #_{:debug true})
        {:syms [Position ImpulseSpeed]} (vp/make-components
                                         '{ImpulseSpeed [[:value :double]]
                                           Position [[:x :double] [:y :double]]})]
    (merge w {:a [(Position {:x -105.1}) :aaa]
              :b [(Position {:x 333.1}) (ImpulseSpeed 311)]
              :c [(Position {:x 0.1}) (ImpulseSpeed -43)]})
    (vf/with-each w [speed ImpulseSpeed
                     {:keys [x] :as pos} Position
                     e :vf/entity]
      [e (update pos :x dec) x (update speed :value inc)]))

  ())

(defn -system-callback
  [f]
  (-> (reify ecs_iter_action_t$Function
        (apply [_ it]
          (f it)))
      (ecs_iter_action_t/allocate (vp/default-arena))))

(defn -system
  [^VybeFlecsWorldMap w bindings+opts each-handler]
  (let [{:keys [opts f-arr query-expr]} (-each-bindings-adapter w bindings+opts)
        e (ent w (:vf/name opts))
        ;; Delete entity if it's a system already and recreate it.
        e (if (vf.c/ecs-has-id w e (flecs/EcsSystem))
            (do (vf.c/ecs-delete w e)
                (ent w (:vf/name opts)))
            e)
        {:vf/keys [phase]} opts
        _system-id (vf.c/ecs-system-init
                    w (ecs_system_desc_t
                       {:entity e
                        :query (parse-query-expr w query-expr)
                        :callback (-system-callback
                                   (fn [it]
                                     (if (vf.c/ecs-iter-changed it)
                                       (let [it (vp/jx-p->map it ecs_iter_t)
                                             f-idx (mapv (fn [f] (f it)) f-arr)]
                                         (doseq [idx (range (:count it))]
                                           (each-handler (mapv (fn [f] (f idx)) f-idx))))
                                       (vf.c/ecs-iter-skip it))))}))]
    (assoc w e [[(flecs/EcsDependsOn) (or phase (flecs/EcsOnUpdate))]])
    (make-entity w e)))

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
  "Similar to `with-each`, see its documentation.

  The differences are that `with-system` requires a
  :vf/name (you put it in the bindings, see example) and it won't
  run the code in place, it will build a Flecs system instead that can be run
  with `system-run`.

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
                       (partition 2 bindings))
        code `(-system ~w ~(mapv (fn [[k v]] [`(quote ~k) v]) bindings)
                       (fn [~(vec (remove keyword? (mapv first bindings)))]
                         (try
                           ~@body
                           (catch Throwable e#
                             (println e#)))))
        hashed (hash code)]
    (when-not (contains? (set (mapv first bindings)) :vf/name)
      (throw (ex-info "`with-system` requires a :vf/name" {:bindings bindings
                                                           :body body})))
    `(or (when-let [e# (get-in @*-each-cache [(vp/mem ~w) ~hashed])]
           (when (vf.c/ecs-is-alive ~w (ent ~w e#))
             e#))
         (let [res# ~code]
           (swap! *-each-cache assoc-in [(vp/mem ~w) ~hashed] (ent ~w res#))
           res#))))

(defn system-run
  "Run a system (which is just an entity)."
  [^VybeFlecsWorldMap w e]
  (vf.c/ecs-run w (ent w e) 0 vp/null))

(defn progress
  "Progress the world by running the systems."
  ([^VybeFlecsWorldMap w]
   (progress w 0))
  ([^VybeFlecsWorldMap w delta-time]
   (vf.c/ecs-progress w delta-time)))

(defn _
  "Used for creating anonymous entities."
  []
  (keyword "vf" (str (gensym "ANOM_"))))

;; -- Tests
(defn- ->edn
  [v]
  (edn/read-string (pr-str v)))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/c/entities/basics/src/main.c
(deftest ex-1
  ;; Create the world.
  (let [wptr (vf/-init)]
    #_ (def wptr (vf/-init))
    #_(def wptr wptr)

    ;; Create a entity called :bob and also add/create
    ;; :walking (tag) and Position (component).
    (vf/-set-c wptr :bob [:walking
                          (Position {:x 10 :y 20})])

    ;; Get position, it will return a hash map representation, you can use
    ;; normal clojure functions with it (e.g. `get`, `select-keys`... it's a map
    ;; backed by a pointer (memory segment) + a component).
    (vf/-get-c wptr :bob Position)

    ;; Override position.
    (vf/-set-c wptr :bob [(Position {:x 20 :y 30})])

    ;; Create another entity.
    (vf/-set-c wptr :alice [(Position {:x 10 :y 20})])

    ;; Add a tag in a separate step.
    (vf/-set-c wptr :alice [:walking])

    ;; Check all the components (including the ones we hide from you) of an
    ;; entity in string format.
    ;; TODO Hash map representation just like as we have in vybe.api.
    (vf/type-str wptr :alice)
    #_(let [{:keys [array count]}
            (-> (vf.c/ecs-get-type wptr (vf/ent wptr :alice))
                (p->map (-to-c (ecs_type_t/layout))))]
        (mapv (fn [idx]
                (.getAtIndex array ValueLayout/JAVA_LONG idx))
              (range count)))

    ;; Remove tag.
    (vf/-remove-c wptr :alice [:walking])

    ;; Iterate over all the entities with Position.
    (let [it (vf.c/ecs-each-id wptr (vf/ent wptr Position))
          *acc (atom [])]
      (while (vf.c/ecs-each-next it)
        (let [pos (vf.c/ecs-field-w-size it (.byteSize (.layout Position)) 0)]
          (swap! *acc conj
                 (mapv (fn [^long idx]
                         [(-> (vf.c/ecs-get-name wptr (.getAtIndex ^MemorySegment (:entities it)
                                                                   ValueLayout/JAVA_LONG
                                                                   idx))
                              vp/->string)
                          (->> (vp/p->map (.asSlice ^MemorySegment pos
                                                    (* idx (.byteSize (.layout Position)))
                                                    (.layout Position))
                                          Position)
                               (into {}))])
                       (range (:count it))))))
      (swap! *acc #(apply concat %))
      #_(vf.c/ecs-fini wptr)
      (is (= #{["bob" {:x 20.0, :y 30.0}]
               ["alice" {:x 10.0, :y 20.0}]}
             (set @*acc))))))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/c/entities/basics/src/main.c
(deftest ex-1-w-map
  ;; Create the world.
  (let [w (vf/make-world)]
    #_(def w (vf/make-world))
    #_(def w w)

    ;; We can also do the same thing as in `ex-1`, but using a clojure hash map
    ;; representation of the world. You can use the clojure functions you are
    ;; used to. It's a mutable map, though, e.g. `assoc` mutates it in place.

    ;; Let's setup some entities.
    (merge w {:bob [:walking (Position {:x 10 :y 20}) nil]
              :alice [(Position {:x 10 :y 21})]})

    ;; Get the position component of an entity.
    (get-in w [:bob Position])
    ;; Or a value from the component (hash map magic!! *backed by pointers).
    (get-in w [:bob Position :y])
    ;; Or whatever you need from it.
    (-> (get-in w [:bob Position])
        (select-keys [:y]))

    ;; Override position.
    (assoc w :bob (Position {:x 20 :y 30}))

    ;; Add a tag in a separate step.
    (assoc w :alice :walking)

    ;; Check all the components (including the ones we hide from you) of an
    ;; entity in string format.
    ;; TODO Hash map representation just like as we have in vybe.api.
    (vf/type-str (:alice w))

    ;; Remove tag.
    (-> w
        (update :alice disj :walking)
        ;; Update x field in Position (maps everywhere!).
        (update-in [:bob Position :x] inc))

    ;; Iterate over all the entities with Position using `with-each`, also
    ;; retrieving the positions.
    (is (= '[[#{:alice #:vybe.flecs{Position {:x 10.0, :y 21.0}}}
              #:vybe.flecs{Position {:x 10.0, :y 21.0}}]
             [#{:bob :walking #:vybe.flecs{Position {:x 21.0, :y 30.0}}}
              #:vybe.flecs{Position {:x 21.0, :y 30.0}}]]
           (->edn (with-each w [pos Position, e :vf/entity]
                    [e pos]))))

    ;; `with-system` has basically the same interface as
    ;; `with-each`. The differences are that `with-system` requires a
    ;; :vf/name (you put it in the bindings, see below) and it won't
    ;; run the code in place, but will build a Flecs system that can be run
    ;; with `system-run`.
    (let [*acc (atom [])
          ;; Note that we need to accumulate values here explictly as `with-system`
          ;; doesn't run the system immediately.
          system-id (with-system w [:vf/name :my-system, pos Position, e :vf/entity]
                      (swap! *acc conj [e pos]))]

      (testing "system has not run yet"
        (is (= '[]
               (->edn @*acc))))

      (vf/system-run w :my-system)
      (testing "system has run"
        (is (= '[[#{:alice #:vybe.flecs{Position {:x 10.0, :y 21.0}}}
                  #:vybe.flecs{Position {:x 10.0, :y 21.0}}]
                 [#{:bob :walking #:vybe.flecs{Position {:x 21.0, :y 30.0}}}
                  #:vybe.flecs{Position {:x 21.0, :y 30.0}}]]
               (->edn @*acc))))

      (vf/progress w)
      (testing "system has run again, now using vf/progress, if there was no iter change, system won't really run"
        (is (= '[[#{:alice #:vybe.flecs{Position {:x 10.0, :y 21.0}}}
                  #:vybe.flecs{Position {:x 10.0, :y 21.0}}]
                 [#{:bob :walking #:vybe.flecs{Position {:x 21.0, :y 30.0}}}
                  #:vybe.flecs{Position {:x 21.0, :y 30.0}}]]
               (->edn @*acc))))

      (testing "adding it twice returns a different entity"
        (is (not= system-id
                  (with-system w [:vf/name :my-system, pos Position]
                    pos)))))))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/c/entities/hierarchy/src/main.c
(deftest children-test
  (let [w (vf/make-world #_{:debug true})]
    #_(def w (vf/make-world))
    #_(def w w)

    (merge w {:sun [:star (Position {:x 1 :y 1})
                    ;; These are all children.
                    {:mercury [:planet (Position {:x 1 :y 1})]
                     :venus [:planet (Position {:x 2 :y 2})
                             ;; NESTED!
                             {:moon [:moon (Position {:x 0.1 :y 0.1})]}]}]
              ;; You can also define children like below.
              :earth [:planet (Position {:x 3 :y 3}) [:vf/child-of :sun]]})
    (is (= `{:sun #{#:vybe.flecs{Position {:x 1.0 :y 1.0}} :star}
             :mercury #{#:vybe.flecs{Position {:x 1.0 :y 1.0}} [:vf/child-of :sun] :planet}
             :venus #{[:vf/child-of :sun] #:vybe.flecs{Position {:x 2.0 :y 2.0}} :planet}
             :earth #{[:vf/child-of :sun] #:vybe.flecs{Position {:x 3.0 :y 3.0}} :planet}
             :moon #{:moon #:vybe.flecs{Position {:x 0.1 :y 0.1}} [:vf/child-of :venus]}}
           (->edn w)))))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/cpp/entities/prefab/src/main.cpp
;; and https://github.com/SanderMertens/flecs/blob/master/examples/c/prefabs/variant/src/main.c
;; Re: overriding, check See https://www.flecs.dev/flecs/md_docs_2Manual.html#automatic-overriding
(deftest prefab-test
  (let [w (vf/make-world #_{:debug true})
        ;; You can defined multiple components like this, these won't
        ;; be global as the `defcomp` ones are.
        {:syms [Attack Defense FreightCapacity ImpulseSpeed Position]}
        (vp/make-components
         '{Attack [[:value :double]]
           Defense [[:value :double]]
           FreightCapacity [[:value :double]]
           ImpulseSpeed [[:value :double]]
           Position [[:x :double] [:y :double]]})]
    #_(def w (vf/make-world))
    #_(def w w)

    ;; Prefabs are template-like entities that you can use to define other
    ;; entities. See how :mammoth has
    (merge w {:spaceship [:vf/prefab (ImpulseSpeed 50) (Defense 50)
                          ;; Position will always be overriden, it means that
                          ;; the prefab Position component will be used only
                          ;; for the initial construction of the new entity,
                          ;; being decoupled from it afterwards.
                          (vf/override (Position {:x 30 :y 20}))]
              :freighter [:vf/prefab (vf/is-a :spaceship) :has-ftl
                          (FreightCapacity 100) (Defense 100)
                          ;; The child of a prefab is also a prefab.
                          {:mammoth-freighter [(vf/is-a :freighter)
                                               (FreightCapacity 500) (Defense 300)]}]
              :frigate [:vf/prefab (vf/is-a :spaceship) :has-ftl
                        (Attack 100) (Defense 75) (ImpulseSpeed 125)]
              :mammoth [(vf/is-a :mammoth-freighter)]
              :mammoth-2 [(vf/is-a :mammoth-freighter)
                          ;; FreightCapacity is overriden.
                          (FreightCapacity -51)]})
    ;; When you update a prefab, entities inheriting from it wil
    ;; get updated as well (as long as it's not overriden).
    (update-in w [:mammoth-freighter Defense :value] inc)
    (is (= '[["mammoth"
              {Position {:x 31.0, :y 20.0}}
              {ImpulseSpeed {:value 50.0}}
              {Defense {:value -500.0}}
              {FreightCapacity {:value 499.0}}]
             ["mammoth-2"
              {Position {:x 30.0, :y 20.0}}
              {ImpulseSpeed {:value 50.0}}
              {Defense {:value -500.0}}
              {FreightCapacity {:value -51.0}}]]
           (->edn
            ;; You can iterate over all the inherited components.
            (with-each w [e :vf/entity, pos Position, speed ImpulseSpeed
                          defense Defense, capacity FreightCapacity]
              (if (= e (make-entity w :mammoth))
                ;; We modify capacity, defense and position here when :mammoth, note
                ;; how only defense will be changed in both (as it's originally from the
                ;; prefab) while capacity and position are not shared (as they are
                ;; overridden).
                [(get-name e) (update pos :x inc) speed (assoc defense :value -500) (update capacity :value dec)]
                [(get-name e) pos speed defense capacity])))))))

(deftest pair-wildcard-test
  (is (= '[[{A {:x 34.0}} [:a :c]] [{A {:x 34.0}} [:a :d]]]
         (let [w (vf/make-world)
               A (vp/make-component 'A [[:x :double]])]
           (merge w {:b [(A {:x 34})
                         [:a :c]
                         [:a :d]]})
           (->edn
            (vf/with-each w [a A
                             v [:a :*]]
              [a v]))))))

(deftest pair-any-test
  (is (= #_'[[{A {:x 34.0}} [:a :c]] [{A {:x 34.0}} [:a :d]]]
         0
         (let [w (vf/make-world)
               A (vp/make-component 'A [[:x :double]])]
           (merge w {:b [(A {:x 34})
                         [:a :c]
                         [:a :d]]})
           (->edn
            (vf/with-each w [a A
                             v [:a :_]]
              [a v]))))))
