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
 * struct ImGuiTabItem {
 *     ImGuiID ID;
 *     ImGuiTabItemFlags Flags;
 *     ImGuiWindow *Window;
 *     int LastFrameVisible;
 *     int LastFrameSelected;
 *     float Offset;
 *     float Width;
 *     float ContentWidth;
 *     float RequestedWidth;
 *     ImS32 NameOffset;
 *     ImS16 BeginOrder;
 *     ImS16 IndexDuringLayout;
 *     bool WantClose;
 * }
 * }
 */
public class ImGuiTabItem {

    ImGuiTabItem() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("Flags"),
        imgui.C_POINTER.withName("Window"),
        imgui.C_INT.withName("LastFrameVisible"),
        imgui.C_INT.withName("LastFrameSelected"),
        imgui.C_FLOAT.withName("Offset"),
        imgui.C_FLOAT.withName("Width"),
        imgui.C_FLOAT.withName("ContentWidth"),
        imgui.C_FLOAT.withName("RequestedWidth"),
        imgui.C_INT.withName("NameOffset"),
        imgui.C_SHORT.withName("BeginOrder"),
        imgui.C_SHORT.withName("IndexDuringLayout"),
        imgui.C_BOOL.withName("WantClose"),
        MemoryLayout.paddingLayout(7)
    ).withName("ImGuiTabItem");

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

    private static final OfInt Flags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Flags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTabItemFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTabItemFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTabItemFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTabItemFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
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

    private static final long Window$OFFSET = 8;

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

    private static final OfInt LastFrameVisible$LAYOUT = (OfInt)$LAYOUT.select(groupElement("LastFrameVisible"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int LastFrameVisible
     * }
     */
    public static final OfInt LastFrameVisible$layout() {
        return LastFrameVisible$LAYOUT;
    }

    private static final long LastFrameVisible$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int LastFrameVisible
     * }
     */
    public static final long LastFrameVisible$offset() {
        return LastFrameVisible$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int LastFrameVisible
     * }
     */
    public static int LastFrameVisible(MemorySegment struct) {
        return struct.get(LastFrameVisible$LAYOUT, LastFrameVisible$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int LastFrameVisible
     * }
     */
    public static void LastFrameVisible(MemorySegment struct, int fieldValue) {
        struct.set(LastFrameVisible$LAYOUT, LastFrameVisible$OFFSET, fieldValue);
    }

    private static final OfInt LastFrameSelected$LAYOUT = (OfInt)$LAYOUT.select(groupElement("LastFrameSelected"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int LastFrameSelected
     * }
     */
    public static final OfInt LastFrameSelected$layout() {
        return LastFrameSelected$LAYOUT;
    }

    private static final long LastFrameSelected$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int LastFrameSelected
     * }
     */
    public static final long LastFrameSelected$offset() {
        return LastFrameSelected$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int LastFrameSelected
     * }
     */
    public static int LastFrameSelected(MemorySegment struct) {
        return struct.get(LastFrameSelected$LAYOUT, LastFrameSelected$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int LastFrameSelected
     * }
     */
    public static void LastFrameSelected(MemorySegment struct, int fieldValue) {
        struct.set(LastFrameSelected$LAYOUT, LastFrameSelected$OFFSET, fieldValue);
    }

    private static final OfFloat Offset$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("Offset"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float Offset
     * }
     */
    public static final OfFloat Offset$layout() {
        return Offset$LAYOUT;
    }

    private static final long Offset$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float Offset
     * }
     */
    public static final long Offset$offset() {
        return Offset$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float Offset
     * }
     */
    public static float Offset(MemorySegment struct) {
        return struct.get(Offset$LAYOUT, Offset$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float Offset
     * }
     */
    public static void Offset(MemorySegment struct, float fieldValue) {
        struct.set(Offset$LAYOUT, Offset$OFFSET, fieldValue);
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

    private static final long Width$OFFSET = 28;

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

    private static final OfFloat ContentWidth$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("ContentWidth"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float ContentWidth
     * }
     */
    public static final OfFloat ContentWidth$layout() {
        return ContentWidth$LAYOUT;
    }

    private static final long ContentWidth$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float ContentWidth
     * }
     */
    public static final long ContentWidth$offset() {
        return ContentWidth$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float ContentWidth
     * }
     */
    public static float ContentWidth(MemorySegment struct) {
        return struct.get(ContentWidth$LAYOUT, ContentWidth$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float ContentWidth
     * }
     */
    public static void ContentWidth(MemorySegment struct, float fieldValue) {
        struct.set(ContentWidth$LAYOUT, ContentWidth$OFFSET, fieldValue);
    }

    private static final OfFloat RequestedWidth$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("RequestedWidth"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float RequestedWidth
     * }
     */
    public static final OfFloat RequestedWidth$layout() {
        return RequestedWidth$LAYOUT;
    }

    private static final long RequestedWidth$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float RequestedWidth
     * }
     */
    public static final long RequestedWidth$offset() {
        return RequestedWidth$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float RequestedWidth
     * }
     */
    public static float RequestedWidth(MemorySegment struct) {
        return struct.get(RequestedWidth$LAYOUT, RequestedWidth$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float RequestedWidth
     * }
     */
    public static void RequestedWidth(MemorySegment struct, float fieldValue) {
        struct.set(RequestedWidth$LAYOUT, RequestedWidth$OFFSET, fieldValue);
    }

    private static final OfInt NameOffset$LAYOUT = (OfInt)$LAYOUT.select(groupElement("NameOffset"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImS32 NameOffset
     * }
     */
    public static final OfInt NameOffset$layout() {
        return NameOffset$LAYOUT;
    }

    private static final long NameOffset$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImS32 NameOffset
     * }
     */
    public static final long NameOffset$offset() {
        return NameOffset$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImS32 NameOffset
     * }
     */
    public static int NameOffset(MemorySegment struct) {
        return struct.get(NameOffset$LAYOUT, NameOffset$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImS32 NameOffset
     * }
     */
    public static void NameOffset(MemorySegment struct, int fieldValue) {
        struct.set(NameOffset$LAYOUT, NameOffset$OFFSET, fieldValue);
    }

    private static final OfShort BeginOrder$LAYOUT = (OfShort)$LAYOUT.select(groupElement("BeginOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImS16 BeginOrder
     * }
     */
    public static final OfShort BeginOrder$layout() {
        return BeginOrder$LAYOUT;
    }

    private static final long BeginOrder$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImS16 BeginOrder
     * }
     */
    public static final long BeginOrder$offset() {
        return BeginOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImS16 BeginOrder
     * }
     */
    public static short BeginOrder(MemorySegment struct) {
        return struct.get(BeginOrder$LAYOUT, BeginOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImS16 BeginOrder
     * }
     */
    public static void BeginOrder(MemorySegment struct, short fieldValue) {
        struct.set(BeginOrder$LAYOUT, BeginOrder$OFFSET, fieldValue);
    }

    private static final OfShort IndexDuringLayout$LAYOUT = (OfShort)$LAYOUT.select(groupElement("IndexDuringLayout"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImS16 IndexDuringLayout
     * }
     */
    public static final OfShort IndexDuringLayout$layout() {
        return IndexDuringLayout$LAYOUT;
    }

    private static final long IndexDuringLayout$OFFSET = 46;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImS16 IndexDuringLayout
     * }
     */
    public static final long IndexDuringLayout$offset() {
        return IndexDuringLayout$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImS16 IndexDuringLayout
     * }
     */
    public static short IndexDuringLayout(MemorySegment struct) {
        return struct.get(IndexDuringLayout$LAYOUT, IndexDuringLayout$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImS16 IndexDuringLayout
     * }
     */
    public static void IndexDuringLayout(MemorySegment struct, short fieldValue) {
        struct.set(IndexDuringLayout$LAYOUT, IndexDuringLayout$OFFSET, fieldValue);
    }

    private static final OfBoolean WantClose$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("WantClose"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool WantClose
     * }
     */
    public static final OfBoolean WantClose$layout() {
        return WantClose$LAYOUT;
    }

    private static final long WantClose$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool WantClose
     * }
     */
    public static final long WantClose$offset() {
        return WantClose$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool WantClose
     * }
     */
    public static boolean WantClose(MemorySegment struct) {
        return struct.get(WantClose$LAYOUT, WantClose$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool WantClose
     * }
     */
    public static void WantClose(MemorySegment struct, boolean fieldValue) {
        struct.set(WantClose$LAYOUT, WantClose$OFFSET, fieldValue);
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
