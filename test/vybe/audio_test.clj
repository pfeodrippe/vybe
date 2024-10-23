(ns vybe.audio-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.audio :as va]
   [overtone.core :refer :all]
   matcher-combinators.test))

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
         (->> (inline-poll (sin-osc 4000)
                           (fn [{:keys [data]}]
                             (swap! *collector conj data)))
              (demo 0.5)
              va/sound
              (into {}))))

    (Thread/sleep 600)

    (testing {:data @*collector}
      (is (some pos? @*collector)))))

#_(deftest directional-test
  (let [*collector (atom [])]
    (is (match?
         {:id some?}
         (->> (inline-poll (sin-osc 440)
                           (fn [{:keys [data]}]
                             (swap! *collector conj data)))
              (demo 0.2)
              va/sound
              (into {}))))

    (Thread/sleep 300)

    (is (some pos? @*collector))))
