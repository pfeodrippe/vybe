const std = @import("std");

pub fn build(b: *std.Build) void {
    const target = b.standardTargetOptions(.{});
    const optimize = b.standardOptimizeOption(.{});

    const vybe_lib = b.addSharedLibrary(.{
        .name = "hello",
        .root_source_file = b.path("hello.zig"),
        .target = target,
        .optimize = optimize,
        .version = .{ .major = 1, .minor = 2, .patch = 3 },
    });

    //_ = libfizzbuzz.getEmittedH();

    const flecs_c = .{ .file = b.path("../flecs/distr/flecs.c"), .flags = &.{ "-std=c99", "-Dflecs_EXPORTS", "-DFLECS_NO_REST", "-DFLECS_NO_TIMER", "-DFLECS_NO_STATS", "-DFLECS_NO_ALERTS" } };

    //libfizzbuzz.addIncludePath(b.path("../bin"));
    vybe_lib.addIncludePath(b.path("../flecs/distr"));
    vybe_lib.addCSourceFile(flecs_c);

    //b.getInstallStep().dependOn(&b.addInstallHeaderFile(libfizzbuzz.getEmittedH(), "eita.h").step);
    b.installArtifact(vybe_lib);

    //const wf = b.addWriteFiles();
    _ = vybe_lib.getEmittedBin().getDisplayName();
    //std.fmt.print(a, {});
    //wf.addCopyFile(b.path(""), "../native");

    // ======================== Testing
    const test_step = b.step("test", "Run unit tests");

    const unit_tests = b.addTest(.{
        .root_source_file = b.path("hello.zig"),
        .target = target,
    });
    unit_tests.addIncludePath(b.path("../flecs/distr"));
    unit_tests.addCSourceFile(flecs_c);

    const run_unit_tests = b.addRunArtifact(unit_tests);
    test_step.dependOn(&run_unit_tests.step);
}
