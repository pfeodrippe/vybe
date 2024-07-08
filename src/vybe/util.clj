(ns vybe.util
  (:require
   [clojure.string :as str]))

(defonce *state (atom {:debug false}))
#_(defonce *state (atom {:debug true}))
#_ (swap! *state assoc :debug true)
#_ (swap! *state assoc :debug false)

(defn debug-set!
  [v]
  (swap! *state assoc :debug v))

(defmacro debug
  [& strs]
  `(when (:debug @*state)
     (println (str "[Vybe] - " (str/join " " [~@strs])))))
