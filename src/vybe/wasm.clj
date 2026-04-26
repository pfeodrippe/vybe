(ns vybe.wasm
  (:refer-clojure :exclude [free null?])
  (:require
   [clojure.set :as set]
   [clojure.pprint :as pp]
   [potemkin :refer [def-map-type]]
   [clojure.string :as str]
   [vybe.panama :as panama]
   [vybe.wasm.alloc :as alloc]
   [vybe.wasm.callback :as callback]
   [vybe.wasm.layout :as layout]
   [vybe.wasm.memory :as memory]
   [vybe.wasm.runtime :as runtime])
  (:import
   (java.lang.foreign MemoryLayout MemorySegment ValueLayout)
   (vybe.panama IVybeMemorySegment IVybeWithComponent VybeComponent VybePMap)))

(def load-module runtime/load-module)
(def call runtime/call)
(def export-function runtime/export-function)
(def export-global runtime/export-global)
(def global-address runtime/global-address)
(def global-i32 runtime/global-i32)
(def global-i64 runtime/global-i64)
(def host-function runtime/host-function)
(def unwind-raise-exception-host-function runtime/unwind-raise-exception-host-function)
(def emscripten-notify-memory-growth-host-function runtime/emscripten-notify-memory-growth-host-function)
(def empty-result runtime/empty-result)
(def zero-result runtime/zero-result)
(def raw-i32->float runtime/raw-i32->float)
(def raw-i64->double runtime/raw-i64->double)

(def memory memory/memory)
(def ptr memory/ptr)
(def ptr? memory/ptr?)
(def u32 memory/u32)
(def u64 memory/u64)
(def offset memory/offset)
(def read-i8 memory/read-i8)
(def write-i8! memory/write-i8!)
(def read-i16 memory/read-i16)
(def write-i16! memory/write-i16!)
(def read-i32 memory/read-i32)
(def write-i32! memory/write-i32!)
(def read-i64 memory/read-i64)
(def write-i64! memory/write-i64!)
(def read-f32 memory/read-f32)
(def write-f32! memory/write-f32!)
(def read-f64 memory/read-f64)
(def write-f64! memory/write-f64!)
(def read-bytes memory/read-bytes)
(def write-bytes! memory/write-bytes!)
(def fill! memory/fill!)
(def zero! memory/zero!)

(def malloc alloc/malloc)
(def free alloc/free)
(def write-c-string! alloc/write-c-string!)
(def read-c-string alloc/read-c-string)
(def with-alloc* alloc/with-alloc*)
(defmacro with-alloc
  "Bind `ptr` to Wasm memory allocated by `malloc` for the scope of `body`."
  [[ptr module bytes] & body]
  `(alloc/with-alloc [~ptr ~module ~bytes]
     ~@body))
(def with-c-string* alloc/with-c-string*)
(defmacro with-c-string
  "Bind `ptr` to a temporary UTF-8 C string in Wasm memory."
  [[ptr module s] & body]
  `(alloc/with-c-string [~ptr ~module ~s]
     ~@body))

(defonce callback-registry (callback/registry))
(defn register-callback! [f] (callback/register! callback-registry f))
(defn unregister-callback! [id] (callback/unregister! callback-registry id))
(defn callback [id] (callback/callback callback-registry id))
(def callback-host-function callback/host-function)

(def helper-layout layout/helper-layout)
(def write-field! layout/write-field!)
(def read-field layout/read-field)

;; Panama-shaped compatibility layer.
;;
;; The existing high-level native code is written against `vybe.panama`.
;; Wasm uses numeric wasm32 pointers instead of `MemorySegment`, so this
;; namespace keeps the same public surface where possible and swaps pointer
;; operations to Wasm memory when the input is numeric.

(defonce ^:private default-module* (atom nil))

(def ^:dynamic *dyn-arena*
  "Compatibility binding for code that scopes Panama arenas around tests."
  nil)

(defn set-default-module!
  "Set the module used by Panama-compatible helpers that receive raw pointers."
  [module]
  (reset! default-module* module)
  nil)

(defn default-module
  "Return the module used by pointer helpers, or throw when none is loaded."
  []
  (or @default-module*
      (throw (ex-info "No default Wasm module is loaded"
                      {:hint "Load a native Wasm backend before reading raw pointers."}))))

(def null 0)

(defn null?
  [p]
  (or (nil? p)
      (and (number? p) (zero? (long p)))
      (and (not (number? p)) (panama/null? p))))

(defn mem
  ([v]
   (cond
     (number? v) (long v)
     :else (let [p (panama/mem v)]
             (if (instance? MemorySegment p)
               (.address ^MemorySegment p)
               p))))
  ([identifier v]
   (panama/mem identifier v))
  ([identifier v mem-size]
   (panama/mem identifier v mem-size)))

(defn mem?
  [v]
  (or (number? v)
      (panama/mem? v)))

(defn ->string
  "Read a C string from a Wasm pointer, or delegate existing JVM values."
  [p]
  (cond
    (nil? p) nil
    (string? p) p
    (number? p) (read-c-string (default-module) p)
    :else (panama/->string p)))

(defn &
  "Return a raw address/pointer for either Wasm or Panama-backed values."
  [v]
  (if (number? v)
    (long v)
    (panama/& v)))

(def address &)

(declare p->map)

(defn- field-value
  [module ptr {:keys [type offset]}]
  (let [p (+ (long ptr) (long offset))]
    (case type
      :double (read-f64 module p)
      :float (read-f32 module p)
      :long (read-i64 module p)
      :long-long (read-i64 module p)
      :uint (Integer/toUnsignedLong (read-i32 module p))
      :int (read-i32 module p)
      :short (read-i16 module p)
      :byte (read-i8 module p)
      :boolean (not (zero? (read-i8 module p)))
      :char (char (read-i16 module p))
      :string (->string (read-i32 module p))
      :pointer (read-i32 module p)
      :* (read-i32 module p)
      (if (instance? VybeComponent type)
        (p->map p type)
        (read-i32 module p)))))

(defn- write-field-value!
  [module ptr {:keys [type offset]} v]
  (let [p (+ (long ptr) (long offset))]
    (case type
      :double (write-f64! module p (double (or v 0.0)))
      :float (write-f32! module p (float (or v 0.0)))
      :long (write-i64! module p (long (or v 0)))
      :long-long (write-i64! module p (long (or v 0)))
      :uint (write-i32! module p (unchecked-int (long (or v 0))))
      :int (write-i32! module p (int (or v 0)))
      :short (write-i16! module p (short (or v 0)))
      :byte (write-i8! module p (byte (or v 0)))
      :boolean (write-i8! module p (if v 1 0))
      :char (write-i16! module p (int v))
      :pointer (write-i32! module p (int (or v 0)))
      :* (write-i32! module p (int (or v 0)))
      (if (instance? VybeComponent type)
        (doseq [[nested-k nested-field] (.fields ^VybeComponent type)]
          (write-field-value! module p nested-field (get v nested-k)))
        (write-i32! module p (int (or v 0))))))
  nil)

(declare pmap-metadata vectorish->seq)

(def-map-type WasmPMap [module ptr component mta]
  (get [this k default-value]
       (if-let [field (get (.fields ^VybeComponent component) k)]
         (field-value module ptr field)
         default-value))
  (assoc [this k v]
         (if (:vp/const mta)
           (throw (ex-info "WasmPMap is set as a constant, mutation is not allowed"
                           {:pointer ptr
                            :component component}))
           (write-field-value! module ptr
                               (get (.fields ^VybeComponent component) k)
                               v))
         this)
  (dissoc [_ k]
          (throw (ex-info "WasmPMap dissoc not applicable"
                          {:k k})))
  (keys [_] (keys (.fields ^VybeComponent component)))
  (keySet [this] (set (keys this)))
  (meta [this] (merge mta (pmap-metadata this)))
  (with-meta [_ mta] (WasmPMap. module ptr component mta))

  clojure.lang.IMapEntry
  (key [_] component)
  (val [this] this)

  java.util.Map$Entry
  (getValue [this] (.val this))
  (getKey [this] (.key this))

  clojure.lang.IDeref
  (deref [this] (when-let [f (:vp/deref (.opts ^VybeComponent component))]
                  (f this)))

  IVybeWithComponent
  (component [_] component)

  Object
  (toString [this]
    (str {(symbol (.get (.name (.layout ^VybeComponent component))))
          (into {} this)})))

(defmethod print-method WasmPMap
  [^WasmPMap o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch WasmPMap
  [^WasmPMap o]
  (pp/simple-dispatch (into {} o)))

(defmulti pmap-metadata
  (fn [v]
    (if (instance? WasmPMap v)
      (.-component ^WasmPMap v)
      v)))

(defmethod pmap-metadata :default [_] nil)

(defn pmap?
  [v]
  (or (instance? VybePMap v)
      (instance? WasmPMap v)))

(defn p->map
  ([ptr component]
   (p->map ptr component nil))
  ([ptr component {:keys [as-map] :as _opts}]
   (let [m (if (number? ptr)
             (WasmPMap. (default-module) (long ptr) component nil)
             (panama/p->map ptr component))]
     (cond->> m
       as-map (into {})))))

(defmacro as
  ([v component]
   `(let [v# ~v]
      (if (mem? v#)
        (p->map v# ~component nil)
        v#)))
  ([v component opts]
   `(let [v# ~v]
      (if (mem? v#)
        (p->map v# ~component ~opts)
        v#))))

(defn ->with-pmap
  [p-map]
  (if (instance? WasmPMap p-map)
    (if-let [to-with-pmap (-> ^WasmPMap p-map .-component .to_with_pmap)]
      (to-with-pmap p-map)
      p-map)
    (panama/->with-pmap p-map)))

(defn component?
  [v]
  (panama/component? v))

(defn component
  [v]
  (or (when (instance? WasmPMap v)
        (.-component ^WasmPMap v))
      (panama/component v)))

(def make-components panama/make-components)
(def comp-cache panama/comp-cache)
(def cache-comp panama/cache-comp)
(def comp-name panama/comp-name)
(def sizeof panama/sizeof)
(def alignof panama/alignof)
(def default-arena panama/default-arena)
(def clone panama/clone)
(def try-string panama/try-string)
(def alloc-native panama/alloc)
(def address->mem panama/address->mem)

(defn int*
  "Allocate a Wasm int pointer in the default module."
  [v]
  (let [p (malloc (default-module) 4)]
    (write-i32! (default-module) p (int v))
    p))

(defmacro with-arena-root
  [& body]
  `(panama/with-arena-root
     ~@body))

(defmacro if-windows?
  [then else]
  `(panama/if-windows? ~then ~else))

(defn wasm-layout
  "Create a C ABI layout descriptor from `sizeof`/`offsetof` data.

  `fields` entries are `[field type offset]`. The resulting component uses the
  compiled C module as the layout authority while preserving the `defcomp`
  call shape used by Panama-backed code.
  "
  [name size fields]
  {:vybe.wasm/layout true
   :name name
   :size size
   :fields fields})

(defn- segment-layout
  [t]
  (case t
    :double ValueLayout/JAVA_DOUBLE
    :float ValueLayout/JAVA_FLOAT
    :long ValueLayout/JAVA_LONG
    :long-long ValueLayout/JAVA_LONG
    :uint ValueLayout/JAVA_INT
    :int ValueLayout/JAVA_INT
    :short ValueLayout/JAVA_SHORT
    :byte ValueLayout/JAVA_BYTE
    :boolean ValueLayout/JAVA_BYTE
    :char ValueLayout/JAVA_SHORT
    :string ValueLayout/JAVA_INT
    :pointer ValueLayout/JAVA_INT
    :* ValueLayout/JAVA_INT
    nil))

(defn- coerce-segment-value
  [t v]
  (case t
    :double (double (or v 0.0))
    :float (float (or v 0.0))
    :long (long (or v 0))
    :long-long (long (or v 0))
    :uint (unchecked-int (long (or v 0)))
    :int (int (or v 0))
    :short (short (or v 0))
    :byte (byte (or v 0))
    :boolean (byte (if v 1 0))
    :char (short (int (or v 0)))
    :string (int (if (string? v)
                   (write-c-string! (default-module) v)
                   (mem (or v 0))))
    :pointer (int (mem (or v 0)))
    :* (int (mem (or v 0)))
    v))

(defn- segment-getter
  [t offset]
  (fn [^MemorySegment mem-segment]
    (case t
      :double (.get mem-segment ValueLayout/JAVA_DOUBLE offset)
      :float (.get mem-segment ValueLayout/JAVA_FLOAT offset)
      :long (.get mem-segment ValueLayout/JAVA_LONG offset)
      :long-long (.get mem-segment ValueLayout/JAVA_LONG offset)
      :uint (Integer/toUnsignedLong (.get mem-segment ValueLayout/JAVA_INT offset))
      :int (.get mem-segment ValueLayout/JAVA_INT offset)
      :short (.get mem-segment ValueLayout/JAVA_SHORT offset)
      :byte (.get mem-segment ValueLayout/JAVA_BYTE offset)
      :boolean (not (zero? (.get mem-segment ValueLayout/JAVA_BYTE offset)))
      :char (char (.get mem-segment ValueLayout/JAVA_SHORT offset))
      :string (->string (.get mem-segment ValueLayout/JAVA_INT offset))
      :pointer (.get mem-segment ValueLayout/JAVA_INT offset)
      :* (.get mem-segment ValueLayout/JAVA_INT offset)
      nil)))

(defn- scalar-array-getter
  [t offset elem-size array-count]
  (let [getter (segment-getter t 0)]
    (when getter
      (fn [^MemorySegment mem-segment]
        (mapv (fn [idx]
                (getter (.asSlice mem-segment
                                  (+ offset (* idx elem-size))
                                  elem-size)))
              (range array-count))))))

(defn- segment-builder
  [t offset]
  (let [layout (segment-layout t)]
    (fn [^MemorySegment mem-segment value]
      (when layout
        (.set mem-segment layout offset (coerce-segment-value t value))))))

(defn- run-segment-params!
  [^MemorySegment mem-segment params fields init c]
  (cond
    init
    (run-segment-params! mem-segment (init params) fields nil c)

    (vector? params)
    (mapv (fn [[_field {:keys [builder]}] value]
            (builder mem-segment value))
          fields
          params)

    (and (= 1 (count fields)) (not (map? params)))
    ((:builder (val (first fields))) mem-segment params)

    :else
    (run! (fn [[field value]]
            (let [f (:builder (get fields field))]
              (try
                (f mem-segment value)
                (catch Exception e
                  (throw (ex-info "Error when setting params for a Wasm component"
                                  {:field field
                                   :value value
                                   :value-type (type value)
                                   :fields fields
                                   :mem-segment mem-segment}
                                  e))))))
          params)))

(defn- component-segment
  [component value]
  (cond
    (instance? MemorySegment value) value
    (instance? IVybeMemorySegment value) (.mem_segment ^IVybeMemorySegment value)
    :else (.mem_segment (component value))))

(defn- component-init
  [opts fields]
  (let [constructor (:vp/constructor opts)]
    (fn [mem-segment value c]
      (run-segment-params! mem-segment value fields constructor c))))

(defn- normalized-opts
  [opts]
  (set/rename-keys (or opts {})
                   {:constructor :vp/constructor
                    :to-with-pmap :vp/to-with-pmap
                    :byte-alignment :vp/byte-alignment}))

(defn- wasm-layout->component
  ([layout]
   (wasm-layout->component layout nil))
  ([{:keys [name size fields]} opts]
  (let [layout (-> (MemoryLayout/sequenceLayout (long size) ValueLayout/JAVA_BYTE)
                   (.withName (str name)))
        opts (normalized-opts opts)
        fields (->> fields
                    (map-indexed
                     (fn [idx field-spec]
                       (let [{:keys [field type offset component array-count elem-size]}
                             (if (map? field-spec)
                               field-spec
                               (let [[field type offset] field-spec]
                                 {:field field :type type :offset offset}))
                             type (or component type)
                             getter (cond
                                      (and component array-count)
                                      (fn [^MemorySegment mem-segment]
                                        (mapv (fn [idx]
                                                (panama/p->map
                                                 (.asSlice mem-segment
                                                           (+ offset (* idx elem-size))
                                                           elem-size)
                                                 component))
                                              (range array-count)))

                                      component
                                      (fn [^MemorySegment mem-segment]
                                        (panama/p->map (.asSlice mem-segment
                                                                 offset
                                                                 (.byteSize (.layout ^VybeComponent component)))
                                                       component))

                                      (nil? array-count)
                                      (segment-getter type offset)

                                      :else
                                      (scalar-array-getter
                                       type
                                       offset
                                       (or elem-size
                                           (case type
                                             (:long :long-long :double) 8
                                             (:int :uint :float :pointer :*) 4
                                             (:short :char) 2
                                             1))
                                       array-count))
                             builder (cond
                                       (and component array-count)
                                       (fn [^MemorySegment mem-segment values]
                                         (doseq [[idx value] (map-indexed vector values)]
                                           (when (< idx array-count)
                                             (MemorySegment/copy
                                              (component-segment component value)
                                              0
                                              mem-segment
                                              (+ offset (* idx elem-size))
                                              elem-size))))

                                       component
                                       (fn [^MemorySegment mem-segment value]
                                         (MemorySegment/copy
                                          (component-segment component value)
                                          0
                                          mem-segment
                                          offset
                                          (.byteSize (.layout ^VybeComponent component))))

                                       array-count
                                       (let [elem-layout (segment-layout type)
                                             elem-size (or elem-size
                                                           (case type
                                                             (:long :long-long :double) 8
                                                             (:int :uint :float :pointer :*) 4
                                                             (:short :char) 2
                                                             1))]
                                         (fn [^MemorySegment mem-segment values]
                                           (doseq [[idx value] (map-indexed vector
                                                                            (or (vectorish->seq values) []))]
                                             (when (and elem-layout (< idx array-count))
                                               (.set mem-segment elem-layout
                                                     (+ offset (* idx elem-size))
                                                     (coerce-segment-value type value))))))

                                       :else
                                       (segment-builder type offset))]
                         [field (cond-> {:idx idx
                                          :type type
                                          :offset offset
                                          :component component
                                          :array-count array-count
                                          :elem-size elem-size}
                                  getter (assoc :getter getter)
                                  builder (assoc :builder builder))])))
                    (into {}))]
    (VybeComponent. layout fields
                    (component-init opts fields)
                    (:vp/to-with-pmap opts)
                    opts))))

(defn- align-to
  [offset alignment]
  (let [rem (mod offset alignment)]
    (if (zero? rem)
      offset
      (+ offset (- alignment rem)))))

(defn- type-size-align
  [t]
  (cond
    (instance? VybeComponent t)
    [(sizeof t) (max 1 (alignof t))]

    (and (vector? t) (= :vec (first t)))
    (let [[_ {:keys [size]} elem-type] t
          [elem-size elem-align] (type-size-align elem-type)]
      [(* size elem-size) elem-align])

    :else
    (let [size (case t
                 (:byte :boolean) 1
                 (:short :char) 2
                 (:int :uint :float :pointer :* :string) 4
                 (:long :long-long :double) 8
                 4)]
      [size size])))

(defn- schema-field
  [offset [field maybe-meta maybe-type]]
  (let [type (if maybe-type maybe-type maybe-meta)
        [size alignment] (type-size-align type)
        offset (align-to offset alignment)
        field-spec (cond
                     (instance? VybeComponent type)
                     {:field field
                      :component type
                      :offset offset}

                     (and (vector? type) (= :vec (first type)))
                     (let [[_ {:keys [size]} elem-type] type
                           [elem-size _] (type-size-align elem-type)]
                       {:field field
                        :type elem-type
                        :offset offset
                        :array-count size
                        :elem-size elem-size})

                     :else
                     {:field field
                      :type type
                      :offset offset})]
    [(+ offset size) field-spec]))

(defn- schema->wasm-layout
  [identifier schema]
  (loop [offset 0
         fields []
         schema schema]
    (if-let [field (first schema)]
      (let [[next-offset field-spec] (schema-field offset field)]
        (recur next-offset (conj fields field-spec) (rest schema)))
      (wasm-layout identifier offset fields))))

(defn- alias-component
  [identifier opts ^VybeComponent component]
  (let [opts (normalized-opts (merge (.opts component) opts))
        layout (.withName (.layout component) (str identifier))
        fields (.fields component)]
    (VybeComponent. layout fields
                    (component-init opts fields)
                    (:vp/to-with-pmap opts)
                    opts)))

(defn make-component
  ([schema]
   (if (and (map? schema) (:vybe.wasm/layout schema))
     (wasm-layout->component schema)
     (panama/make-component schema)))
  ([identifier schema]
   (cond
     (and (map? schema) (:vybe.wasm/layout schema))
     (let [component (wasm-layout->component (assoc schema :name identifier))]
       (panama/cache-comp identifier component)
       component)

     (instance? VybeComponent schema)
     (let [component (alias-component identifier nil schema)]
       (panama/cache-comp identifier component)
       component)

     (vector? schema)
     (let [component (wasm-layout->component (schema->wasm-layout identifier schema))]
       (panama/cache-comp identifier component)
       component)

     :else
     (panama/make-component identifier schema)))
  ([identifier opts schema]
   (cond
     (and (map? schema) (:vybe.wasm/layout schema))
     (let [component (wasm-layout->component (assoc schema :name identifier) opts)]
       (panama/cache-comp identifier component)
       component)

     (instance? VybeComponent schema)
     (let [component (alias-component identifier opts schema)]
       (panama/cache-comp identifier component)
       component)

     (vector? schema)
     (let [component (wasm-layout->component (schema->wasm-layout identifier schema) opts)]
       (panama/cache-comp identifier component)
       component)

     :else
     (panama/make-component identifier opts schema))))

(defmacro defcomp
  [& args]
  (let [sym (first args)
        [maybe-doc maybe-opts] (rest args)
        {:keys [doc opts]} {:doc (cond
                                   (string? maybe-doc) maybe-doc
                                   (string? maybe-opts) maybe-opts)
                            :opts (cond
                                    (map? maybe-doc) maybe-doc
                                    (map? maybe-opts) maybe-opts)}
        schema (last args)
        opts (cond-> (or opts {})
               doc (assoc :doc doc))]
    `(do (def ~(vary-meta sym merge {:tag `VybeComponent} opts)
           (make-component
            (quote ~(symbol (str *ns*) (str sym)))
            ~opts
            ~schema))
         (alter-meta! (var ~sym) update :doc #(str "VybeComponent\n\n"
                                                   ~sym
                                                   (when %
                                                     (str "\n\n" %))))
         (var ~sym))))

(defn- vectorish->seq
  [v]
  (cond
    (nil? v) nil
    (sequential? v) v
    (map? v) (map v [:x :y :z :w])
    :else nil))

(declare primitive-size write-component!)

(defn- write-array!
  [module ptr type array-count elem-size values]
  (let [values (or (vectorish->seq values) [])]
    (doseq [idx (range array-count)
            :let [p (+ ptr (* idx elem-size))
                  v (nth values idx 0)]]
      (case type
        :double (write-f64! module p (double (or v 0.0)))
        :float (write-f32! module p (float (or v 0.0)))
        :long (write-i64! module p (long (or v 0)))
        :long-long (write-i64! module p (long (or v 0)))
        :uint (write-i32! module p (unchecked-int (long (or v 0))))
        :int (write-i32! module p (int (or v 0)))
        :short (write-i16! module p (short (or v 0)))
        :byte (write-i8! module p (byte (or v 0)))
        :boolean (write-i8! module p (if v 1 0))
        :char (write-i16! module p (int (or v 0)))
        :pointer (write-i32! module p (int (mem (or v 0))))
        :* (write-i32! module p (int (mem (or v 0))))
        nil)))
  ptr)

(defn write-component!
  "Copy component map data into Wasm linear memory at `ptr`."
  ([component ptr m]
   (write-component! (default-module) component ptr m))
  ([module component ptr m]
   (doseq [[k {:keys [type offset component array-count elem-size]}] (.fields ^VybeComponent component)
           :let [p (+ (long ptr) (long offset))
                 v (get m k)]]
     (cond
       component
       (write-component! module component p v)

       array-count
       (write-array! module p type array-count
                     (or elem-size (primitive-size type))
                     v)

       :else
       (write-field-value! module ptr {:type type :offset offset} v)))
   ptr))

(defn read-component
  "Read a component map from Wasm linear memory at `ptr`."
  ([component ptr]
   (read-component (default-module) component ptr))
  ([module component ptr]
   (into {}
         (map (fn [[k {:keys [type offset component array-count elem-size]}]]
                (let [p (+ (long ptr) (long offset))]
                  [k (cond
                       component (read-component module component p)
                       array-count (mapv #(field-value module
                                                       (+ p (* % (or elem-size
                                                                    (primitive-size type))))
                                                       {:type type :offset 0})
                                         (range array-count))
                       :else (field-value module ptr {:type type
                                                      :offset offset}))])))
         (.fields ^VybeComponent component))))

(defn p->value
  [ptr component]
  (if (number? ptr)
    (if (instance? VybeComponent component)
      (p->map ptr component)
      (field-value (default-module) ptr {:type component :offset 0}))
    (panama/p->value ptr component)))

(defn- primitive-size
  [t]
  (case t
    (:byte :boolean) 1
    (:short :char) 2
    (:int :uint :float :pointer :*) 4
    (:long :long-long :double) 8
    (sizeof t)))

(defn arr
  ([c-vec]
   (panama/arr c-vec))
  ([primitive-vector-or-size c-or-layout]
   (panama/arr primitive-vector-or-size c-or-layout))
  ([ptr size c-or-layout]
   (if (number? ptr)
     (let [el-size (primitive-size (if (vector? c-or-layout)
                                     (first c-or-layout)
                                     c-or-layout))]
       (mapv (fn [idx]
               (p->value (+ (long ptr) (* idx el-size))
                         (if (vector? c-or-layout)
                           (last c-or-layout)
                           c-or-layout)))
             (range size)))
     (panama/arr ptr size c-or-layout))))

(defn wasm-desc-map
  [m]
  (let [m (if (map? m) m (into {} m))]
    (into {}
          (map (fn [[k v]]
                 [(keyword (str/replace (name k) "_" "-")) v]))
          m)))

(defmacro jx-i
  [m _klass]
  `(wasm-desc-map ~m))

(defn jx-p->map*
  [ptr component]
  (if (str/ends-with? (str (comp-name component)) "/iter_t")
    ((requiring-resolve 'vybe.flecs.wasm-c/make-iter) ptr)
    (p->map ptr component)))

(defmacro jx-p->map
  [ptr component]
  `(jx-p->map* ~ptr ~component))

(defmacro with-apply
  [_klass params & body]
  `(register-callback! (fn ~params ~@body)))

(defn- -opaque-to-with-pmap
  [_identifier]
  identity)

(defmacro defopaques
  "Define Wasm opaque pointer factories."
  [& syms]
  `(do
     ~@(for [sym syms]
         `(def ~sym
            (reify clojure.lang.IFn
              (invoke [_# ptr#] (mem ptr#)))))))
