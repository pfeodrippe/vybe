# flecs.jank Implementation Plan

**Date**: 2025-12-07
**Goal**: Create a jank implementation of vybe's flecs.clj, providing Flecs ECS bindings that feel idiomatic to jank while leveraging native interop patterns.

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [Source Analysis](#source-analysis)
3. [Architecture Comparison](#architecture-comparison)
4. [Implementation Strategy](#implementation-strategy)
5. [Core Modules](#core-modules)
6. [API Design](#api-design)
7. [Native Interop Patterns](#native-interop-patterns)
8. [Component System](#component-system)
9. [Query & System DSL](#query--system-dsl)
10. [Entity & World Abstractions](#entity--world-abstractions)
11. [Technical Challenges](#technical-challenges)
12. [Implementation Phases](#implementation-phases)
13. [File Structure](#file-structure)
14. [Testing Strategy](#testing-strategy)
15. [Open Questions](#open-questions)

---

## Executive Summary

### What We're Building

A jank port of `vybe/flecs.clj` (3,118 lines) that provides:
- Ergonomic Flecs ECS bindings for jank
- Map-based world abstraction (world as map of entities)
- Set-based entity abstraction (entity as set of components)
- Query DSL with destructuring
- System and observer registration macros
- Full native interop with Flecs C/C++ API

### Key Insight from something project

The `something` project demonstrates that **jank can call Flecs directly via header requires**, significantly reducing wrapper code compared to the Clojure JVM approach. The static object file loading pattern (`jit_prc.load_object()`) is the recommended approach.

### Estimated Complexity

| Aspect | Clojure (flecs.clj) | jank (flecs.jank) |
|--------|---------------------|-------------------|
| FFI Layer | ~500 lines (Panama) | ~100 lines (header require) |
| World Map Type | ~100 lines (def-map-type) | ~150 lines (deftype) |
| Entity Set Type | ~100 lines (deftype+) | ~120 lines (deftype) |
| Query/System DSL | ~800 lines | ~600 lines (simpler) |
| Helper Functions | ~1,600 lines | ~1,200 lines |
| **Total** | ~3,100 lines | ~2,200 lines (estimate) |

---

## Source Analysis

### flecs.clj Structure

```
vybe/flecs.clj (3,118 lines)
├── Namespace & Imports (1-31)
├── Flecs Type Definitions via vp/defcomp (33-53)
├── IVybeName Protocol (57-66)
├── Path/Name Utilities (68-175)
├── Documentation (183-319)
├── Core Implementation (-init, builtin-entities) (321-400)
├── Entity Component Helpers (399-443)
├── VybeFlecsWorldMap (def-map-type) (451-534)
├── VybeFlecsEntitySet (deftype+) (621-720)
├── Entity ID Resolution (eid) (891-1010)
├── Component Operations (-get-c, -set-c, -remove-c) (1050-1300)
├── Query Parsing & Adaptation (1350-1700)
├── System Registration (-system, with-system) (1750-2100)
├── Observer Registration (-observer, with-observer) (2150-2450)
├── Utility Functions (2500-3100)
└── World Setup (-setup-world) (3100-3118)
```

### Key Dependencies in flecs.clj

| Clojure Dependency | jank Equivalent |
|--------------------|-----------------|
| `potemkin/def-map-type` | Custom `deftype` implementing map protocols |
| `potemkin/deftype+` | `deftype` with protocol extensions |
| `vybe.panama` (FFI) | Header requires + `cpp/raw` |
| `MemorySegment` | `opaque_box` |
| `FunctionDescriptor` | C function calls via header |
| Upcall stubs | `cpp/raw` callback wrappers |

---

## Architecture Comparison

### Clojure/JVM Approach (flecs.clj)

```
┌─────────────────────────────────────────────────┐
│ flecs.clj (High-Level API)                      │
│   - VybeFlecsWorldMap (def-map-type)            │
│   - VybeFlecsEntitySet (deftype+)               │
│   - Query/System macros                         │
└─────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│ vybe.flecs.c / vybe.flecs.impl                  │
│   - Generated FFI wrappers                      │
│   - FunctionDescriptor reflection               │
└─────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│ Java Panama FFI                                 │
│   - MemorySegment allocation                    │
│   - Downcall/Upcall handles                     │
└─────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│ Flecs C Library (libflecs)                      │
└─────────────────────────────────────────────────┘
```

### jank Approach (flecs.jank)

```
┌─────────────────────────────────────────────────┐
│ flecs.jank (High-Level API)                     │
│   - FlecsWorld (deftype + map protocols)        │
│   - FlecsEntity (deftype + set protocols)       │
│   - Query/System macros                         │
└─────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│ Header Requires                                 │
│   ["flecs.h" :as fl :scope ""]                  │
│   - Direct C function calls                     │
│   - Enum/constant access                        │
└─────────────────────────────────────────────────┘
                      │
┌─────────────────────────────────────────────────┐
│ cpp/raw Wrappers (minimal)                      │
│   - opaque_box for pointer handling             │
│   - ODR-safe global state                       │
│   - System/observer callbacks                   │
└─────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│ Flecs C Library (static .o or dylib)            │
└─────────────────────────────────────────────────┘
```

---

## Implementation Strategy

### Phase 1: Foundation (Native Interop)

1. **Header require setup**
   ```clojure
   (ns vybe.flecs
     (:require
      ["flecs.h" :as fl :scope ""]      ; C API functions
      ["flecs.h" :as flecs]             ; C++ API (limited use)
      [vybe.util :as u]))
   ```

2. **cpp/raw block for essentials**
   ```cpp
   #include <jank/runtime/obj/opaque_box.hpp>

   // World pointer extraction
   inline ecs_world_t* get_world(jank::runtime::object_ref box);

   // World creation/destruction
   inline jank::runtime::object_ref flecs_create_world();
   inline void flecs_destroy_world(jank::runtime::object_ref world_box);

   // Callback infrastructure for systems/observers
   inline jank::runtime::object_ref create_system_callback(...);
   ```

### Phase 2: Core Types

1. **FlecsWorld** - Map-like interface over ecs_world_t*
2. **FlecsEntity** - Set-like interface over entity id
3. **Component wrappers** - Via jank's native struct support

### Phase 3: High-Level API

1. Port core functions: `eid`, `ent`, `-get-c`, `-set-c`
2. Port query infrastructure
3. Port system/observer macros
4. Port utility functions

---

## Core Modules

### Module Structure

```
src/vybe/
├── flecs.jank           ; Main public API
├── flecs/
│   ├── native.jank      ; cpp/raw wrappers, header requires
│   ├── world.jank       ; FlecsWorld type
│   ├── entity.jank      ; FlecsEntity type
│   ├── component.jank   ; Component definition macros
│   ├── query.jank       ; Query parsing and execution
│   ├── system.jank      ; System registration
│   └── observer.jank    ; Observer registration
└── util.jank            ; Shared utilities (already exists in something)
```

### vybe.flecs.native

The native module encapsulates all cpp/raw code:

```clojure
(ns vybe.flecs.native
  (:require
   ["flecs.h" :as fl :scope ""]
   ["flecs.h" :as flecs]))

(cpp/raw "
#include <jank/runtime/obj/opaque_box.hpp>
#include <flecs.h>

// ============= Pointer Helpers =============

inline void* opaque_box_ptr(jank::runtime::object_ref box) {
    auto o = jank::runtime::expect_object<jank::runtime::obj::opaque_box>(box);
    return o->data.data;
}

inline ecs_world_t* to_ecs_world(jank::runtime::object_ref box) {
    return static_cast<ecs_world_t*>(opaque_box_ptr(box));
}

// ============= World Management =============

inline jank::runtime::object_ref flecs_create_world() {
    ecs_world_t* world = ecs_init();
    return jank::runtime::make_box<jank::runtime::obj::opaque_box>(
        static_cast<void*>(world), \"ecs_world_t\");
}

inline void flecs_destroy_world(jank::runtime::object_ref world_box) {
    ecs_fini(to_ecs_world(world_box));
}

// ============= Entity Operations =============

inline uint64_t flecs_new_entity(jank::runtime::object_ref world_box) {
    return ecs_new(to_ecs_world(world_box));
}

inline void flecs_delete_entity(jank::runtime::object_ref world_box, uint64_t id) {
    ecs_delete(to_ecs_world(world_box), id);
}

// ============= Name/Symbol Operations =============

inline uint64_t flecs_lookup_symbol(jank::runtime::object_ref w, const char* name) {
    return ecs_lookup_symbol(to_ecs_world(w), name, true, false);
}

inline uint64_t flecs_set_name(jank::runtime::object_ref w, uint64_t entity, const char* name) {
    return ecs_set_name(to_ecs_world(w), entity, name);
}

inline const char* flecs_get_name(jank::runtime::object_ref w, uint64_t entity) {
    return ecs_get_name(to_ecs_world(w), entity);
}

inline const char* flecs_get_path(jank::runtime::object_ref w, uint64_t entity) {
    return ecs_get_path(to_ecs_world(w), 0, entity);
}

// ============= Pair Operations =============

inline uint64_t flecs_pair(uint64_t first, uint64_t second) {
    return ecs_pair(first, second);
}

inline bool flecs_id_is_pair(uint64_t id) {
    return ecs_id_is_pair(id);
}

inline uint64_t flecs_pair_first(jank::runtime::object_ref w, uint64_t pair) {
    return ecs_pair_first(to_ecs_world(w), pair);
}

inline uint64_t flecs_pair_second(jank::runtime::object_ref w, uint64_t pair) {
    return ecs_pair_second(to_ecs_world(w), pair);
}

// ============= Component Operations =============

inline void* flecs_get_id(jank::runtime::object_ref w, uint64_t entity, uint64_t id) {
    return const_cast<void*>(ecs_get_id(to_ecs_world(w), entity, id));
}

inline void flecs_set_id(jank::runtime::object_ref w, uint64_t entity, uint64_t id,
                         size_t size, const void* ptr) {
    ecs_set_id(to_ecs_world(w), entity, id, size, ptr);
}

inline void flecs_add_id(jank::runtime::object_ref w, uint64_t entity, uint64_t id) {
    ecs_add_id(to_ecs_world(w), entity, id);
}

inline void flecs_remove_id(jank::runtime::object_ref w, uint64_t entity, uint64_t id) {
    ecs_remove_id(to_ecs_world(w), entity, id);
}

inline bool flecs_has_id(jank::runtime::object_ref w, uint64_t entity, uint64_t id) {
    return ecs_has_id(to_ecs_world(w), entity, id);
}

// ============= Type/Component Registration =============

inline uint64_t flecs_component_init(jank::runtime::object_ref w, uint64_t entity,
                                     size_t size, size_t alignment) {
    ecs_component_desc_t desc = {};
    desc.entity = entity;
    desc.type.size = size;
    desc.type.alignment = alignment;
    return ecs_component_init(to_ecs_world(w), &desc);
}

inline uint64_t flecs_entity_init(jank::runtime::object_ref w, const char* name, const char* symbol) {
    ecs_entity_desc_t desc = {};
    desc.name = name;
    desc.symbol = symbol;
    desc.use_low_id = true;
    return ecs_entity_init(to_ecs_world(w), &desc);
}

// ============= Iteration =============

inline bool flecs_progress(jank::runtime::object_ref w, float delta_time) {
    return ecs_progress(to_ecs_world(w), delta_time);
}

// ============= Validity Checks =============

inline bool flecs_is_valid(jank::runtime::object_ref w, uint64_t entity) {
    return ecs_is_valid(to_ecs_world(w), entity);
}

inline bool flecs_is_alive(jank::runtime::object_ref w, uint64_t entity) {
    return ecs_is_alive(to_ecs_world(w), entity);
}
")

;; ============= jank Wrapper Functions =============

(defn create-world [] (cpp/flecs_create_world))
(defn destroy-world! [w] (cpp/flecs_destroy_world w))
(defn new-entity [w] (cpp/flecs_new_entity w))
(defn delete-entity! [w id] (cpp/flecs_delete_entity w id))
(defn lookup-symbol [w name] (cpp/flecs_lookup_symbol w name))
(defn set-name! [w entity name] (cpp/flecs_set_name w entity name))
(defn get-name [w entity] (cpp/flecs_get_name w entity))
(defn get-path [w entity] (cpp/flecs_get_path w entity))
(defn pair [first second] (cpp/flecs_pair first second))
(defn pair? [id] (cpp/flecs_id_is_pair id))
(defn pair-first [w pair-id] (cpp/flecs_pair_first w pair-id))
(defn pair-second [w pair-id] (cpp/flecs_pair_second w pair-id))
(defn get-id [w entity id] (cpp/flecs_get_id w entity id))
(defn set-id! [w entity id size ptr] (cpp/flecs_set_id w entity id size ptr))
(defn add-id! [w entity id] (cpp/flecs_add_id w entity id))
(defn remove-id! [w entity id] (cpp/flecs_remove_id w entity id))
(defn has-id? [w entity id] (cpp/flecs_has_id w entity id))
(defn component-init! [w entity size align] (cpp/flecs_component_init w entity size align))
(defn entity-init! [w name symbol] (cpp/flecs_entity_init w name symbol))
(defn progress! [w dt] (cpp/flecs_progress w dt))
(defn valid? [w entity] (cpp/flecs_is_valid w entity))
(defn alive? [w entity] (cpp/flecs_is_alive w entity))

;; Builtin entity constants
(def EcsChildOf (fl/EcsChildOf))
(def EcsIsA (fl/EcsIsA))
(def EcsPrefab (fl/EcsPrefab))
(def EcsDisabled (fl/EcsDisabled))
(def EcsExclusive (fl/EcsExclusive))
(def EcsTrait (fl/EcsTrait))
(def EcsSlotOf (fl/EcsSlotOf))
(def EcsWildcard (fl/EcsWildcard))
(def EcsAny (fl/EcsAny))
(def EcsOnUpdate (fl/EcsOnUpdate))
(def EcsOnAdd (fl/EcsOnAdd))
(def EcsOnSet (fl/EcsOnSet))
(def EcsOnRemove (fl/EcsOnRemove))
```

---

## API Design

### Public API (vybe.flecs namespace)

```clojure
;; World operations
(make-world)                    ; Create initialized world
(with-world [w] ...)           ; Scoped world with cleanup
(progress w dt)                 ; Advance pipeline

;; Entity operations
(eid w e)                       ; Resolve entity designator to id
(ent w e)                       ; Wrap entity in FlecsEntity
(entity? v)                     ; Type predicate
(valid? w e)                    ; Check validity
(alive? w e)                    ; Check if alive (not deleted)

;; Component operations (via entity-as-set)
(get entity component)          ; Read component value
(conj entity component-value)   ; Add component
(disj entity component)         ; Remove component

;; Pair operations
(pair? e)                       ; Check if pair
(pair-first e)                  ; Get relationship
(pair-second e)                 ; Get target

;; Hierarchy
(children entity)               ; Get child entities
(parent entity)                 ; Get parent entity

;; Queries
(with-query w [bindings...] body)       ; Iterate matches
(with-query-one w [bindings...] body)   ; First match only

;; Systems
(with-system w [bindings...] body)      ; Register system
(defsystem name w [bindings...] body)   ; Define named system

;; Observers
(with-observer w [bindings...] body)    ; Register observer
(defobserver name w [bindings...] body) ; Define named observer

;; Utilities
(get-name entity)               ; Get entity name
(get-path entity)               ; Get entity path
(hierarchy entity)              ; Get full hierarchy tree
(is-a parent)                   ; Create IsA pair
(child-of parent)               ; Create ChildOf pair
(override component)            ; Create override marker
(del)                           ; Create deletion marker
```

### Map Semantics for World

```clojure
;; World as map of entities
(get w :player)                 ; Lookup entity by name
(assoc w :player [Position])    ; Create/update entity
(dissoc w :player)              ; Delete entity
(merge w {:npc-1 [Health]
          :npc-2 [Health]})     ; Batch create
(keys w)                        ; All entity names

;; Nested children via maps
(merge w {:parent [Component
                   {:child-1 [ChildComponent]
                    :child-2 [ChildComponent]}]})
```

### Set Semantics for Entity

```clojure
;; Entity as set of components
(let [e (get w :player)]
  (get e Position)              ; Read component
  (conj e (Health {:hp 100}))   ; Add component
  (disj e :dead)                ; Remove tag
  (seq e))                      ; Iterate components
```

---

## Native Interop Patterns

### Pattern 1: opaque_box for World Pointers

```clojure
;; Create world (returns opaque_box)
(def w (cpp/flecs_create_world))

;; Pass to C functions via extraction
(cpp/flecs_new_entity w)  ; C++ extracts void* internally
```

### Pattern 2: Direct Header Require for Constants

```clojure
;; Access Flecs constants directly (no wrapper needed!)
fl/EcsChildOf
fl/EcsIsA
fl/EcsWildcard
fl/EcsOnUpdate
```

### Pattern 3: ODR-Safe Global State

```cpp
// For caches, registries
static std::unordered_map<std::string, uint64_t>* g_name_cache = nullptr;

inline auto& get_name_cache() {
    if (!g_name_cache) g_name_cache = new std::unordered_map<std::string, uint64_t>();
    return *g_name_cache;
}
```

### Pattern 4: System/Observer Callbacks

The most complex part - bridging jank functions to Flecs callbacks:

```cpp
#include <jank/runtime/core.hpp>
#include <jank/runtime/obj/persistent_vector.hpp>

// Global callback storage (ODR-safe)
static std::vector<jank::runtime::object_ref>* g_callbacks = nullptr;

inline auto& get_callbacks() {
    if (!g_callbacks) g_callbacks = new std::vector<jank::runtime::object_ref>();
    return *g_callbacks;
}

// System callback trampoline
inline void system_callback_trampoline(ecs_iter_t* it) {
    // Extract callback index from ctx
    size_t idx = reinterpret_cast<size_t>(it->ctx);
    auto& callbacks = get_callbacks();

    // Call jank function with iterator info
    auto fn = callbacks[idx];

    // Build args: world-box, entity-ids, component-ptrs...
    // This requires careful marshalling
    jank::runtime::apply(fn, /* args */);
}

inline uint64_t flecs_register_system(jank::runtime::object_ref w,
                                       const char* name,
                                       const char* query_expr,
                                       jank::runtime::object_ref callback,
                                       uint64_t phase) {
    auto& callbacks = get_callbacks();
    size_t idx = callbacks.size();
    callbacks.push_back(callback);

    ecs_system_desc_t desc = {};
    desc.entity = ecs_entity(to_ecs_world(w), {
        .name = name,
        .add = { ecs_dependson(phase), phase }
    });
    desc.query.expr = query_expr;
    desc.callback = system_callback_trampoline;
    desc.ctx = reinterpret_cast<void*>(idx);

    return ecs_system_init(to_ecs_world(w), &desc);
}
```

---

## Component System

### Component Definition

In jank, we need a way to define components with known layouts:

```clojure
(ns vybe.flecs.component
  (:require [vybe.flecs.native :as n]))

;; Macro to define a component type
(defmacro defcomponent [name fields]
  ;; Generate:
  ;; 1. Struct layout info (size, alignment)
  ;; 2. Constructor function
  ;; 3. Field accessors
  ;; 4. Registration function
  `(do
     (cpp/raw ~(generate-struct-code name fields))

     (def ~name
       {:name ~(str name)
        :size (cpp/sizeof ~(str name))
        :alignment (cpp/alignof ~(str name))
        :constructor (fn [m#] ...)
        :fields ~fields})))

;; Usage
(defcomponent Position
  [[:x :float]
   [:y :float]
   [:z :float]])

(defcomponent Velocity
  [[:x :float]
   [:y :float]
   [:z :float]])
```

### Component Registration

```clojure
(defn register-component!
  "Register a component type with the world."
  [w component]
  (let [{:keys [name size alignment]} component
        entity (n/entity-init! w name name)]
    (n/component-init! w entity size alignment)
    entity))
```

### Component Access

```clojure
(defn -get-component
  "Get component data from entity."
  [w entity-id component]
  (let [comp-id (get-component-id w component)
        ptr (n/get-id w entity-id comp-id)]
    (when ptr
      (read-component ptr component))))

(defn -set-component!
  "Set component data on entity."
  [w entity-id component value]
  (let [comp-id (get-component-id w component)
        {:keys [size]} component
        ptr (allocate-and-write value component)]
    (n/set-id! w entity-id comp-id size ptr)))
```

---

## Query & System DSL

### Query Parsing

The query DSL must translate jank bindings to Flecs query strings:

```clojure
;; jank query syntax
[pos Position
 vel Velocity
 e :vf/entity]

;; Translates to Flecs query
"Position, Velocity"

;; With modifiers
[{:keys [x y]} [:mut Position]
 vel [:in Velocity]]

;; Translates to
"[inout] Position, [in] Velocity"
```

### Query Implementation

```clojure
(defn parse-query-binding
  "Parse a single binding from query DSL."
  [binding]
  (cond
    ;; Keyword special case
    (= binding :vf/entity) {:type :entity}
    (= binding :vf/eid) {:type :eid}
    (= binding :vf/iter) {:type :iter}

    ;; Vector with modifiers
    (vector? binding)
    (let [[modifier component] (if (keyword? (first binding))
                                 [(first binding) (second binding)]
                                 [nil (first binding)])]
      {:type :component
       :modifier modifier
       :component component})

    ;; Plain component
    :else
    {:type :component
     :component binding}))

(defn bindings->query-string
  "Convert query bindings to Flecs query expression string."
  [bindings]
  (->> bindings
       (partition 2)
       (keep (fn [[sym spec]]
               (let [parsed (parse-query-binding spec)]
                 (when (= (:type parsed) :component)
                   (let [modifier (case (:modifier parsed)
                                    :mut "[inout] "
                                    :in "[in] "
                                    :out "[out] "
                                    nil "")]
                     (str modifier (component-name (:component parsed))))))))
       (str/join ", ")))
```

### with-query Macro

```clojure
(defmacro with-query
  "Iterate over entities matching query, returning results."
  [w bindings & body]
  (let [parsed (parse-bindings bindings)
        query-str (bindings->query-string bindings)]
    `(let [query# (create-query ~w ~query-str)
           iter# (query-iter query#)
           results# (transient [])]
       (while (query-next iter#)
         (dotimes [i# (iter-count iter#)]
           (let [~@(generate-let-bindings parsed 'iter# 'i#)]
             (conj! results# (do ~@body)))))
       (query-fini query#)
       (persistent! results#))))
```

### with-system Macro

```clojure
(defmacro with-system
  "Register a system with the world."
  [w bindings & body]
  (let [config (extract-config bindings)
        query-bindings (remove-config bindings)
        query-str (bindings->query-string query-bindings)]
    `(let [callback# (fn [iter#]
                       (dotimes [i# (iter-count iter#)]
                         (let [~@(generate-let-bindings query-bindings 'iter# 'i#)]
                           ~@body)))]
       (register-system! ~w
                         ~(:vf/name config)
                         ~query-str
                         callback#
                         ~(or (:vf/phase config) 'EcsOnUpdate)))))
```

---

## Entity & World Abstractions

### FlecsWorld Type

```clojure
(deftype FlecsWorld [world-ptr metadata]
  ;; Map-like interface
  clojure.lang.ILookup
  (valAt [this k]
    (valAt this k nil))
  (valAt [this k not-found]
    (if (nil? k)
      not-found
      (let [eid (lookup-entity this k)]
        (if (and eid (alive? world-ptr eid))
          (->FlecsEntity this eid)
          not-found))))

  clojure.lang.Associative
  (assoc [this k v]
    (set-entity! this k v)
    this)

  clojure.lang.IPersistentMap
  (without [this k]
    (when-let [eid (lookup-entity this k)]
      (delete-entity! world-ptr eid))
    this)

  clojure.lang.Seqable
  (seq [this]
    (map (fn [eid] [(entity-name this eid) (->FlecsEntity this eid)])
         (world-entities this)))

  ;; Custom protocol for native access
  IFlecsWorld
  (world-ptr [_] world-ptr)
  (world-meta [_] metadata))

(defn make-world
  "Create a new FlecsWorld."
  ([]
   (make-world {}))
  ([meta]
   (let [ptr (n/create-world)]
     (setup-world! (->FlecsWorld ptr meta)))))
```

### FlecsEntity Type

```clojure
(deftype FlecsEntity [world entity-id]
  ;; Set-like interface (components as set members)
  clojure.lang.IPersistentSet
  (seq [this]
    (entity-components world entity-id))

  (cons [this component]
    (add-component! world entity-id component)
    this)

  (disjoin [this component]
    (remove-component! world entity-id component)
    this)

  ;; Map-like access for component values
  clojure.lang.ILookup
  (valAt [this k]
    (get-component world entity-id k))
  (valAt [this k not-found]
    (or (get-component world entity-id k) not-found))

  clojure.lang.Associative
  (assoc [this k v]
    (set-component! world entity-id k v)
    this)

  ;; Function invocation for component access
  clojure.lang.IFn
  (invoke [this component]
    (get-component world entity-id component))

  ;; Custom protocol
  IFlecsEntity
  (entity-id [_] entity-id)
  (entity-world [_] world))
```

---

## Technical Challenges

### Challenge 1: Callback Trampolines

**Problem**: Flecs systems/observers need C function pointers, but jank functions are runtime objects.

**Solution**: Use indexed callback storage with C++ trampolines:

```cpp
// Store jank functions by index
std::vector<jank::runtime::object_ref> g_callbacks;

// C callback that looks up and invokes jank fn
void trampoline(ecs_iter_t* it) {
    size_t idx = (size_t)it->ctx;
    auto fn = g_callbacks[idx];
    // Marshal iterator data to jank and invoke
}
```

### Challenge 2: Component Layout

**Problem**: Need to know struct sizes/alignments for Flecs component registration.

**Solution**: Generate C++ struct definitions and use `sizeof`/`alignof`:

```cpp
struct Position { float x, y, z; };

inline size_t position_size() { return sizeof(Position); }
inline size_t position_align() { return alignof(Position); }
```

### Challenge 3: Query Result Iteration

**Problem**: Flecs iterators provide raw pointers to component arrays.

**Solution**: Use `cpp/.at` for indexed access or wrap in helper:

```clojure
(defn iter-component-at
  "Get component value at index from iterator."
  [iter component-idx entity-idx]
  (let [ptr (iter-field-ptr iter component-idx)
        size (component-size component)]
    ;; Read value at ptr + (entity-idx * size)
    (read-component-at ptr entity-idx component)))
```

### Challenge 4: Name Mangling

**Problem**: Clojure uses `_DOT_`, `_SLASH_`, `_DASH_` for name encoding.

**Solution**: Port the name encoding/decoding functions:

```clojure
(defn vybe-name
  "Encode a keyword/symbol for Flecs symbol lookup."
  [k]
  (-> (name k)
      (str/replace "." "_DOT_")
      (str/replace "/" "_SLASH_")
      (str/replace "-" "_DASH_")))

(defn flecs->vybe
  "Decode a Flecs symbol back to keyword."
  [s]
  (-> s
      (str/replace "_DOT_" ".")
      (str/replace "_SLASH_" "/")
      (str/replace "_DASH_" "-")
      keyword))
```

### Challenge 5: deftype Protocol Implementation

**Problem**: jank's `deftype` may not support all Clojure protocols natively.

**Solution**: Implement core protocols manually or use jank-specific alternatives:

```clojure
;; If clojure.lang.* protocols not available, define our own
(defprotocol IMapLike
  (-get [this k])
  (-assoc [this k v])
  (-dissoc [this k]))

;; Implement as wrappers
(extend-type FlecsWorld
  IMapLike
  (-get [this k] ...)
  (-assoc [this k v] ...)
  (-dissoc [this k] ...))
```

---

## Implementation Phases

### Phase 1: Bootstrap (Week 1-2)

**Goal**: Minimal working system

- [ ] Set up project structure
- [ ] Implement `vybe.flecs.native` with cpp/raw wrappers
- [ ] Basic world create/destroy
- [ ] Basic entity create/delete
- [ ] Component registration (manual, not macro)
- [ ] Simple get/set component

**Deliverable**: Can create world, entities, components manually

### Phase 2: Core API (Week 2-3)

**Goal**: Port core abstractions

- [ ] FlecsWorld deftype with map interface
- [ ] FlecsEntity deftype with set interface
- [ ] `eid` function (full entity resolution)
- [ ] `ent` function (entity wrapping)
- [ ] Name encoding/decoding
- [ ] Pair operations
- [ ] Builtin entity constants

**Deliverable**: Map-style world API working

### Phase 3: Query System (Week 3-4)

**Goal**: Query DSL

- [ ] Query string generation from bindings
- [ ] `with-query` macro
- [ ] `with-query-one` macro
- [ ] Iterator wrapper utilities
- [ ] Component access in queries

**Deliverable**: Can query entities with DSL

### Phase 4: Systems & Observers (Week 4-5)

**Goal**: Reactive patterns

- [ ] System callback trampolines
- [ ] `with-system` macro
- [ ] `defsystem` macro
- [ ] Observer callback trampolines
- [ ] `with-observer` macro
- [ ] `defobserver` macro
- [ ] Event triggering

**Deliverable**: Full ECS reactive system

### Phase 5: Polish & Utilities (Week 5-6)

**Goal**: Feature parity

- [ ] `defcomponent` macro
- [ ] Hierarchy functions (children, parent)
- [ ] Pretty printing
- [ ] Debug utilities
- [ ] Documentation
- [ ] Test suite
- [ ] Performance optimization

**Deliverable**: Production-ready library

---

## File Structure

```
src/vybe/
├── flecs.jank                 ; Public API re-exports
│   (ns vybe.flecs
│     (:require
│      [vybe.flecs.world :as world]
│      [vybe.flecs.entity :as entity]
│      [vybe.flecs.query :as query]
│      [vybe.flecs.system :as system]
│      [vybe.flecs.observer :as observer]
│      [vybe.flecs.component :as component]))
│
├── flecs/
│   ├── native.jank            ; ~400 lines
│   │   - cpp/raw blocks
│   │   - Header requires
│   │   - Low-level wrappers
│   │
│   ├── world.jank             ; ~300 lines
│   │   - FlecsWorld deftype
│   │   - World operations
│   │   - Setup/teardown
│   │
│   ├── entity.jank            ; ~250 lines
│   │   - FlecsEntity deftype
│   │   - Entity operations
│   │   - Name resolution (eid)
│   │
│   ├── component.jank         ; ~200 lines
│   │   - Component definition
│   │   - Get/set operations
│   │   - Type registration
│   │
│   ├── query.jank             ; ~350 lines
│   │   - Query DSL parsing
│   │   - with-query macro
│   │   - Iterator utilities
│   │
│   ├── system.jank            ; ~300 lines
│   │   - System registration
│   │   - Callback trampolines
│   │   - with-system macro
│   │
│   └── observer.jank          ; ~250 lines
│       - Observer registration
│       - Event handling
│       - with-observer macro
│
└── util.jank                  ; Shared utilities
    (Already exists in something project)
```

**Estimated total**: ~2,050 lines (vs 3,118 in Clojure)

---

## Testing Strategy

### Unit Tests

```clojure
(ns vybe.flecs-test
  (:require [vybe.flecs :as vf]
            [clojure.test :refer [deftest is testing]]))

(deftest world-creation-test
  (testing "World lifecycle"
    (let [w (vf/make-world)]
      (is (some? w))
      (is (= 0 (count (keys w))))
      (vf/destroy-world! w))))

(deftest entity-creation-test
  (testing "Entity via assoc"
    (vf/with-world [w]
      (assoc w :player [:alive])
      (is (vf/alive? w :player))
      (is (contains? (get w :player) :alive)))))

(deftest component-test
  (testing "Component get/set"
    (vf/with-world [w]
      (assoc w :player [(Position {:x 10 :y 20})])
      (let [pos (get-in w [:player Position])]
        (is (= 10 (:x pos)))
        (is (= 20 (:y pos)))))))

(deftest query-test
  (testing "Basic query"
    (vf/with-world [w]
      (merge w {:e1 [(Position {:x 1 :y 1})]
                :e2 [(Position {:x 2 :y 2})]})
      (let [results (vf/with-query w [pos Position]
                      (:x pos))]
        (is (= #{1 2} (set results)))))))

(deftest system-test
  (testing "System execution"
    (vf/with-world [w]
      (def *counter (atom 0))
      (vf/with-system w [:vf/name :test-system
                         pos Position]
        (swap! *counter inc))
      (merge w {:e1 [(Position {:x 1 :y 1})]})
      (vf/progress w 0.016)
      (is (pos? @*counter)))))
```

### Integration Tests

```clojure
(deftest integrated-demo-test
  (testing "Full ECS workflow"
    (vf/with-world [w]
      ;; Define components
      (vf/defcomponent Velocity [[:x :float] [:y :float]])

      ;; Create entities with components
      (merge w {:player [(Position {:x 0 :y 0})
                         (Velocity {:x 1 :y 0})]
                :enemy [(Position {:x 10 :y 10})
                        (Velocity {:x -1 :y 0})]})

      ;; Register movement system
      (vf/with-system w [:vf/name :movement
                         pos [:mut Position]
                         vel Velocity]
        (assoc pos :x (+ (:x pos) (:x vel))))

      ;; Run simulation
      (vf/progress w 0.016)

      ;; Verify movement
      (is (> (:x (get-in w [:player Position])) 0))
      (is (< (:x (get-in w [:enemy Position])) 10)))))
```

---

## Open Questions

### Q1: jank Protocol Support

**Question**: Does jank support `clojure.lang.ILookup`, `clojure.lang.Associative`, etc.?

**Fallback**: Define custom protocols if not available.

### Q2: Macro Hygiene

**Question**: How does jank handle gensym and macro hygiene?

**Reference**: Check jank documentation for `~'` and `#` behavior.

### Q3: Transients

**Question**: Does jank support `transient`/`persistent!` for performance?

**Fallback**: Use regular vectors if not available.

### Q4: Multiple Arity

**Question**: Does jank's `defn` support multiple arities like Clojure?

**Reference**: Test with simple cases first.

### Q5: Interned Symbols

**Question**: How does jank handle `def` at runtime (e.g., in macros)?

**Fallback**: May need different approach for system/observer registration.

### Q6: WASM Compatibility

**Question**: Should the implementation consider WASM target?

**Reference**: The `#?(:wasm ...)` reader conditional in something examples.

---

## Appendix A: Clojure → jank Translation Guide

| Clojure | jank |
|---------|------|
| `def-map-type` (potemkin) | `deftype` + protocols |
| `deftype+` (potemkin) | `deftype` |
| `MemorySegment` | `opaque_box` |
| `FunctionDescriptor` | Header require |
| `Linker.upcallStub` | `cpp/raw` function pointer |
| `vp/defcomp` | `defcomponent` macro |
| `vp/jx-i` | Direct struct construction |
| `vp/p->map` | `cpp/.-field` access |
| `(.method obj args)` | `(cpp/.method obj args)` |

## Appendix B: Key Flecs C API Functions

```c
// World
ecs_world_t* ecs_init(void);
int ecs_fini(ecs_world_t*);
bool ecs_progress(ecs_world_t*, float);

// Entity
ecs_entity_t ecs_new(ecs_world_t*);
void ecs_delete(ecs_world_t*, ecs_entity_t);
ecs_entity_t ecs_entity_init(ecs_world_t*, const ecs_entity_desc_t*);

// Component
ecs_entity_t ecs_component_init(ecs_world_t*, const ecs_component_desc_t*);
const void* ecs_get_id(const ecs_world_t*, ecs_entity_t, ecs_id_t);
void ecs_set_id(ecs_world_t*, ecs_entity_t, ecs_id_t, size_t, const void*);
void ecs_add_id(ecs_world_t*, ecs_entity_t, ecs_id_t);
void ecs_remove_id(ecs_world_t*, ecs_entity_t, ecs_id_t);
bool ecs_has_id(const ecs_world_t*, ecs_entity_t, ecs_id_t);

// Query
ecs_query_t* ecs_query_init(ecs_world_t*, const ecs_query_desc_t*);
void ecs_query_fini(ecs_query_t*);
ecs_iter_t ecs_query_iter(const ecs_world_t*, const ecs_query_t*);
bool ecs_query_next(ecs_iter_t*);

// System
ecs_entity_t ecs_system_init(ecs_world_t*, const ecs_system_desc_t*);

// Observer
ecs_entity_t ecs_observer_init(ecs_world_t*, const ecs_observer_desc_t*);

// Naming
ecs_entity_t ecs_lookup_symbol(const ecs_world_t*, const char*, bool, bool);
ecs_entity_t ecs_set_name(ecs_world_t*, ecs_entity_t, const char*);
const char* ecs_get_name(const ecs_world_t*, ecs_entity_t);

// Pairs
#define ecs_pair(first, second) ...
bool ecs_id_is_pair(ecs_id_t);
ecs_entity_t ecs_pair_first(const ecs_world_t*, ecs_id_t);
ecs_entity_t ecs_pair_second(const ecs_world_t*, ecs_id_t);
```

---

## Appendix C: something Project Reference Files

Key files to reference during implementation:

1. **`something/src/my_flecs_static.jank`** - Basic Flecs world/entity creation
2. **`something/src/my_integrated_demo.jank`** - Full integration example
3. **`something/ai/20251202-native-resources-guide.md`** - C++ interop patterns
4. **`something/src/vybe/util.jank`** - Utility functions (`->*` macro, etc.)

---

*This plan is a living document. Update as implementation progresses and new challenges are discovered.*
