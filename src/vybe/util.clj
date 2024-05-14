(ns vybe.util
  (:require
   [jsonista.core :as json]))

(defn dev-mode?
  []
  true)

(def ^:private obj-mapper
  (json/object-mapper {:decode-key-fn true}))

(defn parse-json
  [v]
  (json/read-value v obj-mapper))
