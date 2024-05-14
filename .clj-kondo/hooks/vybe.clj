(ns hooks.vybe
  (:require [clj-kondo.hooks-api :as api]))

(defn with-each [{:keys [node]}]
  (let [[w binding-vec & body] (rest (:children node))
        new-node (api/list-node
                  (list*
                   (api/token-node 'let)
                   binding-vec
                   w
                   body))]
    {:node new-node}))
