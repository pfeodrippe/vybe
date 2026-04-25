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
   or layouts. Generate ABI metadata from the C headers and Wasm module.
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

### Test Namespace Status

The requested test file remains the target:

```sh
clj -M:test test/vybe/flecs_test.clj
```

Current repo result:

```text
No such suite: :test/vybe/flecs_test.clj, valid options: :unit.
```

Kaocha focus also currently loads unrelated test namespaces and fails before
Flecs because `vybe.audio_test` pulls Raylib native classes:

```text
UnsatisfiedLinkError: no raylib in java.library.path
```

Directly requiring `vybe.flecs-test` also pulls `vybe.game.system`, which pulls
Raylib namespaces that are not yet Wasm-backed. This is now the next cross-lib
blocker for running the full existing Flecs test namespace in a Wasm-only test
process. No separate smoke-test namespace should be added; instead either:

1. Convert the Raylib/game-system dependencies used by `vybe.flecs-test` to Wasm
   or pure Clojure where needed.
2. Split the Flecs test namespace so Flecs core tests do not require Raylib/game
   systems at load time.
3. Add Kaocha test selectors/configuration that can run only the Flecs core tests
   without loading unrelated native namespaces.

## Remaining Work

### Flecs Completion

1. Run the full `vybe.flecs-test` namespace once Raylib load-time blockers are
   removed or isolated.
2. Validate `c-systems-test` after `vybe.game.system` no longer requires Raylib
   native dylibs at load time.
3. Validate REST behavior if REST is part of the required Flecs feature set.
   Current socket imports instantiate the module; real socket behavior should be
   Java-hosted if REST is used.
4. Build the jar and verify Flecs artifacts:

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

1. Finish Flecs Wasm-only testability by removing Raylib load-time blockers from
   Flecs tests or converting the required Raylib/game-system pieces.
2. Run `clj -M:test` and fix Flecs regressions without adding fallback dylibs.
3. Build jar and verify no Flecs dylib/jextract artifacts are packaged.
4. Generalize ABI config overrides based on the first non-Flecs library.
5. Convert the next CPU-only library slice, preferably JoltC or raymath.
6. Address socket/window/audio libraries with explicit Java host integrations
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
