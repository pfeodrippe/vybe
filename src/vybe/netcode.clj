(ns vybe.netcode
  (:require
   [vybe.netcode.c :as vn.c]
   [vybe.panama :as vp]
   [aleph.udp :as udp]
   [clj-commons.byte-streams :as bs]
   [manifold.stream :as s]
   [clojure.string :as str]
   [clojure.pprint :as pp])
  (:import
   (org.vybe.netcode netcode netcode$netcode_init netcode$netcode_term
                     netcode_server_config_t netcode_client_config_t)))

(defn put!
  [{:vn/keys [host port client-id *state]} msg]
  (println {::put {:msg msg :client-id client-id}})
  (s/put! (:vn/socket @*state)
          {:host host
           :port port
           :message msg}))

(defn session-msg
  [session-id num-of-players]
  (str "rs:" session-id ":" num-of-players))

(defn client-msg
  [session-id client-id]
  (str "rc:" client-id ":" session-id))

(defn make-socket
  [callback]
  (let [socket @(udp/socket {})]
    (->> socket (s/consume callback))
    socket))

(declare puncher-consumer)

(defn puncher-socket!
  "Close actual socket (if any) and create a new one."
  [{:vn/keys [*state] :as puncher}]
  (let [socket (make-socket #(puncher-consumer puncher %))]
    (swap! *state (fn [state]
                    (some-> (:vn/socket state) s/close!)
                    (merge state {:vn/socket socket}))))
  puncher)

(defn puncher-consumer
  [{:vn/keys [session-id client-id is-host *state] :as puncher}
   {:keys [message]}]
  (let [msg (bs/to-string message)
        {:vn/keys [is-server-found]}  @*state]
    (println {::received {:msg msg :client-id client-id}})

    ;; ok message
    (when (str/starts-with? msg "ok")
      (swap! *state merge (let [[_ port-str] (str/split msg #":")]
                            {:vn/own-port (Long/parseLong port-str)
                             :vn/is-server-found true}))
      (when (and is-host (not is-server-found))
        (puncher-socket! puncher)
        (put! puncher (client-msg session-id client-id))))

    ;; peers message
    (when (and (not (:vn/is-peer-info-received *state))
               (str/starts-with? msg "peers"))
      #_(s/close! (:vn/socket @*state))
      #_(swap! *state merge
               {:vn/is-peer-info-received true
                :vn/socket @(udp/socket {})}))))

(defn make-hole-puncher
  "For `host`, don't use static ip, use machine's public IP.

  All the keys are required, the optional ones are:
    - `:is-host`
    - `:num-of-players`"
  [host port {:keys [is-host session-id client-id num-of-players]
              :or {num-of-players 2
                   is-host false}}]
  {:pre [(some? host) (some? port) (some? session-id) (some? client-id)]}
  (let [puncher {:vn/host host
                 :vn/port port
                 :vn/is-host is-host
                 :vn/session-id session-id
                 :vn/client-id client-id
                 :vn/num-of-players num-of-players
                 :vn/*state (atom {:vn/socket nil})}]
    (puncher-socket! puncher)
    (if is-host
      (put! puncher (session-msg (:vn/session-id puncher) (:vn/num-of-players puncher)))
      (put! puncher (client-msg (:vn/session-id puncher) (:vn/client-id puncher))))
    puncher))

(comment

  (defonce *acc (atom 50))

  (let [session-id (str "gamecode" @*acc)
        client-ids [(str @*acc "20")
                    (str @*acc "21")]
        _ (swap! *acc inc)
        server-ip "147.182.133.53"
        server-port 8080
        host-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                               :client-id (first client-ids)
                                                               :num-of-players 2
                                                               :is-host true})
        client-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                                 :client-id (second client-ids)})]
    [host-puncher client-puncher])

  ())

(comment

  (do
    (def server-port 10002)

    (def client-socket @(udp/socket {:port 10003}))

    (defn send-metric!
      "This encodes a message in the typical statsd format, which is two strings, `metric` and
   `value`, delimited by a colon."
      [metric ^long value]
      (s/put! client-socket
              {:host "147.182.133.53"
               :port server-port
               ;; The UDP contents can be anything which byte-streams can coerce to a byte-array.  If
               ;; the combined length of the metric and value were to exceed 65536 bytes, this would
               ;; fail, and `send-metrics!` would return a deferred value that yields an error.
               :message (str metric ":" value)})))

  (s/put! server-socket
          {:host "167.172.0.184"
           :port 8080
           :message "abc 30"})

  (defn parse-statsd-packet
    "This is the inverse operation of `send-metrics!`, taking the message, splitting it on the
   colon delimiter, and parsing the `value`."
    [{:keys [message]}]
    (let [message        (bs/to-string message)
          [metric value] (str/split message #":")]
      [metric (Long/parseLong value)]))

  (->> client-socket
       (s/consume
        (fn [{:keys [message]}]
          (println :MES (bs/to-string message)))))

  (s/close! client-socket)

  (defn start-statsd-server
    []
    (let [accumulator   (atom {})
          server-socket @(udp/socket {:port server-port})
          ;; Once a second, take all the values that have accumulated, `put!` them out, and
          ;; clear the accumulator.
          metric-stream (s/periodically 1000 #(first (swap-vals! accumulator {})))]
      (def server-socket server-socket)

      ;; Listens on a socket, parses each incoming message, and increments the appropriate metric.
      (->> server-socket
           (s/map parse-statsd-packet)
           (s/consume
            (fn [[metric value]]
              (println :AAA [metric value])
              (swap! accumulator update metric #(+ (or % 0) value)))))

      ;; If `metric-stream` is closed, close the associated socket.
      (s/on-drained metric-stream #(s/close! server-socket))

      metric-stream))

  (def server (start-statsd-server))

  (send-metric! "fasdd" 10)

  @(s/take! server)

  (s/close! server)

  ())

(vp/defcomp netcode_server_config (netcode_server_config_t/layout))
(vp/defcomp netcode_client_config (netcode_client_config_t/layout))

(defonce ^:private *state (atom {}))

(defn init!
  "Initiate netcode."
  []
  (when-not (:initiated @*state)
    (-> (netcode$netcode_init/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
        (.apply (into-array Object [])))
    (swap! *state assoc :initiated true)))
#_ (init!)

(defn server-update
  [server time]
  (vn.c/netcode-server-update server time)

  (doseq [client-idx (range (netcode/NETCODE_MAX_CLIENTS))]
    (loop []
      (let [packet-bytes (vp/int* 0)
            packet-sequence (vp/long* 0)
            packet (vn.c/netcode-server-receive-packet server client-idx packet-bytes packet-sequence)]
        (when (pos? (vn.c/netcode-server-client-connected server 0))
          (vn.c/netcode-server-send-packet server 0 (vp/arr (range 10) :byte) 10))
        (when-not (vp/null? packet)
          (println :PACKET_SERVER (vp/p->value packet-sequence :long) (vp/arr packet (vp/p->value packet-bytes :int) :byte))
          (vn.c/netcode-server-free-packet server packet)
          (recur))))))

(defn client-update
  [client time]
  (vn.c/netcode-client-update client time)

  ;; Send a message to the server, if connected.
  (when (= (vn.c/netcode-client-state client) (netcode/NETCODE_CLIENT_STATE_CONNECTED))
    (let [initial (rand-int 100)]
      (vn.c/netcode-client-send-packet client (vp/arr (range initial (+ initial 20)) :byte) 20)))

  (loop []
    (let [packet-bytes (vp/int* 0)
          packet-sequence (vp/long* 0)
          packet (vn.c/netcode-client-receive-packet client packet-bytes packet-sequence)]
      (when-not (vp/null? packet)
        (println :PACKET_CLIENT (vp/p->value packet-sequence :long) (vp/arr packet (vp/p->value packet-bytes :int) :byte))
        (vn.c/netcode-client-free-packet client packet)
        (recur)))))

(defonce server nil)
(defonce client nil)

(comment

  ;; -- Server
  (do
    (when server (vn.c/netcode-server-destroy server))
    (when client (vn.c/netcode-client-destroy client))
    (def server-address #_"[::1]:40000" #_"127.0.0.1:40000" "147.182.133.53:40000")
    (def client-server-address #_"147.182.143.53:40000" server-address)

    (let [_ (do (vn.c/netcode-log-level (netcode/NETCODE_LOG_LEVEL_DEBUG))
                (init!))
          server-address server-address
          server-config (netcode_server_config
                         {:protocol_id 0x1122334455667788
                          :private_key (vp/arr [0x60, 0x6a, 0xbe, 0x6e, 0xc9, 0x19, 0x10, 0xea,
                                                0x9a, 0x65, 0x62, 0xf6, 0x6f, 0x2b, 0x30, 0xe4,
                                                0x43, 0x71, 0xd6, 0x2c, 0xd1, 0x99, 0x27, 0x26,
                                                0x6b, 0x3c, 0x60, 0xf4, 0xb7, 0x15, 0xab, 0xa1]
                                               :byte)})]
      (vn.c/netcode-default-server-config server-config)
      (def server (vn.c/netcode-server-create server-address server-config 0.0)))

    (vn.c/netcode-server-start server (netcode/NETCODE_MAX_CLIENTS)))

  ;; -- Client
  (do
    (let [_ (do (vn.c/netcode-log-level (netcode/NETCODE_LOG_LEVEL_INFO))
                (init!))
          client-config (netcode_client_config)]
      (vn.c/netcode-default-client-config client-config)
      (def client (vn.c/netcode-client-create "0.0.0.0" client-config 0.0))
      (def client-id (vp/long* 0) #_(-> (java.util.Random.) (.nextLong))))

    (vn.c/netcode-random-bytes client-id 8)

    (def user-data (vp/arr (netcode/NETCODE_USER_DATA_BYTES) :byte))
    (vn.c/netcode-random-bytes user-data (netcode/NETCODE_USER_DATA_BYTES))

    (def connect-token (vp/arr (netcode/NETCODE_CONNECT_TOKEN_BYTES) :byte))

    (def private-key
      (vp/arr [0x60, 0x6a, 0xbe, 0x6e, 0xc9, 0x19, 0x10, 0xea,
               0x9a, 0x65, 0x62, 0xf6, 0x6f, 0x2b, 0x30, 0xe4,
               0x43, 0x71, 0xd6, 0x2c, 0xd1, 0x99, 0x27, 0x26,
               0x6b, 0x3c, 0x60, 0xf4, 0xb7, 0x15, 0xab, 0xa1]
              :byte))

    (vn.c/netcode-generate-connect-token
     1
     (doto (vp/arr 1 :pointer) (vp/set* 0 client-server-address))
     (doto (vp/arr 1 :pointer) (vp/set* 0 client-server-address))
     300 50 (vp/p->value client-id :long) 0x1122334455667788 private-key user-data connect-token)

    (vn.c/netcode-client-connect client connect-token))

  (def *enabled (atom true))
  (reset! *enabled false)

  (defn iter
    [i]
    (let [t-range (range i (+ i 1) 0.1)]
      (->> [(future
              (doseq [t t-range]
                (client-update client t)
                #_(server-update server t)
                (Thread/sleep 100)))

            (future
              (doseq [t t-range]
                (server-update server t)
                (Thread/sleep 100)))]
           (mapv deref))))

  (future
    (reset! *enabled true)
    (loop [i 0]
      (iter i)
      (Thread/sleep 1000)
      (when @*enabled
        (recur (inc i)))))

  ())

(defn close!
  "Finish netcode."
  []
  (-> (netcode$netcode_term/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (into-array Object []))))
#_ (close!)
