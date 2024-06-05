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
 * struct ecs_map_params_t {
 *     struct ecs_allocator_t *allocator;
 *     struct ecs_block_allocator_t entry_allocator;
 * }
 * }
 */
public class ecs_map_params_t {

    ecs_map_params_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("allocator"),
        ecs_block_allocator_t.layout().withName("entry_allocator")
    ).withName("ecs_map_params_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout allocator$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static final AddressLayout allocator$layout() {
        return allocator$LAYOUT;
    }

    private static final long allocator$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static final long allocator$offset() {
        return allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static MemorySegment allocator(MemorySegment struct) {
        return struct.get(allocator$LAYOUT, allocator$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_allocator_t *allocator
     * }
     */
    public static void allocator(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(allocator$LAYOUT, allocator$OFFSET, fieldValue);
    }

    private static final GroupLayout entry_allocator$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("entry_allocator"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t entry_allocator
     * }
     */
    public static final GroupLayout entry_allocator$layout() {
        return entry_allocator$LAYOUT;
    }

    private static final long entry_allocator$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t entry_allocator
     * }
     */
    public static final long entry_allocator$offset() {
        return entry_allocator$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t entry_allocator
     * }
     */
    public static MemorySegment entry_allocator(MemorySegment struct) {
        return struct.asSlice(entry_allocator$OFFSET, entry_allocator$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_block_allocator_t entry_allocator
     * }
     */
    public static void entry_allocator(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, entry_allocator$OFFSET, entry_allocator$LAYOUT.byteSize());
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

