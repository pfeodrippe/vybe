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
   (java.time Duration)
   (java.util ArrayList Base64 Collection)
   (java.util.concurrent CompletableFuture ConcurrentHashMap TimeUnit)))

(set! *warn-on-reflection* true)

(def ^:private http-port 8787)
(def ^:private debug-port 9227)
(def ^:private page-url
  (str "http://127.0.0.1:" http-port "/vybe/wasm/browser/raylib-host.html"))

(defonce ^:private state*
  (atom {:id 0
         :pending (ConcurrentHashMap.)}))

(defonce ^:private ready?*
  (volatile! false))

(defonce ^:private batch*
  (volatile! nil))

(def ^:private flush-before-call
  #{"rlGetMatrixModelview"
    "rlGetMatrixProjection"
    "rlReadTexturePixels"})

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
  (locking state*
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
        message))))

(declare ensure! install-functions! install-layouts! return-spec)

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

(defn input-state!
  []
  (ensure!)
  (eval-js! "window.vybeRaylibBridge.inputState()"))

(defn set-mouse-position!
  [x y]
  (ensure!)
  (eval-js! (str "window.vybeRaylibBridge.setMousePosition("
                 (long x)
                 ","
                 (long y)
                 ")")))

(defn- execute-call!
  [spec]
  (eval-js! (str "window.vybeRaylibBridge.call("
                 (json/write-value-as-string spec)
                 ")")))

(defn- begin-batch! []
  (vreset! batch* (ArrayList. 512))
  nil)

(defn- batch-active? []
  (some? @batch*))

(defn- enqueue-batch! [spec]
  (let [batch @batch*]
    (if (instance? java.util.List batch)
      (.add ^java.util.List batch spec)
      (vreset! batch* (conj (or batch []) spec))))
  nil)

(defn- take-batch! []
  (let [batch @batch*]
    (vreset! batch* nil)
    batch))

(defn- flush-batch! []
  (when-let [specs (take-batch!)]
    (when (if (instance? Collection specs)
            (pos? (.size ^Collection specs))
            (seq specs))
      (eval-js! (str "window.vybeRaylibBridge.callBatch("
                     (json/write-value-as-string specs)
                     ")")))))

(defn begin-frame!
  []
  (ensure!)
  (when-not (batch-active?)
    (begin-batch!))
  nil)

(defn end-frame!
  []
  (when (batch-active?)
    (flush-batch!))
  nil)

(defn ensure!
  []
  (or @ready?*
      (locking state*
        (or @ready?*
            (and (:ready? @state*)
                 (:layouts-installed? @state*)
                 (:functions-installed? @state*)
                 (do
                   (vreset! ready?* true)
                   true))
            (do
              (loop [attempt 0]
                (if (= true (eval-js! "window.vybeRaylibBridgeReady === true"))
                  true
                  (do
                    (when (> attempt 100)
                      (throw (ex-info "Raylib browser bridge is not ready"
                                      {:url page-url})))
                      (Thread/sleep 100)
                      (recur (inc attempt)))))
              (install-layouts!)
              (install-functions!)
              (swap! state* assoc :ready? true)
              (vreset! ready?* true)
              true)))))

(def ^:private component-layout-aliases
  {"C_vybe_DOT_type_SLASH_Matrix" "Matrix"
   "C_vybe_DOT_type_SLASH_Rotation" "Vector4"
   "C_vybe_DOT_type_SLASH_Scale" "Vector3"
   "C_vybe_DOT_type_SLASH_Transform" "Matrix"
   "C_vybe_DOT_type_SLASH_Translation" "Vector3"
   "C_vybe_DOT_type_SLASH_Vector2" "Vector2"
   "C_vybe_DOT_type_SLASH_Vector3" "Vector3"
   "C_vybe_DOT_type_SLASH_Vector4" "Vector4"
   "C_vybe_DOT_type_SLASH_Velocity" "Vector3"})

(defn- base-type
  [ctype]
  (let [aliases (merge component-layout-aliases (:type-aliases (abi/abi)))
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

(defn- layout-name
  [ctype]
  (base-type ctype))

(defn- field-path-prefix?
  [parent child]
  (and (< (count parent) (count child))
       (= (seq parent) (take (count parent) child))))

(defonce ^:private leaf-fields-cache*
  (atom {}))

(defonce ^:private aggregate-value-cache*
  (atom {}))

(defonce ^:private aggregate-encoder-cache*
  (atom {}))

(defonce ^:private function-plan-cache*
  (atom {}))

(defonce ^:private layout-ids*
  (delay
    (into {}
          (map-indexed (fn [idx layout-name]
                         [layout-name (inc idx)]))
          (sort (map name (keys (:layouts (abi/abi))))))))

(defonce ^:private function-ids*
  (delay
    (into {}
          (map-indexed (fn [idx c-name]
                         [c-name (inc idx)]))
          (sort (keys (:functions (abi/abi)))))))

(defn- layout-id
  [ctype]
  (let [layout-name (layout-name ctype)]
    (or (get @layout-ids* layout-name)
        (throw (ex-info "Missing Raylib ABI layout id"
                        {:layout layout-name
                         :ctype ctype})))))

(defn- leaf-fields
  [ctype]
  (let [layout-name (layout-name ctype)]
    (or (get @leaf-fields-cache* layout-name)
        (let [fields (:fields (serializable-layout layout-name))
              leaves (vec (remove (fn [{:keys [path]}]
                                    (some #(field-path-prefix? path (:path %)) fields))
                                  fields))]
          (swap! leaf-fields-cache* assoc layout-name leaves)
          leaves))))

(defn- browser-layouts
  []
  (mapv (fn [[layout-name id]]
          {:id id
           :name layout-name
           :layout (serializable-layout layout-name)})
        @layout-ids*))

(defn- install-layouts!
  []
  (when-not (:layouts-installed? @state*)
    (locking state*
      (when-not (:layouts-installed? @state*)
        (eval-js! (str "window.vybeRaylibBridge.installLayouts("
                       (json/write-value-as-string (browser-layouts))
                       ")"))
        (swap! state* assoc :layouts-installed? true)))))

(defn- browser-functions
  []
  (mapv (fn [[c-name id]]
          {:id id
           :name c-name
           :ret (return-spec (:ret (abi/function-data c-name)))})
        @function-ids*))

(defn- install-functions!
  []
  (when-not (:functions-installed? @state*)
    (locking state*
      (when-not (:functions-installed? @state*)
        (eval-js! (str "window.vybeRaylibBridge.installFunctions("
                       (json/write-value-as-string (browser-functions))
                       ")"))
        (swap! state* assoc :functions-installed? true)))))

(defn- return-spec
  [{:keys [schema ctype]}]
  (cond
    (aggregate-type? ctype) ["a" (layout-id ctype)]
    (= schema :void) ["v"]
    (= schema :boolean) ["b"]
    (= schema :string) ["s"]
    :else ["n"]))

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

(defn- aggregate-cache-key
  [layout-name v]
  (case layout-name
    "RenderTexture" [:RenderTexture
                     (:id v)
                     (get-in v [:texture :id])
                     (get-in v [:depth :id])
                     (get-in v [:texture :width])
                     (get-in v [:texture :height])]
    "Texture" [:Texture (:id v) (:width v) (:height v) (:mipmaps v) (:format v)]
    "Shader" [:Shader (:id v) (:locs v)]
    "Color" [:Color (:r v) (:g v) (:b v) (:a v)]
    "Rectangle" [:Rectangle (:x v) (:y v) (:width v) (:height v)]
    "Vector2" [:Vector2 (:x v) (:y v)]
    "Vector3" [:Vector3 (:x v) (:y v) (:z v)]
    nil))

(defn- aggregate-value*
  [ctype v]
  (let [layout-name (layout-name ctype)
        encoder (or (get @aggregate-encoder-cache* layout-name)
                    (let [getters (mapv (fn [{:keys [path]}]
                                           (let [path (vec path)]
                                             (case (count path)
                                               1 (let [k0 (path 0)]
                                                   #(or (get % k0) 0))
                                               2 (let [k0 (path 0)
                                                       k1 (path 1)]
                                                   #(or (get (get % k0) k1) 0))
                                               3 (let [k0 (path 0)
                                                       k1 (path 1)
                                                       k2 (path 2)]
                                                   #(or (get (get (get % k0) k1) k2) 0))
                                               #(or (get-in % path) 0))))
                                         (leaf-fields layout-name))
                          encoder (fn [value]
                                    (mapv #(% value) getters))]
                      (swap! aggregate-encoder-cache* assoc layout-name encoder)
                      encoder))]
    (encoder v)))

(defn- aggregate-value
  [ctype v]
  (let [layout-name (layout-name ctype)
        v (if (or (nil? v) (map? v) (sequential? v))
            v
            (try
              (into {} v)
              (catch Exception _
                v)))]
    (if (and (sequential? v) (not (map? v)))
      (vec v)
      (if-let [cache-key (aggregate-cache-key layout-name v)]
        (or (get @aggregate-value-cache* cache-key)
            (let [value (aggregate-value* layout-name v)]
              (swap! aggregate-value-cache* assoc cache-key value)
              value))
        (aggregate-value* layout-name v)))))

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

(defn- uniform-values
  [uniform-type v]
  (let [m (map-value v)
        values (cond
                 (map? m) (mapv #(double (or (get m %) 0.0))
                                 [:x :y :z :w])
                 (sequential? m) (mapv double m)
                 :else [(double (or m 0.0))])
        t (long uniform-type)
        n (cond
            (or (= t (long (abi/const-value "SHADER_UNIFORM_VEC2")))
                (= t (long (abi/const-value "SHADER_UNIFORM_IVEC2")))) 2
            (or (= t (long (abi/const-value "SHADER_UNIFORM_VEC3")))
                (= t (long (abi/const-value "SHADER_UNIFORM_IVEC3")))) 3
            (or (= t (long (abi/const-value "SHADER_UNIFORM_VEC4")))
                (= t (long (abi/const-value "SHADER_UNIFORM_IVEC4")))) 4
            :else 1)]
    (mapv #(nth values % 0.0) (range n))))

(defn- uniform-array-values
  [uniform-type values]
  (let [values (if (sequential? values) values [values])]
    (->> values
         (mapcat #(uniform-values uniform-type %))
         vec)))

(defn- aggregate-array-value
  [ctype values]
  (mapv #(aggregate-value ctype %) values))

(defn- uniform-pointer-size
  [uniform-type]
  (condp = (long uniform-type)
    (long (abi/const-value "SHADER_UNIFORM_FLOAT")) 4
    (long (abi/const-value "SHADER_UNIFORM_VEC2")) 8
    (long (abi/const-value "SHADER_UNIFORM_VEC3")) 12
    (long (abi/const-value "SHADER_UNIFORM_VEC4")) 16
    (long (abi/const-value "SHADER_UNIFORM_INT")) 4
    16))

(defn- fast-spec
  [c-name call-args]
  (case c-name
    "BeginTextureMode"
    (let [[target] call-args]
      ["btm" (aggregate-value "RenderTexture" target)])

    "ClearBackground"
    (let [[color] call-args]
      ["cb"
       (long (or (:r color) 0))
       (long (or (:g color) 0))
       (long (or (:b color) 0))
       (long (or (:a color) 0))])

    "DrawMesh"
    (let [[mesh material transform] call-args]
      (when-let [mesh-ptr (:vybe.raylib.browser/ptr (meta mesh))]
        ["dm"
         (long mesh-ptr)
         (aggregate-value "Material" material)
         (aggregate-value "Matrix" transform)]))

    "DrawTextureRec"
    (let [[texture source position tint] call-args]
      ["dtr"
       (long (or (:id texture) 0))
       (long (or (:width texture) 0))
       (long (or (:height texture) 0))
       (long (or (:mipmaps texture) 0))
       (long (or (:format texture) 0))
       (double (or (:x source) 0.0))
       (double (or (:y source) 0.0))
       (double (or (:width source) 0.0))
       (double (or (:height source) 0.0))
       (double (or (:x position) 0.0))
       (double (or (:y position) 0.0))
       (long (or (:r tint) 0))
       (long (or (:g tint) 0))
       (long (or (:b tint) 0))
       (long (or (:a tint) 0))])

	    "DrawTexturePro"
	    (let [[texture source dest origin rotation tint] call-args]
	      ["dtp"
	       (long (or (:id texture) 0))
	       (long (or (:width texture) 0))
	       (long (or (:height texture) 0))
	       (long (or (:mipmaps texture) 0))
	       (long (or (:format texture) 0))
	       (double (or (:x source) 0.0))
	       (double (or (:y source) 0.0))
	       (double (or (:width source) 0.0))
	       (double (or (:height source) 0.0))
	       (double (or (:x dest) 0.0))
	       (double (or (:y dest) 0.0))
	       (double (or (:width dest) 0.0))
	       (double (or (:height dest) 0.0))
	       (double (or (:x origin) 0.0))
	       (double (or (:y origin) 0.0))
	       (double rotation)
	       (long (or (:r tint) 0))
	       (long (or (:g tint) 0))
	       (long (or (:b tint) 0))
	       (long (or (:a tint) 0))])

	    "DrawRectanglePro"
	    (let [[rec origin rotation color] call-args]
	      ["drp"
	       (double (or (:x rec) 0.0))
	       (double (or (:y rec) 0.0))
	       (double (or (:width rec) 0.0))
	       (double (or (:height rec) 0.0))
	       (double (or (:x origin) 0.0))
	       (double (or (:y origin) 0.0))
	       (double rotation)
	       (long (or (:r color) 0))
	       (long (or (:g color) 0))
	       (long (or (:b color) 0))
	       (long (or (:a color) 0))])

	    "DrawCircleLines"
	    (let [[center-x center-y radius color] call-args]
	      ["dcl"
	       (long center-x)
	       (long center-y)
	       (double radius)
	       (long (or (:r color) 0))
	       (long (or (:g color) 0))
	       (long (or (:b color) 0))
	       (long (or (:a color) 0))])

	    "DrawCircle"
	    (let [[center-x center-y radius color] call-args]
	      ["dc"
	       (long center-x)
	       (long center-y)
	       (double radius)
	       (long (or (:r color) 0))
	       (long (or (:g color) 0))
	       (long (or (:b color) 0))
	       (long (or (:a color) 0))])

	    "GuiGroupBox"
	    (let [[bounds text] call-args]
	      ["ggb"
	       (double (or (:x bounds) 0.0))
	       (double (or (:y bounds) 0.0))
	       (double (or (:width bounds) 0.0))
	       (double (or (:height bounds) 0.0))
	       (str text)])

	    "GuiDummyRec"
	    (let [[bounds text] call-args]
	      ["gdr"
	       (double (or (:x bounds) 0.0))
	       (double (or (:y bounds) 0.0))
	       (double (or (:width bounds) 0.0))
	       (double (or (:height bounds) 0.0))
	       (str text)])

	    "SetShaderValueMatrix"
	    (let [[shader location matrix] call-args]
	      ["svm"
       (aggregate-value "Shader" shader)
       (long location)
       (aggregate-value "Matrix" matrix)])

    nil))

(defn- pointer-arg
  [c-name args idx v]
  (cond
    (string? v)
    ["s" v]

    (and (= c-name "SetShaderValue")
         (= idx 2)
         (or (number? v) (vw/component v) (sequential? v) (map? v)))
    ["u" (long (nth args 3)) (uniform-values (nth args 3) v)]

    (and (= c-name "SetShaderValueV")
         (= idx 2)
         (or (number? v) (vw/component v) (sequential? v) (map? v)))
    ["u" (long (nth args 3)) (uniform-array-values (nth args 3) v)]

    (and (= c-name "VySetShaderValueMatrixV")
         (= idx 2)
         (sequential? v))
    ["aa" (layout-id "Matrix") (aggregate-array-value "Matrix" v)]

    (and (= c-name "SetShaderValue")
         (= idx 2)
         (vw/component v))
    (let [ctype (name (vw/component v))]
      ["a" (layout-id ctype) (aggregate-value ctype v)])

    (number? v)
    ["n" (long v)]

    (vw/component v)
    (let [ctype (name (vw/component v))]
      ["a" (layout-id ctype) (aggregate-value ctype v)])

    :else
    ["n" (long (vw/mem v))]))

(defn- arg-encoder
  [c-name idx {:keys [ctype schema]}]
  (cond
    (aggregate-type? ctype)
    (let [layout-name (layout-name ctype)]
      (fn [_all-args v]
        (if-let [browser-ptr (:vybe.raylib.browser/ptr (meta v))]
          ["n" (long browser-ptr)]
          ["a" (layout-id layout-name) (aggregate-value layout-name v)])))

    (= schema :pointer)
    (fn [all-args v]
      (if-let [browser-ptr (:vybe.raylib.browser/ptr (meta v))]
        ["n" (long browser-ptr)]
        (pointer-arg c-name all-args idx v)))

    (= schema :boolean)
    (fn [_all-args v]
      (if-let [browser-ptr (:vybe.raylib.browser/ptr (meta v))]
        ["n" (long browser-ptr)]
        ["n" (if v 1 0)]))

    :else
    (fn [_all-args v]
      (if-let [browser-ptr (:vybe.raylib.browser/ptr (meta v))]
        ["n" (long browser-ptr)]
        ["n" v]))))

(defn- function-plan
  [c-name]
  (or (get @function-plan-cache* c-name)
      (let [{:keys [ret args]} (abi/function-data c-name)
            ret-ctype (:ctype ret)
            ret-schema (:schema ret)
            encoders (mapv (fn [idx arg]
                              (arg-encoder c-name idx arg))
                            (range)
                            args)
            ret-spec (return-spec ret)
            plan {:ret ret
                  :ret-ctype ret-ctype
                  :ret-schema ret-schema
                  :ret-aggregate? (aggregate-type? ret-ctype)
                  :ret-spec ret-spec
                  :function-id (get @function-ids* c-name)
                  :arg-encoders encoders
                  :zero-arg-spec (when (empty? encoders)
                                   [(get @function-ids* c-name) []])}]
        (swap! function-plan-cache* assoc c-name plan)
        plan)))

(defn call!
  [c-name call-args]
  (ensure!)
  (let [{:keys [ret-schema ret-aggregate? arg-encoders
                function-id zero-arg-spec]} (function-plan c-name)
        spec (or (fast-spec c-name call-args)
                 zero-arg-spec
                 [function-id
                  (mapv (fn [encoder v]
                          (encoder call-args v))
                        arg-encoders
                        call-args)])
        result (cond
                 (= c-name "BeginDrawing")
                 (do
                   (when-not (batch-active?)
                     (begin-batch!))
                   (enqueue-batch! spec)
                   nil)

                 (and (batch-active?) (= c-name "EndDrawing"))
                 (do
                   (enqueue-batch! spec)
                   (flush-batch!)
                   nil)

                 (and (batch-active?) (= ret-schema :void))
                 (do
                   (enqueue-batch! spec)
                   nil)

                 (and (batch-active?) (contains? flush-before-call c-name))
                 (do
                   (flush-batch!)
                   (let [result (execute-call! spec)]
                     (begin-batch!)
                     result))

                 :else
                 (execute-call! spec))]
    (if ret-aggregate?
      result
      (case ret-schema
        :void nil
        :boolean (not (zero? (long result)))
        :uint (Integer/toUnsignedLong (int result))
        result))))

(defn load-model-from-bytes!
  [bytes]
  (ensure!)
  (let [spec ["VyLoadModelFromMemory"
              ["a" (layout-name "Model")]
              [["b" (.encodeToString (Base64/getEncoder) bytes)]
               ["n" (alength ^bytes bytes)]]]
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
