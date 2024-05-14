(ns pfeodrippe.main
  (:gen-class))

(defn -main
  [& _args]
  (binding [*ns* *ns*]
    (require 'pfeodrippe.healthcare)
    ((resolve 'pfeodrippe.healthcare/start-game))))
