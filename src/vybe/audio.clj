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
   [vybe.panama :as vp]))

(defonce ^:private *audio-enabled? (atom false))

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

(defn audio-enable!
  "Enable overtone audio. You need to call this before using `sound` or
  you will need to reeval the places where `sound` is used.

  `(require '[overtone.core :refer :all])` will be called and you will have
  the overtone vars available on your namespace."
  []
  (try
    #_(ov.conn/scsynth-path)
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

;; ---------------- EXPERIMENTAL
(defonce *buffers (atom []))
#_ (hash @*buffers)

(vp/defcomp VybeSlice
  [[:len :long]
   [:arr :pointer]
   [:timeline :pointer]])

(defn -plugin
  []
  (let [len 88000
        slice (VybeSlice {:len len
                          :arr (vp/arr len :float)
                          :timeline (vp/arr len :long)})]
    (swap! *buffers conj slice)
    (vp/address slice)))

(comment

  (audio-enable!)

  ;; https://github.com/ShirasawaSama/JavaSharedMemory/blob/master/src/main/java/cn/apisium/shm/impl/MmapSharedMemory.java
  (do
    (def O_RDWR 0x0002)
    (def O_CREAT 0x00000200)
    (def O_EXCL 0x00000800)
    (def S_IRUSR 00400)
    (def S_IWUSR 00200)

    (require '[vybe.flecs.c :as vf.c])
    (import '(org.vybe.flecs flecs))

    (vp/defcomp VybeSlice2
      [[:len :long]]))

  (def fd
    (-> (org.vybe.flecs.flecs_1$shm_open/makeInvoker
         (into-array java.lang.foreign.MemoryLayout [(vp/type->layout :int)]))
        (.apply (vp/mem "/tmp/vybe")
                (bit-or (int O_RDWR)
                        (int O_CREAT)
                        #_(int O_EXCL))
                (into-array Object
                            [(int (bit-or S_IRUSR S_IWUSR))]))))

  (def p-buf
    (vf.c/mmap vp/null
               1024
               (bit-or (flecs/PROT_READ) (flecs/PROT_WRITE))
               (flecs/MAP_SHARED)
               fd
               0))

  (vp/set-mem p-buf (VybeSlice2 {:len Long/MAX_VALUE}))

  (vp/p->map (vp/reinterpret p-buf (.byteSize (.layout VybeSlice2)))
             VybeSlice2)

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
