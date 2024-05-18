(ns vybe.native.loader
  "This namespace is just for copying the dynamic libs to the right place.

  It should be run at least once before starting the app."
  (:require
   vybe.raylib.impl
   vybe.flecs.impl))

(defn -main
  [& _args]
  #_bogus)
