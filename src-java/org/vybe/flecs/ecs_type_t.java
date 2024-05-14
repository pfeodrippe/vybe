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
 *     ecs_id_t *array;
 *     int32_t count;
 * }
 * }
 */
public class ecs_type_t {

    ecs_type_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("array"),
        flecs.C_INT.withName("count"),
        MemoryLayout.paddingLayout(4)
    ).withName("$anon$2775:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout array$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("array"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_id_t *array
     * }
     */
    public static final AddressLayout array$layout() {
        return array$LAYOUT;
    }

    private static final long array$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_id_t *array
     * }
     */
    public static final long array$offset() {
        return array$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_id_t *array
     * }
     */
    public static MemorySegment array(MemorySegment struct) {
        return struct.get(array$LAYOUT, array$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_id_t *array
     * }
     */
    public static void array(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(array$LAYOUT, array$OFFSET, fieldValue);
    }

    private static final OfInt count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static final OfInt count$layout() {
        return count$LAYOUT;
    }

    private static final long count$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static final long count$offset() {
        return count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static int count(MemorySegment struct) {
        return struct.get(count$LAYOUT, count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static void count(MemorySegment struct, int fieldValue) {
        struct.set(count$LAYOUT, count$OFFSET, fieldValue);
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
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction) (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction) (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

