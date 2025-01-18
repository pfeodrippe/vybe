(ns vybe.util-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer [deftest testing is]]
   [vybe.util :as vy.u]))

(deftest app-resource-test
  (is (.exists (io/file (vy.u/app-resource "jacob folder/file.txt")))))
