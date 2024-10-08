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
 * struct ImGuiTableSettings {
 *     ImGuiID ID;
 *     ImGuiTableFlags SaveFlags;
 *     float RefScale;
 *     ImGuiTableColumnIdx ColumnsCount;
 *     ImGuiTableColumnIdx ColumnsCountMax;
 *     bool WantApply;
 * }
 * }
 */
public class ImGuiTableSettings {

    ImGuiTableSettings() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("SaveFlags"),
        imgui.C_FLOAT.withName("RefScale"),
        imgui.C_SHORT.withName("ColumnsCount"),
        imgui.C_SHORT.withName("ColumnsCountMax"),
        imgui.C_BOOL.withName("WantApply"),
        MemoryLayout.paddingLayout(3)
    ).withName("ImGuiTableSettings");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt ID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID ID
     * }
     */
    public static final OfInt ID$layout() {
        return ID$LAYOUT;
    }

    private static final long ID$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID ID
     * }
     */
    public static final long ID$offset() {
        return ID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID ID
     * }
     */
    public static int ID(MemorySegment struct) {
        return struct.get(ID$LAYOUT, ID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID ID
     * }
     */
    public static void ID(MemorySegment struct, int fieldValue) {
        struct.set(ID$LAYOUT, ID$OFFSET, fieldValue);
    }

    private static final OfInt SaveFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SaveFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableFlags SaveFlags
     * }
     */
    public static final OfInt SaveFlags$layout() {
        return SaveFlags$LAYOUT;
    }

    private static final long SaveFlags$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableFlags SaveFlags
     * }
     */
    public static final long SaveFlags$offset() {
        return SaveFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableFlags SaveFlags
     * }
     */
    public static int SaveFlags(MemorySegment struct) {
        return struct.get(SaveFlags$LAYOUT, SaveFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableFlags SaveFlags
     * }
     */
    public static void SaveFlags(MemorySegment struct, int fieldValue) {
        struct.set(SaveFlags$LAYOUT, SaveFlags$OFFSET, fieldValue);
    }

    private static final OfFloat RefScale$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("RefScale"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float RefScale
     * }
     */
    public static final OfFloat RefScale$layout() {
        return RefScale$LAYOUT;
    }

    private static final long RefScale$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float RefScale
     * }
     */
    public static final long RefScale$offset() {
        return RefScale$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float RefScale
     * }
     */
    public static float RefScale(MemorySegment struct) {
        return struct.get(RefScale$LAYOUT, RefScale$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float RefScale
     * }
     */
    public static void RefScale(MemorySegment struct, float fieldValue) {
        struct.set(RefScale$LAYOUT, RefScale$OFFSET, fieldValue);
    }

    private static final OfShort ColumnsCount$LAYOUT = (OfShort)$LAYOUT.select(groupElement("ColumnsCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCount
     * }
     */
    public static final OfShort ColumnsCount$layout() {
        return ColumnsCount$LAYOUT;
    }

    private static final long ColumnsCount$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCount
     * }
     */
    public static final long ColumnsCount$offset() {
        return ColumnsCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCount
     * }
     */
    public static short ColumnsCount(MemorySegment struct) {
        return struct.get(ColumnsCount$LAYOUT, ColumnsCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCount
     * }
     */
    public static void ColumnsCount(MemorySegment struct, short fieldValue) {
        struct.set(ColumnsCount$LAYOUT, ColumnsCount$OFFSET, fieldValue);
    }

    private static final OfShort ColumnsCountMax$LAYOUT = (OfShort)$LAYOUT.select(groupElement("ColumnsCountMax"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCountMax
     * }
     */
    public static final OfShort ColumnsCountMax$layout() {
        return ColumnsCountMax$LAYOUT;
    }

    private static final long ColumnsCountMax$OFFSET = 14;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCountMax
     * }
     */
    public static final long ColumnsCountMax$offset() {
        return ColumnsCountMax$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCountMax
     * }
     */
    public static short ColumnsCountMax(MemorySegment struct) {
        return struct.get(ColumnsCountMax$LAYOUT, ColumnsCountMax$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumnIdx ColumnsCountMax
     * }
     */
    public static void ColumnsCountMax(MemorySegment struct, short fieldValue) {
        struct.set(ColumnsCountMax$LAYOUT, ColumnsCountMax$OFFSET, fieldValue);
    }

    private static final OfBoolean WantApply$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("WantApply"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool WantApply
     * }
     */
    public static final OfBoolean WantApply$layout() {
        return WantApply$LAYOUT;
    }

    private static final long WantApply$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool WantApply
     * }
     */
    public static final long WantApply$offset() {
        return WantApply$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool WantApply
     * }
     */
    public static boolean WantApply(MemorySegment struct) {
        return struct.get(WantApply$LAYOUT, WantApply$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool WantApply
     * }
     */
    public static void WantApply(MemorySegment struct, boolean fieldValue) {
        struct.set(WantApply$LAYOUT, WantApply$OFFSET, fieldValue);
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

