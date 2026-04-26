#pragma once

#include "JoltPhysicsSharp/src/joltc/joltc.h"

#ifdef __cplusplus
extern "C" {
#endif

JPC_BodyID vybe_jolt_physics_system_cast_ray_body(const JPC_PhysicsSystem *phys,
                                                  float origin_x,
                                                  float origin_y,
                                                  float origin_z,
                                                  float direction_x,
                                                  float direction_y,
                                                  float direction_z);

#ifdef __cplusplus
}
#endif
