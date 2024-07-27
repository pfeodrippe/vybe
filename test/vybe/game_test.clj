(ns vybe.game-test
  (:require
   [clojure.test :refer [deftest testing is]]
   vybe.game))

(deftest sanity-test
  (testing "if we get here, it means all needed vars are loaded"
    (is (= 0 0))))
