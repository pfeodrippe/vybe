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
 * struct cn_ack_system_t {
 *     double time;
 *     int max_packet_size;
 *     void *udata;
 *     void *mem_ctx;
 *     uint16_t sequence;
 *     int acks_count;
 *     int acks_capacity;
 *     uint16_t *acks;
 *     cn_sequence_buffer_t sent_packets;
 *     cn_sequence_buffer_t received_packets;
 *     double rtt;
 *     double packet_loss;
 *     double outgoing_bandwidth_kbps;
 *     double incoming_bandwidth_kbps;
 *     int index;
 *     cn_result_t (*send_packet_fn)(int, void *, int, void *);
 *     uint64_t counters[7];
 * }
 * }
 */
public class cn_ack_system_t {

    cn_ack_system_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_DOUBLE.withName("time"),
        netcode.C_INT.withName("max_packet_size"),
        MemoryLayout.paddingLayout(4),
        netcode.C_POINTER.withName("udata"),
        netcode.C_POINTER.withName("mem_ctx"),
        netcode.C_SHORT.withName("sequence"),
        MemoryLayout.paddingLayout(2),
        netcode.C_INT.withName("acks_count"),
        netcode.C_INT.withName("acks_capacity"),
        MemoryLayout.paddingLayout(4),
        netcode.C_POINTER.withName("acks"),
        cn_sequence_buffer_t.layout().withName("sent_packets"),
        cn_sequence_buffer_t.layout().withName("received_packets"),
        netcode.C_DOUBLE.withName("rtt"),
        netcode.C_DOUBLE.withName("packet_loss"),
        netcode.C_DOUBLE.withName("outgoing_bandwidth_kbps"),
        netcode.C_DOUBLE.withName("incoming_bandwidth_kbps"),
        netcode.C_INT.withName("index"),
        MemoryLayout.paddingLayout(4),
        netcode.C_POINTER.withName("send_packet_fn"),
        MemoryLayout.sequenceLayout(7, netcode.C_LONG_LONG).withName("counters")
    ).withName("cn_ack_system_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfDouble time$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("time"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double time
     * }
     */
    public static final OfDouble time$layout() {
        return time$LAYOUT;
    }

    private static final long time$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double time
     * }
     */
    public static final long time$offset() {
        return time$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double time
     * }
     */
    public static double time(MemorySegment struct) {
        return struct.get(time$LAYOUT, time$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double time
     * }
     */
    public static void time(MemorySegment struct, double fieldValue) {
        struct.set(time$LAYOUT, time$OFFSET, fieldValue);
    }

    private static final OfInt max_packet_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("max_packet_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int max_packet_size
     * }
     */
    public static final OfInt max_packet_size$layout() {
        return max_packet_size$LAYOUT;
    }

    private static final long max_packet_size$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int max_packet_size
     * }
     */
    public static final long max_packet_size$offset() {
        return max_packet_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int max_packet_size
     * }
     */
    public static int max_packet_size(MemorySegment struct) {
        return struct.get(max_packet_size$LAYOUT, max_packet_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int max_packet_size
     * }
     */
    public static void max_packet_size(MemorySegment struct, int fieldValue) {
        struct.set(max_packet_size$LAYOUT, max_packet_size$OFFSET, fieldValue);
    }

    private static final AddressLayout udata$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("udata"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *udata
     * }
     */
    public static final AddressLayout udata$layout() {
        return udata$LAYOUT;
    }

    private static final long udata$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *udata
     * }
     */
    public static final long udata$offset() {
        return udata$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *udata
     * }
     */
    public static MemorySegment udata(MemorySegment struct) {
        return struct.get(udata$LAYOUT, udata$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *udata
     * }
     */
    public static void udata(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(udata$LAYOUT, udata$OFFSET, fieldValue);
    }

    private static final AddressLayout mem_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("mem_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static final AddressLayout mem_ctx$layout() {
        return mem_ctx$LAYOUT;
    }

    private static final long mem_ctx$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static final long mem_ctx$offset() {
        return mem_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static MemorySegment mem_ctx(MemorySegment struct) {
        return struct.get(mem_ctx$LAYOUT, mem_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static void mem_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(mem_ctx$LAYOUT, mem_ctx$OFFSET, fieldValue);
    }

    private static final OfShort sequence$LAYOUT = (OfShort)$LAYOUT.select(groupElement("sequence"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint16_t sequence
     * }
     */
    public static final OfShort sequence$layout() {
        return sequence$LAYOUT;
    }

    private static final long sequence$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint16_t sequence
     * }
     */
    public static final long sequence$offset() {
        return sequence$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint16_t sequence
     * }
     */
    public static short sequence(MemorySegment struct) {
        return struct.get(sequence$LAYOUT, sequence$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint16_t sequence
     * }
     */
    public static void sequence(MemorySegment struct, short fieldValue) {
        struct.set(sequence$LAYOUT, sequence$OFFSET, fieldValue);
    }

    private static final OfInt acks_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("acks_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int acks_count
     * }
     */
    public static final OfInt acks_count$layout() {
        return acks_count$LAYOUT;
    }

    private static final long acks_count$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int acks_count
     * }
     */
    public static final long acks_count$offset() {
        return acks_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int acks_count
     * }
     */
    public static int acks_count(MemorySegment struct) {
        return struct.get(acks_count$LAYOUT, acks_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int acks_count
     * }
     */
    public static void acks_count(MemorySegment struct, int fieldValue) {
        struct.set(acks_count$LAYOUT, acks_count$OFFSET, fieldValue);
    }

    private static final OfInt acks_capacity$LAYOUT = (OfInt)$LAYOUT.select(groupElement("acks_capacity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int acks_capacity
     * }
     */
    public static final OfInt acks_capacity$layout() {
        return acks_capacity$LAYOUT;
    }

    private static final long acks_capacity$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int acks_capacity
     * }
     */
    public static final long acks_capacity$offset() {
        return acks_capacity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int acks_capacity
     * }
     */
    public static int acks_capacity(MemorySegment struct) {
        return struct.get(acks_capacity$LAYOUT, acks_capacity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int acks_capacity
     * }
     */
    public static void acks_capacity(MemorySegment struct, int fieldValue) {
        struct.set(acks_capacity$LAYOUT, acks_capacity$OFFSET, fieldValue);
    }

    private static final AddressLayout acks$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("acks"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint16_t *acks
     * }
     */
    public static final AddressLayout acks$layout() {
        return acks$LAYOUT;
    }

    private static final long acks$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint16_t *acks
     * }
     */
    public static final long acks$offset() {
        return acks$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint16_t *acks
     * }
     */
    public static MemorySegment acks(MemorySegment struct) {
        return struct.get(acks$LAYOUT, acks$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint16_t *acks
     * }
     */
    public static void acks(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(acks$LAYOUT, acks$OFFSET, fieldValue);
    }

    private static final GroupLayout sent_packets$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("sent_packets"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t sent_packets
     * }
     */
    public static final GroupLayout sent_packets$layout() {
        return sent_packets$LAYOUT;
    }

    private static final long sent_packets$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t sent_packets
     * }
     */
    public static final long sent_packets$offset() {
        return sent_packets$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t sent_packets
     * }
     */
    public static MemorySegment sent_packets(MemorySegment struct) {
        return struct.asSlice(sent_packets$OFFSET, sent_packets$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t sent_packets
     * }
     */
    public static void sent_packets(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, sent_packets$OFFSET, sent_packets$LAYOUT.byteSize());
    }

    private static final GroupLayout received_packets$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("received_packets"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t received_packets
     * }
     */
    public static final GroupLayout received_packets$layout() {
        return received_packets$LAYOUT;
    }

    private static final long received_packets$OFFSET = 104;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t received_packets
     * }
     */
    public static final long received_packets$offset() {
        return received_packets$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t received_packets
     * }
     */
    public static MemorySegment received_packets(MemorySegment struct) {
        return struct.asSlice(received_packets$OFFSET, received_packets$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_sequence_buffer_t received_packets
     * }
     */
    public static void received_packets(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, received_packets$OFFSET, received_packets$LAYOUT.byteSize());
    }

    private static final OfDouble rtt$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("rtt"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double rtt
     * }
     */
    public static final OfDouble rtt$layout() {
        return rtt$LAYOUT;
    }

    private static final long rtt$OFFSET = 152;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double rtt
     * }
     */
    public static final long rtt$offset() {
        return rtt$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double rtt
     * }
     */
    public static double rtt(MemorySegment struct) {
        return struct.get(rtt$LAYOUT, rtt$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double rtt
     * }
     */
    public static void rtt(MemorySegment struct, double fieldValue) {
        struct.set(rtt$LAYOUT, rtt$OFFSET, fieldValue);
    }

    private static final OfDouble packet_loss$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("packet_loss"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double packet_loss
     * }
     */
    public static final OfDouble packet_loss$layout() {
        return packet_loss$LAYOUT;
    }

    private static final long packet_loss$OFFSET = 160;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double packet_loss
     * }
     */
    public static final long packet_loss$offset() {
        return packet_loss$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double packet_loss
     * }
     */
    public static double packet_loss(MemorySegment struct) {
        return struct.get(packet_loss$LAYOUT, packet_loss$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double packet_loss
     * }
     */
    public static void packet_loss(MemorySegment struct, double fieldValue) {
        struct.set(packet_loss$LAYOUT, packet_loss$OFFSET, fieldValue);
    }

    private static final OfDouble outgoing_bandwidth_kbps$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("outgoing_bandwidth_kbps"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double outgoing_bandwidth_kbps
     * }
     */
    public static final OfDouble outgoing_bandwidth_kbps$layout() {
        return outgoing_bandwidth_kbps$LAYOUT;
    }

    private static final long outgoing_bandwidth_kbps$OFFSET = 168;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double outgoing_bandwidth_kbps
     * }
     */
    public static final long outgoing_bandwidth_kbps$offset() {
        return outgoing_bandwidth_kbps$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double outgoing_bandwidth_kbps
     * }
     */
    public static double outgoing_bandwidth_kbps(MemorySegment struct) {
        return struct.get(outgoing_bandwidth_kbps$LAYOUT, outgoing_bandwidth_kbps$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double outgoing_bandwidth_kbps
     * }
     */
    public static void outgoing_bandwidth_kbps(MemorySegment struct, double fieldValue) {
        struct.set(outgoing_bandwidth_kbps$LAYOUT, outgoing_bandwidth_kbps$OFFSET, fieldValue);
    }

    private static final OfDouble incoming_bandwidth_kbps$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("incoming_bandwidth_kbps"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double incoming_bandwidth_kbps
     * }
     */
    public static final OfDouble incoming_bandwidth_kbps$layout() {
        return incoming_bandwidth_kbps$LAYOUT;
    }

    private static final long incoming_bandwidth_kbps$OFFSET = 176;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double incoming_bandwidth_kbps
     * }
     */
    public static final long incoming_bandwidth_kbps$offset() {
        return incoming_bandwidth_kbps$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double incoming_bandwidth_kbps
     * }
     */
    public static double incoming_bandwidth_kbps(MemorySegment struct) {
        return struct.get(incoming_bandwidth_kbps$LAYOUT, incoming_bandwidth_kbps$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double incoming_bandwidth_kbps
     * }
     */
    public static void incoming_bandwidth_kbps(MemorySegment struct, double fieldValue) {
        struct.set(incoming_bandwidth_kbps$LAYOUT, incoming_bandwidth_kbps$OFFSET, fieldValue);
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

    private static final long index$OFFSET = 184;

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

    /**
     * {@snippet lang=c :
     * cn_result_t (*send_packet_fn)(int, void *, int, void *)
     * }
     */
    public static class send_packet_fn {

        send_packet_fn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            MemorySegment apply(int _x0, MemorySegment _x1, int _x2, MemorySegment _x3);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            cn_result_t.layout(),
            netcode.C_INT,
            netcode.C_POINTER,
            netcode.C_INT,
            netcode.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(send_packet_fn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(send_packet_fn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static MemorySegment invoke(MemorySegment funcPtr, SegmentAllocator alloc,int _x0, MemorySegment _x1, int _x2, MemorySegment _x3) {
            try {
                return (MemorySegment) DOWN$MH.invokeExact(funcPtr, alloc, _x0, _x1, _x2, _x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout send_packet_fn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("send_packet_fn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_result_t (*send_packet_fn)(int, void *, int, void *)
     * }
     */
    public static final AddressLayout send_packet_fn$layout() {
        return send_packet_fn$LAYOUT;
    }

    private static final long send_packet_fn$OFFSET = 192;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_result_t (*send_packet_fn)(int, void *, int, void *)
     * }
     */
    public static final long send_packet_fn$offset() {
        return send_packet_fn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_result_t (*send_packet_fn)(int, void *, int, void *)
     * }
     */
    public static MemorySegment send_packet_fn(MemorySegment struct) {
        return struct.get(send_packet_fn$LAYOUT, send_packet_fn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_result_t (*send_packet_fn)(int, void *, int, void *)
     * }
     */
    public static void send_packet_fn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(send_packet_fn$LAYOUT, send_packet_fn$OFFSET, fieldValue);
    }

    private static final SequenceLayout counters$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("counters"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static final SequenceLayout counters$layout() {
        return counters$LAYOUT;
    }

    private static final long counters$OFFSET = 200;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static final long counters$offset() {
        return counters$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static MemorySegment counters(MemorySegment struct) {
        return struct.asSlice(counters$OFFSET, counters$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static void counters(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, counters$OFFSET, counters$LAYOUT.byteSize());
    }

    private static long[] counters$DIMS = { 7 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static long[] counters$dimensions() {
        return counters$DIMS;
    }
    private static final VarHandle counters$ELEM_HANDLE = counters$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static long counters(MemorySegment struct, long index0) {
        return (long)counters$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * uint64_t counters[7]
     * }
     */
    public static void counters(MemorySegment struct, long index0, long fieldValue) {
        counters$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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
