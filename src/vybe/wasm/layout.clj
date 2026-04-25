(ns vybe.wasm.layout
  (:require
   [vybe.wasm.memory :as mem]
   [vybe.wasm.runtime :as rt]))

(defn helper-layout
  "Build a layout map by calling `prefix_sizeof_<name>` and offsets.

  `fields` is a map of output keys to helper suffixes. Example:
  `{:entity \"entity\"}` calls `<prefix>_offset_<name>_entity`."
  [module prefix name fields]
  (into {:size (rt/call module (str prefix "_sizeof_" name))}
        (map (fn [[k suffix]]
               [k (rt/call module (str prefix "_offset_" name "_" suffix))])
             fields)))

(defn write-field!
  "Write `value` to `base + offset` using wasm field type `t`."
  [module base offset t value]
  (let [p (+ (long base) (long offset))]
    (case t
      :i8 (mem/write-i8! module p value)
      :i16 (mem/write-i16! module p value)
      :i32 (mem/write-i32! module p value)
      :ptr (mem/write-i32! module p value)
      :i64 (mem/write-i64! module p value)
      :f32 (mem/write-f32! module p value)
      :f64 (mem/write-f64! module p value)
      (throw (ex-info "Unsupported Wasm layout field type"
                      {:type t})))))

(defn read-field
  "Read from `base + offset` using wasm field type `t`."
  [module base offset t]
  (let [p (+ (long base) (long offset))]
    (case t
      :i8 (mem/read-i8 module p)
      :i16 (mem/read-i16 module p)
      :i32 (mem/read-i32 module p)
      :ptr (mem/read-i32 module p)
      :i64 (mem/read-i64 module p)
      :f32 (mem/read-f32 module p)
      :f64 (mem/read-f64 module p)
      (throw (ex-info "Unsupported Wasm layout field type"
                      {:type t})))))
