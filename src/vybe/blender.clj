(ns vybe.blender
  "Requires the JVM to be embedded into Blender."
  (:require
   [clojure.edn :as edn]
   [nrepl.core :as nrepl]
   [clojure.string :as str]
   [vybe.panama :as vp]
   [clojure.walk :as walk]
   [vybe.basilisp.blender :as-alias vbb]
   [vybe.blender.impl :refer [*basilisp-eval]]
   [vybe.raylib.c :as vr.c]
   [vybe.flecs :as vf]
   [vybe.math :as vm]
   [vybe.type :as vt]))

(defonce ^:no-doc ^:dynamic *nrepl-init* nil)
#_ (def blender-session (connect 7889))
#_ (*nrepl-init* blender-session)

(defonce *nrepl-session (atom nil))

(defn connect
  "Connect to a running basilisp Blender nREPL server.

  Returns a blender-session map containing connection and the client (you can use it
  with other functions from this namespace)."
  [port]
  (let [conn (nrepl/connect :port port)
        client (nrepl/client conn Long/MAX_VALUE)
        blender-session {:conn conn
                         :client client}]
    (reset! *nrepl-session blender-session)
    blender-session))
#_ (def blender-session (connect 7889))

(defn disconnect
  [{:keys [conn]}]
  (.close conn))
#_ (disconnect blender-session)

(defn message
  [params {:keys [client]}]
  (-> client
      (nrepl/message params)))
#_ (-> {:ns "vybe.basilisp.blender"
        :op "complete"
        :prefix "edn/"}
       (message blender-session))

(defn nrepl-eval-str
  "Eval from the basilisp Blender nREPL session.
  It will start a blender session on port 7889 if
  not started already."
  ([code-str]
   (when (nil? @*nrepl-session)
     (connect 7889))
   (nrepl-eval-str @*nrepl-session code-str))
  ([blender-session code-str]
   (nrepl-eval-str blender-session {} code-str))
  ([{:keys [client]} params code-str]
   (let [res (-> client
                 (nrepl/message (merge {:op "eval"
                                        :code code-str}
                                       params))
                 doall)
         {:keys [value err ex]} (-> res
                                    nrepl/combine-responses)
         v (first value)]
     (if (not (or err ex))
       {:value (cond
                 (str/starts-with? v "<module")
                 v

                 (str/starts-with? v "bpy.")
                 (-> v symbol pr-str)

                 :else
                 (try
                   (-> v edn/read-string)
                   (catch Exception _
                     v)))}
       (do
         (println ex)
         (println err)
         (throw (ex-info "Error when evaluating basilisp code in Blender"
                         {:error {:via {:type ex
                                        :message err}}})))))))
#_ (nrepl-eval-str (pr-str '(+ 2 3)))
#_ (nrepl-eval-str (format "(%s %s)"
                     (pr-str '(fn [x] (+ x 34)))
                     10))
#_ (->> (pr-str '#'map)
        (nrepl-eval-str blender-session))
#_ (->> (pr-str '(+ 2 12))
        (nrepl-eval-str blender-session {:ns "user"
                                   :line 1
                                   :column 1
                                   :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/blender.lpy"
                                   :context :expr}))
#_ (->> (pr-str '#'edn/read)
        (nrepl-eval-str blender-session {:ns "user"
                                   :line 1
                                   :column 1
                                   :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/blender.lpy"
                                   :context :expr}))
#_ (->> (pr-str '[bpy.data/objects])
        (nrepl-eval-str blender-session {:ns "vybe.basilisp.blender"}))
#_ (nrepl-eval-str 'bpy.ops/objects)

(defn blender-eval-str
  "Evaluate string form if you are connected from Blender."
  [code-str]
  #_(println :CODE_STR code-str)
  (let [*out (atom nil)
        res (@*basilisp-eval code-str (fn [v]
                                        (reset! *out v)))
        out-v @*out]
    (when res
      (println :blender-error res)
      (throw (ex-info "Error from Blender"
                      {:res res})))

    (try
      (cond
        (and (string? out-v)
             (str/starts-with? out-v "[:vybe-expection"))
        (throw (ex-info "Exception from Blender"
                        {:message (last (edn/read-string out-v))}))

        (instance? Object out-v)
        out-v

        :else
        (edn/read-string (pr-str out-v)))
      (catch Exception e
        (when-not (str/starts-with? (pr-str out-v) "#'")
          (println :blender-parsing-error {:res out-v
                                           :e e})
          (throw e))
        out-v))))
#_ (blender-eval-str (pr-str '(+ 4 1)))
;; => 5

#_ ((blender-eval-str (pr-str '(vybe.basilisp.jvm/make-fn
                                (fn my-fn
                                  [x]
                                  (+ x 6)))))
    10)
;; => 16

(defn- make-eval-str
  "Will return a function that call blender-eval-str if we are running inside
  Blender, oterwise we run the nREPL one."
  []
  (if @*basilisp-eval
    blender-eval-str
    #(:value (nrepl-eval-str %))))

(defmacro defn*
  "We will run the function in basilisp context.

  We will use nREPL if we are not running inside Blender."
  {:clj-kondo/lint-as 'schema.core/defn
   :clj-kondo/ignore [:aliased-namespace-var-usage]
   :arglists '([name doc-string? [params*] body])}
  [n & fn-tail]
  ;; First, unalias any aliased symbol.
  (let [[doc-string args fn-tail] (if (string? (first fn-tail))
                                    [(first fn-tail) (second fn-tail) (drop 2 fn-tail)]
                                    [nil (first fn-tail) (drop 1 fn-tail)])
        fn-tail (let [aliases (-> (ns-aliases *ns*)
                                  (update-vals (comp symbol str)))]
                  (walk/postwalk (fn [v]
                                   (let [v (if-let [ns-orig (and (symbol? v)
                                                                 (get aliases (some-> v namespace symbol)))]
                                             (symbol (str ns-orig) (name v))
                                             v)]
                                     (if (and (symbol? v)
                                              (= (namespace v) "clojure.core"))
                                       (symbol "basilisp.core" (name v))
                                       v)))
                                 fn-tail))]
    (if @*basilisp-eval
      `(let [f# (blender-eval-str ~(pr-str `(do
                                              (~'import ~'bpy)
                                              (vybe.basilisp.jvm/make-fn
                                               (~'let [~'f (~'fn ~n
                                                            ~args
                                                            (~'try
                                                             ~@fn-tail
                                                             (~'catch ~'Exception ~'e
                                                              (~'str [:vybe-exception ~'e #_(~'str ~'e)]))))]
                                                #_(~'intern 'vybe.basilisp.blender (quote ~n) ~'f)
                                                ~'f)))))]
         (defn ~(vary-meta n merge
                           {:doc doc-string}
                           (meta &form))
           [~@args]
           (let [res# (f# ~@args)]
             (when (and (string? res#)
                        (str/starts-with? res# "[:vybe-exception"))
               (throw (ex-info "Exception from Blender"
                               {:error (last (edn/read-string res#))})) )
             res#)))
      ;; nREPL
      `(let [*keeper# (atom nil)
             *eval-str# (atom nil)]
         (defn ~n
           ~args

           (when-not @*keeper#
             (reset! *eval-str# (make-eval-str))
             (reset! *keeper# (@*eval-str# ~(pr-str `(~'defn ~n ~args ~@fn-tail)))))

           (-> (if ~(boolean (seq args))
                 (apply format "(%s %s)"
                        (quote ~n)
                        (mapv pr-str ~args))
                 (format "(%s)" (quote ~n)))
               (@*eval-str#)))))))
#_ (defn* my-fn-1
     [x]
     (+ x 6))
#_ (time
    (doseq [_ (range 5000)]
      (my-fn-1 1)))

#_ ((defn* my-fn-2
      []
      (str (vbb/obj-find "Scene"))))

(defmacro defn*async
  "Like `defn*`, but will register the function so it can run
  in the Blender thread."
  {:clj-kondo/lint-as 'schema.core/defn
   :clj-kondo/ignore [:aliased-namespace-var-usage]
   :arglists '([name doc-string? [params*] body])}
  [n & fn-tail]
  (let [[doc-string args fn-tail] (if (string? (first fn-tail))
                                    [(first fn-tail) (second fn-tail) (drop 2 fn-tail)]
                                    [nil (first fn-tail) (drop 1 fn-tail)])]
    `(defn* ~n ~(or doc-string "") ~args
       (bpy.app.timers/register
        (fn []
          ~@fn-tail

          nil)))))

(def blender-object-comp
  (memoize
   (fn []
     (when (Class/forName "org.vybe.blender.Object")
       (eval '(do
                (vybe.panama/defcomp BlenderObject
                  (org.vybe.blender.Object/layout))
                BlenderObject))))))

(defn* obj-raw-pointer
  "Get Blender object raw pointer.

  Returns a long."
  [obj]
  (some-> (vbb/obj-find obj) .as_pointer))
#_ (obj-raw-pointer "Cube.001")

(defn obj-pointer
  "Get Blender object VybePMap."
  [obj]
  (when-let [pointer (obj-raw-pointer obj)]
    (-> pointer
        (vp/address->mem)
        (vp/reinterpret (.byteSize (.layout (blender-object-comp))))
        (vp/p->map (blender-object-comp)))))
#_ (:loc (obj-pointer "Cube.001"))

(defn*async toggle-original-objs
  []
  (vbb/toggle-original-objs))
#_ (toggle-original-objs)

(defn*async bake-obj
  "Bake obj + their children."
  [obj]
  (let [is-visible (vbb/original-visible?)]
    (when is-visible (vbb/toggle-original-objs))

    (-> (vbb/obj+children obj) (vbb/bake-objs))

    (when is-visible (vbb/toggle-original-objs))))
#_ (do (bake-obj "Scene")
       (bake-obj "SceneOutdoors"))

(defn get-blender-name
  [flecs-ent]
  (some-> (vf/get-internal-name flecs-ent)
          vf/-flecs->vybe
          name))

(defn- -get-blender-trs
  ([blender-name]
   (-get-blender-trs blender-name {}))
  ([blender-name {:keys [use-original]
                  :or {use-original true}}]
   (when-let [pointer (or (and use-original
                               (obj-pointer (str blender-name ".__original")))
                          (obj-pointer blender-name))]
     #_(println :POINTER)
     (let [{:keys [loc scale quat]} pointer
           [x z y] loc
           [x y z] [x y (- z)]
           rotation (let [[w x z y] quat]
                      (vt/Rotation [x y (- z) w]))]
       {:translation (vt/Translation [x y z])
        :rotation rotation
        :scale (vt/Scale [(first scale)
                          (last scale)
                          (second scale)])}))))

(defn entity-trs
  "Get translation, rotation and scale from Blender for one VybeFlecsEntity."
  [flecs-ent]
  (let [blender-name (get-blender-name flecs-ent)
        parent (vf/parent flecs-ent)
        parent-matrix (get parent [vt/Transform :initial])
        {:keys [translation rotation scale]} (-get-blender-trs blender-name)

        matrix (cond-> (vm/matrix-transform translation rotation scale)
                 parent-matrix
                 (vr.c/matrix-multiply (vr.c/matrix-invert parent-matrix)))

        t (vt/Translation)
        r (vt/Rotation)
        s (vt/Scale)]
    (vr.c/matrix-decompose matrix t r s)
    {:translation t
     :rotation r
     :scale s}))

(defn entity-sync!
  "Sync transform from Blender for one entity."
  [flecs-ent]
  (let [entities (loop [acc [flecs-ent]
                        p (vf/parent flecs-ent)]
                   (if p
                     (recur (cons p acc) (vf/parent p))
                     (vec acc)))]
    (doseq [flecs-ent entities]
      (let [ent-translation (-> flecs-ent (get vt/Translation))
            ent-scale (-> flecs-ent (get vt/Scale))
            ent-rotation (-> flecs-ent (get vt/Rotation))
            {:keys [translation scale rotation]} (entity-trs flecs-ent)]
        (merge ent-rotation rotation)
        (merge ent-scale scale)
        (merge ent-translation translation)))))
