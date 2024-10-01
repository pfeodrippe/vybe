const std = @import("std");

const f = @import("flecs_c.zig");

export fn vybe_eita(_: i32) f.ecs_entity_t {
    //_ = flecs.ecs_log_set_level(1);

    const world = f.ecs_init().?;
    return f.ecs_new(world);
    //return v + 30;
}

fn LinkedList(comptime T: type) type {
    return struct {
        pub const Node = struct {
            prev: ?*Node,
            next: ?*Node,
            data: T,
        };

        first: ?*Node,
        last: ?*Node,
        len: usize,
    };
}

const Position = struct { x: f32, y: f32 };
const Velocity = struct { x: f32, y: f32 };

pub fn field(it: *f.ecs_iter_t, comptime T: type, index: i8) ?[]T {
    if (f.ecs_field_w_size(it, @sizeOf(T), index)) |anyptr| {
        const ptr = @as([*]T, @ptrCast(@alignCast(anyptr)));
        return ptr[0..@intCast(it.count)];
    }
    return null;
}

const Eid = struct { name: []u8 };

fn eid(world: *f.ecs_world_t, comptime T: anytype) f.ecs_entity_t {
    if (@TypeOf(T) == type) {
        const entity = f.ecs_entity_init(world, &.{
            .use_low_id = true,
            .name = @typeName(T),
        });

        _ = f.ecs_component_init(
            world,
            &.{
                .entity = entity,
                .type = .{
                    .alignment = @alignOf(T),
                    .size = @sizeOf(T),
                    .hooks = .{ .dtor = null },
                },
            },
        );
        return entity;
    } else {
        return f.ecs_entity_init(world, &.{});
    }
}

pub fn cast(comptime T: type, val: ?*const anyopaque) *const T {
    return @as(*const T, @ptrCast(@alignCast(val)));
}

pub fn set(world: *f.ecs_world_t, entity: f.ecs_entity_t, comptime T: type, data: T) void {
    f.ecs_set_id(
        world,
        entity,
        eid(world, T),
        @sizeOf(T),
        @as(*const anyopaque, @ptrCast(&data)),
    );
}

pub fn get(world: *f.ecs_world_t, entity: f.ecs_entity_t, comptime T: type) ?*const T {
    if (f.ecs_get_id(world, entity, eid(world, T))) |ptr| {
        return cast(T, ptr);
    }
    return null;
}

fn Move(it: *f.ecs_iter_t) callconv(.C) void {
    var p = field(it, Position, 0).?;
    const v = field(it, Velocity, 1).?;

    for (0..@intCast(it.count)) |i| {
        p[i].x += v[i].x;
        p[i].y += v[i].y;
    }
}

test "system" {
    const world = f.ecs_init().?;
    defer _ = f.ecs_fini(world);

    const pos_id = eid(world, Position);
    const vel_id = eid(world, Velocity);

    var entity_desc = f.ecs_entity_desc_t{};
    entity_desc.id = f.ecs_new(world);
    entity_desc.name = "Move";
    entity_desc.add = &.{
        f.ecs_make_pair(f.EcsDependsOn, f.EcsOnUpdate),
        f.EcsOnUpdate,
        0,
    };

    var system_desc = f.ecs_system_desc_t{};
    system_desc.callback = &Move;
    system_desc.entity = f.ecs_entity_init(world, &entity_desc);
    system_desc.query.terms[0] = .{
        .id = pos_id,
        .inout = f.EcsInOut,
    };
    system_desc.query.terms[1] = .{
        .id = vel_id,
        .inout = f.EcsIn,
    };

    const move = f.ecs_system_init(world, &system_desc);

    const e1 = eid(world, .{});
    set(world, e1, Position, .{ .x = 4.3, .y = 5.0 });
    set(world, e1, Velocity, .{ .x = 10.0, .y = 15.0 });

    const pos_id_2 = eid(world, Position);
    try std.testing.expectEqual(pos_id, pos_id_2);

    _ = f.ecs_run(world, move, @as(f32, 0.0), null);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        get(world, e1, Position).?.*,
    );
}
