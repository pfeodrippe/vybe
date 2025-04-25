---
title: Vybe Flecs
description: Clojure bindings for Flecs
---

Vybe Flecs contains Clojure bindings for the innovative ECS Flecs C
library (currently v4.0.4), <https://www.flecs.dev/flecs/>.

Vybe Flecs is part of the Vybe game framework,
<https://github.com/pfeodrippe/vybe>, but you can use it standalone if
desired as shown below.

## Getting Started

[![Clojars Project](https://img.shields.io/clojars/v/io.github.pfeodrippe/vybe-flecs.svg)](https://clojars.org/io.github.pfeodrippe/vybe-flecs)

You should have Clojure (clj CLI) and at least Java 22 installed.

From <https://clojars.org/io.github.pfeodrippe/vybe-flecs>, you can
choose among one package of the 3 supported OSs that Vybe builds to, Mac
Universal, Windows x64 or Linux x64 (if your OS is not yet supported,
you can build it yourself, follow the github ci.yml file in this repo
and feel free to open an issue/PR adding it).

E.g. if you have a Mac, you would create a new folder and add
`deps.edn` like below

``` clojure
;; deps.edn
{:deps {org.clojure/clojure {:mvn/version "1.12.0"}
        io.github.pfeodrippe/vybe-flecs {:mvn/version "0.7.598-macos-universal"}}

 :paths ["src" "resources" "vybe_native"]

 :aliases
 {:flecs
  {:jvm-opts ["--enable-native-access=ALL-UNNAMED"
              "-Djava.library.path=vybe_native"

              ;; For tracing panama calls.
              #_"-Djextract.trace.downcalls=true"]}}}

```

The Flecs shared library is contained in the dep and, only once, you
can setup it by running

``` shell
clj -M:flecs -m vybe.native.loader
```

Then, you would create the following `src/sample.clj` file and you can
start the REPL on your favorite IDE, try it. You can also check the
Flecs tests at <https://github.com/pfeodrippe/vybe/blob/main/test/vybe/flecs_test.clj>.

```clojure
(ns sample
  (:require
   [vybe.flecs :as vf]
   [vybe.panama :as vp]))

;; Start a Flecs world.
(def w (vf/make-world))

;; Create an empty entity `:bob`.
(merge w {:bob []})
;; Get its name.
(vf/get-name (:bob w))
;; => "bob"

;; Define a componnent (struct in C).
(vp/defcomp Position
  [[:x :float]
   [:y :float]
   [:z :float]])

;; Create an observer.
(vf/with-observer w [:vf/name :ex-1-observer
                     :vf/events #{:set}
                     {:keys [x] :as pos} [:mut Position]
                     e :vf/entity
                     _event :vf/event
                     _it :vf/iter]
  (when (= (vf/get-rep e) :alice)
    (merge w {e [:from-observer]}))
  (when (= x 10.0)
    (update pos :x inc)))

;; Let's setup some entities.
(merge w {:bob [:walking (Position {:x 10 :y 20}) nil]
          :alice [(Position {:x 10 :y 21})]})

;; Get the position component of an entity.
(get-in w [:bob Position])
;; Or a value from the component (hash map magic!! *backed by pointers).
(get-in w [:bob Position :y])
;; Or whatever you need from it.
(-> (get-in w [:bob Position])
    (select-keys [:y]))

;; Override bob's position.
(assoc w :bob (Position {:x 20 :y 30}))

;; Add a tag to Alice.
(assoc w :alice :walking)

;; Check all the components of an
;; entity in string format.
(vf/type-str (:alice w))

;; Remove a tag from alice and increment bob's x Position.
(-> w
    (update :alice disj :walking)
    ;; Update x field in Position (maps everywhere!).
    (update-in [:bob Position :x] inc))

;; Iterate over all the entities with Position using `with-query`, also
;; retrieving the positions.
(vf/with-query w [pos Position, e :vf/entity]
  [e pos])
;; =>
[[#{:alice
    #:sample{Position {:x 11.0, :y 21.0, :z 0.0}}
    :from-observer}
  #:sample{Position {:x 11.0, :y 21.0, :z 0.0}}]
 [#{:bob :walking #:sample{Position {:x 21.0, :y 30.0, :z 0.0}}}
  #:sample{Position {:x 21.0, :y 30.0, :z 0.0}}]]

;; `with-system` has basically the same interface as
;; `with-query`. The differences are that `with-system` requires a
;; :vf/name (you put it in the bindings, see below) and it won't
;; run the code in place, but will build a Flecs system that can be run
;; with `system-run`.
(def *acc (atom []))
;; Note that we need to accumulate values here explictly as `with-system`
;; doesn't run the system immediately.
(def my-system
  (vf/with-system w [:vf/name :my-system, pos Position, e :vf/entity]
    (swap! *acc conj [e pos])))

(vf/system-run w :my-system)
;; system has run, you can see values in `*acc`
@*acc
;; =>
[[#{:alice
    #:sample{Position {:x 11.0, :y 21.0, :z 0.0}}
    :from-observer}
  #:sample{Position {:x 11.0, :y 21.0, :z 0.0}}]
 [#{:bob :walking #:sample{Position {:x 21.0, :y 30.0, :z 0.0}}}
  #:sample{Position {:x 21.0, :y 30.0, :z 0.0}}]]

;; Run system again, now using `progress`.
(vf/progress w)
@*acc
;; =>
[[#{:alice
    #:sample{Position {:x 11.0, :y 21.0, :z 0.0}}
    :from-observer}
  #:sample{Position {:x 11.0, :y 21.0, :z 0.0}}]
 [#{:bob :walking #:sample{Position {:x 21.0, :y 30.0, :z 0.0}}}
  #:sample{Position {:x 21.0, :y 30.0, :z 0.0}}]]
```

### Other resources

Check Flecs' quick start guide at
<https://www.flecs.dev/flecs/md_docs_2Quickstart.html>.

## Tags mapping and query-specific keywords

``` clojure
{:flecs/tags
 {:vf/child-of
  {:doc "Maps to EcsChildOf"}

  :vf/is-a
  {:doc "Maps to EcsIsA"}

  :vf/prefab
  {:doc "Maps to EcsPrefab"}

  :vf/union
  {:doc "Maps to EcsUnion"}

  :vf/trait
  {:doc "Maps to EcsTrait"}

  :vf/exclusive
  {:doc "Maps to EcsExclusive"}

  :vf/disabled
  {:doc "Maps to EcsDisabled"}

  :*
  {:doc "Maps to EcsWildcard, the `*` symbol that allows you to query for all of the elements in a pair, for example"}

  :_
  {:doc "Maps to EcsAny, the `_` symbol that allows you to query for only one element in a pair (in contrast with `*`), for example"}

  :vf/unique
  {:doc "Component trait that lets will force the usage of a component to only one entity"}

  :vf/print-disabled
  {:doc "Disables print of this component when printing an entity"}}

 :flecs/query-config
 {:vf/name
  {:doc "Required in systems or observers, it may be a keyword or string (including the result of `vf/path`)"
   :examples '[[:vf/name :my-system]]}

  :vf/disabled
  {:doc "Disables a system or observer if set to true"
   :examples '[[:vf/name :my-observer
                :vf/disabled true]]}

  :vf/always
  {:doc "Only for systems, it will make the system run every time, independently if there was a change or not"
   :examples '[[:vf/name :my-system
                :vf/always true]]}

  :vf/phase
  {:doc "Only for systems, the phase of the system, it defaults to EcsOnUpdate"
   :examples '[[:vf/name :my-system
                :vf/always true]]}

  :vf/immediate
  {:doc "Only for systems, if true, it ensures that system will not be in readonly mode"
   :examples '[[:vf/name :my-system
                :vf/immediate true]]}

  :vf/events
  {:doc "Only for observers, you can pass a list of built-in (`:add`, `:set`, `:remove`) or custom events"
   :examples '[[:vf/name :my-observer
                :vf/events #{:set}]]}

  :vf/yield-existing
  {:doc "Only for observers, will cause entities that match the query to be triggered (take care with this one in the REPL as re-creation will trigger the observer!)"
   :examples '[[:vf/name :my-observer
                :vf/yield-existing true]]}}

 :flecs/query-special
 {:vf/entity
  {:doc "Fetches the entity (`VybeFlecsEntitySet`) associated with the match"
   :examples '[[e :vf/entity]
               [e [:vf/entity :c]]]}

  :vf/eid
  {:doc "Fetches the entity id (a long) associated with the match"
   :examples '[[e :vf/eid]
               [e [:vf/eid :c]]]}

  :vf/iter
  {:doc "Fetches the iter (`iter_t`) associated with the match"
   :examples '[[it :vf/iter]]}

  :vf/event
  {:doc "Only for observers, fetches the event associated with the match"
   :examples '[[ev :vf/event]]}}

 :flecs/query-terms
 {:or
  {:doc "Maps to EcsOr"
   :examples '[[c [:or :c1 :c2]]]}

  :not
  {:doc "Maps to EcsNot"
   :examples '[[c [:not :c]]]}

  :maybe
  {:doc "Maps to EcsOptional"
   :examples '[[c [:maybe :c]]]}

  :meta
  {:doc "Use this so you can set flags directly in Flecs, it's low-level"
   :examples '[[c [:meta {:term {:src {:id 521}}} :c]]]}

  :in
  {:doc "Maps to EcsIn, by default, all components are input, so you don't necessarily need to use this"
   :examples '[[c [:in my-component]]]}

  :out
  {:doc "Maps to EcsOut"
   :examples '[[c [:out my-component]]]}

  :inout
  {:doc "Maps to EcsInOut"
   :examples '[[c [:inout my-component]]]}

  :inout-filter
  {:doc "Maps to EcsInOutFilter"
   :examples '[[c [:inout-filter my-component]]]}

  :filter
  {:doc "Maps to EcsInOutFilter, same as `:inout-filter`"
   :examples '[[c [:filter my-component]]]}

  :none
  {:doc "Maps to EcsNone"
   :examples '[[c [:none my-component]]]}

  :mut
  {:doc "Maps to EcsInOut, same as `:inout`"
   :examples '[[c [:mut my-component]]]}

  :src
  {:doc "Receives a fixed or variable source, you can match anything with it"
   :examples '[[c [:src :my-entity my-component]]
               [c1 [:src '?e my-component-1]
                c2 [:src '?e my-component-2]]]}}}
```
