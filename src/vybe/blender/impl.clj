(ns vybe.blender.impl)

;; This will contain the eval function from basilisp when
;; we start the REPL from Blender.
;; See `vybe/basilisp/blender.lpy`.
;; When set, this function has 2 arities:
;;   - [form-str], that evaluates and returns a string
;;   - [form-str out], that calls `out` with the evaluated result
(defonce *basilisp-eval (atom nil))
#_ (@*basilisp-eval "3" (fn [v]
                          (println (+ v 4))))
