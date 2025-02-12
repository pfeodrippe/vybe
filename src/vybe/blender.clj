(ns vybe.blender
  (:require
   [clojure.edn :as edn]
   [nrepl.core :as nrepl]
   [clojure.string :as str]
   [vybe.panama :as vp]))

(comment

  (vp/defcomp BlenderObject
    (org.vybe.blender.Object/layout))

  (def mmm
    (-> (vp/address->mem 14619812896)
        (vp/reinterpret (.byteSize (.layout BlenderObject)))
        (vp/p->map BlenderObject)))
  (:loc mmm)

  (merge mmm {:loc (vt/Translation [7.19 4.5 1.3277])})

  ())

(def ^:no-doc ^:dynamic *nrepl-init* nil)
#_ (def blender-session (connect 7889))
#_ (*nrepl-init* blender-session)

(defonce *blender-session (atom nil))

(defn connect
  "Connect to a running basilisp Blender nREPL server.

  Returns a blender-session map containing connection and the client (you can use it
  with other functions from this namespace)."
  [port]
  (let [conn (nrepl/connect :port port)
        client (nrepl/client conn Long/MAX_VALUE)
        blender-session {:conn conn
                         :client client}]
    (reset! *blender-session blender-session)
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
#_ (-> {:ns "vybe.basilisp.bake"
        :op "complete"
        :prefix "edn/"}
       (message blender-session))

(defn eval-str
  "Eval from the basilisp Blender session.
  It will start a blender session on port 7889 if
  not started already."
  ([code-str]
   (when (nil? @*blender-session)
     (connect 7889))
   (eval-str @*blender-session code-str))
  ([blender-session code-str]
   (eval-str blender-session {} code-str))
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
#_ (eval-str (pr-str '(+ 2 3)))
#_ (eval-str (format "(%s %s)"
                     (pr-str '(fn [x] (+ x 34)))
                     10))
#_ (->> (pr-str '#'map)
        (eval-str blender-session))
#_ (->> (pr-str '(+ 2 12))
        (eval-str blender-session {:ns "user"
                                   :line 1
                                   :column 1
                                   :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"
                                   :context :expr}))
#_ (->> (pr-str '#'edn/read)
        (eval-str blender-session {:ns "user"
                                   :line 1
                                   :column 1
                                   :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"
                                   :context :expr}))
#_ (->> (pr-str '[bpy.data/objects])
        (eval-str blender-session {:ns "vybe.basilisp.bake"}))
#_ (eval-str 'bpy.ops/objects)

(defmacro defn*
  [n args & fn-tail]
  `(let [*keeper# (atom nil)]
     (defn ~n
       ~args

       ;; TODO Cache function.
       (when-not @*keeper#
         (reset! *keeper# (eval-str ~(pr-str `(~'defn ~n ~args ~@fn-tail)))))

       (-> (apply format "(%s %s)"
                    (quote ~n)
                    (mapv pr-str ~args))
           #_ #_eval-str
           :value))))
#_ (defn* my-fn
     [x]
     (+ x 5))
#_ (time (doseq [_ (range 5)]
           (my-fn 40)))

#_(defmacro eval*
    "Eval form. It will start a blender session on port 7889 if
  not started already."
    [form & [blender-session]]
    (let [form-metadata (meta &form)]
      `(let [_# (when (and (not ~blender-session) (nil? @*blender-session))
                  (connect 7889))
             blender-session# (or ~blender-session @*blender-session)]
         (-> (->> (with-meta (quote (~'let
                                     ~(->> (into [] (zipmap (keys &env) ['y] #_(range 100 1000)))
                                           (apply concat)
                                           vec)
                                     ~form))
                    (meta ~form-metadata))
                  pr-str
                  (eval-str blender-session#
                            {:ns "vybe.basilisp.bake"
                             :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"}))
             :value))))
#_ (let [y 11]
     #_(eval-str (pr-str '(fn [y] (- 4 y))))
     (eval* (- 4 y)))
#_ (eval* (- 4 10))
#_ (eval* (do (select "Scene")
              (select "SceneOutdoors")
              (select-only "Scene")))
