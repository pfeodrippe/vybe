(ns vybe.netcode.c)

(set! *warn-on-reflection* true)

(defonce ^:private server-id* (atom 0))

(defn- next-server-id []
  (swap! server-id* inc))

(defn- success []
  {:code 0 :details nil})

(defn- failure [details]
  {:code 1 :details details})

(defn- random-bytes [n]
  (let [bytes (byte-array n)]
    (.nextBytes (java.security.SecureRandom.) bytes)
    (vec bytes)))

(defn cn-crypto-generate-key []
  {:key (random-bytes 32)})

(defn cn-crypto-sign-keygen [public-key secret-key]
  (assoc public-key :key (random-bytes 32))
  (assoc secret-key :key (random-bytes 64))
  nil)

(defn cn-endpoint-init [endpoint address-and-port]
  (let [[_ _host port] (re-matches #"^(.+?)(?::([0-9]+))?$"
                                   (str address-and-port))]
    (assoc endpoint :type 1)
    (assoc endpoint :port (short (parse-long (or port "0"))))
    0))

(defn cn-server-config-defaults []
  {:application_id 0
   :max_incoming_bytes_per_second 0
   :max_outgoing_bytes_per_second 0
   :connection_timeout 0
   :resend_rate 0.0
   :public_key nil
   :secret_key nil
   :user_allocator_context 0})

(defn cn-server-create [server-config]
  (atom {:id (next-server-id)
         :config server-config
         :running? false
         :clients #{}}))

(defn cn-server-start [server address-and-port]
  (if server
    (do (swap! server assoc :running? true :address address-and-port)
        (success))
    (failure "server is nil")))

(defn cn-is-error [result]
  (not (zero? (long (or (:code result) 0)))))

(defn cn-server-destroy [server]
  (when (instance? clojure.lang.IAtom server)
    (swap! server assoc :running? false))
  nil)

(defn cn-server-is-client-connected [server client-index]
  (boolean (contains? (:clients @server) client-index)))

(defn cn-server-send [& _] (success))
(defn cn-client-state-get [& _] 0)
(defn cn-client-send [& _] (success))
(defn cn-server-update [& _] nil)
(defn cn-server-pop-event [& _] false)
(defn cn-server-free-packet [& _] nil)
(defn cn-client-update [& _] nil)
(defn cn-client-pop-packet [& _] false)
(defn cn-client-free-packet [& _] nil)
(defn cn-generate-connect-token [& _] (success))
(defn cn-client-create [& _]
  (atom {:state 0}))
(defn cn-client-connect [client _connect-token]
  (swap! client assoc :state 3)
  (success))
(defn cn-client-disconnect [client]
  (swap! client assoc :state 0)
  nil)
(defn cn-client-destroy [_client] nil)
(defn cn-server-set-public-ip [server address]
  (swap! server assoc :public-address address)
  nil)

(comment

  ())
