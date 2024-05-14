(ns vybe.jnr
  (:refer-clojure :exclude [struct])
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.walk :as walk]
   [omkamra.jnr.struct :as struct]
   [omkamra.jnr.union :as union]
   [omkamra.jnr.enum :as enum]
   [omkamra.jnr.library :as library]
   [clj-antlr.core :as antlr]
   [clojure.pprint :as pp]
   [clojure.zip :as zip]
   [clojure.java.shell :as sh]
   [clj-java-decompiler.core :refer [decompile disassemble]])
  (:import
   (jnr.ffi LibraryLoader Pointer Struct Struct$String Struct$Pointer Memory)
   (jnr.ffi.byref PointerByReference)
   (com.vybe VybeInteropFunction VybeInteropFunction$VybePtrCallback VybeInteropFunction$VybeRetIntLongPtrLongPtrCallback)))

(set! *warn-on-reflection* true)

;; Generate source code (download JAR from https://www.antlr.org/download.html).
;; https://github.com/aphyr/clj-antlr/issues/25#issuecomment-1364543045
;; java -jar antlr.jar grammars/CSharpParser.g4

(defn- to-map
  [v]
  #_(def v v)
  (->> (rest v)
       (remove (comp #{:property_declaration} first))
       (mapv (fn [v]
               (vec
                (let [v (cond
                          (= (last v) "*")
                          (-> (vec (drop-last v))
                              (update 1 with-meta {:pointer true}))

                          (string? (last v))
                          (vec (drop-last v))

                          :else v)]
                  v))))
       (into {})))

(defn- struct-alias?
  [attrs]
  ;; Go to `CNode(Kind = "TypeAlias")]`.
  (let [*alias (atom false)]
    (->> (-> (meta attrs)
             :zipper
             zip/up
             zip/left
             zip/left
             zip/down
             zip/right
             zip/node)
         (walk/prewalk (fn [v]
                         (when (and (sequential? v)
                                    (and (= (first v) :string_literal)
                                         (= (last v) (pr-str "TypeAlias"))))
                           (reset! *alias true))
                         v)))
    (boolean @*alias)))

(defn- union?
  [attrs]
  ;; Go to `CNode(Kind = "Union")]`.
  (let [*alias (atom false)]
    (->> (-> (meta attrs)
             :zipper
             zip/up
             zip/left
             zip/left
             zip/down
             zip/right
             zip/node)
         (walk/prewalk (fn [v]
                         (when (and (sequential? v)
                                    (and (= (first v) :string_literal)
                                         (= (last v) (pr-str "Union"))))
                           (reset! *alias true))
                         v)))
    (boolean @*alias)))

(defn- normalize-field-type
  [structs field-type]
  (let [{:keys [alias fields]} (get structs field-type)
        new-field-type (:field-type (first fields))]
    (if alias
      (or (normalize-field-type structs new-field-type)
          new-field-type)
      field-type)))

#_ (normalize-field-type structs 'ecs_entity_t)
#_ (normalize-field-type structs 'ecs_id_t)

(defn- extract-offset
  [attributes]
  (-> (walk/postwalk
       (fn [v]
         (cond
           (and (sequential? v)
                (= (first v) :literal))
           [::literal (last v)]

           (and (sequential? v)
                (= (first v) :identifier))
           [::identifier (last v)]

           (and (sequential? v)
                (contains? #{::literal
                             ::identifier
                             ::attribute
                             ::result}
                           (first (last v))))
           (last v)

           (and (sequential? v)
                (= (first v) :attribute))
           (let [{::keys [identifier literal]}
                 (->> (remove string? v)
                      rest
                      (into {}))]
             (when (= identifier "FieldOffset")
               [::result (Integer/parseInt literal)]))

           :else
           v))
       attributes)
      last))

(defn- extract-lib-info
  [parsed]
  (when (instance? clj_antlr.ParseError parsed)
    (throw parsed))
  #_ (def parsed parsed)
  (let [*structs (atom [])
        *enums (atom [])
        *constants (atom [])
        tree (->> parsed
                  (walk/postwalk (fn [v]
                                   (if (sequential? v)
                                     (case (first v)
                                       :all_member_modifiers
                                       [(first v) (->> (rest v)
                                                       (mapv last))]

                                       (:conversion_operator_declarator
                                        :right_arrow
                                        :throwable_expression)
                                       nil

                                       :fixed_size_buffer_declarator
                                       [(first v)
                                        (->> (rest v)
                                             (remove string?)
                                             (into {}))]

                                       :attribute_section
                                       (vec (concat [(first v)]
                                                    (->> (rest v)
                                                         (remove string?))))

                                       :constant_declaration
                                       (swap! *constants conj v)

                                       (let [v-2 (->> v
                                                      (remove nil?)
                                                      (remove #{";"})
                                                      vec)]
                                         (if (> (count v-2) 1)
                                           v-2
                                           [(first v-2) {}])))
                                     v)))
                  zip/vector-zip)]

    (let [zip-children (fn [loc]
                         (when (vector? (first loc))
                           (when (= (first (first loc)) :struct_definition)
                             (swap! *structs conj (with-meta (first loc)
                                                    {:zipper loc})))
                           (when (= (first (first loc)) :enum_definition)
                             (swap! *enums conj (with-meta (first loc)
                                                  {:zipper loc}))))
                         (when-let [first-child (zip/down loc)]
                           (take-while (comp not nil?)
                                       (iterate zip/right first-child))))]
      (loop [ret []
             queue (conj clojure.lang.PersistentQueue/EMPTY tree)]
        (if (seq queue)
          (let [[node children] ((juxt zip/node zip-children) (peek queue))]
            (recur (conj ret node) (into (pop queue) children)))
          ret)))

    #_(def *structs *structs)
    #_(def *enums *enums)

    (let [structs @*structs
          structs
          (->> structs
               #_(drop 222)
               #_(take 1)
               (map-indexed
                (fn [idx [_struct-token _struct-str
                          [_identifier-token identifier]
                          [_struct-body-token & body] :as x]]
                  #_(def idx idx)
                  #_(def x x)
                  #_(def body body)
                  (let [body (->> body
                                  ;; Discard brackets from body.
                                  rest
                                  (drop-last)
                                  (mapv (comp vec rest))
                                  #_(drop 10)
                                  #_(take 1)
                                  (mapv #(vec (remove #{"fixed" ";"} %)))
                                  #_(mapv #(vec (remove (comp #{:fixed_size_buffer_declarator} first) %)))
                                  (mapv #(into {} %)))]

                    #_(def body body)
                    [(symbol identifier)
                     (with-meta
                       {:type :struct
                        :identifier (symbol identifier)
                        :fields (->> body
                                     #_(drop 10)
                                     #_(take 1)
                                     (mapv (fn [{:keys [attributes
                                                        _all_member_modifiers
                                                        common_member_declaration
                                                        fixed_size_buffer_declarator]}]
                                             #_(def fixed_size_buffer_declarator )
                                             #_(def common_member_declaration common_member_declaration)

                                             (if fixed_size_buffer_declarator
                                               {:field-type 'byte
                                                :field-name (symbol
                                                             (subs
                                                              (:identifier fixed_size_buffer_declarator)
                                                              1))
                                                :field-offset (extract-offset attributes)
                                                :pointer false
                                                :array true
                                                :size (let [*keeper (atom [])]
                                                        (walk/prewalk
                                                         (fn [v]
                                                           (if (and (sequential? v)
                                                                    (= (first v) :literal))
                                                             (do
                                                               (reset! *keeper
                                                                       (Integer/parseInt (last v)))
                                                               v)
                                                             v))
                                                         fixed_size_buffer_declarator)
                                                        @*keeper)}
                                               (let [{:keys [type_ field_declaration]}
                                                     (to-map common_member_declaration)

                                                     field-name-str (get-in field_declaration [1 1 1])]

                                                 #_(def type_ type_)
                                                 #_(def field_declaration field_declaration)
                                                 (when field-name-str
                                                   (let [field-type (or (some-> (get-in type_ [1 1 1 1])
                                                                                symbol)
                                                                        ;; E.g. void
                                                                        (some-> (get-in type_ [1])
                                                                                symbol)
                                                                        (some-> (get-in type_ [1 1])
                                                                                symbol))]
                                                     {:field-type field-type
                                                      :field-offset (extract-offset attributes)
                                                      :field-name (symbol (cond
                                                                            (= field-type 'CString)
                                                                            ;; Remove `_` from the beginning.
                                                                            (subs field-name-str 1)

                                                                            (str/starts-with? field-name-str "@")
                                                                            (subs field-name-str 1)

                                                                            :else
                                                                            field-name-str))
                                                      :pointer (boolean
                                                                (or (:pointer (meta type_))
                                                                    (= (get-in type_ [2]) "*")))}))))))
                                     (remove nil?)
                                     vec)}
                       (meta x))])))
               ;; Add alias or union.
               (mapv (fn [[identifier attrs]]
                       (let [alias (struct-alias? attrs)
                             union (union? attrs)]
                         [identifier (assoc attrs
                                            :alias alias
                                            :union union)])))
               (into (sorted-map)))

          enums @*enums
          enums
          (->> enums
               (map-indexed
                (fn [_idx [_enum-token _enum-str
                           [_identifier-token identifier]
                           _enum-base
                           [_enum-body-token & body]]]

                  [(symbol identifier)
                   {:identifier (symbol identifier)
                    :type :enum
                    :fields (->> body
                                 ;; Discard brackets from body.
                                 (remove string?)
                                 vec
                                 (mapv #(vec (remove string? %)))
                                 (mapv (comp vec rest))
                                 (mapv #(into {} %))
                                 (mapv (fn [{:keys [identifier expression]}]
                                         (let [*keeper (atom [])]
                                           (walk/prewalk (fn [v]
                                                           (if (and (sequential? v)
                                                                    (= (first v) :literal))
                                                             ;; Get the integer correspondent to the
                                                             ;; enum value.
                                                             (do
                                                               (reset! *keeper (Integer/parseInt (last v)))
                                                               v)
                                                             v))
                                                         expression)
                                           {:field-name (symbol identifier)
                                            :idx @*keeper}))))}]))
               vec
               (into (sorted-map)))

          constants
          (->> @*constants
               (mapv (fn [constant]
                       (let [*keeper (atom {})]
                         (walk/prewalk (fn [v]
                                         (when (sequential? v)
                                           (case (first v)
                                             :integral_type (swap! *keeper assoc :type (last v))
                                             :identifier (swap! *keeper assoc :identifier (last v))
                                             :literal (swap! *keeper assoc :value (last v))
                                             v))
                                         v)
                                       constant)
                         @*keeper)))
               (remove (comp nil? :type))
               (mapv (fn [{:keys [type identifier value]}]
                       [(keyword identifier)
                        {:type :constant
                         :value (case type
                                  ("int" "uint" "long" "ulong")
                                  (unchecked-long (bigdec value)))}]))
               (into (sorted-map)))]
      (merge
       (-> structs
           ;; Normalize types.
           (update-vals
            (fn [attrs]
              (-> attrs
                  (update :fields
                          (fn [fields]
                            (->> fields
                                 (mapv #(-> %
                                            (update :field-type
                                                    (partial normalize-field-type structs)))))))))))
       enums
       constants))))

(defn- adapt
  [s]
  (-> s
      (str/replace #"public delegate\* unmanaged.*" "")
      (str/replace #".*new\(\).*" "")
      (str/replace #"namespace.*" "")))

(defonce ^:private cs
  (antlr/parser "grammars/CSharpLexer.g4" "grammars/CSharpParser.g4" {}))

(defn- parse-cs
  [cs file]
  (try
    (-> (-> (slurp file)
            adapt)
        cs)
    (catch clj_antlr.ParseError e
      e)))

(comment

  (spit "gen.edn"
        (with-out-str
          (pp/pprint (parse-cs cs "gen.cs"))))

  (parse-cs cs "gen_safe.cs")

  ())

(def flecs-info
  (do
    #_(def parsed (parse-cs cs "gen.cs"))
    (-> (extract-lib-info (parse-cs cs "gen.cs"))
        (update-vals (fn add-field->idx
                       [struct-val]
                       (if (= (:type struct-val) :enum)
                         struct-val
                         (-> struct-val
                             (assoc :field->idx
                                    (->> (:fields struct-val)
                                         (map-indexed (fn [idx {:keys [field-name]}]
                                                        [field-name idx]))
                                         (into {})))))))
        ;; Manual overrides.
        (update-vals (let [overrides '{ecs_entity_desc_t
                                       {:f-overrides {add {:field-type unsigned-long-long
                                                           :size 32}}}

                                       ecs_observer_desc_t
                                       {:f-overrides {events {:field-type unsigned-long-long
                                                              :size 8}}}

                                       ecs_filter_desc_t
                                       {:f-overrides {terms {:field-type ecs_term_t
                                                             :size 16}}}

                                       ecs_term_id_t
                                       {:append {:field-type int,
                                                 :field-offset 28,
                                                 :field-name _padding,
                                                 :pointer false}}}]
                       (fn struct-overrides
                         [{:keys [identifier fields] :as struct-val}]
                         (if-let [{:keys [f-overrides append]} (get overrides identifier)]
                           (assoc struct-val :fields
                                  (->> (cond-> fields
                                         append (conj append))
                                       (mapv (fn [{:keys [field-name] :as field}]
                                               (merge field (get f-overrides field-name))))))
                           struct-val)))))))

#_ (-struct-info ecs_term_id_t)

(defn- eval-definition!
  ([definitions level identifier]
   (eval-definition! definitions level identifier nil))
  ([definitions level identifier definition-override]
   #_(def definitions definitions)
   #_(def identifier identifier)
   #_(def definition-override definition-override)

   #_(println (str/join (repeat (* level 3) \.)) identifier)
   (when-let [{:keys [identifier fields alias type union]} (or definition-override
                                                               (get definitions identifier))]
     #_(def fields fields)
     #_(def level level)
     #_(def identifier identifier)
     (case type
       :constant nil
       :enum
       (let [fields-with-types (mapv (fn [{:keys [field-name idx]}]
                                       [field-name idx])
                                     fields)
             form (concat (list `enum/define identifier)
                          fields-with-types)]
         #_(def fields-with-types fields-with-types)
         #_(def form form)
         (eval form))

       :struct
       (when-not alias
         (let [fields-with-types (mapv (fn [{:keys [field-type field-name pointer size]}]
                                         #_(keyword (str "vy.b." identifier) (str field-name))
                                         (let [field-type (case field-type
                                                            ulong 'uint64_t
                                                            uint 'uint32_t
                                                            ushort 'short
                                                            int 'int32_t
                                                            sbyte 'byte
                                                            CString 'Pointer
                                                            CBool 'Boolean
                                                            field-type)]
                                           #_(def field-type field-type)
                                           #_(def field-name field-name)
                                           ;; Make structs that this rely on first.
                                           (when-not (or (= field-type identifier) ; no recursion
                                                         pointer)
                                             (eval-definition! definitions (inc level) field-type))

                                           (when (and (not pointer)
                                                      (not (struct/resolve-struct-field-tag field-type)))
                                             (throw (ex-info "Type inexistent"
                                                             {:field-type field-type})))
                                           {:field-type (if (or pointer
                                                                (str/starts-with?
                                                                 (name field-type)
                                                                 "FnPtr"))
                                                          'Pointer
                                                          field-type)
                                            :field-name field-name
                                            :size size}))
                                       fields)
               #_ #__ (println (str/join (repeat (* level 3) \.))
                               "__ DEFINING __"
                               identifier)
               form (concat (list (if union `union/define `struct/define)
                                  identifier)
                            (->> fields-with-types
                                 (mapv (fn [{:keys [field-type field-name size]}]
                                         (if size
                                           (do #_(def field-name field-name)
                                               #_(def field-type field-type)
                                               #_(def size size)
                                               [(with-meta field-name
                                                  {:tag field-type})
                                                size])
                                           (with-meta field-name
                                             {:tag field-type}))))))]
           #_(def fields-with-types fields-with-types)
           #_(def form form)
           (eval form)))))))

#_ (eval-definitions!)

(defn eval-definitions!
  []
  (->> (keys flecs-info)
       #_(take 10)
       #_(filter #{'ecs_iter_t})
       (mapv (partial eval-definition! flecs-info 0))))

(eval-definitions!)

(defmacro -t
  "Return the type of an identifier. For REPL usage."
  [identifier]
  `((quote ~(let [s (name identifier)]
              (symbol
               ;; If it's a constructor, remove the end `.`, just for
               ;; convenience.
               (if (str/ends-with? s ".")
                 (subs s 0 (dec (count s)))
                 s))))
    flecs-info))

(defn -p
  "For REPL usage.

  Print representation to the STDOUT and returns the value."
  [v]
  (println (str v))
  v)

;; DONE Resolve type alias for ecs_entity_desc_t
;;   DONE Tag an alias as such
;; DONE Convert types (e.g. `int` to `int32_t`)
;; DONE Extract enums
;; DONE Fix array
;;   DONE `add` is missing for `ecs_entity_desc_t`
;; LATER Fix alignment
;;   LATER `ecs_event_desc_t` should be 72, but it's 68
;; DONE Fix ecs_set_id
;;   DONE Fix size
;;     - https://github.com/jnr/jnr-ffi/issues/256
;; DONE Do https://github.com/flecs-hub/flecs-cs/blob/main/src/cs/production/Flecs.Core/World.cs#L80
;;   DONE Create entity
;;   DONE Create component
;;   DONE Add component
;;   DONE Get component
;;   DONE Cache ->id and others per ecs
;;   DONE Cache struct size and alignment
;;   DONE Remove component
;;   DONE Query
;;     DONE Handle UNIONS!!
;;     LATER Are we able to use strings instead of pointers for CString?
;;     DONE Create helper to translate query in Clojure to filter.expr
;;     LATER Ability to use `terms` (we have to get rid of the bytes and use the
;;           propert type when parsing the CS file)
;; LATER For arrays, see a way to use the structs themselves instead of only bytes
;; DONE Pairs
;; DONE Make keywords work
;; DONE Delete entity
;; DONE For strings in structs, adapt with `str->buf` automatically
;; DONE `pget` `pselect-keys`
;;   DONE Store offset
;;   DONE pget
;;   DONE pselect-keys
;; DONE Ability to callback
;; DONE Systems
;;   DONE Run system callback
;;   DONE Create system from vars (or defsys?)
;;   DONE Use Flecs systems?
;;     - It seems wise to do so as commands are deferred by default
;;     - https://www.flecs.dev/flecs/md_docs_Systems.html
;;   DONE Query DSL using EDN
;;   LATER [Out/in] components in systems
;;   DONE Fix FnPtr and void reflections
;;   DONE Make compilation work
;;     - Seems to be related to https://clojure.atlassian.net/browse/CLJ-1741
;;     - The "fix" was to not compile anything for now
;;   DONE Make hooks work (`ecs_type_hooks_t`)
;;   DONE Make queries using terms instead of expr
;;   DONE Run system using query expr translation
;;   DONE Make wildcard and any work again
;;   DONE Query iteration
;;   LATER Pipelines?
;;   LATER Multithread?
;; LATER Move ECS things to `vybe.ecs`
;; DONE Mutate component from a pointer (cheap)
;; TODO Restart the game dev
;; TODO Bundle `world` when creating entities/components so we don't need to
;;      specify it for every operation
;; TODO Should we create a record for world + entity?
;;   - It would obligate us to register instances ahead of time
;;   - It would also make most of the functions able to remove the `world` arg
;; TODO When the user creates a struct, automatically add it to the default
;;      ECS component
;; TODO Function to explore struct's shape
;; TODO Print components as clojure maps (with some indication that these are components)
;; TODO Filters
;; TODO Rules
;; TODO Mark values as `in` by default for queries?
;; TODO Make a datatype where a seqable wraps pointer + count?
;; TODO Create function in C to get the size of structs (also alignment?)
;; TODO Use `in`/`out` to improve perf
;; TODO Check if we can add padding for all of the structs
;; TODO Instead of doing callbacks (expensive), check if we can do pooling by
;;      creating custom C code
;; TODO Prefab
;; TODO Modules
;; TODO Update in bulk

;; DONE query
;; DONE iter
;; DONE next

;; To compile the lib, use
;; gcc -std=gnu99 -Dflecs_EXPORTS -dynamiclib flecs.c -o flecs.dylib
;; See https://www.flecs.dev/flecs/md_docs_Quickstart.html#autotoc_md28.

;; To inspect all symbols, do
;; nm -gU flecs.dylib

(def lib-name
  (let [lib-name (str (gensym))
        {:keys [err]}
        (sh/with-sh-dir "resources"
          (->> (-> (format "gcc -std=gnu99 -Dflecs_EXPORTS -DFLECS_LOG_3 -DFLECS_%s -dynamiclib vybe.c -o %s.dylib"
                           "NDEBUG" #_ "DEBUG"
                           lib-name)
                   (str/split #" "))
               (apply sh/sh)))]
      (when (seq err)
        (throw (ex-info err {})))
      lib-name))

#_(defonce lib-name
  (if (io/resource "vybe.dylib")
    "vybe"
    (let [lib-name "vybe"
          {:keys [err]}
          (sh/with-sh-dir "resources"
            (->> (-> (format "gcc -std=gnu99 -Dflecs_EXPORTS -DFLECS_%s -dynamiclib vybe.c -o %s.dylib"
                             "NDEBUG" #_ "DEBUG"
                             lib-name)
                     (str/split #" "))
                 (apply sh/sh)))]
      (when (seq err)
        (throw (ex-info err {})))
      lib-name)))

#_(println (io/resource "vybe.dylib"))

;; TODO Create macros out of these methods.
(library/define ^vybe.jnr.omkamra_jnr_interface_vybe vybe (.getPath
                                                           (io/resource
                                                            (format "%s.dylib" lib-name)))
  #_"/Users/paulo.feodrippe/dev/experiments/resources/vybe.dylib"
  (^Pointer ecs_init [])
  (^uint64_t ecs_new_id [^Pointer world])
  (^uint64_t ecs_new_w_id [^Pointer world ^long id])
  (^Boolean ecs_is_alive [^Pointer world ^uint64_t e])
  (^uint64_t ecs_get_alive [^Pointer world ^uint64_t e])
  (^String ecs_entity_str [^Pointer world ^uint64_t e])
  (^String ecs_id_str [^Pointer world ^uint64_t e])
  (^String ecs_get_name [^Pointer world ^uint64_t e])
  (^Boolean ecs_id_is_valid [^Pointer world ^uint64_t e])
  (^Boolean ecs_id_is_tag [^Pointer world ^uint64_t e])
  (^Boolean ecs_id_is_pair [^uint64_t e])
  (^ecs_type_t ecs_get_type [^Pointer world ^uint64_t e])
  (^long ecs_get_typeid [^Pointer world ^uint64_t id])
  (^String ecs_type_str [^Pointer world ^ecs_type_t t])
  (^long-long ecs_lookup [^Pointer ^:in world ^String ^:in name])
  (^long-long ecs_lookup_symbol [^Pointer ^:in world
                                 ^String ^:in name
                                 ^Boolean ^:in lookup_as_path
                                 ^Boolean ^:in recursive])
  (^String ecs_get_path_w_sep [^Pointer ^:in world
                               ^long ^:in parent
                               ^long ^:in child
                               ^String ^:in sep
                               ^String ^:in prefix])
  (^String ecs_lookup_path_w_sep [^Pointer ^:in world
                                  ^long ^:in parent
                                  ^String ^:in path
                                  ^String ^:in sep
                                  ^String ^:in prefix
                                  ^Boolean ^:in recursive])
  (^void ecs_delete [^Pointer world ^uint64_t e])
  (^void ecs_delete_with [^Pointer world ^uint64_t id])
  (^uint64_t ecs_entity_init [^Pointer world ^ecs_entity_desc_t desc])
  (^uint64_t ecs_component_init [^Pointer world ^ecs_component_desc_t desc])

  (^uint64_t ecs_set_id [^Pointer ^:in world,
                         ^uint64_t ^:in entity,
                         ^uint64_t ^:in id,
                         ^size_t ^:in size,
                         ^Pointer ^:in ptr])

  (^int ecs_app_run [^Pointer ^:in world ^ecs_app_desc_t desc])

  (^void ecs_add_id [^Pointer world, ^uint64_t entity, ^uint64_t id])
  (^Pointer ecs_get_id [^Pointer world, ^uint64_t entity, ^uint64_t id])
  (^Boolean ecs_has_id [^Pointer world, ^uint64_t entity, ^uint64_t id])
  (^void ecs_remove_id [^Pointer world, ^uint64_t entity, ^uint64_t id])
  (^void ecs_override_id [^Pointer world, ^uint64_t entity, ^uint64_t id])
  ;; Destroy/end a world.
  (^int ecs_fini [^Pointer ^:in world])
  (^Boolean ecs_is_fini [^Pointer ^:in world])

  ;; Query.
  (^Pointer ecs_query_init [^Pointer world, ^ecs_query_desc_t desc])
  (^void ecs_query_fini [^Pointer query])
  (^ecs_filter_t ecs_query_get_filter [^ecs_query_t query])
  (^String ecs_query_str [^ecs_query_t query])
  (^Boolean ecs_query_orphaned [^ecs_query_t query])
  (^int ecs_query_table_count [^ecs_query_t query])
  (^int ecs_query_entity_count [^ecs_query_t query])
  (^int ecs_query_empty_table_count [^ecs_query_t query])
  (^Pointer ecs_query_get_ctx [^ecs_query_t query])
  (^Pointer ecs_query_get_binding_ctx [^ecs_query_t query])
  ;; See vybe_query_iter.
  #_(^ecs_iter_t ecs_query_iter [^Pointer world, ^Pointer desc])
  (^Boolean ecs_query_next [^Pointer #_ecs_iter_t iter])
  (^Boolean ecs_query_next_table [^Pointer #_ecs_iter_t iter])
  (^Boolean ecs_query_changed [^Pointer #_ecs_query_t query ^Pointer #_ecs_iter_t iter])
  (^Boolean ecs_query_skip [^Pointer #_ecs_iter_t iter])
  (^int ecs_query_populate [^Pointer #_ecs_iter_t iter ^Boolean when_changed])

  ;; Filters
  (^Pointer #_ecs_filter_t ecs_filter_init [^Pointer world, ^ecs_filter_desc_t desc])
  (^void ecs_filter_fini [^Pointer filter])
  (^Boolean ecs_filter_next [^Pointer #_ecs_iter_t iter])

  (^uint64_t ecs_field_id [^Pointer #_ecs_iter_t iter ^int index])
  (^uint64_t ecs_field_src [^Pointer #_ecs_iter_t iter ^int index])
  (^long ecs_field_size [^Pointer #_ecs_iter_t iter ^int index])
  (^Pointer ecs_field_w_size [^Pointer #_ecs_iter_t iter ^uint64_t size ^int index])
  (^Boolean ecs_field_is_set [^Pointer #_ecs_iter_t iter ^int index])
  (^Boolean ecs_field_is_self [^Pointer #_ecs_iter_t iter ^int index])
  (^Boolean ecs_field_is_readonly [^Pointer #_ecs_iter_t iter ^int index])
  (^Boolean ecs_field_is_writeonly [^Pointer #_ecs_iter_t iter ^int index])
  (^String ecs_iter_str [^Pointer #_ecs_iter_t iter])
  (^Pointer ecs_table_get_id [^Pointer world
                              ^Pointer #_ecs_table_t table
                              ^long #_ecs_id id
                              ^int offset])

  ;; World.
  (^Pointer ecs_get_world [^Pointer world])
  (^Boolean ecs_is_deferred [^Pointer world])
  (^Boolean ecs_stage_is_readonly [^Pointer world])

  ;; System.
  (^long #_ecs_entity_t ecs_system_init [^Pointer world ^ecs_system_desc_t desc])
  (^long #_ecs_entity_t ecs_run [^Pointer world ^long system
                                 ^float delta_time ^Pointer param])
  (^ecs_query_t #_ecs_entity_t ecs_system_get_query [^Pointer world ^long entity])
  (^Boolean #_ecs_entity_t ecs_progress [^Pointer world ^float delta_time])

  ;; Event.
  (^long #_ecs_entity_t ecs_observer_init [^Pointer world ^ecs_observer_desc_t desc])
  (^void ecs_emit [^Pointer world ^ecs_event_desc_t desc])

  ;; Enable/disable.
  (^void ecs_enable [^Pointer world ^long entity ^Boolean enable])
  (^void ecs_enable_id [^Pointer world ^long entity ^long id ^Boolean enable])
  (^Boolean ecs_is_enabled_id [^Pointer world ^long entity ^long id])

  ;; Hook.
  (^void ecs_set_hooks_id [^Pointer world ^long id ^ecs_type_hooks_t hooks])

  ;; Scope.
  (^long ecs_set_scope [^Pointer world ^long scope])
  (^long ecs_get_scope [^Pointer world])

  ;; Pipeline.
  (^long ecs_pipeline_init [^Pointer world ^ecs_pipeline_desc_t pipe_desc])
  (^long ecs_set_pipeline [^Pointer world ^long pipeline])
  (^long ecs_get_pipeline [^Pointer world])

  ;; Log.
  (^int ecs_log_set_level [^int level])
  (^Boolean ecs_log_enable_colors [^Boolean level])
  (^int ecs_log_last_error [])

  ;; Relationships.
  (^long ecs_get_parent [^Pointer world ^long entity])
  (^long ecs_get_target [^Pointer world ^long entity ^long rel ^int index])
  (^long ecs_get_target_for_id [^Pointer world ^long entity ^long rel ^long id])

  ;; ----------- CUSTOM --------
  (^void vybe_enable_rest [^Pointer ^:in world])
  (^long vybe_ECS_OVERRIDE [])
  (^long vybe_new_tag [^Pointer ^:in world])

  ;; Query
  ;; Wrapper for ecs_query_iter so we can return a pointer.
  (^Pointer #_ecs_iter_t vybe_query_iter [^Pointer ^:in world, ^Pointer ^:in query])
  (^Pointer #_ecs_iter_t vybe_query_iter_ttt [^Pointer ^:in world,
                                              ^Pointer ^:in desc
                                              ^ecs_system_desc_t ^:in sys])

  ;; Filter.
  (^Pointer #_ecs_iter_t vybe_filter_iter [^Pointer ^:in world, ^Pointer ^:in filter])

  ;; Pair.
  (^long #_ecs_entity_t vybe_pair [^long component_1 ^long component_2])
  (^String vybe_pair_str [^long component_1 ^long component_2])
  (^long #_ecs_entity_t vybe_pair_first [^Pointer world ^long pair])
  (^long #_ecs_entity_t vybe_pair_second [^Pointer world ^long pair])
  (^long #_ecs_entity_t vybe_wildcard [])
  (^long #_ecs_entity_t vybe_any [])

  ;; Test.
  (^void vybe_test [^Pointer eita])
  (^int vybe_main []))

#_ (c-api vybe_main)

(def ^jnr.ffi.Runtime runtime (library/runtime vybe))
(def ^jnr.ffi.ObjectReferenceManager ref-manager (.newObjectReferenceManager runtime))

(defn str->buf
  ^Pointer [^String s]
  (let [bytes (.getBytes (.concat s "\0") "UTF-8")
        buffer (.allocateDirect (.getMemoryManager runtime) (count bytes))]
    (.add ref-manager buffer)
    (.add ref-manager s)
    (.put buffer 0 bytes 0 (count bytes))
    buffer))

(defn k->buf
  ^Pointer [^String s]
  (str->buf (str (symbol s))))

(defn buf->str
  ^String [^Struct$Pointer p-struct]
  (.getString (.get p-struct) 0))

(defprotocol JnrFieldMapping
  (-mapping [this]))

(defn set-instance
  [instance params]
  (let [->jnr (-mapping instance)]
    (->> params
         (mapv (fn [[k v]]
                 (try
                   ((get ->jnr k) v)
                   (catch Exception e
                     (throw (ex-info "Field error when -mapping (does this field exist?)"
                                     {:field k
                                      :value v
                                      :e e})))))))
    instance))

(def -my-ns *ns*)

(def ^:dynamic *class->extra-info*
  "To be used inside `defcomp` only."
  nil)

(def -utf-8
  (java.nio.charset.Charset/forName "UTF-8"))

(def -prepare-class
  (memoize
   (fn [^Class c]
     (let [extra-info (get *class->extra-info* c)
           #_ #__ (def c ecs_filter_desc_t)
           struct-info (get flecs-info (symbol (.getSimpleName c)))
           fields (->> (.getFields c)
                       (map-indexed (fn [idx ^java.lang.reflect.Field field]
                                      (let [type (.getType field)
                                            field-name (keyword (.getName field))]
                                        {:name field-name
                                         :type type
                                         :array-type (or (when (get-in struct-info [:fields idx :array])
                                                           (get-in struct-info [:fields idx :field-type]))
                                                         (when (:size (:field-meta (get-in extra-info [:fields field-name])))
                                                           (:field-type (get-in extra-info [:fields field-name]))))
                                         :original-type (or (:field-type (get-in extra-info [:fields field-name]))
                                                            ({'CString :string
                                                              'int :int
                                                              'uint :int
                                                              'unsigned-long-long :long
                                                              'ulong :long
                                                              'float :float
                                                              'double :double
                                                              'ushort :short
                                                              'sbyte :byte
                                                              'byte :byte}
                                                             (get-in struct-info [:fields idx :field-type])))
                                         :struct (when (= (.getSuperclass type)
                                                          Struct)
                                                   (-prepare-class type)
                                                   true)}))))
           this-sym (with-meta (gensym)
                      {:tag (symbol (.getTypeName c))})]
       #_(def extra-info extra-info)
       #_(def fields fields)
       (binding [*ns* -my-ns]
         (let [form `(extend-type ~c
                       ~'JnrFieldMapping
                       (~'-mapping [~this-sym]
                        ~(->> fields
                              (mapv (fn [{:keys [name original-type type array-type struct]}]
                                      (let [t-fn (case original-type
                                                   :string `str->buf
                                                   :keyword `k->buf
                                                   :char `char
                                                   :short `short
                                                   :unsigned-short `short
                                                   :byte `byte
                                                   :int `int
                                                   :double `double
                                                   :unsigned-char `char
                                                   :unsigned-int `int
                                                   :boolean `(comp unchecked-byte {true 1 false 0})
                                                   :long `long
                                                   :float `float
                                                   nil)]
                                        `[~name
                                          ~(cond
                                             struct
                                             (if (and (some? (:adapter original-type))
                                                      (not= (:adapter original-type) identity))
                                               `(fn [v#]
                                                  (set-instance (.. ~this-sym ~(symbol name)) (~(:adapter original-type) v#)))
                                               `(fn [v#]
                                                  (set-instance (.. ~this-sym ~(symbol name)) v#)))

                                             ;; Structs.
                                             (and array-type
                                                  (get flecs-info array-type)
                                                  (do (-prepare-class (ns-resolve -my-ns array-type))
                                                      true))
                                             (if (and (some? (:adapter original-type))
                                                      (not= (:adapter original-type) identity))
                                               `(fn [v#]
                                                  (let [field# (.. ~this-sym ~(symbol name))]
                                                    (->> v#
                                                         (map-indexed (fn [idx# params#]
                                                                        (set-instance (aget field# idx#) (~(:adapter original-type) params#))))
                                                         vec)))
                                               `(fn [v#]
                                                  (let [field# (.. ~this-sym ~(symbol name))]
                                                    (->> v#
                                                         (map-indexed (fn [idx# params#]
                                                                        (set-instance (aget field# idx#) params#)))
                                                         vec))))

                                             ;; Non-structs.
                                             array-type
                                             (let [field-sym (gensym "field-sym-")
                                                   aget-sym (with-meta (gensym "aget-sym-")
                                                              {:tag (-> (.getComponentType ^Class type)
                                                                        .getTypeName)})]
                                               `(fn [v#]
                                                  (let [~field-sym (.. ~this-sym ~(symbol name))]
                                                    (->> v#
                                                         (map-indexed
                                                          (fn [idx# params#]
                                                            (let [~aget-sym (aget ~field-sym idx#)]
                                                              (.set ~aget-sym (~t-fn params#)))))
                                                         vec))))

                                             (= original-type :string)
                                             `(fn [v#]
                                                (let [buf# (.allocateDirect (.getMemoryManager runtime) 8092)]
                                                  (.add ref-manager buf#)
                                                  (.add ref-manager v#)
                                                  (.putString (do (.set (.. ~this-sym ~(symbol name))
                                                                        buf#)
                                                                  (.get (.. ~this-sym ~(symbol name))))
                                                              0
                                                              v#
                                                              (count v#)
                                                              -utf-8)))

                                             t-fn
                                             `(fn [v#]
                                                (.set (.. ~this-sym ~(symbol name))
                                                      (~t-fn v#)))

                                             ;; If we don't know what's is, it's probably a
                                             ;; Pointer.
                                             :else
                                             `(fn [v#]
                                                (.set (.. ~this-sym ~(symbol name))
                                                      ^Pointer v#)))])))
                              (into {}))))]
           (eval form)
           form))))))

#_ (-> (->> {:terms [{:id 4020}]}
            (make-instance ecs_filter_desc_t))
       .terms)

(def -declared-ctor
  (memoize
   (fn [^Class c]
     (-prepare-class c)
     (.getDeclaredConstructor c (into-array Class [jnr.ffi.Runtime])))))

(def ^:private runtime-array
  (into-array Object [runtime]))

(defn make-instance
  "Creates a new instance of a class using the JNR runtime, optionally with
  params (map with keys that refer to the fields)."
  ([^Class c]
   (.newInstance ^java.lang.reflect.Constructor (-declared-ctor c) runtime-array))
  ([^Class c params]
   (set-instance (make-instance c) params)))
#_ (do (struct/define Child
         ^float eita)
       (struct/define Position
         ^float x
         ^float y
         ^Child child)
       [(make-instance Position)
        (make-instance Position {:y 20
                                 :child {:eita 40}})
        (make-instance Position {:x 10 :y 20})])

#_ (-> (->> {:filter {:expr "ss"}}
            (make-instance ecs_query_desc_t))
       .filter
       .expr
       buf->str)

(defn p*->instance
  [^Pointer pointer ^Class component-type]
  (when pointer
    (let [^jnr.ffi.Struct component-instance (make-instance component-type)]
      (.useMemory component-instance pointer)
      component-instance)))

(set! *warn-on-reflection* false)
(defmacro -struct-info
  "Struct info derived from a component."
  [identifier]
  (let [c (ns-resolve -my-ns (symbol identifier))
        v-sym (gensym)]
    `(let [~v-sym (make-instance ~c)]
       {:struct-size (Struct/size ~v-sym)
        :data
        ~(->> (.getFields ^Class c)
              #_(take 1)
              (mapv #(:name (bean %)))
              (mapv (fn [n]
                      [(keyword n)
                       `{:offset (try
                                   (.offset (~(symbol (str "." n)) ~v-sym))
                                   (catch Exception _#))
                         :size (try
                                 (.size (~(symbol (str "." n)) ~v-sym))
                                 (catch Exception _#))}])))})))
(-struct-info :ecs_term_t)
(set! *warn-on-reflection* true)

(defmacro struct
  "Get struct from built-in keyword (if the built-in is a tag or
  is invalid, it returns `nil`)."
  [c & [{:keys [info]}]]
  (if info
    `(-t ~c)
    `(if (= (namespace ~c) "vy.b")
       (ns-resolve -my-ns (symbol (name ~c)))
       (ns-resolve -my-ns (symbol (name ~c))))))
#_(struct :vy.b/EcsRest)
#_(struct :vy.b/ecs_query_t)
#_(struct :ecs_query_t)
#_(struct :ecs_query_desc_t {:info true})

(defmacro new-t
  "Creates a new instance of a class using the JNR runtime, optionally with
  params (map with keys that refer to the fields)."
  ([identifier]
   `(make-instance ~(struct identifier)))
  ([identifier params]
   `(make-instance ~(struct identifier) ~params)))

(defmacro enum
  "Like `struct`, but for enums values. Input a namespaced keyword.
  Returns an int value.

  If you pass a unnamespaced key, it returns the possible values for the enum."
  [v]
  (if-let [nss (namespace v)]
    (binding [*ns* -my-ns]
      (eval `(.intValue (. ~(symbol nss)
                           ~(symbol (name v))))))
    (binding [*ns* -my-ns]
      (eval `(->> ~(symbol (name v))
                  .getEnumConstants
                  (mapv (comp (partial keyword ~(name v))
                              str)))))))
#_ (enum :ecs_inout_kind_t)
#_ (enum :ecs_inout_kind_t/EcsIn)

(defn constant
  "Like `struct`, but for constant values."
  [k]
  (or (:value (get flecs-info k))
      (throw (ex-info "Constant does not exist!"
                      {:constant k}))))
#_ (constant :ECS_ACCESS_VIOLATION)

(defn ptr-callback
  "Passed function must accept the following arguments

    (Pointer p)"
  [f]
  (let [callback-class VybeInteropFunction$VybePtrCallback
        instance (reify VybeInteropFunction$VybePtrCallback
                   (invoke [_ p]
                     (f p)))
        callback-p (do (.add ref-manager instance)
                       (-> (.getClosureManager runtime)
                           (.getClosurePointer callback-class instance)))]
    callback-p))
#_ (ptr-callback
    (fn [p]
      (def my-p p)))

(defn long-ptr-long-ptr-callback
  "Passed function must accept the following arguments

    (long e1, Pointer p1, long e2, Pointer p2)

  and

    should return an int."
  [f]
  (let [callback-class VybeInteropFunction$VybeRetIntLongPtrLongPtrCallback
        instance (reify VybeInteropFunction$VybeRetIntLongPtrLongPtrCallback
                   (invoke [_ e1 p1 e2 p2]
                     (f e1 p1 e2 p2)))
        callback-p (do (.add ref-manager instance)
                       (-> (.getClosureManager runtime)
                           (.getClosurePointer callback-class instance)))]
    callback-p))

(defmacro c-api
  "Macro that calls the ECS C api using `vybe`.
  Adds `ecs_` as the prefix, accepts dashes for the method names.

  E.g. to call `(.ecs_field_id vybe iter-p field-idx)`, do
  `(c-api :field_id iter-p field-idx)`."
  [method & args]
  (let [method-name (if (or (str/starts-with? (name method) "vybe")
                            (str/starts-with? (str/lower-case (name method)) "ecs"))
                      (symbol (str "." (name method)))
                      (symbol (str ".ecs_" (name method))))]
    `(do #_(println :C_API (quote ~method-name)
                    ~@(->> args
                           (mapv (fn [arg]
                                   (if (= arg 'world)
                                     (quote 'world)
                                     arg)))))
         (~method-name vybe ~@args))))
#_ (macroexpand-1
    '(c-api :field_id iter-p field-idx))
#_ (macroexpand-1
    '(c-api :ecs_field_id iter-p field-idx))
#_ (macroexpand-1
    '(c-api :vybe_pair iter-p field-idx))

(comment

  ;; Setting a pointer.
  ;; https://github.com/jnr/jnr-ffi/blob/be866dd25ff94f47858973622673746496cfc245/src/test/java/jnr/ffi/struct/NumericStructTest.java#L804

  ;; Playground.

  (let [ee (.ecs_new_id vybe ecs)]
    (.ecs_add_id vybe
                 ecs
                 (.ecs_lookup vybe ecs "Olha")
                 ee))

  (.ecs_add_id vybe
               ecs
               (.ecs_lookup vybe ecs "Olha")
               (.ecs_lookup vybe ecs "Olha2"))

  (.ecs_type_str vybe ecs (.ecs_get_type vybe ecs (.ecs_lookup vybe ecs "Olha")))
  (.ecs_has_id vybe ecs (.ecs_lookup vybe ecs "Olha") 525)
  (.ecs_has_id vybe ecs (.ecs_lookup vybe ecs "Olha") (.ecs_lookup vybe ecs "Olha2"))
  (.ecs_remove_id vybe ecs (.ecs_lookup vybe ecs "Olha") (.ecs_lookup vybe ecs "Olha2"))



  (.ecs_lookup vybe ecs "Olha2")

  ())
