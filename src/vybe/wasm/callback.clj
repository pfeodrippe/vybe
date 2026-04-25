(ns vybe.wasm.callback
  (:require
   [vybe.wasm.runtime :as rt]))

(defn registry
  "Create an atom-backed callback registry."
  []
  (atom {:next-id 1
         :callbacks {}}))

(defn register!
  "Register callback `f` and return its numeric id."
  [registry f]
  (let [id (:next-id (swap! registry
                            (fn [{:keys [next-id] :as state}]
                              (-> state
                                  (update :next-id inc)
                                  (assoc-in [:callbacks next-id] f)))))]
    (dec id)))

(defn unregister!
  "Remove callback `id` from `registry`."
  [registry id]
  (swap! registry update :callbacks dissoc id)
  nil)

(defn callback
  "Return callback `id` from `registry`, or nil."
  [registry id]
  (get-in @registry [:callbacks id]))

(defn host-function
  "Create a host function that dispatches raw Wasm args to `f`."
  [{:keys [name params results f]
    :or {params [:i32]
         results []}}]
  (rt/host-function {:name name
                     :params params
                     :results results
                     :f f}))
