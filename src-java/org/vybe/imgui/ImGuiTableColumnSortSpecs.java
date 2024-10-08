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
 * struct ImGuiTableColumnSortSpecs {
 *     ImGuiID ColumnUserID;
 *     ImS16 ColumnIndex;
 *     ImS16 SortOrder;
 *     ImGuiSortDirection SortDirection;
 * }
 * }
 */
public class ImGuiTableColumnSortSpecs {

    ImGuiTableColumnSortSpecs() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("ColumnUserID"),
        imgui.C_SHORT.withName("ColumnIndex"),
        imgui.C_SHORT.withName("SortOrder"),
        imgui.C_INT.withName("SortDirection")
    ).withName("ImGuiTableColumnSortSpecs");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt ColumnUserID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ColumnUserID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID ColumnUserID
     * }
     */
    public static final OfInt ColumnUserID$layout() {
        return ColumnUserID$LAYOUT;
    }

    private static final long ColumnUserID$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID ColumnUserID
     * }
     */
    public static final long ColumnUserID$offset() {
        return ColumnUserID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID ColumnUserID
     * }
     */
    public static int ColumnUserID(MemorySegment struct) {
        return struct.get(ColumnUserID$LAYOUT, ColumnUserID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID ColumnUserID
     * }
     */
    public static void ColumnUserID(MemorySegment struct, int fieldValue) {
        struct.set(ColumnUserID$LAYOUT, ColumnUserID$OFFSET, fieldValue);
    }

    private static final OfShort ColumnIndex$LAYOUT = (OfShort)$LAYOUT.select(groupElement("ColumnIndex"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImS16 ColumnIndex
     * }
     */
    public static final OfShort ColumnIndex$layout() {
        return ColumnIndex$LAYOUT;
    }

    private static final long ColumnIndex$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImS16 ColumnIndex
     * }
     */
    public static final long ColumnIndex$offset() {
        return ColumnIndex$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImS16 ColumnIndex
     * }
     */
    public static short ColumnIndex(MemorySegment struct) {
        return struct.get(ColumnIndex$LAYOUT, ColumnIndex$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImS16 ColumnIndex
     * }
     */
    public static void ColumnIndex(MemorySegment struct, short fieldValue) {
        struct.set(ColumnIndex$LAYOUT, ColumnIndex$OFFSET, fieldValue);
    }

    private static final OfShort SortOrder$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SortOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImS16 SortOrder
     * }
     */
    public static final OfShort SortOrder$layout() {
        return SortOrder$LAYOUT;
    }

    private static final long SortOrder$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImS16 SortOrder
     * }
     */
    public static final long SortOrder$offset() {
        return SortOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImS16 SortOrder
     * }
     */
    public static short SortOrder(MemorySegment struct) {
        return struct.get(SortOrder$LAYOUT, SortOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImS16 SortOrder
     * }
     */
    public static void SortOrder(MemorySegment struct, short fieldValue) {
        struct.set(SortOrder$LAYOUT, SortOrder$OFFSET, fieldValue);
    }

    private static final OfInt SortDirection$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SortDirection"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiSortDirection SortDirection
     * }
     */
    public static final OfInt SortDirection$layout() {
        return SortDirection$LAYOUT;
    }

    private static final long SortDirection$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiSortDirection SortDirection
     * }
     */
    public static final long SortDirection$offset() {
        return SortDirection$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiSortDirection SortDirection
     * }
     */
    public static int SortDirection(MemorySegment struct) {
        return struct.get(SortDirection$LAYOUT, SortDirection$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiSortDirection SortDirection
     * }
     */
    public static void SortDirection(MemorySegment struct, int fieldValue) {
        struct.set(SortDirection$LAYOUT, SortDirection$OFFSET, fieldValue);
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

