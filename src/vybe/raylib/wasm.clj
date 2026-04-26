(ns vybe.raylib.wasm
  (:require
   [vybe.raylib.abi :as abi]
   [vybe.wasm :as vw])
  (:import
   (com.dylibso.chicory.runtime Memory)))

(def ^:private default-width 600)
(def ^:private default-height 600)
(def ^:private gl-framebuffer-complete 0x8CD5)

(defonce ^:private gl-id* (atom 4))

(defn- next-gl-id
  []
  (swap! gl-id* inc))

(defn- zero-result
  [results]
  (if (seq results)
    (long-array [0])
    (vw/empty-result)))

(defn- write-f64!
  [instance ptr v]
  (when (pos? (long ptr))
    (.writeF64 ^Memory (.memory instance) (int ptr) (double v))))

(defn- write-i32!
  [instance ptr v]
  (when (pos? (long ptr))
    (.writeI32 ^Memory (.memory instance) (int ptr) (int v))))

(defn- write-gl-ids!
  [instance n ptr]
  (doseq [idx (range (long n))]
    (write-i32! instance (+ (long ptr) (* idx 4)) (next-gl-id))))

(defn- gl-name
  [name]
  (if (.startsWith ^String name "emscripten_")
    (subs name (count "emscripten_"))
    name))

(defn- host-result
  [name results instance args]
  (let [gname (gl-name name)]
    (case name
      "glfwInit" (long-array [1])
      "glfwCreateWindow" (long-array [1])
      "glfwGetTime" (long-array [(Double/doubleToRawLongBits
                                  (/ (System/nanoTime) 1000000000.0))])
      "GetWindowInnerWidth" (long-array [default-width])
      "GetWindowInnerHeight" (long-array [default-height])
      "emscripten_get_element_css_size" (do
                                          (write-f64! instance (aget args 1) default-width)
                                          (write-f64! instance (aget args 2) default-height)
                                          (long-array [0]))
      "emscripten_get_now" (long-array [(Double/doubleToRawLongBits
                                         (/ (System/nanoTime) 1000000.0))])
      (case gname
        ("glCreateProgram" "glCreateShader") (long-array [(next-gl-id)])
        ("glGenBuffers" "glGenFramebuffers" "glGenRenderbuffers"
         "glGenTextures" "glGenQueriesEXT" "glGenVertexArraysOES")
        (do
          (write-gl-ids! instance (aget args 0) (aget args 1))
          (vw/empty-result))
        ("glGetProgramiv" "glGetShaderiv")
        (do
          (write-i32! instance (aget args 2) 1)
          (vw/empty-result))
        "glCheckFramebufferStatus" (long-array [gl-framebuffer-complete])
        ("glGetAttribLocation" "glGetUniformLocation") (long-array [0])
        ("glIsBuffer" "glIsFramebuffer" "glIsProgram" "glIsRenderbuffer"
         "glIsShader" "glIsTexture" "glIsVertexArrayOES")
        (long-array [1])
        (zero-result results)))))

(defn- host-function
  [{:keys [module name params results]}]
  (vw/host-function {:module module
                     :name name
                     :params params
                     :results results
                     :f (fn [instance args]
                          (host-result name results instance args))}))

(defn host-functions
  []
  (->> (abi/wasm-imports)
       (filter #(= "env" (:module %)))
       (mapv host-function)))

(defn load-module
  []
  (vw/load-module {:resource "vybe/wasm/raylib.wasm"
                   :host-functions (host-functions)
                   :wasi-directories [["/" "/"]]
                   :initialize? true
                   :after-init vw/set-default-module!}))
