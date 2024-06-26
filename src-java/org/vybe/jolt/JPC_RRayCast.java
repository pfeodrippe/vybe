// Generated by jextract

package org.vybe.jolt;

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
 * struct JPC_RRayCast {
 *     JPC_Real origin[4];
 *     float direction[4];
 * }
 * }
 */
public class JPC_RRayCast {

    JPC_RRayCast() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("origin"),
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("direction")
    ).withName("JPC_RRayCast");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final SequenceLayout origin$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("origin"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static final SequenceLayout origin$layout() {
        return origin$LAYOUT;
    }

    private static final long origin$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static final long origin$offset() {
        return origin$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static MemorySegment origin(MemorySegment struct) {
        return struct.asSlice(origin$OFFSET, origin$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static void origin(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, origin$OFFSET, origin$LAYOUT.byteSize());
    }

    private static long[] origin$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static long[] origin$dimensions() {
        return origin$DIMS;
    }
    private static final VarHandle origin$ELEM_HANDLE = origin$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static float origin(MemorySegment struct, long index0) {
        return (float)origin$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * JPC_Real origin[4]
     * }
     */
    public static void origin(MemorySegment struct, long index0, float fieldValue) {
        origin$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout direction$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("direction"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static final SequenceLayout direction$layout() {
        return direction$LAYOUT;
    }

    private static final long direction$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static final long direction$offset() {
        return direction$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static MemorySegment direction(MemorySegment struct) {
        return struct.asSlice(direction$OFFSET, direction$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static void direction(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, direction$OFFSET, direction$LAYOUT.byteSize());
    }

    private static long[] direction$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static long[] direction$dimensions() {
        return direction$DIMS;
    }
    private static final VarHandle direction$ELEM_HANDLE = direction$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static float direction(MemorySegment struct, long index0) {
        return (float)direction$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float direction[4]
     * }
     */
    public static void direction(MemorySegment struct, long index0, float fieldValue) {
        direction$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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

