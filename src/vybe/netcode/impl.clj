(ns vybe.netcode.impl
  (:require
   [clojure.string :as str]
   [camel-snake-kebab.core :as csk]
   [vybe.panama :as vp])
  (:import
   (java.lang.foreign Arena MemorySegment MemoryLayout ValueLayout FunctionDescriptor StructLayout)
   (jdk.internal.foreign.layout ValueLayouts)
   (java.lang.reflect Method Parameter)
   (org.vybe.netcode netcode)))

(set! *warn-on-reflection* true)

(def ^:private declared-methods
  (concat (:declaredMethods (bean netcode))
          (:declaredMethods (vp/-try-bean "org.vybe.netcode.netcode_1"))
          (:declaredMethods (vp/-try-bean "org.vybe.netcode.netcode_2"))
          (:declaredMethods (vp/-try-bean "org.vybe.netcode.netcode_3"))
          (:declaredMethods (vp/-try-bean "org.vybe.netcode.netcode_4"))
          (:declaredMethods (vp/-try-bean "org.vybe.netcode.netcode_5"))
          (:declaredMethods (vp/-try-bean "org.vybe.netcode.netcode_6"))))

(defn- ->type
  [^StructLayout v]
  (let [n (.name v)
        type-name (when (.isPresent n)
                    (.get n))]
    (cond
      (and (= (type v)
              jdk.internal.foreign.layout.ValueLayouts$OfAddressImpl)
           (not type-name))
      :pointer

      type-name
      (keyword "netcode" type-name)

      :else
      (case (symbol (.getName (class v)))
        jdk.internal.foreign.layout.ValueLayouts$OfDoubleImpl
        :double

        jdk.internal.foreign.layout.ValueLayouts$OfLongImpl
        :long

        jdk.internal.foreign.layout.ValueLayouts$OfFloatImpl
        :float

        jdk.internal.foreign.layout.ValueLayouts$OfIntImpl
        :int

        jdk.internal.foreign.layout.ValueLayouts$OfShortImpl
        :short

        jdk.internal.foreign.layout.ValueLayouts$OfCharImpl
        :char

        jdk.internal.foreign.layout.ValueLayouts$OfByteImpl
        :byte

        jdk.internal.foreign.layout.ValueLayouts$OfBooleanImpl
        :boolean))))

(defn layout?
  [t]
  (and t
       (= (namespace t)
          "netcode")))

(defn address?
  [t]
  (or (layout? t)
      (= t :pointer)))

(defn -methods
  []
  (->> declared-methods
       (filter #(str/includes? (.getName ^Method %) "$descriptor"))
       (filter #(let [method-name (.getName ^Method %)]
                  (or (str/starts-with? (str/lower-case method-name) "netcode")
                      (str/starts-with? (str/lower-case method-name) "cn_")
                      (str/starts-with? method-name "vybe_"))))
       #_(filter #(= (.getName %) "GetMonitorName$descriptor"))
       #_(take 10)
       (pmap (fn [^Method method]
               (let [^FunctionDescriptor desc (.invoke method nil (into-array Object []))
                     args (.argumentLayouts desc)

                     ret' (.returnLayout desc)
                     ret-layout (when (and (.isPresent ret')
                                           (instance? StructLayout (.get ret')))
                                  (symbol (str "org.vybe.netcode." (.get (.name ^StructLayout (.get ret'))))
                                          "layout"))
                     ret (when (.isPresent ret')
                           (->type (.get ret')))

                     desc-name ((comp :name bean) method)
                     main-name (str/replace desc-name #"\$descriptor" "")
                     ^Method main-method (->> declared-methods
                                              (filter (comp #(= main-name (.getName ^Method %))))
                                              first)]
                 (when-not main-method
                   (throw (ex-info "Method for desc does not exist"
                                   {:desc desc
                                    :desc-name desc-name})))
                 (let [args (mapv (fn [v ^Parameter param]
                                    {:name (.getName param)
                                     :clj-type (if (= v :panama/allocator)
                                                 v
                                                 (->type v))})
                                  args
                                  ;; If return is a layout, the method
                                  ;; receives an allocator (e.g. Arena) as
                                  ;; the first arg.
                                  (if (layout? ret)
                                    (rest (.getParameters main-method))
                                    (.getParameters main-method)))]
                   (vector main-name
                           {:args args
                            :ret ret
                            :ret-layout ret-layout
                            :has-arena? (or (layout? ret)
                                            (some (comp address? :clj-type)
                                                  args))
                            :main-thread? (nil? ret)})))))))
#_(def methods-to-intern (vec (-methods)))

(defn -debug
  [v]
  (let [t (type v)]
    (if (contains? #{Long String Boolean} t)
      v
      t)))

(defmacro -intern-methods
  [init size]
  `(do ~(->> (-methods)
             (drop init)
             (take size)
             (mapv (fn [[n {:keys [args ret ret-layout has-arena?]}]]
                     (when (= (System/getenv "VYBE_DEBUG") "true")
                       (println :NETCODE_VAR (csk/->kebab-case-symbol n)))
                     (let [ray-args (mapv (fn [{:keys [name clj-type]}]
                                            (if (address? clj-type)
                                              ``(vp/mem ~~(symbol name))
                                              (symbol name)))
                                          args)]
                       (try
                         `(defmacro ~(csk/->kebab-case-symbol
                                      n
                                      #_(subs n 4))
                            {:arglists (list
                                        (quote
                                         ~(mapv (fn [{:keys [name clj-type]}]
                                                  [(symbol name) clj-type])
                                                args)))
                             :doc ~(format "Returns %s." (or ret "void"))}
                            ;; Fn args.
                            ~(mapv (comp symbol :name) args)
                            ;; Fn body.
                            `(do #_(println '~'~(csk/->kebab-case-symbol n)
                                            (mapv -debug ~~(mapv (comp symbol :name) args)))
                                 (vp/try-p->map
                                  ~~``(~(symbol "org.vybe.netcode.netcode" ~n)
                                       ~@~(vec
                                           (concat
                                            (when (and has-arena? (layout? ret))
                                              [``(vp/default-arena)])
                                            ray-args)))
                                  ~~(when ret-layout
                                      ``(let [~'~'l (~(symbol ~(str ret-layout)))]
                                          (vp/make-component (symbol (.get (.name ~'~'l)))
                                                             ~'~'l))))))
                         (catch Error _e
                           nil))))))))

(def intern-methods
  (memoize
   (fn []
     (vec
      (pmap (fn [n]
              ;; We use `eval` to avoid macroexpansion of
              ;; all the methods, which would give us a
              ;; "method too large" error.
              (eval `(-intern-methods ~(* n 100) 100)))
            (range (inc (int (/ (count (-methods))
                                100)))))))))
#_(intern-methods)
#_(macroexpand-1 '(-intern-methods 300 10))
#_(meta #'draw-text!)

#_(macroexpand-1 '(ecs-add-id w e id))
#_(macroexpand-1 '(cn-socket-cleanup socket))
#_(macroexpand-1 '(update-camera! 1 2))
#_(macroexpand-1 '(get-monitor-name 0))

(comment

  ())
