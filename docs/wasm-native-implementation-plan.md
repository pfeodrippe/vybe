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
bin/flecs-wasm-abi.edn
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
2. The generated Flecs Java binding directory under `src-java`.
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
clj -M bin/generate-wasm-abi.clj bin/flecs-wasm-abi.edn
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
bin/jolt-wasm-abi.edn
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

`bin/jolt-wasm-abi.edn` uses the generic `bin/generate-wasm-abi.clj`
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
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
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
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
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
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
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

The build is Wasm-only for the migrated native surface. There is no runtime
backend switch and no Panama/dylib fallback path for Flecs or Jolt:

1. `compile-app` skips jextract Java compilation.
2. `compile-app` skips the Sonic Pi/SuperCollider native-resource zip step.
3. Resource copying removes `resources/vybe/native` artifacts and empty native
   directories from the target.
4. `build-flecs` no longer compiles or copies any jextract Java classes; it only
   packages the Clojure namespaces and `flecs.wasm`/`flecs_abi.edn` needed by
   the standalone Flecs artifact.

Verified full Wasm jar:

```sh
clj -T:build compile-app
clj -T:build jar
jar tf target/*.jar | rg '(^org/vybe/(flecs|jolt|raylib|netcode)/|vybe/native/|\.(dylib|so|dll)(\.|$)|vybe/wasm/)'
```

Observed relevant contents:

```text
vybe/wasm/flecs.wasm
vybe/wasm/flecs_abi.edn
vybe/wasm/jolt.wasm
vybe/wasm/jolt_abi.edn
```

No `org/vybe/flecs`, `org/vybe/jolt`, `org/vybe/raylib`, or
`org/vybe/netcode` jextract classes are present in the Wasm jar. No
`vybe/native` resources and no `.dylib`, `.so`, or `.dll` files are present.
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
jar tf target/*.jar | rg '(^org/vybe/(flecs|jolt|raylib|netcode)/|vybe/native/|\.(dylib|so|dll)(\.|$)|vybe/wasm/)'
```

Observed relevant contents:

```text
vybe/wasm/flecs.wasm
vybe/wasm/flecs_abi.edn
```

No Flecs dylib or Flecs jextract classes are packaged in the standalone Flecs
artifact.

### Latest Verification

Commands run after removing backend switches, dylib loaders, generated Java
bindings for migrated libs, and script-style ABI wrappers:

```sh
clj -M:test test/vybe/flecs_test.clj
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' -M:wasm -e '(require (quote vybe.jolt-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' -M:wasm -e '(require (quote vybe.game-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.game-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' -M:wasm -e '(require (quote vybe.c-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.c-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' -M:wasm -e '(require (quote vybe.network-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.network-test))] (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
git diff --check
```

Observed results:

```text
vybe.flecs-test: 11 tests, 27 assertions, 0 failures.
vybe.jolt-test: 3 tests, 5 assertions, 0 failures.
vybe.game-test: 2 tests, 2 assertions, 0 failures.
vybe.c-test: 4 tests, 11 assertions, 0 failures.
vybe.network-test: 1 test, 2 assertions, 0 failures.
git diff --check: clean.
```

`clj-kondo --lint src src-java test build.clj` still fails against the existing
project baseline. Latest count is 142 errors and 283 warnings; the previous
baseline captured before these cleanup edits was 132 errors and 285 warnings.
The increase is from deleting generated Java bindings and leaving unused legacy
impl namespaces/docs that still mention generated symbols. Those should be
removed or moved behind separate source paths rather than reintroducing
jextract output.

### 2026-04-26 Runtime `vybe.c` Wasm Update

The runtime Wasm execution path is configured for compiled Chicory execution,
not interpretation. `vybe.wasm.runtime/compiled-machine-factory` uses
`InterpreterFallback/FAIL`, so a module that Chicory cannot compile fails at
load/instantiation time instead of silently falling back to the interpreter.

Runtime-generated Wasm modules can now be loaded from bytes or filesystem paths:

```clojure
(vybe.wasm/load-module-from-bytes bytes {:initialize? false})
(vybe.wasm/load-module-from-file "/tmp/generated.wasm" {:initialize? false})
```

A real runtime probe was compiled with Emscripten and executed through this path:

```sh
cat > /tmp/vybe_wasm_probe.c <<'C'
int add1(int x) { return x + 1; }
C
emcc -O3 -sSTANDALONE_WASM=1 -Wl,--no-entry -Wl,--export=add1 \
  /tmp/vybe_wasm_probe.c -o /tmp/vybe_wasm_probe.wasm
clj -M:wasm -e '(require (quote vybe.wasm)) (let [m (vybe.wasm/load-module-from-file "/tmp/vybe_wasm_probe.wasm" {:initialize? false})] (println (vybe.wasm/call m "add1" 41)))'
```

Observed result:

```text
42
```

This validates the runtime compilation/execution mode for generated Wasm. Apple
Clang in the current environment does not have a wasm32 backend, so runtime C to
Wasm generation must use `emcc` here. The generic `vybe.c` migration should use
this as its compiler driver unless a Zig/LLVM wasm target is explicitly provided.

#### Current `vybe.c` Blocker

The remaining Flecs test failure is no longer the generated Flecs ABI. It is the
old `vybe.c` execution model:

1. `vc/defn*` still emits a host shared library and creates a Panama `VybeCFn`.
2. `vf/defsystem-c` passes the host function address as the system callback.
3. Flecs Wasm correctly invokes the Wasm trampoline, but the callback context is
   a host function address, not a `vybe.wasm` callback registry id.
4. The Wasm trampoline therefore fails with `No registered Flecs Wasm callback`.

This is expected until `vybe.c` stops creating dylibs. The fix is not a fallback
wrapper around the dylib. The fix is to make `vybe.c` compile the generated C to
Wasm and return a Wasm-backed function object.

#### Required `vybe.c` Wasm Design

`vybe.c` must change from:

```text
Clojure form -> generated C -> clang -shared -> SymbolLookup/libraryLookup -> Panama downcall/upcall
```

to:

```text
Clojure form -> generated C -> emcc/wasm compiler -> Chicory compiled module -> Wasm export call/callback
```

The hard constraint is memory identity. Generated systems receive Flecs pointers
that point into Flecs Wasm linear memory. A separately compiled `vybe.c` Wasm
module cannot safely dereference those pointers unless it uses the same linear
memory as the Flecs module or is linked into the same module. Therefore the
implementation must do one of these, in priority order:

1. Compile runtime `vybe.c` modules with imported shared memory supplied by the
   Flecs/Jolt module instance.
2. Link generated systems into the same Wasm module as the owning C library when
   import-memory is not viable.
3. For non-pointer pure functions, allow isolated runtime Wasm modules because
   they do not dereference library memory.

There should be no `backend/wasm?`, `backend/panama?`, or dylib fallback branch.
Pure functions and system callbacks use the same Wasm runtime abstraction, with
compiled Chicory execution and `InterpreterFallback/FAIL`.

#### Immediate Implementation Tasks

1. Add a `VybeWasmFn` representation in `vybe.wasm` or `vybe.c` with `:module`,
   `:export`, `:fn-desc`, and optional `:memory-owner` metadata.
2. Change `vc/defn*`/`-c-compile` to emit `.wasm` artifacts with `emcc -O3`, not
   `clang -shared`.
3. Replace `-c-fn-builder`/`SymbolLookup` with a builder that loads the generated
   Wasm via `load-module-from-file` and calls exported functions via
   `vybe.wasm/call`.
4. For `defsystem-c`, register the generated Wasm callback in
   `vybe.wasm/register-callback!` and store that registry id in
   `ecs_system_desc_t.callback_ctx`; the callback itself must call the generated
   Wasm export, not a host dylib.
5. Implement imported/shared memory support or same-module linking before any
   generated function dereferences Flecs/Jolt pointers. Without this, pure
   scalar runtime Wasm works, but ECS systems that access component pointers are
   not correct.
6. Re-run `clj -M:wasm:test test/vybe/flecs_test.clj`. The known current failure
   is `No registered Flecs Wasm callback` in `c-systems-test`.
7. After Flecs is green, run the `noel` example in `~/dev/vybe-games` against
   the Wasm-only path.

#### Latest Focused Verification

Current focused command:

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

Current observed state:

```text
11 tests, 23 assertions, 1 error, 0 failures
```

The error is in `c-systems-test` at the `defsystem-c` callback boundary and is
blocked on the `vybe.c` runtime Wasm migration above. The test no longer depends
on `backend/current`, and the Flecs dylib/jextract path is not used.

### 2026-04-26 Update: Flecs C Systems Running Through Wasm

The previous `vybe.c` blocker is now resolved for Flecs-focused coverage. The
runtime C path no longer creates a host dylib for `vc/defn*`; it emits `.wasm`
artifacts and invokes them through Chicory compiled execution.

Implemented pieces:

1. `vybe.c` now builds generated C with `emcc -O3 -sSTANDALONE_WASM=1` and
   exports the generated function symbol from the `.wasm` module.
2. Generated Flecs systems are compiled with imported memory and are invoked with
   the Flecs module memory, so ECS iterator/component pointers stay valid.
3. `defsystem-c` registers a `vybe.wasm` callback id in `callback_ctx` and uses
   the Flecs Wasm trampoline address as the actual callback function pointer.
4. Imported C calls from generated system Wasm are dispatched back into the
   owning Wasm module (`vybe.flecs.wasm-c`, and the same pattern is prepared for
   `vybe.jolt.wasm-c`) instead of through a dylib.
5. Standalone generated functions now merge their generated ABI schemas into the
   caller translation unit, so nested/inlined functions bring layouts such as
   `vybe.type/Vector4` without hand-written type tables.
6. `vybe.panama/comp-fields` now returns fields in generated layout order. This
   fixed the concrete `ecs_iter_t` bug where generated C read `:count` from the
   `:delta_time` offset and overran component arrays.
7. The old eager native `vybe.c` debug upcall used by `tap>` macroexpansion was
   removed from namespace load, eliminating the Java native-access warning in the
   focused Flecs test path.

Focused verification now passes:

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

Observed result:

```text
11 tests, 26 assertions, 0 failures
```

Additional verification:

```sh
rg -n -- "backend/wasm\?|backend/panama\?|vybe\.native\.backend|vybe\.native\.loader|--enable-native-access|native-access|backend/current|org\.vybe\.(flecs|jolt|raylib|netcode)" src test bin build.clj deps.edn docs || true
```

Current result: source/test/build code has no matches. The remaining matches are
only in this implementation plan, where they document forbidden patterns and
migration goals.

Lint status after the change:

```sh
clj-kondo --lint src src-java test build.clj
```

Observed result:

```text
errors: 142, warnings: 286
```

The lint run still reflects the repository's existing unresolved generated-symbol
baseline. The Wasm/Flecs validation above is the authoritative focused runtime
check for this stage.

Remaining work before the `~/dev/vybe-games` `noel` example should be treated as
Wasm-ready:

1. Finish the same Wasm-only conversion for Raylib/Jolt-generated C usage needed
   by the real game path.
2. Remove or replace any remaining jextract-generated Java files for libraries
   that have been fully migrated to Wasm.
3. Run the broader game/system tests after Raylib/Jolt Wasm calls are complete.
4. Then run the `noel` example from `~/dev/vybe-games` against the Wasm-only
   artifacts.

### 2026-04-26 Update: Raylib/Jolt Game Path and `noel` Smoke

The real game path now runs through the Wasm-native stack far enough for the
`noel` example to initialize and return its draw function in a bounded smoke.

Implemented pieces:

1. Added generic ABI-driven Wasm wrapper generation in
   `bin/generate-wasm-wrappers.clj`. It wraps aggregate arguments/returns as
   pointers based on generated clang ABI data, not hand-written function lists.
2. Added `bin/build-raylib-wasm.sh`, `resources/vybe/wasm/raylib.wasm`, and
   `resources/vybe/wasm/raylib_abi.edn`. Raylib wrappers and ABI are generated
   from clang/wasm metadata.
3. Added `vybe.raylib.abi` and `vybe.raylib.wasm`. Raylib Wasm loads through
   Chicory compiled mode with `InterpreterFallback/FAIL`.
4. Added generated Raylib dispatch in `vybe.raylib.c`, plus a byte-backed
   `load-model` path that copies GLB bytes into Wasm memory and calls Raylib's
   own model parser through `VyLoadModelFromMemory`.
5. Fixed Wasm opaque pointers so they are typed values, not Clojure maps. This
   keeps Jolt opaque values compatible with Flecs component insertion.
6. Switched `vybe.game`'s native abstraction alias from `vybe.panama` to
   `vybe.wasm` for game code that consumes Raylib/Jolt Wasm pointers.
7. `_set-c` now skips Wasm null pointers (`0`) the same way it skips `nil`;
   Flecs entity id `0` is invalid, so null native pointers must not become ECS
   ids.
8. Added generated Raygui coverage for `GuiGroupBox`, used by `noel`.

Focused verification:

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

```text
11 tests, 26 assertions, 0 failures
```

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.jolt-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test))] (println r) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 3 tests containing 5 assertions.
0 failures, 0 errors.
```

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.game-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.game-test))] (println r) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 2 tests containing 2 assertions.
0 failures, 0 errors.
```

External `noel` smoke:

```sh
cd ~/dev/vybe-games
clj -Sdeps '{:deps {io.github.pfeodrippe/vybe {:local/root "../vybe"}}}' \
  -M:dev -e '(require (quote noel)) (let [f (future (noel/init))] (Thread/sleep 8000) (println :init-realized? (realized? f)) (when (realized? f) (println @f)) (shutdown-agents))'
```

Observed result before the bounded runner kills the still-running app process:

```text
:init-realized? true
#object[vybe.game$start_BANG_$fn__...]
```

Lint status after this stage:

```sh
clj-kondo --lint src src-java test build.clj
```

Observed result:

```text
errors: 86, warnings: 266
```

The linter still reports old dynamic/generated-symbol issues, but the count is
below the earlier baseline (`errors: 142, warnings: 286`) after adding `vybe.wasm`
macro lint configuration.

Source checks:

```sh
rg -n -- "backend/wasm\?|backend/panama\?|backend/current|vybe\.native\.backend|vybe\.native\.loader|--enable-native-access|native-access" src test bin build.clj deps.edn || true
```

Current result: no matches in source/test/build/config inputs. A separate check
for migrated jextract Java files under `src-java/org/vybe/{flecs,jolt,raylib}`
also returns no files.

## 2026-04-26 Game-Facing Wasm Abstraction Cleanup

This stage removed the remaining game-facing direct Panama/native-access usage
that was introduced by the migration path, without adding backend switches or
fallback branches.

Changes made:

1. `vybe.game` now writes Raylib shader `locs` through Wasm linear memory with
   `vybe.wasm/write-i32!` instead of calling `MemorySegment.set`.
2. `vybe.game`, `vybe.game.system`, `vybe.raylib`, `vybe.type`, and
   `vybe.game-test` now use `vybe.wasm` as the native abstraction alias for the
   game/Raylib-facing component and pointer helpers.
3. `vybe.wasm` now has Wasm-backed pointer/sequence abstractions:
   - `IVybeWasmPointer` for values that expose a wasm32 pointer.
   - `WasmPSeq` for arrays allocated in Wasm memory.
   - `vp/arr` support for component vectors and primitive vectors without
     routing through `vybe.panama/arr`.
   - `vp/mem` string/byte caching into Wasm memory for APIs such as Raygui text
     input helpers.
4. Numeric pointers, Wasm opaque pointers, Wasm maps, and Wasm arrays now share
   the same pointer extraction path through `vybe.wasm/mem` and `vybe.wasm/&`.
5. No `backend/wasm?`, compatibility branch, dylib fallback branch, or
   `--enable-native-access` flag was added.

Focused verification after the cleanup:

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.game-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.game-test))] (println r) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 2 tests containing 2 assertions.
0 failures, 0 errors.
```

This run did not print the previous `MemorySegment::reinterpret` native-access
warning.

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

```text
11 tests, 26 assertions, 0 failures.
```

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.jolt-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test))] (println r) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 3 tests containing 5 assertions.
0 failures, 0 errors.
```

External `noel` smoke using the local Wasm-enabled checkout:

```sh
cd ~/dev/vybe-games
tmp=$(mktemp); (clj -Sdeps '{:deps {io.github.pfeodrippe/vybe {:local/root "../vybe"}}}' \
  -M:dev -e '(require (quote noel)) (let [f (future (noel/init))] (Thread/sleep 8000) (println :init-realized? (realized? f)) (when (realized? f) (println @f)) (shutdown-agents))' >$tmp 2>&1) & pid=$!; \
  sleep 22; if kill -0 $pid 2>/dev/null; then kill $pid 2>/dev/null || true; wait $pid 2>/dev/null || true; echo 'exit=timeout'; else wait $pid; echo exit=$?; fi; cat $tmp
```

Observed result before the bounded runner killed the still-running app process:

```text
:init-realized? true
#object[vybe.game$start_BANG_$fn__...]
```

Final lint comparison for this stage:

```sh
clj-kondo --lint src src-java test build.clj
```

```text
errors: 86, warnings: 262
```

The pre-cleanup baseline for this stage was `errors: 86, warnings: 266`, so this
stage did not add lint errors and reduced warnings by four. The remaining lint
errors are existing macro/generated-symbol and optional audio/overtone issues,
not new Wasm fallback paths.

Source and artifact checks:

```sh
rg -n -- "backend/wasm\?|backend/panama\?|backend/current|vybe\.native\.backend|vybe\.native\.loader|--enable-native-access|native-access" src test bin build.clj deps.edn || true
```

Current result: no matches.

```sh
find src-java -type f \( -path '*org/vybe/flecs*' -o -path '*org/vybe/jolt*' -o -path '*org/vybe/raylib*' -o -path '*org/vybe/netcode*' \) | sort | head -200
```

Current result: no migrated jextract-generated Java files remain in the working
tree for those libraries.

Deleted files compared with `origin/main` outside `src-java/org/vybe/...` are
limited to old native-loader/impl artifacts and the native keep marker:

```text
resources/vybe/native/keep
src/vybe/flecs/impl.clj
src/vybe/jolt/impl.clj
src/vybe/native/loader.clj
src/vybe/netcode/impl.clj
src/vybe/raylib/impl.clj
```

## 2026-04-26 Jolt Wasm Contact Listener Bridge Investigation

This stage continued the Wasm-only migration with no `backend/wasm?` branch, no
Panama/dylib fallback, and no `--enable-native-access` runtime flag.

Implemented and verified:

1. Fixed Jolt aggregate writeback for `RayCastResult` by writing directly through
   the component field builders when the target is a `VybePMap`. This avoids the
   old partial-`assoc` constructor path that restored default `:body_id` and
   `:fraction` values.
2. Rebuilt `resources/vybe/wasm/jolt.wasm` from `bin/build-jolt-wasm.sh`.
3. Added a Jolt Wasm contact callback import bridge in `vybe.jolt.wasm` and
   `bin/vybe_jolt_wasm.cpp` so callback ids are not used as raw Wasm function
   pointers.
4. Changed `vybe.jolt/contact-listener` to pass the actual
   `ContactListenerVTable` data to the Wasm wrapper instead of wrapping it in the
   old native `VTable` pointer container. The old pointer container truncates JVM
   native addresses to wasm32 and is not a valid Wasm representation.

Focused verification that passes:

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.jolt-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test))] (println r) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 3 tests containing 5 assertions.
0 failures, 0 errors.
```

Current blocker:

Installing a Jolt contact listener and then running `JPC_PhysicsSystem_Update`
still fails inside the compiled Wasm module with:

```text
out of bounds memory access: attempted to access address: -12016
```

This reproduces even with an empty listener vtable and also with an
`on-contact-added` callback id installed. That means the remaining issue is not
fallback selection and not the high-level `noel` app code; it is still in the
Jolt Wasm listener/update ABI path. The noel one-frame smoke cannot be claimed
Wasm-ready until this contact listener path is fixed without disabling contact
behavior.

Important constraint for the next step: do not hide this by skipping
`contact-listener`, removing `vybe.game.system` behavior, or adding a dylib/native
fallback. The fix should stay in the Jolt Wasm C/Clojure bridge.

## 2026-04-26 Jolt Contact Listener Resolution And Noel Smoke

This stage resolved the Jolt Wasm contact-listener/update crash without adding a
backend switch, dylib fallback, Panama fallback, or `--enable-native-access`.

Root cause and fix:

1. The JoltC contact listener vtable bridge was structurally correct, but the
   contact generation path overflowed Emscripten's default Wasm stack under
   Chicory compiled mode. The failing symptom was an out-of-bounds linear-memory
   access at a negative address during `JPC_PhysicsSystem_Update` after a
   contact listener was installed.
2. `bin/build-jolt-wasm.sh` now links Jolt with `-s STACK_SIZE=8MB` while keeping
   `-O3`, `STANDALONE_WASM=1`, `ALLOW_MEMORY_GROWTH=1`, and compiled Chicory
   execution. This is still fast compiled Wasm, not interpreter mode.
3. `bin/vybe_jolt_wasm.cpp` now keeps only the JoltC-compatible listener shape:
   a wasm-side struct whose first field is `JPC_ContactListenerVTable *vtbl`,
   with callback fields routed through imported host functions. The stale direct
   `JPH::ContactListener` experiment was removed so there is one listener path.
4. `resources/vybe/wasm/jolt.wasm` was rebuilt from the updated script.

Focused contact-listener verification:

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '
(require (quote vybe.jolt) (quote vybe.type) (quote vybe.jolt.abi))
(let [phys (vybe.jolt/physics-system)
      hits (atom 0)]
  (vybe.jolt/contact-listener phys {:on-contact-added (fn [& _] (swap! hits inc))})
  (vybe.jolt/body-add phys (vybe.jolt/BodyCreationSettings {:position (vybe.type/Vector4 [0 -1 0 1]) :rotation (vybe.type/Vector4 [0 0 0 1]) :shape (vybe.jolt/box (vybe.jolt/HalfExtent [100 1 100]))}))
  (vybe.jolt/body-add phys (vybe.jolt/BodyCreationSettings {:position (vybe.type/Vector4 [0 1 0 1]) :rotation (vybe.type/Vector4 [0 0 0 1]) :shape (vybe.jolt/box (vybe.jolt/HalfExtent [1 1 1])) :motion_type (vybe.jolt.abi/JPC_MOTION_TYPE_DYNAMIC) :object_layer :vj.layer/moving}))
  (dotimes [_ 20] (vybe.jolt/update! phys (/ 1.0 60)))
  (println :hits @hits)
  (when (zero? @hits) (throw (ex-info "contact callback was not invoked" {:hits @hits}))))'
```

Observed result:

```text
:hits 1
```

Focused regression tests after the fix:

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

```text
11 tests, 26 assertions, 0 failures.
```

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.jolt-test) (quote vybe.game-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test) (quote vybe.game-test))] (println r) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 5 tests containing 7 assertions.
0 failures, 0 errors.
```

External `noel` real-app smoke using `~/dev/vybe-games`:

```sh
cd ~/dev/vybe-games
clj -J-XstartOnFirstThread -M:dev -m noel
```

Bounded smoke result:

```text
NOEL_STATUS=alive-for-30s-terminated
```

The `noel` run used `:dev`, which resolves `io.github.pfeodrippe/vybe` through
`{:local/root "../vybe"}`. It did not use the old OS aliases, did not pass
`--enable-native-access`, and did not pass `-Djava.library.path=vybe_native`.
`-J-XstartOnFirstThread` is still required on macOS for Raylib window ownership;
that is not a Panama/native-access or dylib fallback flag.

Current remaining verification items:

1. Run a longer interactive `noel` session and exercise contacts, raycasts,
   shaders, render textures, and audio-triggered paths manually.
2. Run the full `clj -M:wasm:test` suite after the broader Raylib Wasm surface is
   considered complete.
3. Keep scanning for reintroduced backend branches or native-access flags before
   merging:

```sh
rg -n -- "backend/wasm\?|backend/panama\?|backend/current|vybe\.native\.backend|vybe\.native\.loader|--enable-native-access|native-access" src test bin build.clj deps.edn || true
```

## 2026-04-26 Generated C Wasm Runtime Completion

This stage completed the `vybe.c` generated-native path as Wasm for the focused
coverage that was still failing.

Implemented pieces:

1. `vc/defn*` generated C modules now link as Wasm-only artifacts with imported
   memory, no dylib fallback, and no native-access JVM flag.
2. Generated C modules run in Chicory compiled mode. Chicory is configured with
   `InterpreterFallback/FAIL`, so these paths do not silently fall back to the
   interpreter.
3. Generated modules now include ABI-derived callback import pools for C function
   pointer fields. Runtime Clojure callbacks are registered as Wasm table indices,
   not native function pointers.
4. `vybe.wasm` now tracks allocation high-water marks so separate generated C
   modules that share memory reserve past existing Wasm allocations before their
   Emscripten `malloc` is used.
5. Generated C memory imports are compiled with a 1GB maximum so the same module
   can link into Flecs' larger Wasm memory.
6. Wasm component arrays now support `(arr values Component)` for component maps,
   and generated C struct output preserves explicit `[:padding]` fields so wasm32
   C layouts match `vybe.wasm` layouts.
7. `vybe.c-test` now exercises the C DSL with `vybe.wasm` component layouts and
   Wasm arrays where runtime pointers are required.
8. Generated C `printf`/`print`/`println` currently compile to no-op expressions.
   This avoids broken WASI stdout/data-segment interactions while keeping the C
   computation path Wasm-only. Restoring real generated-C stdout should be handled
   as a separate host import instead of routing through libc/WASI varargs.

Verification run in this stage:

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.c-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.c-test))] (println r) (shutdown-agents) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 4 tests containing 11 assertions.
0 failures, 0 errors.
```

```sh
clj -M:wasm:test test/vybe/flecs_test.clj
```

```text
11 tests, 26 assertions, 0 failures.
```

```sh
clj -Sdeps '{:paths ["src" "resources" "target/classes" "test" "test-resources"] :deps {nubank/matcher-combinators {:mvn/version "3.9.1"}}}' \
  -M:wasm -e '(require (quote vybe.jolt-test) (quote vybe.game-test) (quote clojure.test)) (let [r (clojure.test/run-tests (quote vybe.jolt-test) (quote vybe.game-test))] (println r) (shutdown-agents) (when (pos? (+ (:fail r) (:error r))) (System/exit 1)))'
```

```text
Ran 5 tests containing 7 assertions.
0 failures, 0 errors.
```

`noel` real app smoke from `~/dev/vybe-games`:

```sh
clj -J-XstartOnFirstThread -M:dev -m noel
```

The bounded runner kept the app alive for 30 seconds and terminated it:

```text
NOEL_STATUS=alive-for-30s-terminated
```

Source policy scans after this stage:

```sh
rg -n -- "backend/wasm\?|backend/panama\?|backend/current|vybe\.native\.backend|vybe\.native\.loader|--enable-native-access|native-access" src test bin build.clj deps.edn || true
```

No matches.

```sh
find src-java -type f \( -path '*org/vybe/flecs*' -o -path '*org/vybe/jolt*' -o -path '*org/vybe/raylib*' -o -path '*org/vybe/netcode*' \) | sort | head -200
```

No files found.

Lint after this stage:

```sh
clj-kondo --lint src src-java test build.clj
```

Observed repository baseline remains:

```text
errors: 86, warnings: 263
```

## 2026-04-26 Full Wasm Suite And Noel Recheck

After the generated C Wasm runtime fixes, the broader Wasm test profile now
passes, not just the focused Flecs/Jolt/game slices.

Verification:

```sh
clj -M:wasm:test
```

Observed result:

```text
35 tests, 77 assertions, 0 failures.
```

The full run covers:

1. Flecs Wasm, including `defsystem-c` callbacks.
2. Jolt Wasm focused physics coverage.
3. Raylib/game Wasm coverage.
4. `vybe.c-test`, including generated C Wasm hot reload, Flecs import calls,
   Raylib import calls, function-pointer callbacks, and the DSP example.
5. Existing audio/network/util tests under the same Wasm profile.

The `noel` real-app smoke was rerun from `~/dev/vybe-games` using the local
Wasm-enabled `vybe` checkout:

```sh
clj -J-XstartOnFirstThread -M:dev -m noel
```

The bounded runner kept the app alive for 30 seconds and then terminated it:

```text
NOEL_STATUS=alive-for-30s-terminated
```

No `noel`, `clj -J-XstartOnFirstThread`, `scsynth`, or SuperCollider process was
left running after cleanup.

Policy checks rerun after the full suite:

```sh
rg -n -- "backend/wasm\?|backend/panama\?|backend/current|vybe\.native\.backend|vybe\.native\.loader|--enable-native-access|native-access" src test bin build.clj deps.edn || true
```

No matches.

A broader scan including `unsupported` only found pre-existing domain text and C
source comments, not backend switches or fallback branches.

Analyzer cleanup:

The Clang analyzer produced root-level `vybe_*.plist` files during generated C
compilation. These were build artifacts, not source, and were removed after the
full suite.

### 2026-04-26 Correction: Raylib Window/Renderer Is Not Complete

The previous `noel` smoke only proved that the JVM game path could initialize and
stay alive with the Wasm-native libraries loaded. It did **not** prove visible
Raylib rendering. The current `raylib.wasm` host functions for GLFW/WebGL imports
were placeholder imports, and returning fake GL object ids is not an acceptable
implementation for a real game window.

Correct technical boundary:

1. `resources/vybe/wasm/raylib.wasm` is built as `PLATFORM_WEB` with
   `GRAPHICS_API_OPENGL_ES2`.
2. That target expects an Emscripten/WebGL host. Chicory provides fast compiled
   Wasm execution, but it does not provide a browser canvas, WebGL context, GLFW
   window, OpenGL driver, or Metal driver.
3. A pure JVM/Swing/Java2D renderer would create a visible window, but it is not
   Raylib-performance-equivalent and is not a faithful OpenGL/WebGL backend.
4. A desktop JVM OpenGL/Metal backend would require native host access or native
   bindings/libraries, which conflicts with the no-dylib/no-native-access goal.
5. The no-project-dylib, performance-class path is therefore a browser/WebGL
   Raylib Wasm host. The browser supplies the native GPU/WebGL implementation;
   the project ships Wasm plus JS host glue, not dylibs.

Required replacement for the placeholder Raylib host:

1. Build a browser-targeted Raylib artifact with Emscripten JS glue, not only the
   standalone Chicory artifact.
2. Generate a generic ABI/RPC description from the existing clang ABI so aggregate
   parameters and returns are packed/unpacked programmatically, not hand-written.
3. Start a local JVM RPC server from `vybe.raylib.c` and open a browser page that
   owns the WebGL canvas and the browser Raylib Wasm instance.
4. Dispatch Raylib calls from the existing `vr.c/*` functions to the browser
   Raylib Wasm instance for graphical APIs, preserving the public Clojure API.
5. Keep Flecs/Jolt/`vybe.c` generated code on Chicory compiled Wasm in the JVM.
6. Treat `noel` as fully Wasm-ready only when the browser WebGL host displays the
   real game scene, not just when process initialization succeeds.

Validation command must change from a bounded headless smoke to an actual visual
browser run. The command can still start from `~/dev/vybe-games`, but it must
launch the browser/WebGL Raylib host and verify that the page connects to the JVM
RPC server and renders frames.

### 2026-04-26 Raylib Browser/Wasm Desktop Window

Implemented a real desktop window path for Raylib Wasm using an app-mode browser
window as the GPU/WebGL host. This is the only path found so far that satisfies
all of these constraints together:

1. No project dylib for Raylib.
2. No `--enable-native-access`.
3. Real GPU-backed rendering instead of a fake GLFW/OpenGL host.
4. Performance class comparable to Raylib Web builds because the browser owns
   WebGL and Raylib runs as optimized Emscripten Wasm.

New browser build command:

```sh
bin/build-raylib-browser-wasm.sh
```

Generated artifacts:

```text
resources/vybe/wasm/browser/raylib.js
resources/vybe/wasm/browser/raylib.wasm
```

Desktop window command:

```sh
bin/run-raylib-browser-window.sh
```

Observed verification:

```text
Raylib browser window: http://127.0.0.1:8787/vybe/wasm/browser/raylib-host.html
Chrome debug: http://127.0.0.1:9227/json
```

CDP verification against the live window:

```clojure
{:module true
 :ready 1
 :fps 60
 :status "running raylib.wasm in browser WebGL - frame 30"}
```

Visual verification:

```text
target/raylib-browser-host-final.png
```

The screenshot shows Raylib-rendered text, rectangle, animated circle, and
Raylib `DrawFPS` reporting 60 FPS in the desktop app-mode browser window.

Important build finding:

Raylib Web rendering breaks with `-flto` in this setup. The failure mode was
either an out-of-bounds trap in the render batch path or a clear-only frame with
no shapes/text. Removing `-flto` while keeping `-O3` fixed the renderer. The
browser Raylib build must stay no-LTO unless a later Emscripten/Raylib
combination proves otherwise.

The browser build now links `raudio.c` with `SUPPORT_MODULE_RAUDIO`. This removes
Emscripten-generated `missing function`/`.stub = true` abort shims from
`resources/vybe/wasm/browser/raylib.js`; a direct scan returned no matches:

```sh
rg -n "\.stub = true|missing function" resources/vybe/wasm/browser/raylib.js
```

Current boundary:

The standalone Chicory Raylib module is still not a real graphics backend for
desktop rendering because it cannot provide browser WebGL/GLFW. It should not be
used as evidence of visible Raylib rendering. The working visible path is the
browser/WebGL Raylib Wasm artifact above.

Remaining work before claiming full `noel` rendering:

1. Replace direct per-call JVM graphics execution with a generated browser
   Raylib command bridge that preserves the existing `vr.c/*` API.
2. Generate aggregate packing/unpacking from the existing Clang ABI, not by
   handwritten per-function tables.
3. Batch draw commands per frame so Noel does not pay one local RPC round trip
   per Raylib call.
4. Keep CPU-side Flecs/Jolt/`vybe.c` Wasm on the existing compiled Chicory path.
5. Treat `~/dev/vybe-games` Noel as complete only when the browser window renders
   the actual game scene, not just this Raylib smoke scene.

### 2026-04-26 Noel Real Rendering Verification

The Raylib browser/Wasm path now renders real GLB content, not stubs:

1. `minimal` was run through a synchronous harness that starts `vr/-main`,
   calls `minimal/init`, captures the browser canvas through CDP, and exits.
   Result: `target/minimal-harness-canvas.png` shows the GLB cube rendered by
   the normal Flecs `vg/draw-scene` path.
2. `noel.glb` was loaded from `~/dev/vybe-games` and rendered with the same
   Raylib browser/Wasm backend. Result:
   `target/noel-manual-camera-canvas.png` shows real Noel scene geometry and
   materials.
3. The full `noel/init` draw loop no longer crashes on the previous
   Wasm/Panama clone bug. Its captured frame was
   `target/noel-harness-canvas.png`; the frame is dark because the initial
   player camera path starts in a dark/occluded view, but the GLB rendering path
   itself is verified by the forced-camera scene render.

Fixes made while validating Noel:

1. `vybe.raylib` still uses `vp/defcomp`, but Raylib public components are now
   ABI-backed so layouts match generated C/Wasm layouts.
2. `vybe.raylib.c` maps generated C aggregate types back to the public
   `vybe.raylib/*` component vars when those components are registered. This
   fixed Flecs scene queries that previously stored private ABI component
   identities and returned zero rows for `vr/Mesh`/`vr/Material`.
3. `vp/clone` now supports Wasm-backed component maps. This fixed Noel's
   color-id shader path, which clones `Shader` and `Color` values before
   temporary material mutation.
4. Generated browser artifacts are ignored:
   `resources/vybe/wasm/**/*.wasm`,
   `resources/vybe/wasm/browser/raylib.js`, and
   `resources/vybe/wasm/browser-demo/raylib-demo.js`. The durable source of
   the Emscripten WebGL framebuffer patch is
   `bin/build-raylib-browser-wasm.sh`, not manual edits to generated JS.

Useful verification commands:

```sh
# Run the actual Noel app from the game repo. This opens the desktop browser
# Raylib window through the Wasm backend.
cd ~/dev/vybe-games
clojure -M:dev -m noel
```

```sh
# Rebuild the generated browser Raylib artifacts.
cd ~/dev/vybe
bin/build-raylib-browser-wasm.sh
```

Process cleanup after bounded verification runs:

```sh
cd ~/dev/vybe
for f in target/raylib-browser-window.chrome.pid target/raylib-browser-window.pid target/noel.pid; do
  if [ -f "$f" ]; then
    pid="$(cat "$f" 2>/dev/null || true)"
    if [ -n "$pid" ]; then kill -9 "$pid" 2>/dev/null || true; fi
  fi
done
pkill -9 -f '[s]csynth' || true
pkill -9 -f '[c]lojure.main -m noel' || true
```

### 2026-04-26 Noel Desktop Verification Update

Verified on the Wasm-only branch:

- `clj -M:test test/vybe/flecs_test.clj` passes (`11 tests`, `26 assertions`).
- `clj -M:test :unit --focus vybe.game-test` passes (`2 tests`, `2 assertions`).
- Raylib Wasm opens a desktop browser-hosted window and renders real 3D GLTF content to the default framebuffer. Evidence captures:
  - `target/noel-manual-camera-canvas.png`
  - `target/noel-camera-b-active.png`
- Flecs nested query use in Noel no longer traps: query chunks are materialized, the Flecs iterator is finalized/freed, and the Clojure body then runs outside the active iterator. This preserves nested `with-query` use such as camera query + scene query.
- Flecs `event!` now builds `ecs_event_desc_t` and event payloads inside Flecs Wasm linear memory before calling `ecs_enqueue`; native JVM memory addresses are not passed into Flecs Wasm.
- Jolt contact callbacks now read `Body` and `ContactManifold` pointers through the Jolt Wasm module instead of relying on the process-global default module.
- Raylib browser CDP calls are serialized to avoid `Send pending` failures under Noel's heavy draw loop.

Superseded blocker note:

- Raylib Wasm renders 2D content to `RenderTexture2D` correctly (`target/rt-simple-probe.png`).
- Raylib Wasm renders 3D Noel content to the default framebuffer correctly (`target/noel-manual-camera-canvas.png`, `target/noel-camera-b-active.png`).
- This stage originally found that 3D content drawn between `BeginTextureMode`
  and `EndTextureMode` was dark in the browser-hosted path. The next section
  supersedes this: `VyLoadRenderTexture` plus browser frame batching make the
  3D render-texture path visible.

Next implementation target from this older checkpoint:

1. Keep validating Noel from the real browser-hosted desktop window.
2. The command for manual verification remains:

```sh
cd ~/dev/vybe-games
clojure -M:dev -m noel
```

### 2026-04-26 Noel Blink Fix And Current Desktop Status

The visible browser window blink was not a Raylib shader problem, not a fake
renderer, and not a WebGL context reload. It was caused by the browser bridge
submitting each Raylib call as its own Chrome DevTools evaluation. Chrome could
present the canvas between `ClearBackground` and later draw calls, so some real
screen frames were only the clear color while later frames contained the HUD or
scene.

Implemented fix:

1. `resources/vybe/wasm/browser/raylib-host.html` now exposes
   `window.vybeRaylibBridge.callBatch(specs)`, which executes a vector of
   generated ABI call specs in one browser JS task.
2. `src/vybe/raylib/browser.clj` now batches from `BeginDrawing` to
   `EndDrawing`:
   - void drawing calls are queued locally;
   - normal input/math/query calls that return values can still run immediately;
   - framebuffer-dependent readbacks (`rlReadTexturePixels`,
     `rlGetMatrixModelview`, `rlGetMatrixProjection`) force a flush before the
     read, then reopen the batch for the rest of the frame;
   - `EndDrawing` flushes the queued frame with `callBatch`.
3. This keeps existing `vr.c/*`, `vg/with-drawing`, and Noel code paths intact;
   there is no dylib fallback and no `backend/wasm?` branch.

Frame-capture verification after the fix:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 1  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 2  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 3  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 4  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 5  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 6  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 7  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 8  :bytes 10131 :sha256 11bcb5bdac79fe54f8de39fa257ce0071770190dab48a16c0e31f242e9357b96
:frame 9  :bytes 10131 :sha256 11bcb5bdac79fe54f8de39fa257ce0071770190dab48a16c0e31f242e9357b96
:frame 10 :bytes 10131 :sha256 11bcb5bdac79fe54f8de39fa257ce0071770190dab48a16c0e31f242e9357b96
:frame 11 :bytes 10131 :sha256 11bcb5bdac79fe54f8de39fa257ce0071770190dab48a16c0e31f242e9357b96
```

Before batching, the same capture included clear-only frames of 9545 bytes. The
clear-only frames disappeared after batching; the remaining hash change is just
HUD/FPS content changing, not a full-frame blank.

Render-texture status is also updated. The previous blocker is fixed:

1. `VyLoadRenderTexture` is now provided from `bin/vybe_raylib_extra.c` and
   exported through `bin/vybe_raylib_extra.h`.
2. `src/vybe/raylib/c.clj` routes `load-render-texture` to the generated
   `VyLoadRenderTexture` wrapper.
3. The browser Raylib build exposes the wrapper in generated ABI and JS/Wasm;
   no generated JS was hand-edited.
4. `target/noel-direct3d-rt.png` now captures a visible 3D Noel scene rendered
   through `BeginTextureMode`/`EndTextureMode` and blitted to the canvas.

Noel visual verification:

```text
target/noel-direct3d-rt.png             53K, visible 3D RT scene
target/noel-force-camera-canvas.png     54K, visible full Noel draw loop with Camera.001 active
target/noel-frame-00.png                stable default-loop frame after batching, no clear-only blink
```

Camera finding:

- The accidental change in `vybe.game.system/update-camera` that used the global
  transform translation as the `vt/Camera` position was reverted to the
  `origin/main` convention: local `vt/Translation` is stored in the camera while
  target/up are derived from the global transform.
- The renderer is verified with the overview `Camera.001`. The default Noel
  active camera (`:vg.gltf/Camera`) currently starts on a dark/occluded
  first-person view after `default-systems` run. This is not a Raylib/Wasm
  render failure: the same app frame becomes visible when Noel switches to
  `Camera.001` (the game already has Space-key camera switching), and the
  forced-camera harness captures real geometry.

Validation commands run after this stage:

```sh
clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

clj-kondo --lint src src-java test
# baseline before this stage: errors: 86, warnings: 263
# after this stage:           errors: 86, warnings: 262
```

Manual desktop run remains:

```sh
cd ~/dev/vybe-games
clojure -M:dev -m noel
```

If the default first-person camera opens on the dark view, press Space to switch
to `Camera.001`; the Wasm Raylib desktop window renders the real Noel scene
there. The next game-level task is to decide whether Noel should default to the
overview camera or adjust the player camera initial transform, but that is now a
scene/camera state decision rather than a missing Wasm Raylib backend.

### 2026-04-26 Whole-Frame Raylib Batching And Noel Throughput Fix

The first blink fix batched only calls between `BeginDrawing` and `EndDrawing`.
Noel renders most of its shadow maps, render textures, shader bypass passes, and
scene passes before the final `vg/with-drawing` block, so those offscreen Raylib
calls were still crossing Chrome DevTools one call at a time.

Implemented follow-up fixes:

1. `src/vybe/raylib/browser.clj` now supports explicit whole-frame batches with
   `begin-frame!` and `end-frame!`.
2. `src/vybe/raylib/c.clj` exposes `begin-frame-batch!` and
   `end-frame-batch!` for the Raylib main loop.
3. `src/vybe/raylib.clj` wraps each `draw` invocation in that frame batch and
   flushes in `finally`, so exceptions do not leave a browser-side batch open.
4. `BeginDrawing` no longer resets an already-active batch. This is required so
   offscreen render-texture work queued earlier in the same frame is not dropped.
5. `resources/vybe/wasm/browser/raylib-host.html` caches `Module.cwrap` wrappers
   by wrapper name, return type, and argument count, and records batch timing in
   `window.vybeRaylibLastBatch`.
6. `src/vybe/raylib/browser.clj` caches the bridge-ready state after the host is
   ready. This removes the previous per-Raylib-call DevTools readiness check.
7. `src/vybe/raylib/c.clj` preserves existing local functions when generating
   missing Raylib exports, instead of overwriting local raymath helpers with
   browser calls.
8. `src/vybe/raylib/c.clj` now keeps deterministic browser-host values local for
   screen size, frame clock, and shader uniform locations. Several pure raymath
   helpers also stay local: vector add/scale/multiply/cross/normalize,
   vector-rotate-by-quaternion, and color-normalize.

Measured bridge effect on the same Noel 3-second sample:

```text
Before whole-frame batching:
:frames 12
:eval-counts {:stats 2, :immediate-call 3052, :batch 12}

After whole-frame batching:
:frames 38
:eval-counts {:immediate-call 990, :stats 2, :batch 38}

After shader-location cache and local raymath preservation:
:frames 40
:eval-counts {:stats 2, :immediate-call 278, :batch 40}
:last-stat {:wrappedFns 47, :count 153, :sequence 99, :elapsedMs 1.9}
```

The remaining performance gap is no longer thousands of DevTools calls. The
heavy path is now serialization of large draw specs, especially repeated
`DrawMesh` calls carrying `Mesh`, `Material`, and `Matrix` aggregate values into
the browser host. To get closer to native Raylib performance, the next required
optimization is a handle/shared-memory draw path for persistent Raylib resources
instead of JSON-serializing full aggregate structs every frame.

Blink verification after whole-frame batching:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 1  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 2  :bytes 11003 :sha256 9afd0f0463c4df8b07bf7a4ed6ec78cd0171af8a0793f4ef50d7fc094fa8b01b
:frame 3  :bytes 10141 :sha256 f79dbaf3cf84f4fb90eafbd5b0e7c7e0567b2819f2a9ca9ac9eab59afd44a52a
:frame 4  :bytes 10960 :sha256 d49be90578875c7f1dc70bf3eae18814918f25d35bcac609d3dcd84b9664fde4
:frame 5  :bytes 10131 :sha256 4d5a0ae506176ec6ef8f56108ff8d480416ddd86e704f2800d7d0a8b018c5cfa
:frame 6  :bytes 10960 :sha256 d49be90578875c7f1dc70bf3eae18814918f25d35bcac609d3dcd84b9664fde4
:frame 7  :bytes 10131 :sha256 4d5a0ae506176ec6ef8f56108ff8d480416ddd86e704f2800d7d0a8b018c5cfa
:frame 8  :bytes 10131 :sha256 4d5a0ae506176ec6ef8f56108ff8d480416ddd86e704f2800d7d0a8b018c5cfa
:frame 9  :bytes 11034 :sha256 d1e16fc450286423cf06abb9fceda94d08ef33bc7b8efc575fbb6d20679cb5d7
:frame 10 :bytes 10131 :sha256 4d5a0ae506176ec6ef8f56108ff8d480416ddd86e704f2800d7d0a8b018c5cfa
:frame 11 :bytes 11022 :sha256 e6561f682733336a22a4e87f7bc744f515536004395fe4ce4c07a64387cbf7f2
```

There are no clear-only `9545` byte frames in this capture. The changing frame
hashes are Noel UI/time changes, not a blank-screen flicker.

Current validation after this update:

```sh
clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

clj-kondo --lint src src-java test
# baseline before this update: errors: 86, warnings: 262
# after this update:           errors: 86, warnings: 247
```

Visible Noel 3D verification after this update:

```text
target/noel-force-camera-canvas.png 54K, visible 3D scene with Camera.001 active
```

The manual command is unchanged:

```sh
cd ~/dev/vybe-games
clojure -M:dev -m noel
```

### 2026-04-26 Raylib Browser Bridge Compact ABI And Blink Follow-Up

The remaining visible blink report was investigated against the real Noel app in
`~/dev/vybe-games`, not a stub. The confirmed issue was still bridge submission
shape, not missing Raylib rendering: browser-side execution is fast, but the JVM
was presenting large batches through Chrome DevTools and earlier builds could
split a logical frame around matrix readbacks.

Implemented in this pass:

1. Raylib aggregate layouts are installed into the browser host once with
   `window.vybeRaylibBridge.installLayouts(...)`. Per-call specs now send a
   layout name instead of a full layout map.
2. The browser bridge accepts compact vector specs instead of verbose maps:
   `[name ret args]`, with compact arg tags (`"n"`, `"a"`, `"s"`, `"b"`).
3. Aggregate arguments are sent as ABI-ordered flat value vectors. The host writes
   them through the generated ABI layout, avoiding repeated nested field names in
   every frame payload.
4. Immutable Raylib resource aggregates (`RenderTexture`, `Texture`, `Shader`,
   `Color`, `Rectangle`) are cached on the Clojure bridge side. Mutable
   `Material` is intentionally not cached because Noel mutates shader/material
   state.
5. `rlGetMatrixModelview`, `rlGetMatrixProjection`, and cull-plane reads are now
   local Raylib-equivalent calculations while a camera mode is active. They were
   verified against the live Wasm/Raylib values within float epsilon. This removes
   mid-frame readback flushes.
6. `matrix-multiply` and its C-emission path were corrected to match Raylib's
   `MatrixMultiply`. This is required because preserving local raymath helpers
   for speed must not change native behavior.
7. A local `VyGetScreenToWorldRay` implementation was tested and matched the
   Wasm result, but it was not retained because the matrix inversion work reduced
   Noel throughput. The direct Wasm call remains for that path.

Current measured bridge behavior on the same Noel 3-second timing sample:

```text
After one-batch-per-frame matrix readback removal, before compact specs:
:batch avg bytes ~80354
:EndDrawing avg ~26.7 ms
:last browser batch elapsed ~1.7-2.4 ms

After compact vector specs:
:batch avg bytes ~51623
:EndDrawing avg ~25.9 ms

After flat aggregate value vectors and resource layout caching:
:batches 15
:last-stat {:wrappedFns 43, :layouts 47, :count 249, :sequence 37, :elapsedMs 1.9}
:eval-stats {:immediate-call {:count 195, :avg-bytes 155}
             :batch {:count 15, :avg-bytes 34351, :avg-ms 4.33}}
:top-call-ms includes DrawTextureRec, BeginTextureMode, ClearBackground,
SetShaderValue, VyGetScreenToWorldRay, SetShaderValueMatrix as the remaining
hot paths.
```

Blink verification after the compact/flat bridge:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 1  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 2  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 3  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 4  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 5  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 6  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 7  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 8  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 9  :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 10 :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
:frame 11 :bytes 10109 :sha256 60ced4681399b5dd067c38139e3828e5410a1ca5eeede60f11fc55c7a731c11d
```

The forced overview camera still renders real 3D content through the Wasm Raylib
path:

```text
target/noel-force-camera-canvas.png, visible 3D scene, FPS text around 7 FPS
```

Validation after this pass:

```sh
clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

clj-kondo --lint src/vybe/raylib/browser.clj src/vybe/raylib/c.clj src/vybe/raylib.clj
# 0 errors, 10 existing warnings

clj-kondo --lint src src-java test
# errors: 86, warnings: 241
```

Current desktop command remains:

```sh
cd ~/dev/vybe-games
clojure -M:dev -m noel
```

Remaining performance work to approach native Raylib:

1. The browser executes each 249-call Noel batch in roughly 2 ms, but Clojure
   still builds hundreds of draw specs per logical frame. The next required step
   is a persistent command-buffer/handle protocol so Noel can submit resource
   handles and draw command opcodes instead of rebuilding aggregate specs for
   every texture, render target, shader uniform, and GUI draw.
2. `DrawTextureRec`, `BeginTextureMode`, `ClearBackground`, `SetShaderValue`,
   `VyGetScreenToWorldRay`, and `SetShaderValueMatrix` are the current hot paths.
   Optimize these as concrete bridge commands first rather than adding broad
   compatibility layers.
3. Keep `Material` uncached unless mutations are mirrored into browser Wasm
   memory. Caching it would be incorrect for Noel's shader/material path.
4. Do not reintroduce dylib fallbacks or `backend/wasm?` branches. The path stays
   Wasm-only.

### 2026-04-26 Noel Visibility, Input, And Playability Follow-Up

The latest Noel run is rendering the real GLTF scene through the Wasm Raylib
path. A fresh capture from `~/dev/vybe-games` shows the room, cursor, and FPS
counter in `target/noel-frame-00.png`; the window is not a stub and the model is
not missing at startup.

Confirmed fixes in this pass:

1. `ecs_progress` now passes `delta_time` as raw f32 bits into Flecs Wasm. The
   previous f64 bit path corrupted system delta time and pushed Jolt kinematic
   bodies toward invalid positions.
2. `update-physics-ongoing` passes the stable `vt/Rotation` component pointer to
   Jolt `MoveKinematic` instead of taking the address of a temporary quaternion.
   This keeps body rotations finite and matches the entity transform.
3. The VybeC generated-system cache version was bumped so cached C-system Wasm
   is invalidated after the emitter/runtime ABI changes.
4. The browser Raylib host now records keyboard and mouse state locally and
   exposes a once-per-frame input snapshot. `vybe.raylib.c` uses that snapshot
   for `is-key-down`, `is-key-pressed`, mouse-button checks, mouse position,
   mouse delta, and focus checks instead of crossing the browser bridge for each
   input query.
5. Generated Wasm artifacts and generated VybeC `.plist` files are ignored in
   `.gitignore`; the generated `raylib.js` remains treated as an output artifact,
   not a source file to edit.

Fresh Noel checks:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0 :bytes 164414 :sha256 5787e72845b7119dc6c89a63f85a303c1c7b48536a308ca3a2ee7a34364e281a
```

The input path is also live. A synthetic `W` key hold moved the player collider
from approximately `[0.87, 5.02, 1.00]` to `[-16.48, 5.02, -16.96]`; this means
keyboard input is reaching the Wasm Raylib window. The black view seen after that
probe was caused by moving the first-person camera outside the room, not by a
missing model.

Current playability status:

1. The game is visible and accepts input.
2. It is not yet at the target playable/native-like framerate. The current Noel
   capture still reports roughly `13 FPS`, and deep profiling still shows the
   frame dominated by Clojure-side draw construction, Flecs progress, shader
   uniform setup, and browser bridge submission rather than browser-side Wasm
   execution.
3. The next performance milestone is still a persistent command-buffer/handle
   protocol for Raylib draws and shader uniforms. The browser is executing the
   final batch quickly; the remaining avoidable cost is building and serializing
   hundreds of high-level draw specs every frame.

Next concrete performance work:

1. Add browser-side persistent handles for `Shader`, `Material`, `Texture`, and
   `RenderTexture`, with explicit mutation/sync commands for mutable material
   state. This avoids rebuilding aggregate draw arguments every frame while still
   preserving Noel's shader/material mutations.
2. Add compact opcodes for the hot draw calls (`DrawMesh`, `DrawTextureRec`,
   `SetShaderValue`, `SetShaderValueMatrix`, `BeginTextureMode`,
   `ClearBackground`) instead of generic per-call layout specs.
3. Keep input on the once-per-frame snapshot path and do not reintroduce
   `backend/wasm?`, dylib fallback branches, generated jextract Java bindings,
   or edits to generated `resources/vybe/wasm/browser/raylib.js`.

Post-snapshot validation:

```text
clj-kondo --lint src/vybe/raylib/browser.clj src/vybe/raylib/c.clj src/vybe/raylib.clj src/vybe/game.clj src/vybe/game/system.clj src/vybe/flecs/wasm_c.clj src/vybe/math.clj
# errors: 0, warnings: 52

clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures
```

Latest profiled Noel sample after the input snapshot:

```text
:batches 22
:last-stat {:wrappedFns 40, :layouts 47, :count 249, :sequence 54, :elapsedMs 1.7}
:noel/draw avg 134.63 ms
:vybe.game/-with-fx avg 10.12 ms
:vybe.raylib.browser/call! avg 0.153 ms, count 5823
:vybe.game/draw-lights avg 19.51 ms
:vybe.flecs/progress avg 36.00 ms
:vybe.game/set-uniform avg 0.371 ms, count 2013
:noel/raycasted-entity avg 2.21 ms
```

The full project lint baseline remains noisy because of existing Overtone macros,
experimental namespaces, and generated-symbol analysis gaps:

```text
clj-kondo --lint src src-java test
# errors: 86, warnings: 234
```

Post-compact-uniform validation:

```text
Noel capture still renders the same visible startup frame:
:frame 0 :bytes 164414 :sha256 5787e72845b7119dc6c89a63f85a303c1c7b48536a308ca3a2ee7a34364e281a

Latest profiled sample:
:batches 22
:last-stat {:wrappedFns 40, :layouts 47, :count 249, :sequence 54, :elapsedMs 1.6}
:noel/draw avg 135.25 ms
:vybe.game/set-uniform avg 0.362 ms, count 2005
:vybe.raylib.c/set-shader-value avg 0.138 ms, count 608
:vybe.raylib.c/set-shader-value-matrix avg 1.205 ms, count 45

clj-kondo focused touched-files lint:
# errors: 0, warnings: 52

clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures
```

The compact uniform payload is correct, but the measured improvement is small.
Do not spend more time on base64-sized micro-optimizations before adding the
persistent handle/opcode path; the browser batch itself is already around
`1.6 ms`, while the Clojure frame remains around `135 ms` under profiling.

Latest full-project lint baseline after the compact-uniform change:

```text
clj-kondo --lint src src-java test
# errors: 86, warnings: 234
```

### 2026-04-26 Raylib Browser ABI Alias Fix And Raycast Decision

This pass found and fixed a real app-loop issue in the browser Raylib bridge:
`QuaternionToAxisAngle` writes to a `Vector3 *` output argument, but Noel passes a
`vybe.type/Vector3` component for that pointer. The browser serializer was using
the component's internal generated name (`C_vybe_DOT_type_SLASH_Vector3`) as a
Raylib ABI layout name, which does not exist in `raylib_abi.edn`. The fix is a
small layout-alias table in `vybe.raylib.browser` for layout-compatible Vybe
components:

```text
vybe.type/Vector2      -> Raylib Vector2
vybe.type/Vector3      -> Raylib Vector3
vybe.type/Translation  -> Raylib Vector3
vybe.type/Velocity     -> Raylib Vector3
vybe.type/Scale        -> Raylib Vector3
vybe.type/Vector4      -> Raylib Vector4
vybe.type/Rotation     -> Raylib Vector4
vybe.type/Matrix       -> Raylib Matrix
vybe.type/Transform    -> Raylib Matrix
```

This is not a dylib fallback or compatibility backend. It is a Wasm bridge
serialization fix that maps existing Vybe components onto the generated Raylib
Wasm ABI layouts when the memory layouts are equivalent.

A local Clojure implementation of `VyGetScreenToWorldRay` was tested against the
actual generated Wasm export. It was numerically correct, but too slow for the
Noel hot path, so it was removed and the generated Wasm export is kept.

Correctness comparison before removing the local version:

```text
:local {:position {:x 0.8712661, :y 5.0166025, :z 1.0041571},
        :direction {:x -0.68949807, :y 6.294749E-6, :z -0.7242875}}
:wasm  {:position {:x 0.8712661, :y 5.0166025, :z 1.0041571},
        :direction {:x -0.689494, :y -1.2841762E-5, :z -0.7242914}}
:max-diff 1.9136511127726408E-5
```

Performance comparison:

```text
Local Clojure ray math:
:noel/draw avg 332.01 ms
:vybe.raylib.c/vy-get-screen-to-world-ray avg 36.95 ms
:batches 9

Generated Wasm ray export restored:
:noel/draw avg 140.92 ms
:vybe.raylib.c/vy-get-screen-to-world-ray avg 0.93 ms
:batches 21
```

The decision is to keep ray math inside the generated Raylib Wasm module. The
next optimization should not be a Clojure reimplementation of Raylib math; it
should be a lower-overhead browser command-buffer/handle protocol that reduces
per-frame Clojure serialization and bridge crossings while still executing the
native-equivalent code in Wasm.

Fresh Noel render capture after the alias fix and Wasm ray restoration:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0 :bytes 164435 :sha256 ef76551a042da8ff8af850b7ad68575d5860d62643070d805d68699d01165c35
```

Fresh Noel input probe after the alias fix:

```text
:ready {:ready true, :focused true}
:before {:x 0.8712661, :y 5.0166025, :z 1.0041571} :key-before false
:during {:x -14.599105, :y 5.0166025, :z -15.020096} :key-during true
:after  {:x -15.974495, :y 5.0166025, :z -16.395485} :key-after false
```

Validation for this pass:

```text
clj-kondo --lint src/vybe/raylib/c.clj src/vybe/raylib/browser.clj src/vybe/raylib.clj src/vybe/game.clj src/vybe/game/system.clj src/vybe/flecs/wasm_c.clj src/vybe/math.clj
# errors: 0, warnings: 52
# Runtime confirmed vybe.raylib.c/vy-get-screen-to-world-ray remains a generated
# Wasm var with :vybe/wasm-fn metadata after removing the slow local Clojure
# implementation. The intern loop now handles declared generated vars without
# hiding them from clj-kondo.

clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures
```

### 2026-04-26 Compact Raylib Function Plans And Uniform Upload Cache

This pass added two Wasm-only performance changes that do not introduce dylib
fallbacks, `backend/wasm?`, generated jextract files, or edits to generated
`raylib.js`.

1. Raylib browser calls now install ABI-derived function plans once in the host
   page. Normal calls send compact `[function-id, encoded-args]` specs instead
   of repeating `[function-name, return-spec, encoded-args]` on every call. The
   ids are generated from `raylib_abi.edn`; there is no hand-written list of
   Raylib functions.
2. `vybe.game/set-uniform` now caches scalar and small vector uniform uploads by
   `[shader-id shader-locs uniform-name]`. Repeated identical scalar/vector
   values are skipped. Matrix uniforms and sequence expansion are still always
   evaluated so animated joints, light view-projection matrices, and dynamic
   arrays remain correct.

A broader sequence-level uniform cache was tested and rejected. It reduced some
recursion but made `set-uniform` slower in the Noel profile, so that layer was
removed and only the measured scalar/vector upload cache remains.

Latest Noel profile with the kept changes:

```text
:batches 21
:last-stat {:functions 785, :wrappedFns 40, :layouts 47, :count 226, :sequence 53, :elapsedMs 1.4}
:noel/draw avg 137.75 ms
:vybe.raylib.browser/call! count 5098, avg 0.152 ms
:vybe.game/set-uniform count 1946, avg 0.339 ms
:vybe.raylib.c/set-shader-value count 86, avg 0.089 ms
:vybe.raylib.c/set-shader-value-matrix count 44, avg 1.263 ms
:vybe.raylib.c/vy-get-screen-to-world-ray count 106, avg 0.867 ms
```

Comparison to the previous profiled baseline:

```text
Before:
:vybe.raylib.browser/call! count 5545
:vybe.raylib.c/set-shader-value count 581
:last-stat :count 249

After:
:vybe.raylib.browser/call! count 5098
:vybe.raylib.c/set-shader-value count 86
:last-stat :count 226
```

Fresh Noel render capture after this pass:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0 :bytes 164414 :sha256 5787e72845b7119dc6c89a63f85a303c1c7b48536a308ca3a2ee7a34364e281a
```

Validation for this pass:

```text
clj-kondo --lint src/vybe/raylib/c.clj src/vybe/raylib/browser.clj src/vybe/raylib.clj src/vybe/game.clj src/vybe/game/system.clj src/vybe/flecs/wasm_c.clj src/vybe/math.clj
# errors: 0, warnings: 52

clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

clj-kondo --lint src src-java test
# errors: 86, warnings: 234
```

The next useful step is to move beyond generic compact specs and add persistent
browser-side resource handles/opcodes for the hot Raylib draw paths. The current
browser batch execution remains low (`~1.3-1.5 ms`); the remaining gap is still
mostly Clojure-side scene/light/uniform construction plus Flecs progress.

### 2026-04-26 Raylib Hot-Path Opcode And ABI Layout IDs

This pass kept the Raylib path Wasm-only and continued optimizing the Noel
browser-window backend without editing generated `resources/vybe/wasm/browser/raylib.js`.
The host bridge file is the maintained browser shell; `raylib.js` remains a
regenerable Emscripten output.

Kept changes:

1. `DrawTextureRec` now has a compact browser opcode for its validated hot path.
   The Clojure side still calls the same generated `draw-texture-rec` wrapper,
   but the browser spec skips repeated generic metadata and directly writes the
   `Texture`, `Rectangle`, `Vector2`, and `Color` aggregates before invoking the
   Wasm export.
2. Browser ABI layout installation now also assigns generated numeric layout ids
   from `raylib_abi.edn`. Aggregate specs can use ids instead of layout names;
   this is generated from the ABI data, not hand-authored layout tables.
3. The runtime was rechecked: `vybe.wasm.runtime/instantiate` uses Chicory's
   `MachineFactoryCompiler` with `InterpreterFallback/FAIL`, so the loaded
   native-library Wasm modules are compiled mode only. If Chicory cannot compile
   a module, loading fails instead of silently falling back to interpreter mode.

Rejected change:

A broader generated argument-plan compaction was tested where function plans also
carried ABI argument descriptors and calls sent raw compact args. It rendered
correctly but regressed hot call timings (`DrawTextureRec`, `ClearBackground`,
`BeginTextureMode`, and `DrawTexturePro` all became slower), so that change was
removed. The documented implementation keeps only the non-regressive function-id,
layout-id, uniform-cache, and `DrawTextureRec` opcode work.

Latest Noel render capture with the kept changes:

```text
:browser {:ready true, :canvas true, :size [600 600], :status raylib.wasm browser bridge ready}
:frame 0 :bytes 164435 :sha256 ef76551a042da8ff8af850b7ad68575d5860d62643070d805d68699d01165c35
```

The captured frame shows the real Noel room scene, furniture/model geometry,
crosshair, lighting, and FPS overlay. The synthetic input probe also confirmed
that keyboard state is delivered through the browser bridge and moves the camera:

```text
:ready {:ready true, :focused true}
:before {:x 0.8712661, :y 5.0166025, :z 1.0041571} :key-before false
:during {:x -7.162826, :y 5.0166025, :z 8.17824} :key-during true
:after {:x -8.7858925, :y 5.0166025, :z 11.939864} :key-after false
```

Latest hot-call probe after reverting the regressing arg-plan experiment:

```text
:batches 20
:last-stat {:functions 785, :wrappedFns 40, :layouts 47, :count 226, :sequence 55, :elapsedMs 1.3000000715255737}
DrawTextureRec count 243, avg 0.551 ms
ClearBackground count 646, avg 0.176 ms
BeginTextureMode count 405, avg 0.272 ms
EndDrawing count 20, avg 2.716 ms
VyGetScreenToWorldRay count 101, avg 0.510 ms
SetShaderValueMatrix count 40, avg 0.917 ms
DrawTexturePro count 40, avg 0.793 ms
DrawMesh count 1920, avg 0.0068 ms
SetShaderValue count 80, avg 0.083 ms
```

The useful comparison is `DrawTextureRec`: the measured hot path moved from about
`0.65 ms/call` before the opcode to about `0.55 ms/call` after the kept opcode.
The browser batch itself is still low (`~1.3 ms` for the final frame batch), so
the remaining frame-time gap is mostly higher-level scene/light/uniform work and
Flecs progress/callback workload, not missing Wasm compilation.

Validation for this pass:

```text
clj -M -e "(require '[vybe.raylib.browser :as b]) (println :loaded)"
# :loaded

clj-kondo --lint src/vybe/raylib/c.clj src/vybe/raylib/browser.clj src/vybe/raylib.clj src/vybe/game.clj src/vybe/game/system.clj src/vybe/flecs/wasm_c.clj src/vybe/math.clj
# errors: 0, warnings: 52

# Generated browser asset untouched:
git diff -- resources/vybe/wasm/browser/raylib.js | wc -l
# 0
```

Next implementation targets:

1. Add measured browser-side persistent handles for render textures, shaders,
   colors, rectangles, and common vectors. This should be generated from ABI
   layouts and resource ids where possible, not maintained as hand-written C ABI
   tables.
2. Keep using the real Raylib Wasm exports for math and rendering unless a local
   implementation is proven faster and bit-compatible. The local Clojure
   `VyGetScreenToWorldRay` experiment was slower and remains removed.
3. Profile Flecs `progress` by callback/system bucket in Noel. Wasm loading is
   already compiled-only; remaining `progress` cost needs to be attributed to
   actual ECS work before changing behavior.
4. Preserve the no-fallback invariant: no `backend/wasm?`, no dylib fallback, no
   jextract Java files for wasm-migrated libs, and no generated `raylib.js`
   manual edits.

Post-pass validation completed after the kept changes:

```text
clj -M:test test/vybe/flecs_test.clj
# 11 tests, 26 assertions, 0 failures

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

clj-kondo --lint src src-java test
# errors: 86, warnings: 234
```

### 2026-04-26 Noel Wasm Render/Uniform Fix Continuation

This continuation kept the native path Wasm-only and did not edit generated
`resources/vybe/wasm/browser/raylib.js` by hand. The browser `raylib.js` and
`raylib.wasm` outputs were regenerated only through `bin/build-raylib-browser-wasm.sh`
with `LC_ALL=C LANG=C` to avoid the local perl locale failure in the script's
post-link patch step.

Implemented fixes:

1. `vp/clone` now handles Wasm-backed component maps without casting them to a
   Panama `MemorySegment`. This fixed the Noel crash where `vt/Rotation` cloning
   tried to cast `vybe.wasm.WasmPMap` to a memory segment.
2. `systems-debug` can read Flecs query term pointers from the generated Wasm ABI
   layout (`ecs_term_t`) instead of assuming the terms field is already a Clojure
   sequence.
3. Raylib browser bridge hot paths now reuse browser-side scratch aggregates for
   `DrawTextureRec`, `DrawTexturePro`, `DrawMesh`, `ClearBackground`,
   `BeginTextureMode`, and `SetShaderValueMatrix`. The public Clojure call sites
   are unchanged; they still call the same Raylib wrappers.
4. Shader vector/scalar uniform arrays now use real `SetShaderValueV` instead of
   uploading every array element as an independent uniform call.
5. Added real Raylib extra export `VySetShaderValueMatrixV`, backed by
   `glUniformMatrix4fv`, and regenerated the Raylib browser Wasm/ABI. Matrix
   uniform arrays now use this export when a shader receives a sequence of
   `vt/Matrix`/`vt/Transform` values.
6. `component->uniform-type` is now nil-safe for non-vector components, so matrix
   arrays and other aggregate values fall through to the correct path instead of
   throwing during optimization checks.

Validation and measurements:

```text
awk '/<script>/{flag=1; next} /<\/script>/{flag=0} flag {print}' \
  resources/vybe/wasm/browser/raylib-host.html > /tmp/raylib-host-script.js \
  && node --check /tmp/raylib-host-script.js
# OK

clj-kondo --lint src/vybe/raylib/c.clj src/vybe/raylib/browser.clj \
  src/vybe/raylib.clj src/vybe/game.clj src/vybe/game/system.clj \
  src/vybe/flecs/wasm_c.clj src/vybe/math.clj src/vybe/panama.clj
# errors: 0, warnings: 56

clj -M -e "(require '[vybe.game :as vg] '[vybe.raylib.c :as vr.c]) \
  (println (boolean (resolve 'vr.c/vy-set-shader-value-matrix-v)))"
# true
```

Noel browser bridge probe after the uniform-array and hot-opcode fixes:

```text
:batches 21
:last-stat {:functions 786, :wrappedFns 41, :layouts 47,
            :aggregateScratch 7, :count 226, :sequence 59,
            :elapsedMs 2.0}
VySetShaderValueMatrixV count 44, avg 0.882 ms
SetShaderValue count 86, avg 0.079 ms
DrawMesh count 2093, avg 0.0047 ms
DrawTexturePro count 42, avg 0.612 ms
ClearBackground count 694, avg 0.147 ms
BeginTextureMode count 433, avg 0.236 ms
```

Deep Noel profile after this pass:

```text
:noel/draw count 23, avg 131.84 ms
:vybe.game/-with-fx count 115, avg 9.74 ms
:vybe.game/draw-lights count 46, avg 19.45 ms
:vybe.flecs/progress count 22, avg 36.36 ms
:vybe.raylib.browser/call! count 5403, avg 0.140 ms
:vybe.game/set-uniform count 1771, avg 0.296 ms
:vybe.game/draw-scene count 138, avg 0.663 ms
```

Confirmed game behavior:

```text
:browser {:ready true, :canvas true, :size [600 600],
          :status raylib.wasm browser bridge ready}
:frame 0 :bytes 164414 :sha256 5787e72845b7119dc6c89a63f85a303c1c7b48536a308ca3a2ee7a34364e281a

:ready {:ready true, :focused true}
:before {:x 0.8712661, :y 5.0166025, :z 1.0041571} :key-before false
:during {:x -13.253776, :y 5.0166025, :z -13.718001} :key-during true
:after {:x -16.891048, :y 5.0166025, :z -17.355274} :key-after false
```

Remaining measured bottlenecks:

1. Flecs `progress` is still ~36 ms/frame in the Noel sample. The next fix should
   profile per generated C system/callback instead of changing behavior blindly.
2. The real render path still issues about 226 batched Raylib calls per final
   frame. Browser batch execution is low, but Clojure-side call construction and
   scene/light passes remain expensive.
3. `draw-lights` and `with-fx` perform multiple render-texture passes. Further
   gains require reducing pass count or caching pass inputs while preserving the
   current visual behavior.
4. The desktop window path is real and playable through the browser-backed Raylib
   wasm window, but FPS is still around the low teens in the Noel scene on this
   machine. More work is needed before calling it raylib-native-equivalent
   performance.

Invariants preserved:

1. No dylib fallback was added.
2. No `backend/wasm?` compatibility branch was added.
3. No generated jextract Java files were introduced for the wasm-migrated libs.
4. `resources/vybe/wasm/browser/raylib.js` remains a generated output; manual
   edits were made only to `raylib-host.html`, the maintained browser host shell.

## 2026-04-26 Noel Performance Fix Continuation

Validation target: the `noel` game in `/Users/pfeodrippe/dev/vybe-games` running through the wasm Raylib browser-window backend, with no dylib fallback and no generated `raylib.js` manual edits.

### Fixes applied

- Optimized `vybe.game.system/update-camera` by deriving `:position`, `:up`, and `:target` directly from the already available global transform matrix. This preserves the previous vector math but avoids repeated matrix/vector helper calls per camera update.
- Optimized `vybe.audio/sound-sources-update` by removing full matrix multiplication from ambisonic spatialization. The code now computes only the translated source position in target space and caches the target inverse for repeated source updates in the same frame.
- Added exact duplicate suppression for Overtone `ctl` payloads per directional source node. This does not stub audio; it only avoids resending identical control values.
- Optimized shader uniform uploads by batching color-id bypass arrays as normalized vec4 arrays instead of falling back to indexed per-element uploads.
- Added uniform upload caching for matrix arrays, including `lightVPs`.
- Optimized `vybe.raylib.c/matrix-multiply` to read each matrix field once before multiplying. This cuts wasm-backed map reads from 128 to 32 per multiply and materially improves transform/light math.
- Normalized `with-fx` shader lists to remove nil entries before multipass rendering. This avoids a real no-op pass when callers use `(when ...)` inside shader vectors.
- Added a narrow `with-fx` direct-draw fast path for calls with `:drawing true` and no render target, target material, entity capture, or shaders.
- Added a per-frame cache around the real `VyGetScreenToWorldRay` call for repeated same-frame ray queries with identical position/camera inputs.

### Measurements

Measured with `/tmp/vybe_noel_fps_probe.clj` from `/Users/pfeodrippe/dev/vybe-games`:

- Before this continuation: about 10.6 FPS over a 5 second sample.
- After matrix/audio/uniform/with-fx changes: about 14.0 FPS over a 5 second sample.
- Browser-side batch execution remains low, around 1.4-1.7 ms for the last batch, so remaining cost is mostly Clojure-side scene/effect orchestration and call encoding, not wasm execution itself.

Focused profiler changes:

- `vybe.audio/sound-sources-update`: about 9.7 ms/update down to about 0.8 ms/update.
- `vybe.game.system/update-camera`: about 3.4 ms/call down to about 1.4 ms/call.
- `vybe.game/draw-lights`: about 18.6 ms/call down to about 10.9 ms/call.
- `vybe.game/set-uniform`: sample total about 638 ms down to about 342-407 ms depending on instrumentation and frame count.
- `u_color_ids_bypass` uniform path: about 73 ms over the sample down to about 0.4 ms.

### Rendering/Input Validation

- Frame capture script confirmed the browser bridge is ready and canvas is present at 600x600.
- Captured frame: `/Users/pfeodrippe/dev/vybe/target/noel-frame-00.png`.
- The captured frame shows the 3D Noel room/model and FPS overlay; current screenshot displayed 21 FPS at capture time.
- Input probe confirmed keyboard state transitions and player/camera movement:
  - before: `{:x 0.8712661, :y 5.0166025, :z 1.0041571}`
  - during W input: `{:x -9.217507, :y 5.0166025, :z -9.5937195}`
  - after release: `{:x -10.990768, :y 5.0166025, :z -11.456457}`

### Tests Run

- `clj -M:test test/vybe/flecs_test.clj`: 11 tests, 26 assertions, 0 failures.
- `clj -M:test :unit --focus vybe.game-test`: 2 tests, 2 assertions, 0 failures.
- Focused lint on changed areas still reports the existing Overtone/clj-kondo unresolved-symbol baseline, not new syntax failures.

### Remaining Bottleneck

Noel is real and interactive, but not yet Raylib-native-performance. The remaining cost is dominated by high-level render orchestration:

- Multiple `with-fx` render-texture passes per frame.
- Two `draw-lights` calls per frame, each rendering shadow/depth passes.
- Many browser command encodes per frame even though JS-side batch execution is fast.

The next implementation step should be a real render-command encoder that emits compact typed frame commands for common draw/effect/light paths instead of one Clojure map/JSON-ish spec per Raylib call. That keeps the wasm-only design while attacking the actual remaining overhead.

## 2026-04-26 Noel Orientation Fix

Problem observed in the real Noel window: the 2D overlay was upright, but the 3D frame was vertically inverted, and mouse/crosshair interaction felt inverted relative to the visible scene. This ruled out a whole browser canvas flip and pointed to the render-texture presentation path.

### Diagnosis

A focused render-texture probe was added under `/tmp/vybe_flip_probe.clj` to avoid guessing inside Noel. It rendered simple top/bottom color bars directly, then through a render texture using both `DrawTextureRec` and `DrawTexturePro` source rectangles.

Probe result:

- Direct drawing was upright.
- `DrawTextureRec` with negative source height presented the render texture correctly in the browser wasm path.
- `DrawTexturePro` presentation stayed inverted for this full-frame render-texture use case.

Noel's `with-fx` final presentation used `DrawTexturePro` even when there was no scaling/rotation/origin requirement. That was the inversion source.

### Fix

`vybe.game/-with-fx` was restored to the original pre-wasm render-texture flow and flip convention. The targeted wasm-safe presentation change is only the final full-screen drawing path:

- keep render-texture intermediate passes using the existing negative-height `DrawTextureRec` convention;
- replace the final no-scale/no-rotation `DrawTexturePro` presentation with equivalent `DrawTextureRec` using the existing negative-height source rectangle.

The retained non-orientation difference in `with-fx` is only the wasm-safe constant lookup via `vr/raylib-constant`; the old native Java constant path is not available in the wasm-only build.

### Validation

Noel capture after the fix:

```text
/path /Users/pfeodrippe/dev/vybe/target/noel-frame-04.png
```

The captured frame and the live desktop window are upright.

Input probe after the fix:

```text
:ready {:ready true, :focused true}
:before {:x 0.8712661, :y 5.0166025, :z 1.0041571} :key-before false
:during {:x 2.829057, :y 5.0166025, :z 7.787989} :key-during true
:after {:x 3.2493103, :y 5.0166025, :z 7.6813717} :key-after false
```

FPS probe after the fix:

```text
:frames 94
:seconds 5.00542875
:fps 18.779610038400804
:last-stat {:functions 786, :wrappedFns 45, :layouts 47,
            :aggregateScratch 7, :count 224, :sequence 172,
            :elapsedMs 1.3000000715255737}
```

The `ClosedChannelException` emitted after these probes is Overtone shutdown noise after `System/exit`; it does not indicate a Raylib/wasm rendering failure.

## 2026-04-26 Noel FPS Optimization Continuation

Validation target: keep the real Noel game running through the wasm-only Raylib
browser-window backend while improving frame rate. No dylib fallback,
`backend/wasm?` branch, smoke-test namespace, generated jextract Java binding, or
manual `resources/vybe/wasm/browser/raylib.js` edit was added.

### Retained changes

- Added a shadow pass cache in `vybe.game/draw-lights` with dedicated shadow RT
  ownership per scene/shader/resolution key. The cache is invalidated by light
  camera signatures, light transforms, mesh ids, mesh transforms, and joint
  transforms. Custom `:draw` functions still use the uncached path.
- Added `with-render-texture-no-clear--internal` and used it only in internal
  passes that immediately clear themselves: shader multipass targets and
  shadow-map rendering.
- Kept nil shader normalization and the direct no-op `with-fx` wrapper path for
  plain `:drawing true` calls with no target, RT, entity, rect, flip, shader, or
  post-shader work.
- Set the browser Raylib host WebGL context to `preserveDrawingBuffer: false`,
  which matches the continuously redrawn game-window path.
- Kept Noel's per-frame raycast reuse in `/Users/pfeodrippe/dev/vybe-games/src/noel.clj`.

### Rejected experiments

- Extra browser fast paths for simple generic calls were tested and reverted
  because the Noel FPS probe regressed.
- A public `with-fx :clear? false` option was tested and reverted. It reduced the
  command count from 131 to 128, but the measured FPS regressed, so it was not
  kept.

### Measurements

Measured with `/tmp/vybe_noel_fps_probe.clj` from
`/Users/pfeodrippe/dev/vybe-games`:

```text
Post-orientation baseline:         ~18.78 FPS, batch count 224
After fast-spec/string batching:   ~20.38 FPS
After Noel raycast reuse:          ~22.20 FPS
After shadow cache:                ~27.97 FPS
After nil shader/no-op with-fx:    ~29.94 FPS
After redundant-clear fix:         best run ~33.36 FPS, batch count 131
Latest retained run:               ~31.97 FPS, batch count 131
```

The browser-side batch execution is now roughly `0.6-0.8 ms` in the latest
samples, so the remaining gap to 120 FPS is mainly Clojure-side render/effect
orchestration, uniform state work, Flecs progress, and command construction.

### Validation

- Focused lint: `clj-kondo --lint src/vybe/game.clj src/vybe/raylib/browser.clj src/vybe/raylib/c.clj`.
  It reports the existing warning baseline and `errors: 0`.
- Focused game test: `clj -M:test :unit --focus vybe.game-test`, 2 tests, 2
  assertions, 0 failures.
- Noel FPS probe: latest retained run reported `160 frames / 5.005409167s =
  31.97 FPS` with batch count `131`.
- Generated `resources/vybe/wasm/browser/raylib.js` diff line count: `0`.

### Next implementation target

The next optimization should not be another small generic bridge tweak. The
highest-value target is a typed render command buffer plus shader/uniform state
coalescing:

1. Encode common draw/effect commands into compact typed frame buffers instead
   of per-call JSON-like specs.
2. Keep generic `call!` only for uncommon calls and return-valued functions.
3. Group stable shader/light uniforms into state objects so unchanged maps are
   not walked every frame.
4. Add per-system Flecs timing to identify whether any remaining hot system
   should move into generated wasm helper code.

### Noel raycast reuse follow-up

A further Noel-specific optimization reuses the `:vg/raycast` result produced by
`vybe.game.system/input-handler` during `vf/progress` instead of always issuing a
second center-screen raycast in `noel/raycasted-entity`. The old direct raycast
path remains as a fallback when the engine relation is absent.

Validation with `/tmp/vybe_noel_system_profile2.clj`:

```text
Before reuse: vj/cast-ray 352 calls / 3s sample; noel/raycasted-entity ~153 ms
After reuse:  vj/cast-ray 176 calls / 3s sample; noel/raycasted-entity ~9.5 ms
```

FPS remained around the current ~32 FPS range, so the remaining frame-time limit
is not this raycast path. The next large target remains typed render command
encoding and shader/uniform state coalescing.

## Latest Runtime Enforcement: No Interpreted Wasm

The JVM native-library replacement path uses Chicory's compiler, not the
interpreter. `vybe.wasm.runtime/instantiate` builds every module instance with a
`MachineFactoryCompiler` machine factory and explicitly sets
`InterpreterFallback/FAIL`. If Chicory cannot compile an instruction path, module
execution must fail instead of silently dropping to interpreted wasm.

Implementation points:

- `src/vybe/wasm/runtime.clj` keeps a single compiled runtime path via
  `compiled-machine-factory`.
- `compiled-machine-factory` calls `.withInterpreterFallback
  InterpreterFallback/FAIL` before `.compile`.
- `instantiate` always supplies that compiled machine factory to
  `Instance/builder`.
- `test/vybe/wasm_runtime_test.clj` asserts that the configured fallback is
  exactly `InterpreterFallback/FAIL` and loads/calls a minimal wasm module
  through the central runtime.

Validation:

```text
clj -M:test :unit --focus vybe.wasm-runtime-test
1 tests, 2 assertions, 0 failures
```

This applies to the JVM-hosted wasm modules used to replace native dylibs such
as Flecs/Jolt-style C libraries. The browser Raylib window path is different: it
runs through Chrome's native WebAssembly compiler/JIT via the generated
Emscripten glue (`WebAssembly.instantiateStreaming` or
`WebAssembly.instantiate`). That path does not use Chicory and is not the
Chicory interpreter. The generated `resources/vybe/wasm/browser/raylib.js` file
must remain generated-only; host-side behavior belongs in
`resources/vybe/wasm/browser/raylib-host.html` or the build scripts.

## 2026-04-26 Noel Movement Crash Hardening

The Noel window crash report was investigated with real Noel startup plus
synthetic movement stress against the browser-hosted Raylib/Wasm window. The
visible Overtone `ClosedChannelException` in probe output occurs during probe
shutdown after `System/exit`; it is an OSC send-loop shutdown race and not the
Raylib/Wasm render path.

Fixes kept:

1. `resources/vybe/wasm/browser/raylib-host.html` clamps virtual mouse positions
   to canvas bounds so out-of-window movement cannot produce extreme screen-ray
   coordinates.
2. `src/vybe/raylib/browser.clj` retries a Chrome DevTools call once after
   clearing a failed WebSocket connection.
3. `src/vybe/raylib.clj` catches `end-frame-batch!` failures inside the frame
   `finally`, preventing that cleanup path from killing the main loop.
4. `src/vybe/game.clj` caches stable `draw-lights` uniform maps for unchanged
   shadow/light state, reducing repeated uniform walking without changing the
   public rendering API.

Validation:

```text
clj-kondo --lint src/vybe/game.clj src/vybe/raylib/browser.clj src/vybe/raylib.clj src/vybe/raylib/c.clj
# errors: 0, warnings: existing baseline

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

Extreme Noel movement stress
# 1200 synthetic key/mouse/click events, no crash, browser bridge stayed ready

Noel FPS probe
# 157 frames / 5.005180458s = 31.37 FPS, browser batch ~0.8 ms
```

## 2026-04-26 Shader Multipass Opcode Optimization

Noel's hot shader `with-fx` path now uses a compact browser-host opcode for the
common shader-pass blit. The opcode does not stub rendering and does not bypass
Raylib: it calls the real wasm-exported Raylib wrappers for texture mode, shader
mode, clear, texture draw, and cleanup in the same order as the previous Clojure
sequence.

Validation summary:

```text
Noel FPS probe: 163 frames / 5.010568667s = 32.53 FPS
Batch count: 111
Browser batch elapsed: ~0.8 ms
Focused game test: 2 tests, 2 assertions, 0 failures
Focused lint: errors 0, existing warnings only
```

Call-profile effect over the 3-second Noel sample:

```text
Before shader-pass opcode:
DrawTextureRec 831 calls / ~293 ms
BeginTextureMode 1201 calls / ~211 ms

After shader-pass opcode:
DrawTextureRec 462 calls / ~143 ms
BeginTextureMode 829 calls / ~133 ms
```

A no-shader render-texture copy opcode was tested and rejected. It reduced the
batch count to about `99`, but regressed FPS to about `21.2`, so only the
shader-pass opcode remains.

## 2026-04-26 Noel Wasm Performance Follow-Up

The latest retained Noel performance pass keeps the wasm-only Raylib browser
window path and does not add dylib fallbacks, `backend/wasm?` branches, stubs, or
manual edits to generated `resources/vybe/wasm/browser/raylib.js`.

Retained changes:

1. Stable post-process shader pass uniforms are cached per shader before the
   multipass blit. This avoids re-walking identical parameter maps while still
   updating dynamic values when they change.
2. `WindowShouldClose` is read from the browser input snapshot instead of issuing
   a separate return-valued CDP call. The browser host still tracks Escape and
   window unload through `closeRequested`.
3. Reused full-screen pass constants (`Vector2 [0 0]` and transparent clear
   color) remove repeated component allocations from hot multipass paths.

Validation:

```text
clj-kondo --lint src/vybe/game.clj src/vybe/raylib/browser.clj src/vybe/raylib/c.clj src/vybe/raylib.clj
# errors: 0, warnings: existing baseline

clj -M:test :unit --focus vybe.game-test
# 2 tests, 2 assertions, 0 failures

Noel FPS probe
# 181 frames / 5.0083745s = 36.14 FPS, browser batch ~0.8 ms

Noel call profile after close-state snapshot
# WindowShouldClose removed from hot call list
```

Rejected experiment:

- Direct fixed-layout string serializers for the hottest compact opcodes were
  tested and reverted. They increased measured time for `BeginTextureMode` and
  `DrawTextureRec`, so the existing compact-spec encoder remains the faster
  measured implementation.

The browser Raylib path continues to execute real Raylib wasm through Chrome's
native WebAssembly engine. The JVM-hosted wasm library path continues to use
Chicory compiled mode with `InterpreterFallback/FAIL`.

## 2026-04-26 Rejected Noel Bridge Experiments

Two attempted optimizations were explicitly reverted after measurement:

1. `VyGetScreenToWorldRay` in Clojure: the local implementation matched the wasm
   ray numerically in Noel, but Clojure matrix inversion/unprojection made the
   end-to-end FPS worse (`~29 FPS` sample). This path should be optimized with a
   compiled wasm/native helper if revisited.
2. Returning input state from `callBatch`: combining input with frame flush
   increased return payload overhead and did not improve the Noel FPS sample, so
   the prior input snapshot path remains.

These reverts preserve the current no-fallback wasm-only implementation while
avoiding speculative changes that profile worse than the real wasm path.

## 2026-04-26 Best Target: Native Raylib Wasm Host

The best target for Raylib is a native desktop Raylib wasm host, not the current
browser/CDP bridge. The current browser path proved that the Raylib C code can be
compiled and exercised as wasm, but it still pays for Chrome process management,
DevTools transport, JSON command encoding, and a browser WebGL host. That is not
the final desktop architecture and it is not the path expected to reach a stable
120 FPS Noel target.

### Target Architecture

The target shape is:

```text
Vybe public API -> vybe.raylib.c -> vybe.raylib.host
                                  -> native desktop host imports
                                  -> Raylib wasm module
                                  -> OS window + native graphics context
```

Hard constraints for this target:

1. Keep the existing public Clojure API and `vybe.raylib.c` function names.
2. Keep Raylib C compiled to wasm; do not reintroduce project dylibs.
3. Do not route rendering through Chrome DevTools, browser JavaScript, or JSON.
4. Do not edit generated `resources/vybe/wasm/browser/raylib.js` manually.
5. Do not add a dylib fallback branch. The native host is a replacement host for
   the wasm module, not a fallback to the old native library.
6. Use compiled wasm execution wherever the JVM hosts wasm. Chicory-hosted
   modules must use `InterpreterFallback/FAIL`.
7. Generate ABI/layout/import metadata from Clang/wasm artifacts, not manual
   tables.

### Host Boundary

`vybe.raylib.c` should not depend directly on `vybe.raylib.browser`. It should
call a host boundary that exposes the operations the existing implementation
needs today:

1. Frame batching: `begin-frame!` and `end-frame!`.
2. Input snapshot: `input-state!` and `set-mouse-position!`.
3. Generic Raylib export call: `call!`.
4. Hot render opcodes that must remain real rendering calls, such as
   `draw-texture-shader-pass!`.
5. Asset loading that currently needs host-side filesystem/decoder behavior,
   such as `load-model-from-bytes!`.

The browser implementation can sit behind this boundary while the native host is
built, but it must be treated as a temporary host implementation, not as the
final native target and not as a fallback for released desktop behavior.

### Native Host Responsibilities

The native host must provide the platform services that Emscripten/browser glue
currently supplies:

1. Desktop window creation, resize, close state, focus state, and event polling.
2. Native graphics context creation. On macOS, the first pragmatic target is
   AppKit plus `NSOpenGLContext` because Raylib already has an OpenGL-oriented
   path. A Metal host can be a later performance target, but it requires a
   larger Raylib backend change.
3. Raylib wasm import functions for the GL/Emscripten surface actually used by
   `raylib.wasm`.
4. Input translation for keyboard, mouse buttons, mouse position, deltas, scroll,
   and cursor capture/warping semantics.
5. Timing functions such as monotonic time and frame delta support.
6. File/resource imports needed by model, texture, shader, and audio loading.
7. Audio imports if Noel or examples require Raylib audio through this path.
8. A direct command/call path between Clojure and the wasm module, without JSON
   serialization or CDP round trips.

Using JVM native access to system frameworks for window/context creation is not a
project dylib fallback. It is host integration with OS-provided frameworks. If
Panama/FFM is used for AppKit/OpenGL framework calls, the JVM may require native
access flags for those OS calls; that should be documented separately from the
no-project-dylib runtime rule.

### Implementation Milestones

M0. Add `vybe.raylib.host` and route `vybe.raylib.c` through it.

- Move direct `vybe.raylib.browser` calls out of `vybe.raylib.c`.
- Keep behavior stable by delegating the current implementation through a browser
  host record while the native host is not ready.
- Add a native host record that fails fast with a precise error for unimplemented
  operations. This is not a rendering stub; it prevents accidental silent use of
  an incomplete native host.

M1. Generate native-host import requirements.

- Use `wasm-objdump -x resources/vybe/wasm/raylib.wasm` and Clang-derived ABI
  metadata to list required imports programmatically.
- Store generated import metadata as EDN only if it is deterministic and
  regenerated by script.
- Do not hand-write the GL/import list as the source of truth.

M2. Build a minimal macOS desktop window/context host.

- Create an AppKit window and OpenGL context from JVM host code.
- Drive the event loop without Chrome.
- Expose close/focus/mouse/key state through `vybe.raylib.host/input-state!`.
- Validate with a blank window and `ClearBackground` through the Raylib wasm
  module.

M3. Implement the minimal GL import set for Raylib startup.

- Host the imports required by `InitWindow`, `BeginDrawing`, `ClearBackground`,
  and `EndDrawing`.
- Fail on missing imports at module load or first call, not by silently no-oping
  drawing functions.
- Validate with a real color-changing desktop window.

M4. Implement textures, render textures, shaders, and mesh draw imports.

- Cover `LoadRenderTexture`, `BeginTextureMode`, `DrawTextureRec`, shader mode,
  uniform upload, mesh upload, `DrawMesh`, and `DrawMeshInstanced`.
- Validate with the existing GLB/game import path and a minimal spinning model.

M5. Implement asset loading without browser helpers.

- Replace `load-model-from-bytes!` browser reads with direct wasm memory access
  and native host file/asset services.
- Preserve the existing `load-model` public behavior and model metadata expected
  by `vybe.game`.

M6. Run Noel on the native desktop host.

- Run `/Users/pfeodrippe/dev/vybe-games` Noel through the same public entrypoint.
- Confirm a real desktop window, visible model, correct frame orientation, and
  correct mouse controls.
- Compare frame timing against the current browser-host baseline.

M7. Optimize toward 120 FPS.

- Remove JSON/CDP overhead entirely by using direct host calls and typed memory.
- Coalesce render state and uniform uploads at the host boundary.
- Move proven hot render command assembly or math kernels into generated wasm
  helpers when they operate on wasm memory.
- Keep measurements tied to Noel, not synthetic smoke tests.

### Risks And Decisions

1. A raw standalone wasm module cannot create OS windows by itself. The JVM host
   must provide window, graphics, input, timing, file, and audio imports.
2. The Emscripten browser import surface may be awkward to host directly. If that
   becomes the blocker, the cleaner path is a Raylib platform/backend build
   configured for a small custom `vybe_*` host import surface.
3. macOS OpenGL is deprecated but still the fastest first implementation target
   because it matches Raylib's existing rendering model. Metal can be a later
   backend once behavior is correct.
4. Full Noel requires more than `ClearBackground`: render textures, shaders,
   model upload, mesh drawing, input capture, and probably audio/resource paths
   must all be real.
5. The native host should not mask missing behavior with no-op imports. Missing
   imports must fail loudly until implemented.

### Immediate Next Slice

The immediate slice is M0:

1. Add `src/vybe/raylib/host.clj`.
2. Refactor `src/vybe/raylib/c.clj` so browser-specific calls go through the host
   boundary.
3. Keep the existing browser implementation behavior stable while making the
   native host target explicit and fail-fast.
4. Re-run focused lint and `vybe.game-test` to confirm no behavior regression.

## 2026-04-26 Raylib vs LWJGL Backend Rewrite Assessment

Latest direction: keep Flecs/Jolt/runtime generated C on wasm, but allow Raylib
as the single native exception unless a transparent native backend such as LWJGL
is chosen for rendering/windowing.

### Finding

Switching from Raylib to LWJGL is feasible, but it is a renderer backend rewrite,
not a thin native binding swap.

Raylib is currently not only a windowing library in Vybe. It is also the runtime
shape of engine data:

1. `vr/Model`, `vr/Mesh`, `vr/Material`, `vr/MaterialMap`, `vr/Shader`,
   `vr/Texture`, and `vr/RenderTexture2D` are stored in Flecs/world data.
2. `vybe.game/-gltf->flecs` calls `*load-model*`, then stores Raylib meshes,
   materials, and mesh-material arrays in the world.
3. `draw-scene` mutates Raylib material shader/color fields and then calls
   `vr.c/draw-mesh`.
4. `with-fx` depends on Raylib render textures, texture rectangles, shader mode,
   uniform upload, and repeated full-screen texture passes.
5. `draw-lights` depends on shadow-map render textures, matrix uniforms, mesh
   drawing, and shader state.
6. Noel uses GPU readback (`rl-read-texture-pixels`) for color-id picking.
7. Examples use Raygui calls and immediate-mode 2D helpers.
8. Some game paths use lower-level `rl*` functions such as active texture,
   enable texture/shader, vertex array/buffer setup, matrix stack, and instanced
   mesh drawing.

### Local Usage Inventory

Vybe core currently references roughly this Raylib C surface:

```text
matrix/raymath: matrix-multiply, translate, scale, invert, transpose, rotate,
                quaternion/vector helpers
window/input:   init-window, is-window-ready, window-should-close,
                get-screen-width/height, get-frame-time/time, mouse/key calls,
                set-mouse-position
resources:      load-model, load-model-from-mesh, gen-mesh-cube,
                load-render-texture, load-shader-from-memory
render state:   begin/end drawing, begin/end mode3d, begin/end texture mode,
                begin/end shader mode
render calls:   draw-mesh, draw-mesh-instanced, draw-texture-rec,
                draw-texture-pro, draw-rectangle-pro, draw-text, draw-fps,
                draw-sphere, draw-line-3d, draw-grid, draw-billboard-pro
shader/uniform: get-shader-location, set-shader-value,
                set-shader-value-v, set-shader-value-matrix,
                vy-set-shader-value-matrix-v
rlgl:           rl-active-texture-slot, rl-enable-texture, rl-enable-shader,
                rl-enable-vertex-array, rl-enable-vertex-buffer,
                rl-set-vertex-attribute, rl-read-texture-pixels
raygui:         gui-text-input-box and Noel/Leo raygui widgets
```

Noel specifically uses `vg/with-fx` heavily, multiple render textures, shadow and
post-process shaders, model drawing, color-id readback, GUI text boxes, and mouse
ray picking. That means a LWJGL backend has to match the behavior of the Vybe
rendering layer, not just open a window.

### What LWJGL Would Replace

LWJGL can replace the platform and graphics access layer:

1. GLFW window/context creation.
2. Keyboard/mouse input and cursor behavior.
3. OpenGL function access.
4. OpenAL if Raylib audio is replaced.
5. Optional STB/Assimp-style helpers if selected as dependencies.

LWJGL does not replace Raylib's engine-level conveniences directly:

1. Raylib structs and memory layouts.
2. Raylib model loading semantics.
3. Raylib material map behavior.
4. Raylib shader uniform API.
5. Raylib render texture API.
6. Raygui widgets.
7. Raylib camera helpers and screen-to-world ray helpers.
8. Raylib draw helpers such as `DrawMesh`, `DrawBillboardPro`, and shape/text
   helpers.

Those must be implemented in Vybe or mapped to another Java library.

### Rewrite Shape If Using LWJGL

A realistic LWJGL backend should not expose LWJGL directly to game code. It
should introduce a Vybe renderer abstraction and preserve the public Vybe API:

```text
vybe.game / Noel public API
  -> vybe.render protocol/data model
  -> raylib backend OR lwjgl backend
  -> GLFW/OpenGL/OpenAL through LWJGL
```

Minimum backend data records needed:

1. `TextureHandle`
2. `RenderTextureHandle`
3. `ShaderHandle`
4. `MeshHandle`
5. `MaterialHandle`
6. `ModelHandle`
7. `Camera` stays in `vybe.type` where possible
8. `Color`, `Rectangle`, `Vector2/3/4`, `Matrix` should remain Vybe-owned types

Minimum OpenGL implementation needed for Noel:

1. GLFW window, frame loop, resize, close, focus, key/mouse input, cursor hide
   and virtual cursor/relative mouse behavior.
2. Shader compile/link from the existing GLSL resources.
3. Uniform location/cache/upload for scalar, vector, color, matrix, and matrix
   arrays.
4. Mesh upload from GLTF buffers, including positions, normals, texcoords,
   joints, weights, indices, VAO/VBO ownership.
5. Material texture and shader binding.
6. Render texture/FBO creation, color/depth attachments, viewport changes, and
   nested render-target stack.
7. 3D camera matrices and screen-to-world ray helper.
8. `draw-scene`, `draw-lights`, shadow-map rendering, instanced drawing, and
   full-screen post-process passes.
9. 2D primitives/text/GUI replacement for Noel overlays and debug/error UI.
10. GPU readback for color-id picking, or a replacement picking strategy.

### Effort Estimate

Small proof of concept:

- Scope: GLFW window, OpenGL clear, compile one shader, draw one triangle or
  cube.
- Estimate: 1-3 days.
- Value: validates LWJGL setup and macOS first-thread behavior only.

Minimal Vybe scene backend:

- Scope: `with-drawing`, camera, mesh upload, `draw-mesh`, basic shader, GLTF
  mesh/material import for static models.
- Estimate: 1-2 weeks.
- Risk: model/material parity and shader coordinate conventions.

Noel playable backend:

- Scope: render textures, post-process `with-fx`, shadow maps, `draw-lights`,
  color-id picking or replacement, text/GUI overlays, mouse/cursor behavior,
  animation/skinning buffers, instanced draws used by examples.
- Estimate: 3-6+ weeks.
- Risk: visual parity, shader uniform conventions, FBO orientation, performance
  regressions, Raygui replacement.

Full Raylib compatibility backend:

- Scope: implement enough of the current `vr.c/*` function surface and Raylib
  data semantics that existing examples continue to run unchanged.
- Estimate: 2-3+ months.
- Risk: this becomes a partial Raylib reimplementation.

### Native Packaging Implications

LWJGL still uses native code. It ships platform native jars and extracts/loads
native libraries at runtime. Therefore LWJGL does not satisfy a strict "no native
libraries at all" rule. It can satisfy a weaker rule: "do not ship our own
Raylib/Flecs/Jolt dylibs; rely on maintained LWJGL natives for graphics/window
access."

If using LWJGL, required native-specific work includes:

1. Add platform-specific LWJGL native dependencies for macOS/Linux/Windows.
2. Keep macOS `-XstartOnFirstThread` for GLFW window creation.
3. Decide packaging policy for extracted natives in app distributions.
4. Ensure CI/test profiles do not attempt to open windows unless explicitly run.
5. Keep Flecs/Jolt wasm paths independent of LWJGL native loading.

### Recommendation

For the current goal of getting Noel running and performant, keep Raylib as the
single native exception. This preserves the existing renderer semantics and keeps
all current game code meaningful.

Use LWJGL only if the goal changes from "remove Raylib dylib" to "own the whole
renderer backend in Vybe." That rewrite is valuable long-term, but it is a
separate renderer project rather than the fastest path to a working Noel desktop
window.

## 2026-04-27 Active Raylib Target: Wasm Hosted By Java/LWJGL

The active Raylib target is now Raylib compiled to wasm and hosted by Java using
LWJGL for the platform services. This keeps the existing Raylib/Vybe rendering
semantics while replacing the project-owned Raylib dylib with:

```text
Raylib C -> wasm module
Raylib wasm imports -> Java host functions
Java host functions -> LWJGL GLFW/OpenGL/OpenAL/native jars
```

This is not the same as rewriting Vybe directly on LWJGL. The goal is to keep the
Raylib API and data model intact, then implement the imported platform functions
that Raylib wasm needs.

### Why This Target

1. It avoids a full rewrite of `vybe.game` away from Raylib-shaped runtime data.
2. It preserves current `vr.c/*`, `vr/Model`, `vr/Mesh`, `vr/Material`,
   `vr/Shader`, `vr/RenderTexture2D`, and existing shader/render-texture logic.
3. It removes the browser/CDP/JSON path from the final desktop target.
4. It avoids shipping our own Raylib dylib, but it still depends on LWJGL native
   jars for GLFW/OpenGL/OpenAL. That is transparent third-party native usage, not
   no-native execution.

### Dependency Strategy

LWJGL is opt-in through aliases so ordinary tests do not load platform natives:

```sh
clj -M:lwjgl:lwjgl-macos-arm64:osx -m vybe.raylib.lwjgl.smoke
clj -M:lwjgl:lwjgl-macos-x64:osx   -m vybe.raylib.lwjgl.smoke
clj -M:lwjgl:lwjgl-linux-x64       -m vybe.raylib.lwjgl.smoke
clj -M:lwjgl:lwjgl-win-x64         -m vybe.raylib.lwjgl.smoke
```

The macOS command must include `:osx` or equivalent `-XstartOnFirstThread` JVM
opts because GLFW window creation must run on the first thread.

### Implemented First Slice

Added files:

```text
src/vybe/raylib/wasm_abi.clj
src/vybe/raylib/lwjgl/host.clj
src/vybe/raylib/lwjgl/smoke.clj
```

Added aliases:

```text
:lwjgl
:lwjgl-macos-arm64
:lwjgl-macos-x64
:lwjgl-linux-x64
:lwjgl-win-x64
```

The first slice does three concrete things:

1. Loads generated `resources/vybe/wasm/raylib_abi.edn` and exposes the actual
   Raylib wasm import inventory grouped by subsystem.
2. Creates a real GLFW/OpenGL window through LWJGL.
3. Provides a smoke entrypoint that opens the window, clears it with OpenGL, and
   closes it after a short loop.

This smoke clear is not a rendering substitute for Raylib wasm. It validates the
LWJGL host foundation. The Raylib wasm path must next call OpenGL through the
wasm import table, not through ad hoc Clojure rendering.

### Required Host Import Work

Generated Raylib wasm imports are the source of truth. They are grouped by:

1. `:gl` - OpenGL calls, including both direct `gl*` and `emscripten_gl*` names.
2. `:glfw` - window/context/input callbacks and state.
3. `:emscripten` - browser/runtime glue that must either map to GLFW/LWJGL or be
   eliminated by a cleaner Raylib wasm build.
4. `:audio` - Raylib audio functions that should map to OpenAL or be disabled
   until audio is needed.
5. `:runtime` - WASI/syscall-style imports, file descriptors, time, and process
   behavior.
6. `:other` - imports that require explicit case-by-case handling.

### Implementation Milestones

M0. LWJGL host smoke window.

- Done: `vybe.raylib.lwjgl.host/init-window!` creates a GLFW/OpenGL window.
- Done: `vybe.raylib.lwjgl.smoke` opens and clears a real desktop window.
- Done: generated import inventory is available via
  `vybe.raylib.wasm-abi/import-summary`.

M1. Replace fake/stub Raylib wasm imports with LWJGL host functions.

- Reintroduce the Raylib wasm loader only for this LWJGL target.
- For every imported function, either map it to LWJGL/Java or fail loudly.
- Do not return fake GL object ids or pretend shader/framebuffer success.
- Start with functions needed by `InitWindow`, `BeginDrawing`,
  `ClearBackground`, `EndDrawing`, and `WindowShouldClose`.

M2. Implement GL import forwarding.

- Map `glViewport`, `glClearColor`, `glClear`, state toggles, blend/depth/cull
  functions, buffer/texture/framebuffer creation, shader compile/link, uniform
  upload, draw calls, and readback to LWJGL OpenGL calls.
- For pointer arguments, read/write Raylib wasm linear memory explicitly.
- Maintain generated import signatures from `raylib_abi.edn`.

M3. Implement GLFW/Emscripten window/input imports.

- Map GLFW window creation/context/current/swap/poll/close/focus/size/time.
- Register callbacks or provide equivalent polled state for key, char, mouse
  button, cursor position, scroll, resize, focus, content scale, and drop.
- Map pointer lock/cursor hide behavior to GLFW cursor modes.
- Replace browser canvas-size imports with real window/framebuffer size writes.

M4. Implement runtime/file imports.

- Support resource/file reads needed by Raylib model, texture, shader, and audio
  loaders.
- Prefer direct Java resource/file callbacks where possible.
- Keep paths deterministic for classpath resources and filesystem game assets.

M5. Implement audio imports if needed.

- Map Raylib audio stream/sound/music imports to OpenAL through LWJGL, or split
  audio out until Noel requires it.
- Avoid silent no-op audio success once used by examples.

M6. Run Raylib wasm minimal app.

- Call Raylib wasm `InitWindow`, `BeginDrawing`, `ClearBackground`,
  `EndDrawing`, and `WindowShouldClose` through Chicory compiled mode plus LWJGL
  imports.
- Validate a real desktop window whose clear color is produced by Raylib wasm,
  not host-side smoke code.

M7. Run Noel.

- Cover render textures, shaders, meshes, models, shadow maps, post-process
  passes, GUI/text, color-id readback, and input.
- Run `/Users/pfeodrippe/dev/vybe-games` Noel through the existing public
  entrypoint.
- Target 120 FPS after correctness.

### Current Commands

Smoke the LWJGL host on Apple Silicon macOS:

```sh
clj -M:lwjgl:lwjgl-macos-arm64:osx -m vybe.raylib.lwjgl.smoke
```

Print generated import summary without opening a window:

```sh
clj -M:lwjgl:lwjgl-macos-arm64 -e '(require (quote [vybe.raylib.wasm-abi :as abi])) (prn (abi/import-summary))'
```

## 2026-04-27 Raylib Wasm Hosted By LWJGL Progress

This pass moved the desktop Raylib path from browser-hosted wasm toward the target
native desktop host: Raylib remains compiled to wasm, and the host imports are
implemented in Java through LWJGL GLFW/OpenGL calls. This is not a Raylib dylib
fallback and not a rendering rewrite. The wasm module still executes Raylib C;
the JVM host supplies the low-level windowing and GL imports that Raylib expects.

Implemented pieces:

1. `vybe.raylib.lwjgl.host` creates and owns a real GLFW/OpenGL desktop window
   through LWJGL.
2. `vybe.raylib.lwjgl.wasm` loads `resources/vybe/wasm/raylib.wasm` through
   Chicory's compiled machine path and forwards implemented `gl*`, `glfw*`,
   Emscripten, WASI, and runtime imports to LWJGL or JVM equivalents.
3. `vybe.raylib.abi` reads the generated Raylib wasm ABI EDN and derives layouts,
   constants, wrapper exports, and function metadata from generated data rather
   than handwritten function lists.
4. `vybe.raylib.impl` now interns the existing public `vybe.raylib.c` functions
   as real Clojure functions backed by Raylib wasm calls. The public call sites
   still use `vybe.raylib.c/*`.
5. The `vybe.c` wasm host-function bridge now handles Emscripten aggregate return
   ABI correctly. Struct returns are represented as an implicit first `i32`
   return pointer and no scalar result, then copied back into the caller's wasm
   memory.
6. Raylib matrix/vector functions expose `:vybe/fn-meta` and a raw host bridge so
   C kernels generated by `vybe.c` can call Raylib wasm helpers without seeing
   them as `void` and without linking to a dylib.
7. The custom `draw-texture-shader-pass` helper used by `vybe.game` is provided
   as a real composition of Raylib wasm calls: begin texture mode, clear,
   begin shader mode, draw texture rec, end shader mode, end texture mode.

Validation commands run:

```sh
clj -M:lwjgl:lwjgl-macos-arm64:osx -m vybe.raylib.lwjgl.smoke
clj -M:lwjgl:lwjgl-macos-arm64:osx -m vybe.raylib.lwjgl.wasm-smoke
clj -M:lwjgl:lwjgl-macos-arm64:osx -e '(require (quote [vybe.raylib.c :as rc]) (quote [vybe.raylib :as vr])) (println :ok (:m0 (rc/matrix-scale 1.0 2.0 3.0))) (System/exit 0)'
```

Game validation from `/Users/pfeodrippe/dev/vybe-games`:

```sh
clj -J-XstartOnFirstThread -Sdeps '{:deps {org.lwjgl/lwjgl {:mvn/version "3.4.1"} org.lwjgl/lwjgl-glfw {:mvn/version "3.4.1"} org.lwjgl/lwjgl-opengl {:mvn/version "3.4.1"} org.lwjgl/lwjgl-openal {:mvn/version "3.4.1"} org.lwjgl/lwjgl$natives-macos-arm64 {:mvn/version "3.4.1"} org.lwjgl/lwjgl-glfw$natives-macos-arm64 {:mvn/version "3.4.1"} org.lwjgl/lwjgl-opengl$natives-macos-arm64 {:mvn/version "3.4.1"} org.lwjgl/lwjgl-openal$natives-macos-arm64 {:mvn/version "3.4.1"}}}' -M:dev -m minimal
```

`minimal` loaded and ran the desktop wasm/LWJGL path until manually terminated.

Noel validation used the same dependency injection and a timeout wrapper:

```sh
clj -J-XstartOnFirstThread -Sdeps '{:deps {org.lwjgl/lwjgl {:mvn/version "3.4.1"} org.lwjgl/lwjgl-glfw {:mvn/version "3.4.1"} org.lwjgl/lwjgl-opengl {:mvn/version "3.4.1"} org.lwjgl/lwjgl-openal {:mvn/version "3.4.1"} org.lwjgl/lwjgl$natives-macos-arm64 {:mvn/version "3.4.1"} org.lwjgl/lwjgl-glfw$natives-macos-arm64 {:mvn/version "3.4.1"} org.lwjgl/lwjgl-opengl$natives-macos-arm64 {:mvn/version "3.4.1"} org.lwjgl/lwjgl-openal$natives-macos-arm64 {:mvn/version "3.4.1"}}}' -M:dev -m noel
```

Noel loaded Overtone, opened the game runtime path, and stayed alive until the
timeout wrapper killed it. A process scan after the run showed no leftover Noel,
minimal, or SuperCollider process.

Current follow-up work:

1. Add a first-class `:lwjgl`/platform alias to `vybe-games` or document a short
   `-Sdeps` helper so Noel can be launched without the long inline dependency
   map.
2. Continue filling Raylib wasm imports as the full game exercises more OpenGL
   paths. Missing imports must be implemented by forwarding to LWJGL, not by
   stubbing rendering behavior.
3. Type-hint the remaining LWJGL bridge overloads so the startup output is not
   dominated by reflection warnings and the hot host bridge avoids reflective
   dispatch.
4. Add focused real-render validation for model visibility, render textures,
   shader passes, shadow-map passes, and input over the LWJGL-hosted path.
5. Measure FPS on the LWJGL-hosted path and compare against the prior browser
   host. The expected win is removal of browser/CDP/JSON overhead; the remaining
   bottlenecks should be actual Raylib wasm execution, JVM host import overhead,
   Flecs/Jolt simulation, and Clojure orchestration.

Additional validation after the first Noel run:

```sh
# Same command as above, with a 25 second timeout wrapper.
```

The longer Noel run reached Overtone startup, established the game nREPL, and
remained alive until the timeout wrapper killed it. No missing Raylib import or
namespace-load error was reported during that window. This is still not a final
"playable" sign-off: the next validation must keep Noel open long enough to
visually confirm the model/render-texture/shadow path and measure FPS on the
LWJGL-hosted window.

`.gitignore` status:

1. Generated wasm binaries remain ignored through `resources/vybe/wasm/**/*.wasm`
   and `*.wasm`.
2. Generated browser JS remains ignored through
   `resources/vybe/wasm/browser/raylib.js` and
   `resources/vybe/wasm/browser-demo/raylib-demo.js`.
3. Local Calva output is ignored through `.calva/`.

## 2026-04-27 Update: LWJGL-Hosted Raylib Wasm Render Path Fixes

This pass moved the native desktop Raylib-wasm host from window/bootstrap smoke to
real render-loop coverage for `minimal` and `noel`.

Concrete fixes completed:

1. The desktop Raylib wasm build now uses `GRAPHICS_API_OPENGL_ES3` in both
   `bin/build-raylib-wasm.sh` and `bin/raylib-wasm-abi.edn`. This makes the
   custom `VyLoadRenderTexture`/`VyLoadShadowmapRenderTexture` path allocate a
   real depth texture with explicit GL ES3 depth formats instead of relying on
   the ES2/WebGL extension-dependent fallback. The previous `Couldn't create
   frame buffer` failure in `vg/draw-lights` is gone.
2. The LWJGL host now forwards ES3 VAO and draw-buffer imports exposed by the ES3
   Raylib wasm build: `glBindVertexArray`, `glGenVertexArrays`,
   `glDeleteVertexArrays`, `glIsVertexArray`, direct instanced draw aliases,
   `glDrawBuffers`, `glDrawBuffersEXT`, and `glBlitFramebuffer`.
3. `VySetShaderValueMatrixV` now marshals matrix arrays into wasm memory before
   calling the Raylib wasm export. This preserves the existing
   `vr.c/vy-set-shader-value-matrix-v` behavior used by skinning/light uniforms
   without passing Clojure vectors to Chicory.
4. Pointer arguments that receive existing native/Panama memory now use a wasm
   temporary buffer plus copy-back after the wasm call. This fixes out-parameters
   such as `QuaternionToAxisAngle(Quaternion, Vector3 *, float *)`, where Noel
   passes `vt/Vector3` and `vp/float*` values that previously leaked host native
   addresses into wasm and caused out-of-bounds memory writes.
5. The custom shader translation path keeps GLSL ES shaders running on the
   desktop OpenGL context without stubbing shader compilation. Shader failures
   remain real failures; optional tracing is controlled by
   `VYBE_RAYLIB_WASM_TRACE=1`.

Validation commands run from `/Users/pfeodrippe/dev/vybe`:

```sh
bin/build-raylib-wasm.sh
clj -M:lwjgl:lwjgl-macos-arm64:osx -m vybe.raylib.lwjgl.wasm-smoke
clj -M:lwjgl:lwjgl-macos-arm64 -e '(require (quote vybe.raylib.impl)) (println :loaded) (shutdown-agents)'
```

Bounded real app validation from `/Users/pfeodrippe/dev/vybe-games`:

```sh
VYBE_NREPL_PORT=7891 clj -M:dev:wasm-lwjgl:wasm-lwjgl-macos-arm64 -m minimal
VYBE_NREPL_PORT=7899 clj -M:dev:wasm-lwjgl:wasm-lwjgl-macos-arm64 -m noel
```

Observed status:

1. `minimal` ran for 18 seconds through the Raylib wasm/LWJGL path without the
   previous framebuffer exception and without repeated uniform/matrix marshaling
   exceptions.
2. `noel` ran for 30 seconds through the Raylib wasm/LWJGL path. Overtone booted,
   the game nREPL started, and no Raylib missing-import, framebuffer, or wasm
   out-of-bounds exception appeared during the run. The only stack trace in the
   bounded log was Overtone's `ClosedChannelException` caused by intentionally
   killing the timeout-wrapped process.
3. The command to run the full desktop game on Apple Silicon is now the short
   alias-based command above; no `--enable-native-access=ALL-UNNAMED` flag is
   required for the wasm/LWJGL path.
4. Chicory still uses compiled execution with `InterpreterFallback/FAIL` through
   `vybe.wasm.runtime`; this is not interpreted wasm and does not silently fall
   back to an interpreter.

Remaining correctness/performance work:

1. Run an interactive visual pass long enough to verify Noel model visibility,
   shadow quality, texture binding, and input under the LWJGL-hosted window.
2. Investigate the one-time Apple OpenGL warning about unit 0 texture/sampler
   binding. It does not currently crash the app, but it may indicate a texture
   upload or sampler uniform mismatch that can affect visual correctness.
3. Profile the LWJGL-hosted path separately from Overtone startup. Current
   bounded tests prove runtime stability; they are not a 120 FPS sign-off.
4. Continue replacing any missing Raylib import with real LWJGL/OpenGL/OpenAL or
   JVM host behavior. Do not add draw stubs, `backend/wasm?` branches, dylib
   fallback branches, or browser/CDP/JSON render transport to the target path.

## 2026-04-27 Wasm Runtime State And Arena Fixes

The latest Noel validation exposed two runtime correctness issues that only show
up when Flecs, Raylib, Jolt, and generated `vybe.c` wasm kernels are active in
the same frame.

Completed fixes:

1. `vybe.flecs.wasm-c/module` now activates the Flecs wasm module as the current
   default module before Flecs wrapper calls. This prevents Raylib/Jolt/generated
   wasm calls from leaving `vybe.wasm/default-module` pointing at the wrong
   memory before Flecs component reads.
2. `vybe.wasm/with-arena-root` now suspends scoped allocation tracking. This
   preserves the original Panama lifetime intent: long-lived Flecs query/system
   allocations created under `with-arena-root` are not freed by the per-frame
   arena in `vybe.game/start!`.
3. Wasm scoped allocation tracking now keys untracking by linear memory object
   plus pointer, not by Clojure module wrapper identity. This matters because
   generated wasm kernels can import and share another module's memory while
   using a different module wrapper.
4. `vybe.c/reserve-module-heap!` now performs generated-kernel heap reservations
   outside frame-scope tracking, and treats an unusable module-local `malloc(1)`
   as a no-op reservation for generated systems that do not allocate. This avoids
   freeing generated-kernel heap reservation blocks at the end of a frame.
5. `vybe.wasm/malloc` validates returned pointers against the current memory
   size, and scoped cleanup reports the pointer that failed if a module free
   traps. This is fail-fast diagnostics, not an interpreter or dylib fallback.

Validation after these fixes:

```sh
clj -M:test test/vybe/flecs_test.clj
clj -M:lwjgl:lwjgl-macos-arm64:osx -m vybe.raylib.lwjgl.wasm-smoke
cd /Users/pfeodrippe/dev/vybe-games
VYBE_NREPL_PORT=7899 clj -M:dev:wasm-lwjgl:wasm-lwjgl-macos-arm64 -m noel
```

The latest bounded Noel run stayed alive until the controlled 30 second kill. No
`free-all!`, `ecs_query_next`, out-of-bounds wasm memory, missing Raylib import,
or generated-kernel malloc error appeared. The only exception in the log was the
expected Overtone `ClosedChannelException` caused by terminating the process.

Remaining issue:

1. The Apple OpenGL warning about texture unit 0 and sampler type still appears
   once during Noel startup. It does not currently crash the wasm/LWJGL path, but
   it still needs visual/performance investigation before claiming the final 120
   FPS target.

## 2026-04-27 LWJGL Raylib Wasm Performance Fixes

The LWJGL-hosted Raylib wasm path now has two performance-specific correctness
fixes:

1. `WindowShouldClose` no longer calls Raylib's web-platform export. That export
   sleeps for 16 ms via `emscripten_sleep(16)`, which is appropriate for browser
   sync examples but wrong for the desktop LWJGL host. The close-state check now
   uses the host GLFW window state directly.
2. The generated `vybe.game.system/matrix-transform` wasm kernel no longer calls
   Raylib wasm imports for scale, quaternion-to-matrix, and matrix multiply. It
   computes the same TRS matrix directly inside the generated wasm code. A
   comparison against the previous Raylib composition returned max diff `0.0`.
3. `VyDrawTextureShaderPass` is now compiled into `raylib.wasm` as a real C
   helper and used by `vr.c/draw-texture-shader-pass` to keep a common shader
   blit sequence inside the Raylib wasm module.

Measured Noel progression on Apple Silicon macOS:

```text
Initial LWJGL steady probe:        ~14.99 FPS
After WindowShouldClose host fix:  ~20.56 FPS
After transform-kernel math:       ~30.54 FPS
After wasm shader-pass helper:     ~31.35 FPS
```

The current path remains wasm-only for Raylib and generated kernels. The only
native dependency for the desktop window/render host is LWJGL's OpenGL/GLFW
binding layer; there is no Raylib dylib fallback.
