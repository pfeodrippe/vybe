(ns generate-wasm-abi
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.java.shell :as sh]
   [clojure.pprint :as pp]
   [clojure.string :as str]
   [jsonista.core :as json]))

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
(defn- cc [] (or (:cc (config)) "emcc"))
(defn- source-extension [] (or (:source-extension (config)) "c"))

(defn- header-layout-types
  [header-text]
  (let [lines (str/split-lines header-text)]
    (loop [lines lines
           in-typedef? false
           depth 0
           acc []
           result []]
      (if-let [line (first lines)]
        (let [start? (and (not in-typedef?)
                          (re-find #"^\s*typedef\s+struct(?:\s+[A-Za-z_][A-Za-z0-9_]*)?\s*\{"
                                   line))
              in-typedef? (or in-typedef? start?)
              depth (if in-typedef?
                      (+ depth
                         (count (re-seq #"\{" line))
                         (- (count (re-seq #"\}" line))))
                      depth)
              acc (if in-typedef? (conj acc line) acc)
              done? (and in-typedef? (zero? depth))]
          (if done?
            (let [decl (str/join "\n" acc)
                  name (some-> (re-find #"}\s*([A-Za-z_][A-Za-z0-9_]*)\s*;" decl)
                                second)]
              (recur (rest lines) false 0 [] (cond-> result name (conj name))))
            (recur (rest lines) in-typedef? depth acc result)))
        result))))

(defn- forced-layout-types
  []
  (if-let [layout-types (:layout-types (config))]
    layout-types
    (let [from-source (when-let [layout-source (:layout-source (config))]
                        (let [f (io/file (repo-root) layout-source)]
                          (when (.exists f)
                            (->> (re-seq #"\(abi/layout\s+:([A-Za-z_][A-Za-z0-9_]*)\)"
                                         (slurp f))
                                 (map second)))))
          from-header (header-layout-types (slurp (header-file)))]
      (->> (concat from-source from-header)
           distinct
           sort
           vec))))

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
        c-file (.resolve tmp (str "probe." (source-extension)))
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
          (apply sh/sh (cc) "-c" (str c-file)
                 (concat (cflags)
                         ["-Xclang" "-fdump-record-layouts-complete"
                          "-o" (str o-file)]))]
      (when-not (zero? exit)
        (throw (ex-info "Unable to dump Wasm record layouts"
                        {:exit exit :out out :err err})))
      (str out err))))

(defn- scalar-type-aliases
  []
  (let [header-text (slurp (header-file))
        primitive->schema (fn [primitive]
                            (cond
                              (re-find #"\b(_Bool|bool)\b" primitive) :boolean
                              (re-find #"\bdouble\b" primitive) :double
                              (re-find #"\bfloat\b" primitive) :float
                              (re-find #"\b(char|int8_t|uint8_t)\b" primitive) :byte
                              (re-find #"\b(int16_t|uint16_t|short)\b" primitive) :short
                              (re-find #"\b(int64_t|uint64_t|long long|unsigned long long)\b" primitive) :long
                              (re-find #"\b(size_t|uintptr_t|intptr_t)\b" primitive) :long
                              (re-find #"\b(uint32_t|unsigned int)\b" primitive) :uint
                              (re-find #"\b(int32_t|int)\b" primitive) :int))]
    (->> (re-seq #"(?m)^\s*typedef\s+([A-Za-z_][A-Za-z0-9_\s]*?)\s+([A-Za-z_][A-Za-z0-9_]*)\s*;" header-text)
         (keep (fn [[_ primitive alias]]
                 (when-let [schema (primitive->schema primitive)]
                   [alias schema])))
         (into {}))))

(defn- field-type
  [decl ctype]
  (cond
    (re-find #"\*" decl) :pointer
    (get (scalar-type-aliases) ctype) (get (scalar-type-aliases) ctype)
    (re-find #"\b(_Bool|bool)\b" decl) :boolean
    (re-find #"\bdouble\b" decl) :double
    (re-find #"\bfloat\b" decl) :float
    (re-find #"\b(char|int8_t|uint8_t)\b" decl) :byte
    (re-find #"\b(int16_t|uint16_t|ecs_flags16_t|short)\b" decl) :short
    (re-find #"\b(int64_t|uint64_t|ecs_entity_t|ecs_id_t|ecs_flags64_t|ecs_time_t)\b" decl) :long
    (re-find #"\b(uint32_t|unsigned int)\b" decl) :uint
    (re-find #"\b(int32_t|ecs_size_t|ecs_flags32_t|ecs_flags_t|int)\b" decl) :int
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
          name (field-name decl)
          ctype (ctype-name decl name)]
      (when (and name
                 (not (contains? #{"struct" "union"} name)))
        {:name name
         :level level
         :offset (parse-long offset)
         :bit-field? (boolean bit-field)
         :array-count (some-> (re-find #"\[([0-9]+)\]" decl) second parse-long)
         :type (field-type decl ctype)
         :ctype ctype
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


        (and current (re-find #"\[sizeof=\d+," line))
        (let [[_ size align] (re-find #"\[sizeof=(\d+),.*align=(\d+)" line)
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
  (let [parse-number (fn [s]
                       (let [s (str/replace s #"[uUlL]+$" "")]
                         (cond
                           (re-matches #"0x[0-9A-Fa-f]+" s)
                           (Long/parseUnsignedLong (subs s 2) 16)

                           (re-matches #"0b[01]+" s)
                           (Long/parseLong (subs s 2) 2)

                           (re-matches #"-?\d+" s)
                           (parse-long s))))
        eval-expr (fn [expr]
                    (let [expr (-> expr
                                   (str/replace #"[()]" "")
                                   str/trim)]
                      (or (parse-number expr)
                          (when (= expr "FLT_EPSILON")
                            (float 1.1920929E-7))
                          (when-let [[_ a b] (re-matches #"([0-9]+|0x[0-9A-Fa-f]+)[uUlL]*\s*<<\s*([0-9]+)" expr)]
                            (bit-shift-left (long (parse-number a)) (parse-long b))))))]
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
      (let [[_ name explicit] (re-matches #"([A-Za-z_][A-Za-z0-9_]*)(?:\s*=\s*(.+?))?(?:\s*/.*)?$" entry)
            value (letfn [(parse-number [s]
                            (let [s (str/replace s #"[uUlL]+$" "")]
                              (cond
                                (re-matches #"0x[0-9A-Fa-f]+" s)
                                (Long/parseUnsignedLong (subs s 2) 16)

                                (re-matches #"0b[01]+" s)
                                (Long/parseLong (subs s 2) 2)

                                (re-matches #"-?\d+" s)
                                (parse-long s))))
                          (parse-explicit [s]
                            (when s
                              (let [s (str/trim s)]
                                (cond
                                  (str/includes? s "|")
                                  (reduce bit-or
                                          (map #(long (or (parse-explicit %) (get result (str/trim %)) 0))
                                               (str/split s #"\|")))

                                  (re-matches #"[A-Za-z_][A-Za-z0-9_]*" s)
                                  (get result s)

                                  :else
                                  (if-let [[_ a b] (re-matches #"([-]?(?:0x[0-9A-Fa-f]+|0b[01]+|[0-9]+))\s*<<\s*([0-9]+)" s)]
                                    (bit-shift-left (long (parse-number a)) (parse-long b))
                                    (parse-number s))))))]
                    (if explicit (parse-explicit explicit) next-value))]
        (recur (rest entries)
               (inc value)
               (cond-> result
                 name (assoc name value))))
      result)))

(defn- parse-enums
  [header-text]
  (->> (re-seq #"(?s)(?:typedef\s+)?enum(?:\s+[A-Za-z_][A-Za-z0-9_]*)?\s*\{(.*?)\}\s*(?:[A-Za-z_][A-Za-z0-9_]*)?\s*;" header-text)
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

(defn- parse-function-return
  [qual-type]
  (if-let [idx (str/last-index-of qual-type " (")]
    (subs qual-type 0 idx)
    (or (some-> (re-find #"^(.+?)\s*\(" qual-type) second str/trim)
        qual-type)))

(defn- c-type-schema
  [{:keys [qualType desugaredQualType]}]
  (let [decl (or qualType "")
        type (or desugaredQualType qualType "")]
    (cond
      (or (str/includes? decl "*")
          (str/includes? type "*")) :long
      (re-find #"\bvoid\b" type) :void
      (re-find #"\b(_Bool|bool)\b" type) :boolean
      (re-find #"\bdouble\b" type) :double
      (re-find #"\bfloat\b" type) :float
      (re-find #"\b(char|int8_t|uint8_t)\b" type) :byte
      (re-find #"\b(int16_t|uint16_t|short)\b" type) :short
      (re-find #"\b(int64_t|uint64_t|long long|unsigned long long|ecs_entity_t|ecs_id_t|ecs_flags64_t|ecs_time_t)\b" type) :long
      (re-find #"\b(size_t|uintptr_t|intptr_t)\b" type) :long
      (re-find #"\b(uint32_t|unsigned int)\b" type) :uint
      (re-find #"\b(int32_t|ecs_size_t|ecs_flags32_t|ecs_flags_t|int)\b" type) :int
      :else :long)))

(defn- ast-dump
  []
  (let [tmp (java.nio.file.Files/createTempDirectory
             (or (:tmp-prefix (config)) "vybe-wasm-abi-functions")
             (make-array java.nio.file.attribute.FileAttribute 0))
        c-file (.resolve tmp (str "probe." (source-extension)))]
    (spit (.toFile c-file)
          (str "#include \"" (:include-header (config)) "\"\n"))
    (let [{:keys [exit out err]}
          (apply sh/sh (cc)
                 (concat (cflags)
                         ["-Xclang" "-ast-dump=json"
                          "-fsyntax-only"
                          (str c-file)]))]
      (when-not (zero? exit)
        (throw (ex-info "Unable to dump Clang AST for Wasm ABI"
                        {:exit exit :out out :err err})))
      out)))

(defn- function-name-selected?
  [name]
  (let [prefixes (:function-prefixes (config))]
    (or (empty? prefixes)
        (some #(str/starts-with? name %) prefixes))))

(defn- function-decl->data
  [{:keys [name type inner]}]
  (let [args (->> inner
                  (filter #(= "ParmVarDecl" (:kind %)))
                  (map-indexed
                   (fn [idx {:keys [name type]}]
                     {:symbol (or name (str "arg_" idx))
                      :ctype (:qualType type)
                      :desugared-ctype (:desugaredQualType type)
                      :schema (c-type-schema type)}))
                  vec)
        ret-ctype (parse-function-return (:qualType type))]
    {:name name
     :ctype (:qualType type)
     :ret {:ctype ret-ctype
           :schema (c-type-schema {:qualType ret-ctype})}
     :args args}))

(defn- parse-functions
  []
  (let [ast (json/read-value (ast-dump) json/keyword-keys-object-mapper)
        functions* (atom {})]
    (letfn [(walk [node]
              (when (map? node)
                (when (and (= "FunctionDecl" (:kind node))
                           (:name node)
                           (function-name-selected? (:name node)))
                  (swap! functions* assoc (:name node) (function-decl->data node)))
                (doseq [child (:inner node)]
                  (walk child))))]
      (walk ast))
    (into (sorted-map) @functions*)))

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
              :functions (parse-functions)
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
