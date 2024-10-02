const std = @import("std");

const f = @import("flecs_c.zig");
const world_t = f.ecs_world_t;
const entity_t = f.ecs_entity_t;

export fn vybe_eita(_: i32) entity_t {
    //_ = flecs.ecs_log_set_level(1);

    const world = f.ecs_init().?;
    return f.ecs_new(world);
    //return v + 30;
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

fn eid(w: *world_t, comptime T: anytype) entity_t {
    if (@TypeOf(T) == type) {
        const entity = f.ecs_entity_init(w, &.{
            .use_low_id = true,
            .name = @typeName(T),
        });

        _ = f.ecs_component_init(
            w,
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
    } else if (@TypeOf(T) == *const [2:0]u8) {
        return f.ecs_entity_init(w, &.{ .name = T });
    } else {
        if (@hasField(@TypeOf(T), "name")) {
            return f.ecs_entity_init(w, &.{ .name = T.name });
        } else {
            return f.ecs_entity_init(w, &.{});
        }
    }
}

pub fn cast(comptime T: type, val: ?*const anyopaque) *const T {
    return @as(*const T, @ptrCast(@alignCast(val)));
}

pub fn set(w: *world_t, entity: entity_t, comptime T: type, data: T) void {
    if (T == *const anyopaque) {
        f.ecs_set_id(
            w,
            entity,
            eid(w, T),
            @sizeOf(T),
            data,
        );
    } else {
        f.ecs_set_id(
            w,
            entity,
            eid(w, T),
            @sizeOf(T),
            @as(*const anyopaque, @ptrCast(&data)),
        );
    }
}

pub fn get(w: *f.ecs_world_t, entity: entity_t, comptime T: type) ?*const T {
    if (f.ecs_get_id(w, entity, eid(w, T))) |ptr| {
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

fn make_world() *world_t {
    return f.ecs_init().?;
}

test "raw basics" {
    const w = make_world();
    defer _ = f.ecs_fini(w);

    const pos_id = eid(w, Position);
    const vel_id = eid(w, Velocity);

    var entity_desc = f.ecs_entity_desc_t{};
    entity_desc.id = f.ecs_new(w);
    entity_desc.name = "Move";
    entity_desc.add = &.{
        f.ecs_make_pair(f.EcsDependsOn, f.EcsOnUpdate),
        f.EcsOnUpdate,
        0,
    };

    var system_desc = f.ecs_system_desc_t{};
    system_desc.callback = Move;
    system_desc.entity = f.ecs_entity_init(w, &entity_desc);
    system_desc.query.terms[0] = .{
        .id = pos_id,
        .inout = f.EcsInOut,
    };
    system_desc.query.terms[1] = .{
        .id = vel_id,
        .inout = f.EcsIn,
    };

    const move = f.ecs_system_init(w, &system_desc);

    const e1 = eid(w, .{});
    set(w, e1, Position, .{ .x = 4.3, .y = 5.0 });
    set(w, e1, Velocity, .{ .x = 10.0, .y = 15.0 });

    const pos_id_2 = eid(w, Position);
    try std.testing.expectEqual(pos_id, pos_id_2);

    _ = f.ecs_run(w, move, @as(f32, 0.0), null);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        get(w, e1, Position).?.*,
    );
}

fn merge(w: *world_t, comptime hash_map: anytype) void {
    inline for (std.meta.fields(@TypeOf(hash_map))) |k| {
        const entity = eid(w, .{ .name = k.name });
        const tuple = @as(k.type, @field(hash_map, k.name));

        inline for (std.meta.fields(@TypeOf(tuple))) |k2| {
            set(
                w,
                entity,
                k2.type,
                cast(k2.type, k2.default_value).*,
            );
        }
    }
}

test "preferred basics" {
    const w = make_world();
    defer _ = f.ecs_fini(w);

    const pos_id = eid(w, Position);
    const vel_id = eid(w, Velocity);

    var entity_desc = f.ecs_entity_desc_t{};
    entity_desc.id = f.ecs_new(w);
    entity_desc.name = "Move";
    entity_desc.add = &.{
        f.ecs_make_pair(f.EcsDependsOn, f.EcsOnUpdate),
        f.EcsOnUpdate,
        0,
    };

    var system_desc = f.ecs_system_desc_t{};
    system_desc.callback = Move;
    system_desc.entity = f.ecs_entity_init(w, &entity_desc);
    system_desc.query.terms[0] = .{
        .id = pos_id,
        .inout = f.EcsInOut,
    };
    system_desc.query.terms[1] = .{
        .id = vel_id,
        .inout = f.EcsIn,
    };

    const move = f.ecs_system_init(w, &system_desc);

    // TODO Use hashmap approach here
    merge(w, .{ .e1 = .{
        Position{ .x = 4.3, .y = 5.0 },
        Velocity{ .x = 10.0, .y = 15.0 },
    } });

    _ = f.ecs_run(w, move, @as(f32, 0.0), null);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        get(w, eid(w, "e1"), Position).?.*,
    );
}
