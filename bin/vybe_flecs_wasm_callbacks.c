#include "flecs.h"

#include <stddef.h>
#include <stdint.h>

extern void vybe_flecs_system_callback(uintptr_t it, uintptr_t callback_ctx);

void vybe_flecs_system_trampoline(ecs_iter_t *it) {
    uintptr_t ctx = (uintptr_t)it->callback_ctx;
    if (!ctx) {
        ctx = (uintptr_t)it->run_ctx;
    }
    if (!ctx) {
        ctx = (uintptr_t)it->ctx;
    }
    vybe_flecs_system_callback((uintptr_t)it, ctx);
}

uintptr_t vybe_flecs_system_trampoline_addr(void) {
    return (uintptr_t)vybe_flecs_system_trampoline;
}

uintptr_t vybe_flecs_os_get_api_ptr(void) {
    return (uintptr_t)&ecs_os_api;
}

void vybe_flecs_os_set_api_ptr(uintptr_t api) {
    ecs_os_set_api((ecs_os_api_t*)api);
}

void vybe_flecs_enqueue_map(uintptr_t world, uintptr_t desc) {
    ecs_enqueue((ecs_world_t*)world, (ecs_event_desc_t*)desc);
}

ecs_entity_t vybe_flecs_observer_init_map(uintptr_t world, uintptr_t desc) {
    return ecs_observer_init((ecs_world_t*)world, (ecs_observer_desc_t*)desc);
}
