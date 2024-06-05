// Generated by jextract

package org.vybe.raylib;

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
 * struct AutomationEventList {
 *     unsigned int capacity;
 *     unsigned int count;
 *     AutomationEvent *events;
 * }
 * }
 */
public class AutomationEventList {

    AutomationEventList() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        raylib.C_INT.withName("capacity"),
        raylib.C_INT.withName("count"),
        raylib.C_POINTER.withName("events")
    ).withName("AutomationEventList");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt capacity$LAYOUT = (OfInt)$LAYOUT.select(groupElement("capacity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int capacity
     * }
     */
    public static final OfInt capacity$layout() {
        return capacity$LAYOUT;
    }

    private static final long capacity$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int capacity
     * }
     */
    public static final long capacity$offset() {
        return capacity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int capacity
     * }
     */
    public static int capacity(MemorySegment struct) {
        return struct.get(capacity$LAYOUT, capacity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int capacity
     * }
     */
    public static void capacity(MemorySegment struct, int fieldValue) {
        struct.set(capacity$LAYOUT, capacity$OFFSET, fieldValue);
    }

    private static final OfInt count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int count
     * }
     */
    public static final OfInt count$layout() {
        return count$LAYOUT;
    }

    private static final long count$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int count
     * }
     */
    public static final long count$offset() {
        return count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int count
     * }
     */
    public static int count(MemorySegment struct) {
        return struct.get(count$LAYOUT, count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int count
     * }
     */
    public static void count(MemorySegment struct, int fieldValue) {
        struct.set(count$LAYOUT, count$OFFSET, fieldValue);
    }

    private static final AddressLayout events$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("events"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * AutomationEvent *events
     * }
     */
    public static final AddressLayout events$layout() {
        return events$LAYOUT;
    }

    private static final long events$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * AutomationEvent *events
     * }
     */
    public static final long events$offset() {
        return events$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * AutomationEvent *events
     * }
     */
    public static MemorySegment events(MemorySegment struct) {
        return struct.get(events$LAYOUT, events$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * AutomationEvent *events
     * }
     */
    public static void events(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(events$LAYOUT, events$OFFSET, fieldValue);
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

