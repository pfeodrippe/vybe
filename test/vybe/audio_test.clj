(ns vybe.audio-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.audio :as va]
   matcher-combinators.test))

(va/audio-enable!)

(deftest check-audio-test
  (is (match?
       {:id some?}
       (into {} (va/sound (demo 0.2 (sin-osc 400)))))))
