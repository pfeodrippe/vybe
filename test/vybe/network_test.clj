(ns vybe.network-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.panama :as vp]))

(when-not vp/linux?
  ;; In the CI for Linux, we have some linker issue.
  (eval
   '(do
      (require '[vybe.network :as vn])
      (deftest bogus-test
        (is (= 1 1))))))
