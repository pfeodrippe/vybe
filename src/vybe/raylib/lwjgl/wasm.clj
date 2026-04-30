(ns vybe.raylib.lwjgl.wasm
  (:require
   [clojure.string :as str]
   [vybe.raylib.lwjgl.host :as host]
   [vybe.raylib.wasm-abi :as raylib-abi]
   [vybe.wasm :as vw])
  (:import
   (java.nio.charset StandardCharsets)
   (java.nio ByteBuffer)
   (com.dylibso.chicory.runtime Instance Memory)
   (org.lwjgl.glfw GLFW GLFWCharCallbackI GLFWCursorEnterCallbackI
                   GLFWCursorPosCallbackI GLFWDropCallbackI GLFWErrorCallbackI
                   GLFWKeyCallbackI GLFWMouseButtonCallbackI GLFWScrollCallbackI
                   GLFWWindowContentScaleCallbackI GLFWWindowFocusCallbackI
                   GLFWWindowIconifyCallbackI GLFWWindowSizeCallbackI)
   (org.lwjgl.opengl GL11 GL13 GL14 GL15 GL20 GL30 GL31 GL33)))

(set! *warn-on-reflection* true)

(declare load-module)

(defonce ^:private host-string-ptrs* (atom {}))
(defonce ^:private host-string-next* (atom (- (* 64 1024 1024) (* 64 1024))))
(defonce ^:private host-memory-ptrs* (atom {}))
(defonce ^:private callback-ptrs* (atom {}))
(defonce ^:private callback-objects* (atom {}))
(defonce ^:private shader-types* (atom {}))
(defonce ^:private shader-sources* (atom {}))
(defonce ^:private trace-counts* (atom {}))
(defonce ^:private trace-enabled?*
  (delay
    (contains? #{"1" "true" "yes"}
               (some-> (or (System/getProperty "VYBE_RAYLIB_WASM_TRACE")
                           (System/getenv "VYBE_RAYLIB_WASM_TRACE"))
                       str/lower-case))))
(defonce ^:private trace-active-texture-slot* (atom 0))
(defonce ^:private trace-bound-textures* (atom {}))

(defn- memory
  ^Memory [^Instance instance]
  (.memory instance))

(defn- read-c-string*
  [^Instance instance ptr]
  (when (pos? (long ptr))
    (.readCString (memory instance) (int ptr) StandardCharsets/UTF_8)))

(defn- write-i32!*
  [^Instance instance ptr v]
  (when (pos? (long ptr))
    (.writeI32 (memory instance) (int ptr) (int v))))

(defn- read-i32*
  [^Instance instance ptr]
  (if (pos? (long ptr))
    (.readInt (memory instance) (int ptr))
    0))

(defn- read-f32*
  [^Instance instance ptr]
  (if (pos? (long ptr))
    (.readFloat (memory instance) (int ptr))
    0.0))

(defn- write-i64!*
  [^Instance instance ptr v]
  (when (pos? (long ptr))
    (.writeLong (memory instance) (int ptr) (long v))))

(defn- write-f32!*
  [^Instance instance ptr v]
  (when (pos? (long ptr))
    (.writeF32 (memory instance) (int ptr) (float v))))

(defn- write-f64!*
  [^Instance instance ptr v]
  (when (pos? (long ptr))
    (.writeF64 (memory instance) (int ptr) (double v))))

(defn- write-bytes!*
  [^Instance instance ptr ^bytes bytes]
  (.write (memory instance) (int ptr) ^bytes bytes)
  nil)

(defn- read-bytes*
  ^bytes [^Instance instance ptr size]
  (if (and (pos? (long ptr)) (pos? (long size)))
    (.readBytes (memory instance) (int ptr) (int size))
    (byte-array 0)))

(defn- write-c-string!*
  [^Instance instance ptr s]
  (when (pos? (long ptr))
    (let [bytes (.getBytes (str s) StandardCharsets/UTF_8)
          out (byte-array (inc (alength bytes)))]
      (System/arraycopy bytes 0 out 0 (alength bytes))
      (write-bytes!* instance ptr out))))

(defn- read-int-array*
  [^Instance instance ptr n]
  (let [values (int-array (int n))]
    (dotimes [i (int n)]
      (aset values i (int (read-i32* instance (+ (long ptr) (* i 4))))))
    values))

(defn- read-float-array*
  [^Instance instance ptr n]
  (let [values (float-array (int n))]
    (dotimes [i (int n)]
      (aset values i (float (read-f32* instance (+ (long ptr) (* i 4))))))
    values))

(defn- direct-buffer*
  ^ByteBuffer [^Instance instance ptr size]
  (when (and (pos? (long ptr)) (pos? (long size)))
    (let [bytes (read-bytes* instance ptr size)
          buf (ByteBuffer/allocateDirect (alength ^bytes bytes))]
      (.put buf ^bytes bytes)
      (.flip buf)
      buf)))

(defn- direct-output-buffer*
  ^ByteBuffer [size]
  (ByteBuffer/allocateDirect (max 0 (int size))))

(defn- copy-buffer-to-memory!*
  [^Instance instance ptr ^ByteBuffer buffer size]
  (when (and (pos? (long ptr)) (pos? (long size)))
    (let [bytes (byte-array (int size))]
      (.position buffer 0)
      (.get buffer bytes 0 (min (alength bytes) (.remaining buffer)))
      (write-bytes!* instance ptr bytes))))

(defn- host-memory-ptr
  [^Instance instance key size]
  (or (get @host-memory-ptrs* key)
      (let [aligned-size (* 8 (long (Math/ceil (/ (double (max 1 (long size))) 8.0))))
            ptr (- (swap! host-string-next* + aligned-size) aligned-size)]
        (write-bytes!* instance ptr (byte-array (int aligned-size)))
        (swap! host-memory-ptrs* assoc key ptr)
        ptr)))

(defn- host-string-ptr
  [^Instance instance s]
  (let [s (str s)]
    (or (get @host-string-ptrs* s)
        (let [bytes (.getBytes s StandardCharsets/UTF_8)
              size (inc (alength bytes))
              ptr (- (swap! host-string-next* + size) size)]
          (write-bytes!* instance ptr bytes)
          (.writeByte (memory instance) (int (+ ptr (alength bytes))) (byte 0))
          (swap! host-string-ptrs* assoc s ptr)
          ptr))))

(defn- f32
  [raw]
  (Float/intBitsToFloat (unchecked-int raw)))

(defn- bool
  [v]
  (not (zero? (long v))))

(defn- trace?
  []
  @trace-enabled?*)

(defn- trace!
  [& xs]
  (when (trace?)
    (binding [*out* *err*]
      (apply println xs))))

(defn- trace-limited!
  [k & xs]
  (when (trace?)
    (let [n (get (swap! trace-counts* update k (fnil inc 0)) k)]
      (when (<= n 20)
        (apply trace! xs)))))

(defn- trace-bound-texture-2d
  [slot]
  (let [active (GL11/glGetInteger GL13/GL_ACTIVE_TEXTURE)]
    (try
      (GL13/glActiveTexture (+ GL13/GL_TEXTURE0 (int slot)))
      (let [texture (GL11/glGetInteger GL11/GL_TEXTURE_BINDING_2D)]
        {:slot (int slot)
         :texture texture
         :gl-texture? (and (pos? texture) (GL11/glIsTexture texture))
         :width (when (pos? texture)
                  (GL11/glGetTexLevelParameteri GL11/GL_TEXTURE_2D 0
                                                GL11/GL_TEXTURE_WIDTH))
         :height (when (pos? texture)
                   (GL11/glGetTexLevelParameteri GL11/GL_TEXTURE_2D 0
                                                 GL11/GL_TEXTURE_HEIGHT))})
      (finally
        (GL13/glActiveTexture active)))))

(defn- truncate-source
  [source]
  (let [source (str source)]
    (if (> (count source) 4000)
      (str (subs source 0 4000) "\n/* ... shader source truncated ... */")
      source)))

(defn- gl-query-count
  [pname]
  (case (int pname)
    2978 4  ;; GL_VIEWPORT
    3088 4  ;; GL_SCISSOR_BOX
    3106 4  ;; GL_COLOR_CLEAR_VALUE
    2928 2  ;; GL_DEPTH_RANGE
    33901 2 ;; GL_ALIASED_POINT_SIZE_RANGE
    33902 2 ;; GL_ALIASED_LINE_WIDTH_RANGE
    2982 16 ;; GL_MODELVIEW_MATRIX
    2983 16 ;; GL_PROJECTION_MATRIX
    2984 16 ;; GL_TEXTURE_MATRIX
    34467 (max 1 (GL11/glGetInteger 34466)) ;; GL_COMPRESSED_TEXTURE_FORMATS
    1))

(defn- gl-type-size
  [type]
  (case (int type)
    5120 1 ;; GL_BYTE
    5121 1 ;; GL_UNSIGNED_BYTE
    5122 2 ;; GL_SHORT
    5123 2 ;; GL_UNSIGNED_SHORT
    5124 4 ;; GL_INT
    5125 4 ;; GL_UNSIGNED_INT
    5126 4 ;; GL_FLOAT
    32819 2 ;; GL_UNSIGNED_SHORT_4_4_4_4
    32820 2 ;; GL_UNSIGNED_SHORT_5_5_5_1
    33635 2 ;; GL_UNSIGNED_SHORT_5_6_5
    4))

(defn- gl-format-components
  [format]
  (case (int format)
    6401 1  ;; GL_STENCIL_INDEX
    6402 1  ;; GL_DEPTH_COMPONENT
    6403 1  ;; GL_RED
    6406 1  ;; GL_ALPHA
    6407 3  ;; GL_RGB
    6408 4  ;; GL_RGBA
    6409 1  ;; GL_LUMINANCE
    6410 2  ;; GL_LUMINANCE_ALPHA
    33319 2 ;; GL_RG
    34041 2 ;; GL_DEPTH_STENCIL
    4))

(defn- image-byte-size
  [width height format type]
  (* (max 0 (long width))
     (max 0 (long height))
     (long (gl-format-components format))
     (long (gl-type-size type))))

(defn- shader-source*
  [^Instance instance count string-ptrs lengths-ptr]
  (apply str
         (for [i (range (int count))
               :let [ptr (read-i32* instance (+ (long string-ptrs) (* i 4)))
                     len (when (pos? (long lengths-ptr))
                           (read-i32* instance (+ (long lengths-ptr) (* i 4))))]]
           (if (and len (not (neg? (long len))))
             (String. ^bytes (.readBytes (memory instance) (int ptr) (int len))
                      StandardCharsets/UTF_8)
             (or (read-c-string* instance ptr) "")))))

(defn- strip-glsl-es-lines
  [source]
  (->> (str/split-lines source)
       (remove #(re-matches #"\s*precision\s+.*;\s*" %))
       (remove #(re-matches #"\s*#extension\s+GL_OES_standard_derivatives\s*:\s*enable\s*\s*" %))
       (str/join "\n")))

(defn- ensure-desktop-version
  [source]
  (if (re-find #"(?m)^\s*#version\b" source)
    (-> source
        (str/replace #"#version\s+100\s*" "#version 330 core\n")
        (str/replace #"#version\s+300\s+es\s*" "#version 330 core\n"))
    (str "#version 330 core\n" source)))

(defn- ensure-fragment-output
  [source]
  (if (and (str/includes? source "finalColor")
           (not (re-find #"(?m)^\s*out\s+vec4\s+finalColor\s*;" source)))
    (str/replace source
                 #"(?m)^\s*#version[^\n]*\n"
                 "$0out vec4 finalColor;\n")
    source))

(defn- translate-glsl-source
  [shader-type source]
  (let [source (strip-glsl-es-lines source)
        source (-> source
                   (str/replace "\r" "")
                   ensure-desktop-version
                   (str/replace #"(?i)\b(?:lowp|mediump|highp)\s+(?=(?:float|int|bool|vec|mat|sampler))" "")
                   (str/replace #"(?i)\b(\d+(?:\.\d+)?)f\b" "$1")
                   (str/replace #"\btexture2D\b" "texture")
                   (str/replace #"\btextureCube\b" "texture"))]
    (case (int shader-type)
      35633 ;; GL_VERTEX_SHADER
      (-> source
          (str/replace #"\battribute\b" "in")
          (str/replace #"\bvarying\b" "out"))

      35632 ;; GL_FRAGMENT_SHADER
      (-> source
          (str/replace #"\bvarying\b" "in")
          (str/replace #"\bgl_FragColor\b" "finalColor")
          ensure-fragment-output)

      source)))

(defn- write-float-array!*
  [^Instance instance ptr ^floats values]
  (doseq [i (range (alength ^floats values))]
    (write-f32!* instance (+ (long ptr) (* i 4)) (aget ^floats values i))))

(defn- write-int-array!*
  [^Instance instance ptr ^ints values]
  (doseq [i (range (alength ^ints values))]
    (write-i32!* instance (+ (long ptr) (* i 4)) (aget ^ints values i))))

(defn- write-active-name!*
  [^Instance instance length-ptr name-ptr max-length s]
  (let [bytes (.getBytes (str s) StandardCharsets/UTF_8)
        n (max 0 (min (alength bytes) (dec (max 1 (int max-length)))))
        out (byte-array (inc n))]
    (System/arraycopy bytes 0 out 0 n)
    (write-i32!* instance length-ptr n)
    (write-bytes!* instance name-ptr out)))

(defn- fd-write!
  [^Instance instance fd iovs iovs-len nwritten]
  (let [out (StringBuilder.)
        total (volatile! 0)]
    (dotimes [i (int iovs-len)]
      (let [entry (+ (long iovs) (* i 8))
            ptr (read-i32* instance entry)
            len (read-i32* instance (+ entry 4))]
        (when (pos? len)
          (.append out (String. ^bytes (.readBytes (memory instance) (int ptr) (int len))
                                StandardCharsets/UTF_8))
          (vswap! total + len))))
    (when (pos? @total)
      (binding [*out* (if (= 2 (long fd)) *err* *out*)]
        (print (str out))
        (flush)))
    (write-i32!* instance nwritten @total)
    0))

(defn- result
  [v]
  (long-array [(long v)]))

(defn- double-result
  [v]
  (long-array [(Double/doubleToRawLongBits (double v))]))

(defn- f32-bits
  [v]
  (Float/floatToRawIntBits (float v)))

(defn- f64-bits
  [v]
  (Double/doubleToRawLongBits (double v)))

(defn- window-handle
  [window-id]
  (if (zero? (long window-id))
    (long (host/window))
    (long (host/window))))

(defn- wasm-window-handle
  []
  1)

(defn- call-callback!
  [^Instance instance callback-ptr args]
  (when (pos? (long callback-ptr))
    (let [function-index (.ref (.table instance 0) (int callback-ptr))]
      (.call (.getMachine instance) function-index (long-array args)))))

(defn- set-callback-ptr!
  [name callback-ptr]
  (let [previous (long (get @callback-ptrs* name 0))]
    (swap! callback-ptrs* assoc name (long callback-ptr))
    previous))

(defn- retain-callback!
  [name callback]
  (swap! callback-objects* assoc name callback)
  callback)

(defn- glfw-result
  [name ^Instance instance ^longs args]
  (case name
    "glfwInit"
    (result (if (GLFW/glfwInit) 1 0))

    "glfwDefaultWindowHints"
    (do (GLFW/glfwDefaultWindowHints) (vw/empty-result))

    "glfwWindowHint"
    (do (GLFW/glfwWindowHint (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    "glfwCreateWindow"
    (let [width (int (aget args 0))
          height (int (aget args 1))
          title (or (read-c-string* instance (aget args 2)) "Vybe Raylib Wasm/LWJGL")]
      (host/init-window! {:width width :height height :title title})
      (result 1))

    "glfwMakeContextCurrent"
    (do (GLFW/glfwMakeContextCurrent (window-handle (aget args 0)))
        (vw/empty-result))

    "glfwSwapBuffers"
    (do (GLFW/glfwSwapBuffers (window-handle (aget args 0)))
        (vw/empty-result))

    "glfwGetTime"
    (let [t (GLFW/glfwGetTime)]
      (trace-limited! :glfwGetTime :raylib-wasm/glfwGetTime t)
      (double-result t))

    "glfwSetWindowShouldClose"
    (do (GLFW/glfwSetWindowShouldClose (window-handle (aget args 0)) (bool (aget args 1)))
        (vw/empty-result))

    "glfwDestroyWindow"
    (do (host/destroy!) (vw/empty-result))

    "glfwTerminate"
    (do (host/destroy!) (vw/empty-result))

    "glfwGetPrimaryMonitor"
    (result 1)

    "glfwGetVideoModes"
    (let [mode-ptr (host-memory-ptr instance "glfw-video-mode" 24)
          monitor (GLFW/glfwGetPrimaryMonitor)
          mode (when-not (zero? monitor)
                 (GLFW/glfwGetVideoMode monitor))
          width (if mode (.width mode) (:width (host/window-size)))
          height (if mode (.height mode) (:height (host/window-size)))
          red-bits (if mode (.redBits mode) 8)
          green-bits (if mode (.greenBits mode) 8)
          blue-bits (if mode (.blueBits mode) 8)
          refresh-rate (if mode (.refreshRate mode) 60)]
      (write-i32!* instance (aget args 1) 1)
      (doseq [[offset value] {0 width 4 height 8 red-bits 12 green-bits
                              16 blue-bits 20 refresh-rate}]
        (write-i32!* instance (+ mode-ptr offset) value))
      (result mode-ptr))

    "glfwSetWindowSize"
    (do (GLFW/glfwSetWindowSize (window-handle (aget args 0))
                                (int (aget args 1))
                                (int (aget args 2)))
        (vw/empty-result))

    "glfwSetWindowAttrib"
    (do (GLFW/glfwSetWindowAttrib (window-handle (aget args 0))
                                  (int (aget args 1))
                                  (int (aget args 2)))
        (vw/empty-result))

    "glfwSetCursorPos"
    (do (GLFW/glfwSetCursorPos (window-handle (aget args 0))
                               (Double/longBitsToDouble (aget args 1))
                               (Double/longBitsToDouble (aget args 2)))
        (vw/empty-result))

    "glfwSetErrorCallback"
    (let [callback-ptr (aget args 0)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetErrorCallback
       (retain-callback!
        name
        (reify GLFWErrorCallbackI
          (invoke [_ error _description]
            (call-callback! instance callback-ptr
                            [(int error) (int 0)])))))
      (result previous))

    "glfwSetWindowSizeCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetWindowSizeCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWWindowSizeCallbackI
          (invoke [_ _window width height]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (int width) (int height)])))))
      (result previous))

    "glfwSetWindowIconifyCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetWindowIconifyCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWWindowIconifyCallbackI
          (invoke [_ _window iconified]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (if iconified 1 0)])))))
      (result previous))

    "glfwSetWindowFocusCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetWindowFocusCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWWindowFocusCallbackI
          (invoke [_ _window focused]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (if focused 1 0)])))))
      (result previous))

    "glfwSetDropCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetDropCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWDropCallbackI
          (invoke [_ _window count names]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (int count) (long names)])))))
      (result previous))

    "glfwSetWindowContentScaleCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetWindowContentScaleCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWWindowContentScaleCallbackI
          (invoke [_ _window xscale yscale]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (f32-bits xscale) (f32-bits yscale)])))))
      (result previous))

    "glfwSetKeyCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetKeyCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWKeyCallbackI
          (invoke [_ _window key scancode action mods]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (int key) (int scancode)
                             (int action) (int mods)])))))
      (result previous))

    "glfwSetCharCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetCharCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWCharCallbackI
          (invoke [_ _window codepoint]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (int codepoint)])))))
      (result previous))

    "glfwSetMouseButtonCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetMouseButtonCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWMouseButtonCallbackI
          (invoke [_ _window button action mods]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (int button) (int action) (int mods)])))))
      (result previous))

    "glfwSetCursorPosCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetCursorPosCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWCursorPosCallbackI
          (invoke [_ _window xpos ypos]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (f64-bits xpos) (f64-bits ypos)])))))
      (result previous))

    "glfwSetScrollCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetScrollCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWScrollCallbackI
          (invoke [_ _window xoffset yoffset]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (f64-bits xoffset) (f64-bits yoffset)])))))
      (result previous))

    "glfwSetCursorEnterCallback"
    (let [callback-ptr (aget args 1)
          previous (set-callback-ptr! name callback-ptr)]
      (GLFW/glfwSetCursorEnterCallback
       (window-handle (aget args 0))
       (retain-callback!
        name
        (reify GLFWCursorEnterCallbackI
          (invoke [_ _window entered]
            (call-callback! instance callback-ptr
                            [(wasm-window-handle) (if entered 1 0)])))))
      (result previous))

    (host/unsupported-import! name)))

(defn- gl-result
  [name ^Instance instance ^longs args]
  (case name
    ("glViewport" "emscripten_glViewport")
    (do (GL11/glViewport (int (aget args 0)) (int (aget args 1))
                         (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glClearColor" "emscripten_glClearColor")
    (do (GL11/glClearColor (f32 (aget args 0)) (f32 (aget args 1))
                           (f32 (aget args 2)) (f32 (aget args 3)))
        (vw/empty-result))

    ("glClear" "emscripten_glClear")
    (do (GL11/glClear (int (aget args 0)))
        (vw/empty-result))

    ("glGetError" "emscripten_glGetError")
    (result (GL11/glGetError))

    ("glGetString" "emscripten_glGetString")
    (result (host-string-ptr instance (or (GL11/glGetString (int (aget args 0))) "")))

    ("glGetFloatv" "emscripten_glGetFloatv")
    (let [pname (int (aget args 0))
          values (float-array (gl-query-count pname))]
      (GL11/glGetFloatv pname values)
      (write-float-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glGetIntegerv" "emscripten_glGetIntegerv")
    (let [pname (int (aget args 0))
          values (int-array (gl-query-count pname))]
      (GL11/glGetIntegerv pname values)
      (write-int-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glGetBooleanv" "emscripten_glGetBooleanv")
    (do (write-i32!* instance (aget args 1)
                     (if (GL11/glGetBoolean (int (aget args 0))) 1 0))
        (vw/empty-result))

    ("glBindTexture" "emscripten_glBindTexture")
    (do (when (trace?)
          (let [target (int (aget args 0))
                texture (int (aget args 1))
                slot @trace-active-texture-slot*]
            (swap! trace-bound-textures* assoc [slot target] texture)
            (trace-limited! :glBindTexture
                            :raylib-wasm/glBindTexture
                            {:slot slot
                             :target target
                             :texture texture
                             :gl-texture? (and (pos? texture)
                                               (GL11/glIsTexture texture))})))
        (GL11/glBindTexture (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glGenTextures" "emscripten_glGenTextures")
    (let [n (int (aget args 0))
          values (int-array n)]
      (GL11/glGenTextures values)
      (write-int-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glDeleteTextures" "emscripten_glDeleteTextures")
    (do (GL11/glDeleteTextures ^ints (read-int-array* instance (aget args 1) (aget args 0)))
        (vw/empty-result))

    ("glTexParameteri" "emscripten_glTexParameteri")
    (do (GL11/glTexParameteri (int (aget args 0)) (int (aget args 1)) (int (aget args 2)))
        (vw/empty-result))

    ("glTexParameterf" "emscripten_glTexParameterf")
    (do (GL11/glTexParameterf (int (aget args 0)) (int (aget args 1)) (f32 (aget args 2)))
        (vw/empty-result))

    ("glTexParameterfv" "emscripten_glTexParameterfv")
    (do (GL11/glTexParameterfv (int (aget args 0)) (int (aget args 1))
                               ^floats (read-float-array* instance (aget args 2) 4))
        (vw/empty-result))

    ("glTexParameteriv" "emscripten_glTexParameteriv")
    (do (GL11/glTexParameteriv (int (aget args 0)) (int (aget args 1))
                               ^ints (read-int-array* instance (aget args 2) 4))
        (vw/empty-result))

    ("glGetTexParameterfv" "emscripten_glGetTexParameterfv")
    (let [values (float-array 4)]
      (GL11/glGetTexParameterfv (int (aget args 0)) (int (aget args 1)) values)
      (write-float-array!* instance (aget args 2) values)
      (vw/empty-result))

    ("glGetTexParameteriv" "emscripten_glGetTexParameteriv")
    (let [values (int-array 4)]
      (GL11/glGetTexParameteriv (int (aget args 0)) (int (aget args 1)) values)
      (write-int-array!* instance (aget args 2) values)
      (vw/empty-result))

    ("glTexImage2D" "emscripten_glTexImage2D")
    (let [width (int (aget args 3))
          height (int (aget args 4))
          format (int (aget args 6))
          type (int (aget args 7))
          data (direct-buffer* instance (aget args 8)
                               (image-byte-size width height format type))]
      (GL11/glTexImage2D (int (aget args 0)) (int (aget args 1)) (int (aget args 2))
                         width height (int (aget args 5)) format type data)
      (vw/empty-result))

    ("glTexSubImage2D" "emscripten_glTexSubImage2D")
    (let [width (int (aget args 4))
          height (int (aget args 5))
          format (int (aget args 6))
          type (int (aget args 7))
          data (direct-buffer* instance (aget args 8)
                               (image-byte-size width height format type))]
      (GL11/glTexSubImage2D (int (aget args 0)) (int (aget args 1))
                            (int (aget args 2)) (int (aget args 3))
                            width height format type data)
      (vw/empty-result))

    ("glCompressedTexImage2D" "emscripten_glCompressedTexImage2D")
    (let [image-size (int (aget args 6))
          data (direct-buffer* instance (aget args 7) image-size)]
      (GL13/glCompressedTexImage2D (int (aget args 0)) (int (aget args 1))
                                   (int (aget args 2)) (int (aget args 3))
                                   (int (aget args 4)) (int (aget args 5))
                                   (or data (direct-output-buffer* 0)))
      (vw/empty-result))

    ("glCompressedTexSubImage2D" "emscripten_glCompressedTexSubImage2D")
    (let [image-size (int (aget args 7))
          data (direct-buffer* instance (aget args 8) image-size)]
      (GL13/glCompressedTexSubImage2D (int (aget args 0)) (int (aget args 1))
                                      (int (aget args 2)) (int (aget args 3))
                                      (int (aget args 4)) (int (aget args 5))
                                      (int (aget args 6))
                                      (or data (direct-output-buffer* 0)))
      (vw/empty-result))

    ("glCopyTexImage2D" "emscripten_glCopyTexImage2D")
    (do (GL11/glCopyTexImage2D (int (aget args 0)) (int (aget args 1))
                               (int (aget args 2)) (int (aget args 3))
                               (int (aget args 4)) (int (aget args 5))
                               (int (aget args 6)) (int (aget args 7)))
        (vw/empty-result))

    ("glCopyTexSubImage2D" "emscripten_glCopyTexSubImage2D")
    (do (GL11/glCopyTexSubImage2D (int (aget args 0)) (int (aget args 1))
                                  (int (aget args 2)) (int (aget args 3))
                                  (int (aget args 4)) (int (aget args 5))
                                  (int (aget args 6)) (int (aget args 7)))
        (vw/empty-result))

    ("glPixelStorei" "emscripten_glPixelStorei")
    (do (GL11/glPixelStorei (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glDrawArrays" "emscripten_glDrawArrays")
    (do (GL11/glDrawArrays (int (aget args 0)) (int (aget args 1)) (int (aget args 2)))
        (vw/empty-result))

    ("glDrawElements" "emscripten_glDrawElements")
    (do (GL11/glDrawElements (int (aget args 0)) (int (aget args 1))
                             (int (aget args 2)) (long (aget args 3)))
        (vw/empty-result))

    ("glDrawArraysInstanced" "emscripten_glDrawArraysInstanced"
     "glDrawArraysInstancedANGLE" "emscripten_glDrawArraysInstancedANGLE")
    (do (GL31/glDrawArraysInstanced (int (aget args 0)) (int (aget args 1))
                                    (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glDrawElementsInstanced" "emscripten_glDrawElementsInstanced"
     "glDrawElementsInstancedANGLE" "emscripten_glDrawElementsInstancedANGLE")
    (do (GL31/glDrawElementsInstanced (int (aget args 0)) (int (aget args 1))
                                      (int (aget args 2)) (long (aget args 3))
                                      (int (aget args 4)))
        (vw/empty-result))

    ("glBindBuffer" "emscripten_glBindBuffer")
    (do (GL15/glBindBuffer (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glGenBuffers" "emscripten_glGenBuffers")
    (let [n (int (aget args 0))
          values (int-array n)]
      (GL15/glGenBuffers values)
      (write-int-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glDeleteBuffers" "emscripten_glDeleteBuffers")
    (do (GL15/glDeleteBuffers ^ints (read-int-array* instance (aget args 1) (aget args 0)))
        (vw/empty-result))

    ("glBufferData" "emscripten_glBufferData")
    (let [size (long (aget args 1))
          data (direct-buffer* instance (aget args 2) size)]
      (if data
        (GL15/glBufferData (int (aget args 0)) data (int (aget args 3)))
        (GL15/glBufferData (int (aget args 0)) size (int (aget args 3))))
      (vw/empty-result))

    ("glBufferSubData" "emscripten_glBufferSubData")
    (let [size (long (aget args 2))
          data (direct-buffer* instance (aget args 3) size)]
      (when data
        (GL15/glBufferSubData (int (aget args 0)) (long (aget args 1)) data))
      (vw/empty-result))

    ("glCreateShader" "emscripten_glCreateShader")
    (let [shader-type (int (aget args 0))
          shader (GL20/glCreateShader shader-type)]
      (swap! shader-types* assoc shader shader-type)
      (result shader))

    ("glShaderSource" "emscripten_glShaderSource")
    (let [shader (int (aget args 0))
          ^String source (->> (shader-source* instance (aget args 1)
                                              (aget args 2) (aget args 3))
                              (translate-glsl-source (get @shader-types* shader 0)))]
      (swap! shader-sources* assoc shader source)
      (GL20/glShaderSource (int (aget args 0)) source)
      (vw/empty-result))

    ("glCompileShader" "emscripten_glCompileShader")
    (let [shader (int (aget args 0))]
      (GL20/glCompileShader shader)
      (when (zero? (GL20/glGetShaderi shader GL20/GL_COMPILE_STATUS))
        (let [source (get @shader-sources* shader)
              path (format "/tmp/vybe-raylib-shader-%s.glsl" shader)]
          (when (trace?)
            (spit path source))
          (trace! :raylib-wasm/shader-compile-failed
                  {:shader shader
                   :type (get @shader-types* shader)
                   :log (GL20/glGetShaderInfoLog shader)
                   :path path
                   :source (truncate-source source)})))
      (vw/empty-result))

    ("glGetShaderiv" "emscripten_glGetShaderiv")
    (do (write-i32!* instance (aget args 2)
                     (GL20/glGetShaderi (int (aget args 0)) (int (aget args 1))))
        (vw/empty-result))

    ("glGetShaderInfoLog" "emscripten_glGetShaderInfoLog")
    (let [log (or (GL20/glGetShaderInfoLog (int (aget args 0)) (int (aget args 1))) "")]
      (write-i32!* instance (aget args 2) (count log))
      (write-c-string!* instance (aget args 3) log)
      (vw/empty-result))

    ("glGetShaderSource" "emscripten_glGetShaderSource")
    (let [source (or (GL20/glGetShaderSource (int (aget args 0)) (int (aget args 1))) "")]
      (write-i32!* instance (aget args 2) (count source))
      (write-c-string!* instance (aget args 3) source)
      (vw/empty-result))

    ("glDeleteShader" "emscripten_glDeleteShader")
    (do (swap! shader-types* dissoc (int (aget args 0)))
        (swap! shader-sources* dissoc (int (aget args 0)))
        (GL20/glDeleteShader (int (aget args 0)))
        (vw/empty-result))

    ("glIsShader" "emscripten_glIsShader")
    (result (if (GL20/glIsShader (int (aget args 0))) 1 0))

    ("glCreateProgram" "emscripten_glCreateProgram")
    (result (GL20/glCreateProgram))

    ("glAttachShader" "emscripten_glAttachShader")
    (do (GL20/glAttachShader (int (aget args 0)) (int (aget args 1))) (vw/empty-result))

    ("glLinkProgram" "emscripten_glLinkProgram")
    (let [program (int (aget args 0))]
      (GL20/glLinkProgram program)
      (when (zero? (GL20/glGetProgrami program GL20/GL_LINK_STATUS))
        (trace! :raylib-wasm/program-link-failed
                {:program program
                 :log (GL20/glGetProgramInfoLog program)}))
      (vw/empty-result))

    ("glGetProgramiv" "emscripten_glGetProgramiv")
    (do (write-i32!* instance (aget args 2)
                     (GL20/glGetProgrami (int (aget args 0)) (int (aget args 1))))
        (vw/empty-result))

    ("glGetProgramInfoLog" "emscripten_glGetProgramInfoLog")
    (let [log (or (GL20/glGetProgramInfoLog (int (aget args 0)) (int (aget args 1))) "")]
      (write-i32!* instance (aget args 2) (count log))
      (write-c-string!* instance (aget args 3) log)
      (vw/empty-result))

    ("glDeleteProgram" "emscripten_glDeleteProgram")
    (do (GL20/glDeleteProgram (int (aget args 0))) (vw/empty-result))

    ("glIsProgram" "emscripten_glIsProgram")
    (result (if (GL20/glIsProgram (int (aget args 0))) 1 0))

    ("glDetachShader" "emscripten_glDetachShader")
    (do (GL20/glDetachShader (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glValidateProgram" "emscripten_glValidateProgram")
    (do (GL20/glValidateProgram (int (aget args 0))) (vw/empty-result))

    ("glGetAttachedShaders" "emscripten_glGetAttachedShaders")
    (let [max-count (int (aget args 1))
          count (int-array 1)
          shaders (int-array (max 0 max-count))]
      (GL20/glGetAttachedShaders (int (aget args 0)) count shaders)
      (write-i32!* instance (aget args 2) (aget count 0))
      (write-int-array!* instance (aget args 3) shaders)
      (vw/empty-result))

    ("glGetActiveUniform" "emscripten_glGetActiveUniform")
    (let [size (int-array 1)
          type (int-array 1)
          name (GL20/glGetActiveUniform (int (aget args 0)) (int (aget args 1))
                                        (int (aget args 2)) size type)]
      (write-active-name!* instance (aget args 3) (aget args 6) (aget args 2) name)
      (write-i32!* instance (aget args 4) (aget size 0))
      (write-i32!* instance (aget args 5) (aget type 0))
      (vw/empty-result))

    ("glGetActiveAttrib" "emscripten_glGetActiveAttrib")
    (let [size (int-array 1)
          type (int-array 1)
          name (GL20/glGetActiveAttrib (int (aget args 0)) (int (aget args 1))
                                       (int (aget args 2)) size type)]
      (write-active-name!* instance (aget args 3) (aget args 6) (aget args 2) name)
      (write-i32!* instance (aget args 4) (aget size 0))
      (write-i32!* instance (aget args 5) (aget type 0))
      (vw/empty-result))

    ("glGetUniformLocation" "emscripten_glGetUniformLocation")
    (let [^String name (or (read-c-string* instance (aget args 1)) "")]
      (result (GL20/glGetUniformLocation (int (aget args 0)) name)))

    ("glGetAttribLocation" "emscripten_glGetAttribLocation")
    (let [^String name (or (read-c-string* instance (aget args 1)) "")]
      (result (GL20/glGetAttribLocation (int (aget args 0)) name)))

    ("glBindAttribLocation" "emscripten_glBindAttribLocation")
    (let [^String name (or (read-c-string* instance (aget args 2)) "")]
      (GL20/glBindAttribLocation (int (aget args 0)) (int (aget args 1)) name)
      (vw/empty-result))

    ("glUniform1i" "emscripten_glUniform1i")
    (do (when (trace?)
          (let [slot (int (aget args 1))]
            (trace-limited! [:glUniform1i (int (aget args 0))]
                            :raylib-wasm/glUniform1i
                            {:location (int (aget args 0))
                             :value slot
                             :active-slot @trace-active-texture-slot*
                             :actual-bound (trace-bound-texture-2d slot)
                             :traced-bound (get @trace-bound-textures*
                                                [slot GL11/GL_TEXTURE_2D])})))
        (GL20/glUniform1i (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glUniform1f" "emscripten_glUniform1f")
    (do (GL20/glUniform1f (int (aget args 0)) (f32 (aget args 1))) (vw/empty-result))

    ("glUniform2i" "emscripten_glUniform2i")
    (do (GL20/glUniform2i (int (aget args 0)) (int (aget args 1)) (int (aget args 2)))
        (vw/empty-result))

    ("glUniform3i" "emscripten_glUniform3i")
    (do (GL20/glUniform3i (int (aget args 0)) (int (aget args 1))
                          (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glUniform4i" "emscripten_glUniform4i")
    (do (GL20/glUniform4i (int (aget args 0)) (int (aget args 1))
                          (int (aget args 2)) (int (aget args 3)) (int (aget args 4)))
        (vw/empty-result))

    ("glUniform2f" "emscripten_glUniform2f")
    (do (GL20/glUniform2f (int (aget args 0)) (f32 (aget args 1)) (f32 (aget args 2)))
        (vw/empty-result))

    ("glUniform3f" "emscripten_glUniform3f")
    (do (GL20/glUniform3f (int (aget args 0)) (f32 (aget args 1))
                          (f32 (aget args 2)) (f32 (aget args 3)))
        (vw/empty-result))

    ("glUniform4f" "emscripten_glUniform4f")
    (do (GL20/glUniform4f (int (aget args 0)) (f32 (aget args 1))
                          (f32 (aget args 2)) (f32 (aget args 3)) (f32 (aget args 4)))
        (vw/empty-result))

    ("glUniformMatrix4fv" "emscripten_glUniformMatrix4fv")
    (do (GL20/glUniformMatrix4fv (int (aget args 0)) (boolean (bool (aget args 2)))
                                 ^floats (read-float-array* instance (aget args 3)
                                                            (* 16 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniformMatrix2fv" "emscripten_glUniformMatrix2fv")
    (do (GL20/glUniformMatrix2fv (int (aget args 0)) (boolean (bool (aget args 2)))
                                 ^floats (read-float-array* instance (aget args 3)
                                                            (* 4 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniformMatrix3fv" "emscripten_glUniformMatrix3fv")
    (do (GL20/glUniformMatrix3fv (int (aget args 0)) (boolean (bool (aget args 2)))
                                 ^floats (read-float-array* instance (aget args 3)
                                                            (* 9 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniform1fv" "emscripten_glUniform1fv")
    (do (GL20/glUniform1fv (int (aget args 0))
                           ^floats (read-float-array* instance (aget args 2)
                                                      (long (aget args 1))))
        (vw/empty-result))

    ("glUniform2fv" "emscripten_glUniform2fv")
    (do (GL20/glUniform2fv (int (aget args 0))
                           ^floats (read-float-array* instance (aget args 2)
                                                      (* 2 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniform3fv" "emscripten_glUniform3fv")
    (do (GL20/glUniform3fv (int (aget args 0))
                           ^floats (read-float-array* instance (aget args 2)
                                                      (* 3 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniform4fv" "emscripten_glUniform4fv")
    (do (GL20/glUniform4fv (int (aget args 0))
                           ^floats (read-float-array* instance (aget args 2)
                                                      (* 4 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniform1iv" "emscripten_glUniform1iv")
    (do (GL20/glUniform1iv (int (aget args 0))
                           ^ints (read-int-array* instance (aget args 2)
                                                  (long (aget args 1))))
        (vw/empty-result))

    ("glUniform2iv" "emscripten_glUniform2iv")
    (do (GL20/glUniform2iv (int (aget args 0))
                           ^ints (read-int-array* instance (aget args 2)
                                                  (* 2 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniform3iv" "emscripten_glUniform3iv")
    (do (GL20/glUniform3iv (int (aget args 0))
                           ^ints (read-int-array* instance (aget args 2)
                                                  (* 3 (long (aget args 1)))))
        (vw/empty-result))

    ("glUniform4iv" "emscripten_glUniform4iv")
    (do (GL20/glUniform4iv (int (aget args 0))
                           ^ints (read-int-array* instance (aget args 2)
                                                  (* 4 (long (aget args 1)))))
        (vw/empty-result))

    ("glGetUniformfv" "emscripten_glGetUniformfv")
    (let [values (float-array 16)]
      (GL20/glGetUniformfv (int (aget args 0)) (int (aget args 1)) values)
      (write-float-array!* instance (aget args 2) values)
      (vw/empty-result))

    ("glGetUniformiv" "emscripten_glGetUniformiv")
    (let [values (int-array 16)]
      (GL20/glGetUniformiv (int (aget args 0)) (int (aget args 1)) values)
      (write-int-array!* instance (aget args 2) values)
      (vw/empty-result))

    ("glEnableVertexAttribArray" "emscripten_glEnableVertexAttribArray")
    (do (GL20/glEnableVertexAttribArray (int (aget args 0))) (vw/empty-result))

    ("glDisableVertexAttribArray" "emscripten_glDisableVertexAttribArray")
    (do (GL20/glDisableVertexAttribArray (int (aget args 0))) (vw/empty-result))

    ("glVertexAttribPointer" "emscripten_glVertexAttribPointer")
    (do (GL20/glVertexAttribPointer (int (aget args 0)) (int (aget args 1))
                                    (int (aget args 2)) (boolean (bool (aget args 3)))
                                    (int (aget args 4)) (long (aget args 5)))
        (vw/empty-result))

    ("glVertexAttribDivisorANGLE" "emscripten_glVertexAttribDivisorANGLE")
    (do (GL33/glVertexAttribDivisor (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glVertexAttrib1f" "emscripten_glVertexAttrib1f")
    (do (GL20/glVertexAttrib1f (int (aget args 0)) (f32 (aget args 1))) (vw/empty-result))

    ("glVertexAttrib2f" "emscripten_glVertexAttrib2f")
    (do (GL20/glVertexAttrib2f (int (aget args 0)) (f32 (aget args 1)) (f32 (aget args 2)))
        (vw/empty-result))

    ("glVertexAttrib3f" "emscripten_glVertexAttrib3f")
    (do (GL20/glVertexAttrib3f (int (aget args 0)) (f32 (aget args 1))
                               (f32 (aget args 2)) (f32 (aget args 3)))
        (vw/empty-result))

    ("glVertexAttrib4f" "emscripten_glVertexAttrib4f")
    (do (GL20/glVertexAttrib4f (int (aget args 0)) (f32 (aget args 1))
                               (f32 (aget args 2)) (f32 (aget args 3)) (f32 (aget args 4)))
        (vw/empty-result))

    ("glVertexAttrib1fv" "emscripten_glVertexAttrib1fv")
    (let [^floats v (read-float-array* instance (aget args 1) 1)]
      (GL20/glVertexAttrib1f (int (aget args 0)) (aget v 0))
      (vw/empty-result))

    ("glVertexAttrib2fv" "emscripten_glVertexAttrib2fv")
    (let [^floats v (read-float-array* instance (aget args 1) 2)]
      (GL20/glVertexAttrib2f (int (aget args 0)) (aget v 0) (aget v 1))
      (vw/empty-result))

    ("glVertexAttrib3fv" "emscripten_glVertexAttrib3fv")
    (let [^floats v (read-float-array* instance (aget args 1) 3)]
      (GL20/glVertexAttrib3f (int (aget args 0)) (aget v 0) (aget v 1) (aget v 2))
      (vw/empty-result))

    ("glVertexAttrib4fv" "emscripten_glVertexAttrib4fv")
    (let [^floats v (read-float-array* instance (aget args 1) 4)]
      (GL20/glVertexAttrib4f (int (aget args 0)) (aget v 0) (aget v 1)
                             (aget v 2) (aget v 3))
      (vw/empty-result))

    ("glGetVertexAttribfv" "emscripten_glGetVertexAttribfv")
    (let [values (float-array 4)]
      (GL20/glGetVertexAttribfv (int (aget args 0)) (int (aget args 1)) values)
      (write-float-array!* instance (aget args 2) values)
      (vw/empty-result))

    ("glGetVertexAttribiv" "emscripten_glGetVertexAttribiv")
    (let [values (int-array 4)]
      (GL20/glGetVertexAttribiv (int (aget args 0)) (int (aget args 1)) values)
      (write-int-array!* instance (aget args 2) values)
      (vw/empty-result))

    ("glGetVertexAttribPointerv" "emscripten_glGetVertexAttribPointerv")
    (do (write-i32!* instance (aget args 2)
                     (GL20/glGetVertexAttribPointer (int (aget args 0)) (int (aget args 1))))
        (vw/empty-result))

    ("glBindVertexArray" "emscripten_glBindVertexArray"
     "glBindVertexArrayOES" "emscripten_glBindVertexArrayOES")
    (do (GL30/glBindVertexArray (int (aget args 0))) (vw/empty-result))

    ("glGenVertexArrays" "emscripten_glGenVertexArrays"
     "glGenVertexArraysOES" "emscripten_glGenVertexArraysOES")
    (let [n (int (aget args 0))
          values (int-array n)]
      (GL30/glGenVertexArrays values)
      (write-int-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glDeleteVertexArrays" "emscripten_glDeleteVertexArrays"
     "glDeleteVertexArraysOES" "emscripten_glDeleteVertexArraysOES")
    (do (GL30/glDeleteVertexArrays ^ints (read-int-array* instance (aget args 1) (aget args 0)))
        (vw/empty-result))

    ("glIsVertexArray" "emscripten_glIsVertexArray"
     "glIsVertexArrayOES" "emscripten_glIsVertexArrayOES")
    (result (if (GL30/glIsVertexArray (int (aget args 0))) 1 0))

    ("glEnable" "emscripten_glEnable")
    (do (GL11/glEnable (int (aget args 0))) (vw/empty-result))

    ("glDisable" "emscripten_glDisable")
    (do (GL11/glDisable (int (aget args 0))) (vw/empty-result))

    ("glDepthMask" "emscripten_glDepthMask")
    (do (GL11/glDepthMask (bool (aget args 0))) (vw/empty-result))

    ("glColorMask" "emscripten_glColorMask")
    (do (GL11/glColorMask (bool (aget args 0)) (bool (aget args 1))
                          (bool (aget args 2)) (bool (aget args 3)))
        (vw/empty-result))

    ("glClearStencil" "emscripten_glClearStencil")
    (do (GL11/glClearStencil (int (aget args 0))) (vw/empty-result))

    ("glCullFace" "emscripten_glCullFace")
    (do (GL11/glCullFace (int (aget args 0))) (vw/empty-result))

    ("glFrontFace" "emscripten_glFrontFace")
    (do (GL11/glFrontFace (int (aget args 0))) (vw/empty-result))

    ("glScissor" "emscripten_glScissor")
    (do (GL11/glScissor (int (aget args 0)) (int (aget args 1))
                        (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glLineWidth" "emscripten_glLineWidth")
    (do (GL11/glLineWidth (f32 (aget args 0))) (vw/empty-result))

    ("glClearDepthf" "emscripten_glClearDepthf")
    (do (GL11/glClearDepth (double (f32 (aget args 0)))) (vw/empty-result))

    ("glBlendFunc" "emscripten_glBlendFunc")
    (do (GL11/glBlendFunc (int (aget args 0)) (int (aget args 1))) (vw/empty-result))

    ("glBlendEquation" "emscripten_glBlendEquation")
    (do (GL14/glBlendEquation (int (aget args 0))) (vw/empty-result))

    ("glBlendColor" "emscripten_glBlendColor")
    (do (GL14/glBlendColor (f32 (aget args 0)) (f32 (aget args 1))
                           (f32 (aget args 2)) (f32 (aget args 3)))
        (vw/empty-result))

    ("glBlendFuncSeparate" "emscripten_glBlendFuncSeparate")
    (do (GL14/glBlendFuncSeparate (int (aget args 0)) (int (aget args 1))
                                  (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glBlendEquationSeparate" "emscripten_glBlendEquationSeparate")
    (do (GL20/glBlendEquationSeparate (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glDepthFunc" "emscripten_glDepthFunc")
    (do (GL11/glDepthFunc (int (aget args 0))) (vw/empty-result))

    ("glDepthRangef" "emscripten_glDepthRangef")
    (do (GL11/glDepthRange (double (f32 (aget args 0))) (double (f32 (aget args 1))))
        (vw/empty-result))

    ("glPolygonOffset" "emscripten_glPolygonOffset")
    (do (GL11/glPolygonOffset (f32 (aget args 0)) (f32 (aget args 1)))
        (vw/empty-result))

    ("glStencilFunc" "emscripten_glStencilFunc")
    (do (GL11/glStencilFunc (int (aget args 0)) (int (aget args 1)) (int (aget args 2)))
        (vw/empty-result))

    ("glStencilMask" "emscripten_glStencilMask")
    (do (GL11/glStencilMask (int (aget args 0))) (vw/empty-result))

    ("glStencilOp" "emscripten_glStencilOp")
    (do (GL11/glStencilOp (int (aget args 0)) (int (aget args 1)) (int (aget args 2)))
        (vw/empty-result))

    ("glStencilFuncSeparate" "emscripten_glStencilFuncSeparate")
    (do (GL20/glStencilFuncSeparate (int (aget args 0)) (int (aget args 1))
                                    (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glStencilMaskSeparate" "emscripten_glStencilMaskSeparate")
    (do (GL20/glStencilMaskSeparate (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glStencilOpSeparate" "emscripten_glStencilOpSeparate")
    (do (GL20/glStencilOpSeparate (int (aget args 0)) (int (aget args 1))
                                  (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glActiveTexture" "emscripten_glActiveTexture")
    (do (when (trace?)
          (let [raw (int (aget args 0))
                slot (- raw GL13/GL_TEXTURE0)]
            (reset! trace-active-texture-slot* slot)
            (trace-limited! :glActiveTexture
                            :raylib-wasm/glActiveTexture
                            {:raw raw
                             :slot slot})))
        (GL13/glActiveTexture (int (aget args 0)))
        (vw/empty-result))

    ("glUseProgram" "emscripten_glUseProgram")
    (do (GL20/glUseProgram (int (aget args 0))) (vw/empty-result))

    ("glFinish" "emscripten_glFinish")
    (do (GL11/glFinish) (vw/empty-result))

    ("glFlush" "emscripten_glFlush")
    (do (GL11/glFlush) (vw/empty-result))

    ("glIsBuffer" "emscripten_glIsBuffer")
    (result (if (GL15/glIsBuffer (int (aget args 0))) 1 0))

    ("glIsEnabled" "emscripten_glIsEnabled")
    (result (if (GL11/glIsEnabled (int (aget args 0))) 1 0))

    ("glIsTexture" "emscripten_glIsTexture")
    (result (if (GL11/glIsTexture (int (aget args 0))) 1 0))

    ("glGetBufferParameteriv" "emscripten_glGetBufferParameteriv")
    (do (write-i32!* instance (aget args 2)
                     (GL15/glGetBufferParameteri (int (aget args 0)) (int (aget args 1))))
        (vw/empty-result))

    ("glDrawBuffers" "emscripten_glDrawBuffers"
     "glDrawBuffersEXT" "emscripten_glDrawBuffersEXT"
     "glDrawBuffersWEBGL" "emscripten_glDrawBuffersWEBGL")
    (do (GL20/glDrawBuffers ^ints (read-int-array* instance (aget args 1) (aget args 0)))
        (vw/empty-result))

    ("glBindFramebuffer" "emscripten_glBindFramebuffer")
    (do (GL30/glBindFramebuffer (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glBlitFramebuffer" "emscripten_glBlitFramebuffer")
    (do (GL30/glBlitFramebuffer (int (aget args 0)) (int (aget args 1))
                                 (int (aget args 2)) (int (aget args 3))
                                 (int (aget args 4)) (int (aget args 5))
                                 (int (aget args 6)) (int (aget args 7))
                                 (int (aget args 8)) (int (aget args 9)))
        (vw/empty-result))

    ("glGenFramebuffers" "emscripten_glGenFramebuffers")
    (let [n (int (aget args 0))
          values (int-array n)]
      (GL30/glGenFramebuffers values)
      (write-int-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glDeleteFramebuffers" "emscripten_glDeleteFramebuffers")
    (do (GL30/glDeleteFramebuffers ^ints (read-int-array* instance (aget args 1) (aget args 0)))
        (vw/empty-result))

    ("glIsFramebuffer" "emscripten_glIsFramebuffer")
    (result (if (GL30/glIsFramebuffer (int (aget args 0))) 1 0))

    ("glFramebufferTexture2D" "emscripten_glFramebufferTexture2D")
    (do (GL30/glFramebufferTexture2D (int (aget args 0)) (int (aget args 1))
                                     (int (aget args 2)) (int (aget args 3))
                                     (int (aget args 4)))
        (vw/empty-result))

    ("glCheckFramebufferStatus" "emscripten_glCheckFramebufferStatus")
    (result (GL30/glCheckFramebufferStatus (int (aget args 0))))

    ("glGetFramebufferAttachmentParameteriv" "emscripten_glGetFramebufferAttachmentParameteriv")
    (do (write-i32!* instance (aget args 3)
                     (GL30/glGetFramebufferAttachmentParameteri
                      (int (aget args 0)) (int (aget args 1)) (int (aget args 2))))
        (vw/empty-result))

    ("glBindRenderbuffer" "emscripten_glBindRenderbuffer")
    (do (GL30/glBindRenderbuffer (int (aget args 0)) (int (aget args 1)))
        (vw/empty-result))

    ("glGenRenderbuffers" "emscripten_glGenRenderbuffers")
    (let [n (int (aget args 0))
          values (int-array n)]
      (GL30/glGenRenderbuffers values)
      (write-int-array!* instance (aget args 1) values)
      (vw/empty-result))

    ("glDeleteRenderbuffers" "emscripten_glDeleteRenderbuffers")
    (do (GL30/glDeleteRenderbuffers ^ints (read-int-array* instance (aget args 1) (aget args 0)))
        (vw/empty-result))

    ("glIsRenderbuffer" "emscripten_glIsRenderbuffer")
    (result (if (GL30/glIsRenderbuffer (int (aget args 0))) 1 0))

    ("glRenderbufferStorage" "emscripten_glRenderbufferStorage")
    (do (GL30/glRenderbufferStorage (int (aget args 0)) (int (aget args 1))
                                    (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glGetRenderbufferParameteriv" "emscripten_glGetRenderbufferParameteriv")
    (do (write-i32!* instance (aget args 2)
                     (GL30/glGetRenderbufferParameteri
                      (int (aget args 0)) (int (aget args 1))))
        (vw/empty-result))

    ("glFramebufferRenderbuffer" "emscripten_glFramebufferRenderbuffer")
    (do (GL30/glFramebufferRenderbuffer (int (aget args 0)) (int (aget args 1))
                                        (int (aget args 2)) (int (aget args 3)))
        (vw/empty-result))

    ("glGenerateMipmap" "emscripten_glGenerateMipmap")
    (do (GL30/glGenerateMipmap (int (aget args 0))) (vw/empty-result))

    ("glReadPixels" "emscripten_glReadPixels")
    (let [width (int (aget args 2))
          height (int (aget args 3))
          format (int (aget args 4))
          type (int (aget args 5))
          size (image-byte-size width height format type)
          out (direct-output-buffer* size)]
      (GL11/glReadPixels (int (aget args 0)) (int (aget args 1))
                         width height format type out)
      (copy-buffer-to-memory!* instance (aget args 6) out size)
      (vw/empty-result))

    ("glGetShaderPrecisionFormat" "emscripten_glGetShaderPrecisionFormat")
    (do (write-i32!* instance (aget args 2) 127)
        (write-i32!* instance (+ (long (aget args 2)) 4) 127)
        (write-i32!* instance (aget args 3) 23)
        (vw/empty-result))

    (host/unsupported-import! name)))

(defn- runtime-result
  [name ^Instance instance ^longs args]
  (case name
    "GetWindowInnerWidth" (result (:width (host/window-size)))
    "GetWindowInnerHeight" (result (:height (host/window-size)))
    "emscripten_get_element_css_size" (do (write-f64!* instance (aget args 1) (:width (host/window-size)))
                                          (write-f64!* instance (aget args 2) (:height (host/window-size)))
                                          (result 0))
    "emscripten_set_canvas_element_size" (do (write-i32!* instance 0 0) (result 0))
    "emscripten_set_window_title" (do (host/init-window! {:title (or (read-c-string* instance (aget args 0))
                                                                     "Vybe Raylib Wasm/LWJGL")})
                                      (vw/empty-result))
    "emscripten_sleep" (do (Thread/sleep (long (aget args 0))) (vw/empty-result))
    "emscripten_notify_memory_growth" (vw/empty-result)
    ("emscripten_set_fullscreenchange_callback_on_thread"
     "emscripten_set_pointerlockchange_callback_on_thread"
     "emscripten_set_resize_callback_on_thread"
     "emscripten_set_click_callback_on_thread"
     "emscripten_set_touchstart_callback_on_thread"
     "emscripten_set_touchend_callback_on_thread"
     "emscripten_set_touchmove_callback_on_thread"
     "emscripten_set_touchcancel_callback_on_thread"
     "emscripten_set_gamepadconnected_callback_on_thread"
     "emscripten_set_gamepaddisconnected_callback_on_thread"
     "emscripten_request_pointerlock"
     "emscripten_exit_pointerlock"
     "emscripten_sample_gamepad_data")
    (result 0)
    "emscripten_get_num_gamepads" (result 0)
    "emscripten_get_gamepad_status" (result -1)
    "emscripten_run_script" (vw/empty-result)
    "emscripten_asm_const_int" (result 0)
    "_Unwind_RaiseException" (result 0)
    "__syscall_getcwd" (do (write-c-string!* instance (aget args 0) "/")
                           (result (aget args 0)))
    "__syscall_chdir" (result 0)
    "__syscall_faccessat" (result 0)
    "__syscall_getdents64" (result 0)
    "clock_time_get" (do (write-i64!* instance (aget args 2) (System/nanoTime))
                         (result 0))
    "fd_write" (result (fd-write! instance (aget args 0) (aget args 1)
                                  (aget args 2) (aget args 3)))
    "fd_read" (do (write-i32!* instance (aget args 3) 0) (result 0))
    "fd_seek" (do (write-i64!* instance (aget args 3) 0) (result 0))
    "fd_fdstat_get" (do (write-i32!* instance (aget args 1) 0)
                        (write-i32!* instance (+ (long (aget args 1)) 4) 0)
                        (write-i64!* instance (+ (long (aget args 1)) 8) 0)
                        (write-i64!* instance (+ (long (aget args 1)) 16) 0)
                        (result 0))
    "fd_close" (result 0)
    "proc_exit" (throw (ex-info "Raylib wasm requested proc_exit" {:code (aget args 0)}))
    (host/unsupported-import! name)))

(defn- host-result
  [name ^Instance instance ^longs args]
  (case (raylib-abi/import-category {:name name})
    :gl (gl-result name instance args)
    :glfw (glfw-result name instance args)
    :emscripten (runtime-result name instance args)
    :runtime (runtime-result name instance args)
    :other (runtime-result name instance args)
    (host/unsupported-import! name)))

(defn- host-function
  [{:keys [module name params results]}]
  (vw/host-function {:module module
                     :name name
                     :params params
                     :results results
                     :f (fn [^Instance instance ^longs args]
                          (host-result name instance args))}))

(defn host-functions
  []
  (->> (raylib-abi/wasm-imports)
       (filter #(= "env" (:module %)))
       (mapv host-function)))

(defn load-module
  []
  (vw/load-module {:resource "vybe/wasm/raylib.wasm"
                   :host-functions (host-functions)
                   :wasi-directories [["/" "/"]]
                   :initialize? false}))

(defonce ^:private module* (delay (load-module)))

(defn module
  []
  @module*)

(defn poll-events!
  []
  (host/poll-events!))

(defn should-close?
  []
  (host/should-close?))

(defn call
  [name & args]
  (apply vw/call (module) name args))

(defn color-arg
  [[r g b a]]
  (bit-or (long r)
          (bit-shift-left (long g) 8)
          (bit-shift-left (long b) 16)
          (bit-shift-left (long (or a 255)) 24)))
