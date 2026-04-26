# Wasm Native Implementation Plan

Date: 2026-04-25

This plan tracks the migration from shipped native dynamic libraries to JVM-hosted
WebAssembly modules while preserving the existing public Clojure API. The target
shape is not a parallel smoke-test API and not a dylib fallback. Public callers
should keep using namespaces like `vybe.flecs`, `vybe.flecs.c`, and the existing
`defcomp`/`with-query`/`with-system`/`with-observer` forms; the implementation
under those namespaces switches from Panama/jextract/native symbols to Wasm
exports and Wasm linear memory.

## Hard Requirements

1. Flecs must be Wasm-only. `libvybe_flecs.dylib` and `org.vybe.flecs.*`
   generated Java bindings are removed from the Flecs runtime path.
2. Do not create separate smoke-test namespaces. Use `test/vybe/flecs_test.clj`
   and equivalent real examples that exercise the same public API.
3. Do not hand-write ABI tables for Flecs functions, constants, sizes, offsets,
   function signatures, or layouts. Generate ABI metadata from the C headers
   and Wasm module.
4. Keep `flecs.clj` behavior stable. Only change it where the native seam must
   switch from Panama/jextract to Wasm.
5. Add `vybe.wasm` as the Wasm equivalent of `vybe.panama` for reusable native
   abstractions: module loading, calls, memory, allocation, pointer maps,
   callbacks, `defcomp`, and generated ABI layouts.
6. Avoid unsupported placeholders. If a module needs host functions to link,
   provide real host functions or explicit stubs with documented behavior and a
   follow-up implementation path.
7. Make ABI generation generic so the same script can be reused for Raylib,
   Jolt, Netcode, or other C/C-compatible libraries, with library-specific
   config and shims only where needed.

## Current Implementation Status

### Generic Wasm Layer

Implemented namespaces:

1. `src/vybe/wasm.clj`
   - Public Wasm-native abstraction layer.
   - Provides `defcomp` with the same shape as `vybe.panama/defcomp`.
   - Supports ordinary schema components and generated Wasm ABI layouts.
   - Provides Wasm pointer maps over linear-memory offsets.
   - Supports generated nested structs and fixed-size arrays in map views.
   - Provides callback registration used by Flecs trampolines.

2. `src/vybe/wasm/runtime.clj`
   - Chicory module loading and export invocation.
   - Export/global lookup and cached function calls.
   - WASI/host import wiring.
   - Uses Chicory's built-in `ByteArrayMemory`, so Flecs tests no longer
     require a precompiled custom Java memory class in `target/classes`.

### Raylib/Raymath Load-Time Split

`vybe.audio` no longer loads Raylib for ambisonic distance/orientation math.
The required matrix/vector operations are implemented locally in pure Clojure
over the existing `vybe.type` components:

1. Translation extraction from `vt/Transform`.
2. `Vector3` distance.
3. 4x4 matrix multiplication.
4. 4x4 matrix inverse.

`vybe.raylib.c` is now backend-aware. In Panama mode it keeps the existing
jextract-generated Raylib binding intern path. In Wasm mode it exposes real
CPU-only raymath functions that do not require the Raylib dynamic library. The
implemented functions needed by the current tests are:

1. `vector-2-add`
2. `vector-2-subtract`
3. `matrix-identity`
4. `matrix-scale`
5. `matrix-translate`
6. `matrix-multiply`
7. `matrix-transpose`
8. `quaternion-to-matrix`
9. `quaternion-from-matrix`
10. `vector-3-length`
11. `vector-2-zero`

These are pure raymath operations and are not fallback calls into a dylib.
Windowing, rendering, input, and audio-device Raylib functions still need a
separate host strategy before they can be Wasm-only on the desktop JVM.

`vybe.raylib` and `vybe.type` are also backend-aware at namespace load. In Wasm
mode they avoid jextract layout classes because those classes initialize the
Raylib dynamic library. The current Wasm-side struct definitions cover the real
`vybe.game-test` GLTF/Flecs path. The long-term replacement is generated Raylib
ABI metadata from the generic Clang generator, not permanent handwritten Raylib
layouts.

3. `src/vybe/wasm/memory.clj`
   - Primitive reads/writes for Wasm linear memory.
   - Byte and string helpers.

4. `src/vybe/wasm/alloc.clj`
   - `malloc`/`free` wrappers and C string allocation helpers.

5. `src/vybe/wasm/callback.clj`
   - Callback id registry for host callbacks invoked from Wasm.

6. `src/vybe/native/backend.clj`
   - Backend selector. Flecs now uses the Wasm path when `:wasm` is active.

### Generic ABI Generator

Implemented script:

```text
bin/generate-wasm-abi.clj
```

The generator is generic and config-driven. It accepts an EDN config when run
directly, or can be called as a Clojure function by a library-specific wrapper.
It currently generates:

1. `:layouts`
   - Struct/union layout data from Clang record-layout dumps using `emcc` and
     `-Xclang -fdump-record-layouts-complete`.
   - Target layout is wasm32, so pointers are 32-bit offsets.
   - Includes field paths, offsets, array counts, field C declarations, inferred
     scalar types, and nested C type names.

2. `:constants`
   - Enum values parsed from headers.
   - Simple numeric `#define` values such as shift expressions.
   - `NULL`.

3. `:extern-constants`
   - Header-declared `extern const ecs_entity_t`/`ecs_id_t` names.

4. `:wasm-globals`
   - Exported Wasm globals parsed from `wasm-objdump -x`.

5. `:functions`
   - Function declarations parsed from `emcc -Xclang -ast-dump=json`.
   - Library configs can select function names by prefix. Flecs currently
     selects `ecs_` and `vybe_`.
   - Entries include the original C function type, return C type, argument
     names, argument C types, desugared C types when Clang provides them, and
     the schema used by the Clojure bridge.

The generator does not require hand-authored x-macro tables or hard-coded Flecs
ABI lists. The set of layouts is discovered from `(abi/layout :c_type)` usages in
the configured layout source, or can be supplied explicitly by config for other
libraries.

Library-specific Flecs wrapper:

```text
bin/generate-flecs-wasm-abi.clj
```

Generated Flecs ABI resource:

```text
resources/vybe/wasm/flecs_abi.edn
```

### Flecs Wasm Build

Implemented script:

```text
bin/build-flecs-wasm.sh
```

The script builds:

```text
resources/vybe/wasm/flecs.wasm
resources/vybe/wasm/flecs_abi.edn
```

The build compiles:

1. `flecs/distr/flecs.c`
2. `bin/vybe_flecs.c`
3. `bin/vybe_flecs_wasm_callbacks.c`

Important Emscripten flags:

```sh
-s STANDALONE_WASM=1
-s ALLOW_MEMORY_GROWTH=0
-s INITIAL_MEMORY=1073741824
-s STACK_SIZE=8388608
-s EMULATE_FUNCTION_POINTER_CASTS=1
-s DISABLE_EXCEPTION_THROWING=1
-s ERROR_ON_UNDEFINED_SYMBOLS=0
-s WARN_ON_UNDEFINED_SYMBOLS=0
-s SUPPORT_LONGJMP=0
-Wl,--export-all
--no-entry
```

Flecs addons currently enabled include doc, meta, module, app, stats, parser,
pipeline, query DSL, system, JSON, and REST. REST imports currently link through
host functions in the Wasm runtime; full Java-backed socket/HTTP behavior remains
a follow-up if REST is required at runtime.

### Flecs Runtime

Implemented/converted namespaces:

1. `src/vybe/flecs/wasm.clj`
   - Loads `vybe/wasm/flecs.wasm` with Chicory.
   - Wires Flecs host callbacks.
   - Provides import stubs/host functions required for module instantiation.

2. `src/vybe/flecs/wasm_c.clj`
   - Wasm-backed implementation of the `vybe.flecs.c` API surface used today.
   - Calls Wasm exports instead of jextract Java methods.
   - Allocates descriptor structs in Wasm memory.
   - Writes descriptor layouts from generated ABI offsets.
   - Handles Flecs systems, observers, queries, components, ids, strings, and
     world operations through Wasm memory.

3. `src/vybe/flecs/abi.clj`
   - Loads `resources/vybe/wasm/flecs_abi.edn`.
   - Provides `abi/layout`, `abi/sizeof`, `abi/offsetof`, `abi/const-value`, and
     `abi/component`.
   - Lets `flecs.clj` keep concise component declarations such as:

```clojure
(vp/defcomp iter_t (abi/layout :ecs_iter_t))
(vp/defcomp system_desc_t (abi/layout :ecs_system_desc_t))
(vp/defcomp EcsComponent (abi/layout :EcsComponent))
```

4. `src/vybe/flecs/ids.clj`
   - Interns constant/global accessors from generated ABI metadata.
   - Reads exported Wasm globals for Flecs ids such as `FLECS_IDEcsComponentID_`.
   - Does not depend on `org.vybe.flecs.*`.

Removed from Flecs path:

1. `src/vybe/flecs/impl.clj`
2. `src-java/org/vybe/flecs/**`
3. Flecs jextract imports from game namespaces.

### Flecs Callback Handling

The Wasm module cannot call Clojure functions directly. The current path is:

1. `vp/with-apply` registers a Clojure callback and returns an integer callback
   id.
2. `ecs-system-init`/`ecs-observer-init` write:
   - callback/run function pointer: `vybe_flecs_system_trampoline_addr()`
   - language callback context: the registered callback id
3. `bin/vybe_flecs_wasm_callbacks.c` receives `ecs_iter_t *` and forwards the
   iterator pointer plus callback id to the Chicory host function.
4. `vybe.flecs.wasm-c` resolves the callback id and invokes the original
   Clojure function.

Important implementation details fixed during migration:

1. Zero-valued pointer fields from generated map views must be treated as absent
   because Clojure considers `0` truthy.
2. Fixed-size generated arrays like `ecs_query_desc_t.terms[32]` and observer
   event arrays must filter inactive zero-filled slots before writing Wasm
   descriptors.
3. Zero pointer/string fields like `expr` and term-ref `name` must not be written
   as C strings containing `"0"`.
4. Nested generated layouts must expose map/array getters so descriptor pmap
   values can round-trip through existing public constructors.

## Verification Performed

### Build

Command:

```sh
bin/build-flecs-wasm.sh
```

Result:

```text
resources/vybe/wasm/flecs.wasm
resources/vybe/wasm/flecs_abi.edn
```

### Real Flecs API Examples

Component get/query through existing public API:

```clojure
(require '[vybe.flecs :as vf]
         '[vybe.wasm :as vp])

(vp/defcomp Position [[:x :double] [:y :double]])

(let [w (vf/make-world)]
  (merge w {:alice [(Position {:x 10.0 :y 20.0})]
            :bob   [(Position {:x 20.0 :y 30.0})]})
  (into {} (vf/-get-c w :alice Position))
  (vec (vf/with-query w [{:keys [x y] :as p} Position e :vf/entity]
         [e x y (into {} p)])))
```

Observed result:

```clojure
{:x 10.0, :y 20.0}
;; query returns alice and bob with expected Position values
```

System callback through existing `with-system`:

```clojure
(let [w (vf/make-world)
      seen (atom [])]
  (merge w {:alice [(Position {:x 1.0 :y 2.0})]})
  (vf/with-system w [:vf/name :my-system pos Position]
    (swap! seen conj (into {} pos)))
  (vf/system-run w :my-system)
  @seen)
```

Observed result:

```clojure
[{:x 1.0, :y 2.0}]
```

Observer callback through existing `with-observer`:

```clojure
(let [w (vf/make-world)
      seen (atom [])]
  (vf/with-observer w [:vf/name :obs
                       :vf/events #{:set}
                       {:keys [x y] :as pos} [:mut Position]]
    (swap! seen conj (into {} pos)))
  (merge w {:alice [(Position {:x 4.0 :y 5.0})]})
  (assoc w :alice (Position {:x 6.0 :y 7.0}))
  @seen)
```

Observed result:

```clojure
[{:x 4.0, :y 5.0} {:x 6.0, :y 7.0}]
```

Game-system transform example through existing `vybe.game.system/vybe-transform`:

```clojure
(require '[vybe.flecs :as vf]
         '[vybe.game.system :as vg.s]
         '[vybe.type :as vt])

(let [w (vf/make-world)]
  (vg.s/vybe-transform w)
  (merge w {:alice [(vt/Scale [1.0 1.0 1.0])
                    (vt/Translation)
                    (vt/Velocity)
                    (vt/Rotation [0 0 0 1])
                    [(vt/Transform) :global]
                    (vt/Transform)
                    {:bob [(vt/Scale [1.0 1.0 1.0])
                           (vt/Translation {:x 20 :y 30})
                           (vt/Velocity)
                           (vt/Rotation [0 0 0 1])
                           [(vt/Transform) :global]
                           (vt/Transform)]}]})
  (vf/progress w)
  (select-keys (get (w [:alice :bob]) [vt/Transform :global])
               [:m12 :m13 :m15]))
```

Observed result:

```clojure
{:m12 20.0, :m13 30.0, :m15 1.0}
```

### Test Namespace Status

The requested test namespace now runs on the Wasm backend without a separate
smoke-test namespace. `tests.edn` includes a suite id that matches the AGENTS
single-file command, so the command runs the real `vybe.flecs-test` namespace
through Kaocha instead of a wrapper.

```sh
clj -M:test test/vybe/flecs_test.clj
```

Observed result:

```text
--- vybe/flecs_test.clj (clojure.test) ---------------------------
vybe.flecs-test
  ...
11 tests, 27 assertions, 0 failures.
```

`vybe.game.system` is now backend-aware enough for the Flecs tests: in Wasm mode
it exposes a pure Clojure transform system and does not load Raylib/Jolt at
namespace load time. Native mode keeps the existing Raylib/Jolt-backed systems
behind a native-only macro guard.

The same test also passes with the explicit `:wasm` alias:

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

The generated ABI now includes Flecs function declarations. `vybe.flecs.abi`
exposes `function-data` and `function-desc`, and `vybe.flecs.wasm-c` attaches
`:vybe/fn-meta` by iterating generated `:functions` and resolving matching
wrapper vars. The previous local bridge signature list has been removed.

`vybe.c-test` contains `defn*` functions that compile native C and call
`vybe.flecs.c` functions as if they were native C symbols. That path now works
through generated C-callable Panama upcall stubs that forward into the
Wasm-backed Flecs wrappers. The stubs are not Flecs dylib fallbacks; their
function pointer descriptors come from generated Flecs ABI function data:

```text
Testing vybe.c-test
Ran 4 tests containing 11 assertions.
0 failures, 0 errors.
```

The real game import path also passes under the Wasm backend:

```text
Testing vybe.game-test
Ran 2 tests containing 2 assertions.
0 failures, 0 errors.
```

This uses `vg/-gltf->flecs`, the real `test/vybe/game_test.clj`, and the test's
model-loader binding. It does not load the Raylib or Jolt dylibs.

After removing jextract classes from the default Wasm build output, this test
exposed remaining Raylib class references in namespace imports and static
constant calls. The Wasm path now avoids those load-time class references:

1. `vybe.math` and `vybe.game` no longer import `org.vybe.raylib.raylib`.
2. `vybe.raylib` defines Raylib components with compile-time backend macros so
   the Panama/jextract layout branch is not compiled in Wasm mode.
3. Raylib constants and no-arg calls are routed through `vybe.raylib` helpers.
   Panama resolves the generated Java class lazily; Wasm uses the small set of
   constants required for the current CPU-only/game-test path.

Jolt was the next dylib blocker after this point. The 2026-04-25 update below
records the Jolt Wasm conversion and the passing real `vybe.jolt-test` result.

## Remaining Work

### Flecs Completion

1. Validate REST behavior if REST is part of the required Flecs feature set.
   Current socket imports instantiate the module; real socket behavior should be
   Java-hosted if REST is used.
2. Build the jar and verify Flecs artifacts:

```sh
clj -T:build compile-app
clj -T:build jar
jar tf target/io.github.pfeodrippe.vybe-flecs-*.jar | rg 'flecs|dylib|so|dll|wasm|abi'
```

Expected Flecs jar state:

1. Include `vybe/wasm/flecs.wasm`.
2. Include `vybe/wasm/flecs_abi.edn`.
3. Exclude `libvybe_flecs.dylib`, `libvybe_flecs.so`, and
   `vybe_flecs.dll`.
4. Exclude `org/vybe/flecs/**` generated jextract classes.
5. Exclude the removed custom `org/vybe/wasm/ContiguousByteArrayMemory.class`;
   the runtime uses Chicory memory directly.

### Generic ABI Generator Hardening

1. Add config-driven scalar typedef overrides so each library can map aliases
   without changing generator source.
2. Add config-driven exported-function metadata if a future wrapper generator
   needs generated Clojure wrappers for all exports.
3. Add config-driven constant patterns beyond Flecs `ecs_entity_t`/`ecs_id_t`.
4. Add a small self-check command that compares generated `sizeof`/`offsetof`
   values against selected Wasm helper exports for any library that provides
   them. This should be a validation check, not the source of truth.
5. Keep the generated EDN deterministic and suitable for committing.

### Raylib

Raylib is not a simple dylib-to-Wasm swap for the desktop JVM use case because
windowing, OpenGL, audio, and file APIs require host/platform integration. The
pragmatic path is:

1. Split CPU-only pieces first, especially raymath-style structs/functions.
   `vector-2-add` and `vector-2-subtract` are already available in Wasm mode.
2. For rendering/windowing, decide between:
   - Java-hosted rendering backend with the same high-level Vybe API.
   - Browser/WebGL target for Emscripten Raylib.
   - Keeping Raylib out of the first Wasm-only JVM milestone.
3. Remove load-time Raylib dependencies from Flecs tests and game system code
   where they are not actually needed.

### Jolt

Jolt is feasible but more complex than Flecs:

1. Build Jolt/JoltC with Emscripten.
2. Decide exception/RTTI/threads/atomics settings.
3. Generate Wasm ABI metadata from the JoltC headers with
   `bin/generate-wasm-abi.clj` plus a Jolt config wrapper.
4. Implement `vybe.jolt.wasm_c` as the backend for `vybe.jolt.c`.
5. Validate physics examples against existing tests.

### Netcode

Cute Net cannot open UDP sockets from pure Wasm without host functions.
Recommended split:

1. Move protocol/serialization/CPU-only code to Wasm.
2. Implement socket I/O as Java host functions or a Java transport layer.
3. Keep the public `vybe.netcode.c` API stable while routing socket operations
   through the host layer.

## Implementation Order

1. Keep Flecs green while packaging it without `libvybe_flecs.*` or
   `org/vybe/flecs/**`.
2. Add the `defn*` to Wasm API bridge so generated native code can call
   Wasm-backed C APIs without reintroducing Flecs dylibs.
3. Generalize ABI config overrides based on the first non-Flecs library.
4. Convert the next CPU-only library slice, preferably JoltC or more raymath.
5. Address socket/window/audio libraries with explicit Java host integrations
   rather than pretending raw Wasm can perform native OS operations directly.

## Commands

Build Flecs Wasm and ABI:

```sh
bin/build-flecs-wasm.sh
```

Regenerate Flecs ABI only:

```sh
clj -M bin/generate-flecs-wasm-abi.clj
```

Run a real Flecs Wasm example:

```sh
clj -M:wasm -e '
(require (quote [vybe.flecs :as vf]) (quote [vybe.wasm :as vp]))
(vp/defcomp Position [[:x :double] [:y :double]])
(let [w (vf/make-world)
      seen (atom [])]
  (merge w {:alice [(Position {:x 1.0 :y 2.0})]})
  (vf/with-system w [:vf/name :my-system pos Position]
    (swap! seen conj (into {} pos)))
  (vf/system-run w :my-system)
  (println @seen))
'
```

Run the configured test suite once non-Flecs native load blockers are resolved:

```sh
clj -M:test
```

Run linter:

```sh
clj-kondo --lint src src-java test
```

## 2026-04-25 Jolt Wasm Implementation Update

Jolt has now been converted far enough to run the real `test/vybe/jolt_test.clj`
against `resources/vybe/wasm/jolt.wasm` without loading the `joltc_zig` dylib or
jextract classes on the Wasm path.

### Jolt Build Artifacts

Implemented files:

```text
bin/build-jolt-wasm.sh
bin/generate-jolt-wasm-abi.clj
bin/vybe_jolt_wasm.cpp
resources/vybe/wasm/jolt.wasm
resources/vybe/wasm/jolt_abi.edn
src/vybe/jolt/abi.clj
src/vybe/jolt/wasm.clj
src/vybe/jolt/wasm_c.clj
```

`bin/build-jolt-wasm.sh` builds Jolt/JoltC with Emscripten and links a standalone
Wasm module. The build disables native pieces that do not fit the current
standalone JVM Wasm runtime:

```text
-DUSE_WASM_SIMD=OFF
-DUSE_ASSERTS=OFF
-DDOUBLE_PRECISION=OFF
-DENABLE_OBJECT_STREAM=OFF
-DPROFILER_IN_DEBUG_AND_RELEASE=OFF
-DPROFILER_IN_DISTRIBUTION=OFF
-DDEBUG_RENDERER_IN_DEBUG_AND_RELEASE=OFF
-DDEBUG_RENDERER_IN_DISTRIBUTION=OFF
-s STANDALONE_WASM=1
-s ALLOW_MEMORY_GROWTH=1
-s INITIAL_MEMORY=64MB
-Wl,--export-all
--no-entry
```

The script was run successfully and produced:

```text
resources/vybe/wasm/jolt.wasm  ; about 2.4 MB
```

`bin/generate-jolt-wasm-abi.clj` uses the generic `bin/generate-wasm-abi.clj`
script with C++ support enabled (`em++`, `.cpp` probes). The generated ABI EDN
contains Jolt layouts, constants, and function signatures extracted from Clang,
not hand-written type tables.

### Generic ABI Improvements From Jolt

The generic ABI generator now supports Jolt-style C++/C headers:

1. Configurable compiler (`:cc`) and source extension (`:source-extension`).
2. Header-discovered top-level `typedef struct { ... } Name;` layouts when no
   explicit layout list is provided.
3. Clang record-layout output with `dsize` and extended alignment metadata.
4. Scalar typedef alias extraction, including Jolt aliases such as `JPC_BodyID`,
   `JPC_SubShapeID`, `JPC_ObjectLayer`, and `JPC_MotionType`.
5. Unsigned 32-bit fields as `:uint`, so values like `4294967295` round-trip
   without overflow.
6. Enum parsing for anonymous/typedef enums, hex/binary values, bit shifts, and
   `|` expressions.
7. Simple define parsing for constants including `FLT_EPSILON`.
8. Function declaration extraction through Clang JSON AST instead of generated
   Java bindings.

### Jolt Runtime Path

`src/vybe/jolt/c.clj` is now backend-aware. In Wasm mode it exposes public vars
from `vybe.jolt.wasm-c`; in non-Wasm mode it can still lazy-load the old impl
path, but the Wasm test path does not load `org.vybe.jolt.*` or `joltc_zig`.

`src/vybe/jolt/wasm.clj` loads `vybe/wasm/jolt.wasm` and wires the required host
imports for the current standalone module:

1. Emscripten exception/unwind host function.
2. Memory-growth notification host function.
3. `_msync_js` host function returning success for the standalone runtime.

`src/vybe/jolt/wasm_c.clj` interns generated wrappers from
`resources/vybe/wasm/jolt_abi.edn`. Numeric arguments are coerced by the
Clang-derived function schema, so floats/doubles are passed as raw Wasm bits
instead of being treated as pointers. This fixed `JPC_PhysicsSystem_Update`,
which was previously receiving a zero delta time.

Special wrappers are still needed for Jolt APIs that take array/out pointers or
need standalone-Wasm-safe C++ helpers:

1. Vector pointer arguments for shape creation and body velocity/position APIs.
2. `JPC_PhysicsSystem_Create` via `vybe_jolt_physics_system_create_default`.
3. `JPC_JobSystem_Create` via `vybe_jolt_job_system_create_single_threaded`.
4. `JPC_BodyInterface_CreateAndAddBody` with generated `JPC_BodyCreationSettings`
   layout copying into Wasm memory.
5. `JPC_NarrowPhaseQuery_CastRay` with 16-byte-aligned temporary ray/hit structs
   and explicit out-parameter copyback.

The helper C++ file `bin/vybe_jolt_wasm.cpp` keeps the native behavior that was
previously supplied through Clojure callback objects: non-moving/moving broad
phase layers, object-vs-broadphase filtering, and object-layer pair filtering.
It also creates a single-threaded Jolt job system because the Jolt thread-pool
job system is not valid in the current standalone Wasm runtime.

### Jolt Component/Memory Fixes

`vybe.wasm` now supports more of the `vybe.panama` component surface:

1. `defcomp` keeps the same macro shape.
2. Generated ABI layouts still come from Clang-derived `abi/layout` data.
3. Simple schema components now get Wasm-aware pointer builders instead of
   falling back to Panama pointer fields.
4. Constructor-backed components work for initial construction.
5. Unsigned `:uint` fields read/write as unsigned 32-bit values.
6. Fixed arrays accept sequential values and vector-like pmap values with
   `:x/:y/:z/:w` keys.

One important out-parameter rule was discovered during Jolt raycast work:
constructor-backed pmaps should not be updated one field at a time through
`assoc`, because the constructor can reset previously written fields. Jolt
out-parameter copyback now calls the component field builders directly on the
existing Java memory segment.

### Real Test Results

Passing real Jolt test command:

```sh
clj -Sdeps '{:paths ["src" "resources" "vybe_native" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm \
  -e '(require (quote vybe.jolt-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

Observed result:

```text
Testing vybe.jolt-test
Ran 3 tests containing 5 assertions.
0 failures, 0 errors.
```

Passing real game test command:

```sh
clj -Sdeps '{:paths ["src" "resources" "vybe_native" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm \
  -e '(require (quote vybe.game-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.game-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

Observed result:

```text
Testing vybe.game-test
Ran 2 tests containing 2 assertions.
0 failures, 0 errors.
```

### Flecs Regression Fixed

After generalizing `vybe.wasm/defcomp` for Jolt/simple-schema components, the
real Flecs namespace temporarily regressed because observer event arrays were
being built with the Panama `vp/arr` shape. In Wasm mode that produced a JVM
`MemorySegment`, while the generated Wasm component writer expects sequential
array data for fixed C arrays. The observer descriptor's `events[8]` field was
therefore zero-filled, so `EcsOnSet` observers never fired.

The fix keeps the existing Flecs behavior and changes only the backend seam:
`-observer` still derives the same event ids, but passes a plain vector to the
generated `ecs_observer_desc_t` layout. The Wasm component builder then writes
the real Clang-derived `events` offset and element width.

Passing real Flecs test command:

```sh
clj -M:test test/vybe/flecs_test.clj
```

Observed result:

```text
11 tests, 27 assertions, 0 failures.
```

`ex-1-w-map`, `unique-trait-test`, and `pair-wildcard-test` are now green on the
real test file. This validates observer callbacks, observer-driven unique trait
setup, pair wildcard lookup, and scheduled system progress through the Wasm
backend.

Passing real `defn*` bridge test command:

```sh
clj -Sdeps '{:paths ["src" "resources" "vybe_native" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm \
  -e '(require (quote vybe.c-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.c-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

Observed result:

```text
Testing vybe.c-test
Ran 4 tests containing 11 assertions.
0 failures, 0 errors.
```

### Current Priority Order

1. Keep the combined relevant suite green: Flecs, Jolt, game, and `vybe.c-test`.
2. Keep the Wasm packaging checks green for both the full `vybe` jar and the
   standalone `vybe-flecs` jar.
3. Run `git diff --check` and clj-kondo to separate new issues from the existing
   linter baseline.

### Wasm Packaging Check

The default build backend is now Wasm unless `VYBE_NATIVE_BACKEND=panama` or
`-Dvybe.native.backend=panama` is supplied. In Wasm mode:

1. `compile-app` skips jextract Java compilation.
2. `compile-app` skips the Sonic Pi/SuperCollider native-resource zip step.
3. Resource copying removes `resources/vybe/native` artifacts from the target
   while preserving `src/vybe/native/backend.clj` and
   `src/vybe/native/loader.clj`.
4. `build-flecs` no longer compiles or copies any jextract Java classes; it only
   packages the Clojure namespaces and `flecs.wasm`/`flecs_abi.edn` needed by
   the standalone Flecs artifact.

Verified full Wasm jar:

```sh
clj -T:build compile-app
clj -T:build jar
jar tf target/*.jar | rg '(^org/vybe/(flecs|jolt|raylib)/|vybe/native/|\.(dylib|so|dll)(\.|$)|vybe/wasm/)'
```

Observed relevant contents:

```text
vybe/native/backend.clj
vybe/native/loader.clj
vybe/wasm/flecs.wasm
vybe/wasm/flecs_abi.edn
vybe/wasm/jolt.wasm
vybe/wasm/jolt_abi.edn
```

No `org/vybe/flecs`, `org/vybe/jolt`, or `org/vybe/raylib` jextract classes are
present in the Wasm jar, and no `.dylib`, `.so`, or `.dll` files are present.
`vybe.game-test` was rerun after this packaging cleanup with `target/classes`
containing no Raylib jextract classes and still passes:

```text
Testing vybe.game-test
Ran 2 tests containing 2 assertions.
0 failures, 0 errors.
```

Verified standalone Flecs jar:

```sh
clj -T:build build-flecs
jar tf target/*.jar | rg '(^org/vybe/(flecs|jolt|raylib)/|vybe/native/|\.(dylib|so|dll)(\.|$)|vybe/wasm/)'
```

Observed relevant contents:

```text
vybe/native/backend.clj
vybe/native/loader.clj
vybe/wasm/flecs.wasm
vybe/wasm/flecs_abi.edn
```

No Flecs dylib or Flecs jextract classes are packaged in the standalone Flecs
artifact.
