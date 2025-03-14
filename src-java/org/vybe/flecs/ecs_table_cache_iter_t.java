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
 * struct ecs_table_cache_iter_t {
 *     struct ecs_table_cache_hdr_t *cur;
 *     struct ecs_table_cache_hdr_t *next;
 *     bool iter_fill;
 *     bool iter_empty;
 * }
 * }
 */
public class ecs_table_cache_iter_t {

    ecs_table_cache_iter_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("cur"),
        flecs.C_POINTER.withName("next"),
        flecs.C_BOOL.withName("iter_fill"),
        flecs.C_BOOL.withName("iter_empty"),
        MemoryLayout.paddingLayout(6)
    ).withName("ecs_table_cache_iter_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout cur$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("cur"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *cur
     * }
     */
    public static final AddressLayout cur$layout() {
        return cur$LAYOUT;
    }

    private static final long cur$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *cur
     * }
     */
    public static final long cur$offset() {
        return cur$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *cur
     * }
     */
    public static MemorySegment cur(MemorySegment struct) {
        return struct.get(cur$LAYOUT, cur$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_table_cache_hdr_t *cur
     * }
     */
    public static void cur(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(cur$LAYOUT, cur$OFFSET, fieldValue);
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

    private static final long next$OFFSET = 8;

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

    private static final OfBoolean iter_fill$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("iter_fill"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool iter_fill
     * }
     */
    public static final OfBoolean iter_fill$layout() {
        return iter_fill$LAYOUT;
    }

    private static final long iter_fill$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool iter_fill
     * }
     */
    public static final long iter_fill$offset() {
        return iter_fill$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool iter_fill
     * }
     */
    public static boolean iter_fill(MemorySegment struct) {
        return struct.get(iter_fill$LAYOUT, iter_fill$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool iter_fill
     * }
     */
    public static void iter_fill(MemorySegment struct, boolean fieldValue) {
        struct.set(iter_fill$LAYOUT, iter_fill$OFFSET, fieldValue);
    }

    private static final OfBoolean iter_empty$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("iter_empty"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool iter_empty
     * }
     */
    public static final OfBoolean iter_empty$layout() {
        return iter_empty$LAYOUT;
    }

    private static final long iter_empty$OFFSET = 17;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool iter_empty
     * }
     */
    public static final long iter_empty$offset() {
        return iter_empty$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool iter_empty
     * }
     */
    public static boolean iter_empty(MemorySegment struct) {
        return struct.get(iter_empty$LAYOUT, iter_empty$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool iter_empty
     * }
     */
    public static void iter_empty(MemorySegment struct, boolean fieldValue) {
        struct.set(iter_empty$LAYOUT, iter_empty$OFFSET, fieldValue);
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

