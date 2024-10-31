(ns vybe.audio.overtone.sclang
  (:require
   [babashka.process :as proc]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [overtone.helpers.system :refer [get-os linux-os? mac-os? windows-os?]]
   [overtone.sc.machinery.server.connection :as ov.conn]
   [overtone.sc.synth :as ov.synth]))

(defn sclang-path
  "Returns sclang path, will throw if inexistent."
  []
  "/Applications/SuperCollider.app/Contents/MacOS/sclang")

(defn transpile
  "Converts hiccup-like syntax to a SC string."
  [sc-clj]
  (let [[op & body] (if (sequential? sc-clj)
                      sc-clj
                      [sc-clj])]
    (try
      (cond
        (and (= op :SynthDef) (sequential? sc-clj))
        (let [{:keys [args file-dir]
               synthdef-name :name}
              (first body)

              body (rest body)]
          {:sc-clj sc-clj
           :synthdef-name synthdef-name
           :file-path (.getAbsolutePath (io/file (str file-dir "/" synthdef-name ".scsyndef")))
           :synthdef-str (str "SynthDef"
                              "(" (str "\"" synthdef-name "\"" ",") " {\n"
                              (->> [(str "arg "
                                         (->> args
                                              (mapv (fn [[arg-identifier default]]
                                                      (str (name arg-identifier) "=" default)))
                                              (str/join ", "))
                                         ";")

                                    (->> body
                                         (mapv (fn [row]
                                                 (str (transpile row) ";")))
                                         (str/join "\n  "))]
                                   (mapv #(str "  " %))
                                   (str/join "\n"))
                              (format "\n}).writeDefFile(\"%s\").add;"
                                      file-dir))})

        (contains? #{:* :=} op)
        (->> body
             (mapv transpile)
             (str/join (str " " (name op) " ")))

        (contains? #{:.} op)
        (->> body
             (mapv transpile)
             (str/join (name op)))

        (= :vars op)
        (str "var "
             (->> body
                  (mapv (fn [var-identifier]
                          (name var-identifier)))
                  (str/join ", ")))

        (and (keyword? op) body)
        (str (name op)
             "( "
             (->> body
                  (mapv transpile)
                  (str/join ", "))
             " )")

        (and (or (keyword? op)
                 (symbol? op))
             (not body))
        (name op)

        (sequential? sc-clj)
        (str "["
             (->> sc-clj
                  (str/join ", "))
             "]")

        (number? sc-clj)
        sc-clj

        (string? sc-clj)
        (str "\"" sc-clj "\"")

        (map? sc-clj)
        (->> sc-clj
             (mapv (fn [[k v]]
                     (str (name k) ": " v)))
             (str/join ", "))

        :else
        (throw (ex-info "Unhandled expression while transpiling into SC"
                        {:op op
                         :body body})))
      (catch Exception e
        (throw (ex-info "Transpiler error"
                        {:op op
                         :body body}
                        e))))))

(defonce ^:private *procs
  (atom []))

(defn stop-procs!
  "Stop all running sclang process."
  []
  (mapv proc/destroy @*procs)
  (reset! *procs []))
#_(stop-procs!)

(defn -wrap-code-with-interpreter
  ([code-str]
   (-wrap-code-with-interpreter code-str {}))
  ([code-str {:keys [boot]
              :or {boot false}}]
   (let [boot-init-coll ["s.boot;"
                         "s.waitForBoot({"]
         boot-end-coll ["});"]

         app-clock-init-coll ["AppClock.sched(0.0,{ arg time;"]
         app-clock-end-coll ["});"]]
     (println code-str)
     (format (->> (concat
                   (when boot boot-init-coll)
                   app-clock-init-coll
                   [""
                    "try {this.interpret("
                    "%s"
                    ") }"
                    "  { |error|"
                    "    \""
                    ""
                    "ERROR"
                    "----------------------------------"
                    "\".postln;"
                    "    error.reportError;"
                    "    \"-------------------------------------"
                    ""
                    "\".postln;"
                    "  };"
                    ""]
                   app-clock-end-coll
                   (when boot boot-end-coll))
                  (str/join "\n"))
             (pr-str code-str)))))

(defn exec!
  "Execute a sclang script in the background.

  It returns a `babashka.process/process`, `out` and `err` are redirected
  to stdout.

  Call `stop-procs!` or `babaskha.process/destroy-tree` to kill the
  returned process.

  E.g.

    (exec! [:. :SynthDef :help])"
  [sc-clj]
  (stop-procs!)
  (let [temp-scd (io/file (str ".vybe/sc/" "_" (random-uuid) ".scd"))
        _port (or (:port @ov.conn/connection-info*)
                  (:port (:opts @ov.conn/connection-info*)))
        str (-wrap-code-with-interpreter (transpile sc-clj))]
    (io/make-parents temp-scd)
    (spit temp-scd str)
    (let [proc (proc/process {:out *out* :err *err*} (sclang-path) temp-scd)]
      (swap! *procs conj proc)
      proc)))
#_ (exec! [:. :FoaRotate :help])

(defn help
  "Open SC help window for a object."
  [obj-k]
  (exec! [:. obj-k :help]))
#_(help :SynthDef)

(defn- check-proc!
  [proc args]
  (loop [counter 5]
    (cond
      (zero? counter)
      (throw (ex-info "Process had an error"
                      (merge args
                             {:proc (proc/destroy-tree proc)})))

      (proc/alive? proc)
      (do (Thread/sleep 200)
          (recur (dec counter)))

      :else
      @proc)))

(defn -synthdef-save!
  [{:keys [synthdef-name file-path synthdef-str] :as args}]
  (let [temp-scd (io/file (str ".vybe/sc/" synthdef-name "_" (random-uuid) ".scd"))]
    (io/make-parents temp-scd)
    (spit temp-scd (-> synthdef-str
                       (str "\n\n0.exit;")
                       -wrap-code-with-interpreter))
    (stop-procs!)
    (check-proc! (proc/process {:out *out* :err :out} (sclang-path) temp-scd) args)
    (when-not (.exists (io/file file-path))
      (throw (ex-info "Error when defining a synthdef" args)))
    file-path))

(comment

  (def my-synth
    (-> [:SynthDef {:name 'event
                    :args [[:freq 240] [:amp 0.5] [:pan 0.0]]
                    :file-dir "resources"}
         [:vars :env]
         [:= :env [:EnvGen.ar
                   [:Env [0 1 1 0] [0.01 0.1 0.2]]
                   {:doneAction 2}]]
         [:Out.ar 0 [:Pan2.ar [:* [:Blip.ar :freq] :env :amp]
                     :pan]]]
        transpile
        -synthdef-save!
        ov.synth/synth-load))

  (my-synth :freq 100)

  ())

(defn synth
  "Defines a synth (SynthDef in SC) from a clojure data representation."
  [sc-clj]
  (-> sc-clj
      transpile
      -synthdef-save!
      ov.synth/synth-load
      ;; FIXME Temporary while we don't use the newest version of overtone.
      (vary-meta update :arglists eval)))

(defn SynthDef
  [opts & body]
  (into [:SynthDef (merge {:file-dir "resources"}
                          opts)]
        body))

(comment

  (def my-synth
    (synth
     (SynthDef
      {:name 'event
       :args [[:freq 120] [:amp 0.5] [:pan 0.0]]}
      [:vars :env]
      [:= :env [:EnvGen.ar
                [:Env [0 1 1 0] [0.01 0.1 0.2]]
                {:doneAction 2}]]
      [:Out.ar 0 [:Pan2.ar [:* [:Blip.ar :freq] :env :amp]
                  :pan]])))

  (my-synth)

  ())

(defmacro defsynth
  "Defines a synth (SynthDef in SC) from a clojure data representation."
  [s-name & s-form]
  {:arglists '([name doc-string? params sc-clj])}
  (let [[doc-string params sc-clj] (if (string? (first s-form))
                                      [(first s-form) (second s-form) (drop 2 s-form)]
                                      [nil (first s-form) (drop 1 s-form)])]
    `(do (def ~s-name
           (synth
            (SynthDef
             {:name (quote ~s-name)
              :args ~(->> params
                          (partition-all 2 2)
                          (mapv (fn [[arg default]]
                                  [(keyword arg) default])))}
             ~@sc-clj)))
         (alter-meta! (var ~s-name) merge (cond-> (meta ~s-name)
                                            ~doc-string (assoc :doc ~doc-string)))
         (var ~s-name))))

(comment

  (defsynth my-synth-2
    "Some synth."
    [freq 440, amp 0.5, pan 0.0]
    [:vars :env]
    [:= :env [:EnvGen.ar [:Env [0 1 1 0] [0.01 0.1 0.2]] {:doneAction 2}]]
    [:Out.ar 0 [:Pan2.ar [:* [:Blip.ar :freq] :env :amp]
                :pan]])

  (my-synth-2)

  ())
