(ns vybe.jolt-test
  (:require
   [clojure.test :refer [deftest testing is use-fixtures]]
   [vybe.jolt :as vj]
   [clojure.edn :as edn]
   [vybe.panama :as vp]
   [vybe.jolt.c :as vj.c])
  (:import
   (org.vybe.jolt jolt)))

(defn- ->edn
  [v]
  (edn/read-string (pr-str v)))

(deftest update-test
  (let [phys (vj/physics-system)
        floor-id (vj/body-add phys (vj/BodyCreationSettings
                                    {:position (vj/Vector4 [0 -1 0 1])
                                     :rotation (vj/Vector4 [0 0 0 1])
                                     :shape (vj/box (vj/HalfExtent [100 1 100]))
                                     :motion_type (jolt/JPC_MOTION_TYPE_KINEMATIC)}))
        _bodies (->> (range 16)
                     (mapv (fn [idx]
                             (vj/body-add phys (vj/BodyCreationSettings
                                                {:position (vj/Vector4 [0 (+ 8 (* idx 1.2)) 8 1])
                                                 :rotation (vj/Vector4 [0 0 0 1])
                                                 :shape (vj/box (vj/HalfExtent [0.5 0.5 0.5]))
                                                 :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                                                 :object_layer :vj.layer/moving})))))]
    (vj/update! phys (/ 1.0 60))
    (vj/update! phys (/ 1.0 60))

    (vj/body-linear-velocity! phys floor-id (vj/Vector3 [0 0.02 0]))

    (vj/update! phys (/ 1.0 60))

    (let [bodies (vj/bodies phys)]
      (is (= [[0.0 -0.9996667 0.0 1.0]
              [0.0 7.9836726 8.0 1.0]
              [0.0 9.183672 8.0 1.0]
              [0.0 10.383672 8.0 1.0]
              [0.0 11.583673 8.0 1.0]
              [0.0 12.783672 8.0 1.0]
              [0.0 13.983672 8.0 1.0]
              [0.0 15.183672 8.0 1.0]
              [0.0 16.383673 8.0 1.0]
              [0.0 17.583673 8.0 1.0]
              [0.0 18.783672 8.0 1.0]
              [0.0 19.983673 8.0 1.0]
              [0.0 21.183674 8.0 1.0]
              [0.0 22.383673 8.0 1.0]
              [0.0 23.583673 8.0 1.0]
              [0.0 24.783672 8.0 1.0]
              [0.0 25.983673 8.0 1.0]]
             (->edn (mapv :position bodies)))))))

(deftest cast-ray-test
  (let [phys (vj/physics-system)
        floor-id (vj/body-add phys (vj/BodyCreationSettings
                                    {:position (vj/Vector4 [0 -1 0 1])
                                     :rotation (vj/Vector4 [0 0 0 1])
                                     :shape (vj/box (vj/HalfExtent [100 1 100]))}))]
    (vj/optimize-broad-phase phys)

    (is (= {:body_id floor-id
            :fraction 0.5
            :sub_shape_id (jolt/JPC_SUB_SHAPE_ID_EMPTY)}
           (->> (vj/cast-ray phys
                             (vj/Vector3 [0 10 0])
                             (vj/Vector3 [0 -20 0])
                             {:original true})
                (into {}))))

    (is (= floor-id
           (->> (vj/cast-ray phys (vj/Vector3 [0 10 0]) (vj/Vector3 [0 -20 0]))
                :id)))))

(deftest remove-body-test
  (let [phys (vj/physics-system)
        [id-1 id-2 id-3] (repeatedly 3 #(vj/body-add phys (vj/BodyCreationSettings
                                                           {:position (vj/Vector4 [0 -1 0 1])
                                                            :rotation (vj/Vector4 [0 0 0 1])
                                                            :shape (vj/box (vj/HalfExtent [100 1 100]))})))]


    (is (= [id-1 id-2 id-3] (mapv :id (vj/bodies phys))))

    (vj/body-remove phys id-1)
    (is (= [id-2 id-3] (mapv :id (vj/bodies phys))))))
