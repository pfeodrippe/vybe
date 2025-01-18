(ns vybe.game-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.panama :as vp]))

(when-not vp/linux?
  ;; In the CI for Linux, we have some linker issue.
  (require 'vybe.game))

(deftest sanity-test
  (testing "If we reach here, it means all needed vars are loaded correctly"
    (is (= 0 0))))
