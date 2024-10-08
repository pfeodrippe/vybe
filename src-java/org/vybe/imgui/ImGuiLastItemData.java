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
 * struct ImGuiLastItemData {
 *     ImGuiID ID;
 *     ImGuiItemFlags InFlags;
 *     ImGuiItemStatusFlags StatusFlags;
 *     ImRect Rect;
 *     ImRect NavRect;
 *     ImRect DisplayRect;
 *     ImRect ClipRect;
 *     ImGuiKeyChord Shortcut;
 * }
 * }
 */
public class ImGuiLastItemData {

    ImGuiLastItemData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("InFlags"),
        imgui.C_INT.withName("StatusFlags"),
        ImRect.layout().withName("Rect"),
        ImRect.layout().withName("NavRect"),
        ImRect.layout().withName("DisplayRect"),
        ImRect.layout().withName("ClipRect"),
        imgui.C_INT.withName("Shortcut")
    ).withName("ImGuiLastItemData");

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

    private static final OfInt InFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("InFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiItemFlags InFlags
     * }
     */
    public static final OfInt InFlags$layout() {
        return InFlags$LAYOUT;
    }

    private static final long InFlags$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiItemFlags InFlags
     * }
     */
    public static final long InFlags$offset() {
        return InFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiItemFlags InFlags
     * }
     */
    public static int InFlags(MemorySegment struct) {
        return struct.get(InFlags$LAYOUT, InFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiItemFlags InFlags
     * }
     */
    public static void InFlags(MemorySegment struct, int fieldValue) {
        struct.set(InFlags$LAYOUT, InFlags$OFFSET, fieldValue);
    }

    private static final OfInt StatusFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("StatusFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiItemStatusFlags StatusFlags
     * }
     */
    public static final OfInt StatusFlags$layout() {
        return StatusFlags$LAYOUT;
    }

    private static final long StatusFlags$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiItemStatusFlags StatusFlags
     * }
     */
    public static final long StatusFlags$offset() {
        return StatusFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiItemStatusFlags StatusFlags
     * }
     */
    public static int StatusFlags(MemorySegment struct) {
        return struct.get(StatusFlags$LAYOUT, StatusFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiItemStatusFlags StatusFlags
     * }
     */
    public static void StatusFlags(MemorySegment struct, int fieldValue) {
        struct.set(StatusFlags$LAYOUT, StatusFlags$OFFSET, fieldValue);
    }

    private static final GroupLayout Rect$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("Rect"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImRect Rect
     * }
     */
    public static final GroupLayout Rect$layout() {
        return Rect$LAYOUT;
    }

    private static final long Rect$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImRect Rect
     * }
     */
    public static final long Rect$offset() {
        return Rect$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImRect Rect
     * }
     */
    public static MemorySegment Rect(MemorySegment struct) {
        return struct.asSlice(Rect$OFFSET, Rect$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImRect Rect
     * }
     */
    public static void Rect(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Rect$OFFSET, Rect$LAYOUT.byteSize());
    }

    private static final GroupLayout NavRect$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("NavRect"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImRect NavRect
     * }
     */
    public static final GroupLayout NavRect$layout() {
        return NavRect$LAYOUT;
    }

    private static final long NavRect$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImRect NavRect
     * }
     */
    public static final long NavRect$offset() {
        return NavRect$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImRect NavRect
     * }
     */
    public static MemorySegment NavRect(MemorySegment struct) {
        return struct.asSlice(NavRect$OFFSET, NavRect$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImRect NavRect
     * }
     */
    public static void NavRect(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, NavRect$OFFSET, NavRect$LAYOUT.byteSize());
    }

    private static final GroupLayout DisplayRect$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("DisplayRect"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImRect DisplayRect
     * }
     */
    public static final GroupLayout DisplayRect$layout() {
        return DisplayRect$LAYOUT;
    }

    private static final long DisplayRect$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImRect DisplayRect
     * }
     */
    public static final long DisplayRect$offset() {
        return DisplayRect$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImRect DisplayRect
     * }
     */
    public static MemorySegment DisplayRect(MemorySegment struct) {
        return struct.asSlice(DisplayRect$OFFSET, DisplayRect$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImRect DisplayRect
     * }
     */
    public static void DisplayRect(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, DisplayRect$OFFSET, DisplayRect$LAYOUT.byteSize());
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

    private static final long ClipRect$OFFSET = 60;

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

    private static final OfInt Shortcut$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Shortcut"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiKeyChord Shortcut
     * }
     */
    public static final OfInt Shortcut$layout() {
        return Shortcut$LAYOUT;
    }

    private static final long Shortcut$OFFSET = 76;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiKeyChord Shortcut
     * }
     */
    public static final long Shortcut$offset() {
        return Shortcut$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiKeyChord Shortcut
     * }
     */
    public static int Shortcut(MemorySegment struct) {
        return struct.get(Shortcut$LAYOUT, Shortcut$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiKeyChord Shortcut
     * }
     */
    public static void Shortcut(MemorySegment struct, int fieldValue) {
        struct.set(Shortcut$LAYOUT, Shortcut$OFFSET, fieldValue);
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

