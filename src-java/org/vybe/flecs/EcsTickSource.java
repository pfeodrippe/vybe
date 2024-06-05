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
 * struct EcsTickSource {
 *     bool tick;
 *     float time_elapsed;
 * }
 * }
 */
public class EcsTickSource {

    EcsTickSource() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_BOOL.withName("tick"),
        MemoryLayout.paddingLayout(3),
        flecs.C_FLOAT.withName("time_elapsed")
    ).withName("EcsTickSource");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfBoolean tick$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("tick"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool tick
     * }
     */
    public static final OfBoolean tick$layout() {
        return tick$LAYOUT;
    }

    private static final long tick$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool tick
     * }
     */
    public static final long tick$offset() {
        return tick$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool tick
     * }
     */
    public static boolean tick(MemorySegment struct) {
        return struct.get(tick$LAYOUT, tick$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool tick
     * }
     */
    public static void tick(MemorySegment struct, boolean fieldValue) {
        struct.set(tick$LAYOUT, tick$OFFSET, fieldValue);
    }

    private static final OfFloat time_elapsed$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("time_elapsed"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float time_elapsed
     * }
     */
    public static final OfFloat time_elapsed$layout() {
        return time_elapsed$LAYOUT;
    }

    private static final long time_elapsed$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float time_elapsed
     * }
     */
    public static final long time_elapsed$offset() {
        return time_elapsed$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float time_elapsed
     * }
     */
    public static float time_elapsed(MemorySegment struct) {
        return struct.get(time_elapsed$LAYOUT, time_elapsed$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float time_elapsed
     * }
     */
    public static void time_elapsed(MemorySegment struct, float fieldValue) {
        struct.set(time_elapsed$LAYOUT, time_elapsed$OFFSET, fieldValue);
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

