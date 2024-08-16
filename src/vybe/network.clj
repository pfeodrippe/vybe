(ns vybe.network
  (:require
   [vybe.netcode.c :as vn.c]
   [vybe.panama :as vp]
   [aleph.udp :as udp]
   [clj-commons.byte-streams :as bs]
   [manifold.stream :as s]
   [clojure.string :as str]
   [clojure.pprint :as pp]
   [clojure.set :as set]
   [clojure.edn :as edn]
   [potemkin :refer [defprotocol+]]
   [vybe.type :as vt])
  (:import
   (org.vybe.netcode netcode cn_endpoint_t netcode$cn_crypto_generate_key
                     cn_result_t cn_server_event_t cn_crypto_sign_public_t cn_crypto_sign_secret_t)
   (java.time Instant)
   (vybe.panama VybePMap)))

(set! *warn-on-reflection* true)

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

(vp/defcomp PacketData
  [[:size :int]
   [:kind :int]
   [:data [:vec {:size 256} :byte]]])

(def ^:private packet-component-size
  (.byteSize (.layout PacketData)))

(def ^:private kinds-ser
  {String -10
   clojure.lang.IObj -20})

(def ^:private kinds-deser
  {-10 vp/->string
   -20 (comp edn/read-string vp/->string)})

(defn -pmap->schema
  [^VybePMap pmap]
  (let [c (.component pmap)]
    {:vn.msg/type :vn.msg.type/component
     :kind (vp/cache-comp c)
     :name (symbol (.get (.name (.layout c))))
     :schema (->> (.fields c)
                  (mapv (fn [[field {:keys [type]}]]
                          [field (if (vp/component? type)
                                   (symbol (.get (.name (vp/layout type))))
                                   type)])))}))
#_(-pmap->schema (vt/Translation))
#_(-pmap->schema (PacketData))

#_(vp/defcomp Dadasd
    [[:a vt/Translation]])
#_(-pmap->schema (Dadasd))

(def ^:private -packet-data-offset 8)

(defn -make-packet
  [v]
  (cond
    (instance? VybePMap v)
    (let [^VybePMap v v
          c (.component v)]
      (PacketData
       {:size (+ -packet-data-offset (.byteSize (.layout c)))
        :kind (vp/cache-comp c)
        :data v}))

    (instance? clojure.lang.IObj v)
    (let [mem (vp/mem (pr-str v))]
      (PacketData
       {:size (+ -packet-data-offset (.byteSize mem))
        :kind (get kinds-ser clojure.lang.IObj)
        :data mem}))

    :else
    (let [mem (vp/mem v)]
      (PacketData
       {:size (+ -packet-data-offset (.byteSize mem))
        :kind (get kinds-ser (type v))
        :data mem}))))
#_ (-packet-deser (vp/mem (-make-packet (vt/Translation {:x 30}))))
#_ (-packet-deser (vp/mem (-make-packet (-pmap->schema (vt/Translation {:x 30})))))
#_ (-packet-deser (vp/mem (-make-packet "abcde")))
#_ (-packet-deser (vp/mem (-make-packet {:a 4})))

(defonce ^:private *tracker (atom {}))

(defn -packet-deser
  ([packet-data]
   (-packet-deser vp/comp-cache packet-data))
  ([get-f packet-data]
   (let [{:keys [kind] :as packet-value} (vp/p->map packet-data PacketData)
         data-mem (.asSlice (vp/mem packet-value) -packet-data-offset)]
     (if-let [f (get kinds-deser kind)]
       (f data-mem)
       (vp/clone (vp/p->value data-mem (get-f kind)))))))

(defn -client-send!
  ([client msg]
   (-client-send! client msg {}))
  ([client msg {:keys [reliable]
                :or {reliable false}}]
   (when (= (vn.c/cn-client-state-get client) (netcode/CN_CLIENT_STATE_CONNECTED))
     (let [{:keys [size] :as data} (-make-packet msg)]

       ;; When it's a pmap and it's the first time we are sending this component
       ;; to the other part, send the schema reliably.
       (when (and (vp/pmap? msg)
                  (not (get @*tracker [client :vn.tracker.component/sent (vp/component msg)])))
         (-client-send! client (-pmap->schema msg) {:reliable true})
         (swap! *tracker assoc [client :vn.tracker.component/sent (vp/component msg)] true))

       (vn.c/cn-client-send client data size reliable)))))

(defn -server-send!
  ([server client-index msg]
   (-server-send! server client-index msg {}))
  ([server client-index msg {:keys [reliable]
                             :or {reliable false}}]
   (when (vn.c/cn-server-is-client-connected server client-index)
    (let [{:keys [size] :as data} (-make-packet msg)]

      ;; When it's a pmap and it's the first time we are sending this component
      ;; to the other part, send the schema reliably.
      (when (and (vp/pmap? msg)
                 (not (get @*tracker [server client-index :vn.tracker.component/sent (vp/component msg)])))
        (-server-send! server client-index (-pmap->schema msg) {:reliable true})
        (swap! *tracker assoc [server client-index :vn.tracker.component/sent (vp/component msg)] true))

      (vn.c/cn-server-send server data size client-index reliable)))))

(defn send!
  "Send a message."
  ([{:vn/keys [*state] :as puncher} msg]
   (when *state
     (or (when (:vn/server @*state)
           (send! puncher 0 msg))
         (when-let [client (:vn/client @*state)]
           (-client-send! client msg)))))
  ([{:vn/keys [*state]} client-index msg]
   (when-let [server (:vn/server @*state)]
     (-server-send! server client-index msg))))

(defn host?
  [{:vn/keys [is-host]}]
  is-host)

(defn -server-update!
  [server delta-time]
  (swap! *tracker update-in [server :counter] (fnil inc 0))
  (vn.c/cn-server-update server delta-time (timestamp))

  (let [event (server_event_t)
        *msgs (atom [])]
    (while (vn.c/cn-server-pop-event server event)
      (condp = (:type event)
        ;; -- NEW CONNECTION
        (netcode/CN_SERVER_EVENT_TYPE_NEW_CONNECTION)
        (debug! {} :SERVER :NEW_CONNECTION
                (-> event :u :new_connection :client_index)
                (-> event :u :new_connection :client_id)
                (-> event :u :new_connection :endpoint))

        ;; -- PAYLOAD
        (netcode/CN_SERVER_EVENT_TYPE_PAYLOAD_PACKET)
        (let [packet-data (-> event :u :payload_packet :data)
              packet-size (-> event :u :payload_packet :size)
              client-index (-> event :u :payload_packet :client_index)
              msg (-packet-deser #(get @*tracker [server client-index :vn.tracker.component/received %])
                                 packet-data)]

          ;; If the receive a component (which contains the schema),
          ;; we associate it with this client index.
          (let [{:vn.msg/keys [type]} msg]
            (when (= type :vn.msg.type/component)
              (let [{:keys [kind name schema]} msg
                    ;; We get comp cache twice so we have the component in the end.
                    c (or (-> name vp/comp-cache vp/comp-cache)
                          (vp/make-component name schema))]
                (swap! *tracker assoc [server client-index :vn.tracker.component/received kind] c))))

          (debug! {} :SERVER :PACKET packet-size client-index msg)

          (when-not (= msg "ALIVE")
            (swap! *msgs conj {:client-index (-> event :u :payload_packet :client_index)
                               :data msg}))

          (vn.c/cn-server-free-packet server
                                      (-> event :u :payload_packet :client_index)
                                      (-> event :u :payload_packet :data))

          (when (zero? (mod (get-in @*tracker [server :counter]) 4))
            (let [msg "ALIVE"]
              (-server-send! server (-> event :u :payload_packet :client_index) msg))))

        ;; -- DISCONNECTED
        (netcode/CN_SERVER_EVENT_TYPE_DISCONNECTED)
        (debug! {} :SERVER :DISCONNECTED
                (-> event :u :disconnected :client_index))))
    @*msgs))

(defn -client-update!
  [client delta-time]
  (swap! *tracker update-in [client :counter] (fnil inc 0))
  (vn.c/cn-client-update client delta-time (timestamp))

  (when (= (vn.c/cn-client-state-get client) (netcode/CN_CLIENT_STATE_CONNECTED))
    (let [packet-size (vp/int* 0)
          packet (vp/arr 1 :pointer)
          *msgs (atom [])]
      (while (vn.c/cn-client-pop-packet client packet packet-size vp/null)
        (let [msg (-packet-deser #(get @*tracker [client :vn.tracker.component/received %])
                                 (vp/reinterpret (vp/get-at packet 0) packet-component-size))]

          ;; If the receive a component (which contains the schema),
          ;; we associate it with this client index.
          (let [{:vn.msg/keys [type]} msg]
            (when (= type :vn.msg.type/component)
              (let [{:keys [kind name schema]} msg
                    ;; We get comp cache twice so we have the component in the end.
                    c (or (-> name vp/comp-cache vp/comp-cache)
                          (vp/make-component name schema))]
                (swap! *tracker assoc [client :vn.tracker.component/received kind] c))))

          (debug! {} :CLIENT :PACKET (vp/p->value packet-size :int) msg)

          (when-not (= msg "ALIVE")
            (swap! *msgs conj {:client-index -1
                               :data msg})))

        (doseq [packet packet]
          (vn.c/cn-client-free-packet client packet)))

      (when (zero? (mod (get-in @*tracker [client :counter]) 20))
        (let [msg "ALIVE"]
          (-client-send! client msg)))

      @*msgs)))

(defn update!
  "It returns a vector of messages (if any received)."
  [{:vn/keys [*state]} delta-time]
  (when *state
    (or (when-let [server (:vn/server @*state)]
          (-server-update! server delta-time))
        (when-let [client (:vn/client @*state)]
          (-client-update! client delta-time)))))

(defn- -cn-server-iter
  [server i]
  (let [t-range (range i (+ i 1) 0.016)]
    (doseq [t t-range]
      (-server-update! server 1/60)
      (Thread/sleep 16))))

(defn- -cn-client-iter
  [client i]
  (let [t-range (range i (+ i 1) 0.016)]
    (doseq [t t-range]
      (-client-update! client 1/60)
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
            (vp/with-arena _
              (-server-send! server 0 (vybe.type/Translation [2 10 440]))
              (-cn-server-iter server i))
            (when @*enabled
              (recur (inc i))))
          (catch Exception e
            (println e)))))

  (future
    (try
      (loop [i 0]
        (debug! {} :CLIENT_I i)
        (vp/with-arena _
          (-client-send! client (vt/Translation [2 10 44]))
          (-cn-client-iter client i))
        (when @*enabled
          (recur (inc i))))
      (catch Exception e
        (println e))))

  ())

;; -- Puncher.
(defn -socket-put!
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

(comment

  (bs/def-conversion [vybe.panama.VybePMap ByteBuffer] )

  (let [buffer (.asByteBuffer (vp/mem (vybe.type/Translation [1 2 0])))]
    (->> (range (.limit buffer))
         (mapv #(.get buffer %))
         (byte-array)))

  (bs/convert (.asByteBuffer (vp/mem (vybe.type/Translation [1 2 0]))) java.nio.ByteBuffer)

  (.byteSize (.layout vybe.type/Translation))

  ())

(defn puncher-consumer
  [{:vn/keys [session-id client-id is-host *state] :as puncher}
   {:keys [message]}]
  (let [msg (bs/to-string message)
        {:vn/keys [is-server-found is-peer-info-received own-ip own-port peers]} @*state
        ^manifold.stream.SplicedStream socket (:vn/socket @*state)
        local-port (-> (.description ^aleph.netty.ChannelSink (.sink socket))
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
              (let [{:vn/keys [^String connect-token-1 ^String connect-token-2]} @*state
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
          (-socket-put! puncher (client-msg session-id client-id))))

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
      (-socket-put! puncher (session-msg (:vn/session-id puncher) (:vn/num-of-players puncher)))
      (-socket-put! puncher (client-msg (:vn/session-id puncher) (:vn/client-id puncher))))
    puncher))

(def ^:private *acc (atom 52))

(comment

  (def *enabled (atom false))

  (let [_ (reset! *enabled true)
        session-id   (str "gamecode" @*acc)
        client-id    (str @*acc "20")
        server-ip    "147.182.133.53"
        server-port  8080
        host-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                               :client-id client-id
                                                               :num-of-players 2
                                                               :is-host true})]
    (def host-puncher host-puncher)
    (future
      (try
        (loop [i 0]
          (debug! {} :SERVER_I i)
          (vp/with-arena _
            (send! host-puncher 0 (vybe.type/Translation [2 10 440]))
            (update! host-puncher 1/60))
          (Thread/sleep 16)
          (when @*enabled
            (recur (inc i))))
        (catch Exception e
          (println e)))))

  (let [_ (reset! *enabled true)
        session-id     (str "gamecode" @*acc)
        client-id      (str @*acc "21")
        server-ip      "147.182.133.53"
        server-port    8080
        client-puncher (make-hole-puncher server-ip server-port {:session-id session-id
                                                                 :client-id client-id})]
    (def client-puncher client-puncher)
    (future
      (try
        (loop [i 0]
          (debug! {} :CLIENT_I i)
          (vp/with-arena _
            (send! client-puncher (vybe.type/Translation [1 5 220]))
            (update! client-puncher 1/60))
          (Thread/sleep 16)
          (when @*enabled
            (recur (inc i))))
        (catch Exception e
          (println e)))))

  ())
