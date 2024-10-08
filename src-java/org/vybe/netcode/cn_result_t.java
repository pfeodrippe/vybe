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
 * struct cn_result_t {
 *     int code;
 *     const char *details;
 * }
 * }
 */
public class cn_result_t {

    cn_result_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_INT.withName("code"),
        MemoryLayout.paddingLayout(4),
        netcode.C_POINTER.withName("details")
    ).withName("cn_result_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt code$LAYOUT = (OfInt)$LAYOUT.select(groupElement("code"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static final OfInt code$layout() {
        return code$LAYOUT;
    }

    private static final long code$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static final long code$offset() {
        return code$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static int code(MemorySegment struct) {
        return struct.get(code$LAYOUT, code$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static void code(MemorySegment struct, int fieldValue) {
        struct.set(code$LAYOUT, code$OFFSET, fieldValue);
    }

    private static final AddressLayout details$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("details"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *details
     * }
     */
    public static final AddressLayout details$layout() {
        return details$LAYOUT;
    }

    private static final long details$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *details
     * }
     */
    public static final long details$offset() {
        return details$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *details
     * }
     */
    public static MemorySegment details(MemorySegment struct) {
        return struct.get(details$LAYOUT, details$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *details
     * }
     */
    public static void details(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(details$LAYOUT, details$OFFSET, fieldValue);
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

