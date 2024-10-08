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
 * struct ImGuiTextIndex {
 *     ImVector_int LineOffsets;
 *     int EndOffset;
 * }
 * }
 */
public class ImGuiTextIndex {

    ImGuiTextIndex() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ImVector_int.layout().withName("LineOffsets"),
        imgui.C_INT.withName("EndOffset"),
        MemoryLayout.paddingLayout(4)
    ).withName("ImGuiTextIndex");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout LineOffsets$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("LineOffsets"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_int LineOffsets
     * }
     */
    public static final GroupLayout LineOffsets$layout() {
        return LineOffsets$LAYOUT;
    }

    private static final long LineOffsets$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_int LineOffsets
     * }
     */
    public static final long LineOffsets$offset() {
        return LineOffsets$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_int LineOffsets
     * }
     */
    public static MemorySegment LineOffsets(MemorySegment struct) {
        return struct.asSlice(LineOffsets$OFFSET, LineOffsets$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_int LineOffsets
     * }
     */
    public static void LineOffsets(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, LineOffsets$OFFSET, LineOffsets$LAYOUT.byteSize());
    }

    private static final OfInt EndOffset$LAYOUT = (OfInt)$LAYOUT.select(groupElement("EndOffset"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int EndOffset
     * }
     */
    public static final OfInt EndOffset$layout() {
        return EndOffset$LAYOUT;
    }

    private static final long EndOffset$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int EndOffset
     * }
     */
    public static final long EndOffset$offset() {
        return EndOffset$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int EndOffset
     * }
     */
    public static int EndOffset(MemorySegment struct) {
        return struct.get(EndOffset$LAYOUT, EndOffset$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int EndOffset
     * }
     */
    public static void EndOffset(MemorySegment struct, int fieldValue) {
        struct.set(EndOffset$LAYOUT, EndOffset$OFFSET, fieldValue);
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

