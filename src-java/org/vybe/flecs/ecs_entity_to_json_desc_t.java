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
 *     bool serialize_path;
 *     bool serialize_label;
 *     bool serialize_brief;
 *     bool serialize_link;
 *     bool serialize_color;
 *     bool serialize_ids;
 *     bool serialize_id_labels;
 *     bool serialize_base;
 *     bool serialize_private;
 *     bool serialize_hidden;
 *     bool serialize_values;
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
        flecs.C_BOOL.withName("serialize_path"),
        flecs.C_BOOL.withName("serialize_label"),
        flecs.C_BOOL.withName("serialize_brief"),
        flecs.C_BOOL.withName("serialize_link"),
        flecs.C_BOOL.withName("serialize_color"),
        flecs.C_BOOL.withName("serialize_ids"),
        flecs.C_BOOL.withName("serialize_id_labels"),
        flecs.C_BOOL.withName("serialize_base"),
        flecs.C_BOOL.withName("serialize_private"),
        flecs.C_BOOL.withName("serialize_hidden"),
        flecs.C_BOOL.withName("serialize_values"),
        flecs.C_BOOL.withName("serialize_type_info"),
        flecs.C_BOOL.withName("serialize_alerts"),
        MemoryLayout.paddingLayout(3),
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

    private static final OfBoolean serialize_path$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_path"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_path
     * }
     */
    public static final OfBoolean serialize_path$layout() {
        return serialize_path$LAYOUT;
    }

    private static final long serialize_path$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_path
     * }
     */
    public static final long serialize_path$offset() {
        return serialize_path$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_path
     * }
     */
    public static boolean serialize_path(MemorySegment struct) {
        return struct.get(serialize_path$LAYOUT, serialize_path$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_path
     * }
     */
    public static void serialize_path(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_path$LAYOUT, serialize_path$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_label$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_label"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_label
     * }
     */
    public static final OfBoolean serialize_label$layout() {
        return serialize_label$LAYOUT;
    }

    private static final long serialize_label$OFFSET = 1;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_label
     * }
     */
    public static final long serialize_label$offset() {
        return serialize_label$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_label
     * }
     */
    public static boolean serialize_label(MemorySegment struct) {
        return struct.get(serialize_label$LAYOUT, serialize_label$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_label
     * }
     */
    public static void serialize_label(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_label$LAYOUT, serialize_label$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_brief$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_brief"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_brief
     * }
     */
    public static final OfBoolean serialize_brief$layout() {
        return serialize_brief$LAYOUT;
    }

    private static final long serialize_brief$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_brief
     * }
     */
    public static final long serialize_brief$offset() {
        return serialize_brief$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_brief
     * }
     */
    public static boolean serialize_brief(MemorySegment struct) {
        return struct.get(serialize_brief$LAYOUT, serialize_brief$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_brief
     * }
     */
    public static void serialize_brief(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_brief$LAYOUT, serialize_brief$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_link$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_link"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_link
     * }
     */
    public static final OfBoolean serialize_link$layout() {
        return serialize_link$LAYOUT;
    }

    private static final long serialize_link$OFFSET = 3;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_link
     * }
     */
    public static final long serialize_link$offset() {
        return serialize_link$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_link
     * }
     */
    public static boolean serialize_link(MemorySegment struct) {
        return struct.get(serialize_link$LAYOUT, serialize_link$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_link
     * }
     */
    public static void serialize_link(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_link$LAYOUT, serialize_link$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_color$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_color"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_color
     * }
     */
    public static final OfBoolean serialize_color$layout() {
        return serialize_color$LAYOUT;
    }

    private static final long serialize_color$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_color
     * }
     */
    public static final long serialize_color$offset() {
        return serialize_color$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_color
     * }
     */
    public static boolean serialize_color(MemorySegment struct) {
        return struct.get(serialize_color$LAYOUT, serialize_color$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_color
     * }
     */
    public static void serialize_color(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_color$LAYOUT, serialize_color$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_ids$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_ids"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_ids
     * }
     */
    public static final OfBoolean serialize_ids$layout() {
        return serialize_ids$LAYOUT;
    }

    private static final long serialize_ids$OFFSET = 5;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_ids
     * }
     */
    public static final long serialize_ids$offset() {
        return serialize_ids$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_ids
     * }
     */
    public static boolean serialize_ids(MemorySegment struct) {
        return struct.get(serialize_ids$LAYOUT, serialize_ids$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_ids
     * }
     */
    public static void serialize_ids(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_ids$LAYOUT, serialize_ids$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_id_labels$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_id_labels"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_id_labels
     * }
     */
    public static final OfBoolean serialize_id_labels$layout() {
        return serialize_id_labels$LAYOUT;
    }

    private static final long serialize_id_labels$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_id_labels
     * }
     */
    public static final long serialize_id_labels$offset() {
        return serialize_id_labels$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_id_labels
     * }
     */
    public static boolean serialize_id_labels(MemorySegment struct) {
        return struct.get(serialize_id_labels$LAYOUT, serialize_id_labels$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_id_labels
     * }
     */
    public static void serialize_id_labels(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_id_labels$LAYOUT, serialize_id_labels$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_base$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_base"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_base
     * }
     */
    public static final OfBoolean serialize_base$layout() {
        return serialize_base$LAYOUT;
    }

    private static final long serialize_base$OFFSET = 7;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_base
     * }
     */
    public static final long serialize_base$offset() {
        return serialize_base$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_base
     * }
     */
    public static boolean serialize_base(MemorySegment struct) {
        return struct.get(serialize_base$LAYOUT, serialize_base$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_base
     * }
     */
    public static void serialize_base(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_base$LAYOUT, serialize_base$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_private$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_private"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_private
     * }
     */
    public static final OfBoolean serialize_private$layout() {
        return serialize_private$LAYOUT;
    }

    private static final long serialize_private$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_private
     * }
     */
    public static final long serialize_private$offset() {
        return serialize_private$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_private
     * }
     */
    public static boolean serialize_private(MemorySegment struct) {
        return struct.get(serialize_private$LAYOUT, serialize_private$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_private
     * }
     */
    public static void serialize_private(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_private$LAYOUT, serialize_private$OFFSET, fieldValue);
    }

    private static final OfBoolean serialize_hidden$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("serialize_hidden"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool serialize_hidden
     * }
     */
    public static final OfBoolean serialize_hidden$layout() {
        return serialize_hidden$LAYOUT;
    }

    private static final long serialize_hidden$OFFSET = 9;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool serialize_hidden
     * }
     */
    public static final long serialize_hidden$offset() {
        return serialize_hidden$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool serialize_hidden
     * }
     */
    public static boolean serialize_hidden(MemorySegment struct) {
        return struct.get(serialize_hidden$LAYOUT, serialize_hidden$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool serialize_hidden
     * }
     */
    public static void serialize_hidden(MemorySegment struct, boolean fieldValue) {
        struct.set(serialize_hidden$LAYOUT, serialize_hidden$OFFSET, fieldValue);
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

    private static final long serialize_values$OFFSET = 10;

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

    private static final long serialize_type_info$OFFSET = 11;

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

    private static final long serialize_alerts$OFFSET = 12;

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

    private static final long serialize_refs$OFFSET = 16;

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

    private static final long serialize_matches$OFFSET = 24;

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

