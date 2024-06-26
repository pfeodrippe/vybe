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
 * struct EcsAlertsActive {
 *     int32_t info_count;
 *     int32_t warning_count;
 *     int32_t error_count;
 *     ecs_map_t alerts;
 * }
 * }
 */
public class EcsAlertsActive {

    EcsAlertsActive() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_INT.withName("info_count"),
        flecs.C_INT.withName("warning_count"),
        flecs.C_INT.withName("error_count"),
        MemoryLayout.paddingLayout(4),
        ecs_map_t.layout().withName("alerts")
    ).withName("EcsAlertsActive");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt info_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("info_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t info_count
     * }
     */
    public static final OfInt info_count$layout() {
        return info_count$LAYOUT;
    }

    private static final long info_count$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t info_count
     * }
     */
    public static final long info_count$offset() {
        return info_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t info_count
     * }
     */
    public static int info_count(MemorySegment struct) {
        return struct.get(info_count$LAYOUT, info_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t info_count
     * }
     */
    public static void info_count(MemorySegment struct, int fieldValue) {
        struct.set(info_count$LAYOUT, info_count$OFFSET, fieldValue);
    }

    private static final OfInt warning_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("warning_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t warning_count
     * }
     */
    public static final OfInt warning_count$layout() {
        return warning_count$LAYOUT;
    }

    private static final long warning_count$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t warning_count
     * }
     */
    public static final long warning_count$offset() {
        return warning_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t warning_count
     * }
     */
    public static int warning_count(MemorySegment struct) {
        return struct.get(warning_count$LAYOUT, warning_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t warning_count
     * }
     */
    public static void warning_count(MemorySegment struct, int fieldValue) {
        struct.set(warning_count$LAYOUT, warning_count$OFFSET, fieldValue);
    }

    private static final OfInt error_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("error_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t error_count
     * }
     */
    public static final OfInt error_count$layout() {
        return error_count$LAYOUT;
    }

    private static final long error_count$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t error_count
     * }
     */
    public static final long error_count$offset() {
        return error_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t error_count
     * }
     */
    public static int error_count(MemorySegment struct) {
        return struct.get(error_count$LAYOUT, error_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t error_count
     * }
     */
    public static void error_count(MemorySegment struct, int fieldValue) {
        struct.set(error_count$LAYOUT, error_count$OFFSET, fieldValue);
    }

    private static final GroupLayout alerts$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("alerts"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_map_t alerts
     * }
     */
    public static final GroupLayout alerts$layout() {
        return alerts$LAYOUT;
    }

    private static final long alerts$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_map_t alerts
     * }
     */
    public static final long alerts$offset() {
        return alerts$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_map_t alerts
     * }
     */
    public static MemorySegment alerts(MemorySegment struct) {
        return struct.asSlice(alerts$OFFSET, alerts$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_map_t alerts
     * }
     */
    public static void alerts(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, alerts$OFFSET, alerts$LAYOUT.byteSize());
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

