(ns vybe.flecs.c
  (:require
   [vybe.flecs.wasm-c]))

(set! *warn-on-reflection* true)

(doseq [[sym v] (ns-publics 'vybe.flecs.wasm-c)]
  (let [target (intern *ns* sym @v)]
    (alter-meta! target merge (meta v))))

(comment

  ())
