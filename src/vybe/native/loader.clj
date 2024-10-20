(ns vybe.native.loader
  "This namespace is just for copying the dynamic libs into a temporary folder
  (as a side-effect of importing some namespaces).

  It should be run at least once before starting the app for the first time."
  (:require
   [clojure.tools.build.api :as b]
   [vybe.panama :as vp]))

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
(vp/-copy-lib! "zig_vybe")

;; -- Netcode.
(vp/-copy-lib! "vybe_cutenet")

;; -- Prebuilt libs for SC from the Sonic Pi repo.
(vp/-copy-resource! "vybe-sc-prebuilt.zip")
;; Unzip the lib into `native`.
(b/unzip {:zip-file "native/vybe-sc-prebuilt.zip"
          :target-dir "native"})

(defn -main
  [& _args]
  #_bogus)
