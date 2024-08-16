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
 * struct cn_protocol_packet_challenge_t {
 *     uint8_t packet_type;
 *     uint64_t challenge_nonce;
 *     uint8_t challenge_data[256];
 * }
 * }
 */
public class cn_protocol_packet_challenge_t {

    cn_protocol_packet_challenge_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_CHAR.withName("packet_type"),
        MemoryLayout.paddingLayout(7),
        netcode.C_LONG_LONG.withName("challenge_nonce"),
        MemoryLayout.sequenceLayout(256, netcode.C_CHAR).withName("challenge_data")
    ).withName("cn_protocol_packet_challenge_t");

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

    private static final OfLong challenge_nonce$LAYOUT = (OfLong)$LAYOUT.select(groupElement("challenge_nonce"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static final OfLong challenge_nonce$layout() {
        return challenge_nonce$LAYOUT;
    }

    private static final long challenge_nonce$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static final long challenge_nonce$offset() {
        return challenge_nonce$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static long challenge_nonce(MemorySegment struct) {
        return struct.get(challenge_nonce$LAYOUT, challenge_nonce$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static void challenge_nonce(MemorySegment struct, long fieldValue) {
        struct.set(challenge_nonce$LAYOUT, challenge_nonce$OFFSET, fieldValue);
    }

    private static final SequenceLayout challenge_data$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("challenge_data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static final SequenceLayout challenge_data$layout() {
        return challenge_data$LAYOUT;
    }

    private static final long challenge_data$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static final long challenge_data$offset() {
        return challenge_data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static MemorySegment challenge_data(MemorySegment struct) {
        return struct.asSlice(challenge_data$OFFSET, challenge_data$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static void challenge_data(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, challenge_data$OFFSET, challenge_data$LAYOUT.byteSize());
    }

    private static long[] challenge_data$DIMS = { 256 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static long[] challenge_data$dimensions() {
        return challenge_data$DIMS;
    }
    private static final VarHandle challenge_data$ELEM_HANDLE = challenge_data$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static byte challenge_data(MemorySegment struct, long index0) {
        return (byte)challenge_data$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static void challenge_data(MemorySegment struct, long index0, byte fieldValue) {
        challenge_data$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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
