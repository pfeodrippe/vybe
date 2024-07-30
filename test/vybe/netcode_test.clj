(ns vybe.netcode-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.netcode :as vn]))

(deftest init-close-test
  (is (= 1 (vn/init!)))
  (is (= nil (vn/close!))))
