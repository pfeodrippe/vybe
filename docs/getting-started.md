---
title: Vybe Starter
---

# Getting Started

This is the minimal amount of code to have physics (with Jolt) + ECS (with Flecs) +
rendering (with Raylib) using Vybe. We will talk about each of the parts later,
check the code in full below.

``` clojure
--8<-- "test/minimal.clj"
```

``` clojure hl_lines="2 3"
(defn- ->comp
  [wptr v]
  (or (get builtin-entities-rev v)
      (vp/comp-cache (:id (-get-c wptr v VybeComponentId)))
      (eid wptr v)))
```
