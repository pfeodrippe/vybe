(ns vybe.game-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [vybe.panama :as vp]
   [vybe.game :as vg]
   [vybe.flecs :as vf]
   [vybe.raylib :as vr]))

#_(when-not vp/linux?
  ;; In the CI for Linux, we have some linker issue.
  (require 'vybe.game))

(deftest sanity-test
  (testing "If we reach here, it means all needed vars are loaded correctly"
    (is (= 0 0))))

(deftest gltf->flecs-test
  (let [w (vf/make-world)]
    (binding [vg/*load-model* (constantly
                               (vr/Model
                                {:transform {:m0 1.0,
                                             :m4 0.0,
                                             :m8 0.0,
                                             :m12 0.0,
                                             :m1 0.0,
                                             :m5 1.0,
                                             :m9 0.0,
                                             :m13 0.0,
                                             :m2 0.0,
                                             :m6 0.0,
                                             :m10 1.0,
                                             :m14 0.0,
                                             :m3 0.0,
                                             :m7 0.0,
                                             :m11 0.0,
                                             :m15 1.0},
                                 :meshCount 1
                                 :materialCount 2
                                 :boneCount 0
                                 :materials (vp/arr [(vr/Material {:shader {:id 3}})
                                                     (vr/Material {:shader {:id 3}})])
                                 :meshes (vp/arr [(vr/Mesh
                                                   {:vertexCount 24,
                                                    :triangleCount 12,
                                                    :vaoId 2})])
                                 :meshMaterial (vp/arr [1] :int)}))]
      (is (= :vg.gltf.scene/Scene
             (-> ((vg/-gltf->flecs w ::vg/uncached (vg/resource "com/pfeodrippe/vybe/model/minimal.glb"))
                  (vf/path [::vg/uncached :vg.gltf/Cube]))
                 :vg.gltf.scene/Scene))))))
