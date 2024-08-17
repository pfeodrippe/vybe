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
 * struct ImGuiOldColumnData {
 *     float OffsetNorm;
 *     float OffsetNormBeforeResize;
 *     ImGuiOldColumnFlags Flags;
 *     ImRect ClipRect;
 * }
 * }
 */
public class ImGuiOldColumnData {

    ImGuiOldColumnData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_FLOAT.withName("OffsetNorm"),
        imgui.C_FLOAT.withName("OffsetNormBeforeResize"),
        imgui.C_INT.withName("Flags"),
        ImRect.layout().withName("ClipRect")
    ).withName("ImGuiOldColumnData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfFloat OffsetNorm$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("OffsetNorm"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float OffsetNorm
     * }
     */
    public static final OfFloat OffsetNorm$layout() {
        return OffsetNorm$LAYOUT;
    }

    private static final long OffsetNorm$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float OffsetNorm
     * }
     */
    public static final long OffsetNorm$offset() {
        return OffsetNorm$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float OffsetNorm
     * }
     */
    public static float OffsetNorm(MemorySegment struct) {
        return struct.get(OffsetNorm$LAYOUT, OffsetNorm$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float OffsetNorm
     * }
     */
    public static void OffsetNorm(MemorySegment struct, float fieldValue) {
        struct.set(OffsetNorm$LAYOUT, OffsetNorm$OFFSET, fieldValue);
    }

    private static final OfFloat OffsetNormBeforeResize$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("OffsetNormBeforeResize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float OffsetNormBeforeResize
     * }
     */
    public static final OfFloat OffsetNormBeforeResize$layout() {
        return OffsetNormBeforeResize$LAYOUT;
    }

    private static final long OffsetNormBeforeResize$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float OffsetNormBeforeResize
     * }
     */
    public static final long OffsetNormBeforeResize$offset() {
        return OffsetNormBeforeResize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float OffsetNormBeforeResize
     * }
     */
    public static float OffsetNormBeforeResize(MemorySegment struct) {
        return struct.get(OffsetNormBeforeResize$LAYOUT, OffsetNormBeforeResize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float OffsetNormBeforeResize
     * }
     */
    public static void OffsetNormBeforeResize(MemorySegment struct, float fieldValue) {
        struct.set(OffsetNormBeforeResize$LAYOUT, OffsetNormBeforeResize$OFFSET, fieldValue);
    }

    private static final OfInt Flags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Flags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiOldColumnFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiOldColumnFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiOldColumnFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiOldColumnFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
    }

    private static final GroupLayout ClipRect$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("ClipRect"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImRect ClipRect
     * }
     */
    public static final GroupLayout ClipRect$layout() {
        return ClipRect$LAYOUT;
    }

    private static final long ClipRect$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImRect ClipRect
     * }
     */
    public static final long ClipRect$offset() {
        return ClipRect$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImRect ClipRect
     * }
     */
    public static MemorySegment ClipRect(MemorySegment struct) {
        return struct.asSlice(ClipRect$OFFSET, ClipRect$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImRect ClipRect
     * }
     */
    public static void ClipRect(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ClipRect$OFFSET, ClipRect$LAYOUT.byteSize());
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

