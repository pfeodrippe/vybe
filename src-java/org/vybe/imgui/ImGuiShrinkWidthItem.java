// Generated by jextract

package org.vybe.imgui;

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
 * struct ImGuiShrinkWidthItem {
 *     int Index;
 *     float Width;
 *     float InitialWidth;
 * }
 * }
 */
public class ImGuiShrinkWidthItem {

    ImGuiShrinkWidthItem() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Index"),
        imgui.C_FLOAT.withName("Width"),
        imgui.C_FLOAT.withName("InitialWidth")
    ).withName("ImGuiShrinkWidthItem");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt Index$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int Index
     * }
     */
    public static final OfInt Index$layout() {
        return Index$LAYOUT;
    }

    private static final long Index$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int Index
     * }
     */
    public static final long Index$offset() {
        return Index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int Index
     * }
     */
    public static int Index(MemorySegment struct) {
        return struct.get(Index$LAYOUT, Index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int Index
     * }
     */
    public static void Index(MemorySegment struct, int fieldValue) {
        struct.set(Index$LAYOUT, Index$OFFSET, fieldValue);
    }

    private static final OfFloat Width$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("Width"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float Width
     * }
     */
    public static final OfFloat Width$layout() {
        return Width$LAYOUT;
    }

    private static final long Width$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float Width
     * }
     */
    public static final long Width$offset() {
        return Width$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float Width
     * }
     */
    public static float Width(MemorySegment struct) {
        return struct.get(Width$LAYOUT, Width$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float Width
     * }
     */
    public static void Width(MemorySegment struct, float fieldValue) {
        struct.set(Width$LAYOUT, Width$OFFSET, fieldValue);
    }

    private static final OfFloat InitialWidth$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("InitialWidth"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float InitialWidth
     * }
     */
    public static final OfFloat InitialWidth$layout() {
        return InitialWidth$LAYOUT;
    }

    private static final long InitialWidth$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float InitialWidth
     * }
     */
    public static final long InitialWidth$offset() {
        return InitialWidth$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float InitialWidth
     * }
     */
    public static float InitialWidth(MemorySegment struct) {
        return struct.get(InitialWidth$LAYOUT, InitialWidth$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float InitialWidth
     * }
     */
    public static void InitialWidth(MemorySegment struct, float fieldValue) {
        struct.set(InitialWidth$LAYOUT, InitialWidth$OFFSET, fieldValue);
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
