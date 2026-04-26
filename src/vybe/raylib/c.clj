(ns vybe.raylib.c
  (:require
   [clojure.java.io :as io]
   [vybe.c :as vc]
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
      (let [cell (fn [row col]
                   (reduce +
                           (map (fn [idx]
                                  (* (mget a (+ row (* idx 4)))
                                     (mget b (+ idx (* col 4)))))
                                (range 4))))]
        (vt/Transform
         {:m0 (cell 0 0) :m4 (cell 0 1) :m8 (cell 0 2) :m12 (cell 0 3)
          :m1 (cell 1 0) :m5 (cell 1 1) :m9 (cell 1 2) :m13 (cell 1 3)
          :m2 (cell 2 0) :m6 (cell 2 1) :m10 (cell 2 2) :m14 (cell 2 3)
          :m3 (cell 3 0) :m7 (cell 3 1) :m11 (cell 3 2) :m15 (cell 3 3)})))

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
    (format "({__auto_type a__ = ({%s;}); __auto_type b__ = ({%s;}); (%s){.m0 = a__.m0*b__.m0 + a__.m4*b__.m1 + a__.m8*b__.m2 + a__.m12*b__.m3, .m4 = a__.m0*b__.m4 + a__.m4*b__.m5 + a__.m8*b__.m6 + a__.m12*b__.m7, .m8 = a__.m0*b__.m8 + a__.m4*b__.m9 + a__.m8*b__.m10 + a__.m12*b__.m11, .m12 = a__.m0*b__.m12 + a__.m4*b__.m13 + a__.m8*b__.m14 + a__.m12*b__.m15, .m1 = a__.m1*b__.m0 + a__.m5*b__.m1 + a__.m9*b__.m2 + a__.m13*b__.m3, .m5 = a__.m1*b__.m4 + a__.m5*b__.m5 + a__.m9*b__.m6 + a__.m13*b__.m7, .m9 = a__.m1*b__.m8 + a__.m5*b__.m9 + a__.m9*b__.m10 + a__.m13*b__.m11, .m13 = a__.m1*b__.m12 + a__.m5*b__.m13 + a__.m9*b__.m14 + a__.m13*b__.m15, .m2 = a__.m2*b__.m0 + a__.m6*b__.m1 + a__.m10*b__.m2 + a__.m14*b__.m3, .m6 = a__.m2*b__.m4 + a__.m6*b__.m5 + a__.m10*b__.m6 + a__.m14*b__.m7, .m10 = a__.m2*b__.m8 + a__.m6*b__.m9 + a__.m10*b__.m10 + a__.m14*b__.m11, .m14 = a__.m2*b__.m12 + a__.m6*b__.m13 + a__.m10*b__.m14 + a__.m14*b__.m15, .m3 = a__.m3*b__.m0 + a__.m7*b__.m1 + a__.m11*b__.m2 + a__.m15*b__.m3, .m7 = a__.m3*b__.m4 + a__.m7*b__.m5 + a__.m11*b__.m6 + a__.m15*b__.m7, .m11 = a__.m3*b__.m8 + a__.m7*b__.m9 + a__.m11*b__.m10 + a__.m15*b__.m11, .m15 = a__.m3*b__.m12 + a__.m7*b__.m13 + a__.m11*b__.m14 + a__.m15*b__.m15};})"
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

(doseq [sym (raylib-c-usages)
        :when (nil? (ns-resolve *ns* sym))]
  (intern *ns* sym no-raylib-wasm-export))

(comment

  ())
