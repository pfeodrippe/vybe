const std = @import("std");

const fc = @import("flecs_c.zig");
pub const world_t = fc.ecs_world_t;
pub const entity_t = fc.ecs_entity_t;
pub const id_t = fc.ecs_id_t;
const entity_desc_t = fc.ecs_entity_desc_t;
const system_desc_t = fc.ecs_system_desc_t;
const observer_desc_t = fc.ecs_observer_desc_t;
const query_desc_t = fc.ecs_query_desc_t;
const iter_t = fc.ecs_iter_t;
const term_t = fc.ecs_term_t;
const query_t = fc.ecs_query_t;

// C exports.
pub export fn vybe_setup_allocator() void {
    World.setup_allocator();
}

pub export fn vybe_pair(e1: entity_t, e2: entity_t) id_t {
    return make_pair(e1, e2);
}

pub export fn vybe_pair_first(wptr: *const world_t, pair: entity_t) id_t {
    return fc.ecs_get_alive(wptr, fc.ECS_PAIR_FIRST(pair));
}

pub export fn vybe_pair_second(wptr: *const world_t, pair: entity_t) id_t {
    return fc.ecs_get_alive(wptr, fc.ECS_PAIR_SECOND(pair));
}

/// From the zflecs project!
const EcsAllocator = struct {
    const AllocationHeader = struct {
        size: usize,
    };

    const Alignment = 16;

    var gpa: ?std.heap.GeneralPurposeAllocator(.{}) = null;
    var allocator: ?std.mem.Allocator = null;

    fn alloc(size: i32) callconv(.C) ?*anyopaque {
        if (size < 0) {
            return null;
        }

        const allocation_size = Alignment + @as(usize, @intCast(size));

        const data = allocator.?.alignedAlloc(u8, Alignment, allocation_size) catch {
            return null;
        };

        var allocation_header = @as(
            *align(Alignment) AllocationHeader,
            @ptrCast(@alignCast(data.ptr)),
        );

        allocation_header.size = allocation_size;

        return data.ptr + Alignment;
    }

    fn free(ptr: ?*anyopaque) callconv(.C) void {
        if (ptr == null) {
            return;
        }
        var ptr_unwrapped = @as([*]u8, @ptrCast(ptr.?)) - Alignment;
        const allocation_header = @as(
            *align(Alignment) AllocationHeader,
            @ptrCast(@alignCast(ptr_unwrapped)),
        );

        allocator.?.free(
            @as([]align(Alignment) u8, @alignCast(ptr_unwrapped[0..allocation_header.size])),
        );
    }

    fn realloc(old: ?*anyopaque, size: i32) callconv(.C) ?*anyopaque {
        if (old == null) {
            return alloc(size);
        }

        const ptr_unwrapped = @as([*]u8, @ptrCast(old.?)) - Alignment;

        const allocation_header = @as(
            *align(Alignment) AllocationHeader,
            @ptrCast(@alignCast(ptr_unwrapped)),
        );

        const old_allocation_size = allocation_header.size;
        const old_slice = @as([*]u8, @ptrCast(ptr_unwrapped))[0..old_allocation_size];
        const old_slice_aligned = @as([]align(Alignment) u8, @alignCast(old_slice));

        const new_allocation_size = Alignment + @as(usize, @intCast(size));
        const new_data = allocator.?.realloc(old_slice_aligned, new_allocation_size) catch {
            return null;
        };

        var new_allocation_header = @as(*align(Alignment) AllocationHeader, @ptrCast(@alignCast(new_data.ptr)));
        new_allocation_header.size = new_allocation_size;

        return new_data.ptr + Alignment;
    }

    fn calloc(size: i32) callconv(.C) ?*anyopaque {
        const data_maybe = alloc(size);
        if (data_maybe) |data| {
            @memset(@as([*]u8, @ptrCast(data))[0..@as(usize, @intCast(size))], 0);
        }

        return data_maybe;
    }
};

pub fn field(it: *fc.ecs_iter_t, comptime T: type, index: i8) ?[]T {
    if (fc.ecs_field_w_size(it, @sizeOf(T), index)) |anyptr| {
        const ptr = @as([*]T, @ptrCast(@alignCast(anyptr)));
        return ptr[0..@intCast(it.count)];
    }
    return null;
}

pub const Eid = struct {
    name: [:0]const u8,
    parent: ?entity_t,
};

// FIXME Put allocation into `World`.
const allocator_ = std.heap.page_allocator;

fn get_type(T: type) bool {
    switch (@typeInfo(T)) {
        .Struct => |info| {
            if (info.is_tuple) {
                if (info.fields.len != 2) {
                    std.debug.panic(
                        "Invalid pair: {any}, the tuple should have exactly 2 elements",
                        .{T},
                    );
                    @compileError("Invalid pair!");
                }
                return if (@TypeOf(T[0]) == type) T[0] else T[1];
            } else {
                return T;
            }
        },
        else => {
            std.debug.panic("Invalid type: {any}", .{T});
            return 0;
        },
    }
}

fn _eid(wptr: *world_t, T: anytype) entity_t {
    switch (@TypeOf(T)) {
        type => {
            if (allocator_.dupe(u8, @typeName(T))) |output| {
                defer allocator_.free(output);
                std.mem.replaceScalar(u8, output, '.', '!');
                const entity = fc.ecs_entity_init(wptr, &.{
                    .use_low_id = true,
                    .name = if (@hasDecl(T, "vybe_name")) T.vybe_name else output.ptr,
                });

                _ = fc.ecs_component_init(
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
            } else |_| unreachable;
        },
        Eid => {
            const eid = @as(Eid, T);
            var desc = fc.ecs_entity_desc_t{};
            desc.name = eid.name;
            if (T.parent != null) {
                desc.parent = T.parent.?;
            }
            return fc.ecs_entity_init(wptr, &desc);
        },
        else => {},
    }

    switch (@typeInfo(@TypeOf(T))) {
        .Pointer => |_| {
            // String.
            return fc.ecs_entity_init(wptr, &.{ .name = T });
        },
        .Struct => |info| {
            if (info.fields.len == 0) {
                return fc.ecs_entity_init(wptr, &.{});
            }
            //@compileLog(@TypeOf(T), info.is_tuple, info.fields.len);

            if (info.is_tuple) {
                if (info.fields.len != 2) {
                    std.debug.panic(
                        "Invalid pair: {any}, the tuple should have exactly 2 elements",
                        .{T},
                    );
                    @compileError("Invalid pair!");
                }
                return make_pair(
                    _eid(wptr, T[0]),
                    _eid(wptr, T[1]),
                );
            } else {
                //return _eid(wptr, @as(Eid, T));
                // It's a value.
                return _eid(wptr, @TypeOf(T));
            }
        },
        .EnumLiteral => |_| {
            return fc.ecs_entity_init(wptr, &.{ .name = @tagName(T) });
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

pub const Entity = struct {
    w: World,
    id: entity_t,

    pub fn name(self: Entity) ?[*:0]const u8 {
        const w = self.w;
        const entity_id = self.id;
        const c_name = fc.ecs_get_name(w.wptr, entity_id) orelse return null;
        return @as([*:0]const u8, c_name);
    }

    pub fn path(self: Entity) ?[*:0]const u8 {
        const w = self.w;
        const entity_id = self.id;
        const c_name = fc.ecs_get_path_w_sep(w.wptr, 0, entity_id, ".", null) orelse return null;
        return @as([*:0]const u8, c_name);
    }

    pub fn parent(self: Entity) ?Entity {
        const parent_id = fc.ecs_get_parent(self.w.wptr, self.id);
        if (parent_id == 0) {
            return null;
        } else {
            return self.w.ent(parent_id);
        }
    }

    pub fn has_id(self: Entity, id: anytype) bool {
        const w = self.w;
        return fc.ecs_has_id(
            w.wptr,
            w.eid(self.id),
            w.eid(id),
        );
    }

    pub fn get(self: Entity, comptime T: type) ?T {
        const w = self.w;
        const entity_id = self.id;
        if (fc.ecs_get_id(w.wptr, w.eid(entity_id), w.eid(T))) |ptr| {
            return cast(T, ptr).*;
        }
        return null;
    }

    /// Get pair component using rel as the type.
    pub fn get_1(self: Entity, comptime T: type, target: anytype) ?T {
        const w = self.w;
        const entity_id = self.id;
        if (fc.ecs_get_id(w.wptr, w.eid(entity_id), w.eid(.{ T, target }))) |ptr| {
            return cast(T, ptr).*;
        }
        return null;
    }

    /// Get pair component using target as the type.
    pub fn get_2(self: Entity, rel: anytype, comptime T: type) ?T {
        const w = self.w;
        const entity_id = self.id;
        if (fc.ecs_get_id(w.wptr, w.eid(entity_id), w.eid(.{ rel, T }))) |ptr| {
            return cast(T, ptr).*;
        }
        return null;
    }

    pub fn set(self: Entity, value: anytype) void {
        const w = self.w;
        const entity_id = self.id;
        const T = @TypeOf(value);
        //std.debug.print("---\nvalue: {any}\n", .{value});
        switch (@typeInfo(T)) {
            .Struct => |v| {
                if (v.is_tuple) {
                    if (v.fields.len != 2) {
                        std.debug.panic("Invalid pair: {any}, the tuple should have exactly 2 elements", .{value});
                    }
                    const c_id = make_pair(w.eid(value[0]), w.eid(value[1]));
                    if (fc.ecs_id_is_tag(w.wptr, c_id)) {
                        self.add_id(make_pair(w.eid(value[0]), w.eid(value[1])));
                    } else {
                        // Initialize everything to 0 if we only have the type.
                        const T0 = if (@TypeOf(value[0]) == type) value[0] else @TypeOf(value[0]);
                        const data_0 = if (@TypeOf(value[0]) == type) std.mem.zeroes(value[0]) else value[0];
                        const T1 = if (@TypeOf(value[1]) == type) value[1] else @TypeOf(value[1]);
                        const data_1 = if (@TypeOf(value[1]) == type) std.mem.zeroes(value[1]) else value[1];

                        //std.debug.print("---\nT: {any}\n", .{T});
                        fc.ecs_set_id(
                            w.wptr,
                            entity_id,
                            w.eid(value),
                            @sizeOf(if (@typeInfo(T0) == .Struct) T0 else T1),
                            @as(*const anyopaque, @ptrCast(&(if (@typeInfo(T0) == .Struct) data_0 else data_1))),
                        );
                    }
                } else {
                    fc.ecs_set_id(
                        w.wptr,
                        entity_id,
                        w.eid(T),
                        @sizeOf(T),
                        @as(*const anyopaque, @ptrCast(&value)),
                    );
                }
            },
            .Type => {
                // Initialize everything to 0 if we only have the type.
                // std.debug.print(
                //     "---\n{any} -- size {any}\n\n",
                //     .{ &std.mem.zeroes(value), @sizeOf(T) },
                // );
                fc.ecs_set_id(
                    w.wptr,
                    entity_id,
                    w.eid(value),
                    @sizeOf(value),
                    @as(*const anyopaque, @ptrCast(&std.mem.zeroes(value))),
                );
            },
            else => {
                @compileLog("Invalid value: ", value);
                @compileError("\n\n====== CHECK THE LOGS!! =======\n\n");
            },
        }
    }

    pub fn add_id(self: Entity, id: anytype) void {
        const w = self.w;
        const entity_id = self.id;
        fc.ecs_add_id(
            w.wptr,
            entity_id,
            w.eid(id),
        );
    }
};

const MergeParams = struct {
    parent: ?entity_t,
};

pub const World = struct {
    var has_allocator_setup = false;

    wptr: *world_t,

    pub fn setup_allocator() void {
        if (!has_allocator_setup) {
            has_allocator_setup = true;
            EcsAllocator.gpa = .{};
            EcsAllocator.allocator = EcsAllocator.gpa.?.allocator();

            fc.ecs_os_api.malloc_ = &EcsAllocator.alloc;
            fc.ecs_os_api.free_ = &EcsAllocator.free;
            fc.ecs_os_api.realloc_ = &EcsAllocator.realloc;
            fc.ecs_os_api.calloc_ = &EcsAllocator.calloc;
        }
    }

    pub fn new() World {
        setup_allocator();
        return World{ .wptr = fc.ecs_init().? };
    }

    pub fn from_ptr(wptr: *world_t) World {
        return World{ .wptr = wptr };
    }

    pub fn close(self: World) bool {
        return fc.ecs_fini(self.wptr) == 0;
    }

    pub fn ent(self: World, entity: anytype) Entity {
        return Entity{ .w = self, .id = self.eid(entity) };
    }

    pub fn eid(self: World, entity: anytype) entity_t {
        return _eid(self.wptr, entity);
    }

    pub fn merge(self: World, hash_map: anytype) void {
        _merge(
            self,
            hash_map,
            .{ .parent = null },
        );
    }

    pub fn progress(self: World, delta_time: f32) bool {
        return fc.ecs_progress(self.wptr, delta_time);
    }

    pub fn run(self: World, sys: anytype, delta_time: f32) Entity {
        return self.ent(fc.ecs_run(
            self.wptr,
            self.eid(sys),
            delta_time,
            null,
        ));
    }

    pub fn system(self: World, params: SystemParams, function_struct: anytype) Entity {
        return self.ent(_system(self, params, function_struct));
    }

    pub fn observer(self: World, params: ObserverParams, function_struct: anytype) Entity {
        return self.ent(_observer(self, params, function_struct));
    }
};

fn cast(comptime T: type, val: ?*const anyopaque) *const T {
    return @as(*const T, @ptrCast(@alignCast(val)));
}

pub fn make_pair(first: entity_t, second: entity_t) fc.ecs_id_t {
    return fc.ecs_make_pair(first, second);
}

fn Move(it: *fc.ecs_iter_t) callconv(.C) void {
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

    var entity_desc = fc.ecs_entity_desc_t{};
    entity_desc.id = w.eid(.{});
    entity_desc.name = "Move";
    entity_desc.add = &.{
        make_pair(fc.EcsDependsOn, fc.EcsOnUpdate),
        0,
    };

    var system_desc = fc.ecs_system_desc_t{};
    system_desc.callback = Move;
    system_desc.entity = fc.ecs_entity_init(w.wptr, &entity_desc);
    system_desc.query.terms[0] = .{
        .id = pos_id,
        .inout = fc.EcsInOut,
    };
    system_desc.query.terms[1] = .{
        .id = vel_id,
        .inout = fc.EcsIn,
    };

    const move = fc.ecs_system_init(w.wptr, &system_desc);

    const e1 = w.eid(.{});
    w.ent(e1).set(Position{ .x = 4.3, .y = 5.0 });
    w.ent(e1).set(Velocity{ .x = 10.0, .y = 15.0 });

    const pos_id_2 = w.eid(Position);
    try std.testing.expectEqual(pos_id, pos_id_2);

    _ = w.run(move, 0.0);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        w.ent(e1).get(Position).?,
    );
}

fn _merge(w: World, hash_map: anytype, params: MergeParams) void {
    inline for (std.meta.fields(@TypeOf(hash_map))) |k| {
        //std.debug.print("----\n parent {any}, name {s}\n\n", .{ params.parent, k.name });
        const entity_id = w.eid(Eid{ .name = k.name, .parent = params.parent });
        const entity = w.ent(entity_id);
        const tuple = @as(k.type, @field(hash_map, k.name));

        //std.debug.print("entity {s}", .{k.name});

        inline for (std.meta.fields(@TypeOf(tuple)), 0..) |k2, idx| {
            //std.debug.print("---\n{any} : {any}\n\n", .{ tuple[idx], k2.type });
            const casted = tuple[idx];

            switch (@typeInfo(k2.type)) {
                .Struct => |info| {
                    if (info.is_tuple) {
                        if (info.fields.len != 2) {
                            std.debug.panic("Invalid pair: {any}, the tuple should have exactly 2 elements", .{casted});
                        }
                        //std.debug.print("---\npair: {any}\n", .{casted});
                        entity.set(casted);
                        //entity.add_id(make_pair(w.eid(casted[0]), w.eid(casted[1])));
                    } else {
                        // std.debug.print(
                        //     "----\n info.fields {any}\n{any}\n{any}\n\n",
                        //     .{
                        //         k2.type,
                        //         info.fields[0].is_comptime,
                        //         //@typeInfo(k2.type),
                        //         null,
                        //     },
                        // );

                        // Hack to check for unnamed struct (which will be children).
                        const to_be_compared = "struct{";
                        if (comptime std.mem.eql(u8, @typeName(k2.type)[0..to_be_compared.len], to_be_compared)) {
                            // Children.
                            _merge(w, casted, .{ .parent = entity_id });
                        } else {
                            entity.set(casted);
                        }
                    }
                },
                .EnumLiteral => |_| {
                    entity.add_id(w.eid(casted));
                },
                .Type => {
                    entity.set(casted);
                },
                else => {
                    @compileLog("Invalid type: ", casted, @typeInfo(k2.type));
                    @compileError("\n\n====== CHECK THE LOGS!! =======\n\n");
                },
            }
        }
    }
}

fn get_main_type(comptime param: anytype) type {
    const is_optional = @typeInfo(param.type.?) == .Optional;
    // @compileLog("Param type: ", @typeInfo(param.type.?));
    // if (is_optional) {
    //     @compileLog("Param type: ", @typeInfo(param.type.?));
    // }
    return if (is_optional) @typeInfo(param.type.?).Optional.child else param.type.?;
}

const EachFnParsed = struct {
    callback: *const fn (*iter_t) callconv(.C) void,
};

fn _parse_each_fn(w: World, each_fn: anytype, query: *query_desc_t) EachFnParsed {
    const function = each_fn;
    const fn_params = @typeInfo(@TypeOf(function)).Fn.params;

    comptime var params_idx = 0;
    inline for (fn_params) |param| {
        const main_type = get_main_type(param);
        const is_optional = @typeInfo(param.type.?) == .Optional;
        const oper = if (is_optional) fc.EcsOptional else fc.EcsAnd;
        const term = &query.terms[params_idx];

        switch (@typeInfo(main_type)) {
            .Pointer => |pointer| {
                const param_type = pointer.child;
                const param_is_const = pointer.is_const;
                // std.debug.print("fn param: {any}, is_const = {any}\n", .{
                //     param_eid,
                //     param_is_const,
                // });

                if (param_type != iter_t) {
                    term.id = w.eid(param_type);
                    term.inout = if (param_is_const) fc.EcsIn else fc.EcsInOut;
                    term.oper = oper;

                    params_idx += 1;
                }
            },
            .Struct => |_| {
                const param_type = main_type;
                if (param_type.meta) |term_v| {
                    //@compileLog(param_type.meta);
                    const term_meta = @as(Term, term_v);
                    if (term_meta.flags) |flags| {
                        term.src.id = flags;
                    }
                }
                switch (param_type._type) {
                    .tag => {
                        term.id = w.eid(param_type.vybe_identifier);
                        term.inout = fc.EcsInOutNone;
                        term.oper = oper;

                        params_idx += 1;
                    },
                    .component => {
                        const pointer = @typeInfo(param_type.data_type).Pointer;
                        //@compileLog(param_type.meta);
                        const param_is_const = pointer.is_const;

                        term.id = w.eid(param_type.vybe_identifier);
                        term.inout = if (param_is_const) fc.EcsIn else fc.EcsInOut;
                        term.oper = oper;

                        params_idx += 1;
                    },
                    .iter => {},
                }
                //@compileLog("Struct param type: ", param_type.vybe_identifier);
            },
            else => {
                @compileLog("Invalid param: ", @typeInfo(main_type));
                @compileError("\n\n====== CHECK THE LOGS!! =======\n\n");
            },
        }

        // std.debug.print("==========================\n", .{});
        // for (system_desc.query.terms) |term| {
        //     if (term.id == 0) continue;
        //     std.debug.print("--\n{any}\n\n", .{term.oper});
        // }
    }

    // Build callback.
    const callback_struct = struct {
        fn exec(itptr: *iter_t) callconv(.C) void {
            //std.debug.print("ITER -------------==============\n", .{});

            comptime var field_types: [fn_params.len]type = undefined;
            inline for (fn_params, 0..) |param, idx| {
                const main_type = get_main_type(param);
                const is_optional = @typeInfo(param.type.?) == .Optional;
                switch (@typeInfo(main_type)) {
                    .Pointer => |pointer| {
                        const param_type = pointer.child;
                        switch (param_type) {
                            iter_t => {
                                field_types[idx] = *iter_t;
                            },

                            else => {
                                if (is_optional) {
                                    field_types[idx] = comptime ?[]param_type;
                                } else {
                                    field_types[idx] = comptime []param_type;
                                }
                            },
                        }
                    },
                    .Struct => |_| {
                        // Bogus type.
                        field_types[idx] = ?i32;

                        switch (main_type._type) {
                            .tag => {
                                // Bogus type.
                                field_types[idx] = ?i32;
                            },
                            .component => {
                                const param_type = @typeInfo(main_type.data_type).Pointer.child;
                                if (is_optional) {
                                    field_types[idx] = comptime ?[]param_type;
                                } else {
                                    field_types[idx] = comptime []param_type;
                                }
                            },
                            .iter => {
                                // Bogus type.
                                field_types[idx] = ?i32;
                            },
                        }
                    },
                    else => {},
                }
            }

            var args_vec_tuple: std.meta.Tuple(&field_types) = undefined;
            comptime var params_idx_2 = 0;
            inline for (fn_params, 0..) |param, idx| {
                const main_type = get_main_type(param);
                const is_optional = @typeInfo(param.type.?) == .Optional;
                //std.debug.print("Iter Param: {any}\n", .{param});
                switch (@typeInfo(main_type)) {
                    .Pointer => |pointer| {
                        const param_type = pointer.child;
                        switch (param_type) {
                            iter_t => {
                                args_vec_tuple[idx] = itptr;
                            },

                            else => {
                                if (is_optional) {
                                    if (fc.ecs_field_is_set(itptr, params_idx_2)) {
                                        args_vec_tuple[idx] = field(itptr, param_type, params_idx_2);
                                    } else {
                                        args_vec_tuple[idx] = null;
                                    }
                                } else {
                                    std.debug.print("", .{});
                                    args_vec_tuple[idx] = field(itptr, param_type, params_idx_2).?;
                                }
                                params_idx_2 += 1;
                            },
                        }
                    },
                    .Struct => |_| {
                        switch (main_type._type) {
                            .tag => {
                                if (is_optional and !fc.ecs_field_is_set(itptr, params_idx_2)) {
                                    args_vec_tuple[idx] = null;
                                } else {
                                    args_vec_tuple[idx] = 1;
                                }
                                params_idx_2 += 1;
                            },
                            .component => {
                                const param_type = @typeInfo(main_type.data_type).Pointer.child;
                                if (is_optional) {
                                    if (fc.ecs_field_is_set(itptr, params_idx_2)) {
                                        args_vec_tuple[idx] = field(itptr, param_type, params_idx_2);
                                    } else {
                                        args_vec_tuple[idx] = null;
                                    }
                                } else {
                                    args_vec_tuple[idx] = field(itptr, param_type, params_idx_2).?;
                                }
                                params_idx_2 += 1;
                            },
                            .iter => {},
                        }
                    },
                    else => {},
                }
            }

            for (0..@intCast(itptr.count)) |i| {
                var args_tuple: std.meta.ArgsTuple(@TypeOf(function)) = undefined;
                inline for (fn_params, 0..) |param, idx| {
                    const main_type = get_main_type(param);
                    const is_optional = @typeInfo(param.type.?) == .Optional;
                    switch (@typeInfo(main_type)) {
                        .Pointer => |_| {
                            const param_type = @TypeOf(args_vec_tuple[idx]);
                            switch (param_type) {
                                *iter_t => {
                                    args_tuple[idx] = args_vec_tuple[idx];
                                },

                                else => {
                                    if (is_optional) {
                                        if (args_vec_tuple[idx] != null) {
                                            if (fc.ecs_field_is_self(itptr, idx)) {
                                                args_tuple[idx] = &args_vec_tuple[idx].?[i];
                                            } else {
                                                // It's not a pointer, so get the first one.
                                                args_tuple[idx] = &args_vec_tuple[idx].?[0];
                                            }
                                        } else {
                                            args_tuple[idx] = null;
                                        }
                                    } else {
                                        if (fc.ecs_field_is_self(itptr, idx)) {
                                            args_tuple[idx] = &args_vec_tuple[idx][i];
                                        } else {
                                            args_tuple[idx] = &args_vec_tuple[idx][0];
                                        }
                                    }
                                },
                            }
                        },
                        .Struct => |_| {
                            switch (main_type._type) {
                                .tag => {
                                    if (is_optional and args_vec_tuple[idx] == null) {
                                        args_tuple[idx] = null;
                                    } else {
                                        const iter_w = World.from_ptr(itptr.world.?);
                                        args_tuple[idx] = .{ .entity = iter_w.ent(main_type.vybe_identifier) };
                                    }
                                },
                                .component => {
                                    //std.debug.print("----\nidx: {any}\n", .{args_vec_tuple[idx]});
                                    if (is_optional) {
                                        if (args_vec_tuple[idx] != null) {
                                            if (fc.ecs_field_is_self(itptr, idx)) {
                                                args_tuple[idx] = .{ .data = &args_vec_tuple[idx].?[i] };
                                            } else {
                                                args_tuple[idx] = .{ .data = &args_vec_tuple[idx].?[0] };
                                            }
                                        } else {
                                            args_tuple[idx] = null;
                                        }
                                    } else {
                                        if (fc.ecs_field_is_self(itptr, idx)) {
                                            args_tuple[idx] = .{ .data = &args_vec_tuple[idx][i] };
                                        } else {
                                            args_tuple[idx] = .{ .data = &args_vec_tuple[idx][0] };
                                        }
                                    }
                                },
                                .iter => {
                                    args_tuple[idx] = Iter{
                                        .itptr = itptr,
                                        .index = i,
                                    };
                                },
                            }
                        },
                        else => {},
                    }
                }
                //std.debug.print("Call Params: {any}\n", .{args_tuple});

                _ = @call(.always_inline, function, args_tuple);
            }
        }
    };

    return EachFnParsed{ .callback = callback_struct.exec };
}

pub const SystemParams = struct {
    name: []const u8,
};

fn _system(w: World, params: SystemParams, function_struct: anytype) entity_t {
    var system_desc = system_desc_t{
        .entity = fc.ecs_entity_init(w.wptr, &entity_desc_t{
            .name = params.name.ptr,
            .add = &.{
                make_pair(fc.EcsDependsOn, fc.EcsOnUpdate),
                0,
            },
        }),
    };

    const parsed = _parse_each_fn(w, function_struct.each, &system_desc.query);
    system_desc.callback = parsed.callback;

    return fc.ecs_system_init(w.wptr, &system_desc);
}

pub const ObserverParams = struct {
    name: []const u8,
    events: []const entity_t,
};

fn _observer(w: World, params: ObserverParams, function_struct: anytype) entity_t {
    var events: [8]entity_t = std.mem.zeroes([8]entity_t);
    for (params.events, 0..) |event, idx| {
        events[idx] = event;
    }

    var observer_desc = observer_desc_t{
        .entity = fc.ecs_entity_init(w.wptr, &entity_desc_t{
            .name = params.name.ptr,
        }),
        .events = events,
    };

    const parsed = _parse_each_fn(w, function_struct.each, &observer_desc.query);
    observer_desc.callback = parsed.callback;

    return fc.ecs_observer_init(w.wptr, &observer_desc);
}

fn is_term_pair(comptime term: anytype) bool {
    switch (@typeInfo(term.type)) {
        .Struct => |info| {
            if (info.is_tuple) {
                if (info.fields.len == 2) {
                    std.debug.panic("Invalid pair: {any}, the tuple should have exactly 2 elements", .{term});
                }
                return true;
            }
            return false;
        },
        else => {
            return false;
        },
    }
}

const TermKind = enum { tag, component, iter };

pub const Term = struct {
    pub const Flag = struct {
        pub const self = fc.EcsSelf;
        pub const up = fc.EcsUp;
        pub const trav = fc.EcsTrav;
        pub const cascade = fc.EcsCascade;
        pub const desc = fc.EcsDesc;
        pub const is_variable = fc.EcsIsVariable;
        pub const is_entity = fc.EcsIsEntity;
        pub const is_name = fc.EcsIsName;
    };

    pub const InOut = enum {
        Default,
        None,
        Filter,
        InOut,
        In,
        Out,
    };

    /// Use a bit set of `Term.Flag`.
    flags: ?entity_t = null,
    inout: ?InOut = null,
};

const FromTerm = @TypeOf(Term);

pub const Iter = struct {
    const _type = TermKind.iter;
    const meta = null;

    itptr: *iter_t,
    index: usize,

    pub fn entity(self: Iter) Entity {
        const w = World{ .wptr = self.itptr.world.? };
        return w.ent(self.itptr.entities[self.index]);
    }

    pub fn world(self: Iter) World {
        return World.from_ptr(self.itptr.world.?);
    }
};

pub fn Tag(comptime term: anytype) type {
    return struct {
        const _type = TermKind.tag;
        const vybe_identifier = term;
        const meta = null;

        entity: Entity,
    };
}

pub fn TagMeta(comptime term: anytype, term_params: ?Term) type {
    return struct {
        const _type = TermKind.tag;
        const vybe_identifier = term;
        const meta = term_params;

        entity: Entity,
    };
}

pub fn Comp(comptime T: type, comptime term: anytype, term_params: ?Term) type {
    return struct {
        const _type = TermKind.component;
        const vybe_identifier = term;
        const data_type = T;
        const meta = term_params;

        data: T,
    };
}

test "basics" {
    const w = World.new();
    defer _ = w.close();

    // We have some complex system here using relationships as components, parents, cascade etc, just
    // for testing.
    // The query terms of the system are derived from the `each` function arguments (for more complex
    // terms, we can use `Term`).
    _ = w.system(.{ .name = "Move" }, struct {
        fn each(
            p: *Position,
            _: Iter,
            // We are getting the parent from the parent (as it's also cascade, the position of the
            // parent will be calculated first).
            parent_p: ?Comp(
                *const Position,
                Position,
                // We want to get (optionally) the parent position in cascade mode.
                Term{ .flags = Term.Flag.cascade | Term.Flag.up, .inout = .Out },
            ),
            v: ?*const Velocity,
            _: Tag(.some_arbitrary_tag),
            _: ?Comp(
                *const Position,
                .{ Position, .something },
                Term{ .flags = Term.Flag.up },
            ),
        ) void {
            // std.debug.print("---\n{any}\n\n", .{it.world()});
            // std.debug.print("it_ent: {s}\nother_pos: {any}\n\n", .{
            //     it.entity().name().?,
            //     if (other_pos) |pos_v| pos_v.data else null,
            // });
            if (v) |vel| {
                p.x += vel.x;
                p.y += vel.y;
            } else {
                p.x += 100;
                p.y -= 100;
            }

            // This doesn't make sense in a transform system, we just want to test it.
            if (parent_p) |p1| {
                p.x += p1.data.x;
                p.y += p1.data.y;
            }
        }
    });

    _ = w.system(.{ .name = "MoveBack" }, struct {
        fn each(
            p: *Position,
            _: *iter_t,
            v: *const Velocity,
            _: Tag("stalled"),
            _: Tag(.{ .e4, .e6 }),
        ) void {
            //std.debug.print("pair_tag: {s}\n", .{pair_tag.entity.name() orelse "NULL_NAME"});
            p.x -= v.x;
            p.y -= v.y;
        }
    });

    const e10 = .{
        .e10 = .{
            .some_arbitrary_tag,
            Position{ .x = 24.3, .y = 25.0 },
            Velocity{ .x = 20.0, .y = -5.0 },
        },
    };

    w.merge(.{
        .e1 = .{
            Position{ .x = 4.3, .y = 5.0 },
            Velocity{ .x = 10.0, .y = 15.0 },
            .some_arbitrary_tag,
            .{ Position{ .x = 40.0, .y = 50.0 }, .something },
            .{ .something, Position{ .x = 41.0, .y = 51.0 } },
        },
        .e100 = .{
            Position{ .x = -4.3, .y = 5.0 },
            Velocity{ .x = 10.0, .y = -15.0 },
            .some_arbitrary_tag,
        },
        .no_velocity_e = .{
            Position{ .x = 4.3, .y = 5.0 },
            .some_arbitrary_tag,
        },
        // We also test pairs with this entity.
        .e0 = .{
            Position{ .x = 2.0, .y = 4.0 },
            Velocity{ .x = 13.0, .y = 20.0 },
            .some_arbitrary_tag,
            .stalled,
            .{ .e4, .e6 },
        },
        .no_tag_e = .{
            Position{ .x = 3.0, .y = 5.0 },
            Velocity{ .x = 13.0, .y = 20.0 },
        },
        .e2 = .{
            .some_arbitrary_tag,
            Position{ .x = 5.3, .y = 6.0 },
            Velocity{ .x = 11.0, .y = 16.0 },
            .{ .e4, .e6 },
            e10,
            .some_tag,
        },
    });

    _ = w.progress(0.0);

    try std.testing.expectEqual(
        Position{ .x = 14.3, .y = 20.0 },
        w.ent(.e1).get(Position).?,
    );

    // This entity has no velocity, so it will be +100 for x and -100 for y (see "Move").
    try std.testing.expectEqual(
        Position{ .x = 104.3, .y = -95.0 },
        w.ent(.no_velocity_e).get(Position).?,
    );

    // This one moves forward and back as it has the "stalled" tag.
    try std.testing.expectEqual(
        Position{ .x = 2.0, .y = 4.0 },
        w.ent(.e0).get(Position).?,
    );

    // e10 is a child of e2.
    // Its position will be summed with the new position of e2.
    try std.testing.expectEqual(
        Position{ .x = 44.3 + 16.3, .y = 20.0 + 22.0 },
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

    // no_tag_e is unchanged.
    try std.testing.expectEqual(
        Position{ .x = 3.0, .y = 5.0 },
        w.ent(.no_tag_e).get(Position).?,
    );

    // Now we make the tag optional in another system and we should see no_tag_e modified.
    _ = w.system(.{ .name = "MoveWithOptionalTag" }, struct {
        fn each(
            p: *Position,
            _: Iter,
            _: ?Tag(.some_arbitrary_tag),
            v: *const Velocity,
        ) void {
            //std.debug.print("it.entity: {s},\n", .{it.entity().name()});
            // std.debug.print(
            //     "optional: {s}, pos: {any}\n",
            //     .{ if (opt == null) "NULL" else opt.?.entity.name(), p },
            // );
            p.x += v.x;
            p.y += v.y;
        }
    });
    _ = w.progress(0.0);

    try std.testing.expectEqual(
        Position{ .x = 16.0, .y = 25.0 },
        w.ent(.no_tag_e).get(Position).?,
    );
}
