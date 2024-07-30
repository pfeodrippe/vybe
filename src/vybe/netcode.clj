(ns vybe.netcode
  (:require
   [vybe.netcode.c :as vn.c])
  (:import
   (org.vybe.netcode netcode netcode$netcode_init
                     netcode$netcode_term)))

(defn init!
  "Initiate netcode."
  []
  (-> (netcode$netcode_init/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (into-array Object []))))
#_ (init)

(defn close!
  "Finish netcode."
  []
  (-> (netcode$netcode_term/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (into-array Object []))))
#_ (close)
