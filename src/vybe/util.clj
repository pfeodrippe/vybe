(ns vybe.util
  (:require
   [clojure.string :as str]))

(defonce prd?
  (or (= (System/getenv "VYBE_PROD") "true")
      (= (System/getProperty "VYBE_PROD") "true")))

(defonce *state (atom {:debug false}))
#_(defonce *state (atom {:debug true}))
#_ (swap! *state assoc :debug true)
#_ (swap! *state assoc :debug false)

(defn debug-set!
  [v]
  (swap! *state assoc :debug v))

(defmacro debug
  "Print vybe debug message.

  It can be enabled by calling `debug-set!` only when the env var or jvm prop
  VYBE_PROD is not set to \"true\", otherwise it just returns `nil`."
  [& strs]
  (when-not prd?
    `(when (:debug @*state)
       (println (str "[Vybe] - " (str/join " " [~@strs]))))))

(defmacro if-prd
  "Runs `prd-body`, it can be enabled by calling `debug-set!`."
  [prd-body else-body]
  (if prd?
    `(do ~prd-body)
    `(if (:debug @*state)
       (do ~else-body)
       (do ~prd-body))))

(defonce *commands (atom []))

(defn enqueue-command!
  "Receives a zero-arity function that will be run before the next draw
  call."
  [f]
  (swap! *commands conj f))

(defonce *probe (atom {}))

(defn counter!
  "Used for debugging."
  [k]
  (swap! *probe update-in [::counter k] (fnil inc 0)))
#_ (counter! :a)
