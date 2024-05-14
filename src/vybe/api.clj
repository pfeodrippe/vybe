(ns vybe.api
  (:require
   [vybe.jnr :as jnr :refer [c-api constant enum new-t]]
   [clojure.string :as str]
   [omkamra.jnr.struct :as struct]
   [potemkin :refer [def-map-type]]
   [vybe.type :as v.type]
   [clojure.set :as set]
   [clojure.pprint :as pp]
   [malli.core :as m]
   [malli.error :as me]
   [malli.util :as mu]
   [clj-java-decompiler.core :refer [decompile disassemble]]
   [meta-merge.core :as meta-merge]
   [clojure.walk :as walk]
   #_:reload)
  (:import
   (jnr.ffi Pointer Struct$Pointer Struct Struct$String Struct$Boolean Memory)
   (jnr.ffi.byref PointerByReference)
   (vybe.jnr ecs_entity_desc_t ecs_component_desc_t ecs_query_desc_t)
   (vybe.type VybeComponent)))

(set! *warn-on-reflection* true)
#_(set! *unchecked-math* :warn-on-boxed)

;; -------------- COMPONENTS ------------

;; Used for -get-accessors.
(def ^:dynamic *-component-being-accessed* nil)

(defn- -get-accessors
  []
  (->> (.getFields ^Class (type *-component-being-accessed*))
       (mapv #(.getName ^java.lang.reflect.Field %))
       (mapv (fn [variable-name]
               [(keyword variable-name)
                (let [type-sym (with-meta (gensym)
                                 {:tag (.getTypeName ^Class (type *-component-being-accessed*))})]
                  `(~(symbol (str "." variable-name))
                    (let [~type-sym *-component-being-accessed*]
                      ~type-sym)))]))
       (into {})))

(def -offsets
  (memoize
   (fn [component-type]
     (let [internal-fn
           (fn internal-offsets
             [component-type]
             (let [accessors (binding [*-component-being-accessed* (or *-component-being-accessed*
                                                                       (component-type))]
                               (eval (-get-accessors)))]
               (->> accessors
                    (mapv (fn [[k v]]
                            [k (cond
                                 (instance? Struct v)
                                 (try
                                   (binding [*-component-being-accessed* v]
                                     (-> (first (internal-offsets (v.type/-component-data v)))
                                         last))
                                   (catch Exception e
                                     (throw e)))

                                 :else
                                 (.offset ^jnr.ffi.Struct$NumberField v))])))))]
       (into {} (internal-fn component-type))))))

#_ (do
     (defcomp OffsetTest1
       [:d :double]
       [:s :string]
       [:b :byte]
       [:f :float])

     (defcomp OffsetTest2
       [:d :double]
       [:s :string]
       [:b :byte]
       [:o OffsetTest1]
       [:f :float])

     (-offsets OffsetTest2))

#_ (-offsets B)
#_ (-offsets A)

(def *components-cache (atom {}))

(set! *warn-on-reflection* false)
(defn -retrieve-p-identifier
  [p env]
  (let [symbol->metadata (into {} (mapv (juxt identity meta) (keys env)))
        adapter (fn [metadata]
                  (->> metadata
                       keys
                       (mapv symbol)
                       (mapv #(some-> % resolve deref))
                       (filter #(instance? VybeComponent %))
                       first
                       :comp-sym))
        p-meta (merge (meta p)
                                 (get symbol->metadata p)
                                 (meta (macroexpand-1 p)))
        identifier (or (adapter (meta p))
                       (adapter (get symbol->metadata p))
                       (adapter (meta (macroexpand-1 p))))]
    (when-not identifier
      (throw (ex-info (format "Type hint %s\n e.g. if you component is `Position`, type hint it with `::Position`"
                              p)
                      {:p p
                       :p-meta p-meta})))
    [p-meta identifier]))
(set! *warn-on-reflection* true)

(declare pselect)

(defmacro pget
  "Analogous to `clojure.core/get`, but this is a macro
  that translations to a `.getXxx` call, e.g. `(pget p Position x)`
  will output `(.getFloat p 16)`, where `16` is the offset of the component."
  ([p]
   (let [[p-meta identifier] (-retrieve-p-identifier p &env)
         {:keys [fields]} (cond
                            (instance? VybeComponent identifier)
                            identifier

                            (keyword? identifier)
                            (or (get jnr/flecs-info (symbol (name identifier)))
                                @(resolve identifier))

                            :else
                            (or (some-> identifier resolve deref)
                                (get jnr/flecs-info (symbol (name identifier)))))]
     (when (> (count fields) 1)
       (throw (ex-info "Can't use 1-arity pget with components that have more than 1 field"
                       {:p p
                        :identifier identifier})))
     (with-meta `(pget ~p ~identifier ~(:field-name (first fields)))
       p-meta)))
  ([p field]
   (let [[p-meta identifier] (-retrieve-p-identifier p &env)]
     (with-meta `(pget ~p ~identifier ~field)
       p-meta)))
  ([p identifier field]
   `(pget ~p ~identifier ~field {}))
  ([p identifier field {:keys [offset]
                        :or {offset 0}}]
   #_(def identifier identifier)
   #_(def field field)
   #_(def offset offset)
   (let [{:keys [fields field->idx] :as component} (cond
                                                     (instance? VybeComponent identifier)
                                                     identifier

                                                     (keyword? identifier)
                                                     (or (get jnr/flecs-info (symbol (name identifier)))
                                                         @(resolve identifier))

                                                     :else
                                                     (or (some-> identifier resolve deref)
                                                         (get jnr/flecs-info (symbol (name identifier)))))
         #_ #__ (do (def field->idx field->idx)
                    (def fields fields))
         {:keys [field-offset field-type pointer]} (get fields (get field->idx (symbol (name field))))
         field-offset (if (instance? VybeComponent component)
                        (get (-offsets component) (keyword (name field)))
                        field-offset)
         field-type (cond
                      (instance? VybeComponent field-type)
                      field-type

                      (and (symbol? field-type)
                           (resolve field-type)
                           (instance? VybeComponent @(resolve field-type)))
                      @(resolve field-type)

                      :else
                      field-type)
         field-offset (some-> field-offset (+ offset))
         field-type (cond
                      (= field-type :string)
                      'CString

                      (keyword? field-type)
                      (symbol (name field-type))

                      :else
                      field-type)
         comp-data (get jnr/flecs-info field-type)]
     #_(def field-type field-type)
     (cond
       (instance? VybeComponent field-type)
       `(get (pselect ~p ~identifier [~field]) ~field)

       (= (:type comp-data)
          :enum)
       ;; Get the enum value from the integer.
       ` (let [idx# (~'.getInt
                     (let [^Pointer p# ~p]
                       p#)
                     ~field-offset)]
           (keyword (str (:identifier comp-data))
                    (str (get-in comp-data [:fields idx# :field-name]))))

       (= field-type 'CString)
       `(some-> (.getPointer
                 (let [^Pointer p# ~p]
                   p#)
                 ~field-offset)
                (.getString 0))

       (= field-type 'boolean)
       `(= (.getByte (let [^Pointer p# ~p]
                       p#)
                     ~field-offset)
           1)

       (= field-type 'keyword)
       `(some-> (.getPointer
                 (let [^Pointer p# ~p]
                   p#)
                 ~field-offset)
                (.getString 0)
                keyword)

       :else
       `(~(cond
            (or (= (:type comp-data)
                   :struct)
                pointer)
            '.getPointer

            (= field-type 'int)
            '.getInt

            (contains? '#{ulong long} field-type)
            '.getLong

            (= field-type 'float)
            '.getFloat

            (= field-type 'double)
            '.getDouble

            (contains? #{'short 'ushort} field-type)
            '.getShort

            :else
            (throw (ex-info "pget - Field not supported or it does not exist, refer to the component documentation"
                            {:identifier identifier
                             :field field
                             :field-type field-type
                             :field-offset field-offset
                             :struct-fields fields})))
         (let [^Pointer p# ~p]
           p#)
         ~field-offset)))))

(def -utf-8
  (java.nio.charset.Charset/forName "UTF-8"))

(defmacro pset
  "Mutable set a pointer for a component (identifier)."
  ([p params]
   (let [[p-meta identifier] (-retrieve-p-identifier p &env)]
     (with-meta `(pset ~p ~identifier ~params)
       p-meta)))
  ([p identifier params]
   ;; TODO Refactor this after tests!!
   (let [[{:vy/keys [-read-only-code]}] (try (-retrieve-p-identifier p &env)
                                             (catch Exception _))
         {:keys [fields field->idx] :as component} (cond
                                                     (instance? VybeComponent identifier)
                                                     identifier

                                                     (keyword? identifier)
                                                     (or (get jnr/flecs-info (symbol (name identifier)))
                                                         @(resolve identifier))

                                                     :else
                                                     (or (some-> identifier resolve deref)
                                                         (get jnr/flecs-info (symbol (name identifier)))))]
     (->> params
          (mapv (fn [[k v]]
                  (let [{:keys [field-offset field-type]}
                        (get fields (get field->idx (symbol (name k))))

                        field-offset (if (instance? VybeComponent component)
                                       (get (-offsets component) (keyword (name k)))
                                       field-offset)]
                    (list
                     `do
                     ;; TODO This read only check could be activated only in debug mode
                     (when -read-only-code
                       `(when ~-read-only-code
                          (println (ex-info "Can't set a read only field" {~k ~v}))))
                     (cond
                       (map? v)
                       `(pset (.slice ~p ~field-offset) ~field-type ~v)

                       (= field-type :boolean)
                       `(.putByte ~p ~field-offset (byte ({true 1 false 0} ~v)))

                       (= field-type :keyword)
                       `(.putPointer ~p ~field-offset (jnr/k->buf ~v))

                       :else
                       (let [*is-string (atom nil)
                             form (->> (walk/macroexpand-all
                                        `(pget ~p ~identifier ~k))
                                       (walk/prewalk (fn [v]
                                                       (if-let [method ({'getFloat 'putFloat
                                                                         'getDouble 'putDouble
                                                                         'getInt 'putInt
                                                                         'getString 'putString
                                                                         'getLong 'putLong
                                                                         'getPointer 'putPointer
                                                                         'getShort 'putShort
                                                                         'getByte 'putByte}
                                                                        v)]
                                                         (do (when (= method 'putString)
                                                               (reset! *is-string true))
                                                             method)
                                                         v))))]
                         (if @*is-string
                           `(.putPointer ~p ~field-offset (jnr/str->buf ~v))
                           (concat
                            ;; Walk over the result of `pget` and replace the symbols accordingly.
                            form
                            `(~v)))))))))))))

;; DONE Fix string
;; DONE Fix add-c
(comment

  (defcomp Str
    [:x :float]
    [:str :string]
    [:y :double])

  (add-c world entity (Str {:x 44 :y 45}))
  (let [p (get-c world entity Str)]
    (def p p)
    ;; This `pset` works.
    (pset p Str {:x 44 :y 455 :str "1ss"})
    #_(.putString p 8 "dosls" 8 -utf-8)
    (get-c world entity Str [:x :y :str]))

  ())

(defmacro -pupdate
  [p identifier k f & args]
  `(let [p# ~p]
     (do (pset p# ~identifier {~k (~f (pget p# ~identifier ~k) ~@args)})
         p#)))

(defmacro pupdate
  "Like `update`, but for component pointers."
  [p k-or-identifier f-or-k & args]
  (if (keyword? k-or-identifier)
    (let [[p-meta identifier] (-retrieve-p-identifier p &env)]
      (with-meta `(-pupdate ~p ~identifier ~k-or-identifier ~f-or-k ~@args)
        p-meta))
    `(-pupdate ~p ~k-or-identifier ~f-or-k ~@args)))

#_ (meta (macroexpand-1
          '(pupdate ^::Position pos-p :x + (pget vel-p Vel :x))))

#_ (jnr/-prepare-class (:struct Str))

#_ (let [p (get-c world entity E)]
     (pset p E {:x 44 :y 244})
     (get-c world entity E [:x :y]))

#_ (let [p (get-c world entity B)]
     (pset p B {:x 44 :a {:x 52}})
     (get-c world entity B [:x {:a [:x]}]))

#_ (let [e (get-c world entity E)]
     (pset e E {:x 0.4 :y 0.66})
     (pset e E {:x 9.4})
     (pset e E {:y 1000})
     (get-c world entity E [:x :y]))

(defn -expose-all-select-fields
  [component]
  (->> (:fields component)
       (mapv (fn [{:keys [field-type field-name]}]
               (if (and (symbol? field-type)
                        (instance? VybeComponent (some-> (resolve field-type) deref)))
                 {field-name (-expose-all-select-fields @(resolve field-type))}
                 (keyword field-name))))))
#_ (-expose-all-select-fields B)

(defmacro pselect
  "Analogous to `clojure.core/get`, but this is a macro
  that translations to `.getXxx` calls, e.g. `(pselect p Position [:x :y])`
  will output `{:x(.getFloat p 16) :y {.getFloat p 24}}`, where `16` and `24` are
  the offsets of the component.

  For nested compoments/structs, use a EQL-like syntax, e.g.
  `(pselect p Vel [:x {:pos [:x]}])` will give you
  `{:x -3.0, :pos {:x 93.2}}`."
  ([p]
   (let [[_p-meta identifier] (-retrieve-p-identifier p &env)]
     `(pselect ~p ~(-expose-all-select-fields @(resolve identifier)))))
  ([p select-query]
   (if (sequential? select-query)
     ;; A normal select-query.
     (let [[p-meta identifier] (-retrieve-p-identifier p &env)]
       (with-meta `(pselect ~p ~identifier ~select-query)
         p-meta))
     ;; Otherwise we try to interpret it as an identifier for selecting all
     ;; the fields.
     (let [identifier select-query]
       `(pselect ~p ~identifier ~(-expose-all-select-fields @(resolve identifier))))))
  ([p identifier select-query & [{:keys [offset]
                                  :or {offset 0}}]]
   (let [p-sym (with-meta (gensym 'p_)
                 {:tag `Pointer})
         {:keys [fields field->idx] :as component} (try
                                                     (cond
                                                       (instance? VybeComponent identifier)
                                                       identifier

                                                       (keyword? identifier)
                                                       (or (get jnr/flecs-info (symbol (name identifier)))
                                                           @(resolve identifier))

                                                       :else
                                                       (or (some-> identifier resolve deref)
                                                           (get jnr/flecs-info (symbol (name identifier)))))
                                                     (catch Exception e
                                                       (throw (ex-info "pselect error"
                                                                       {:identifier identifier
                                                                        :select-query select-query
                                                                        :offset offset}
                                                                       e))))
         form (->> select-query
                   (mapv (fn [q]
                           (if (map? q)
                             (let [field-k (ffirst q)
                                   subfields (last (last q))
                                   {:keys [field-offset field-type]}
                                   (get fields (get field->idx (symbol (name field-k))))

                                   field-offset (if (instance? VybeComponent component)
                                                  (get (-offsets component) (keyword (name field-k)))
                                                  field-offset)]

                               [field-k `(pselect ~(if (zero? (+ field-offset offset))
                                                     `~p-sym
                                                     `(.slice ~p-sym ~(+ field-offset offset)))
                                                  ~field-type ~subfields)])
                             (let [{:keys [field-offset field-type]}
                                   (get fields (get field->idx (symbol (name q))))

                                   field-offset (if (instance? VybeComponent component)
                                                  (get (-offsets component) (keyword (name q)))
                                                  field-offset)]
                               (if (and (symbol? field-type)
                                        (instance? VybeComponent @(resolve field-type)))
                                 [q `(pselect ~(if (zero? (+ field-offset offset))
                                                 `~p-sym
                                                 `(.slice ~p-sym ~(+ field-offset offset)))
                                              ~field-type
                                              ~(-expose-all-select-fields @(resolve field-type)))]
                                 [q `(pget ~(if (zero? offset)
                                              `~p-sym
                                              `(.slice ~p-sym ~offset))
                                           ~identifier ~q)])))))
                   (into {}))]
     `(let [~p-sym ~p]
        ~form))))


(comment

  ;; DONE Fix `add-c`
  ;; DONE Create an equivalent struct in C for testing
  #_(.x (A {:x 33}))

  (let [p (Struct/getMemory (A))]
    (pset p A {:x 121232})
    (c-api :vybe_test (get-c world-test entity A)))

  (let [p (Struct/getMemory (A))]
    (pset p A {:x 121232})
    (c-api :vybe_test p))

  (do
    (defcomp A
      [:x :float])

    (defcomp B
      [:x :float]
      [:a A])

    (defcomp C
      [:x :float]
      [:a A]
      [:b B])

    (def world-test (init))
    (def entity (make-entity world-test))

    (add-c world-test entity (C {:x -10
                                 :a {:x 2342.5}
                                 :b {:x 101
                                     :a {:x 50}}}))
    (set-c world-test entity A {:x -4414}))

  (keys @*env)

  (-> (get-c world-test entity A)
      #_(pset A {:x 4})
      (pselect A [:x]))

  (do
    (require '[clojure.test :refer [deftest testing is]])

    [(is (= {:x -10.0
             :a {:x 2342.5}
             :b {:x 101.0
                 :a {:x 50.0}}}
            (-> (get-c world-test entity C)
                (pselect C [:x
                            {:b [:x
                                 {:a [:x]}]}
                            {:a [:x]}]))))

     (is (= {:x -10.0
             :a {:x 2342.5}
             :b {:x 101.0
                 :a {:x 50.0}}}
            (-> (get-c world-test entity C)
                (pselect C [:x :b :a]))))])

  (walk/macroexpand-all
   '
   (-> (get-c world-test entity C)
       (pselect C [:x
                   {:b [:x
                        #_ {:a [:x]}]}
                   {:a [:x]}])))

  ())

#_ (pselect p Vel [:x {:pos [:x]}])

#_ (pselect p Vel [:x :y])

(defn -adapt-fields
  [fields]
  #_(def fields fields)
  (->> fields
       (mapv (fn [{:keys [field-name field-meta field-type] :as field}]
               (let [{:keys [size]} field-meta
                     tag (or ({:string 'Pointer
                               :keyword 'Pointer
                               :boolean 'byte}
                              field-type)
                             (cond
                               (keyword? field-type)
                               (symbol (name field-type))

                               (var? field-type)
                               (:vybe.internal/struct-sym (meta field-type))

                               (symbol? field-type)
                               (:vybe.internal/struct-sym (meta (resolve field-type)))

                               (class? field-type)
                               (symbol (.getTypeName ^Class field-type))

                               :else
                               (throw (ex-info "Field not handled"
                                               {:field field}))))
                     field-name-sym (symbol field-name)
                     field (with-meta field-name-sym
                             {:tag tag})]

                 (if size
                   [field size]
                   field))))))

(def -struct-alignment
  (memoize
   (fn [component-type]
     (Struct/alignment (jnr/make-instance component-type)))))

(def -struct-size
  (memoize
   (fn [^Class component-type]
     (let [fake-size (Struct/size component-type)
           alig (-struct-alignment component-type)]
       (+ fake-size (- alig (rem fake-size alig)))))))

;; DONE Make a builder using simple keywords
;; LATER Move it to the jnr ns?
(defmacro defcomp
  "Define a component."
  [name & fields*]
  (let [[comp-meta fields*] (if (map? (first fields*))
                              [(first fields*) (rest fields*)]
                              [nil fields*])
        fields (->> fields*
                    (reduce (fn [{:keys [offset] :as acc}
                                 [field-name field-type-or-meta field-type]]
                              (let [[field-meta field-type] (if (map? field-type-or-meta)
                                                              [field-type-or-meta field-type]
                                                              [nil field-type-or-meta])
                                    field-type (if (symbol? field-type)
                                                 (symbol (resolve field-type))
                                                 field-type)
                                    field-name (keyword (str/replace (clojure.core/name field-name) #"-" "_"))]
                                (-> acc
                                    (update :fields conj
                                            {:field-name field-name
                                             :field-meta field-meta
                                             :field-type field-type
                                             :field-offset offset})
                                    (update :offset +
                                            (or ({:string 8
                                                  :keyword 8
                                                  :char 1
                                                  :short 2
                                                  :unsigned-short 2
                                                  :byte 1
                                                  :boolean 1
                                                  :int 4
                                                  :double 8
                                                  :unsigned-char 1
                                                  :unsigned-int 4
                                                  :long 8
                                                  :float 4}
                                                 field-type)
                                                (try
                                                  (-struct-size (:struct @(resolve field-type)))
                                                  (catch Exception e
                                                    (throw (ex-info "Error in defcomp"
                                                                    {:name name
                                                                     :fields* fields*
                                                                     :field-type field-type}
                                                                    e)))))))))
                            {:fields []
                             :offset 0})
                    :fields)
        field->idx (->> fields
                        (map-indexed (fn [idx {:keys [field-name]}]
                                       [(symbol (clojure.core/name field-name)) idx]))
                        (into {}))
        params-hash (hash [*ns* name comp-meta fields])]
    (or (get @*components-cache params-hash)
        (let [arglists (list [] [{:keys (mapv (comp symbol :field-name) fields)}])
              struct-schema (-> (into [:map {:registry {:float 'number?
                                                        :byte 'number?
                                                        :short 'number?
                                                        :unsigned-short 'number?
                                                        :unsigned-char 'number?
                                                        :char 'number?
                                                        :unsigned-int 'number?
                                                        :double 'number?
                                                        :long :int}}]
                                      (->> fields
                                           (mapv (fn [{:keys [field-name field-meta field-type]}]
                                                   (let [field-type (if (symbol? field-type)
                                                                      (:vybe.internal/struct-schema
                                                                       (meta (resolve field-type)))
                                                                      field-type)
                                                         field-meta (merge field-meta {:optional true})]
                                                     [field-name field-meta field-type])))))
                                mu/closed-schema
                                m/form)
              struct-schema-human (-> (into [:map]
                                            (->> fields
                                                 (mapv (fn [{:keys [field-name field-meta field-type]}]
                                                         (let [field-type (if (symbol? field-type)
                                                                            (:vybe.internal/struct-schema-human
                                                                             (meta (resolve field-type)))
                                                                            field-type)]
                                                           (if field-meta
                                                             [field-name field-meta field-type]
                                                             [field-name field-type])))))))]
          `(let [struct-sym# (quote ~(symbol (str "VY_" name)))
                 struct# (eval (concat (list (if false `union/define `struct/define)
                                             struct-sym#)
                                       (-adapt-fields (quote ~fields))))
                 comp-adapter# (:adapter ~comp-meta)]

             (def ~name
               (v.type/->VybeComponent struct#
                                       (quote ~struct-schema)
                                       (quote ~fields)
                                       (quote ~field->idx)
                                       (symbol (var ~name))
                                       (or comp-adapter# identity)))

             (eval (@#'clojure.core/emit-extend-type
                    struct-sym#
                    (list 'v.type/IComponentData
                          (list '-component-data '[_#]
                                ~name))))

             (binding [jnr/*class->extra-info* {struct# {:fields (->> ~fields
                                                                      (mapv (juxt :field-name identity))
                                                                      (into {}))
                                                         :adapter comp-adapter#}}]
               (jnr/-prepare-class struct#))

             (alter-meta! (var ~name) merge
                          {:vybe.internal/struct-sym (symbol (.getTypeName ^Class struct#))
                           :vybe.internal/struct-schema (quote ~struct-schema)
                           :vybe.internal/struct-schema-human (quote ~struct-schema-human)
                           :arglists (quote ~arglists)
                           :doc (format "Builder for component %s, it accepts (optionally) the following fields:

%s%s

---------------

>> Expanded schema (Malli) <<

%s
---------------"
                                        (quote ~name)
                                        (->> ~fields
                                             (mapv (fn [field#]
                                                     (let [v# (:field-type field#)]
                                                       (format "    %s: %s"
                                                               (name (:field-name field#)),
                                                               (cond
                                                                 (var? v#)
                                                                 (symbol v#)

                                                                 (symbol? v#)
                                                                 v#

                                                                 (instance? VybeComponent v#)
                                                                 (:comp-sym v#)

                                                                 :else
                                                                 (name v#))))))
                                             (str/join "\n"))
                                        (if comp-adapter#
                                          "\n\nNOTE: This component is using a custom constructor, so the keys displayed as inputs to the builder may not reflect the reality!"
                                          "")
                                        (with-out-str
                                          (pp/pprint ~struct-schema-human)))})
             (swap! *components-cache assoc
                    ~params-hash ~name
                    struct# ~name)
             ~name)))))

(comment

  (defcomp Translation
    [:x :float]
    [:name :string])

  (v.type/-component-data (Position*_. 4))

  (meta #'Translation)

  (Translation)
  (Translation #_{:x 4}
               {:x 4
                :name "asdf"})


  (jnr/buf->str (.name (Translation {:name "asdf"})))

  (defcomp Vel
    [:x :float]
    [:trans Translation])

  (let [a {:x 20
           :trans {:x 32
                   :name "3"}}]
    (Vel a))

  ())

(def pselect-builder
  "Generates unevaluated expression with a query to fetch all the possible (including nested)
  fields (by default) for a component."
  (memoize
   (fn [component]
     (let [p (gensym)
           v (list `pselect p
                   (:comp-sym component)
                   (-expose-all-select-fields component))]
       (eval (list 'fn [p] v))))))

(defn -pselect-all
  "This is non-performant! Only use this for debugging. If you want to select all
  the fields for a field, use `pselect` with no select query."
  ([instance]
   (-pselect-all (Struct/getMemory instance)
                (v.type/-component-data instance)))
  ([p component]
   ((pselect-builder component) p)))

(comment

  (do
    (defcomp A
      [:x :float])

    (defcomp B
      [:x :float]
      [:y A]
      [:nana :string])

    (def world (init))
    (def entity (make-entity world {:name "Olha"}))

    (add-c world entity A {:x 55})
    (add-c world entity B {:y {:x 43} :nana "eae"}))

  (def a-select (pselect-builder A))
  (a-select (get-c world entity A))

  (def b-select (pselect-builder B))
  (b-select (get-c world entity B))

  ())

;; ---------------- ECS -------------------

(def *env (atom {}))

(extend-protocol v.type/IVybeName
  VybeComponent
  (-vybe-name [component]
    (v.type/-vybe-name (:struct component)))

  Class
  (-vybe-name [c]
    (-> (.getTypeName c)
        (str/replace #"\." "_")))

  clojure.lang.Var
  (-vybe-name [v]
    (str "V_" (-> v
                  symbol
                  str
                  (str/replace #"\." "_"))))

  clojure.lang.Keyword
  (-vybe-name [k]
    (str "K_" (-> (symbol k)
                  str
                  (str/replace #"\." "_"))))

  clojure.lang.Symbol
  (-vybe-name [sym]
    (str "S_" (-> sym
                  (str/replace #"\." "_")))))

(defn- cache-id
  [world identifier ^long id]
  (when (nil? id)
    (throw (ex-info "id is nil for this identifier" {:identifier identifier})))
  #_(def id id)
  (when-not (zero? id)
    (swap! *env (fn [cache]
                  (cond-> (-> cache
                              (update world assoc identifier id)
                              (update-in [world id] (comp set conj) identifier))
                    (or (instance? VybeComponent identifier)
                        (keyword? identifier))
                    (update-in [world :component] assoc id identifier)

                    (and (class? identifier)
                         (= (.getSuperclass ^Class identifier) Struct))
                    (update-in [world :component] assoc id (get @*components-cache identifier))))))
  id)

#_(:component (val (first @*env)))

(comment

  (do
    (defcomp A
      [:x :float])

    (def world (init))
    (def entity (make-entity world {:name "Olha"}))

    (add-c world entity A {:x 10}))

  (get-in @*env [world :component 9])

  ())

(defn init
  []
  (c-api :init))
#_ (def world (init))

(defn world-destroy
  "Destroy the world."
  [world]
  (when world
    (c-api :fini world)))

(defn world-exists?
  "Check if world is NOT destroyed."
  [world]
  (when world
    (not (c-api :is_fini world))))

(defn get-world
  "Get world from a world or stage."
  [world]
  (c-api :get_world world))

(defn world-read-only?
  [world]
  (c-api :stage_is_readonly (get-world world)))

(defn register-component
  [world c]
  #_(def c c)
  (or (get-in @*env [world c])

      (and (keyword? c)
           (= (namespace c) "vy.b"))

      (cond
        (class? c)
        (let [^Class component-type c
              name (v.type/-vybe-name component-type)
              ^ecs_entity_desc_t edesc (jnr/make-instance
                                        ecs_entity_desc_t
                                        {:id 0
                                         :name name
                                         :symbol name
                                         :use_low_id true})
              entity-id (c-api :entity_init world edesc)
              ^ecs_component_desc_t desc (jnr/make-instance ecs_component_desc_t
                                                            {:entity entity-id
                                                             :type {:size (-struct-size component-type)
                                                                    :alignment (-struct-alignment component-type)}})
              _id (c-api :component_init world desc)]
          (cache-id world component-type entity-id))

        (instance? VybeComponent c)
        (cache-id world c (register-component world (:struct c)))

        (keyword? c)
        (let [k c
              name (v.type/-vybe-name k)
              ^ecs_entity_desc_t edesc (jnr/make-instance
                                        ecs_entity_desc_t
                                        {:id 0
                                         :name name
                                         :symbol name})
              entity-id (c-api :entity_init world edesc)]
          (cache-id world k entity-id))

        :else
        (throw (ex-info "Register not defined"
                        {:component c})))))
#_ (do (defcomp Position
         [:x :float]
         [:y :float])
       (register-component world Position)
       (register-component world :a/b))
#_ (entity-info world Position)

(defn -ecs-pair
  "Receives two ids (numbers) and returns the id of the pair."
  [id-1 id-2]
  (c-api :vybe_pair id-1 id-2))

#_(defn -repair-id
    [world id identifier]
    (when id
      (if (c-api :id_is_pair id)
        (let [id-1 (c-api :vybe_pair_first world id)
              id-2 (c-api :vybe_pair_second world id)]
          (when (zero? id-1)
            (println :NOT_ALIVE_PAIR_1 id-1 identifier))
          (when (zero? id-2)
            (println :NOT_ALIVE_PAIR_2 id-2 identifier)))
        (when-not (c-api :is_alive world id)
          (println :NOT_ALIVE id identifier))))
    #_(println :ACTUAL id identifier)
    id)

(defn -remove-env-key
  [world id identifier]
  (swap! *env update world (fn [m]
                             (-> m
                                 (dissoc identifier id)
                                 (update :component dissoc id)))))

(declare ->id)

(defn pair?
  [world identifier]
  (c-api :id_is_pair (->id world identifier)))

(defn -repair-id
  [world id identifier]
  (when id
    (if (c-api :id_is_pair id)
      (let [id-1 (c-api :vybe_pair_first world id)
            id-2 (c-api :vybe_pair_second world id)]
        (when (zero? id-1)
          (println :NOT_ALIVE_PAIR_1 id-1 identifier))
        (when (zero? id-2)
          (println :NOT_ALIVE_PAIR_2 id-2 identifier))
        id)
      (if (c-api :is_alive world id)
        id
        (if (number? identifier)
          (do
            (println :NUMBER_NOT_ALIVE id identifier)
            id)
          (do
            (-remove-env-key world id identifier)
            (-remove-env-key (get-world world) id identifier)
            ;; Make identifier alive again.
            (let [new-id (->id (get-world world) identifier)]
              (->id world identifier)
              #_(println :REPAIRED :old id :new new-id :identifier identifier)
              new-id)))))))

(defn ->id
  "Converts/registers a world identifier/entity/component/anything to an Vybe/Flecs entity.
  A number is returned as it's."
  ^long [world identifier]
  (if (number? identifier)
    #_identifier
    (-repair-id world identifier identifier)
    (or #_(get-in @*env [world identifier])
        (-repair-id world (get-in @*env [world identifier]) identifier)
        (-repair-id world (get-in @*env [(get-world world) identifier]) identifier)

        (when (world-read-only? world)
          (let [unsafe-world (get-world world)]
            (if-let [id (get-in @*env [unsafe-world identifier])]
              #_(cache-id world identifier id)
              (cache-id world identifier (-repair-id unsafe-world id identifier))
              #_(let [ex (ex-info (format "Preregister the component/entity before using `->id`: example %s"
                                          (list 'vy/->id 'world identifier))
                                  {:identifier identifier})]
                  (println ex)
                  (throw ex)))))

        (cond
          (keyword? identifier)
          (let [identifier (case identifier
                             :* :vy.b/EcsWildcard
                             :_ :vy.b/EcsAny
                             identifier)
                [id-ns id-v] ((juxt namespace name) identifier)]
            (cache-id world identifier (if (= id-ns "vy.b")
                                         ;; Builtin.
                                         (case id-v
                                           "EcsWildcard" (c-api :vybe_wildcard)
                                           "EcsAny" (c-api :vybe_any)
                                           (let [id (->id world id-v)]
                                             (if (zero? id)
                                               (let [id (c-api :lookup_symbol
                                                               world
                                                               (str "flecs.core." (subs id-v 3))
                                                               true
                                                               false)]
                                                 (if (zero? id)
                                                   (throw (ex-info "Built-in identifier does not exist"
                                                                   {:identifier identifier}))
                                                   id))
                                               id)))
                                         (let [id (register-component world identifier)]
                                           ;; Add EcsTag to a keyword.
                                           (c-api :add_id world id (->id world :vy.b/EcsTag))
                                           id))))

          (string? identifier)
          (let [id (c-api :lookup world identifier)]
            (if (zero? id)
              (let [id (c-api :lookup_symbol world identifier true false)]
                (if (zero? id)
                  id
                  (cache-id world identifier id)))
              (cache-id world identifier id)))

          (sequential? identifier)
          (let [[t1 t2] identifier]
            (-ecs-pair (->id world t1)
                       (->id world t2)))

          #_(sequential? identifier)
          #_(let [[t1 t2] identifier]
              (cache-id world identifier (-ecs-pair (->id world t1)
                                                    (->id world t2))))

          (instance? VybeComponent identifier)
          (do (register-component world identifier)
              (cache-id world identifier (->id world (:struct identifier))))

          :else
          (do (register-component world identifier)
              (cache-id world identifier (->id world (v.type/-vybe-name identifier))))))))
#_ (->id world "Olha")

#_ (def world (init))
#_ (->id world :vy.b/EcsOnAdd)
#_ (entity-info world :a)

(defn pair-id
  "Get id of the pair."
  [world c1 c2]
  (->id world [c1 c2]))

(defn pair
  "Build a pair representation. For usage when adding multiple components."
  ([c1 c2]
   [c1 c2])
  ([c1 c2 data]
   {[c1 c2] data}))

(defn make-entity
  "Make (add) an entity. you can pass as parameters:

  - `:name`, name of the entity"
  ([world]
   (c-api :new_w_id world 0))
  ([world params]
   (if (map? params)
     (let [{:keys [name]} params
           ^ecs_entity_desc_t edesc (jnr/make-instance
                                     ecs_entity_desc_t
                                     {:name name})]
       (c-api :entity_init world edesc))
     ;; Add a component.
     (c-api :new_w_id world (->id world params)))))
#_ (def entity (make-entity world {:name "Olha"}))
#_ (def entity-2 (make-entity world))
#_ (do
     (def entity-3 (make-entity world :vy.b/EcsPhase))
     (entity-info world entity-3))

(defn add-c
  ([world component]
   (add-c world (make-entity world) component))
  ([world entity component]
   (let [entity (->id world entity)]
     (cond
       (or (instance? VybeComponent component)
           (sequential? component)
           (keyword? component)
           (number? component))
       ;; Tag or Pair without data.
       (c-api :add_id world entity (->id world component))

       (map? component)
       (let [[c params] (first component)]
         (add-c world entity c params))

       :else
       ;; Component.
       (let [component-type ^Class (type component)
             component-instance component]
         (c-api :set_id world entity
                (->id world component-type)
                (-struct-size component-type)
                (Struct/getMemory component-instance))))
     entity))
  ([world entity component params]
   (cond
     (sequential? component)
     (let [entity (->id world entity)
           [t1 _t2] component
           ;; First parameter is always a vybe component.
           component-instance (t1 params)
           component-type ^Class (:struct t1)]
       (c-api :set_id world entity
              (->id world component)
              (-struct-size component-type)
              (Struct/getMemory component-instance))
       entity)

     :else
     ;; VybeComponent
     (do (->id world component)         ; for caching
         (add-c world entity (component params))))))
#_ (add-c world entity Position)
#_ (add-c world entity (Position {:x 45}))
#_ (entity-info world "Olha")
#_ (add-c world entity :ss)
#_ (add-c world entity :a)

(defn add-many
  "Add many components to an entity (it creates a unnamed one if no `entity` is
  passed).

  If `entity` is a keyword, it also adds it to itself so it's
  queryable (note that `add-c` and `set-c` do not behave this way!)."
  ([world components]
   (add-many world (make-entity world) components))
  ([world entity components]
   (run! (fn [c]
           (add-c world entity c))
         (if (keyword? entity)
           (concat components [entity])
           components))
   entity))

#_ (add-many world entity [(Translation :local {:x 10 :y 10})])

(defn remove-c
  [world entity component]
  (c-api :remove_id world (->id world entity) (->id world component)))
#_ (remove-c world entity Position)
#_ (entity-info world "Olha")

(defn delete
  "Delete the entity, the entity ID will be recycled!

  Not valid for deleting components."
  [world entity]
  (let [id (->id world entity)]
    ;; First delete from our cache...
    (swap! *env update world dissoc entity id)
    ;; ... and then for real.
    (c-api :delete world id)))

(defn delete-children
  "Delete the entity chidlren."
  [world entity]
  (let [id (->id world [:vy.b/EcsChildOf entity])]
    (c-api :delete_with world id)))

(defn delete-with
  "Delete all the entities with this `id`."
  [world id]
  (c-api :delete_with world (->id world id)))

(defn alive?
  "Check if entity (an id) is alive."
  [world entity]
  (c-api :is_alive world entity))

(defn is-a
  "Returns is-a pair. Check `EcsIsA` in Flecs.

  Example

    (vy/add-c world :pitoco (vy/is-a :vy.pf/sprite))"
  [entity]
  [:vy.b/EcsIsA entity])

(defn child-of
  "Returns a child-of pair. Check `EcsChildOf` in Flecs."
  [entity]
  [:vy.b/EcsChildOf entity])

(extend-protocol v.type/IResolveComponent
  VybeComponent
  (-resolve-component [c world id p]
    (v.type/-resolve-component (:struct c) world id p))

  Class
  (-resolve-component [_c _world _id p]
    p
    #_(jnr/p*->instance p c))

  clojure.lang.Keyword
  (-resolve-component [k _world _id p]
    (when p
      k))

  clojure.lang.Symbol
  (-resolve-component [sym _world _id p]
    (when p
      sym))

  java.util.List
  (-resolve-component [_coll _world _id p]
    (when p
      p
      #_(let [c (first coll)]

          (if (.isInstance VybeComponent c)
            (jnr/p*->instance p (:struct c))
            p)))))

;; DONE Make tags-only relationship not explode (maybe use ecs_id_is_tag ?).
(defmacro get-c
  "Get component data from an entity. The 3-arity version returns a JNR pointer.
  Use the 4-arity version to specify the data you need, the return will be a
  hash-map with only the defined values.

  This is a macro so we can optmize and create only the needed `.getXX` method
  calls.

  `query` is a EQL-like query (inlined so we can parse it a compile-time) in the
  same format as used at `pselect`, e.g. if we want to get the `x` fields of the
  component and the `y` field of a nested struct, we cad do

  [:x {:other-component [:y]}]

  the response would be (assuming `x` and `y` are floats`) something like

  {:x 10.0
   :other-component {:y 30.0}}
  "
  ([world entity component]
   `(let [component-id# (->id ~world ~component)]
      (when-not (c-api :id_is_tag ~world component-id#)
        (v.type/-resolve-component ~component
                                  ~world
                                  component-id#
                                  (c-api :get_id ~world (->id ~world ~entity) component-id#)))))
  #_(-pselect-all (c-api :get_id world (->id world entity) 11) Position)
  #_ `(let [component-id# (->id ~world ~component)]
        (vybe.api/->id world-test A)
        (when-not (c-api :id_is_tag ~world component-id#)
          (v.type/-resolve-component ~component
                                    ~world
                                    component-id#
                                    (-> (c-api :get_id world (->id world entity) 11)
                                        (-pselect-all Position)))))
  ([world entity component query]
   `(-> (get-c ~world ~entity ~component)
        (pselect ~(if (sequential? component)
                    ;; Pair.
                    (first component)
                    ;; Normal component.
                    component)
                 ~query))))
#_ (get-c world entity E)
#_ (get-c world entity E [:x])
#_ (get-c world entity [B C] [:x])

(defmacro set-c
  "Set component data for an entity. If the component does not exist
  for this entity, it will be created.

  This is a macro, so use it with components inline, use `add-c`
  if you want something more dynamic (but slower and it replaces entirely
  the existing component)."
  [world entity component params]
  `(if-let [p# (get-c ~world ~entity ~component)]
     (pset p# ~(if (sequential? component)
                 ;; Pair.
                 (first component)
                 ;; Normal component.
                 component)
           ~params)
     (add-c ~world ~entity ~component ~params)))
#_ (do
     (set-c world entity E {:x 40})
     (get-c world entity E [:x]))

(defn get-name
  [world entity]
  (c-api :get_name world (->id world entity)))

(def -parser-special-keywords
  #{:or :not :maybe :pair :meta :entity
    :filter :query
    :in :out :inout :none
    :notify :sync})

(defn -parse-query-expr
  "Internal function to parse a query expr to a filter terms + additional info for the
  query/filter/rule descriptor. "
  [world query-expr]
  (let [*additional-info (atom {})]
    {:terms
     (->> (if (and (sequential? query-expr)
                   (not (contains? -parser-special-keywords (first query-expr))))
            query-expr
            [query-expr])
          (mapcat (fn parse-one-expr [c]
                    (if (not (sequential? c))
                      [{:id (->id world
                                  (case c
                                    (:* *) :vy.b/EcsWildcard
                                    (:_ _) :vy.b/EcsAny
                                    c))
                        :inout (enum :ecs_inout_kind_t/EcsIn)}]
                      (let [{:keys [flags inout term]
                             :or {inout :in}
                             :as metadata}
                            (some (fn [v]
                                    (when (and (map? v)
                                               (not (instance? VybeComponent v)))
                                      v))
                                  c)

                            c (remove (fn [v]
                                        (and (map? v)
                                             (not (instance? VybeComponent v))))
                                      c)
                            args (rest c)
                            result (case (first c)
                                     :or
                                     (vec
                                      (concat (->> (:terms (-parse-query-expr world (drop-last args)))
                                                   (mapv (fn [term]
                                                           (assoc term :oper
                                                                  (enum :ecs_oper_kind_t/EcsOr)))))
                                              ;; We put EcsOr only to the first arguments above.
                                              ;; See https://www.flecs.dev/flecs/md_docs_Queries.html#autotoc_md205.
                                              (parse-one-expr (last args))))


                                     :not
                                     [(assoc (first (parse-one-expr (last args)))
                                             :oper (enum :ecs_oper_kind_t/EcsNot))]

                                     :maybe
                                     [(assoc (first (parse-one-expr (last args)))
                                             :oper (enum :ecs_oper_kind_t/EcsOptional))]

                                     ;; Force a sync point for this component.
                                     :sync
                                     (parse-one-expr (into [:in {:flags #{:is-entity}}]
                                                           args))

                                     ;; Notify other systems that they should sync
                                     ;; for this component.
                                     :notify
                                     (parse-one-expr (into [:out {:flags #{:is-entity}}]
                                                           args))

                                     :meta
                                     (:terms (-parse-query-expr world args))

                                     :entity
                                     (do (swap! *additional-info assoc-in [:filter :entity] (->id world (last args)))
                                         nil)

                                     :filter
                                     (do (swap! *additional-info update :filter meta-merge/meta-merge metadata)
                                         nil)

                                     :query
                                     (do (swap! *additional-info update :query meta-merge/meta-merge metadata)
                                         nil)

                                     ;; Inout(s), see Access Modifiers in the Flecs manual.
                                     (:in :out :inout :none)
                                     (parse-one-expr (into [:meta {:inout (first c)}]
                                                           args))

                                     ;; Pair.
                                     (let [adapt #(case %
                                                    (:* *) :vy.b/EcsWildcard
                                                    (:_ _) :vy.b/EcsAny
                                                    %)]
                                       (if (= (first c) :pair)
                                         [{:id (pair-id world
                                                        (adapt (first args))
                                                        (adapt (last args)))}]
                                         [{:id (pair-id world
                                                        (adapt (first c))
                                                        (adapt (last args)))}])))]
                        (when result
                          #_(println result inout)
                          (cond-> (-> result
                                      (update 0 meta-merge/meta-merge term))
                            flags
                            (assoc-in [0 :src :flags] (->> flags
                                                           (mapv {:parent (constant :EcsParent)
                                                                  :cascade (constant :EcsCascade)
                                                                  :is-entity (constant :EcsIsEntity)})
                                                           (apply (partial bit-or 0))))

                            (and inout (or (not (get-in result [0 :inout]))
                                           (= (get-in result [0 :inout])
                                              (enum :ecs_inout_kind_t/EcsIn))))
                            (assoc-in [0 :inout] ({:in (enum :ecs_inout_kind_t/EcsIn)
                                                   :out (enum :ecs_inout_kind_t/EcsOut)
                                                   :inout (enum :ecs_inout_kind_t/EcsInOut)
                                                   :none (enum :ecs_inout_kind_t/EcsInOutNone)}
                                                  inout))))))))
          vec)

     :additional-info @*additional-info}))

(defn parse-query-expr
  "Parse a query expr into a query description (`ecs_query_desc_t`)."
  [world query-expr]
  (let [{:keys [terms additional-info]} (-parse-query-expr world query-expr)
        query (:query additional-info)]
    (meta-merge/meta-merge
     {:filter (meta-merge/meta-merge {:terms terms}
                                     (:filter additional-info))}
     (cond-> query
       (:order_by_component query)
       (update :order_by_component #(->id world %))))))

#_(def world (init))

#_ (->> [[:notify :a]]
        (parse-query-expr world))

#_ (->> [[:or [:in Translation] A] [:query {:adfs 4}]]
        (parse-query-expr world))

#_ (->> [[:or [:in Translation] A]]
        (parse-query-expr world))

#_ (->> [[:in Translation] A]
        (parse-query-expr world))

#_ (->> [[:A :*] [:entity 423]]
        (parse-query-expr world))

#_ (->> [[:A :*]]
        (parse-query-expr world))

;; Nested metas work just fine.
#_ (->> [[:meta [:meta [:meta [:A :*]]]]]
        (parse-query-expr world))

#_ (->> [[A '_]]
        (parse-query-expr world))

#_ (->> [:pair A :*]
        (parse-query-expr world))

#_ (->> [:or {:flags #{:parent}} Translation A]
        (parse-query-expr world))

#_ (->> [:meta {:flags #{:parent}} [:or Translation A]]
        (parse-query-expr world))

#_ (->> [A]
        (parse-query-expr world))

#_ (->> [[:out [:pair Translation :local]]
         [:out Translation]
         [:or
          [:pair Translation :global]
          [:pair Translation :local]]]
        (parse-query-expr world))

#_ (->> [Translation [:maybe A]]
        (parse-query-expr world))

#_ (->> [[:pair Translation :local]
         [:pair Translation :global]
         [:maybe {:flags #{:parent :cascade}}
          [:pair Translation :global]]]
        (parse-query-expr world))

#_ (->> [Translation A]
        (parse-query-expr world))

(defn get-full-path
  "Get full path for an entity."
  [world entity]
  (c-api :get_path_w_sep world 0 (->id world entity) "." nil))

(defn -entity-info
  ^vybe.jnr.ecs_type_t [world identifier]
  (let [id (->id world identifier)]
    (when-not (zero? id)
      (->> id
           (c-api :get_type world)
           #_(c-api :type_str world)))))
#_ (entity-info world "Olha")
#_ (entity-info world :vy/ChildOf)

(defn -entity-info-str
  [world identifier]
  (c-api :type_str world (-entity-info world identifier)))

(defn entity-overriden?
  "Check if entity (a component is also an entity) is overriden."
  [world entity]
  (boolean
   (not= (bit-and (->id world entity) (c-api :vybe_ECS_OVERRIDE))
         0)))

(defn has-id
  "Check if the entity has this id."
  [world entity id]
  (c-api :has_id world (->id world entity) (->id world id)))

(defn entity-info
  "Find everything about an entity."
  [world entity]
  (when-let [info (-entity-info world entity)]
    (let [components (distinct
                      (let [arr (.get (.array info))
                            size (.get (.count info))
                            adapt (fn [id]
                                    (if-let [v (get-in @*env [world :component id])]
                                      v
                                      id))]
                        (for [idx (range size)]
                          (let [c-id (.getLong arr (* idx 8))
                                c-id (if (entity-overriden? world c-id)
                                       (bit-xor (->id world entity) (c-api :vybe_ECS_OVERRIDE))
                                       c-id)]
                            (when (c-api :has_id world (->id world entity) (->id world c-id))
                              (if (c-api :id_is_pair c-id)
                                [(adapt (c-api :vybe_pair_first world c-id))
                                 (adapt (c-api :vybe_pair_second world c-id))]
                                (adapt c-id)))))))]
      (->> components
           (remove nil?)
           (mapv #(cond
                    (instance? VybeComponent %)
                    [(:comp-sym %) (-pselect-all (get-c world entity %) %)]

                    (sequential? %)
                    (if (instance? VybeComponent (first %))
                      [[(:comp-sym (first %)) (second %)]
                       (-pselect-all (get-c world entity %) (first %))]
                      [(if (number? (first %))
                         (get-full-path world (first %))
                         (first %))
                       (if (number? (second %))
                         (get-full-path world (second %))
                         (second %))])

                    :else
                    (if (number? %)
                      (get-full-path world %)
                      %)))
           vec))))

(defn field-set?
  "Check if a field (by idx) in a iterator is set, useful for `:maybe` (Optional)."
  [iter idx]
  (c-api :field_is_set iter idx))

(defn -query-debug
  "Query components based on a query desc. See `query-debug`."
  ([world query-desc]
   (-query-debug world query-desc {}))
  ([world query-desc {:keys [entity-info?]
                      :or {entity-info? false}}]
   (when-let [query (->> query-desc
                         (jnr/make-instance ecs_query_desc_t)
                         (c-api :query_init world))]
     (try
       (let [iter-p (c-api :vybe_query_iter world query)
             id->identifier (get-in @*env [world :component])
             *acc (transient [])]
         (while (c-api :query_next iter-p)
           (let [iter-table (pget iter-p :ecs_iter_t :table)
                 iter-offset (pget iter-p :ecs_iter_t :offset)

                 ;; DEBUG
                 #_ #__ (do
                          (println :field_count (pget iter-p :ecs_iter_t :field_count))
                          (println :iter_count (pget iter-p :ecs_iter_t :count))
                          (def iter-p iter-p))

                 iter-fields
                 (->> (range (pget iter-p :ecs_iter_t :field_count))
                      (mapv (fn [^long idx]
                              (let [field-idx (inc idx)
                                    c-or-pair-id (c-api :field_id iter-p field-idx)
                                    pair? (c-api :id_is_pair c-or-pair-id)
                                    c-id (if pair?
                                           (c-api :vybe_pair_first world c-or-pair-id)
                                           c-or-pair-id)
                                    pair (when pair?
                                           (let [rel (id->identifier c-id)
                                                 target (or (-> (c-api :vybe_pair_second world c-or-pair-id)
                                                                id->identifier)
                                                            (c-api :vybe_pair_second world c-or-pair-id))]
                                             [(if (instance? VybeComponent rel)
                                                (:comp-sym rel)
                                                rel)
                                              (if (instance? VybeComponent target)
                                                (:comp-sym target)
                                                target)]))
                                    c (id->identifier c-id)]

                                (cond
                                  (and pair? (c-api :id_is_tag world c-or-pair-id))
                                  {:pair pair}

                                  (and (:struct c) (not (c-api :id_is_tag world c-or-pair-id)))
                                  (let [it-p (when (field-set? iter-p field-idx)
                                               (or (c-api :field_w_size iter-p
                                                          (-struct-size (:struct c))
                                                          field-idx)
                                                   ;; If the field is a `OR` (or some query)
                                                   ;; where you have 2 different types, you will
                                                   ;; have to fetch it from the table.
                                                   (c-api :table_get_id world iter-table c-id iter-offset)
                                                   ;; Or it will be `nil` if it comes from
                                                   ;; a `NOT`.
                                                   ))]
                                    (when it-p
                                      {:it-p it-p
                                       :size (-struct-size (:struct c))
                                       :comp-select (pselect-builder c)
                                       :comp-sym (:comp-sym c)
                                       :pair pair
                                       :read-only (c-api :field_is_readonly iter-p field-idx)
                                       :write-only (c-api :field_is_writeonly iter-p field-idx)})))))))]

             ;; DEBUG
             #_(def iter-fields iter-fields)

             (doseq [^int idx (range (pget iter-p :ecs_iter_t :count))]
               ;; Get entities + data.
               (conj! *acc {:vy/entity (let [id (-> (pget iter-p :ecs_iter_t :entities)
                                                    (.getLong (* idx 8)))]
                                         (if entity-info?
                                           {:id id
                                            :identifier (or (id->identifier id)
                                                            (get-name world id)
                                                            id)
                                            :components (entity-info world id)}
                                           {:id id
                                            :identifier (or (id->identifier id)
                                                            (get-name world id)
                                                            id)}))
                            :vy/data (->> iter-fields
                                          (mapv (fn [{:keys [it-p size comp-sym comp-select pair read-only write-only]}]
                                                  (cond
                                                    it-p
                                                    [(or pair comp-sym)
                                                     (merge (-> (.slice ^Pointer it-p (* idx ^int size))
                                                                comp-select)
                                                            {:vy/read-only read-only
                                                             :vy/write-only write-only})]

                                                    pair
                                                    pair))))}))))
         (persistent! *acc))
       (finally
         (c-api :query_fini query))))))

(defn query
  "Create a cached query. It's lower-level than a system, but you have more
  flexibility."
  [world query-expr]
  (->> (parse-query-expr world query-expr)
       (jnr/make-instance ecs_query_desc_t)
       (c-api :query_init world)))

(defn query-debug
  "Query components based on a query expression (EDN). Useful for debugging as it
  will return all the available fields for all the components.

  `params` available:
  - `entity-info?`, if you want the entire info for the matched entities"
  ([world query-expr]
   (query-debug world query-expr {}))
  ([world query-expr params]
   (-query-debug world (parse-query-expr world query-expr) params)))

(defn add-hooks
  "Add hooks to a component.

  This is a Flecs feature that allows us to list to component events,
  e.g. when adding a component to some entity or when removing from it.

  It could be faster if https://github.com/jnr/jnr-ffi/issues/125 were resolved."
  [world component {:keys [on-add on-set on-remove]}]
  (c-api :set_hooks_id world (->id world component)
         (new-t :ecs_type_hooks_t
                (cond-> {}
                  on-add (assoc :on_add (jnr/ptr-callback on-add))
                  on-set (assoc :on_set (jnr/ptr-callback on-set))
                  on-remove (assoc :on_remove (jnr/ptr-callback on-remove))))))
#_ (do
     (def world (init))
     (defcomp MyComp
       [:x :float]
       [:y :float])
     (add-hooks world MyComp {:on-add (fn [iter-p]
                                        (def ent
                                          (-> (pget iter-p :ecs_iter_t :entities)
                                              (.getLong (* 0 8)))))}))


(comment

  ;; DONE
  (query-debug world [[:pair Translation :local]
                      [:pair Translation :global]
                      [:maybe {:flags #{:parent :cascade}}
                       [:pair Translation :global]]
                      #_[:or
                         [:maybe {:flags #{:parent :cascade}}
                          [:pair Translation :global]]
                         [:pair Translation :local]]])
  (-sss)

  ;; DONE (the issue was the offset!!!)
  (->> {:filter {:terms [{:id (pair world Translation :local)}
                         {:id (pair world Translation :global)}
                         {:id (pair world Translation :global)
                          :src {:flags (bit-or (constant :EcsParent)
                                               (constant :EcsCascade))}
                          :oper (enum :ecs_oper_kind_t/EcsOptional)
                          #_ #_:inout (enum :ecs_inout_kind_t/EcsIn)}]}}
       (-query-debug world))
  (-sss)


  (jnr/-t :ecs_filter_desc_t)
  (bean (jnr/struct :ecs_filter_desc_t #_{:info true}))

  ;; For some reason, oper is not being set.
  ;; Because it's in an array???
  ;; Investigate field offsets and size
  ;; DONE, offset was the issue.

  (jnr/-t :ecs_term_t)
  (Struct/size (jnr/struct :ecs_term_t))

  (->id world entity)
  (->id world :parent)






  ;; DONE
  (-query-debug world {:filter {:terms [{:id (->id world A)}]}})
  (-query-debug world {:filter {:terms [{:id (->id world :d)}]}})

  ;; BASELINE
  (query-debug world [A])
  (query-debug world :d)

  ;; DONE (good for debugging the filter)
  (let [filter (->> {:terms [{:id (->id world :d)}]}
                    (jnr/make-instance (jnr/struct :ecs_filter_desc_t)))]
    (-> (c-api :filter_init world filter)
        (pget :ecs_filter_t :term_count)))

  (let [filter (->> {:expr "vybe_api_VY_Translation || vybe_api_VY_A ||  vybe_api_VY_B"}
                    (jnr/make-instance (jnr/struct :ecs_filter_desc_t)))
        terms-p (-> (c-api :filter_init world filter)
                    (pget :ecs_filter_t :terms))]
    (pselect (.slice terms-p (* 1 (-struct-size (jnr/struct :ecs_term_t))))
             :ecs_term_t [:oper]))




  (v.type/-vybe-name Translation)
  (entity-info world entity)

  ;; DONE
  (query-debug world [[:pair Translation :local]
                      [:pair Translation :global]])
  ;; DONE
  (query-debug world [:pair Translation :*])

  (query-debug world D)

  (query-debug world [:pair :vy.b/EcsIsA :bolha])
  (query-debug world [:pair B :_])
  (query-debug world [:pair B :*])
  (query-debug world [:pair B C])
  (query-debug world [A [:pair B C]])
  (query-debug world [A [:pair C :a]])
  (query-debug world [A [:pair :a C]])
  (query-debug world [A [:pair :a :d]])
  (query-debug world [A C])

  ())

(defcomp A
  [:x :float])

;; DONE Check the reflection here!
(defcomp B
  [:x :float]
  [:a A])

#_(jnr/-prepare-class (:struct B))
#_(jnr/-prepare-class (:struct A))

(defcomp C
  [:x :float]
  [:y :float])

(defcomp D
  [:x :float]
  [:b B])

(defcomp Translation
  [:x :float]
  [:n1 :string]
  [:n2 :string]
  [:n3 :string]
  [:y :float])

(defcomp E
  [:x :float]
  [:y :float])

(defcomp Color
  {#_ #_:adapter (fn color-adapter
              [color]
              (zipmap [:r :g :b :a]
                      (color/scale-down color true)))}
  [:r :float]
  [:g :float]
  [:b :float]
  [:a :float])

(defcomp TextSection
  [:text :string]
  [:scale :float]
  [:fg_color Color]
  [:bg_color Color])

(defn -sss
  []

  (def world (init))
  (def entity (make-entity world {:name "Olha"}))
  (def entity-2 (make-entity world))

  (add-c world :bolha D {:x 33.333})

  ;; Callback for component `E` for when this component is added
  ;; to some entity.
  (add-hooks world E
             {:on-add (fn [iter-p]
                        (def bb (c-api :field_id iter-p 1))
                        (def aa (pget iter-p :ecs_iter_t :count))
                        (println
                         (let [id (-> (pget iter-p :ecs_iter_t :entities)
                                      (.getLong (* 0 8)))]
                           (or (get-name world id)
                               id))))})

  #_ (jnr/-prepare-class (jnr/struct :ecs_type_hooks_t))
  #_ (jnr/-t :ecs_type_hooks_t)

  (->> [(A {:x 11})
        (pair B C {:x 505})
        [:vy.b/EcsIsA :bolha]
        [:vy.b/EcsChildOf :parent]
        [B D]
        E
        :d
        [:a :d]
        [:a C]
        [C :a]
        :a

        (Translation :local nil)
        (Translation :global nil)
        (TextSection {:text "sss"})]
       (add-many world entity))

  (->> [(Translation :global {:x 200 :y 200 :n3 "ne33"})
        (Translation :local {:x 1 :y 0})]
       (add-many world :parent))

  (add-c world entity-2 [B D])
  (add-c world entity-2 E)

  (add-c world entity B {:x 10})
  (add-c world entity C {:x 15}))

#_(-sss)

(comment

  ;; DONE And
  ;; DONE Or
  ;; DONE Not
  ;; DONE Optional
  ;; DONE Test with keywords (tags)
  ;; DONE Pair
  ;; - Example, https://github.com/SanderMertens/flecs/blob/master/examples/c/relationships/relation_component/src/main.c
  ;; DONE Wildcard (*), returns all
  ;; DONE Any (_), at most one

  (doseq [idx (range 3 #_60)]
    (let [e (make-entity world)]
      (->> [(A {:x idx})
            (C {:x (- idx 100)
                :y (* 3 idx)})
            (B {:x (- idx)
                :a {:x (* 2 idx)}})]
           (add-many world e))))

  (c-api :lookup_path_w_sep world 0 ":parent.Olha" "." nil true)
  (c-api :lookup_path_w_sep world (->id world :parent) "Olha" "." nil true)

  (entity-info world entity)
  (for [_ (range 3000)] (entity-info world :parent))
  (query-debug world E {:entity-info? true})
  (entity-info world :a)
  (entity-info world :d)

  (->id world entity)

  ;; Position, Vel
  [A B C]
  [A :d]

  ;; Position, Velocity || Speed, Mass
  [A [:or B C]]

  ;; Position, !Velocity
  [A [:not C] B]
  [A [:or B C] [:not C]]
  ;; ONLY SUPPORTED BY RULES
  [A [:not [:or B C]]]

  ;; Position, ?Velocity
  [A [:maybe B]]

  ;; (Likes, Apples), (Likes, Pairs)
  [A [:pair :a :d]]
  [A [:pair :a C]]
  [A [:pair C :a]]
  [A [:pair B C]]
  [:pair B :*]                          ; all pairs for an entity
  [:pair B :_]                          ; at most one pair per entity







  ;; DONE Traversable
  ;; DONE Parent
  ;;   - https://github.com/SanderMertens/flecs/blob/master/examples/c/queries/hierarchies/src/main.c

  ;; `D` is shared , so this query gets both root and childs (but it's
  ;; not ChildOf!).
  D

  ;; Position, ?Position(parent|cascade)

  ;; This one gets all the D components from parents (and IsA?)








  ;; ;;;;;;;;;;;;;;;;;; FOR LATER ;;;;;;;;;;;;;;

  ;; LATER Source
  ;; LATER Change detection
  ;; LATER Access modifiers
  ;; LATER Sorting
  ;; LATER Grouping
  ;; LATER Singleton

  ;; Position, [in] Velocity




  ;; RULES
  ;; LATER Component inheritance
  ;; LATER Transitive relationships
  ;; LATER Query scope
  ;; LATER Equality
  ;; LATER Variables

  ;; Position, !{ Velocity || Speed }

  ;; Position($this), !{ ChildOf($child, $this), Position($child) }


  ())

(defn add-overrides
  "Add components as overrides for an entity, useful for Prefabs so a component
  is not shared."
  [world entity components]
  (let [is-a-id (->id world :vy.b/EcsIsA)]
    (run! #(let [id (->id world %)
                 pair? (c-api :id_is_pair id)]
             (when-not (and pair?
                            (= (c-api :vybe_pair_first world id)
                               is-a-id))
               ;; Check that we are not doing some illegal override.
               (c-api :override_id world (->id world entity) id)))
          components)))

(defn add-prefab
  "Add a prefab to the world with some components.

  All components are overrided by default."
  [world entity components]
  (let [id (add-c world entity :vy.b/EcsPrefab)]
    (add-many world entity components)
    (->> components
         (mapv (fn [v]
                 (cond
                   ;; Pair.
                   (map? v)
                   (ffirst v)

                   (instance? VybeComponent v)
                   v

                   (instance? jnr.ffi.Struct v)
                   (type v)

                   :else
                   v)))
         (add-overrides world entity))
    id))

(comment

  (defcomp Position
    [:x :float]
    [:y :float]
    [:z :float])

  (defcomp Size
    [:size :float])

  (defcomp Color
    [:r :float]
    [:g :float]
    [:b :float]
    [:a :float])

  (add-prefab world :vy.pf/sprite
              [(pair Position :global {:x 0 :y 0 :z 0})
               (pair Position :local {:x 0 :y 0 :z 0})
               (Size {:size 0})
               (Color {:r 1 :g 1 :b 1 :a 1})])

  (entity-info world :vy.pf/sprite)
  (get-c world :vy.pf/sprite [Position :local] [:x :y :z])
  (get-c world :vy.pf/sprite [Position :global] [:x :y :z])
  (get-c world :vy.pf/sprite Color [:r :g :b :a])

  (def ent
    (make-entity world [:vy.b/EcsIsA :vy.pf/sprite]))
  (entity-info world ent)

  (get-c world ent Size [:size])
  (get-c world ent [Position :global] [:x :y :z])

  (set-c world ent [Position :local] {:x 30})
  (get-c world ent [Position :local] [:x :y :z])

  (set-c world ent Color {:g 0.5})
  (get-c world ent Color)

  (add-c world ent (Color {:r 10}))

  (doseq [_ (range 30)]
    ;; ~4.8ms
    (time
     (doseq [_ (range 10000)]
       (get-c world ent Color [:r :g :b :a]))))

  ())

(defmacro slice
  "Macro to get a iterator field based on a component and idx.

  `component` should exist at compile-time."
  [arr component idx]
  (let [^int comp-size (-struct-size (:struct @(resolve component)))]
    `(.slice ~arr (* ~(with-meta idx {:tag 'int}) ~comp-size))))

(defmacro field
  "Macro to get a iterator field (Pointer array) based on a component and idx.
  It checks if the field is set.

  `component` should exist at compile-time."
  [iter component idx]
  (cond
    (keyword? component)
    `(when (c-api :field_is_set ~iter ~idx)
       ~component)

    (and (sequential? component)
         (contains? #{:* :_} (last component)))
    `(when (c-api :field_is_set ~iter ~idx)
       [~(first component)
        (let [world# (iter-world ~iter)
              id# (c-api :vybe_pair_second world# (c-api :field_id ~iter ~idx))]
          (or (get-in @*env [(get-world world#) :component id#])
              id#))])

    (:struct @(resolve component))
    (let [^int comp-size (-struct-size (:struct @(resolve component)))]
      `(when (c-api :field_is_set ~iter ~idx)
         (c-api :field_w_size ~iter ~comp-size ~idx)))

    :else
    (throw (ex-info "Field not supported for this component (yet)"
                    {:iter iter
                     :component component
                     :idx idx}))))

(defmacro with-changed
  "Only iterate if changed."
  [iter & body]
  `(if-not (c-api :query_changed nil ~iter)
     (c-api :query_skip ~iter)
     (do ~@body)))

(defmacro with-each
  "Macro to iterate over a `iter`. It returns `nil`, if you need the returned
  values, use `with-for` (more expensive, less performant).

  All components in `component-bindings` should exist at compile-time.

  There is also support to special bindings:
  - :vy/entity, get the entity associated to this loop iteration
    - A long
  - :vy/idx, get the index associated to this loop iteration
    - You can do a lot of things with the index, refer to the Flecs manual
      about queries, https://www.flecs.dev/flecs/md_docs_Queries.html

  Example

  (vy/with-each iter [size vy.c/Size
                      pos vy.c/Position
                      entity :vy/entity]
   (def my-entity entity)
   (-> pos
       (vy/pupdate :x + (vy/pget size :width))
       (vy/pupdate :y + (vy/pget size :height))))"
  [iter component-bindings & body]
  (let [[opts component-bindings] (if (map? (first component-bindings))
                                    [(first component-bindings) (rest component-bindings)]
                                    [nil component-bindings])
        partitioned (->> component-bindings
                         (partition-all 2 2))
        special-keywords #{:vy/idx :vy/entity}
        special-bindings (->>  partitioned
                               (filter (comp special-keywords last))
                               (mapv vec)
                               (into {}))
        partitioned (remove (comp special-keywords last) partitioned)
        arr-bindings (->> partitioned
                          (map-indexed (fn [idx [sym component]]
                                         [(gensym (symbol (str sym "-arr-")))
                                          `(field ~iter ~component ~(inc idx))]))
                          vec)
        idx-sym (with-meta (gensym 'idx-)
                  {:tag 'long})
        field-bindings (->> arr-bindings
                            (mapv (fn [idx [sym component] [arr-sym _]]
                                    (if (or (keyword? component)
                                            (sequential? component))
                                      [sym arr-sym]
                                      [(with-meta sym
                                         {(keyword (:comp-sym @(resolve component)))
                                          true

                                          :vy/-read-only-code `(c-api :field_is_readonly ~iter ~(inc idx))
                                          :vy/-write-only-code `(c-api :field_is_writeonly ~iter ~(inc idx))})
                                       `(when ~arr-sym
                                          (slice ~arr-sym ~component ~idx-sym))]))
                                  (range)
                                  partitioned))]
    `(let ~(vec (apply concat arr-bindings))
       (doall
        (~(if (:with-for? opts) `for `doseq)
         [~idx-sym (range (pget ~iter :ecs_iter_t :count))]
         (let ~(vec (concat (apply concat field-bindings)
                            (->> special-bindings
                                 (mapcat (fn [[sym tag]]
                                           (case tag
                                             :vy/idx
                                             [sym idx-sym]

                                             :vy/entity
                                             [sym `(-> (pget ~iter :ecs_iter_t :entities)
                                                       (.getLong (* ~idx-sym 8)))]))))))
           ~@body))))))

(defmacro with-for
  "Like `with-each`, but returns the values."
  [iter component-bindings & body]
  `(with-each ~iter ~(cons {:with-for? true} component-bindings) ~@body))

(defmacro with-first
  "Convenience that uses `with-for` and returns the first value."
  [iter component-bindings & body]
  `(first (with-for ~iter ~component-bindings ~@body)))

(defn iter-world
  "Get world from iterator."
  [iter]
  (pget iter :ecs_iter_t :world))

(defn iter-event?
  "Check that `event` is the one associated with this iterator, usually
  used with observers.."
  [iter event]
  (= (pget iter :ecs_iter_t :event)
     (->id (iter-world iter) event)))

(defmacro with-scope
  "Sets temporarily a scope (usually a parent). All the entities defined in the `body` will
  be a child of the scope. This includes components, so make sure to create them
  ahead of time if you don't want them being a child of this scope!."
  [world scope & body]
  `(let [world# ~world
         entity# (->id world# ~scope)
         previous-scope# (c-api :set_scope world# entity#)]
     (try
       ~@body
       (finally
         (c-api :set_scope world# previous-scope#)))))

(defn add-system
  "Add a system from a var or from a function. Metadata should be
  associated with the var (or function):

    :vy/query      : required
    :vy/name       : optional, if `sys` is a var, then the name will be the var name
    :vy/scope      : optional, uses `with-scope` to define a parent for this system, useful if you want to group systems
                     so you can, e.g., disable them
    :vy/phase      : optional, default is :vy.b/EcsOnUpdate, see https://www.flecs.dev/flecs/md_docs_DesignWithFlecs.html#autotoc_md92
    :vy/wrapper    : optional, a function which receives the same arguments as the system you are setting + a function, its main
                     purpose is to have control for things that you want to do only once (e.g. beginning and ending a batch) "
  ([world sys]
   (add-system world [] sys))
  ([world args sys]
   (let [{:vy/keys [name query phase wrapper scope]
          :or {phase :vy.b/EcsOnUpdate}}
         (meta sys)

         iterator-fn (fn [iter]
                       (try
                         ((apply partial sys args) iter)
                         (catch Exception e
                           (println e))))
         run-fn (let [wrapper (if wrapper
                                (apply partial wrapper args)
                                (fn [_iter f]
                                  (f)))]
                  (if query
                    (fn [iter]
                      (try
                        (wrapper iter (fn []
                                        (while (c-api :query_next iter)
                                          (iterator-fn iter))))
                        (catch Exception e
                          (println e))))
                    (fn [iter]
                      (try
                        (wrapper iter (fn [] (iterator-fn iter)))
                        (catch Exception e
                          (println e))))))
         name (cond
                name (str name)
                (var? sys) (v.type/-vybe-name sys)
                #_ #_:else (throw (ex-info (str "When adding a non-var as a system, associate "
                                                "`:vy/name` as its metadata")
                                           {:sys sys
                                            :args args
                                            :world world})))
         entity-id (if name
                     (let [^ecs_entity_desc_t edesc (jnr/make-instance
                                                     ecs_entity_desc_t
                                                     {:id 0
                                                      :name name
                                                      :add [(pair-id world :vy.b/EcsDependsOn phase)
                                                            (->id world phase)]})]
                       (if scope
                         (with-scope world scope
                           (c-api :entity_init world edesc))
                         (c-api :entity_init world edesc)))
                     (if scope
                       (with-scope world scope
                         (make-entity world))
                       (make-entity world)))
         desc (jnr/make-instance
               (jnr/struct :vy.b/ecs_system_desc_t)
               (cond-> {:entity entity-id}
                 run-fn
                 (assoc :run (jnr/ptr-callback run-fn))

                 (not run-fn)
                 (assoc :callback (jnr/ptr-callback iterator-fn))

                 query
                 (assoc :query (parse-query-expr world query))))]
     (cache-id world sys entity-id)
     (c-api :system_init world desc))))

#_(-> (jnr/make-instance
       (jnr/struct :vy.b/ecs_observer_desc_t)
       (assoc (parse-query-expr world :a)
              :events [(->id world :vy.b/EcsOnAdd)
                       (->id world :vy.b/EcsOnSet)
                       (->id world :vy.b/EcsOnRemove)])))

#_ (def world (init))

#_(jnr/make-instance
   (jnr/struct :vy.b/ecs_system_desc_t))

#_(Struct/size (jnr/struct :vy.b/ecs_observer_desc_t))

(defn add-systems
  "Add multiple systems, see `add-system`."
  ([world systems]
   (add-systems world [] systems))
  ([world args systems]
   (mapv (partial add-system world args) systems)))

(defn progress
  "Progress the systems attached to the pipelines."
  ([world]
   (c-api :progress world 0.0))
  ([world delta-time]
   (c-api :progress world delta-time)))

(defn add-observer
  "Add a observer from a var or from a function (very similar to a system).
  Metadata should be associated with the var (or function):

    :vy/events     : required, list of events to match on
        :on-add, when a component is added
        :on-set, when a component is added or set
        :on-remove, when a component is removed
      OR
        any other custom event you define
    :vy/query      : optional, if not defined, `:*` will be used
    :vy/name       : optional, if `sys` is a var, then the name will be the var name"
  ([world sys]
   (add-observer world [] sys))
  ([world args sys]
   (add-observer world args sys (meta sys)))
  ([world args sys {:vy/keys [name query events]
                    :vy.observer/keys [yield-existing]}]
   (when (empty? events)
     (throw (ex-info "Events shouldn't be empty for an observer"
                     {:name name
                      :events events
                      :sys sys
                      :args args
                      :world world})))
   (let [iterator-fn (apply partial sys args)
         name (cond
                name (str name)
                (var? sys) (v.type/-vybe-name sys))
         entity-id (if name
                     (let [^ecs_entity_desc_t edesc (jnr/make-instance
                                                     ecs_entity_desc_t
                                                     {:id 0
                                                      :name name})]
                       (c-api :entity_init world edesc))
                     (make-entity world))
         desc (jnr/make-instance
               (jnr/struct :vy.b/ecs_observer_desc_t)
               (merge
                {:entity entity-id
                 :callback (jnr/ptr-callback iterator-fn)
                 :events (mapv (fn [v]
                                 (->id world (or ({:on-add :vy.b/EcsOnAdd
                                                   :on-set :vy.b/EcsOnSet
                                                   :on-remove :vy.b/EcsOnRemove}
                                                  v)
                                                 v)))
                               events)}
                (if (seq query)
                  (parse-query-expr world query)
                  (parse-query-expr world [:*]))
                (when yield-existing
                  {:yield_existing yield-existing})))]
     (cache-id world sys entity-id)
     (c-api :observer_init world desc))))

(defn add-observers
  "Add multiple observers, see `add-observer`."
  ([world systems]
   (add-observers world [] systems))
  ([world args systems]
   (mapv (partial add-observer world args) systems)))

(defn add-event-handler
  "Add an observer that matches `events` to the `identifier` entity.
  Adds the entity to itself so we can use it in the query.

  It uses observers behind the scenes."
  [world identifier events f]
  (add-c world identifier identifier)
  (add-observer world (with-meta f {:vy/query [identifier]
                                    :vy/events events})))

(defn add-pipeline
  "Add a pipeline to the world, but it does not use it. To replace the existing
  pipeline, use `set-pipeline`."
  [world pipe-identifier query]
  (let [pipe-desc (jnr/make-instance
                   (jnr/struct :vy.b/ecs_pipeline_desc_t)
                   {:entity (->id world pipe-identifier)
                    :query (parse-query-expr world (cons :vy.b/EcsSystem
                                                         (if (and (sequential? query)
                                                                  (not (contains?
                                                                        -parser-special-keywords
                                                                        (first query))))
                                                           query
                                                           [query])))})]
    (c-api :pipeline_init world pipe-desc)))

(defn set-pipeline
  "Replace existing world pipeline with this one. Create the pipeline using
  `add-pipeline` or use the 3-arity version to create a pipeline and set it
  in one step."
  ([world pipe-identifier]
   (c-api :set_pipeline world (->id world pipe-identifier)))
  ([world pipe-identifier query]
   (set-pipeline world (add-pipeline world pipe-identifier query))))

(defn add-phase
  "Add a pipeline phase."
  ([world phase-identifier depends-on]
   (add-many world phase-identifier [:vy.b/EcsPhase
                                     [:vy.b/EcsDependsOn depends-on]])))

(defn emit-event
  "Emit an event.

  E.g.

    (vy/emit-event world entity :vy.ev/hover)"
  ([world event]
   (emit-event world event event))
  ([world entity event]
   (let [entity-id (->id world entity)
         event-id (->id world event)
         e-info (-entity-info world entity-id)
         event-desc (jnr/make-instance
                     (jnr/struct :vy.b/ecs_event_desc_t)
                     {:event event-id
                      :entity entity-id
                      :ids (Struct/getMemory e-info)})]
     (c-api :emit world event-desc))))

(defn enable
  "Enable a component/entity."
  ([world entity]
   (c-api :enable world (->id world entity) true))
  ([world entity component]
   (c-api :enable_id world (->id world entity) (->id world component) true)))

(defn disable
  "Disable a component/entity."
  ([world entity]
   (c-api :enable world (->id world entity) false))
  ([world entity component]
   (c-api :enable_id world (->id world entity) (->id world component) false)))

(defn log-set
  "Set log level. Check the nREPL server.

  - `false` for disabling it (equivalent to -1 in Flecs)
  - `true` for enabling it (equivalent to 2 in Flecs)
  - `:all` for enabling all logs (equivalent to 3 in Flecs)"
  [enabled]
  (case enabled
    :all         (c-api :log_set_level 3)
    (false, nil) (c-api :log_set_level -1)
                 (c-api :log_set_level 2))
  #_ (c-api :log_enable_colors true))

#_(defcomp Velocity
    [:x :float]
    [:y :float]
    [:z :float])

#_(defn move
    {:vy/query [Position Velocity]}
    [iter]
    (let [^int pos-size (-struct-size (:struct Position))
          ^int vel-size (-struct-size (:struct Velocity))
          pos-p (c-api :field_w_size iter pos-size 1)
          vel-p (c-api :field_w_size iter vel-size 2)]
      (doseq [^int idx (range (pget iter :ecs_iter_t :count))]
        ;; You can hint (using keywords) that this pointer represents some
        ;; component.
        (let [^::Position pos (.slice pos-p (* idx pos-size))
              ^::Velocity vel (.slice vel-p (* idx vel-size))]
          (-> pos
              (pupdate :x + (pget vel :x))
              (pupdate :y + (pget vel :y))))

        ;; Or you can add the component type inlined (more verbose).
        #_(let [pos (.slice pos-p (* idx pos-size))
                vel (.slice vel-p (* idx vel-size))]
            (-> pos
                (pupdate Position :x + (pget vel Velocity :x))
                (pupdate Position :y + (pget vel Velocity :y)))))))

#_(def world (init))

(comment

  (do
    (add-system world #'move)

    (->> [(Position {:x 10 :y 10})
          (Velocity {:x 0.2 :y 0.1})]
         (add-many world :pitoco))

    (doseq [idx (range 600000)]
      (->> [(Position {:x idx :y (/ idx 2)})
            (Velocity {:x 0.2 :y 0.1})]
           (add-many world (make-entity world)))))

  ;; Run all systems using delta time from last time it was run..
  (doseq [_ (range 100)]
    (time (c-api :progress world 0.0)))

  (take 300 (query-debug world [Position Velocity]))



  (get-c world :pitoco Position [:x :y :z])

  ;; Delete system.
  (delete world #'move)
  (entity-info world #'move)

  ;; Call a one-off.
  #_(time (c-api :run world (->id world #'jjj) 0.0 nil))

  ())

(comment

  (do
    (defcomp Position
      [:x :float]
      [:weird :long]
      [:y :float])

    (defcomp Vel
      [:x :float]
      [:y :float]
      [:pos Position]
      [:name :string])

    (def world (init))
    (def entity (make-entity world {:name "Olha"}))

    (def *ents (atom []))

    #_(add-hooks world Position
                 {:on-add (fn [iter-p]
                            (def ent
                              (-> (pget iter-p :ecs_iter_t :entities)
                                  (.getLong (* 0 8)))))
                  :on-set (fn [iter-p]
                            (swap! *ents conj
                                   (-> (pget iter-p :ecs_iter_t :entities)
                                       (.getLong (* 0 8)))))})

    (->> [(Position {:x 10 :y 31})
          (Vel {:x -3 :y 31 :pos {:x 93.2}})
          [Vel Position]
          {[Position Vel] {:x 15}}
          {[Position :porreta] {:x 16}}
          [Position Position]
          [:porreta :doida/demais]]
         (add-many world entity)))

  ;; 10 to 15ms
  ;; 18 to 21 ms with the :on-set enabled for Position
  ;; 3ms using set-c.
  ;; 2.8ms after NDEBUG
  (doseq [_ (range 300)]
    (time
     (let [j (float 20)]
       (doseq [i (range 10000)]
         (set-c world entity Position {:x i :y j})))))

  ;; 6 to 8ms
  ;; 3.5ms
  ;; 2.8ms after NDEBUG.
  (doseq [_ (range 300)]
    (time
     (doseq [i (range 10000)]
       #_(let [p (get-c world entity Position)]
           {:x (pget p Position :x)
            :y (pget p Position :y)})

       #_(pselect (get-c world entity Position) Position [:x :y])

       (let [vel (get-c world entity Vel)]
         #_(pselect vel Vel [:x :y {:pos [:x :weird :y]}])
         (pselect vel Vel [:x :y :pos])))))

  (doseq [idx (range 600000)]
    (let [e (make-entity world)]
      (set-c world e Position {:x idx})
      (set-c world e Vel {:x 0.3 :y 0.5})))

  (doseq [_ (range 60)]
    (let [e (make-entity world)]
      (add-c world e Position {:x (rand 20)})
      (add-c world e Vel {:x 10 :y 34})))




  ;; Query.
  ;; Create query only once as it will be updated and cached automatically
  ;; by Flecs.
  (def query
    (->> (jnr/make-instance ecs_query_desc_t
                            {:filter {:expr (format "%s, %s"
                                                    (v.type/-vybe-name Position)
                                                    (v.type/-vybe-name Vel))}})
         (c-api :query_init world)))

  (def system-p
    (->  (jnr/struct :vy.b/ecs_system_desc_t)
         (jnr/make-instance {:callback (jnr/ptr-callback
                                        (fn [pp]
                                          #_ (pget pp :ecs_iter_t :count)
                                          (def pp pp)))})))

  ;; Only set.
  ;; DONE: Type hint `pos-p` and `vel-p` so that `pupdate`/`pget` can know
  ;;       about it.
  (when query
    (let [pos-size (-struct-size (:struct Position))
          vel-size (-struct-size (:struct Vel))]
      (doseq [_ (range 30)]
        ;; ~16ms for 1200000 entities (setting x and y positions using velocities).
        (time
         (let [iter-p (c-api :vybe_query_iter world query)]
           (while (c-api :query_next iter-p)
             (let [pos-p (c-api :field_w_size iter-p pos-size 1)
                   vel-p (c-api :field_w_size iter-p vel-size 2)]
               (doseq [idx (range (pget iter-p :ecs_iter_t :count))]
                 (let [^::Position pos (.slice pos-p (* idx pos-size))
                       ^::Vel vel (.slice vel-p (* idx vel-size))]
                   (-> pos
                       (pupdate :x + (pget vel :x))
                       (pupdate :y + (pget vel :y))))))))))))

  ;; Read.
  (when query
    (let [struct- (:struct Position)
          struct-size (-struct-size struct-)]
      (def struct-size struct-size)
      (doseq [_ (range 30)]
        (time
         ;; For ~1200000 entities, it takes ~26ms when JIT kicks in (select only).
         ;; UPDATE: For ~1200000, it takes about the same time, but with a set as well!!
         (let [iter-p (c-api :vybe_query_iter world query)
               #_(c-api :vybe_query_iter_ttt world query system-p)
               *acc (transient [])]
           (while (c-api :query_next iter-p)
             (let [pos-p (c-api :field_w_size iter-p struct-size 1)]
               #_(println :COUNT (pget iter-p :ecs_iter_t :count))
               (doseq [idx (range (pget iter-p :ecs_iter_t :count))]
                 (let [^::Position p (.slice ^Pointer pos-p (unchecked-multiply idx struct-size))]
                   (conj! *acc (pselect p [:x :y]))))))
           (def acc (persistent! *acc)))))))

  (take 300 acc)
  (count acc)








  (pget (get-c world entity Vel) Vel x)
  (pget (get-c world entity Vel) Vel pos)
  Vel




  (->id world :doida/demais)




  (entity-info world "Olha")
  (entity-info world Position)

  (->id world [Position Vel])
  (delete world [Position Vel])
  (delete world Position)


  (-> (get-c world entity [Position Vel])
      (pget Position x))
  (get-c world entity [Position :porreta])
  (get-c world entity [:porreta :doida/demais])




  (remove-c world entity [Position Vel])
  (remove-c world entity [:porreta :doida/demais])

  (->id world :vy.b/EcsIdentifier)
  (->id world :vy.b/EcsName)

  (entity-info world "Olha")
  (entity-info world Position)

  (c-api :get_typeid world (->id world [:vy.b/EcsIdentifier :vy.b/EcsName]))
  (get-c world :vy.b/EcsIsA :vy.b/EcsComponent)
  (c-api :get_typeid world (->id world [Position Vel]))
  (c-api :get_typeid world (->id world [:porreta :doida/demais]))
  (get-c world entity [Position Vel])

  (c-api :get_id world (->id world entity) (->id world [:porreta :doida/demais]))
  (get-c world entity [:porreta :doida/demais])
  (add-c world entity [Vel :doida/demais] {:y 10})
  (get-c world entity [Vel :doida/demais])

  (->id world ::Component)

  (entity-info world "Olha")

  ;; Start the REST api, but it's not working!!
  (future
    (c-api :app_run world (jnr/make-instance (jnr/struct :vy.b/ecs_app_desc_t)
                                             {:enable_rest true})))

  ())
