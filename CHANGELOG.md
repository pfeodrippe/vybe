## Master

- Support multiple GLTF scenes
- Use `pfeodrippe/sonic-pi` fork where we add the ATK plugin
- Add `vy.u/app-resource` for getting resource correctly
- Using the ATK plugin, we add a directional scsynthdef file, refer to
  the synths.scs file (the synthdef file can be extracted in run time with
  `vy.u/app-resource`)
- Support immediate mode (same as no readonly mode) for systems with `:vf/immediate`
  - https://www.flecs.dev/flecs/md_docs_2Systems.html#immediate-systems

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
