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
 * struct netcode_client_config_t {
 *     void *allocator_context;
 *     void *(*allocate_function)(void *, size_t);
 *     void (*free_function)(void *, void *);
 *     struct netcode_network_simulator_t *network_simulator;
 *     void *callback_context;
 *     void (*state_change_callback)(void *, int, int);
 *     void (*send_loopback_packet_callback)(void *, int, const uint8_t *, int, uint64_t);
 *     int override_send_and_receive;
 *     void (*send_packet_override)(void *, struct netcode_address_t *, const uint8_t *, int);
 *     int (*receive_packet_override)(void *, struct netcode_address_t *, uint8_t *, int);
 * }
 * }
 */
public class netcode_client_config_t {

    netcode_client_config_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_POINTER.withName("allocator_context"),
        netcode.C_POINTER.withName("allocate_function"),
        netcode.C_POINTER.withName("free_function"),
        netcode.C_POINTER.withName("network_simulator"),
        netcode.C_POINTER.withName("callback_context"),
        netcode.C_POINTER.withName("state_change_callback"),
        netcode.C_POINTER.withName("send_loopback_packet_callback"),
        netcode.C_INT.withName("override_send_and_receive"),
        MemoryLayout.paddingLayout(4),
        netcode.C_POINTER.withName("send_packet_override"),
        netcode.C_POINTER.withName("receive_packet_override")
    ).withName("netcode_client_config_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout allocator_context$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("allocator_context"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *allocator_context
     * }
     */
    public static final AddressLayout allocator_context$layout() {
        return allocator_context$LAYOUT;
    }

    private static final long allocator_context$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *allocator_context
     * }
     */
    public static final long allocator_context$offset() {
        return allocator_context$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *allocator_context
     * }
     */
    public static MemorySegment allocator_context(MemorySegment struct) {
        return struct.get(allocator_context$LAYOUT, allocator_context$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *allocator_context
     * }
     */
    public static void allocator_context(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(allocator_context$LAYOUT, allocator_context$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void *(*allocate_function)(void *, size_t)
     * }
     */
    public static class allocate_function {

        allocate_function() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            MemorySegment apply(MemorySegment _x0, long _x1);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            netcode.C_POINTER,
            netcode.C_POINTER,
            netcode.C_LONG
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(allocate_function.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(allocate_function.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static MemorySegment invoke(MemorySegment funcPtr,MemorySegment _x0, long _x1) {
            try {
                return (MemorySegment) DOWN$MH.invokeExact(funcPtr, _x0, _x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout allocate_function$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("allocate_function"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *(*allocate_function)(void *, size_t)
     * }
     */
    public static final AddressLayout allocate_function$layout() {
        return allocate_function$LAYOUT;
    }

    private static final long allocate_function$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *(*allocate_function)(void *, size_t)
     * }
     */
    public static final long allocate_function$offset() {
        return allocate_function$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *(*allocate_function)(void *, size_t)
     * }
     */
    public static MemorySegment allocate_function(MemorySegment struct) {
        return struct.get(allocate_function$LAYOUT, allocate_function$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *(*allocate_function)(void *, size_t)
     * }
     */
    public static void allocate_function(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(allocate_function$LAYOUT, allocate_function$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*free_function)(void *, void *)
     * }
     */
    public static class free_function {

        free_function() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            netcode.C_POINTER,
            netcode.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(free_function.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(free_function.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout free_function$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("free_function"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*free_function)(void *, void *)
     * }
     */
    public static final AddressLayout free_function$layout() {
        return free_function$LAYOUT;
    }

    private static final long free_function$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*free_function)(void *, void *)
     * }
     */
    public static final long free_function$offset() {
        return free_function$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*free_function)(void *, void *)
     * }
     */
    public static MemorySegment free_function(MemorySegment struct) {
        return struct.get(free_function$LAYOUT, free_function$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*free_function)(void *, void *)
     * }
     */
    public static void free_function(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(free_function$LAYOUT, free_function$OFFSET, fieldValue);
    }

    private static final AddressLayout network_simulator$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("network_simulator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct netcode_network_simulator_t *network_simulator
     * }
     */
    public static final AddressLayout network_simulator$layout() {
        return network_simulator$LAYOUT;
    }

    private static final long network_simulator$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct netcode_network_simulator_t *network_simulator
     * }
     */
    public static final long network_simulator$offset() {
        return network_simulator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct netcode_network_simulator_t *network_simulator
     * }
     */
    public static MemorySegment network_simulator(MemorySegment struct) {
        return struct.get(network_simulator$LAYOUT, network_simulator$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct netcode_network_simulator_t *network_simulator
     * }
     */
    public static void network_simulator(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(network_simulator$LAYOUT, network_simulator$OFFSET, fieldValue);
    }

    private static final AddressLayout callback_context$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("callback_context"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *callback_context
     * }
     */
    public static final AddressLayout callback_context$layout() {
        return callback_context$LAYOUT;
    }

    private static final long callback_context$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *callback_context
     * }
     */
    public static final long callback_context$offset() {
        return callback_context$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *callback_context
     * }
     */
    public static MemorySegment callback_context(MemorySegment struct) {
        return struct.get(callback_context$LAYOUT, callback_context$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *callback_context
     * }
     */
    public static void callback_context(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(callback_context$LAYOUT, callback_context$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*state_change_callback)(void *, int, int)
     * }
     */
    public static class state_change_callback {

        state_change_callback() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, int _x1, int _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            netcode.C_POINTER,
            netcode.C_INT,
            netcode.C_INT
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(state_change_callback.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(state_change_callback.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, int _x1, int _x2) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout state_change_callback$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("state_change_callback"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*state_change_callback)(void *, int, int)
     * }
     */
    public static final AddressLayout state_change_callback$layout() {
        return state_change_callback$LAYOUT;
    }

    private static final long state_change_callback$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*state_change_callback)(void *, int, int)
     * }
     */
    public static final long state_change_callback$offset() {
        return state_change_callback$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*state_change_callback)(void *, int, int)
     * }
     */
    public static MemorySegment state_change_callback(MemorySegment struct) {
        return struct.get(state_change_callback$LAYOUT, state_change_callback$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*state_change_callback)(void *, int, int)
     * }
     */
    public static void state_change_callback(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(state_change_callback$LAYOUT, state_change_callback$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*send_loopback_packet_callback)(void *, int, const uint8_t *, int, uint64_t)
     * }
     */
    public static class send_loopback_packet_callback {

        send_loopback_packet_callback() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, int _x1, MemorySegment _x2, int _x3, long _x4);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            netcode.C_POINTER,
            netcode.C_INT,
            netcode.C_POINTER,
            netcode.C_INT,
            netcode.C_LONG_LONG
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(send_loopback_packet_callback.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(send_loopback_packet_callback.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, int _x1, MemorySegment _x2, int _x3, long _x4) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2, _x3, _x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout send_loopback_packet_callback$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("send_loopback_packet_callback"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*send_loopback_packet_callback)(void *, int, const uint8_t *, int, uint64_t)
     * }
     */
    public static final AddressLayout send_loopback_packet_callback$layout() {
        return send_loopback_packet_callback$LAYOUT;
    }

    private static final long send_loopback_packet_callback$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*send_loopback_packet_callback)(void *, int, const uint8_t *, int, uint64_t)
     * }
     */
    public static final long send_loopback_packet_callback$offset() {
        return send_loopback_packet_callback$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*send_loopback_packet_callback)(void *, int, const uint8_t *, int, uint64_t)
     * }
     */
    public static MemorySegment send_loopback_packet_callback(MemorySegment struct) {
        return struct.get(send_loopback_packet_callback$LAYOUT, send_loopback_packet_callback$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*send_loopback_packet_callback)(void *, int, const uint8_t *, int, uint64_t)
     * }
     */
    public static void send_loopback_packet_callback(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(send_loopback_packet_callback$LAYOUT, send_loopback_packet_callback$OFFSET, fieldValue);
    }

    private static final OfInt override_send_and_receive$LAYOUT = (OfInt)$LAYOUT.select(groupElement("override_send_and_receive"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int override_send_and_receive
     * }
     */
    public static final OfInt override_send_and_receive$layout() {
        return override_send_and_receive$LAYOUT;
    }

    private static final long override_send_and_receive$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int override_send_and_receive
     * }
     */
    public static final long override_send_and_receive$offset() {
        return override_send_and_receive$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int override_send_and_receive
     * }
     */
    public static int override_send_and_receive(MemorySegment struct) {
        return struct.get(override_send_and_receive$LAYOUT, override_send_and_receive$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int override_send_and_receive
     * }
     */
    public static void override_send_and_receive(MemorySegment struct, int fieldValue) {
        struct.set(override_send_and_receive$LAYOUT, override_send_and_receive$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*send_packet_override)(void *, struct netcode_address_t *, const uint8_t *, int)
     * }
     */
    public static class send_packet_override {

        send_packet_override() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2, int _x3);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            netcode.C_POINTER,
            netcode.C_POINTER,
            netcode.C_POINTER,
            netcode.C_INT
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(send_packet_override.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(send_packet_override.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2, int _x3) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2, _x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout send_packet_override$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("send_packet_override"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*send_packet_override)(void *, struct netcode_address_t *, const uint8_t *, int)
     * }
     */
    public static final AddressLayout send_packet_override$layout() {
        return send_packet_override$LAYOUT;
    }

    private static final long send_packet_override$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*send_packet_override)(void *, struct netcode_address_t *, const uint8_t *, int)
     * }
     */
    public static final long send_packet_override$offset() {
        return send_packet_override$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*send_packet_override)(void *, struct netcode_address_t *, const uint8_t *, int)
     * }
     */
    public static MemorySegment send_packet_override(MemorySegment struct) {
        return struct.get(send_packet_override$LAYOUT, send_packet_override$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*send_packet_override)(void *, struct netcode_address_t *, const uint8_t *, int)
     * }
     */
    public static void send_packet_override(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(send_packet_override$LAYOUT, send_packet_override$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * int (*receive_packet_override)(void *, struct netcode_address_t *, uint8_t *, int)
     * }
     */
    public static class receive_packet_override {

        receive_packet_override() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2, int _x3);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            netcode.C_INT,
            netcode.C_POINTER,
            netcode.C_POINTER,
            netcode.C_POINTER,
            netcode.C_INT
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = netcode.upcallHandle(receive_packet_override.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(receive_packet_override.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2, int _x3) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2, _x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout receive_packet_override$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("receive_packet_override"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int (*receive_packet_override)(void *, struct netcode_address_t *, uint8_t *, int)
     * }
     */
    public static final AddressLayout receive_packet_override$layout() {
        return receive_packet_override$LAYOUT;
    }

    private static final long receive_packet_override$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int (*receive_packet_override)(void *, struct netcode_address_t *, uint8_t *, int)
     * }
     */
    public static final long receive_packet_override$offset() {
        return receive_packet_override$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int (*receive_packet_override)(void *, struct netcode_address_t *, uint8_t *, int)
     * }
     */
    public static MemorySegment receive_packet_override(MemorySegment struct) {
        return struct.get(receive_packet_override$LAYOUT, receive_packet_override$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int (*receive_packet_override)(void *, struct netcode_address_t *, uint8_t *, int)
     * }
     */
    public static void receive_packet_override(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(receive_packet_override$LAYOUT, receive_packet_override$OFFSET, fieldValue);
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

