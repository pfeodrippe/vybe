(ns vybe.system
  "Namespace for some default systems/queries/observers."
  (:require
   [vybe.flecs :as vf]
   [vybe.type :as vt]
   [vybe.panama :as vp]
   [vybe.raylib.c :as vr.c]
   [vybe.jolt :as vj]))

;; -- Physics.
(vf/defsystem update-model-meshes _w
  [translation [:out vt/Translation]
   rotation [:out vt/Rotation]
   body vj/VyBody
   :vf/always true ; TODO We shouldn't need this if we get the activate/deactivate events
   #_ #_:vf/disabled true
   _ :vg/dynamic]
  (let [pos (vj/position body)
        rot (vj/rotation body)]
    (when (and pos rot)
      (merge rotation (vt/Rotation rot))
      (merge translation (vt/Translation pos)))))

;; -- Animation.
(vf/defsystem animation-controller _w
  [player [:mut vt/AnimationPlayer]
   {speed :v} [:maybe {:flags #{:up}} [vt/Scalar :vg.anim/speed]]
   _ :vg.anim/active
   _loop [:maybe :vg.anim/loop]
   stop [:maybe :vg.anim/stop]
   e :vf/entity
   {:keys [delta_time]} :vf/iter]
  (if stop
    (do (assoc player :current_time 0)
        (-> e
            (disj :vg.anim/active :vg.anim/started :vg.anim/stop)
            (conj :vg/selected)))
    (do
      (conj e :vg.anim/started)
      (update player :current_time + (* delta_time (or speed 1))))))

(defn- indices [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn- lerp
  ([a b]
   (lerp a b 0.6))
  ([a b t]
   (+ a (* t (- b a)))))

(defn- lerp-p
  [p1 p2 t]
  (let [c (vp/component p1)]
    (c (cond
         (vp/layout-equal? c vt/Vector3)
         (vr.c/vector-3-lerp p1 p2 t)

         (vp/layout-equal? c vt/Rotation)
         (vr.c/quaternion-slerp p1 p2 t)

         :else
         (mapv (fn [field]
                 (lerp (get p1 field)
                       (get p2 field)
                       t))
               (keys p1))))))

(vf/defsystem animation-node-player w
  [[_ node] [:vg.anim/target-node :*]
   [_ c] [:vg.anim/target-component :*]
   node-ref vf/Ref
   {:keys [timeline_count values timeline]} vt/AnimationChannel
   player [:meta {:flags #{:up :cascade}
                  :inout :mut}
           vt/AnimationPlayer]
   parent-e [:vf/entity {:flags #{:up}} :vg.anim/active]
   _ [:not {:flags #{:up}} :vg.anim/stop]]
  (let [values (vp/arr values timeline_count c)
        timeline (vp/arr timeline timeline_count :float)
        idx* (first (indices #(>= % (:current_time player)) timeline))
        idx (max (dec (or idx* (count timeline))) 0)
        t (when idx*
            (/ (- (:current_time player)
                  (nth timeline idx))
               (- (nth timeline (inc idx))
                  (nth timeline idx))))]

    (when-not idx*
      (conj parent-e :vg.anim/stop))

    ;; We modify the component from the ref and then we have to notify flecs
    ;; that it was modified.
    (merge @node-ref (if t
                       (lerp-p (nth values idx)
                               (nth values (inc idx))
                               t)
                       (nth values idx)))
    (vf/modified! w node c)))
