(ns vybe.raylib.c
  (:require
   [camel-snake-kebab.core :as csk]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [vybe.c :as vc]
   [vybe.panama :as panama]
   [vybe.raylib.abi :as abi]
   [vybe.raylib.browser :as browser]
   [vybe.raylib.wasm :as raylib-wasm]
   [vybe.wasm :as vw]
   [vybe.type :as vt]))

(set! *warn-on-reflection* true)

(defn- mget
  [m idx]
  (double (or (get m (keyword (str "m" idx))) 0.0)))

(defn- raylib-c-usages
  []
  (->> ["vybe/game.clj" "vybe/game/system.clj" "vybe/math.clj"
        "vybe/raylib.clj" "vybe/example/minimal.clj"]
       (keep io/resource)
       (mapcat #(re-seq #"vr\.c/([A-Za-z0-9_?!<>*+=.-]+)" (slurp %)))
       (map second)
       (map symbol)
       distinct))

(defn- no-raylib-wasm-export
  [& _]
  nil)

(defonce module* (delay (raylib-wasm/load-module)))
(defn module [] @module*)
(defn raw-call [name & args] (apply vw/call @module* name args))

(declare refresh-input-state!
         vy-get-screen-to-world-ray
         set-shader-value-v
         vy-set-shader-value-matrix-v)

(defn begin-frame-batch!
  []
  (refresh-input-state!)
  (browser/begin-frame!))

(defn end-frame-batch!
  []
  (browser/end-frame!))

(defn get-font-default [] 1)

    (defn matrix-identity
      []
      (vt/Transform {:m0 1.0 :m5 1.0 :m10 1.0 :m15 1.0}))

    (defn matrix-scale
      [x y z]
      (vt/Transform {:m0 (float x) :m5 (float y) :m10 (float z) :m15 1.0}))

    (defn matrix-translate
      [x y z]
      (vt/Transform {:m0 1.0 :m5 1.0 :m10 1.0 :m15 1.0
                     :m12 (float x) :m13 (float y) :m14 (float z)}))

	    (defn matrix-multiply
	      [a b]
	      (let [a0 (mget a 0)   a1 (mget a 1)   a2 (mget a 2)   a3 (mget a 3)
	            a4 (mget a 4)   a5 (mget a 5)   a6 (mget a 6)   a7 (mget a 7)
	            a8 (mget a 8)   a9 (mget a 9)   a10 (mget a 10) a11 (mget a 11)
	            a12 (mget a 12) a13 (mget a 13) a14 (mget a 14) a15 (mget a 15)
	            b0 (mget b 0)   b1 (mget b 1)   b2 (mget b 2)   b3 (mget b 3)
	            b4 (mget b 4)   b5 (mget b 5)   b6 (mget b 6)   b7 (mget b 7)
	            b8 (mget b 8)   b9 (mget b 9)   b10 (mget b 10) b11 (mget b 11)
	            b12 (mget b 12) b13 (mget b 13) b14 (mget b 14) b15 (mget b 15)]
	        (vt/Transform
	         {:m0 (+ (* a0 b0) (* a1 b4) (* a2 b8) (* a3 b12))
	          :m1 (+ (* a0 b1) (* a1 b5) (* a2 b9) (* a3 b13))
	          :m2 (+ (* a0 b2) (* a1 b6) (* a2 b10) (* a3 b14))
	          :m3 (+ (* a0 b3) (* a1 b7) (* a2 b11) (* a3 b15))
	          :m4 (+ (* a4 b0) (* a5 b4) (* a6 b8) (* a7 b12))
	          :m5 (+ (* a4 b1) (* a5 b5) (* a6 b9) (* a7 b13))
	          :m6 (+ (* a4 b2) (* a5 b6) (* a6 b10) (* a7 b14))
	          :m7 (+ (* a4 b3) (* a5 b7) (* a6 b11) (* a7 b15))
	          :m8 (+ (* a8 b0) (* a9 b4) (* a10 b8) (* a11 b12))
	          :m9 (+ (* a8 b1) (* a9 b5) (* a10 b9) (* a11 b13))
	          :m10 (+ (* a8 b2) (* a9 b6) (* a10 b10) (* a11 b14))
	          :m11 (+ (* a8 b3) (* a9 b7) (* a10 b11) (* a11 b15))
	          :m12 (+ (* a12 b0) (* a13 b4) (* a14 b8) (* a15 b12))
	          :m13 (+ (* a12 b1) (* a13 b5) (* a14 b9) (* a15 b13))
	          :m14 (+ (* a12 b2) (* a13 b6) (* a14 b10) (* a15 b14))
	          :m15 (+ (* a12 b3) (* a13 b7) (* a14 b11) (* a15 b15))})))

    (defn matrix-transpose
      [m]
      (vt/Transform
       {:m0 (:m0 m) :m4 (:m1 m) :m8 (:m2 m) :m12 (:m3 m)
        :m1 (:m4 m) :m5 (:m5 m) :m9 (:m6 m) :m13 (:m7 m)
        :m2 (:m8 m) :m6 (:m9 m) :m10 (:m10 m) :m14 (:m11 m)
        :m3 (:m12 m) :m7 (:m13 m) :m11 (:m14 m) :m15 (:m15 m)}))

    (defn quaternion-to-matrix
      [q]
      (let [x (double (or (:x q) 0.0))
            y (double (or (:y q) 0.0))
            z (double (or (:z q) 0.0))
            w (double (or (:w q) 1.0))
            xx (* x x) yy (* y y) zz (* z z)
            xy (* x y) xz (* x z) yz (* y z)
            wx (* w x) wy (* w y) wz (* w z)]
        (vt/Transform
         {:m0 (- 1.0 (* 2.0 (+ yy zz)))
          :m4 (* 2.0 (- xy wz))
          :m8 (* 2.0 (+ xz wy))
          :m12 0.0
          :m1 (* 2.0 (+ xy wz))
          :m5 (- 1.0 (* 2.0 (+ xx zz)))
          :m9 (* 2.0 (- yz wx))
          :m13 0.0
          :m2 (* 2.0 (- xz wy))
          :m6 (* 2.0 (+ yz wx))
          :m10 (- 1.0 (* 2.0 (+ xx yy)))
          :m14 0.0
          :m3 0.0 :m7 0.0 :m11 0.0 :m15 1.0})))

    (defn quaternion-from-matrix
      [m]
      (let [trace (+ (mget m 0) (mget m 5) (mget m 10))]
        (if (pos? trace)
          (let [s (* 2.0 (Math/sqrt (inc trace)))]
            (vt/Rotation [(/ (- (mget m 6) (mget m 9)) s)
                          (/ (- (mget m 8) (mget m 2)) s)
                          (/ (- (mget m 1) (mget m 4)) s)
                          (/ s 4.0)]))
          (vt/Rotation [0 0 0 1]))))

    (defn vector-3-length
      [v]
      (Math/sqrt (+ (* (double (or (:x v) 0.0)) (double (or (:x v) 0.0)))
                    (* (double (or (:y v) 0.0)) (double (or (:y v) 0.0)))
                    (* (double (or (:z v) 0.0)) (double (or (:z v) 0.0))))))


(defn vector-3-subtract
  [a b]
  (vt/Vector3 {:x (- (double (or (:x a) 0.0)) (double (or (:x b) 0.0)))
               :y (- (double (or (:y a) 0.0)) (double (or (:y b) 0.0)))
               :z (- (double (or (:z a) 0.0)) (double (or (:z b) 0.0)))}))

(defn vector-3-distance
  [a b]
  (vector-3-length (vector-3-subtract a b)))

(defn vector-3-lerp
  [a b t]
  (let [t (double t)]
    (vt/Vector3 {:x (+ (double (or (:x a) 0.0)) (* t (- (double (or (:x b) 0.0)) (double (or (:x a) 0.0)))))
                 :y (+ (double (or (:y a) 0.0)) (* t (- (double (or (:y b) 0.0)) (double (or (:y a) 0.0)))))
                 :z (+ (double (or (:z a) 0.0)) (* t (- (double (or (:z b) 0.0)) (double (or (:z a) 0.0)))))})))

(defn vector-3-transform
  [v m]
  (let [x (double (or (:x v) 0.0))
        y (double (or (:y v) 0.0))
        z (double (or (:z v) 0.0))]
    (vt/Vector3 {:x (+ (* x (mget m 0)) (* y (mget m 4)) (* z (mget m 8))  (mget m 12))
                 :y (+ (* x (mget m 1)) (* y (mget m 5)) (* z (mget m 9))  (mget m 13))
                 :z (+ (* x (mget m 2)) (* y (mget m 6)) (* z (mget m 10)) (mget m 14))})))

(defn matrix-invert
  [m]
  (let [a00 (mget m 0)  a01 (mget m 4)  a02 (mget m 8)  a03 (mget m 12)
        a10 (mget m 1)  a11 (mget m 5)  a12 (mget m 9)  a13 (mget m 13)
        a20 (mget m 2)  a21 (mget m 6)  a22 (mget m 10) a23 (mget m 14)
        a30 (mget m 3)  a31 (mget m 7)  a32 (mget m 11) a33 (mget m 15)
        b00 (- (* a00 a11) (* a01 a10))
        b01 (- (* a00 a12) (* a02 a10))
        b02 (- (* a00 a13) (* a03 a10))
        b03 (- (* a01 a12) (* a02 a11))
        b04 (- (* a01 a13) (* a03 a11))
        b05 (- (* a02 a13) (* a03 a12))
        b06 (- (* a20 a31) (* a21 a30))
        b07 (- (* a20 a32) (* a22 a30))
        b08 (- (* a20 a33) (* a23 a30))
        b09 (- (* a21 a32) (* a22 a31))
        b10 (- (* a21 a33) (* a23 a31))
        b11 (- (* a22 a33) (* a23 a32))
        det (+ (* b00 b11) (- (* b01 b10)) (* b02 b09)
               (* b03 b08) (- (* b04 b07)) (* b05 b06))]
    (if (zero? det)
      (matrix-identity)
      (let [inv-det (/ 1.0 det)]
        (vt/Transform
         {:m0 (* (+ (* a11 b11) (- (* a12 b10)) (* a13 b09)) inv-det)
          :m4 (* (+ (- (* a01 b11)) (* a02 b10) (- (* a03 b09))) inv-det)
          :m8 (* (+ (* a31 b05) (- (* a32 b04)) (* a33 b03)) inv-det)
          :m12 (* (+ (- (* a21 b05)) (* a22 b04) (- (* a23 b03))) inv-det)
          :m1 (* (+ (- (* a10 b11)) (* a12 b08) (- (* a13 b07))) inv-det)
          :m5 (* (+ (* a00 b11) (- (* a02 b08)) (* a03 b07)) inv-det)
          :m9 (* (+ (- (* a30 b05)) (* a32 b02) (- (* a33 b01))) inv-det)
          :m13 (* (+ (* a20 b05) (- (* a22 b02)) (* a23 b01)) inv-det)
          :m2 (* (+ (* a10 b10) (- (* a11 b08)) (* a13 b06)) inv-det)
          :m6 (* (+ (- (* a00 b10)) (* a01 b08) (- (* a03 b06))) inv-det)
          :m10 (* (+ (* a30 b04) (- (* a31 b02)) (* a33 b00)) inv-det)
          :m14 (* (+ (- (* a20 b04)) (* a21 b02) (- (* a23 b00))) inv-det)
          :m3 (* (+ (- (* a10 b09)) (* a11 b07) (- (* a12 b06))) inv-det)
          :m7 (* (+ (* a00 b09) (- (* a01 b07)) (* a02 b06)) inv-det)
          :m11 (* (+ (- (* a30 b03)) (* a31 b01) (- (* a32 b00))) inv-det)
          :m15 (* (+ (* a20 b03) (- (* a21 b01)) (* a22 b00)) inv-det)})))))

(defn quaternion-invert
  [q]
  (let [x (double (or (:x q) 0.0))
        y (double (or (:y q) 0.0))
        z (double (or (:z q) 0.0))
        w (double (or (:w q) 1.0))
        norm (+ (* x x) (* y y) (* z z) (* w w))]
    (if (zero? norm)
      (vt/Rotation [0 0 0 1])
      (vt/Rotation [(/ (- x) norm) (/ (- y) norm) (/ (- z) norm) (/ w norm)]))))

(defn vy-quaternion-to-axis-angle
  [q]
  (let [w (max -1.0 (min 1.0 (double (or (:w q) 1.0))))]
    (* 2.0 (Math/acos w))))

(defn vy-quaternion-to-axis-vector
  [q]
  (let [angle (vy-quaternion-to-axis-angle q)
        s (Math/sin (/ angle 2.0))]
    (if (< (Math/abs s) 1.0e-8)
      (vt/Vector3 [1 0 0])
      (vt/Vector3 [(/ (double (or (:x q) 0.0)) s)
                   (/ (double (or (:y q) 0.0)) s)
                   (/ (double (or (:z q) 0.0)) s)]))))

(defn matrix-rotate
  [axis angle]
  (let [x0 (double (or (:x axis) 0.0))
        y0 (double (or (:y axis) 0.0))
        z0 (double (or (:z axis) 0.0))
        len (Math/sqrt (+ (* x0 x0) (* y0 y0) (* z0 z0)))
        [x y z] (if (zero? len) [1.0 0.0 0.0] [(/ x0 len) (/ y0 len) (/ z0 len)])
        c (Math/cos angle)
        s (Math/sin angle)
        t (- 1.0 c)]
    (vt/Transform {:m0 (+ c (* x x t))
                   :m1 (+ (* y x t) (* z s))
                   :m2 (- (* z x t) (* y s))
                   :m3 0.0
                   :m4 (- (* x y t) (* z s))
                   :m5 (+ c (* y y t))
                   :m6 (+ (* z y t) (* x s))
                   :m7 0.0
                   :m8 (+ (* x z t) (* y s))
                   :m9 (- (* y z t) (* x s))
                   :m10 (+ c (* z z t))
                   :m11 0.0
                   :m12 0.0 :m13 0.0 :m14 0.0 :m15 1.0})))

(defn quaternion-slerp
  [a b t]
  (let [t (double t)
        lerp (fn [k] (+ (double (or (k a) 0.0))
                        (* t (- (double (or (k b) 0.0))
                                (double (or (k a) 0.0))))))
        x (lerp :x) y (lerp :y) z (lerp :z) w (lerp :w)
        len (Math/sqrt (+ (* x x) (* y y) (* z z) (* w w)))]
    (if (zero? len)
      (vt/Rotation [0 0 0 1])
      (vt/Rotation [(/ x len) (/ y len) (/ z len) (/ w len)]))))

(defn vector-2-zero [] (vt/Vector2))

    (defn vector-2-add
      [a b]
      (vt/Vector2 {:x (+ (float (or (:x a) 0.0))
                         (float (or (:x b) 0.0)))
                   :y (+ (float (or (:y a) 0.0))
                         (float (or (:y b) 0.0)))}))

    (defn vector-2-subtract
      [a b]
      (vt/Vector2 {:x (- (float (or (:x a) 0.0))
                         (float (or (:x b) 0.0)))
                   :y (- (float (or (:y a) 0.0))
                         (float (or (:y b) 0.0)))}))

    (defn- vector-2-c-expr
      [args op]
      (let [[a b] (mapv vc/emit args)
            c-name (vc/->name vt/Vector2)]
        (format "({__auto_type a__ = ({%s;}); __auto_type b__ = ({%s;}); (%s){.x = a__.x %s b__.x, .y = a__.y %s b__.y};})"
                a b c-name op op)))

    (defmethod vc/c-invoke #'vector-2-add
      [{:keys [args]}]
      (vector-2-c-expr args "+"))

    (defmethod vc/c-invoke #'vector-2-subtract
      [{:keys [args]}]
      (vector-2-c-expr args "-"))

(defn- transform-c-type [] (vc/->name vt/Transform))
(defn- vector-3-c-type [] (vc/->name vt/Vector3))
(defn- rotation-c-type [] (vc/->name vt/Rotation))

(defmethod vc/c-invoke #'matrix-scale
  [{:keys [args]}]
  (let [[x y z] (mapv vc/emit args)
        c-name (transform-c-type)]
    (format "((%s){.m0 = %s, .m5 = %s, .m10 = %s, .m15 = 1.0f})"
            c-name x y z)))

(defmethod vc/c-invoke #'matrix-translate
  [{:keys [args]}]
  (let [[x y z] (mapv vc/emit args)
        c-name (transform-c-type)]
    (format "((%s){.m0 = 1.0f, .m5 = 1.0f, .m10 = 1.0f, .m15 = 1.0f, .m12 = %s, .m13 = %s, .m14 = %s})"
            c-name x y z)))

(defmethod vc/c-invoke #'matrix-multiply
  [{:keys [args]}]
  (let [[a b] (mapv vc/emit args)
        c-name (transform-c-type)]
    (format "({__auto_type a__ = ({%s;}); __auto_type b__ = ({%s;}); (%s){.m0 = a__.m0*b__.m0 + a__.m1*b__.m4 + a__.m2*b__.m8 + a__.m3*b__.m12, .m1 = a__.m0*b__.m1 + a__.m1*b__.m5 + a__.m2*b__.m9 + a__.m3*b__.m13, .m2 = a__.m0*b__.m2 + a__.m1*b__.m6 + a__.m2*b__.m10 + a__.m3*b__.m14, .m3 = a__.m0*b__.m3 + a__.m1*b__.m7 + a__.m2*b__.m11 + a__.m3*b__.m15, .m4 = a__.m4*b__.m0 + a__.m5*b__.m4 + a__.m6*b__.m8 + a__.m7*b__.m12, .m5 = a__.m4*b__.m1 + a__.m5*b__.m5 + a__.m6*b__.m9 + a__.m7*b__.m13, .m6 = a__.m4*b__.m2 + a__.m5*b__.m6 + a__.m6*b__.m10 + a__.m7*b__.m14, .m7 = a__.m4*b__.m3 + a__.m5*b__.m7 + a__.m6*b__.m11 + a__.m7*b__.m15, .m8 = a__.m8*b__.m0 + a__.m9*b__.m4 + a__.m10*b__.m8 + a__.m11*b__.m12, .m9 = a__.m8*b__.m1 + a__.m9*b__.m5 + a__.m10*b__.m9 + a__.m11*b__.m13, .m10 = a__.m8*b__.m2 + a__.m9*b__.m6 + a__.m10*b__.m10 + a__.m11*b__.m14, .m11 = a__.m8*b__.m3 + a__.m9*b__.m7 + a__.m10*b__.m11 + a__.m11*b__.m15, .m12 = a__.m12*b__.m0 + a__.m13*b__.m4 + a__.m14*b__.m8 + a__.m15*b__.m12, .m13 = a__.m12*b__.m1 + a__.m13*b__.m5 + a__.m14*b__.m9 + a__.m15*b__.m13, .m14 = a__.m12*b__.m2 + a__.m13*b__.m6 + a__.m14*b__.m10 + a__.m15*b__.m14, .m15 = a__.m12*b__.m3 + a__.m13*b__.m7 + a__.m14*b__.m11 + a__.m15*b__.m15};})"
            a b c-name)))

(defmethod vc/c-invoke #'quaternion-to-matrix
  [{:keys [args]}]
  (let [[q] (mapv vc/emit args)
        c-name (transform-c-type)]
    (format "({__auto_type q__ = ({%s;}); float xx__ = q__.x*q__.x; float yy__ = q__.y*q__.y; float zz__ = q__.z*q__.z; float xy__ = q__.x*q__.y; float xz__ = q__.x*q__.z; float yz__ = q__.y*q__.z; float wx__ = q__.w*q__.x; float wy__ = q__.w*q__.y; float wz__ = q__.w*q__.z; (%s){.m0 = 1.0f - 2.0f*(yy__ + zz__), .m4 = 2.0f*(xy__ - wz__), .m8 = 2.0f*(xz__ + wy__), .m12 = 0.0f, .m1 = 2.0f*(xy__ + wz__), .m5 = 1.0f - 2.0f*(xx__ + zz__), .m9 = 2.0f*(yz__ - wx__), .m13 = 0.0f, .m2 = 2.0f*(xz__ - wy__), .m6 = 2.0f*(yz__ + wx__), .m10 = 1.0f - 2.0f*(xx__ + yy__), .m14 = 0.0f, .m3 = 0.0f, .m7 = 0.0f, .m11 = 0.0f, .m15 = 1.0f};})"
            q c-name)))

(defmethod vc/c-invoke #'vector-3-lerp
  [{:keys [args]}]
  (let [[a b t] (mapv vc/emit args)
        c-name (vector-3-c-type)]
    (format "({__auto_type a__ = ({%s;}); __auto_type b__ = ({%s;}); float t__ = %s; (%s){.x = a__.x + t__*(b__.x - a__.x), .y = a__.y + t__*(b__.y - a__.y), .z = a__.z + t__*(b__.z - a__.z)};})"
            a b t c-name)))

(defmethod vc/c-invoke #'quaternion-slerp
  [{:keys [args]}]
  (let [[a b t] (mapv vc/emit args)
        c-name (rotation-c-type)]
    (format "({__auto_type a__ = ({%s;}); __auto_type b__ = ({%s;}); float t__ = %s; float x__ = a__.x + t__*(b__.x - a__.x); float y__ = a__.y + t__*(b__.y - a__.y); float z__ = a__.z + t__*(b__.z - a__.z); float w__ = a__.w + t__*(b__.w - a__.w); float len__ = sqrtf(x__*x__ + y__*y__ + z__*z__ + w__*w__); (%s){.x = len__ == 0.0f ? 0.0f : x__/len__, .y = len__ == 0.0f ? 0.0f : y__/len__, .z = len__ == 0.0f ? 0.0f : z__/len__, .w = len__ == 0.0f ? 1.0f : w__/len__};})"
            a b t c-name)))

(defn- pointer-type?
  [ctype]
  (str/includes? (or ctype "") "*"))

(defn- base-type
  [ctype]
  (let [aliases (:type-aliases (abi/abi))
        base (-> (or ctype "")
                 (str/replace #"\bconst\b" "")
                 (str/replace #"\bstruct\s+" "")
                 (str/replace #"\*" "")
                 str/trim
                 (str/split #"\s+")
                 last)]
    (loop [base base]
      (if-let [target (get aliases base)]
        (recur target)
        base))))

(defn- aggregate-type?
  [ctype]
  (and (not (pointer-type? ctype))
       (contains? (:layouts (abi/abi)) (keyword (base-type ctype)))))

(def ^:private public-raylib-components
  {"Camera3D" 'vybe.raylib/Camera
   "Image" 'vybe.raylib/Image
   "Material" 'vybe.raylib/Material
   "MaterialMap" 'vybe.raylib/MaterialMap
   "Mesh" 'vybe.raylib/Mesh
   "Model" 'vybe.raylib/Model
   "Rectangle" 'vybe.raylib/Rectangle
   "RenderTexture" 'vybe.raylib/RenderTexture2D
   "Shader" 'vybe.raylib/Shader
   "Texture" 'vybe.raylib/Texture})

(defn- cached-public-component
  [c-type]
  (when-let [component-id (some-> (get public-raylib-components c-type)
                                  panama/comp-cache)]
    (panama/comp-cache component-id)))

(defn- component-for-c-type
  [ctype]
  (let [base (base-type ctype)]
    (case (str (or ctype ""))
      "Matrix" vt/Transform
      "Vector2" vt/Vector2
      "Vector3" vt/Vector3
      "Vector4" vt/Vector4
      "Quaternion" vt/Rotation
      (or (cached-public-component base)
          (abi/component (keyword base))))))

(defn- keywordize-keys
  [v]
  (cond
    (map? v) (into {} (map (fn [[k value]]
                             [(if (string? k) (keyword k) k)
                              (keywordize-keys value)]))
                   v)
    (vector? v) (mapv keywordize-keys v)
    :else v))

(defn- mem
  [v]
  (cond
    (nil? v) 0
    (number? v) (long v)
    :else (let [p (panama/mem v)]
            (if (instance? java.lang.foreign.MemorySegment p)
              (.address ^java.lang.foreign.MemorySegment p)
              p))))

(defn- write-aggregate!
  [ptr ctype v]
  (let [component (if (vw/mem? v)
                    (vw/component v)
                    (component-for-c-type ctype))
        value (cond
                (map? v) (into {} v)
                (sequential? v) v
                :else (into {} v))]
    (vw/write-component! @module* component ptr value)))

(defn- with-arg
  [{:keys [ctype schema]} v f]
  (cond
    (aggregate-type? ctype)
    (let [component (if (vw/mem? v)
                      (vw/component v)
                      (component-for-c-type ctype))
          ptr (vw/malloc @module* (vw/sizeof component))]
      (try
        (vw/zero! @module* ptr (vw/sizeof component))
        (write-aggregate! ptr ctype v)
        (f ptr)
        (finally
          (vw/free @module* ptr))))

    (and (= schema :pointer) (string? v))
    (vw/with-c-string* @module* v f)

    (= schema :float)
    (f (Float/floatToRawIntBits (float v)))

    (= schema :double)
    (f (Double/doubleToRawLongBits (double v)))

    (= schema :boolean)
    (f (if v 1 0))

    :else
    (f (mem v))))

(defn- with-args
  [arg-descs args f]
  (if-let [arg-desc (first arg-descs)]
    (with-arg arg-desc (first args)
      (fn [v]
        (with-args (rest arg-descs) (rest args)
          #(f (cons v %)))))
    (f '())))

(defn- ret-value
  [ret raw]
  (let [{:keys [schema ctype]} ret]
    (case schema
      :void nil
      :boolean (not (zero? (long raw)))
      :float (vw/raw-i32->float raw)
      :double (vw/raw-i64->double raw)
      :uint (Integer/toUnsignedLong (int raw))
      raw)))

(defonce ^:private input-state*
  (atom {:keys-down #{}
         :keys-pressed #{}
         :keys-released #{}
         :mouse-down #{}
         :mouse-pressed #{}
         :mouse-released #{}
         :mouse-position (vt/Vector2)
         :mouse-delta (vt/Vector2)
         :focused? true}))

(defn- long-set
  [xs]
  (into #{} (map long) (or xs [])))

(defn refresh-input-state!
  []
  (let [{:keys [keysDown keysPressed keysReleased
                mouseDown mousePressed mouseReleased
                mouseX mouseY mouseDeltaX mouseDeltaY focused]}
        (browser/input-state!)]
    (reset! input-state*
            {:keys-down (long-set keysDown)
             :keys-pressed (long-set keysPressed)
             :keys-released (long-set keysReleased)
             :mouse-down (long-set mouseDown)
             :mouse-pressed (long-set mousePressed)
             :mouse-released (long-set mouseReleased)
             :mouse-position (vt/Vector2 [(double (or mouseX 0.0))
                                          (double (or mouseY 0.0))])
             :mouse-delta (vt/Vector2 [(double (or mouseDeltaX 0.0))
                                       (double (or mouseDeltaY 0.0))])
             :focused? (boolean focused)}))
  nil)

(defn is-window-focused
  []
  (:focused? @input-state*))

(defn is-key-down
  [key]
  (contains? (:keys-down @input-state*) (long key)))

(defn is-key-pressed
  [key]
  (contains? (:keys-pressed @input-state*) (long key)))

(defn is-mouse-button-pressed
  [button]
  (contains? (:mouse-pressed @input-state*) (long button)))

(defn is-mouse-button-released
  [button]
  (contains? (:mouse-released @input-state*) (long button)))

(defn get-mouse-position
  []
  (:mouse-position @input-state*))

(defn get-mouse-delta
  []
  (:mouse-delta @input-state*))

(defn set-mouse-position
  [x y]
  (swap! input-state* assoc
         :mouse-position (vt/Vector2 [(double x) (double y)])
         :mouse-delta (vt/Vector2))
  (browser/set-mouse-position! x y))

(defn- generated-call
  [c-name call-args]
  (let [{:keys [ret]} (abi/function-data c-name)
        ret-ctype (:ctype ret)
        result (browser/call! c-name call-args)]
    (if (aggregate-type? ret-ctype)
      ((component-for-c-type ret-ctype) (keywordize-keys result))
      result)))

(doseq [[c-name _] (:functions (abi/abi))
        :when (not (str/starts-with? c-name "vybe_raylib_"))
        :let [clj-name (csk/->kebab-case-symbol c-name)
              existing (ns-resolve *ns* clj-name)
              v (or (when (and existing (bound? existing))
                      existing)
                    (intern *ns* clj-name
                            (fn [& args] (generated-call c-name args))))]]
  (alter-meta! v assoc
               :vybe/wasm-fn (abi/function-desc c-name)
               :vybe/fn-meta {:fn-desc (abi/function-desc c-name)
                              :fn-address 0}))

(defonce ^:private browser-screen*
  (atom {:width 600
         :height 600
         :ready? false}))

(defonce ^:private browser-clock*
  (atom {:started-ns (System/nanoTime)
         :last-frame-ns nil
         :frame-time (/ 1.0 60.0)
         :frame-id 0}))

(defonce ^:private screen-ray-cache*
  (atom nil))

(defonce ^:private raylib-state*
  (atom {:target-stack []
         :camera-stack []
         :cull-near 0.01
         :cull-far 1000.0}))

(defonce ^:private render-texture-size-cache*
  (atom {}))

(def ^:private deg->rad
  (/ Math/PI 180.0))

(defn- popv
  [v]
  (if (seq v) (pop v) []))

(defn- current-target-size
  []
  (or (peek (:target-stack @raylib-state*))
      [(:width @browser-screen*) (:height @browser-screen*)]))

(defn- render-texture-size
  [target]
  (let [cache-key [(:id target) (get-in target [:texture :id])]]
    (or (get @render-texture-size-cache* cache-key)
        (let [texture (:texture target)
              size [(long (or (:width texture) (:width target) (:width @browser-screen*)))
                    (long (or (:height texture) (:height target) (:height @browser-screen*)))]]
          (swap! render-texture-size-cache* assoc cache-key size)
          size))))

(defn- current-camera
  []
  (peek (:camera-stack @raylib-state*)))

(defn- normalize-vy-camera
  [camera]
  (if (:camera camera)
    camera
    {:camera camera
     :rotation (vt/Rotation [0 0 0 1])}))

(defn- matrix-perspective
  [fov-y aspect near-plane far-plane]
  (let [top (* near-plane (Math/tan (* fov-y 0.5)))
        bottom (- top)
        right (* top aspect)
        left (- right)
        rl (- right left)
        tb (- top bottom)
        fn (- far-plane near-plane)]
    (vt/Transform
     {:m0 (/ (* near-plane 2.0) rl)
      :m1 0.0
      :m2 0.0
      :m3 0.0
      :m4 0.0
      :m5 (/ (* near-plane 2.0) tb)
      :m6 0.0
      :m7 0.0
      :m8 (/ (+ right left) rl)
      :m9 (/ (+ top bottom) tb)
      :m10 (- (/ (+ far-plane near-plane) fn))
      :m11 -1.0
      :m12 0.0
      :m13 0.0
      :m14 (- (/ (* far-plane near-plane 2.0) fn))
      :m15 0.0})))

(defn- matrix-ortho
  [left right bottom top near-plane far-plane]
  (let [rl (- right left)
        tb (- top bottom)
        fn (- far-plane near-plane)]
    (vt/Transform
     {:m0 (/ 2.0 rl)
      :m1 0.0
      :m2 0.0
      :m3 0.0
      :m4 0.0
      :m5 (/ 2.0 tb)
      :m6 0.0
      :m7 0.0
      :m8 0.0
      :m9 0.0
      :m10 (- (/ 2.0 fn))
      :m11 0.0
      :m12 (- (/ (+ left right) rl))
      :m13 (- (/ (+ top bottom) tb))
      :m14 (- (/ (+ far-plane near-plane) fn))
      :m15 1.0})))

(defn- vy-matrix-view
  [vy-camera]
  (let [{:keys [camera rotation]} (normalize-vy-camera vy-camera)
        {:keys [x y z]} (:position camera)
        quat (quaternion-invert (or rotation (vt/Rotation [0 0 0 1])))]
    (matrix-multiply (matrix-translate (- (double (or x 0.0)))
                                       (- (double (or y 0.0)))
                                       (- (double (or z 0.0))))
                     (matrix-rotate (vy-quaternion-to-axis-vector quat)
                                    (vy-quaternion-to-axis-angle quat)))))

(defn begin-texture-mode
  [target]
  (let [result (generated-call "BeginTextureMode" [target])]
    (swap! raylib-state* update :target-stack conj (render-texture-size target))
    result))

(defn end-texture-mode
  []
  (let [result (generated-call "EndTextureMode" [])]
    (swap! raylib-state* update :target-stack popv)
    result))

(defn begin-mode-3-d
  [camera]
  (swap! raylib-state* update :camera-stack conj (normalize-vy-camera camera))
  (try
    (generated-call "BeginMode3D" [camera])
    (catch Throwable t
      (swap! raylib-state* update :camera-stack popv)
      (throw t))))

(defn end-mode-3-d
  []
  (let [result (generated-call "EndMode3D" [])]
    (swap! raylib-state* update :camera-stack popv)
    result))

(defn rl-set-clip-planes
  [near far]
  (swap! raylib-state* assoc
         :cull-near (double near)
         :cull-far (double far))
  (generated-call "rlSetClipPlanes" [near far]))

(defn rl-get-cull-distance-near
  []
  (:cull-near @raylib-state*))

(defn rl-get-cull-distance-far
  []
  (:cull-far @raylib-state*))

(defn rl-get-matrix-modelview
  []
  (if-let [camera (current-camera)]
    (vy-matrix-view camera)
    (generated-call "rlGetMatrixModelview" [])))

(defn rl-get-matrix-projection
  []
  (if-let [vy-camera (current-camera)]
    (let [{:keys [camera]} (normalize-vy-camera vy-camera)
          [width height] (current-target-size)
          aspect (/ (double width) (double (max 1 height)))
          fovy (double (or (:fovy camera) 45.0))
          near-plane (:cull-near @raylib-state*)
          far-plane (:cull-far @raylib-state*)]
      (if (= (long (or (:projection camera) 0)) 0)
        (matrix-perspective (* fovy deg->rad) aspect near-plane far-plane)
        (let [top (/ fovy 2.0)
              right (* top aspect)]
          (matrix-ortho (- right) right (- top) top near-plane far-plane))))
    (generated-call "rlGetMatrixProjection" [])))

(defn init-window
  [width height title]
  (let [result (generated-call "InitWindow" [width height title])]
    (swap! browser-screen* assoc
           :width (long width)
           :height (long height)
           :ready? true)
    result))

(defn is-window-ready
  []
  (or (:ready? @browser-screen*)
      (let [ready? (boolean (generated-call "IsWindowReady" []))]
        (when ready?
          (swap! browser-screen* assoc :ready? true))
        ready?)))

(defn get-screen-width
  []
  (:width @browser-screen*))

(defn get-screen-height
  []
  (:height @browser-screen*))

(defn get-time
  []
  (/ (- (System/nanoTime) (:started-ns @browser-clock*))
     1000000000.0))

(defn get-frame-time
  []
  (:frame-time @browser-clock*))

(defn vy-get-screen-to-world-ray
  [position camera]
  (let [frame-id (:frame-id @browser-clock*)
        cache-key [frame-id position camera]]
    (if (= cache-key (:key @screen-ray-cache*))
      (:ray @screen-ray-cache*)
      (let [ray (generated-call "VyGetScreenToWorldRay" [position camera])]
        (reset! screen-ray-cache* {:key cache-key
                                   :ray ray})
        ray))))

(defn begin-drawing
  []
  (let [now (System/nanoTime)]
    (swap! browser-clock*
           (fn [{:keys [last-frame-ns] :as state}]
	             (assoc state
	                    :last-frame-ns now
	                    :frame-id (inc (long (or (:frame-id state) 0)))
	                    :frame-time (if last-frame-ns
	                                  (/ (- now (long last-frame-ns))
	                                     1000000000.0)
                                  (:frame-time state)))))
    (generated-call "BeginDrawing" [])))

(defn end-drawing
  []
  (generated-call "EndDrawing" []))

(defonce ^:private shader-location-cache*
  (atom {}))

(defn get-shader-location
  [shader uniform-name]
  (let [shader-id (if (map? shader) (:id shader) shader)
        cache-key [shader-id (str uniform-name)]]
    (if-some [location (get @shader-location-cache* cache-key)]
      location
      (let [location (generated-call "GetShaderLocation" [shader uniform-name])]
        (swap! shader-location-cache* assoc cache-key location)
        location))))

(defn vector-3-add
  [a b]
  (vt/Vector3 {:x (+ (double (or (:x a) 0.0)) (double (or (:x b) 0.0)))
               :y (+ (double (or (:y a) 0.0)) (double (or (:y b) 0.0)))
               :z (+ (double (or (:z a) 0.0)) (double (or (:z b) 0.0)))}))

(defn vector-3-scale
  [v scale]
  (let [scale (double scale)]
    (vt/Vector3 {:x (* (double (or (:x v) 0.0)) scale)
                 :y (* (double (or (:y v) 0.0)) scale)
                 :z (* (double (or (:z v) 0.0)) scale)})))

(defn vector-3-multiply
  [a b]
  (vt/Vector3 {:x (* (double (or (:x a) 0.0)) (double (or (:x b) 0.0)))
               :y (* (double (or (:y a) 0.0)) (double (or (:y b) 0.0)))
               :z (* (double (or (:z a) 0.0)) (double (or (:z b) 0.0)))}))

(defn vector-3-cross-product
  [a b]
  (let [ax (double (or (:x a) 0.0))
        ay (double (or (:y a) 0.0))
        az (double (or (:z a) 0.0))
        bx (double (or (:x b) 0.0))
        by (double (or (:y b) 0.0))
        bz (double (or (:z b) 0.0))]
    (vt/Vector3 {:x (- (* ay bz) (* az by))
                 :y (- (* az bx) (* ax bz))
                 :z (- (* ax by) (* ay bx))})))

(defn vector-3-normalize
  [v]
  (let [len (double (vector-3-length v))]
    (if (zero? len)
      (vt/Vector3)
      (vector-3-scale v (/ 1.0 len)))))

(defn vector-3-rotate-by-quaternion
  [v q]
  (let [x (double (or (:x v) 0.0))
        y (double (or (:y v) 0.0))
        z (double (or (:z v) 0.0))
        qx (double (or (:x q) 0.0))
        qy (double (or (:y q) 0.0))
        qz (double (or (:z q) 0.0))
        qw (double (or (:w q) 1.0))
        qx2 (* qx qx)
        qy2 (* qy qy)
        qz2 (* qz qz)
        qw2 (* qw qw)]
    (vt/Vector3
     {:x (+ (* x (+ qx2 qw2 (- qy2) (- qz2)))
            (* y (- (* 2.0 qx qy) (* 2.0 qw qz)))
            (* z (+ (* 2.0 qx qz) (* 2.0 qw qy))))
      :y (+ (* x (+ (* 2.0 qw qz) (* 2.0 qx qy)))
            (* y (+ qw2 (- qx2) qy2 (- qz2)))
            (* z (- (* 2.0 qy qz) (* 2.0 qw qx))))
      :z (+ (* x (- (* 2.0 qx qz) (* 2.0 qw qy)))
            (* y (+ (* 2.0 qw qx) (* 2.0 qy qz)))
            (* z (+ qw2 (- qx2) (- qy2) qz2)))})))

(defn color-normalize
  [color]
  (vt/Vector4 {:x (/ (double (or (:r color) 0.0)) 255.0)
               :y (/ (double (or (:g color) 0.0)) 255.0)
               :z (/ (double (or (:b color) 0.0)) 255.0)
               :w (/ (double (or (:a color) 255.0)) 255.0)}))

(defonce ^:private browser-mesh-pointers*
  (atom {}))

(defn- mesh-key
  [mesh]
  [(long (or (:vaoId mesh) 0))
   (long (or (:vboId mesh) 0))
   (long (or (:vertexCount mesh) 0))
   (long (or (:triangleCount mesh) 0))])

(defn- with-browser-ptr
  [v ptr]
  (with-meta v (assoc (or (meta v) {}) :vybe.raylib.browser/ptr ptr)))

(defn- register-browser-mesh!
  [mesh ptr]
  (swap! browser-mesh-pointers* assoc (mesh-key mesh) (long ptr))
  (with-browser-ptr mesh (long ptr)))

(defn- browser-mesh-ptr
  [mesh]
  (or (:vybe.raylib.browser/ptr (meta mesh))
      (get @browser-mesh-pointers* (mesh-key mesh))))

(defn draw-mesh
  [mesh material transform]
  (if-let [mesh-ptr (browser-mesh-ptr mesh)]
    (generated-call "DrawMesh" [(with-browser-ptr mesh mesh-ptr) material transform])
    (generated-call "DrawMesh" [mesh material transform])))

(defn draw-mesh-instanced
  [mesh material transforms instances]
  (if-let [mesh-ptr (browser-mesh-ptr mesh)]
    (generated-call "DrawMeshInstanced"
                    [(with-browser-ptr mesh mesh-ptr) material transforms instances])
    (generated-call "DrawMeshInstanced" [mesh material transforms instances])))

(defn vy-begin-mode-3-d
  [camera]
  (swap! raylib-state* update :camera-stack conj (normalize-vy-camera camera))
  (try
    (generated-call "VyBeginMode3D" [camera])
    (catch Throwable t
      (swap! raylib-state* update :camera-stack popv)
      (throw t))))

(defn load-render-texture
  [width height]
  (generated-call "VyLoadRenderTexture" [width height]))

(defn load-model
  [file-name]
  (let [bytes (java.nio.file.Files/readAllBytes
		               (java.nio.file.Paths/get (str file-name) (make-array String 0)))
		        model-data (keywordize-keys (browser/load-model-from-bytes! bytes))
        mesh-component (component-for-c-type "Mesh")
        mesh-base-ptr (long (or (:meshes model-data) 0))
        mesh-size (long (abi/sizeof :Mesh))
		        model ((component-for-c-type "Model")
		               (select-keys model-data
		                            [:transform :meshCount :materialCount :boneCount
		                             :bones :meshes :materials :bindPose
		                             :meshMaterial]))]
	    (with-meta model
	      {:vybe.raylib.browser/meshes
		       (mapv (fn [idx mesh-data]
                   (register-browser-mesh!
                    (mesh-component mesh-data)
                    (+ mesh-base-ptr (* idx mesh-size))))
                 (range)
		             (:vybe.raylib.browser/meshes model-data))
		       :vybe.raylib.browser/materials
		       (mapv (component-for-c-type "Material")
		             (:vybe.raylib.browser/materials model-data))
	       :vybe.raylib.browser/mesh-materials
	       (vec (:vybe.raylib.browser/mesh-materials model-data))})))

(doseq [sym (raylib-c-usages)
        :when (nil? (ns-resolve *ns* sym))]
  (throw (ex-info "Missing generated Raylib Wasm export"
                  {:symbol sym})))

(comment

  ())
