(ns vybe.native.loader
  "This namespace is just for copying the dynamic libs into a temporary folder
  (as a side-effect of importing some namespaces).

  It should be run at least once before starting the app for the first time."
  (:require
   vybe.raylib.impl
   vybe.flecs.impl
   vybe.jolt.impl))

(defn -main
  [& _args]
  #_bogus)
