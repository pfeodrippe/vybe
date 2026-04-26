(ns vybe.raylib.c
  (:require
   [clojure.java.io :as io]
   [vybe.c :as vc]
   [vybe.native.backend :as backend]
   [vybe.type :as vt]))

(set! *warn-on-reflection* true)

(if (backend/wasm?)
  (do
    (defn- mget
      [m idx]
      (double (or (get m (keyword (str "m" idx))) 0.0)))

    (defn- raylib-c-usages
      []
      (->> ["vybe/game.clj" "vybe/game/system.clj" "vybe/math.clj" "vybe/raylib.clj"]
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

    (doseq [sym (raylib-c-usages)
            :when (nil? (ns-resolve *ns* sym))]
      (intern *ns* sym no-raylib-wasm-export)))
  (do
    (require '[vybe.raylib.impl :as vr.impl])
    ((requiring-resolve 'vybe.raylib.impl/intern-methods))))

(comment

  ())
