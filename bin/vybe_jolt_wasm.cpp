#include "Jolt/Jolt.h"
#include "Jolt/Core/JobSystemSingleThreaded.h"
#include "Jolt/Physics/Collision/BroadPhase/BroadPhaseLayer.h"
#include "Jolt/Physics/Collision/ObjectLayer.h"
#include "JoltPhysicsSharp/src/joltc/joltc.h"

namespace {
static constexpr JPH::ObjectLayer kLayerNonMoving = 0;
static constexpr JPH::ObjectLayer kLayerMoving = 1;
static constexpr JPH::BroadPhaseLayer kBroadPhaseNonMoving(0);
static constexpr JPH::BroadPhaseLayer kBroadPhaseMoving(1);

class BroadPhaseLayerInterface final : public JPH::BroadPhaseLayerInterface {
public:
  unsigned int GetNumBroadPhaseLayers() const override { return 2; }

  JPH::BroadPhaseLayer GetBroadPhaseLayer(JPH::ObjectLayer inLayer) const override {
    return inLayer == kLayerMoving ? kBroadPhaseMoving : kBroadPhaseNonMoving;
  }
#if defined(JPH_EXTERNAL_PROFILE) || defined(JPH_PROFILE_ENABLED)
  const char *GetBroadPhaseLayerName(JPH::BroadPhaseLayer inLayer) const override {
    return inLayer == kBroadPhaseMoving ? "moving" : "non-moving";
  }
#endif
};

class ObjectVsBroadPhaseLayerFilter final : public JPH::ObjectVsBroadPhaseLayerFilter {
public:
  bool ShouldCollide(JPH::ObjectLayer inLayer1, JPH::BroadPhaseLayer inLayer2) const override {
    if (inLayer1 == kLayerNonMoving) {
      return inLayer2 == kBroadPhaseMoving;
    }
    if (inLayer1 == kLayerMoving) {
      return true;
    }
    return false;
  }
};

class ObjectLayerPairFilter final : public JPH::ObjectLayerPairFilter {
public:
  bool ShouldCollide(JPH::ObjectLayer inObject1, JPH::ObjectLayer inObject2) const override {
    if (inObject1 == kLayerNonMoving) {
      return inObject2 == kLayerMoving;
    }
    if (inObject1 == kLayerMoving) {
      return true;
    }
    return false;
  }
};

static BroadPhaseLayerInterface broadPhaseLayerInterface;
static ObjectVsBroadPhaseLayerFilter objectVsBroadPhaseLayerFilter;
static ObjectLayerPairFilter objectLayerPairFilter;
}

extern "C" JPC_PhysicsSystem *vybe_jolt_physics_system_create_default(
    uint32_t in_max_bodies,
    uint32_t in_num_body_mutexes,
    uint32_t in_max_body_pairs,
    uint32_t in_max_contact_constraints) {
  return JPC_PhysicsSystem_Create(in_max_bodies,
                                  in_num_body_mutexes,
                                  in_max_body_pairs,
                                  in_max_contact_constraints,
                                  &broadPhaseLayerInterface,
                                  &objectVsBroadPhaseLayerFilter,
                                  &objectLayerPairFilter);
}

extern "C" JPC_JobSystem *vybe_jolt_job_system_create_single_threaded(uint32_t in_max_jobs) {
  return reinterpret_cast<JPC_JobSystem *>(new JPH::JobSystemSingleThreaded(in_max_jobs));
}
