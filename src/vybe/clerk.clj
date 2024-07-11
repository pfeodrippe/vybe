(ns vybe.clerk
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [nextjournal.clerk :as clerk]
   vybe.clerk.util))

^::clerk/sync
(defonce ^:private *state
  (atom {}))

^::clerk/sync
(defonce ^:private *state
  (atom {}))

(clerk/eval-cljs
 '(do
    (require '[reagent.core :as r])

    (defonce *internal-state
      (atom {}))

    (some-> @*internal-state :gui .destroy)
    (def gui (new (.. js/dat -GUI)))

    (defonce jxg js/JXG)

    (defn reupdate!
      []
      (when-let [folder (:folder @*internal-state)]
        (.. gui (removeFolder folder)))
      (let [folder (.. gui (addFolder "Controls"))
            -state (->> @*state
                        (mapv (fn [[k v]]
                                [k (cond
                                     (= v ::function)
                                     (fn []
                                       (nextjournal.clerk.render/clerk-eval `((get @*fn-cache ~k))))

                                     (map? v)
                                     (:v v)

                                     :else
                                     v)]))
                        (into {}))
            state (js/Proxy. (clj->js -state)
                             (clj->js {:set (fn [target k v]
                                              (aset target k v)
                                              (swap! *state assoc (keyword k) v)
                                              true)
                                       :get (fn [target k]
                                              (aget target k))}))]

        (run! (fn [[k v]]
                (cond
                  (number? v)
                  (.. folder (add state (str (symbol k)) -100 100))

                  (map? v)
                  (.. folder (add state (str (symbol k))
                                  (or (first (:range v)) -100)
                                  (or (second (:range v)) 100)
                                  (or (:step v) 1)))

                  (or  (string? v)
                       (keyword? v)
                       (boolean? v)
                       (= v ::function))
                  (.. folder (add state (str (symbol k))))

                  :else
                  (throw (ex-info "Value type not supported"
                                  {:k k
                                   :v v}))))
              @*state)

        (swap! *internal-state merge
               {:gui gui
                :folder folder
                :state state
                :previous-keys (keys @*state)})

        (.. folder open)))))

(def jgx-viewer
  {:transform-fn clerk/mark-presented
   :render-fn '(fn [value]
                 (when value
                   [:div {:style {:width "500px" :height "500px"}
                          :ref (fn [el]
                                 (when el
                                   (.. jxg -JSXGraph
                                       (initBoard el (clj->js value)))))}]))})

(clerk/with-viewer jgx-viewer
  {:boundingBox [-10 10 10 -10]
   :axis true})

(def transform-var
  (comp (clerk/update-val symbol)
        clerk/mark-presented))

(comment

  (clerk/serve! {:browse false})

  (clerk/serve! {:watch-paths ["notebooks" "src" "../vybe/src"]})

  (def afa (atom {}))

  (add-watch afa ::afa
             (fn [_ _ _ new-state]
               ))

  ())

(defonce ^:private *fn-cache (atom {}))

(add-watch *state ::*state
           (fn [_k _ref _old-state _new-state]
             (clerk/recompute!)))

(defn- adapt-data
  [data]
  (if-let [fn-kvs (some->> data
                           (filter (comp fn? last))
                           seq)]
    (let [adapted (->> fn-kvs
                       (mapv (fn [[k f]]
                               (swap! *fn-cache assoc k f)
                               [k ::function]))
                       (into {}))]
      (-> data
          (merge adapted)))
    data))

(defn init!
  [initial-data]
  (reset! *state (adapt-data initial-data))
  (clerk/serve! {:browse false}))
#_ (init! {:a 10
           :a-map {:range [-200 200]
                   :step 20
                   :v 140}
           :b false
           :c (fn []
                (println :AddAdda))
           :d "look"})

(defn swap
  "Like `clojure.core/swap!`, but adapts the internal state."
  [f & args]
  (swap! *state (fn [old-state]
                  (adapt-data (apply f old-state args)))))
#_(swap dissoc :ggggg)

{::clerk/visibility {:code :hide :result :show}}

(clerk/with-viewer
  {:transform-fn clerk/mark-presented
   :render-fn '(fn [_]
                 (when (not= (keys @*state)
                             (:previous-keys @*internal-state))
                   (reupdate!)))}
  nil)

{::clerk/visibility {:code :show :result :show}}

@*state

{::clerk/visibility {:code :hide :result :hide}}

(clerk/show! *ns*)
