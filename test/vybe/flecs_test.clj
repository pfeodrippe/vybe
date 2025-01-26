(ns vybe.flecs-test
  {:clj-kondo/ignore [:unused-value :missing-test-assertion]}
  (:require
   [clojure.test :refer [deftest testing is use-fixtures]]
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.raylib.c :as vr.c]
   [vybe.game.system :as vg.s]
   [clojure.edn :as edn]
   [vybe.panama :as vp]
   [vybe.type :as vt]
   [vybe.c :as vc]
   #_[matcher-combinators.test])
  (:import
   (java.lang.foreign Arena ValueLayout MemorySegment)
   (org.vybe.flecs flecs)))

(use-fixtures :once
  (fn [f]
    (with-open [arena (Arena/ofShared)]
      (binding [vp/*dyn-arena* arena]
        (f)))))

(vp/defcomp Position
  [[:x :double]
   [:y :double]])

(defn- ->edn
  [v]
  (edn/read-string {:default str} (pr-str v)))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/c/entities/basics/src/main.c
(deftest ex-1
  ;; Create the world.
  (let [wptr (vf/-init)]
    #_ (def wptr (vf/-init))
    #_(def wptr wptr)

    ;; Create a entity called :bob and also add/create
    ;; :walking (tag) and Position (component).
    (vf/-set-c wptr :bob [:walking
                          (Position {:x 10 :y 20})])

    ;; Get position, it will return a hash map representation, you can use
    ;; normal clojure functions with it (e.g. `get`, `select-keys`... it's a map
    ;; backed by a pointer (memory segment) + a component).
    (vf/-get-c wptr :bob Position)

    ;; Override position.
    (vf/-set-c wptr :bob [(Position {:x 20 :y 30})])

    ;; Create another entity.
    (vf/-set-c wptr :alice [(Position {:x 10 :y 20})])

    ;; Add a tag in a separate step.
    (vf/-set-c wptr :alice [:walking])

    ;; Check all the components (including the ones we hide from you) of an
    ;; entity in string format.
    ;; TODO Hash map representation just like as we have in vybe.api.
    (vf/type-str wptr :alice)
    #_(let [{:keys [array count]}
            (-> (vf.c/ecs-get-type wptr (vf/eid wptr :alice))
                (p->map (-to-c (ecs_type_t/layout))))]
        (mapv (fn [idx]
                (.getAtIndex array ValueLayout/JAVA_LONG idx))
              (range count)))

    ;; Remove tag.
    (vf/-remove-c wptr :alice [:walking])

    ;; Iterate over all the entities with Position.
    (let [it (vf.c/ecs-each-id wptr (vf/eid wptr Position))
          *acc (atom [])]
      (while (vf.c/ecs-each-next it)
        (let [pos (vf.c/ecs-field-w-size it (.byteSize (.layout Position)) 0)]
          (swap! *acc conj
                 (mapv (fn [^long idx]
                         [(-> (vf.c/ecs-get-name wptr (.getAtIndex ^MemorySegment (:entities it)
                                                                   ValueLayout/JAVA_LONG
                                                                   idx))
                              vp/->string)
                          (->> (vp/p->map (.asSlice ^MemorySegment pos
                                                    (* idx (.byteSize (.layout Position)))
                                                    (.layout Position))
                                          Position)
                               (into {}))])
                       (range (:count it))))))
      (swap! *acc #(apply concat %))
      #_(vf.c/ecs-fini wptr)
      (is (= #{["bob" {:x 20.0, :y 30.0}]
               ["alice" {:x 10.0, :y 20.0}]}
             (set @*acc))))))

(deftest c-systems-test
  (let [w (vf/make-world)]
    (vg.s/vybe-transform w)

    (testing "system builders can be called over and over if they don't change"
      (= (vf/eid (vg.s/animation-node-player-2 w))
         (vf/eid (vg.s/animation-node-player-2 w))))

    (merge w {:alice [(vt/Scale [1.0 1.0 1.0]) (vt/Translation)
                      (vt/Rotation [0 0 0 1]) [(vt/Transform) :global] (vt/Transform)

                      {:bob [(vt/Scale [1.0 1.0 1.0]) (vt/Translation)
                             (vt/Rotation [0 0 0 1]) [(vt/Transform) :global] (vt/Transform)]}]})

    (assoc w (vf/path [:alice :bob]) (vt/Translation {:x 20 :y 30}))
    (vf/progress w)

    (is (= {:m12 20.0
            :m13 30.0
            :m15 1.0}
           (select-keys (get (w (vf/path [:alice :bob])) [vt/Transform :global])
                        [:m12 :m13 :m15])))

    (assoc w :alice (vt/Translation {:x 3 :y 4}))
    (vf/progress w)

    (is (= {:m12 23.0
            :m13 34.0
            :m15 1.0}
           (select-keys (get (w (vf/path [:alice :bob])) [vt/Transform :global])
                        [:m12 :m13 :m15])))
    (is (= {:m12 20.0
            :m13 30.0
            :m15 1.0}
           (select-keys (get (w (vf/path [:alice :bob])) vt/Transform)
                        [:m12 :m13 :m15])))))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/c/entities/basics/src/main.c
(deftest ex-1-w-map
  ;; Create the world.
  (let [w (vf/make-world)]
    #_(def w (vf/make-world))
    #_(def w w)

    ;; Create a observer.
    (vf/with-observer w [:vf/name :ex-1-observer
                         :vf/events #{:set}
                         {:keys [x] :as pos} [:mut Position]
                         e :vf/entity
                         _event :vf/event
                         _it :vf/iter]
      (when (= (vf/get-rep e) :alice)
        (merge w {e [:from-observer]}))
      (when (= x 10.0)
        (update pos :x inc)))

    ;; We can also do the same thing as in `ex-1`, but using a clojure hash map
    ;; representation of the world. You can use the clojure functions you are
    ;; used to. It's a mutable map, though, e.g. `assoc` mutates it in place.

    ;; Let's setup some entities.
    (merge w {:bob [:walking (Position {:x 10 :y 20}) nil]
              :alice [(Position {:x 10 :y 21})]})

    ;; Get the position component of an entity.
    (get-in w [:bob Position])
    ;; Or a value from the component (hash map magic!! *backed by pointers).
    (get-in w [:bob Position :y])
    ;; Or whatever you need from it.
    (-> (get-in w [:bob Position])
        (select-keys [:y]))

    ;; Override position.
    (assoc w :bob (Position {:x 20 :y 30}))

    ;; Add a tag in a separate step.
    (assoc w :alice :walking)

    ;; Check all the components (including the ones we hide from you) of an
    ;; entity in string format.
    ;; TODO Hash map representation just like as we have in vybe.api.
    (vf/type-str (:alice w))

    ;; Remove tag.
    (-> w
        (update :alice disj :walking)
        ;; Update x field in Position (maps everywhere!).
        (update-in [:bob Position :x] inc))

    ;; Iterate over all the entities with Position using `with-query`, also
    ;; retrieving the positions.
    (is (= '[[#{:alice #:vybe.flecs-test{Position {:x 11.0, :y 21.0}} :from-observer}
              #:vybe.flecs-test{Position {:x 11.0, :y 21.0}}]
             [#{:bob :walking #:vybe.flecs-test{Position {:x 21.0, :y 30.0}}}
              #:vybe.flecs-test{Position {:x 21.0, :y 30.0}}]]
           (->edn (vf/with-query w [pos Position, e :vf/entity]
                    [e pos]))))

    ;; `with-system` has basically the same interface as
    ;; `with-query`. The differences are that `with-system` requires a
    ;; :vf/name (you put it in the bindings, see below) and it won't
    ;; run the code in place, but will build a Flecs system that can be run
    ;; with `system-run`.
    (let [*acc (atom [])
          ;; Note that we need to accumulate values here explictly as `with-system`
          ;; doesn't run the system immediately.
          system-id (vf/with-system w [:vf/name :my-system, pos Position, e :vf/entity]
                      (swap! *acc conj [e pos]))]

      (testing "system has not run yet"
        (is (= '[]
               (->edn @*acc))))

      (vf/system-run w :my-system)
      (testing "system has run"
        (is (= '[[#{:alice #:vybe.flecs-test{Position {:x 11.0, :y 21.0}} :from-observer}
                  #:vybe.flecs-test{Position {:x 11.0, :y 21.0}}]
                 [#{:bob :walking #:vybe.flecs-test{Position {:x 21.0, :y 30.0}}}
                  #:vybe.flecs-test{Position {:x 21.0, :y 30.0}}]]
               (->edn @*acc))))

      (vf/progress w)
      (testing "system has run again, now using vf/progress, if there was no iter change, system won't really run"
        (is (= '[[#{:alice #:vybe.flecs-test{Position {:x 11.0, :y 21.0}} :from-observer}
                  #:vybe.flecs-test{Position {:x 11.0, :y 21.0}}]
                 [#{:bob :walking #:vybe.flecs-test{Position {:x 21.0, :y 30.0}}}
                  #:vybe.flecs-test{Position {:x 21.0, :y 30.0}}]]
               (->edn @*acc))))

      (testing "adding it twice returns a different entity"
        (is (not= system-id
                  (vf/with-system w [:vf/name :my-system, pos Position]
                    pos)))))))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/c/entities/hierarchy/src/main.c
(deftest children-test
  (let [w (vf/make-world #_{:debug true})]
    #_(def w (vf/make-world))
    #_(def w w)

    (merge w {:sun [:star (Position {:x 1 :y 1})
                    ;; These are all children.
                    {:mercury [:planet (Position {:x 1 :y 1})]
                     :venus [:planet (Position {:x 2 :y 2})
                             ;; NESTED!
                             {:moon [:moon (Position {:x 0.1 :y 0.1})
                                     {:jujuh [:satellite]}]}]}]
              ;; You can also define children like below.
              :earth [:planet (Position {:x 3 :y 3}) [:vf/child-of :sun]]})

    (is (= '{:sun
             #{#:vybe.flecs-test{Position {:x 1.0, :y 1.0}}
               :star
               {:mercury
                #{[:vf/child-of :sun]
                  #:vybe.flecs-test{Position {:x 1.0, :y 1.0}}
                  :planet},
                :venus
                #{[:vf/child-of :sun]
                  #:vybe.flecs-test{Position {:x 2.0, :y 2.0}}
                  :planet
                  {:moon
                   #{:moon
                     [:vf/child-of (vybe.flecs/path [:sun :venus])]
                     {:jujuh
                      #{:satellite
                        [:vf/child-of (vybe.flecs/path [:sun :venus :moon])]}}
                     #:vybe.flecs-test{Position {:x 0.1, :y 0.1}}}}},
                :earth
                #{[:vf/child-of :sun]
                  #:vybe.flecs-test{Position {:x 3.0, :y 3.0}}
                  :planet}}}}
           (->edn w)))))

#_(deftest children-delete-test
    (let [w (vf/make-world)]
      (merge w {:sun [{:mercury [:fff (Position)]}]})

      ;; Dissoc and merge again to make sure that we don't have any Flecs issue.
      #_(dissoc w :sun)
      #_(merge w {:sun [{:mercury []}]})

      (is (= {}
             (->edn w)))))

(deftest children-simple-delete-test
  (let [w (vf.c/ecs-mini)

        e1 (vf.c/ecs-set-name w 0 "e1")
        _ (vf.c/ecs-delete w e1)
        e1' (vf.c/ecs-set-name w 0 "e1")

        e2' (vf.c/ecs-set-name w 0 (str "#" (unchecked-int e1') ".e2"))]

    (is (pos? e1))
    (is (pos? e1'))
    (is (pos? e2'))

    (vf.c/ecs-fini w)))

;; Based on https://github.com/SanderMertens/flecs/blob/master/examples/cpp/entities/prefab/src/main.cpp
;; and https://github.com/SanderMertens/flecs/blob/master/examples/c/prefabs/variant/src/main.c
;; Re: overriding, check See https://www.flecs.dev/flecs/md_docs_2Manual.html#automatic-overriding
(deftest prefab-test
  (let [w (vf/make-world #_{:debug true})
        ;; You can defined multiple components like this, these won't
        ;; be global as the `defcomp` ones are.
        {:syms [Attack Defense FreightCapacity ImpulseSpeed Position]}
        (vp/make-components
         '{Attack [[:value :double]]
           Defense [[:value :double]]
           FreightCapacity [[:value :double]]
           ImpulseSpeed [[:value :double]]
           Position [[:x :double] [:y :double]]})]
    #_(def w (vf/make-world))
    #_(def w w)

    ;; Make Defense inheritable, components and tags will override by default.
    (merge w {Defense [vf/on-instantiate-inherit-id]})

    ;; Prefabs are template-like entities that you can use to define other
    ;; entities.
    (merge w {:spaceship [:vf/prefab (ImpulseSpeed 50) (Defense 50)
                          (Position {:x 30 :y 20})]
              :freighter [:vf/prefab (vf/is-a :spaceship) :has-ftl
                          (FreightCapacity 100) (Defense 100)]
              :mammoth-freighter [:vf/prefab (vf/is-a :freighter)
                                  (FreightCapacity 500) (Defense 300)]
              :frigate [:vf/prefab (vf/is-a :spaceship) :has-ftl
                        (Attack 100) (Defense 75) (ImpulseSpeed 125)]
              :mammoth [(vf/is-a :mammoth-freighter)]
              :mammoth-2 [(vf/is-a :mammoth-freighter)
                          ;; FreightCapacity is overridden.
                          (FreightCapacity -51)]})
    ;; When you update a prefab, entities inheriting from it wil
    ;; get updated as well (as long as it's not overridden).
    (update-in w [(vf/path [:mammoth-freighter]) Defense :value] inc)
    (is (= '[[:mammoth
              {Position {:x 31.0, :y 20.0}}
              {ImpulseSpeed {:value 50.0}}
              {Defense {:value -500.0}}
              {FreightCapacity {:value 499.0}}]
             [:mammoth-2
              {Position {:x 30.0, :y 20.0}}
              {ImpulseSpeed {:value 50.0}}
              {Defense {:value -500.0}}
              {FreightCapacity {:value -51.0}}]]
           (->edn
            ;; You can iterate over all the inherited components.
            ;; `:mut` means that the pointer will (probably) be modified, it's used by
            ;; Flecs to trigger other systems. If you try to mutate a pointer
            ;; that doesn't have it, you will receive an exception complaining
            ;; that the pointer is a const.
            (vf/with-query w [e :vf/entity, pos [:mut Position], speed ImpulseSpeed
                              defense [:mut Defense], capacity [:mut FreightCapacity]]
              (if (= e (vf/ent w :mammoth))
                ;; We modify capacity, defense and position here when :mammoth, note
                ;; how only defense will be changed in both (as it's originally from the
                ;; prefab) while capacity and position are not shared (as they are
                ;; overridden).
                [(vf/get-rep e) (update pos :x inc) speed (assoc defense :value -500) (update capacity :value dec)]
                [(vf/get-rep e) pos speed defense capacity])))))))

(deftest pair-wildcard-test
  (is (= '[[{A {:x 34.0}} [:a :c]] [{A {:x 34.0}} [:a :d]]]
         (let [w (vf/make-world)
               A (vp/make-component 'A [[:x :double]])]
           (merge w {:b [(A {:x 34})
                         [:a :c]
                         [:a :d]]})
           (->edn
            (vf/with-query w [a A
                              v [:a :*]]
              [a v]))))))

(vp/defcomp Translation
  [[:x :float]
   [:y :float]
   [:z :float]])

(vp/defcomp Rotation
  [[:x :float]
   [:y :float]
   [:z :float]
   [:w :float]])

(deftest datalog-query-test
  ;; https://github.com/SanderMertens/flecs/blob/v4/docs/Queries.md#variables
  (let [w (vf/make-world)]
    (merge w {:e1 [[(Translation {:x 34}) :a]
                   [(Rotation {:z 24}) :a]]
              :e2 [[(Translation {:y 293}) :b]
                   [(Rotation {:z 213}) :c]]
              :e3 [[(Translation {:y 193}) :d]
                   [(Rotation {:z 123}) :d]]
              :d [:some-tag]
              :a [:velocity :speed]})

    (testing "different sources (uses ?e and ?f)"
      (is (= [:e1 :e2 :e3]
             (vf/with-query w [_ [Translation '?e]
                               _ [Rotation '?f]
                               e :vf/entity]
               (vf/get-rep e)))))

    (testing "same source (uses only ?e)"
      ;; `?e` is arbitrary, same for `e`, they don't have
      ;; anything in common with each other, they are just
      ;; binding/variable names.
      (is (= [:e1 :e3]
             (vf/with-query w [_ [Translation '?e]
                               _ [Rotation '?e]
                               e :vf/entity]
               (vf/get-rep e)))))

    (testing "?e should have :some-tag"
      (is (= [:e3]
             (vf/with-query w [_ [Translation '?e]
                               _ [Rotation '?e]
                               _ [:src '?e :some-tag]
                               e :vf/entity]
               (vf/get-rep e)))))

    (testing "?e should NOT have :some-tag"
      (is (= [:e1]
             (vf/with-query w [_ [Translation '?e]
                               _ [Rotation '?e]
                               _ [:not [:src '?e :some-tag]]
                               e :vf/entity]
               (vf/get-rep e)))))

    ;; https://github.com/SanderMertens/flecs/blob/v4/docs/Queries.md#query-scopes
    (testing "?e should NOT have velocity or speed (query scope)"
      (is (= [:e3]
             (vf/with-query w [_ [Translation '?e]
                               _ [Rotation '?e]
                               _ [:not [:scope
                                        [:or
                                         [:src '?e :velocity]
                                         [:src '?e :speed]]]]
                               e :vf/entity]
               (vf/get-rep e)))))))

(deftest unique-trait-test
  (let [w (vf/make-world)]
    (assoc w
           :my-unique [:vf/unique]
           :e1 [:my-unique :a]
           :e2 [:my-unique :b])

    (testing "only :e2 should have :my-unique"
      (is (= {:my-unique #{:vf/unique :CanToggle}
              :e1 #{:a}
              :e2 #{:my-unique :b}}
             (->edn w))))))

(deftest ref-test
  (let [w (vf/make-world)]
    (merge w {:e1 [(Translation {:x 34})]})
    (merge w {:e2 [(vf/ref w :e1 Translation)]})

    (is (= [(Translation {:x 34})]
           (vf/with-query w [r vf/Ref]
             @r)))))

;; Rest is working, so we don't need to test this anymore for now.
#_(deftest rest-test
    (let [test-f (fn [rest?]
                   (vf/-with-world w
                     (let [*acc (atom 0)
                           e1 (vf.c/ecs-new w)
                           camera_active (vf.c/ecs-new w)
                           comp1 (vf.c/ecs-new w)
                           _ (vf.c/ecs-add-id w camera_active (flecs/EcsCanToggle))]
                       (when rest?
                         (vf/rest-enable! w))

                       (vf.c/ecs-add-id w e1 camera_active)

                       (vf/with-system w [:vf/name :system/update-camera
                                          #_ #_:vf/always true
                                          _ camera_active
                                          _ comp1]
                         (swap! *acc inc))

                       (vf.c/ecs-add-id   w e1 comp1)
                       (vf.c/ecs-progress w 0.1)

                       (vf.c/ecs-remove-id w e1 comp1)
                       (vf.c/ecs-add-id    w e1 comp1)
                       (vf.c/ecs-progress  w 0.1)

                       (is (= 2 @*acc)))))]
      (testing "without rest"
        (test-f false))

      (testing "with rest"
        (test-f true))

      (testing "without rest - from C"
        (is (= 2 (vf.c/vybe-test-rest-issue false))))

      (testing "with rest - from C"
        (is (= 2 (vf.c/vybe-test-rest-issue true))))))

#_(deftest pair-any-test
    (is (= #_'[[{A {:x 34.0}} [:a :c]] [{A {:x 34.0}} [:a :d]]]
           0
           (let [w (vf/make-world)
                 A (vp/make-component 'A [[:x :double]])]
             (merge w {:b [(A {:x 34})
                           [:a :c]
                           [:a :d]]})
             (->edn
              (vf/with-query w [a A
                                v [:a :_]]
                [a v]))))))
