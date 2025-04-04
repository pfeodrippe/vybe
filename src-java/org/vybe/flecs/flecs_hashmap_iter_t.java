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
 *     ecs_map_iter_t it;
 *     ecs_hm_bucket_t *bucket;
 *     int32_t index;
 * }
 * }
 */
public class flecs_hashmap_iter_t {

    flecs_hashmap_iter_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ecs_map_iter_t.layout().withName("it"),
        flecs.C_POINTER.withName("bucket"),
        flecs.C_INT.withName("index"),
        MemoryLayout.paddingLayout(4)
    ).withName("$anon$4084:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout it$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("it"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_map_iter_t it
     * }
     */
    public static final GroupLayout it$layout() {
        return it$LAYOUT;
    }

    private static final long it$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_map_iter_t it
     * }
     */
    public static final long it$offset() {
        return it$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_map_iter_t it
     * }
     */
    public static MemorySegment it(MemorySegment struct) {
        return struct.asSlice(it$OFFSET, it$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_map_iter_t it
     * }
     */
    public static void it(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, it$OFFSET, it$LAYOUT.byteSize());
    }

    private static final AddressLayout bucket$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("bucket"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_hm_bucket_t *bucket
     * }
     */
    public static final AddressLayout bucket$layout() {
        return bucket$LAYOUT;
    }

    private static final long bucket$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_hm_bucket_t *bucket
     * }
     */
    public static final long bucket$offset() {
        return bucket$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_hm_bucket_t *bucket
     * }
     */
    public static MemorySegment bucket(MemorySegment struct) {
        return struct.get(bucket$LAYOUT, bucket$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_hm_bucket_t *bucket
     * }
     */
    public static void bucket(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(bucket$LAYOUT, bucket$OFFSET, fieldValue);
    }

    private static final OfInt index$LAYOUT = (OfInt)$LAYOUT.select(groupElement("index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t index
     * }
     */
    public static final OfInt index$layout() {
        return index$LAYOUT;
    }

    private static final long index$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t index
     * }
     */
    public static final long index$offset() {
        return index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t index
     * }
     */
    public static int index(MemorySegment struct) {
        return struct.get(index$LAYOUT, index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t index
     * }
     */
    public static void index(MemorySegment struct, int fieldValue) {
        struct.set(index$LAYOUT, index$OFFSET, fieldValue);
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

