(ns vybe.audio
  (:require
   [clojure.java.io :as io]
   #_[overtone.live :refer :all :as l]
   [overtone.sc.machinery.server.connection :as ov.conn]
   [overtone.config.store :as ov.config]
   [overtone.helpers.file :as ov.file]
   [overtone.sc.defaults :as ov.defaults]
   [overtone.helpers.system :refer [get-os linux-os? mac-os? windows-os?]]
   [overtone.helpers.lib :as ov.lib]
   [clojure.tools.build.api :as b]
   [vybe.util :as vy.u]
   [vybe.panama :as vp]
   [vybe.raylib.c :as vr.c]
   [vybe.math :as vm]
   [vybe.flecs :as vf]
   [vybe.type :as vt]
   [overtone.core :refer :all]))

(defonce *audio-enabled? (atom false))

#_(vy.u/debug-set! true)

(defn- scsynth-path
  "Temporary!!"
  ([]
   (scsynth-path {}))
  ([{:keys [native?]
     :or {native? true}
     :as args}]
   (vy.u/debug "calling scsynth-path" {:args args})
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
         sc-path (delay (or (when (windows-os?)
                              (ov.file/find-executable "scsynth.exe"))
                            (ov.file/find-executable "scsynth")))
         sc-wellknown (delay (ov.file/find-well-known-sc-path ov.defaults/SC-PATHS))
         match (or sc-config @sc-path @sc-wellknown)]
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
                                                             @sc-path
                                                             "PATH"
                                                             @sc-wellknown
                                                             (str "well-known location for " (name (get-os))))
                 ")")
     (if (coll? match)
      (mapv str match)
      [(str match)]))))
(alter-var-root #'ov.conn/scsynth-path (constantly scsynth-path))
#_ (ov.conn/scsynth-path)

(defn- windows-sc-path
  "Returns a string representing the path for SuperCollider on Windows,
   or nil if not on Windows.

  Temporary!!"
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

(declare synths-init!)
#_ (stop)

(defn audio-enable!
  "Enable overtone audio. You need to call this before using `sound` or
  you will need to reeval the places where `sound` is used."
  []
  (try
    #_(ov.conn/scsynth-path)
    #_(require '[overtone.core :refer :all])
    (when-not @*audio-enabled?
      (boot-server)
      (synths-init!)
      (reset! *audio-enabled? true))
    (catch Exception e#
      (println e#)
      (println "\n\n ----- WARNING -----\nIf you want audio working, download SuperCollider at\nhttps://supercollider.github.io/downloads.html"))))

#_(stop)
#_(audio-enable!)

(defmacro sound
  "Macro used to wrap audio calls so we can use it safely for users who
  have overtone installed."
  [& body]
  `(when @*audio-enabled?
     #_(eval '(require '[overtone.core :refer :all]))
     ~@body))

;; -- Components.
(vp/defcomp SoundSource
  {:constructor (fn [{:keys [synth mul args]
                      :or {mul 1
                           args nil}}]
                  (when-not synth
                    (throw (ex-info "In SoundSource, `:synth` should not be `nil`, but a synth"
                                    {:synth synth})))
                  {:synth (vt/-clj->idx synth)
                   :mul mul
                   :args (vt/-clj->idx args)})}
  "`:synth` is a overtone `defsynth`, `:args` are extra parameters that will be set for the synth instance"
  [[:synth {:vp/getter vt/-idx->clj} :long]
   [:mul :float]
   [:_padding :float]
   [:args {:vp/getter vt/-idx->clj} :long]])
#_ (SoundSource {:synth noise-wind})

;; -- Ambisonic.
(defn- -ambisonic
  [sound-source source-transform target-transform {:keys [mul]} low-pass-synth]
  (when sound-source
    (let [d (vr.c/vector-3-distance
             (vm/matrix->translation target-transform)
             (vm/matrix->translation source-transform))
          [azim elev] (let [{:keys [x y z] :as _v} (-> source-transform
                                                       (vr.c/matrix-multiply (vr.c/matrix-invert target-transform))
                                                       vm/matrix->translation)]
                        (if (> z 0)
                          [(- (Math/atan2 (- x) (- z)))
                           (Math/atan2 (- y) (- z))]
                          [(Math/atan2 (- x) (- z))
                           (Math/atan2 (- y) (- z))]))
          amp (if (zero? d)
                1
                (/ 1 (* d d)))
          freq (max (min (* (/ 1 (* d d))
                            4000)
                         15000)
                    10)]
      #_(println :FREQ freq)
      (ctl low-pass-synth :freq freq :rq 0.8)
      (ctl sound-source
           :azim azim
           :elev elev
           :amp (min (* amp 1 mul) 1) #_(* mul 10)
           :distance d))))

(defonce ^:private *directional-nodes (atom {}))
#_ (stop)

;; -- Synths.
(defn- synths-init! []

  (defonce fx-directional
    (synth-load (vy.u/app-resource "com/pfeodrippe/vybe/overtone/directional.scsyndef")))

  (defsynth fx-lpf
    [in_bus 10 mul 1 freq 10000 rq 1 out_bus 0]
    (replace-out out_bus
                 (let [input (in in_bus)
                       filtered (-> input
                                    (b-low-pass freq rq)
                                    (b-low-pass freq rq)

                                    (b-low-pass freq rq)
                                    (b-low-pass freq rq)
                                    (b-low-pass freq rq)
                                    (b-low-pass freq rq)
                                    (b-low-pass freq rq)
                                    (b-low-pass freq rq))]
                   (+ filtered
                      (/ 8)))))

  (defsynth fx-fader
    [in_bus 10 out_bus 0 fade_time 2.0 gate 1]
    (let [input (in in_bus 1)
          env (env-gen (asr 0.01 0.1 0.1) #_ #_:action FREE :gate gate)]
      (replace-out out_bus (* env input 3)))))
#_ (stop)
#_ (synths-init!)

(defn- directional-node
  [w e {syn :synth}]
  (let [e-id (vf/eid w e)
        w-addr (vp/address (vf/get-world w))]
    (or (when-let [{:keys [dir *state] :as nodes} (get-in @*directional-nodes [w-addr e-id syn])]
          (let [{:keys [destroyed]} @*state]
            (when (and (not destroyed)
                       (not= (node-status dir) :destroyed))
              nodes)))
        (let [identifier (fn [s] (str s "-" w-addr "-" e-id))

              main-g (group (identifier "main"))
              sounds-g (group (identifier "sounds") :head main-g)
              effects-g (group (identifier "effects") :after sounds-g)

              directional-bus (audio-bus 2)

              synth-inst (syn [:tail sounds-g] :out_bus directional-bus)
              mixer (fx-fader [:tail sounds-g] :in_bus directional-bus :out_bus directional-bus)

              low-pass-synth (fx-lpf [:tail effects-g] :in_bus directional-bus :out_bus directional-bus)
              #_ #_rrr (fx-freeverb [:tail effects-g] :bus directional-bus)
              dir (fx-directional [:tail effects-g] :in directional-bus :out_bus 0 :amp 0)

              *state (atom {:destroyed false})]
          #_ (stop)

          ;; If the synth is a var, add a watch so we can kill the synths whenever
          ;; the var is recreated.
          (when (var? syn)
            (add-watch syn (identifier "directional-watcher")
                       (fn [_ _ _ _]
                         (vy.u/command-enqueue!
                          (fn []
                            (swap! *state assoc :destroyed true)
                            ;; Cutoff of 0.3s.
                            (ctl mixer :gate -1.1)
                            (ctl dir :amp 0)
                            (Thread/sleep 100)
                            (try
                              (free-bus directional-bus)
                              (group-free main-g)
                              (catch Exception e
                                (println e))))))))

          (swap! *directional-nodes assoc-in [w-addr e-id syn]
                 {:main-g main-g
                  :directional-bus directional-bus
                  :dir dir
                  :synth-inst synth-inst
                  :low-pass-synth low-pass-synth
                  :*state *state
                  #_ #_:rrr rrr})
          dir))))

;; -- Systems.
(defn- kill-sound-source
  [w e source]
  (let [{:keys [main-g directional-bus]} (directional-node w e source)]
    (free-bus directional-bus)
    (group-free main-g)))

(vf/defsystem sound-sources-update w
  [source-transform [vt/Transform :global]
   {:keys [args] :as source} SoundSource
   _ [:src '?e :vg/camera-active]
   target-transform [:src '?e [vt/Transform :global]]
   e :vf/entity]
  (sound
    (let [{:keys [dir synth-inst low-pass-synth rrr]} (directional-node w e source)]
      (when (every? #(and % (= (node-status dir) :live))
                    [dir synth-inst low-pass-synth])
        #_ (ctl rrr :wet-dry 3 :room-size 0.5)
        (-ambisonic dir source-transform target-transform source low-pass-synth)
        (when (and args synth-inst (not= (node-status synth-inst) :destroyed))
          (apply ctl synth-inst (apply concat args)))))))

#_(stop)
(vf/defobserver sound-source-remove w
  [:vf/events #{:remove}
   source SoundSource
   e :vf/entity]
  (sound
    (kill-sound-source w e source)))

(defn systems
  [w]
  [(sound-sources-update w)
   (sound-source-remove w)])
















;; ---------------- EXPERIMENTAL
(defonce *buffers (atom []))
#_ (hash @*buffers)

(vp/defcomp VybeSlice
  [[:len :long]
   [:arr [:vec {:size 4400} :float]]
   [:timeline [:vec {:size 4400} :long]]])

(defn -plugin
  [p-buf]
  (vp/with-arena [arena (vp/make-pool-arena (java.lang.foreign.SegmentAllocator/slicingAllocator p-buf))]
    (let [len 4400
          slice (VybeSlice)]
      (merge slice {:len len
                    :arr (vp/arr len :float)
                    :timeline (vp/arr len :long)})
      (swap! *buffers conj slice)
      (vp/address slice))))

#_(defn -shared
    []
    ;; https://github.com/ShirasawaSama/JavaSharedMemory/blob/master/src/main/java/cn/apisium/shm/impl/MmapSharedMemory.java
    (require '[vybe.flecs.c :as vf.c])
    (import '(org.vybe.flecs flecs))
    (let [O_RDWR 0x0002
          O_CREAT 0x00000200
          O_EXCL 0x00000800
          S_IRUSR 00400
          S_IWUSR 00200
          fd (let [fd (-> (org.vybe.flecs.flecs_1$shm_open/makeInvoker
                           (into-array java.lang.foreign.MemoryLayout [(vp/type->layout :int)]))
                          (.apply (vp/mem "/tmp_vybe100")
                                  (bit-or (int O_RDWR)
                                          (int O_CREAT)
                                          #_(int O_EXCL))
                                  (into-array Object
                                              [(int (bit-or S_IRUSR S_IWUSR))])))]
               (when (= (eval `(vf.c/ftruncate ~fd (* 1024 1024 128))) -1)
                 (println :ftruncate-warn))
               fd)
          p-buf (-> (eval `(vf.c/mmap vp/null
                                      (* 1024 1024 128)
                                      (bit-or (flecs/PROT_READ) (flecs/PROT_WRITE))
                                      (flecs/MAP_SHARED)
                                      ~fd
                                      0))
                    (vp/reinterpret (* 1024 1024 128)))]

      (-plugin p-buf)))

(comment

  (audio-enable!)

  ())

#_ (vp/arr (:arr (nth @*buffers 0)) 64 :float)

#_(.getAtIndex lala
               ^java.lang.foreign.ValueLayout$OfFloat (vp/type->layout :float)
               0)
#_(.setAtIndex lala
               ^java.lang.foreign.ValueLayout$OfFloat (vp/type->layout :float)
               0
               (float 0.9))

#_(System/getProperty "java.class.path")
#_(-> (java.lang.management.ManagementFactory/getRuntimeMXBean)
      .getInputArguments)
