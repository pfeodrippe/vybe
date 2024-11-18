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
   [clojure.datafy :as datafy]))

#_(set! *warn-on-reflection* true)

(comment

  (do #_(portal/close portal)

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

  (do
    (def a (atom 0))
    (tap> a)
    (time
     (doseq [n (range 120)]
       (reset! a n))))

  (tap> ^{:portal.viewer/default :vybe.experimental.portal/view-presentation}
        [^{:portal.viewer/default :portal.viewer/hiccup}
         [:h1 "hello"]
         ^{:portal.viewer/default :portal.viewer/hiccup}
         [:h1 "world"]])

  (tap> ^{:portal.viewer/default :portal.viewer/hiccup}
        [:<>
         [:script {:src "https://cdn.jsdelivr.net/npm/vega@5.30.0"}]])

  (do (in-ns 'portal.runtime)
      (defn- invalidate [session-id a old new]
        (when-not (= (value->key old) (value->key new))
          (set-timeout
           #(when (= @a new) (notify session-id a))
           0))))

  (reset! portal 0)

  (time
   (doseq [_ (range 60)]
     (swap! portal inc)))


  ;; ------------- CLJS
  (portal/repl portal)
  :cljs/quit

  (do (defn add-script! [src]
        (let [script (js/document.createElement "script")]
          (.setAttribute script "src" src)
          (js/document.head.appendChild script)))

      (run! add-script! ["https://cdn.jsdelivr.net/npm/vega@5.30.0"
                         "https://cdn.jsdelivr.net/npm/vega-lite@5.21.0"
                         "https://cdn.jsdelivr.net/npm/vega-embed@6.26.0"]))

  @#'portal.ui.viewer.vega/vega-url
  (alter-var-root #'portal.ui.viewer.vega/vega-url (constantly "eita"))

  (do
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
(defn- parens
  [v-str]
  (format "(%s)" v-str))

(defn- ->sym
  [klass]
  (symbol (.getName klass)))

(defn -transpile
  "See https://clojure.github.io/tools.analyzer.jvm/spec/quickref.html"
  [{:keys [op] :as v}]
  (case op
    :def
    (let [{:keys [name init]} v]
      (when (= (:op (:expr init)) :fn)
        (let [{:keys [params body]} (first (:methods (:expr init)))
              return-tag (.getName ^Class (:return-tag (:expr init)))]

          (str "VYBE_EXPORT "  return-tag " "
               name (->> params
                         (mapv (fn [{:keys [tag form]}]
                                 (str (case (.getName ^Class tag)
                                        "[F" "float*"
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

    :const
    (case (:val v)
      true 1
      false 0
      (:val v))

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

          aget
          (let [[s1 s2] (mapv -transpile args)]
            (mapv :op args)
            (-transpile (last args))
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
          (format "for (int %s = 0; %s < %s; ++%s) {\n  %s\n}"
                  binding-sym binding-sym range-arg binding-sym
                  (->> (-> (cons 'do body)
                           (ana/analyze
                            (-> (ana/empty-env)
                                (update :locals merge
                                        (:locals env)
                                        {binding-sym (ana/analyze range-arg
                                                                  (-> (ana/empty-env)
                                                                      (update :locals merge
                                                                              (:locals env))))})))
                           -transpile)
                       str/split-lines
                       (mapv #(str "  " % ";"))
                       (str/join))))))

    :invoke
    (case (symbol (:var (:fn v)))
      (clojure.core/* clojure.core/+)
      (if (= (count (:args v)) 1)
        (-transpile (first (:args v)))
        (throw (ex-info "Unsupported" {:op (:op v)
                                       :args v
                                       :form (:form v)}))))

    :var
    @(:var v)

    (do #_(def v v)
        #_ (:op v)
        #_ (keys v)
        (throw (ex-info (str "Unhandled: " (:op v)) {:op (:op v)
                                                     :raw-form (:raw-forms v)
                                                     :form (:form v)})))))

(defn transpile
  [form]
  (let [transpiled (->> ["#define VYBE_EXPORT __attribute__((__visibility__(\"default\")))"
                         (-transpile (ana/analyze form))]
                        (str/join "\n\n"))]
    transpiled))
#_ (defonce ^:private *plugin-idx (atom 0))

(defn -c-compile
  [code-form]
  (let [path-prefix (str (System/getProperty "user.dir") "/resources/vybe/dynamic")
        c-code (-> code-form transpile)
        lib-name (format "lib%s.dylib" (str "vybe_" (abs (hash [code-form c-code]))))
        lib-full-path (str path-prefix "/" lib-name)
        file (io/file lib-full-path)]
    (io/make-parents file)
    (if (.exists file)
      {:lib-full-path lib-full-path
       :code-form code-form}

      (do (spit "/tmp/a.c" c-code)
          (println c-code)
          (let [{:keys [err]} (proc/sh (format "clang -shared /tmp/a.c -o %s" lib-full-path))]
            (when (seq err)
              (throw (ex-info (str "Found error when compiling C code:\n" err) {:c-code c-code}))))
          {:lib-full-path lib-full-path
           :code-form code-form}))))

(defmacro c-compile
  "Macro that compiles a form to c and generates a shared lib out of it.
  It returns the shared lib full path.

  E,g.
    (c-compile
      (defn olha
        ^float [^float v]
        (* 0.9 v)))"
  [& code]
  `(-c-compile
    (quote (do ~@code))))

(defmacro defc
  "Create a C function that can be used in other C functions."
  {:clj-kondo/lint-as 'clojure.core/defn}
  [n & fn-tail]
  `(do (def ~n
         (quote (defn ~n ~@fn-tail)))
       (var ~n)))

(defmacro defdsp
  "Create a DSP, it's similar to `defc`, but here is where the actual compilation
  will happen."
  {:clj-kondo/lint-as 'clojure.core/defn}
  [n & fn-tail]
  `(do (def ~n
         (c-compile
           (defn ~n ~@fn-tail)))
       (snd "/cmd" "/vybe_dlopen" (:lib-full-path ~n) ~(str n))
       (var ~n)))

(def lala2 0.5)

(defdsp mydsp
  ^void [^floats output
         ^floats input
         ^int n_samples]
  (doseq [i (range n_samples)]
    (aset output i (* (+ (* (aget input i)
                            0.5)
                         #_(* (aget input (if (> i 10)
                                            (- i 9)
                                            i))
                              0.2))
                      ))))

(comment
  #_overtone.sc.machinery.server.connection/connection-info*

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
