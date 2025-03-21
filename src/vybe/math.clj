(ns vybe.math
  "Common math functions."
  (:require
   [vybe.raylib.c :as vr.c]
   [vybe.type :as vt]
   [vybe.jolt :as vj]
   [vybe.jolt.c :as vj.c]
   [vybe.c :as vc]
   [vybe.panama :as vp])
  (:import
   (org.vybe.raylib raylib)
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

(defn matrix->rotation
  [matrix]
  (-> (vr.c/quaternion-from-matrix matrix)
      vj/normalize))

(vc/defn* matrix->rotation-c :- vt/Rotation
  [matrix :- [:* vt/Matrix]]
  (let [out (vp/& (vt/Rotation))
        mat (vr.c/quaternion-from-matrix (vc/cast* @matrix vt/Transform))]
    (vj.c/jpc-vec-4-normalize (vp/& mat) out)
    @out))
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
  (* v (raylib/RAD2DEG)))

(defn distance
  "Distance between 2 entities."
  [^VybeFlecsEntitySet e1 ^VybeFlecsEntitySet e2]
  (vr.c/vector-3-distance
   (-> e1 (get [vt/Transform :global]) matrix->translation)
   (-> e2 (get [vt/Transform :global]) matrix->translation)))
