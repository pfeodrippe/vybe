(ns vybe.flecs.embedded
  "Embedded-JVM controller for a Flecs world owned by a native application."
  (:require
   [nrepl.server :as nrepl]
   [vybe.ai.gemma :as gemma]
   [vybe.flecs :as vf]
   [vybe.flecs.native :as vf.native])
  (:import
   (java.lang.foreign FunctionDescriptor Linker MemoryLayout SymbolLookup)
   (java.util.concurrent ConcurrentLinkedQueue Executors ExecutorService)))

(defonce ^:private *sessions (atom {}))
(defonce ^:private *nrepl-server (atom nil))

(defn- -int-env
  [name default-value]
  (or (some-> (System/getenv name) parse-long) default-value))

(defn- -wake-native!
  []
  (try
    (when-let [address (.orElse (.find (SymbolLookup/loaderLookup) "vybe_jvm_wake") nil)]
      (let [handle (.downcallHandle (Linker/nativeLinker)
                                    address
                                    (FunctionDescriptor/ofVoid
                                     (make-array MemoryLayout 0)))]
        (.invokeWithArguments handle (java.util.List/of))))
    (catch Throwable _
      ;; The native companion also performs a low-frequency fallback poll.
      nil)))

(defn- -runtime-ns
  [world-address]
  (let [n (symbol (str "vybe.flecs.live.world_" world-address))]
    (create-ns n)
    (binding [*ns* (the-ns n)]
      (clojure.core/refer 'clojure.core)
      (require '[vybe.flecs :as vf]
               '[vybe.panama :as vp]
               '[vybe.flecs.c :as vf.c]))
    n))

(defn- -start-nrepl!
  []
  (when-let [port (some-> (System/getenv "VYBE_NREPL_PORT") parse-long)]
    (when (and (pos? port) (nil? @*nrepl-server))
      (reset! *nrepl-server
              (nrepl/start-server :bind "127.0.0.1" :port port)))
    @*nrepl-server))

(defn- -stop-nrepl!
  []
  (when-let [server @*nrepl-server]
    (nrepl/stop-server server)
    (reset! *nrepl-server nil)))

(defn- -system-symbol
  [form]
  (when (and (seq? form)
             (contains? #{'defsystem-c 'vf/defsystem-c 'vybe.flecs/defsystem-c}
                        (first form))
             (symbol? (second form)))
    (second form)))

(defn- -compile-system
  [session source]
  (let [form (binding [*read-eval* false] (read-string source))
        system-name (-system-symbol form)]
    (when-not system-name
      (throw (ex-info "Expected one vf/defsystem-c form"
                      {:source source})))
    (binding [*ns* (the-ns (:runtime-ns session))]
      (vf/with-component-registry (:registry session)
        (eval form))
      {:name system-name
       :builder (ns-resolve *ns* system-name)
       :source source})))

(defn- -install-system!
  [session compiled]
  (let [name (:name compiled)
        previous (get @(:active-systems session) name)]
    (try
      ((:builder compiled) (:world session))
      (swap! (:active-systems session) assoc name compiled)
      (swap! (:system-versions session) update name (fnil conj []) compiled)
      (swap! (:history session) conj
             {:event :installed :system name :source (:source compiled)
              :at (System/currentTimeMillis)})
      (catch Throwable throwable
        (when previous
          ((:builder previous) (:world session)))
        (swap! (:history session) conj
               {:event :install-failed :system name :error (str throwable)
                :at (System/currentTimeMillis)})
        (throw throwable)))))

(defn- -rollback-system!
  [session system-name]
  (let [versions (get @(:system-versions session) system-name)]
    (when (< (count versions) 2)
      (throw (ex-info "System does not have a previous installed version"
                      {:system system-name
                       :version-count (count versions)})))
    (let [current (peek versions)
          previous (peek (pop versions))]
      (try
        ((:builder previous) (:world session))
        (swap! (:system-versions session) update system-name pop)
        (swap! (:active-systems session) assoc system-name previous)
        (swap! (:history session) conj
               {:event :rolled-back :system system-name
                :source (:source previous) :at (System/currentTimeMillis)})
        (catch Throwable throwable
          ;; Reinstalling the current builder keeps the live system usable if
          ;; applying the previous version fails for an environmental reason.
          ((:builder current) (:world session))
          (swap! (:history session) conj
                 {:event :rollback-failed :system system-name
                  :error (str throwable) :at (System/currentTimeMillis)})
          (throw throwable))))))

(defn- -enqueue!
  [session f]
  (.add ^ConcurrentLinkedQueue (:main-queue session) f)
  (-wake-native!)
  true)

(defn submit-source!
  "Compile a `defsystem-c` form off the game thread, then install it on poll."
  [world-address source]
  (let [session (get @*sessions (long world-address))]
    (when-not session
      (throw (ex-info "No embedded Vybe session for world" {:world-address world-address})))
    (.submit ^ExecutorService (:executor session)
             ^Runnable
             (fn []
               (try
                 (let [compiled (-compile-system session source)]
                   (-enqueue! session #(-install-system! session compiled)))
                 (catch Throwable throwable
                   (.printStackTrace throwable System/err)
                   (swap! (:history session) conj
                          {:event :compile-failed :error (str throwable)
                           :source source :at (System/currentTimeMillis)})))))
    true))

(defn request-system!
  "Ask the configured local Gemma 4 26B A4B Q4 endpoint for a system."
  [world-address request]
  (let [session (get @*sessions (long world-address))]
    (when-not session
      (throw (ex-info "No embedded Vybe session for world" {:world-address world-address})))
    (.submit ^ExecutorService (:executor session)
             ^Runnable
             (fn []
               (try
                 (let [max-attempts (-int-env "VYBE_AI_REPAIR_ATTEMPTS" 3)
                       compiled
                       (loop [attempt 1
                              feedback nil]
                         (let [generation-request
                               (cond-> request
                                 feedback
                                 (str "\n\nThe previous generated form did not compile. "
                                      "Fix it and return the complete replacement form. "
                                      "Compiler feedback: " feedback))
                               source (gemma/generate-system
                                       {:request generation-request
                                        :registry (:registry session)})
                               result (try
                                        {:compiled (-compile-system session source)}
                                        (catch Throwable throwable
                                          {:error throwable}))]
                           (swap! (:history session) conj
                                  {:event :generated :request request :source source
                                   :attempt attempt :at (System/currentTimeMillis)})
                           (if-let [value (:compiled result)]
                             value
                             (if (< attempt max-attempts)
                               (let [message (str (:error result))]
                                 (recur (inc attempt)
                                        (subs message 0 (min 2000 (count message)))))
                               (throw (:error result))))))]
                   (if (= "1" (System/getenv "VYBE_AI_AUTO_APPLY"))
                     (-enqueue! session #(-install-system! session compiled))
                     (swap! (:proposals session) conj
                            {:name (:name compiled)
                             :source (:source compiled)
                             :compiled compiled
                             :request request
                             :at (System/currentTimeMillis)})))
                 (catch Throwable throwable
                   (.printStackTrace throwable System/err)
                   (swap! (:history session) conj
                          {:event :generation-failed :request request
                           :error (str throwable) :at (System/currentTimeMillis)})))))
    true))

(defn proposals
  [world-address]
  (some->> (get @*sessions (long world-address))
           :proposals
           deref
           (mapv #(dissoc % :compiled))))

(defn apply-proposal!
  "Queue a validated proposal, by vector index, for installation on the game thread."
  [world-address proposal-index]
  (let [session (get @*sessions (long world-address))]
    (when-not session
      (throw (ex-info "No embedded Vybe session for world" {:world-address world-address})))
    (let [selected (atom nil)]
      (swap! (:proposals session)
             (fn [items]
               (let [proposal-index (long proposal-index)]
                 (when (<= 0 proposal-index (dec (count items)))
                   (reset! selected (nth items proposal-index)))
                 (if @selected
                   (into (subvec items 0 proposal-index)
                         (subvec items (inc proposal-index)))
                   items))))
      (when-not @selected
        (throw (ex-info "Proposal index is out of range"
                        {:proposal-index proposal-index})))
      (-enqueue! session #(-install-system! session (:compiled @selected))))))

(defn rollback-system!
  "Queue a rollback to the previous installed version of a named system."
  [world-address system-name]
  (let [session (get @*sessions (long world-address))
        system-name (if (symbol? system-name) system-name (symbol (name system-name)))]
    (when-not session
      (throw (ex-info "No embedded Vybe session for world" {:world-address world-address})))
    (-enqueue! session #(-rollback-system! session system-name))))

(defn history
  [world-address]
  (some-> (get @*sessions (long world-address)) :history deref))

(defn world-addresses
  "Return the native Flecs worlds currently attached to the embedded JVM."
  []
  (vec (keys @*sessions)))

(defn attach!
  "Attach Vybe to a borrowed native world. Called by the JNI facade."
  [world-address]
  (let [world-address (long world-address)]
    (if (contains? @*sessions world-address)
      true
      (do
        ;; Requiring vybe.flecs has completed by this point, so ABI-size checks
        ;; are safe and happen before the borrowed pointer is used.
        (vf.native/verify-provider!)
        (let [world (vf/wrap-world world-address {:embedded? true})
              registry (vf/derive-components! world)
              session {:world world
                       :registry registry
                       :runtime-ns (-runtime-ns world-address)
                       :main-queue (ConcurrentLinkedQueue.)
                       :executor (Executors/newSingleThreadExecutor)
                       :active-systems (atom {})
                       :system-versions (atom {})
                       :proposals (atom [])
                       :history (atom [{:event :attached
                                        :reflected-count (:reflected-count registry)
                                        :opaque-count (:opaque-count registry)
                                        :at (System/currentTimeMillis)}])}]
          (swap! *sessions assoc world-address session)
          (try
            (-start-nrepl!)
            (catch Throwable throwable
              (.printStackTrace throwable System/err)
              (swap! (:history session) conj
                     {:event :nrepl-failed :error (str throwable)
                      :at (System/currentTimeMillis)})))
          (when-let [source (System/getenv "VYBE_JVM_BOOTSTRAP_SOURCE")]
            (when (seq source)
              (submit-source! world-address source)))
          (when-let [request (System/getenv "VYBE_AI_BOOTSTRAP_REQUEST")]
            (when (seq request)
              (request-system! world-address request)))
          true)))))

(defn poll!
  "Run queued world mutations on the native game thread."
  [world-address]
  (if-let [session (get @*sessions (long world-address))]
    (let [limit (-int-env "VYBE_JVM_MAX_ACTIONS_PER_POLL" 1)
          queue ^ConcurrentLinkedQueue (:main-queue session)]
      (loop [completed 0]
        (if (>= completed limit)
          completed
          (if-let [action (.poll queue)]
            (do (action) (recur (inc completed)))
            completed))))
    0))

(defn detach!
  "Detach without finalizing the borrowed Flecs world."
  [world-address]
  (when-let [session (get @*sessions (long world-address))]
    (.shutdownNow ^ExecutorService (:executor session))
    (vf/clear-component-registry! (:registry session))
    (swap! *sessions dissoc (long world-address))
    (when (empty? @*sessions)
      (-stop-nrepl!)))
  nil)
