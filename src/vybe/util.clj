(ns vybe.util
  (:require
   [clojure.string :as str]))

(defonce *state (atom {:debug false}))
#_ (swap! *state assoc :debug true)

(defmacro debug
  [& strs]
  `(when (:debug @*state)
     (println (str "[Vybe] - " (str/join " " [~@strs])))))
