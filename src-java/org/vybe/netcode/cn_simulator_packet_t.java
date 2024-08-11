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
 * struct cn_simulator_packet_t {
 *     double delay;
 *     cn_endpoint_t to;
 *     void *data;
 *     int size;
 * }
 * }
 */
public class cn_simulator_packet_t {

    cn_simulator_packet_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_DOUBLE.withName("delay"),
        cn_endpoint_t.layout().withName("to"),
        netcode.C_POINTER.withName("data"),
        netcode.C_INT.withName("size"),
        MemoryLayout.paddingLayout(4)
    ).withName("cn_simulator_packet_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfDouble delay$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("delay"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double delay
     * }
     */
    public static final OfDouble delay$layout() {
        return delay$LAYOUT;
    }

    private static final long delay$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double delay
     * }
     */
    public static final long delay$offset() {
        return delay$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double delay
     * }
     */
    public static double delay(MemorySegment struct) {
        return struct.get(delay$LAYOUT, delay$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double delay
     * }
     */
    public static void delay(MemorySegment struct, double fieldValue) {
        struct.set(delay$LAYOUT, delay$OFFSET, fieldValue);
    }

    private static final GroupLayout to$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("to"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_endpoint_t to
     * }
     */
    public static final GroupLayout to$layout() {
        return to$LAYOUT;
    }

    private static final long to$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_endpoint_t to
     * }
     */
    public static final long to$offset() {
        return to$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_endpoint_t to
     * }
     */
    public static MemorySegment to(MemorySegment struct) {
        return struct.asSlice(to$OFFSET, to$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_endpoint_t to
     * }
     */
    public static void to(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, to$OFFSET, to$LAYOUT.byteSize());
    }

    private static final AddressLayout data$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *data
     * }
     */
    public static final AddressLayout data$layout() {
        return data$LAYOUT;
    }

    private static final long data$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *data
     * }
     */
    public static final long data$offset() {
        return data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *data
     * }
     */
    public static MemorySegment data(MemorySegment struct) {
        return struct.get(data$LAYOUT, data$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *data
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

    private static final long size$OFFSET = 40;

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

