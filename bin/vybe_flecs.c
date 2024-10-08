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

void vybe_default_systems(ecs_world_t *world);

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
