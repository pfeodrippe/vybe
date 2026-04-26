(ns vybe.jolt.abi
  (:require
   [vybe.wasm :as vw]
   [vybe.wasm.abi :as abi]))

(defonce ^:private abi* (abi/load-abi "vybe/wasm/jolt_abi.edn"))

(defn abi [] (abi/abi abi*))
(defn layout-data [c-type] (abi/layout-data abi* "jolt" c-type))
(defn sizeof [c-type] (abi/sizeof abi* "jolt" c-type))
(defn offsetof [c-type field] (abi/offsetof abi* "jolt" c-type field))
(defn const-value [name] (abi/const-value abi* name))
(defn function-data [name] (abi/function-data abi* "jolt" name))
(defn function-desc [name] (abi/function-desc abi* "jolt" name))

(declare component)

(defn layout
  ([c-type]
   (layout (symbol "vybe.jolt.abi" (name c-type)) c-type))
  ([name c-type]
   (abi/layout abi* "jolt" component name c-type)))

(def component
  (memoize
   (fn [c-type]
     (let [name (symbol "vybe.jolt.abi" (name c-type))]
       (vw/make-component name (layout name c-type))))))

(abi/intern-constant-functions! *ns* abi*)
