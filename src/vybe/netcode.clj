(ns vybe.netcode
  (:require
   [vybe.netcode.c :as vn.c]
   [vybe.panama :as vp]
   [aleph.udp :as udp]
   [clj-commons.byte-streams :as bs]
   [manifold.stream :as s]
   [clojure.string :as str]
   [clojure.pprint :as pp]
   [clojure.edn :as edn])
  (:import
   (org.vybe.netcode netcode netcode$netcode_init netcode$netcode_term
                     netcode_server_config_t netcode_client_config_t)))

(defonce ^:private lock (Object.))

(defn debug!
  [{:vn/keys [client-id]} & msgs]
  (locking lock
    (apply println :DEBUG_NET :client-id client-id msgs)))

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
        (if-not (vp/null? packet)
          (do (debug! {} :PACKET_SERVER (vp/p->value packet-sequence :long) (vp/arr packet (vp/p->value packet-bytes :int) :byte))
              (vn.c/netcode-server-free-packet server packet)
              (when (pos? (vn.c/netcode-server-client-connected server client-idx))
                (vn.c/netcode-server-send-packet server client-idx (vp/arr (range 10) :byte) 10))
              (recur))
          (when (pos? (vn.c/netcode-server-client-connected server client-idx))
            (vn.c/netcode-server-send-packet server client-idx (vp/arr (range 10) :byte) 10)))))))

(defn client-update
  [client time]
  (vn.c/netcode-client-update client time)
  (loop []
    (let [packet-bytes (vp/int* 0)
          packet-sequence (vp/long* 0)
          packet (vn.c/netcode-client-receive-packet client packet-bytes packet-sequence)]
      (if-not (vp/null? packet)
        (do (debug! {} :PACKET_CLIENT (vp/p->value packet-sequence :long) (vp/arr packet (vp/p->value packet-bytes :int) :byte))
            (vn.c/netcode-client-free-packet client packet)
            (recur))
        ;; Send a message to the server, if connected.
        (when (= (vn.c/netcode-client-state client) (netcode/NETCODE_CLIENT_STATE_CONNECTED))
          (let [initial (rand-int 100)]
            (vn.c/netcode-client-send-packet client (vp/arr (range initial (+ initial 20)) :byte) 20))) ))))

(defn- -netcode-server-iter
  [server i]
  (let [t-range (range i (+ i 1) 0.1)]
    (doseq [t t-range]
      (server-update server t)
      (Thread/sleep 100))))

(defn- -netcode-client-iter
  [client i]
  (let [t-range (range i (+ i 1) 0.1)]
    (doseq [t t-range]
      (client-update client t)
      (Thread/sleep 100))))

(defn close!
  "Finish netcode."
  []
  (-> (netcode$netcode_term/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (into-array Object []))))
#_ (close!)

(defonce ^:private bogus-private-key
  (vp/arr [0x60, 0x6a, 0xbe, 0x6e, 0xc9, 0x19, 0x10, 0xea,
           0x9a, 0x65, 0x62, 0xf6, 0x6f, 0x2b, 0x30, 0xe4,
           0x43, 0x71, 0xd6, 0x2c, 0xd1, 0x99, 0x27, 0x26,
           0x6b, 0x3c, 0x60, 0xf4, 0xb7, 0x15, 0xab, 0xa1]
          :byte))

(defn netcode-server
  [server-address private-key]
  #_(def server-address "[::1]:65424" #_"127.0.0.1:40000" #_"147.182.133.53:40000" #_"69.158.246.202:35351")
  (vn.c/netcode-log-level (netcode/NETCODE_LOG_LEVEL_DEBUG))
  (init!)
  (let [server-config (netcode_server_config
                       {:protocol_id 0x1122334455667788
                        :private_key private-key})
        _ (vn.c/netcode-default-server-config server-config)
        server (vn.c/netcode-server-create server-address server-config 0.0)]
    (debug! {} :NETCODE_SERVER server)
    (when (= server vp/null)
      (throw (ex-info "Couldn't create netcode server" {:server-address server-address
                                                        :server server})))
    (vn.c/netcode-server-start server (netcode/NETCODE_MAX_CLIENTS))
    server))

(defn netcode-client
  [client-port connect-token-seq]
  (vn.c/netcode-log-level (netcode/NETCODE_LOG_LEVEL_INFO))
  (init!)
  (let [client-config (netcode_client_config)
        _ (vn.c/netcode-default-client-config client-config)
        client (vn.c/netcode-client-create (str "0.0.0.0:" client-port) client-config 0.0)
        connect-token (vp/arr connect-token-seq :byte)]
    (debug! {} :NETCODE_CLIENT client)
    (when (= client vp/null)
      (throw (ex-info "Couldn't connect netcode client" {:client-port client-port
                                                         :client client})))
    (vn.c/netcode-client-connect client connect-token)
    client))

(defn netcode-connect-token
  [public-server-address internal-server-address client-id private-key]
  (let [user-data (doto (vp/arr (netcode/NETCODE_USER_DATA_BYTES) :byte)
                    (vn.c/netcode-random-bytes (netcode/NETCODE_USER_DATA_BYTES)))
        connect-token (vp/arr (netcode/NETCODE_CONNECT_TOKEN_BYTES) :byte)]
    (debug! {} :NETCODE_CONNECT_TOKEN public-server-address internal-server-address client-id)
    (vn.c/netcode-generate-connect-token
     1
     (doto (vp/arr 1 :pointer) (vp/set* 0 public-server-address))
     (doto (vp/arr 1 :pointer) (vp/set* 0 internal-server-address))
     300 50 client-id 0x1122334455667788 private-key user-data connect-token)
    (into [] connect-token)))

(comment

  (def *enabled (atom true))
  #_(reset! *enabled false)

  (def my-server-address "127.0.0.1:40010")

  (let [my-server (netcode-server my-server-address bogus-private-key)]
    (def my-server my-server)
    (future
      (try
        (loop [i 0]
          (debug! {} :SERVER_I i)
          (-netcode-server-iter my-server i)
          (Thread/sleep 1000)
          (when @*enabled
            (recur (inc i))))
        (catch Exception e
          (println e))))
    #_(vn.c/netcode-server-destroy my-server))

  (let [my-client (netcode-client 40020 (netcode-connect-token my-server-address my-server-address 100 bogus-private-key))]
    (def my-client my-client)
    (future
      (try
        (loop [i 0]
          (debug! {} :CLIENT_I i)
          (-netcode-client-iter my-client i)
          (Thread/sleep 1000)
          (when @*enabled
            (recur (inc i))))
        (catch Exception e
          (println e))))
    #_(vn.c/netcode-client-destroy my-client))

  ())

;; -- Puncher.
(defn put!
  [{:vn/keys [host port *state] :as puncher} msg]
  (debug! puncher :PUT msg)
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
  (when-let [socket (:vn/socket @*state)]
    (debug! puncher :SOCKET_CLOSE socket)
    (s/close! socket))
  (let [socket (make-socket #(try
                               (puncher-consumer puncher %)
                               (catch Exception e
                                 (println e)
                                 (throw e))))]
    (swap! *state (fn [state]
                    (merge state {:vn/socket socket}))))
  puncher)

(defn- -serialize
  [data]
  (str "#EDN" (pr-str data)))

(defn puncher-consumer
  [{:vn/keys [session-id client-id is-host *state] :as puncher}
   {:keys [message]}]
  (let [msg (bs/to-string message)
        {:vn/keys [is-server-found is-peer-info-received own-ip own-port]} @*state
        local-port (-> (.description (.sink (:vn/socket @*state)))
                       :connection
                       :local-address
                       (str/split #":")
                       last
                       Long/parseLong)]
    (debug! puncher :RECEIVED msg)
    (cond
      (str/starts-with? msg "#EDN")
      (let [data (edn/read-string (subs msg 4))]
        (case (:vn/type data)
          :vn.type/connect-token-1
          (swap! *state merge {:vn/connect-token-1 (:vn/connect-token-part data)
                               :vn/server-address (:vn/server-address data)})

          :vn.type/connect-token-2
          (swap! *state merge {:vn/connect-token-2 (:vn/connect-token-part data)})

          nil)

        ;; Check if we have all the token parts.
        (when (and (:vn/connect-token-1 @*state)
                   (:vn/connect-token-2 @*state))
          (debug! puncher :CONNECT_TOKEN_COMPLETE :SOCKET (:vn/socket @*state))
          (debug! puncher :SOCKET_CLOSE (s/close! (:vn/socket @*state)) :IS_HOST is-host)
          (debug! puncher :SOCKET_IS_CLOSED (s/closed? (:vn/socket @*state)))

          (future
            (Thread/sleep 1000)
            (try
              (let [{:vn/keys [connect-token-1 connect-token-2]} @*state
                    connect-token-1-vec (->> (.decode (java.util.Base64/getDecoder) connect-token-1)
                                             (into []))
                    connect-token-2-vec (->> (.decode (java.util.Base64/getDecoder) connect-token-2)
                                             (into []))
                    connect-token-vec (vec (concat connect-token-1-vec connect-token-2-vec))
                    client (netcode-client local-port connect-token-vec)]
                (debug! puncher :starting-netcode-client)
                (future
                  (try
                    (loop [i 0]
                      (debug! {} :CLIENT_I i)
                      (-netcode-client-iter client i)
                      (Thread/sleep 1000)
                      (recur (inc i)))
                    (catch Exception e
                      (println e)))))
              (catch Exception e
                (println e))))))

      (str/starts-with? msg "ok")
      (do
        (swap! *state merge (let [[_ ip port-str] (str/split msg #":")]
                              {:vn/own-port (Long/parseLong port-str)
                               :vn/own-ip ip
                               :vn/is-server-found true}))
        (when (and is-host (not is-server-found))
          #_(puncher-socket! puncher)
          (put! puncher (client-msg session-id client-id))))

      (str/starts-with? msg "peers")
      (when (not is-peer-info-received)
        (swap! *state merge {:vn/is-peer-info-received true})

        ;; We have a peer here, send a packet to it.
        (let [[_ peer-client-id peer-ip peer-port] (str/split msg #":")]
          (debug! puncher :PEER [peer-client-id peer-ip peer-port local-port])
          @(s/put! (:vn/socket @*state)
                   {:host    peer-ip
                    :port    (Long/parseLong peer-port)
                    :message (-serialize {:vn/type :vn.type/greeting
                                          :vn/client-id peer-client-id})})

          ;; Generate connect token and send to a client peer.
          (when is-host
            (let [server-address (str own-ip ":" own-port)
                  connect-token (netcode-connect-token server-address
                                                       (str "0.0.0.0:" local-port)
                                                       (Long/parseLong peer-client-id)
                                                       bogus-private-key)
                  token-1 (subvec connect-token 0 (/ (count connect-token) 2))
                  token-2 (subvec connect-token (/ (count connect-token) 2))]
              (Thread/sleep 1000)
              @(s/put! (:vn/socket @*state)
                       {:host    peer-ip
                        :port    (Long/parseLong peer-port)
                        :message (-serialize {:vn/type :vn.type/connect-token-1
                                              :vn/connect-token-part (.encodeToString (java.util.Base64/getEncoder) (byte-array token-1))
                                              :vn/server-address server-address})})
              (Thread/sleep 500)
              @(s/put! (:vn/socket @*state)
                       {:host    peer-ip
                        :port    (Long/parseLong peer-port)
                        :message (-serialize {:vn/type :vn.type/connect-token-2
                                              :vn/connect-token-part (.encodeToString (java.util.Base64/getEncoder) (byte-array token-2))
                                              :vn/server-address server-address})}))

            (debug! puncher :SOCKET (:vn/socket @*state))
            (debug! puncher :SOCKET_CLOSE (s/close! (:vn/socket @*state)) :IS_HOST is-host)
            (debug! puncher :SOCKET_IS_CLOSED (s/closed? (:vn/socket @*state))))

          (future
            (Thread/sleep 1000)
            (when is-host
              (debug! puncher :starting-netcode-server)
              (let [server (netcode-server (str "0.0.0.0:" local-port) bogus-private-key)]
                (debug! puncher :SERVER_STARTING_LOOP server)
                (future
                  (try
                    (loop [i 0]
                      (debug! {} :SERVER_I i)
                      (-netcode-server-iter server i)
                      (Thread/sleep 1000)
                      (recur (inc i)))
                    (catch Exception e
                      (println e))))))))))))

(defn make-hole-puncher
  "For `host`, don't use static ip, use machine's public IP.

  All the keys are required, the optional ones are:
    - `:is-host`
    - `:num-of-players`

  See https://github.com/pomme-grenade/2planets/blob/b8d9d9296a00d6ca781bdb73d249d08b2bffc5a8/addons/Holepunch/holepunch_node.gd"
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

(def *acc (atom 52))

(comment

  (let [session-id     (str "gamecode" @*acc)
        client-id     (str @*acc "20")
        server-ip      "147.182.133.53"
        server-port    8080
        host-puncher   (make-hole-puncher server-ip server-port {:session-id     session-id
                                                                 :client-id client-id
                                                                 :num-of-players 2
                                                                 :is-host        true})]
    host-puncher)

  (let [session-id     (str "gamecode" @*acc)
        client-id     (str @*acc "21")
        server-ip      "147.182.133.53"
        server-port    8080
        client-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                                 :client-id   client-id})]
    client-puncher)

  ;; --------------------
  (def aaa @(udp/socket {:port 55630}))
  (->> aaa (s/consume println))
  (s/put! aaa
          {:host "69.158.246.202"
           :port 44389
           :message "from host"})

  (def bbb @(udp/socket {:port 45060}))
  (->> bbb (s/consume println))
  (s/put! bbb
          {:host "142.198.99.236"
           :port 49360
           :message "from client"})

  ())
