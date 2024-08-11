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
 * struct cn_protocol_packet_disconnect_t {
 *     uint8_t packet_type;
 * }
 * }
 */
public class cn_protocol_packet_disconnect_t {

    cn_protocol_packet_disconnect_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_CHAR.withName("packet_type")
    ).withName("cn_protocol_packet_disconnect_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfByte packet_type$LAYOUT = (OfByte)$LAYOUT.select(groupElement("packet_type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t packet_type
     * }
     */
    public static final OfByte packet_type$layout() {
        return packet_type$LAYOUT;
    }

    private static final long packet_type$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t packet_type
     * }
     */
    public static final long packet_type$offset() {
        return packet_type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t packet_type
     * }
     */
    public static byte packet_type(MemorySegment struct) {
        return struct.get(packet_type$LAYOUT, packet_type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t packet_type
     * }
     */
    public static void packet_type(MemorySegment struct, byte fieldValue) {
        struct.set(packet_type$LAYOUT, packet_type$OFFSET, fieldValue);
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

