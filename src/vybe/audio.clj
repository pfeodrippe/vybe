(ns vybe.audio
  (:require
   [clojure.java.io :as io]
   #_[overtone.live :refer :all :as l]
   [overtone.midi :as midi]
   [overtone.sc.machinery.server.connection :as ov.conn]))

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

(defonce ^:private *audio-enabled? (atom false))

(defn audio-enable!
  "Enable overtone audio. You need to call this before using `sound` or
  you will need to reeval the places where `sound` is used.

  `(require '[overtone.core :refer :all])` will be called and you will have
  the overtone vars available on your namespace."
  []
  (try
    (ov.conn/scsynth-path)
    (when-not @*audio-enabled?
      (require '[overtone.core :refer :all])
      (eval '(boot-server))
      (reset! *audio-enabled? true))
    (catch Exception e#
      (println e#)
      (println "\n\n ----- WARNING -----\nIf you want audio working, download SuperCollider at\nhttps://supercollider.github.io/downloads.html"))))

;; Try to enable audio.
#_(audio-enable!)

(defmacro sound
  "Macro used to wrap audio calls so we can use it safely for users who
  have overtone installed.

  If `audio-enable!` wasn't called, nothing will be evaluated."
  [& body]
  (when @*audio-enabled?
    `(do ~@body)))

#_(defn play
    ([snd]
     (play snd {}))
    ([snd {:keys [pos]
           :or {pos 0}}]
     (sample-player snd)))
#_ (-> (sound "sounds/mixkit-cool-interface-click-tone-2568.wav")
       (play))
