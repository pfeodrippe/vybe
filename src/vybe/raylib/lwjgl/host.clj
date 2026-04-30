(ns vybe.raylib.lwjgl.host
  (:require
   [vybe.raylib.wasm-abi :as raylib-abi])
  (:import
   (org.lwjgl.glfw GLFW GLFWErrorCallback)
   (org.lwjgl.opengl GL GL11)
   (org.lwjgl.system MemoryUtil)))

(set! *warn-on-reflection* true)

(defonce ^:private state*
  (atom {:window 0
         :initialized? false
         :width 0
         :height 0
         :title nil}))

(defn import-summary
  "Return the generated Raylib wasm import inventory grouped by host subsystem."
  []
  (raylib-abi/import-summary))

(defn initialized?
  []
  (:initialized? @state*))

(defn window
  []
  (:window @state*))

(defn window-size
  []
  (select-keys @state* [:width :height]))

(defn init-window!
  "Create a real GLFW/OpenGL window for the Raylib-wasm LWJGL host.

  Options:
    - :width  integer window width, default 600
    - :height integer window height, default 600
    - :title  window title, default \"Vybe Raylib Wasm/LWJGL\"

  This initializes LWJGL/GLFW and creates OpenGL capabilities for the current
  thread. On macOS this must run with `-XstartOnFirstThread`.
  "
  ([]
   (init-window! {}))
  ([{:keys [width height title]
     :or {width 600
          height 600
          title "Vybe Raylib Wasm/LWJGL"}}]
   (when-not (initialized?)
     (GLFWErrorCallback/createPrint System/err)
     (when-not (GLFW/glfwInit)
       (throw (ex-info "Unable to initialize GLFW for Raylib wasm LWJGL host" {})))
     (GLFW/glfwDefaultWindowHints)
     (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_TRUE)
     (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
     (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
     (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 3)
     (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)
     (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_FORWARD_COMPAT GLFW/GLFW_TRUE)
     (let [window (GLFW/glfwCreateWindow (int width)
                                         (int height)
                                         (str title)
                                         MemoryUtil/NULL
                                         MemoryUtil/NULL)]
       (when (zero? window)
         (GLFW/glfwTerminate)
         (throw (ex-info "Unable to create GLFW window for Raylib wasm LWJGL host"
                         {:width width
                          :height height
                          :title title})))
       (GLFW/glfwMakeContextCurrent window)
       (GLFW/glfwSwapInterval 0)
       (GL/createCapabilities)
       (GL11/glViewport 0 0 (int width) (int height))
       (swap! state* assoc
              :window window
              :initialized? true
              :width (int width)
              :height (int height)
              :title (str title))))
   @state*))

(defn poll-events!
  []
  (GLFW/glfwPollEvents))

(defn should-close?
  []
  (let [window (long (window))]
    (or (zero? window)
        (GLFW/glfwWindowShouldClose window))))

(defn clear!
  "Clear the host window using OpenGL directly.

  This is a host smoke operation, not a replacement for Raylib wasm rendering.
  The Raylib wasm host must eventually invoke equivalent GL calls through the
  generated wasm import table.
  "
  ([r g b a]
   (GL11/glClearColor (float r) (float g) (float b) (float a))
   (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT)))
  ([]
   (clear! 0.05 0.07 0.09 1.0)))

(defn swap-buffers!
  []
  (let [window (long (window))]
    (when-not (zero? window)
      (GLFW/glfwSwapBuffers window))))

(defn destroy!
  []
  (let [window (long (window))]
    (when-not (zero? window)
      (GLFW/glfwDestroyWindow window)))
  (when (:initialized? @state*)
    (GLFW/glfwTerminate))
  (reset! state* {:window 0
                  :initialized? false
                  :width 0
                  :height 0
                  :title nil})
  nil)

(defn unsupported-import!
  [import-name]
  (throw
   (ex-info "Raylib wasm import is not implemented by the LWJGL host yet"
            {:import (or (raylib-abi/find-import import-name)
                         {:name import-name})
             :category (raylib-abi/import-category {:name import-name})
             :summary (raylib-abi/import-summary)})))
