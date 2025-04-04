(ns vybe.raylib.impl
  (:require
   [clojure.string :as str]
   [camel-snake-kebab.core :as csk]
   [clojure.java.shell :as sh]
   [vybe.panama :as vp])
  (:import
   (org.vybe.raylib raylib Color)
   (java.lang.foreign Arena MemorySegment MemoryLayout ValueLayout FunctionDescriptor StructLayout)
   (jdk.internal.foreign.layout ValueLayouts)
   (java.lang.reflect Method Parameter)
   (java.lang.management ManagementFactory)))

(set! *warn-on-reflection* true)

;; Compile to OSX
#_(def lib-name
    (let [lib-name "vybe"
          {:keys [err]}
          (->> (-> (format "gcc -undefined dynamic_lookup -shared -I /opt/homebrew/Cellar/raylib/5.0/include bin/vybe_raylib.c -o /opt/homebrew/Cellar/raylib/5.0/lib/lib%s.dylib" lib-name)
                   (str/split #" "))
               #_(-> (format "gcc -I /opt/homebrew/Cellar/raylib/5.0/include bin/vybe_raylib.h -o %s.dylib" lib-name)
                     (str/split #" "))
               (apply sh/sh))]
      (when (seq err)
        (throw (ex-info err {})))
      lib-name))
;; END of Compile to OSZ

(defonce *state
  (atom {:buf-general []
         :buf1 []
         :buf2 []
         :front-buf? true}))

(defn -add-command
  [cmd {:keys [general prom form]
        :or {prom (promise)}}]
  (let [cmd-data {:cmd cmd
                  :prom prom
                  :form form}]
    (locking *state
      (swap! *state (fn [state]
                      (if general
                        (-> state
                            (update :buf-general conj cmd-data))
                        (-> state
                            (update :buf1 conj cmd-data)
                            (update :buf2 conj cmd-data))))))))

(defmacro add-command
  [cmd params]
  `(-add-command ~cmd (merge ~params
                             {:form (quote ~&form)})))

(def ^:private declared-methods
  (concat (:declaredMethods (bean raylib))
          (:declaredMethods (vp/-try-bean "org.vybe.raylib.raylib_1"))
          (:declaredMethods (vp/-try-bean "org.vybe.raylib.raylib_2"))))

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
      (keyword "raylib" type-name)

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
          "raylib")))

(defn address?
  [t]
  (or (layout? t)
      (= t :pointer)))

(defn raylib-methods
  []
  (->> declared-methods
       (filter #(str/includes? (.getName ^Method %) "$descriptor"))

       (remove #(or (str/starts-with? (.getName ^Method %) "_")
                    (str/starts-with? (.getName ^Method %) "gl")
                    ;; Windows has some really weird functions.
                    ;; (count "$descriptor") + 2
                    (<= (count (.getName ^Method %)) (+ 11 2))))

       #_(take 10)
       (pmap (fn [^Method method]
               (let [^FunctionDescriptor desc (.invoke method nil (into-array Object []))
                     args (.argumentLayouts desc)

                     ret' (.returnLayout desc)
                     ret-layout (when (and (.isPresent ret')
                                           (instance? StructLayout (.get ret')))
                                  (symbol (str "org.vybe.raylib." (.get (.name ^StructLayout (.get ret'))))
                                          "layout"))
                     ret (when (.isPresent ret')
                           (->type (.get ret')))

                     desc-name ((comp :name bean) method)
                     desc-full-name (str (.getName (.getDeclaringClass method)) "/" desc-name)

                     main-name (str/replace desc-name #"\$descriptor" "")
                     address-name (str (str/replace desc-name #"\$descriptor" "") "$address")
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
                            :java-fn-desc `(~(symbol desc-full-name))
                            :ret-layout ret-layout
                            :fn-address `(~(symbol (str "org.vybe.raylib.raylib/" address-name)))
                            :has-arena? (or (layout? ret)
                                            (some (comp address? :clj-type)
                                                  args))
                            :main-thread? (nil? ret)})))))))
#_ (def methods-to-intern (raylib-methods))

;; TODO We could not check main thread for OSs other than OSX.
(defmacro t
  "Runs command (delayed) in the main thread.

  Useful for REPL testing as it will block and return
  the result from the command."
  [& body]
  `(let [*res# (promise)]
     (add-command
      (fn [] ~@body)
      {:general true
       :prom *res#})
     (let [res# (deref *res#)]
       (when (:error res#)
         (throw (ex-info "Error while running command"
                         {:error (:error res#)
                          :form (quote ~&form)
                          :form-meta ~(meta &form)})))
       res#)))

(set! *warn-on-reflection* false)
(def ^:private get-first-thread
  (memoize
   (fn []
     (let [thread-mxbean (ManagementFactory/getThreadMXBean)
           thread-ids (seq (.getAllThreadIds thread-mxbean))]
       (when (seq thread-ids)
         (let [first-thread-id (apply min thread-ids)
               first-thread-info (.getThreadInfo thread-mxbean first-thread-id)]
           (when first-thread-info
             (.getThreadId first-thread-info))))))))
(set! *warn-on-reflection* true)

(defn first-thread?
  []
  (= (.getId (Thread/currentThread)) (get-first-thread)))

(def any-thread-methods
  #{"WindowShouldClose"
    "GetMonitorName"
    "SetWindowState"
    "SetConfigFlags"
    "GetFontDefault"
    "GetFrameTime"
    "GetMonitorWidth"
    "GetMonitorHeight"
    "LoadImageFromTexture"
    "rlReadTexturePixels"
    "ExportImage"})

(def any-thread-methods-regexes
  #{#"Quaternion.*"
    #"Vector.*"
    #"Matrix.*"})

(defmacro -intern-methods
  [init size]
  `(do ~(->> (raylib-methods)
             (drop init)
             (take size)
             (mapv (fn [[n {:keys [args ret ret-layout ^FunctionDescriptor java-fn-desc
                                   fn-address has-arena? main-thread?]}]]
                     #_(when (= (System/getenv "VYBE_DEBUG") "true")
                       (println :RAYLIB_VAR (csk/->kebab-case-symbol n)))
                     (let [ray-args (mapv (fn [{:keys [name clj-type]}]
                                            (if (address? clj-type)
                                              ``(vp/mem ~~(symbol name))
                                              (symbol name)))
                                          args)]
                       (try
                         `(defmacro ~(csk/->kebab-case-symbol n)
                            {:arglists (list
                                        (quote
                                         ~(mapv (fn [{:keys [name clj-type]}]
                                                  [(symbol name) clj-type])
                                                args)))
                             :doc ~(format "Returns %s." (or ret "void"))
                             ;; We use the foreign version so we don't do (more)
                             ;; unnecessary stuff here.
                             :vybe/fn-meta {:fn-desc [:fn {:foreign-fn-desc ~java-fn-desc
                                                           :fn-args (quote
                                                                     ~(mapv (fn [{:keys [name]}]
                                                                              (symbol name))
                                                                            args))}]
                                            :fn-address ~fn-address}}
                            ;; Fn args.
                            ~(mapv (comp symbol :name) args)
                            ;; Fn body.
                            `(vp/try-p->map
                              ~~(cond
                                  ;; Functions that start with `Is` and other
                                  ;; prefixes can be safely run outside the main
                                  ;; thread.
                                  (or (str/starts-with? n "Is")
                                      (contains? any-thread-methods n)
                                      (some #(re-matches % n) any-thread-methods-regexes))
                                  ``(~(symbol "org.vybe.raylib.raylib" ~n)
                                     ~@~(vec
                                         (concat
                                          (when (and has-arena? (layout? ret))
                                            [``(vp/default-arena)])
                                          ray-args)))

                                  (or (not main-thread?)
                                      (and main-thread?
                                           (str/includes? n "Window")))
                                  ``(if (first-thread?)
                                      (~(symbol "org.vybe.raylib.raylib" ~n)
                                       ~@~(vec
                                           (concat
                                            (when (and has-arena? (layout? ret))
                                              [``(vp/default-arena)])
                                            ray-args)))
                                      (t (~(symbol "org.vybe.raylib.raylib" ~n)
                                          ~@~(vec
                                              (concat
                                               (when (and has-arena? (layout? ret))
                                                 [``(vp/default-arena)])
                                               ray-args)))))

                                  :else
                                  ;; Main thread.
                                  ``(if (first-thread?)
                                      (~(symbol "org.vybe.raylib.raylib" ~n)
                                       ~@~(vec
                                           (concat
                                            (when (layout? ret)
                                              [``(vp/default-arena)])
                                            ray-args)))
                                      (add-command
                                       (with-meta
                                         (fn ~'~'--internal-fn
                                           ([]
                                            ~~(if has-arena?
                                                ``(~'~'--internal-fn (vp/default-arena))
                                                ``(~'~'--internal-fn nil)))
                                           ([~'~'arena]
                                            ;; (org.raylib.raylib_h/WHATEVER [allocator?] and some args)
                                            (~(symbol "org.vybe.raylib.raylib" ~n)
                                             ~@~(vec
                                                 (concat
                                                  (when (layout? ret)
                                                    [''arena])
                                                  ray-args)))))
                                         {:form (quote ~~'&form)})
                                       {})))
                              ~~(when ret-layout
                                  ``(let [^MemoryLayout  ~'~'l (~(symbol ~(str ret-layout)))]
                                      (vp/make-component (symbol (.get (.name ~'~'l)))
                                                         ~'~'l)))))
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
            (range (inc (int (/ (count (raylib-methods))
                                100)))))))))
#_(intern-methods)
#_(macroexpand-1 '(-intern-methods 300 10))
#_(meta #'draw-text!)

#_(macroexpand-1 ' (load-model "OOOB"))
#_(macroexpand-1 '(update-camera 1 2))
#_(macroexpand-1 '(get-monitor-name 0))

(comment

  ())
