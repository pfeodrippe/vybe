(ns vybe.blender
  (:require
   [clojure.edn :as edn]
   [nrepl.core :as nrepl]
   [clojure.string :as str]))

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
  "Eval from the basilisp Blender session."
  ([code-str blender-session]
   (eval-str code-str blender-session {}))
  ([code-str {:keys [client]} params]
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

                 (str/includes? v "bpy.")
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
#_ (-> (pr-str '#'map)
       (eval-str blender-session))
#_ (-> (pr-str '(+ 2 12))
       (eval-str blender-session {:ns "user"
                                  :line 1
                                  :column 1
                                  :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"
                                  :context :expr}))
#_ (-> (pr-str '#'edn/read)
       (eval-str blender-session {:ns "user"
                                  :line 1
                                  :column 1
                                  :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"
                                  :context :expr}))
#_ (-> (pr-str '[bpy.data/objects])
       (eval-str blender-session {:ns "vybe.basilisp.bake"}))
#_ (-> (pr-str 'bpy.ops/objects)
       (eval-str blender-session))

(defmacro eval*
  "Eval form. It will start a blender session on port 7889 if
  not started already."
  ([form]
   (when (nil? @*blender-session)
     (connect 7889))
   (eval* form @*blender-session))
  ([form blender-session]
   `(-> (pr-str ~form)
        (eval-str ~blender-session {:ns "vybe.basilisp.bake"
                                    :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"})
        :value)))
#_ (eval* (- 4 10))
#_ (eval* (do (select "Scene")
              (select "SceneOutdoors")
              (select-only "Scene")))
