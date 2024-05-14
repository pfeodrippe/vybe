(ns vybe.audio
  (:require
   [clojure.java.io :as io]
   [overtone.live :refer :all :as l]
   [overtone.midi :as midi]))

(comment

  (doseq [_ (range 100)]
    (demo 0.2
          [(sin-osc :freq 400)
           (sin-osc :freq 300)])
    (Thread/sleep 500)
    (demo 0.2
          [(sin-osc :freq 600)
           (sin-osc :freq 300)])
    (Thread/sleep 500))

  ;; Midi.
  (doseq [_ (range 100)]
    (-> (first (midi-connected-receivers))
        (midi/midi-note 58 30 1000))
    (Thread/sleep 500)
    (-> (first (midi-connected-receivers))
        (midi/midi-note 60 30 1000))
    (Thread/sleep 500))

  ;; How To Fully Connect Bitwig Studio & VCV Rack (On A Mac), https://www.youtube.com/watch?v=mAxDrDPXtvA
  ;; Audio.
  (do (kill-server)
      (connect-server 57110))

  ())

(defn sound
  [res-path & args]
  (apply sample (-> (io/resource res-path) io/file .getPath) args))

(defn play
  ([snd]
   (play snd {}))
  ([snd {:keys [pos]
         :or {pos 0}}]
   (sample-player snd)))
#_ (-> (sound "sounds/mixkit-cool-interface-click-tone-2568.wav")
       (play))
