(ns vybe.example.basic
  (:require
   [vybe.game :as vg]
   [vybe.raylib :as vr]
   [vybe.panama :as vp]
   [vybe.raylib.c :as vr.c]
   [vybe.flecs :as vf]
   [clojure.math :as math]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [jsonista.core :as json]
   [clj-java-decompiler.core :refer [decompile disassemble]])
  (:import
   (org.vybe.flecs flecs)
   (org.vybe.raylib raylib Shader Material Model)
   (java.lang.foreign Arena ValueLayout MemorySegment)))

#_(init)

(set! *warn-on-reflection* true)

(defonce env (vg/make-env))

(defn load-shadowmap-render-texture
  "From https://github.com/raysan5/raylib/blob/master/examples/shaders/shaders_shadowmap.c#L202."
  [width height]
  (let [rt (vr/RenderTexture2D)
        id (vr.c/rl-load-framebuffer)
        _ (assoc rt :id id)
        _ (vr.c/rl-enable-framebuffer id)
        tex-depth-id (vr.c/rl-load-texture-depth width height false)]
    (merge rt {:texture {:width width, :height height}
               :depth {:id tex-depth-id, :width width, :height height,
                       :format 19, :mipmaps 1}})
    (vr.c/rl-framebuffer-attach id tex-depth-id (raylib/RL_ATTACHMENT_DEPTH) (raylib/RL_ATTACHMENT_TEXTURE2D) 0)
    (when-not (vr.c/rl-framebuffer-complete id)
      (throw (ex-info "Couldn't create frame buffer" {})))
    (vr.c/rl-disable-framebuffer)

    rt))

#_(load-shadowmap-render-texture 600 600)

(defonce light-dir-vec [0.1 0.35 0.3])

(defn wobble
  ([v]
   (wobble v 1.0))
  ([v freq]
   (* v (math/sin (* (vr.c/get-time) freq)))))

(defn wobble-rand
  ([v]
   (wobble-rand v 1.0))
  ([v freq]
   (let [f #(wobble v (* % freq))]
     (+ (f 2) (* (f 3) (f 4.5))))))

#_(init)

(defonce vec3-zero
  (vg/Translation {:x 0 :y 0 :z 0}))

#_(init)
(defn- d
  [v]
  (println :___DEBUG v)
  v)

#_ (:id (vg/Shader))

#_(init)

(defn draw
  []
  (let [{:keys [vf/world view-3 view-2 shadowmap shadowmap-2 shadowmap-shader
                dither-shader dof-shader noise-blur-shader kuwahara-shader
                depth-rts]}
        env

        w world
        _ (do (vg/run-reloadable-commands!)
              (def sss (vg/default-systems w))
              (mapv :vf/id sss)
              ;; For dev mode.
              (vf/progress w (vr.c/get-frame-time)))
        _ (do (def w w)
              (def shadowmap-shader shadowmap-shader)
              (def kuwahara-shader kuwahara-shader)
              (def dither-shader dither-shader))

        #_ (init)]

    (vg/draw-lights w shadowmap-shader depth-rts)

    #_(init)
    #_(def w w)

    (vg/with-multipass view-2 {:shaders [#_[dof-shader {:u_near_clip 0.1
                                                        :u_far_clip 5.0}]
                                         [noise-blur-shader {:u_radius (+ 2.0 (rand 2))}]
                                         [dither-shader {:u_offsets (vg/Vector3 (mapv #(* % 0.5) [0.02 (+ 0.016 (wobble 0.005))
                                                                                                  (+ 0.040 (wobble 0.01))]))
                                                         #_ #_:u_mouse (let [{:keys [x y]} (vr.c/get-mouse-position)]
                                                                         (->> [x (- 600 y)]
                                                                              (mapv #(* % 1/600))))}
                                          #_{:u_offsets [(wobble-rand 0.002 1.2)
                                                         (wobble-rand 0.003 2.1)
                                                         (wobble-rand 0.004 1)]}]
                                         #_kuwahara-shader

                                         #_[noise-blur-shader {:u_radius 20.0}]]
                               #_[dither-shader {:u_offsets [(* 0.014 (math/cos (* (vr/get-time) 0.6)))
                                                             (* 0.017 (math/cos (* (vr/get-time) 0.5)))
                                                             (* 0.008 (math/cos (* (vr/get-time) 0.4)))]}]}
      (vr.c/clear-background (vr/Color [255 20 90 255]))
      (vg/with-camera (get-in w [:vf.gltf/Camera vg/Camera])
        (vg/draw-scene w)
        (vg/draw-debug w)))

    ;; Draw to the screen.
    (vg/with-drawing
      (vr.c/clear-background (vr/Color [255 20 100 255]))

      (vr.c/draw-texture-pro (:texture view-2)
                             (vr/Rectangle [0 0 600 -600]) (vr/Rectangle [0 0 600 600])
                             (vr/Vector2 [0 0]) 0 vg/color-white)

      #_(vg/with-camera (get-in w [(do #_(do :vf.gltf/Light2)
                                       (do :vf.gltf/Camera))
                                   vg/Camera])
          (vg/draw-scene w)
          (vg/draw-debug w))

      (vr.c/draw-fps 510 570))))

#_(init)
#_(alter-var-root #'vr/draw (constantly (fn [] (vr.c/clear-background (vr/Color [255 255 255 255])))))

#_(init)

(defn init
  []
  (when-not (vr.c/is-window-ready)
    (vr.c/set-config-flags (raylib/FLAG_MSAA_4X_HINT))
    (vr.c/init-window 600 600 "Opa")
    (vr.c/set-window-state (raylib/FLAG_WINDOW_UNFOCUSED))
    (vr.c/set-target-fps 30)
    #_(vr.c/set-target-fps 60)
    #_(vr.c/set-target-fps 120)
    (vr.c/set-window-position 1120 200)
    (vr.c/clear-background (vr/Color [10 100 200 255]))
    (vr.c/draw-rectangle 30 50 100 200 (vr/Color [255 100 10 255]))
    (vr.c/draw-rectangle 300 50 100 200 (vr/Color [255 100 10 255])))

  (reset! env {})
  (swap! env merge {:vf/world (-> (vf/make-world)
                                  (vg/gltf->flecs :flecs "/Users/pfeodrippe/Documents/Blender/Healthcare Game/models.glb"))})

  (swap! env merge {#_ #_ #_ #_ #_ #_:cam-1 (vg/Camera
                                             {:position [1 1 1]
                                              :target [0 0.3 0]
                                              :up [0 1 0]})
                    :cam-2 (vg/Camera
                            {:position [1 1 3]
                             :target [0 0.6 0]
                             :up [0 1 0]})
                    :cam-3 (vg/Camera
                            {:position #_(vr/Vector3 [7.358890056610107 4.958309173583984 6.925790786743164])
                             [2 0 0]
                             :target [0 0.1 0]
                             :up [0 1 0]
                             #_ #_:projection (raylib/CAMERA_ORTHOGRAPHIC)
                             #_ #_:fovy 10})
                    :light-dir (vr/Vector3 light-dir-vec)
                    #_ #_:light-cam (vg/Camera
                                     {:position (vr/Vector3 light-dir-vec)
                                      :target [0 0 1]
                                      :up [0 1 0]
                                      :projection (raylib/CAMERA_ORTHOGRAPHIC)
                                      :fovy 20})
                    :shadowmap (load-shadowmap-render-texture 600 600)
                    :shadowmap-2 (load-shadowmap-render-texture 600 600)
                    ;; Create 10 depth render textures for reuse.
                    :depth-rts (pmap #(do % (load-shadowmap-render-texture 600 600))
                                     (range 10))
                    :shadowmap-shader (vg/shader-program :shadowmap-shader
                                                         {::vg/shader.vert "shaders/shadowmap.vs"
                                                          ::vg/shader.frag "shaders/shadowmap.fs"})
                    :kuwahara-shader (vg/shader-program :kuwahara-shader
                                                        {::vg/shader.frag "shaders/kuwahara_2d.fs"})
                    :dither-shader (vg/shader-program :dither-shader
                                                      {::vg/shader.frag "shaders/dither.fs"})
                    :noise-blur-shader (vg/shader-program :noise-blur-shader
                                                          {::vg/shader.frag "shaders/noise_blur_2d.fs"})
                    :edge-shader (vg/shader-program :edge-shader
                                                    {::vg/shader.frag "shaders/edge_2d.fs"})
                    :process-shader (vg/shader-program :process-shader
                                                       {::vg/shader.frag "shaders/process_2d.fs"})
                    :dof-shader (vg/shader-program :dof-shader
                                                   {::vg/shader.frag "shaders/dof.fs"})
                    :default-shader (vg/shader-program :default-shader {})
                    :render-texture (vr.c/load-render-texture 600 600)
                    :view-1 (vr.c/load-render-texture 600 600)
                    :view-1* (vr.c/load-render-texture 600 600)
                    :view-2 (vr.c/load-render-texture 600 600)
                    :view-3 (vr.c/load-render-texture 600 600)})


  #_(swap! env merge {:model (vg/model :arm "/Users/pfeodrippe/Documents/Blender/Healthcare Game/models.glb")}
         #_{:model (vg/model :donut "/Users/pfeodrippe/Documents/Blender/Learning/01_donut.glb")})
  (alter-var-root #'vr/draw (constantly #'draw)))
#_(alter-var-root #'vr/draw (constantly (fn [] (Thread/sleep 10))))
#_(init)

(comment

  (-> @(vg/shader-program :shadowmap-shader7
                          {::vg/shader.vert "shaders/shadowmap.vs"
                           ::vg/shader.frag "shaders/shadowmap.fs"})
      (vr.c/get-shader-location "fragPosition"))

  ;; This is how you set a material!
  (doseq [idx (range (Model/materialCount (:model env)))]
    (-> (Material/asSlice (Model/materials (:model env)) idx)
        (Material/shader (:shadowmap-shader env))))

  (.set (Shader/locs (:shadowmap-shader env))
        ValueLayout/JAVA_INT
        (* 4 (raylib/SHADER_LOC_VECTOR_VIEW))
        (vr.c/get-shader-location (:shadowmap-shader env) "viewPos"))

  (doto (:shadowmap-shader env)
    (vg/set-uniform :lightDir (vr/Vector3 light-dir-vec) {:type :vec3})
    (vg/set-uniform :lightColor (vr/color-normalize (vr/Color [0 20 200 255])) {:type :vec4})
    (vg/set-uniform :ambient (vr/color-normalize (vr/Color [200 200 200 255])) {:type :vec4})
    (vg/set-uniform :shadowMapResolution (vr/int* 600)))

  ())
