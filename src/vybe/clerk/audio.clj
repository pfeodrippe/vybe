(ns vybe.clerk.audio
  "[EXPERIMENTAL] Helper functions for overtone."
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [nextjournal.clerk :as clerk]
   [vybe.audio :as va]
   [vybe.panama :as vp])
  (:import
   (com.github.psambit9791.jdsp.transform FastFourier)))

(comment

  (def fft (FastFourier. (double-array lala)))
  (.transform fft)
  (seq (.getMagnitude fft true))
  (seq (.getFFTFreq fft 44000 true))

  ())

#_ (clerk/serve! {:watch-paths ["../vybe/src/vybe/clerk/audio.clj"]})
#_ (clerk/show! *ns*)

(defn get-data
  []
  (let [{:keys [arr timeline]} (last @va/*buffers)
        data (->> (mapv vector
                        (seq (vp/arr arr 88000 :float))
                        (seq (vp/arr timeline 88000 :long)))
                  (sort-by last)
                  (mapv first)
                  (take-last 2200)
                  #_(take-nth 100)
                  #_(apply max))
        fft (FastFourier. (double-array data))]
    (.transform fft)
    data
    [{:y data
      :type :scatter}
     {:x (take 200 (seq (.getFFTFreq fft 44000 true)))
      :y (take 200 (seq (.getMagnitude fft true)))
      :type :scatter}]))

(comment

  (def *continue (atom true))
  (reset! *continue false)

  (future
    (while @*continue
      (clerk/recompute!)
      (Thread/sleep 100)))

  ())

(let [[t f] (get-data)]
  (def time-data t)
  (def fft-data f))

{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/plotly
 {:data [time-data]
  :layout {:autorange true
           :margin {:l 20 :r 0 :b 20 :t 20}
           :paper_bgcolor "transparent"
           :plot_bgcolor "transparent"}
  :config {:displayModeBar false
           :displayLogo false}})

{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/plotly
 {:data [fft-data]
  :layout {:autorange true
           :margin {:l 20 :r 0 :b 20 :t 20}
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
