(ns vybe.raylib.wasm-abi
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defonce ^:private abi*
  (delay
    (with-open [r (io/reader (io/resource "vybe/wasm/raylib_abi.edn"))]
      (edn/read (java.io.PushbackReader. r)))))

(defn abi
  []
  @abi*)

(defn wasm-imports
  []
  (:wasm-imports (abi)))

(defn import-category
  [{:keys [name]}]
  (cond
    (str/starts-with? name "glfw") :glfw
    (or (str/starts-with? name "gl")
        (str/starts-with? name "emscripten_gl")) :gl
    (str/starts-with? name "emscripten") :emscripten
    (or (str/includes? name "Audio")
        (str/includes? name "Sound")
        (str/includes? name "Music")
        (str/includes? name "Wave")) :audio
    (or (str/starts-with? name "__syscall")
        (str/starts-with? name "fd_")
        (= name "proc_exit")
        (= name "clock_time_get")) :runtime
    :else :other))

(defn imports-by-category
  []
  (->> (wasm-imports)
       (group-by import-category)
       (into (sorted-map))))

(defn import-summary
  []
  (into (sorted-map)
        (map (fn [[category imports]]
               [category {:count (count imports)
                          :names (mapv :name imports)}]))
        (imports-by-category)))

(defn find-import
  [name]
  (some #(when (= name (:name %)) %) (wasm-imports)))
