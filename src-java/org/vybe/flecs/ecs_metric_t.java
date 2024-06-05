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
 * union ecs_metric_t {
 *     ecs_gauge_t gauge;
 *     ecs_counter_t counter;
 * }
 * }
 */
public class ecs_metric_t {

    ecs_metric_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.unionLayout(
        ecs_gauge_t.layout().withName("gauge"),
        ecs_counter_t.layout().withName("counter")
    ).withName("ecs_metric_t");

    /**
     * The layout of this union
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout gauge$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("gauge"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_gauge_t gauge
     * }
     */
    public static final GroupLayout gauge$layout() {
        return gauge$LAYOUT;
    }

    private static final long gauge$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_gauge_t gauge
     * }
     */
    public static final long gauge$offset() {
        return gauge$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_gauge_t gauge
     * }
     */
    public static MemorySegment gauge(MemorySegment union) {
        return union.asSlice(gauge$OFFSET, gauge$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_gauge_t gauge
     * }
     */
    public static void gauge(MemorySegment union, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, union, gauge$OFFSET, gauge$LAYOUT.byteSize());
    }

    private static final GroupLayout counter$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("counter"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_counter_t counter
     * }
     */
    public static final GroupLayout counter$layout() {
        return counter$LAYOUT;
    }

    private static final long counter$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_counter_t counter
     * }
     */
    public static final long counter$offset() {
        return counter$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_counter_t counter
     * }
     */
    public static MemorySegment counter(MemorySegment union) {
        return union.asSlice(counter$OFFSET, counter$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_counter_t counter
     * }
     */
    public static void counter(MemorySegment union, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, union, counter$OFFSET, counter$LAYOUT.byteSize());
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this union
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

