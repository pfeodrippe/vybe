(ns vybe.blender
  (:require
   [clojure.edn :as edn]
   [nrepl.core :as nrepl]
   [clojure.string :as str]
   [clojure.pprint :as pprint]))

(def ^:no-doc ^:dynamic *nrepl-init* nil)
#_ (def blender-session (connect 7889))
#_ (*nrepl-init* blender-session)

(defn connect
  "Connect to a running basilisp Blender nREPL server.

  Returns a blender-session map containing connection and the client (you can use it
  with other functions from this namespace)."
  [port]
  (let [conn (nrepl/connect :port port)
        client (nrepl/client conn Long/MAX_VALUE)]
    {:conn conn
     :client client}))
#_ (def blender-session (connect 7889))

(defn disconnect
  [{:keys [conn]}]
  (.close conn))

(defn eval-str
  "Eval from the basilisp Blender session."
  ([code-str blender-session]
   (eval-str code-str blender-session {}))
  ([code-str {:keys [client]} params]
   (let [{:keys [value err ex] :as sss} (-> client
                                            (nrepl/message (merge {:op "eval"
                                                                   :code code-str}
                                                                  params))
                                            doall
                                            nrepl/combine-responses)
         v (first value)]
     (if (not (or err ex))
       {:value (if (str/starts-with? v "<module")
                 v
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
#_ (-> (pr-str '[(+ 2 12)])
       (eval-str blender-session {:ns "user"
                                  :line 1
                                  :column 1
                                  :file "/Users/pfeodrippe/dev/vybe/basilisp/src/vybe/basilisp/bake.lpy"
                                  :context :expr}))
#_ (-> (pr-str '[bpy.data/objects])
       (eval-str blender-session {:ns "vybe.basilisp.bake"}))
#_ (-> (pr-str 'bpy.ops/objects)
       (eval-str blender-session))
