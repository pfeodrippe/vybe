(ns vybe.game.system
  "Namespace for some default systems/queries/observers."
  (:require
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.type :as vt]
   [vybe.panama :as vp]
   [vybe.raylib.c :as vr.c]
   [vybe.jolt :as vj]
   [vybe.raylib :as vr]
   [vybe.math :as vm]
   #_[vybe.audio :as va]
   [vybe.util :as vy.u]
   [vybe.c :as vc]
   #_[overtone.core :refer :all])
  (:import
   (org.vybe.jolt jolt)
   (org.vybe.raylib raylib)
   (org.vybe.flecs flecs)))

(defn root
  "Get path to vybe.game flecs parent."
  [& ks]
  (vf/path (concat [:vg/root] ks)))

(defn body-path
  [vy-body]
  (vf/path [(root) (keyword (str "vj-" (:id vy-body)))]))

(defn gen-cube
  "Returns a hash map with `:mesh` and `:material`.

  `idx` is used just to choose some color.
  "
  ([params]
   (gen-cube params (rand-int 5)))
  ([{:keys [x y z]
     :or {x 1 y 1 z 1}
     :as _size}
    idx]
   (let [model (vr.c/load-model-from-mesh (vr.c/gen-mesh-cube x y z))
         model-material (first (vp/arr (:materials model) (:materialCount model) vr/Material))
         model-mesh (first (vp/arr (:meshes model) (:meshCount model) vr/Mesh))]
     ;; Set material color so we can have a better constrast.
     (-> (vr/material-get model-material (raylib/MATERIAL_MAP_ALBEDO))
         (assoc :color (vr/Color (nth [[200 155 255 255.0]
                                       [100 255 255 255.0]
                                       [240 155 155 255.0]
                                       [10 20 200 255.0]
                                       [10 255 24 255.0]]
                                      (mod idx 5)))))
     {:mesh model-mesh
      :material model-material})))

;; -- Transform.
(vc/defn* ^:private matrix-transform :- vt/Transform
  [translation :- vt/Translation
   rotation :- vt/Rotation
   scale :- vt/Scale]
  (let [mat-scale (vr.c/matrix-scale (:x scale) (:y scale) (:z scale))
        mat-rotation (vr.c/quaternion-to-matrix @(vp/as (vp/& rotation) [:* vt/Vector4]))
        mat-translation (vr.c/matrix-translate (:x translation) (:y translation) (:z translation))]
    (vr.c/matrix-multiply (vr.c/matrix-multiply mat-scale mat-rotation) mat-translation)))

(vf/defsystem-c vybe-transform _w [pos vt/Translation, rot vt/Rotation, scale vt/Scale
                                   transform-global [:out [vt/Transform :global]]
                                   transform-local [:out vt/Transform]
                                   transform-parent [:maybe {:flags #{:up :cascade}}
                                                     [vt/Transform :global]]]
  #_(tap> 44)
  (let [local (matrix-transform @pos @rot @scale)]
    (merge @transform-local local)
    (merge @transform-global local (cond-> local
                                     transform-parent
                                     (vr.c/matrix-multiply @transform-parent)))))

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

(vf/defsystem update-physics w
  [ ;; TODO Derive it from transform-global.
   scale vt/Scale
   {aabb-min :min aabb-max :max} vt/Aabb
   vy-body [:maybe vj/VyBody]
   transform-global [vt/Transform :global]
   kinematic [:maybe :vg/kinematic]
   dynamic [:maybe :vg/dynamic]
   sensor [:maybe :vg/sensor]
   ;; Used to find if we are setting `[:vg/raycast :vg/disabled]`
   ;; in Blender.
   raycast [:maybe {:flags #{:up :self}}
            [:vg/raycast :*]]
   phys [:src (root) vj/PhysicsSystem]
   e :vf/entity
   it :vf/iter]
  #_(println :e (vf/get-name e) :kin kinematic)
  (let [half #(max (/ (- (% aabb-max)
                         (% aabb-min))
                      2.0)
                   0.1)
        center #(+ (* (/ (+ (% aabb-max)
                            (% aabb-min))
                         2.0)))
        scaled #(* (half %) 2 (scale %))
        {:keys [x y z]} (vm/matrix->translation
                         (-> (vr.c/matrix-translate (center :x) (center :y) (center :z))
                             (vr.c/matrix-multiply transform-global)))
        body (if vy-body
               (do (when kinematic
                     #_(println :KINEMATIC (matrix->rotation transform-global))
                     (vj/move vy-body (vt/Vector3 [x y z]) (vm/matrix->rotation transform-global) (:delta_time it)))
                   vy-body)
               (let [body (vj/body-add phys (vj/BodyCreationSettings
                                             (cond-> {:position #_(vt/Vector4 [0 0 0 1])
                                                      (vt/Vector4 [x y z 1])
                                                      :rotation #_(vt/Rotation [0 0 0 1])
                                                      (vm/matrix->rotation transform-global)
                                                      :shape (vj/box (vj/HalfExtent [(half :x) (half :y) (half :z)])
                                                                     scale
                                                                     #_(vt/Vector4 [x y z 1])
                                                                     #_(vt/Translation [0 0 0])
                                                                     #_(matrix->rotation transform-global))}
                                               kinematic
                                               (assoc :motion_type (jolt/JPC_MOTION_TYPE_KINEMATIC))

                                               sensor
                                               (assoc :is_sensor true)

                                               dynamic
                                               (assoc :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                                                      :object_layer :vj.layer/moving))))]
                 (when (= (vf/get-name e) (vf/path [:my/model :vg.gltf/my-cube]))
                   #_(clojure.pprint/pprint (-> (vj/-body-get phys (:id body))
                                                :motion_properties
                                                #_(vp/p->map vj/MotionProperties))))
                 body))
        {:keys [mesh material]} (when-not vy-body
                                  (gen-cube {:x (scaled :x) :y (scaled :y) :z (scaled :z)}
                                            (rand-int 10)))]
    (merge w {(body-path body)
              [:vg/debug mesh material phys body
               (vt/Eid e)]

              e [phys body
                 (when-not raycast
                   [:vg/raycast :vg/enabled])]})))

(vf/defobserver body-removed w
  [:vf/events #{:remove}
   body vj/VyBody
   {:keys [id]} [:maybe vt/Eid]]
  (when (vj/added? body)
    (vj/remove* body))
  (dissoc w (body-path body) id))

#_(throw (ex-info "OOPs"
                  #:clojure.error{:phase :compile-syntax-check,
                                  :line 91,
                                  :column 43,
                                  :source
                                  "/Users/pfeodrippe/dev/vybe/src/vybe/game/system.clj"}))

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
            (disj :vg.anim/active :vg.anim/stop)))
    (update player :current_time + (* delta_time (or speed 1)))))

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
   _ [:vg.anim/target-component '?c]
   {:keys [id]} [:src '?c vf/VybeComponentId]
   node-ref vf/Ref
   {:keys [timeline_count values timeline]} vt/AnimationChannel
   player [:meta {:flags #{:up :cascade}
                  :inout :mut}
           vt/AnimationPlayer]
   parent-e [:vf/entity {:flags #{:up}} :vg.anim/active]
   _ [:not {:flags #{:up}} :vg.anim/stop]]
  #_(println :c c)

  #_(def aaa )
  (let [c (vp/comp-cache id)
        values (vp/arr values timeline_count c)
        timeline (vp/arr timeline timeline_count :float)
        idx* (first (indices #(>= % (:current_time player)) timeline))
        idx (max (dec (or idx* (count timeline))) 0)
        t (when idx*
            (/ (- (:current_time player)
                  (nth timeline idx))
               (- (nth timeline (inc idx))
                  (nth timeline idx))))]

    (when-not idx*
      (conj parent-e :vg.anim/stop)
      ;; Just for triggering the `animation-loop` system.
      (conj (vf/ent w node) :vg.anim.entity/stop))

    ;; We modify the component from the ref and then we have to notify flecs
    ;; that it was modified.
    (merge @node-ref (if t
                       (lerp-p (nth values idx)
                               (nth values (inc idx))
                               t)
                       (nth values idx)))

    (vf/modified! w node c)))

;; TODO We could also c-macroexpand/c-invoke from a protocol.
(defmethod vc/c-macroexpand #'conj
  [{:keys [args]}]
  `(do (vf.c/ecs-add-id ~vf/c-w ~(first args)
                        ~(let [c (second args)]
                           (if (keyword? c)
                             ;; TODO OPTM We get by name for now,
                             ;; this can be optimized.
                             `(vf.c/ecs-lookup ~vf/c-w ~(vf/vybe-name c))
                             c)))
       nil))

(vf/defsystem-c ^:debug animation-node-player-2 w
  [_ [:vg.anim/target-node '?node]
   translation [:src '?node vt/Translation]
   scale [:src '?node vt/Scale]
   rotation [:src '?node vt/Rotation]
   ;; TODO Maybe [:vg.anim/target-component [:vf/component'?c]] ?
   _ [:vg.anim/target-component '?c]
   {:keys [id]} [:src '?c vf/VybeComponentId]
   {:keys [kind timeline_count values timeline]} vt/AnimationChannel
   player [:meta {:flags #{:up :cascade}
                  :inout :mut}
           vt/AnimationPlayer]
   parent-e [:vf/entity {:flags #{:up}} :vg.anim/active]
   _ [:not {:flags #{:up}} :vg.anim/stop]]
  #_(tap> id)
  (let [#_ #_values (-> (if (= 0 0)
                          (vp/arr values timeline_count vt/Translation)
                          (vp/arr values timeline_count vt/Scale))
                        (vp/as [:* :void]))
        timeline* (vp/arr timeline timeline_count :float)
        ;; TODO OPTM We could also leverage the previous index.
        idx* (vc/bs_lower_bound timeline* timeline_count (:current_time @player))
        idx (cond
              (= idx* 0)
              0

              (>= idx* timeline_count)
              -1

              :else
              (dec idx*))

        #_ #_my-ref (-> (vf.c/ecs-ref-get-id (:w @node-ref)
                                             (vp/& (:flecs_ref @node-ref))
                                             (:id (:flecs_ref @node-ref)))
                        #_(vp/p->map c))]
    #_(tap> (long (vp/& (:flecs_ref @node-ref))))

    #_(when tttt
      (tap> @tttt))

    ;; TODO We could use some `:void` metadata to indicate that
    ;; a form does not return anything.
    (if (>= idx 0)
      (let [v (/ (- (:current_time @player)
                    (nth timeline* idx))
                 (- (nth timeline* (inc idx))
                    (nth timeline* idx)))]
        ^:void
        (cond
          (= kind 0)
          (do (merge @translation
                     (nth (vp/arr values timeline_count vt/Translation) idx))
              nil)

          (= kind 2)
          (do (merge @rotation
                     (nth (vp/arr values timeline_count vt/Rotation) idx))
              nil)

          #_(= kind 1)
          #_(nth (vp/arr values timeline_count vt/Scale) idx)

          #_(= kind 2)
          #_(nth (vp/arr values timeline_count vt/Rotation) idx))

        #_(merge @node-ref (nth values idx)
                 #_(if t
                     (lerp-p (nth values idx)
                             (nth values (inc idx))
                             t)
                     (nth values idx)))))

    #_(when (< idx* 0)
        (conj parent-e :vg.anim/stop)

        ;; Just for triggering the `animation-loop` system.
        ;; TODO Use `(vf/ent w node)` instead of just `node`
        (conj node :vg.anim.entity/stop)
        #_(conj (vf/ent w node) :vg.anim.entity/stop))

    ;; TODO Using `comp-id` is not great as the ID may change
    ;; after restart.
    #_(when (= id (vc/comptime (vp/comp-id vt/Translation)))
        (tap> (nth (vp/arr values timeline_count vt/Translation) idx)))

    ;; We modify the component from the ref and then we have to notify flecs
    ;; that it was modified.
    #_(merge node-ref (nth values idx))

    ;; TODO lerp
    #_(merge @node-ref (if t
                         (lerp-p (nth values idx)
                                 (nth values (inc idx))
                                 t)
                         (nth values idx)))

    #_(vf/modified! w node c)))

(vf/defsystem animation-loop w
  [[_ action] [:vg.anim/loop :*]
   _ [:maybe :vg.anim.entity/stop]
   e :vf/entity]
  (disj e :vg.anim.entity/stop)
  (let [action-ent (w (vf/path [e action]))]
    (conj action-ent :vg.anim/active)))

;; -- Input.
(vf/defobserver on-close _w
  [_ [:event :vg.window/on-close]]
  (System/exit 0))

(vf/defsystem input-handler w
  [:vf/always true
   _ :vg/camera-active
   camera vt/Camera
   phys [:src (root) vj/PhysicsSystem]
   [_ last-body-entity] [:maybe [:src :vg/raycast [:vg/raycast-body :_]]]]
  ;; -- Window.
  (when (vr.c/window-should-close)
    (vf/event! w :vg.window/on-close))

  ;; -- Raycast.
  (let [{:keys [position direction]} (-> (vr.c/get-mouse-position)
                                         (vr.c/vy-get-screen-to-world-ray camera))
        direction (mapv #(* % 10000) (vals direction))
        body (vj/cast-ray phys position direction)
        path (body-path body)]
    (if (and body (get-in w [(get-in w [path vt/Eid :id])
                             [:vg/raycast :vg/enabled]]))
      ;; Only trigger hover is not the same body as before.
      (let [same-body? (get-in w [:vg/raycast [:vg/raycast-body path]])]
        (when-not same-body?
          (assoc w :vg/raycast [[:vg/raycast-body path]]))
        (if (vr.c/is-mouse-button-pressed (raylib/MOUSE_BUTTON_LEFT))
          (vf/event! w path :vg.raycast/on-click)
          (do (when-not same-body?
                (vf/event! w path :vg.raycast/on-enter))
              (vf/event! w path :vg.raycast/on-hover))))
      (when last-body-entity
        (update w :vg/raycast disj [:vg/raycast-body last-body-entity])
        (vf/event! w :vg.raycast/on-leave)))))

;; -- Camera.
(vf/defsystem update-camera _w
  [_ :vg/camera-active
   camera [:out vt/Camera]
   translation vt/Translation
   rotation vt/Rotation
   e :vf/entity
   {:keys [delta_time]} :vf/iter]
  (when (pos? delta_time)
    (let [cam-pos (get-in camera [:camera :position])
          vel (vt/Velocity (mapv #(/ % delta_time)
                                 [(- (:x translation)
                                     (:x cam-pos))
                                  (- (:y translation)
                                     (:y cam-pos))
                                  (- (:z translation)
                                     (:z cam-pos))]))]
      (conj e vel)))

  (-> camera
      (assoc-in [:camera :position] translation)
      (assoc-in [:rotation] rotation)))

;; -- Audio.
#_(defn- -ambisonic
    [sound-source source-transform target-transform]
    (let [d (vr.c/vector-3-distance
             (vm/matrix->translation target-transform)
             (vm/matrix->translation source-transform))
          [azim elev] (let [{:keys [x y z] :as _v} (-> source-transform
                                                       (vr.c/matrix-multiply (vr.c/matrix-invert target-transform))
                                                       vm/matrix->translation)]
                        (if (> z 0)
                          [(- (Math/atan2 x z))
                           (Math/atan2 y z)
                           _v]
                          [(Math/atan2 x z)
                           (Math/atan2 y z)
                           _v]))
          amp (if (zero? d)
                1
                (/ 1 (* d d)))]
      (va/sound
        (ctl sound-source :azim azim :elev elev :amp (* amp 100) :distance d))))

#_(defsynth ks1
  [note  {:default 60  :min 10   :max 120  :step 1}
   amp   {:default 0.8 :min 0.01 :max 0.99 :step 0.01}
   dur   {:default 2   :min 0.1  :max 4    :step 0.1}
   decay {:default 30  :min 1    :max 50   :step 1}
   coef  {:default 0.3 :min 0.01 :max 2    :step 0.01}
   out-bus 0]
  (let [freq (midicps note)
        noize (* 0.8 (white-noise))
        dly (/ 1.0 freq)
        plk   (pluck noize 1 (/ 1.0 freq) dly
                     decay
                     coef)
        dist (distort plk)
        filt (rlpf dist (* 12 freq) 0.6)
        clp (clip2 filt 0.8)
        reverb (free-verb clp 0.4 0.8 0.2)]
    (out out-bus (* amp (env-gen (perc 0.0001 dur) :action FREE) reverb))))

#_(va/sound

  (def directional
    (synth-load (vy.u/app-resource "com/pfeodrippe/vybe/overtone/directional.scsyndef"))
    #_(synth-load (app-resource "/resources/sc/compiled/directional.scsyndef")))

  (defonce main-g (group "get-on-the-bus main"))
  (defonce early-g (group "early birds" :head main-g))
  (defonce later-g (group "latecomers" :after early-g))

  #_(defonce my-bus
      (audio-bus 1))

  #_(def sound-d (directional [:tail later-g] :in my-bus :out_bus 0)))

#_(vf/defsystem update-sound-sources _w
  [_ :vg/sound-source
   source-transform [vt/Transform :global]
   _ [:src '?e :vg/camera-active]
   target-transform [:src '?e [vt/Transform :global]]]

  #_(let [sss (overtone.inst.synth/ks1 :note (+ (rand-int 3) 50))]
      (va/sound (-ambisonic sss
                            source-transform target-transform)))

  #_(overtone.inst.synth/ks1 :note (+ (rand-int 3) 50) :in (:bus overtone.inst.synth/ks1))

  #_(println :AAA)

  #_(let [bus (audio-bus 1)]

      (ks1 [:tail early-g]
           :out-bus bus
           :note (+ (rand-int 3) 90)
           :amp 0.01
           #_(* (max (abs (:penetration_depth contact-manifold))
                     0.02)
                20))

      (va/sound (-ambisonic (directional [:tail later-g] :in bus :out_bus 0)
                            source-transform target-transform))
      #_ (va/sound (-ambisonic (directional [:tail later-g] :in (audio-bus 1) :out_bus 0)
                               source-transform target-transform))))
