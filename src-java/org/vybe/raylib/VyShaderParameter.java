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
 * struct VyShaderParameter {
 *     char *name;
 *     int type;
 *     int size;
 *     int loc;
 * }
 * }
 */
public class VyShaderParameter {

    VyShaderParameter() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        raylib.C_POINTER.withName("name"),
        raylib.C_INT.withName("type"),
        raylib.C_INT.withName("size"),
        raylib.C_INT.withName("loc"),
        MemoryLayout.paddingLayout(4)
    ).withName("VyShaderParameter");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout name$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("name"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char *name
     * }
     */
    public static final AddressLayout name$layout() {
        return name$LAYOUT;
    }

    private static final long name$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char *name
     * }
     */
    public static final long name$offset() {
        return name$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char *name
     * }
     */
    public static MemorySegment name(MemorySegment struct) {
        return struct.get(name$LAYOUT, name$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char *name
     * }
     */
    public static void name(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(name$LAYOUT, name$OFFSET, fieldValue);
    }

    private static final OfInt type$LAYOUT = (OfInt)$LAYOUT.select(groupElement("type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int type
     * }
     */
    public static final OfInt type$layout() {
        return type$LAYOUT;
    }

    private static final long type$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int type
     * }
     */
    public static final long type$offset() {
        return type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int type
     * }
     */
    public static int type(MemorySegment struct) {
        return struct.get(type$LAYOUT, type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int type
     * }
     */
    public static void type(MemorySegment struct, int fieldValue) {
        struct.set(type$LAYOUT, type$OFFSET, fieldValue);
    }

    private static final OfInt size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int size
     * }
     */
    public static final OfInt size$layout() {
        return size$LAYOUT;
    }

    private static final long size$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int size
     * }
     */
    public static final long size$offset() {
        return size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int size
     * }
     */
    public static int size(MemorySegment struct) {
        return struct.get(size$LAYOUT, size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int size
     * }
     */
    public static void size(MemorySegment struct, int fieldValue) {
        struct.set(size$LAYOUT, size$OFFSET, fieldValue);
    }

    private static final OfInt loc$LAYOUT = (OfInt)$LAYOUT.select(groupElement("loc"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int loc
     * }
     */
    public static final OfInt loc$layout() {
        return loc$LAYOUT;
    }

    private static final long loc$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int loc
     * }
     */
    public static final long loc$offset() {
        return loc$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int loc
     * }
     */
    public static int loc(MemorySegment struct) {
        return struct.get(loc$LAYOUT, loc$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int loc
     * }
     */
    public static void loc(MemorySegment struct, int fieldValue) {
        struct.set(loc$LAYOUT, loc$OFFSET, fieldValue);
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

