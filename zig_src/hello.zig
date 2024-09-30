const std = @import("std");

const flecs = @cImport({
    @cInclude("flecs.h");
});

/// From zig-gamedev
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

export fn vybe_eita(_: i32) flecs.ecs_entity_t {
    EcsAllocator.gpa = .{};
    EcsAllocator.allocator = EcsAllocator.gpa.?.allocator();

    flecs.ecs_os_api.malloc_ = &EcsAllocator.alloc;
    flecs.ecs_os_api.free_ = &EcsAllocator.free;
    flecs.ecs_os_api.realloc_ = &EcsAllocator.realloc;
    flecs.ecs_os_api.calloc_ = &EcsAllocator.calloc;
    //_ = flecs.ecs_log_set_level(1);

    const world = flecs.ecs_mini().?;
    return flecs.ecs_new(world);
    //return v + 30;
}

test "simple test" {
    const e1 = vybe_eita(3);
    //defer _ = flecs.ecs_fini(world);
    try std.testing.expectEqual(402, e1);
}
