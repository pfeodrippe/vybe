(ns vybe.c-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.c :as vc]))

(vc/defn* simple :- :int
  [v :- :int]
  (* v 4))

(deftest simple-test
  (is (= 220
         (simple 55))))
