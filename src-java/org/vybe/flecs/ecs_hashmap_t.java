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
 *     ecs_hash_value_action_t hash;
 *     ecs_compare_action_t compare;
 *     ecs_size_t key_size;
 *     ecs_size_t value_size;
 *     ecs_block_allocator_t *hashmap_allocator;
 *     ecs_block_allocator_t bucket_allocator;
 *     ecs_map_t impl;
 * }
 * }
 */
public class ecs_hashmap_t {

    ecs_hashmap_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("hash"),
        flecs.C_POINTER.withName("compare"),
        flecs.C_INT.withName("key_size"),
        flecs.C_INT.withName("value_size"),
        flecs.C_POINTER.withName("hashmap_allocator"),
        ecs_block_allocator_t.layout().withName("bucket_allocator"),
        ecs_map_t.layout().withName("impl")
    ).withName("$anon$3983:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout hash$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("hash"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_hash_value_action_t hash
     * }
     */
    public static final AddressLayout hash$layout() {
        return hash$LAYOUT;
    }

    private static final long hash$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_hash_value_action_t hash
     * }
     */
    public static final long hash$offset() {
        return hash$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_hash_value_action_t hash
     * }
     */
    public static MemorySegment hash(MemorySegment struct) {
        return struct.get(hash$LAYOUT, hash$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_hash_value_action_t hash
     * }
     */
    public static void hash(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(hash$LAYOUT, hash$OFFSET, fieldValue);
    }

    private static final AddressLayout compare$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("compare"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_compare_action_t compare
     * }
     */
    public static final AddressLayout compare$layout() {
        return compare$LAYOUT;
    }

    private static final long compare$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_compare_action_t compare
     * }
     */
    public static final long compare$offset() {
        return compare$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_compare_action_t compare
     * }
     */
    public static MemorySegment compare(MemorySegment struct) {
        return struct.get(compare$LAYOUT, compare$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_compare_action_t compare
     * }
     */
    public static void compare(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(compare$LAYOUT, compare$OFFSET, fieldValue);
    }

    private static final OfInt key_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("key_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_size_t key_size
     * }
     */
    public static final OfInt key_size$layout() {
        return key_size$LAYOUT;
    }

    private static final long key_size$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_size_t key_size
     * }
     */
    public static final long key_size$offset() {
        return key_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_size_t key_size
     * }
     */
    public static int key_size(MemorySegment struct) {
        return struct.get(key_size$LAYOUT, key_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_size_t key_size
     * }
     */
    public static void key_size(MemorySegment struct, int fieldValue) {
        struct.set(key_size$LAYOUT, key_size$OFFSET, fieldValue);
    }

    private static final OfInt value_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("value_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_size_t value_size
     * }
     */
    public static final OfInt value_size$layout() {
        return value_size$LAYOUT;
    }

    private static final long value_size$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_size_t value_size
     * }
     */
    public static final long value_size$offset() {
        return value_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_size_t value_size
     * }
     */
    public static int value_size(MemorySegment struct) {
        return struct.get(value_size$LAYOUT, value_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_size_t value_size
     * }
     */
    public static void value_size(MemorySegment struct, int fieldValue) {
        struct.set(value_size$LAYOUT, value_size$OFFSET, fieldValue);
    }

    private static final AddressLayout hashmap_allocator$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("hashmap_allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t *hashmap_allocator
     * }
     */
    public static final AddressLayout hashmap_allocator$layout() {
        return hashmap_allocator$LAYOUT;
    }

    private static final long hashmap_allocator$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t *hashmap_allocator
     * }
     */
    public static final long hashmap_allocator$offset() {
        return hashmap_allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t *hashmap_allocator
     * }
     */
    public static MemorySegment hashmap_allocator(MemorySegment struct) {
        return struct.get(hashmap_allocator$LAYOUT, hashmap_allocator$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t *hashmap_allocator
     * }
     */
    public static void hashmap_allocator(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(hashmap_allocator$LAYOUT, hashmap_allocator$OFFSET, fieldValue);
    }

    private static final GroupLayout bucket_allocator$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("bucket_allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t bucket_allocator
     * }
     */
    public static final GroupLayout bucket_allocator$layout() {
        return bucket_allocator$LAYOUT;
    }

    private static final long bucket_allocator$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t bucket_allocator
     * }
     */
    public static final long bucket_allocator$offset() {
        return bucket_allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t bucket_allocator
     * }
     */
    public static MemorySegment bucket_allocator(MemorySegment struct) {
        return struct.asSlice(bucket_allocator$OFFSET, bucket_allocator$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t bucket_allocator
     * }
     */
    public static void bucket_allocator(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, bucket_allocator$OFFSET, bucket_allocator$LAYOUT.byteSize());
    }

    private static final GroupLayout impl$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("impl"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_map_t impl
     * }
     */
    public static final GroupLayout impl$layout() {
        return impl$LAYOUT;
    }

    private static final long impl$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_map_t impl
     * }
     */
    public static final long impl$offset() {
        return impl$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_map_t impl
     * }
     */
    public static MemorySegment impl(MemorySegment struct) {
        return struct.asSlice(impl$OFFSET, impl$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_map_t impl
     * }
     */
    public static void impl(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, impl$OFFSET, impl$LAYOUT.byteSize());
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

