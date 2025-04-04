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
 * struct ecs_block_allocator_t {
 *     ecs_block_allocator_chunk_header_t *head;
 *     ecs_block_allocator_block_t *block_head;
 *     ecs_block_allocator_block_t *block_tail;
 *     int32_t chunk_size;
 *     int32_t data_size;
 *     int32_t chunks_per_block;
 *     int32_t block_size;
 * }
 * }
 */
public class ecs_block_allocator_t {

    ecs_block_allocator_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("head"),
        flecs.C_POINTER.withName("block_head"),
        flecs.C_POINTER.withName("block_tail"),
        flecs.C_INT.withName("chunk_size"),
        flecs.C_INT.withName("data_size"),
        flecs.C_INT.withName("chunks_per_block"),
        flecs.C_INT.withName("block_size")
    ).withName("ecs_block_allocator_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout head$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("head"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_block_allocator_chunk_header_t *head
     * }
     */
    public static final AddressLayout head$layout() {
        return head$LAYOUT;
    }

    private static final long head$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_block_allocator_chunk_header_t *head
     * }
     */
    public static final long head$offset() {
        return head$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_chunk_header_t *head
     * }
     */
    public static MemorySegment head(MemorySegment struct) {
        return struct.get(head$LAYOUT, head$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_chunk_header_t *head
     * }
     */
    public static void head(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(head$LAYOUT, head$OFFSET, fieldValue);
    }

    private static final AddressLayout block_head$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("block_head"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_head
     * }
     */
    public static final AddressLayout block_head$layout() {
        return block_head$LAYOUT;
    }

    private static final long block_head$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_head
     * }
     */
    public static final long block_head$offset() {
        return block_head$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_head
     * }
     */
    public static MemorySegment block_head(MemorySegment struct) {
        return struct.get(block_head$LAYOUT, block_head$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_head
     * }
     */
    public static void block_head(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(block_head$LAYOUT, block_head$OFFSET, fieldValue);
    }

    private static final AddressLayout block_tail$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("block_tail"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_tail
     * }
     */
    public static final AddressLayout block_tail$layout() {
        return block_tail$LAYOUT;
    }

    private static final long block_tail$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_tail
     * }
     */
    public static final long block_tail$offset() {
        return block_tail$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_tail
     * }
     */
    public static MemorySegment block_tail(MemorySegment struct) {
        return struct.get(block_tail$LAYOUT, block_tail$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_block_t *block_tail
     * }
     */
    public static void block_tail(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(block_tail$LAYOUT, block_tail$OFFSET, fieldValue);
    }

    private static final OfInt chunk_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("chunk_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t chunk_size
     * }
     */
    public static final OfInt chunk_size$layout() {
        return chunk_size$LAYOUT;
    }

    private static final long chunk_size$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t chunk_size
     * }
     */
    public static final long chunk_size$offset() {
        return chunk_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t chunk_size
     * }
     */
    public static int chunk_size(MemorySegment struct) {
        return struct.get(chunk_size$LAYOUT, chunk_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t chunk_size
     * }
     */
    public static void chunk_size(MemorySegment struct, int fieldValue) {
        struct.set(chunk_size$LAYOUT, chunk_size$OFFSET, fieldValue);
    }

    private static final OfInt data_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("data_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t data_size
     * }
     */
    public static final OfInt data_size$layout() {
        return data_size$LAYOUT;
    }

    private static final long data_size$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t data_size
     * }
     */
    public static final long data_size$offset() {
        return data_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t data_size
     * }
     */
    public static int data_size(MemorySegment struct) {
        return struct.get(data_size$LAYOUT, data_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t data_size
     * }
     */
    public static void data_size(MemorySegment struct, int fieldValue) {
        struct.set(data_size$LAYOUT, data_size$OFFSET, fieldValue);
    }

    private static final OfInt chunks_per_block$LAYOUT = (OfInt)$LAYOUT.select(groupElement("chunks_per_block"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t chunks_per_block
     * }
     */
    public static final OfInt chunks_per_block$layout() {
        return chunks_per_block$LAYOUT;
    }

    private static final long chunks_per_block$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t chunks_per_block
     * }
     */
    public static final long chunks_per_block$offset() {
        return chunks_per_block$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t chunks_per_block
     * }
     */
    public static int chunks_per_block(MemorySegment struct) {
        return struct.get(chunks_per_block$LAYOUT, chunks_per_block$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t chunks_per_block
     * }
     */
    public static void chunks_per_block(MemorySegment struct, int fieldValue) {
        struct.set(chunks_per_block$LAYOUT, chunks_per_block$OFFSET, fieldValue);
    }

    private static final OfInt block_size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("block_size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t block_size
     * }
     */
    public static final OfInt block_size$layout() {
        return block_size$LAYOUT;
    }

    private static final long block_size$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t block_size
     * }
     */
    public static final long block_size$offset() {
        return block_size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t block_size
     * }
     */
    public static int block_size(MemorySegment struct) {
        return struct.get(block_size$LAYOUT, block_size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t block_size
     * }
     */
    public static void block_size(MemorySegment struct, int fieldValue) {
        struct.set(block_size$LAYOUT, block_size$OFFSET, fieldValue);
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

