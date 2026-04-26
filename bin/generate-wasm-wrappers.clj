(ns generate-wasm-wrappers
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- identifier
  [s]
  (-> (or s "arg")
      (str/replace #"[^A-Za-z0-9_]" "_")
      (str/replace #"^[^A-Za-z_]" "_$0")))

(defn- pointer-type?
  [ctype]
  (str/includes? (or ctype "") "*"))

(defn- base-type
  ([ctype]
   (base-type {} ctype))
  ([aliases ctype]
   (let [base (-> (or ctype "")
                  (str/replace #"\bconst\b" "")
                  (str/replace #"\bstruct\s+" "")
                  (str/replace #"\*" "")
                  str/trim
                  (str/split #"\s+")
                  last)]
     (loop [base base]
       (if-let [target (get aliases base)]
         (recur target)
         base)))))

(defn- aggregate-type?
  [layouts aliases ctype]
  (and (not (pointer-type? ctype))
       (contains? layouts (keyword (base-type aliases ctype)))))

(defn- arg-decl
  [layouts aliases {:keys [ctype symbol]}]
  (let [name (identifier symbol)]
    (if (aggregate-type? layouts aliases ctype)
      (format "%s *%s" ctype name)
      (format "%s %s" ctype name))))

(defn- call-arg
  [layouts aliases {:keys [ctype symbol]}]
  (let [name (identifier symbol)]
    (if (aggregate-type? layouts aliases ctype)
      (str "*" name)
      name)))

(defn- wrapper-source
  [{:keys [include-header wrapper-prefix abi]}]
  (let [layouts (:layouts abi)
        aliases (:type-aliases abi)
        wrapper-prefix (or wrapper-prefix "vybe_wasm_")]
    (str "#include \"" include-header "\"\n\n"
         (str/join
          "\n\n"
          (for [[c-name {:keys [ret args ctype]}] (:functions abi)
                :when (not (str/includes? (or ctype "") "..."))]
            (let [ret-ctype (:ctype ret)
                  aggregate-ret? (aggregate-type? layouts aliases ret-ctype)
                  wrapper-name (str wrapper-prefix c-name)
                  arg-decls (mapv #(arg-decl layouts aliases %) args)
                  call-args (mapv #(call-arg layouts aliases %) args)
                  decls (cond-> []
                          aggregate-ret? (conj (format "%s *out" ret-ctype))
                          true (into arg-decls))
                  call (format "%s(%s)" c-name (str/join ", " call-args))]
              (cond
                aggregate-ret?
                (format "void %s(%s) { *out = %s; }"
                        wrapper-name (str/join ", " decls) call)

                (= "void" ret-ctype)
                (format "void %s(%s) { %s; }"
                        wrapper-name (str/join ", " decls) call)

                :else
                (format "%s %s(%s) { return %s; }"
                        ret-ctype wrapper-name (str/join ", " decls) call)))))
         "\n")))

(defn generate!
  [{:keys [abi-file out-file] :as config}]
  (let [abi (edn/read-string (slurp abi-file))
        out-file (io/file out-file)]
    (io/make-parents out-file)
    (spit out-file (wrapper-source (assoc config :abi abi)))
    (println (.getPath out-file))))

(defn -main
  [& [config-file]]
  (when-not config-file
    (throw (ex-info "Usage: clj -M bin/generate-wasm-wrappers.clj config.edn" {})))
  (generate! (edn/read-string (slurp config-file)))
  (shutdown-agents))

(apply -main *command-line-args*)
