(ns build
  (:require
   [clojure.tools.build.api :as b]
   #_[clojure.tools.build.tasks.copy :as copy]))

(def lib 'pfeodrippe/healthcare)
(def version (format "1.0.0" #_(b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn compile-app [& _]
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]})

  ;; Java.
  (b/javac {:src-dirs  ["src-java" "grammars"]
            :class-dir "target/classes"
            :basis basis
            :javac-opts ["-parameters"]
            #_ #_:javac-opts ["--enable-preview" "--release" "22" "-Xlint:preview"]})

  ;; Clojure.
  (b/copy-dir {:src-dirs ["src"
                          "resources"
                          ;; `.sounds` contains the files that will be used
                          ;; for the build.
                          ;; curl https://keymusician01.s3.amazonaws.com/FluidR3_GM.zip --output FluidR3_GM.zip
                          ;; mkdir -p .sounds
                          ;; unzip FluidR3_GM.zip -d .sounds
                          #_".sounds"]
               :target-dir class-dir
               #_ #_:ignores (conj copy/default-ignores #".*dylib")}))

;; clj -T:build compile-app

(defn uber [_]
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

;; clj -M:dev -m vybe.raylib
