// Generated by jextract

package org.vybe.netcode;

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
 * struct cn_fragment_t {
 *     uint64_t id;
 *     int index;
 *     double timestamp;
 *     uint8_t *data;
 *     int size;
 * }
 * }
 */
public class cn_fragment_t {

    cn_fragment_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_LONG_LONG.withName("id"),
        netcode.C_INT.withName("index"),
        MemoryLayout.paddingLayout(4),
        netcode.C_DOUBLE.withName("timestamp"),
        netcode.C_POINTER.withName("data"),
        netcode.C_INT.withName("size"),
        MemoryLayout.paddingLayout(4)
    ).withName("cn_fragment_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong id$LAYOUT = (OfLong)$LAYOUT.select(groupElement("id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static final OfLong id$layout() {
        return id$LAYOUT;
    }

    private static final long id$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static final long id$offset() {
        return id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static long id(MemorySegment struct) {
        return struct.get(id$LAYOUT, id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static void id(MemorySegment struct, long fieldValue) {
        struct.set(id$LAYOUT, id$OFFSET, fieldValue);
    }

    private static final OfInt index$LAYOUT = (OfInt)$LAYOUT.select(groupElement("index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int index
     * }
     */
    public static final OfInt index$layout() {
        return index$LAYOUT;
    }

    private static final long index$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int index
     * }
     */
    public static final long index$offset() {
        return index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int index
     * }
     */
    public static int index(MemorySegment struct) {
        return struct.get(index$LAYOUT, index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int index
     * }
     */
    public static void index(MemorySegment struct, int fieldValue) {
        struct.set(index$LAYOUT, index$OFFSET, fieldValue);
    }

    private static final OfDouble timestamp$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("timestamp"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double timestamp
     * }
     */
    public static final OfDouble timestamp$layout() {
        return timestamp$LAYOUT;
    }

    private static final long timestamp$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double timestamp
     * }
     */
    public static final long timestamp$offset() {
        return timestamp$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double timestamp
     * }
     */
    public static double timestamp(MemorySegment struct) {
        return struct.get(timestamp$LAYOUT, timestamp$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double timestamp
     * }
     */
    public static void timestamp(MemorySegment struct, double fieldValue) {
        struct.set(timestamp$LAYOUT, timestamp$OFFSET, fieldValue);
    }

    private static final AddressLayout data$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t *data
     * }
     */
    public static final AddressLayout data$layout() {
        return data$LAYOUT;
    }

    private static final long data$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t *data
     * }
     */
    public static final long data$offset() {
        return data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t *data
     * }
     */
    public static MemorySegment data(MemorySegment struct) {
        return struct.get(data$LAYOUT, data$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t *data
     * }
     */
    public static void data(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(data$LAYOUT, data$OFFSET, fieldValue);
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

    private static final long size$OFFSET = 32;

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
