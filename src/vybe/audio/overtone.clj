(ns vybe.audio.overtone
  "Helper functions for overtone."
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [nextjournal.clerk :as clerk]
   [clojure.java.io :as io]
   [overtone.live :refer :all :as l]
   [overtone.midi :as midi]
   [overtone.sc.machinery.server.connection :as ov.conn]
   [overtone.config.store :as ov.config]
   [overtone.helpers.file :as ov.file]
   [overtone.sc.defaults :as ov.defaults]
   [overtone.helpers.system :refer [get-os linux-os? mac-os? windows-os?]]
   [overtone.config.log :as ov.log]
   [overtone.helpers.lib :as ov.lib]
   [overtone.sc.machinery.ugen.special-ops :as special-ops]
   [clojure.tools.build.api :as b]
   [clojure.string :as str]
   [vybe.util :as vy.u]
   [instaparse.core :as insta]))

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

(def doc
  "CLASS:: FoaRotate
summary:: First Order Ambisonic (FOA) rotation transformer
categories:: Libraries>Ambisonic Toolkit>UGens>FOA>Transformer, UGens>Multichannel>Ambisonics
related:: Classes/FoaTilt, Classes/FoaTumble, Classes/FoaRTT, Classes/FoaTransform, Classes/FoaXform, Classes/Rotate2

DESCRIPTION::
Rotate a First Order Ambisonic signal (B-format) around the z-axis.


The inbuilt equivalent is link::Classes/Rotate2::.


NOTE::
link::Classes/FoaRotate:: is usually called via the convenience wrapper link::Classes/FoaTransform::.
::


CLASSMETHODS::

METHOD:: ar

argument:: in
The B-format signal, an array: [w, x, y, z]

argument:: angle
Rotation angle, in radians.

argument:: mul
Output will be multiplied by this value.

argument:: add
This value will be added to the output.

discussion::
A rotation of pi/2 will rotate a source at code:: [0, 0] :: to code:: [pi/2, 0] ::.

note:: Corresponding matrix transformer: link::Classes/FoaXformerMatrix#*newRotate:: ::



EXAMPLES::

link::Guides/Transforming-FOA::.")

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

(comment

  (#'overtone.sc.machinery.ugen.fn-gen/def-ugen

    {:Name "MouseX",
     :args [{:name "min",
             :default 0.0
             :doc "minimum value (when mouse is at the left of the
                     screen)"}

            {:name "max",
             :default 1.0
             :doc "maximum value (when mouse is at the right of the
                     screen)"}

            {:name "warp",
             :default 0
             :doc "mapping curve - either LINEAR or EXPONENTIAL (LIN
                     and EXP abbreviations are allowed). Default is
                     LINEAR." }

            {:name "lag",
             :default 0.2
             :doc "lag factor to dezipper cursor movement." }],

     :rates #{:kr}
     :doc "maps the current mouse X coordinate to a value between min
             and max"})

  (->> (-> (str/replace doc #"\w*::" #(let [upper-cased (str/upper-case %)
                                            value (subs upper-cased 0 (- (count upper-cased) 2))]
                                        (if (contains? all-tags-set value)
                                          (str "___" value "___")
                                          %
                                          #_(str (subs % 0 (- (count %) 2)) "__END__"))))
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
       #_(reduce (fn [{:keys [end-count] :as acc}
                      [op v]]
                   (let [ends (count (re-seq #"__END__"
                                             "Classes/FoaXformerMatrix#*NEWROTATE__END__ __END__\n\n\n\n"))]))
                 {:end-count 0})
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

                 [section data]))))

  #_(->> (str/split doc #"::"))

  (def data
    '(["METHOD" "ar"]
      ["ARGUMENT" "in\nThe B-format signal, an array: [w, x, y, z]"]
      ["ARGUMENT" "angle\nRotation angle, in radians."]
      ["ARGUMENT" "mul\nOutput will be multiplied by this value."]
      ["ARGUMENT" "add\nThis value will be added to the output."]
      ["DISCUSSION" "A rotation of pi/2 will rotate a source at"]
      ["CODE" " [0, 0] :: to "]
      ["CODE" " [pi/2, 0] ::.\n\n"]
      ["NOTE" " Corresponding matrix transformer: "]
      ["LINK" "Classes/FoaXformerMatrix#*newRotate:: ::\n\n\n\n"]
      ["METHOD" "kr"]
      ["ARGUMENT" "in\nThe C-formt signal, an array: [w, x, y, z]"]
      ["ARGUMENT" "angle\nRotation angle, in radians."]
      ["ARGUMENT" "mul\nOutput will be multiplied by this value."]
      ["ARGUMENT" "add\nThis value will be added to the output."]
      ["DISCUSSION" "A rotation of pi/2 will rotate a source at"]
      ["CODE" " [0, 0] :: to "]
      ["CODE" " [pi/2, 0] ::.\n\n"]
      ["NOTE" " Corresponding matrix transformer: "]
      ["LINK" "Classes/FoaXformerMatrix#*newRotate:: ::\n\n\n\n"]))

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
