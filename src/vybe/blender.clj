(ns vybe.blender
  (:require
   [clojure.edn :as edn]
   [nrepl.core :as nrepl]
   [clojure.string :as str]
   [vybe.panama :as vp]))

(def ^:no-doc ^:dynamic *nrepl-init* nil)
#_ (def blender-session (connect 7889))
#_ (*nrepl-init* blender-session)

(defonce *nrepl-session (atom nil))

;; This will contain the eval function from basilisp when
;; we start the REPL from Blender.
;; See `vybe/basilisp/blender.lpy`.
(defonce *basilisp-eval (atom nil))

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
  (let [res (@*basilisp-eval code-str)]
    (try
      (edn/read-string res)
      (catch Exception e
        (when-not (str/starts-with? res "#'")
          (println :blender-parsing-error {:res res
                                           :e e})
          (throw e))
        res))))
#_ (blender-eval-str (pr-str '(+ 4 1)))

(defn eval-str
  "Will call blender-eval-str if we are running inside Blender."
  [code-str]
  (if @*basilisp-eval
    (blender-eval-str code-str)
    (:value (nrepl-eval-str   code-str))))

(defmacro defn*
  "We will run the function in basilisp context.

  We will use nREPL if we are not running inside Blender."
  [n args & fn-tail]
  `(let [*keeper# (atom nil)]
     (defn ~n
       ~args

       (when-not @*keeper#
         (reset! *keeper# (eval-str ~(pr-str `(~'defn ~n ~args ~@fn-tail)))))

       (-> (if ~(boolean (seq args))
             (apply format "(%s %s)"
                    (quote ~n)
                    (mapv pr-str ~args))
             (format "(%s)" (quote ~n)))
           eval-str))))
#_ (defn* my-fn
     [x]
     (+ x 5))
#_ (time
    (doseq [_ (range 500)]
      (my-fn 1)))

(comment

  (do
    (vp/defcomp BlenderObject
      (org.vybe.blender.Object/layout))

    (defn* obj-pointer
      [obj]
      (.as_pointer (vybe.basilisp.blender/obj-find obj))))
  (obj-pointer "Cube.001")

  (def mmm
    (-> (vp/address->mem (obj-pointer "Cube.001"))
        (vp/reinterpret (.byteSize (.layout BlenderObject)))
        (vp/p->map BlenderObject)))
  (:loc mmm)

  (merge mmm {:loc (vt/Translation [7.19 4.5 1.3277])})

  ()

  ())
