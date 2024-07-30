(ns vybe.netcode
  (:require
   [vybe.netcode.c :as vn.c]
   [vybe.panama :as vp])
  (:import
   (org.vybe.netcode netcode netcode$netcode_init netcode$netcode_term
                     netcode_server_config_t netcode_client_config_t)))

(vp/defcomp netcode_server_config (netcode_server_config_t/layout))
(vp/defcomp netcode_client_config (netcode_client_config_t/layout))

(defn init!
  "Initiate netcode."
  []
  (-> (netcode$netcode_init/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (into-array Object []))))
#_ (init!)

(comment

  ;; -- Server
  (do
    (let [_ (do (vn.c/netcode-log-level (netcode/NETCODE_LOG_LEVEL_DEBUG))
                (init!))
          server-address "127.0.0.1:40000"
          server-config (netcode_server_config
                         {:protocol_id 0x1122334455667788
                          :private_key (vp/arr [0x60, 0x6a, 0xbe, 0x6e, 0xc9, 0x19, 0x10, 0xea,
                                                0x9a, 0x65, 0x62, 0xf6, 0x6f, 0x2b, 0x30, 0xe4,
                                                0x43, 0x71, 0xd6, 0x2c, 0xd1, 0x99, 0x27, 0x26,
                                                0x6b, 0x3c, 0x60, 0xf4, 0xb7, 0x15, 0xab, 0xa1]
                                               :byte)})]
      (vn.c/netcode-default-server-config server-config)
      (def server (vn.c/netcode-server-create server-address server-config 0.0)))

    (vn.c/netcode-server-start server (netcode/NETCODE_MAX_CLIENTS))

    (defn server-update
      [server time]
      (vn.c/netcode-server-update server time)

      ;; Send a message to the client 0, if connected.
      (when (pos? (vn.c/netcode-server-client-connected server 0))
        (vn.c/netcode-server-send-packet server 0 (vp/arr (range 10) :byte) 10))

      (doseq [client-idx (range (netcode/NETCODE_MAX_CLIENTS))]
        (loop []
          (let [packet-bytes (vp/int* 0)
                packet-sequence (vp/long* 0)
                packet (vn.c/netcode-server-receive-packet server client-idx packet-bytes packet-sequence)]
            (when-not (vp/null? packet)
              (println :PACKET_SERVER (vp/p->value packet-bytes :int) '__ (vp/arr packet 10 :byte))
              (vn.c/netcode-server-free-packet server packet)
              (recur))))))

    #_(server-update server 0.4))

  ;; -- Client
  (do
    (let [#_ #__ (do (vn.c/netcode-log-level (netcode/NETCODE_LOG_LEVEL_INFO))
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
     (doto (vp/arr 1 :pointer) (vp/set* 0 "127.0.0.1:40000"))
     (doto (vp/arr 1 :pointer) (vp/set* 0 "127.0.0.1:40000"))
     300 50 (vp/p->value client-id :long) 0x1122334455667788 private-key user-data connect-token)

    (vn.c/netcode-client-connect client connect-token)

    (defn client-update
      [client time]
      (vn.c/netcode-client-update client time)

      ;; Send a message to the server, if connected.
      (when (= (vn.c/netcode-client-state client) (netcode/NETCODE_CLIENT_STATE_CONNECTED))
        (let [initial (rand-int 100)]
          (vn.c/netcode-client-send-packet client (vp/arr (range initial (+ initial 10)) :byte) 10)))

      (loop []
        (let [packet-bytes (vp/int* 0)
              packet-sequence (vp/long* 0)
              packet (vn.c/netcode-client-receive-packet client packet-bytes packet-sequence)]
          (when-not (vp/null? packet)
            (println :PACKET_CLIENT (vp/p->value packet-bytes :int) '__ (vp/arr packet 10 :byte))
            (vn.c/netcode-client-free-packet client packet)
            (recur))))))

  #_(client-update client 0.6)

  (time
   (let [i 2
         t-range (range i (inc i) 0.01)]
     (->> [(future
             (doseq [t t-range]
               (server-update server t)
               (Thread/sleep 1)))

           (future
             (doseq [t t-range]
               (client-update client t)
               (Thread/sleep 1)))]
          (mapv deref))))

  ())

(defn close!
  "Finish netcode."
  []
  (-> (netcode$netcode_term/makeInvoker (into-array java.lang.foreign.MemoryLayout []))
      (.apply (into-array Object []))))
#_ (close!)
