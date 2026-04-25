(ns vybe.wasm.memory
  (:import
   (com.dylibso.chicory.runtime Memory)
   (java.nio.charset StandardCharsets)))

(defn memory
  "Return the Chicory memory for a loaded `vybe.wasm` module."
  ^Memory [module]
  (:memory module))

(defn ptr
  "Normalize a wasm32 pointer offset to a JVM long."
  [v]
  (long (or v 0)))

(defn null?
  "Return true when `v` is a null wasm32 pointer."
  [v]
  (zero? (ptr v)))

(defn ptr?
  "Return true when `v` is a non-null wasm32 pointer."
  [v]
  (pos? (ptr v)))

(defn u32
  "Return the unsigned 32-bit representation of `v` as a long."
  [v]
  (bit-and (long v) 0xffffffff))

(defn u64
  "Return `v` coerced to a long for unsigned 64-bit Flecs ids."
  [v]
  (long v))

(defn offset
  "Add byte offset `n` to wasm pointer `p`."
  [p n]
  (+ (ptr p) (long n)))

(defn read-i8 [module p] (.read (memory module) (int p)))
(defn write-i8! [module p v] (.writeByte (memory module) (int p) (byte v)) nil)
(defn read-i16 [module p] (.readShort (memory module) (int p)))
(defn write-i16! [module p v] (.writeShort (memory module) (int p) (short v)) nil)
(defn read-i32 [module p] (.readInt (memory module) (int p)))
(defn write-i32! [module p v] (.writeI32 (memory module) (int p) (int v)) nil)
(defn read-i64 [module p] (.readLong (memory module) (int p)))
(defn write-i64! [module p v] (.writeLong (memory module) (int p) (long v)) nil)
(defn read-f32 [module p] (.readFloat (memory module) (int p)))
(defn write-f32! [module p v] (.writeF32 (memory module) (int p) (float v)) nil)
(defn read-f64 [module p] (.readDouble (memory module) (int p)))
(defn write-f64! [module p v] (.writeF64 (memory module) (int p) (double v)) nil)

(defn read-bytes
  "Read `size` bytes from wasm memory."
  [module p size]
  (.readBytes (memory module) (int p) (int size)))

(defn write-bytes!
  "Write byte array `bytes` into wasm memory."
  ([module p bytes]
   (.write (memory module) (int p) ^bytes bytes)
   nil)
  ([module p bytes off size]
   (.write (memory module) (int p) ^bytes bytes (int off) (int size))
   nil))

(defn fill!
  "Fill `[p, p + size)` in wasm memory with byte value `v`."
  [module p size v]
  (.fill (memory module) (byte v) (int p) (int (+ p size)))
  nil)

(defn zero!
  "Fill `[p, p + size)` in wasm memory with zero bytes."
  [module p size]
  (fill! module p size 0)
  p)

(defn read-c-string
  "Read a UTF-8 null-terminated C string from wasm memory."
  [module p]
  (when (ptr? p)
    (.readCString (memory module) (int p) StandardCharsets/UTF_8)))
