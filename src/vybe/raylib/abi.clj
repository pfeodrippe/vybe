(ns vybe.raylib.abi
  (:require
   [vybe.wasm :as vw]
   [vybe.wasm.abi :as abi]))

(defonce ^:private abi* (abi/load-abi "vybe/wasm/raylib_abi.edn"))

(defn abi [] (abi/abi abi*))
(defn layout-data [c-type] (abi/layout-data abi* "raylib" c-type))
(defn sizeof [c-type] (abi/sizeof abi* "raylib" c-type))
(defn offsetof [c-type field] (abi/offsetof abi* "raylib" c-type field))
(defn const-value [name] (abi/const-value abi* name))
(defn function-data [name] (abi/function-data abi* "raylib" name))
(defn function-desc [name] (abi/function-desc abi* "raylib" name))
(defn wasm-imports [] (abi/wasm-imports abi*))

(declare component)

(defn layout
  ([c-type]
   (layout (symbol "vybe.raylib.abi" (name c-type)) c-type))
  ([name c-type]
   (abi/layout abi* "raylib" component name c-type)))

(def component
  (memoize
   (fn [c-type]
     (let [name (symbol "vybe.raylib.abi" (name c-type))]
       (vw/make-component name (layout name c-type))))))

(abi/intern-constant-functions! *ns* abi*)
