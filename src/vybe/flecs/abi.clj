(ns vybe.flecs.abi
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [vybe.wasm :as vw]))

(defonce ^:private abi*
  (delay
    (with-open [r (io/reader (io/resource "vybe/wasm/flecs_abi.edn"))]
      (edn/read (java.io.PushbackReader. r)))))

(defn abi
  []
  @abi*)

(defn layout-data
  [c-type]
  (or (get-in @abi* [:layouts (keyword c-type)])
      (throw (ex-info "Missing generated Flecs Wasm ABI layout"
                      {:c-type c-type}))))

(defn sizeof
  [c-type]
  (:size (layout-data c-type)))

(defn offsetof
  [c-type field]
  (let [path (if (sequential? field)
               (mapv keyword field)
               [(keyword field)])]
    (or (some (fn [{field-path :path offset :offset}]
                (when (= path field-path)
                  offset))
              (:fields (layout-data c-type)))
        (throw (ex-info "Missing generated Flecs Wasm ABI field"
                        {:c-type c-type
                         :field field})))))

(defn const-value
  [name]
  (get-in @abi* [:constants (str name)]))

(defn function-data
  [name]
  (or (get-in @abi* [:functions (str name)])
      (throw (ex-info "Missing generated Flecs Wasm ABI function"
                      {:name name}))))

(defn function-desc
  [name]
  (let [{:keys [ret args]} (function-data name)]
    (into [:fn (:schema ret)]
          (mapv (fn [{:keys [symbol schema]}]
                  [(keyword symbol) schema])
                args))))

(defn extern-constant?
  [name]
  (contains? (set (:extern-constants @abi*)) (str name)))

(defn- wasm-type
  [{:keys [type ctype decl]}]
  (cond
    (and (= type :pointer)
         decl
         (not (str/includes? decl "*"))
         ctype
         (or (str/ends-with? ctype "_kind_t")
             (str/ends-with? ctype "_op_kind_t")))
    :int

    :else type))

(declare component)

(defn- component-field
  [layouts {:keys [path ctype array-count offset] :as field}]
  (let [field-name (first path)
        nested-layout (get layouts (keyword ctype))
        nested? (and nested-layout
                     (not (str/includes? (or (:decl field) "") "*"))
                     (not (= (keyword ctype) :ecs_time_t)))]
    {:field field-name
     :offset offset
     :type (wasm-type field)
     :array-count array-count
     :component (when nested? (component (keyword ctype)))
     :elem-size (when (and nested? array-count) (:size nested-layout))}))

(defn layout
  ([c-type]
   (layout (symbol "vybe.flecs.abi" (name c-type)) c-type))
  ([name c-type]
   (let [{:keys [size fields]} (layout-data c-type)
         layouts (:layouts @abi*)
         top-fields (->> fields
                         (filter #(= 1 (count (:path %))))
                         (remove :bit-field?)
                         (mapv #(component-field layouts %)))]
     (vw/wasm-layout name size top-fields))))

(def component
  (memoize
   (fn [c-type]
     (let [name (symbol "vybe.flecs.abi" (name c-type))]
       (vw/make-component name (layout name c-type))))))

(defn field-offsets
  [c-type]
  (into {}
        (map (fn [{:keys [path offset]}]
               [(keyword (str/join "." (map name path))) offset]))
        (:fields (layout-data c-type))))
