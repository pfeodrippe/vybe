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
 * struct {
 *     void *key;
 *     void *value;
 *     uint64_t hash;
 * }
 * }
 */
public class flecs_hashmap_result_t {

    flecs_hashmap_result_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("key"),
        flecs.C_POINTER.withName("value"),
        flecs.C_LONG_LONG.withName("hash")
    ).withName("$anon$4009:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout key$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("key"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *key
     * }
     */
    public static final AddressLayout key$layout() {
        return key$LAYOUT;
    }

    private static final long key$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *key
     * }
     */
    public static final long key$offset() {
        return key$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *key
     * }
     */
    public static MemorySegment key(MemorySegment struct) {
        return struct.get(key$LAYOUT, key$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *key
     * }
     */
    public static void key(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(key$LAYOUT, key$OFFSET, fieldValue);
    }

    private static final AddressLayout value$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("value"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *value
     * }
     */
    public static final AddressLayout value$layout() {
        return value$LAYOUT;
    }

    private static final long value$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *value
     * }
     */
    public static final long value$offset() {
        return value$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *value
     * }
     */
    public static MemorySegment value(MemorySegment struct) {
        return struct.get(value$LAYOUT, value$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *value
     * }
     */
    public static void value(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(value$LAYOUT, value$OFFSET, fieldValue);
    }

    private static final OfLong hash$LAYOUT = (OfLong)$LAYOUT.select(groupElement("hash"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t hash
     * }
     */
    public static final OfLong hash$layout() {
        return hash$LAYOUT;
    }

    private static final long hash$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t hash
     * }
     */
    public static final long hash$offset() {
        return hash$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t hash
     * }
     */
    public static long hash(MemorySegment struct) {
        return struct.get(hash$LAYOUT, hash$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t hash
     * }
     */
    public static void hash(MemorySegment struct, long fieldValue) {
        struct.set(hash$LAYOUT, hash$OFFSET, fieldValue);
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

