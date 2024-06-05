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
 * struct ecs_member_t {
 *     const char *name;
 *     ecs_entity_t type;
 *     int32_t count;
 *     int32_t offset;
 *     ecs_entity_t unit;
 *     ecs_member_value_range_t range;
 *     ecs_member_value_range_t error_range;
 *     ecs_member_value_range_t warning_range;
 *     ecs_size_t size;
 *     ecs_entity_t member;
 * }
 * }
 */
public class ecs_member_t {

    ecs_member_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("name"),
        flecs.C_LONG_LONG.withName("type"),
        flecs.C_INT.withName("count"),
        flecs.C_INT.withName("offset"),
        flecs.C_LONG_LONG.withName("unit"),
        ecs_member_value_range_t.layout().withName("range"),
        ecs_member_value_range_t.layout().withName("error_range"),
        ecs_member_value_range_t.layout().withName("warning_range"),
        flecs.C_INT.withName("size"),
        MemoryLayout.paddingLayout(4),
        flecs.C_LONG_LONG.withName("member")
    ).withName("ecs_member_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout name$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("name"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static final AddressLayout name$layout() {
        return name$LAYOUT;
    }

    private static final long name$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static final long name$offset() {
        return name$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static MemorySegment name(MemorySegment struct) {
        return struct.get(name$LAYOUT, name$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static void name(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(name$LAYOUT, name$OFFSET, fieldValue);
    }

    private static final OfLong type$LAYOUT = (OfLong)$LAYOUT.select(groupElement("type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static final OfLong type$layout() {
        return type$LAYOUT;
    }

    private static final long type$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static final long type$offset() {
        return type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static long type(MemorySegment struct) {
        return struct.get(type$LAYOUT, type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static void type(MemorySegment struct, long fieldValue) {
        struct.set(type$LAYOUT, type$OFFSET, fieldValue);
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

    private static final long count$OFFSET = 16;

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

    private static final OfInt offset$LAYOUT = (OfInt)$LAYOUT.select(groupElement("offset"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t offset
     * }
     */
    public static final OfInt offset$layout() {
        return offset$LAYOUT;
    }

    private static final long offset$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t offset
     * }
     */
    public static final long offset$offset() {
        return offset$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t offset
     * }
     */
    public static int offset(MemorySegment struct) {
        return struct.get(offset$LAYOUT, offset$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t offset
     * }
     */
    public static void offset(MemorySegment struct, int fieldValue) {
        struct.set(offset$LAYOUT, offset$OFFSET, fieldValue);
    }

    private static final OfLong unit$LAYOUT = (OfLong)$LAYOUT.select(groupElement("unit"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t unit
     * }
     */
    public static final OfLong unit$layout() {
        return unit$LAYOUT;
    }

    private static final long unit$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t unit
     * }
     */
    public static final long unit$offset() {
        return unit$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t unit
     * }
     */
    public static long unit(MemorySegment struct) {
        return struct.get(unit$LAYOUT, unit$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t unit
     * }
     */
    public static void unit(MemorySegment struct, long fieldValue) {
        struct.set(unit$LAYOUT, unit$OFFSET, fieldValue);
    }

    private static final GroupLayout range$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("range"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t range
     * }
     */
    public static final GroupLayout range$layout() {
        return range$LAYOUT;
    }

    private static final long range$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t range
     * }
     */
    public static final long range$offset() {
        return range$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t range
     * }
     */
    public static MemorySegment range(MemorySegment struct) {
        return struct.asSlice(range$OFFSET, range$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t range
     * }
     */
    public static void range(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, range$OFFSET, range$LAYOUT.byteSize());
    }

    private static final GroupLayout error_range$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("error_range"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t error_range
     * }
     */
    public static final GroupLayout error_range$layout() {
        return error_range$LAYOUT;
    }

    private static final long error_range$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t error_range
     * }
     */
    public static final long error_range$offset() {
        return error_range$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t error_range
     * }
     */
    public static MemorySegment error_range(MemorySegment struct) {
        return struct.asSlice(error_range$OFFSET, error_range$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t error_range
     * }
     */
    public static void error_range(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, error_range$OFFSET, error_range$LAYOUT.byteSize());
    }

    private static final GroupLayout warning_range$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("warning_range"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t warning_range
     * }
     */
    public static final GroupLayout warning_range$layout() {
        return warning_range$LAYOUT;
    }

    private static final long warning_range$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t warning_range
     * }
     */
    public static final long warning_range$offset() {
        return warning_range$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t warning_range
     * }
     */
    public static MemorySegment warning_range(MemorySegment struct) {
        return struct.asSlice(warning_range$OFFSET, warning_range$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_member_value_range_t warning_range
     * }
     */
    public static void warning_range(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, warning_range$OFFSET, warning_range$LAYOUT.byteSize());
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

    private static final long size$OFFSET = 80;

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

    private static final OfLong member$LAYOUT = (OfLong)$LAYOUT.select(groupElement("member"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t member
     * }
     */
    public static final OfLong member$layout() {
        return member$LAYOUT;
    }

    private static final long member$OFFSET = 88;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t member
     * }
     */
    public static final long member$offset() {
        return member$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t member
     * }
     */
    public static long member(MemorySegment struct) {
        return struct.get(member$LAYOUT, member$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t member
     * }
     */
    public static void member(MemorySegment struct, long fieldValue) {
        struct.set(member$LAYOUT, member$OFFSET, fieldValue);
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

