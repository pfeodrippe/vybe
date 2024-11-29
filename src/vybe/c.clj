(ns vybe.c
  (:require
   [babashka.process :as proc]
   [bling.core :as bling]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.pprint :as pp]
   [clojure.string :as str]
   [clojure.tools.analyzer.ast :as ast]
   [clojure.tools.analyzer.jvm :as ana]
   [clojure.walk :as walk]
   [vybe.panama :as vp]
   [potemkin :refer [defrecord+]])
  (:import
   (java.lang.foreign SymbolLookup)))

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

(vp/defcomp VybeHooks
  [[:ctor :pointer]
   [:dtor :pointer]
   [:next :pointer]])

(vp/defcomp VybeAllocator
  [[:alloc [:fn [:* :void]
            [:world [:* :void]]
            [:size :long]]]
   [:free [:fn :void
           [:world [:* :void]]
           [:ptr [:* :void]]]]])

(defn ->name
  [component-or-var]
  (let [var-str (if (vp/component? component-or-var)
                  (vp/comp-name component-or-var)
                  (str (symbol component-or-var)))]
    (-> var-str
        (str/replace #"\." "_")
        (str/replace #"/" "___")
        (str/replace #"-" "_"))))

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

(defn -adapt-type
  [type*]
  ;; Just count the number of nested pointer
  ;; schemas so we can put the same number of
  ;; `*`s and put the type* at the front (e.g. `float**`).
  (let [type* (schema-adapter type*)]
    (->> type*
         (walk/postwalk (fn [v]
                          (cond
                            (and (vector? v)
                                 (contains? #{:pointer :*}
                                            (first v)))
                            (str (last v) "*")

                            (contains? #{:pointer :*} v)
                            v

                            :else
                            (if-let [resolved (and (symbol? v)
                                                   (resolve v))]
                              (->name @resolved)
                              (name v))))))))
#_(-adapt-type [:* Rate])
#_(-adapt-type Rate)
#_(-adapt-type '[:* [:* :float]])
#_(-adapt-type '[:* [:* [:* Unit]]])
#_(-adapt-type '[:* [:* Unist]])

(defn- comp->c
  [component]
  (let [c-name (->name component)]
    (format "typedef struct %s {\n%s\n} %s;"
            c-name
            (->> (vp/comp-fields component)
                 (sort-by (comp :idx last))
                 (mapv (fn [[k type]]
                         (if (and (vector? type)
                                  (= (first type) :fn))
                           (let [[_ ret & args] type]
                             (format "  %s (*%s)(%s);"
                                     (-adapt-type ret)
                                     (name k)
                                     (->> (mapv (fn [[k schema]]
                                                  (str (-adapt-type schema) " " (name k)))
                                                args)
                                          (str/join ", "))))
                           (str "  " (cond
                                       (and (vector? type)
                                            (= (first type) :pointer))
                                       (-adapt-type type)

                                       (= type :pointer)
                                       "void*"

                                       :else
                                       (name type))
                                " " (name k) ";"))))
                 (str/join "\n"))
            c-name)))
#_ (println (comp->c VybeAllocator))
#_ (println (comp->c Unit))

(def -clz-h
  "From clz.h in SC."
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
#include <stdlib.h>
#include <string.h>
#include <math.h>

//#include <signal.h>
//#include <unistd.h>

//#include <setjmp.h>
#include <stdio.h>

#if (defined(_MSC_VER) || defined(__MINGW32__) || defined(_WIN32))
  #define VYBE_EXPORT __declspec(dllexport)
#else
  #define VYBE_EXPORT __attribute__((__visibility__(\"default\")))
#endif

#define member_size(type, member) (sizeof( ((type *){})->member ))

static inline double __attribute__((overloadable)) vybe_abs(double x) {
   return fabs(x);
}

static inline float __attribute__((overloadable)) vybe_abs(float x) {
   return fabsf(x);
}
static inline int __attribute__((overloadable)) vybe_abs(int x) {
   return abs(x);
}

/*
jmp_buf buf;

void do_stuff(void) {
	printf(\"Executing before longjmp! \");
	longjmp(buf, 1);
	printf(\"This part of the code will never be executed!\");
}

void sighandler(int signo) {
   longjmp(buf, 1);
}
*/


// From SC_SndBuf.h
static inline float cubicinterp(float x, float y0, float y1, float y2, float y3) {
    // 4-point, 3rd-order Hermite (x-form)
    float c0 = y1;
    float c1 = 0.5f * (y2 - y0);
    float c2 = y0 - 2.5f * y1 + 2.f * y2 - 0.5f * y3;
    float c3 = 0.5f * (y3 - y0) + 1.5f * (y1 - y2);

    return ((c3 * x + c2) * x + c1) * x + c0;
}

// From SC_InlineUnaryOp.h
/*
 * Zap dangerous values (subnormals, infinities, nans) in feedback loops to zero.
 * Prevents pathological math operations in ugens and can be used at the end of a
 * block to fix any recirculating filter values.
 */
static inline float zapgremlins(float x) {
    float absx = vybe_abs(x);
    // very small numbers fail the first test, eliminating denormalized numbers
    //    (zero also fails the first test, but that is OK since it returns zero.)
    // very large numbers fail the second test, eliminating infinities
    // Not-a-Numbers fail both tests and are eliminated.
    return (absx > (float)1e-15 && absx < (float)1e15) ? x : (float)0.;
}


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

#_(def c-emit nil)
(defmulti c-emit
  "Handle custom invocations from C code for a var.

  It receives a tools.analyzer node and dispatches on the var,
  see example below.

  E.g.

    (defmethod c-emit (declare NEXTPOWEROFTWO)
     [{:keys [args]}]
     (str \"NEXTPOWEROFTWO(\"
          (-transpile (first args))
          \")\"))"
  (fn [node]
    (let [v (:var (:fn node))]
      (if (vp/component? @v)
        `vp/component
        v))))

(def ^:dynamic *transpilation* {:trace-history ()})

(defn -transpile
  "See https://clojure.github.io/tools.analyzer.jvm/spec/quickref.html"
  [{:keys [op] :as v}]
  (binding [*transpilation* (update *transpilation*
                                    :trace-history conj
                                    (let [v* (select-keys v [:op :form :fn :var])]
                                      (cond-> v*
                                        (:fn v*)
                                        (update :fn select-keys [:var]))))]
    #_(pp/pprint {:transpilation *transpilation*})
    (try
      (str (let [{:keys [line column]} (and (not (or (:no-source-mapping *transpile-opts*)
                                                     (:nosm *transpile-opts*)))
                                            (or (meta (:form v))
                                                (meta (first (:raw-forms v)))))]
             (when (and line column)
               (format "\n#line %s %s %s\n"
                       line
                       (pr-str (str "VYBE_CLJ:" *file* ":" *ns* ":" column))
                       {:op op})))
           (case op
             :def
             (let [{:keys [name init]} v]
               (cond
                 (= (:op (:expr init)) :fn)
                 (let [{:keys [params body]} (first (:methods (:expr init)))
                       schema (::schema (meta (first (:form (first (:methods (:expr init)))))))
                       schema (if-let [s (and (symbol? schema)
                                              (resolve schema))]
                                @s
                                schema)
                       return-tag (or (some-> (::schema (meta (first (:form (first (:methods (:expr init)))))))
                                              -adapt-type
                                              ->name)
                                      (some-> ^Class (:return-tag (:expr init))
                                              (.getName)))]
                   (str "VYBE_EXPORT "  return-tag " "
                        (->name (resolve name))
                        (->> params
                             (mapv (fn [{:keys [tag form]}]
                                     (str (case (.getName ^Class tag)
                                            "[F"
                                            "float*"

                                            "java.lang.Object"
                                            (let [schema (::schema (meta form))]
                                              (-adapt-type schema))

                                            tag)
                                          " " form)))
                             (str/join ", ")
                             parens)
                        " {\n"

                        #_(cond
                          (vp/component? schema)
                          (format "
if (setjmp(buf)) {return (%s){};};
signal(SIGSEGV, sighandler);
"
                                  return-tag)

                          (str/includes? return-tag "*")
                          "
if (setjmp(buf)) return NULL;
signal(SIGSEGV, sighandler);
"
                          (= return-tag "void")
                          ""

                          :else
                          "
if (setjmp(buf)) return -1;
signal(SIGSEGV, sighandler);
")


                        ;; Add `return` only to the last expression (if applicable).
                        (if (= return-tag "void")
                          (-transpile body)
                          (str "\nreturn\n ({" (-transpile body) ";})")
                          #_(let [expressions (str/split (-transpile body) #";")]
                              (str (str/join ";" (drop-last expressions)) ";\n"
                                   ;; Finally, our return expression.
                                   "\nreturn\n" (last expressions))))
                        ";"
                        "\n}"))

                 ;; Else, we have static variables.
                 :else
                 (format "static %s %s = (%s){};"
                         (or (some-> (::schema (meta (:var v)))
                                     -adapt-type
                                     ->name)
                             (throw (ex-info "Var or schema not found for static variable"
                                             {:var (:var v)
                                              :meta (meta (:var v))
                                              :type (some-> (::schema (meta (:var v))) -adapt-type)
                                              :op (:op v)
                                              :raw-forms (:raw-forms v)
                                              :form (:form v)})))
                         (->name (:var v))
                         (some-> (::schema (meta (:var v)))
                                 -adapt-type
                                 ->name))))

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
                   (add multiply divide minus gt gte lt lte)
                   (->> args
                        (mapv -transpile)
                        (str/join (format " %s "
                                          ('{multiply *
                                             divide /
                                             add +
                                             minus -
                                             gt > gte >= lt < lte <=}
                                           (->sym method))))
                        parens)

                   inc
                   (format "(%s + 1)" (-transpile (first args)))

                   dec
                   (format "(%s - 1)" (-transpile (first args)))

                   abs
                   (format "vybe_abs(%s)" (-transpile (first args)))

                   ;; bit-and
                   and
                   (apply format "(%s & %s)" (mapv -transpile args))

                   or
                   (apply format "(%s | %s)" (mapv -transpile args)) )

                 clojure.lang.RT
                 (case (->sym method)
                   aset
                   (let [[s1 s2 s3] (mapv -transpile args)]
                     (-> (->> (format " %s[%s] = %s"
                                      s1 s2 s3)
                              parens)
                         (str ";")))

                   (nth aget)
                   (let [[s1 s2] (mapv -transpile args)]
                     (->> (format " %s[%s] "
                                  s1 s2)
                          parens))

                   get
                   (let [[s1 s2] (mapv -transpile args)]
                     (format "%s.%s" s1 s2))

                   intCast
                   (format "(int)%s" (-transpile (first args)))

                   floatCast
                   (format "(float)%s" (-transpile (first args)))

                   doubleCast
                   (format "(double)%s" (-transpile (first args))))))

             :keyword-invoke
             (let [{:keys [keyword target]} v]
               (format "%s.%s"
                       (-transpile target)
                       (-transpile keyword)))

             :local
             (let [{:keys [form]} v]
               form)

             :do
             (let [{:keys [statements ret]} v]
               (->> (concat statements [ret])
                    (mapv -transpile)
                    (str/join ";\n\n")))

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
                   ;; Find the `doseq` resolved op.
                   raw-form (->> raw-forms
                                 (filter #(some-> %
                                                  meta
                                                  :clojure.tools.analyzer/resolved-op
                                                  symbol
                                                  #{`doseq}))
                                 first)
                   {:keys [clojure.tools.analyzer/resolved-op]} (meta raw-form)]
               (case (some-> resolved-op symbol)
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
             (if-let [var* (:var (:fn v))]
               (case (symbol var*)
                 (clojure.core/* clojure.core/+)
                 (if (= (count (:args v)) 1)
                   (-transpile (first (:args v)))
                   (throw (ex-info "Unsupported" {:op (:op v)
                                                  :form (:form v)
                                                  :args v})))

                 (c-emit v))
               (format "(%s)(%s)"
                       (-transpile (:fn v))
                       (->> (:args v)
                            (mapv -transpile)
                            (str/join ", "))))

             :var
             (let [my-var (:var v)
                   var-value @my-var
                   c-fn (::c-function (meta var-value))]
               (cond
                 c-fn
                 c-fn

                 (::schema (meta (:var v)))
                 (->name (:var v))

                 (vp/component? var-value)
                 (->name (vp/comp-name var-value))

                 :else
                 (-transpile (ana/analyze var-value))))

             :the-var
             (let [my-var (:var v)
                   var-value @my-var]
               (format "&%s"
                       (if-let [c-fn (::c-function (meta var-value))]
                         c-fn
                         var-value)))

             :set!
             (format "%s = %s;"
                     (-transpile (:target v))
                     (-transpile (:val v)))

             :host-interop
             (format "%s.%s"
                     (-transpile (:target v))
                     (:m-or-f v))

             :let
             (let [locals (:locals (:env v))]
               (format "%s\n%s"
                       (->> (:bindings v)
                            (reduce (fn [{:keys [env-symbols] :as acc}
                                         {:keys [form init]}]
                                      (let [existing? (get env-symbols form)
                                            parsed
                                            (format "%s%s = %s;"
                                                    ;; Don't redefine.
                                                    (if existing?
                                                      ""
                                                      "__auto_type ")
                                                    form
                                                    (-transpile init))]
                                        (-> acc
                                            (update :env-symbols conj form)
                                            (update :collector conj parsed))))
                                    {:env-symbols (set (keys locals))
                                     :collector []})
                            :collector
                            (str/join "\n"))
                       (or (-transpile (:body v))
                           "")))

             nil
             (throw (ex-info (str "Unhandled `nil`: " v " (" (type v) ")")
                             {:v v
                              :v-type (type v)}))

             (do #_(def v v) #_ (:op v) #_ (keys v)
                 (throw (ex-info (str "Unhandled: " (:op v))
                                 {:op (:op v)
                                  :raw-forms (:raw-forms v)
                                  :form (:form v)})))))
      (catch Exception e
        (when (::transpiler-error? (ex-data e))
          (throw e))

        (let [error-map (merge {:ns *ns*
                                :transpilation *transpilation*
                                :raw-forms (:raw-forms v)
                                :form (:form v)
                                ::transpiler-error? true
                                :exception e}
                               (ex-data e)
                               (select-keys v [:var]))]

          (println (bling/callout {:label "Error when transpiling to C"
                                   :type :error}
                                  (with-out-str
                                    (pp/pprint error-map))))
          (throw (ex-info "Error when transpiling to C" error-map)))))))

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
                                             (swap! *var-collector update :c-fns concat
                                                    ;; Remove `do` by using `rest`.
                                                    (rest (:code-form @(:var v))))

                                             (::schema (meta (:var v)))
                                             (do
                                               (swap! *schema-collector conj (::schema (meta (:var v))))
                                               (swap! *var-collector update :c-fns conj
                                                      `(def ~(with-meta (:form v)
                                                               {::schema (-adapt-type
                                                                          (::schema (meta (:var v))))}))))

                                             (vp/component? @(:var v))
                                             (swap! *schema-collector conj @(:var v))

                                             :else
                                             (swap! *var-collector update :non-c-fns conj @(:var v))))
                                         v)))
           _ (prewalk-form code-form)
           {:keys [c-fns]} @*var-collector
           final-form (distinct (concat ['do] (distinct c-fns) [code-form]))

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
                      schemas-c-code
                      (-transpile (ana/analyze final-form))]
                     (str/join "\n\n"))
        :final-form final-form
        :schemas (distinct @*schema-collector)
        :form-hash (abs (hash [-common-c
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

(defonce ^:private *debug-counter (atom 0))

(defn -c-compile
  ([code-form]
   (-c-compile code-form {}))
  ([code-form {:keys [sym-meta sym sym-name] :as opts}]
   (let [path-prefix (str (System/getProperty "user.dir") "/resources/vybe/dynamic")
         {:keys [c-code form-hash schemas final-form]} (-> code-form (transpile opts))
         obj-name (str "vybe_" sym-name "_"
                       (when (or (:no-cache sym-meta)
                                 (:debug sym-meta))
                         (str (swap! *debug-counter inc) "_"))
                       form-hash)
         lib-name (System/mapLibraryName obj-name)
         lib-full-path (str path-prefix "/" lib-name)
         file (io/file lib-full-path)
         generated-c-file-path (str ".vybe/c/" obj-name ".c")]
     (if (and (not (or (:no-cache sym-meta)
                       (:debug sym-meta)))
              (.exists file))
       {:lib-full-path lib-full-path
        :code-form final-form
        :schemas schemas}

       (do (io/make-parents file)
           (io/make-parents (io/file generated-c-file-path))
           (spit generated-c-file-path c-code)

           (when (:debug sym-meta)
             (println c-code))

           ;; Using clang, we will analyze the code and then, if no errors,
           ;; try to compile it.
           (let [{:keys [err]} (proc/sh (->> ["clang"
                                              "--analyze"
                                              ;; https://stackoverflow.com/questions/19863242/static-analyser-issues-with-command-line-tools
                                              "-Xanalyzer -analyzer-disable-checker -Xanalyzer deadcode.DeadStores"
                                              "-fdiagnostics-print-source-range-info"
                                              "-fcolor-diagnostics"
                                              generated-c-file-path]
                                             (str/join " ")))

                 {:keys [err analyzer-err]}
                 (if (seq err)
                   {:err err
                    :analyzer-err (->> (str/split-lines (remove-ansi err))
                                       (mapv (comp second #(str/split % #"#line")))
                                       (remove nil?)
                                       (mapv str/trim)
                                       (mapv #(str/replace % "\"" ""))
                                       (take 1)
                                       (mapv (fn [s]
                                               (let [[line & other] (str/split s #" ")
                                                     [_ file-path -ns column]
                                                     (str/split (first other) #":")]
                                                 (->> [file-path -ns column line
                                                       (first (str/split-lines
                                                               (remove-ansi err)))]
                                                      (str/join ":"))))))}
                   (proc/sh (format
                             (->> ["clang"
                                   "-fdiagnostics-print-source-range-info"
                                   "-fcolor-diagnostics"
                                   "-shared"
                                   (when vp/linux?
                                     "-fPIC")
                                   generated-c-file-path
                                   " -o %s"]
                                  (remove nil?)
                                  (str/join " "))
                             lib-full-path)))]
             (when (seq err)
               ;; Println c code only if we haven't printed it before.
               (when-not (:debug sym-meta)
                 (println c-code))

               (let [errors (->> (or (seq (str/split (remove-ansi err) #"VYBE_CLJ:"))
                                     (seq analyzer-err))
                                 (filter seq)
                                 (mapv (fn [s]
                                         (let [[file-path -ns column line _c-line
                                                & error-str]
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
                            (mapv (fn [{:keys [file-path file-line _-ns
                                               line column error]}]
                                    (error-callout
                                     error
                                     {:point-of-interest-opts
                                      {:type   :error
                                       :file   file-path
                                       :line   line
                                       :column column
                                       :form   (try (edn/read-string
                                                     (subs file-line (dec column)))
                                                    (catch Exception _
                                                      file-line))}
                                      :callout-opts {:type :error}})))
                            (str/join "\n\n"))]
                 (println (bling/callout {:label (format "C %s error"
                                                         (if analyzer-err
                                                           "ANALYZER"
                                                           "COMPILE"))
                                          :type :error}
                                         err))
                 (throw (ex-info clj-error
                                 {:error-lines errors
                                  :error (str/split-lines (remove-ansi err))
                                  :code-form final-form})))))
           {:lib-full-path lib-full-path
            :code-form final-form
            :schemas schemas})))))

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

(defn -fn-downcall-builder
  [c-defn]
  (let [{:keys [lib-full-path]} c-defn
        lib (SymbolLookup/libraryLookup lib-full-path (vp/default-arena))
        f-mem (-> (.find lib (::c-function (meta c-defn))) .get)
        handle (vp/downcall-handle (:fn-desc c-defn))]
    (fn [& args]
      (apply handle f-mem args))))

(defrecord+ VybeDefn [fn-downcall]
  clojure.lang.IFn
  (invoke [_] (fn-downcall))
  (invoke [_ a1] (fn-downcall a1))
  (invoke [_ a1 a2] (fn-downcall a1 a2))
  (invoke [_ a1 a2 a3] (fn-downcall a1 a2 a3))
  (invoke [_ a1 a2 a3 a4] (fn-downcall a1 a2 a3 a4))
  (invoke [_ a1 a2 a3 a4 a5] (fn-downcall a1 a2 a3 a4 a5))
  (invoke [_ a1 a2 a3 a4 a5 a6] (fn-downcall a1 a2 a3 a4 a5 a6))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7] (fn-downcall a1 a2 a3 a4 a5 a6 a7))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7 a8] (fn-downcall a1 a2 a3 a4 a5 a6 a7 a8))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7 a8 a9] (fn-downcall a1 a2 a3 a4 a5 a6 a7 a8 a9))
  (invoke [_ a1 a2 a3 a4 a5 a6 a7 a8 a9 a10] (fn-downcall a1 a2 a3 a4 a5 a6 a7 a8 a9 a10)))
#_ defrecord

(defmacro defn*
  "Create a C function that can be used in other C functions."
  {:clj-kondo/lint-as 'schema.core/defn}
  [n _ ret-schema args & fn-tail]
  `(let [args-desc# (quote ~(->> args
                                 (partition-all 3 3)
                                 (mapv (fn [[sym _ schema]]
                                         [sym schema]))))]
     (def ~n
       (let [v# (-> (c-compile
                      {:sym (quote ~n)
                       :sym-name ~(->name (symbol (str *ns*) (str n)))
                       :sym-meta ~(meta n)
                       :ret-schema ~ret-schema
                       :args (quote ~args)}
                      (defn ~n
                        ~(with-meta (adapt-fn-args args)
                           (adapt-schema ret-schema))
                        ~@fn-tail))
                    (with-meta {::c-function ~(->name (symbol (str *ns*) (str n)))})
                    (merge {:fn-desc (into [:fn ~ret-schema] args-desc#)}))]
         (-> (map->VybeDefn (merge v# {:fn-downcall (-fn-downcall-builder v#)}))
             ;; TODO We could put this in the returned map instead of in the metadata.
             (with-meta {::c-function ~(->name (symbol (str *ns*) (str n)))}))))
     (alter-meta! (var ~n) merge
                  {:arglists (list args-desc#)
                   :doc (cond-> (format "Returns %s" (if (vp/component? ~ret-schema)
                                                       (vp/comp-name ~ret-schema)
                                                       (quote ~ret-schema)))
                          (:doc (meta (var ~n)))
                          (str "\n\n" (:doc (meta (var ~n)))))})
     (var ~n)))
#_ (vybe.c/defn* ^:debug eita :- :int
     [a :- :int]
     (+ a 4550))
#_ (eita 55)

;; Libs.
(def stdlib-h
  {:malloc
   {:type :function
    :symbol "malloc"
    :args [{:symbol "size" :schema :long}]
    :ret {:schema [:* :void]}}

   :calloc
   {:type :function
    :symbol "calloc"
    :args [{:symbol "count" :schema :long}
           {:symbol "size" :schema :long}]
    :ret {:schema [:* :void]}}

   :free
   {:type :function
    :symbol "free"
    :args [{:symbol "mem" :schema [:* [:void]]}]
    :ret {:schema :void}}})

;; c-emit methods.
(defn -invoke
  ([node]
   (-invoke node {}))
  ([{:keys [args] :as node} {:keys [sym]}]
   (let [v (:var (:fn node))]
     (str (or (some-> sym name)
              (if (:no-ns (meta v))
                (name (symbol v))
                (->name v)))
          "("
          (->> args
               (mapv -transpile)
               (str/join ", "))
          ")"))))

(defmethod c-emit :default
  [node]
  (-invoke node))

(declare ^:no-ns NEXTPOWEROFTWO
         ^:no-ns cubicinterp
         ^:no-ns zapgremlins)
(declare ^:no-ns malloc
         ^:no-ns calloc
         ^:no-ns free)

;; -- Special case for a VybeComponent invocation.
(defmethod c-emit `vp/component
  [{:keys [args] :as node}]
  (let [v (:var (:fn node))]
    (str  "(" (->name v) ")"
          (or (some-> (first args) -transpile)
              "{}"))))

;; -- Clojure core.
(defmethod c-emit #'reset!
  [{:keys [args]}]
  (let [[*atom newval] args]
    (format "%s = %s;"
            (-transpile *atom)
            (-transpile newval))))

(defmethod c-emit #'deref
  [{:keys [args]}]
  (format "(*%s)" (-transpile (first args))))

(defmethod c-emit #'merge
  [{:keys [args]}]
  (let [[target] args]
    (->> (rest args)
         (mapcat (fn [{:keys [op] :as params}]
                   (let [kvs (case op
                               :map
                               (mapv vector
                                     (mapv (comp :form) (:keys params))
                                     (mapv -transpile (:vals params)))

                               :const (case (:type params)
                                        :map
                                        (:val params)))]
                     (->> kvs
                          (mapv (fn [[k v]]
                                  (str (-transpile target)
                                       "."
                                       (name k)
                                       " = "
                                       v
                                       ";")))))))
         (str/join "\n"))))

;; -- Others.
(defmethod c-emit #'vp/address
  [{:keys [args]}]
  (format "&%s" (-transpile (first args))))

(defmethod c-emit #'vp/sizeof
  [{:keys [args]}]
  (if (= (count args) 2)
    ;; Arity of 2 means we want to know the size of a struct field.
    (format "member_size(%s, %s)"
            (-transpile (first args))
            (-transpile (second args)))
    (format "sizeof(%s)" (-transpile (first args)))))

(defmethod c-emit #'vp/new*
  [{:keys [args]}]
  (let [[params c-sym] (mapv -transpile args)]
    (if params
      ;; We use a "statement expression" to have
      ;; the struct initialized.
      (format "({
__auto_type _my_v = (%s*)malloc(sizeof (%s));
%s
_my_v;
})"
              c-sym
              c-sym
              (-transpile (ana/analyze `(merge ~'@_my_v
                                               ~(:form (first args)))
                                       (-> (:env (first args))
                                           (update :locals assoc '_my_v {})))))
      (format "((%s*)malloc(sizeof (%s)))" c-sym c-sym))))

(defmethod c-emit #'vp/zero!
  [node]
  (-invoke node {:sym "memset"}))
