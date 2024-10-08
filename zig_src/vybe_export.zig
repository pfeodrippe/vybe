const std = @import("std");
const vf = @import("vybe/flecs.zig");
const vt = @import("vybe/type.zig");

const rm = @cImport({
    @cDefine("RAYLIB_IMPLEMENTATION", {});
    @cInclude("raymath.h");
});

fn cast(comptime T: type, val: anytype) *const T {
    return @as(*const T, @ptrCast(@alignCast(val)));
}

fn matrix_transform(
    translation: *const vt.Translation,
    rotation: *const vt.Rotation,
    scale: *const vt.Scale,
) vt.Transform {
    const mat_scale = rm.MatrixScale(scale.x, scale.y, scale.z);
    const mat_rotation = rm.QuaternionToMatrix(cast(rm.Vector4, rotation).*);
    const mat_translation = rm.MatrixTranslate(translation.x, translation.y, translation.z);
    const res = rm.MatrixMultiply(
        rm.MatrixMultiply(mat_scale, mat_rotation),
        mat_translation,
    );

    return cast(vt.Transform, &res).*;
}

test "matrix_transform" {
    const res = matrix_transform(
        &vt.Translation{ .x = 0.5, .y = 0.7, .z = 0.8 },
        &vt.Rotation{ .x = 0, .y = 0, .z = 0, .w = 1 },
        &vt.Scale{ .x = 1, .y = 1, .z = 1 },
    );

    try std.testing.expectEqual(
        .{ 0.5, 0.7, 0.8 },
        .{ res.m12, res.m13, res.m14 },
    );
}

export fn vybe_default_systems(wptr: *vf.world_t) void {
    _ = vf.World.new();
    const w = vf.World.from_ptr(wptr);

    _ = w.system(.{ .name = "zig_vybe_transform" }, struct {
        pub fn each(
            pos: *const vt.Translation,
            rot: *const vt.Rotation,
            scale: *const vt.Scale,
            transform_global: vf.Comp(
                *vt.Transform,
                .{ vt.Transform, .global },
                vf.Term{ .inout = vf.Term.InOut.Out },
            ),
            transform_local: vf.Comp(
                *vt.Transform,
                vt.Transform,
                vf.Term{ .inout = vf.Term.InOut.Out },
            ),
            transform_parent: ?vf.Comp(
                *const vt.Transform,
                .{ vt.Transform, .global },
                vf.Term{ .flags = vf.Term.Flag.cascade | vf.Term.Flag.up },
            ),
        ) void {
            // std.debug.print("---\nSYSTE\n{any}\n\n", .{.{transform_global}});
            const local = matrix_transform(pos, rot, scale);
            transform_local.data.* = local;

            if (transform_parent) |t_parent| {
                transform_global.data.* = cast(vt.Transform, &rm.MatrixMultiply(
                    cast(rm.Matrix, &local).*,
                    cast(rm.Matrix, t_parent.data).*,
                )).*;
            } else {
                transform_global.data.* = local;
            }
        }
    });
}

fn near(expected: anytype, actual: anytype) !void {
    try std.testing.expectApproxEqAbs(expected, actual, 0.01);
}

test "vybe_default_systems" {
    const w = vf.World.new();
    vybe_default_systems(w.wptr);

    var pos = vt.Translation{ .x = 0, .y = 0, .z = 0 };
    pos.x += 0;

    const e1 = .{
        .e1 = .{
            pos,
            vt.Scale{ .x = 1, .y = 1, .z = 1 },
            vt.Rotation{ .x = 0, .y = 0, .z = 0, .w = 1 },
            vt.Transform,
            .{ vt.Transform, .global },
        },
    };

    w.merge(.{
        .e2 = .{
            pos,
            vt.Scale{ .x = 1, .y = 1, .z = 1 },
            vt.Rotation{ .x = 0, .y = 0, .z = 0, .w = 1 },
            vt.Transform,
            .{ vt.Transform, .global },
            e1,
        },
    });

    _ = w.progress(0.0);

    var res = w.ent("e2.e1").get_1(vt.Transform, .global).?;
    //std.debug.print("{any}", .{.{ res.m12, res.m13, res.m14 }});

    try std.testing.expectEqual(
        .{ 0, 0, 0 },
        .{ res.m12, res.m13, res.m14 },
    );

    w.merge(
        .{
            .e2 = .{
                vt.Translation{ .x = 0.1, .y = 0.3, .z = 5.3 }, .{
                    .e1 = .{vt.Translation{ .x = 0.5, .y = 0.15, .z = 1.1 }},
                },
            },
        },
    );

    _ = w.progress(0.0);

    res = w.ent("e2.e1").get_1(vt.Transform, .global).?;
    try near(0.6, res.m12);
    try near(0.45, res.m13);
    try near(6.4, res.m14);
}

fn vybe_default_systems_2(wptr: *vf.world_t) void {
    _ = vf.World.new();
    const w = vf.World.from_ptr(wptr);

    const out = std.io.getStdOut().writer();
    out.print("---EITsA\n\n{any}\n\n", .{w.eid(.rrr)}) catch return;

    //out.print("---EITA\n\n{any}\n\n", .{w2.progress(0.0)}) catch return;
}

pub fn main() void {
    const w = vf.World.new();
    vybe_default_systems_2(w.wptr);

    const pos = vt.Translation{ .x = 1.5, .y = 3.2, .z = 0.6 };
    const e1 = .{
        .e1 = .{
            pos,
            vt.Scale{ .x = 1, .y = 1, .z = 1 },
            vt.Rotation{ .x = 0, .y = 0, .z = 0, .w = 1 },
            vt.Transform,
            .{ vt.Transform, .global },
        },
    };

    w.merge(.{
        .e2 = .{
            pos,
            vt.Scale{ .x = 1, .y = 1, .z = 1 },
            vt.Rotation{ .x = 0, .y = 0, .z = 0, .w = 1 },
            vt.Transform,
            .{ vt.Transform, .global },
            e1,
        },
    });

    _ = w.progress(0.0);

    const res = w.ent("e2.e1").get_1(vt.Transform, .global).?;
    std.debug.print("{any}\n", .{.{ res.m12, res.m13, res.m14 }});
}
