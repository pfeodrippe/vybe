#include "flecs.h"

ecs_entity_t vybe_pair(ecs_entity_t e1, ecs_entity_t e2);
ecs_entity_t vybe_pair_first(const ecs_world_t *world, ecs_entity_t pair);
ecs_entity_t vybe_pair_second(const ecs_world_t *world, ecs_entity_t pair);

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
