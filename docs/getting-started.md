---
title: Vybe Starter
---

# Getting Started

``` clojure hl_lines="2 3"
(defn- ->comp
  [wptr v]
  (or (get builtin-entities-rev v)
      (vp/comp-cache (:id (-get-c wptr v VybeComponentId)))
      (eid wptr v)))
```
