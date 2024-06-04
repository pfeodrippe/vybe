(ns vybe.type
  #_(:require
   [potemkin.types :as pt]
   [potemkin :refer [def-map-type]]
   [clojure.pprint :as pp]
   [vybe.panama :as vp]))

(set! *warn-on-reflection* true)

(defprotocol IVybeName
  (vybe-name [e]))

(defprotocol IResolveComponent
  (resolve-component [_ world id p]))

(defprotocol IComponentData
  (component-data [struct-type]
    "Get component data from a JNR struct type."))
