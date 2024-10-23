(ns vybe.audio
  (:require
   [clojure.java.io :as io]
   #_[overtone.live :refer :all :as l]
   [overtone.midi :as midi]
   [overtone.sc.machinery.server.connection :as ov.conn]
   [overtone.config.store :as ov.config]
   [overtone.helpers.file :as ov.file]
   [overtone.sc.defaults :as ov.defaults]
   [overtone.helpers.system :refer [get-os linux-os? mac-os? windows-os?]]
   [overtone.config.log :as ov.log]
   [overtone.helpers.lib :as ov.lib]
   [clojure.tools.build.api :as b]
   [vybe.util :as vy.u]))

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

#_(vy.u/debug-set! true)

;; Temporary!!
(defn- scsynth-path
  ([]
   (scsynth-path {}))
  ([{:keys [native?]
     :or {native? true}}]
   (let [sc-config (or (ov.config/config-get :sc-path)
                       ;; TODO Use env var insted of hardcoded.
                       (when native?
                         (cond
                           (mac-os?)
                           (let [file (io/file (vy.u/app-resource "vybe_native/macos/universal/supercollider/Resources/scsynth"))
                                 path (.getAbsolutePath file)]
                             (if (.exists file)
                               (do
                                 (when-not (.canExecute file)
                                   (vy.u/debug "making scsynth executable"
                                               {:output (b/process {:command-args ["chmod" "+x" path]})}))
                                 (vy.u/debug "using scsynth from the native folder" {:path path})
                                 path)
                               (vy.u/debug "inexistent" {:file file})))

                           (windows-os?)
                           (let [file (io/file (vy.u/app-resource "vybe_native/windows/x64/scsynth.exe"))
                                 path (.getAbsolutePath file)]
                             (if (.exists file)
                               (do
                                 (vy.u/debug "scsynth executable?"
                                             {:file file
                                              :executable? (.canExecute file)})
                                 path)
                               (vy.u/debug "inexistent" {:file file})))

                           ;; No linux built-in lib :(
                           )))
         sc-path (or (when (windows-os?)
                       (ov.file/find-executable "scsynth.exe"))
                     (ov.file/find-executable "scsynth"))
         sc-wellknown (#'ov.conn/find-well-known-sc-path)
         match (or sc-config sc-path sc-wellknown)]
     (if (and (not sc-config) native?)
       (scsynth-path {:native? false})
       (when-not match
         (throw (ex-info (str "Failed to find SuperCollider server executable (scsynth). The file does not exist or is not executable. Places I've looked:\n"
                              "- `:sc-path` in " ov.config/OVERTONE-CONFIG-FILE " (" (pr-str sc-config) ")\n"
                              "- The current PATH (" (System/getenv "PATH") ")\n"
                              "- Well-known locations " (seq (ov.defaults/SC-PATHS (get-os)))"")
                         {}))))
     (vy.u/debug "Found SuperCollider server: " match " (" (cond
                                                             sc-config
                                                             (str "configured in " ov.config/OVERTONE-CONFIG-FILE)
                                                             sc-path
                                                             "PATH"
                                                             sc-wellknown
                                                             (str "well-known location for " (name (get-os))))
                 ")")
     (str match))))
(alter-var-root #'ov.conn/scsynth-path (constantly scsynth-path))

;; Temporary!!!
(defn- windows-sc-path
  "Returns a string representing the path for SuperCollider on Windows,
   or nil if not on Windows."
  []
  (when (windows-os?)
    (let [p-files   (map str (concat
                              (ov.lib/env-files "PROGRAMFILES")
                              (ov.lib/env-files "PROGRAMFILES(X86)")))
          sc-files  (filter #(.contains % "SuperCollider") p-files)
          recent-sc (or (last (sort (seq sc-files)))
                        ".")]
      recent-sc)))
(alter-var-root #'ov.lib/windows-sc-path (constantly windows-sc-path))

(defn audio-enable!
  "Enable overtone audio. You need to call this before using `sound` or
  you will need to reeval the places where `sound` is used.

  `(require '[overtone.core :refer :all])` will be called and you will have
  the overtone vars available on your namespace."
  []
  (try
    (ov.conn/scsynth-path)
    (require '[overtone.core :refer :all])
    (when-not @*audio-enabled?
      (eval '(boot-server))
      (reset! *audio-enabled? true))
    (catch Exception e#
      (println e#)
      (println "\n\n ----- WARNING -----\nIf you want audio working, download SuperCollider at\nhttps://supercollider.github.io/downloads.html"))))
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
