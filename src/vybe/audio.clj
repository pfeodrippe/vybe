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

(defn- init-synths! []
  (defonce directional
    (synth-load (vy.u/app-resource "com/pfeodrippe/vybe/overtone/directional.scsyndef")))

  (defsynth noise-wind
    [freq 300, mul 0.5, out_bus 0]
    (out out_bus
         (let [noise (-> (pink-noise)
                         (lpf 500)
                         (hpf 1000)
                         (* 0.6))]
           #_[noise noise]
           noise)))

  (defsynth alarm
    [out_bus 0]
    (let [v (-> (* (sin-osc (* 800 (lf-saw 240))))
                (lpf 600)
                (hpf 300)
                (* (lf-pulse 2 :width 0.2))
                (* 0.02))]
      (out out_bus v))))

#_(stop)

(defn audio-enable!
  "Enable overtone audio. You need to call this before using `sound` or
  you will need to reeval the places where `sound` is used."
  []
  (try
    #_(ov.conn/scsynth-path)
    #_(require '[overtone.core :refer :all])
    (when-not @*audio-enabled?
      (boot-server)
      (init-synths!)
      (reset! *audio-enabled? true))
    (catch Exception e#
      (println e#)
      (println "\n\n ----- WARNING -----\nIf you want audio working, download SuperCollider at\nhttps://supercollider.github.io/downloads.html"))))
#_(audio-enable!)

(defmacro sound
  "Macro used to wrap audio calls so we can use it safely for users who
  have overtone installed."
  [& body]
  `(when @*audio-enabled?
     #_(eval '(require '[overtone.core :refer :all]))
     ~@body))

;; -- Ambisonic.
(defn- -ambisonic
  [sound-source source-transform target-transform]
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
              (/ 1 (* d d)))]
    (sound
      (ctl sound-source :azim azim :elev elev :amp (min (* amp 30) 30) :distance d))))

(defonce ^:private *directional-nodes (atom {}))
#_ (stop)

(defn directional-node
  "Create/get a directional node providing the world, the entity
  and the synth."
  [w e syn]
  (let [e-id (vf/eid w e)
        w-addr (vp/address (vf/get-world w))]
    (or (when-let [node (get-in @*directional-nodes [w-addr e-id syn])]
          (when (not= (node-status node) :destroyed)
            node))
        (let [identifier (fn [s] (str s "-" w-addr "-" e-id ))
              main-g (group (identifier "main"))
              early-g (group (identifier "early") :head main-g)
              later-g (group (identifier "latecomers") :after early-g)
              directional-bus (audio-bus 1)
              _ (syn [:tail early-g] :out_bus directional-bus)
              dir (directional [:tail later-g] :in directional-bus :out_bus 0)]
          #_(fx-reverb 0)
          (swap! *directional-nodes assoc-in [w-addr e-id syn] dir)))))

;; -- Systems.
(vf/defsystem update-sound-sources _w
  [source-transform [vt/Transform :global]
   {syn :v} [vt/Clj :*]
   _ [:src '?e :vg/camera-active]
   target-transform [:src '?e [vt/Transform :global]]
   e :vf/entity
   w :vf/world]
  (sound (-ambisonic (directional-node w e syn) source-transform target-transform)))

(defn systems
  [w]
  [(update-sound-sources w)])

















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
