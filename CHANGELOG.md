## Master

- VybeC
  - Don't make things const by default as we don't get `const` info from the jextracts anyway
  - Add `vc/comptime`
  - Support nested components in VybeC
  - Static calls with no arguments are now called in compile time automatically
  - Support anonymous structs for referred components
  - Add long long support
  - Fix caching
  - Now it's working fine with the `vybe-games` builds
  - Create `vc/eval*` to debug standalone forms
  - Accept meta `^:void` in `if`/`cond` expressions to signal to the C compiler that we don't care about the return
  - Move animation-node-player to VybeC
    - CPU went from 84% to 63%
- Add `vf/defsystem-c`

## v0.7.469

- Make non-pointers constant by default
  - Support `^:mut`
- `vf/del` now can delete the entity itself as well
- Use safe eval for model loading
- Use dynamic fns by defaults in `vybe.c`
  - `::vc/standalone` flag was added so you can have the dynamic fns
    inlined again
  - Watch global fn pointers vars (if any) so we can have hot reloading
- Create `vybe.game.system` for default systems
- Create `vybe.math` for common math functions
- Allow `[:vg.anim/loop :vg.gltf.anim/some-animation]` tag, usable from blender
- Add `vg/with-drawing-fx` so it's easier to use effects
  - Add `vg/fx-painting` for a painting-like effect
- Add `vf/entity-debug` and `vf/systems-debug`
- Add `vj/ContactManifold`
- Improve error for VybeC compilation
- Add `mixer.fs`

## v0.7.444

- Support multiple GLTF scenes
- Use `pfeodrippe/sonic-pi` fork where we add the ATK plugin
- Add `vy.u/app-resource` for getting resource correctly
- Using the ATK plugin, we add a directional scsynthdef file to the project ( you can
  refer to it in  synths.scs file) (the synthdef file can be extracted in run time with
  `vy.u/app-resource`)
- Support immediate mode (same as no readonly mode) for systems with `:vf/immediate`
  - https://www.flecs.dev/flecs/md_docs_2Systems.html#immediate-systems
- Support typed pointers, e.g. `[:* [:* [:* :float]]]` corresponds to `float***`
- Add ability to inherit from a component (struct) with `comp-merge`,
  also add `comp-name` and `comp-fields`
- Create `vybe.c` to transpile from CLJ to C
  - Tested (using Github Actions) in OSX, Windows and Linux
- Use `clang` also for static analysis
- Can call generated C function
- Add `vp/p*`
- Add `vp/fnc` and `vp/defnc`
  - A `VybeComponent` that has function pointers fields described with `[:fn ...]`
    can receive a `VybeCFn` and a normal clojure function!
- Seamless integration, using Vybe wrappers, with jextract generated code
  - The macro/function acting as a wrapper has to contain `:vybe/fn-meta` with
    at least `:fn-desc` (function description) and `:fn-address` (address of the
    C function)
- Add `vp/update-aliases!` so we can create aliases for components for a better
  integration with jextract layouts
- Support `tap>` from C, you can use it as you would normally use from CLJ
  - Even by integrating with tools like Portal!
- Add ability to reset the globals in a cfn (useful for hot reloading, for example)

## v0.6.338

- Add target and slot support
- Move transform system to C
- Add IDerefable Ref
- Add zig_src for being able to write systems in zig instead of c
- Move vybe_transform from C to zig

## v0.4.302

- Add multiplayer
  - Add `vybe.network`
  - Support hole punchings (see `server.py`)
    - There is a digital ocean server that you can use to initiate the process
  - You can use `:vg/sync` for an entity that should be networked sync
  - You can use `:vg/networked` to say which components should be networked sync
- Add RayGui
- Support unions in components

## v0.4.165

- Besides OSX, the sample (in the vybe-games repo) is now known to work on a Windows

## v0.4.141

- Create github action for Mac, Linux and Windows
- Mac, Linux and Windows packages are now published by Github Actions

## v0.1.98

- Create `vg/start!` as the game wrapper so we can abstract some implementation details
- Add `vf/event!`
- Emit event when body contact is added
- Add `:vf/unique` component trait, it forces a component to be applied to one entity
  only, removing it from all the others entities
- Add on click and on hover events
- Support `:vf/disabled` for systems and observers
- Support getting source for any term (e.g. parent) by using :vf/entity
- Rename `vf/get-name` to `vf/get-rep`, re-add `vf/get-name`, but now it returns a string (flecs path)
- Add `vybe.clerk` ns
  - Add systems/observers charts
- Expose option to enable the remote (rest) api
- Add Clerk docs tab
- Add JoltPhysicSharp
  - Use latest Jolt (support for soft bodies!)
- Support linked GLTF meshes

## v0.1.75

- Make pointers (VybePMap) const if you use the default Flecs access modifier (:in)
- Use rotations from GLTF file to set physics
- Create VyBody and modify jolt functions to return it instead of just the ID
- Use `w` to as the storage for everything, remove `env`

## v0.1.67

- Allow dynamic flecs queries
- Add Flecs observers
- Allow datalog-like queries using Flecs
  - Query scopes
  - Variables
  - Sources
  - See https://github.com/SanderMertens/flecs/blob/v4/docs/Queries.md
- `:vg/dynamic` converts meshes to dynamic objects (AABB)

## v0.1.49

- Add `vf/ref` operator to refer to existing components
- Add `vf/del` operator to flag an component for deletion
- Add jolt
- Add raycast
- Create VybePOpaque to represent memory segments
- GLTF loading now also loads physics (statically)
  - Using the axis aligned bounding box (AABB) method only

## v0.1.23

- Fix memory leaks

## v0.1.18

- Now supporting skins
