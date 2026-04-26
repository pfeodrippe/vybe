(ns vybe.network
  (:require
   [vybe.netcode.c :as vn.c]
   [aleph.udp :as udp]
   [clj-commons.byte-streams :as bs]
   [manifold.stream :as s]
   [clojure.string :as str]
   [clojure.pprint :as pp]
   [clojure.set :as set]
   [clojure.edn :as edn]
   [potemkin :refer [defprotocol+]]
   [vybe.wasm :as vp]
   [vybe.type :as vt]
   [vybe.flecs :as vf]
   [vybe.util :as vy.u])
  (:import
   (java.time Instant)
   (vybe.flecs VybeFlecsEntitySet)))

(set! *warn-on-reflection* true)

(def ^:private netcode-constants
  {:CN_CLIENT_STATE_CONNECTED 3
   :CN_CONNECT_TOKEN_SIZE 1114
   :CN_CONNECT_TOKEN_USER_DATA_SIZE 256
   :CN_SERVER_EVENT_TYPE_NEW_CONNECTION 0
   :CN_SERVER_EVENT_TYPE_DISCONNECTED 1
   :CN_SERVER_EVENT_TYPE_PAYLOAD_PACKET 2})

(defn- netcode-constant
  [constant]
  (or (get netcode-constants constant)
      (throw (ex-info "Netcode constant is not available"
                      {:constant constant}))))

(vp/defcomp endpoint_t
  "Representation of a netcode endpoint."
  [[:type :int]
   [:port :short]])

(vp/defcomp result_t
  "Layout-compatible result map for netcode calls."
  [[:code :int]
   [:details :pointer]])

(vp/defcomp crypto_sign_public_t
  "Public key used by the netcode crypto API."
  [[:key [:vec {:size 32} :byte]]])

(vp/defcomp crypto_sign_secret_t
  "Secret key used by the netcode crypto API."
  [[:key [:vec {:size 64} :byte]]])

(vp/defcomp new_connection_t
  [[:client_index :int]
   [:client_id :long]
   [:endpoint endpoint_t]])

(vp/defcomp disconnected_t
  [[:client_index :int]])

(vp/defcomp payload_packet_t
  [[:client_index :int]
   [:data :pointer]
   [:size :int]])

(vp/defcomp server_event_union_t
  [[:new_connection new_connection_t]
   [:disconnected disconnected_t]
   [:payload_packet payload_packet_t]])

(vp/defcomp server_event_t
  "Layout describing a server-side event from the netcode API."
  [[:type :int]
   [:u server_event_union_t]])

(defonce ^:private lock (Object.))

(defn debug!
  "Log network debugging information.

  Accepts a map with `:vn/client-id` under `:vn/keys` and any number of
  additional message parts. Messages are printed using `println` while a
  small global lock ensures lines remain coherent when multiple threads
  log concurrently.

  Example:
    (debug! {:vn/client-id 42} :SERVER :PACKET payload)"
  [{:vn/keys [client-id]} & msgs]
  (locking lock
    (apply println :DEBUG_NET :client-id client-id msgs)
    #_(vy.u/debug :DEBUG_NET :client-id client-id msgs)))

(defn- cn-crypto-generate-key
  "Invoke the netcode `cn_crypto_generate_key` helper and return a newly
  allocated key segment.

  This function returns the representation expected by the netcode helper
  functions."
  []
  (vn.c/cn-crypto-generate-key))

(defn- timestamp
  "Return the current POSIX epoch second as a long.

  Used to provide time stamps for netcode operations (connect token
  expiration and update timestamps)."
  []
  (.getEpochSecond (Instant/now)))

(def ^:private -packet-data-size 1024)

(vp/defcomp PacketData
  "In-memory layout for a network packet used by our netcode layer.

  Fields:
    - :size  - total size of the payload data
    - :kind  - type id used to (de)serialize the payload
    - :eid   - entity id if the packet is tied to an entity, -1 otherwise
    - :data  - raw payload bytes (fixed-size vector)

  This layout is used by `-make-packet` and `-packet-deser` to serialize
  and deserialize payload kinds (component maps, EDN values and opaque
  values). The `:size` field is computed relative to the fixed component
  size and the contained payload's byte-size."
  [[:size :int]
   [:kind :int]
   [:eid :long]
   [:data [:vec {:size -packet-data-size} :byte]]])

(def ^:private -packet-component-size
  (.byteSize (.layout PacketData)))

(def ^:private -packet-data-offset
  (- -packet-component-size -packet-data-size))

(def ^:private kinds-ser
  {String -10
   clojure.lang.IObj -20})

(def ^:private kinds-deser
  {-10 vp/->string
   -20 (comp edn/read-string vp/->string)})

(defn -pmap->msg
  "Convert a component-backed map into a simple message map describing
  the component schema.

  Returned map contains keys:
    - :vn.msg/type  => :vn.msg.type/component
    - :kind         => numeric kind id used by the netcode wire format
    - :name         => symbol name of the component layout
    - :schema       => vector of [field field-type] entries describing
                        the component's schema (types may be primitive
                        types or component layout symbols)."
  [pmap]
  (let [c (vp/component pmap)
        schema (->> (.fields c)
                    (mapv (fn [[field {:keys [type]}]]
                            [field (if (vp/component? type)
                                     (symbol (.get (.name (vp/layout type))))
                                     type)])))]
    {:vn.msg/type :vn.msg.type/component
     :kind (vp/cache-comp c)
     :name (symbol (.get (.name (.layout c))))
     :schema schema}))
#_(-pmap->msg (vt/Translation))
#_(-pmap->msg (PacketData))

#_(vp/defcomp Dadasd
    [[:a vt/Translation]])
#_(-pmap->msg (Dadasd))

(defn -entity->msg
  "Create a small message map describing an entity for the netcode
  protocol.

  The returned map has keys :vn.msg/type (set to
  :vn.msg.type/entity), :eid (the numeric entity id) and :name which is
  the flecs entity name. Used to inform peers about entity ids and
  names when entities are first referenced over the network."
  [^VybeFlecsEntitySet em]
  {:vn.msg/type :vn.msg.type/entity
   :eid (.id em)
   :name (vf/get-name em)})
#_(-> (vf/make-world)
      (merge {:a [:bgg]})
      :a
      -entity->msg)

(defn -make-packet
  "Serialize a value into a `PacketData` layout suitable for the
  netcode send helpers.

  Arguments:
    - entity - optional flecs entity or entity-like value used to
               populate the `:eid` field; if not provided the eid is -1
    - v      - the payload to serialize; allowed payloads:
               * a component-backed map
               * any IObj (serialized via `pr-str` and sent as EDN)
               * other values which are serialized via `vp/mem`

  The function chooses the appropriate kind id and stores the payload
  into the fixed-size PacketData `:data` field. Returns a `PacketData`
  instance ready to pass to send APIs."
  ([v]
   (-make-packet nil v))
  ([entity v]
   (let [eid (or (some-> entity vf/eid) -1)]
     (cond
       (vp/pmap? v)
       (let [c (vp/component v)]
         (PacketData
          {:size (+ -packet-data-offset (.byteSize (.layout c)))
           :kind (vp/cache-comp c)
           :eid eid
           :data v}))

       (instance? clojure.lang.IObj v)
       (let [mem (vp/mem (pr-str v))]
         (PacketData
          {:size (+ -packet-data-offset (.byteSize mem))
           :kind (get kinds-ser clojure.lang.IObj)
           :eid eid
           :data mem}))

       :else
       (let [mem (vp/mem v)]
         (PacketData
          {:size (+ -packet-data-offset (.byteSize mem))
           :kind (get kinds-ser (type v))
           :eid eid
           :data mem}))))))
#_ (-packet-deser (vp/mem (-make-packet 30 {:a 4})))
#_ (-packet-deser (vp/mem (-make-packet 50 (vt/Translation {:x 30}))))
#_ (-packet-deser (vp/mem (-make-packet (-pmap->msg (vt/Translation {:x 30})))))
#_ (-packet-deser (vp/mem (-make-packet "abcde")))
#_ (-packet-deser (vp/mem (-make-packet {:a 4})))

(defonce ^:private *tracker (atom {}))

(defn -packet-deser
  "Deserialize a `PacketData` memory-backed value into a Clojure value.

  By default the function uses `vp/comp-cache` to resolve component kinds
  back into layouts. When the packet `:kind` is one of the special
  built-in kinds (EDN or string) the function uses the `kinds-deser`
  table to convert the payload into a Clojure value. Returns a vector
  [eid value] where eid is nil when the packet was not bound to an
  entity (eid == -1 on the wire)."
  ([packet-data]
   (-packet-deser vp/comp-cache packet-data))
  ([get-f packet-data]
   (let [{:keys [kind eid] :as packet-value} (vp/p->map packet-data PacketData)
         data-mem (.asSlice (vp/mem packet-value) -packet-data-offset)]
     [(when-not (neg? eid)
        eid)
      (if-let [f (get kinds-deser kind)]
        (f data-mem)
        (vp/clone (vp/p->value data-mem (get-f kind))))])))

(defn -server-send!
  "Send a packet from a server instance to a connected client.

  The function accepts a `server` object from `vn.c`, a `client-index`
  identifying the targeted client on the server and `msg` which will be
  marshalled with `-make-packet`.

  Options:
    - :reliable (boolean) - whether to use reliable delivery
    - :entity (flecs entity) - an optional entity to inform the peer

  Side effects: may send additional informative messages (entity or
  component schema) the first time an entity/component is referenced by
  a particular client. Returns nil."
  ([server client-index msg]
   (-server-send! server client-index msg {}))
  ([server client-index msg {:keys [reliable entity]
                             :or {reliable false}}]
   (when (vn.c/cn-server-is-client-connected server client-index)
     (let [{:keys [size] :as data} (-make-packet entity msg)]

       ;; Setup the entity id in the other party.
       (when (and entity
                  (vf/entity? entity)
                  (not (get @*tracker [server client-index :vn.tracker.entity/sent (.id ^VybeFlecsEntitySet entity)])))
         (-server-send! server client-index (-entity->msg entity) {:reliable true})
         (swap! *tracker assoc [server client-index :vn.tracker.entity/sent (.id ^VybeFlecsEntitySet entity)] true))

       ;; When it's a pmap and it's the first time we are sending this component
       ;; to the other part, send the schema reliably.
       (when (and (vp/pmap? msg)
                  (not (get @*tracker [server client-index :vn.tracker.component/sent (vp/component msg)])))
         (-server-send! server client-index (-pmap->msg msg) {:reliable true})
         (swap! *tracker assoc [server client-index :vn.tracker.component/sent (vp/component msg)] true))

       (vn.c/cn-server-send server data size client-index reliable)))))

(defn -client-send!
  "Send a packet from the client instance to its connected server.

  The function checks client connection state and marshals `msg` using
  `-make-packet`. Similar to `-server-send!`, it may also send entity or
  component schema information the first time a value is referenced.

  Options: same as `-server-send!` (`:reliable`, `:entity`). Returns nil."
  ([client msg]
   (-client-send! client msg {}))
  ([client msg {:keys [reliable entity]
                :or {reliable false}}]
   (when (= (vn.c/cn-client-state-get client) (netcode-constant :CN_CLIENT_STATE_CONNECTED))
     (let [{:keys [size] :as data} (-make-packet entity msg)]

       ;; Setup the entity id in the other party.
       (when (and entity
                  (vf/entity? entity)
                  (not (get @*tracker [client :vn.tracker.entity/sent (.id ^VybeFlecsEntitySet entity)])))
         (-client-send! client (-entity->msg entity) {:reliable true})
         (swap! *tracker assoc [client :vn.tracker.entity/sent (.id ^VybeFlecsEntitySet entity)] true))

       ;; When it's a pmap and it's the first time we are sending this component
       ;; to the other party, send the schema reliably.
       (when (and (vp/pmap? msg)
                  (not (get @*tracker [client :vn.tracker.component/sent (vp/component msg)])))
         (-client-send! client (-pmap->msg msg) {:reliable true})
         (swap! *tracker assoc [client :vn.tracker.component/sent (vp/component msg)] true))

       (vn.c/cn-client-send client data size reliable)))))

(defn connected?
  "Check if it's connected."
  [{:vn/keys [*state]}]
  (when *state
     (or (when-let [server (:vn/server @*state)]
           (vn.c/cn-server-is-client-connected server 0))
         (when-let [client (:vn/client @*state)]
           (= (vn.c/cn-client-state-get client) (netcode-constant :CN_CLIENT_STATE_CONNECTED))))))

(defn send!
  "Send a message through a puncher.

  Arguments:
    - puncher : map produced by `make-hole-puncher` (holds :vn/*state)
    - msg     : message payload (component, EDN serializable value, or string)

  Options map accepts:
    - :client-index - index of the client when operating as server (default 0)
    - :reliable     - boolean flag for reliable delivery (default false)
    - :entity       - optional flecs entity associated with the payload

  Returns nil. The function dispatches to the server or client native
  send helper depending on the puncher state."
  ([puncher msg]
   (send! puncher msg {}))
  ([{:vn/keys [*state]} msg {:keys [client-index reliable entity]
                             :or {client-index 0
                                  reliable false}}]
   (when *state
     (or (when-let [server (:vn/server @*state)]
           (-server-send! server client-index msg {:reliable reliable
                                                   :entity entity}))
         (when-let [client (:vn/client @*state)]
           (-client-send! client msg {:reliable reliable
                                      :entity entity}))))))

(defn host?
  "Predicate that returns true when the puncher is a host.

  Expects a puncher map created by `make-hole-puncher` and looks up the
  `:vn/is-host` key under `:vn/keys`."
  [{:vn/keys [is-host]}]
  is-host)

(defn -server-update!
  "Process queued server events from the netcode server and return a
  vector of message maps produced by incoming packets.

  The function advances internal server state, pops events and handles
  new connections, incoming payload packets and disconnections. For
  payload packets the function deserializes the data and, when
  appropriate, updates internal trackers for components and entities.

  Returns a vector of message maps with :client-index, :eid,
  :entity-name and :data keys representing received packets that the
  caller should process."
  [server delta-time]
  (swap! *tracker update-in [server :counter] (fnil inc 0))
  (vn.c/cn-server-update server delta-time (timestamp))

  (let [event (server_event_t)
        *msgs (atom [])]
    (while (vn.c/cn-server-pop-event server event)
      (condp = (:type event)
        ;; -- NEW CONNECTION
        (netcode-constant :CN_SERVER_EVENT_TYPE_NEW_CONNECTION)
        (debug! {} :SERVER :NEW_CONNECTION
                (-> event :u :new_connection :client_index)
                (-> event :u :new_connection :client_id)
                (-> event :u :new_connection :endpoint))

        ;; -- PAYLOAD
        (netcode-constant :CN_SERVER_EVENT_TYPE_PAYLOAD_PACKET)
        (let [packet-data (-> event :u :payload_packet :data)
              packet-size (-> event :u :payload_packet :size)
              client-index (-> event :u :payload_packet :client_index)
              [eid msg] (-packet-deser #(get @*tracker [server client-index :vn.tracker.component/received %])
                                       packet-data)]

          ;; If we receive a component (which contains the schema) or entity,
          ;; we associate it with this client index.
          (let [{:vn.msg/keys [type]} msg]
            (case type
              :vn.msg.type/component
              (let [{:keys [kind name schema]} msg
                    ;; We get comp cache twice so we have the component in the end.
                    c (or (-> name vp/comp-cache vp/comp-cache)
                          (vp/make-component name schema))]
                (swap! *tracker assoc [server client-index :vn.tracker.component/received kind] c))

              :vn.msg.type/entity
              (let [{:keys [eid name]} msg]
                (swap! *tracker assoc [server client-index :vn.tracker.entity/received eid] name))

              nil))

          (debug! {} :SERVER :PACKET packet-size client-index msg)

          (when-not (= msg "ALIVE")
            (swap! *msgs conj {:client-index (-> event :u :payload_packet :client_index)
                               :eid eid
                               :entity-name (get @*tracker [server client-index :vn.tracker.entity/received eid])
                               :data msg}))

          (vn.c/cn-server-free-packet server
                                      (-> event :u :payload_packet :client_index)
                                      (-> event :u :payload_packet :data))

          (let [msg "ALIVE"]
            (-server-send! server (-> event :u :payload_packet :client_index) msg)))

        ;; -- DISCONNECTED
        (netcode-constant :CN_SERVER_EVENT_TYPE_DISCONNECTED)
        (debug! {} :SERVER :DISCONNECTED
                (-> event :u :disconnected :client_index))))
    @*msgs))

(defn -client-update!
  "Process incoming packets for a client and return a vector of message
  maps (same shape as `-server-update!`).

  The function reads packets from the client instance, deserializes
  their payloads, updates component/entity caches and returns the
  collected messages for higher-level logic to process. It also
  periodically sends heartbeat \"ALIVE\" messages to the server."
  [client delta-time]
  (swap! *tracker update-in [client :counter] (fnil inc 0))
  (vn.c/cn-client-update client delta-time (timestamp))

  (when (= (vn.c/cn-client-state-get client) (netcode-constant :CN_CLIENT_STATE_CONNECTED))
    (let [packet-size (vp/int* 0)
          packet (vp/arr 1 :pointer)
          *msgs (atom [])]
      (while (vn.c/cn-client-pop-packet client packet packet-size vp/null)
        (let [[eid msg] (-packet-deser #(get @*tracker [client :vn.tracker.component/received %])
                                       (vp/reinterpret (vp/get-at packet 0) -packet-component-size))]

          ;; If we receive a component (which contains the schema) or entity,
          ;; we associate it with this server.
          (let [{:vn.msg/keys [type]} msg]
            (case type
              :vn.msg.type/component
              (let [{:keys [kind name schema]} msg
                    ;; We get comp cache twice so we have the component in the end.
                    c (or (-> name vp/comp-cache vp/comp-cache)
                          (vp/make-component name schema))]
                (swap! *tracker assoc [client :vn.tracker.component/received kind] c))

              :vn.msg.type/entity
              (let [{:keys [eid name]} msg]
                (swap! *tracker assoc [client :vn.tracker.entity/received eid] name))

              nil))

          (debug! {} :CLIENT :PACKET (vp/p->value packet-size :int) msg)

          (when-not (= msg "ALIVE")
            (swap! *msgs conj {:client-index -1
                               :eid eid
                               :entity-name (get @*tracker [client :vn.tracker.entity/received eid])
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
      (let [msgs (-server-update! server 1/60)]
        (when (seq msgs)
          (debug! {} :SERVER :MSGS msgs)))
      (Thread/sleep 16))))

(defn- -cn-client-iter
  [client i]
  (let [t-range (range i (+ i 1) 0.016)]
    (doseq [t t-range]
      (let [msgs (-client-update! client 1/60)]
        (when (seq msgs)
          (debug! {} :CLIENT :MSGS msgs)))
      (Thread/sleep 16))))

(defn -cn-server
  "Create and start a netcode server bound to `server-address`.

  Arguments:
    - server-address : string address:port the server listens on
    - application-id  : numeric application id used by netcode
    - public-key      : public key segment or map with :key
    - secret-key      : secret key segment or map with :key

  Returns the server object. Throws if server startup returns an error."
  [server-address application-id public-key secret-key]
  (let [endpoint (endpoint_t)
        _ (vn.c/cn-endpoint-init endpoint server-address)
        server-config (-> (vn.c/cn-server-config-defaults)
                          (merge {:application_id application-id
                                  :public_key (if (vp/arr? public-key)
                                                {:key public-key}
                                                (vp/arr (:key public-key) :byte))
                                  :secret_key (if (vp/arr? secret-key)
                                                {:key secret-key}
                                                (vp/arr (:key secret-key) :byte))}))
        server (vn.c/cn-server-create server-config)
        result (vn.c/cn-server-start server server-address)]
    (when (vn.c/cn-is-error result)
      (vn.c/cn-server-destroy server)
      (throw (ex-info "Couldn't start CN server" {:error result})))
    server))

(defn -cn-server-destroy
  "Destroy/stop a netcode server and free associated resources."
  [server]
  (vn.c/cn-server-destroy server))

(defn -cn-gen-keys
  "Generate a new public/secret signing key pair using the netcode
  crypto API.

  Returns a vector [public-key secret-key] where each element is a
  key layout instance that can be passed to connect token generation or
  server/client configuration."
  []
  (let [public-key (crypto_sign_public_t)
        secret-key (crypto_sign_secret_t)]
    (vn.c/cn-crypto-sign-keygen public-key secret-key)
    [public-key secret-key]))

(defn -cn-connect-token
  "Create a connect token byte-array used by the netcode client.

  Arguments are server address, application id, client id and the
  secret key. The returned value is a byte array suitable for passing to
  `-cn-client`. The function will throw if token generation fails."
  [server-address application-id client-id secret-key]
  (let [connect-token (vp/arr (netcode-constant :CN_CONNECT_TOKEN_SIZE) :byte)
        client-to-server-key (cn-crypto-generate-key)
        server-to-client-key (cn-crypto-generate-key)
        current-ts (timestamp)
        expiration-ts (+ current-ts 60)
        handshake-timeout 5
        endpoints (doto (vp/arr 1 :pointer) (vp/set-at 0 server-address))
        user-data (vp/arr (netcode-constant :CN_CONNECT_TOKEN_USER_DATA_SIZE) :byte)
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

(defn -cn-client
  "Create a netcode client and connect it using the provided connect-token.

  Returns the client instance. Throws on connect errors."
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
      (let [[public-key secret-key] (-cn-gen-keys)]
        (def cn-public-key public-key)
        (def cn-secret-key secret-key)

        (defonce test-lock (Object.))

        (defonce server nil)
        (defonce client nil)

        (def my-world
          (merge (vf/make-world) {:ddd [:some-c {:aaa [:bbb]}]}))

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
    (-cn-server server-address application-id cn-public-key cn-secret-key))

  ;; -- Client
  (def client
    (-cn-client (-cn-connect-token server-address application-id 10 cn-secret-key) 43001 application-id))

  (do (reset! *enabled true)
      (future
        (try
          (loop [i 0]
            (debug! {} :SERVER_I i)
            (vp/with-arena _
              (-server-send! server 0 (vybe.type/Translation [2 10 440]) {:entity (my-world (vf/path [:ddd :aaa]))})
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
          (-client-send! client (vt/Translation [2 10 44]) {:entity (my-world :ddd)})
          (-cn-client-iter client i))
        (when @*enabled
          (recur (inc i))))
      (catch Exception e
        (println e))))

  ())

;; -- Puncher.
(defn -socket-put!
  "Send a raw UDP/public socket message via the puncher socket.

  The function puts a simple map {:host :port :message} onto the
  manifold socket stream consumed by the puncher. This is used by the
  hole-punching flow to exchange control and token fragments with peers."
  [{:vn/keys [host port *state] :as puncher} msg]
  (debug! puncher :PUT msg)
  (s/put! (:vn/socket @*state)
          {:host host
           :port port
           :message msg}))

(defn -session-msg
  "Format a session advertisement message used by the puncher host.

  Produces a compact string used by the rendezvous server to indicate a
  hosting session: \"rs:<session-id>:<num-of-players>\"."
  [session-id num-of-players]
  (str "rs:" session-id ":" num-of-players))

(defn -client-msg
  "Format a client join message used by the puncher to contact the
  rendezvous server: \"rc:<client-id>:<session-id>\"."
  [session-id client-id]
  (str "rc:" client-id ":" session-id))

(defn -make-socket
  "Create an Aleph UDP socket and wire its incoming stream to the
  provided callback function. Returns the socket object."
  [callback]
  (let [socket @(udp/socket {})]
    (->> socket (s/consume callback))
    socket))

(declare -puncher-consumer)

(defn puncher-socket!
  "Close actual socket (if any) and create a new one."
  [{:vn/keys [*state] :as puncher}]
  (when-let [socket (:vn/socket @*state)]
    (debug! puncher :SOCKET_CLOSE socket)
    (s/close! socket))
  (let [socket (-make-socket #(try
                               (-puncher-consumer puncher %)
                               (catch Exception e
                                 (println e)
                                 (throw e))))]
    (swap! *state (fn [state]
                    (merge state {:vn/socket socket}))))
  puncher)

(defn- -serialize
  "Serialize data to a compact EDN-based wire string used on the UDP
  puncher channel. A small prefix `#EDN` is attached so the receiver
  can quickly detect EDN payloads."
  [data]
  (str "#EDN" (pr-str data)))

(defn -puncher-consumer
  "Consume raw UDP messages received by the puncher socket and drive
  the hole-punching and netcode bootstrap protocol.

  The function decodes messages prefixed with `#EDN` and handles token
  fragments, greeting messages and peer discovery. When acting as a
  host it will assemble connect tokens and start a local CN server; as
  a client it will assemble the token parts and start a CN client.

  The function mutates the puncher `:vn/*state` atom and may spawn
  background futures for server/client lifecycle operations."
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
                    [public-key secret-key] (-cn-gen-keys)
                    connect-token (-cn-connect-token server-address #_(str "0.0.0.0:" local-port)
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
                    (let [server (-cn-server #_server-address #_(str "127.0.0.1:" local-port)
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
                    client (-cn-client connect-token local-port 12345)]
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
          (-socket-put! puncher (-client-msg session-id client-id))))

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
      (-socket-put! puncher (-session-msg (:vn/session-id puncher) (:vn/num-of-players puncher)))
      (-socket-put! puncher (-client-msg (:vn/session-id puncher) (:vn/client-id puncher))))
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
            (send! host-puncher (vybe.type/Translation [2 10 440]))
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
