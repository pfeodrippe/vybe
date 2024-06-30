## Master

- Create `vg/start!` to remove some implementation details from games

## v0.1.75

- Make pointers (VybePMap) const if you use the default Flecs access modifier (:in)
- Use rotations from GLTF file to set physics
- Create VyBody and modify jolt functions to return it instead of just the ID

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
