(ns vybe.c-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.c :as vc]
   [vybe.panama :as vp]
   [vybe.flecs.c :as vf.c]
   [vybe.raylib.c :as vr.c]
   [vybe.type :as vt]
   [clojure.pprint :as pp]
   [vybe.util :as vy.u]
   matcher-combinators.test))

(comment

  (def portal
    (portal.api/open))

  (do
    (def submit (comp portal.api/submit clojure.datafy/datafy))
    (add-tap #'submit))

  ())

#_(add-tap #'pp/pprint)

(defn- some-mem?
  [mem]
  (not (vp/null? mem)))

(vp/defcomp Translation
  [[:x :float]
   [:y :float]
   [:z :float]])

(vc/defn* simple :- :int
  [v :- Translation]
  (* (:x v) 4))

(vc/defn* simple-10 :- :int
  [v :- Translation]
  (simple v))

(defonce this-ns (ns-name *ns*))

(deftest simple-test
  (is (= 40
         (simple-10 (Translation [10]))))

  (testing "Hot reloading"
    (binding [*ns* (the-ns this-ns)]
      (eval
       `(vc/defn* ~'simple :- :int
          [~'v :- Translation]
          (* (:x ~'v) 5))))

    (is (= 50
           (simple-10 (Translation [10]))))

    ;; Rollback.
    (binding [*ns* (the-ns this-ns)]
      (eval
       `(vc/defn* ~'simple :- :int
          [~'v :- Translation]
          (* (:x ~'v) 4))))

    (is (= 40
           (simple-10 (Translation [10]))))))

(vp/defcomp AnalogEcho
  [[:max_delay :float]
   [:buf_size :int]
   [:mask :int]
   [:pad1 [:padding {:size 4}]]
   [:buf [:* :float]]
   [:write_phase :int]
   [:s1 :float]])

;; TODO Make destructuring work
;; From https://github.com/supercollider/example-plugins/blob/main/03-AnalogEcho/AnalogEcho.cpp
(vc/defn* mydsp :- :void
  [unit :- [:* vc/Unit]
   echo :- [:* AnalogEcho]
   n-samples :- :int]
  (let [[input] (:in_buf @unit)
        [output] (:out_buf @unit)

        fb 0.9
        coeff 0.95

        {:keys [buf mask write_phase s1]
         max-delay :max_delay}
        @echo

        delay (if (> 0.01 max-delay)
                max-delay
                0.01)
        ;; Compute the delay in samples and the integer and fractional parts of this delay.
        delay-samples (* (-> @unit :rate deref :sample_rate)
                         delay)
        offset (int delay-samples)
        frac (float (- delay-samples offset))

        ;; Precompute a filter coefficient.
        a_coeff (- 1 (abs coeff))]
    (doseq [i (range n-samples)]
      (let [
            ;; Four integer phases into the buffer.
            phase1 (- write_phase offset)
            phase2 (dec phase1)
            phase3 (- phase1 2)
            phase0 (inc phase1)
            d0 (aget buf (bit-and phase0 mask))
            d1 (aget buf (bit-and phase1 mask))
            d2 (aget buf (bit-and phase2 mask))
            d3 (aget buf (bit-and phase3 mask))
            ;; Use cubic interpolation with the fractional part of the delay in samples.
            delayed (vc/cubicinterp frac d0 d1 d2 d3)

            ;; Apply lowpass filter and store the state of the filter.
            lowpassed (+ (* a_coeff delayed)
                         (* coeff s1))]
        (reset! s1 lowpassed)
        ;; Multiply by feedback coefficient and add to input signal.
        ;; zapgremlins gets rid of Bad Things like denormals, explosions, etc.
        (aset output i (-> (+ (aget input i)
                              (* fb lowpassed))
                           vc/zapgremlins))
        (aset buf write_phase (aget output i))

        (reset! write_phase (-> (inc write_phase)
                                (bit-and mask)))))

    (reset! (:write_phase @echo) write_phase)
    (reset! (:s1 @echo) s1)))

(vc/defn* mydtor :- :void
  [unit :- [:* vc/Unit]
   echo :- [:* AnalogEcho]
   allocator :- [:* vc/VybeAllocator]]
  ((:free @allocator)
   (:world @unit)
   (:buf @echo)))

(vc/defn* myctor :- [:* :void]
  [unit :- [:* vc/Unit]
   allocator :- [:* vc/VybeAllocator]]
  (let [max-delay (-> @unit :in_buf (nth 2) (nth 0))
        buf-size (vc/NEXTPOWEROFTWO
                  (* (-> @unit :rate deref :sample_rate)
                     max-delay))
        buf (-> ((:alloc @allocator)
                 (:world @unit)
                 (* buf-size (vp/sizeof :float)))
                (vp/zero! buf-size :float))
        echo (-> {:max_delay max-delay
                  :buf_size buf-size
                  :mask (dec buf-size)
                  :write_phase 0
                  :s1 0
                  :buf buf}
                 (vp/new* AnalogEcho))]
    (tap> @echo)
    echo))

(vc/defn* myplugin :- vc/VybeHooks
  [_allocator :- [:* :void]]
  (vc/VybeHooks {:ctor #'myctor
                 :dtor #'mydtor
                 :next #'mydsp}))

(vc/defn* myplugin-simple :- vc/VybeHooks
  [_allocator :- [:* :void]]
  (vc/VybeHooks {:ctor #'simple}))

(vp/defcomp World
  [[:counter :long]])

(deftest dsp-test
  (testing "Pointers to functions are not NULL"
    (is (match?
         {:ctor some-mem?
          :dtor some-mem?
          :next some-mem?}
         (into {} (myplugin "")))))

  (testing "Can convert a mem segment into a VybeCFn and call it as a normal function"
    (let [simple-2 (-> (:ctor (myplugin-simple ""))
                       (vc/p->fn simple))]
      (is (= 160
             (simple-2 (Translation {:x 40}))))))

  (testing "DSP logic"
    (let [world (World {:counter 0})
          unit (vc/Unit {:world world
                         :in_buf (-> [(vp/arr 64 :float)
                                      (vp/arr [20 40 60] :float)
                                      (vp/arr [0.9 400 600] :float)]
                                     vp/arr)
                         :out_buf (-> [(vp/arr 64 :float)
                                       (vp/arr [20 40 60] :float)
                                       (vp/arr [2.5 400 600] :float)]
                                      vp/arr)
                         :rate (vc/Rate {:sample_rate 44000})})
          allocator (vc/VybeAllocator
                     {:alloc (fn [world size]
                               (-> (vp/p* world World)
                                   (update :counter + size))
                               (vp/alloc size 1))})
          echo (-> (myctor unit allocator)
                   (vp/p* AnalogEcho))]
      (testing "Allocator function was called correctly"
        (is (= {:counter (* 4 (:buf_size echo))}
               world)))

      (testing "Ctor"
        (is (match?
             {:max_delay (float 0.9)
              :buf_size 65536
              :mask 65535
              :buf some-mem?
              :write_phase 0
              :s1 0.0}
             (into {} echo))))

      (testing "DSP"
        ;; Set in buf and apply it sometimes so the effect can kick in.
        (assoc unit :in_buf (-> [(vp/arr (range 64) :float)]
                                vp/arr))

        (doseq [_ (range 6)]
          (mydsp unit echo 64))
        (testing "Echo effect hasn't kicked in yet"
          (is (= (mapv float (range 64))
                 (into [] (-> (:out_buf unit)
                              (vp/p* :pointer)
                              (vp/arr 64 :float))))))

        (mydsp unit echo 64)
        (testing "Echo effect is in place"
          (is (not= (mapv float (range 64))
                    (into [] (-> (:out_buf unit)
                                 (vp/p* :pointer)
                                 (vp/arr 64 :float))))))))))

(vc/defn* myflecs-22 :- :int
  []
  (let [w (vf.c/ecs-init)]
    (vf.c/ecs-new w)
    (vf.c/ecs-new w)))

(vc/defn* myflecs :- :int
  [myint :- :int]
  (printf "%d " myint)
  (println "ssfffs sdas")
  (tap> 444)
  (myflecs-22))

(deftest flecs-test
  (is (> (myflecs 4) 500)))

(vc/defn* myraylib :- vt/Vector2
  [myint :- :int]
  ;; Check that myint is really mutable.
  (swap! myint + 3)

  (let [initial (vr.c/vector-2-add
                 ;; This is the positional version equivalent to
                 ;; (vt/Vector2 {:x 2 :y 10}]}
                 (vt/Vector2 [2 10])
                 (vt/Vector2 [4 myint]))]
    (tap> (+ 4431.4 myint))
    (tap> initial)
    (vr.c/vector-2-subtract
     initial
     (vt/Vector2 {:x 10 :y 40}))))
#_ (myraylib 100)

(deftest raylib-test
  (is (= {:x -4.0 :y -20.0}
         (myraylib 7))))

;; We can use this in the CI, running the tests a second time.
(when (= (vy.u/getenv "VYBE_TEST_AGAIN") "true")

  (deftest caching-test
    (is (:existent? myraylib))
    (is (:existent? myflecs))
    (is (:existent? mydsp))
    (is (:existent? myctor))))
