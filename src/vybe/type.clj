(ns vybe.type
  "Common components for Vybe."
  (:require
   [vybe.panama :as vp]
   [vybe.type :as vt]))

(set! *warn-on-reflection* true)

(def docs
  "Builtin documentation."
  {:game/tags
   {:vg/camera-active
    {:doc "Current active camera will have this tag"}}

   :game/entities
   {:vg/root
    {:doc "Entity that stores some game entities used by Vybe"}}

   :game/gltf
   {:doc "In Blender, copy the script at https://github.com/pfeodrippe/vybe/blob/main/resources/com/pfeodrippe/vybe/blender/glb_export.py to add a Vybe Components panel"

    :vg/light
    {:doc "Tag a node as a light"}

    :vg/active
    {:doc "Default a node as active (e.g. if you have multiple cameras, you tag one of them so it's the default one)"}

    :vg/dynamic
    {:doc "Tag a node as dynamic, it will react to physics"}

    :vg/kinematic
    {:doc "Tag a node as kinematic, it won't react to physics, but its velocity can be set"}

    :vg/static
    {:doc "Tag a node as static (default), it won't react to physics"}}

   :game/events
   {:doc "Observers can listen (observe) events"

    :vg.raycast/on-click
    {:doc "Mouse click on a body"}

    :vg.raycast/on-hover
    {:doc "Mouse hover on a body. This is continuous, check `:vg.raycast/on-enter` for another option"}

    :vg.raycast/on-enter
    {:doc "Mouse entered a body"}

    :vg.raycast/on-leave
    {:doc "Mouse left all bodies (it's not for one body only!)"}

    :vg.window/on-close
    {:doc "When the user closes the window, it will trigger this event"}}})

(defmacro ^:private with-raylib
  [& body]
  (when (try (requiring-resolve 'vybe.raylib/draw)
             (catch Exception _))
    (require '[vybe.raylib.c :as vr.c])
    (require '[vybe.raylib :as vr])
    `(do ~@body)))

(defmacro ^:private with-flecs
  [& body]
  (when (try (requiring-resolve 'vybe.flecs/make-world)
             (catch Exception _))
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
  [[:kind :long]
   [:timeline_count :long]
   [:values :pointer]
   [:timeline :pointer]])

(vp/defcomp AnimationPlayer
  [[:current_time :float]])

(vp/defcomp Index
  [[:index :int]])

(vp/defcomp VBO
  "VBO, used with shaders"
  [[:id :int]])

(vp/defcomp Aabb
  "Axis-aligned bounding box, used for bodies"
  [[:min Vector3]
   [:max Vector3]])

(with-flecs
  (vp/defcomp Eid
    "Stores a long representing an entity id"
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

(vp/defcomp Scalar
  "Scalar that can be used in a pair, e.g. [(vt/Scalar 0.8) :vg.anim/speed]"
  [[:v :float]])

(vp/defcomp Str
  {:constructor (fn [s]
                  {:v (vp/arr (seq (.getBytes (str s "\0"))) :byte)})}
  "String that can be used in a pair, e.g. [(vt/Str \"My Str\") :something], maximum of 256 characters"
  [[:v {:getter (fn [v] (String. (byte-array (take-while #(not (zero? %)) v))))}
    [:vec {:size 256} :byte]]])
#_ (vt/Str "Olha Só!!\nEita")

(defonce ^:private *idx->clj (atom {}))
(defonce ^:private *clj->idx (atom {}))
(defonce ^:private *counter (atom 0))

(vp/defcomp Clj
  {:constructor (fn [v]
                  {:v (or (get @*clj->idx v)
                          (let [idx (swap! *counter inc)]
                            (swap! *idx->clj assoc idx v)
                            (swap! *clj->idx assoc v idx)
                            idx))})}
  "Can store a var, keyword, string, map etc, anything from Clojure. Useful to be used in a pair."
  [[:v {:getter (fn [idx]
                  (get @*idx->clj idx))}
    :long]])
#_ (vt/Clj #'map)
#_ (vt/Clj {:a 4})

(vp/defcomp EntityName
  [[:name :string]])

(vp/update-aliases!
 {'Vector2 Vector2
  'Vector3 Vector3
  'Vector4 Vector4
  'Matrix Transform})
