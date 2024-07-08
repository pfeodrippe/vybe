(ns vybe.type)

(set! *warn-on-reflection* true)

(defprotocol IVybeName
  (vybe-name [e]))
