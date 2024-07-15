(ns vybe.clerk
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [vybe.util :as vy.u]
   [nextjournal.clerk :as clerk]
   vybe.clerk.util))

(defonce *docs (atom {}))

^::clerk/sync
(defonce *state
  (atom {}))

(clerk/eval-cljs
 '(do
    (require '[reagent.core :as r])
    (require '[clojure.math :as math])

    (defonce *internal-state
      (atom {}))

    (some-> @*internal-state :gui .destroy)
    (def gui (new (.. js/dat -GUI)))

    (defonce jxg js/JXG)
    (defonce react js/react)
    (defonce tremor js/tremor)

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
                               (swap! *fn-cache assoc k (fn [] (vy.u/enqueue-command! f)))
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
  "Like `clojure.core/swap!`, but updates the internal state."
  [f & args]
  (swap! *state (fn [old-state]
                  (adapt-data (apply f old-state args)))))
#_(swap dissoc :ggggg)

^::clerk/sync
(defonce *graphs (atom []))

{::clerk/visibility {:code :hide :result :show}}

(clerk/with-viewer
  {:transform-fn clerk/mark-presented
   :render-fn '(fn [_]
                 (when (not= (keys @*state)
                             (:previous-keys @*internal-state))
                   (reupdate!)))}
  nil)

{::clerk/visibility {:code :hide :result :show}}

(clerk/with-viewer
  {:transform-fn clerk/mark-presented
   :render-fn
   '(fn [{:keys [docs]}]
      ;; Inspired by https://github.com/mrdoob/stats.js/blob/master/src/Stats.js
      (r/with-let [*tab (atom :graphs)]
        (let [width 600
              height (* (count @*graphs) 60) #_ 60
              tab (fn [k s]
                    [:span.tab.link-secondary.font-bold {:role :tab
                                                         :class (when (= @*tab k)
                                                                  [:tab-active])
                                                         :on-click #(reset! *tab k)}
                     s])]
          [:div

           [:div.tabs.tabs-lifted.mb-5.w-24 {:role :tablist}
            (tab :graphs "Graphs")
            (tab :docs "Docs")]

           (case @*tab
             :graphs
             [:div

              [:canvas
               {:width width
                :height height
                :ref (fn [el]
                       (when el
                         (let [ctx (.getContext el "2d")
                               props! (fn [m]
                                        (->> m
                                             (mapv (fn [[k v]]
                                                     (aset ctx (name k) v)))))]
                           (.clearRect ctx 0 0 width height)

                           (doseq [[i [identifier data]] (map-indexed vector @*graphs)]
                             (let [data  (mapv #(* (/ % 300.0) 16) data)]
                               (props! {:fillStyle (nth ["purple" "blue" "green"]
                                                        (mod i 3))})
                               (.fillText ctx (str identifier " ------------ " (int (peek data)) " cpf") 0 (+ 10 (* 60 i)))
                               (doseq [[j v] (->> data
                                                  (map (fn [v]
                                                         (min 20 (math/ceil v))))
                                                  (map-indexed vector))]
                                 (.fillRect ctx
                                            (* j 3)
                                            (+ 35 (* 60 i))
                                            3
                                            (- v))))))))}]]

             :docs
             [:div {:class "overflow-x-auto"}
              (for [[title documentation] (sort-by first docs)]
                [:<>
                 [:h2 (str (symbol title))]
                 (when-let [doc (:doc documentation)]
                   [:p.text-sm.font-mono doc])
                 [:table {:class "table table-xs"}
                  [:thead
                   [:tr
                    [:th {:class ["w-1/3"]} "Name"]
                    [:th {:class ["w-2/3"]} "Doc"]]]
                  [:tbody
                   (for [[k {:keys [doc examples]}] (sort-by key (dissoc documentation :doc))]
                     [:tr
                      [:td
                       [:code (str k)]]
                      [:td
                       [:p.font-mono doc]
                       (for [example examples]
                         [:p [:code (str example)]])]])]]])])])))}
  {:docs @*docs})
#_ (reset! *graphs [[0 10 20 10 5 3 2 1]
                    [0 20 10 10 50 20 1]
                    [10 9 8 7 6 5 3 2]])

#_@*state

{::clerk/visibility {:code :hide :result :hide}}

(defonce *graph-cache (atom {}))

(defn ^:private recompute
  []
  (Thread/sleep 300)
  (when (:debug @vy.u/*state)
    (reset! *graphs (->> (::vy.u/counter @vy.u/*probe)
                         (map (fn [[k v]]
                                (let [s (if (string? k)
                                          k
                                          (str (symbol k)))
                                      res (->> (if-let [{:keys [coll acc]} (get @*graph-cache s)]
                                                 (conj coll (- v acc))
                                                 (concat (repeat 200 0) [v]))
                                               (take-last 200)
                                               vec)]
                                  (swap! *graph-cache assoc s {:coll res
                                                               :acc v})
                                  [s res])))
                         (sort-by (juxt #(every? #{0} (second %)) first))))))

(defonce ^:private my-loop
  (when-not vy.u/prd?
    (future
      (loop []
        (#'recompute)
        (recur)))))

(clerk/show! *ns*)
