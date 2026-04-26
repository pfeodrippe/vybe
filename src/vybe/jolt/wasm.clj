(ns vybe.jolt.wasm
  (:require
   [vybe.wasm :as vw]))

(defonce ^:private contact-callback-handler* (atom nil))

(defn set-contact-callback-handler!
  [handler]
  (reset! contact-callback-handler* handler)
  nil)

(defn- msync-host-function
  []
  (vw/host-function {:name "_msync_js"
                     :params [:i32 :i32 :i32 :i32 :i32 :i64]
                     :results [:i32]
                     :f (fn [_ _] (vw/zero-result))}))

(defn- contact-host-function
  [{:keys [name params results event]}]
  (vw/host-function
   {:name name
    :params params
    :results results
    :f (fn [_ args]
         (if-let [handler @contact-callback-handler*]
           (or (handler event args)
               (if (seq results)
                 (vw/zero-result)
                 (vw/empty-result)))
           (throw (ex-info "No Jolt contact callback handler installed"
                           {:event event
                            :args (vec args)}))))}))

(defn- contact-host-functions
  []
  [(contact-host-function
    {:name "vybe_jolt_contact_validate"
     :event :validate
     :params [:i32 :i32 :i32 :i32 :i32]
     :results [:i32]})
   (contact-host-function
    {:name "vybe_jolt_contact_added"
     :event :added
     :params [:i32 :i32 :i32 :i32 :i32]})
   (contact-host-function
    {:name "vybe_jolt_contact_persisted"
     :event :persisted
     :params [:i32 :i32 :i32 :i32 :i32]})
   (contact-host-function
    {:name "vybe_jolt_contact_removed"
     :event :removed
     :params [:i32 :i32]})])

(defn load-module
  []
  (vw/load-module
   {:resource "vybe/wasm/jolt.wasm"
    :host-functions (into [(vw/unwind-raise-exception-host-function)
                           (vw/emscripten-notify-memory-growth-host-function)
                           (msync-host-function)]
                          (contact-host-functions))
    :after-init vw/set-default-module!}))
