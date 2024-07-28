(ns vybe.game-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.game :as vg]
   [vybe.flecs :as vf]))

(deftest sanity-test
  (testing "if we get here, it means all needed vars are loaded"
    (is (= 0 0))))

(deftest resource-test
  (is (some? (-> (vg/shader-program (vf/make-world) :ddd "shaders/shadowmap.vs" "shaders/shadowmap.fs")
                 :ddd))))
