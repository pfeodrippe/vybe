(ns vybe.wasm.runtime
  (:require
   [clojure.java.io :as io])
  (:import
   (java.io ByteArrayInputStream)
   (com.dylibso.chicory.compiler InterpreterFallback MachineFactoryCompiler)
   (com.dylibso.chicory.runtime ByteArrayMemory ExportFunction HostFunction ImportFunction
                                ImportGlobal ImportMemory ImportTable Instance Memory Store
                                WasmFunctionHandle)
   (com.dylibso.chicory.wasi WasiOptions WasiPreview1)
   (com.dylibso.chicory.wasm Parser WasmModule)
   (com.dylibso.chicory.wasm.types ValueType)))

(defrecord WasmModuleInstance [^Instance instance ^Memory memory])

(defonce ^:private export-function-cache* (atom {}))
(defonce ^:private export-global-cache* (atom {}))

(def ^:private empty-result-array (long-array 0))
(def ^:private zero-result-array (long-array [0]))

(def value-types
  {:i32 ValueType/I32
   :i64 ValueType/I64
   :f32 ValueType/F32
   :f64 ValueType/F64})

(defn empty-result
  "Return the reusable empty Wasm host-function result."
  []
  empty-result-array)

(defn zero-result
  "Return the reusable zero-valued Wasm host-function result."
  []
  zero-result-array)

(defn raw-i32->float
  "Convert a raw i32 result into a JVM float."
  [x]
  (Float/intBitsToFloat (unchecked-int x)))

(defn raw-i64->double
  "Convert a raw i64 result into a JVM double."
  [x]
  (Double/longBitsToDouble (long x)))

(defn- resource-stream
  [resource-name]
  (or (io/input-stream (io/resource resource-name))
      (throw (ex-info "Missing WASM resource"
                      {:resource resource-name}))))

(defn- ->value-type
  [t]
  (or (value-types t)
      (throw (ex-info "Unsupported Wasm value type"
                      {:type t
                       :supported (set (keys value-types))}))))

(defn host-function
  "Create a Chicory host function.

  Options:
  - `:module` import module name, defaults to `\"env\"`.
  - `:name` import function name.
  - `:params` vector of `:i32`, `:i64`, `:f32`, or `:f64`.
  - `:results` vector of result types.
  - `:f` function called with `[instance args]` and returning a `long-array`."
  [{:keys [module name params results f]
    :or {module "env"
         params []
         results []}}]
  (HostFunction.
   module
   name
   (mapv ->value-type params)
   (mapv ->value-type results)
   (reify WasmFunctionHandle
     (apply [_ instance args]
       (or (f instance args)
           empty-result-array)))))

(defn unwind-raise-exception-host-function
  "Return a stub `_Unwind_RaiseException` host function for Emscripten output."
  []
  (host-function {:name "_Unwind_RaiseException"
                  :params [:i32]
                  :results [:i32]
                  :f (fn [_ _] zero-result-array)}))

(defn emscripten-notify-memory-growth-host-function
  "Return a no-op `emscripten_notify_memory_growth` host function."
  []
  (host-function {:name "emscripten_notify_memory_growth"
                  :params [:i32]
                  :f (fn [_ _] empty-result-array)}))

(defn store
  "Create a Chicory store with WASI Preview 1 and optional imports."
  [{:keys [host-functions host-globals host-memories host-tables]}]
  (let [wasi (-> (WasiOptions/builder)
                 (.build)
                 (->> (.withOptions (WasiPreview1/builder)))
                 (.build))
        functions (into-array ImportFunction host-functions)
        globals (into-array ImportGlobal host-globals)
        memories (into-array ImportMemory host-memories)
        tables (into-array ImportTable host-tables)]
    (cond-> (Store.)
      true (.addFunction (.toHostFunctions wasi))
      (seq host-functions) (.addFunction functions)
      (seq host-globals) (.addGlobal globals)
      (seq host-memories) (.addMemory memories)
      (seq host-tables) (.addTable tables))))

(defn compiled-machine-factory
  "Compile a Chicory machine factory and fail instead of interpreting."
  [^WasmModule module]
  (-> (MachineFactoryCompiler/builder module)
      (.withInterpreterFallback InterpreterFallback/FAIL)
      (.compile)))

(defn instantiate
  "Instantiate a parsed Wasm module."
  [^Store store ^WasmModule module]
  (-> (Instance/builder module)
      (.withImportValues (.toImportValues store))
      (.withMemoryFactory
       (reify java.util.function.Function
         (apply [_ limits]
           (ByteArrayMemory. limits))))
      (.withMachineFactory (compiled-machine-factory module))
      (.build)))

(defn- try-export-function
  [^Instance instance name]
  (try
    (.export instance name)
    (catch RuntimeException _
      nil)))

(defn export-function
  "Return a Wasm export function, trying both `name` and `_name`."
  [^WasmModuleInstance module name]
  (let [instance (:instance module)
        cache-key [instance name]]
    (or (get @export-function-cache* cache-key)
        (let [f (or (try-export-function instance name)
                    (try-export-function instance (str "_" name))
                    (throw (ex-info "Missing WASM export" {:export name})))]
          (swap! export-function-cache* assoc cache-key f)
          f))))

(defn- try-export-global
  [^Instance instance name]
  (try
    (.global (.exports instance) name)
    (catch RuntimeException _
      nil)))

(defn export-global
  "Return a Wasm exported global, trying both `name` and `_name`."
  [^WasmModuleInstance module name]
  (let [instance (:instance module)
        cache-key [instance name]]
    (or (get @export-global-cache* cache-key)
        (let [g (or (try-export-global instance name)
                    (try-export-global instance (str "_" name))
                    (throw (ex-info "Missing WASM global export"
                                    {:export name})))]
          (swap! export-global-cache* assoc cache-key g)
          g))))

(defn call
  "Call a Wasm export and return its first result, or `0` for void exports."
  [module name & args]
  (let [^ExportFunction f (export-function module name)
        result (.apply f (long-array args))]
    (if (and result (pos? (alength result)))
      (aget result 0)
      0)))

(defn global-address
  "Return the raw value of an exported global."
  [module name]
  (.getValue (export-global module name)))

(defn global-i32
  "Read an i32 from the address stored in an exported global."
  [module name]
  (.readInt ^Memory (:memory module) (int (global-address module name))))

(defn global-i64
  "Read an i64 from the address stored in an exported global."
  [module name]
  (.readLong ^Memory (:memory module) (int (global-address module name))))

(defn- load-module*
  [^java.io.InputStream in {:keys [initialize? after-init]
                            :or {initialize? true}
                            :as opts}]
  (let [store (store opts)
        parsed (Parser/parse in)
        instance (instantiate store parsed)
        module (->WasmModuleInstance instance (.memory instance))]
    (when (and initialize? (try-export-function instance "_initialize"))
      (call module "_initialize"))
    (when after-init
      (after-init module))
    module))

(defn load-module
  "Load and instantiate a Wasm module from a classpath resource.

  Options:
  - `:resource` classpath resource path.
  - `:host-functions` collection of Chicory host/import functions.
  - `:initialize?` calls `_initialize` when present, defaults to true.
  - `:after-init` optional function called with the loaded module."
  [{:keys [resource] :as opts}]
  (with-open [in (resource-stream resource)]
    (load-module* in opts)))

(defn load-module-from-bytes
  "Load and instantiate a Wasm module from a byte array using compiled mode."
  [bytes opts]
  (with-open [in (ByteArrayInputStream. bytes)]
    (load-module* in opts)))

(defn load-module-from-file
  "Load and instantiate a Wasm module from a filesystem path using compiled mode."
  [file opts]
  (with-open [in (io/input-stream file)]
    (load-module* in opts)))
