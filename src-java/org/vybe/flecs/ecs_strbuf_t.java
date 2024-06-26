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
 * struct ecs_strbuf_t {
 *     char *content;
 *     ecs_size_t length;
 *     ecs_size_t size;
 *     ecs_strbuf_list_elem list_stack[32];
 *     int32_t list_sp;
 *     char small_string[512];
 * }
 * }
 */
public class ecs_strbuf_t {

    ecs_strbuf_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("content"),
        flecs.C_INT.withName("length"),
        flecs.C_INT.withName("size"),
        MemoryLayout.sequenceLayout(32, ecs_strbuf_list_elem.layout()).withName("list_stack"),
        flecs.C_INT.withName("list_sp"),
        MemoryLayout.sequenceLayout(512, flecs.C_CHAR).withName("small_string"),
        MemoryLayout.paddingLayout(4)
    ).withName("ecs_strbuf_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout content$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("content"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char *content
     * }
     */
    public static final AddressLayout content$layout() {
        return content$LAYOUT;
    }

    private static final long content$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char *content
     * }
     */
    public static final long content$offset() {
        return content$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char *content
     * }
     */
    public static MemorySegment content(MemorySegment struct) {
        return struct.get(content$LAYOUT, content$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char *content
     * }
     */
    public static void content(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(content$LAYOUT, content$OFFSET, fieldValue);
    }

    private static final OfInt length$LAYOUT = (OfInt)$LAYOUT.select(groupElement("length"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_size_t length
     * }
     */
    public static final OfInt length$layout() {
        return length$LAYOUT;
    }

    private static final long length$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_size_t length
     * }
     */
    public static final long length$offset() {
        return length$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_size_t length
     * }
     */
    public static int length(MemorySegment struct) {
        return struct.get(length$LAYOUT, length$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_size_t length
     * }
     */
    public static void length(MemorySegment struct, int fieldValue) {
        struct.set(length$LAYOUT, length$OFFSET, fieldValue);
    }

    private static final OfInt size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_size_t size
     * }
     */
    public static final OfInt size$layout() {
        return size$LAYOUT;
    }

    private static final long size$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_size_t size
     * }
     */
    public static final long size$offset() {
        return size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_size_t size
     * }
     */
    public static int size(MemorySegment struct) {
        return struct.get(size$LAYOUT, size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_size_t size
     * }
     */
    public static void size(MemorySegment struct, int fieldValue) {
        struct.set(size$LAYOUT, size$OFFSET, fieldValue);
    }

    private static final SequenceLayout list_stack$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("list_stack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static final SequenceLayout list_stack$layout() {
        return list_stack$LAYOUT;
    }

    private static final long list_stack$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static final long list_stack$offset() {
        return list_stack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static MemorySegment list_stack(MemorySegment struct) {
        return struct.asSlice(list_stack$OFFSET, list_stack$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static void list_stack(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, list_stack$OFFSET, list_stack$LAYOUT.byteSize());
    }

    private static long[] list_stack$DIMS = { 32 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static long[] list_stack$dimensions() {
        return list_stack$DIMS;
    }
    private static final MethodHandle list_stack$ELEM_HANDLE = list_stack$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static MemorySegment list_stack(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)list_stack$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ecs_strbuf_list_elem list_stack[32]
     * }
     */
    public static void list_stack(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, list_stack(struct, index0), 0L, ecs_strbuf_list_elem.layout().byteSize());
    }

    private static final OfInt list_sp$LAYOUT = (OfInt)$LAYOUT.select(groupElement("list_sp"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t list_sp
     * }
     */
    public static final OfInt list_sp$layout() {
        return list_sp$LAYOUT;
    }

    private static final long list_sp$OFFSET = 528;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t list_sp
     * }
     */
    public static final long list_sp$offset() {
        return list_sp$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t list_sp
     * }
     */
    public static int list_sp(MemorySegment struct) {
        return struct.get(list_sp$LAYOUT, list_sp$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t list_sp
     * }
     */
    public static void list_sp(MemorySegment struct, int fieldValue) {
        struct.set(list_sp$LAYOUT, list_sp$OFFSET, fieldValue);
    }

    private static final SequenceLayout small_string$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("small_string"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static final SequenceLayout small_string$layout() {
        return small_string$LAYOUT;
    }

    private static final long small_string$OFFSET = 532;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static final long small_string$offset() {
        return small_string$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static MemorySegment small_string(MemorySegment struct) {
        return struct.asSlice(small_string$OFFSET, small_string$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static void small_string(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, small_string$OFFSET, small_string$LAYOUT.byteSize());
    }

    private static long[] small_string$DIMS = { 512 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static long[] small_string$dimensions() {
        return small_string$DIMS;
    }
    private static final VarHandle small_string$ELEM_HANDLE = small_string$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static byte small_string(MemorySegment struct, long index0) {
        return (byte)small_string$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * char small_string[512]
     * }
     */
    public static void small_string(MemorySegment struct, long index0, byte fieldValue) {
        small_string$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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

