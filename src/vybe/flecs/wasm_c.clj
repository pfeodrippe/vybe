(ns vybe.flecs.wasm-c
  (:refer-clojure :exclude [ref])
  (:require
   [clojure.string :as str]
   [vybe.flecs.abi :as abi]
   [vybe.flecs.wasm :as flecs-wasm]
   [vybe.panama :as vp]
   [vybe.wasm :as vw])
  (:import
   (java.lang.foreign Arena MemorySegment ValueLayout)
   (vybe.panama VybeComponent VybePMap)))

(defonce module*
  (delay
    (let [module (flecs-wasm/load-module)]
      (flecs-wasm/set-system-callback-handler!
       (fn [it-ptr callback-ctx]
         (if-let [f (vw/callback callback-ctx)]
           (f nil it-ptr)
           (throw (ex-info "No registered Flecs Wasm callback"
                           {:callback-ctx callback-ctx
                            :it it-ptr})))))
      module)))

(defn module [] @module*)
(defn raw-call [name & args] (apply vw/call @module* name args))
(defn raw-global [name] (vw/global-i64 @module* name))

(defn set-system-callback-handler!
  [handler]
  (flecs-wasm/set-system-callback-handler!
   (fn [it-ptr callback-ctx]
     (handler it-ptr callback-ctx))))

(defn zero!
  [ptr size]
  (vw/zero! @module* ptr size))

(defn alloc [size] (vw/malloc @module* size))
(defn free [ptr] (vw/free @module* ptr))

(defmacro with-c-string
  [[ptr s] & body]
  `(vw/with-c-string [~ptr @module* ~s]
     ~@body))

(defn- mem
  [v]
  (cond
    (number? v) (long v)
    (instance? MemorySegment v) (.address ^MemorySegment v)
    :else (let [p (vp/mem v)]
            (if (instance? MemorySegment p)
              (.address ^MemorySegment p)
              p))))

(defn- desc-map
  [desc]
  (cond
    (nil? desc) {}
    (map? desc) desc
    (and (vector? desc)
         (even? (count desc))
         (every? keyword? (take-nth 2 desc))) (apply hash-map desc)
    (coll? desc) (try
                   (into {} desc)
                   (catch IllegalArgumentException _
                     {}))
    :else {}))

(defn- some-nonzero
  [& xs]
  (some (fn [x]
          (when (and x (not= 0 x))
            x))
        xs))

(defn ecs-os-set-api-defaults [] (raw-call "ecs_os_set_api_defaults"))
(defn ecs-os-init [] (raw-call "ecs_os_init"))
(defn ecs-init [] (raw-call "ecs_init"))
(defn ecs-mini [] (raw-call "ecs_mini"))
(defn ecs-fini [w] (raw-call "ecs_fini" (mem w)))
(defn ecs-new [w] (raw-call "ecs_new" (mem w)))
(defn ecs-delete [w e] (raw-call "ecs_delete" (mem w) e))
(defn ecs-is-valid [w e] (not (zero? (raw-call "ecs_id_is_valid" (mem w) e))))
(defn ecs-is-alive [w e] (not (zero? (raw-call "ecs_is_alive" (mem w) e))))
(defn ecs-get-alive [w e] (raw-call "ecs_get_alive" (mem w) e))
(defn ecs-add-id [w e id] (raw-call "ecs_add_id" (mem w) e id))
(defn ecs-remove-id [w e id] (raw-call "ecs_remove_id" (mem w) e id))
(defn ecs-has-id [w e id] (not (zero? (raw-call "ecs_has_id" (mem w) e id))))
(defn ecs-id-is-pair [id] (not (zero? (raw-call "ecs_id_is_pair" id))))
(defn ecs-id-is-wildcard [id] (not (zero? (raw-call "ecs_id_is_wildcard" id))))
(defn ecs-id-is-tag
  ([w id] (not (zero? (raw-call "ecs_id_is_tag" (mem w) id))))
  ([id] (ecs-id-is-tag 0 id)))
(defn ecs-enable-id [w e id enabled?]
  (raw-call "ecs_enable_id" (mem w) e id (if enabled? 1 0)))
(defn ecs-modified-id [w e id] (raw-call "ecs_modified_id" (mem w) e id))
(defn ecs-progress
  [w dt]
  (raw-call "ecs_progress" (mem w) (Double/doubleToRawLongBits (double dt))))
(defn ecs-get-world [poly] (raw-call "ecs_get_world" (mem poly)))

(defn- attach-c-bridge!
  [v c-name f]
  (alter-meta! v assoc :vybe/fn-meta
               (select-keys (vp/c-fn f (abi/function-desc c-name))
                            [:fn-desc :fn-address]))
  nil)

(doseq [[c-name _] (:functions (abi/abi))
        :let [clj-name (-> c-name
                           (str/replace "_" "-")
                           symbol)
              v (ns-resolve *ns* clj-name)]
        :when (and (var? v) (fn? @v))]
  (attach-c-bridge! v c-name @v))

(defn ecs-set-name
  [w e s]
  (vw/with-c-string* @module* s
    (fn [sp]
      (raw-call "ecs_set_name" (mem w) e sp))))

(defn ecs-set-symbol
  [w e s]
  (vw/with-c-string* @module* s
    (fn [sp]
      (raw-call "ecs_set_symbol" (mem w) e sp))))

(defn ecs-set-alias
  [w e s]
  (vw/with-c-string* @module* s
    (fn [sp]
      (raw-call "ecs_set_alias" (mem w) e sp))))

(defn ecs-lookup
  [w s]
  (vw/with-c-string* @module* s
    (fn [sp]
      (raw-call "ecs_lookup" (mem w) sp))))

(defn ecs-lookup-symbol
  [w s lookup-as-path? recursive?]
  (vw/with-c-string* @module* s
    (fn [sp]
      (raw-call "ecs_lookup_symbol" (mem w) sp
                (if lookup-as-path? 1 0)
                (if recursive? 1 0)))))

(defn ecs-get-name
  [w e]
  (vw/read-c-string @module* (raw-call "ecs_get_name" (mem w) e)))

(defn ecs-get-symbol
  [w e]
  (vw/read-c-string @module* (raw-call "ecs_get_symbol" (mem w) e)))

(defn ecs-get-path-w-sep
  ([w parent e sep prefix]
   (let [ptr (vw/with-c-string* @module* sep
               (fn [sep-p]
                 (if (not (vw/null? prefix))
                   (vw/with-c-string* @module* prefix
                     (fn [prefix-p]
                       (raw-call "ecs_get_path_w_sep"
                                 (mem w) parent e sep-p prefix-p)))
                   (raw-call "ecs_get_path_w_sep" (mem w) parent e sep-p 0))))]
     (vw/read-c-string @module* ptr)))
  ([w e]
   (ecs-get-path-w-sep w 0 e "." nil)))

(defn ecs-get-parent [w e] (raw-call "ecs_get_parent" (mem w) e))
(defn ecs-get-target [w e rel idx] (raw-call "ecs_get_target" (mem w) e rel idx))
(defn ecs-get-type [w e] (raw-call "ecs_get_type" (mem w) e))
(defn ecs-type-str [w type-ptr]
  (vw/read-c-string @module* (raw-call "ecs_type_str" (mem w) type-ptr)))
(defn ecs-get-id [w e id] (raw-call "ecs_get_id" (mem w) e id))
(defn ecs-get-mut-id [w e id] (raw-call "ecs_get_mut_id" (mem w) e id))

(defonce ^:private type-abi*
  (delay {:size (abi/sizeof :ecs_type_t)
          :array (abi/offsetof :ecs_type_t :array)
          :count (abi/offsetof :ecs_type_t :count)}))

(defn ecs-type-count
  [type-ptr]
  (if (pos? (long type-ptr))
    (vw/read-i32 @module* (+ type-ptr (:count @type-abi*)))
    0))

(defn ecs-type-id-at
  [type-ptr idx]
  (let [array-ptr (vw/read-i32 @module* (+ type-ptr (:array @type-abi*)))]
    (vw/read-i64 @module* (+ array-ptr (* idx 8)))))

(defn ecs-type-ids
  [type-ptr]
  (mapv #(ecs-type-id-at type-ptr %) (range (ecs-type-count type-ptr))))

(declare write-component!)

(defn ecs-set-id
  [w e id size data]
  (let [ptr (cond
              (number? data) data

              (instance? VybePMap data)
              (let [ptr (alloc size)]
                (zero! ptr size)
                (write-component! (.-component ^VybePMap data) ptr (into {} data))
                ptr)

              (instance? MemorySegment data)
              (let [bytes (.toArray ^MemorySegment data ValueLayout/JAVA_BYTE)
                    ptr (alloc (alength bytes))]
                (vw/write-bytes! @module* ptr bytes)
                ptr)

              :else 0)]
    (try
      (raw-call "ecs_set_id" (mem w) e id size ptr)
      (finally
        (when-not (number? data)
          (free ptr))))))

(defn ecs-doc-set-name
  [w e s]
  (vw/with-c-string* @module* s
    (fn [sp]
      (raw-call "ecs_doc_set_name" (mem w) e sp))))

(defonce ^:private entity-desc-abi*
  (delay {:size (abi/sizeof :ecs_entity_desc_t)
          :id (abi/offsetof :ecs_entity_desc_t :id)
          :parent (abi/offsetof :ecs_entity_desc_t :parent)
          :name (abi/offsetof :ecs_entity_desc_t :name)
          :symbol (abi/offsetof :ecs_entity_desc_t :symbol)
          :use-low-id (abi/offsetof :ecs_entity_desc_t :use_low_id)}))

(defn ecs-entity-init
  [w desc]
  (let [{:keys [id parent name symbol use-low-id use_low_id]} (desc-map desc)
        use-low-id (or use-low-id use_low_id)
        {desc-size :size
         id-off :id
         parent-off :parent
         name-off :name
         symbol-off :symbol
         use-low-id-off :use-low-id} @entity-desc-abi*
        desc (alloc desc-size)]
    (try
      (zero! desc desc-size)
      (vw/write-i64! @module* (+ desc id-off) (long (or id 0)))
      (vw/write-i64! @module* (+ desc parent-off) (long (or parent 0)))
      (if name
        (vw/with-c-string* @module* name
          (fn [name-p]
            (if symbol
              (vw/with-c-string* @module* symbol
                (fn [sym-p]
                  (vw/write-i32! @module* (+ desc name-off) (int name-p))
                  (vw/write-i32! @module* (+ desc symbol-off) (int sym-p))
                  (vw/write-i8! @module* (+ desc use-low-id-off)
                                (if use-low-id 1 0))
                  (raw-call "ecs_entity_init" (mem w) desc)))
              (do
                (vw/write-i32! @module* (+ desc name-off) (int name-p))
                (vw/write-i8! @module* (+ desc use-low-id-off)
                              (if use-low-id 1 0))
                (raw-call "ecs_entity_init" (mem w) desc)))))
        (raw-call "ecs_entity_init" (mem w) desc))
      (finally
        (free desc)))))

(defonce ^:private component-desc-abi*
  (delay {:size (abi/sizeof :ecs_component_desc_t)
          :entity (abi/offsetof :ecs_component_desc_t :entity)
          :type-size (abi/offsetof :ecs_component_desc_t [:type :size])
          :type-alignment (abi/offsetof :ecs_component_desc_t [:type :alignment])}))

(defn ecs-component-init
  [w desc]
  (let [{:keys [entity size alignment type]} (desc-map desc)
        {:keys [size alignment]} (merge {:size size :alignment alignment}
                                        (when type (desc-map type)))
        {desc-size :size
         entity-off :entity
         type-size-off :type-size
         type-alignment-off :type-alignment} @component-desc-abi*
        desc (alloc desc-size)]
    (try
      (zero! desc desc-size)
      (vw/write-i64! @module* (+ desc entity-off) (long entity))
      (vw/write-i32! @module* (+ desc type-size-off) (int size))
      (vw/write-i32! @module* (+ desc type-alignment-off) (int alignment))
      (raw-call "ecs_component_init" (mem w) desc)
      (finally
        (free desc)))))

(defonce ^:private system-desc-abi*
  (delay {:size (abi/sizeof :ecs_system_desc_t)
          :entity (abi/offsetof :ecs_system_desc_t :entity)
          :query (abi/offsetof :ecs_system_desc_t :query)
          :callback (abi/offsetof :ecs_system_desc_t :callback)
          :run (abi/offsetof :ecs_system_desc_t :run)
          :ctx (abi/offsetof :ecs_system_desc_t :ctx)
          :callback-ctx (abi/offsetof :ecs_system_desc_t :callback_ctx)
          :run-ctx (abi/offsetof :ecs_system_desc_t :run_ctx)
          :immediate (abi/offsetof :ecs_system_desc_t :immediate)
          :query-expr (abi/offsetof :ecs_query_desc_t :expr)}))

(defonce ^:private query-desc-abi*
  (delay {:size (abi/sizeof :ecs_query_desc_t)
          :terms (abi/offsetof :ecs_query_desc_t :terms)
          :expr (abi/offsetof :ecs_query_desc_t :expr)
          :cache-kind (abi/offsetof :ecs_query_desc_t :cache_kind)
          :flags (abi/offsetof :ecs_query_desc_t :flags)}))

(defonce ^:private term-abi*
  (delay {:size (abi/sizeof :ecs_term_t)
          :id (abi/offsetof :ecs_term_t :id)
          :src-id (abi/offsetof :ecs_term_t [:src :id])
          :src-name (abi/offsetof :ecs_term_t [:src :name])
          :first-id (abi/offsetof :ecs_term_t [:first :id])
          :first-name (abi/offsetof :ecs_term_t [:first :name])
          :second-id (abi/offsetof :ecs_term_t [:second :id])
          :second-name (abi/offsetof :ecs_term_t [:second :name])
          :trav (abi/offsetof :ecs_term_t :trav)
          :inout (abi/offsetof :ecs_term_t :inout)
          :oper (abi/offsetof :ecs_term_t :oper)}))

(defonce ^:private observer-desc-abi*
  (delay {:size (abi/sizeof :ecs_observer_desc_t)
          :entity (abi/offsetof :ecs_observer_desc_t :entity)
          :query (abi/offsetof :ecs_observer_desc_t :query)
          :events (abi/offsetof :ecs_observer_desc_t :events)
          :yield-existing (abi/offsetof :ecs_observer_desc_t :yield_existing)
          :callback (abi/offsetof :ecs_observer_desc_t :callback)
          :callback-ctx (abi/offsetof :ecs_observer_desc_t :callback_ctx)}))

(defn- keep-c-string!
  [allocated* s]
  (let [p (vw/write-c-string! @module* s)]
    (swap! allocated* conj p)
    p))

(defn- write-term-ref!
  [base ref prefix allocated*]
  (let [ref (desc-map ref)
        id (or (:id ref) 0)
        name (some-nonzero (:name ref))]
    (vw/write-i64! @module* (+ base (get @term-abi* (keyword (str prefix "-id"))))
                   (long id))
    (when name
      (vw/write-i32! @module* (+ base (get @term-abi* (keyword (str prefix "-name"))))
                     (int (keep-c-string! allocated* name))))))

(defn- write-term!
  [base term allocated*]
  (let [term (desc-map term)]
    (vw/write-i64! @module* (+ base (:id @term-abi*)) (long (or (:id term) 0)))
    (write-term-ref! base (:src term) "src" allocated*)
    (write-term-ref! base (:first term) "first" allocated*)
    (write-term-ref! base (:second term) "second" allocated*)
    (vw/write-i64! @module* (+ base (:trav @term-abi*)) (long (or (:trav term) 0)))
    (vw/write-i16! @module* (+ base (:inout @term-abi*)) (short (or (:inout term) 0)))
    (vw/write-i16! @module* (+ base (:oper @term-abi*)) (short (or (:oper term) 0)))))

(defn- active-term-ref?
  [ref]
  (let [ref (desc-map ref)]
    (boolean (some-nonzero (:id ref) (:name ref)))))

(defn- active-term?
  [term]
  (let [term (desc-map term)]
    (boolean
     (or (some-nonzero (:id term))
         (active-term-ref? (:src term))
         (active-term-ref? (:first term))
         (active-term-ref? (:second term))))))

(defn- write-query-desc!
  [base desc allocated*]
  (let [desc (desc-map desc)
        terms (:terms desc)
        expr (some-nonzero (:expr desc))]
    (when expr
      (vw/write-i32! @module* (+ base (:expr @query-desc-abi*))
                     (int (keep-c-string! allocated* expr))))
    (when (some-nonzero (:cache-kind desc))
      (vw/write-i32! @module* (+ base (:cache-kind @query-desc-abi*))
                     (int (:cache-kind desc))))
    (when (some-nonzero (:cache_kind desc))
      (vw/write-i32! @module* (+ base (:cache-kind @query-desc-abi*))
                     (int (:cache_kind desc))))
    (when (some-nonzero (:flags desc))
      (vw/write-i32! @module* (+ base (:flags @query-desc-abi*))
                     (int (:flags desc))))
    (doseq [[idx term] (map-indexed vector (filter active-term? terms))]
      (write-term! (+ base (:terms @query-desc-abi*) (* idx (:size @term-abi*)))
                   term
                   allocated*))))

(defn ecs-system-init
  [w desc]
  (let [{:keys [entity expr query callback run immediate
                callback-ctx callback_ctx run-ctx run_ctx
                callback-ptr callback_ptr run-ptr run_ptr]} (desc-map desc)
        callback (some-nonzero callback)
        run (some-nonzero run)
        callback-ctx (some-nonzero callback-ctx callback_ctx callback)
        run-ctx (some-nonzero run-ctx run_ctx run)
        callback-ptr (or callback-ptr callback_ptr)
        run-ptr (or run-ptr run_ptr)
        {size :size
         entity-off :entity
         query-off :query
         callback-off :callback
         run-off :run
         ctx-off :ctx
         callback-ctx-off :callback-ctx
         run-ctx-off :run-ctx
         immediate-off :immediate
         query-expr-off :query-expr} @system-desc-abi*
        desc (alloc size)
        allocated* (atom [])
        callback-ptr (or callback-ptr
                         (when callback
                           (raw-call "vybe_flecs_system_trampoline_addr")))
        run-ptr (or run-ptr
                    (when run
                      (raw-call "vybe_flecs_system_trampoline_addr")))]
    (try
      (zero! desc size)
      (vw/write-i64! @module* (+ desc entity-off) (long entity))
      (if query
        (write-query-desc! (+ desc query-off) query allocated*)
        (when expr
          (vw/write-i32! @module* (+ desc query-off query-expr-off)
                         (int (keep-c-string! allocated* expr)))))
      (when callback-ptr
        (vw/write-i32! @module* (+ desc callback-off) (int callback-ptr)))
      (when run-ptr
        (vw/write-i32! @module* (+ desc run-off) (int run-ptr)))
      (vw/write-i32! @module* (+ desc ctx-off) (int (or run-ctx callback-ctx 0)))
      (vw/write-i32! @module* (+ desc callback-ctx-off) (int (or callback-ctx 0)))
      (vw/write-i32! @module* (+ desc run-ctx-off) (int (or run-ctx 0)))
      (vw/write-i8! @module* (+ desc immediate-off) (if immediate 1 0))
      (raw-call "ecs_system_init" (mem w) desc)
      (finally
        (run! free @allocated*)
        (free desc)))))

(defonce ^:private iter-abi*
  (delay {:size (abi/sizeof :ecs_iter_t)
          :world (abi/offsetof :ecs_iter_t :world)
          :count (abi/offsetof :ecs_iter_t :count)
          :entities (abi/offsetof :ecs_iter_t :entities)
          :event (abi/offsetof :ecs_iter_t :event)
          :event-id (abi/offsetof :ecs_iter_t :event_id)
          :field-count (abi/offsetof :ecs_iter_t :field_count)
          :query (abi/offsetof :ecs_iter_t :query)}))

(defn- long-array-segment
  [ptr count]
  (let [arr (long-array count)]
    (dotimes [i count]
      (aset arr i (vw/read-i64 @module* (+ ptr (* i 8)))))
    (MemorySegment/ofArray arr)))

(defn- bytes-segment
  [ptr size]
  (let [bytes (vw/read-bytes @module* ptr size)
        seg (.allocate (Arena/ofAuto) (long size) 8)]
    (dotimes [i size]
      (.set seg ValueLayout/JAVA_BYTE (long i) (aget bytes i)))
    seg))

(deftype Iter [ptr state*]
  clojure.lang.ILookup
  (valAt [_ k] (get @state* k))
  (valAt [_ k not-found] (get @state* k not-found))
  clojure.lang.IDeref
  (deref [_] @state*)
  Object
  (toString [_] (str @state*)))

(defn iter-state
  [ptr]
  (let [{:keys [world count entities event event-id field-count query]} @iter-abi*
        cnt (vw/read-i32 @module* (+ ptr count))
        entities-ptr (vw/read-i32 @module* (+ ptr entities))]
    {:ptr ptr
     :world (vw/read-i32 @module* (+ ptr world))
     :count cnt
     :entities (when (and (pos? cnt) (pos? entities-ptr))
                 (long-array-segment entities-ptr cnt))
     :event (vw/read-i64 @module* (+ ptr event))
     :event_id (vw/read-i64 @module* (+ ptr event-id))
     :field_count (vw/read-i32 @module* (+ ptr field-count))
     :query (vw/read-i32 @module* (+ ptr query))}))

(defn make-iter [ptr] (Iter. ptr (atom (iter-state ptr))))
(defn free-iter [^Iter it] (free (.-ptr it)) nil)
(defn refresh-iter! [^Iter it] (reset! (.-state* it) (iter-state (.-ptr it))) it)

(defn- iter-ptr
  [it]
  (if (number? it)
    (long it)
    (.-ptr ^Iter it)))

(defn ecs-each-id
  [w id]
  (let [ptr (alloc (:size @iter-abi*))]
    (zero! ptr (:size @iter-abi*))
    (raw-call "ecs_each_id" ptr (mem w) id)
    (make-iter ptr)))

(defn ecs-each-next
  [^Iter it]
  (let [res (raw-call "ecs_each_next" (.-ptr it))]
    (refresh-iter! it)
    (not (zero? res))))

(defn ecs-children
  [w parent]
  (let [ptr (alloc (:size @iter-abi*))]
    (zero! ptr (:size @iter-abi*))
    (raw-call "ecs_children" ptr (mem w) parent)
    (make-iter ptr)))

(defn ecs-children-next
  [^Iter it]
  (let [res (raw-call "ecs_children_next" (.-ptr it))]
    (refresh-iter! it)
    (not (zero? res))))

(defn ecs-field-w-size
  [it size index]
  (let [ptr (raw-call "ecs_field_w_size" (iter-ptr it) size index)]
    (when (pos? ptr)
      (bytes-segment ptr (* size (:count it))))))

(defn ecs-field-ptr [it size index]
  (raw-call "ecs_field_w_size" (iter-ptr it) size index))
(defn ecs-field-at-ptr [it size index row]
  (raw-call "ecs_field_at_w_size" (iter-ptr it) size index row))
(defn ecs-field-is-set [it index]
  (not (zero? (raw-call "ecs_field_is_set" (iter-ptr it) index))))
(defn ecs-field-is-self [it index]
  (not (zero? (raw-call "ecs_field_is_self" (iter-ptr it) index))))
(defn ecs-field-id [it index] (raw-call "ecs_field_id" (iter-ptr it) index))
(defn ecs-field-src [it index] (raw-call "ecs_field_src" (iter-ptr it) index))

(defn ecs-query-init
  ([w] (ecs-query-init w nil))
  ([w query-desc]
   (let [query-desc (cond
                      (nil? query-desc) {}
                      (string? query-desc) {:expr query-desc}
                      :else (desc-map query-desc))
         desc-size (:size @query-desc-abi*)
         desc (alloc desc-size)
         allocated* (atom [])]
     (try
       (zero! desc desc-size)
       (write-query-desc! desc query-desc allocated*)
       (raw-call "ecs_query_init" (mem w) desc)
       (finally
         (run! free @allocated*)
         (free desc))))))

(defn ecs-query-iter
  [w q]
  (let [ptr (alloc (:size @iter-abi*))]
    (zero! ptr (:size @iter-abi*))
    (raw-call "ecs_query_iter" ptr (mem w) q)
    (make-iter ptr)))

(defn ecs-query-next
  [it]
  (let [res (raw-call "ecs_query_next" (iter-ptr it))]
    (when-not (number? it)
      (refresh-iter! it))
    (not (zero? res))))

(defn ecs-query-fini
  [q]
  (when (pos? (long q))
    (raw-call "ecs_query_fini" q))
  nil)

(defn ecs-ref-init-id
  [w e id]
  (let [ptr (alloc 40)]
    (zero! ptr 40)
    (raw-call "ecs_ref_init_id" ptr (mem w) e id)
    {:ptr ptr :id id}))

(defn ecs-ref-get-id
  [w ref id]
  (raw-call "ecs_ref_get_id" (mem w) (:ptr ref) id))

(defn write-component!
  [^VybeComponent component ptr m]
  (doseq [[k {:keys [type offset]}] (.fields component)]
    (let [offset (+ ptr offset)
          v (get m k)]
      (case type
        :double (vw/write-f64! @module* offset (double (or v 0.0)))
        :float (vw/write-f32! @module* offset (float (or v 0.0)))
        :long (vw/write-i64! @module* offset (long (or v 0)))
        :int (vw/write-i32! @module* offset (int (or v 0)))
        :short (vw/write-i16! @module* offset (short (or v 0)))
        :byte (vw/write-i8! @module* offset (byte (or v 0)))
        :boolean (vw/write-i8! @module* offset (if v 1 0))
        :pointer (vw/write-i32! @module* offset (int (or v 0)))
        (vw/write-i32! @module* offset (int (or v 0))))))
  ptr)

(defonce ^:private component-field-info* (atom {}))

(defn- component-field-info
  [^VybeComponent component k]
  (or (get @component-field-info* [component k])
      (let [{:keys [offset type]} (get (.fields component) k)
            field-type type
            info (when offset [offset field-type])]
        (when info
          (swap! component-field-info* assoc [component k] info))
        info)))

(defn read-component-field
  [^VybeComponent component ptr k]
  (when-let [[offset field-type] (component-field-info component k)]
    (let [p (+ ptr offset)]
      (case field-type
        :double (vw/read-f64 @module* p)
        :float (vw/read-f32 @module* p)
        :long (vw/read-i64 @module* p)
        :int (vw/read-i32 @module* p)
        :short (vw/read-i16 @module* p)
        :byte (vw/read-i8 @module* p)
        :boolean (not (zero? (vw/read-i8 @module* p)))
        :pointer (vw/read-i32 @module* p)
        (vw/read-i32 @module* p)))))

(defn write-component-field!
  [^VybeComponent component ptr k v]
  (when-let [[offset field-type] (component-field-info component k)]
    (let [p (+ ptr offset)]
      (case field-type
        :double (vw/write-f64! @module* p (double (or v 0.0)))
        :float (vw/write-f32! @module* p (float (or v 0.0)))
        :long (vw/write-i64! @module* p (long (or v 0)))
        :int (vw/write-i32! @module* p (int (or v 0)))
        :short (vw/write-i16! @module* p (short (or v 0)))
        :byte (vw/write-i8! @module* p (byte (or v 0)))
        :boolean (vw/write-i8! @module* p (if v 1 0))
        :pointer (vw/write-i32! @module* p (int (or v 0)))
        (vw/write-i32! @module* p (int (or v 0))))))
  ptr)

(defn read-component
  [^VybeComponent component ptr]
  (into {}
        (map (fn [[k {:keys [type offset]}]]
               (let [p (+ ptr offset)
                     value (case type
                             :double (vw/read-f64 @module* p)
                             :float (vw/read-f32 @module* p)
                             :long (vw/read-i64 @module* p)
                             :int (vw/read-i32 @module* p)
                             :short (vw/read-i16 @module* p)
                             :byte (vw/read-i8 @module* p)
                             :boolean (not (zero? (vw/read-i8 @module* p)))
                             :pointer (vw/read-i32 @module* p)
                             (vw/read-i32 @module* p))]
                 [k value]))
             (.fields component))))

(defn pmap-from-ptr
  [^VybeComponent component ptr]
  (read-component component ptr))

(defn vybe-pair
  [rel target]
  (let [ecs-pair (raw-global "ECS_PAIR")
        lo (bit-and (long target) 0xffffffff)
        hi (bit-shift-left (long rel) 32)]
    (bit-or ecs-pair hi lo)))

(def ^:private ecs-id-flags-mask (Long/parseUnsignedLong "FF00000000000000" 16))
(def ^:private ecs-component-mask (bit-not ecs-id-flags-mask))

(defn vybe-pair-first
  [_w id]
  (unsigned-bit-shift-right (bit-and (long id) ecs-component-mask) 32))

(defn vybe-pair-second
  [_w id]
  (bit-and (long id) 0xffffffff))

(defn vybe-rest-enable [w] (raw-call "vybe_rest_enable" (mem w)))
(defn vybe-default-systems-c [w] (raw-call "vybe_default_systems_c" (mem w)))
(defn vybe--test--rest-issue [enabled?]
  (raw-call "vybe__test__rest_issue" (if enabled? 1 0)))

(defn ecs-defer-begin [w] (raw-call "ecs_defer_begin" (mem w)))
(defn ecs-defer-end [w] (raw-call "ecs_defer_end" (mem w)))
(defn ecs-log-set-level [level] (raw-call "ecs_log_set_level" level))
(defn ecs-import-c [_import-fn _module-name] 0)
(defn ecs-os-get-api [] (raw-call "vybe_flecs_os_get_api_ptr"))
(defn ecs-os-set-api [api] (raw-call "vybe_flecs_os_set_api_ptr" (mem api)))
(defn vybe-setup-allocator [] nil)

(defn ecs-get-table [w e]
  (raw-call "ecs_get_table" (mem w) e))

(defn ecs-table-get-type [table]
  (raw-call "ecs_table_get_type" table))

(defn ecs-search-offset [w table offset id trav]
  (raw-call "ecs_search_offset" (mem w) table offset id (mem trav)))

(defn ecs-query-changed [q]
  (not (zero? (raw-call "ecs_query_changed" (mem q)))))

(defn ecs-iter-changed [it]
  (not (zero? (raw-call "ecs_iter_changed" (iter-ptr it)))))

(defn ecs-iter-skip [it]
  (raw-call "ecs_iter_skip" (iter-ptr it)))

(defn ecs-run [w system delta-time param]
  (raw-call "ecs_run" (mem w) system (double delta-time) (mem param)))

(defn ecs-enqueue [w event-desc]
  (raw-call "vybe_flecs_enqueue_map" (mem w) (mem event-desc)))

(defn ecs-system-get [w e]
  (raw-call "ecs_system_get" (mem w) e))

(defn ecs-query-get [w e]
  (raw-call "ecs_query_get" (mem w) e))

(defn ecs-observer-get [w e]
  (raw-call "ecs_observer_get" (mem w) e))

(defn ecs-system-stats-get [w e stats]
  (not (zero? (raw-call "ecs_system_stats_get" (mem w) e (mem stats)))))

(defn ecs-query-str [q]
  (vw/read-c-string @module* (raw-call "ecs_query_str" (mem q))))

(defn ecs-term-str [w term]
  (vw/read-c-string @module* (raw-call "ecs_term_str" (mem w) (mem term))))

(defn ecs-term-ref-is-set [term]
  (not (zero? (raw-call "ecs_term_ref_is_set" (mem term)))))

(defn ecs-observer-init [w desc]
  (let [{:keys [entity query events yield-existing yield_existing callback callback-ctx callback_ctx]} (desc-map desc)
        callback (some-nonzero callback)
        callback-ctx (some-nonzero callback-ctx callback_ctx callback)
        {size :size
         entity-off :entity
         query-off :query
         events-off :events
         yield-existing-off :yield-existing
         callback-off :callback
         callback-ctx-off :callback-ctx} @observer-desc-abi*
        desc-p (alloc size)
        allocated* (atom [])]
    (try
      (zero! desc-p size)
      (vw/write-i64! @module* (+ desc-p entity-off) (long (or entity 0)))
      (when query
        (write-query-desc! (+ desc-p query-off) query allocated*))
      (doseq [[idx event] (map-indexed vector (take 8 (filter some-nonzero events)))]
        (vw/write-i64! @module* (+ desc-p events-off (* idx 8)) (long event)))
      (vw/write-i8! @module* (+ desc-p yield-existing-off)
                    (if (or yield-existing yield_existing) 1 0))
      (vw/write-i32! @module* (+ desc-p callback-off)
                     (int (raw-call "vybe_flecs_system_trampoline_addr")))
      (vw/write-i32! @module* (+ desc-p callback-ctx-off)
                     (int (or callback-ctx 0)))
      (raw-call "ecs_observer_init" (mem w) desc-p)
      (finally
        (run! free @allocated*)
        (free desc-p)))))
