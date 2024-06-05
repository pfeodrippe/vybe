// Generated by jextract

package org.vybe.flecs;

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
 * struct __darwin_pthread_handler_rec {
 *     void (*__routine)(void *);
 *     void *__arg;
 *     struct __darwin_pthread_handler_rec *__next;
 * }
 * }
 */
public class __darwin_pthread_handler_rec {

    __darwin_pthread_handler_rec() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("__routine"),
        flecs.C_POINTER.withName("__arg"),
        flecs.C_POINTER.withName("__next")
    ).withName("__darwin_pthread_handler_rec");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    /**
     * {@snippet lang=c :
     * void (*__routine)(void *)
     * }
     */
    public static class __routine {

        __routine() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            flecs.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = flecs.upcallHandle(__routine.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(__routine.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout __routine$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("__routine"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*__routine)(void *)
     * }
     */
    public static final AddressLayout __routine$layout() {
        return __routine$LAYOUT;
    }

    private static final long __routine$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*__routine)(void *)
     * }
     */
    public static final long __routine$offset() {
        return __routine$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*__routine)(void *)
     * }
     */
    public static MemorySegment __routine(MemorySegment struct) {
        return struct.get(__routine$LAYOUT, __routine$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*__routine)(void *)
     * }
     */
    public static void __routine(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(__routine$LAYOUT, __routine$OFFSET, fieldValue);
    }

    private static final AddressLayout __arg$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("__arg"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *__arg
     * }
     */
    public static final AddressLayout __arg$layout() {
        return __arg$LAYOUT;
    }

    private static final long __arg$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *__arg
     * }
     */
    public static final long __arg$offset() {
        return __arg$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *__arg
     * }
     */
    public static MemorySegment __arg(MemorySegment struct) {
        return struct.get(__arg$LAYOUT, __arg$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *__arg
     * }
     */
    public static void __arg(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(__arg$LAYOUT, __arg$OFFSET, fieldValue);
    }

    private static final AddressLayout __next$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("__next"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct __darwin_pthread_handler_rec *__next
     * }
     */
    public static final AddressLayout __next$layout() {
        return __next$LAYOUT;
    }

    private static final long __next$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct __darwin_pthread_handler_rec *__next
     * }
     */
    public static final long __next$offset() {
        return __next$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct __darwin_pthread_handler_rec *__next
     * }
     */
    public static MemorySegment __next(MemorySegment struct) {
        return struct.get(__next$LAYOUT, __next$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct __darwin_pthread_handler_rec *__next
     * }
     */
    public static void __next(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(__next$LAYOUT, __next$OFFSET, fieldValue);
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

