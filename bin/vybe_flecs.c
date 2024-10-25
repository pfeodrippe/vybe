#include "flecs.h"

ecs_entity_t vybe_pair(ecs_entity_t e1, ecs_entity_t e2);
ecs_entity_t vybe_pair_first(const ecs_world_t *world, ecs_entity_t pair);
ecs_entity_t vybe_pair_second(const ecs_world_t *world, ecs_entity_t pair);
//void vybe_rest_enable(ecs_world_t *world);

void vybe_default_systems(ecs_world_t *world);
void vybe_setup_allocator(void);

//int vybe__test__rest_issue(bool is_rest_enabled);
