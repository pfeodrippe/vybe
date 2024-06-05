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
 * struct ecs_stack_t {
 *     ecs_stack_page_t first;
 *     ecs_stack_page_t *tail_page;
 *     ecs_stack_cursor_t *tail_cursor;
 *     int32_t cursor_count;
 * }
 * }
 */
public class ecs_stack_t {

    ecs_stack_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ecs_stack_page_t.layout().withName("first"),
        flecs.C_POINTER.withName("tail_page"),
        flecs.C_POINTER.withName("tail_cursor"),
        flecs.C_INT.withName("cursor_count"),
        MemoryLayout.paddingLayout(4)
    ).withName("ecs_stack_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout first$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("first"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_stack_page_t first
     * }
     */
    public static final GroupLayout first$layout() {
        return first$LAYOUT;
    }

    private static final long first$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_stack_page_t first
     * }
     */
    public static final long first$offset() {
        return first$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_stack_page_t first
     * }
     */
    public static MemorySegment first(MemorySegment struct) {
        return struct.asSlice(first$OFFSET, first$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_stack_page_t first
     * }
     */
    public static void first(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, first$OFFSET, first$LAYOUT.byteSize());
    }

    private static final AddressLayout tail_page$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("tail_page"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_stack_page_t *tail_page
     * }
     */
    public static final AddressLayout tail_page$layout() {
        return tail_page$LAYOUT;
    }

    private static final long tail_page$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_stack_page_t *tail_page
     * }
     */
    public static final long tail_page$offset() {
        return tail_page$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_stack_page_t *tail_page
     * }
     */
    public static MemorySegment tail_page(MemorySegment struct) {
        return struct.get(tail_page$LAYOUT, tail_page$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_stack_page_t *tail_page
     * }
     */
    public static void tail_page(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(tail_page$LAYOUT, tail_page$OFFSET, fieldValue);
    }

    private static final AddressLayout tail_cursor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("tail_cursor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_stack_cursor_t *tail_cursor
     * }
     */
    public static final AddressLayout tail_cursor$layout() {
        return tail_cursor$LAYOUT;
    }

    private static final long tail_cursor$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_stack_cursor_t *tail_cursor
     * }
     */
    public static final long tail_cursor$offset() {
        return tail_cursor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_stack_cursor_t *tail_cursor
     * }
     */
    public static MemorySegment tail_cursor(MemorySegment struct) {
        return struct.get(tail_cursor$LAYOUT, tail_cursor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_stack_cursor_t *tail_cursor
     * }
     */
    public static void tail_cursor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(tail_cursor$LAYOUT, tail_cursor$OFFSET, fieldValue);
    }

    private static final OfInt cursor_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("cursor_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t cursor_count
     * }
     */
    public static final OfInt cursor_count$layout() {
        return cursor_count$LAYOUT;
    }

    private static final long cursor_count$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t cursor_count
     * }
     */
    public static final long cursor_count$offset() {
        return cursor_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t cursor_count
     * }
     */
    public static int cursor_count(MemorySegment struct) {
        return struct.get(cursor_count$LAYOUT, cursor_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t cursor_count
     * }
     */
    public static void cursor_count(MemorySegment struct, int fieldValue) {
        struct.set(cursor_count$LAYOUT, cursor_count$OFFSET, fieldValue);
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

