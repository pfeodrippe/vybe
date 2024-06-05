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
 * struct ecs_term_t {
 *     ecs_id_t id;
 *     ecs_term_ref_t src;
 *     ecs_term_ref_t first;
 *     ecs_term_ref_t second;
 *     ecs_entity_t trav;
 *     int16_t inout;
 *     int16_t oper;
 *     int16_t field_index;
 *     ecs_flags16_t flags_;
 * }
 * }
 */
public class ecs_term_t {

    ecs_term_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_LONG_LONG.withName("id"),
        ecs_term_ref_t.layout().withName("src"),
        ecs_term_ref_t.layout().withName("first"),
        ecs_term_ref_t.layout().withName("second"),
        flecs.C_LONG_LONG.withName("trav"),
        flecs.C_SHORT.withName("inout"),
        flecs.C_SHORT.withName("oper"),
        flecs.C_SHORT.withName("field_index"),
        flecs.C_SHORT.withName("flags_")
    ).withName("ecs_term_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong id$LAYOUT = (OfLong)$LAYOUT.select(groupElement("id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_id_t id
     * }
     */
    public static final OfLong id$layout() {
        return id$LAYOUT;
    }

    private static final long id$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_id_t id
     * }
     */
    public static final long id$offset() {
        return id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_id_t id
     * }
     */
    public static long id(MemorySegment struct) {
        return struct.get(id$LAYOUT, id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_id_t id
     * }
     */
    public static void id(MemorySegment struct, long fieldValue) {
        struct.set(id$LAYOUT, id$OFFSET, fieldValue);
    }

    private static final GroupLayout src$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("src"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_term_ref_t src
     * }
     */
    public static final GroupLayout src$layout() {
        return src$LAYOUT;
    }

    private static final long src$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_term_ref_t src
     * }
     */
    public static final long src$offset() {
        return src$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_term_ref_t src
     * }
     */
    public static MemorySegment src(MemorySegment struct) {
        return struct.asSlice(src$OFFSET, src$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_term_ref_t src
     * }
     */
    public static void src(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, src$OFFSET, src$LAYOUT.byteSize());
    }

    private static final GroupLayout first$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("first"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_term_ref_t first
     * }
     */
    public static final GroupLayout first$layout() {
        return first$LAYOUT;
    }

    private static final long first$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_term_ref_t first
     * }
     */
    public static final long first$offset() {
        return first$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_term_ref_t first
     * }
     */
    public static MemorySegment first(MemorySegment struct) {
        return struct.asSlice(first$OFFSET, first$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_term_ref_t first
     * }
     */
    public static void first(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, first$OFFSET, first$LAYOUT.byteSize());
    }

    private static final GroupLayout second$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("second"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_term_ref_t second
     * }
     */
    public static final GroupLayout second$layout() {
        return second$LAYOUT;
    }

    private static final long second$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_term_ref_t second
     * }
     */
    public static final long second$offset() {
        return second$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_term_ref_t second
     * }
     */
    public static MemorySegment second(MemorySegment struct) {
        return struct.asSlice(second$OFFSET, second$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_term_ref_t second
     * }
     */
    public static void second(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, second$OFFSET, second$LAYOUT.byteSize());
    }

    private static final OfLong trav$LAYOUT = (OfLong)$LAYOUT.select(groupElement("trav"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t trav
     * }
     */
    public static final OfLong trav$layout() {
        return trav$LAYOUT;
    }

    private static final long trav$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t trav
     * }
     */
    public static final long trav$offset() {
        return trav$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t trav
     * }
     */
    public static long trav(MemorySegment struct) {
        return struct.get(trav$LAYOUT, trav$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t trav
     * }
     */
    public static void trav(MemorySegment struct, long fieldValue) {
        struct.set(trav$LAYOUT, trav$OFFSET, fieldValue);
    }

    private static final OfShort inout$LAYOUT = (OfShort)$LAYOUT.select(groupElement("inout"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int16_t inout
     * }
     */
    public static final OfShort inout$layout() {
        return inout$LAYOUT;
    }

    private static final long inout$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int16_t inout
     * }
     */
    public static final long inout$offset() {
        return inout$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int16_t inout
     * }
     */
    public static short inout(MemorySegment struct) {
        return struct.get(inout$LAYOUT, inout$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int16_t inout
     * }
     */
    public static void inout(MemorySegment struct, short fieldValue) {
        struct.set(inout$LAYOUT, inout$OFFSET, fieldValue);
    }

    private static final OfShort oper$LAYOUT = (OfShort)$LAYOUT.select(groupElement("oper"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int16_t oper
     * }
     */
    public static final OfShort oper$layout() {
        return oper$LAYOUT;
    }

    private static final long oper$OFFSET = 66;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int16_t oper
     * }
     */
    public static final long oper$offset() {
        return oper$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int16_t oper
     * }
     */
    public static short oper(MemorySegment struct) {
        return struct.get(oper$LAYOUT, oper$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int16_t oper
     * }
     */
    public static void oper(MemorySegment struct, short fieldValue) {
        struct.set(oper$LAYOUT, oper$OFFSET, fieldValue);
    }

    private static final OfShort field_index$LAYOUT = (OfShort)$LAYOUT.select(groupElement("field_index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int16_t field_index
     * }
     */
    public static final OfShort field_index$layout() {
        return field_index$LAYOUT;
    }

    private static final long field_index$OFFSET = 68;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int16_t field_index
     * }
     */
    public static final long field_index$offset() {
        return field_index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int16_t field_index
     * }
     */
    public static short field_index(MemorySegment struct) {
        return struct.get(field_index$LAYOUT, field_index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int16_t field_index
     * }
     */
    public static void field_index(MemorySegment struct, short fieldValue) {
        struct.set(field_index$LAYOUT, field_index$OFFSET, fieldValue);
    }

    private static final OfShort flags_$LAYOUT = (OfShort)$LAYOUT.select(groupElement("flags_"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t flags_
     * }
     */
    public static final OfShort flags_$layout() {
        return flags_$LAYOUT;
    }

    private static final long flags_$OFFSET = 70;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t flags_
     * }
     */
    public static final long flags_$offset() {
        return flags_$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t flags_
     * }
     */
    public static short flags_(MemorySegment struct) {
        return struct.get(flags_$LAYOUT, flags_$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t flags_
     * }
     */
    public static void flags_(MemorySegment struct, short fieldValue) {
        struct.set(flags_$LAYOUT, flags_$OFFSET, fieldValue);
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

