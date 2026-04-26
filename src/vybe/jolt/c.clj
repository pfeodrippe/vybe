(ns vybe.jolt.c
  (:require
   [vybe.native.backend :as backend]))

(set! *warn-on-reflection* true)

(if (backend/wasm?)
  (do
    (require 'vybe.jolt.wasm-c)
    (doseq [[sym v] (ns-publics 'vybe.jolt.wasm-c)]
      (let [target (intern *ns* sym @v)]
        (alter-meta! target merge (meta v)))))
  (do
    (require 'vybe.jolt.impl)
    ((requiring-resolve 'vybe.jolt.impl/intern-methods))))

(comment

  ())
