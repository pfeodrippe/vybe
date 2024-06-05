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
 * struct ecs_event_record_t {
 *     struct ecs_event_id_record_t *any;
 *     struct ecs_event_id_record_t *wildcard;
 *     struct ecs_event_id_record_t *wildcard_pair;
 *     ecs_map_t event_ids;
 *     ecs_entity_t event;
 * }
 * }
 */
public class ecs_event_record_t {

    ecs_event_record_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("any"),
        flecs.C_POINTER.withName("wildcard"),
        flecs.C_POINTER.withName("wildcard_pair"),
        ecs_map_t.layout().withName("event_ids"),
        flecs.C_LONG_LONG.withName("event")
    ).withName("ecs_event_record_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout any$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("any"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *any
     * }
     */
    public static final AddressLayout any$layout() {
        return any$LAYOUT;
    }

    private static final long any$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *any
     * }
     */
    public static final long any$offset() {
        return any$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *any
     * }
     */
    public static MemorySegment any(MemorySegment struct) {
        return struct.get(any$LAYOUT, any$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *any
     * }
     */
    public static void any(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(any$LAYOUT, any$OFFSET, fieldValue);
    }

    private static final AddressLayout wildcard$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("wildcard"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard
     * }
     */
    public static final AddressLayout wildcard$layout() {
        return wildcard$LAYOUT;
    }

    private static final long wildcard$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard
     * }
     */
    public static final long wildcard$offset() {
        return wildcard$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard
     * }
     */
    public static MemorySegment wildcard(MemorySegment struct) {
        return struct.get(wildcard$LAYOUT, wildcard$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard
     * }
     */
    public static void wildcard(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(wildcard$LAYOUT, wildcard$OFFSET, fieldValue);
    }

    private static final AddressLayout wildcard_pair$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("wildcard_pair"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard_pair
     * }
     */
    public static final AddressLayout wildcard_pair$layout() {
        return wildcard_pair$LAYOUT;
    }

    private static final long wildcard_pair$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard_pair
     * }
     */
    public static final long wildcard_pair$offset() {
        return wildcard_pair$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard_pair
     * }
     */
    public static MemorySegment wildcard_pair(MemorySegment struct) {
        return struct.get(wildcard_pair$LAYOUT, wildcard_pair$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_event_id_record_t *wildcard_pair
     * }
     */
    public static void wildcard_pair(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(wildcard_pair$LAYOUT, wildcard_pair$OFFSET, fieldValue);
    }

    private static final GroupLayout event_ids$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("event_ids"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_map_t event_ids
     * }
     */
    public static final GroupLayout event_ids$layout() {
        return event_ids$LAYOUT;
    }

    private static final long event_ids$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_map_t event_ids
     * }
     */
    public static final long event_ids$offset() {
        return event_ids$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_map_t event_ids
     * }
     */
    public static MemorySegment event_ids(MemorySegment struct) {
        return struct.asSlice(event_ids$OFFSET, event_ids$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_map_t event_ids
     * }
     */
    public static void event_ids(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, event_ids$OFFSET, event_ids$LAYOUT.byteSize());
    }

    private static final OfLong event$LAYOUT = (OfLong)$LAYOUT.select(groupElement("event"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t event
     * }
     */
    public static final OfLong event$layout() {
        return event$LAYOUT;
    }

    private static final long event$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t event
     * }
     */
    public static final long event$offset() {
        return event$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t event
     * }
     */
    public static long event(MemorySegment struct) {
        return struct.get(event$LAYOUT, event$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t event
     * }
     */
    public static void event(MemorySegment struct, long fieldValue) {
        struct.set(event$LAYOUT, event$OFFSET, fieldValue);
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

