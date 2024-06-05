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
 * struct ecs_sync_stats_t {
 *     int64_t first_;
 *     ecs_metric_t time_spent;
 *     ecs_metric_t commands_enqueued;
 *     int64_t last_;
 *     int32_t system_count;
 *     bool multi_threaded;
 *     bool immediate;
 * }
 * }
 */
public class ecs_sync_stats_t {

    ecs_sync_stats_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_LONG_LONG.withName("first_"),
        ecs_metric_t.layout().withName("time_spent"),
        ecs_metric_t.layout().withName("commands_enqueued"),
        flecs.C_LONG_LONG.withName("last_"),
        flecs.C_INT.withName("system_count"),
        flecs.C_BOOL.withName("multi_threaded"),
        flecs.C_BOOL.withName("immediate"),
        MemoryLayout.paddingLayout(2)
    ).withName("ecs_sync_stats_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong first_$LAYOUT = (OfLong)$LAYOUT.select(groupElement("first_"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int64_t first_
     * }
     */
    public static final OfLong first_$layout() {
        return first_$LAYOUT;
    }

    private static final long first_$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int64_t first_
     * }
     */
    public static final long first_$offset() {
        return first_$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int64_t first_
     * }
     */
    public static long first_(MemorySegment struct) {
        return struct.get(first_$LAYOUT, first_$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int64_t first_
     * }
     */
    public static void first_(MemorySegment struct, long fieldValue) {
        struct.set(first_$LAYOUT, first_$OFFSET, fieldValue);
    }

    private static final GroupLayout time_spent$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("time_spent"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_metric_t time_spent
     * }
     */
    public static final GroupLayout time_spent$layout() {
        return time_spent$LAYOUT;
    }

    private static final long time_spent$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_metric_t time_spent
     * }
     */
    public static final long time_spent$offset() {
        return time_spent$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_metric_t time_spent
     * }
     */
    public static MemorySegment time_spent(MemorySegment struct) {
        return struct.asSlice(time_spent$OFFSET, time_spent$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_metric_t time_spent
     * }
     */
    public static void time_spent(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, time_spent$OFFSET, time_spent$LAYOUT.byteSize());
    }

    private static final GroupLayout commands_enqueued$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("commands_enqueued"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_metric_t commands_enqueued
     * }
     */
    public static final GroupLayout commands_enqueued$layout() {
        return commands_enqueued$LAYOUT;
    }

    private static final long commands_enqueued$OFFSET = 1208;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_metric_t commands_enqueued
     * }
     */
    public static final long commands_enqueued$offset() {
        return commands_enqueued$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_metric_t commands_enqueued
     * }
     */
    public static MemorySegment commands_enqueued(MemorySegment struct) {
        return struct.asSlice(commands_enqueued$OFFSET, commands_enqueued$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_metric_t commands_enqueued
     * }
     */
    public static void commands_enqueued(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, commands_enqueued$OFFSET, commands_enqueued$LAYOUT.byteSize());
    }

    private static final OfLong last_$LAYOUT = (OfLong)$LAYOUT.select(groupElement("last_"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int64_t last_
     * }
     */
    public static final OfLong last_$layout() {
        return last_$LAYOUT;
    }

    private static final long last_$OFFSET = 2408;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int64_t last_
     * }
     */
    public static final long last_$offset() {
        return last_$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int64_t last_
     * }
     */
    public static long last_(MemorySegment struct) {
        return struct.get(last_$LAYOUT, last_$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int64_t last_
     * }
     */
    public static void last_(MemorySegment struct, long fieldValue) {
        struct.set(last_$LAYOUT, last_$OFFSET, fieldValue);
    }

    private static final OfInt system_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("system_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t system_count
     * }
     */
    public static final OfInt system_count$layout() {
        return system_count$LAYOUT;
    }

    private static final long system_count$OFFSET = 2416;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t system_count
     * }
     */
    public static final long system_count$offset() {
        return system_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t system_count
     * }
     */
    public static int system_count(MemorySegment struct) {
        return struct.get(system_count$LAYOUT, system_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t system_count
     * }
     */
    public static void system_count(MemorySegment struct, int fieldValue) {
        struct.set(system_count$LAYOUT, system_count$OFFSET, fieldValue);
    }

    private static final OfBoolean multi_threaded$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("multi_threaded"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static final OfBoolean multi_threaded$layout() {
        return multi_threaded$LAYOUT;
    }

    private static final long multi_threaded$OFFSET = 2420;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static final long multi_threaded$offset() {
        return multi_threaded$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static boolean multi_threaded(MemorySegment struct) {
        return struct.get(multi_threaded$LAYOUT, multi_threaded$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static void multi_threaded(MemorySegment struct, boolean fieldValue) {
        struct.set(multi_threaded$LAYOUT, multi_threaded$OFFSET, fieldValue);
    }

    private static final OfBoolean immediate$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("immediate"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static final OfBoolean immediate$layout() {
        return immediate$LAYOUT;
    }

    private static final long immediate$OFFSET = 2421;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static final long immediate$offset() {
        return immediate$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static boolean immediate(MemorySegment struct) {
        return struct.get(immediate$LAYOUT, immediate$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static void immediate(MemorySegment struct, boolean fieldValue) {
        struct.set(immediate$LAYOUT, immediate$OFFSET, fieldValue);
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

