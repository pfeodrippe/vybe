# Noel Wasm 120 FPS Performance Plan

Goal: make the Noel example in `/Users/pfeodrippe/dev/vybe-games` run through the wasm-only Raylib path at 120 FPS where the display can support it.

Frame budget for 120 FPS: 8.33 ms. The post-orientation baseline was about 18.8 FPS, roughly 53 ms/frame. The browser-side wasm batch itself is usually around 1.3 ms, so most remaining work is Clojure-side orchestration, repeated render passes, repeated raycasts, Flecs/Jolt simulation, and command encoding.

## Rules

- Keep wasm-only. Do not add dylib fallback paths.
- Do not add `backend/wasm?` compatibility branches.
- Do not edit generated `resources/vybe/wasm/browser/raylib.js` manually.
- Keep generated `.wasm` and generated JS ignored; track source resources and ABI EDN needed at runtime.
- Preserve `with-fx` behavior unless a focused probe proves a browser-wasm rendering mismatch.
- Validate with the real Noel app, not smoke stubs.

## Baseline

Initial validated baseline after the orientation fix:

```text
Noel FPS probe: 94 frames / 5.00542875s = 18.7796 FPS
Last browser batch: {:count 224, :elapsedMs 1.3000000715255737}
```

Deep profile at that point showed:

```text
noel/draw avg:                 64.58 ms
vybe.game/-with-fx avg:         6.12 ms, called 5x/frame
vybe.raylib.browser/call!:    984 ms cumulative / 11140 calls
vybe.game/draw-lights avg:      7.77 ms, called 2x/frame
vybe.flecs/progress avg:        9.26 ms
noel/raycasted-entity avg:      1.53 ms, called repeatedly/frame
```

## Completed Optimizations

### 1. Bridge Fast-Spec Short Circuit

Problem: `browser/call!` computed ABI function plans and return conversion metadata even for high-volume fast specs known to be `void` commands.

Fix:

- Compute `fast-spec` first.
- If present, enqueue/execute it as a `void` call without consulting ABI plans.
- Preserve existing fallback for non-fast calls and non-fast `DrawMesh` values.

Validation:

```text
Noel FPS probe: 97 frames / 5.003993958s = 19.3845 FPS
Last browser batch: {:count 227, :elapsedMs 1.2999999523162842}
```

### 2. Browser Input Virtual Mouse Fix

Problem: the browser host cannot warp the OS cursor, so native-style `SetMousePosition` caused the next DOM `mousemove` to jump the game mouse state back toward the real cursor.

Fix:

- Keep separate DOM cursor coordinates and virtual raylib coordinates in `raylib-host.html`.
- Apply real DOM deltas to the virtual raylib position.
- Let `setMousePosition` update only the virtual raylib position and clear deltas.

Validation:

```text
:after-set {:mouseX 750, :mouseY 500, :mouseDeltaX 0, :mouseDeltaY 0}
:after-small-move {:mouseX 750.25, :mouseY 500, :mouseDeltaX 0.25, :mouseDeltaY 0}
```

The live Noel mouse controls were confirmed correct after this change.

### 3. Bridge Batch String Encoder

Problem: each frame accumulated Clojure vectors and then serialized the whole batch at flush time.

Fix:

- Store the active batch as a `StringBuilder` plus count.
- Serialize individual specs as they are enqueued.
- Flush by wrapping the accumulated JSON fragments in one `callBatch([...])` expression.
- Keep the old collection fallback for defensive compatibility.

Validation:

```text
Noel FPS probe: 102 frames / 5.005179666s = 20.3789 FPS
Last browser batch: {:count 227, :elapsedMs 1.5}
```

A more aggressive manual JSON path for selected commands was tested and rejected because it regressed the FPS probe to about 18.6 FPS.

### 4. `with-fx` No-Shader Fast Path

Problem: `with-fx` always rendered into temporary RTs and copied through the pass pipeline even when no shaders or post-shaders were present.

Fix:

- When `:shaders` and `:shaders-post` are empty after entity processing, render directly to the requested RT.
- Preserve the final browser-wasm `DrawTextureRec` presentation path for `:drawing true`.
- Leave shader, post-shader, bypass, and entity paths unchanged.

Validation:

```text
Focused game test: 2 tests, 2 assertions, 0 failures
Noel FPS probe: 101 frames / 5.0056595s = 20.1772 FPS
Last browser batch: {:count 219, :elapsedMs 1.2999999523162842}
```

Deep profile after this change:

```text
noel/draw avg:                 56.20 ms
vybe.game/-with-fx avg:         5.28 ms, called 5x/frame
vybe.raylib.browser/call!:    932 ms cumulative / 11712 calls
vybe.game/draw-lights avg:      7.52 ms, called 2x/frame
vybe.flecs/progress avg:        8.22 ms
noel/raycasted-entity avg:      1.43 ms, still called repeatedly/frame
```

### 5. Noel Per-Frame Raycast Reuse

Problem: Noel called `raycasted-entity` multiple times in the same frame for TV switching, hover text, mailbox interaction, and cursor styling. Each call performs screen-to-world ray generation plus Jolt raycast.

Fix:

- Compute `hovered-entity` once near the start of Noel's draw function.
- Reuse it for TV switch logic and text/cursor hover logic.

Validation:

```text
Noel namespace load: :noel-ok
Noel FPS probe: 111 frames / 5.000806375s = 22.1964 FPS
Last browser batch: {:count 219, :elapsedMs 1.3000000715255737}
```

### 6. Shadow Pass Cache With Dedicated RT Ownership

Problem: Noel calls `draw-lights` twice per frame. The old path rendered shadow
maps every call even when light/caster transforms had not changed.

Fix:

- Added a per-scene/per-shader/per-resolution shadow cache in `vybe.game`.
- Each cache key owns its own shadow render textures, so separate scenes do not
  overwrite each other.
- Cache invalidation is based on light camera signatures, light transform
  matrices, mesh transforms, mesh ids, and joint transforms.
- Custom `:draw` functions still use the uncached path; only the default
  `draw-scene` path is cached.

Validation:

```text
Noel FPS probe: about 28.0 FPS
Last browser batch after this stage: {:count 149, :elapsedMs ~0.9}
```

### 7. Nil Shader Normalization And Direct Drawing No-Op

Problem: Noel often supplies shader vectors containing conditional nil entries,
and one `with-fx` call wraps plain overlay drawing with no RT/target/entity or
shader work.

Fix:

- Normalize `:shaders` and `:shaders-post` with nil removal at the `with-fx`
  boundary.
- Add a direct no-op path for `with-fx` when it is only a drawing wrapper and no
  render target, entity, target, rect, flip, shader, or post-shader work is
  requested.
- Preserve the existing render-target and shader behavior for all non-empty
  cases.

Validation:

```text
Noel FPS probe: 150 frames / 5.009307042s = 29.94 FPS
Last browser batch: {:count 136, :elapsedMs ~0.7}
```

### 8. Redundant Internal Render-Target Clears

Problem: several internal render passes called `with-render-texture--internal`,
which clears the target, and then immediately called `ClearBackground` again
inside the body.

Fix:

- Added `with-render-texture-no-clear--internal`.
- Used it only in internal paths where the body explicitly clears:
  `_apply-multipass` pass targets and shadow-map rendering.
- Rejected a broader public `with-fx :clear? false` experiment because it
  reduced command count but regressed measured FPS.

Validation:

```text
Noel FPS probe: 167 frames / 5.006610459s = 33.36 FPS
Last browser batch: {:count 131, :elapsedMs ~0.8}
```

### 9. WebGL Realtime Context Settings

Problem: the browser Raylib host requested `preserveDrawingBuffer: true`, which
can force extra framebuffer preservation work in WebGL. Noel redraws
continuously, so preserving old frame contents is not needed.

Fix:

- Set `preserveDrawingBuffer: false` in both intercepted `canvas.getContext`
  attributes and the Emscripten module `webglContextAttributes`.
- Kept `alpha: false`, `antialias: false`, and `powerPreference:
  "high-performance"`.

Validation:

```text
Noel FPS probe: 160 frames / 5.005409167s = 31.97 FPS
Last browser batch: {:count 131, :elapsedMs ~0.7}
```

This is neutral-to-slightly-positive in the current probe variance and matches
the intended realtime-rendering mode.

## Next Steps

### A. Per-System Flecs Timing And Wasm Promotion

Problem: `vf/progress` remains several ms per frame.

Implementation plan:

- Add per-system timing around generated Flecs callbacks.
- Identify hot pure/math-heavy systems.
- Move only proven hot pure/math-heavy systems to generated wasm/C helpers.

Expected result: reduce simulation/update cost without changing ECS behavior.

### B. Render Command Buffer V2

Problem: bridge calls still spend significant Clojure time despite low browser-side wasm execution time.

Implementation plan:

- Add a compact typed frame-command buffer for common void draw calls.
- Encode numeric commands into primitive arrays or byte buffers.
- Keep generic `call!` as the fallback for uncommon calls.

Expected result: reduce per-frame command construction and serialization cost beyond the current string-batch encoder.

### C. Uniform And Shader State Coalescing

Problem: `set-uniform` remains hot even with upload caching because the engine
still computes cache keys and walks uniform maps every frame.

Implementation plan:

- Profile uniform map construction separately from native/browser calls.
- Add shader-state objects for stable light/material uniforms.
- Upload only changed dynamic values (`u_time`, active camera-dependent values,
  changed light matrices) while retaining the public `set-uniform` API.

Expected result: reduce Clojure-side shader orchestration cost without changing
shader behavior.

## Current Status

The current validated Noel FPS probe is around 32 FPS, with the best single run
after the retained render-target clear optimization at about 33.36 FPS. This is
not close to 120 FPS yet. The next meaningful gains should come from reducing
Clojure-side orchestration in `with-fx`/uniform handling, per-system Flecs
timing, and a typed render command buffer rather than more small generic bridge
patches.

## Latest Follow-Up: Raycast Reuse

Noel now reuses the `:vg/raycast` body relation produced by `input-handler`
after `vf/progress`, falling back to the old explicit center-screen raycast only
when that relation is absent.

Validation:

```text
Before reuse: vj/cast-ray 352 calls / 3s sample; noel/raycasted-entity ~153 ms
After reuse:  vj/cast-ray 176 calls / 3s sample; noel/raycasted-entity ~9.5 ms
FPS probe after change: ~32.16 FPS
```

This removes duplicate Jolt raycasts, but it does not change the main remaining
bottleneck: Clojure-side render/effect orchestration and command construction.

## Latest Follow-Up: Compiled Wasm Runtime Guard

The JVM wasm runtime now has an explicit regression guard against interpreted
execution. `vybe.wasm.runtime` compiles modules with Chicory's
`MachineFactoryCompiler` and uses `InterpreterFallback/FAIL`, with a focused test
covering that configuration.

The browser Raylib path remains a browser-native WebAssembly path. It uses
Chrome's WebAssembly compilation/JIT pipeline through generated Emscripten glue,
not Chicory. No manual edits are made to generated `raylib.js`.

Recent optimization notes:

- Local quaternion math removed repeated Raylib bridge calls for
  `QuaternionNormalize`, `QuaternionFromAxisAngle`, and `QuaternionMultiply`.
- A direct compact-spec JSON renderer experiment was reverted because it
  regressed Noel FPS and increased batch count.
- The latest retained FPS remains around 31-32 FPS; the next meaningful target
  is still typed render command encoding plus shader/uniform state coalescing.

## Latest Follow-Up: Movement Crash Hardening

A reported crash while moving around was investigated separately from FPS work.
The repeated Overtone `ClosedChannelException` seen in probe output happens after
probe shutdown (`System/exit`) when Overtone's OSC send loop writes to an already
closed SuperCollider UDP channel. That shutdown trace is noisy, but it is not the
Raylib/Wasm render-loop failure.

Crash hardening kept in this pass:

- The browser host clamps virtual mouse coordinates to the canvas bounds. Extreme
  out-of-window mouse events now produce screen positions in `[0,width]` and
  `[0,height]` instead of feeding multi-thousand-pixel coordinates into
  screen-ray and physics paths.
- The Raylib main loop now catches `end-frame-batch!` failures in the `finally`
  path, so a transient Chrome DevTools/WebSocket failure cannot escape the draw
  loop directly.
- The Chrome DevTools bridge now clears and reconnects once after a failed CDP
  send/evaluate call before surfacing the original error.

Stress validation:

```text
Extreme movement/input stress: 1200 synthetic mouse/key/click events over ~12s
Result: frame sequence kept advancing, browser status stayed ready, no crash
Final batch: {:count 131, :elapsedMs ~0.8}
```

Performance validation after the crash hardening and stable light-uniform cache:

```text
Noel FPS probe: 157 frames / 5.005180458s = 31.37 FPS
Last browser batch: {:count 131, :elapsedMs ~0.8}
Deep draw profile: draw-lights avg ~2.29 ms, browser batch still <1 ms
```

The retained performance work in this pass caches stable `draw-lights` uniform
maps, reducing repeated `lightVPs`/base-light uniform walking when the cached
shadow state is unchanged. The main remaining cost is still shader `with-fx`
multipass work and Clojure-side frame orchestration, not browser-side wasm
execution.

## Latest Follow-Up: Shader-Pass Opcode

A browser-host opcode now collapses each shader multipass blit into one queued
command while still executing the same real Raylib wasm calls in order:
`BeginTextureMode`, `BeginShaderMode`, `ClearBackground`, `DrawTextureRec`,
`EndShaderMode`, and `EndTextureMode`.

Kept result:

```text
Noel FPS probe: 163 frames / 5.010568667s = 32.53 FPS
Last browser batch: {:count 111, :elapsedMs ~0.8}
Call profile after shader-pass opcode:
  DrawTextureRec: 462 calls / ~143 ms over 3s sample
  BeginTextureMode: 829 calls / ~133 ms over 3s sample
Previous same profile shape:
  DrawTextureRec: 831 calls / ~293 ms
  BeginTextureMode: 1201 calls / ~211 ms
```

Rejected experiment:

- A broader no-shader render-texture copy opcode reduced batch count further to
  about `99`, but regressed the FPS probe to about `21.2 FPS`. It was reverted.
  The retained opcode is limited to shader multipass blits, where the measured
  call count/time reduction did not introduce that regression.

## Latest Follow-Up: Uniform Pass Cache And Close-State Snapshot

Two small optimizations were retained after profiling Noel with the real
browser-hosted Raylib/Wasm window:

- Stable post-process shader parameter maps are cached per shader before the
  multipass draw. Dynamic values still update when their value changes, but
  stable pass uniforms no longer walk the full `set-uniforms` path every frame.
- `WindowShouldClose` no longer performs a separate Chrome DevTools call. The
  browser host already tracks `closeRequested`, so the Clojure side now reads it
  from the once-per-frame input snapshot.

Measured effect:

```text
Before pass-param cache:
  vybe.game/set-uniforms: ~82.5 ms over 3s deep profile
  :set-uniform :u_color:  ~83.9 ms over 3s deep profile

After pass-param cache:
  vybe.game/set-uniforms: ~31.6 ms over 3s deep profile
  repeated :u_color pass disappeared from hot profile

After close-state snapshot:
  WindowShouldClose removed from the call profile hot list

Noel FPS probe after retained changes:
  181 frames / 5.0083745s = 36.14 FPS
  browser batch count: 111
  browser batch elapsed: ~0.8 ms
```

Rejected experiment in this pass:

- A hand-written direct serializer for `BeginTextureMode`, `DrawTextureRec`, and
  the shader-pass opcode was tested. It increased measured Clojure-side time for
  those hot calls, so it was reverted. The existing compact-spec path remains.

The next meaningful target is still reducing full-screen render/effect passes
and/or moving more of the render command assembly into a typed command buffer or
wasm kernel. Small serialization-only changes are no longer enough to close the
remaining gap to 120 FPS.

## Latest Follow-Up: Rejected Ray/Input Round-Trip Experiments

Two additional bridge-reduction experiments were tested and rejected:

- Local Clojure implementation of `VyGetScreenToWorldRay`: it matched the wasm
  result closely (`~1e-5` to `~2e-5` direction delta in Noel), but the Clojure
  matrix inversion/unprojection path dropped the plain FPS probe to about
  `29 FPS`. The wasm/browser call remains faster for this path.
- Returning the next input snapshot from `callBatch`: this removed no hot Raylib
  draw call and increased the amount of data returned by the frame flush. The
  FPS probe stayed below the retained path, so the separate input snapshot call
  remains.

Current retained path remains the shader-pass opcode plus stable uniform/light
caches. Future work should move such math into a compiled wasm/native kernel or
reduce whole render/effect passes, not port hot matrix math to plain Clojure.

## Native Raylib Wasm Host Target

The current browser-hosted Raylib wasm path is a proof and debugging path, not
the best path to 120 FPS. The browser batch itself is now usually below 1 ms, but
Clojure still pays for host orchestration around a Chrome process, DevTools
transport, JSON command payloads, browser event translation, and WebGL hosting.
Those costs do not belong in the final desktop runtime.

The performance target is a native Raylib wasm host:

1. Raylib remains compiled to wasm.
2. The JVM host owns the desktop window, graphics context, input, timing, file,
   and audio imports.
3. `vybe.raylib.c` talks to `vybe.raylib.host`, not directly to the browser/CDP
   implementation.
4. Rendering calls cross the host boundary as direct calls/typed memory, not JSON
   and not Chrome DevTools evaluations.
5. Missing native host imports fail loudly until implemented; they are not
   replaced by silent draw stubs.

The expected FPS gain comes from removing the browser/CDP command transport and
then optimizing state upload directly against the native host. The first step is
therefore architectural: add the host boundary and make the native host the
explicit implementation target. After that, implementation proceeds through a
minimal desktop window/context, then real GL imports, then texture/shader/mesh
coverage, then Noel validation.

## 2026-04-27 Native Desktop Host Track

The performance target has moved from browser-hosted Raylib wasm to a native
Raylib-wasm host backed by LWJGL. This keeps Raylib C compiled to wasm and uses
LWJGL only for the low-level `gl*`/`glfw*` host imports. It removes the
browser/CDP/JSON path from the target performance architecture.

Completed in this pass:

1. Public `vybe.raylib.c` functions are generated from the Raylib wasm ABI and
   call the Raylib wasm exports through the LWJGL host.
2. `vybe.c` generated wasm kernels can call Raylib matrix/vector helpers through
   raw host imports with correct aggregate return ABI.
3. `minimal` and `noel` both load through the desktop wasm/LWJGL path. Noel ran
   until a timeout wrapper killed it, rather than failing at namespace load.
4. The first custom render helper, `draw-texture-shader-pass`, is implemented as
   real Raylib wasm calls rather than a browser-only shortcut.

Next FPS work for the LWJGL path:

1. Finish host import coverage for framebuffer, renderbuffer, texture readback,
   mipmap, shader uniform vector, and instancing paths as real LWJGL forwards.
2. Add low-overhead host-side buffers for repeated pointer-to-buffer marshalling
   so draw calls do not allocate direct buffers unnecessarily.
3. Reduce reflection in `vybe.raylib.lwjgl.wasm`; the high-volume `long[]` host
   argument path is now type-hinted, but several overloaded LWJGL calls still need
   explicit casts.
4. Profile Noel after the full render path is visible and stable. The goal is to
   separate time spent in Raylib wasm, LWJGL host imports, Flecs/Jolt simulation,
   and Clojure-side game orchestration.

Additional Noel validation:

A 25 second timeout run reached Overtone startup, established the game nREPL, and
remained alive until killed by the timeout wrapper. No missing Raylib import was
reported during that run. The next measurement should be an interactive visual
run with audio either already warm or temporarily bypassed at the game level, so
the timeout is spent on actual frames rather than Overtone boot.

## 2026-04-27 LWJGL Host Correctness Update

The active 120 FPS path is now the native desktop Raylib-wasm host backed by
LWJGL, not the browser/CDP/JSON host.

Completed since the previous note:

1. Raylib wasm now builds as `GRAPHICS_API_OPENGL_ES3` for the desktop LWJGL host.
   This fixed the shadow/render texture framebuffer creation failure in Noel's
   `draw-lights` path.
2. ES3 VAO/draw-buffer imports are forwarded directly to LWJGL OpenGL calls.
3. Matrix-array uniform uploads are marshalled into wasm memory for
   `VySetShaderValueMatrixV`.
4. Panama/native out-pointer arguments are copied into wasm temps and copied back
   after the call. This fixed Noel camera movement crashes in
   `QuaternionToAxisAngle` without changing `vybe.game` behavior.
5. `minimal` and `noel` both ran bounded real app sessions through
   `:dev:wasm-lwjgl:wasm-lwjgl-macos-arm64` without Raylib missing-import,
   framebuffer, uniform vector, or wasm memory crashes.

Current run command for Noel on Apple Silicon macOS:

```sh
cd /Users/pfeodrippe/dev/vybe-games
VYBE_NREPL_PORT=7899 clj -M:dev:wasm-lwjgl:wasm-lwjgl-macos-arm64 -m noel
```

Performance next steps:

1. Run a visual interactive FPS pass after Overtone is warm so startup does not
   pollute frame-time measurements.
2. Measure host import overhead on the LWJGL path. The browser JSON/CDP transport
   is no longer in the target loop, so remaining hotspots should be direct wasm
   calls, OpenGL host calls, Flecs/Jolt simulation, audio, and Clojure game logic.
3. Investigate the one-time Apple GL texture/sampler warning before accepting
   visual correctness for textured Noel content.
4. Keep `InterpreterFallback/FAIL`; do not use interpreted wasm to hide compile
   failures.

## 2026-04-27 Runtime Stability Fix For 120 FPS Work

Before further FPS tuning, the wasm runtime had to stop corrupting memory during
real Noel frames. The current stability fixes are now part of the performance
baseline:

1. Flecs wrapper calls restore the Flecs wasm module as the default module so
   component reads do not accidentally use Raylib/Jolt/generated-kernel memory.
2. `with-arena-root` in `vybe.wasm` is no longer a no-op; it suspends per-frame
   scoped allocation tracking for long-lived Flecs query/system allocations.
3. Scoped allocation untracking is keyed by shared linear memory plus pointer,
   allowing generated kernels and their owner modules to share memory safely.
4. Generated-kernel heap reservation is lifetime-scoped to the module, not the
   frame arena, and no-op generated heaps do not abort no-allocation systems.

Validation result: Noel ran for a bounded 30 seconds through
`:dev:wasm-lwjgl:wasm-lwjgl-macos-arm64` and only produced the expected Overtone
shutdown exception from the controlled kill. This removes the previous
`free-all!`, `ecs_query_next`, and generated-kernel malloc crashes from the active
120 FPS path.

Next FPS pass should now measure actual frame time instead of chasing wasm memory
corruption. The first target remains the one-time Apple GL texture/sampler warning
and any texture upload/sampler state mismatch behind it.

## 2026-04-27 LWJGL Performance Pass: Web Sleep And Transform Kernel

The first steady-state Noel probe on the LWJGL-hosted Raylib wasm path measured
about `15 FPS`. Profiling showed that the largest single issue was not rendering:
`WindowShouldClose` was still calling Raylib's `PLATFORM_WEB` implementation,
which executes `emscripten_sleep(16)`. For the desktop LWJGL host this is wrong;
close state belongs to GLFW. `vybe.raylib.impl/invoke-raylib!` now answers
`WindowShouldClose` directly from the LWJGL host (`glfwWindowShouldClose`) and no
longer calls the web sleep export.

Validation after that change:

```text
Noel LWJGL FPS probe:
  103 frames / 5.0100185s = 20.56 FPS
```

The next profile showed thousands of Raylib wasm host calls from the generated
transform system: `MatrixScale`, `QuaternionToMatrix`, and `MatrixMultiply` were
used inside a `vc/defn*` helper. That code is now direct matrix math inside the
same generated wasm kernel, preserving the previous Raylib result exactly while
removing the host-import round trips.

Correctness check:

```text
matrix-transform direct wasm math vs previous Raylib composition: max diff 0.0
```

Validation after the transform-kernel change:

```text
Noel LWJGL FPS probe:
  153 frames / 5.010022209s = 30.54 FPS
```

A real Raylib wasm helper was also added for the common shader blit pass:
`VyDrawTextureShaderPass`. It is implemented in C and compiled into
`raylib.wasm`; the existing `vr.c/draw-texture-shader-pass` custom method now
calls that single wasm export instead of issuing six separate Clojure-to-wasm
calls. This is not a stub and not a host-side rendering rewrite; it runs the same
Raylib calls inside the Raylib wasm module.

Validation after the shader-pass helper:

```text
Noel LWJGL FPS probe:
  157 frames / 5.007731s = 31.35 FPS
```

Latest profile shape after retained changes:

```text
noel/draw avg:                 ~34 ms
vybe.game/-with-fx avg:         ~3.85 ms, called 5x/frame
vybe.raylib.impl/invoke-raylib: ~2730 ms / 26257 calls over 5s sample
vybe.flecs/progress avg:        ~9.56 ms
Top Raylib wasm calls:
  VyDrawTextureShaderPass: 588 calls / ~686 ms
  BeginTextureMode:       1323 calls / ~450 ms
  DrawTextureRec:          735 calls / ~320 ms
  MatrixMultiply:         5592 calls / ~266 ms
```

Current conclusion: the easy web-platform and generated-transform host-call bugs
are fixed. The remaining gap to 120 FPS is dominated by real render/effect pass
count, render-target switches, texture blits, and remaining Clojure orchestration
around `with-fx`, plus some remaining MatrixMultiply-heavy skinning/light code.
The next useful work is not another compatibility layer; it is either reducing
full-screen pass count or moving more pass/skinning assembly into real wasm
helpers/kernels.

Next concrete targets:

1. Add focused timing for each `with-fx` call site in Noel so the engine can
   remove or fuse the most expensive passes without guessing.
2. Move remaining high-volume matrix multiplication in skinning/light setup into
   generated wasm helpers where inputs are already contiguous.
3. Consider a Raylib-wasm helper for common non-shader render-texture copy passes
   only after profiling proves the pass shape is stable; the browser-era broad
   copy opcode regressed, so this must be measured on LWJGL separately.
4. Keep `InterpreterFallback/FAIL` and the LWJGL host path. Do not reintroduce
   browser JSON/CDP, `backend/wasm?`, or dylib fallback branches.
