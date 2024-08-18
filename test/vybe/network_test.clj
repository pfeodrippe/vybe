(ns vybe.network-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.panama :as vp]))

(when-not vp/linux?
  ;; In the CI for Linux, we have some linker issue.
  (eval
   '(do
      (require '[vybe.network :as vn])
      (deftest init-test
        (let [server-address "127.0.0.1"
              application-id 2000
              [public-key secret-key] (vn/-cn-gen-keys)
              server (vn/-cn-server server-address application-id public-key secret-key)]
          (is (some? server))
          (is (nil? (vn/-cn-server-destroy server))))))))
