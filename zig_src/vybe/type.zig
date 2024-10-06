const std = @import("std");
const rm = @cImport({
    @cDefine("RAYLIB_IMPLEMENTATION", {});
    @cInclude("raymath.h");
});

//pub const Translation = rm.Vector2;
pub const Translation = extern struct { x: f32, y: f32, z: f32 };
pub const Scale = extern struct { x: f32, y: f32, z: f32 };
pub const Rotation = extern struct { x: f32, y: f32, z: f32, w: f32 };
pub const Transform = extern struct {
    m0: f32,
    m4: f32,
    m8: f32,
    m12: f32,

    m1: f32,
    m5: f32,
    m9: f32,
    m13: f32,

    m2: f32,
    m6: f32,
    m10: f32,
    m14: f32,

    m3: f32,
    m7: f32,
    m11: f32,
    m15: f32,
};
