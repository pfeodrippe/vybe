(ns vybe.protocol)

(defprotocol IAddComponent
  (-attach-entity [this e]))

(defprotocol IClassable
  (-to-class [this])
  (-to-class-obj [this])
  (-to-original [this]))
