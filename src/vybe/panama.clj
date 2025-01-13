(ns vybe.panama
  (:require
   [clojure.core.protocols :as core-p]
   [clojure.java.io :as io]
   [clojure.pprint :as pp]
   [clojure.set :as set]
   [clojure.string :as str]
   [potemkin :refer [def-map-type defrecord+ deftype+]])
  (:import
   (java.lang.foreign Arena AddressLayout MemoryLayout$PathElement MemoryLayout
                      ValueLayout ValueLayout$OfDouble ValueLayout$OfLong
                      ValueLayout$OfInt ValueLayout$OfBoolean ValueLayout$OfFloat
                      ValueLayout$OfByte ValueLayout$OfShort
                      StructLayout MemorySegment PaddingLayout SequenceLayout
                      UnionLayout SegmentAllocator
                      FunctionDescriptor Linker SymbolLookup)
   (java.lang.invoke MethodHandles MethodHandle)))

(set! *warn-on-reflection* true)

(def ^:dynamic *debug*
  "To be used for tests."
  nil)

(defn -os?
  [included-str]
  (str/includes? (str/lower-case (System/getProperty "os.name")) included-str))

(def windows?
  "If `true` we are on a Windows OS."
  (-os? "win"))

(def linux?
  "If `true` we are on a Linux OS."
  (or (-os? "nix") (-os? "nux") (-os? "aix")))

(def mac?
  "If `true` we are on a Mac OS."
  (-os? "mac"))

(defmacro if-windows?
  "Evaluate `then` only if we are on a Windows OS."
  [then else]
  (if windows?
    then
    else))

(defn -copy-resource!
  [resource-filename target-filename]
  #_(def resource-filename (System/mapLibraryName "raylib"))
  (if-let [resource-file (io/resource resource-filename)]
    (let [tmp-file (io/file (or (System/getenv "VYBE_RESOURCE_FOLDER")
                                #_"/tmp/pfeodrippe_vybe_native"
                                "vybe_native")
                            target-filename)]
      (when-not (and (str/starts-with? (str resource-file) "file")
                     ;; Normalize to forward slashes so we can have it working for
                     ;; Windows as well... always Windows.
                     (str/includes? (-> (str resource-file)
                                        (str/replace #"\\" "/"))
                                    (-> (.getAbsolutePath tmp-file)
                                        (str/replace #"\\" "/"))))
        (println :COPYING_RESOURCE resource-file (.getAbsolutePath tmp-file))
        (io/make-parents tmp-file)
        (with-open [in (io/input-stream resource-file)]
          (io/copy in tmp-file))))
    (throw (ex-info (str "Resource does not exist:" resource-filename) {}))))

(defn -copy-lib!
  [lib-name]
  (-copy-resource! (str "vybe/native/" (System/mapLibraryName lib-name))
                   (System/mapLibraryName lib-name)))

(defn -try-bean
  [s]
  (try
    (bean (Class/forName s))
    (catch Exception _)))

(deftype VybePoolArena [^Arena arena allocator]
  Arena
  (^MemorySegment allocate [_ ^long byte-size ^long byte-alignment]
   (-> ^SegmentAllocator allocator
       (.allocate byte-size byte-alignment)
       (.fill 0)))
  (scope [_]
    (.scope arena))
  (close [_]
    #_(.close arena)))

(defonce ^Arena -shared-arena
  (Arena/ofShared))

(defonce ^MemorySegment -shared-arena-mem-segment
  (.allocate -shared-arena (* 1024 1024)))

(defn make-pool-arena
  (^VybePoolArena []
   (make-pool-arena (SegmentAllocator/slicingAllocator -shared-arena-mem-segment)
                    -shared-arena))
  (^VybePoolArena [segment-allocator]
   (make-pool-arena segment-allocator -shared-arena))
  (^VybePoolArena [segment-allocator arena]
   (VybePoolArena. arena segment-allocator)))
#_ (let [arena (make-pool-arena 128)]
     (.allocate arena 64)
     (.allocate arena 64)
     (.allocate arena 64)
     (.allocate arena 64)
     (.allocate arena 64)
     #_(.*counter arena)
     (.*state arena)
     arena)

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
                            #_[arena-params `(make-pool-arena ~(* 1024 100))]
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

  clojure.lang.Named
  (getName [this]
    (str "C_"
         (-> (-> this .layout .name .get)
             (str/replace #"\." "_DOT_")
             (str/replace #"/" "_SLASH_")
             (str/replace #"-" "_DASH_"))))

  #_ #_clojure.lang.IPersistentCollection
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
    (str (-vybe-component-rep this)))
  (equals [this x]
    (and (instance? VybeComponent x)
         (= (.layout this)
            (.layout ^VybeComponent x)))))

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

(extend-protocol core-p/Datafiable
  VybeComponent
  (datafy [this]
    (-> (into (.fields this)
              (-> (.fields this)
                  (update-vals #(if-not (:constructor %)
                                  (:type %)
                                  (select-keys % [:type :constructor])))))
        (vary-meta merge
                   {:portal.runtime/type
                    (symbol (.get (.name (.layout this))))

                    #_ #_`core-p/nav (fn [_ _k v]
                                       (if-let [c (:vp/component (meta v))]
                                         (core-p/datafy c)
                                         v))}))))

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
  ^MemorySegment [s]
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
           (throw (ex-info "VybePMap is set as a constant, you aren't allowed to modify it!"
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

  clojure.lang.IDeref
  (deref [this] (when-let [f (:vp/deref (.opts (.component this)))]
                  (f this)))

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

(extend-protocol core-p/Datafiable
  VybePMap
  (datafy [this]
    (let [c-sym (symbol (.get (.name (.layout (.component this)))))]
      (-> (let [k->idx (zipmap (keys this) (range))
                metadata (pmap-metadata this)]
            (cond-> (into (sorted-map-by (fn [k1 k2]
                                           (compare (k->idx k1) (k->idx k2))))
                          this)
              metadata (assoc :vp/metadata metadata)))
          (vary-meta merge
                     {:portal.runtime/type c-sym

                      `core-p/nav (fn [_ _k v]
                                    #_(if (= c-sym v)
                                        (core-p/datafy (.component this))
                                        v)
                                    (core-p/datafy (.component this)))})))))

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

(defmacro as
  "Macro acting as an alias for `p->map`."
  ([mem-segment component]
   `(p->map ~mem-segment ~component nil))
  ([mem-segment component opts]
   `(p->map ~mem-segment ~component ~opts)))

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

(defn component?
  "Check if value is a IVybeComponent"
  [v]
  (instance? IVybeComponent v))

(defn comp-fields
  "Get fields of a VybeComponent.

  The return is in the schema format, e.g.

    `[[:a :float]
      [:b :int]]"
  [^IVybeComponent c]
  (mapv (fn [[k {:keys [type]}]]
          [k type])
        (.fields c)))

(defn comp-merge
  "Merge a component with another component or with some
  fields (e.g. for inheritance)."
  [^IVybeComponent c fields-or-c]
  (into (comp-fields c)
        (if (component? fields-or-c)
          (comp-fields fields-or-c)
          fields-or-c)))

(defn comp-name
  "Get component name."
  [^IVybeComponent c]
  (-> c .layout .name .get))

(defn new*
  "Create new component, you should prefer using
  the component itself, e.g. `(MyComponent {:field1 22 :field2 \"ddd\"})}`."
  ([c]
   (c))
  ([params c]
   (c params)))

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
  (let [byte? (= el-byte-size 1)]
    (fn arr-vybe-component-builder
      [^MemorySegment mem-segment coll]
      (if (or (instance? IVybeMemorySegment coll)
              (instance? MemorySegment coll))
        (let [^MemorySegment v (mem coll)]
          (MemorySegment/copy v
                              0
                              mem-segment
                              field-offset
                              (if byte?
                                (.byteSize v)
                                (* el-byte-size (count coll)))))

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
             vec)))))

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

(def ^:private *mem-cache (atom {}))

(defn mem
  "Get memory segment from a string, a IVybeMemorySegment or the memory segment
  itself.

  The arity-2 and arity-3 versions cache it (it will use the root arena regardless of what's
  being used), it's useful for local state, for example."
  (^MemorySegment [v]
   (cond
     (instance? IVybeMemorySegment v)
     (.mem_segment ^IVybeMemorySegment v)

     (string? v)
     (.allocateFrom (default-arena) v)

     (instance? Long v)
     (MemorySegment/ofAddress v)

     :else
     v))
  (^MemorySegment [identifier v]
   (or (get @*mem-cache identifier)
       (let [original-mem (mem v)
             byte-size (.byteSize original-mem)
             p (with-arena-root
                 (.allocate (default-arena) byte-size))]
         (MemorySegment/copy original-mem 0
                             p 0
                             byte-size)
         (swap! *mem-cache assoc identifier p)
         p)))
  (^MemorySegment [identifier v mem-size]
   (or (get @*mem-cache [identifier mem-size])
       (let [original-mem (mem v)
             byte-size (.byteSize original-mem)
             p (with-arena-root
                 (alloc mem-size 8))]
         (MemorySegment/copy original-mem 0
                             p 0
                             byte-size)
         (swap! *mem-cache assoc [identifier mem-size] p)
         p))))

(defn mem-cache
  ([identifier]
   (get @*mem-cache identifier))
  ([identifier mem-size]
   (get @*mem-cache [identifier mem-size])))

(defn set-at
  "Set at index for a VybePSeq."
  [^VybePSeq pseq ^long idx v]
  (.setAtIndex ^MemorySegment (mem pseq) ^AddressLayout (.layout pseq) idx (try-string v)))

(defn get-at
  "Get at index for a VybePSeq."
  [^VybePSeq pseq ^long idx]
  (.getAtIndex ^MemorySegment (mem pseq) ^AddressLayout (.layout pseq) idx))

(defn address
  "Get address from a value."
  [v]
  (.address (mem v)))

(defn &
  "Get address from a value (same as `address`)."
  [v]
  (.address (mem v)))

(defn- &-reader
  [form]
  `(vybe.panama/& ~form))

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

(defn set-mem
  "Set a memory segment to some value."
  [^MemorySegment mem-segment v]
  (let [v-mem (mem v)]
    (MemorySegment/copy v-mem       0
                        mem-segment 0
                        (.byteSize  v-mem)))
  mem-segment)

(defn- -primitive-builders
  [field-type ^long field-offset field-layout]
  (let [type* (if (instance? MemoryLayout field-type)
                (-value-layout->type field-type)
                field-type)]
    (case type*
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
                        (unchecked-short value)))
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
                      {:field-type field-type})))))

(def string-layout
  (-> ValueLayout/ADDRESS
      (.withTargetLayout (MemoryLayout/sequenceLayout
                          Long/MAX_VALUE
                          ValueLayout/JAVA_BYTE))
      (.withName (str `string-layout))))

(defn fn-descriptor?
  "Check if value is a foreign.FunctionDescriptor or a `:fn`."
  [fn-desc]
  (or (and (vector? fn-desc)
           (contains? #{:fn} (first fn-desc)))
      (instance? FunctionDescriptor fn-desc)))

(defn -type->layout
  ^MemoryLayout [field-type]
  (cond
    (instance? IVybeComponent field-type)
    (.layout ^IVybeComponent field-type)

    (instance? MemoryLayout field-type)
    field-type

    (and (vector? field-type)
         (= (first field-type) :vec))
    (let [[_ {:keys [size]} t] field-type]
      (MemoryLayout/sequenceLayout size (-type->layout t)))

    (and (vector? field-type)
         (= (first field-type) :padding))
    (let [[_ {:keys [size]}] field-type]
      (MemoryLayout/paddingLayout size))

    (and (vector? field-type)
         (contains? #{:pointer :*} (first field-type)))
    ValueLayout/ADDRESS

    ;; Function description.
    (fn-descriptor? field-type)
    ValueLayout/ADDRESS

    :else
    (case field-type
      :* ValueLayout/ADDRESS
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

(defmacro type->layout
  [field-type]
  (case field-type
    :* `ValueLayout/ADDRESS
    :pointer `ValueLayout/ADDRESS
    :double `ValueLayout/JAVA_DOUBLE
    :long `ValueLayout/JAVA_LONG
    :int `ValueLayout/JAVA_INT
    :boolean `ValueLayout/JAVA_BOOLEAN
    :char `ValueLayout/JAVA_CHAR
    :float `ValueLayout/JAVA_FLOAT
    :short `ValueLayout/JAVA_SHORT
    :byte `ValueLayout/JAVA_BYTE
    :string `string-layout
    `(-type->layout ~field-type)))
#_ (type->layout [:vec :float])

(defn sizeof
  "Get layout size."
  ([c]
   (if (component? c)
     (-> ^IVybeComponent c .layout .byteSize)
     (-> (type->layout c) .byteSize)))
  ([^IVybeComponent c field]
   (-> (.fields c)
       (get field)
       :layout
       sizeof)))

(defn alignof
  "Get alignment size."
  ([c]
   (if (component? c)
     (-> ^IVybeComponent c .layout .byteAlignment)
     (-> (type->layout c) .byteAlignment)))
  ([^IVybeComponent c field]
   (-> (.fields c)
       (get field)
       :layout
       alignof)))

(defn p->value
  "Convert a pointer into a value."
  [mem-segment component]
  (if (instance? VybeComponent component)
    (VybePMap. mem-segment component nil)
    (let [getter (:getter (-primitive-builders component 0 (type->layout component)))]
      (getter mem-segment))))

(defn arr?
  "Check if value is a IVybePSeq."
  [v]
  (instance? IVybePSeq v))

(defn arr
  "Create array of a component type with a specific size."
  (^VybePSeq [c-vec]
   (let [first-el (first c-vec)]
     (cond
       (arr? first-el)
       (arr c-vec :pointer)

       :else
       (let [c (.component ^IVybeWithComponent (first c-vec))
             c-arr (arr (count c-vec) c)]
         (vec (map-indexed (fn [idx ^IVybeMemorySegment v]
                             (.copyFrom (.mem_segment ^IVybeMemorySegment (nth c-arr idx))
                                        (.reinterpret (.mem_segment v)
                                                      (.byteSize (.layout c))
                                                      (default-arena)
                                                      nil)))
                           c-vec))
         c-arr))))
  (^VybePSeq [primitive-vector-or-size c-or-layout]
   (if (sequential? primitive-vector-or-size)

     ;; For primitives.
     (let [c-vec primitive-vector-or-size
           size (count primitive-vector-or-size)
           ^ValueLayout l (type->layout c-or-layout)
           c-arr (arr size c-or-layout)
           ^Object obj (condp = l
                         (type->layout :float)
                         (float-array (mapv unchecked-float c-vec))

                         (type->layout :int)
                         (int-array (mapv unchecked-int c-vec))

                         (type->layout :long)
                         (long-array (mapv unchecked-long c-vec))

                         (type->layout :byte)
                         (byte-array (mapv unchecked-byte c-vec))

                         (type->layout :pointer)
                         (long-array (mapv (comp unchecked-long address) c-vec)))
           target-mem (.mem_segment c-arr)]
       (if (= l (type->layout :pointer))
         (doseq [^long i (range size)]
           (MemorySegment/.setAtIndex target-mem AddressLayout/ADDRESS i (mem (nth c-vec i))))
         (MemorySegment/copy obj 0 target-mem l 0 size))
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

(defn p*
  "Convenience for when you have a pointer to an element.

  It's equivalent to `(vp/arr [pmap])` or `(vp/arr mem 1 MyComponent)`. It will
  return the first element of the VybePSeq."
  ([pmap]
   (first (arr [pmap])))
  ([mem c]
   (first (arr mem 1 c))))

(defn layout
  "Get layout from a VybeComponent."
  ^MemoryLayout [^VybeComponent c]
  (.layout c))

(defn reinterpret
  "Reinterpret a memory segment to a new size."
  [^MemorySegment mem-segment ^long size]
  (.reinterpret mem-segment size (default-arena) nil))

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
(declare -adapt-layout)

(defn adapt-layout
  "Convert a layout to a EDN representation."
  ([^MemoryLayout l]
   (adapt-layout l {}))
  ([^MemoryLayout l field->meta]
   (adapt-layout l field->meta nil))
  ([^MemoryLayout l field->meta struct-layout]
   (adapt-layout l field->meta struct-layout []))
  ([^MemoryLayout l field->meta ^StructLayout struct-layout path-acc]
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
       (layout->c l field->meta (conj path-acc (some-> struct-layout .name))))

     (instance? UnionLayout l)
     (let [^UnionLayout l l]
       (layout->c l field->meta (conj path-acc (some-> struct-layout .name))))

     (instance? SequenceLayout l)
     (let [^SequenceLayout l l]
       (if struct-layout
         [:vec {:size (.elementCount l)}
          ((-adapt-layout struct-layout field->meta
                          (conj path-acc (.elementLayout l)))
           (.elementLayout l))]
         [:vec {:size (.elementCount l)}
          (.elementLayout l)]))

     :else
     (throw (ex-info "Layout not supported"
                     {:layout l
                      :path (conj path-acc (.name struct-layout) #_(.name l))})))))

(defn- -adapt-layout
  [^StructLayout struct-layout field->meta path-acc]
  (fn -adapt-layout--internal [l]
    (adapt-layout l field->meta struct-layout path-acc)))
#_ ((-adapt-layout nil {} []) ValueLayout/JAVA_INT)

(declare c-fn)

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

                (and (vector? field-type)
                     (= (first field-type) :vec))
                (let [c (last field-type)
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

                (and (vector? field-type)
                     (contains? #{:pointer :*} (first field-type)))
                (-primitive-builders :pointer field-offset field-layout)

                (fn-descriptor? field-type)
                (let [generated-fn (c-fn null field-type)]
                  {:builder (fn c-fn-builder
                              [^MemorySegment mem-segment value]
                              ;; Handle normal clj functions as well.
                              (let [value (if (fn? value)
                                            (c-fn value field-type)
                                            value)
                                    ^MemorySegment value (mem value)]
                                (.set mem-segment
                                      ^AddressLayout field-layout
                                      field-offset
                                      value)))
                   :getter (fn c-fn-getter
                             [^MemorySegment mem-segment]
                             (assoc generated-fn :fn-address (.get mem-segment
                                                                   ^AddressLayout field-layout
                                                                   field-offset)))})

                :else
                (-primitive-builders field-type field-offset field-layout)))])))

(defonce ^:private *identifier->component (atom {}))

(defn update-aliases!
  "Receives a map of symbol identifiers to components, essentially creating
  aliases.

  It's used mainly to convert jextract layout identifier to well-known components."
  [m]
  (swap! *identifier->component merge m))

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
   (or (let [n (.name layout)]
         (when (.isPresent n)
           (get @*identifier->component (symbol (.get n)))))
       (let [fields (->> (.memberLayouts layout)
                         (remove #(instance? PaddingLayout %))
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
          #_(zero? (count fields)))))))
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
   (or (get @*identifier->component identifier)
       (let [schema ((fn schema-adapter [v]
                       (cond
                         (vector? v)
                         (mapv schema-adapter v)

                         (seq? v)
                         (map schema-adapter v)

                         (= v :*)
                         :pointer

                         :else
                         v))
                     schema)
             opts (set/rename-keys opts {:constructor :vp/constructor
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
                 component)))))))
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
    `(do (def ~(with-meta sym (merge {:tag `VybeComponent}
                                     opts))
           (make-component
            (quote ~(symbol (str *ns*) (str sym)))
            ~opts
            ~schema))
         (alter-meta! (var ~sym) update :doc #(str "VybeComponent\n\n"
                                                   ~sym
                                                   (when %
                                                     (str "\n\n" %))))
         (var ~sym))))
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

(defn bool*
  ^MemorySegment [v]
  (byte* (if v 1 0)))

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
    `(-> (reify ~(symbol (str klass "$Function"))
           (~'apply ~params
            (try
              ~@fn-tail
              (catch Exception e#
                (println e#)))))
         (~(symbol (str klass "/allocate"))
          (vp/default-arena)))))
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

(defn fn-descriptor->map
  "Ednify fn descriptor. You should use this function if you want to parse
  `[:fn ...]` or foreign.FunctionDescriptor info.

  NOTE The return is not a function descriptor per se, just a way to read information from
  it.

  Returned keys are:

    - `:type`, hardcoded to the `:function` value
    - `:args`, vector containing maps with `:symbol` (a string) and `:schema`
    - `:ret` , map with `:schema`"
  [fn-desc]
  #_(def fn-desc fn-desc)
  ;; If we have some metadata, normalize it.
  (let [[^FunctionDescriptor fn-desc {:keys [fn-args]}] (if (and (vector? fn-desc)
                                                                 (map? (second fn-desc)))
                                                          [(:foreign-fn-desc (second fn-desc))
                                                           (second fn-desc)]
                                                          [fn-desc nil])
        fn-desc (if (instance? FunctionDescriptor fn-desc)
                  (let [ret (.returnLayout fn-desc)
                        ret-schema (if (.isPresent ret)
                                     (adapt-layout (.get ret))
                                     :void)
                        args (->> (.argumentLayouts fn-desc)
                                  (map-indexed (fn [idx foreign-layout]
                                                 (let [n (.name ^MemoryLayout foreign-layout)
                                                       n (or (nth fn-args idx)
                                                             (if (.isPresent n)
                                                               (.get n)
                                                               (str "_arg_" idx)))]
                                                   [n (adapt-layout foreign-layout)])))
                                  vec)]
                    (into [:fn ret-schema]
                          args))
                  fn-desc)
        [_ ret & args] fn-desc]
    {:type :function
     :args (mapv (fn [[k schema]]
                   {:symbol (name k)
                    :schema (or schema :void)})
                 args)
     :ret {:schema (or ret :void)}}))
#_ (fn-descriptor->map
    (fn-descriptor
     [:fn vybe.type/Vector2
      [:world [:* :void]]
      [:size vybe.type/Vector2]]))
#_ (fn-descriptor->map
    (fn-descriptor
     [:fn [:* :void]
      [:world [:* :void]]
      [:size :long]]))
#_ (fn-descriptor->map
    (fn-descriptor
     [:fn nil
      [:world [:* :void]]
      [:size :long]]))

(defn fn-descriptor
  "Create a foreign.FunctionDescriptor from EDN, see usage below.

  E.g.

    (fn-descriptor
     [:fn [:* :void]
      [:world [:* :void]]
      [:size :long]])

  or, equivalently

    (fn-descriptor
     {:type :function
      :args [{:symbol \"world\" :schema :*}
             {:symbol \"size\" :schema :long}]
      :ret {:schema [:* :void]}})"
  [fn-desc]
  (cond
    (map? fn-desc)
    (let [{:keys [args ret]} fn-desc
          void-fn (->> args
                       (mapv (fn [{:keys [schema symbol]}]
                               (cond-> (type->layout schema)
                                 symbol
                                 (.withName (name symbol)))))
                       (into-array MemoryLayout)
                       FunctionDescriptor/ofVoid)]
      (cond-> void-fn
        (not (contains? #{nil :void} (:schema ret)))
        (FunctionDescriptor/.changeReturnLayout (type->layout (:schema ret)))))

    (and (vector? fn-desc) (= (first fn-desc) :fn))
    (fn-descriptor (fn-descriptor->map fn-desc))

    (instance? FunctionDescriptor fn-desc)
    fn-desc

    :else
    (throw (ex-info "Unrecognized function descriptor format, it should be in a `[:fn ...]`, `{:type :function ...}` or a FunctionDescriptor"
                    {:desc fn-desc}))))
#_ (-> (fn-descriptor
        [:fn [:* :void]
         [:world [:* :void]]
         [:size :long]])
       fn-descriptor)
#_(fn-descriptor
   {:type :function
    :args [{:symbol "world" :schema :*}
           {:symbol "size" :schema :long}]
    :ret {:schema [:* :void]}})

(defonce ^Linker native-linker
  (memoize (fn [] (Linker/nativeLinker))))

(defrecord+ VybeCFn [c-fn fn-address]
  clojure.lang.IFn
  (invoke [_] (c-fn fn-address))
  (invoke [_ a1] (c-fn fn-address a1))
  (invoke [_ a1 a2] (c-fn fn-address a1 a2))
  (invoke [_ a1 a2 a3] (c-fn fn-address a1 a2 a3))
  (invoke [_ a1 a2 a3 a4] (c-fn fn-address a1 a2 a3 a4))
  (invoke [_ a1 a2 a3 a4 a5] (c-fn fn-address a1 a2 a3 a4 a5))
  (invoke [_ a1 a2 a3 a4 a5 a6] (c-fn fn-address a1 a2 a3 a4 a5 a6))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7] (c-fn fn-address a1 a2 a3 a4 a5 a6 a7))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7 a8] (c-fn fn-address a1 a2 a3 a4 a5 a6 a7 a8))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7 a8 a9] (c-fn fn-address a1 a2 a3 a4 a5 a6 a7 a8 a9))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7 a8 a9 a10] (c-fn fn-address a1 a2 a3 a4 a5 a6 a7 a8 a9 a10))

  IVybeMemorySegment
  (mem_segment [_] fn-address))

(defn fnc?
  "Check if value ias a `VybeCFn`."
  [v]
  (instance? VybeCFn v))

(defn- -schema->stub-type
  [schema]
  (cond
    (and (vector? schema)
         (contains? #{:pointer :*} (first schema)))
    `MemorySegment

    (contains? #{:pointer :*} schema)
    `MemorySegment

    (component? schema)
    `MemorySegment

    :else
    (symbol schema)))
#_ (-> ((fnc :- [:* :void]
          [world :- [:* :void]
           size :- :long]
          world)
        (vybe.c/VybeAllocator {:alloc (vybe.c/VybeAllocator)})
        30)
       (p* vybe.c/VybeAllocator))

(def ^:private *reify-clj-cache (atom {}))

(defn- -upcall-mem
  "Returns a memory segment representing a CLJ function that can be called
  from C (upcall)."
  [f fn-desc]
  (let [{:keys [ret args]} (fn-descriptor->map fn-desc)
        desc (fn-descriptor fn-desc)
        linker (native-linker)
        return-tag (-schema->stub-type (:schema ret))
        interface-sym (symbol (str "_VybeCljFn__"
                                   (->> (concat [(-> return-tag
                                                     str
                                                     (str/replace #"\." "_")
                                                     symbol)]
                                                (->> args
                                                     (mapv (fn [{:keys [schema]}]
                                                             (-> (-schema->stub-type schema)
                                                                 str
                                                                 (str/replace #"\." "_")
                                                                 symbol)))))
                                        (str/join "__"))))
        f-sym (gensym)
        reify-builder (or (get @*reify-clj-cache interface-sym)
                          (let [builder (eval `(do (definterface ~interface-sym
                                                     (~(with-meta 'apply
                                                         {:tag return-tag})
                                                      ~(->> args
                                                            (mapv (fn [{:keys [schema]}]
                                                                    (with-meta (gensym)
                                                                      {:tag (-schema->stub-type schema)}))))))
                                                   (fn [~f-sym]
                                                     (reify ~interface-sym
                                                       ~(let [params (repeatedly (count args) gensym)]
                                                          `(~'apply [_ ~@params]
                                                            (~f-sym ~@params)))))))]
                            (swap! *reify-clj-cache assoc interface-sym builder)
                            builder))
        f (reify-builder (fn [& args]
                           (try
                             (mem (apply f args))
                             (catch Exception e
                               (println e)))))
        mh (-> (MethodHandles/lookup)
               (.findVirtual (class f) "apply" (FunctionDescriptor/.toMethodType desc)))
        linker-options (into-array java.lang.foreign.Linker$Option [])
        mem-segment (.upcallStub linker (.bindTo mh f) desc (default-arena) linker-options)]
    mem-segment))

(defn c-fn
  "For the arity-1 version, it receives a function description and returns a CLJ function that receives
  a mem segment (C function pointer) + the C function arguments (downcall).

  For the arity-2 version, it returns a VybeCFn. A VybeCFn is a record that can also be invoked,
  it stores the memory segment to a C function and a C function call.

  You can also get the memory segment from a VybeCFn using `mem`.

  Primitive arguments will be coerced into their defined types."
  ([fn-desc]
   (let [linker-options (into-array java.lang.foreign.Linker$Option [])
         fn-desc-map (fn-descriptor->map fn-desc)
         handle (.downcallHandle (native-linker)
                                 (fn-descriptor fn-desc)
                                 linker-options)
         c-adapter (fn [schema]
                     (fn [v]
                       (let [c (component v)]
                         (if (and c (layout-equal? schema c))
                           (mem v)
                           (throw (ex-info "Invalid c-fn argument"
                                           {:expected-schema schema
                                            :actual-value v}))))))
         adapters (mapv (fn [{:keys [schema]}]
                          (cond
                            (and (vector? schema)
                                 (contains? #{:pointer :*} (first schema)))
                            mem

                            (component? schema)
                            (c-adapter schema)

                            :else
                            (case schema
                              :int unchecked-int
                              :long unchecked-long
                              :float unchecked-float
                              :double unchecked-double
                              :short unchecked-short
                              :byte unchecked-byte
                              (:string :pointer :*) mem)))
                        (:args fn-desc-map))
         ret (:schema (:ret fn-desc-map))
         ret-adapter (if (component? ret)
                       #(when-not (null? %)
                          (p->map % ret))
                       identity)]
     (fn [& args]
       (->> (rest args)
            (mapv (fn [adapter arg]
                    (adapter arg))
                  adapters)
            (concat [(first args)]
                    (when (component? ret)
                      [(default-arena)]))
            ^{:tag "[Ljava.lang.Object;"} (into-array Object)
            ;; TODO Invoke with exact arguments.
            (MethodHandle/.invokeWithArguments handle)
            ret-adapter))))
  ([f-or-mem fn-desc]
   (-> {:fn-address (if (fn? f-or-mem)
                  (-upcall-mem f-or-mem fn-desc)
                  f-or-mem)
        :fn-desc fn-desc
        :c-fn (c-fn fn-desc)}
       map->VybeCFn)))

(comment

  (require 'vybe.c)
  (vybe.c/defn* eita :- :int
    [a :- :int]
    (+ a 40))

  (let [{:keys [lib-full-path]} eita
        lib (SymbolLookup/libraryLookup lib-full-path (default-arena))
        eita-mem (-> (.find lib (vybe.c/->name #'eita)) .get)]
    ((c-fn eita-mem (:fn-desc eita))
     440))

  (let [f (-> (fn [v y]
                (+ v y 46))
              (c-fn [:fn :long
                     [:v :long]
                     [:v1 :long]]))]
    (f 20 34))

  ())

(defmacro fnc
  "Macro wrapper for `c-fn`. It acts like `fn`, but
  you have to pass the return and the arguments types, see
  example below where we call a function with arguments 10 and 40.

  It will create a C pointer backed by a JVM (clojure) function. It
  does not transpile/compile to C (for this other purpose, check the
  `vybe.c` namespace).

  ((fnc :- :long
     [x :- :long
      y :- :long]
     (+ x y 56))
   10 40)
  ;; => 106"
  {:clj-kondo/lint-as 'schema.core/fn}
  [_ ret-schema & [args & fn-body]]
  `(let [args# ~(->> args
                     (partition-all 3 3)
                     (mapv (fn [[sym _ schema]]
                             [`(quote ~sym) schema])))
         args-desc-1# (quote ~(->> args
                                   (partition-all 3 3)
                                   (mapv (fn [[sym _ schema]]
                                           [sym schema]))))]
     (-> (fn ~(mapv first (partition-all 3 3 args))
           ~@fn-body)
         (c-fn (into [:fn ~ret-schema] args#))
         (with-meta {:arglists (list args-desc-1#)
                     :doc (format "Returns %s" (if (component? ~ret-schema)
                                                 (comp-name ~ret-schema)
                                                 (quote ~ret-schema)))}))))

(defmacro defnc
  "Like `fnc`, see its documentation.

  (defnc jj :- :long
    [x :- :long
     y :- :long]
    (+ x y 56))
  (jj 20 40)
  ;; => 116"
  {:clj-kondo/lint-as 'schema.core/defn}
  [n & fn-tail]
  `(do
     (def ~n
       (fnc ~@fn-tail))
     (alter-meta! (var ~n) merge (meta ~n))
     (var ~n)))

(comment

  (defcomp VybeAllocator
    [[:alloc [:fn [:* :void]
              [:world [:* :void]]
              [:size :long]]]
     [:free [:fn :void
             [:world [:* :void]]
             [:ptr [:* :void]]]]])

  ;; You can call a function pointer like this...
  (let [valloc (VybeAllocator
                {:alloc (fnc :- [:* :void]
                          [world :- [:* :void]
                           size :- :long]
                          world)})]
    ((:alloc valloc) (VybeAllocator) 30))

  ;; or by using a normal clj function as the struct field already has
  ;; an associated function descriptor.
  (let [valloc (VybeAllocator
                {:alloc (fn [world
                             size]
                          world)})
        p (VybeAllocator)]
    (= (mem ((:alloc valloc) p 30))
       (mem p)))

  ((fnc :- :long
     [x :- :long
      y :- :long]
     (+ x y 56))
   10 40)

  (defnc jj :- :long
    [x :- :long
     y :- :long]
    (+ x y 56))
  (jj 20 40)

  ((fnc :- :long
     []
     44))

  ())

(defn zero!
  "Fill a mem segment with zeros.

  `len` is not in bytes, but the number of`vybe-schema` elements."
  [^MemorySegment mem len vybe-schema]
  (let [l (type->layout vybe-schema)]
    (.fill (.reinterpret mem (* len (.byteSize l))) 0))
  mem)
