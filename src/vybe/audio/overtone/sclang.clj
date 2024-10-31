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
  [ir-data]
  (let [[op & body] (if (sequential? ir-data)
                      ir-data
                      [ir-data])]
    (try
      (cond
        (and (= op :SynthDef) (sequential? ir-data))
        (let [{:keys [args vars file-dir]
               synthdef-name :name}
              (first body)

              body (rest body)]
          {:synthdef-name synthdef-name
           :file-path (.getAbsolutePath (io/file (str file-dir "/" synthdef-name ".scsyndef")))
           :synthdef-str (str "SynthDef"
                              "(" (str "\"" synthdef-name "\"" ",") " {\n"
                              (->> [

                                    (str "arg "
                                         (->> args
                                              (mapv (fn [[arg-identifier default]]
                                                      (str (name arg-identifier) "=" default)))
                                              (str/join ", "))
                                         ";")

                                    (str "var "
                                         (->> vars
                                              (mapv (fn [var-identifier]
                                                      (name var-identifier)))
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

        (sequential? ir-data)
        (str "["
             (->> ir-data
                  (str/join ", "))
             "]")

        (number? ir-data)
        ir-data

        (string? ir-data)
        (str "\"" ir-data "\"")

        (map? ir-data)
        (->> ir-data
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
         boot-end-coll ["});"]]
     (format (->> (concat
                   (when boot boot-init-coll)
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
  [ir-data]
  (stop-procs!)
  (let [temp-scd (io/file (str ".vybe/sc/" "_" (random-uuid) ".scd"))
        _port (or (:port @ov.conn/connection-info*)
                  (:port (:opts @ov.conn/connection-info*)))
        str (-wrap-code-with-interpreter (transpile ir-data))]
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
  [proc]
  (loop [counter 20]
    (cond
      (zero? counter)
      (throw (ex-info "Process had an error"
                      {:proc (proc/destroy-tree proc)}))

      (proc/alive? proc)
      (do (Thread/sleep 200)
          (recur (dec counter)))

      :else
      @proc)))

(defn synthdef-save!
  [{:keys [synthdef-name file-path synthdef-str]}]
  (let [temp-scd (io/file (str ".vybe/sc/" synthdef-name "_" (random-uuid) ".scd"))]
    (io/make-parents temp-scd)
    (spit temp-scd (-> synthdef-str
                       (-wrap-code-with-interpreter {:boot false})
                       (str "\n\n0.exit;")))
    (stop-procs!)
    (check-proc! (proc/process {:out *out* :err :out} (sclang-path) temp-scd))
    file-path))

(comment

  (def my-synth
    (-> [:SynthDef {:name 'event
                    :args [[:freq 240] [:amp 0.5] [:pan 0.0]]
                    :vars [:env]
                    :file-dir "resources"}
         [:= :env [:EnvGen.ar
                   [:Env [0 1 1 0] [0.01 0.1 0.2]]
                   {:doneAction 2}]]
         [:Out.ar 0 [:Pan2.ar [:* [:Blip.ar :freq] :env :amp]
                     :pan]]]
        transpile
        synthdef-save!
        ov.synth/synth-load))

  (my-synth :freq 100)

  ())
