(ns vybe.raylib.browser
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [jsonista.core :as json]
   [vybe.raylib.abi :as abi]
   [vybe.wasm :as vw])
  (:import
   (java.net URI)
   (java.net.http HttpClient WebSocket WebSocket$Listener)
   (java.nio ByteBuffer)
   (java.nio.charset StandardCharsets)
   (java.time Duration)
   (java.util Base64)
   (java.util.concurrent CompletableFuture ConcurrentHashMap TimeUnit)))

(set! *warn-on-reflection* true)

(def ^:private http-port 8787)
(def ^:private debug-port 9227)
(def ^:private page-url
  (str "http://127.0.0.1:" http-port "/vybe/wasm/browser/raylib-host.html"))

(defonce ^:private state*
  (atom {:id 0
         :pending (ConcurrentHashMap.)}))

(defn- read-json-url
  [url]
  (json/read-value (slurp url) json/keyword-keys-object-mapper))

(defn- resource-file
  [path]
  (try
    (some-> path io/resource io/file)
    (catch Exception _
      nil)))

(defn- parent-chain
  [file]
  (take-while some? (iterate #(.getParentFile ^java.io.File %) file)))

(defn- launch-script
  []
  (let [starts (keep identity
                     [(resource-file "vybe/wasm/browser/raylib-host.html")
                      (resource-file "vybe/raylib/browser.clj")
                      (io/file (System/getProperty "user.dir"))])]
    (or (some (fn [dir]
                (let [script (io/file dir "bin/run-raylib-browser-window.sh")]
                  (when (.isFile script)
                    script)))
              (mapcat parent-chain starts))
        (throw (ex-info "Raylib browser launcher was not found"
                        {:user-dir (System/getProperty "user.dir")})))))

(defn- raylib-page
  []
  (try
    (some #(when (str/ends-with? (:url %) "raylib-host.html") %)
          (read-json-url (str "http://127.0.0.1:" debug-port "/json")))
    (catch java.io.IOException _
      nil)))

(defn- launch-window!
  []
  (let [^java.io.File script (launch-script)
        pb (ProcessBuilder. ^"[Ljava.lang.String;"
                            (into-array String [(.getAbsolutePath script)]))]
    (.directory pb (.getParentFile (.getParentFile script)))
    (.redirectErrorStream pb true)
    (.start pb))
  nil)

(defn- ws-listener
  [pending]
  (let [buf (StringBuilder.)]
    (reify WebSocket$Listener
      (onOpen [_ ws]
        (.request ws 1))
      (onText [_ ws data last?]
        (.append buf ^CharSequence data)
        (when last?
          (let [message (json/read-value (str buf) json/keyword-keys-object-mapper)]
            (.setLength buf 0)
            (when-let [id (:id message)]
              (when-let [^CompletableFuture fut (.remove ^ConcurrentHashMap pending (str id))]
                (.complete fut message)))))
        (.request ws 1)
        nil)
      (onBinary [_ ws _data _last?]
        (.request ws 1)
        nil)
      (onError [_ _ws err]
        (doseq [^CompletableFuture fut (.values ^ConcurrentHashMap pending)]
          (.completeExceptionally fut err))))))

(defn- connect!
  []
  (let [pending (:pending @state*)
        page (or (raylib-page)
                 (do
                   (launch-window!)
                   (loop [attempt 0]
                     (or (raylib-page)
                         (do
                           (when (> attempt 80)
                             (throw (ex-info "Raylib browser page did not start"
                                             {:url page-url})))
                           (Thread/sleep 100)
                           (recur (inc attempt)))))))
        client (-> (HttpClient/newBuilder)
                   (.connectTimeout (Duration/ofSeconds 5))
                   (.build))
        ws (-> client
               (.newWebSocketBuilder)
               (.connectTimeout (Duration/ofSeconds 5))
               (.buildAsync (URI/create (:webSocketDebuggerUrl page))
                            (ws-listener pending))
               (.get 5 TimeUnit/SECONDS))]
    (swap! state* assoc :ws ws)
    ws))

(defn- ws
  []
  (or (:ws @state*)
      (connect!)))

(defn- cdp!
  [method params]
  (let [{:keys [pending]} @state*
        _ (swap! state* update :id inc)
        id (:id @state*)
        fut (CompletableFuture.)
        payload (json/write-value-as-string {:id id
                                             :method method
                                             :params params})]
    (.put ^ConcurrentHashMap pending (str id) fut)
    (.get (.sendText ^WebSocket (ws) payload true) 5 TimeUnit/SECONDS)
    (let [message (.get fut 30 TimeUnit/SECONDS)]
      (when-let [error (:error message)]
        (throw (ex-info "Chrome DevTools call failed"
                        {:method method
                         :params params
                         :error error})))
      message)))

(defn eval-js!
  [expression]
  (let [message (cdp! "Runtime.evaluate"
                      {:expression expression
                       :returnByValue true
                       :awaitPromise true})
        result (get-in message [:result :result])]
    (when-let [exception (get-in message [:result :exceptionDetails])]
      (throw (ex-info "Browser Raylib evaluation failed"
                      {:expression expression
                       :exception exception})))
    (or (:value result)
        (:description result))))

(defn ensure!
  []
  (eval-js! "window.vybeRaylibBridgeReady === true")
  (loop [attempt 0]
    (if (= true (eval-js! "window.vybeRaylibBridgeReady === true"))
      true
      (do
        (when (> attempt 100)
          (throw (ex-info "Raylib browser bridge is not ready"
                          {:url page-url})))
        (Thread/sleep 100)
        (recur (inc attempt))))))

(defn- base-type
  [ctype]
  (let [aliases (:type-aliases (abi/abi))
        base (-> (or ctype "")
                 (str/replace #"\bconst\b" "")
                 (str/replace #"\bstruct\s+" "")
                 (str/replace #"\*" "")
                 str/trim
                 (str/split #"\s+")
                 last)]
    (loop [base base]
      (if-let [target (get aliases base)]
        (recur target)
        base))))

(defn- pointer-type?
  [ctype]
  (str/includes? (or ctype "") "*"))

(defn aggregate-type?
  [ctype]
  (and (not (pointer-type? ctype))
       (contains? (:layouts (abi/abi)) (keyword (base-type ctype)))))

(defn- serializable-layout
  [ctype]
  (select-keys (abi/layout-data (keyword (base-type ctype)))
               [:fields :size :align]))

(defn- map-value
  [v]
  (cond
    (nil? v) nil
    (map? v) (into {} v)
    (sequential? v) (vec v)
    :else (try
            (into {} v)
            (catch Exception _
              v))))

(defn- pointer-bytes
  [ptr byte-size]
  (let [bytes (vw/read-bytes (vw/default-module) (long ptr) (long byte-size))]
    (.encodeToString (Base64/getEncoder) bytes)))

(defn- scalar-base64
  [schema v]
  (let [buf (ByteBuffer/allocate (case schema
                                   :double 8
                                   :long 8
                                   :long-long 8
                                   4))
        _ (.order buf java.nio.ByteOrder/LITTLE_ENDIAN)]
    (case schema
      :double (.putDouble buf (double v))
      :float (.putFloat buf (float v))
      :long (.putLong buf (long v))
      :long-long (.putLong buf (long v))
      :uint (.putInt buf (unchecked-int (long v)))
      :int (.putInt buf (int v))
      (.putFloat buf (float v)))
    (.encodeToString (Base64/getEncoder) (.array buf))))

(defn- uniform-value-base64
  [uniform-type v]
  (let [m (map-value v)
        values (cond
                 (map? m) (mapv #(double (or (get m %) 0.0))
                                 [:x :y :z :w])
                 (sequential? m) (mapv double m)
                 :else [(double (or m 0.0))])
        t (long uniform-type)
        float? (not= t (long (abi/const-value "SHADER_UNIFORM_INT")))
        n (cond
            (= t (long (abi/const-value "SHADER_UNIFORM_VEC2"))) 2
            (= t (long (abi/const-value "SHADER_UNIFORM_VEC3"))) 3
            (= t (long (abi/const-value "SHADER_UNIFORM_VEC4"))) 4
            :else 1)
        buf (ByteBuffer/allocate (* n 4))
        _ (.order buf java.nio.ByteOrder/LITTLE_ENDIAN)]
    (doseq [idx (range n)]
      (let [value (nth values idx 0)]
        (if float?
          (.putFloat buf (float value))
          (.putInt buf (int value)))))
    (.encodeToString (Base64/getEncoder) (.array buf))))

(defn- uniform-pointer-size
  [uniform-type]
  (condp = (long uniform-type)
    (long (abi/const-value "SHADER_UNIFORM_FLOAT")) 4
    (long (abi/const-value "SHADER_UNIFORM_VEC2")) 8
    (long (abi/const-value "SHADER_UNIFORM_VEC3")) 12
    (long (abi/const-value "SHADER_UNIFORM_VEC4")) 16
    (long (abi/const-value "SHADER_UNIFORM_INT")) 4
    16))

(defn- pointer-arg
  [c-name args idx v]
  (cond
    (string? v)
    {:kind "string" :value v}

    (and (= c-name "SetShaderValue")
         (= idx 2)
         (or (number? v) (vw/component v) (sequential? v) (map? v)))
    {:kind "bytes"
     :base64 (uniform-value-base64 (nth args 3) v)}

    (and (= c-name "SetShaderValue")
         (= idx 2)
         (vw/component v))
    {:kind "aggregate"
     :layout (serializable-layout (name (vw/component v)))
     :value (map-value v)}

    (number? v)
    {:kind "number" :value (long v)}

    (vw/component v)
    {:kind "aggregate"
     :layout (serializable-layout (name (vw/component v)))
     :value (map-value v)}

    :else
    {:kind "number" :value (long (vw/mem v))}))

(defn- arg-spec
  [c-name all-args idx {:keys [ctype schema]} v]
  (cond
    (aggregate-type? ctype)
    {:kind "aggregate"
     :layout (serializable-layout ctype)
     :value (map-value v)}

    (= schema :pointer)
    (pointer-arg c-name all-args idx v)

    (= schema :boolean)
    {:kind "number" :value (if v 1 0)}

    :else
    {:kind "number" :value v}))

(defn call!
  [c-name call-args]
  (ensure!)
  (let [{:keys [ret args]} (abi/function-data c-name)
        ret-ctype (:ctype ret)
        spec {:name c-name
              :ret (cond-> {:schema (:schema ret)
                            :ctype ret-ctype}
                     (aggregate-type? ret-ctype)
                     (assoc :layout (serializable-layout ret-ctype)))
              :args (mapv (fn [idx arg v]
                            (arg-spec c-name call-args idx arg v))
                          (range)
                          args
                          call-args)}
        result (eval-js! (str "window.vybeRaylibBridge.call("
                              (json/write-value-as-string spec)
                              ")"))]
    (if (aggregate-type? ret-ctype)
      result
      (case (:schema ret)
        :void nil
        :boolean (not (zero? (long result)))
        :uint (Integer/toUnsignedLong (int result))
        result))))

(defn load-model-from-bytes!
  [bytes]
  (ensure!)
  (let [spec {:name "VyLoadModelFromMemory"
              :ret {:schema :long
                    :ctype "Model"
                    :layout (serializable-layout "Model")}
              :args [{:kind "bytes"
                      :base64 (.encodeToString (Base64/getEncoder) bytes)}
	                     {:kind "number"
	                      :value (alength ^bytes bytes)}]}
	        result (eval-js! (str "window.vybeRaylibBridge.call("
	                              (json/write-value-as-string spec)
	                              ")"))
	        read-array (fn [layout-name ptr count]
	                     (if (and (pos? (long ptr)) (pos? (long count)))
	                       (eval-js!
	                        (str "window.vybeRaylibBridge.readArray("
	                             (json/write-value-as-string
	                              (serializable-layout layout-name))
	                             ","
	                             (long ptr)
	                             ","
	                             (long count)
	                             ")"))
	                       []))
	        read-i32-array (fn [ptr count]
	                         (if (and (pos? (long ptr)) (pos? (long count)))
	                           (eval-js!
	                            (str "window.vybeRaylibBridge.readI32Array("
	                                 (long ptr)
	                                 ","
	                                 (long count)
	                                 ")"))
	                           []))
	        mesh-count (long (or (:meshCount result) 0))
	        material-count (long (or (:materialCount result) 0))]
	    (assoc result
	           :vybe.raylib.browser/meshes
	           (read-array "Mesh" (:meshes result) mesh-count)
	           :vybe.raylib.browser/materials
	           (read-array "Material" (:materials result) material-count)
	           :vybe.raylib.browser/mesh-materials
	           (read-i32-array (:meshMaterial result) mesh-count))))
