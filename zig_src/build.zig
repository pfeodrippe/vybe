const std = @import("std");
//const vf = @import("flecs.zig");

pub fn build(b: *std.Build) void {
    const target = b.standardTargetOptions(.{});
    const optimize = b.standardOptimizeOption(.{});

    // Common
    const flecs_c = .{
        .file = b.path("../flecs/distr/flecs.c"),
        .flags = &.{
            "-std=gnu99",
            "-DFLECS_NDEBUG",
            "-Dflecs_EXPORTS",
            "-DFLECS_KEEP_ASSERT",
            "-DFLECS_SOFT_ASSERT",
            "-fno-sanitize=undefined",
            "-DFLECS_NO_CPP",
            "-DFLECS_USE_OS_ALLOC",
        },
    };

    const raylib_c = .{
        .file = b.path("stub.c"),
        .flags = &.{
            "-fno-sanitize=undefined",
            "-DRAYMATH_IMPLEMENTATION",
            "-std=gnu99",
            "-D_GNU_SOURCE",
        },
    };

    // Exe
    const exe = b.addExecutable(.{
        .name = "zig_vybe",
        .root_source_file = b.path("vybe_export.zig"),
        .target = target,
        .optimize = optimize,
    });
    const install_artifact_step = b.addInstallArtifact(exe, .{ .dest_dir = .{ .override = .prefix } });
    b.getInstallStep().dependOn(&install_artifact_step.step);

    exe.addIncludePath(b.path("../flecs/distr"));
    exe.addCSourceFile(flecs_c);
    exe.addIncludePath(b.path("../raylib/src"));
    exe.addCSourceFile(raylib_c);

    const run_exe = b.addRunArtifact(exe);
    const run_step = b.step("run", "Run the application");
    run_step.dependOn(&run_exe.step);

    switch (target.result.os.tag) {
        .windows => {
            exe.linkSystemLibrary("ws2_32");
        },
        .linux => {
            exe.linkSystemLibrary("m");
            exe.linkSystemLibrary("dl");
            exe.linkSystemLibrary("rt");
        },
        else => {},
    }
    exe.linkLibC();

    b.addInstallBinFile(exe.getEmittedBin(), "../../eita").step.dependOn(&install_artifact_step.step);

    // Shared
    const vybe_lib = b.addSharedLibrary(.{
        .name = "zig_vybe",
        .root_source_file = b.path("vybe_export.zig"),
        .target = target,
        .optimize = optimize,
        .version = .{ .major = 1, .minor = 2, .patch = 3 },
    });

    switch (target.result.os.tag) {
        .windows => {
            vybe_lib.linkSystemLibrary("ws2_32");
        },
        .linux => {
            vybe_lib.linkSystemLibrary("m");
            vybe_lib.linkSystemLibrary("dl");
            vybe_lib.linkSystemLibrary("rt");
        },
        else => {},
    }

    vybe_lib.linkLibC();

    //_ = libfizzbuzz.getEmittedH();

    vybe_lib.addIncludePath(b.path("../flecs/distr"));
    vybe_lib.addCSourceFile(flecs_c);
    vybe_lib.addIncludePath(b.path("../raylib/src"));
    vybe_lib.addCSourceFile(raylib_c);

    b.installArtifact(vybe_lib);

    // ======================== Unit tests
    const unit_step = b.step("test_unit", "Run unit tests");

    const unit_tests = b.addTest(.{
        .root_source_file = b.path("vybe/flecs.zig"),
        .target = target,
    });

    unit_tests.addIncludePath(b.path("../flecs/distr"));
    unit_tests.addCSourceFile(flecs_c);
    unit_tests.addIncludePath(b.path("../raylib/src"));
    unit_tests.addCSourceFile(raylib_c);

    switch (target.result.os.tag) {
        .windows => {
            unit_tests.linkSystemLibrary("ws2_32");
        },
        .linux => {
            unit_tests.linkSystemLibrary("m");
            unit_tests.linkSystemLibrary("dl");
            unit_tests.linkSystemLibrary("rt");
        },
        else => {},
    }

    unit_step.dependOn(&b.addRunArtifact(unit_tests).step);

    // ======================== Clj export tests
    const export_step = b.step("test_export", "Run export tests");

    const export_tests = b.addTest(.{
        .root_source_file = b.path("vybe_export.zig"),
        .target = target,
    });
    export_tests.addIncludePath(b.path("../flecs/distr"));
    export_tests.addCSourceFile(flecs_c);
    export_tests.addIncludePath(b.path("../raylib/src"));
    export_tests.addCSourceFile(raylib_c);

    switch (target.result.os.tag) {
        .windows => {
            export_tests.linkSystemLibrary("ws2_32");
        },
        .linux => {
            export_tests.linkSystemLibrary("m");
            export_tests.linkSystemLibrary("dl");
            export_tests.linkSystemLibrary("rt");
        },
        else => {},
    }

    export_step.dependOn(&b.addRunArtifact(export_tests).step);
    //export_step.dependOn(unit_step);
}
