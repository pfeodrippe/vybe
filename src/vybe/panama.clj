(ns vybe.panama
  (:require
   [clojure.java.io :as io]
   [potemkin :refer [def-map-type defrecord+ deftype+]]
   [clojure.pprint :as pp]
   [clojure.set :as set]
   [clojure.string :as str]
   #_[clj-java-decompiler.core :refer [decompile disassemble]])
  (:import
   (java.lang.foreign Arena AddressLayout MemoryLayout$PathElement MemoryLayout
                      ValueLayout ValueLayout$OfDouble ValueLayout$OfLong
                      ValueLayout$OfInt ValueLayout$OfBoolean ValueLayout$OfFloat
                      ValueLayout$OfByte ValueLayout$OfShort
                      StructLayout MemorySegment PaddingLayout SequenceLayout
                      UnionLayout)))

(set! *warn-on-reflection* true)

(def windows?
  "If `true` we are on a Windows OS."
  (str/includes? (str/lower-case (System/getProperty "os.name"))
                 "win"))

(defmacro if-windows?
  "Evaluate `then` only if we are on a Windows OS."
  [then else]
  (if windows?
    then
    else))

(defn -copy-resource!
  [resource-filename]
  (if-let [resource-file (io/resource resource-filename)]
    (let [tmp-file (io/file "/tmp/pfeodrippe_vybe_native" resource-filename)]
      (io/make-parents tmp-file)
      (with-open [in (io/input-stream resource-file)] (io/copy in tmp-file)))
    (throw (ex-info "Resource does not exist" {:resource resource-filename}))))

(defn -copy-lib!
  [lib-name]
  (-copy-resource! (System/mapLibraryName lib-name)))

(defn -try-bean
  [s]
  (try
    (bean (Class/forName s))
    (catch Exception _)))

(defonce ^Arena arena-root
  (Arena/ofAuto)
  #_(Arena/ofShared))

(defonce *default-arena
  (atom arena-root))

(def ^:dynamic *dyn-arena*
  "To be used for tests."
  nil)

(defn default-arena
  ^Arena []
  (or *dyn-arena*
      @*default-arena))

(defmacro with-arena
  "Creates or uses an existing arena, available only during the `with-arena` scope.

  (with-arena _ ...)

  (with-arena [arena (Arena/ofAuto] ...)"
  [arena-params & body]
  (let [[arena-sym arena] (if (vector? arena-params)
                            arena-params
                            ;; ofConfined works, but not with the REPL.
                            [arena-params `(Arena/ofShared)]
                            #_[arena-params `(Arena/ofConfined)])]
    `(let [~arena-sym ~arena]
       (try
         (let [orig-arena# @*default-arena]
           (binding [*dyn-arena* ~arena-sym]
             ~@body))
         (finally
           (when (not= ~arena-sym arena-root)
             (.close ~arena-sym)))))))

(defmacro with-arena-root
  "Uses default arena, won't be closed!"
  [& body]
  `(with-arena [_# arena-root]
     ~@body))

(defmacro with-dyn-arena-root
  "Uses default arena via dynamic binding, won't be closed!"
  [& body]
  `(binding [*dyn-arena* arena-root]
     ~@body))

(defn alloc
  "Allocate memory for a layout."
  (^MemorySegment [^MemoryLayout layout]
   (.allocate (default-arena) layout))
  (^MemorySegment [^long byte-size ^long byte-alignment]
   (.allocate (default-arena) byte-size byte-alignment)))

(def null MemorySegment/NULL)

(defn null?
  [p]
  (or (nil? p) (= p MemorySegment/NULL)))

(defn ->string
  "Pointer to string."
  [^MemorySegment p]
  (when (not= p MemorySegment/NULL)
    (.getString p 0)))

;; -- Types
(definterface IVybeMemorySegment
  (^java.lang.foreign.MemorySegment mem_segment []))

(definterface IVybeWithPMap
  (^vybe.panama.VybePMap pmap []))

(definterface IVybeComponent
  (^java.lang.foreign.MemoryLayout layout [])
  (^clojure.lang.IPersistentMap fields [])
  (^clojure.lang.IFn init [])
  (^clojure.lang.IFn to_with_pmap [])
  (^clojure.lang.IFn opts []))

(declare -vybe-component-rep)
(declare -instance)

(deftype+ VybeComponent [-layout -fields -init -to-with-pmap -opts]
  IVybeComponent
  (layout [_] -layout)
  (fields [_] -fields)
  (init [_] -init)
  (to_with_pmap [_] -to-with-pmap)
  (opts [_] -opts)

  clojure.lang.IPersistentCollection
  (equiv [this x]
         (and (instance? VybeComponent x)
              (= (.layout this)
                 (.layout ^VybeComponent x))))

  clojure.lang.IFn
  (invoke [this]
          (-instance this {}))
  (invoke [this params]
          (-instance this params))
  (applyTo [this arglist]
           (apply -instance this arglist))

  Object
  (toString [this]
            (str (-vybe-component-rep this))))

(defn- -vybe-component-rep
  [^IVybeComponent this]
  #_(symbol (.get (.name (.layout this))))
  {(symbol (.get (.name (.layout this))))
   (into (.fields this)
         (-> (.fields this)
             (update-vals #(if-not (:constructor %)
                             (:type %)
                             (select-keys % [:type :constructor])))))})

(defmethod print-method VybeComponent
  [^VybeComponent o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybeComponent
  [^VybeComponent o]
  (pp/simple-dispatch (-vybe-component-rep o)))

(definterface IVybeWithComponent
  (^vybe.panama.VybeComponent component []))

#_(reset! pt/type-bodies {})

;; -- Pointer Helpers

(defn- -pget
  "Get the value of a pointer."
  [mem-segment ^IVybeComponent component field]
  (let [fields (.fields component)
        f (:getter (get fields field))]
    (when f
      (f mem-segment))))
#_ (let [Position (make-component 'Position [[:x :double] [:y :double]])]
     (-> (-instance Position {:x 104.5})
         .mem_segment
         (-pget Position :x)))

(defn try-string
  [s]
  (if (string? s)
    (.allocateFrom (default-arena) s)
    s))

(defn- -run-p-params
  [^MemorySegment mem-segment params fields init c]
  (cond
    init
    (init mem-segment params c)

    (vector? params)
    (mapv (fn [[_field {:keys [builder]}] value]
            (builder mem-segment value))
          fields
          params)

    :else
    (run! (fn [[field value]]
            (let [f (:builder (get fields field))]
              (try
                (f mem-segment value)
                (catch Exception e
                  (throw (ex-info "Error when setting params for a component"
                                  {:field field
                                   :value value
                                   :value-type (type value)
                                   :fields fields
                                   :mem-segment mem-segment}
                                  e))))))
          params)))

(declare -vybe-p-map-rep)
(declare pmap-metadata)

(def-map-type VybePMap [-mem-segment -component mta]
  (get [this k default-value]
       (or (-pget (.mem_segment this) (.component this) k)
           default-value))
  (assoc [this k v]
         (if (:vp/const mta)
           (throw (ex-info "VybePMap is set as a constant, you aren't allowed to write to it"
                           {:pointer this}))
           (-run-p-params (.mem_segment this)
                          {k v}
                          (.fields (.component this))
                          (.init (.component this))
                          (.component this)))
         this)
  (dissoc [_ k]
          (throw (ex-info "VybePMap dissoc not applicable"
                          {:k k})))
  (keys [this]
        (keys (.fields (.component this))))
  (keySet [this] (set (potemkin.collections/keys* this)))
  (meta [this] (merge mta (pmap-metadata this)))
  (with-meta [this mta]
    (VybePMap. (.mem_segment this) (.component this) mta))

  clojure.lang.IMapEntry
  (key [this] (.component this))
  (val [this] this)

  java.util.Map$Entry
  (getValue [this] (.val this))
  (getKey [this] (.key this))

  Object
  (toString [this] (str (-vybe-p-map-rep this)))

  IVybeMemorySegment
  (mem_segment [_] -mem-segment)

  IVybeWithComponent
  (component [_] -component))

(defmulti pmap-metadata
  "Used for when you want to print something specific for a component."
  (fn [^VybePMap v]
    (.component v)))

(defmethod pmap-metadata :default
  [_v]
  nil)

(defn- -vybe-p-map-rep
  [^VybePMap this]
  {(symbol (.get (.name (.layout (.component this)))))
   ;; Sort map by its keys.
   (let [k->idx (zipmap (keys this) (range))
         metadata (pmap-metadata this)]
     (cond-> (into (sorted-map-by (fn [k1 k2]
                                    (compare (k->idx k1) (k->idx k2))))
                   this)
       metadata (assoc :vp/metadata metadata)))})

(defmethod print-method VybePMap
  [^VybePMap o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybePMap
  [^VybePMap o]
  (pp/simple-dispatch (-vybe-p-map-rep o)))

(defn pmap?
  "Check if `v`is a VybePMap."
  [v]
  (instance? VybePMap v))

(defn p->map
  "Convert a pointer into a mutable map (backed by the pointer)."
  (^VybePMap [mem-segment component]
   (VybePMap. mem-segment component nil))
  (^VybePMap [mem-segment component {:keys [as-map]}]
   (cond->> (p->map mem-segment component)
     as-map (into {}))))

(defn try-p->map
  [v component]
  (if component
    (p->map v component)
    v))

(defn ->with-pmap
  [^VybePMap p-map]
  (if-let [to-with-pmap (-> p-map .component .to_with_pmap)]
    (to-with-pmap p-map)
    p-map))

(defn component
  "Get component, if applicable, otherwise returns `nil`."
  [maybe-has-component]
  (when (instance? IVybeWithComponent maybe-has-component)
    (let [^IVybeWithComponent p maybe-has-component]
      (.component p))))

(defn layout-equal?
  "Check if components are the same by comparing the layouts (while ignoring the
  layout names)."
  [c1 c2]
  (and (instance? VybeComponent c1)
       (instance? VybeComponent c2)
       (= (.withoutName (.layout ^VybeComponent c1))
          (.withoutName (.layout ^VybeComponent c2)))))

(declare mem)

(defn- -arr-builder
  [c field-offset el-byte-size]
  (fn arr-vybe-component-builder
    [^MemorySegment mem-segment coll]
    (if (or (instance? IVybeMemorySegment coll)
            #_(instance? MemorySegment coll))
      (let [v (mem coll)]
        (MemorySegment/copy ^MemorySegment v
                            0
                            mem-segment
                            field-offset
                            (* el-byte-size (count coll))))
      (->> coll
           (map-indexed
            (fn [idx v]
              (if (instance? MemorySegment v)
                (MemorySegment/copy ^MemorySegment v
                                    0
                                    mem-segment
                                    (+ field-offset (* el-byte-size idx))
                                    el-byte-size)

                (MemorySegment/copy (.mem_segment (if (instance? IVybeMemorySegment v)
                                                    ^IVybeMemorySegment v
                                                    ^IVybeMemorySegment (-instance c v)))
                                    0
                                    mem-segment
                                    (+ field-offset (* el-byte-size idx))
                                    el-byte-size))))
           vec))))

(defn- -value-layout->type
  [^ValueLayout l]
  (if (= (.carrier l) MemorySegment)
    :pointer
    (keyword (str (.carrier l)))))

(definterface IVybePSeq
  (^java.lang.foreign.MemoryLayout layout []))

(declare -vybe-pseq-rep)

(declare p->value)

(deftype+ VybePSeq [-mem-segment -component -layout size]
  clojure.lang.Seqable
  (seq [this]
    (->> (-> (.mem_segment this)
             (.elements (.layout this))
             .iterator
             iterator-seq)
         (map #(p->value % (or (.component this) (.layout this))))))

  clojure.lang.Counted
  (count [_]
    size)

  clojure.lang.Indexed
  (nth [this i]
    (let [byte-size (.byteSize (.layout this))]
      (p->value (.asSlice (.mem_segment this) (* byte-size i) byte-size)
                (or (.component this) (.layout this)))))
  (nth [this i not-found]
    (or (nth this i) not-found))

  IVybeMemorySegment
  (mem_segment [_] -mem-segment)

  IVybeWithComponent
  (component [_] -component)

  IVybePSeq
  (layout [_] -layout)

  Object
  (toString [this] (str (-vybe-pseq-rep this))))

(defn- -vybe-pseq-rep
  [^VybePSeq this]
  (list (symbol "#VybePSeq") (vec (seq this))))

(defmethod print-method VybePSeq
  [^VybePSeq o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybePSeq
  [^VybePSeq o]
  (pp/simple-dispatch (-vybe-pseq-rep o)))

(defn mem
  "Get memory segment from some value."
  ^MemorySegment [v]
  (cond
    (instance? IVybeMemorySegment v)
    (.mem_segment ^IVybeMemorySegment v)

    (string? v)
    (.allocateFrom (default-arena) v)

    :else
    v))

(defn address
  "Get address from a value."
  [v]
  (.address (mem v)))

(declare -vybe-popaque-rep)

(deftype+ VybePOpaque [-mem-segment identifier -component]
  IVybeMemorySegment
  (mem_segment [_] -mem-segment)

  IVybeWithComponent
  (component [_]
    -component)

  IVybeWithPMap
  (pmap [this]
    ((.component this) (.mem_segment this)))

  Object
  (toString [this] (str (-vybe-popaque-rep this))))

(defn- -vybe-popaque-rep
  [^VybePOpaque this]
  (symbol (str "#VybePOpaque[" (.identifier this) " " (format "\"0x%x\"" (.address (mem this))) "]")))

(defmethod print-method VybePOpaque
  [^VybePOpaque o ^java.io.Writer w]
  (.write w (str o)))

(defmethod pp/simple-dispatch VybePOpaque
  [^VybePOpaque o]
  (pp/simple-dispatch (-vybe-popaque-rep o)))

(defn clone
  "Clone a VybePMap (component instance)."
  [^IVybeWithComponent m]
  (let [layout (.layout (.component m))
        new-mem-segment (.allocate (default-arena) layout)]
    (MemorySegment/copy (mem m)         0
                        new-mem-segment 0
                        (.byteSize layout))
    (p->map new-mem-segment (.component m))))

(defn- -primitive-builders
  [field-type ^long field-offset field-layout]
  (case (if (instance? MemoryLayout field-type)
          (-value-layout->type field-type)
          field-type)
    :double
    {:builder (fn double-builder
                [^MemorySegment mem-segment ^double value]
                (.set mem-segment
                      ^ValueLayout$OfDouble field-layout
                      field-offset
                      value))
     :getter (fn double-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfDouble field-layout
                     field-offset))}

    :float
    {:builder (fn float-builder
                [^MemorySegment mem-segment ^double value]
                (.set mem-segment
                      ^ValueLayout$OfFloat field-layout
                      field-offset
                      value))
     :getter (fn float-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfFloat field-layout
                     field-offset))}

    :int
    {:builder (fn int-builder
                [^MemorySegment mem-segment ^long value]
                (.set mem-segment
                      ^ValueLayout$OfInt field-layout
                      field-offset
                      value))
     :getter (fn int-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfInt field-layout
                     field-offset))}

    :short
    {:builder (fn short-builder
                [^MemorySegment mem-segment ^long value]
                (.set mem-segment
                      ^ValueLayout$OfShort field-layout
                      field-offset
                      (short value)))
     :getter (fn short-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfShort field-layout
                     field-offset))}

    :byte
    {:builder (fn byte-builder
                [^MemorySegment mem-segment ^long value]
                (.set mem-segment
                      ^ValueLayout$OfByte field-layout
                      field-offset
                      (unchecked-byte value)))
     :getter (fn byte-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfByte field-layout
                     field-offset))}

    :long
    {:builder (fn long-builder
                [^MemorySegment mem-segment ^long value]
                (.set mem-segment
                      ^ValueLayout$OfLong field-layout
                      field-offset
                      value))
     :getter (fn long-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfLong field-layout
                     field-offset))}

    :boolean
    {:builder (fn boolean-builder
                [^MemorySegment mem-segment ^Boolean value]
                (.set mem-segment
                      ^ValueLayout$OfBoolean field-layout
                      field-offset
                      value))
     :getter (fn boolean-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^ValueLayout$OfBoolean field-layout
                     field-offset))}

    :pointer
    {:builder (fn pointer-builder
                [^MemorySegment mem-segment value]
                (let [^MemorySegment value (mem value)]
                  (.set mem-segment
                        ^AddressLayout field-layout
                        field-offset
                        value)))
     :getter (fn pointer-getter
               [^MemorySegment mem-segment]
               (.get mem-segment
                     ^AddressLayout field-layout
                     field-offset))}

    :string
    {:builder (fn string-builder
                [^MemorySegment mem-segment ^String value]
                (.set mem-segment
                      ^AddressLayout field-layout
                      field-offset
                      (.allocateFrom (default-arena) value)))
     :getter (fn string-getter
               [^MemorySegment mem-segment]
               (-> (.get mem-segment
                         ^AddressLayout field-layout
                         field-offset)
                   ->string))}

    (throw (ex-info "No matching clause for field-type"
                    {:field-type field-type}))))

(def string-layout
  (-> ValueLayout/ADDRESS
      (.withTargetLayout (MemoryLayout/sequenceLayout
                          Long/MAX_VALUE
                          ValueLayout/JAVA_BYTE))
      (.withName (str `string-layout))))

(defn type->layout
  ^MemoryLayout [field-type]
  (cond
    (instance? IVybeComponent field-type)
    (.layout ^IVybeComponent field-type)

    (instance? MemoryLayout field-type)
    field-type

    :else
    (case field-type
      :pointer ValueLayout/ADDRESS
      :double ValueLayout/JAVA_DOUBLE
      :long ValueLayout/JAVA_LONG
      :int ValueLayout/JAVA_INT
      :boolean ValueLayout/JAVA_BOOLEAN
      :char ValueLayout/JAVA_CHAR
      :float ValueLayout/JAVA_FLOAT
      :short ValueLayout/JAVA_SHORT
      :byte ValueLayout/JAVA_BYTE
      :string string-layout)))

(defn p->value
  "Convert a pointer into a value."
  [mem-segment component]
  (if (instance? VybeComponent component)
    (VybePMap. mem-segment component nil)
    (let [getter (:getter (-primitive-builders component 0 (type->layout component)))]
      (getter mem-segment))))

(defn arr
  "Create array of a component type with a specific size."
  (^VybePSeq [c-vec]
   (let [c (.component ^IVybeWithComponent (first c-vec))
         c-arr (arr (count c-vec) c)]
     (vec (map-indexed (fn [idx ^IVybeMemorySegment v]
                         (.copyFrom (.mem_segment ^IVybeMemorySegment (nth c-arr idx))
                                    (.reinterpret (.mem_segment v)
                                                  (.byteSize (.layout c))
                                                  (default-arena)
                                                  nil)))
                       c-vec))
     c-arr))
  (^VybePSeq [primitive-vector-or-size c-or-layout]
   (if (sequential? primitive-vector-or-size)
     ;; For primitives.
     (let [c-vec primitive-vector-or-size
           size (count primitive-vector-or-size)
           ^ValueLayout l (type->layout c-or-layout)
           c-arr (arr size c-or-layout)
           ^Object obj (condp = l
                         (type->layout :float)
                         (float-array (mapv float c-vec))

                         (type->layout :int)
                         (int-array (mapv int c-vec))

                         (type->layout :long)
                         (long-array (mapv long c-vec)))]
       (MemorySegment/copy obj 0 (.mem_segment c-arr) l 0 size)
       c-arr)
     (let [size primitive-vector-or-size]
       (if (instance? VybeComponent c-or-layout)
         (let [^IVybeComponent c c-or-layout]
           (arr (.allocate (default-arena) (MemoryLayout/sequenceLayout size (.layout c))) size c))
         (let [^MemoryLayout l (type->layout c-or-layout)]
           (arr (.allocate (default-arena) (MemoryLayout/sequenceLayout size l)) size c-or-layout))))))
  (^VybePSeq [^MemorySegment mem-segment size c-or-layout]
   (if (instance? IVybeComponent c-or-layout)
     (let [^IVybeComponent c c-or-layout
           l (.layout c)]

       (VybePSeq. (-> mem-segment
                      (.reinterpret (* (.byteSize l) size) (default-arena) nil))
                  c
                  l
                  size))
     (let [[c-or-layout wrapped-c] (if (vector? c-or-layout)
                                     c-or-layout
                                     [c-or-layout nil])
           ^MemoryLayout l (type->layout c-or-layout)]
       (if wrapped-c
         (let [pointers (VybePSeq. (-> mem-segment
                                       (.reinterpret (* (.byteSize l) size) (default-arena) nil))
                                   nil
                                   l
                                   size)]
           (mapv (fn [^MemorySegment pointer]
                   (p->value (.reinterpret pointer
                                           (.byteSize (type->layout wrapped-c))
                                           (default-arena)
                                           nil)
                             wrapped-c))
                 pointers))
         (VybePSeq. (-> mem-segment
                        (.reinterpret (* (.byteSize l) size) (default-arena) nil))
                    nil
                    l
                    size))))))
#_ (def ab (arr 3 vybe.raylib/VyModelMeta))
#_ (arr [(vybe.raylib/VyModelMeta)])
#_ (arr [10 100] :int)
#_ (assoc ab 2 4)                       ;; this is not working yet
#_ (.copyFrom (.mem_segment (nth ab 2)) (.reinterpret (.mem_segment (vybe.raylib/VyModelMeta {:drawingDisabled 1}))
                                                      (.byteSize (.layout vybe.raylib/VyModelMeta))))
#_ (assoc (nth ab 2) :drawingDisabled 1)
#_ (nth (arr (.mem_segment ab) 3 (.component ab)) 2)
#_ (nth (arr 3 :int) 2)

(defn arr?
  "Check if value is a IVybePSeq."
  [v]
  (instance? IVybePSeq v))

(defn component?
  "Check if value is a IVybeComponent"
  [v]
  (instance? IVybeComponent v))

(defn -instance
  "Returns a hash map (VybePMap) representing a pointer."
  (^VybePMap [^IVybeComponent c]
   (-instance c {}))
  (^VybePMap [^IVybeComponent c params]
   (let [mem-segment (alloc (.layout c))
         fields (.fields c)]
     (-run-p-params mem-segment params fields (.init c) c)
     (p->map mem-segment c))))

(defn- -sort-by-idx
  [m]
  (into (sorted-map-by (fn [key1 key2]
                         (compare [(get-in m [key1 :idx]) key1]
                                  [(get-in m [key2 :idx]) key2])))
        m))

(declare layout->c)

(defn- -adapt-layout
  [^StructLayout layout field->meta path-acc]
  (fn -adapt-layout--internal [l]
    (cond
      (instance? ValueLayout l)
      (let [^ValueLayout l l
            field (if (.isPresent (.name l))
                    (keyword (.get (.name l)))
                    nil)]
        (if (= (.carrier l) MemorySegment)
          (or (:type (get field->meta field))
              :pointer)
          (keyword (str (.carrier l)))))

      (instance? StructLayout l)
      (let [^StructLayout l l]
        (layout->c l field->meta (conj path-acc (.name layout))))

      (instance? SequenceLayout l)
      (let [^SequenceLayout l l]
        [:vec ((-adapt-layout layout field->meta
                              (conj path-acc (.elementLayout l)))
               (.elementLayout l))])

      :else
      (throw (ex-info "Layout not supported"
                      {:layout l
                       :path (conj path-acc (.name layout) #_(.name l))})))))
#_ ((-adapt-layout nil {} []) ValueLayout/JAVA_INT)

(defn- -adapt-struct-layout
  [^StructLayout layout field->meta path-acc]
  (fn -adapt-struct-layout--internal [^MemoryLayout l]
    (let [field (keyword (.get (.name l)))
          field-type (or (:type (get field->meta field))
                         ((-adapt-layout layout field->meta path-acc) l))
          field-offset (-> layout
                           (.byteOffset
                            (into-array
                             MemoryLayout$PathElement
                             [(MemoryLayout$PathElement/groupElement (name field))])))

          field-layout (-> layout
                           (.select
                            (into-array
                             MemoryLayout$PathElement
                             [(MemoryLayout$PathElement/groupElement (name field))])))]
      [field (merge
              {:offset field-offset
               :layout field-layout
               :type field-type}
              (cond
                (instance? IVybeComponent field-type)
                {:builder (fn vybe-component-builder
                            [^MemorySegment mem-segment value]
                            (MemorySegment/copy (cond
                                                  (instance? MemorySegment value)
                                                  (let [^MemorySegment value value]
                                                    value)

                                                  (instance? IVybeComponent value)
                                                  (let [^IVybeMemorySegment value value]
                                                    (.mem_segment value))

                                                  :else
                                                  (.mem_segment (-instance field-type value)))
                                                0
                                                mem-segment
                                                field-offset
                                                (.byteSize field-layout)))
                 :getter (fn vybe-component-getter
                           [^MemorySegment mem-segment]
                           (p->map (.asSlice mem-segment
                                             field-offset
                                             (.byteSize field-layout))
                                   field-type))}

                (and (vector? field-type) (= (first field-type) :vec))
                (let [c (second field-type)
                      el-layout (type->layout c)
                      el-byte-size (.byteSize el-layout)]
                  {:builder (-arr-builder c field-offset el-byte-size)
                   :getter (fn arr-vybe-component-getter
                             [^MemorySegment mem-segment]
                             (try
                               (->> (-> (.asSlice mem-segment field-offset (.byteSize field-layout))
                                        (.elements el-layout)
                                        .iterator
                                        iterator-seq)
                                    (mapv #(p->value % c)))

                               (catch Exception e
                                 (throw (ex-info "Error when getting array"
                                                 {:field-layout field-layout
                                                  :element-c c
                                                  :field field}
                                                 e)))))})

                :else
                (-primitive-builders field-type field-offset field-layout)))])))

(defn layout->c
  "Convert a layout to a component."
  ([^StructLayout layout]
   (layout->c layout {}))
  ([^StructLayout layout field->meta]
   (layout->c layout field->meta []))
  ([^StructLayout layout
    {:vp/keys [constructor to-with-pmap _byte-alignment] :as field->meta} path-acc]
   #_(def layout layout)
   #_(def field->meta field->meta)
   #_(def path-acc path-acc)
   (let [fields (->> (.memberLayouts layout)
                     (remove #(instance? PaddingLayout %))
                     ;; TODO Allow UnionLayout.
                     (remove #(instance? UnionLayout %))
                     (mapv (-adapt-struct-layout layout field->meta path-acc))
                     (map-indexed (fn [idx [field {:keys [builder] :as field-params}]]
                                    [field (let [{:keys [constructor]} (get field->meta field)]
                                             (cond-> (-> field-params
                                                         (assoc :idx idx))
                                               constructor
                                               (assoc :builder (fn builder-constructor
                                                                 [^MemorySegment mem-segment value]
                                                                 (builder mem-segment (constructor value)))
                                                      :constructor constructor)))]))
                     vec
                     (into {})
                     (-sort-by-idx))]
     (VybeComponent.
      layout
      fields
      (cond
        constructor
        (fn [mem-segment value c]
          (-run-p-params mem-segment (constructor value) fields nil c))

        ;; If we only have one param, we can pass a value
        ;; instead of a map to the component invocation.
        (= (count fields) 1)
        (let [f (:builder (val (first fields)))]
          (fn [mem-segment value c]
            (try
              (if (map? value)
                (-run-p-params mem-segment value fields nil c)
                (f mem-segment value))
              (catch Exception ex
                (throw (ex-info "Error trying to instance a component with only 1 field"
                                {:fields fields
                                 :layout layout}
                                ex)))))))
      to-with-pmap
      field->meta
      #_(zero? (count fields))))))
#_ (-> ((make-component [[:a :double]]) 10)
       (update :a inc))
#_ ((make-component
     'DD
     {:constructor (fn [v] (update v :a inc))}
     [[:a :double] [:b :double]])
    {:a 40})

(defmacro jx-im
  "Creates a jextract instance, represented by a hash map."
  [m klass]
  (with-meta `(-instance (layout->c (~(symbol (str klass) "layout"))) ~m)

    {:tag `VybePMap}))
#_ (let [params {:id 31
                 :name "dd"
                 :symbol "dff"
                 :use_low_id true}]
     (-> params
         (jx-im org.vybe.flecs.ecs_entity_desc_t)))

(defmacro jx-p->map
  "Mem segment to a hash map."
  [mem-segment klass]
  `(p->map ~mem-segment (layout->c (~(symbol (str klass) "layout")))))
#_ (-> {:id 31
        :name "dd"
        :symbol "dff"
        :use_low_id true}
       (jx-i org.vybe.flecs.ecs_entity_desc_t)
       (jx-p->map org.vybe.flecs.ecs_entity_desc_t))

(defmacro jx-i
  "Creates a jextract instance.

  Returns a MemorySegment."
  [m klass]
  `(-> (jx-im ~m ~klass)
       .mem_segment))
#_(let [params {:id 0
                :name "dd"
                :symbol "dff"
                :use_low_id true}]
    (-> params
        (jx-i org.vybe.flecs.ecs_entity_desc_t)
        (org.vybe.flecs.ecs_entity_desc_t/symbol)
        ->string))

(defonce ^:private *components-cache (atom {}))
(defonce ^:private *id-counter (atom 0))

(defn cache-comp
  ([component]
   (cache-comp component component))
  ([identifier component]
   (let [[id _future-id] (if-let [id (get @*components-cache identifier)]
                           [id nil]
                           (swap-vals! *id-counter inc))]
     (swap! *components-cache assoc
            identifier id
            id component
            component id)
     id)))

(defonce ^:private *layouts-cache (atom {}))

(defn make-component
  "Builds a component type.

  If the arity-1 version is used, an anonymous component is created.

  The returned component can be called as a function."
  ([schema]
   (make-component (gensym "COMP_") schema))
  ([identifier schema]
   (make-component identifier {} schema))
  ([identifier opts schema]
   (let [opts (set/rename-keys opts {:constructor :vp/constructor
                                     :doc :vp/doc
                                     :to-with-pmap :vp/to-with-pmap
                                     :byte-alignment :vp/byte-alignment})]
     (or (get @*layouts-cache [identifier [schema opts]])
         (cond
           (instance? MemoryLayout schema)
           (let [c (layout->c (.withName ^MemoryLayout schema (str identifier)) opts)]
             (cache-comp identifier c)
             (swap! *layouts-cache assoc [identifier [schema opts]] c)
             c)

           (instance? IVybeComponent schema)
           (make-component identifier opts (.layout ^IVybeComponent schema))

           :else
           (let [fields-vec schema
                 identifier (symbol identifier)
                 adapt-field-params (fn [field-params]
                                      (case (count field-params)
                                        3 field-params
                                        2 [(first field-params) nil (last field-params)]))
                 java-layouts (->> fields-vec
                                   (mapv (fn [field-params]
                                           (let [[field _metadata field-type] (adapt-field-params field-params)
                                                 ^MemoryLayout l (type->layout field-type)]
                                             (.withName l (name field))))))
                 field->meta (->> fields-vec
                                  (mapv (fn [field-params]
                                          (let [[field metadata field-type] (adapt-field-params field-params)]
                                            [field (merge metadata {:type field-type})])))
                                  (into {}))
                 layout (-> (into-array MemoryLayout java-layouts)
                            MemoryLayout/structLayout
                            (.withName (str identifier)))
                 layout (if-let [alignment (:vp/byte-alignment opts)]
                          (.withByteAlignment ^MemoryLayout layout alignment)
                          layout)
                 component (layout->c layout (merge opts field->meta))]
             (cache-comp identifier component)
             (swap! *layouts-cache assoc [identifier [schema opts]] component)
             component))))))
#_ ((make-component
     `A
     [[:x :double]
      [:y :double]
      [:a :string]])
    [1 9 "dd"])
#_ (let [B (make-component
            `B
            [[:x :double]
             [:y :double]
             [:a :string]
             [:cc {:constructor identity} :string]])]
     [(make-component
       `C
       B)
      (make-component
       `C
       (.layout B))])

(defn make-components
  "Builds multiple components from a map.

  Each map pair from the input should be an identifier (symbol, string
  or keyword that will be converted to a symbol) and the schema,
  e.g. [[:x :double]] which would tell us that this component has a field x of
  type double.

  Returns a map (identifier -> component type)."
  [m]
  (->> m
       (mapv (fn [[k v]]
               [(symbol k) (make-component k v)]))
       (into {})))
#_ (make-components
    '{Attack [[:value :double]]
      Defense [[:value :double]]})

(defn comp-cache
  "Gets the component from an id or an id from a component.
  The id is managed by us, the id is just an incremental counter.

  Usually used when you need to refer to a component in some native code."
  [v]
  (get @*components-cache v))

(defmacro defcomp
  "Creates a component, e.g.

  A doc string is optional and can be used after the symbol.
  `opts` is also optional and it's a map.

  E.g.

  (defcomp Position
    [[:x :double]
     [:y :double]])

  It uses `make-component` under the hood, see its doc."
  {:arglists '([sym doc-string? opts? schema])}
  #_([sym]
     `(defcomp ~sym {} []))
  #_([sym schema]
     `(defcomp ~sym {} ~schema))
  [sym & args]
  (let [[maybe-doc maybe-opts] args
        {:keys [doc opts]} {:doc (cond
                                   (string? maybe-doc) maybe-doc
                                   (string? maybe-opts) maybe-opts)
                            :opts (cond
                                    (map? maybe-doc) maybe-doc
                                    (map? maybe-opts) maybe-opts)}
        schema (last args)
        opts (cond-> (or opts {})
               doc (assoc :doc doc))]
    `(def ~(with-meta sym (merge {:tag `VybeComponent}
                                 opts))
       (make-component
        (quote ~(symbol (str *ns*) (str sym)))
        ~opts
        ~schema))))
#_ (do (defcomp Position
         [[:x :double]
          [:y :double]])
       (Position {:y -13}))
#_(do (defcomp Position
        ;; Uses a constructor that switches x with y.
        {:constructor (fn [{:keys [x y] :as v
                            :or {x 0 y 0}}]
                        (assoc v :y x :x y))}
        [[:x :double]
         [:y :double]])
      (Position {:y -13}))
#_ (do (defcomp VyModel (org.vybe.raylib.VyModel/layout))
       (.fields VyModel)
       (VyModel))

(defn byte*
  ^MemorySegment [v]
  (.allocateFrom (default-arena) ValueLayout/JAVA_BYTE (byte v)))

(defn float*
  ^MemorySegment [v]
  (.allocateFrom (default-arena) ValueLayout/JAVA_FLOAT (float v)))

(defn int*
  ^MemorySegment [v]
  (.allocateFrom (default-arena) ValueLayout/JAVA_INT (int v)))

(defn long*
  ^MemorySegment [v]
  (.allocateFrom (default-arena) ValueLayout/JAVA_LONG (long v)))

(defmacro with-apply
  "Helper to create reified functions.

  First 2 arguments are `this` and the memory segment, you will not usually use
  these.

  E.g.

    (with-apply JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers
      [_ _]
      2)"
  [klass & fn-body]
  (let [[params & fn-tail] fn-body]
    `(do (println :SETTING ~(symbol (str klass "$Function")))
         (-> (reify ~(symbol (str klass "$Function"))
               (~'apply ~params
                (try
                  ~@fn-tail
                  (catch Exception e#
                    (println e#)))))
             (~(symbol (str klass "/allocate"))
              (vp/default-arena))))))
#_ (macroexpand-1
    '(with-apply JPC_BroadPhaseLayerInterfaceVTable$GetNumBroadPhaseLayers
                     [_ _]
                     2))

(defn -opaque-to-with-pmap
  [identifier]
  (fn [^VybePMap p-map]
    (VybePOpaque. (:opaque p-map) identifier (.component p-map))))

(defmacro defopaques
  "Create opaque types."
  [& syms]
  `(do
     ~@(->> syms
            (mapv (fn [sym]
                    `(let [identifier# (quote ~(symbol (str *ns*) (str `~sym)))
                           c# (make-component identifier#
                                              {:to-with-pmap (-opaque-to-with-pmap identifier#)}
                                              [[:opaque :pointer]])]
                       (def ~sym
                         (reify clojure.lang.IFn
                           (invoke [_# mem-segment#]
                             (VybePOpaque. mem-segment# identifier# c#))

                           IVybeWithComponent
                           (component [_#]
                             c#)))
                       #_(cache-comp identifier# ~sym)
                       ~sym))))))
