(ns vybe.netcode-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.panama :as vp]))

(when-not vp/linux?
  ;; In the CI for Linux, we have some linker issue.
  (eval
   '(do
      (require '[vybe.netcode :as vn])
      (deftest init-close-test
        (is (= 1 (vn/init!)))
        (is (= nil (vn/close!)))))))
