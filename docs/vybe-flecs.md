---
title: Vybe Flecs
description: Clojure bindings for Flecs
---

Vybe Flecs contains Clojure bindings for the innovative ECS Flecs C
library (currently v4.0.4), it's part of the Vybe game framework,
<https://github.com/pfeodrippe/vybe>, buy you can use it standalone if
desired.

## Getting Started

[![Clojars Project](https://img.shields.io/clojars/v/io.github.pfeodrippe/vybe-flecs.svg)](https://clojars.org/io.github.pfeodrippe/vybe-flecs)

From <https://clojars.org/io.github.pfeodrippe/vybe-flecs>, you can
choose among one package of the 3 supported OSs that Vybe builds to, Mac
Universal, Windows x64 or Linux x64 (if your OS is not yet supported,
you can build it yourself, follow the github ci.yml file in this repo
and feel free to open an issue/PR adding it).

E.g. if you have a Mac, you would create a `deps.edn` file like below

``` clojure
;; deps.edn
{:deps {org.clojure/clojure {:mvn/version "1.12.0"}
        io.github.pfeodrippe/vybe-flecs {:mvn/version "0.7.594-macos-universal"}}

 :paths ["src" "resources" "vybe_native"]

 :aliases
 {:flecs
  {:jvm-opts ["--enable-native-access=ALL-UNNAMED"
              "-Djava.library.path=vybe_native"

              ;; For tracing panama calls.
              #_"-Djextract.trace.downcalls=true"]}}}

```

```clojure
(require '[vybe.flecs :as vf])
(require '[vybe.flecs.c :as vf.c])
(import '(org.vybe.flecs flecs))

(def w (vf/make-world))

(merge w {:bob []})
(vf/get-name (:bob w))
;; => "bob"
```

Check Flecs' quick start guide at
<https://www.flecs.dev/flecs/md_docs_2Quickstart.html>.
