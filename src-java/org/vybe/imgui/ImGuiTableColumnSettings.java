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
 * struct ImGuiTableColumnSettings {
 *     float WidthOrWeight;
 *     ImGuiID UserID;
 *     ImGuiTableColumnIdx Index;
 *     ImGuiTableColumnIdx DisplayOrder;
 *     ImGuiTableColumnIdx SortOrder;
 *     ImU8 SortDirection : 2;
 *     ImU8 IsEnabled : 1;
 *     ImU8 IsStretch : 1;
 * }
 * }
 */
public class ImGuiTableColumnSettings {

    ImGuiTableColumnSettings() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_FLOAT.withName("WidthOrWeight"),
        imgui.C_INT.withName("UserID"),
        imgui.C_SHORT.withName("Index"),
        imgui.C_SHORT.withName("DisplayOrder"),
        imgui.C_SHORT.withName("SortOrder"),
        MemoryLayout.paddingLayout(2)
    ).withName("ImGuiTableColumnSettings");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfFloat WidthOrWeight$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("WidthOrWeight"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float WidthOrWeight
     * }
     */
    public static final OfFloat WidthOrWeight$layout() {
        return WidthOrWeight$LAYOUT;
    }

    private static final long WidthOrWeight$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float WidthOrWeight
     * }
     */
    public static final long WidthOrWeight$offset() {
        return WidthOrWeight$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float WidthOrWeight
     * }
     */
    public static float WidthOrWeight(MemorySegment struct) {
        return struct.get(WidthOrWeight$LAYOUT, WidthOrWeight$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float WidthOrWeight
     * }
     */
    public static void WidthOrWeight(MemorySegment struct, float fieldValue) {
        struct.set(WidthOrWeight$LAYOUT, WidthOrWeight$OFFSET, fieldValue);
    }

    private static final OfInt UserID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("UserID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID UserID
     * }
     */
    public static final OfInt UserID$layout() {
        return UserID$LAYOUT;
    }

    private static final long UserID$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID UserID
     * }
     */
    public static final long UserID$offset() {
        return UserID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID UserID
     * }
     */
    public static int UserID(MemorySegment struct) {
        return struct.get(UserID$LAYOUT, UserID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID UserID
     * }
     */
    public static void UserID(MemorySegment struct, int fieldValue) {
        struct.set(UserID$LAYOUT, UserID$OFFSET, fieldValue);
    }

    private static final OfShort Index$LAYOUT = (OfShort)$LAYOUT.select(groupElement("Index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx Index
     * }
     */
    public static final OfShort Index$layout() {
        return Index$LAYOUT;
    }

    private static final long Index$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx Index
     * }
     */
    public static final long Index$offset() {
        return Index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx Index
     * }
     */
    public static short Index(MemorySegment struct) {
        return struct.get(Index$LAYOUT, Index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx Index
     * }
     */
    public static void Index(MemorySegment struct, short fieldValue) {
        struct.set(Index$LAYOUT, Index$OFFSET, fieldValue);
    }

    private static final OfShort DisplayOrder$LAYOUT = (OfShort)$LAYOUT.select(groupElement("DisplayOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx DisplayOrder
     * }
     */
    public static final OfShort DisplayOrder$layout() {
        return DisplayOrder$LAYOUT;
    }

    private static final long DisplayOrder$OFFSET = 10;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx DisplayOrder
     * }
     */
    public static final long DisplayOrder$offset() {
        return DisplayOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx DisplayOrder
     * }
     */
    public static short DisplayOrder(MemorySegment struct) {
        return struct.get(DisplayOrder$LAYOUT, DisplayOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx DisplayOrder
     * }
     */
    public static void DisplayOrder(MemorySegment struct, short fieldValue) {
        struct.set(DisplayOrder$LAYOUT, DisplayOrder$OFFSET, fieldValue);
    }

    private static final OfShort SortOrder$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SortOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx SortOrder
     * }
     */
    public static final OfShort SortOrder$layout() {
        return SortOrder$LAYOUT;
    }

    private static final long SortOrder$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx SortOrder
     * }
     */
    public static final long SortOrder$offset() {
        return SortOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx SortOrder
     * }
     */
    public static short SortOrder(MemorySegment struct) {
        return struct.get(SortOrder$LAYOUT, SortOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx SortOrder
     * }
     */
    public static void SortOrder(MemorySegment struct, short fieldValue) {
        struct.set(SortOrder$LAYOUT, SortOrder$OFFSET, fieldValue);
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

