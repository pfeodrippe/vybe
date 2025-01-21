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
   [clojure.tools.analyzer :as ana-]
   [clojure.walk :as walk]
   [vybe.panama :as vp]
   [clojure.core.protocols :as core-p]
   [potemkin :refer [defrecord+]]
   [vybe.util :as vy.u]
   [clojure.tools.analyzer
    [utils :refer [ctx resolve-sym -source-info resolve-ns obj? dissoc-env butlast+last mmerge]]
    [ast :refer [walk prewalk postwalk] :as ast]
    [env :as env :refer [*env*]]
    [passes :refer [schedule]]])
  (:import
   (java.lang.foreign SymbolLookup)
   (vybe.panama VybeComponent)))

(set! *warn-on-reflection* true)

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
  (try
    (let [var-str (cond
                    (vp/component? component-or-var)
                    (name component-or-var)

                    (:no-ns (meta component-or-var))
                    (name (symbol component-or-var))

                    :else
                    (str (symbol component-or-var)))]
      (-> var-str
          (str/replace #"\." "_DOT_")
          (str/replace #"/" "_SLASH_")
          (str/replace #"-" "_DASH_")))
    (catch Exception e
      (throw (ex-info "Error in ->name" {:component-or-var component-or-var} e)))))

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
    (symbol (->name v))

    :else
    v))

(defn -adapt-type
  [type*]
  ;; Just count the number of nested pointer
  ;; schemas so we can put the same number of
  ;; `*`s and put the type* at the front (e.g. `float**`).
  (let [type* (schema-adapter type*)]
    (cond
      (contains? #{:pointer :*} type*)
      "void*"

      (contains? #{:string} type*)
      "char*"

      :else
      (->> type*
           (walk/prewalk (fn prewalk [v]
                           (cond
                             (and (vector? v)
                                  (contains? #{:vec}
                                             (first v)))
                             (str (prewalk (last v))
                                  "[]")

                             (and (vector? v)
                                  (contains? #{:pointer :*}
                                             (first v)))
                             (let [[_ metadata _]
                                   (if (> (count v) 2)
                                     ;; Then we have some metadata.
                                     v
                                     [(first v) nil (last v)])

                                   {:keys [const]} metadata]
                               (str (when const "const ")
                                    (prewalk (last v))
                                    "*"))

                             (contains? #{:pointer :*} v)
                             v

                             (contains? #{:long-long} v)
                             "long long"

                             (contains? #{:long} v)
                             "int64"

                             (and (sequential? v)
                                  (= (first v) `typeof))
                             (format "typeof(%s)" (->name (second v)))

                             :else
                             (if-let [resolved (and (symbol? v)
                                                    (resolve v))]
                               (->name @resolved)
                               (try
                                 (->name v)
                                 (catch Exception e
                                   (throw (ex-info "Error when calling `->name`"
                                                   {:v v
                                                    :type type*}
                                                   e))))))))))))
#_(-adapt-type [:* `(typeof ~'eita--)])
#_(-adapt-type :long-long)
#_(-adapt-type [:vec Rate])
#_(-adapt-type [:* {:const true} Rate])
#_(-adapt-type [:* Rate])
#_(-adapt-type :pointer)
#_(-adapt-type :string)
#_(-adapt-type Rate)
#_(-adapt-type '[:* [:* :float]])
#_(-adapt-type '[:* [:* [:* Unit]]])
#_(-adapt-type '[:* [:* Unist]])

(defn -adapt-fn-desc
  [fn-desc]
  (let [{:keys [ret args]} (vp/fn-descriptor->map fn-desc)]
    (format "%s (*)(%s)"
            (-adapt-type (:schema ret))
            (->> (mapv (fn [{:keys [symbol schema]}]
                         (str (-adapt-type schema) " " (->name symbol)))
                       args)
                 (str/join ", ")))))
#_ (-adapt-fn-desc
    [:fn [:pointer :void]
     [:world [:pointer :void]]
     [:size-f :long]])

(defn- -collect-fn-desc-components
  [fn-desc]
  (let [{:keys [ret args]} (vp/fn-descriptor->map fn-desc)
        *collector (atom [(:schema ret)])]
    (mapv (fn [{:keys [schema]}]
            (swap! *collector conj schema))
          args)

    (let [*coll-2 (atom [])
          walk (fn walk [m]
                 (walk/prewalk (fn [v]
                                 (when (and (vp/component? v)
                                            (resolve (symbol (vp/comp-name v))))
                                   (swap! *coll-2 conj v)
                                   (walk (->> (VybeComponent/.fields v)
                                              vals
                                              (mapv :type)
                                              (remove keyword?))))
                                 v)
                               m))]
      (walk (distinct @*collector))
      (distinct @*coll-2))))
#_ (-collect-fn-desc-components
    [:fn [:pointer :void]
     [:world [:pointer :void]]
     [:size :long]])
#_ (-collect-fn-desc-components
    [:fn [:pointer :void]
     [:world [:* vybe.type/Vector2]]
     [:size [:* [:* vybe.type/Vector3]]]
     [:bb [:* [:* Unit]]]])

(defn- comp->c
  ([component]
   (comp->c component {}))
  ([component {:keys [level embedded]
               :or {level 0
                    embedded false}
               :as opts}]
   (let [c-name (->name component)
         opts (assoc opts :level level)
         nesting (str/join "" (repeat (* (inc level) 2) \space))]
     (format "%sstruct %s{\n%s\n%s}%s"
             (if embedded
               ""
               "typedef ")
             (if embedded
               ""
               (str c-name " "))
             (->> (vp/comp-fields component)
                  (sort-by (comp :idx last))
                  (mapv (fn [[k type]]
                          (if (vp/fn-descriptor? type)
                            (let [{:keys [ret args]} (vp/fn-descriptor->map type)]
                              (format "%s (*%s)(%s);"
                                      (-adapt-type (:schema ret))
                                      (name k)
                                      (->> (mapv (fn [{:keys [symbol schema]}]
                                                   (str (-adapt-type schema) " " (->name symbol)))
                                                 args)
                                           (str/join ", "))))
                            (cond
                              (and (vector? type)
                                   (= (first type) :vec))
                              (let [[_ {:keys [size]} vec-type] type]
                                (format "%s %s[%s];"
                                        (if (vp/component? vec-type)
                                          (comp->c vec-type (merge (update opts :level inc)
                                                                   {:embedded true}))
                                          (-adapt-type vec-type))
                                        (name k)
                                        size))

                              :else
                              (str (cond
                                     (and (vector? type)
                                          (= (first type) :pointer))
                                     (-adapt-type type)

                                     (= type :pointer)
                                     "void*"

                                     (= type :string)
                                     "char*"

                                     (= type :long-long)
                                     "long long"

                                     (= type :long)
                                     "int64"

                                     :else
                                     (try
                                       (if (vp/component? type)
                                         (comp->c type (merge (update opts :level inc)
                                                              {:embedded true}))
                                         (name type))
                                       #_(name type)
                                       (catch Exception e
                                         (throw (ex-info "Error on `comp->c` when getting name"
                                                         {:k k
                                                          :type type
                                                          :component component}
                                                         e)))))
                                   " " (name k) ";")))))
                  (mapv #(str nesting %))
                  (str/join "\n"))
             (if embedded
               (str/join "" (repeat (* level 2) \space))
               "")
             (if embedded
               ""
               (str " " c-name ";"))))))
#_ (println (comp->c vybe.flecs/system_desc_t))
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

typedef bool boolean;
typedef char byte;

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

//#include <dlfcn.h>

//#include <signal.h>
//#include <unistd.h>

//#include <setjmp.h>
#include <stdio.h>

#if (defined(_MSC_VER) || defined(__MINGW32__) || defined(_WIN32))
  #define VYBE_EXPORT __declspec(dllexport)
#else
  #define VYBE_EXPORT __attribute__((__visibility__(\"default\")))
#endif

#define _member_size(type, member) (sizeof( ((type *){})->member ))
#define _member_alignof(type, member) (_Alignof( ((type *){})->member ))

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

//typedef void* (*ecs_init__type)(void);
//__auto_type ecs_init = (void* (*)(void)) 0;

// FIXME Support windows.
//static void eee() {
//__auto_type h = dlopen(\"/Users/pfeodrippe/dev/vybe-games/vybe_native/libvybe_flecs.dylib\", RTLD_LAZY|RTLD_GLOBAL);
//if (!h) return;
//}
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

;; ------------- Multimethods
(defn- -c-invoke
  [node]
  (when-let [v (:var (:fn node))]
    (if (vp/component? @v)
      `vp/component
      v)))

(defmulti c-invoke
  "Handle custom invocations from C code for a var.

  It receives a tools.analyzer node and dispatches on the var,
  see example below.

  E.g.

    (defmethod c-invoke (declare NEXTPOWEROFTWO)
     [{:keys [args]}]
     (str \"NEXTPOWEROFTWO(\"
          (emit (first args))
          \")\"))"
  -c-invoke)

(defn- -c-replace
  [node]
  (let [v (:var node)]
    v))

(defmulti c-replace
  "Replace vars when not invoking them as functions."
  -c-replace)

(defmulti c-macroexpand
  "Called during the analyze process.

  It should return a clojure form."
  (fn [node]
    (let [v (:var node)]
      v)))

(declare emit)

(defn -invoke
  ([node]
   (-invoke node {}))
  ([{:keys [args] :as node} {:keys [sym]}]
   (let [v (:var (:fn node))]
     (str (or (some-> sym name)
              (->name v))
          "("
          (->> args
               (mapv emit)
               (str/join ", "))
          ")"))))

(def ^:dynamic *transpilation* {:trace-history ()})

(defn- -macroexpand-1
  "If form represents a macro form or an inlineable function,returns its expansion,
   else returns form.

  Modified from `clojure.tools.analyzer.jvm`."
  ([form] (-macroexpand-1 form (ana/empty-env)))
  ([form env]
   (env/ensure (ana/global-env)
               (cond

                 (seq? form)
                 (let [[op & args] form]
                   (if (ana/specials op)
                     form
                     (let [v (resolve-sym op env)
                           m (meta v)
                           local? (-> env :locals (get op))
                           macro? (and (not local?) (:macro m)
                                       ;; <<< WE MODIFIED HERE >>>
                                       (not (:vybe/fn-meta m))
                                       (not (get-method c-invoke v))
                                       (not (get-method c-replace v))
                                       (not (get-method c-macroexpand v)))
                           inline-arities-f (:inline-arities m)
                           inline? (and (not local?)
                                        (or (not inline-arities-f)
                                            (inline-arities-f (count args)))
                                        (:inline m))
                           t (:tag m)]
                       (cond
                         ;; <<< WE MODIFIED HERE >>>
                         (get-method c-macroexpand v)
                         (c-macroexpand {:var v
                                         :form form
                                         :args (rest form)
                                         :env env})

                         macro?
                         (let [res (apply v form (:locals env) (rest form))] ; (m &form &env & args)
                           (when-not (ana/ns-safe-macro v)
                             (ana/update-ns-map!))
                           (if (obj? res)
                             (vary-meta res merge (meta form))
                             res))

                         inline?
                         (let [res (apply inline? args)]
                           (ana/update-ns-map!)
                           (if (obj? res)
                             (vary-meta res merge
                                        (and t {:tag t})
                                        (meta form))
                             res))

                         :else
                         (ana/desugar-host-expr form env)))))

                 (symbol? form)
                 (ana/desugar-symbol form env)

                 :else
                 form))))

(defn analyze
  "Analyze form."
  ([form]
   (analyze form (ana/empty-env) {}))
  ([form env]
   (analyze form (or env (ana/empty-env)) {}))
  ([form env opts]
   (try
     (ana/analyze form
                  (or env (ana/empty-env))
                  (merge {:bindings {#'ana-/macroexpand-1 -macroexpand-1}}
                         opts))
     (catch Exception ex
       (let [data {:form form
                   :env env
                   :opts opts
                   :ex ex}]
         (tap> data)
         (pp/pprint data)
         (throw (ex-info "analyze error" data)))))))

(defn emit
  "See https://clojure.github.io/tools.analyzer.jvm/spec/quickref.html .
  Emits a C string from a analyzed node.

  You should call `analyze` before calling this."
  [{:keys [op] :as v}]
  (try
    (let [{:keys [line column]} (and (not (or (:no-source-mapping *transpile-opts*)
                                              (:nosm *transpile-opts*)))
                                     (or (meta (:form v))
                                         (meta (first (:raw-forms v)))))
          debug-line (when (and line column)
                       (format "\n#line %s %s %s\n"
                               line
                               (pr-str (str "VYBE_CLJ:" *file* ":" *ns* ":" column))
                               {:op op}))]
      (binding [*transpilation* (-> *transpilation*
                                    (assoc :debug-line debug-line)
                                    (update :trace-history conj
                                            (let [v* (select-keys v [:op :form :fn :var])]
                                              (cond-> v*
                                                (:fn v*)
                                                (update :fn select-keys [:var])))))]
        (str
         debug-line
         (case op
           :def
           ;; Functions.
           (let [{:keys [name init]} v]
             (cond
               (= (:op (:expr init)) :fn)
               (let [{:keys [params body]} (first (:methods (:expr init)))
                     #_ #_schema (::schema (meta (first (:form (first (:methods (:expr init)))))))
                     #_ #_schema (if-let [s (and (symbol? schema)
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
                                   (str #_(if (:mut (meta form))
                                            ""
                                            "const ")
                                        ""
                                        (case (.getName ^Class tag)
                                          "[F"
                                          "float*"

                                          "java.lang.Object"
                                          (let [schema (::schema (meta form))]
                                            (-adapt-type schema))

                                          tag)
                                        " "
                                        (->name form))))
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
                        (emit body)
                        (str "\nreturn\n ({" (emit body) ";})")
                        #_(let [expressions (str/split (emit body) #";")]
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
                                (str "." (emit k) " = " (emit v)))
                              (:keys v)
                              (:vals v))
                        (str/join ", ")))

           :const
           (cond
             (= (:type v) :map)
             (format "{%s}"
                     (->> (:val v)
                          (mapv (fn [[k v]]
                                  (str "." (name k) " = "
                                       (emit (analyze v)))))
                          (str/join ", ")))

             (= (:type v) :vector)
             (format "{%s}"
                     (->> (:val v)
                          (mapv #(emit (analyze % (-> (ana/empty-env)
                                                      (update :locals merge (:locals (:env v)))))))
                          (str/join ", ")))

             (string? (:val v))
             (pr-str (:val v))

             (keyword? (:val v))
             (->name (name (:val v)))

             (symbol? (:val v))
             (->name (:val v))

             :else
             (case (:val v)
               true 1
               false 0
               nil "NULL"
               (:val v)))

           :vector
           (format "{%s}"
                   (->> (mapv emit (:items v))
                        (str/join ", ")))

           :static-call
           (let [{:keys [form method args] klass :class} v]
             ;; If no args, then this is likely a constant.
             (if (empty? args)
               (emit (analyze (eval form)))
               (case (->sym klass)
                 clojure.lang.Util
                 (case (->sym method)
                   (equiv identical)
                   (format "(%s == %s)"
                           (emit (first args))
                           (emit (second args))))

                 clojure.lang.Numbers
                 (case (->sym method)
                   (add multiply divide minus gt gte lt lte)
                   (->> args
                        (mapv emit)
                        (str/join (format " %s "
                                          ('{multiply *
                                             divide /
                                             add +
                                             minus -
                                             gt > gte >= lt < lte <=}
                                           (->sym method))))
                        parens)

                   inc
                   (format "(%s + 1)" (emit (first args)))

                   dec
                   (format "(%s - 1)" (emit (first args)))

                   abs
                   (format "vybe_abs(%s)" (emit (first args)))

                   ;; bit-and
                   and
                   (apply format "(%s & %s)" (mapv emit args))

                   or
                   (apply format "(%s | %s)" (mapv emit args)))

                 clojure.lang.RT
                 (case (->sym method)
                   aset
                   (let [[s1 s2 s3] (mapv emit args)]
                     (-> (->> (format " %s[%s] = %s"
                                      s1 s2 s3)
                              parens)
                         (str ";")))

                   (nth aget)
                   (let [[s1 s2] (mapv emit args)]
                     (->> (format " %s[%s] "
                                  s1 s2)
                          parens))

                   get
                   (let [[s1 s2] (mapv emit args)]
                     (format "%s.%s" s1 s2))

                   intCast
                   (format "(int)%s" (emit (first args)))

                   longCast
                   (format "(int64)%s" (emit (first args)))

                   floatCast
                   (format "(float)%s" (emit (first args)))

                   doubleCast
                   (format "(double)%s" (emit (first args)))))))

           :keyword-invoke
           (let [{:keys [keyword target]} v]
             (format "%s.%s"
                     (emit target)
                     (emit keyword)))

           :local
           (let [{:keys [form]} v]
             (->name form))

           :do
           (let [{:keys [statements ret]} v]
             (->> (concat statements [ret])
                  (mapv emit)
                  (str/join ";\n\n")))

           :if
           (let [{:keys [then else] t :test} v]
             (format "( %s ? \n   ({%s;}) : \n  ({%s;})  )"
                     (emit t)
                     (emit then)
                     (emit else)))

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
                                                           first)
                     binding-sym2 (symbol (->name binding-sym))]
                 (format "for (int %s = 0; %s < %s; ++%s) {\n  %s;\n}"
                         binding-sym2 binding-sym2
                         (if (symbol? range-arg)
                           (symbol (->name range-arg))
                           (emit (analyze range-arg
                                          (-> (ana/empty-env)
                                              (update :locals merge (:locals env))))))
                         binding-sym2
                         (or (some->> (-> (cons 'do body)
                                          (analyze
                                           (-> (ana/empty-env)
                                               (update :locals merge
                                                       (:locals env)
                                                       {binding-sym (analyze range-arg
                                                                             (-> (ana/empty-env)
                                                                                 (update :locals merge
                                                                                         (:locals env))))})))
                                          emit)
                                      #_ #_ #_str/split-lines
                                      (mapv #(str "  " % ";"))
                                      (str/join))
                             "")))))

           :invoke
           (if-let [var* (:var (:fn v))]
             (if (or (get-method c-invoke var*)
                     (vp/component? @var*))
               (c-invoke v)
               (case (symbol var*)
                 (clojure.core/* clojure.core/+)
                 (if (= (count (:args v)) 1)
                   (emit (first (:args v)))
                   (throw (ex-info "Unsupported" {:op (:op v)
                                                  :form (:form v)
                                                  :args v})))

                 (-invoke v)))
             (format "(%s)(%s)"
                     (emit (:fn v))
                     (->> (:args v)
                          (mapv emit)
                          (str/join ", "))))

           :var
           (let [my-var (:var v)
                 var-value @my-var
                 c-fn (::c-function (meta var-value))]
             (cond
               ;; We will only add the code function inline if
               ;; we have standalone mode enabled.
               (and (::standalone *transpile-opts*)
                    c-fn)
               c-fn

               (or (::schema (meta my-var))
                   (::global (meta my-var))
                   (:vybe/fn-meta (meta my-var))
                   (vp/fnc? var-value))
               (->name (:var v))

               (vp/component? var-value)
               (->name var-value)

               (get-method c-replace my-var)
               (c-replace v)

               :else
               (emit (analyze var-value))))

           :the-var
           (let [my-var (:var v)]
             (format "%s"
                     (->name my-var)))

           :set!
           (format "%s = %s;"
                   (emit (:target v))
                   (emit (:val v)))

           :host-interop
           (format "%s.%s"
                   (emit (:target v))
                   (:m-or-f v))

           :let
           (let [locals (:locals (:env v))]
             (format "({%s\n%s;})"
                     (->> (:bindings v)
                          (reduce (fn [{:keys [env-symbols] :as acc}
                                       {:keys [form init]}]
                                    (let [form (with-meta (symbol (->name form))
                                                 (meta form))
                                          existing? (get env-symbols form)
                                          parsed
                                          (format "%s%s = %s;"
                                                  ;; Don't redefine.
                                                  (if existing?
                                                    ""
                                                    ;; Variables are defined as
                                                    ;; `const` by default.
                                                    #_(if (:mut (meta form))
                                                        "__auto_type "
                                                        "const __auto_type ")
                                                    "__auto_type ")
                                                  form
                                                  (emit init))]
                                      (-> acc
                                          (update :env-symbols conj form)
                                          (update :collector conj parsed))))
                                  {:env-symbols (set (keys locals))
                                   :collector []})
                          :collector
                          (str/join "\n"))
                     (or (emit (:body v))
                         "")))

           nil
           (throw (ex-info (str "Unhandled `nil`: " v " (" (type v) ")")
                           {:v v
                            :v-type (type v)}))

           (do #_(def v v) #_ (:op v) #_ (keys v)
               (throw (ex-info (str "Unhandled: " (:op v))
                               {:op (:op v)
                                :raw-forms (:raw-forms v)
                                :form (:form v)})))))))
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
        (tap> {:label "Error when transpiling to C"
               :error error-map})
        (throw (ex-info "Error when transpiling to C" error-map))))))

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
               (vary-meta sym merge (adapt-schema schema))))))

(defn- -typename-schemas
  [components]
  (format "
#define typename(x) _Generic((x),        /* Get the name of a type */             \\
                                                                                  \\
        _Bool: \"_Bool\",                  unsigned char: \"unsigned char\",          \\
         char: \"char\",                     signed char: \"signed char\",            \\
    short int: \"short int\",         unsigned short int: \"unsigned short int\",     \\
          int: \"int\",                     unsigned int: \"unsigned int\",           \\
     long int: \"long int\",           unsigned long int: \"unsigned long int\",      \\
long long int: \"long long int\", unsigned long long int: \"unsigned long long int\", \\
        float: \"float\",                         double: \"double\",                 \\
  long double: \"long double\",                   char *: \"pointer to char\",        \\
       void *: \"pointer to void\",                int *: \"pointer to int\",         \\
%s)
"
          (if (seq components)
            (str (->> components
                      (mapv (fn [c]
                              (let [n (-adapt-type c)]
                                (format "   struct %s: \"%s\"" n (vp/comp-name c)))))
                      (str/join ", \\\n")))
            "")))
#_ (-typename-schemas [VybeHooks])

(defn transpile
  ([code-form]
   (transpile code-form {}))
  ([code-form {:keys [sym-meta sym sym-name] :as opts}]
   (binding [*transpile-opts* sym-meta]
     (let [*schema-collector (atom [])
           *var-collector (atom {:c-fns []
                                 :non-c-fns []
                                 :global-fn-pointers []})

           ;; Collect vars so we can prepend them to the C code.
           prewalk-form (fn [form]
                          (ast/prewalk (analyze form)
                                       (fn [v]
                                         (when (or (= (:op v) :var)
                                                   (= (:op v) :the-var))
                                           (cond
                                             ;; Generated C code.
                                             (and (::standalone *transpile-opts*)
                                                  (::c-function (meta @(:var v))))
                                             (do
                                               (swap! *var-collector update :c-fns concat
                                                      ;; Remove `do` by using `rest`.
                                                      (rest (:code-form @(:var v))))
                                               (swap! *var-collector update :global-fn-pointers concat
                                                      (:global-fn-pointers (::c-data @(:var v)))))

                                             ;; Clojure code (upcalls).
                                             (vp/fnc? @(:var v))
                                             (swap! *var-collector update :global-fn-pointers conj
                                                    (merge @(:var v)
                                                           {:var (:var v)}))

                                             ;; jextract wrapper.
                                             (:vybe/fn-meta (meta (:var v)))
                                             (swap! *var-collector update :global-fn-pointers conj
                                                    (merge (:vybe/fn-meta (meta (:var v)))
                                                           {:var (:var v)}))

                                             ;; Global vars.
                                             (::schema (meta (:var v)))
                                             (do
                                               (swap! *schema-collector conj (::schema (meta (:var v))))
                                               (swap! *var-collector update :c-fns conj
                                                      `(def ~(with-meta (:form v)
                                                               {::schema (-adapt-type
                                                                          (::schema (meta (:var v))))}))))

                                             ;; Components (structs).
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

                                                              (and (vp/component? v)
                                                                   (resolve (symbol (vp/comp-name v))))
                                                              (swap! *schema-collector conj v)

                                                              :else
                                                              v))
                                                          fields))
                                                  v))
                                              schema))
                             v)
                           final-form)

           {:keys [non-c-fns global-fn-pointers]} @*var-collector

           ;; Global function pointers, they are used so we don't need to
           ;; dynamic load libraries, we can pass the pointers from the JVM
           ;; loaded ones instead.
           global-fn-pointers (distinct global-fn-pointers)
           global-fn-pointers-code (->> global-fn-pointers
                                        (mapv (fn [{:keys [fn-desc var]}]
                                                (try
                                                  (format "__auto_type %s = (%s) 0;"
                                                          (->name var)
                                                          (-adapt-fn-desc fn-desc))
                                                  (catch Exception ex
                                                    (println {:fn-desc fn-desc
                                                              :var var}
                                                             ex)
                                                    (throw (ex-info "global-fn-pointers error"
                                                                    {:fn-desc fn-desc
                                                                     :var var
                                                                     :ex ex}))))))
                                        (str/join "\n"))
           [init-struct init-struct-val] (when (seq global-fn-pointers)
                                           (let [c (vp/make-component
                                                    (symbol (str sym-name "__init__struct"))
                                                    (->> global-fn-pointers
                                                         (mapv (fn [{:keys [fn-desc var]}]
                                                                 [(keyword (->name var)) fn-desc]))))]
                                             [c (c (->> global-fn-pointers
                                                        (mapv (fn [{:keys [fn-address var]}]
                                                                [(keyword (->name var)) fn-address]))
                                                        (into {})))]))
           init-fn-form (when init-struct
                          `(defn ~(with-meta (symbol (str sym "__init"))
                                    {:private true})
                             ~(with-meta (adapt-fn-args ['_init_struct :- [:* init-struct]])
                                (adapt-schema :void))
                             ~@(->> global-fn-pointers
                                    (mapv (fn [{:keys [_fn-desc var]}]
                                            ;; Set the global function pointer.
                                            `(reset! ~(symbol var)
                                                     (~(keyword (->name var))
                                                      @~'_init_struct)))))))

           _ (do (def aaa
                        [@*schema-collector
                         @*var-collector
                         final-form])
                      (def *schema-collector *schema-collector)
                      (def init-struct init-struct)
                      (def global-fn-pointers global-fn-pointers))

           components (->> (concat @*schema-collector
                                   (when init-struct [init-struct])
                                   (->> global-fn-pointers
                                        (keep :fn-desc)
                                        (mapcat -collect-fn-desc-components)
                                        (filter vp/component?)
                                        (group-by identity)
                                        keys))
                           reverse
                           distinct)
           schemas-c-code (->> components
                               (mapv comp->c)
                               (str/join "\n\n"))
           to-be-hashed [-common-c
                         schemas-c-code
                         global-fn-pointers-code
                         (-typename-schemas components)
                         final-form
                         (update opts :ret-schema (fn [v]
                                                    (if (vp/component? v)
                                                      (vp/comp-name v)
                                                      v)))]]
       #_(def to-be-hashed to-be-hashed)
       {::c-data {:schemas (distinct @*schema-collector)
                  :global-fn-pointers global-fn-pointers}

        :form-hash (abs (hash to-be-hashed))
        :c-code (->> [-common-c
                      (-typename-schemas components)
                      schemas-c-code
                      global-fn-pointers-code
                      (emit (analyze (list 'do init-fn-form final-form)))]
                     (str/join "\n\n"))
        :final-form final-form
        :init-struct-val init-struct-val}))))

(defn- remove-ansi
  [s]
  (str/replace s #"\x1b\[[0-9;]*m" ""))

(defn- error-callout
  [error {:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header error
                             #_ #_:body (str "The body of your message goes here."
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
   (let [{:keys [c-code ::c-data form-hash final-form init-struct-val]}
         (-> code-form
             (transpile (assoc opts ::version 40)))

         obj-name (str "vybe_" sym-name "_"
                       (when (or (:no-cache sym-meta)
                                 (:debug sym-meta))
                         (str (swap! *debug-counter inc) "_"))
                       form-hash)
         lib-name (System/mapLibraryName obj-name)
         lib-full-path (vy.u/app-resource (str "com/pfeodrippe/vybe/vybe_c/" lib-name)
                                          {:throw-exception false
                                           :target-folder "resources"})
         file (io/file lib-full-path)
         generated-c-file-path (str ".vybe/c/" obj-name ".c")]
     (vy.u/debug {:exists? (.exists file) :c-lib-path lib-full-path})
     (if (and (not (or (:no-cache sym-meta)
                       (:debug sym-meta)))
              (.exists file))
       {:lib-full-path lib-full-path
        :code-form final-form
        :init-struct-val init-struct-val
        :existent? true
        ::c-data c-data}

       (do (io/make-parents file)
           (io/make-parents (io/file generated-c-file-path))
           (spit generated-c-file-path c-code)

           (when (:debug sym-meta)
             (println c-code))

           ;; Using clang, we will analyze the code and then, if no errors,
           ;; try to compile it.
           (let [{:keys [err]} (proc/sh (->> ["clang"
                                              #_"-std=c23"
                                              "--analyze"

                                              ;; https://stackoverflow.com/questions/19863242/static-analyser-issues-with-command-line-tools
                                              "-Xanalyzer -analyzer-disable-checker"
                                              "-Xanalyzer deadcode.DeadStores"

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
                   ;; For safeness
                   ;; https://clang.llvm.org/docs/UndefinedBehaviorSanitizer.html#ubsan-checks
                   (let [safe-flags [#_"-fsanitize=undefined"
                                     #_"-fno-omit-frame-pointer"
                                     "-g"]]
                     (proc/sh #_{:env {#_ #_"UBSAN_OPTIONS" "print_stacktrace=1"
                                       #_ #_"ASAN_SAVE_DUMPS" "MyFileName.dmp"
                                       #_ #_"PATH" (System/getenv "PATH")}}
                              (format
                               (->> (concat
                                     ["clang"
                                      "-fdiagnostics-print-source-range-info"
                                      "-fcolor-diagnostics"
                                      "-Wno-unused-value"
                                      #_"-std=c23"
                                      #_"-Wpadded"
                                      #_"-O3"]
                                     #_safe-flags
                                     ["-shared"
                                      (when vp/linux?
                                        "-fPIC")
                                      generated-c-file-path
                                      " -o %s"])
                                    (remove nil?)
                                    (str/join " "))
                               lib-full-path))))]
             (when (seq err)
               ;; Println c code only if we haven't printed it before.
               (when-not (:debug sym-meta)
                 (println c-code))

               (let [errors (try
                              (->> (or (seq (str/split (remove-ansi err) #"VYBE_CLJ:"))
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
                              (catch Exception e
                                (println e)
                                ::error))
                     error-problem? (= errors ::error)
                     clj-error (when-not error-problem?
                                 (format "Found error while compiling C\n\n%s"
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
                                              (str/join "\n\n"))))
                     _ (when-not error-problem?
                         (->> errors
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
                              (str/join "\n\n")))]

                 (println (bling/callout {:label (format "C %s error"
                                                         (if analyzer-err
                                                           "ANALYZER"
                                                           "COMPILE"))
                                          :type :error}
                                         err))

                 (if error-problem?
                   (throw (ex-info "Error when compiling C code"
                                   {:error (str/split-lines (remove-ansi err))}))
                   (do
                     (tap> (-> [:div {:style {:background-color "#553333aa"
                                              :padding "10px 20px 40px 10px"}}
                                [:h2 "C error"]
                                (into [:<>]
                                      (for [err errors]
                                        [:div {:style {:padding-bottom "10px"}}
                                         [:portal.viewer/table err]]))
                                [:portal.viewer/code (remove-ansi err)]]
                               (with-meta {:portal.viewer/default :portal.viewer/hiccup})))

                     (let [{:keys [file-path line column error]} (first errors)]
                       (if (and file-path line column error)
                         (throw (clojure.lang.Compiler$CompilerException.
                                 file-path
                                 line
                                 column
                                 (ex-info error {})))
                         (throw (ex-info clj-error
                                         {:error-lines errors
                                          :error (str/split-lines (remove-ansi err))
                                          :code-form final-form})))))))))
           {:lib-full-path lib-full-path
            :code-form final-form
            :init-struct-val init-struct-val
            :existent? false
            ::c-data c-data})))))

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

(defn -c-fn-builder
  [c-defn]
  (let [{:keys [^String lib-full-path]} c-defn
        lib (SymbolLookup/libraryLookup lib-full-path (vp/default-arena))
        symbol-finder #(let [v (.find lib (->name %))]
                         (when (.isPresent v)
                           (.get v)))
        fn-address (symbol-finder (::c-function (meta c-defn)))
        c-fn (vp/c-fn (:fn-desc c-defn))]
    {:fn-address fn-address
     :symbol-finder symbol-finder
     :c-fn c-fn
     :fn-desc (:fn-desc c-defn)}))

(defn find-symbol
  "Find C symbol for a fnc.

  Returns `nil` if no symbol is found."
  [fnc sym-name]
  ((:symbol-finder fnc)
   sym-name))

(defn set-globals!
  "Update C symbols for a fnc using its initializer (if any).

  E.g.

     (set-globals! eita {`-tap -tap})"
  [fnc m]
  (when-let [initializer (:initializer fnc)]
    (initializer (merge (:init-struct-val fnc)
                        (update-keys m (comp keyword ->name))))
    true))
#_ (set-globals! eita {`-tap -tap})
#_ (eita 20)

(defonce -*vybec-fn-watchers (atom {}))

(defmacro defn*
  "Transpiles Clojure code into a C function.

  E.g.

    (vc/defn* simple-10 :- :int
      [v :- Translation]
      (simple v))"
  {:clj-kondo/lint-as 'schema.core/defn}
  [n _ ret-schema args & fn-tail]
  `(let [args-desc-1# (quote ~(->> args
                                   (partition-all 3 3)
                                   (mapv (fn [[sym _ schema]]
                                           [sym schema]))))
         args-desc-2# ~(->> args
                            (partition-all 3 3)
                            (mapv (fn [[sym _ schema]]
                                    [`(quote ~sym) schema])))]
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
                    (merge {:fn-desc (into [:fn ~ret-schema] args-desc-2#)}))

             v# (-> (vp/map->VybeCFn (merge v# (-c-fn-builder v#)))
                    ;; TODO We could put this in the returned map instead of in the metadata.
                    (with-meta {::c-function ~(->name (symbol (str *ns*) (str n)))}))

             initializer# (when (:init-struct-val v#)
                            #(when-let [init-struct-val# %]
                               ((-> ((:symbol-finder v#)
                                     (quote ~(->name (symbol (str *ns*) (str n "__init")))))
                                    (vp/c-fn [:fn :void [:some_struct [:* :void]]]))
                                (vp/mem init-struct-val#))))]

         ;; Initialize globals, if any.
         (when initializer#
           (initializer# (:init-struct-val v#)))

         ;; Return.
         (merge v# {:initializer initializer#})))

     ;; Set some metadata for documentation.
     (alter-meta! (var ~n) merge
                  {:arglists (list args-desc-1#)
                   :doc (cond-> (format "Returns %s" (if (vp/component? ~ret-schema)
                                                       (vp/comp-name ~ret-schema)
                                                       (quote ~ret-schema)))
                          (:doc (meta (var ~n)))
                          (str "\n\n" (:doc (meta (var ~n)))))})

     ;; Remove any existing watchers.
     (->> (get @-*vybec-fn-watchers (var ~n))
          (mapv (fn [[v# identifier#]]
                  (remove-watch v# identifier#))))
     ;; Watch global fn pointers vars (if any) so we can have hot reloading.
     (let [global-fn-pointers# (:global-fn-pointers (::c-data ~n))
           watchers# (->> global-fn-pointers#
                          (mapv (fn [{v# :var}]
                                  (let [identifier# (symbol (str "_vybe_c_watcher_" (symbol (var ~n)) "_" (symbol v#)))]
                                    (add-watch v# identifier#
                                               (fn [& _args#]
                                                 (try
                                                   #_(tap> {:vybec-debug-msg "Updating VybeC fn"
                                                            :vybec-fn (var ~n)
                                                            :trigger v#})
                                                   (set-globals! ~n {(symbol v#) @v#})
                                                   ;; Trigger var mutation so other vars can know
                                                   ;; about it.
                                                   (alter-var-root (var ~n) (constantly @(var ~n)))
                                                   (catch Exception ex#
                                                     (println ex#)))))
                                    [v# identifier#]))))]
       (swap! -*vybec-fn-watchers assoc (var ~n) watchers#))

     (var ~n)))
#_ (vybe.c/defn* ^:debug eita :- :int
     [a :- :int]
     (tap> (+ a 4550)))
#_ (eita 20)

#_ (find-symbol eita `_tap)
#_ (-> (find-symbol eita `eita__init)
       (vp/c-fn [:fn :void [:some_struct [:* :void]]]))

(defn p->fn
  "Convert a pointer (mem segment) so it can be called as a
  VybeCFn."
  [mem c-defn]
  (assoc c-defn :fn-address mem))

(vp/defcomp VybeCObject
  [[:type :string]
   [:size :long]
   [:data [:* {:const true} :void]]
   [:metadata :string]
   [:form :string]])

(defn ^:private -adapt-vybe-c-obj
  [v]
  (let [{:keys [type size data]} v
        data (vp/reinterpret data size)

        primitive-layout (case type
                           ("int" "unsigned int")
                           :int

                           ("long int" "unsigned long int")
                           :long

                           ("long long" "unsigned long  long")
                           :long-long

                           ("double")
                           :double

                           ("_Bool")
                           :boolean

                           ("char" "unsigned char" "signed char")
                           :char

                           ("short int" "unsigned short int")
                           :short

                           ("pointer to char")
                           :string

                           nil)

        [res c] (if primitive-layout
                  [(vp/p->value data primitive-layout) nil]
                  (let [c (eval (symbol type))]
                    [(vp/clone
                      (vp/as data c))
                     c]))]
    {:res res
     :c c}))

(defmacro eval*
  "Evaluate some arbitrary body that will be compiled to C.

  Useful for debugging.

  E.g.

    (eval* (- 4 199))"
  [& body]
  (let [fn-sym (symbol (str "fn-sym-" (hash body)))
        res 'res--
        data 'data--]
    `(do
       (vybe.c/defn* ~fn-sym :- VybeCObject
         []
         (let [~res (do ~@body)
               ~data (vp/as (malloc (vp/sizeof ~res))
                            [:* (typeof ~res)])]
           (reset! @~data ~res)
           (VybeCObject
            {:type (typename ~res)
             :size (vp/sizeof ~res)
             :data ~data})))

       (-> (~fn-sym)
           -adapt-vybe-c-obj
           :res))))
#_(eval* (tap> (- 4 200)))

;; ================= upcall fns ===================
(vp/defnc ^:private -tap :- :void
  [v :- VybeCObject]
  (let [v (vp/as v VybeCObject)
        {:keys [type form metadata]} v
        {:keys [res c]} (-adapt-vybe-c-obj v)

        datafied (core-p/datafy res)
        form-metadata (-> {:form (let [form (edn/read-string form)]
                                   (if (instance? clojure.lang.IMeta form)
                                     (with-meta form {:portal.viewer/default :portal.viewer/pr-str})
                                     form))
                           :type (if c
                                   (symbol (vp/comp-name c))
                                   (keyword type))}
                          (with-meta (meta datafied)))]
    (pp/pprint (merge {:value datafied}
                      form-metadata))

    (tap> (with-meta
            [:div {:style {:color "#999999ff"
                           :background-color "rgb(42 38 45 / 60%)"
                           :padding "4px"
                           :padding-left "6px"}}

             [:h2
              [:b {:style {:color "#ffff00"}}
               "[C] "]
              [:span {:style {:color "#99bb99"}}
               metadata]]

             [:div {:style {:padding-top "10px"
                            :padding-bottom "10px"}}

              [:portal.viewer/table form-metadata]]

             [:div {:style {:margin "4px"}}
              [:portal.viewer/tree datafied]]]

            (merge {:portal.viewer/default :portal.viewer/hiccup}
                   (meta datafied))))))
#_(-tap (VybeCObject {:type "int"
                      :size 4
                      :form "(tap> 100)"
                      :data (vp/int* 100)
                      :metadata "MY META"}))
#_(-tap (VybeCObject {:type "vybe.type/Vector2"
                      :size (vp/sizeof vybe.type/Vector2)
                      :form "(tap> (vybe.type/Vector2 [10 5]))"
                      :data (vybe.type/Vector2 [10 5])
                      :metadata "MY META"}))

(defn comptime
  "In the JVM, it returns `v`. In VybeC, it runs the
  code in compile time (in the JVM) and returns the value."
  [v]
  v)

(defmethod c-macroexpand #'comptime
  [{:keys [form]}]
  (eval form))

(declare ^:no-ns typeof
         ^:no-ns typename
         #_^:no-ns comptime)

(defmethod c-macroexpand #'tap>
  [{:keys [args form]}]
  `(let [arg# ~(first args)]
     (-tap (VybeCObject
            {:type (typename arg#)
             :size (vp/sizeof arg#)
             :metadata ~(let [{:keys [line column]} (meta form)]
                          (str *ns* ":" line ":" column))
             :form ~(str form)
             :data (vp/& arg#)}))
     arg#))

;; ================= c-invoke methods ===================
(declare ^:no-ns NEXTPOWEROFTWO
         ^:no-ns cubicinterp
         ^:no-ns zapgremlins)
(declare ^:no-ns malloc
         ^:no-ns calloc
         ^:no-ns free)

(defmethod c-invoke #'printf
  [node]
  (str "({" (-invoke node {:sym "printf"}) "; fflush(stdout);})"))

(defmethod c-invoke #'println
  [node]
  (let [{:keys [args] :as node*} (assoc-in node [:fn :var] #'printf)]
    (-> node*
        ;; Add newline to last argument.
        (update-in [:args (dec (count args)) :val] str "\n")
        emit)))

(defmethod c-invoke #'print
  [node]
  (let [node* (assoc-in node [:fn :var] #'printf)]
    (-> node* emit)))

;; -- Special case for a VybeComponent invocation.
(defmethod c-invoke `vp/component
  [{:keys [args] :as node}]
  (let [v (:var (:fn node))
        arg (first args)
        arg-str (if (vector? (:form arg))
                  ;; Positional.
                  (let [fields (VybeComponent/.fields @(:var (:fn node)))
                        arg-form (zipmap (keys fields) (:form arg))]
                    (when (seq arg-form)
                      (emit (analyze arg-form (:env arg)))))
                  ;; Map.
                  (some-> arg emit))]
    (str  "(" (->name @v) ")"
          (or arg-str "{}"))))

;; -- Clojure core.
(defmethod c-invoke #'reset!
  [{:keys [args]}]
  (let [[*atom newval] args]
    (format "%s = %s;"
            (emit *atom)
            (emit newval))))

(defmethod c-invoke #'not=
  [{:keys [args]}]
  (format "(%s != %s)"
          (emit (first args))
          (emit (second args))))

(defmethod c-macroexpand #'swap!
  [{:keys [args]}]
  (let [[target f-sym & args-rest] args]
    `(reset! ~target (~f-sym ~@(cons target args-rest)))))

(defmethod c-invoke #'deref
  [{:keys [args]}]
  (format "(*%s)" (emit (first args))))

(defmethod c-macroexpand #'name
  [{:keys [form]}]
  (eval form))

(defmethod c-invoke #'merge
  [{:keys [args]}]
  (let [[target] args]
    (->> (rest args)
         (mapcat (fn [{:keys [op] :as params}]
                   (let [kv-f #(->> %
                                    (mapv (fn [[k v]]
                                            (str (emit target)
                                                 "."
                                                 (name k)
                                                 " = "
                                                 v
                                                 ";"))))]
                     (case op
                       :map
                       (kv-f (mapv vector
                                   (mapv (comp :form) (:keys params))
                                   (mapv emit (:vals params))))

                       :const
                       (kv-f (case (:type params)
                               :map
                               (:val params)))

                       :local
                       (let [l (:form params)]
                         [(str (emit target) " = " l ";")])

                       [(str (emit target) " = " (emit params) ";")]

                       #_(throw (ex-info "Unhandled case when C-merging"
                                         {:op op
                                          :form (:form params)
                                          :keys (keys params)
                                          :val (:val params)}))))))
         (str/join "\n"))))

;; -- Others.
(defmethod c-invoke #'vp/address
  [{:keys [args]}]
  (format "&%s" (emit (first args))))

(defmethod c-invoke #'vp/&
  [{:keys [args]}]
  (format "&%s" (emit (first args))))

(defmethod c-invoke #'vp/as
  [{:keys [args]}]
  (let [form (:form (second args))]
    (format "(%s)%s"
            (-adapt-type form)
            #_"uint64_t[]"
            (emit (first args)))))

(defmethod c-macroexpand #'vp/arr
  [{:keys [args]}]
  `(vp/as ~(first args) [:vec ~(second args)]))

(defmethod c-invoke #'vp/sizeof
  [{:keys [args]}]
  (if (= (count args) 2)
    ;; Arity of 2 means we want to know the size of a struct field.
    (format "_member_size(%s, %s)"
            (emit (first args))
            (emit (second args)))
    (format "sizeof(%s)" (emit (first args)))))

(defmethod c-invoke #'vp/alignof
  [{:keys [args]}]
  (if (= (count args) 2)
    ;; Arity of 2 means we want to know the alignment of a struct field.
    (format "_member_alignof(%s, %s)"
            (emit (first args))
            (emit (second args)))
    (format "_Alignof(%s)" (emit (first args)))))

(defmethod c-invoke #'vp/new*
  [{:keys [args]}]
  (let [[params c-sym] (mapv emit args)]
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
              (emit (analyze `(merge ~'@_my_v
                                     ~(:form (first args)))
                             (-> (:env (first args))
                                 (update :locals assoc '_my_v {})))))
      (format "((%s*)malloc(sizeof (%s)))" c-sym c-sym))))

(declare ^:private ^:no-ns memset)

(defmethod c-macroexpand #'vp/zero!
  [{:keys [args]}]
  (let [[mem len vybe-schema] args]
    `(memset ~mem 0 (* ~len (vp/sizeof ~vybe-schema)))))

;; ================= c-replace methods ===================
(defmethod c-replace #'vp/null
  [_node]
  (emit (analyze nil)))
