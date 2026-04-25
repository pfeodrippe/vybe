(ns generate-wasm-abi
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.java.shell :as sh]
   [clojure.pprint :as pp]
   [clojure.string :as str]))

(defonce ^:private config* (atom nil))

(defn- config []
  (or @config*
      (throw (ex-info "Wasm ABI generator config is not initialized" {}))))

(defn- repo-root [] (:repo-root (config)))
(defn- header-file [] (:header (config)))
(defn- wasm-file [] (:wasm-file (config)))
(defn- out-file [] (:out-file (config)))
(defn- cflags [] (:cflags (config)))
(defn- generator-name [] (or (:generated-by (config)) "bin/generate-wasm-abi.clj"))

(defn- forced-layout-types
  []
  (->> (re-seq #"\(abi/layout\s+:([A-Za-z_][A-Za-z0-9_]*)\)"
               (slurp (io/file (repo-root) (:layout-source (config)))))
       (map second)
       distinct
       sort
       vec))

(defn- anonymous-struct-typedef-lines
  [header-text]
  (let [lines (str/split-lines header-text)]
    (loop [idx 0
           result {}]
      (if-let [line (get lines idx)]
        (if (re-find #"typedef\s+struct\s*\{" line)
          (let [typedef-name (->> (subvec (vec lines) idx (count lines))
                                  (keep #(some-> (re-find #"}\s*([A-Za-z_][A-Za-z0-9_]*)\s*;" %) second))
                                  first)]
            (recur (inc idx)
                   (cond-> result
                     typedef-name (assoc (inc idx) typedef-name))))
          (recur (inc idx) result))
        result))))

(defn- record-layout-dump
  [header-text]
  (let [tmp (java.nio.file.Files/createTempDirectory
             (or (:tmp-prefix (config)) "vybe-wasm-abi")
             (make-array java.nio.file.attribute.FileAttribute 0))
        c-file (.resolve tmp "probe.c")
        o-file (.resolve tmp "probe.o")]
    (spit (.toFile c-file)
          (str "#include \"" (:include-header (config)) "\"\n"
               (str/join "\n"
                         (map-indexed
                          (fn [idx type-name]
                            (format "size_t vybe_wasm_abi_size_%d = sizeof(%s);"
                                    idx type-name))
                          (or (:layout-types (config)) (forced-layout-types))))
               "\n"))
    (let [{:keys [exit out err]}
          (apply sh/sh "emcc" "-c" (str c-file)
                 (concat (cflags)
                         ["-Xclang" "-fdump-record-layouts-complete"
                          "-o" (str o-file)]))]
      (when-not (zero? exit)
        (throw (ex-info "Unable to dump Wasm record layouts"
                        {:exit exit :out out :err err})))
      (str out err))))

(defn- field-type
  [decl]
  (cond
    (re-find #"\*" decl) :pointer
    (re-find #"\b(_Bool|bool)\b" decl) :boolean
    (re-find #"\bdouble\b" decl) :double
    (re-find #"\bfloat\b" decl) :float
    (re-find #"\b(char|int8_t|uint8_t)\b" decl) :byte
    (re-find #"\b(int16_t|uint16_t|ecs_flags16_t|short)\b" decl) :short
    (re-find #"\b(int64_t|uint64_t|ecs_entity_t|ecs_id_t|ecs_flags64_t|ecs_time_t)\b" decl) :long
    (re-find #"\b(int32_t|uint32_t|ecs_size_t|ecs_flags32_t|ecs_flags_t|int|unsigned int)\b" decl) :int
    :else :pointer))

(defn- field-name
  [decl]
  (or (some-> (re-find #"\[[0-9]+\]\s+([A-Za-z_][A-Za-z0-9_]*)$" decl) second)
      (some-> (re-find #"\b([A-Za-z_][A-Za-z0-9_]*)(?:\s*:\s*[0-9]+)?$" decl) second)))

(defn- ctype-name
  [decl field-name]
  (when field-name
    (let [prefix (-> decl
                     (str/replace (re-pattern (str "\\b" (java.util.regex.Pattern/quote field-name) "\\b.*$")) "")
                     (str/replace #"(?s)\[[0-9]+\]" "")
                     (str/replace #"\bconst\b" "")
                     str/trim)]
      (some-> (re-find #"(?:struct\s+)?([A-Za-z_][A-Za-z0-9_]*)\s*(?:\*+)?\s*$" prefix)
              second))))

(defn- parse-field-line
  [line]
  (when-let [[_ offset bit-field rest] (re-matches #"\s*(\d+)(?::([^ ]+))? \| (.+)" line)]
    (let [spaces (count (or (second (re-matches #"^(\s*)\S.*" rest)) ""))
          level (max 0 (quot (- spaces 2) 2))
          decl (-> rest
                   str/trim
                   (str/replace #"/\*.*?\*/" ""))
          name (field-name decl)]
      (when (and name
                 (not (contains? #{"struct" "union"} name)))
        {:name name
         :level level
         :offset (parse-long offset)
         :bit-field? (boolean bit-field)
         :array-count (some-> (re-find #"\[([0-9]+)\]" decl) second parse-long)
         :type (field-type decl)
         :ctype (ctype-name decl name)
         :decl decl}))))

(defn- parse-record-layouts
  [dump anonymous-lines]
  (loop [lines (str/split-lines dump)
         current nil
         layouts {}]
    (if-let [line (first lines)]
      (cond
        (str/includes? line "*** Dumping AST Record Layout")
        (recur (rest lines) nil layouts)

        (nil? current)
        (let [[_ kind name] (or (re-matches #"\s*0 \| (struct|union) ([A-Za-z_][A-Za-z0-9_]*)" line)
                                (when-let [[_ kind _file line-no]
                                           (re-matches #"\s*0 \| (struct|union) \(unnamed at (.*):(\d+):\d+\)" line)]
                                  (when-let [name (get anonymous-lines (parse-long line-no))]
                                    [nil kind name])))]
          (if name
            (recur (rest lines) {:name name :kind (keyword kind) :fields [] :stack []} layouts)
            (recur (rest lines) current layouts)))


        (and current (re-find #"\[sizeof=\d+, align=\d+\]" line))
        (let [[_ size align] (re-find #"\[sizeof=(\d+), align=(\d+)\]" line)
              layout (-> current
                         (select-keys [:kind :fields])
                         (assoc :size (parse-long size)
                                :align (parse-long align)))]
          (recur (rest lines) nil (assoc layouts (keyword (:name current)) layout)))

        current
        (if-let [{:keys [level name] :as field} (parse-field-line line)]
          (let [stack (-> (:stack current)
                          (subvec 0 (min level (count (:stack current))))
                          (conj name))
                field (assoc field :path (mapv keyword stack))]
            (recur (rest lines)
                   (-> current
                       (assoc :stack stack)
                       (update :fields conj (dissoc field :level)))
                   layouts))
          (recur (rest lines) current layouts))

        :else
        (recur (rest lines) current layouts))
      layouts)))

(defn- strip-comments
  [s]
  (-> s
      (str/replace #"(?s)/\*.*?\*/" "")
      (str/replace #"//.*" "")))

(defn- parse-extern-constants
  [header-text]
  (->> (re-seq #"(?m)(?:FLECS_API\s+)?extern\s+const\s+(?:ecs_entity_t|ecs_id_t)\s+([A-Za-z_][A-Za-z0-9_]*)\s*;" header-text)
       (map second)
       sort
       vec))

(defn- parse-simple-defines
  [header-text]
  (let [eval-expr (fn [expr]
                    (let [expr (-> expr
                                   (str/replace #"([0-9]+)[uUlL]*" "$1")
                                   (str/replace #"[()]" "")
                                   str/trim)]
                      (when-let [[_ a b] (re-matches #"(\d+)\s*<<\s*(\d+)" expr)]
                        (bit-shift-left (parse-long a) (parse-long b)))))]
    (->> (re-seq #"(?m)^#define\s+([A-Za-z_][A-Za-z0-9_]*)\s+([^\n\\]+)$" header-text)
         (keep (fn [[_ name expr]]
                 (when-let [v (eval-expr expr)]
                   [name v])))
         (into (sorted-map)))))

(defn- enum-values
  [body]
  (loop [entries (->> (str/split (strip-comments body) #",")
                      (map strip-comments)
                      (map str/trim)
                      (remove str/blank?))
         next-value 0
         result {}]
    (if-let [entry (first entries)]
      (let [[_ name explicit] (re-matches #"([A-Za-z_][A-Za-z0-9_]*)(?:\s*=\s*([-]?[0-9]+))?.*" entry)
            value (if explicit (parse-long explicit) next-value)]
        (recur (rest entries)
               (inc value)
               (cond-> result
                 name (assoc name value))))
      result)))

(defn- parse-enums
  [header-text]
  (->> (re-seq #"(?s)typedef\s+enum\s+[A-Za-z_][A-Za-z0-9_]*\s*\{(.*?)\}\s*[A-Za-z_][A-Za-z0-9_]*\s*;" header-text)
       (mapcat (comp enum-values second))
       (into (sorted-map))))

(defn- parse-wasm-globals
  []
  (if (and (wasm-file) (.exists (wasm-file)))
    (let [{:keys [exit out]} (sh/sh "wasm-objdump" "-x" (.getPath (wasm-file)))]
      (if (zero? exit)
        (->> (re-seq #"(?m) - global\[\d+\].*<([A-Za-z_][A-Za-z0-9_]*)>" out)
             (map second)
             distinct
             sort
             vec)
        []))
    []))

(defn- write-edn!
  [data]
  (io/make-parents (out-file))
  (with-open [w (io/writer (out-file))]
    (binding [*out* w]
      (pp/pprint data))))

(defn normalize-config
  [config]
  (let [repo-root (.getCanonicalFile (io/file (or (:repo-root config) ".")))]
    (-> config
        (assoc :repo-root repo-root)
        (update :header #(io/file repo-root %))
        (update :out-file #(io/file repo-root %))
        (update :wasm-file #(when % (io/file repo-root %)))
        (update :include-header #(or % (.getName ^java.io.File (:header config)))))))

(defn generate!
  [config-map]
  (reset! config* (normalize-config config-map))
  (let [header-text (slurp (header-file))
        data {:generated-by (generator-name)
              :target (or (:target (config)) :wasm32)
              :layouts (parse-record-layouts
                        (record-layout-dump header-text)
                        (anonymous-struct-typedef-lines header-text))
              :wasm-globals (parse-wasm-globals)
              :extern-constants (parse-extern-constants header-text)
              :constants (merge (parse-enums header-text)
                                (parse-simple-defines header-text)
                                {"NULL" 0})}]
    (write-edn! data)
    (println (.getPath (out-file)))
    data))

(defn -main
  [& [config-file]]
  (when-not config-file
    (throw (ex-info "Usage: clj -M bin/generate-wasm-abi.clj config.edn" {})))
  (generate! (edn/read-string (slurp config-file))))

(when-not (Boolean/getBoolean "vybe.wasm-abi.no-main")
  (apply -main *command-line-args*)
  (shutdown-agents))
