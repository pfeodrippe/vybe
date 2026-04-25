(ns vybe.flecs.ids
  (:require
   [vybe.flecs.abi :as abi]
   [vybe.flecs.wasm-c :as c]))

(defn- exported-constant
  [name]
  (try
    (c/raw-global name)
    (catch Exception _
      nil)))

(defn- const
  [name]
  (or (abi/const-value name)
      (exported-constant name)
      (throw (ex-info "Missing generated Flecs constant"
                      {:constant name}))))

(defn- intern-constant!
  [name]
  (intern *ns* (symbol name) (fn [] (const name))))

(doseq [name (concat (keys (:constants (abi/abi)))
                     (:extern-constants (abi/abi))
                     (:wasm-globals (abi/abi)))]
  (when name
    (intern-constant! name)))

(defn FlecsRestImport$address [] 0)
(defn FlecsStatsImport$address [] 0)
