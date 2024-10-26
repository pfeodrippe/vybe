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

(comment

  ;; From https://depts.washington.edu/dxscdoc/Help/Reference/SCDocSyntax.html .
  ;; We have added/fixed a few rules to make it work properly.
  (insta/defparser schelp-parser
    "start             ::= document
document          ::= dochead optsections
                    | sections
                    | dochead optsections
dochead           ::= { headline } headline
headline          ::= ( headtag anyempty words2 | \"CATEGORIES::\" commalist | \"RELATED::\"
                      commalist ) eol
anyempty         ::= ' '*
headtag           ::= \"CLASS::\"
                    | \"TITLE::\"
                    | \"SUMMARY::\"
                    | \"REDIRECT::\"
sectiontag        ::= \"CLASSMETHODS::\"
                    | \"INSTANCEMETHODS::\"
                    | \"DESCRIPTION::\"
                    | \"EXAMPLES::\"
optsections       ::= [ sections ]
sections          ::= sections section
                    | section
                    | subsubsections
section           ::= ( \"SECTION::\" words2 eol | sectiontag anyempty ) optsubsections
optsubsections    ::= [ subsections ]
subsections       ::= subsections subsection
                    | subsection
                    | subsubsections
subsection        ::= \"SUBSECTION::\" words2 eol optsubsubsections
optsubsubsections ::= [ subsubsections ]
subsubsections    ::= subsubsections subsubsection
                    | subsubsection
                    | body
subsubsection     ::= \"METHOD::\" methnames optMETHODARGS eol methodbody
                    | \"COPYMETHOD::\" words eol
                    | \"PRIVATE::\" commalist eol
optMETHODARGS     ::= [ METHODARGS ]
methnames         ::= { METHODNAME COMMA } METHODNAME
methodbody        ::= optbody optargs optreturns optdiscussion
optbody           ::= [ body ]
optargs           ::= [ args ]
args              ::= { arg } arg
arg               ::= \"ARGUMENT::\" ( words eol optbody | eol body )
optreturns        ::= [ \"RETURNS::\" body ]
optdiscussion     ::= [ \"DISCUSSION::\" body ]
body              ::= blockA
                    | blockB
blockA            ::= [ blockB | blockA ] bodyelem
blockB            ::= [ blockA ] prose
bodyelem          ::= rangetag body \"::\"
                    | listtag listbody \"::\"
                    | \"TABLE::\" tablebody \"::\"
                    | \"DEFINITIONLIST::\" deflistbody \"::\"
                    (* | blocktag wordsnl \"::\" *)
                    | \"CLASSTREE::\" words eol
                    | \"KEYWORD::\" commalist eol
                    | EMPTYLINES
                    | \"IMAGE::\" words2 \"::\"
prose             ::= { proseelem } proseelem
proseelem         ::= anyword
                    | URL
                    | inlinetag words \"::\"
                    | \"FOOTNOTE::\" body \"::\"
                    | NEWLINE
inlinetag         ::= \"LINK::\"
                    | \"STRONG::\"
                    | \"SOFT::\"
                    | \"EMPHASIS::\"
                    | \"CODE::\"
                    | \"TELETYPE::\"
                    | \"ANCHOR::\"
(*blocktag          ::= CODEBLOCK | TELETYPEBLOCK*)
listtag           ::= \"LIST::\"
                    | \"TREE::\"
                    | \"NUMBEREDLIST::\"
rangetag          ::= \"WARNING::\"
                    | \"NOTE::\"
listbody          ::= { \"##\" body } \"##\" body
tablerow          ::= \"##\" tablecells
tablebody         ::= { tablerow } tablerow
tablecells        ::= { optbody \"||\" } optbody
defterms          ::= { \"##\" body } \"##\" body
deflistrow        ::= defterms \"||\" optbody
deflistbody       ::= { deflistrow } deflistrow
anywordurl        ::= anyword
                    | URL
anyword           ::= TEXT
                    | COMMA
words             ::= { anyword } anyword
words2            ::= { anywordurl } anywordurl
eol               ::= NEWLINE
                    | EMPTYLINES
anywordnl         ::= anyword
                    | eol
wordsnl           ::= { anywordnl } anywordnl
nocommawords      ::= nocommawords TEXT
                    | nocommawords URL
                    | TEXT
                    | URL
commalist         ::= { nocommawords COMMA } nocommawords
METHODNAME        ::= TEXT
COMMA             ::= ','
COMMA             ::= ','
TEXT              ::= (word | number | anyempty | '(' | ')' | '<' | '>' | '/' | '-')*
CODEBLOCK         ::= (word | number)*
URL               ::= #'http:\\/\\/[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]*'
NEWLINE           ::= '\n'
EMPTYLINES        ::= ''
METHODARGS        ::= '(' (TEXT | ' ')*  ')'
word ::= #'[a-zA-Z]+'
number ::= #'[0-9]+'
")

  (def parsed
    (schelp-parser (str/replace doc #"\w*::" str/upper-case)))

  #_(->> (str/split doc #"::"))

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
