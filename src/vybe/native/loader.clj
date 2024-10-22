(ns vybe.native.loader
  "This namespace is just for copying the dynamic libs into a temporary folder
  (as a side-effect of importing some namespaces).

  It should be run at least once before starting the app for the first time."
  (:require
   [clojure.tools.build.api :as b]
   [clojure.java.io :as io]
   [vybe.panama :as vp]
   [vybe.util :as vy.u]))

(vy.u/debug-set! true)

;; -- Jolt.
(vp/-copy-lib! "joltc_zig")
;; In windows, we build Jolt differently.
(when-not vp/windows?
  (vp/-copy-lib! "vybe_jolt"))

;; -- Raylib.
(vp/-copy-lib! "raylib")
(vp/-copy-lib! "vybe_raylib")

;; -- Flecs.
(vp/-copy-lib! "vybe_flecs")
#_(vp/-copy-lib! "zig_vybe")

;; -- Netcode.
(vp/-copy-lib! "vybe_cutenet")

;; -- Prebuilt libs for SC from the Sonic Pi repo.
(vp/-copy-resource! "vybe/native/vybe-sc-prebuilt.zip"
                    "vybe-sc-prebuilt.zip")
;; Unzip the lib into `vybe_native`.
(b/unzip {:zip-file (str "vybe_native" java.io.File/separator "vybe-sc-prebuilt.zip")
          :target-dir "vybe_native"})
(when vp/mac?
  (let [file (io/file "vybe_native/macos/universal/supercollider/Resources/scsynth")
        path (.getAbsolutePath file)]
    (vy.u/debug "making scsynth executable"
                {:output (b/process {:command-args ["chmod" "+x" path]})})
    path))
#_(println :VYBE_LOG (file-seq (io/file "vybe_native")))

(defn -main
  [& _args]
  #_bogus)
