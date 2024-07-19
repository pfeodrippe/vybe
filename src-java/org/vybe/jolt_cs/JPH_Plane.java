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
 * struct JPH_Plane {
 *     JPH_Vec3 normal;
 *     float distance;
 * }
 * }
 */
public class JPH_Plane {

    JPH_Plane() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        JPH_Vec3.layout().withName("normal"),
        jolt_cs.C_FLOAT.withName("distance")
    ).withName("JPH_Plane");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout normal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("normal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPH_Vec3 normal
     * }
     */
    public static final GroupLayout normal$layout() {
        return normal$LAYOUT;
    }

    private static final long normal$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPH_Vec3 normal
     * }
     */
    public static final long normal$offset() {
        return normal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPH_Vec3 normal
     * }
     */
    public static MemorySegment normal(MemorySegment struct) {
        return struct.asSlice(normal$OFFSET, normal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPH_Vec3 normal
     * }
     */
    public static void normal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, normal$OFFSET, normal$LAYOUT.byteSize());
    }

    private static final OfFloat distance$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("distance"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float distance
     * }
     */
    public static final OfFloat distance$layout() {
        return distance$LAYOUT;
    }

    private static final long distance$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float distance
     * }
     */
    public static final long distance$offset() {
        return distance$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float distance
     * }
     */
    public static float distance(MemorySegment struct) {
        return struct.get(distance$LAYOUT, distance$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float distance
     * }
     */
    public static void distance(MemorySegment struct, float fieldValue) {
        struct.set(distance$LAYOUT, distance$OFFSET, fieldValue);
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

