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
 * struct cn_hashtable_slot_t {
 *     uint64_t key_hash;
 *     int item_index;
 *     int base_count;
 * }
 * }
 */
public class cn_hashtable_slot_t {

    cn_hashtable_slot_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_LONG_LONG.withName("key_hash"),
        netcode.C_INT.withName("item_index"),
        netcode.C_INT.withName("base_count")
    ).withName("cn_hashtable_slot_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong key_hash$LAYOUT = (OfLong)$LAYOUT.select(groupElement("key_hash"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t key_hash
     * }
     */
    public static final OfLong key_hash$layout() {
        return key_hash$LAYOUT;
    }

    private static final long key_hash$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t key_hash
     * }
     */
    public static final long key_hash$offset() {
        return key_hash$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t key_hash
     * }
     */
    public static long key_hash(MemorySegment struct) {
        return struct.get(key_hash$LAYOUT, key_hash$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t key_hash
     * }
     */
    public static void key_hash(MemorySegment struct, long fieldValue) {
        struct.set(key_hash$LAYOUT, key_hash$OFFSET, fieldValue);
    }

    private static final OfInt item_index$LAYOUT = (OfInt)$LAYOUT.select(groupElement("item_index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int item_index
     * }
     */
    public static final OfInt item_index$layout() {
        return item_index$LAYOUT;
    }

    private static final long item_index$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int item_index
     * }
     */
    public static final long item_index$offset() {
        return item_index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int item_index
     * }
     */
    public static int item_index(MemorySegment struct) {
        return struct.get(item_index$LAYOUT, item_index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int item_index
     * }
     */
    public static void item_index(MemorySegment struct, int fieldValue) {
        struct.set(item_index$LAYOUT, item_index$OFFSET, fieldValue);
    }

    private static final OfInt base_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("base_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int base_count
     * }
     */
    public static final OfInt base_count$layout() {
        return base_count$LAYOUT;
    }

    private static final long base_count$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int base_count
     * }
     */
    public static final long base_count$offset() {
        return base_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int base_count
     * }
     */
    public static int base_count(MemorySegment struct) {
        return struct.get(base_count$LAYOUT, base_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int base_count
     * }
     */
    public static void base_count(MemorySegment struct, int fieldValue) {
        struct.set(base_count$LAYOUT, base_count$OFFSET, fieldValue);
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

