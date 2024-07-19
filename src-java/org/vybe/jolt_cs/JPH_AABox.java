// Generated by jextract

package org.vybe.jolt_cs;

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
 * struct JPH_AABox {
 *     JPH_Vec3 min;
 *     JPH_Vec3 max;
 * }
 * }
 */
public class JPH_AABox {

    JPH_AABox() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        JPH_Vec3.layout().withName("min"),
        JPH_Vec3.layout().withName("max")
    ).withName("JPH_AABox");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout min$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("min"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPH_Vec3 min
     * }
     */
    public static final GroupLayout min$layout() {
        return min$LAYOUT;
    }

    private static final long min$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPH_Vec3 min
     * }
     */
    public static final long min$offset() {
        return min$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPH_Vec3 min
     * }
     */
    public static MemorySegment min(MemorySegment struct) {
        return struct.asSlice(min$OFFSET, min$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPH_Vec3 min
     * }
     */
    public static void min(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, min$OFFSET, min$LAYOUT.byteSize());
    }

    private static final GroupLayout max$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("max"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPH_Vec3 max
     * }
     */
    public static final GroupLayout max$layout() {
        return max$LAYOUT;
    }

    private static final long max$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPH_Vec3 max
     * }
     */
    public static final long max$offset() {
        return max$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPH_Vec3 max
     * }
     */
    public static MemorySegment max(MemorySegment struct) {
        return struct.asSlice(max$OFFSET, max$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPH_Vec3 max
     * }
     */
    public static void max(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, max$OFFSET, max$LAYOUT.byteSize());
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

