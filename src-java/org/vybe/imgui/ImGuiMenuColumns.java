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
 * struct ImGuiMenuColumns {
 *     ImU32 TotalWidth;
 *     ImU32 NextTotalWidth;
 *     ImU16 Spacing;
 *     ImU16 OffsetIcon;
 *     ImU16 OffsetLabel;
 *     ImU16 OffsetShortcut;
 *     ImU16 OffsetMark;
 *     ImU16 Widths[4];
 * }
 * }
 */
public class ImGuiMenuColumns {

    ImGuiMenuColumns() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("TotalWidth"),
        imgui.C_INT.withName("NextTotalWidth"),
        imgui.C_SHORT.withName("Spacing"),
        imgui.C_SHORT.withName("OffsetIcon"),
        imgui.C_SHORT.withName("OffsetLabel"),
        imgui.C_SHORT.withName("OffsetShortcut"),
        imgui.C_SHORT.withName("OffsetMark"),
        MemoryLayout.sequenceLayout(4, imgui.C_SHORT).withName("Widths"),
        MemoryLayout.paddingLayout(2)
    ).withName("ImGuiMenuColumns");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt TotalWidth$LAYOUT = (OfInt)$LAYOUT.select(groupElement("TotalWidth"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU32 TotalWidth
     * }
     */
    public static final OfInt TotalWidth$layout() {
        return TotalWidth$LAYOUT;
    }

    private static final long TotalWidth$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU32 TotalWidth
     * }
     */
    public static final long TotalWidth$offset() {
        return TotalWidth$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU32 TotalWidth
     * }
     */
    public static int TotalWidth(MemorySegment struct) {
        return struct.get(TotalWidth$LAYOUT, TotalWidth$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU32 TotalWidth
     * }
     */
    public static void TotalWidth(MemorySegment struct, int fieldValue) {
        struct.set(TotalWidth$LAYOUT, TotalWidth$OFFSET, fieldValue);
    }

    private static final OfInt NextTotalWidth$LAYOUT = (OfInt)$LAYOUT.select(groupElement("NextTotalWidth"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU32 NextTotalWidth
     * }
     */
    public static final OfInt NextTotalWidth$layout() {
        return NextTotalWidth$LAYOUT;
    }

    private static final long NextTotalWidth$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU32 NextTotalWidth
     * }
     */
    public static final long NextTotalWidth$offset() {
        return NextTotalWidth$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU32 NextTotalWidth
     * }
     */
    public static int NextTotalWidth(MemorySegment struct) {
        return struct.get(NextTotalWidth$LAYOUT, NextTotalWidth$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU32 NextTotalWidth
     * }
     */
    public static void NextTotalWidth(MemorySegment struct, int fieldValue) {
        struct.set(NextTotalWidth$LAYOUT, NextTotalWidth$OFFSET, fieldValue);
    }

    private static final OfShort Spacing$LAYOUT = (OfShort)$LAYOUT.select(groupElement("Spacing"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 Spacing
     * }
     */
    public static final OfShort Spacing$layout() {
        return Spacing$LAYOUT;
    }

    private static final long Spacing$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 Spacing
     * }
     */
    public static final long Spacing$offset() {
        return Spacing$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 Spacing
     * }
     */
    public static short Spacing(MemorySegment struct) {
        return struct.get(Spacing$LAYOUT, Spacing$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 Spacing
     * }
     */
    public static void Spacing(MemorySegment struct, short fieldValue) {
        struct.set(Spacing$LAYOUT, Spacing$OFFSET, fieldValue);
    }

    private static final OfShort OffsetIcon$LAYOUT = (OfShort)$LAYOUT.select(groupElement("OffsetIcon"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 OffsetIcon
     * }
     */
    public static final OfShort OffsetIcon$layout() {
        return OffsetIcon$LAYOUT;
    }

    private static final long OffsetIcon$OFFSET = 10;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 OffsetIcon
     * }
     */
    public static final long OffsetIcon$offset() {
        return OffsetIcon$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 OffsetIcon
     * }
     */
    public static short OffsetIcon(MemorySegment struct) {
        return struct.get(OffsetIcon$LAYOUT, OffsetIcon$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 OffsetIcon
     * }
     */
    public static void OffsetIcon(MemorySegment struct, short fieldValue) {
        struct.set(OffsetIcon$LAYOUT, OffsetIcon$OFFSET, fieldValue);
    }

    private static final OfShort OffsetLabel$LAYOUT = (OfShort)$LAYOUT.select(groupElement("OffsetLabel"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 OffsetLabel
     * }
     */
    public static final OfShort OffsetLabel$layout() {
        return OffsetLabel$LAYOUT;
    }

    private static final long OffsetLabel$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 OffsetLabel
     * }
     */
    public static final long OffsetLabel$offset() {
        return OffsetLabel$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 OffsetLabel
     * }
     */
    public static short OffsetLabel(MemorySegment struct) {
        return struct.get(OffsetLabel$LAYOUT, OffsetLabel$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 OffsetLabel
     * }
     */
    public static void OffsetLabel(MemorySegment struct, short fieldValue) {
        struct.set(OffsetLabel$LAYOUT, OffsetLabel$OFFSET, fieldValue);
    }

    private static final OfShort OffsetShortcut$LAYOUT = (OfShort)$LAYOUT.select(groupElement("OffsetShortcut"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 OffsetShortcut
     * }
     */
    public static final OfShort OffsetShortcut$layout() {
        return OffsetShortcut$LAYOUT;
    }

    private static final long OffsetShortcut$OFFSET = 14;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 OffsetShortcut
     * }
     */
    public static final long OffsetShortcut$offset() {
        return OffsetShortcut$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 OffsetShortcut
     * }
     */
    public static short OffsetShortcut(MemorySegment struct) {
        return struct.get(OffsetShortcut$LAYOUT, OffsetShortcut$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 OffsetShortcut
     * }
     */
    public static void OffsetShortcut(MemorySegment struct, short fieldValue) {
        struct.set(OffsetShortcut$LAYOUT, OffsetShortcut$OFFSET, fieldValue);
    }

    private static final OfShort OffsetMark$LAYOUT = (OfShort)$LAYOUT.select(groupElement("OffsetMark"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 OffsetMark
     * }
     */
    public static final OfShort OffsetMark$layout() {
        return OffsetMark$LAYOUT;
    }

    private static final long OffsetMark$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 OffsetMark
     * }
     */
    public static final long OffsetMark$offset() {
        return OffsetMark$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 OffsetMark
     * }
     */
    public static short OffsetMark(MemorySegment struct) {
        return struct.get(OffsetMark$LAYOUT, OffsetMark$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 OffsetMark
     * }
     */
    public static void OffsetMark(MemorySegment struct, short fieldValue) {
        struct.set(OffsetMark$LAYOUT, OffsetMark$OFFSET, fieldValue);
    }

    private static final SequenceLayout Widths$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("Widths"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static final SequenceLayout Widths$layout() {
        return Widths$LAYOUT;
    }

    private static final long Widths$OFFSET = 18;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static final long Widths$offset() {
        return Widths$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static MemorySegment Widths(MemorySegment struct) {
        return struct.asSlice(Widths$OFFSET, Widths$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static void Widths(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Widths$OFFSET, Widths$LAYOUT.byteSize());
    }

    private static long[] Widths$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static long[] Widths$dimensions() {
        return Widths$DIMS;
    }
    private static final VarHandle Widths$ELEM_HANDLE = Widths$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static short Widths(MemorySegment struct, long index0) {
        return (short)Widths$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ImU16 Widths[4]
     * }
     */
    public static void Widths(MemorySegment struct, long index0, short fieldValue) {
        Widths$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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
