(ns vybe.jolt.wasm
  (:require
   [vybe.wasm :as vw]))

(defn- msync-host-function
  []
  (vw/host-function {:name "_msync_js"
                     :params [:i32 :i32 :i32 :i32 :i32 :i64]
                     :results [:i32]
                     :f (fn [_ _] (vw/zero-result))}))

(defn load-module
  []
  (vw/load-module
   {:resource "vybe/wasm/jolt.wasm"
    :host-functions [(vw/unwind-raise-exception-host-function)
                     (vw/emscripten-notify-memory-growth-host-function)
                     (msync-host-function)]
    :after-init vw/set-default-module!}))
