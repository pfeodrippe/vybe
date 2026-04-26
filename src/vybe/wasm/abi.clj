(ns vybe.wasm.abi
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [vybe.wasm :as vw]))

(defn load-abi
  "Load a generated Wasm ABI EDN resource."
  [resource-name]
  (delay
    (with-open [r (io/reader (io/resource resource-name))]
      (edn/read (java.io.PushbackReader. r)))))

(defn abi
  [abi*]
  @abi*)

(defn layout-data
  [abi* lib-name c-type]
  (or (get-in @abi* [:layouts (keyword c-type)])
      (throw (ex-info (str "Missing generated " lib-name " Wasm ABI layout")
                      {:c-type c-type}))))

(defn sizeof
  [abi* lib-name c-type]
  (:size (layout-data abi* lib-name c-type)))

(defn offsetof
  [abi* lib-name c-type field]
  (let [path (if (sequential? field)
               (mapv keyword field)
               [(keyword field)])]
    (or (some (fn [{field-path :path offset :offset}]
                (when (= path field-path)
                  offset))
              (:fields (layout-data abi* lib-name c-type)))
        (throw (ex-info (str "Missing generated " lib-name " Wasm ABI field")
                        {:c-type c-type
                         :field field})))))

(defn const-value
  [abi* name]
  (get-in @abi* [:constants (str name)]))

(defn function-data
  [abi* lib-name name]
  (or (get-in @abi* [:functions (str name)])
      (throw (ex-info (str "Missing generated " lib-name " Wasm ABI function")
                      {:name name}))))

(defn function-desc
  [abi* lib-name name]
  (let [{:keys [ret args]} (function-data abi* lib-name name)]
    (into [:fn (:schema ret)]
          (mapv (fn [{:keys [symbol schema]}]
                  [(keyword symbol) schema])
                args))))

(defn- wasm-type
  [{:keys [type]}]
  type)

(defn component-field
  [component-fn layouts {:keys [path ctype array-count offset] :as field}]
  (let [field-name (first path)
        nested-layout (get layouts (keyword ctype))
        nested? (and nested-layout
                     (not (str/includes? (or (:decl field) "") "*")))]
    {:field field-name
     :offset offset
     :type (wasm-type field)
     :array-count array-count
     :component (when nested? (component-fn (keyword ctype)))
     :elem-size (when (and nested? array-count) (:size nested-layout))}))

(defn layout
  ([abi* lib-name component-fn c-type]
   (layout abi* lib-name component-fn
           (symbol (str "vybe." lib-name ".abi") (name c-type))
           c-type))
  ([abi* lib-name component-fn name c-type]
   (let [{:keys [size fields]} (layout-data abi* lib-name c-type)
         layouts (:layouts @abi*)
         top-fields (->> fields
                         (filter #(= 1 (count (:path %))))
                         (remove :bit-field?)
                         (mapv #(component-field component-fn layouts %)))]
     (vw/wasm-layout name size top-fields))))

(defn component
  [abi* lib-name c-type]
  (let [component* (atom nil)]
    (letfn [(component-fn [c-type]
              (let [name (symbol (str "vybe." lib-name ".abi") (name c-type))]
                (vw/make-component name
                                   (layout abi* lib-name component-fn name c-type))))]
      (reset! component* (memoize component-fn))
      (@component* c-type))))

(defn intern-constant-functions!
  "Intern zero-arity functions for every generated numeric constant."
  [target-ns abi*]
  (doseq [[name value] (:constants @abi*)]
    (intern target-ns (symbol name) (fn [] value)))
  nil)
