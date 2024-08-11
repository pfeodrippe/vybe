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
 * struct cn_transport_config_t {
 *     void *mem_ctx;
 *     float resend_rate;
 *     int fragment_size;
 *     int max_packet_size;
 *     int max_fragments_in_flight;
 *     int max_size_single_send;
 *     int send_receive_queue_size;
 *     void *user_allocator_context;
 *     void *udata;
 *     int index;
 *     cn_result_t (*send_packet_fn)(int, void *, int, void *);
 * }
 * }
 */
public class cn_transport_config_t {

    cn_transport_config_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_POINTER.withName("mem_ctx"),
        netcode.C_FLOAT.withName("resend_rate"),
        netcode.C_INT.withName("fragment_size"),
        netcode.C_INT.withName("max_packet_size"),
        netcode.C_INT.withName("max_fragments_in_flight"),
        netcode.C_INT.withName("max_size_single_send"),
        netcode.C_INT.withName("send_receive_queue_size"),
        netcode.C_POINTER.withName("user_allocator_context"),
        netcode.C_POINTER.withName("udata"),
        netcode.C_INT.withName("index"),
        MemoryLayout.paddingLayout(4),
        netcode.C_POINTER.withName("send_packet_fn")
    ).withName("cn_transport_config_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
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

    private static final long mem_ctx$OFFSET = 0;

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

    private static final OfFloat resend_rate$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("resend_rate"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float resend_rate
     * }
     */
    public static final OfFloat resend_rate$layout() {
        return resend_rate$LAYOUT;
    }

    private static final long resend_rate$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float resend_rate
     * }
     */
    public static final long resend_rate$offset() {
        return resend_rate$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float resend_rate
     * }
     */
    public static float resend_rate(MemorySegment struct) {
        return struct.get(resend_rate$LAYOUT, resend_rate$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float resend_rate
     * }
     */
    public static void resend_rate(MemorySegment struct, float fieldValue) {
        struct.set(resend_rate$LAYOUT, resend_rate$OFFSET, fieldValue);
    }

    private static final OfInt fragment_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("fragment_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int fragment_size
     * }
     */
    public static final OfInt fragment_size$layout() {
        return fragment_size$LAYOUT;
    }

    private static final long fragment_size$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int fragment_size
     * }
     */
    public static final long fragment_size$offset() {
        return fragment_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int fragment_size
     * }
     */
    public static int fragment_size(MemorySegment struct) {
        return struct.get(fragment_size$LAYOUT, fragment_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int fragment_size
     * }
     */
    public static void fragment_size(MemorySegment struct, int fieldValue) {
        struct.set(fragment_size$LAYOUT, fragment_size$OFFSET, fieldValue);
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

    private static final long max_packet_size$OFFSET = 16;

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

    private static final OfInt max_fragments_in_flight$LAYOUT = (OfInt)$LAYOUT.select(groupElement("max_fragments_in_flight"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int max_fragments_in_flight
     * }
     */
    public static final OfInt max_fragments_in_flight$layout() {
        return max_fragments_in_flight$LAYOUT;
    }

    private static final long max_fragments_in_flight$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int max_fragments_in_flight
     * }
     */
    public static final long max_fragments_in_flight$offset() {
        return max_fragments_in_flight$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int max_fragments_in_flight
     * }
     */
    public static int max_fragments_in_flight(MemorySegment struct) {
        return struct.get(max_fragments_in_flight$LAYOUT, max_fragments_in_flight$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int max_fragments_in_flight
     * }
     */
    public static void max_fragments_in_flight(MemorySegment struct, int fieldValue) {
        struct.set(max_fragments_in_flight$LAYOUT, max_fragments_in_flight$OFFSET, fieldValue);
    }

    private static final OfInt max_size_single_send$LAYOUT = (OfInt)$LAYOUT.select(groupElement("max_size_single_send"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int max_size_single_send
     * }
     */
    public static final OfInt max_size_single_send$layout() {
        return max_size_single_send$LAYOUT;
    }

    private static final long max_size_single_send$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int max_size_single_send
     * }
     */
    public static final long max_size_single_send$offset() {
        return max_size_single_send$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int max_size_single_send
     * }
     */
    public static int max_size_single_send(MemorySegment struct) {
        return struct.get(max_size_single_send$LAYOUT, max_size_single_send$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int max_size_single_send
     * }
     */
    public static void max_size_single_send(MemorySegment struct, int fieldValue) {
        struct.set(max_size_single_send$LAYOUT, max_size_single_send$OFFSET, fieldValue);
    }

    private static final OfInt send_receive_queue_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("send_receive_queue_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int send_receive_queue_size
     * }
     */
    public static final OfInt send_receive_queue_size$layout() {
        return send_receive_queue_size$LAYOUT;
    }

    private static final long send_receive_queue_size$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int send_receive_queue_size
     * }
     */
    public static final long send_receive_queue_size$offset() {
        return send_receive_queue_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int send_receive_queue_size
     * }
     */
    public static int send_receive_queue_size(MemorySegment struct) {
        return struct.get(send_receive_queue_size$LAYOUT, send_receive_queue_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int send_receive_queue_size
     * }
     */
    public static void send_receive_queue_size(MemorySegment struct, int fieldValue) {
        struct.set(send_receive_queue_size$LAYOUT, send_receive_queue_size$OFFSET, fieldValue);
    }

    private static final AddressLayout user_allocator_context$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("user_allocator_context"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *user_allocator_context
     * }
     */
    public static final AddressLayout user_allocator_context$layout() {
        return user_allocator_context$LAYOUT;
    }

    private static final long user_allocator_context$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *user_allocator_context
     * }
     */
    public static final long user_allocator_context$offset() {
        return user_allocator_context$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *user_allocator_context
     * }
     */
    public static MemorySegment user_allocator_context(MemorySegment struct) {
        return struct.get(user_allocator_context$LAYOUT, user_allocator_context$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *user_allocator_context
     * }
     */
    public static void user_allocator_context(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(user_allocator_context$LAYOUT, user_allocator_context$OFFSET, fieldValue);
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

    private static final long udata$OFFSET = 40;

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

    private static final long index$OFFSET = 48;

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

    private static final long send_packet_fn$OFFSET = 56;

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

