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
 * struct ImGuiNavItemData {
 *     ImGuiWindow *Window;
 *     ImGuiID ID;
 *     ImGuiID FocusScopeId;
 *     ImRect RectRel;
 *     ImGuiItemFlags InFlags;
 *     float DistBox;
 *     float DistCenter;
 *     float DistAxial;
 *     ImGuiSelectionUserData SelectionUserData;
 * }
 * }
 */
public class ImGuiNavItemData {

    ImGuiNavItemData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("Window"),
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("FocusScopeId"),
        ImRect.layout().withName("RectRel"),
        imgui.C_INT.withName("InFlags"),
        imgui.C_FLOAT.withName("DistBox"),
        imgui.C_FLOAT.withName("DistCenter"),
        imgui.C_FLOAT.withName("DistAxial"),
        imgui.C_LONG_LONG.withName("SelectionUserData")
    ).withName("ImGuiNavItemData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout Window$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Window"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static final AddressLayout Window$layout() {
        return Window$LAYOUT;
    }

    private static final long Window$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static final long Window$offset() {
        return Window$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static MemorySegment Window(MemorySegment struct) {
        return struct.get(Window$LAYOUT, Window$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static void Window(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Window$LAYOUT, Window$OFFSET, fieldValue);
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

    private static final long ID$OFFSET = 8;

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

    private static final long FocusScopeId$OFFSET = 12;

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

    private static final GroupLayout RectRel$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("RectRel"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImRect RectRel
     * }
     */
    public static final GroupLayout RectRel$layout() {
        return RectRel$LAYOUT;
    }

    private static final long RectRel$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImRect RectRel
     * }
     */
    public static final long RectRel$offset() {
        return RectRel$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImRect RectRel
     * }
     */
    public static MemorySegment RectRel(MemorySegment struct) {
        return struct.asSlice(RectRel$OFFSET, RectRel$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImRect RectRel
     * }
     */
    public static void RectRel(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, RectRel$OFFSET, RectRel$LAYOUT.byteSize());
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

    private static final long InFlags$OFFSET = 32;

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

    private static final OfFloat DistBox$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("DistBox"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float DistBox
     * }
     */
    public static final OfFloat DistBox$layout() {
        return DistBox$LAYOUT;
    }

    private static final long DistBox$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float DistBox
     * }
     */
    public static final long DistBox$offset() {
        return DistBox$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float DistBox
     * }
     */
    public static float DistBox(MemorySegment struct) {
        return struct.get(DistBox$LAYOUT, DistBox$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float DistBox
     * }
     */
    public static void DistBox(MemorySegment struct, float fieldValue) {
        struct.set(DistBox$LAYOUT, DistBox$OFFSET, fieldValue);
    }

    private static final OfFloat DistCenter$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("DistCenter"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float DistCenter
     * }
     */
    public static final OfFloat DistCenter$layout() {
        return DistCenter$LAYOUT;
    }

    private static final long DistCenter$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float DistCenter
     * }
     */
    public static final long DistCenter$offset() {
        return DistCenter$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float DistCenter
     * }
     */
    public static float DistCenter(MemorySegment struct) {
        return struct.get(DistCenter$LAYOUT, DistCenter$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float DistCenter
     * }
     */
    public static void DistCenter(MemorySegment struct, float fieldValue) {
        struct.set(DistCenter$LAYOUT, DistCenter$OFFSET, fieldValue);
    }

    private static final OfFloat DistAxial$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("DistAxial"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float DistAxial
     * }
     */
    public static final OfFloat DistAxial$layout() {
        return DistAxial$LAYOUT;
    }

    private static final long DistAxial$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float DistAxial
     * }
     */
    public static final long DistAxial$offset() {
        return DistAxial$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float DistAxial
     * }
     */
    public static float DistAxial(MemorySegment struct) {
        return struct.get(DistAxial$LAYOUT, DistAxial$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float DistAxial
     * }
     */
    public static void DistAxial(MemorySegment struct, float fieldValue) {
        struct.set(DistAxial$LAYOUT, DistAxial$OFFSET, fieldValue);
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

    private static final long SelectionUserData$OFFSET = 48;

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
