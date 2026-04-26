#include "Jolt/Jolt.h"
#include "Jolt/Core/JobSystemSingleThreaded.h"
#include "Jolt/Physics/Collision/ContactListener.h"
#include "Jolt/Physics/Collision/BroadPhase/BroadPhaseLayer.h"
#include "Jolt/Physics/Collision/BroadPhase/BroadPhaseLayerInterfaceTable.h"
#include "Jolt/Physics/Collision/BroadPhase/ObjectVsBroadPhaseLayerFilterTable.h"
#include "Jolt/Physics/Collision/ObjectLayer.h"
#include "Jolt/Physics/Collision/ObjectLayerPairFilterTable.h"
#include "Jolt/Physics/PhysicsSystem.h"
#include "JoltPhysicsSharp/src/joltc/joltc.h"
#include "bin/vybe_jolt_wasm.h"

extern "C" int32_t vybe_jolt_contact_validate(uint32_t callback_id,
                                              uint32_t body1,
                                              uint32_t body2,
                                              uint32_t base_offset,
                                              uint32_t collision_result);
extern "C" void vybe_jolt_contact_added(uint32_t callback_id,
                                        uint32_t body1,
                                        uint32_t body2,
                                        uint32_t manifold,
                                        uint32_t settings);
extern "C" void vybe_jolt_contact_persisted(uint32_t callback_id,
                                            uint32_t body1,
                                            uint32_t body2,
                                            uint32_t manifold,
                                            uint32_t settings);
extern "C" void vybe_jolt_contact_removed(uint32_t callback_id,
                                          uint32_t sub_shape_pair);

namespace {
static constexpr JPH::ObjectLayer kLayerNonMoving = 0;
static constexpr JPH::ObjectLayer kLayerMoving = 1;
static constexpr JPH::BroadPhaseLayer kBroadPhaseNonMoving(0);
static constexpr JPH::BroadPhaseLayer kBroadPhaseMoving(1);

template <typename T>
uint32_t ptr32(T *ptr) {
  return static_cast<uint32_t>(reinterpret_cast<uintptr_t>(ptr));
}

template <typename T>
uint32_t ptr32(const T *ptr) {
  return static_cast<uint32_t>(reinterpret_cast<uintptr_t>(ptr));
}

static JPH::BroadPhaseLayerInterfaceTable *broadPhaseLayerInterface = nullptr;
static JPH::ObjectLayerPairFilterTable *objectLayerPairFilter = nullptr;
static JPH::ObjectVsBroadPhaseLayerFilterTable *objectVsBroadPhaseLayerFilter = nullptr;

void ensure_default_filters() {
  if (broadPhaseLayerInterface) {
    return;
  }

  broadPhaseLayerInterface = new JPH::BroadPhaseLayerInterfaceTable(2, 2);
  broadPhaseLayerInterface->MapObjectToBroadPhaseLayer(kLayerNonMoving, kBroadPhaseNonMoving);
  broadPhaseLayerInterface->MapObjectToBroadPhaseLayer(kLayerMoving, kBroadPhaseMoving);

  objectLayerPairFilter = new JPH::ObjectLayerPairFilterTable(2);
  objectLayerPairFilter->EnableCollision(kLayerNonMoving, kLayerMoving);
  objectLayerPairFilter->EnableCollision(kLayerMoving, kLayerMoving);

  objectVsBroadPhaseLayerFilter =
      new JPH::ObjectVsBroadPhaseLayerFilterTable(*broadPhaseLayerInterface,
                                                  2,
                                                  *objectLayerPairFilter,
                                                  2);
}

struct VybeContactCallbacks {
  JPC_ContactListenerVTable *vtbl;
  JPC_ContactListenerVTable vtable;
  uint32_t validate_cb;
  uint32_t added_cb;
  uint32_t persisted_cb;
  uint32_t removed_cb;
};

JPC_ValidateResult vybe_contact_validate(void *in_self,
                                         const JPC_Body *in_body1,
                                         const JPC_Body *in_body2,
                                         const JPC_Real in_base_offset[3],
                                         const JPC_CollideShapeResult *in_collision_result) {
  auto callbacks = static_cast<VybeContactCallbacks *>(in_self);
  if (!callbacks->validate_cb) {
    return JPC_VALIDATE_RESULT_ACCEPT_ALL_CONTACTS;
  }
  int32_t result = vybe_jolt_contact_validate(
      callbacks->validate_cb,
      ptr32(in_body1),
      ptr32(in_body2),
      ptr32(in_base_offset),
      ptr32(in_collision_result));
  return static_cast<JPC_ValidateResult>(result);
}

void vybe_contact_added(void *in_self,
                        const JPC_Body *in_body1,
                        const JPC_Body *in_body2,
                        const JPC_ContactManifold *in_manifold,
                        JPC_ContactSettings *io_settings) {
  auto callbacks = static_cast<VybeContactCallbacks *>(in_self);
  if (callbacks->added_cb) {
    vybe_jolt_contact_added(callbacks->added_cb,
                            ptr32(in_body1),
                            ptr32(in_body2),
                            ptr32(in_manifold),
                            ptr32(io_settings));
  }
}

void vybe_contact_persisted(void *in_self,
                            const JPC_Body *in_body1,
                            const JPC_Body *in_body2,
                            const JPC_ContactManifold *in_manifold,
                            JPC_ContactSettings *io_settings) {
  auto callbacks = static_cast<VybeContactCallbacks *>(in_self);
  if (callbacks->persisted_cb) {
    vybe_jolt_contact_persisted(callbacks->persisted_cb,
                                ptr32(in_body1),
                                ptr32(in_body2),
                                ptr32(in_manifold),
                                ptr32(io_settings));
  }
}

void vybe_contact_removed(void *in_self,
                          const JPC_SubShapeIDPair *in_sub_shape_pair) {
  auto callbacks = static_cast<VybeContactCallbacks *>(in_self);
  if (callbacks->removed_cb) {
    vybe_jolt_contact_removed(callbacks->removed_cb, ptr32(in_sub_shape_pair));
  }
}

}

extern "C" JPC_PhysicsSystem *vybe_jolt_physics_system_create_default(
    uint32_t in_max_bodies,
    uint32_t in_num_body_mutexes,
    uint32_t in_max_body_pairs,
    uint32_t in_max_contact_constraints) {
  ensure_default_filters();
  return JPC_PhysicsSystem_Create(in_max_bodies,
                                  in_num_body_mutexes,
                                  in_max_body_pairs,
                                  in_max_contact_constraints,
                                  broadPhaseLayerInterface,
                                  objectVsBroadPhaseLayerFilter,
                                  objectLayerPairFilter);
}

extern "C" JPC_JobSystem *vybe_jolt_job_system_create_single_threaded(uint32_t in_max_jobs) {
  return reinterpret_cast<JPC_JobSystem *>(new JPH::JobSystemSingleThreaded(in_max_jobs));
}

extern "C" void *vybe_jolt_contact_listener_create(uint32_t validate_cb,
                                                   uint32_t added_cb,
                                                   uint32_t persisted_cb,
                                                   uint32_t removed_cb) {
  auto callbacks = new VybeContactCallbacks{};
  callbacks->vtbl = &callbacks->vtable;
  callbacks->vtable.OnContactValidate = validate_cb ? vybe_contact_validate : nullptr;
  callbacks->vtable.OnContactAdded = added_cb ? vybe_contact_added : nullptr;
  callbacks->vtable.OnContactPersisted = persisted_cb ? vybe_contact_persisted : nullptr;
  callbacks->vtable.OnContactRemoved = removed_cb ? vybe_contact_removed : nullptr;
  callbacks->validate_cb = validate_cb;
  callbacks->added_cb = added_cb;
  callbacks->persisted_cb = persisted_cb;
  callbacks->removed_cb = removed_cb;
  return callbacks;
}

extern "C" JPC_BodyID vybe_jolt_physics_system_cast_ray_body(const JPC_PhysicsSystem *phys,
                                                             float origin_x,
                                                             float origin_y,
                                                             float origin_z,
                                                             float direction_x,
                                                             float direction_y,
                                                             float direction_z) {
  JPC_RRayCast ray = {};
  ray.origin[0] = origin_x;
  ray.origin[1] = origin_y;
  ray.origin[2] = origin_z;
  ray.origin[3] = 1.0f;
  ray.direction[0] = direction_x;
  ray.direction[1] = direction_y;
  ray.direction[2] = direction_z;
  ray.direction[3] = 0.0f;

  JPC_RayCastResult hit = {};
  hit.body_id = JPC_BODY_ID_INVALID;
  hit.fraction = 1.0f + JPC_FLT_EPSILON;

  const JPC_NarrowPhaseQuery *query =
      JPC_PhysicsSystem_GetNarrowPhaseQueryNoLock(phys);
  if (JPC_NarrowPhaseQuery_CastRay(query, &ray, &hit, nullptr, nullptr, nullptr)) {
    return hit.body_id;
  }

  return JPC_BODY_ID_INVALID;
}
