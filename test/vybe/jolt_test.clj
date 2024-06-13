(ns vybe.jolt-test
  (:require
   [clojure.test :refer [deftest testing is use-fixtures]]
   [vybe.jolt :as vj]
   [clojure.edn :as edn])
  (:import
   (org.vybe.jolt jolt)))

(defn- ->edn
  [v]
  (edn/read-string (pr-str v)))

(deftest update-test
  (let [job-system (vj/init)
        physics-system (vj/default-physics-system)
        body-i (vj/body-interface physics-system)
        _floor (vj/body-add body-i (vj/BodyCreationSettings
                                    {:position (vj/Vector4 [0 -1 0 1])
                                     :rotation (vj/Vector4 [0 0 0 1])
                                     :shape (-> (vj/box-settings (vj/HalfExtent [100 1 100]))
                                                vj/shape)}))
        _bodies (->> (range 16)
                     (mapv (fn [idx]
                             (vj/body-add body-i (vj/BodyCreationSettings
                                                  {:position (vj/Vector4 [0 (+ 8 (* idx 1.2)) 8 1])
                                                   :rotation (vj/Vector4 [0 0 0 1])
                                                   :shape (-> (vj/box-settings (vj/HalfExtent [0.5 0.5 0.5]))
                                                              vj/shape)
                                                   :motion_type (jolt/JPC_MOTION_TYPE_DYNAMIC)
                                                   :object_layer :vj.layer/moving})))))]
    (vj/update! job-system physics-system (/ 1.0 60))
    (vj/update! job-system physics-system (/ 1.0 60))
    (vj/update! job-system physics-system (/ 1.0 60))

    (let [bodies (vj/bodies physics-system)]
      (is (= [[0.0 -1.0 0.0 1.0]
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

(deftest ray-cast-test
  (let [_job-system (vj/init)
        physics-system (vj/default-physics-system)
        body-i (vj/body-interface physics-system)
        floor-id (vj/body-add body-i (vj/BodyCreationSettings
                                      {:position (vj/Vector4 [0 -1 0 1])
                                       :rotation (vj/Vector4 [0 0 0 1])
                                       :shape (-> (vj/box-settings (vj/HalfExtent [100 1 100]))
                                                  vj/shape)}))]
    (vj/optimize-broad-phase physics-system)

    (is (= {:body_id floor-id
            :fraction 0.5
            :sub_shape_id (jolt/JPC_SUB_SHAPE_ID_EMPTY)}
           (->> (-> (vj/narrow-phase-query physics-system)
                    (vj/cast-ray (vj/RayCast
                                  {:origin (vj/Vector4 [0 10 0 1])
                                   :direction (vj/Vector4 [0 -20 0 0])})))
                (into {}))))))
