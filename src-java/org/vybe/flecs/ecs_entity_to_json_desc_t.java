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
 * struct ecs_entity_to_json_desc_t {
 *     bool serialize_entity_id;
 *     bool serialize_doc;
 *     bool serialize_full_paths;
 *     bool serialize_inherited;
 *     bool serialize_values;
 *     bool serialize_builtin;
 *     bool serialize_type_info;
 *     bool serialize_alerts;
 *     ecs_entity_t serialize_refs;
 *     bool serialize_matches;
 * }
 * }
 */
public class ecs_entity_to_json_desc_t {

    ecs_entity_to_json_desc_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_BOOL.withName("serialize_entity_id"),
        flecs.C_BOOL.withName("serialize_doc"),
        flecs.C_BOOL.withName("serialize_full_paths"),
        flecs.C_BOOL.withName("serialize_inherited"),
        flecs.C_BOOL.withName("serialize_values"),
        flecs.C_BOOL.withName("serialize_builtin"),
        flecs.C_BOOL.withName("serialize_type_info"),
        flecs.C_BOOL.withName("serialize_alerts"),
        flecs.C_LONG_LONG.withName("serialize_refs"),
        flecs.C_BOOL.withName("serialize_matches"),
        MemoryLayout.paddingLayout(7)
    ).withName("ecs_entity_to_json_desc_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfBoolean serialize_entity_id$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_entity_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_entity_id
     * }
     */
    public static final OfBoolean serialize_entity_id$layout() {
        return serialize_entity_id$LAYOUT;
    }

    private static final long serialize_entity_id$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_entity_id
     * }
     */
    public static final long serialize_entity_id$offset() {
        return serialize_entity_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_entity_id
     * }
     */
    public static boolean serialize_entity_id(MemorySegment struct) {
        return struct.get(serialize_entity_id$LAYOUT, serialize_entity_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_entity_id
     * }
     */
    public static void serialize_entity_id(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_entity_id$LAYOUT, serialize_entity_id$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_doc$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_doc"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_doc
     * }
     */
    public static final OfBoolean serialize_doc$layout() {
        return serialize_doc$LAYOUT;
    }

    private static final long serialize_doc$OFFSET = 1;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_doc
     * }
     */
    public static final long serialize_doc$offset() {
        return serialize_doc$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_doc
     * }
     */
    public static boolean serialize_doc(MemorySegment struct) {
        return struct.get(serialize_doc$LAYOUT, serialize_doc$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_doc
     * }
     */
    public static void serialize_doc(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_doc$LAYOUT, serialize_doc$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_full_paths$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_full_paths"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_full_paths
     * }
     */
    public static final OfBoolean serialize_full_paths$layout() {
        return serialize_full_paths$LAYOUT;
    }

    private static final long serialize_full_paths$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_full_paths
     * }
     */
    public static final long serialize_full_paths$offset() {
        return serialize_full_paths$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_full_paths
     * }
     */
    public static boolean serialize_full_paths(MemorySegment struct) {
        return struct.get(serialize_full_paths$LAYOUT, serialize_full_paths$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_full_paths
     * }
     */
    public static void serialize_full_paths(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_full_paths$LAYOUT, serialize_full_paths$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_inherited$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_inherited"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_inherited
     * }
     */
    public static final OfBoolean serialize_inherited$layout() {
        return serialize_inherited$LAYOUT;
    }

    private static final long serialize_inherited$OFFSET = 3;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_inherited
     * }
     */
    public static final long serialize_inherited$offset() {
        return serialize_inherited$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_inherited
     * }
     */
    public static boolean serialize_inherited(MemorySegment struct) {
        return struct.get(serialize_inherited$LAYOUT, serialize_inherited$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_inherited
     * }
     */
    public static void serialize_inherited(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_inherited$LAYOUT, serialize_inherited$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_values$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_values"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_values
     * }
     */
    public static final OfBoolean serialize_values$layout() {
        return serialize_values$LAYOUT;
    }

    private static final long serialize_values$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_values
     * }
     */
    public static final long serialize_values$offset() {
        return serialize_values$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_values
     * }
     */
    public static boolean serialize_values(MemorySegment struct) {
        return struct.get(serialize_values$LAYOUT, serialize_values$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_values
     * }
     */
    public static void serialize_values(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_values$LAYOUT, serialize_values$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_builtin$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_builtin"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_builtin
     * }
     */
    public static final OfBoolean serialize_builtin$layout() {
        return serialize_builtin$LAYOUT;
    }

    private static final long serialize_builtin$OFFSET = 5;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_builtin
     * }
     */
    public static final long serialize_builtin$offset() {
        return serialize_builtin$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_builtin
     * }
     */
    public static boolean serialize_builtin(MemorySegment struct) {
        return struct.get(serialize_builtin$LAYOUT, serialize_builtin$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_builtin
     * }
     */
    public static void serialize_builtin(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_builtin$LAYOUT, serialize_builtin$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_type_info$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_type_info"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_type_info
     * }
     */
    public static final OfBoolean serialize_type_info$layout() {
        return serialize_type_info$LAYOUT;
    }

    private static final long serialize_type_info$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_type_info
     * }
     */
    public static final long serialize_type_info$offset() {
        return serialize_type_info$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_type_info
     * }
     */
    public static boolean serialize_type_info(MemorySegment struct) {
        return struct.get(serialize_type_info$LAYOUT, serialize_type_info$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_type_info
     * }
     */
    public static void serialize_type_info(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_type_info$LAYOUT, serialize_type_info$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_alerts$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_alerts"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_alerts
     * }
     */
    public static final OfBoolean serialize_alerts$layout() {
        return serialize_alerts$LAYOUT;
    }

    private static final long serialize_alerts$OFFSET = 7;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_alerts
     * }
     */
    public static final long serialize_alerts$offset() {
        return serialize_alerts$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_alerts
     * }
     */
    public static boolean serialize_alerts(MemorySegment struct) {
        return struct.get(serialize_alerts$LAYOUT, serialize_alerts$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_alerts
     * }
     */
    public static void serialize_alerts(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_alerts$LAYOUT, serialize_alerts$OFFSET, fieldValue);
    }

    private static final OfLong serialize_refs$LAYOUT = (OfLong)$LAYOUT.select(groupElement("serialize_refs"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t serialize_refs
     * }
     */
    public static final OfLong serialize_refs$layout() {
        return serialize_refs$LAYOUT;
    }

    private static final long serialize_refs$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t serialize_refs
     * }
     */
    public static final long serialize_refs$offset() {
        return serialize_refs$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t serialize_refs
     * }
     */
    public static long serialize_refs(MemorySegment struct) {
        return struct.get(serialize_refs$LAYOUT, serialize_refs$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t serialize_refs
     * }
     */
    public static void serialize_refs(MemorySegment struct, long fieldValue) {
        struct.set(serialize_refs$LAYOUT, serialize_refs$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_matches$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_matches"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_matches
     * }
     */
    public static final OfBoolean serialize_matches$layout() {
        return serialize_matches$LAYOUT;
    }

    private static final long serialize_matches$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_matches
     * }
     */
    public static final long serialize_matches$offset() {
        return serialize_matches$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_matches
     * }
     */
    public static boolean serialize_matches(MemorySegment struct) {
        return struct.get(serialize_matches$LAYOUT, serialize_matches$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_matches
     * }
     */
    public static void serialize_matches(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_matches$LAYOUT, serialize_matches$OFFSET, fieldValue);
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

