(ns vybe.jolt.c)

(set! *warn-on-reflection* true)

(require 'vybe.jolt.wasm-c)

(doseq [[sym v] (ns-publics 'vybe.jolt.wasm-c)]
  (let [target (intern *ns* sym @v)]
    (alter-meta! target merge (meta v))))

(comment

  ())
