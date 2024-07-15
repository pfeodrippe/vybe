## Master

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
