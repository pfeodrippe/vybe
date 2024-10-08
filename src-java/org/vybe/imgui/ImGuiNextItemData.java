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
 * struct ImGuiNextItemData {
 *     ImGuiNextItemDataFlags Flags;
 *     ImGuiItemFlags ItemFlags;
 *     ImGuiID FocusScopeId;
 *     ImGuiSelectionUserData SelectionUserData;
 *     float Width;
 *     ImGuiKeyChord Shortcut;
 *     ImGuiInputFlags ShortcutFlags;
 *     bool OpenVal;
 *     ImU8 OpenCond;
 *     ImGuiDataTypeStorage RefVal;
 *     ImGuiID StorageId;
 * }
 * }
 */
public class ImGuiNextItemData {

    ImGuiNextItemData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Flags"),
        imgui.C_INT.withName("ItemFlags"),
        imgui.C_INT.withName("FocusScopeId"),
        MemoryLayout.paddingLayout(4),
        imgui.C_LONG_LONG.withName("SelectionUserData"),
        imgui.C_FLOAT.withName("Width"),
        imgui.C_INT.withName("Shortcut"),
        imgui.C_INT.withName("ShortcutFlags"),
        imgui.C_BOOL.withName("OpenVal"),
        imgui.C_CHAR.withName("OpenCond"),
        ImGuiDataTypeStorage.layout().withName("RefVal"),
        MemoryLayout.paddingLayout(2),
        imgui.C_INT.withName("StorageId"),
        MemoryLayout.paddingLayout(4)
    ).withName("ImGuiNextItemData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt Flags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Flags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiNextItemDataFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiNextItemDataFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiNextItemDataFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiNextItemDataFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
    }

    private static final OfInt ItemFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ItemFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiItemFlags ItemFlags
     * }
     */
    public static final OfInt ItemFlags$layout() {
        return ItemFlags$LAYOUT;
    }

    private static final long ItemFlags$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiItemFlags ItemFlags
     * }
     */
    public static final long ItemFlags$offset() {
        return ItemFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiItemFlags ItemFlags
     * }
     */
    public static int ItemFlags(MemorySegment struct) {
        return struct.get(ItemFlags$LAYOUT, ItemFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiItemFlags ItemFlags
     * }
     */
    public static void ItemFlags(MemorySegment struct, int fieldValue) {
        struct.set(ItemFlags$LAYOUT, ItemFlags$OFFSET, fieldValue);
    }

    private static final OfInt FocusScopeId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("FocusScopeId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID FocusScopeId
     * }
     */
    public static final OfInt FocusScopeId$layout() {
        return FocusScopeId$LAYOUT;
    }

    private static final long FocusScopeId$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID FocusScopeId
     * }
     */
    public static final long FocusScopeId$offset() {
        return FocusScopeId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID FocusScopeId
     * }
     */
    public static int FocusScopeId(MemorySegment struct) {
        return struct.get(FocusScopeId$LAYOUT, FocusScopeId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID FocusScopeId
     * }
     */
    public static void FocusScopeId(MemorySegment struct, int fieldValue) {
        struct.set(FocusScopeId$LAYOUT, FocusScopeId$OFFSET, fieldValue);
    }

    private static final OfLong SelectionUserData$LAYOUT = (OfLong)$LAYOUT.select(groupElement("SelectionUserData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiSelectionUserData SelectionUserData
     * }
     */
    public static final OfLong SelectionUserData$layout() {
        return SelectionUserData$LAYOUT;
    }

    private static final long SelectionUserData$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiSelectionUserData SelectionUserData
     * }
     */
    public static final long SelectionUserData$offset() {
        return SelectionUserData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiSelectionUserData SelectionUserData
     * }
     */
    public static long SelectionUserData(MemorySegment struct) {
        return struct.get(SelectionUserData$LAYOUT, SelectionUserData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiSelectionUserData SelectionUserData
     * }
     */
    public static void SelectionUserData(MemorySegment struct, long fieldValue) {
        struct.set(SelectionUserData$LAYOUT, SelectionUserData$OFFSET, fieldValue);
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

    private static final long Width$OFFSET = 24;

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

    private static final long Shortcut$OFFSET = 28;

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

    private static final OfInt ShortcutFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ShortcutFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiInputFlags ShortcutFlags
     * }
     */
    public static final OfInt ShortcutFlags$layout() {
        return ShortcutFlags$LAYOUT;
    }

    private static final long ShortcutFlags$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiInputFlags ShortcutFlags
     * }
     */
    public static final long ShortcutFlags$offset() {
        return ShortcutFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiInputFlags ShortcutFlags
     * }
     */
    public static int ShortcutFlags(MemorySegment struct) {
        return struct.get(ShortcutFlags$LAYOUT, ShortcutFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiInputFlags ShortcutFlags
     * }
     */
    public static void ShortcutFlags(MemorySegment struct, int fieldValue) {
        struct.set(ShortcutFlags$LAYOUT, ShortcutFlags$OFFSET, fieldValue);
    }

    private static final OfBoolean OpenVal$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("OpenVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool OpenVal
     * }
     */
    public static final OfBoolean OpenVal$layout() {
        return OpenVal$LAYOUT;
    }

    private static final long OpenVal$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool OpenVal
     * }
     */
    public static final long OpenVal$offset() {
        return OpenVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool OpenVal
     * }
     */
    public static boolean OpenVal(MemorySegment struct) {
        return struct.get(OpenVal$LAYOUT, OpenVal$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool OpenVal
     * }
     */
    public static void OpenVal(MemorySegment struct, boolean fieldValue) {
        struct.set(OpenVal$LAYOUT, OpenVal$OFFSET, fieldValue);
    }

    private static final OfByte OpenCond$LAYOUT = (OfByte)$LAYOUT.select(groupElement("OpenCond"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU8 OpenCond
     * }
     */
    public static final OfByte OpenCond$layout() {
        return OpenCond$LAYOUT;
    }

    private static final long OpenCond$OFFSET = 37;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU8 OpenCond
     * }
     */
    public static final long OpenCond$offset() {
        return OpenCond$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU8 OpenCond
     * }
     */
    public static byte OpenCond(MemorySegment struct) {
        return struct.get(OpenCond$LAYOUT, OpenCond$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU8 OpenCond
     * }
     */
    public static void OpenCond(MemorySegment struct, byte fieldValue) {
        struct.set(OpenCond$LAYOUT, OpenCond$OFFSET, fieldValue);
    }

    private static final GroupLayout RefVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("RefVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiDataTypeStorage RefVal
     * }
     */
    public static final GroupLayout RefVal$layout() {
        return RefVal$LAYOUT;
    }

    private static final long RefVal$OFFSET = 38;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiDataTypeStorage RefVal
     * }
     */
    public static final long RefVal$offset() {
        return RefVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiDataTypeStorage RefVal
     * }
     */
    public static MemorySegment RefVal(MemorySegment struct) {
        return struct.asSlice(RefVal$OFFSET, RefVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiDataTypeStorage RefVal
     * }
     */
    public static void RefVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, RefVal$OFFSET, RefVal$LAYOUT.byteSize());
    }

    private static final OfInt StorageId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("StorageId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID StorageId
     * }
     */
    public static final OfInt StorageId$layout() {
        return StorageId$LAYOUT;
    }

    private static final long StorageId$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID StorageId
     * }
     */
    public static final long StorageId$offset() {
        return StorageId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID StorageId
     * }
     */
    public static int StorageId(MemorySegment struct) {
        return struct.get(StorageId$LAYOUT, StorageId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID StorageId
     * }
     */
    public static void StorageId(MemorySegment struct, int fieldValue) {
        struct.set(StorageId$LAYOUT, StorageId$OFFSET, fieldValue);
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

