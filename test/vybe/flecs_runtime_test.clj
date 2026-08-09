(ns vybe.flecs-runtime-test
  {:clj-kondo/ignore [:unresolved-symbol]}
  (:require
   [clojure.test :refer [deftest is]]
   [vybe.flecs :as vf]
   [vybe.flecs.c :as vf.c]
   [vybe.panama :as vp])
  (:import
   (org.vybe.flecs flecs)))

(deftest runtime-component-registry-test
  (vf/with-world w
    (let [entity (vf/eid w "RuntimeRegistryProbe")
          descriptor (vp/jx-i
                      {:entity entity
                       :members [{:name "x"
                                  :type (flecs/FLECS_IDecs_f32_tID_)}
                                 {:name "count"
                                  :type (flecs/FLECS_IDecs_i32_tID_)}]}
                      org.vybe.flecs.ecs_struct_desc_t)
          component-id (vf.c/ecs-struct-init w descriptor)
          registry (vf/derive-components! w)
          component (vf/resolve-component registry :RuntimeRegistryProbe)]
      (is (= component-id (vf/eid w component)))
      (is (= component (vf/resolve-component registry "RuntimeRegistryProbe")))
      (is (= 8 (vp/sizeof component)))
      (is (= [:x :count] (keys (.fields component))))
      (is (= component-id
             (get-in (vf/parse-query-expr w [:RuntimeRegistryProbe])
                     [:terms 0 :id]))))))
