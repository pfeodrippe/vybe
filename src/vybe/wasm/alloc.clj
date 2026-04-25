(ns vybe.wasm.alloc
  (:require
   [vybe.wasm.memory :as mem]
   [vybe.wasm.runtime :as rt])
  (:import
   (java.nio.charset StandardCharsets)))

(defn malloc
  "Allocate `bytes` inside the Wasm module with exported `malloc`."
  [module bytes]
  (rt/call module "malloc" bytes))

(defn free
  "Free a non-null Wasm pointer with exported `free`."
  [module p]
  (when (mem/ptr? p)
    (rt/call module "free" p))
  nil)

(defn write-c-string!
  "Allocate and write `s` as a UTF-8 null-terminated string in Wasm memory."
  [module s]
  (let [bytes (.getBytes (str s) StandardCharsets/UTF_8)
        len (alength bytes)
        p (malloc module (inc len))]
    (mem/write-bytes! module p bytes)
    (mem/write-i8! module (+ p len) 0)
    p))

(defn read-c-string
  "Read a UTF-8 null-terminated string from Wasm memory."
  [module p]
  (mem/read-c-string module p))

(defn with-alloc*
  "Allocate `bytes`, call `f` with the pointer, then free it."
  [module bytes f]
  (let [p (malloc module bytes)]
    (try
      (f p)
      (finally
        (free module p)))))

(defmacro with-alloc
  "Bind `ptr` to `bytes` allocated in `module` for the scope of `body`."
  [[ptr module bytes] & body]
  `(with-alloc* ~module ~bytes (fn [~ptr] ~@body)))

(defn with-c-string*
  "Allocate `s` as a C string, call `f`, then free it."
  [module s f]
  (let [p (write-c-string! module s)]
    (try
      (f p)
      (finally
        (free module p)))))

(defmacro with-c-string
  "Bind `ptr` to a temporary C string in `module` for the scope of `body`."
  [[ptr module s] & body]
  `(with-c-string* ~module ~s (fn [~ptr] ~@body)))
