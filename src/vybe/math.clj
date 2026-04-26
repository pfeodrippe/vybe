(ns vybe.math
  "Common math functions."
  (:require
   [vybe.raylib.c :as vr.c]
   [vybe.type :as vt]
   [vybe.c :as vc])
  (:import
   (vybe.flecs VybeFlecsEntitySet)))

(defn matrix-transform
  [translation rotation scale]
  (let [mat-scale (if scale
                    (vr.c/matrix-scale (:x scale) (:y scale) (:z scale))
                    (vr.c/matrix-scale 1 1 1))
        mat-rotation (vr.c/quaternion-to-matrix (or rotation (vt/Rotation {:x 0 :y 0 :z 0 :w 1})))
        mat-translation (if translation
                          (vr.c/matrix-translate (:x translation) (:y translation) (:z translation))
                          (vr.c/matrix-translate 0 0 0))]
    (vr.c/matrix-multiply (vr.c/matrix-multiply mat-scale mat-rotation) mat-translation)))
#_(matrix-transform
   (vt/Translation [0 0 0])
   (vt/Rotation [0 0 0 1])
   #_(vr.c/matrix-identity)
   (vt/Scale [1 1 1]))

(defn matrix-decompose
  "Decompose matrix into translation, rotation and scale."
  [matrix]
  (let [t (vt/Translation)
        r (vt/Rotation)
        s (vt/Scale)
        _ (vr.c/matrix-decompose matrix t r s)]
    {:translation t
     :rotation r
     :scale s}))

(defn matrix->translation
  [matrix]
  (vt/Translation ((juxt :m12 :m13 :m14) matrix)))

(defn matrix->scale
  [matrix]
  #_(def matrix (vp/clone matrix))
  #_(vt/Scale ((juxt :m0 :m5 :m10) matrix))
  (vt/Vector3 [(vr.c/vector-3-length (vt/Vector3 ((juxt :m0 :m1 :m2) matrix)))
               (vr.c/vector-3-length (vt/Vector3 ((juxt :m4 :m5 :m6) matrix)))
               (vr.c/vector-3-length (vt/Vector3 ((juxt :m8 :m9 :m10) matrix)))]))
#_ (vr.c/vector-3-length (vt/Vector3 ((juxt :m0 :m1 :m2) matrix)))
#_ (vr.c/vector-3-length (vt/Vector3 ((juxt :m4 :m5 :m6) matrix)))
#_ (vr.c/vector-3-length (vt/Vector3 ((juxt :m8 :m9 :m10) matrix)))

(defn- normalize-rotation
  [v]
  (let [x (double (or (:x v) 0.0))
        y (double (or (:y v) 0.0))
        z (double (or (:z v) 0.0))
        w (double (or (:w v) 0.0))
        length (Math/sqrt (+ (* x x) (* y y) (* z z) (* w w)))]
    (if (zero? length)
      (vt/Rotation [0 0 0 1])
      (vt/Rotation [(/ x length) (/ y length) (/ z length) (/ w length)]))))

(defn matrix->rotation
  [matrix]
  (-> (vr.c/quaternion-from-matrix matrix)
      normalize-rotation))

(defn matrix->rotation-c
  [matrix]
  (matrix->rotation @matrix))

(defmethod vc/c-invoke #'matrix->rotation-c
  [{:keys [args]}]
  (let [[matrix] (mapv vc/emit args)
        transform-name (vc/->name vt/Transform)]
    (format "({typedef struct { float x; float y; float z; float w; } vybe_rotation_value_t; __auto_type m__ = *((%s*)({%s;})); vybe_rotation_value_t out__; float trace__ = m__.m0 + m__.m5 + m__.m10; if (trace__ > 0.0f) { float s__ = 2.0f * sqrtf(fmaxf(0.0f, 1.0f + trace__)); out__ = (vybe_rotation_value_t){.x = (m__.m6 - m__.m9) / s__, .y = (m__.m8 - m__.m2) / s__, .z = (m__.m1 - m__.m4) / s__, .w = s__ * 0.25f}; } else if ((m__.m0 > m__.m5) && (m__.m0 > m__.m10)) { float s__ = 2.0f * sqrtf(fmaxf(0.0f, 1.0f + m__.m0 - m__.m5 - m__.m10)); out__ = (vybe_rotation_value_t){.x = s__ * 0.25f, .y = (m__.m4 + m__.m1) / s__, .z = (m__.m8 + m__.m2) / s__, .w = (m__.m6 - m__.m9) / s__}; } else if (m__.m5 > m__.m10) { float s__ = 2.0f * sqrtf(fmaxf(0.0f, 1.0f + m__.m5 - m__.m0 - m__.m10)); out__ = (vybe_rotation_value_t){.x = (m__.m4 + m__.m1) / s__, .y = s__ * 0.25f, .z = (m__.m9 + m__.m6) / s__, .w = (m__.m8 - m__.m2) / s__}; } else { float s__ = 2.0f * sqrtf(fmaxf(0.0f, 1.0f + m__.m10 - m__.m0 - m__.m5)); out__ = (vybe_rotation_value_t){.x = (m__.m8 + m__.m2) / s__, .y = (m__.m9 + m__.m6) / s__, .z = s__ * 0.25f, .w = (m__.m1 - m__.m4) / s__}; } float len__ = sqrtf(out__.x * out__.x + out__.y * out__.y + out__.z * out__.z + out__.w * out__.w); if (len__ == 0.0f) { out__ = (vybe_rotation_value_t){.x = 0.0f, .y = 0.0f, .z = 0.0f, .w = 1.0f}; } else { out__.x = out__.x / len__; out__.y = out__.y / len__; out__.z = out__.z / len__; out__.w = out__.w / len__; } out__;})"
            transform-name matrix)))
#_ (= (matrix->rotation (matrix-transform
                         (vt/Translation [0 1 0])
                         (vt/Rotation [0 0.5 0 1])
                         (vt/Scale [1 1 1])))
      (matrix->rotation-c (matrix-transform
                           (vt/Translation [0 1 0])
                           (vt/Rotation [0 0.5 0 1])
                           (vt/Scale [1 1 1]))))

(defn rad->degree
  [v]
  (* v (/ 180.0 Math/PI)))

(defn distance
  "Distance between 2 entities."
  [^VybeFlecsEntitySet e1 ^VybeFlecsEntitySet e2]
  (vr.c/vector-3-distance
   (-> e1 (get [vt/Transform :global]) matrix->translation)
   (-> e2 (get [vt/Transform :global]) matrix->translation)))
