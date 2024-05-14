(ns vybe.component
  (:require
   [vybe.api :as vy :refer [defcomp]]
   [vybe.jnr :as jnr]
   [clojure2d.color :as color]))

(defcomp Enabled
  [:enabled :boolean])

(defcomp Hover
  [:hover :boolean])

(defcomp Count
  [:count :int])

(defcomp Position
  [:x :float]
  [:y :float]
  [:z :float])

(defcomp Size
  [:width :float]
  [:height :float])

(defcomp Color
  {:adapter (fn color-adapter
              [color]
              (zipmap [:r :g :b :a]
                      (color/scale-down color true)))}
  [:r :float]
  [:g :float]
  [:b :float]
  [:a :float])

(defcomp ActiveScene
  [:bg_color Color])

(defcomp EnvResource
  [:resource :keyword])

(defcomp ResourceChanged
  [:path :string])

(defn special-components
  [world]
  ;; Exclusive components.
  (->> [:vy.c/visibility
        ActiveScene]
       (mapv #(vy/add-c world % :vy.b/EcsExclusive)))

  ;; `EcsWith`-like.
  (->> (-> (fn [iter]
             (let [world (vy/iter-world iter)]
               (vy/with-each iter [entity :vy/entity]
                 (vy/add-c world entity [Position :global] {:x 0 :y 0 :z 0}))))
           (with-meta {:vy/query [Position]
                       :vy/events [:on-add]}))
       (vy/add-observer world)))

(defn sprite-prefab
  [world]
  (vy/add-prefab world :vy.pf/sprite
                 [(Position {:x 0 :y 0 :z 0})
                  (Size {:width 100 :height 100})
                  (Color :white)
                  [:vy.c/visibility :vy.visibility/inherited]]))

(defn initialize!
  "Initialize some common components, like prefabs and unions.

  prefabs
  - :vy.pf/sprite, see `sprite-prefab`

  unions
  - :vy.c/visibility, visibility for an entity"
  [world]

  ;; Register components early.
  (->> [Enabled
        Hover
        Position
        Size
        Color
        ActiveScene
        EnvResource
        ResourceChanged
        Count
        :vy.visibility/hidden]
       (mapv #(vy/->id world %)))

  (doto world
    special-components
    sprite-prefab))
