#include "flecs.h"
#define RAYMATH_IMPLEMENTATION
#include "../raylib/src/raymath.h"

// -- Types.
#define vy(n) ("C_vybe!!type/" "" #n)
#define vyk(n) #n
#define vyi(n) __Vy_##n

typedef struct vyi(Translation) {
    float x;                // Vector x component
    float y;                // Vector y component
    float z;                // Vector z component
} vyi(Translation);

typedef struct vyi(Scale) {
    float x;                // Vector x component
    float y;                // Vector y component
    float z;                // Vector z component
} vyi(Scale);

typedef Matrix vyi(Transform);
typedef Vector4 vyi(Rotation);

ecs_entity_t vybe_pair(ecs_entity_t e1, ecs_entity_t e2);
ecs_entity_t vybe_pair_first(const ecs_world_t *world, ecs_entity_t pair);
ecs_entity_t vybe_pair_second(const ecs_world_t *world, ecs_entity_t pair);
void vybe_rest_enable(ecs_world_t *world);

// Zig.
void vybe_default_systems(ecs_world_t *world);
void vybe_setup_allocator(void);

// Tests.
int vybe__test__rest_issue(bool is_rest_enabled);

// -- Pair.
ecs_entity_t vybe_pair(ecs_entity_t e1, ecs_entity_t e2)
{
    return (ECS_PAIR | ecs_entity_t_comb(e2, e1));
}

ecs_entity_t vybe_pair_first(const ecs_world_t *world, ecs_entity_t pair)
{
    return ecs_get_alive(world, ECS_PAIR_FIRST(pair));
}

ecs_entity_t vybe_pair_second(const ecs_world_t *world, ecs_entity_t pair)
{
    return ecs_get_alive(world, ECS_PAIR_SECOND(pair));
}

void vybe_rest_enable(ecs_world_t *world) {
    // Optional, gather statistics for explorer
    ECS_IMPORT(world, FlecsStats);

    // Creates REST server on default port (27750)
    ecs_singleton_set(world, EcsRest, {0});
}

// ---------- TESTS

int __VYBE_TEST_ACC = 0;

void __UpdateCamera(ecs_iter_t *it) {
    if (ecs_iter_changed(it)) {
        __VYBE_TEST_ACC++;
    } else {
        ecs_iter_skip(it);
    }
}

int vybe__test__rest_issue(bool is_rest_enabled) {
    __VYBE_TEST_ACC = 0;

    ecs_world_t *world = ecs_init();

    ecs_entity_t e1 = ecs_new(world);
    ecs_entity_t camera_active = ecs_new(world);
    ecs_entity_t comp1 = ecs_new(world);

    ecs_add_id(world, camera_active, EcsCanToggle);

    if (is_rest_enabled) {
        ECS_IMPORT(world, FlecsStats);
        ecs_singleton_set(world, EcsRest, {0});
    }

    ecs_add_id(world, e1, camera_active);

    //ECS_SYSTEM(world, UpdateCamera, EcsOnUpdate, [in] camera_active, [in] comp1);
    ecs_system(world, {
            .entity = ecs_entity(world, {
                .name = "UpdateCamera",
                .add = ecs_ids( ecs_dependson(EcsOnUpdate) )
            }),
            .query.terms = {
                { .id = camera_active, .inout = EcsIn },
                { .id = comp1, .inout = EcsIn }
            },
            .callback = __UpdateCamera
        }
    );

    ecs_add_id(world, e1, comp1);
    ecs_progress(world, 0.1);

    ecs_remove_id(world, e1, comp1);
    ecs_add_id(world, e1, comp1);
    ecs_progress(world, 0.1);

    ecs_fini(world);

    return __VYBE_TEST_ACC;
}

// --------- OLD
// vyi(Transform) vybe_matrix_transform(vyi(Translation) translation, vyi(Rotation) rotation, vyi(Scale) scale) {
//     Matrix matScale = MatrixScale(scale.x, scale.y, scale.z);
//     Matrix matRotation = QuaternionToMatrix((Quaternion) rotation);
//     Matrix matTranslation = MatrixTranslate(translation.x, translation.y, translation.z);

//     return (vyi(Transform)) MatrixMultiply(
//         MatrixMultiply(matScale, matRotation),
//         matTranslation
//     );
// }

// void vybe_transform(ecs_iter_t *it) {
//     vyi(Translation) *pos = ecs_field(it, vyi(Translation), 0);
//     vyi(Rotation) *rot = ecs_field(it, vyi(Rotation), 1);
//     vyi(Scale) *scale = ecs_field(it, vyi(Scale), 2);
//     vyi(Transform) *transformGlobal = ecs_field(it, vyi(Transform), 3);
//     vyi(Transform) *transformLocal = ecs_field(it, vyi(Transform), 4);
//     vyi(Transform) *transformParent;

//     bool isParentSet = ecs_field_is_set(it, 5);

//     if (isParentSet) {
//         transformParent = ecs_field(it, vyi(Transform), 5);
//     }

//     for (int i = 0; i < it->count; i++) {
//         vyi(Transform)* iTransformGlobal = &transformGlobal[i];
//         vyi(Transform)* iTransformLocal = &transformLocal[i];

//         Matrix local = vybe_matrix_transform(pos[i], rot[i], scale[i]);
//         *iTransformLocal = local;

//         if (isParentSet) {
//             *iTransformGlobal = MatrixMultiply(local, transformParent[0]);
//         } else {
//             *iTransformGlobal = local;
//         }
//     }
// }

// void vybe_default_systems__old(ecs_world_t *world) {
//     ecs_entity_t systemId = ecs_system(world, {
//             .entity = ecs_entity(world, {
//                 .name = "vybe_transform",
//                 .add = ecs_ids( ecs_dependson(EcsOnUpdate) )
//             }),
//             .query.terms = {
//                 { .first.name = vy(Translation), .inout = EcsIn },
//                 { .first.name = vy(Rotation), .inout = EcsIn },
//                 { .first.name = vy(Scale), .inout = EcsIn },
//                 {
//                     .first.name = vy(Transform),
//                     .second.name = vyk(global),
//                     .inout = EcsOut,
//                 },
//                 {
//                     .first.name = vy(Transform),
//                     .inout = EcsOut,
//                 },
//                 {
//                     .first.name = vy(Transform),
//                     .second.name = vyk(global),
//                     .src.id = EcsCascade|EcsUp,
//                     .inout = EcsIn,
//                     .oper = EcsOptional
//                 },
//             },
//             .callback = vybe_transform
//         }
//     );

//     ecs_assert(systemId != 0, ECS_INVALID_PARAMETER, "failed to create system");
// }

