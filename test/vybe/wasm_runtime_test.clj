(ns vybe.wasm-runtime-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [vybe.wasm.runtime :as wrt])
  (:import
   (com.dylibso.chicory.compiler InterpreterFallback)))

(def ^:private answer-wasm
  (byte-array
   (map unchecked-byte
        [0x00 0x61 0x73 0x6d 0x01 0x00 0x00 0x00
         0x01 0x05 0x01 0x60 0x00 0x01 0x7f
         0x03 0x02 0x01 0x00
         0x07 0x0a 0x01 0x06 0x61 0x6e 0x73 0x77 0x65 0x72 0x00 0x00
         0x0a 0x06 0x01 0x04 0x00 0x41 0x2a 0x0b])))

(deftest compiled-runtime-fails-instead-of-interpreting
  (testing "the central Chicory runtime is configured to reject interpreter fallback"
    (is (identical? InterpreterFallback/FAIL
                   @#'wrt/compiled-interpreter-fallback)))
  (testing "module loading goes through the compiled runtime path"
    (let [module (wrt/load-module-from-bytes answer-wasm {:initialize? false})]
      (is (= 42 (wrt/call module "answer"))))))
