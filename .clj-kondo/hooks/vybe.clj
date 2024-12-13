(ns hooks.vybe
  (:require [clj-kondo.hooks-api :as api]))

(defn with-query [{:keys [node]}]
  (let [[w binding-vec & body] (rest (:children node))
        body-node (api/list-node
                  (list*
                   (api/token-node 'let)
                   binding-vec
                   w
                   body))]
    {:node body-node}))

(defn defquery [{:keys [node]}]
  (let [[name w binding-vec & body] (rest (:children node))
        body-node (api/list-node
                  [(api/token-node 'let)
                   binding-vec
                   (api/list-node
                    (list*
                     (api/token-node 'let)
                     (api/vector-node [w (api/map-node {})])
                     body))])]
    {:node (api/list-node
            [(api/token-node 'def) name
             body-node])}))
