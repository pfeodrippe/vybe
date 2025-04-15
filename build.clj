(ns build
  (:require
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

  ;; Java.
  (b/javac {:src-dirs  ["src-java"]
            :class-dir class-dir
            :basis basis
            :javac-opts ["-parameters"]
            #_ #_:javac-opts ["--enable-preview" "--release" "22" "-Xlint:preview"]})

  ;; Prebuilt native libs for SC from Sonic PI.
  (b/zip {:src-dirs ["sonic-pi/prebuilt"]
          :zip-file "resources/vybe/native/vybe-sc-prebuilt.zip"})

  #_(b/unzip {:target-dir "test44"
              :zip-file "a.zip"})

  ;; Clojure.
  (b/copy-dir {:src-dirs ["src"
                          "resources"
                          #_"vybe_native"]
               :target-dir class-dir
               #_ #_:ignores (conj copy/default-ignores #".*dylib")}))

;; clj -T:build compile-app

(defn jar [_]
  #_(compile-app)

  (b/copy-dir {:src-dirs ["resources"]
               :target-dir class-dir})

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

  ;; Java.
  (b/javac {:src-dirs  ["src-java"]
            :class-dir ".vybe/target/classes"
            :basis basis
            :javac-opts ["-parameters"]})

  (b/copy-dir {:src-dirs [".vybe/target/classes"]
               :target-dir class-dir
               :include "**org/vybe/flecs/**"})

  ;; Clojure.
  (doseq [to-include ["**vybe/flecs**"
                      "vybe/c.clj"
                      "vybe/panama.clj"
                      "vybe/util.clj"]]
    (b/copy-dir {:src-dirs ["src"]
                 :target-dir class-dir
                 :include to-include}))

  (b/copy-dir {:src-dirs ["resources"]
               :target-dir class-dir
               :include "**vybe_flecs**"})

  (b/jar {:class-dir class-dir
          :jar-file (jar-file "vybe-flecs")}))

;; clj -T:build uber

;; # LINUX
;; clj -M:dev -m vybe.native.loader && clj -M:dev -m vybe.raylib

;; # MAC (OSX)
;; clj -M:dev -m vybe.native.loader && clj -M:osx -m vybe.raylib

;; mvn -f target/classes/META-INF/maven/io.github.pfeodrippe/vybe/pom.xml deploy

;; DEPLOY to CLOJARS
;; bin/jextract-libs.sh && clj -T:build compile-app && clj -T:build jar && cp target/classes/META-INF/maven/io.github.pfeodrippe/vybe/pom.xml . && mvn deploy ; rm pom.xml
