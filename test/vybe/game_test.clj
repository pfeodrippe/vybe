(ns vybe.game-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.panama :as vp]
   vybe.netcode))

(when-not vp/linux?
  ;; In the CI for Linux, we have some linker issue.
  (require 'vybe.game))

(deftest sanity-test
  (testing "if we get here, it means all needed vars are loaded"
    (is (= 0 0))))
