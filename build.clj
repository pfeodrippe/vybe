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
  (b/javac {:src-dirs  ["src-java" "grammars"]
            :class-dir "target/classes"
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
                          #_"vybe_native"
                          ;; `.sounds` contains the files that will be used
                          ;; for the build.
                          ;; curl https://keymusician01.s3.amazonaws.com/FluidR3_GM.zip --output FluidR3_GM.zip
                          ;; mkdir -p .sounds
                          ;; unzip FluidR3_GM.zip -d .sounds
                          #_".sounds"]
               :target-dir class-dir
               #_ #_:ignores (conj copy/default-ignores #".*dylib")}))

;; clj -T:build compile-app

(defn jar [_]
  #_(compile-app)
  (b/jar {:class-dir class-dir
          :jar-file (jar-file "vybe")}))

#_(defn uber [_]
    (compile-app)
    (b/compile-clj {:basis basis
                    :src-dirs ["src"]
                    :class-dir class-dir
                    :ns-compile ['pfeodrippe.main]})
    (b/uber {:class-dir class-dir
             :uber-file uber-file
             :basis basis
             :exclude ["LICENSE"]
             :main 'pfeodrippe.main}))

;; clj -T:build uber

;; # LINUX
;; clj -M:dev -m vybe.native.loader && clj -M:dev -m vybe.raylib

;; # MAC (OSX)
;; clj -M:dev -m vybe.native.loader && clj -M:osx -m vybe.raylib

;; mvn -f target/classes/META-INF/maven/io.github.pfeodrippe/vybe/pom.xml deploy

;; DEPLOY to CLOJARS
;; bin/jextract-libs.sh && clj -T:build compile-app && clj -T:build jar && cp target/classes/META-INF/maven/io.github.pfeodrippe/vybe/pom.xml . && mvn deploy ; rm pom.xml
