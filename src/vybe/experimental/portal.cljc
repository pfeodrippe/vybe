(ns vybe.experimental.portal
  "CLJS code."
  (:require
   [portal.ui.api :as p]
   [portal.ui.inspector :as ins]
   [portal.ui.state :as state]
   [reagent.core :as r]
   [portal.viewer :as pv]
   [portal.ui.viewer.vega-lite :as vega-lite]))

(defn deck? [value] (and (coll? value) (not (map? value))))

(do
  (defn view-presentation []
    (let [slide (r/atom 0)]
      (fn [slides]
        [:<>
         [ins/inspector (nth (seq slides) @slide :no-slide)]
         [:button {:on-click #(swap! slide dec)} "Pd"]
         [:button {:on-click #(swap! slide inc)} "nxt"]])))

  (portal.ui.api/register-viewer!
   {:name ::slides
    :predicate deck?
    :component view-presentation})

  nil)

(defn calc-bmi [{:keys [height weight bmi] :as data}]
  (let [h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))

(def bmi-data (r/atom (calc-bmi {:height 180 :weight 80})))

(defn slider [param value min max invalidates]
  [:input {:type "range" :value value :min min :max max
           :style {:width "100%"}
           :on-change (fn [e]
                        (let [new-value (js/parseInt (.. e -target -value))]
                          (swap! bmi-data
                                 (fn [data]
                                   (-> data
                                     (assoc param new-value)
                                     (dissoc invalidates)
                                     calc-bmi)))))}])

(defonce *data
  (r/atom (range 0 (* 2 3.14) 0.25)))

(defonce time-updater
  (js/setInterval
   (fn []
     (reset! *data (range 0 (* (+ 2 (rand-int 50)) 3.14) 0.25))
     (println :AAA (last @*data)))
   (/ 1000.0 1.0)))

(defn bmi-component
  []
  #_(let [{:keys [weight height bmi]} @bmi-data
          [color diagnose] (cond
                             (< bmi 18.5) ["orange" "underweight"]
                             (< bmi 25) ["inherit" "normal"]
                             (< bmi 30) ["orange" "overweight"]
                             :else ["red" "obese"])]
      [:div
       [:h3 "BMI calculator"]
       [:div
        "Height: " (int height) "cm"
        [slider :height height 100 220 :bmi]]
       [:div
        "Weight: " (int weight) "kg"
        [slider :weight weight 30 150 :bmi]]
       [:div
        "BMI: " (int bmi) " "
        [:span {:style {:color color}} diagnose]
        [slider :bmi bmi 10 50 :weight]]])
  (let [data @*data
        chart [vega-lite/vega-lite-viewer
               {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
                :renderer "html"
                :description (last data)
                :data {:values
                       (mapv #(-> {:time % :value (Math/sin %)})
                             data)}
                :encoding {:x {:field "time" :type "quantitative"}
                           :y {:field "value" :type "quantitative"}}
                :mark "line"}]]
    (js/console.log (get chart 1))
    (println :BBB (last data))
    [:div
     (last data)
     chart])
  #_(let [seconds-elapsed (r/atom 0)]
    (fn []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:div
       "Seconds Elapsed: " @seconds-elapsed])))

(portal.ui.api/register-viewer!
 {:name ::view-presentation
  :predicate deck?
  :component (fn [] (fn [_] [bmi-component]))})

(comment

  (js/alert "4")

  (tap> 4)

  ())
