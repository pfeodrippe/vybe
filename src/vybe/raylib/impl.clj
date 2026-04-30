(ns vybe.raylib.impl
  (:require
   [clojure.string :as str]
   [vybe.panama :as vp]
   [vybe.raylib.abi :as abi]
   [vybe.raylib.lwjgl.wasm :as raylib-wasm]
   [vybe.type :as vt]
   [vybe.wasm :as vw])
  (:import
   (java.lang.foreign MemorySegment ValueLayout)
   (java.lang.management ManagementFactory)))

(set! *warn-on-reflection* true)

(defonce *state
  (atom {:buf-general []
         :buf1 []
         :buf2 []
         :front-buf? true}))

(defn module
  []
  (let [m (raylib-wasm/module)]
    (vw/set-default-module! m)
    m))

(defn -add-command
  [cmd {:keys [general prom form]
        :or {prom (promise)}}]
  (let [cmd-data {:cmd cmd
                  :prom prom
                  :form form}]
    (locking *state
      (swap! *state (fn [state]
                      (if general
                        (update state :buf-general conj cmd-data)
                        (-> state
                            (update :buf1 conj cmd-data)
                            (update :buf2 conj cmd-data))))))))

(defmacro add-command
  [cmd params]
  `(-add-command ~cmd (merge ~params {:form (quote ~&form)})))

(defmacro t
  "Run body on the Raylib main thread and return its value."
  [& body]
  `(let [res# (promise)]
     (add-command (fn [] ~@body) {:general true :prom res#})
     (let [v# (deref res#)]
       (when (:error v#)
         (throw (ex-info "Error while running command"
                         {:error (:error v#)
                          :form (quote ~&form)
                          :form-meta ~(meta &form)})))
       v#)))

(set! *warn-on-reflection* false)
(def ^:private get-first-thread
  (memoize
   (fn []
     (let [thread-mxbean (ManagementFactory/getThreadMXBean)
           thread-ids (seq (.getAllThreadIds thread-mxbean))]
       (when (seq thread-ids)
         (let [first-thread-id (apply min thread-ids)
               first-thread-info (.getThreadInfo thread-mxbean first-thread-id)]
           (when first-thread-info
             (.getThreadId first-thread-info))))))))
(set! *warn-on-reflection* true)

(defn first-thread?
  []
  (= (.getId (Thread/currentThread)) (get-first-thread)))

(defn- base-ctype
  [ctype]
  (-> (or ctype "")
      (str/replace #"\bconst\b" "")
      (str/replace #"\bstruct\s+" "")
      (str/replace #"\*" "")
      str/trim
      (str/split #"\s+")
      last
      abi/resolve-type-name))

(defn- component
  [ctype]
  (vw/make-component (abi/layout (base-ctype ctype))))

(def ^:private ctype->c-schema
  {"Matrix" vt/Transform
   "Quaternion" vt/Vector4
   "Vector2" vt/Vector2
   "Vector3" vt/Vector3
   "Vector4" vt/Vector4})

(defn- c-schema
  [{:keys [ctype schema]}]
  (or (get ctype->c-schema (name (base-ctype ctype)))
      schema))

(defn- function-desc
  [c-name]
  (let [{:keys [ret args]} (abi/function c-name)]
    (into [:fn (c-schema ret)]
          (mapv (fn [{:keys [symbol] :as arg}]
                  [(keyword symbol) (c-schema arg)])
                args))))

(defn- aggregate?
  [ctype]
  (abi/aggregate-type? ctype))

(defn- alloc-component!
  [m component value]
  (let [ptr (vw/malloc m (vw/sizeof component))]
    (vw/zero! m ptr (vw/sizeof component))
    (vw/write-component! m component ptr value)
    ptr))

(defn- uniform-type-width
  [uniform-type]
  (condp = (long uniform-type)
    (long (abi/constant :SHADER_UNIFORM_FLOAT)) 1
    (long (abi/constant :SHADER_UNIFORM_VEC2)) 2
    (long (abi/constant :SHADER_UNIFORM_VEC3)) 3
    (long (abi/constant :SHADER_UNIFORM_VEC4)) 4
    (long (abi/constant :SHADER_UNIFORM_INT)) 1
    (long (abi/constant :SHADER_UNIFORM_IVEC2)) 2
    (long (abi/constant :SHADER_UNIFORM_IVEC3)) 3
    (long (abi/constant :SHADER_UNIFORM_IVEC4)) 4
    (long (abi/constant :SHADER_UNIFORM_SAMPLER2D)) 1
    1))

(defn- uniform-type-integer?
  [uniform-type]
  (contains? #{(long (abi/constant :SHADER_UNIFORM_INT))
               (long (abi/constant :SHADER_UNIFORM_IVEC2))
               (long (abi/constant :SHADER_UNIFORM_IVEC3))
               (long (abi/constant :SHADER_UNIFORM_IVEC4))
               (long (abi/constant :SHADER_UNIFORM_SAMPLER2D))}
             (long uniform-type)))

(defn- uniform-scalars
  [value]
  (cond
    (nil? value) []
    (number? value) [value]
    (map? value) (cond
                   (contains? value :x) (cond-> [(:x value) (:y value)]
                                          (contains? value :z) (conj (:z value))
                                          (contains? value :w) (conj (:w value)))
                   (contains? value :r) [(:r value) (:g value) (:b value) (:a value)]
                   :else (mapcat uniform-scalars (vals value)))
    (sequential? value) (mapcat uniform-scalars value)
    :else [value]))

(defn- alloc-uniform-values!
  [m temps uniform-type value count]
  (let [width (uniform-type-width uniform-type)
        scalar-count (* (max 1 (long count)) (long width))
        integer? (uniform-type-integer? uniform-type)
        values (vec (take scalar-count
                          (concat (uniform-scalars value) (repeat 0))))
        ptr (vw/malloc m (* 4 scalar-count))]
    (vw/zero! m ptr (* 4 scalar-count))
    (doseq [idx (range scalar-count)
            :let [p (+ ptr (* idx 4))
                  v (nth values idx 0)]]
      (if integer?
        (vw/write-i32! m p (int (or v 0)))
        (vw/write-f32! m p (float (or v 0.0)))))
    (vswap! temps conj ptr)
    ptr))

(defn- alloc-component-array!
  [m temps component values count]
  (let [values (vec (or values []))
        elem-size (vw/sizeof component)
        elem-count (max 1 (long count))
        ptr (vw/malloc m (* elem-count elem-size))]
    (vw/zero! m ptr (* elem-count elem-size))
    (doseq [idx (range elem-count)]
      (when-let [value (get values idx)]
        (vw/write-component! m component (+ ptr (* idx elem-size)) value)))
    (vswap! temps conj ptr)
    ptr))

(defn- copy-native->wasm!
  [m ptr ^MemorySegment segment size]
  (vw/write-bytes! m ptr (.toArray segment ValueLayout/JAVA_BYTE) 0 size))

(defn- copy-wasm->native!
  [m ptr ^MemorySegment segment size]
  (MemorySegment/copy (MemorySegment/ofArray ^bytes (vw/read-bytes m ptr size))
                      0
                      segment
                      0
                      size))

(defn- alloc-native-pointer!
  [m temps after-copies ^MemorySegment segment]
  (let [size (long (.byteSize segment))
        ptr (vw/malloc m size)]
    (copy-native->wasm! m ptr segment size)
    (vswap! temps conj ptr)
    (vswap! after-copies conj #(copy-wasm->native! m ptr segment size))
    ptr))

(defn- wasm-pointer-arg!
  [m temps after-copies value]
  (cond
    (nil? value) 0
    (number? value) (long value)
    (instance? vybe.wasm.IVybeWasmPointer value) (vw/mem value)
    :else (let [native (vp/mem value)]
            (if (instance? MemorySegment native)
              (alloc-native-pointer! m temps after-copies native)
              (vw/mem value)))))

(defn- prepare-pointer-args!
  [m temps c-name call-args]
  (case c-name
    "SetShaderValue"
    (let [[shader loc value uniform-type] call-args]
      [shader loc (alloc-uniform-values! m temps uniform-type value 1) uniform-type])

    "SetShaderValueV"
    (let [[shader loc value uniform-type count] call-args]
      [shader loc (alloc-uniform-values! m temps uniform-type value count) uniform-type count])

    "VySetShaderValueMatrixV"
    (let [[shader loc matrices count] call-args]
      [shader loc (if (number? matrices)
                    matrices
                    (alloc-component-array! m temps (component "Matrix") matrices count))
       count])

    call-args))

(defn- trace?
  []
  (contains? #{"1" "true" "yes"}
             (some-> (or (System/getProperty "VYBE_RAYLIB_WASM_TRACE")
                         (System/getenv "VYBE_RAYLIB_WASM_TRACE"))
                     str/lower-case)))

(def ^:private trace-calls
  #{"InitWindow" "IsWindowReady" "BeginDrawing" "EndDrawing"
    "ClearBackground" "LoadRenderTexture"
    "BeginTextureMode" "EndTextureMode" "BeginShaderMode" "EndShaderMode"
    "DrawTextureRec" "DrawFPS"})

(defonce ^:private trace-counts* (atom {}))

(defn- trace-call!
  [c-name call-args]
  (when (and (trace?) (contains? trace-calls c-name))
    (let [n (get (swap! trace-counts* update c-name (fnil inc 0)) c-name)]
      (when (<= n 20)
        (binding [*out* *err*]
          (println :raylib-wasm/call c-name call-args))))))

(defn- marshal-arg!
  [m temps after-copies {:keys [ctype schema] :as _arg} value]
  (cond
    (aggregate? ctype)
    (cond
      (nil? value) 0
      (vw/mem? value) (vw/mem value)
      :else (let [ptr (alloc-component! m (component ctype) value)]
              (vswap! temps conj ptr)
              ptr))

    (and (string? value) (str/includes? (or ctype "") "*"))
    (let [ptr (vw/write-c-string! m value)]
      (vswap! temps conj ptr)
      ptr)

    (= schema :float)
    (Float/floatToRawIntBits (float value))

    (= schema :double)
    (Double/doubleToRawLongBits (double value))

    (= schema :boolean)
    (if value 1 0)

    (contains? #{:pointer :string} schema)
    (wasm-pointer-arg! m temps after-copies value)

    :else
    (or value 0)))

(defn- unmarshal-host-arg
  [memory-module {:keys [ctype schema]} raw]
  (cond
    (aggregate? ctype)
    (vw/read-component memory-module (component ctype) raw)

    (= schema :float)
    (vw/raw-i32->float raw)

    (= schema :double)
    (vw/raw-i64->double raw)

    (= schema :boolean)
    (not (zero? (long raw)))

    (and (str/includes? (or ctype "") "*")
         (contains? #{:pointer :string} schema))
    (or (vw/read-c-string memory-module raw) "")

    :else raw))

(defn- decode-result
  [m {:keys [ctype schema]} raw]
  (cond
    (aggregate? ctype)
    (vw/p->map raw (component ctype) {:module m})

    (= schema :void) 0
    (= schema :boolean) (not (zero? (long raw)))
    (= schema :float) (vw/raw-i32->float raw)
    (= schema :double) (vw/raw-i64->double raw)
    :else raw))

(defn invoke-raylib!
  [c-name call-args]
  (trace-call! c-name call-args)
  (if (= "WindowShouldClose" c-name)
    (raylib-wasm/should-close?)
    (let [m (module)
          {:keys [ret args]} (abi/function c-name)
          export (abi/wrapper-export c-name)
          temps (volatile! [])
          after-copies (volatile! [])
          aggregate-ret? (aggregate? (:ctype ret))]
      (try
        (let [out-ptr (when aggregate-ret?
                        (let [c (component (:ctype ret))]
                          (vw/malloc m (vw/sizeof c))))
              call-args (prepare-pointer-args! m temps c-name call-args)
              marshalled (mapv #(marshal-arg! m temps after-copies %1 %2)
                               args
                               call-args)
              raw (apply vw/call m export (cond-> []
                                            aggregate-ret? (conj out-ptr)
                                            true (into marshalled)))]
          (doseq [f @after-copies]
            (f))
          (when (= "EndDrawing" c-name)
            (raylib-wasm/poll-events!))
          (if aggregate-ret?
            (decode-result m ret out-ptr)
            (decode-result m ret raw)))
        (finally
          (doseq [ptr @temps]
            (vw/free m ptr)))))))

(defn invoke-raylib-host!
  [memory-module c-name raw-args]
  (let [{:keys [args]} (abi/function c-name)
        call-args (mapv #(unmarshal-host-arg memory-module %1 %2) args raw-args)]
    (invoke-raylib! c-name call-args)))

(def ^:private general-main-thread-calls
  #{"InitWindow" "CloseWindow" "SetConfigFlags" "SetWindowState"
    "ClearWindowState" "SetTargetFPS" "SetWindowPosition"
    "SetWindowSize" "ToggleFullscreen" "ToggleBorderlessWindowed"
    "MaximizeWindow" "MinimizeWindow" "RestoreWindow"})

(defn invoke-main-thread!
  [c-name args]
  (if (or (first-thread?)
          (str/starts-with? c-name "Is")
          (contains? #{"WindowShouldClose" "GetFrameTime" "GetTime"
                      "GetScreenWidth" "GetScreenHeight" "GetMousePosition"
                      "GetMouseDelta" "GetMouseX" "GetMouseY"}
                     c-name))
    (invoke-raylib! c-name args)
    (let [res (promise)]
      (-add-command #(invoke-raylib! c-name args)
                    {:prom res
                     :general (contains? general-main-thread-calls c-name)})
      @res)))

(defn- intern-method!
  [[c-name {:keys [args ret]}]]
  (let [clj-name (abi/clj-name c-name)
        arg-symbols (mapv (comp symbol str :symbol) args)
        v (intern 'vybe.raylib.c
                  clj-name
                  (fn [& call-args]
                    (invoke-main-thread! c-name call-args)))]
    (alter-meta! v merge
                 {:arglists (list arg-symbols)
                  :doc (format "Call Raylib wasm export `%s` through the LWJGL host." c-name)
                  :vybe/fn-meta {:fn-desc (function-desc c-name)
                                 :wasm-export (abi/wrapper-export c-name)
                                 :ret (select-keys ret [:ctype :schema])}
                  :vybe/raw-host-fn (fn [memory-module raw-args]
                                      (invoke-raylib-host!
                                       memory-module
                                       c-name
                                       raw-args))})))

(defn- draw-texture-shader-pass
  [target shader texture rect position tint clear]
  (invoke-main-thread! "VyDrawTextureShaderPass"
                       [target shader texture rect position tint clear]))

(defn- intern-custom-methods!
  []
  (let [v (intern 'vybe.raylib.c
                  'draw-texture-shader-pass
                  draw-texture-shader-pass)]
    (alter-meta! v merge
                 {:arglists '([target shader texture rect position tint clear])
                  :doc "Draw a texture through a shader into a render texture using Raylib wasm."})))

(def intern-methods
  (memoize
   (fn []
     (run! intern-method! (abi/public-functions))
     (intern-custom-methods!))))
