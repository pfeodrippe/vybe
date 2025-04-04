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
 * struct ecs_table_cache_hdr_t {
 *     struct ecs_table_cache_t *cache;
 *     ecs_table_t *table;
 *     struct ecs_table_cache_hdr_t *prev;
 *     struct ecs_table_cache_hdr_t *next;
 * }
 * }
 */
public class ecs_table_cache_hdr_t {

    ecs_table_cache_hdr_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("cache"),
        flecs.C_POINTER.withName("table"),
        flecs.C_POINTER.withName("prev"),
        flecs.C_POINTER.withName("next")
    ).withName("ecs_table_cache_hdr_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout cache$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("cache"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_t *cache
     * }
     */
    public static final AddressLayout cache$layout() {
        return cache$LAYOUT;
    }

    private static final long cache$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_t *cache
     * }
     */
    public static final long cache$offset() {
        return cache$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_t *cache
     * }
     */
    public static MemorySegment cache(MemorySegment struct) {
        return struct.get(cache$LAYOUT, cache$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_t *cache
     * }
     */
    public static void cache(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(cache$LAYOUT, cache$OFFSET, fieldValue);
    }

    private static final AddressLayout table$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("table"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_table_t *table
     * }
     */
    public static final AddressLayout table$layout() {
        return table$LAYOUT;
    }

    private static final long table$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_table_t *table
     * }
     */
    public static final long table$offset() {
        return table$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_table_t *table
     * }
     */
    public static MemorySegment table(MemorySegment struct) {
        return struct.get(table$LAYOUT, table$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_table_t *table
     * }
     */
    public static void table(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(table$LAYOUT, table$OFFSET, fieldValue);
    }

    private static final AddressLayout prev$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("prev"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *prev
     * }
     */
    public static final AddressLayout prev$layout() {
        return prev$LAYOUT;
    }

    private static final long prev$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *prev
     * }
     */
    public static final long prev$offset() {
        return prev$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *prev
     * }
     */
    public static MemorySegment prev(MemorySegment struct) {
        return struct.get(prev$LAYOUT, prev$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *prev
     * }
     */
    public static void prev(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(prev$LAYOUT, prev$OFFSET, fieldValue);
    }

    private static final AddressLayout next$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("next"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *next
     * }
     */
    public static final AddressLayout next$layout() {
        return next$LAYOUT;
    }

    private static final long next$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *next
     * }
     */
    public static final long next$offset() {
        return next$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *next
     * }
     */
    public static MemorySegment next(MemorySegment struct) {
        return struct.get(next$LAYOUT, next$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *next
     * }
     */
    public static void next(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(next$LAYOUT, next$OFFSET, fieldValue);
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

