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
 * struct ecs_entity_desc_t {
 *     int32_t _canary;
 *     ecs_entity_t id;
 *     ecs_entity_t parent;
 *     const char *name;
 *     const char *sep;
 *     const char *root_sep;
 *     const char *symbol;
 *     bool use_low_id;
 *     const ecs_id_t *add;
 *     const ecs_value_t *set;
 *     const char *add_expr;
 * }
 * }
 */
public class ecs_entity_desc_t {

    ecs_entity_desc_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_INT.withName("_canary"),
        MemoryLayout.paddingLayout(4),
        flecs.C_LONG_LONG.withName("id"),
        flecs.C_LONG_LONG.withName("parent"),
        flecs.C_POINTER.withName("name"),
        flecs.C_POINTER.withName("sep"),
        flecs.C_POINTER.withName("root_sep"),
        flecs.C_POINTER.withName("symbol"),
        flecs.C_BOOL.withName("use_low_id"),
        MemoryLayout.paddingLayout(7),
        flecs.C_POINTER.withName("add"),
        flecs.C_POINTER.withName("set"),
        flecs.C_POINTER.withName("add_expr")
    ).withName("ecs_entity_desc_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt _canary$LAYOUT = (OfInt)$LAYOUT.select(groupElement("_canary"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static final OfInt _canary$layout() {
        return _canary$LAYOUT;
    }

    private static final long _canary$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static final long _canary$offset() {
        return _canary$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static int _canary(MemorySegment struct) {
        return struct.get(_canary$LAYOUT, _canary$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static void _canary(MemorySegment struct, int fieldValue) {
        struct.set(_canary$LAYOUT, _canary$OFFSET, fieldValue);
    }

    private static final OfLong id$LAYOUT = (OfLong)$LAYOUT.select(groupElement("id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t id
     * }
     */
    public static final OfLong id$layout() {
        return id$LAYOUT;
    }

    private static final long id$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t id
     * }
     */
    public static final long id$offset() {
        return id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t id
     * }
     */
    public static long id(MemorySegment struct) {
        return struct.get(id$LAYOUT, id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t id
     * }
     */
    public static void id(MemorySegment struct, long fieldValue) {
        struct.set(id$LAYOUT, id$OFFSET, fieldValue);
    }

    private static final OfLong parent$LAYOUT = (OfLong)$LAYOUT.select(groupElement("parent"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t parent
     * }
     */
    public static final OfLong parent$layout() {
        return parent$LAYOUT;
    }

    private static final long parent$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t parent
     * }
     */
    public static final long parent$offset() {
        return parent$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t parent
     * }
     */
    public static long parent(MemorySegment struct) {
        return struct.get(parent$LAYOUT, parent$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t parent
     * }
     */
    public static void parent(MemorySegment struct, long fieldValue) {
        struct.set(parent$LAYOUT, parent$OFFSET, fieldValue);
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

    private static final long name$OFFSET = 24;

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

    private static final AddressLayout sep$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("sep"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *sep
     * }
     */
    public static final AddressLayout sep$layout() {
        return sep$LAYOUT;
    }

    private static final long sep$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *sep
     * }
     */
    public static final long sep$offset() {
        return sep$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *sep
     * }
     */
    public static MemorySegment sep(MemorySegment struct) {
        return struct.get(sep$LAYOUT, sep$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *sep
     * }
     */
    public static void sep(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(sep$LAYOUT, sep$OFFSET, fieldValue);
    }

    private static final AddressLayout root_sep$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("root_sep"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *root_sep
     * }
     */
    public static final AddressLayout root_sep$layout() {
        return root_sep$LAYOUT;
    }

    private static final long root_sep$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *root_sep
     * }
     */
    public static final long root_sep$offset() {
        return root_sep$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *root_sep
     * }
     */
    public static MemorySegment root_sep(MemorySegment struct) {
        return struct.get(root_sep$LAYOUT, root_sep$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *root_sep
     * }
     */
    public static void root_sep(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(root_sep$LAYOUT, root_sep$OFFSET, fieldValue);
    }

    private static final AddressLayout symbol$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("symbol"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *symbol
     * }
     */
    public static final AddressLayout symbol$layout() {
        return symbol$LAYOUT;
    }

    private static final long symbol$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *symbol
     * }
     */
    public static final long symbol$offset() {
        return symbol$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *symbol
     * }
     */
    public static MemorySegment symbol(MemorySegment struct) {
        return struct.get(symbol$LAYOUT, symbol$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *symbol
     * }
     */
    public static void symbol(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(symbol$LAYOUT, symbol$OFFSET, fieldValue);
    }

    private static final OfBoolean use_low_id$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("use_low_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool use_low_id
     * }
     */
    public static final OfBoolean use_low_id$layout() {
        return use_low_id$LAYOUT;
    }

    private static final long use_low_id$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool use_low_id
     * }
     */
    public static final long use_low_id$offset() {
        return use_low_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool use_low_id
     * }
     */
    public static boolean use_low_id(MemorySegment struct) {
        return struct.get(use_low_id$LAYOUT, use_low_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool use_low_id
     * }
     */
    public static void use_low_id(MemorySegment struct, boolean fieldValue) {
        struct.set(use_low_id$LAYOUT, use_low_id$OFFSET, fieldValue);
    }

    private static final AddressLayout add$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("add"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const ecs_id_t *add
     * }
     */
    public static final AddressLayout add$layout() {
        return add$LAYOUT;
    }

    private static final long add$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const ecs_id_t *add
     * }
     */
    public static final long add$offset() {
        return add$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const ecs_id_t *add
     * }
     */
    public static MemorySegment add(MemorySegment struct) {
        return struct.get(add$LAYOUT, add$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const ecs_id_t *add
     * }
     */
    public static void add(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(add$LAYOUT, add$OFFSET, fieldValue);
    }

    private static final AddressLayout set$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("set"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const ecs_value_t *set
     * }
     */
    public static final AddressLayout set$layout() {
        return set$LAYOUT;
    }

    private static final long set$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const ecs_value_t *set
     * }
     */
    public static final long set$offset() {
        return set$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const ecs_value_t *set
     * }
     */
    public static MemorySegment set(MemorySegment struct) {
        return struct.get(set$LAYOUT, set$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const ecs_value_t *set
     * }
     */
    public static void set(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(set$LAYOUT, set$OFFSET, fieldValue);
    }

    private static final AddressLayout add_expr$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("add_expr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *add_expr
     * }
     */
    public static final AddressLayout add_expr$layout() {
        return add_expr$LAYOUT;
    }

    private static final long add_expr$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *add_expr
     * }
     */
    public static final long add_expr$offset() {
        return add_expr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *add_expr
     * }
     */
    public static MemorySegment add_expr(MemorySegment struct) {
        return struct.get(add_expr$LAYOUT, add_expr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *add_expr
     * }
     */
    public static void add_expr(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(add_expr$LAYOUT, add_expr$OFFSET, fieldValue);
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

