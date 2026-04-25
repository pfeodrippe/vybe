(ns vybe.native.backend
  (:require
   [clojure.string :as str]))

(defn current
  "Return the configured native backend keyword.

  Resolution order:
  - `vybe.native.backend` JVM property.
  - `VYBE_NATIVE_BACKEND` environment variable.
  - `:wasm` when neither is set.

  Supported values are `:panama` and `:wasm`."
  []
  (let [raw (or (System/getProperty "vybe.native.backend")
                (System/getenv "VYBE_NATIVE_BACKEND")
                "wasm")
        backend (-> raw str/lower-case keyword)]
    (case backend
      (:panama :wasm) backend
      (throw (ex-info "Unsupported Vybe native backend"
                      {:backend raw
                       :supported #{:panama :wasm}})))))

(defn panama?
  "Return true when the Panama/native dynamic library backend is active."
  []
  (= :panama (current)))

(defn wasm?
  "Return true when the JVM-hosted WebAssembly backend is active."
  []
  (= :wasm (current)))
