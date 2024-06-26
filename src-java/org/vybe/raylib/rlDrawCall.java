// Generated by jextract

package org.vybe.raylib;

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
 * struct rlDrawCall {
 *     int mode;
 *     int vertexCount;
 *     int vertexAlignment;
 *     unsigned int textureId;
 * }
 * }
 */
public class rlDrawCall {

    rlDrawCall() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        raylib.C_INT.withName("mode"),
        raylib.C_INT.withName("vertexCount"),
        raylib.C_INT.withName("vertexAlignment"),
        raylib.C_INT.withName("textureId")
    ).withName("rlDrawCall");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt mode$LAYOUT = (OfInt)$LAYOUT.select(groupElement("mode"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int mode
     * }
     */
    public static final OfInt mode$layout() {
        return mode$LAYOUT;
    }

    private static final long mode$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int mode
     * }
     */
    public static final long mode$offset() {
        return mode$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int mode
     * }
     */
    public static int mode(MemorySegment struct) {
        return struct.get(mode$LAYOUT, mode$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int mode
     * }
     */
    public static void mode(MemorySegment struct, int fieldValue) {
        struct.set(mode$LAYOUT, mode$OFFSET, fieldValue);
    }

    private static final OfInt vertexCount$LAYOUT = (OfInt)$LAYOUT.select(groupElement("vertexCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int vertexCount
     * }
     */
    public static final OfInt vertexCount$layout() {
        return vertexCount$LAYOUT;
    }

    private static final long vertexCount$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int vertexCount
     * }
     */
    public static final long vertexCount$offset() {
        return vertexCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int vertexCount
     * }
     */
    public static int vertexCount(MemorySegment struct) {
        return struct.get(vertexCount$LAYOUT, vertexCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int vertexCount
     * }
     */
    public static void vertexCount(MemorySegment struct, int fieldValue) {
        struct.set(vertexCount$LAYOUT, vertexCount$OFFSET, fieldValue);
    }

    private static final OfInt vertexAlignment$LAYOUT = (OfInt)$LAYOUT.select(groupElement("vertexAlignment"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int vertexAlignment
     * }
     */
    public static final OfInt vertexAlignment$layout() {
        return vertexAlignment$LAYOUT;
    }

    private static final long vertexAlignment$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int vertexAlignment
     * }
     */
    public static final long vertexAlignment$offset() {
        return vertexAlignment$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int vertexAlignment
     * }
     */
    public static int vertexAlignment(MemorySegment struct) {
        return struct.get(vertexAlignment$LAYOUT, vertexAlignment$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int vertexAlignment
     * }
     */
    public static void vertexAlignment(MemorySegment struct, int fieldValue) {
        struct.set(vertexAlignment$LAYOUT, vertexAlignment$OFFSET, fieldValue);
    }

    private static final OfInt textureId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("textureId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int textureId
     * }
     */
    public static final OfInt textureId$layout() {
        return textureId$LAYOUT;
    }

    private static final long textureId$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int textureId
     * }
     */
    public static final long textureId$offset() {
        return textureId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int textureId
     * }
     */
    public static int textureId(MemorySegment struct) {
        return struct.get(textureId$LAYOUT, textureId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int textureId
     * }
     */
    public static void textureId(MemorySegment struct, int fieldValue) {
        struct.set(textureId$LAYOUT, textureId$OFFSET, fieldValue);
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

