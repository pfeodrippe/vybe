(ns vybe.audio-test
  (:require
   [clojure.test :refer [deftest testing is use-fixtures]]
   [vybe.audio :as va]
   [overtone.core :refer :all]
   [clojure.math :as math]
   matcher-combinators.test))

(use-fixtures :each (fn stop-sounds-for-a-test
                      [f]
                      (try
                        (f)
                        (finally
                          (stop)))))

(va/audio-enable!)

(defn inline-poll
  ([input handler]
   (inline-poll input ::test {} handler))
  ([input event-identifier handler]
   (inline-poll input event-identifier {} handler))
  ([input event-identifier {:keys [freq] :or {freq 60}} handler]
   (on-event
    [:overtone :osc-msg-received]
    (fn [{{path :path args :args} :msg}]
      (let [poll-path "/overtone/internal/poll/"]
        (when (.startsWith ^java.lang.String path poll-path)
          (let [event (.substring ^java.lang.String path (count poll-path))
                data (nth args 2)]
            (try
              (handler {:event event :data data})
              (catch Exception e
                (println e)))))))
    event-identifier)

   (poll (impulse:kr freq) (a2k input) (str (symbol event-identifier)))))

(deftest check-audio-test
  (let [*collector (atom [])]
    (is (match?
         {:id some?}
         (->> (inline-poll (sin-osc 400)
                           (fn [{:keys [data]}]
                             (swap! *collector conj data)))
              (demo 0.5)
              (into {}))))

    (Thread/sleep 600)

    (testing {:data @*collector}
      (is (some pos? @*collector)))))

(def directional
  (synth-load "resources/com/pfeodrippe/vybe/overtone/directional.scsyndef"))

(defonce my-bus
  (audio-bus 1))

(defonce main-g (group "get-on-the-bus main"))
(defonce early-g (group "early birds" :head main-g))
(defonce later-g (group "latecomers" :after early-g))

(defsynth source-sound
  [freq 300, mul 0.5, out_bus 0]
  (out out_bus
       (* mul (lpf (pink-noise 0.8) 500))
       #_(sin-osc 440)))

(deftest directional-test
  (let [*collector (atom [])
        dir (directional [:tail later-g] :in my-bus :out_bus 0)]
    (source-sound [:tail early-g] :out_bus my-bus)
    (is (match?
         {:id some?}
         (->> dir
              (into {}))))

    (let [*monitor (audio-bus-monitor my-bus)]
      (add-watch *monitor ::directional-watcher
                 (fn [_key _ref _old-data new-data]
                   #_(ctl dir :azim  (* (math/sin (* (System/currentTimeMillis) 0.002))
                                        math/PI))
                   (swap! *collector conj new-data)))

      (Thread/sleep 1000)
      (remove-watch *monitor ::directional-watcher))

    (testing {:data @*collector}
      (is (some pos? @*collector)))))
