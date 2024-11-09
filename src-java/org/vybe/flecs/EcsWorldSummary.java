// Generated by jextract

package org.vybe.flecs;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * struct {
 *     double target_fps;
 *     double time_scale;
 *     double frame_time_total;
 *     double system_time_total;
 *     double merge_time_total;
 *     double frame_time_last;
 *     double system_time_last;
 *     double merge_time_last;
 *     int64_t frame_count;
 *     int64_t command_count;
 *     ecs_build_info_t build_info;
 * }
 * }
 */
public class EcsWorldSummary {

    EcsWorldSummary() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_DOUBLE.withName("target_fps"),
        flecs.C_DOUBLE.withName("time_scale"),
        flecs.C_DOUBLE.withName("frame_time_total"),
        flecs.C_DOUBLE.withName("system_time_total"),
        flecs.C_DOUBLE.withName("merge_time_total"),
        flecs.C_DOUBLE.withName("frame_time_last"),
        flecs.C_DOUBLE.withName("system_time_last"),
        flecs.C_DOUBLE.withName("merge_time_last"),
        flecs.C_LONG_LONG.withName("frame_count"),
        flecs.C_LONG_LONG.withName("command_count"),
        ecs_build_info_t.layout().withName("build_info")
    ).withName("$anon$12888:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfDouble target_fps$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("target_fps"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double target_fps
     * }
     */
    public static final OfDouble target_fps$layout() {
        return target_fps$LAYOUT;
    }

    private static final long target_fps$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double target_fps
     * }
     */
    public static final long target_fps$offset() {
        return target_fps$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double target_fps
     * }
     */
    public static double target_fps(MemorySegment struct) {
        return struct.get(target_fps$LAYOUT, target_fps$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double target_fps
     * }
     */
    public static void target_fps(MemorySegment struct, double fieldValue) {
        struct.set(target_fps$LAYOUT, target_fps$OFFSET, fieldValue);
    }

    private static final OfDouble time_scale$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("time_scale"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double time_scale
     * }
     */
    public static final OfDouble time_scale$layout() {
        return time_scale$LAYOUT;
    }

    private static final long time_scale$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double time_scale
     * }
     */
    public static final long time_scale$offset() {
        return time_scale$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double time_scale
     * }
     */
    public static double time_scale(MemorySegment struct) {
        return struct.get(time_scale$LAYOUT, time_scale$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double time_scale
     * }
     */
    public static void time_scale(MemorySegment struct, double fieldValue) {
        struct.set(time_scale$LAYOUT, time_scale$OFFSET, fieldValue);
    }

    private static final OfDouble frame_time_total$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("frame_time_total"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double frame_time_total
     * }
     */
    public static final OfDouble frame_time_total$layout() {
        return frame_time_total$LAYOUT;
    }

    private static final long frame_time_total$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double frame_time_total
     * }
     */
    public static final long frame_time_total$offset() {
        return frame_time_total$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double frame_time_total
     * }
     */
    public static double frame_time_total(MemorySegment struct) {
        return struct.get(frame_time_total$LAYOUT, frame_time_total$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double frame_time_total
     * }
     */
    public static void frame_time_total(MemorySegment struct, double fieldValue) {
        struct.set(frame_time_total$LAYOUT, frame_time_total$OFFSET, fieldValue);
    }

    private static final OfDouble system_time_total$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("system_time_total"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double system_time_total
     * }
     */
    public static final OfDouble system_time_total$layout() {
        return system_time_total$LAYOUT;
    }

    private static final long system_time_total$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double system_time_total
     * }
     */
    public static final long system_time_total$offset() {
        return system_time_total$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double system_time_total
     * }
     */
    public static double system_time_total(MemorySegment struct) {
        return struct.get(system_time_total$LAYOUT, system_time_total$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double system_time_total
     * }
     */
    public static void system_time_total(MemorySegment struct, double fieldValue) {
        struct.set(system_time_total$LAYOUT, system_time_total$OFFSET, fieldValue);
    }

    private static final OfDouble merge_time_total$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("merge_time_total"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double merge_time_total
     * }
     */
    public static final OfDouble merge_time_total$layout() {
        return merge_time_total$LAYOUT;
    }

    private static final long merge_time_total$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double merge_time_total
     * }
     */
    public static final long merge_time_total$offset() {
        return merge_time_total$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double merge_time_total
     * }
     */
    public static double merge_time_total(MemorySegment struct) {
        return struct.get(merge_time_total$LAYOUT, merge_time_total$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double merge_time_total
     * }
     */
    public static void merge_time_total(MemorySegment struct, double fieldValue) {
        struct.set(merge_time_total$LAYOUT, merge_time_total$OFFSET, fieldValue);
    }

    private static final OfDouble frame_time_last$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("frame_time_last"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double frame_time_last
     * }
     */
    public static final OfDouble frame_time_last$layout() {
        return frame_time_last$LAYOUT;
    }

    private static final long frame_time_last$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double frame_time_last
     * }
     */
    public static final long frame_time_last$offset() {
        return frame_time_last$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double frame_time_last
     * }
     */
    public static double frame_time_last(MemorySegment struct) {
        return struct.get(frame_time_last$LAYOUT, frame_time_last$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double frame_time_last
     * }
     */
    public static void frame_time_last(MemorySegment struct, double fieldValue) {
        struct.set(frame_time_last$LAYOUT, frame_time_last$OFFSET, fieldValue);
    }

    private static final OfDouble system_time_last$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("system_time_last"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double system_time_last
     * }
     */
    public static final OfDouble system_time_last$layout() {
        return system_time_last$LAYOUT;
    }

    private static final long system_time_last$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double system_time_last
     * }
     */
    public static final long system_time_last$offset() {
        return system_time_last$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double system_time_last
     * }
     */
    public static double system_time_last(MemorySegment struct) {
        return struct.get(system_time_last$LAYOUT, system_time_last$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double system_time_last
     * }
     */
    public static void system_time_last(MemorySegment struct, double fieldValue) {
        struct.set(system_time_last$LAYOUT, system_time_last$OFFSET, fieldValue);
    }

    private static final OfDouble merge_time_last$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("merge_time_last"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double merge_time_last
     * }
     */
    public static final OfDouble merge_time_last$layout() {
        return merge_time_last$LAYOUT;
    }

    private static final long merge_time_last$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double merge_time_last
     * }
     */
    public static final long merge_time_last$offset() {
        return merge_time_last$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double merge_time_last
     * }
     */
    public static double merge_time_last(MemorySegment struct) {
        return struct.get(merge_time_last$LAYOUT, merge_time_last$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double merge_time_last
     * }
     */
    public static void merge_time_last(MemorySegment struct, double fieldValue) {
        struct.set(merge_time_last$LAYOUT, merge_time_last$OFFSET, fieldValue);
    }

    private static final OfLong frame_count$LAYOUT = (OfLong)$LAYOUT.select(groupElement("frame_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int64_t frame_count
     * }
     */
    public static final OfLong frame_count$layout() {
        return frame_count$LAYOUT;
    }

    private static final long frame_count$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int64_t frame_count
     * }
     */
    public static final long frame_count$offset() {
        return frame_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int64_t frame_count
     * }
     */
    public static long frame_count(MemorySegment struct) {
        return struct.get(frame_count$LAYOUT, frame_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int64_t frame_count
     * }
     */
    public static void frame_count(MemorySegment struct, long fieldValue) {
        struct.set(frame_count$LAYOUT, frame_count$OFFSET, fieldValue);
    }

    private static final OfLong command_count$LAYOUT = (OfLong)$LAYOUT.select(groupElement("command_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int64_t command_count
     * }
     */
    public static final OfLong command_count$layout() {
        return command_count$LAYOUT;
    }

    private static final long command_count$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int64_t command_count
     * }
     */
    public static final long command_count$offset() {
        return command_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int64_t command_count
     * }
     */
    public static long command_count(MemorySegment struct) {
        return struct.get(command_count$LAYOUT, command_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int64_t command_count
     * }
     */
    public static void command_count(MemorySegment struct, long fieldValue) {
        struct.set(command_count$LAYOUT, command_count$OFFSET, fieldValue);
    }

    private static final GroupLayout build_info$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("build_info"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_build_info_t build_info
     * }
     */
    public static final GroupLayout build_info$layout() {
        return build_info$LAYOUT;
    }

    private static final long build_info$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_build_info_t build_info
     * }
     */
    public static final long build_info$offset() {
        return build_info$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_build_info_t build_info
     * }
     */
    public static MemorySegment build_info(MemorySegment struct) {
        return struct.asSlice(build_info$OFFSET, build_info$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_build_info_t build_info
     * }
     */
    public static void build_info(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, build_info$OFFSET, build_info$LAYOUT.byteSize());
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

