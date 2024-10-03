const std = @import("std");

const f = @import("flecs_c.zig");
const world_t = f.ecs_world_t;
const entity_t = f.ecs_entity_t;

pub fn field(it: *f.ecs_iter_t, comptime T: type, index: i8) ?[]T {
    if (f.ecs_field_w_size(it, @sizeOf(T), index)) |anyptr| {
        const ptr = @as([*]T, @ptrCast(@alignCast(anyptr)));
        return ptr[0..@intCast(it.count)];
    }
    return null;
}

pub const Eid = struct { name: []u8 };

fn _eid(wptr: *world_t, T: anytype) entity_t {
    if (@TypeOf(T) == type) {
        const entity = f.ecs_entity_init(wptr, &.{
            .use_low_id = true,
            .name = @typeName(T),
        });

        _ = f.ecs_component_init(
            wptr,
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
    }

    switch (@typeInfo(@TypeOf(T))) {
        .Pointer => |_| {
            return f.ecs_entity_init(wptr, &.{ .name = T });
        },
        .Struct => |info| {
            if (info.fields.len == 0) {
                return f.ecs_entity_init(wptr, &.{});
            }
            if (info.is_tuple) {
                if (info.fields.len != 2) {
                    std.debug.panic(
                        "Invalid pair: {any}, the tuple should have exactly 2 elements",
                        .{T},
                    );
                }
                return make_pair(
                    _eid(wptr, T[0]),
                    _eid(wptr, T[1]),
                );
            }
            var desc = f.ecs_entity_desc_t{};
            desc.name = T.name;
            if (@hasField(@TypeOf(T), "parent")) {
                if (T.parent != null) {
                    desc.parent = T.parent.?;
                }
            }
            return f.ecs_entity_init(wptr, &desc);
        },
        .EnumLiteral => |_| {
            return f.ecs_entity_init(wptr, &.{ .name = @tagName(T) });
        },
        .Int => |_| {
            return T;
        },
        else => {
            std.debug.panic("Invalid type: {any}", .{T});
            return 0;
        },
    }
}

const Entity = struct {
    w: World,
    id: entity_t,

    pub fn has_id(self: Entity, id: anytype) bool {
        const w = self.w;
        return f.ecs_has_id(
            w.wptr,
            w.eid(self.id),
            w.eid(id),
        );
    }

    pub fn get(self: Entity, comptime T: type) ?T {
        const w = self.w;
        const entity = self.id;
        if (f.ecs_get_id(w.wptr, w.eid(entity), w.eid(T))) |ptr| {
            return cast(T, ptr).*;
        }
        return null;
    }

    pub fn set(self: Entity, comptime T: type, data: T) void {
        const w = self.w;
        const entity = self.id;
        if (T == *const anyopaque) {
            f.ecs_set_id(
                w.wptr,
                entity,
                w.eid(T),
                @sizeOf(T),
                data,
            );
        } else {
            f.ecs_set_id(
                w.wptr,
                entity,
                w.eid(T),
                @sizeOf(T),
                @as(*const anyopaque, @ptrCast(&data)),
            );
        }
    }

    pub fn add_id(self: Entity, id: anytype) void {
        const w = self.w;
        const entity = self.id;
        f.ecs_add_id(
            w.wptr,
            entity,
            w.eid(id),
        );
    }
};

const MergeParams = struct {
    parent: ?entity_t,
};

fn _merge(w: World, comptime hash_map: anytype, params: MergeParams) void {
    inline for (std.meta.fields(@TypeOf(hash_map))) |k| {
        const entity_id = w.eid(.{ .name = k.name, .parent = params.parent });
        const entity = w.ent(entity_id);
        const tuple = @as(k.type, @field(hash_map, k.name));

        //std.debug.print("entity {s}", .{k.name});

        inline for (std.meta.fields(@TypeOf(tuple))) |k2| {
            const casted = cast(k2.type, k2.default_value).*;
            //std.debug.print("  {any} : {any}\n", .{ casted, k2.type });

            switch (@typeInfo(k2.type)) {
                .Struct => |info| {
                    if (info.is_tuple) {
                        if (info.fields.len != 2) {
                            std.debug.panic("Invalid pair: {any}, the tuple should have exactly 2 elements", .{casted});
                        }
                        entity.add_id(make_pair(
                            w.eid(casted[0]),
                            w.eid(casted[1]),
                        ));
                    } else {
                        if (info.fields[0].is_comptime) {
                            // Children.
                            _merge(w, casted, .{ .parent = entity_id });
                        } else {
                            entity.set(k2.type, casted);
                        }
                    }
                },
                .EnumLiteral => |_| {
                    entity.add_id(w.eid(casted));
                },
                else => {
                    std.debug.panic("Invalid type: {any}", .{casted});
                    return 0;
                },
            }
        }
    }
}

const World = struct {
    wptr: *world_t,

    pub fn new() World {
        return World{ .wptr = f.ecs_init().? };
    }

    pub fn close(self: World) bool {
        return f.ecs_fini(self.wptr) == 0;
    }

    pub fn ent(self: World, entity: anytype) Entity {
        return Entity{ .w = self, .id = self.eid(entity) };
    }

    pub fn eid(self: World, entity: anytype) entity_t {
        return _eid(self.wptr, entity);
    }

    pub fn merge(self: World, comptime hash_map: anytype) void {
        _merge(
            self,
            hash_map,
            .{ .parent = null },
        );
    }
};

fn cast(comptime T: type, val: ?*const anyopaque) *const T {
    return @as(*const T, @ptrCast(@alignCast(val)));
}

pub fn make_pair(first: entity_t, second: entity_t) f.ecs_id_t {
    return f.ecs_make_pair(first, second);
}

fn Move(it: *f.ecs_iter_t) callconv(.C) void {
    var p = field(it, Position, 0).?;
    const v = field(it, Velocity, 1).?;

    for (0..@intCast(it.count)) |i| {
        p[i].x += v[i].x;
        p[i].y += v[i].y;
    }
}

const Position = struct { x: f32, y: f32 };
const Velocity = struct { x: f32, y: f32 };

test "raw basics" {
    const w = World.new();
    defer _ = w.close();

    const pos_id = w.eid(Position);
    const vel_id = w.eid(Velocity);

    var entity_desc = f.ecs_entity_desc_t{};
    entity_desc.id = w.eid(.{});
    entity_desc.name = "Move";
    entity_desc.add = &.{
        make_pair(f.EcsDependsOn, f.EcsOnUpdate),
        0,
    };

    var system_desc = f.ecs_system_desc_t{};
    system_desc.callback = Move;
    system_desc.entity = f.ecs_entity_init(w.wptr, &entity_desc);
    system_desc.query.terms[0] = .{
        .id = pos_id,
        .inout = f.EcsInOut,
    };
    system_desc.query.terms[1] = .{
        .id = vel_id,
        .inout = f.EcsIn,
    };

    const move = f.ecs_system_init(w.wptr, &system_desc);

    const e1 = w.eid(.{});
    w.ent(e1).set(Position, .{ .x = 4.3, .y = 5.0 });
    w.ent(e1).set(Velocity, .{ .x = 10.0, .y = 15.0 });

    const pos_id_2 = w.eid(Position);
    try std.testing.expectEqual(pos_id, pos_id_2);

    _ = f.ecs_run(w.wptr, move, @as(f32, 0.0), null);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        w.ent(e1).get(Position).?,
    );
}

test "preferred basics" {
    const w = World.new();
    defer _ = w.close();

    const pos_id = w.eid(Position);
    const vel_id = w.eid(Velocity);

    var entity_desc = f.ecs_entity_desc_t{};
    entity_desc.id = w.eid(.{});
    entity_desc.name = "Move";
    entity_desc.add = &.{
        make_pair(f.EcsDependsOn, f.EcsOnUpdate),
        0,
    };

    var system_desc = f.ecs_system_desc_t{};
    system_desc.callback = Move;
    system_desc.entity = f.ecs_entity_init(w.wptr, &entity_desc);
    system_desc.query.terms[0] = .{
        .id = pos_id,
        .inout = f.EcsInOut,
    };
    system_desc.query.terms[1] = .{
        .id = vel_id,
        .inout = f.EcsIn,
    };

    const move = f.ecs_system_init(w.wptr, &system_desc);

    w.merge(.{
        .e1 = .{
            Position{ .x = 4.3, .y = 5.0 },
            Velocity{ .x = 10.0, .y = 15.0 },
        },
        .e2 = .{
            Position{ .x = 5.3, .y = 6.0 }, Velocity{ .x = 11.0, .y = 16.0 },
            .{ .e4, .e6 },
            .{
                .e10 = .{
                    Position{ .x = 24.3, .y = 25.0 },
                    Velocity{ .x = 20.0, .y = -5.0 },
                },
            },
            .some_tag,
        },
    });

    _ = f.ecs_run(w.wptr, move, @as(f32, 0.0), null);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        w.ent(.e1).get(Position).?,
    );

    // e10 is a child of e2.
    try std.testing.expectEqual(
        Position{ .x = 44.3, .y = 20.0 },
        w.ent("e2.e10").get(Position).?,
    );

    try std.testing.expect(
        w.ent(.e2).has_id(.some_tag),
    );

    // Check if it has the pair (e4, e6).
    try std.testing.expect(
        // You can use a enum literal (.e4) or a string ("e4").
        w.ent(.e2).has_id(.{ "e4", .e6 }),
    );
}
