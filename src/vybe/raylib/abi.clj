(ns vybe.raylib.abi
  (:require
   [clojure.string :as str]
   [camel-snake-kebab.core :as csk]
   [vybe.raylib.wasm-abi :as wasm-abi]
   [vybe.wasm :as vw]))

(defn abi [] (wasm-abi/abi))
(defn constants [] (:constants (abi)))
(defn functions [] (:functions (abi)))
(defn layouts [] (:layouts (abi)))
(defn aliases [] (:type-aliases (abi)))

(defn constant
  [k]
  (let [n (name k)]
    (or (get (constants) n)
        (get (constants) (str/replace n #"^RL_" ""))
        (throw (ex-info "Missing Raylib wasm constant" {:constant k})))))

(defn resolve-type-name
  [type-name]
  (let [type-name (name type-name)]
    (keyword (get (aliases) type-name type-name))))

(defn layout-entry
  [layout-name]
  (let [layout-name (resolve-type-name layout-name)]
    (or (get (layouts) layout-name)
        (throw (ex-info "Missing Raylib wasm layout" {:layout layout-name})))))

(defn- pointer-decl?
  [decl]
  (str/includes? (or decl "") "*"))

(defn- top-level-field?
  [{:keys [path]}]
  (= 1 (count path)))

(defn- component-field?
  [{:keys [type ctype decl array-count]}]
  (and (nil? array-count)
       (= :pointer type)
       (not (pointer-decl? decl))
       (contains? (layouts) (resolve-type-name ctype))))

(declare layout)

(defn- primitive-size
  [type]
  (case type
    (:long :long-long :double) 8
    (:int :uint :float :pointer :*) 4
    (:short :char) 2
    1))

(defn- field-spec
  [{:keys [path type offset array-count] :as field}]
  (let [field-name (first path)]
    (cond
      (component-field? field)
      {:field field-name
       :component (delay (layout (:ctype field)))
       :offset offset}

      array-count
      {:field field-name
       :type type
       :offset offset
       :array-count array-count
       :elem-size (primitive-size type)}

      :else
      {:field field-name
       :type type
       :offset offset})))

(defn layout
  [layout-name]
  (let [layout-name (resolve-type-name layout-name)
        {:keys [size fields]} (layout-entry layout-name)]
    (vw/wasm-layout layout-name size
                    (mapv (fn [spec]
                            (let [spec (field-spec spec)]
                              (if-let [component (:component spec)]
                                (assoc spec :component @component)
                                spec)))
                          (filter top-level-field? fields)))))

(defn aggregate-type?
  [ctype]
  (let [ctype (or ctype "")]
    (and (not (str/includes? ctype "*"))
         (contains? (layouts) (resolve-type-name
                               (-> ctype
                                   (str/replace #"\bconst\b" "")
                                   (str/replace #"\bstruct\s+" "")
                                   str/trim))))))

(defn function
  [c-name]
  (or (get (functions) c-name)
      (throw (ex-info "Missing Raylib wasm function metadata" {:function c-name}))))

(defn wrapper-export
  [c-name]
  (str "vybe_raylib_" c-name))

(defn clj-name
  [c-name]
  (csk/->kebab-case-symbol c-name))

(defn public-functions
  []
  (->> (functions)
       (remove (fn [[name {:keys [ctype]}]]
                 (or (str/starts-with? name "vybe_raylib_")
                     (str/includes? (or ctype "") "..."))))
       (sort-by key)))
