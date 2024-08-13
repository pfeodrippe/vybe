(ns vybe.network
  (:require
   [vybe.netcode.c :as vn.c]
   [vybe.panama :as vp]
   [aleph.udp :as udp]
   [clj-commons.byte-streams :as bs]
   [manifold.stream :as s]
   [clojure.string :as str]
   [clojure.pprint :as pp]
   [clojure.edn :as edn]
   [potemkin :refer [defprotocol+]])
  (:import
   (org.vybe.netcode netcode cn_endpoint_t netcode$cn_crypto_generate_key
                     cn_result_t cn_server_event_t cn_crypto_sign_public_t cn_crypto_sign_secret_t)
   (java.time Instant)))

(vp/defcomp endpoint_t (cn_endpoint_t/layout))
(vp/defcomp result_t (cn_result_t/layout))
(vp/defcomp server_event_t (cn_server_event_t/layout))
(vp/defcomp crypto_sign_public_t (cn_crypto_sign_public_t/layout))
(vp/defcomp crypto_sign_secret_t (cn_crypto_sign_secret_t/layout))

(defonce ^:private lock (Object.))

(defn debug!
  [{:vn/keys [client-id]} & msgs]
  (locking lock
    (apply println :DEBUG_NET :client-id client-id msgs)))

(defn- cn-crypto-generate-key
  []
  (-> (netcode$cn_crypto_generate_key/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (vp/default-arena) (into-array Object []))))

(defn- timestamp
  []
  (.getEpochSecond (Instant/now)))

(defn send!
  "Send a message to the server or to a client."
  ([client msg]
   (vn.c/cn-client-send client msg (inc (count msg)) false))
  ([server client-id msg]
   (vn.c/cn-server-send server msg (inc (count msg)) client-id false)))

(defonce ^:private *tracker (atom {}))

(defn server-update!
  [server delta-time]
  (swap! *tracker update-in [server :counter] (fnil inc 0))
  (vp/with-arena _
    (vn.c/cn-server-update server delta-time (timestamp))

    (let [event (server_event_t)]
      (while (vn.c/cn-server-pop-event server event)
        (condp = (:type event)
          (netcode/CN_SERVER_EVENT_TYPE_NEW_CONNECTION)
          (debug! {} :SERVER :NEW_CONNECTION
                  (-> event :u :new_connection :client_index)
                  (-> event :u :new_connection :client_id)
                  (-> event :u :new_connection :endpoint))

          (netcode/CN_SERVER_EVENT_TYPE_PAYLOAD_PACKET)
          (do (debug! {} :SERVER :PACKET
                      (-> event :u :payload_packet :client_index)
                      (-> event :u :payload_packet :data vp/->string))
              (vn.c/cn-server-free-packet server
                                          (-> event :u :payload_packet :client_index)
                                          (-> event :u :payload_packet :data))
              (when (zero? (mod (get-in @*tracker [server :counter]) 10))
                (let [msg "ALIVE"]
                  (vn.c/cn-server-send server msg (inc (count msg)) (-> event :u :payload_packet :client_index) false))))

          (netcode/CN_SERVER_EVENT_TYPE_DISCONNECTED)
          (debug! {} :SERVER :DISCONNECTED
                  (-> event :u :disconnected :client_index)))))))

(defn client-update!
  [client delta-time]
  (swap! *tracker update-in [client :counter] (fnil inc 0))
  (vp/with-arena _
    (vn.c/cn-client-update client delta-time (timestamp))

    (when (= (vn.c/cn-client-state-get client) (netcode/CN_CLIENT_STATE_CONNECTED))
      (let [packet-size (vp/int* 0)
            packet (vp/arr 1 :pointer)]
        (while (vn.c/cn-client-pop-packet client packet packet-size vp/null)
          (debug! {} :CLIENT :PACKET
                  (vp/p->value packet-size :int)
                  (vp/->string (-> (vp/get-at packet 0)
                                   (vp/reinterpret (vp/p->value packet-size :int)))))
          (doseq [packet packet #_(vp/arr (vp/mem packet) (vp/p->value packet-size :int) [:pointer :byte])]
            (vn.c/cn-client-free-packet client packet)))

        (when (zero? (mod (get-in @*tracker [client :counter]) 10))
          (let [msg "ALIVE"]
            (vn.c/cn-client-send client msg (inc (count msg)) false)))))))

(defn update!
  [{:vn/keys [*state]} delta-time]
  (when *state
    (when-let [server (:vn/server @*state)]
      (server-update! server delta-time))

    (when-let [client (:vn/client @*state)]
      (client-update! client delta-time))))

(defn- -cn-server-iter
  [server i]
  (let [t-range (range i (+ i 1) 0.016)]
    (doseq [t t-range]
      (server-update! server 1/60)
      (Thread/sleep 16))))

(defn- -cn-client-iter
  [client i]
  (let [t-range (range i (+ i 1) 0.016)]
    (doseq [t t-range]
      (client-update! client 1/60)
      (Thread/sleep 16))))

(defn cn-server
  [server-address application-id public-key secret-key]
  (let [endpoint (endpoint_t)
        _ (vn.c/cn-endpoint-init endpoint server-address)
        server-config (-> (vn.c/cn-server-config-defaults)
                          (merge {:application_id application-id
                                  :public_key (if (instance? vybe.panama.VybePSeq public-key)
                                                {:key public-key}
                                                (vp/arr (:key public-key) :byte))
                                  :secret_key (if (instance? vybe.panama.VybePSeq secret-key)
                                                {:key secret-key}
                                                (vp/arr (:key secret-key) :byte))}))
        server (vn.c/cn-server-create server-config)
        result (vn.c/cn-server-start server server-address)]
    (when (vn.c/cn-is-error result)
      (vn.c/cn-server-destroy server)
      (throw (ex-info "Couldn't start CN server" {:error result})))
    server))

(defn cn-gen-keys
  []
  (let [public-key (crypto_sign_public_t)
        secret-key (crypto_sign_secret_t)]
    (vn.c/cn-crypto-sign-keygen public-key secret-key)
    [public-key secret-key]))

(defn cn-connect-token
  [server-address application-id client-id secret-key]
  (let [connect-token (vp/arr (netcode/CN_CONNECT_TOKEN_SIZE) :byte)
        client-to-server-key (cn-crypto-generate-key)
        server-to-client-key (cn-crypto-generate-key)
        current-ts (timestamp)
        expiration-ts (+ current-ts 60)
        handshake-timeout 5
        endpoints (doto (vp/arr 1 :pointer) (vp/set-at 0 server-address))
        user-data (vp/arr (netcode/CN_CONNECT_TOKEN_USER_DATA_SIZE) :byte)
        connect-token-res (vn.c/cn-generate-connect-token
                           application-id
                           current-ts
                           client-to-server-key
                           server-to-client-key
                           expiration-ts
                           handshake-timeout
                           (count endpoints)
                           endpoints
                           client-id
                           user-data
                           secret-key
                           connect-token)
        _ (when (vn.c/cn-is-error connect-token-res)
            (throw (ex-info "Couldn't create connect token" {:error connect-token-res
                                                             :client-id client-id
                                                             :server-address server-address})))]
    connect-token))

(defn cn-client
  [connect-token client-port application-id]
  (let [client (vn.c/cn-client-create (unchecked-short client-port) application-id false vp/null)
        client-connect-res (vn.c/cn-client-connect client connect-token)
        _ (when (vn.c/cn-is-error client-connect-res)
            (throw (ex-info "Couldn't client connect" {:error client-connect-res
                                                       :client-port client-port
                                                       :application-id application-id})))]
    client))

(comment

  (do (def *enabled (atom false))
      (let [[public-key secret-key] (cn-gen-keys)]
        (def cn-public-key public-key)
        (def cn-secret-key secret-key)

        (defonce test-lock (Object.))

        (defonce server nil)
        (defonce client nil)

        (locking test-lock
          (some-> client vn.c/cn-client-disconnect)
          (some-> client vn.c/cn-client-destroy)
          (some-> server vn.c/cn-server-destroy)
          (def client nil)
          (def server nil))

        (def server-address "127.0.0.1:43000")
        (def application-id 1000)))

  ;; -- Server
  (def server
    (cn-server server-address application-id cn-public-key cn-secret-key))

  ;; -- Client
  (def client
    (cn-client (cn-connect-token server-address application-id 10 cn-secret-key) 43001 application-id))

  (do (reset! *enabled true)
      (future
        (try
          (loop [i 0]
            (debug! {} :SERVER_I i)
            (-cn-server-iter server i)
            (when @*enabled
              (recur (inc i))))
          (catch Exception e
            (println e)))))

  (future
    (try
      (loop [i 0]
        (debug! {} :CLIENT_I i)
        (-cn-client-iter client i)
        (when @*enabled
          (recur (inc i))))
      (catch Exception e
        (println e))))

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
        {:vn/keys [is-server-found is-peer-info-received own-ip own-port peers]} @*state
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

          :vn.type/greeting
          (when is-host
            (doseq [{:vn/keys [peer-client-id peer-ip peer-port]} peers]
              (let [server-address (str own-ip ":" own-port)
                    [public-key secret-key] (cn-gen-keys)
                    connect-token (cn-connect-token server-address #_(str "0.0.0.0:" local-port)
                                                    #_server-address #_(str "0.0.0.0:" local-port) #_(str "127.0.0.1:" local-port)
                                                    12345
                                                    peer-client-id
                                                    secret-key)
                    connect-token-vec (into [] connect-token)
                    token-1 (subvec connect-token-vec 0 (/ (count connect-token-vec) 2))
                    token-2 (subvec connect-token-vec (/ (count connect-token-vec) 2))]
                (Thread/sleep 1000)
                @(s/put! (:vn/socket @*state)
                         {:host    peer-ip
                          :port    peer-port
                          :message (-serialize {:vn/type :vn.type/connect-token-1
                                                :vn/connect-token-part (.encodeToString (java.util.Base64/getEncoder) (byte-array token-1))
                                                :vn/server-address server-address})})
                (Thread/sleep 500)
                @(s/put! (:vn/socket @*state)
                         {:host    peer-ip
                          :port    peer-port
                          :message (-serialize {:vn/type :vn.type/connect-token-2
                                                :vn/connect-token-part (.encodeToString (java.util.Base64/getEncoder) (byte-array token-2))
                                                :vn/server-address server-address})})

                (debug! puncher :SOCKET (:vn/socket @*state))
                (debug! puncher :SOCKET_CLOSE (s/close! (:vn/socket @*state)) :IS_HOST is-host)
                (debug! puncher :SOCKET_IS_CLOSED (s/closed? (:vn/socket @*state)))

                (future
                  (try
                    (Thread/sleep 1000)
                    (debug! puncher :starting-netcode-server)
                    (let [server (cn-server #_server-address #_(str "127.0.0.1:" local-port)
                                            (str "0.0.0.0:" local-port)
                                            12345 public-key secret-key)]
                      (vn.c/cn-server-set-public-ip server server-address)
                      (swap! *state merge {:vn/server server})
                      #_(debug! puncher :SERVER_STARTING_LOOP server)
                      #_(future
                          (try
                            (loop [i 0]
                              (debug! {} :SERVER_I i)
                              (-cn-server-iter server i)
                              #_(Thread/sleep 1000)
                              (recur (inc i)))
                            (catch Exception e
                              (println e)))))
                    (catch Exception e
                      (println e)))))))

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
                    connect-token (vp/arr connect-token-vec :byte)
                    _ (debug! puncher :starting-netcode-client)
                    client (cn-client connect-token local-port 12345)]
                (swap! *state merge {:vn/client client})
                #_(future
                    (try
                      (loop [i 0]
                        (debug! {} :CLIENT_I i)
                        (-cn-client-iter client i)
                        #_(Thread/sleep 1000)
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

          (swap! *state update :vn/peers conj {:vn/peer-client-id (Long/parseLong peer-client-id)
                                               :vn/peer-ip peer-ip
                                               :vn/peer-port (Long/parseLong peer-port)})

          (doseq [_ (range 10)]
            @(s/put! (:vn/socket @*state)
                     {:host    peer-ip
                      :port    (Long/parseLong peer-port)
                      :message (-serialize {:vn/type :vn.type/greeting})})
            (Thread/sleep 100)))))))

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

(def ^:private *acc (atom 52))

(comment

  (let [session-id   (str "gamecode" @*acc)
        client-id    (str @*acc "20")
        server-ip    "147.182.133.53"
        server-port  8080
        host-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                               :client-id client-id
                                                               :num-of-players 2
                                                               :is-host true})]
    host-puncher)

  (let [session-id     (str "gamecode" @*acc)
        client-id      (str @*acc "21")
        server-ip      "147.182.133.53"
        server-port    8080
        client-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                                 :client-id client-id})]
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
