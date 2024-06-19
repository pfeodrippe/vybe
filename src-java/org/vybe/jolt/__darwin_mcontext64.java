// Generated by jextract

package org.vybe.jolt;

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
 * struct __darwin_mcontext64 {
 *     struct __darwin_arm_exception_state64 __es;
 *     struct __darwin_arm_thread_state64 __ss;
 *     struct __darwin_arm_neon_state64 __ns;
 * }
 * }
 */
public class __darwin_mcontext64 {

    __darwin_mcontext64() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        __darwin_arm_exception_state64.layout().withName("__es"),
        __darwin_arm_thread_state64.layout().withName("__ss"),
        __darwin_arm_neon_state64.layout().withName("__ns")
    ).withName("__darwin_mcontext64");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout __es$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("__es"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct __darwin_arm_exception_state64 __es
     * }
     */
    public static final GroupLayout __es$layout() {
        return __es$LAYOUT;
    }

    private static final long __es$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct __darwin_arm_exception_state64 __es
     * }
     */
    public static final long __es$offset() {
        return __es$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct __darwin_arm_exception_state64 __es
     * }
     */
    public static MemorySegment __es(MemorySegment struct) {
        return struct.asSlice(__es$OFFSET, __es$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct __darwin_arm_exception_state64 __es
     * }
     */
    public static void __es(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, __es$OFFSET, __es$LAYOUT.byteSize());
    }

    private static final GroupLayout __ss$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("__ss"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct __darwin_arm_thread_state64 __ss
     * }
     */
    public static final GroupLayout __ss$layout() {
        return __ss$LAYOUT;
    }

    private static final long __ss$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct __darwin_arm_thread_state64 __ss
     * }
     */
    public static final long __ss$offset() {
        return __ss$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct __darwin_arm_thread_state64 __ss
     * }
     */
    public static MemorySegment __ss(MemorySegment struct) {
        return struct.asSlice(__ss$OFFSET, __ss$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct __darwin_arm_thread_state64 __ss
     * }
     */
    public static void __ss(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, __ss$OFFSET, __ss$LAYOUT.byteSize());
    }

    private static final GroupLayout __ns$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("__ns"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct __darwin_arm_neon_state64 __ns
     * }
     */
    public static final GroupLayout __ns$layout() {
        return __ns$LAYOUT;
    }

    private static final long __ns$OFFSET = 288;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct __darwin_arm_neon_state64 __ns
     * }
     */
    public static final long __ns$offset() {
        return __ns$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct __darwin_arm_neon_state64 __ns
     * }
     */
    public static MemorySegment __ns(MemorySegment struct) {
        return struct.asSlice(__ns$OFFSET, __ns$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct __darwin_arm_neon_state64 __ns
     * }
     */
    public static void __ns(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, __ns$OFFSET, __ns$LAYOUT.byteSize());
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
