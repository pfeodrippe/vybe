(ns build
  (:require
   [clojure.java.io :as io]
   [clojure.tools.build.api :as b]
   #_[clojure.tools.build.tasks.copy :as copy]))

(defn lib [n]
  (symbol "io.github.pfeodrippe" n))

(def branch
  (b/git-process {:git-args "branch --show-current"}))

(def version
  (format "0.7.%s%s%s"
          (b/git-count-revs nil)
          (if-let [suffix (System/getenv "VYBE_VERSION_SUFFIX")]
            (str "-" suffix)
            "")
          (if (= branch "develop")
            "-SNAPSHOT"
            "")))

(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(defn jar-file [n]
  (format "target/%s-%s.jar" (name (lib n)) version))
#_(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

(def wasm-resource-ignores
  [#"vybe/native/.*"
   #".*\.(dylib|dll)$"
   #".*\.so(\..*)?$"])

(defn native-resource-artifact? [file]
  (let [file-name (.getName file)]
    (or (= "keep" file-name)
        (= "vybe-sc-prebuilt.zip" file-name)
        (re-find #"\.(dylib|dll)$" file-name)
        (re-find #"\.so(\..*)?$" file-name))))

(defn delete-wasm-native-resources! [target-dir]
  (let [native-dir (io/file target-dir "vybe/native")]
    (when (.exists native-dir)
      (doseq [file (file-seq native-dir)
              :when (and (.isFile file)
                         (native-resource-artifact? file))]
        (io/delete-file file))
      (doseq [dir (reverse (filter #(.isDirectory %) (file-seq native-dir)))]
        (when (empty? (seq (.list dir)))
          (io/delete-file dir))))))

(defn copy-resources [target-dir]
  (b/copy-dir {:src-dirs ["resources"]
               :target-dir target-dir
               :ignores wasm-resource-ignores})
  (delete-wasm-native-resources! target-dir))

(defn clean [_]
  (b/delete {:path "target"}))

(defn compile-app [& _]
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib (lib "vybe")
                :version version
                :basis basis
                :src-dirs ["src"]
                :pom-data [[:distributionManagement
                            [:repository
                             [:id "clojars"]
                             [:name "Clojars Repository"]
                             [:url "https://clojars.org/repo"]]]
                           [:licenses
                            [:license
                             [:name "MIT License"]
                             [:url "https://opensource.org/license/mit"]]]]})

  #_(b/unzip {:target-dir "test44"
              :zip-file "a.zip"})

  ;; Clojure.
  (b/copy-dir {:src-dirs ["src"
                          #_"vybe_native"]
               :target-dir class-dir})
  (copy-resources class-dir))

;; clj -T:build compile-app

(defn jar [_]
  #_(compile-app)

  (copy-resources class-dir)

  (b/jar {:class-dir class-dir
          :jar-file (jar-file "vybe")}))

;; Standalone vybe-flecs dep, it assumes that the commands above were
;; already run.
(defn build-flecs [& _]
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib (lib "vybe-flecs")
                :version version
                :basis basis
                :src-dirs ["src"]
                :pom-data [[:distributionManagement
                            [:repository
                             [:id "clojars"]
                             [:name "Clojars Repository"]
                             [:url "https://clojars.org/repo"]]]
                           [:licenses
                            [:license
                             [:name "MIT License"]
                             [:url "https://opensource.org/license/mit"]]]]})

  ;; Clojure.
  (doseq [to-include ["**vybe/flecs**"
                      "vybe/c.clj"
                      "vybe/panama.clj"
                      "vybe/wasm.clj"
                      "**vybe/wasm/**"
                      "vybe/util.clj"]]
    (b/copy-dir {:src-dirs ["src"]
                 :target-dir class-dir
                 :include to-include}))

  (doseq [to-include ["**vybe/wasm/flecs.wasm"
                      "**vybe/wasm/flecs_abi.edn"]]
    (b/copy-dir {:src-dirs ["resources"]
                 :target-dir class-dir
                 :include to-include}))

  (b/jar {:class-dir class-dir
          :jar-file (jar-file "vybe-flecs")}))

;; clj -T:build uber

;; mvn -f target/classes/META-INF/maven/io.github.pfeodrippe/vybe/pom.xml deploy

;; DEPLOY to CLOJARS
;; bin/jextract-libs.sh && clj -T:build compile-app && clj -T:build jar && cp target/classes/META-INF/maven/io.github.pfeodrippe/vybe/pom.xml . && mvn deploy ; rm pom.xml
