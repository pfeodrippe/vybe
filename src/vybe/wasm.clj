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
   (vybe.panama IVybeMemorySegment IVybeWithComponent IVybeWithPMap VybeComponent VybePMap)))

(def load-module runtime/load-module)
(def load-module-from-bytes runtime/load-module-from-bytes)
(def load-module-from-file runtime/load-module-from-file)
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

(declare default-module primitive-size)

(defn zero!
  "Fill Wasm memory with zeroes.

  Supports both `(zero! module ptr byte-size)` and the Panama-shaped
  `(zero! ptr element-count type)` form against the default module."
  [module-or-ptr ptr-or-element-count byte-size-or-type]
  (if (map? module-or-ptr)
    (memory/zero! module-or-ptr ptr-or-element-count byte-size-or-type)
    (memory/zero! (default-module)
                  module-or-ptr
                  (* (long ptr-or-element-count)
                     (long (primitive-size byte-size-or-type))))))

(def ^:dynamic *alloc-scope*
  nil)

(defonce allocation-high-water* (atom {}))

(defn allocation-high-water
  "Return the highest allocated byte offset tracked for the module memory."
  [module]
  (get @allocation-high-water* (:memory module) 0))

(defn- track-high-water!
  [module ptr bytes]
  (swap! allocation-high-water*
         update
         (:memory module)
         (fnil max 0)
         (+ (long ptr) (long bytes)))
  ptr)

(defn- track-allocation!
  [module ptr]
  (when *alloc-scope*
    (swap! *alloc-scope* conj [(:memory module) module ptr]))
  ptr)

(defn- memory-byte-size
  [module]
  (* 65536 (long (.pages (memory module)))))

(defn- valid-memory-range?
  [module ptr bytes]
  (let [ptr (long ptr)
        bytes (long bytes)
        limit (memory-byte-size module)]
    (and (pos? ptr)
         (not (neg? bytes))
         (<= (+ ptr bytes) limit))))

(defn- untrack-allocation!
  [module ptr]
  (when *alloc-scope*
    (let [memory (:memory module)
          ptr (long ptr)]
      (swap! *alloc-scope*
             (fn [allocs]
               (vec (remove (fn [[entry-memory _entry-module entry-ptr]]
                              (and (identical? memory entry-memory)
                                   (= ptr (long entry-ptr))))
                            allocs))))))
  nil)

(defn free-all!
  [allocs]
  (let [entries (reverse @allocs)]
    (reset! allocs [])
    (doseq [[_memory module ptr] (distinct entries)]
      (try
        (alloc/free module ptr)
        (catch RuntimeException e
          (throw (ex-info "Failed to free scoped Wasm allocation"
                          {:ptr ptr
                           :memory-bytes (memory-byte-size module)}
                          e)))))))

(defn malloc
  "Allocate bytes in Wasm memory and track it when inside `with-arena`."
  [module bytes]
  (let [ptr (alloc/malloc module bytes)]
    (when-not (valid-memory-range? module ptr bytes)
      (throw (ex-info "Wasm malloc returned an invalid pointer"
                      {:ptr ptr
                       :bytes bytes
                       :memory-bytes (memory-byte-size module)})))
    (track-high-water! module ptr bytes)
    (track-allocation! module ptr)))

(defn free
  "Free a Wasm pointer and remove it from the active allocation scope."
  [module ptr]
  (untrack-allocation! module ptr)
  (alloc/free module ptr))
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

(defonce fn-pointer-resolver* (atom nil))

(defn set-fn-pointer-resolver!
  "Install the resolver used when a Wasm struct field stores a C function pointer."
  [f]
  (reset! fn-pointer-resolver* f)
  nil)

(def helper-layout layout/helper-layout)

(defn layout
  [component]
  (.layout ^VybeComponent component))

(defn reinterpret
  [^MemorySegment mem-segment ^long size]
  (.reinterpret mem-segment size (panama/default-arena) nil))

(defn get-at
  [arr idx]
  (panama/get-at arr idx))

(defn set-at
  [arr idx v]
  (panama/set-at arr idx v))
(def layout-equal? panama/layout-equal?)
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

(definterface IVybeWasmPointer
  (^long wasm_ptr []))

(definterface IVybeWasmSeq)

(defonce ^:private mem-cache* (atom {}))

(defn- byte-array?
  [v]
  (instance? (Class/forName "[B") v))

(defn- wasm-bytes
  [v]
  (cond
    (string? v)
    (.getBytes (str v "\0") java.nio.charset.StandardCharsets/UTF_8)

    (byte-array? v)
    v

    (instance? MemorySegment v)
    (.toArray ^MemorySegment v ValueLayout/JAVA_BYTE)

    (instance? IVybeMemorySegment v)
    (.toArray (.mem_segment ^IVybeMemorySegment v) ValueLayout/JAVA_BYTE)

    :else
    (byte-array 0)))

(deftype WasmOpaque [opaque identifier component]
  IVybeWasmPointer
  (wasm_ptr [_] opaque)

  IVybeWithComponent
  (component [_] component)

  IVybeWithPMap
  (pmap [_]
    (component {:opaque opaque}))

  Object
  (toString [_]
    (str "#WasmOpaque[" identifier " " (format "\"0x%x\"" (long opaque)) "]")))

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
     (instance? IVybeWasmPointer v) (.wasm_ptr ^IVybeWasmPointer v)
     :else (let [p (panama/mem v)]
             (if (instance? MemorySegment p)
               (.address ^MemorySegment p)
               p))))
  ([identifier v]
   (let [bytes (wasm-bytes v)]
     (mem identifier v (alength bytes))))
  ([identifier v mem-size]
   (or (get @mem-cache* [identifier mem-size])
       (let [module (default-module)
             bytes (wasm-bytes v)
             ptr (alloc/malloc module (long mem-size))]
         (zero! module ptr (long mem-size))
         (write-bytes! module ptr bytes)
         (swap! mem-cache* assoc [identifier mem-size] ptr)
         ptr))))

(defn mem?
  [v]
  (or (number? v)
      (instance? IVybeWasmPointer v)
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
  (cond
    (number? v) (long v)
    (instance? IVybeWasmPointer v) (.wasm_ptr ^IVybeWasmPointer v)
    :else (panama/& v)))

(def address &)

(declare p->map primitive-size)

(defn- field-raw-value
  [module ptr {:keys [type offset component array-count elem-size] :as field}]
  (let [p (+ (long ptr) (long offset))
        type (or component type)]
    (cond
      array-count
      (mapv #(field-raw-value module
                              (+ p (* % (or elem-size (primitive-size type))))
                              (assoc field :offset 0 :array-count nil))
            (range array-count))

      (instance? VybeComponent type)
      (p->map p type {:module module})

      :else
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
        :pointer (Integer/toUnsignedLong (read-i32 module p))
        :* (Integer/toUnsignedLong (read-i32 module p))
        (read-i32 module p)))))

(defn- field-value
  [module ptr field]
  (let [value (field-raw-value module ptr field)
        getter (:vp/getter field)]
    (if getter
      (getter value)
      value)))

(declare write-component!)

(defn- write-field-value!
  [module ptr {:keys [type offset] :as field} v]
  (let [p (+ (long ptr) (long offset))
        setter (:vp/setter field)
        v (if setter
            (setter v)
            v)]
    (case type
      :double (write-f64! module p (double (or v 0.0)))
      :float (write-f32! module p (float (or v 0.0)))
      :long (write-i64! module p (long (or v 0)))
      :long-long (write-i64! module p (long (or v 0)))
      :uint (write-i32! module p (unchecked-int (long (or v 0))))
      :int (write-i32! module p (int (or v 0)))
      :short (write-i16! module p (short (or v 0)))
      :byte (write-i8! module p (unchecked-byte (long (or v 0))))
      :boolean (write-i8! module p (if v 1 0))
      :char (write-i16! module p (int v))
      :pointer (write-i32! module p (unchecked-int (mem (or v 0))))
      :* (write-i32! module p (unchecked-int (mem (or v 0))))
      (if (instance? VybeComponent type)
        (doseq [[nested-k nested-field] (.fields ^VybeComponent type)]
          (write-field-value! module p nested-field (get v nested-k)))
        (write-i32! module p
                    (unchecked-int
                     (cond
                       (panama/fn-descriptor? type)
                       ((or @fn-pointer-resolver*
                            (throw (ex-info "No Wasm function-pointer resolver is installed"
                                            {:fn-desc type})))
                        module type v)

                       (and (vector? type)
                            (contains? #{:pointer :*} (first type)))
                       (let [target (last type)]
                         (if (and (instance? VybeComponent target)
                                  (some? v)
                                  (not (number? v))
                                  (not (instance? IVybeWasmPointer v)))
                           (let [nested-ptr (malloc module (panama/sizeof target))]
                             (zero! module nested-ptr (panama/sizeof target))
                             (write-component! module target nested-ptr (into {} v))
                             nested-ptr)
                           (mem (or v 0))))

                       :else
                       (or v 0)))))))
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

  IVybeWasmPointer
  (wasm_ptr [_] ptr)

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
  ([ptr component {:keys [as-map module] :as _opts}]
   (let [m (if (number? ptr)
             (WasmPMap. (or module (default-module)) (long ptr) component nil)
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
      (when (instance? WasmOpaque v)
        (.-component ^WasmOpaque v))
      (panama/component v)))

(def make-components panama/make-components)
(def comp-cache panama/comp-cache)
(def cache-comp panama/cache-comp)
(def comp-name panama/comp-name)
(def sizeof panama/sizeof)
(def alignof panama/alignof)
(def default-arena panama/default-arena)
(defn clone
  "Clone either a Panama-backed value or a Wasm-backed component map."
  [v]
  (if (instance? WasmPMap v)
    (let [^WasmPMap v v
          module (.-module v)
          component (.-component v)
          size (sizeof component)
          ptr (malloc module size)]
      (zero! module ptr size)
      (write-component! module component ptr (into {} v))
      (WasmPMap. module ptr component (meta v)))
    (panama/clone v)))

(defn pmap->memory-segment
  "Return a JVM memory segment containing the bytes of a Panama or Wasm pmap."
  [v]
  (cond
    (instance? WasmPMap v)
    (let [^WasmPMap v v]
      (MemorySegment/ofArray
       (read-bytes (.-module v) (.-ptr v) (sizeof (.-component v)))))

    (instance? VybePMap v)
    (.mem_segment ^VybePMap v)

    :else
    (panama/mem v)))
(def update-aliases! panama/update-aliases!)
(defn arr?
  [v]
  (or (instance? IVybeWasmSeq v)
      (panama/arr? v)))
(def try-string panama/try-string)
(def alloc-native panama/alloc)
(def address->mem panama/address->mem)

(defn int*
  "Allocate a Wasm int pointer in the default module."
  [v]
  (let [p (malloc (default-module) 4)]
    (write-i32! (default-module) p (int v))
    p))

(defn float*
  "Allocate a Wasm float pointer in the default module."
  [v]
  (let [p (malloc (default-module) 4)]
    (write-f32! (default-module) p (float v))
    p))

(defn alloc
  "Allocate bytes in the default Wasm module.

  The optional alignment argument is accepted for Panama-shaped call sites; the
  Wasm allocator owns the actual alignment."
  ([size]
   (malloc (default-module) size))
  ([size _alignment]
   (alloc size)))

(defn new*
  "Create a component instance using the same call shape as `vybe.panama/new*`."
  ([c]
   (c))
  ([params c]
   (c params)))

(defmacro with-arena
  [_arena-params & body]
  `(let [allocs# (atom [])]
     (binding [*alloc-scope* allocs#]
       (try
         ~@body
         (finally
           (free-all! allocs#))))))

(defmacro with-arena-root
  [& body]
  `(binding [*alloc-scope* nil]
     ~@body))

(defmacro if-windows?
  [then else]
  `(panama/if-windows? ~then ~else))

(def windows?
  "If `true` we are on a Windows OS."
  panama/windows?)

(def linux?
  "If `true` we are on a Linux OS."
  panama/linux?)

(def mac?
  "If `true` we are on a Mac OS."
  panama/mac?)

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
    :byte (unchecked-byte (long (or v 0)))
    :boolean (byte (if v 1 0))
    :char (short (int (or v 0)))
    :string (int (if (string? v)
                   (write-c-string! (default-module) v)
                   (mem (or v 0))))
    :pointer (unchecked-int (mem (or v 0)))
    :* (unchecked-int (mem (or v 0)))
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
      :pointer (Integer/toUnsignedLong (.get mem-segment ValueLayout/JAVA_INT offset))
      :* (Integer/toUnsignedLong (.get mem-segment ValueLayout/JAVA_INT offset))
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
    (instance? WasmPMap value)
    (let [^WasmPMap value value]
      (MemorySegment/ofArray
       (read-bytes (.-module value) (.-ptr value) (sizeof component))))
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

(defn- normalized-field-opts
  [opts]
  (set/rename-keys (or opts {})
                   {:getter :vp/getter
                    :setter :vp/setter}))

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
                       (let [{:keys [field type offset component array-count elem-size]
                              :as field-spec}
                             (if (map? field-spec)
                               field-spec
                               (let [[field type offset] field-spec]
                                 {:field field :type type :offset offset}))
                             component (if (and (map? component)
                                                (:vybe.wasm/layout component))
                                         (wasm-layout->component component)
                                         component)
                             field-opts (select-keys (normalized-field-opts field-spec)
                                                     [:vp/getter :vp/setter])
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
                                  (seq field-opts) (merge field-opts)
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
  (let [field-opts (when maybe-type
                     (normalized-field-opts maybe-meta))
        type (if maybe-type maybe-type maybe-meta)
        [size alignment] (type-size-align type)
        offset (align-to offset alignment)
        field-spec (merge
                    field-opts
                    (cond
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
                      :offset offset}))]
    [(+ offset size) field-spec]))

(defn- schema->wasm-layout
  [identifier schema]
  (loop [offset 0
         fields []
         schema schema]
    (if-let [field (first schema)]
      (let [[next-offset field-spec] (schema-field offset field)]
        (recur (long next-offset) (conj fields field-spec) (rest schema)))
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
        :byte (write-i8! module p (unchecked-byte (long (or v 0))))
        :boolean (write-i8! module p (if v 1 0))
        :char (write-i16! module p (int (or v 0)))
        :pointer (write-i32! module p (unchecked-int (mem (or v 0))))
        :* (write-i32! module p (unchecked-int (mem (or v 0))))
        nil)))
  ptr)

(defn write-component!
  "Copy component map data into Wasm linear memory at `ptr`."
  ([component ptr m]
   (write-component! (default-module) component ptr m))
  ([module component ptr m]
   (doseq [[k {:keys [type offset component array-count elem-size] :as field}] (.fields ^VybeComponent component)
           :let [p (+ (long ptr) (long offset))
                 v (get m k)]]
     (cond
       component
       (write-component! module component p
                         (if-let [setter (:vp/setter field)]
                           (setter v)
                           v))

       array-count
       (write-array! module p type array-count
                     (or elem-size (primitive-size type))
                     (if-let [setter (:vp/setter field)]
                       (setter v)
                       v))

       :else
       (write-field-value! module ptr field v)))
   ptr))

(defn read-component
  "Read a component map from Wasm linear memory at `ptr`."
  ([component ptr]
   (read-component (default-module) component ptr))
  ([module component ptr]
   (into {}
         (map (fn [[k field]]
                [k (field-value module ptr field)]))
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

(deftype WasmPSeq [module ptr size component elem-size]
  IVybeWasmSeq

  IVybeWasmPointer
  (wasm_ptr [_] ptr)

  IVybeWithComponent
  (component [_] component)

  clojure.lang.Seqable
  (seq [_]
    (seq (mapv (fn [idx]
                 (p->value (+ (long ptr) (* idx (long elem-size)))
                           component))
               (range size))))

  clojure.lang.Counted
  (count [_] size)

  clojure.lang.Indexed
  (nth [_ idx]
    (if (and (<= 0 idx) (< idx size))
      (p->value (+ (long ptr) (* idx (long elem-size))) component)
      (throw (IndexOutOfBoundsException. (str idx)))))
  (nth [_ idx not-found]
    (if (and (<= 0 idx) (< idx size))
      (p->value (+ (long ptr) (* idx (long elem-size))) component)
      not-found))

  Object
  (toString [this]
    (str (vec (seq this)))))

(defn arr
  ([c-vec]
   (let [first-el (first c-vec)]
     (if (arr? first-el)
       (arr c-vec :pointer)
       (let [component (component first-el)
             size (count c-vec)
             elem-size (sizeof component)
             ptr (malloc (default-module) (* size elem-size))]
         (zero! (default-module) ptr (* size elem-size))
         (doseq [[idx v] (map-indexed vector c-vec)]
           (write-component! (default-module)
                             component
                             (+ ptr (* idx elem-size))
                             (into {} v)))
         (WasmPSeq. (default-module) ptr size component elem-size)))))
  ([primitive-vector-or-size c-or-layout]
   (if (sequential? primitive-vector-or-size)
     (let [values primitive-vector-or-size
           size (count values)
           elem-size (primitive-size c-or-layout)
           ptr (malloc (default-module) (* size elem-size))]
       (zero! (default-module) ptr (* size elem-size))
       (if (instance? VybeComponent c-or-layout)
         (doseq [[idx value] (map-indexed vector values)]
           (write-component! (default-module)
                             c-or-layout
                             (+ ptr (* idx elem-size))
                             (into {} value)))
         (write-array! (default-module) ptr c-or-layout size elem-size values))
       (WasmPSeq. (default-module) ptr size c-or-layout elem-size))
     (let [size primitive-vector-or-size
           elem-size (primitive-size c-or-layout)
           ptr (malloc (default-module) (* size elem-size))]
       (zero! (default-module) ptr (* size elem-size))
       (WasmPSeq. (default-module) ptr size c-or-layout elem-size))))
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

(defn p*
  "Dereference a pointer using the same call shape as `vybe.panama/p*`."
  ([pmap]
   (first (arr [pmap])))
  ([ptr c]
   (p->value ptr c)))

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
  (p->map ptr component))

(defmacro jx-p->map
  [ptr component]
  `(jx-p->map* ~ptr ~component))

(defmacro with-apply
  [_klass params & body]
  `(register-callback! (fn ~params ~@body)))

(defn -opaque-to-with-pmap
  [identifier]
  (fn [p-map]
    (->WasmOpaque (:opaque p-map) identifier (component p-map))))

(defmacro defopaques
  "Define Wasm opaque pointer factories."
  [& syms]
  `(do
     ~@(for [sym syms]
         (let [comp-sym (symbol (str (str sym) "___comp"))]
           `(let [identifier# (quote ~(symbol (str *ns*) (str `~sym)))]
              (defcomp ~(with-meta comp-sym
                          {:private true})
                {:to-with-pmap (-opaque-to-with-pmap identifier#)}
                [[:opaque :pointer]])
              (def ~sym
                (reify clojure.lang.IFn
                  (invoke [_# ptr#] (->WasmOpaque (mem ptr#) identifier# ~comp-sym))

                  IVybeWithComponent
                  (component [_#] ~comp-sym))))))))
