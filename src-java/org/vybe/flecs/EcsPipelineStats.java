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
 * struct {
 *     EcsStatsHeader hdr;
 *     ecs_map_t stats;
 * }
 * }
 */
public class EcsPipelineStats {

    EcsPipelineStats() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        EcsStatsHeader.layout().withName("hdr"),
        ecs_map_t.layout().withName("stats")
    ).withName("$anon$12491:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout hdr$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("hdr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * EcsStatsHeader hdr
     * }
     */
    public static final GroupLayout hdr$layout() {
        return hdr$LAYOUT;
    }

    private static final long hdr$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * EcsStatsHeader hdr
     * }
     */
    public static final long hdr$offset() {
        return hdr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * EcsStatsHeader hdr
     * }
     */
    public static MemorySegment hdr(MemorySegment struct) {
        return struct.asSlice(hdr$OFFSET, hdr$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * EcsStatsHeader hdr
     * }
     */
    public static void hdr(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, hdr$OFFSET, hdr$LAYOUT.byteSize());
    }

    private static final GroupLayout stats$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("stats"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_map_t stats
     * }
     */
    public static final GroupLayout stats$layout() {
        return stats$LAYOUT;
    }

    private static final long stats$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_map_t stats
     * }
     */
    public static final long stats$offset() {
        return stats$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_map_t stats
     * }
     */
    public static MemorySegment stats(MemorySegment struct) {
        return struct.asSlice(stats$OFFSET, stats$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_map_t stats
     * }
     */
    public static void stats(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, stats$OFFSET, stats$LAYOUT.byteSize());
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

