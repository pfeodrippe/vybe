(ns vybe.flecs
  {:clj-kondo/ignore [:unused-value]}
  (:refer-clojure :exclude [ref])
  (:require
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
   )
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
(vp/defcomp EcsIdentifier (org.vybe.flecs.EcsIdentifier/layout))
(vp/defcomp iter_t (ecs_iter_t/layout))

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

(defn- -adapt-name
  [v]
  (or ({:ChildOf :vf/child-of}
       v)
      v))

(defn -flecs->vybe
  [s]
  (if (str/includes? s ".")
    (list `path
          (->> (str/split s #"\.")
               (mapv -flecs->vybe)))
    (let [parsed (str/replace s #"!!" ".")]
      (if (str/starts-with? s "C_")
        (-> (subs parsed 2)
            symbol)
        (-> parsed
            keyword
            -adapt-name)))))
#_ (-flecs->vybe "s!!f/f.al")
#_ (-flecs->vybe (vt/vybe-name Position))

(declare ent)

(vp/defcomp Position
  [[:x :double]
   [:y :double]])

#_ (ex-1)

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
      (let [n (-flecs->vybe (get-internal-path wptr (ent wptr v)))]
        (when-not (contains? (conj skip-meta :Identifier :Name :Symbol :Component)
                             n)
          n))))

(defn- -entity-components
  [wptr e-id]
  (let [{:keys [array count]} (-> (vf.c/ecs-get-type wptr e-id)
                                  (vp/p->map ecs_type_t))]
    (->> (range count)
         (keep (fn [^long idx]
                 (let [c-id (.getAtIndex ^MemorySegment array ValueLayout/JAVA_LONG idx)
                       *c-cache (delay (->comp-rep wptr c-id))]
                   (cond
                     (vf.c/ecs-id-is-pair c-id)
                     (let [[a b] [(->comp-rep wptr (vf.c/vybe-pair-first wptr c-id))
                                  (->comp-rep wptr (vf.c/vybe-pair-second wptr c-id))]]
                       (when (some some? [a b])
                         [(or a (vf.c/vybe-pair-first wptr c-id))
                          (or b (vf.c/vybe-pair-second wptr c-id)) ]))

                     (instance? VybeComponent @*c-cache)
                     (-get-c wptr e-id (->comp-rep wptr c-id))

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

;; -- Types.
(definterface IVybeFlecsEntityMap
  (^long id []))

(declare vybe-flecs-world-map-rep)
(declare make-entity)

(def-map-type VybeFlecsWorldMap [-wptr mta]
  (get [this e default-value]
       (if (some? e)
         (cond
           (vector? e)
           (if (and (not= (vf.c/ecs-lookup-symbol this (vt/vybe-name (first e)) true false)  0)
                    (not= (vf.c/ecs-lookup-symbol this (vt/vybe-name (second e)) true false) 0)
                    (not= (vf.c/ecs-is-valid this (-ecs-pair
                                                   (ent this (first e))
                                                   (ent this (second e))))
                          0))
             (make-entity this e)
             default-value)

           (int? e)
           (make-entity this e)

           :else
           (let [e-id (vf.c/ecs-lookup-symbol this (vt/vybe-name e) true false)]
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
       (remove (if (-> this meta :show-all)
                 (constantly false)
                 (comp empty? val)))
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
   (VybeFlecsWorldMap. (-init) mta))
  (^VybeFlecsWorldMap [wptr mta]
   (VybeFlecsWorldMap. wptr mta)))
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
      (.id this)
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

(extend-protocol vt/IVybeName
  #_ #_clojure.lang.Var
  (vybe-name [v]
    (str "V_" (-> v
                  symbol
                  str
                  (str/replace #"\." "_"))))
  Long
  (vybe-name [v]
    (str v))

  VybeComponent
  (vybe-name [v]
    (str "C_" (-> (.get (.name ^MemoryLayout (.layout v)))
                  (str/replace #"\." "!!"))))

  clojure.lang.Keyword
  (vybe-name [k]
    (-> (symbol k)
        str
        (str/replace #"\." "!!")))

  String
  (vybe-name [s]
    s)

  VybeFlecsEntitySet
  (vybe-name [s]
    (vt/vybe-name (.id s)))

  #_ #_clojure.lang.Symbol
  (vybe-name [sym]
    (str "S_" (-> sym
                  (str/replace #"\." "_")))))

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

(defn make-entity
  ^VybeFlecsEntitySet [w e]
  (VybeFlecsEntitySet. w (ent w e)))
#_ (vf/make-entity (vf/make-world) :a)

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

(defn ent
  "Creates or refers an entity.

  Returns the ID of the entity."
  ([wptr e]
   (ent wptr e {}))
  ([wptr e {:keys [create-entity]
            :or {create-entity true}
            :as opts}]
   #_ (when-not (int? e)
        (println :e3 e))
   (cond
     (int? e)
     e

     (instance? VybeFlecsEntitySet e)
     (.id ^VybeFlecsEntitySet e)

     (instance? IVybeWithComponent e)
     (ent wptr (.component ^IVybeWithComponent e) opts)

     (vector? e)
     (let [id (-ecs-pair (ent wptr (first e) opts)
                         (ent wptr (second e) opts))]
       id)

     :else
     (or (if (or (keyword? e) (string? e))
           (let [e-id (vf.c/ecs-lookup-symbol wptr (vt/vybe-name e) true false)]
             (when-not (zero? e-id)
               e-id))

           (when-let [id (get-in @*world->cache [(vp/mem wptr) e])]
             (when (vf.c/ecs-is-valid wptr id)
               id)))

         (when create-entity
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

                        (string? e)
                        (let [sym (vt/vybe-name e)
                              e-id (vf.c/ecs-lookup-symbol wptr sym true false)]
                          (if (not= e-id 0)
                            e-id
                            (let [id (vf.c/ecs-set-name wptr 0 sym)]
                              #_(vf.c/ecs-set-symbol wptr id sym)
                              #_(vp/cache-comp e)
                              #_(-add-meta wptr e id :vybe.flecs.type/keyword)
                              (-cache-entity wptr e id)
                              id)))

                        (keyword? e)
                        (or (get builtin-entities e)
                            (let [sym (vt/vybe-name e)
                                  e-id (vf.c/ecs-lookup-symbol wptr sym true false)]
                              (if (not= e-id 0)
                                e-id
                                (let [id (vf.c/ecs-set-name wptr 0 sym)]
                                  #_(vf.c/ecs-set-symbol wptr id sym)
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
             e-id))))))
#_ (let [wptr (vf/-init)]
     [(vf/ent wptr :a)
      (vf/ent wptr :b)
      (Position {:x 10})])

;; -- Low-level only.
(defn -override
  [wptr e]
  (bit-or (flecs/ECS_AUTO_OVERRIDE) (ent wptr e)))

(declare path)
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

              ('vf/ref v)
              (let [c (:component v)]
                (-set-c wptr e
                        (if (vector? c)
                          ;; TODO Handle other cases.
                          [(vp/clone (get-in wptr [('vf/ref v) c])) (last c)]
                          (vp/clone (get-in wptr [('vf/ref v) c])))))

              (:vf.op/del v)
              (-remove-c wptr e [(:vf.op/del v)])

              (:vf.op/sym v)
              (vf.c/ecs-set-symbol wptr (ent wptr e) (:vf.op/sym v))

              :else
              ;; Child of hash map syntax.
              (mapv (fn [[nested-entity nested-components]]
                      (-set-c wptr (path [e nested-entity])
                              #_nested-entity
                              #_(vec (cond
                                       (nil? nested-components)
                                       []

                                       (sequential? nested-components)
                                       nested-components

                                       :else
                                       [nested-components]))
                              (conj (vec (cond
                                           (nil? nested-components)
                                           []

                                           (sequential? nested-components)
                                           nested-components

                                           :else
                                           [nested-components]))
                                    #_[:vf/child-of e])))
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


(declare get-name)

(defn -get-c
  [w e c]
  (let [e-id (ent w e)
        c-id (ent w c)]
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
        (vp/p->map (vf.c/ecs-get-id w e-id c-id)
                   (if (vector? c)
                     (if (instance? VybeComponent (first c))
                       (first c)
                       (last c))
                     (cond
                       (instance? VybeComponent c)
                       c

                       (vf.c/ecs-id-is-pair c-id)
                       (vp/comp-cache
                        (:id (-get-c w (vf.c/vybe-pair-first w c-id) VybeComponentId)))

                       :else
                       (vp/comp-cache c-id))))))))

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
  "Data-driven op for making a component overridable, usually used in prefabs,
  see https://www.flecs.dev/flecs/md_docs_2Manual.html#automatic-overriding

  Use like

    (vf/override (Position {:x 10}))"
  [e]
  ;; TODO Use keyword
  {'vf/override e})

(defn ref
  "Data-driven reference for an entity + component."
  [e c]
  ;; TODO Use keyword
  {'vf/ref e
   :component c})

(defn del
  "Data-driven component removal for an entity. Equivalent to

  (update w :my-entity disj c)"
  [c]
  {:vf.op/del c})

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

(defn get-internal-name
  "Retrieve flecs internal name."
  ([^VybeFlecsEntitySet em]
   (get-internal-name (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-get-name wptr (ent wptr e))
       vp/->string)))

(defn get-internal-path
  "Retrieve flecs internal path."
  ([^VybeFlecsEntitySet em]
   (get-internal-path (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-get-path-w-sep wptr 0 (ent wptr e) "." (flecs/NULL))
       vp/->string)))

(defn get-name
  "Retrieve vybe name."
  ([^VybeFlecsEntitySet em]
   (get-name (.w em) (.id em)))
  ([wptr e]
   (-> (get-internal-path wptr e)
       -flecs->vybe)))

(defn get-symbol
  ([^VybeFlecsEntitySet em]
   (get-symbol (.w em) (.id em)))
  ([w e]
   (vp/->string (vf.c/ecs-get-symbol w (vf/ent w e)))))

(defn lookup-symbol
  "Returns an entity id (or nil)."
  [w s]
  (let [e-id (vf.c/ecs-lookup-symbol w s false false)]
    (when (pos? e-id)
      e-id)))

(defn path
  "Builds path of entities (usually keywords), returns a string."
  [ks]
  (->> ks
       (mapv (fn [v]
               (vt/vybe-name v)))
       (str/join ".")))

(defn type-str
  "Get the type of an entity in Flecs string format."
  ([^VybeFlecsEntitySet em]
   (type-str (.w em) (.id em)))
  ([wptr e]
   (-> (vf.c/ecs-type-str wptr (vf.c/ecs-get-type wptr (ent wptr e)))
       vp/->string)))

(defn children-ids
  "Get children of an entity."
  ([^VybeFlecsEntitySet em]
   (children-ids (.w em) (.id em)))
  ([w e]
   (let [it (vf.c/ecs-children w (ent w e))]
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
        (mapv #(make-entity w %)))))

(defn parent-id
  "Get parent ID of an entity."
  ([^VybeFlecsEntitySet em]
   (parent-id (.w em) (.id em)))
  ([w e]
   (when-let [e-id (ent w e {:create-entity false})]
     (let [id (vf.c/ecs-get-parent w e-id)]
       (when-not (zero? id)
         id)))))

(defn parent
  "Get parent of an entity."
  ([^VybeFlecsEntitySet em]
   (parent (.w em) (.id em)))
  ([w e]
   (some->> (parent-id w e) (make-entity w))))

(defn hierarchy
  "Get hierarchy (children and nested children without the components) of an entity."
  ([^VybeFlecsEntitySet em]
   (hierarchy (.w em) (.id em)))
  ([w e]
   (let [cs (children w (ent w e))]
     (->> (mapv (fn [e]
                  (let [h (hierarchy e)
                        n (get-name e)]
                    [n h]))
                cs)
          (into {})))))

(defn hierarchy-no-path
  "Get hierarchy without showing entire children path, useful for debugging."
  ([^VybeFlecsEntitySet em]
   (hierarchy-no-path (.w em) (.id em)))
  ([w e]
   (let [cs (children w (ent w e))]
     (->> (mapv (fn [e]
                  (let [h (hierarchy-no-path e)
                        n (-> e get-internal-name -flecs->vybe)]
                    [n h]))
                cs)
          (into {})))))

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
#_(->> [:aa]
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
      (ent wptr v)))

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

                                         :vf/iter
                                         (fn [it]
                                           (fn [^long _idx]
                                             it))

                                         :vf/world
                                         (fn [it]
                                           (fn [^long _idx]
                                             (make-world (:world it) {})))

                                         (fn [it]
                                           (let [is-set (vf.c/ecs-field-is-set it idx)]
                                             (fn [_idx]
                                               (when is-set
                                                 c))))))
                               (update :idx (if (contains? #{:vf/iter :vf/entity :vf/world} c)
                                              identity
                                              inc))))))
                     {:idx 0 :coll []})
             :coll)]
    {:opts (->> bindings+opts (filter (comp keyword? first)) (into {}))
     :f-arr f-arr
     :query-expr (->> bindings
                      (mapv last)
                      (remove #{:vf/entity :vf/iter :vf/world})
                      vec)}))

(defonce *-each-cache (atom {}))

(defn -each
  [^VybeFlecsWorldMap w bindings+opts]
  (let [{:keys [_opts f-arr query-expr]} (-each-bindings-adapter w bindings+opts)
        wptr w
        q (vp/with-arena-root (-query wptr query-expr))]
    (fn [each-handler]
      (let [it (vf.c/ecs-query-iter wptr q)
            *acc (atom [])
            *idx (atom 0)]
        (while (vf.c/ecs-query-next it)
          (if #_(vf.c/ecs-iter-changed it) true
            (let [f-idx (mapv (fn [f] (f it)) f-arr)]
              (swap! *acc conj (mapv (fn [idx]
                                       (vf.c/ecs-defer-begin w)
                                       (try
                                         (each-handler (mapv (fn [f] (f idx)) f-idx))
                                         (finally (vf.c/ecs-defer-end w))))
                                     (range (:count it)))))
            #_(do (vf.c/ecs-iter-skip it)
                (swap! *acc assoc @*idx (get @*last-value @*idx))))
          (swap! *idx inc))
        #_(reset! *last-value @*acc)
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
        code `(-each ~w ~(mapv (fn [[k v]] [`(quote ~k) v]) bindings))
        hashed (hash code)]
    `((or (get-in @*-each-cache [(vp/mem ~w) ~hashed])
          (let [res# ~code]
            (swap! *-each-cache assoc-in [(vp/mem ~w) ~hashed] res#)
            res#))
      (fn [~(vec (remove keyword? (mapv first bindings)))]
        (try
          ~@body
          (catch Throwable e#
            (println e#)))))))

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
  (vp/with-arena-root
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
      (make-entity w e))))

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
