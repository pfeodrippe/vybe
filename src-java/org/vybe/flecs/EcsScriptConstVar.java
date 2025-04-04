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
 * struct EcsScriptConstVar {
 *     ecs_value_t value;
 *     const ecs_type_info_t *type_info;
 * }
 * }
 */
public class EcsScriptConstVar {

    EcsScriptConstVar() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ecs_value_t.layout().withName("value"),
        flecs.C_POINTER.withName("type_info")
    ).withName("EcsScriptConstVar");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout value$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("value"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_value_t value
     * }
     */
    public static final GroupLayout value$layout() {
        return value$LAYOUT;
    }

    private static final long value$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_value_t value
     * }
     */
    public static final long value$offset() {
        return value$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_value_t value
     * }
     */
    public static MemorySegment value(MemorySegment struct) {
        return struct.asSlice(value$OFFSET, value$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_value_t value
     * }
     */
    public static void value(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, value$OFFSET, value$LAYOUT.byteSize());
    }

    private static final AddressLayout type_info$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("type_info"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const ecs_type_info_t *type_info
     * }
     */
    public static final AddressLayout type_info$layout() {
        return type_info$LAYOUT;
    }

    private static final long type_info$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const ecs_type_info_t *type_info
     * }
     */
    public static final long type_info$offset() {
        return type_info$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const ecs_type_info_t *type_info
     * }
     */
    public static MemorySegment type_info(MemorySegment struct) {
        return struct.get(type_info$LAYOUT, type_info$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const ecs_type_info_t *type_info
     * }
     */
    public static void type_info(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(type_info$LAYOUT, type_info$OFFSET, fieldValue);
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

