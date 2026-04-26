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
