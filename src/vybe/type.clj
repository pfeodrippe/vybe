(ns vybe.type
  "Common components for Vybe."
  (:require
   [vybe.panama :as vp]))

(set! *warn-on-reflection* true)

(defmacro ^:private with-raylib
  [& body]
  (when (requiring-resolve 'vybe.raylib/draw)
    (require '[vybe.raylib.c :as vr.c])
    (require '[vybe.raylib :as vr])
    `(do ~@body)))

(defmacro ^:private with-flecs
  [& body]
  (when (requiring-resolve 'vybe.flecs/make-world)
    (require '[vybe.flecs.c :as vf.c])
    (require '[vybe.flecs :as vf])
    `(do ~@body)))

;; -- Raylib.
(with-raylib
  (vp/defcomp Camera (org.vybe.raylib.VyCamera/layout))
  (vp/defcomp Model (org.vybe.raylib.VyModel/layout))
  (vp/defcomp BoundingBox (org.vybe.raylib.BoundingBox/layout))

  (vp/defcomp Shader (org.vybe.raylib.Shader/layout))
  (defmethod vp/pmap-metadata Shader
    [v]
    (when-not (zero? (:id v))
      (->> (vr.c/vy-gl-get-active-parameters (:id v))
           (mapv #(into % {}))
           (into {})
           ((fn [params]
              (-> params
                  (update :attributes (fn [coll]
                                        (->> (take (:attributesCount params) coll)
                                             (mapv #(update (into {} %) :name vp/->string)))))
                  (update :uniforms (fn [coll]
                                      (->> (take (:uniformsCount params) coll)
                                           (mapv #(update (into {} %) :name vp/->string))))))))
           (into {})))))

;; -- Transform.
(vp/defcomp Vector2
  [[:x :float]
   [:y :float]])

(vp/defcomp Vector3
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Vector4
  [[:x :float]
   [:y :float]
   [:z :float]
   [:w :float]])

(vp/defcomp Matrix
  [[:m0 :float]
   [:m4 :float]
   [:m8 :float]
   [:m12 :float]
   [:m1 :float]
   [:m5 :float]
   [:m9 :float]
   [:m13 :float]
   [:m2 :float]
   [:m6 :float]
   [:m10 :float]
   [:m14 :float]
   [:m3 :float]
   [:m7 :float]
   [:m11 :float]
   [:m15 :float]])

(vp/defcomp Transform
  Matrix)

(vp/defcomp Vector4Byte
  [[:x :byte]
   [:y :byte]
   [:z :byte]
   [:w :byte]])

(vp/defcomp Translation
  Vector3)

(vp/defcomp Velocity
  Vector3)

(vp/defcomp Scale
  Vector3)

(vp/defcomp Rotation
  Vector4)

;; -- Animation.
(vp/defcomp AnimationChannel
  {:constructor (fn [v]
                  (if (:timeline_count v)
                    v
                    (assoc v :timeline_count (count (:timeline v)))))}
  [[:timeline_count :long]
   [:values :pointer]
   [:timeline :pointer]])

(vp/defcomp AnimationPlayer
  [[:current_time :float]])

(vp/defcomp Index
  [[:index :int]])

(vp/defcomp VBO
  [[:id :int]])

(vp/defcomp Aabb
  [[:min Vector3]
   [:max Vector3]])

(with-flecs
  (vp/defcomp Eid
    {:constructor (fn [maybe-id]
                    {:id (cond
                           (number? maybe-id)
                           maybe-id

                           (vf/entity? maybe-id)
                           (vf/entity-get-id maybe-id)

                           :else
                           (throw (ex-info "Unrecognized entity id for Eid"
                                           {:id maybe-id})))})}
    [[:id :long]]))

;; -- Misc.
(vp/defcomp ScreenSize
  [[:width :int]
   [:height :int]])
