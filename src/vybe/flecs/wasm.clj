(ns vybe.flecs.wasm
  (:require
   [vybe.wasm :as vw]))

(defonce ^:private system-callback-handler* (atom nil))

(defn set-system-callback-handler!
  "Install a Flecs system callback handler.

  The handler is called with `[it-ptr callback-ctx]`, where both values are
  wasm32 pointer offsets represented as JVM longs."
  [handler]
  (reset! system-callback-handler* handler)
  nil)

(defn- system-callback-host-function
  [name]
  (vw/host-function
   {:name name
    :params [:i32 :i32]
    :f (fn [_instance args]
         (if-let [handler @system-callback-handler*]
           (handler (aget args 0) (aget args 1))
           (throw (ex-info "No Flecs system callback handler installed"
                           {:it (aget args 0)
                            :callback-ctx (aget args 1)})))
         (vw/empty-result))}))

(defn- errno-host-function
  [name params]
  (vw/host-function
   {:name name
    :params params
    :results [:i32]
    :f (fn [_instance _args]
         (long-array [-1]))}))

(defn- host-functions
  []
  [(vw/unwind-raise-exception-host-function)
   (system-callback-host-function "vybe_flecs_system_callback")
   (system-callback-host-function "_vybe_flecs_system_callback")
   (errno-host-function "getnameinfo" [:i32 :i32 :i32 :i32 :i32 :i32 :i32])
   (errno-host-function "__syscall_accept4" [:i32 :i32 :i32 :i32 :i32 :i32])
   (errno-host-function "__syscall_bind" [:i32 :i32 :i32 :i32 :i32 :i32])
   (errno-host-function "__syscall_listen" [:i32 :i32 :i32 :i32 :i32 :i32])
   (errno-host-function "__syscall_recvfrom" [:i32 :i32 :i32 :i32 :i32 :i32])
   (errno-host-function "__syscall_sendto" [:i32 :i32 :i32 :i32 :i32 :i32])
   (errno-host-function "__syscall_socket" [:i32 :i32 :i32 :i32 :i32 :i32])
   (vw/emscripten-notify-memory-growth-host-function)])

(defn load-module
  "Load `vybe/wasm/flecs.wasm` and initialize Flecs OS defaults."
  []
  (vw/load-module
   {:resource "vybe/wasm/flecs.wasm"
    :host-functions (host-functions)
    :initialize? true
    :after-init (fn [module]
                  (vw/set-default-module! module)
                  (vw/call module "ecs_os_set_api_defaults")
                  (vw/call module "ecs_os_init"))}))
