(ns vybe.experimental.audio.overtone
  "Helper functions for overtone."
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [babashka.process :as proc]
   [nextjournal.clerk :as clerk]
   [overtone.config.log :as ov.log]
   [overtone.config.store :as ov.config]
   [overtone.helpers.file :as ov.file]
   [overtone.helpers.lib :as ov.lib]
   [overtone.helpers.system :refer [get-os linux-os? mac-os? windows-os?]]
   #_[overtone.live :refer :all :as l]
   [overtone.midi :as midi]
   [overtone.sc.defaults :as ov.defaults]
   [overtone.sc.machinery.server.connection :as ov.conn]
   [overtone.sc.machinery.ugen.special-ops :as special-ops]
   [overtone.sc.machinery.synthdef :as synthdef]
   [clojure.tools.analyzer.jvm :as ana]
   [clojure.tools.analyzer.ast :as ast]
   [portal.api :as portal]
   [portal.viewer :as pv]
   [clojure.datafy :as datafy]
   [clojure.walk :as walk]
   [bling.core :as bling]
   [vybe.panama :as vp]))

#_(set! *warn-on-reflection* true)

(comment

  #_(do #_(portal/close portal)

      (declare on-load)
      (def portal
        (portal/open
         {:on-load #'on-load}))

      (defn on-load
        []
        (portal/eval-str portal (slurp (io/resource "vybe/experimental/portal.cljc"))))

      (defn submit [value]
        (if (-> value meta :portal.nrepl/eval)
          #_(let [{:keys [stdio report result]} value]
              (when stdio (portal/submit (datafy/datafy stdio)))
              (when report (portal/submit (datafy/datafy report)))
              (portal/submit (datafy/datafy result)))
          nil
          (portal/submit value)))
      (add-tap #'submit))

  #_(do
    (def a (atom 0))
    (tap> a)
    (time
     (doseq [n (range 120)]
       (reset! a n))))

  #_(tap> ^{:portal.viewer/default :vybe.experimental.portal/view-presentation}
        [^{:portal.viewer/default :portal.viewer/hiccup}
         [:h1 "hello"]
         ^{:portal.viewer/default :portal.viewer/hiccup}
         [:h1 "world"]])

  (tap> ^{:portal.viewer/default :portal.viewer/hiccup}
        [:<>
         [:script {:src "https://cdn.jsdelivr.net/npm/vega@5.30.0"}]])

  #_(do (in-ns 'portal.runtime)
      (defn- invalidate [session-id a old new]
        (when-not (= (value->key old) (value->key new))
          (set-timeout
           #(when (= @a new) (notify session-id a))
           0))))

  (reset! portal 0)

  #_(time
   (doseq [_ (range 60)]
     (swap! portal inc)))


  ;; ------------- CLJS
  #_(portal/repl portal)
  #_:cljs/quit

  #_(do (defn add-script! [src]
        (let [script (js/document.createElement "script")]
          (.setAttribute script "src" src)
          (js/document.head.appendChild script)))

      (run! add-script! ["https://cdn.jsdelivr.net/npm/vega@5.30.0"
                         "https://cdn.jsdelivr.net/npm/vega-lite@5.21.0"
                         "https://cdn.jsdelivr.net/npm/vega-embed@6.26.0"]))

  #_ #_@#'portal.ui.viewer.vega/vega-url
  (alter-var-root #'portal.ui.viewer.vega/vega-url (constantly "eita"))

  #_(do
    (require 'portal.runtime)
    (ns-publics 'portal.runtime)
    (defn- value->key
      "Include metadata when capturing values in cache."
      [value]
      (when (#'portal.runtime/hashable? value)
        [:value value (portal.runtime/hash+ value)]))

    (defn- invalidate [session-id a old new]
      (when-not (= (value->key old) (value->key new))
        (set-timeout
         #(when (= @a new) (notify session-id a))
         0))))

  ())

(comment

  (nextjournal.clerk/serve! {:watch-paths ["src/vybe/audio/overtone.clj"]})

  (doseq [_ (range 100)]
    (demo 0.2
          [(sin-osc :freq 400)
           (sin-osc :freq 300)])
    (Thread/sleep 500)
    (demo 0.2
          [(sin-osc :freq 600)
           (sin-osc :freq 300)])
    (Thread/sleep 500))

  ;; Midi.
  (doseq [_ (range 100)]
    (-> (first (midi-connected-receivers))
        (midi/midi-note 58 30 1000))
    (Thread/sleep 500)
    (-> (first (midi-connected-receivers))
        (midi/midi-note 60 30 1000))
    (Thread/sleep 500))

  ;; How To Fully Connect Bitwig Studio & VCV Rack (On A Mac), https://www.youtube.com/watch?v=mAxDrDPXtvA
  ;; Audio.
  (do (kill-server)
      (connect-server 57110))

  ())

(def ^:private header-tags
  ["TITLE"
   "CATEGORIES"
   "RELATED"
   "SUMMARY"
   "REDIRECT"
   "CLASS"])

(def ^:private section-tags
  ["SECTION"
   "DESCRIPTION"
   "CLASSMETHODS"
   "INSTANCEMETHODS"
   "EXAMPLES"])

(def ^:private sub-section-tags
  ["SUBSECTION"])

(def ^:private method-tags
  ["METHOD"
   "PRIVATE"
   "COPYMETHOD"
   "ARGUMENT"
   "RETURNS"
   "DISCUSSION"])

(def ^:private modal-tags
  ["STRONG"
   "EMPHASIS"
   "SOFT"
   "LINK"
   "ANCHOR"
   "IMAGE"
   "CODE"
   "TELETYPE"])

(def ^:private list-and-table-tags
  ["TABLE"
   "DEFINITIONLIST"
   "LIST"
   "NUMBEREDLIST"
   "TREE"])

(def ^:private note-and-warning-tags
  ["NOTE"
   "WARNING"
   "FOOTNOTE"])

(def ^:private other-tags
  ["KEYWORD"
   "CLASSTREE"])

(def ^:private all-tags-set
  (set (concat header-tags section-tags sub-section-tags method-tags
               modal-tags list-and-table-tags note-and-warning-tags
               other-tags)))

(def ^:private needs-end-tag-set
  (set (concat modal-tags list-and-table-tags note-and-warning-tags
               other-tags)))

(defn schelp-parse
  "Parse information in schelp format into clojure data structures."
  [schelp-doc]
  (->> (-> (str/replace schelp-doc #"\w*::" #(let [upper-cased (str/upper-case %)
                                                   value (subs upper-cased 0 (- (count upper-cased) 2))]
                                               (if (contains? all-tags-set value)
                                                 (str "___" value "___")
                                                 %)))
           (str/split #"___"))
       (remove empty?)
       (partition-all 2 2)
       (mapv (fn [[op v]]
               [op (if (contains? needs-end-tag-set op)
                     v
                     (str/trim v))]))
       ;; Partition documentation into sections.
       (reduce (fn [acc [op v]]
                 (if (contains? (set section-tags) op)
                   ;; New section. Unnamed sections will be put into a map.
                   (conj acc (cond
                               (= op "SECTION")
                               [[op v]]

                               (seq v)
                               [(keyword "overtone.schelp" op) v]

                               :else
                               [(keyword "overtone.schelp" op)]))
                   (update acc (dec (count acc)) conj [op v])))
               [[:overtone.schelp/METADATA]])
       (mapv (fn [[section & data]]
               (case section
                 :overtone.schelp/METADATA
                 (-> (into {} data)
                     (update-keys #(keyword "overtone.ugen.metadata" %)))

                 :overtone.schelp/DESCRIPTION
                 {:overtone.ugen/description
                  (str/join " " (->> data
                                     (mapv (fn [v]
                                             (if (vector? v)
                                               (str (first v)
                                                    ":: "
                                                    (last v))
                                               v)))))}

                 :overtone.schelp/CLASSMETHODS
                 {:overtone.ugen/class-methods
                  (-> (reduce (fn [{::keys [current-method] :as acc}
                                   [op v]]
                                (case op
                                  "METHOD"
                                  (assoc acc (keyword v) {:overtone.ugen.method/args []}
                                         ::current-method (keyword v))

                                  "ARGUMENT"
                                  (let [[arg-name & arg-doc'] (str/split-lines v)
                                        arg-doc (str/join "\n" arg-doc')]
                                    (update-in acc [current-method :overtone.ugen.method/args] conj
                                               {:overtone.ugen.method.arg/name (keyword arg-name)
                                                :overtone.ugen.method.arg/doc arg-doc}))

                                  acc))
                              {}
                              data)
                      (dissoc ::current-method))}

                 nil
                 #_[section data])))
       (apply merge)))

;; -- C transpiler.
(vp/defcomp Rate
  [[:sample_rate :double]
   [:sample_dur :double]
   [:buf_duration :double]
   [:buf_rate :double]
   [:slope_factor :double]
   [:radians_per_sample :double]
   [:buf_length :int]
   [:filter_loops :int]
   [:filter_remain :int]
   [:_not_used_1 :int]
   [:filter_slope :double]])

(vp/defcomp Unit
  [[:world :pointer]
   [:unit_def :pointer]
   [:graph :pointer]

   [:num_inputs :int]
   [:num_outputs :int]

   [:calc_rate :short]
   [:special_index :short]
   [:parent_index :short]
   [:done :short]

   [:input :pointer]
   [:output :pointer]

   [:rate [:* Rate]]
   [:extensions :pointer]

   ;; Equivalent to `float**` in C.
   [:in_buf [:* [:* :float]]]
   [:out_buf [:* [:* :float]]]

   [:calc_func :pointer]
   [:buf_length :int]])

(defn- ->name
  [component-or-var]
  (let [var-str (if (vp/component? component-or-var)
                  (vp/comp-name component-or-var)
                  (str (symbol component-or-var)))]
    (-> var-str
        (str/replace #"\." "_")
        (str/replace #"/" "___"))))

(defn- schema-adapter
  [v]
  (cond
    (vector? v)
    (mapv schema-adapter v)

    (seq? v)
    (map schema-adapter v)

    (= v :*)
    :pointer

    (vp/component? v)
    (symbol (vp/comp-name v))

    :else
    v))

(defn- adapt-type
  [type]
  ;; Just count the number of nested pointer
  ;; schemas so we can put the same number of
  ;; `*`s and put the type at the front (e.g. `float**`).
  (let [type (schema-adapter type)]
    (->> type
         (walk/postwalk (fn [v]
                          (if (and (vector? v)
                                   (contains? #{:pointer :*}
                                              (first v)))
                            (let [v (if-let [resolved (and (symbol? (last v))
                                                           (resolve (last v)))]
                                      (->name @resolved)
                                      (name (last v)))]
                              (str v "*"))
                            v))))))

#_(adapt-type [:* Rate])
#_(adapt-type '[:* [:* :float]])
#_(adapt-type '[:* [:* [:* Unit]]])
#_(adapt-type '[:* [:* Unist]])

(defn- comp->c
  [component]
  (let [c-name (->name component)]
    (format "typedef struct %s {\n%s\n} %s;"
            c-name
            (->> (vp/comp-fields component)
                 (sort-by (comp :idx last))
                 (mapv (fn [[k type]]
                         (str "  " (cond
                                     (and (vector? type)
                                          (= (first type) :pointer))
                                     (adapt-type type)

                                     (= type :pointer)
                                     "void*"

                                     :else
                                     (name type))
                              " " (name k) ";")))
                 (str/join "\n"))
            c-name)))
#_(comp->c Unit)

(def -clz-h
  "From clj.h in SC."
  "
#include <stddef.h>
#include <stdint.h>

#if !defined(__cplusplus)
#    include <stdbool.h>
#endif // __cplusplus

typedef int SCErr;

typedef int64_t int64;
typedef uint64_t uint64;

typedef int32_t int32;
typedef uint32_t uint32;

typedef int16_t int16;
typedef uint16_t uint16;

typedef int8_t int8;
typedef uint8_t uint8;

typedef float float32;
typedef double float64;

typedef union {
    uint32 u;
    int32 i;
    float32 f;
} elem32;

typedef union {
    uint64 u;
    int64 i;
    float64 f;
} elem64;

const unsigned int kSCNameLen = 8;
const unsigned int kSCNameByteLen = 8 * sizeof(int32);

// Do not use this. C casting is bad and causes many subtle issues.
#ifdef __GXX_EXPERIMENTAL_CXX0X__
#    define sc_typeof_cast(x) (decltype(x))
#elif defined(__GNUC__)
#    define sc_typeof_cast(x) (__typeof__(x))
#else
#    define sc_typeof_cast(x) /* (typeof(x)) */
#endif


#ifdef __MWERKS__

#    define __PPC__ 1
#    define __X86__ 0

// powerpc native count leading zeroes instruction:
#    define CLZ(x) ((int)__cntlzw((unsigned int)x))

#elif defined(__GNUC__)

/* use gcc's builtins */
static __inline__ int32 CLZ(int32 arg) {
    if (arg)
        return __builtin_clz(arg);
    else
        return 32;
}

#elif defined(_MSC_VER)

#    include <intrin.h>
#    pragma intrinsic(_BitScanReverse)

__forceinline static int32 CLZ(int32 arg) {
    unsigned long idx;
    if (_BitScanReverse(&idx, (unsigned long)arg)) {
        return (int32)(31 - idx);
    }
    return 32;
}

#elif defined(__ppc__) || defined(__powerpc__) || defined(__PPC__)

static __inline__ int32 CLZ(int32 arg) {
    __asm__ volatile(\"cntlzw %0, %1\" : \"=r\"(arg) : \"r\"(arg));
    return arg;
}

#elif defined(__i386__) || defined(__x86_64__)
static __inline__ int32 CLZ(int32 arg) {
    if (arg) {
        __asm__ volatile(\"bsrl %0, %0\nxorl $31, %0\n\" : \"=r\"(arg) : \"0\"(arg));
    } else {
        arg = 32;
    }
    return arg;
}

#elif defined(SC_IPHONE)
static __inline__ int32 CLZ(int32 arg) { return __builtin_clz(arg); }

#else
#    error \"clz.h: Unsupported architecture\"
#endif
")

(def -common-c
  (str
   -clz-h
   "
#pragma clang diagnostic ignored \"-Wextra-tokens\"
#include <stdint.h>

typedef int64_t int64;
typedef uint64_t uint64;

typedef int32_t int32;
typedef uint32_t uint32;

typedef int16_t int16;
typedef uint16_t uint16;

typedef int8_t int8;
typedef uint8_t uint8;

typedef float float32;
typedef double float64;

typedef void (*UnitCtorFunc)(void* inUnit);
typedef void (*UnitDtorFunc)(void* inUnit);

typedef void (*UnitCalcFunc)(void* inThing, int inNumSamples);

struct SC_Unit_Extensions {
    float* todo;
};

enum { calc_ScalarRate, calc_BufRate, calc_FullRate, calc_DemandRate };

static inline int32 LOG2CEIL(int32 x) { return 32 - CLZ(x - 1); }
static inline int32 NEXTPOWEROFTWO(int32 x) { return (int32)1L << LOG2CEIL(x); }

//void* (*fRTAlloc)(void* inWorld, size_t inSize);
//void* (*fRTRealloc)(void* inWorld, void* inPtr, size_t inSize);
//void (*fRTFree)(void* inWorld, void* inPtr);
"))

;; -- Stubs

(defn- parens
  [v-str]
  (format "(%s)" v-str))

(defn- ->sym
  [v]
  (cond
    (symbol? v)
    v

    (class? v)
    (symbol (.getName ^Class v))))

(def ^:private ^:dynamic *transpile-opts* {})

#_(def c-invoke nil)
(defmulti c-invoke
  "Handle custom invocations from C code for a var.

  It receives a tools.analyzer node and dispatches on the var,
  see example below.

  E.g.

    (defmethod c-invoke (declare NEXTPOWEROFTWO)
     [{:keys [args]}]
     (str \"NEXTPOWEROFTWO(\"
          (-transpile (first args))
          \")\"))"
  (fn [node]
    (let [v (:var (:fn node))]
      (if (vp/component? @v)
        `vp/component
        v))))

(defn -transpile
  "See https://clojure.github.io/tools.analyzer.jvm/spec/quickref.html"
  [{:keys [op] :as v}]
  (str (let [{:keys [line column]} (and (not (:no-source-mapping *transpile-opts*))
                                        (or (meta (:form v))
                                            (meta (first (:raw-forms v)))))]
         (when (and line column)
           (format "\n#line %s %s \n"
                   line
                   (pr-str (str "VYBE_CLJ:" *file* ":" *ns* ":" column)))))
       (case op
         :def
         (let [{:keys [name init]} v]
           (when (= (:op (:expr init)) :fn)
             (let [{:keys [params body]} (first (:methods (:expr init)))
                   return-tag (or (some-> ^Class (:return-tag (:expr init))
                                          (.getName))
                                  (some-> (::schema (meta (first (:form (first (:methods (:expr init)))))))
                                          resolve
                                          deref
                                          vp/comp-name
                                          ->name))]
               (str "VYBE_EXPORT "  return-tag " "
                    name (->> params
                              (mapv (fn [{:keys [tag form]}]
                                      (str (case (.getName ^Class tag)
                                             "[F"
                                             "float*"

                                             "java.lang.Object"
                                             (let [schema (::schema (meta form))]
                                               (adapt-type schema))

                                             tag)
                                           " " form)))
                              (str/join ", ")
                              parens)
                    " {\n"
                    (if (= return-tag "void")
                      "  "
                      "  return ")
                    (-transpile body) ";"
                    "\n}"))))

         :map
         (format "{%s}"
                 (->> (mapv (fn [k v]
                              (str "." (-transpile k) " = " (-transpile v)))
                            (:keys v)
                            (:vals v))
                      (str/join ", ")))

         :const
         (cond
           (= (:type v) :map)
           (format "{%s}"
                   (->> (:val v)
                        (mapv (fn [[k v]]
                                (str "." (name k) " = " v)))
                        (str/join ", ")))

           (string? (:val v))
           (pr-str (:val v))

           (keyword? (:val v))
           (name (:val v))

           :else
           (case (:val v)
             true 1
             false 0
             (:val v)))

         :static-call
         (let [{:keys [method args] klass :class} v]
           (case (->sym klass)
             clojure.lang.Numbers
             (case (->sym method)
               (add multiply minus gt ls)
               (->> args
                    (mapv -transpile)
                    (str/join (format " %s "
                                      ('{multiply *
                                         add +
                                         minus -
                                         gt >
                                         ls <}
                                       (->sym method))))
                    parens)

               inc
               (format "%s + 1" (-transpile (first args)))

               dec
               (format "%s - 1" (-transpile (first args))))

             clojure.lang.RT
             (case (->sym method)
               aset
               (let [[s1 s2 s3] (mapv -transpile args)]
                 (->> (format " %s[%s] = %s "
                              s1 s2 s3)
                      parens))

               (nth aget)
               (let [[s1 s2] (mapv -transpile args)]
                 (->> (format " %s[%s] "
                              s1 s2)
                      parens))

               intCast
               (-transpile (first args))
               #_(:form (first lalll #_args)))))

         :local
         (let [{:keys [form]} v]
           form)

         :do
         (let [{:keys [statements ret]} v]
           (->> (concat statements [ret])
                (mapv -transpile)
                (str/join "\n\n")))

         :if
         (let [{:keys [test then else]} v]
           (format "( %s ? %s : %s  )"
                   (-transpile test)
                   (-transpile then)
                   (-transpile else)))

         ;; Loops have special handling as we want to output a normal
         ;; C `for` as close as possible.
         :loop
         (let [{:keys [raw-forms env]} v
               raw-form (second raw-forms)
               {:keys [clojure.tools.analyzer/resolved-op]} (meta raw-form)]
           (case (symbol resolved-op)
             clojure.core/doseq
             (let [[_ bindings & body] raw-form
                   [binding-sym [_range range-arg]] (->> bindings
                                                         (partition-all 2 2)
                                                         first)]
               (format "for (int %s = 0; %s < %s; ++%s) {\n  %s;\n}"
                       binding-sym binding-sym range-arg binding-sym
                       (or (some->> (-> (cons 'do body)
                                        (ana/analyze
                                         (-> (ana/empty-env)
                                             (update :locals merge
                                                     (:locals env)
                                                     {binding-sym (ana/analyze range-arg
                                                                               (-> (ana/empty-env)
                                                                                   (update :locals merge
                                                                                           (:locals env))))})))
                                        -transpile)
                                    #_ #_ #_str/split-lines
                                    (mapv #(str "  " % ";"))
                                    (str/join))
                           "")))))

         :invoke
         (case (symbol (:var (:fn v)))
           (clojure.core/* clojure.core/+)
           (if (= (count (:args v)) 1)
             (-transpile (first (:args v)))
             (throw (ex-info "Unsupported" {:op (:op v)
                                            :form (:form v)
                                            :args v})))

           clojure.core/merge
           (let [[main-var] (:args v)
                 pointer? (meta (:form main-var))]
             (->> (rest (:args v))
                  (mapcat (fn [{:keys [keys vals]}]
                            (mapv (fn [k v]
                                    (str (:form main-var)
                                         (if pointer?
                                           "->"
                                           ".")
                                         (name (:form k))
                                         " = "
                                         (-transpile v)
                                         ";"))
                                  keys
                                  vals)))
                  (str/join "\n")))

           (c-invoke v))

         :var
         (let [my-var @(:var v)]
           (if-let [c-fn (::c-function (meta my-var))]
             c-fn
             (-transpile (ana/analyze my-var))))

         :the-var
         (let [my-var @(:var v)]
           (format "&%s"
                   (if-let [c-fn (::c-function (meta my-var))]
                     c-fn
                     my-var)))

         :set!
         (format "%s = %s;"
                 (-transpile (:target v))
                 (-transpile (:val v)))

         :host-interop
         (format "%s->%s"
                 (-transpile (:target v))
                 (:m-or-f v))

         :let
         (format "%s\n%s"
                 (->> (:bindings v)
                      (mapv (fn [{:keys [form init]}]
                              (format "__auto_type %s = %s;"
                                      form
                                      (-transpile init))))
                      (str/join "\n"))
                 (or (-transpile (:body v))
                     ""))

         nil
         (throw (ex-info (str "Unhandled `nil`: " v " (" (type v) ")")
                         {:v v
                          :v-type (type v)}))

         (do #_(def v v) #_ (:op v) #_ (keys v)
             (throw (ex-info (str "Unhandled: " (:op v))
                             {:op (:op v)
                              :raw-forms (:raw-forms v)
                              :form (:form v)}))))))

(defmethod c-invoke :default
  [{:keys [args] :as node}]
  (let [v (:var (:fn node))]
    (str (if (:no-ns (meta v))
           (name (symbol v))
           (->name v))
         "("
         (->> args
              (mapv -transpile)
              (str/join ", "))
         ")")))

(declare ^:no-ns NEXTPOWEROFTWO)
#_(defmethod c-invoke
    [{:keys [args]}]
    (str "NEXTPOWEROFTWO("
         (-transpile (first args))
         ")"))

;; Special case for a VybeComponent invocation.
(defmethod c-invoke `vp/component
  [{:keys [args] :as node}]
  (let [v (:var (:fn node))]
    (str  "(" (->name v) ")"
          (-transpile (first args)))))

(defn transpile
  ([code-form]
   (transpile code-form {}))
  ([code-form {:keys [sym-meta] :as opts}]
   (binding [*transpile-opts* sym-meta]
     (let [*schema-collector (atom [])
           *var-collector (atom {:c-fns []
                                 :non-c-fns []})

           ;; Collect vars so we can prepend them to the C code.
           prewalk-form (fn [form]
                          (ast/prewalk (ana/analyze form)
                                       (fn [v]
                                         (when (or (= (:op v) :var)
                                                   (= (:op v) :the-var))
                                           (cond
                                             (::c-function (meta @(:var v)))
                                             (swap! *var-collector update :c-fns conj (:code-form @(:var v)))

                                             (vp/component? @(:var v))
                                             (swap! *schema-collector conj @(:var v))

                                             :else
                                             (swap! *var-collector update :non-c-fns conj @(:var v))))
                                         v)))
           ;; Run twice, one for the initial form and another for the full so
           ;; we can have everything in place.
           _ (prewalk-form code-form)
           {:keys [c-fns]} @*var-collector
           final-form (concat ['do] (reverse (distinct c-fns)) [code-form])

           ;; FIXME We should do it in a loop.
           _ (prewalk-form final-form)
           {:keys [c-fns]} @*var-collector
           final-form (distinct (concat ['do] (reverse (distinct c-fns)) final-form))

           ;; Collect schemas so we can prepend them to the C code.
           _ (walk/prewalk (fn walk-fn [v]
                             (when-let [schema (::schema (meta v))]
                               (walk/postwalk (fn [v]
                                                (if-let [resolved (and (symbol? v)
                                                                       (resolve v))]
                                                  (let [fields (vp/comp-fields @resolved)]
                                                    (swap! *schema-collector conj @resolved)

                                                    (mapv (fn schema-adapter
                                                            [v]
                                                            (cond
                                                              (vector? v)
                                                              (mapv schema-adapter v)

                                                              (seq? v)
                                                              (map schema-adapter v)

                                                              (= v :*)
                                                              :pointer

                                                              (vp/component? v)
                                                              (swap! *schema-collector conj v)

                                                              :else
                                                              v))
                                                          fields))
                                                  v))
                                              schema))
                             v)
                          final-form)

           {:keys [non-c-fns]} @*var-collector
           schemas-c-code (->> @*schema-collector
                               reverse
                               distinct
                               (mapv comp->c)
                               (str/join "\n\n"))]
       {:c-code (->> [-common-c
                      "#define VYBE_EXPORT __attribute__((__visibility__(\"default\")))"
                      schemas-c-code
                      (-transpile (ana/analyze final-form))]
                     (str/join "\n\n"))
        :final-form final-form
        :form-hash (abs (hash [-common-c
                               "#define VYBE_EXPORT __attribute__((__visibility__(\"default\")))"
                               (distinct non-c-fns)
                               schemas-c-code
                               final-form
                               opts]))}))))

(defn- remove-ansi
  [s]
  (str/replace s #"\x1b\[[0-9;]*m" ""))

(defn- error-callout
  [error {:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header error
                             #_ #_:body   (str "The body of your message goes here."
                                          "\n"
                                          "Another line of copy."
                                          "\n"
                                          "Another line."
                                          )}
                            point-of-interest-opts)
        message      (bling/point-of-interest poi-opts)
        callout-opts (merge callout-opts
                            {:padding-top 1})]
    (bling/callout callout-opts message)))

(defn -c-compile
  ([code-form]
   (-c-compile code-form {}))
  ([code-form {:keys [sym-meta sym] :as opts}]
   (let [path-prefix (str (System/getProperty "user.dir") "/resources/vybe/dynamic")
         {:keys [c-code form-hash final-form]} (-> code-form (transpile opts))
         lib-name (format "lib%s.dylib" (str "vybe_" form-hash))
         lib-full-path (str path-prefix "/" lib-name)
         file (io/file lib-full-path)]
     (io/make-parents file)
     (if (and (not (or (:no-cache sym-meta)
                       (:debug sym-meta)))
              (.exists file))
       {:lib-full-path lib-full-path
        :code-form code-form}

       (do (spit "/tmp/a.c" c-code)
           (when (:debug sym-meta)
             (println c-code))
           (let [{:keys [err]} (proc/sh (format
                                         (->> ["clang -O0"
                                               "-fdiagnostics-print-source-range-info "
                                               "-fcolor-diagnostics"
                                               "-shared /tmp/a.c -o %s"]
                                              (str/join " "))
                                         lib-full-path))]
             (when (seq err)
               (let [errors (->> (str/split (remove-ansi err) #"VYBE_CLJ:")
                                 (filter seq)
                                 (mapv (fn [s]
                                         (let [[file-path -ns column line _c-line & error-str]
                                               (str/split s #":")
                                               ;; It seems that the line is reported
                                               ;; incorreclty by clang/gcc, so we
                                               ;; may dec here to make it correct in most cases.
                                               line (try
                                                      (Integer/parseInt line)
                                                      (catch Exception _))
                                               column (try
                                                        (Integer/parseInt column)
                                                        (catch Exception _))]
                                           (when (and column line)
                                             (let [lines (str/split-lines (slurp file-path))
                                                   file-line (nth lines (dec line))]
                                               {:file-path file-path
                                                :file-line file-line
                                                :-ns -ns
                                                :line line
                                                :column column
                                                :error (-> (str/join ":" error-str)
                                                           str/split-lines
                                                           first
                                                           str/trim)})))))
                                 (remove nil?))
                     clj-error (format "Found error while compiling C\n\n%s"
                                       (->> errors
                                            (mapv (fn [{:keys [file-path file-line -ns
                                                               line column error]}]
                                                    (str -ns "/" sym " "
                                                         "(" file-path ":" line ":" column ")"
                                                         "\n"
                                                         error
                                                         "\n"
                                                         file-line
                                                         ;; Caret to present the error in nicer way.
                                                         (str "\n"
                                                              (str/join (repeat (dec column) " "))
                                                              "^ <-- ERROR around here"))))
                                            (str/join "\n\n")))
                     _ (->> errors
                            (mapv (fn [{:keys [file-path file-line -ns
                                               line column error]}]
                                    (error-callout
                                     error
                                     {:point-of-interest-opts
                                      {:type   :error
                                       :file   file-path
                                       :line   line
                                       :column column
                                       :form   (try (clojure.edn/read-string
                                                     (subs file-line (dec column)))
                                                    (catch Exception _
                                                      file-line))}
                                      :callout-opts {:type :error}})))
                            (str/join "\n\n"))]
                 (println (bling/callout {:label "C error" :type :error} err))
                 (throw (ex-info clj-error
                                 {:error-lines errors
                                  :error (str/split-lines (remove-ansi err))
                                  :code-form final-form})))))
           {:lib-full-path lib-full-path
            :code-form code-form})))))

(defmacro c-compile
  "Macro that compiles a form to c and generates a shared lib out of it.
  It returns the shared lib full path.

  E,g.
    (c-compile
      (defn olha
        ^float [^float v]
        (* 0.9 v)))"
  [& code]
  (let [[opts code] (if (map? (first code))
                      [(first code) (rest code)]
                      [nil code])]
    `(-c-compile
      (quote (do ~@code))
      ~opts)))

(defn- adapt-schema
  [schema]
  (case schema
    :int {:tag 'int}
    :void {:tag 'void}
    :float* {:tag 'floats}
    {::schema schema}))

(defn- adapt-fn-args
  [fn-args]
  (->> fn-args
       (partition-all 3 3)
       (mapv (fn [[sym _ schema]]
               (with-meta sym
                 (adapt-schema schema))))))

(defmacro defc
  "Create a C function that can be used in other C functions."
  {:clj-kondo/lint-as 'schema.core/defn}
  [n ret-schema args & fn-tail]
  `(do (def ~n
         (-> (c-compile
               {:sym (quote ~n)
                :sym-meta ~(meta n)
                :ret-schema ~ret-schema
                :args (quote ~args)}
               (defn ~n
                 ~(with-meta (adapt-fn-args args)
                    (adapt-schema ret-schema))
                 ~@fn-tail))
             (with-meta {::c-function ~(str n)})))
       (var ~n)))

(defmacro defdsp
  "It's similar to `defc`, but it will send the plugin to SC (if it's
  turned on)."
  {:clj-kondo/lint-as 'schema.core/defn}
  [n ret-schema args & fn-tail]
  `(do (defc ~n ~ret-schema ~args ~@fn-tail)
       ~(when (resolve 'snd)
          `(snd "/cmd" "/vybe_dlopen" (:lib-full-path ~n) ~(str n)))
       (var ~n)))

(def myparam 0.3)

;; https://github.com/supercollider/example-plugins/blob/main/03-AnalogEcho/AnalogEcho.cpp
(vp/defcomp AnalogEcho
  ;; Create component based on `Unit` (inheritance).
  (->> [[:max_delay :float]
        [:buf_size :int]
        [:mask :int]
        [:buf [:* :float]]
        [:write_phase :int]
        [:s1 :float]]
       (vp/comp-merge Unit)))

(vp/defcomp VybeHooks
  [[:ctor :pointer]
   [:dtor :pointer]
   [:next :pointer]])

(defc mynext :void
  [unit :- [:* :void]
   n_samples :- :int]
  #_(let [[output] (.. unit mOutBuf)
          [input] (.. unit mInBuf)]
      (doseq [i (range n_samples)]
        (-> output
            (aset i (* (+ (-> input
                              (aget i)
                              (* 0.2))
                          #_(* (aget input (if (> i 10)
                                             (- i 9)
                                             i))
                               0.2))
                       0.4))))))

(defc ^:debug myctor :void
  [unit :- [:* AnalogEcho]
   _allocator :- [:* :void]]
  #_(merge ^:* unit
         {:calc_func #'mynext
          :max_delay (-> (.. unit in_buf) (aget 2) (aget 0))
          :buf_size (NEXTPOWEROFTWO
                     (* (.. unit rate sample_rate)
                        (.. unit max_delay)))
          :mask (- (.. unit buf_size) 1)
          :write_phase 0
          :s1 0
          ;; TODO
          #_ #_:buf (vybe_eita 10)}))

(defc ^:debug mydsp :void
  [unit :- [:* AnalogEcho]
   n_samples :- :int]
  (let [[input] (.. unit in_buf)
        [output] (.. unit out_buf)]
    (doseq [i (range n_samples)]
      (-> output
          (aset i (* (+ (-> input
                            (aget i)
                            (* 0.1))
                        #_(* (aget input (if (> i 10)
                                           (- i 9)
                                           i))
                             0.2))
                     myparam))))))

(defdsp ^:debug myplugin VybeHooks
  [_allocator :- [:* :void]]
  (VybeHooks {:ctor #'myctor
              :next #'mydsp}))

(comment
  #_ overtone.sc.machinery.server.connection/connection-info*

  (eee)
  (stop)

  ())

(comment

  (do (require '[vybe.audio :as va])
      (require '[overtone.live :refer :all :as l])
      (let [sc-spec' {:name "VybeSC",
                      :args [{:name "input"}
                             {:name "gain"}],
                      :rates #{:ar}
                      :default-rate :auto}
            sc-spec ((requiring-resolve 'overtone.sc.machinery.ugen.specs/decorate-ugen-spec) sc-spec')]
        ((requiring-resolve 'overtone.sc.machinery.ugen.fn-gen/def-ugen) *ns* sc-spec 0))

      (do (def lala
            {
             ;;Master Controls
             "/quit"               []
             "/notify"             [:zero-or-one]
             "/status"             []
             "/cmd"                [:cmd-name :anything*]
             "/dumpOSC"            [:zero-to-three]
             "/sync"               [:int]
             "/clearSched"         []
             "/error"              [:minus-two-to-one]

             ;;Synth Definition Commands
             "/d_recv"             [:bytes]
             "/d_load"             [:pathname]
             "/d_loadDir"          [:pathname]
             "/d_free"             [:synthdef-name]

             ;;Node Commands
             "/n_free"             [:node-id]
             "/n_run"              [:node-id :zero-or-one]
             "/n_set"              [:node-id :ALTERNATING-ctl-handle-THEN-ctl-val*]
             "/n_setn"             [:node-id :ctl-handle :count :ctl-val*]
             "/n_fill"             [:node-id :ctl-handle :count :ctl-val]
             "/n_map"              [:node-id :ctl-handle :ctl-bus-idx]
             "/n_mapn"             [:node-id :ctl-handle :ctl-bus-idx :count]
             "/n_mapa"             [:node-id :ctl-handle :ctl-bus-idx]
             "/n_mapan"            [:node-id :ctl-handle :ctl-bus-idx :count]
             "/n_before"           [:node-id :node-id]
             "/n_after"            [:node-id :node-id]
             "/n_query"            [:node-id]
             "/n_trace"            [:node-id]
             "/n_order"            [:zero-to-three :node-id :node-id]

             ;;Synth Commands
             "/s_new"              [:synthdef-name :synth-id :zero-to-four :node-id :ALTERNATING-ctl-handle-THEN-ctl-val*]
             "/s_get"              [:synth-id :ctl-handle*]
             "/s_getn"             [:synth-id :ctl-handle :count]
             "/s_noid"             [:synth-id]

             ;;Group Commands
             "/g_new"              [:group-id :zero-to-four :node-id]
             "/p_new"              [:group-id :zero-to-four :node-id]
             "/g_head"             [:group-id :node-id]
             "/g_tail"             [:group-id :node-id]
             "/g_freeAll"          [:group-id]
             "/g_deepFree"         [:group-id]
             "/g_dumpTree"         [:group-id :zero-or-one]
             "/g_queryTree"        [:group-id :zero-or-one]

             ;;Unit Generator Commands
             "/u_cmd"              [:node-id :ugen-idx :cmd-name :anything*]

             ;;Buffer Commands
             "/b_alloc"            [:buf-num :num-frames :count]
             "/b_allocRead"        [:buf-num :pathname :frame-start :num-frames :anything*]
             "/b_allocReadChannel" [:buf-num :pathname :frame-start :num-frames :chan-idx*]
             "/b_read"             [:buf-num :pathname :frame-start :num-frames :frame-start :zero-or-one]
             "/b_readChannel"      [:buf-num :pathname :frame-start :num-frames :frame-start :zero-or-one :chan-idx*]
             "/b_write"            [:buf-num :pathname :header-format :sample-format :num-frames :frame-start :zero-or-one]
             "/b_free"             [:buf-num]
             "/b_zero"             [:buf-num]
             "/b_set"              [:buf-num :sample-idx :sample-val]
             "/b_setn"             [:buf-num :sample-idx :count :sample-val*]
             "/b_fill"             [:buf-num :sample-idx :count :sample-val]
             "/b_gen"              [:buf-num :cmd-name :anything*]
             "/b_close"            [:buf-num]
             "/b_ query"            [:buf-num]
             "/b_get"              [:buf-num :sample-idx]
             "/b_getn"             [:buf-num :sample-idx :count]

             ;;Control Bus Commands
             "/c_set"              [:ctl-bus-idx :ctl-val]
             "/c_setn"             [:ctl-bus-idx :count :ctl-val*]
             "/c_fill"             [:ctl-bus-idx :count :ctl-val]
             "/c_get"              [:ctl-bus-idx]
             "/c_getn"             [:ctl-bus-idx :count]})

          (alter-var-root #'overtone.sc.machinery.server.osc-validator/OSC-TYPE-SIGNATURES (constantly lala)))

      (defn synth-ugen-indexes
        "Find all indexes that match a ugen. The parameter `ugen`
  can be a overtone ugen or its correspondent overtone or supercollider name."
        [synth ugen]
        (->> (:ugens (:sdef synth))
             (mapv vector (range))
             (filter (comp #{(if (string? ugen)
                               (overtone.helpers.lib/overtone-ugen-name ugen)
                               (overtone.helpers.lib/overtone-ugen-name (:name ugen)))}
                           overtone.helpers.lib/overtone-ugen-name
                           :name last))
             (mapv first)))

      #_(va/-shared)

      (defsynth eee
        [out_bus 0]
        (let [sig (-> #_(saw :freq (* 1400 (+ (* (sin-osc:kr :freq 0.7) 0.5)
                                              0.8)))
                      (+ (* (sin-osc :freq (* 3400 (+ (* (sin-osc:kr :freq 0.3) 0.5)
                                                      0.8)))
                            0.7)))]
          (out out_bus
               (-> #_(+ sig (* (delay-n sig :max-delay-time 1 :delay-time (+ (sin-osc:kr :freq 0.3) 0.5))
                               1))
                   (sin-osc 440)
                   (vybe-sc 0.9))))))

  (snd "/cmd" "/vybe_cmd" "/tmp_vybe100")

  (def sss (eee))

  (stop)

  (snd "/u_cmd" (:id sss)
       (first (synth-ugen-indexes eee vybe-sc))
       "/set_shared_memory_path"
       "olha")


  ;; --------------------------
  (demo 2.5 (-> (sin-osc :freq 440)
                (vybe-sc 0.9)))

  (demo 2.5 (-> (sin-osc :freq (* 440 (+ (sin-osc:kr :freq 0.2) 0.5)))
                (vybe-sc 0.9)))

  (demo 2.5 (-> (saw :freq 340)
                (vybe-sc 0.9)))

  (stop)

  ;; Restart server so we can load updated plugins.
  (overtone.sc.machinery.server.connection/shutdown-server)
  (boot-server)

  ())

#_^{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/comment

  (defsynth pad2 [freq 440 amp 0.4 amt 0.3 gate 1.0 out-bus 0]
    (let [vel        (+ 0.5 (* 0.5 amp))
          env        (env-gen (adsr 0.01 0.1 0.7 0.5) gate 1 0 1 FREE)
          f-env      (env-gen (perc 1 3))
          src        (saw [freq (* freq 1.01)])
          signal     (rlpf (* 0.3 src)
                           (+ (* 0.6 freq) (* f-env 2 freq)) 0.2)
          k          (/ (* 2 amt) (- 1 amt))
          distort    (/ (* (+ 1 k) signal) (+ 1 (* k (abs signal))))
          gate       (pulse (* 2 (+ 1 (sin-osc:kr 0.05))))
          compressor (compander distort gate 0.01 1 0.5 0.01 0.01)
          dampener   (+ 1 (* 0.5 (sin-osc:kr 0.5)))
          reverb     (free-verb compressor 0.5 0.5 dampener)
          echo       (comb-n reverb 0.4 0.3 0.5)]
      (out out-bus
           (* vel env echo))))

  (defsynth my-noise
    [freq 300, mul 0.5, out_bus 0]
    (out out_bus
         (* mul (lpf (pink-noise 0.8) 500))))
  #_(synth->data-rep my-noise)

  (comment

    (defsynth directionalss [in 10.0 out_bus 0.0 azim 0.0 elev 0.0 amp 1.0]
      (let [ug-b (in in)
            ug-c (* 0.70710677 ug-b)
            ug-d (dc 0.0)
            ug-e (dc 0.0)
            ug-g (foa-rotate ug-c ug-b ug-d ug-e in)
            ug-i (foa-tilt ug-g ug-g ug-g ug-g in)
            ug-j (* 0.70710677 ug-i)
            ug-k (* 0.70710677 ug-i)
            ug-l (* 0.28678823 ug-i)
            ug-m (* 0.28678823 ug-i)
            ug-n (* 0.40957603 ug-i)
            ug-o (sum3 ug-n ug-l ug-j)
            ug-p (* -0.40957603 ug-i)
            ug-q (sum3 ug-p ug-m ug-k)
            ug-s (* ug-o in)
            ug-t (* ug-q in)
            ug-u (out out_bus ug-s ug-t)]
        ug-u))

    mix

    (defsynth directionalss [in1 10.0 out_bus 0.0 azim 0.0 elev 0.0 amp 1.0]
      (let [ug-b (in in1)
            ug-c (* 0.70710677 ug-b)
            ug-d (dc 0.0)
            ug-e (dc 0.0)
            ug-g (foa-rotate ug-b ug-c ug-d ug-e)
            ug-u (out out_bus ug-g)]
        ug-u))

    (directionalss)

    (stop)

    ())


  (let [ugens (->> (:ugens (:sdef my-noise))
                   (mapv #(overtone.sc.machinery.ugen.sc-ugen/simplify-ugen %)))]
    (->> ugens
         (mapv (fn [{:keys [inputs name] :as m}]
                 [name inputs]))))

  #_(overtone.sc.machinery.synthdef/synthdef-decompile (:sdef my-noise))
  #_(overtone.sc.machinery.synthdef/synthdef-decompile (:sdef pad2))
  #_(overtone.sc.machinery.synthdef/synthdef-decompile
     (:sdef (synth-load "../vybe/resources/com/pfeodrippe/vybe/overtone/directional.scsyndef")))

  (defn synth->data-rep
    [synth]
    (clojure.walk/postwalk
     (fn [v]
       (let [ugen-name (:name v)]
         (if (and (map? v) ugen-name (:args v))
           (let [op-name (case ugen-name
                           "BinaryOpUGen" (get special-ops/REVERSE-BINARY-OPS (:special v))
                           "UnaryOpUGen" (get special-ops/REVERSE-UNARY-OPS (:special v))
                           ugen-name)]
             (if (seq (:args v))
               [op-name (:args v)]
               op-name))
           v)))
     (->> (:ugens (:sdef synth))
          (mapv #(overtone.sc.machinery.ugen.sc-ugen/simplify-ugen %))
          last)))

  (synth->data-rep my-noise)
  (synth->data-rep pad2)
  (synth->data-rep (synth-load "../vybe/resources/com/pfeodrippe/vybe/overtone/directional.scsyndef")))
