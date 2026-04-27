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
