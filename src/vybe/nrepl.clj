(ns vybe.nrepl
  "Inspired by (or fork of) https://github.com/djblue/portal/blob/master/src/portal/nrepl.clj"
  (:require [clojure.datafy :as d]
            [clojure.main :as main]
            [clojure.test :as test]
            [nrepl.middleware :refer [set-descriptor!]]
            [nrepl.middleware.caught :as caught]
            [nrepl.middleware.print :as print]
            [nrepl.misc :refer (response-for)]
            [nrepl.transport :as transport]
            [vybe.blender :as vb])
  (:import [java.util Date]
           [nrepl.transport Transport]))

(def ^:no-doc ^:dynamic *blender-ns* nil)
(def ^:no-doc ^:dynamic *blender-session* nil)

(defn- -wrap-blender-repl
  [handler {:keys [op session transport] :as msg}]
  (when (and (= "eval" op)
             (not (contains? @session #'*blender-session*)))
    (swap! session assoc
           #'*blender-session* nil
           #'vb/*nrepl-init* (fn [blender-session]
                               (set! *blender-session* blender-session)
                               (println "To quit, type:" :blender/quit)
                               [:repl blender-session])))

  (let [blender-session (get @session #'*blender-session*)]
    (cond
      (and blender-session (#{"eval" "load-file"} op))
      (->> (try

             (let [[code file] (if (= "load-file" (:op msg))
                                 [(:file msg) (:file-path msg)]
                                 [(:code msg) (:file msg)])

                   {:keys [value] :as response}
                   (vb/eval-str code
                                blender-session
                                (-> {:ns (get @session #'*blender-ns*)}
                                    (merge msg)
                                    (select-keys [:ns :line :column])
                                    (assoc :file file
                                           #_ #_:verbose true
                                           :context (case op
                                                      "eval" :expr
                                                      "load-file" :statement)
                                           #_ #_:re-render (= op "load-file"))))]

               (when-let [namespace (:ns response)]
                 (swap! session assoc #'*blender-ns* namespace))

               (when (= value :blender/quit)
                 (println :LEAVING_BLENDER_SESSION)
                 (swap! session dissoc #'*blender-session* #'*blender-ns*))

               (merge
                response
                {:status      :done
                 ::print/keys #{:value}}))

             (catch Exception e
               (swap! session assoc #'*e e)
               {::caught/throwable e
                :status            [:done :eval-error]
                :ex                (str (class e)) #_ex
                :root-ex           (str (class (main/root-cause e)))
                :causes            (if-let [via (get-in (ex-data e) [:error :via])]
                                     (for [{:keys [type message at]} via]
                                       {:class      type
                                        :message    message
                                        :stacktrace at})
                                     (for [ex (take-while some? (iterate ex-cause e))]
                                       {:class      (str (class ex))
                                        :message    (ex-message ex)
                                        :stacktrace []}))
                #_ #_:causes            (if-let [via (get-in (ex-data e) [:error :via])]
                                          (for [{:keys [type message at]} via]
                                            {:class      type
                                             :message    message
                                             :stacktrace at})
                                          (for [ex (take-while some? (iterate ex-cause e))]
                                            {:class      (str (class ex))
                                             :message    (ex-message ex)
                                             :stacktrace []}))}))
           (response-for msg)
           (transport/send transport))

      (and blender-session (#{"complete"} op))
      #_blender-session
      (let [res (-> msg
                    ;; https://nrepl.org/nrepl/ops.html
                    (select-keys [:ns :op :prefix :context :id :complete-fn :options
                                  :verbose? :aux :ops :versions
                                  :column])
                    (vb/message blender-session))]
        (->> (merge (first res)
                    #_{:status :done
                       ::print/keys #{:completions}})
             (response-for msg)
             (transport/send transport)))

      :else
      (let [res (handler msg)]
        res))))

(defn wrap-blender-repl
  [handler]
  (partial #'-wrap-blender-repl handler))

(set-descriptor! #'wrap-blender-repl
                 {:requires #{"clone" #'print/wrap-print #'caught/wrap-caught}
                  :expects #{"eval"}
                  :handles {}})

(def middleware
  [`wrap-blender-repl])
