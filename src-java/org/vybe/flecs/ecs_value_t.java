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
 * struct ecs_value_t {
 *     ecs_entity_t type;
 *     void *ptr;
 * }
 * }
 */
public class ecs_value_t {

    ecs_value_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_LONG_LONG.withName("type"),
        flecs.C_POINTER.withName("ptr")
    ).withName("ecs_value_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
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

    private static final long type$OFFSET = 0;

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

    private static final AddressLayout ptr$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ptr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *ptr
     * }
     */
    public static final AddressLayout ptr$layout() {
        return ptr$LAYOUT;
    }

    private static final long ptr$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *ptr
     * }
     */
    public static final long ptr$offset() {
        return ptr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *ptr
     * }
     */
    public static MemorySegment ptr(MemorySegment struct) {
        return struct.get(ptr$LAYOUT, ptr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *ptr
     * }
     */
    public static void ptr(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ptr$LAYOUT, ptr$OFFSET, fieldValue);
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

