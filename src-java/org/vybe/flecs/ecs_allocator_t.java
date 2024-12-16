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
 * struct ecs_allocator_t {
 *     ecs_block_allocator_t chunks;
 *     struct ecs_sparse_t sizes;
 * }
 * }
 */
public class ecs_allocator_t {

    ecs_allocator_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ecs_block_allocator_t.layout().withName("chunks"),
        ecs_sparse_t.layout().withName("sizes")
    ).withName("ecs_allocator_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout chunks$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("chunks"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t chunks
     * }
     */
    public static final GroupLayout chunks$layout() {
        return chunks$LAYOUT;
    }

    private static final long chunks$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t chunks
     * }
     */
    public static final long chunks$offset() {
        return chunks$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t chunks
     * }
     */
    public static MemorySegment chunks(MemorySegment struct) {
        return struct.asSlice(chunks$OFFSET, chunks$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_block_allocator_t chunks
     * }
     */
    public static void chunks(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, chunks$OFFSET, chunks$LAYOUT.byteSize());
    }

    private static final GroupLayout sizes$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("sizes"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct ecs_sparse_t sizes
     * }
     */
    public static final GroupLayout sizes$layout() {
        return sizes$LAYOUT;
    }

    private static final long sizes$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct ecs_sparse_t sizes
     * }
     */
    public static final long sizes$offset() {
        return sizes$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct ecs_sparse_t sizes
     * }
     */
    public static MemorySegment sizes(MemorySegment struct) {
        return struct.asSlice(sizes$OFFSET, sizes$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct ecs_sparse_t sizes
     * }
     */
    public static void sizes(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, sizes$OFFSET, sizes$LAYOUT.byteSize());
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

