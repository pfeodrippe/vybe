(ns vybe.clerk.audio
  "Helper functions for overtone."
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [nextjournal.clerk :as clerk]
   [vybe.audio :as va]
   [vybe.panama :as vp]))

#_ (clerk/serve! {:watch-paths ["../vybe/src/vybe/clerk/audio.clj"]})

(defn data
  []
  (let [{:keys [arr timeline]} (last @va/*buffers)]
      (->> (mapv vector
                   (seq (vp/arr arr 88000 :float))
                   (seq (vp/arr timeline 88000 :long)))
             (sort-by last)
             (mapv first)
             (take-nth 100)
             #_(apply max))))

(comment

  (def *continue (atom true))
  (reset! *continue false)

  (future
    (while @*continue
      (clerk/recompute!)
      (Thread/sleep 100)))

  ())

{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/plotly {:data [{:y (data) :type "scatter"}]
               :layout {:margin {:l 20 :r 0 :b 20 :t 20}
                        :paper_bgcolor "transparent"
                        :plot_bgcolor "transparent"}
               :config {:displayModeBar false
                        :displayLogo false}})
nil

#_(clerk/vl {:width 650 :height 400 :data {:url "https://vega.github.io/vega-datasets/data/us-10m.json"
                                           :format {:type "topojson" :feature "counties"}}
             :transform [{:lookup "id" :from {:data {:url "https://vega.github.io/vega-datasets/data/unemployment.tsv"}
                                              :key "id" :fields ["rate"]}}]
             :projection {:type "albersUsa"} :mark "geoshape" :encoding {:color {:field "rate" :type "quantitative"}}
             :background "transparent"
             :embed/opts {:actions false}})
