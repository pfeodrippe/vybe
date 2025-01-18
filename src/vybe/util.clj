(ns vybe.util
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]))

(defn getenv
  [s]
  (or (System/getenv s) (System/getProperty s)))

(defonce prd?
  (= (getenv "VYBE_PROD") "true"))

(defonce *state (atom {:debug (= (getenv "VYBE_DEBUG") "true")}))
#_(defonce *state (atom {:debug true}))
#_ (swap! *state assoc :debug true)
#_ (swap! *state assoc :debug false)

(defn debug-set!
  [v]
  (swap! *state assoc :debug v))

(defmacro debug
  "Print vybe debug message.

  It can be enabled by calling `debug-set!` only when the env var or jvm prop
  VYBE_PROD is not set to \"true\", otherwise it just returns `nil`."
  [& strs]
  (when-not prd?
    `(when (:debug @*state)
       (println (str "[Vybe] - " (str/join " " [~@strs]))))))

(defmacro if-prd
  "Runs `prd-body`, it can be enabled by calling `debug-set!`."
  [prd-body else-body]
  (if prd?
    `(do ~prd-body)
    `(if (:debug @*state)
       (do ~else-body)
       (do ~prd-body))))

(defonce *commands (atom []))

(defn enqueue-command!
  "Receives a zero-arity function that will be run before the next draw
  call."
  [f]
  (swap! *commands conj f))

(defonce *probe (atom {}))

(defn counter!
  "Used for debugging."
  [k]
  (swap! *probe update-in [::counter k] (fnil inc 0)))
#_ (counter! :a)

(declare app-resource)

(defn extract-resource
  "Extract a resource  into `vybe_native` (default target folder) and return the extracted file
  path (string) if the path is available only in the jar, otherwise returns
  the exisitng file path."
  ([resource-path]
   (extract-resource resource-path {}))
  ([resource-path {:keys [target-folder]
                   :or {target-folder (app-resource "vybe_native")}}]
   (let [res (some-> resource-path io/resource)]
     (cond
       (not res)
       (throw (ex-info "Resource does not exist" {:resource resource-path}))

       (str/includes? (.getPath res) "jar!")
       (let [tmp-file (io/file target-folder resource-path)]
         (debug "Extracting resource" {:tmp-file (.getCanonicalPath tmp-file)})
         (io/make-parents tmp-file)
         (with-open [in (io/input-stream res)]
           (io/copy in tmp-file))
         (.getCanonicalPath tmp-file))

       :else
       ;; We use URI to avoid URL encoding.
       (.getPath (java.net.URI. (str res)))))))

(defn app-resource
  "Check for the existence of the `VYBE_APPDIR` property and return the resource
  accordingly. This is useful when the app is jpackaged.

  Returns the path in string format."
  ([path]
   (app-resource path {}))
  ([path {:keys [throw-exception target-folder]
          :or {throw-exception true}
          :as opts}]
   (let [file (io/file (str (or (System/getProperty "VYBE_APPDIR")
                                (System/getProperty "user.dir"))
                            "/"
                            (if target-folder
                              (str target-folder "/")
                              "")
                            path))]
     (if (.exists file)
       (.getCanonicalPath file)
       ;; If the file doesn't exist, maybe the path is a resource and we will
       ;; try to extract it.
       (cond
         (io/resource path)
         (extract-resource path (merge {:target-folder (app-resource ".")}
                                       opts))

         (not throw-exception)
         (.getCanonicalPath file)

         :else
         (throw (ex-info "App resource not found" {:path path})))))))
