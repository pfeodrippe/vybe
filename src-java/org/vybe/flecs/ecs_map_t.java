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
 * struct ecs_map_t {
 *     uint8_t bucket_shift;
 *     bool shared_allocator;
 *     ecs_bucket_t *buckets;
 *     int32_t bucket_count;
 *     int32_t count;
 *     struct ecs_block_allocator_t *entry_allocator;
 *     struct ecs_allocator_t *allocator;
 * }
 * }
 */
public class ecs_map_t {

    ecs_map_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_CHAR.withName("bucket_shift"),
        flecs.C_BOOL.withName("shared_allocator"),
        MemoryLayout.paddingLayout(6),
        flecs.C_POINTER.withName("buckets"),
        flecs.C_INT.withName("bucket_count"),
        flecs.C_INT.withName("count"),
        flecs.C_POINTER.withName("entry_allocator"),
        flecs.C_POINTER.withName("allocator")
    ).withName("ecs_map_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfByte bucket_shift$LAYOUT = (OfByte)$LAYOUT.select(groupElement("bucket_shift"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t bucket_shift
     * }
     */
    public static final OfByte bucket_shift$layout() {
        return bucket_shift$LAYOUT;
    }

    private static final long bucket_shift$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t bucket_shift
     * }
     */
    public static final long bucket_shift$offset() {
        return bucket_shift$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t bucket_shift
     * }
     */
    public static byte bucket_shift(MemorySegment struct) {
        return struct.get(bucket_shift$LAYOUT, bucket_shift$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t bucket_shift
     * }
     */
    public static void bucket_shift(MemorySegment struct, byte fieldValue) {
        struct.set(bucket_shift$LAYOUT, bucket_shift$OFFSET, fieldValue);
    }

    private static final OfBoolean shared_allocator$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("shared_allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool shared_allocator
     * }
     */
    public static final OfBoolean shared_allocator$layout() {
        return shared_allocator$LAYOUT;
    }

    private static final long shared_allocator$OFFSET = 1;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool shared_allocator
     * }
     */
    public static final long shared_allocator$offset() {
        return shared_allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool shared_allocator
     * }
     */
    public static boolean shared_allocator(MemorySegment struct) {
        return struct.get(shared_allocator$LAYOUT, shared_allocator$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool shared_allocator
     * }
     */
    public static void shared_allocator(MemorySegment struct, boolean fieldValue) {
        struct.set(shared_allocator$LAYOUT, shared_allocator$OFFSET, fieldValue);
    }

    private static final AddressLayout buckets$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("buckets"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_bucket_t *buckets
     * }
     */
    public static final AddressLayout buckets$layout() {
        return buckets$LAYOUT;
    }

    private static final long buckets$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_bucket_t *buckets
     * }
     */
    public static final long buckets$offset() {
        return buckets$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_bucket_t *buckets
     * }
     */
    public static MemorySegment buckets(MemorySegment struct) {
        return struct.get(buckets$LAYOUT, buckets$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_bucket_t *buckets
     * }
     */
    public static void buckets(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(buckets$LAYOUT, buckets$OFFSET, fieldValue);
    }

    private static final OfInt bucket_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("bucket_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t bucket_count
     * }
     */
    public static final OfInt bucket_count$layout() {
        return bucket_count$LAYOUT;
    }

    private static final long bucket_count$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t bucket_count
     * }
     */
    public static final long bucket_count$offset() {
        return bucket_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t bucket_count
     * }
     */
    public static int bucket_count(MemorySegment struct) {
        return struct.get(bucket_count$LAYOUT, bucket_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t bucket_count
     * }
     */
    public static void bucket_count(MemorySegment struct, int fieldValue) {
        struct.set(bucket_count$LAYOUT, bucket_count$OFFSET, fieldValue);
    }

    private static final OfInt count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static final OfInt count$layout() {
        return count$LAYOUT;
    }

    private static final long count$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static final long count$offset() {
        return count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static int count(MemorySegment struct) {
        return struct.get(count$LAYOUT, count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t count
     * }
     */
    public static void count(MemorySegment struct, int fieldValue) {
        struct.set(count$LAYOUT, count$OFFSET, fieldValue);
    }

    private static final AddressLayout entry_allocator$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("entry_allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t *entry_allocator
     * }
     */
    public static final AddressLayout entry_allocator$layout() {
        return entry_allocator$LAYOUT;
    }

    private static final long entry_allocator$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t *entry_allocator
     * }
     */
    public static final long entry_allocator$offset() {
        return entry_allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t *entry_allocator
     * }
     */
    public static MemorySegment entry_allocator(MemorySegment struct) {
        return struct.get(entry_allocator$LAYOUT, entry_allocator$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t *entry_allocator
     * }
     */
    public static void entry_allocator(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(entry_allocator$LAYOUT, entry_allocator$OFFSET, fieldValue);
    }

    private static final AddressLayout allocator$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static final AddressLayout allocator$layout() {
        return allocator$LAYOUT;
    }

    private static final long allocator$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static final long allocator$offset() {
        return allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static MemorySegment allocator(MemorySegment struct) {
        return struct.get(allocator$LAYOUT, allocator$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static void allocator(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(allocator$LAYOUT, allocator$OFFSET, fieldValue);
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

