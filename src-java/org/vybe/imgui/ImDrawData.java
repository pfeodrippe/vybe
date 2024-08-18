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
 * struct ImDrawData {
 *     bool Valid;
 *     int CmdListsCount;
 *     int TotalIdxCount;
 *     int TotalVtxCount;
 *     ImVector_ImDrawListPtr CmdLists;
 *     ImVec2 DisplayPos;
 *     ImVec2 DisplaySize;
 *     ImVec2 FramebufferScale;
 *     ImGuiViewport *OwnerViewport;
 * }
 * }
 */
public class ImDrawData {

    ImDrawData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_BOOL.withName("Valid"),
        MemoryLayout.paddingLayout(3),
        imgui.C_INT.withName("CmdListsCount"),
        imgui.C_INT.withName("TotalIdxCount"),
        imgui.C_INT.withName("TotalVtxCount"),
        ImVector_ImDrawListPtr.layout().withName("CmdLists"),
        ImVec2.layout().withName("DisplayPos"),
        ImVec2.layout().withName("DisplaySize"),
        ImVec2.layout().withName("FramebufferScale"),
        imgui.C_POINTER.withName("OwnerViewport")
    ).withName("ImDrawData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfBoolean Valid$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("Valid"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool Valid
     * }
     */
    public static final OfBoolean Valid$layout() {
        return Valid$LAYOUT;
    }

    private static final long Valid$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool Valid
     * }
     */
    public static final long Valid$offset() {
        return Valid$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool Valid
     * }
     */
    public static boolean Valid(MemorySegment struct) {
        return struct.get(Valid$LAYOUT, Valid$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool Valid
     * }
     */
    public static void Valid(MemorySegment struct, boolean fieldValue) {
        struct.set(Valid$LAYOUT, Valid$OFFSET, fieldValue);
    }

    private static final OfInt CmdListsCount$LAYOUT = (OfInt)$LAYOUT.select(groupElement("CmdListsCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int CmdListsCount
     * }
     */
    public static final OfInt CmdListsCount$layout() {
        return CmdListsCount$LAYOUT;
    }

    private static final long CmdListsCount$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int CmdListsCount
     * }
     */
    public static final long CmdListsCount$offset() {
        return CmdListsCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int CmdListsCount
     * }
     */
    public static int CmdListsCount(MemorySegment struct) {
        return struct.get(CmdListsCount$LAYOUT, CmdListsCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int CmdListsCount
     * }
     */
    public static void CmdListsCount(MemorySegment struct, int fieldValue) {
        struct.set(CmdListsCount$LAYOUT, CmdListsCount$OFFSET, fieldValue);
    }

    private static final OfInt TotalIdxCount$LAYOUT = (OfInt)$LAYOUT.select(groupElement("TotalIdxCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int TotalIdxCount
     * }
     */
    public static final OfInt TotalIdxCount$layout() {
        return TotalIdxCount$LAYOUT;
    }

    private static final long TotalIdxCount$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int TotalIdxCount
     * }
     */
    public static final long TotalIdxCount$offset() {
        return TotalIdxCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int TotalIdxCount
     * }
     */
    public static int TotalIdxCount(MemorySegment struct) {
        return struct.get(TotalIdxCount$LAYOUT, TotalIdxCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int TotalIdxCount
     * }
     */
    public static void TotalIdxCount(MemorySegment struct, int fieldValue) {
        struct.set(TotalIdxCount$LAYOUT, TotalIdxCount$OFFSET, fieldValue);
    }

    private static final OfInt TotalVtxCount$LAYOUT = (OfInt)$LAYOUT.select(groupElement("TotalVtxCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int TotalVtxCount
     * }
     */
    public static final OfInt TotalVtxCount$layout() {
        return TotalVtxCount$LAYOUT;
    }

    private static final long TotalVtxCount$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int TotalVtxCount
     * }
     */
    public static final long TotalVtxCount$offset() {
        return TotalVtxCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int TotalVtxCount
     * }
     */
    public static int TotalVtxCount(MemorySegment struct) {
        return struct.get(TotalVtxCount$LAYOUT, TotalVtxCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int TotalVtxCount
     * }
     */
    public static void TotalVtxCount(MemorySegment struct, int fieldValue) {
        struct.set(TotalVtxCount$LAYOUT, TotalVtxCount$OFFSET, fieldValue);
    }

    private static final GroupLayout CmdLists$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("CmdLists"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_ImDrawListPtr CmdLists
     * }
     */
    public static final GroupLayout CmdLists$layout() {
        return CmdLists$LAYOUT;
    }

    private static final long CmdLists$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_ImDrawListPtr CmdLists
     * }
     */
    public static final long CmdLists$offset() {
        return CmdLists$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_ImDrawListPtr CmdLists
     * }
     */
    public static MemorySegment CmdLists(MemorySegment struct) {
        return struct.asSlice(CmdLists$OFFSET, CmdLists$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_ImDrawListPtr CmdLists
     * }
     */
    public static void CmdLists(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, CmdLists$OFFSET, CmdLists$LAYOUT.byteSize());
    }

    private static final GroupLayout DisplayPos$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("DisplayPos"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 DisplayPos
     * }
     */
    public static final GroupLayout DisplayPos$layout() {
        return DisplayPos$LAYOUT;
    }

    private static final long DisplayPos$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 DisplayPos
     * }
     */
    public static final long DisplayPos$offset() {
        return DisplayPos$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 DisplayPos
     * }
     */
    public static MemorySegment DisplayPos(MemorySegment struct) {
        return struct.asSlice(DisplayPos$OFFSET, DisplayPos$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 DisplayPos
     * }
     */
    public static void DisplayPos(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, DisplayPos$OFFSET, DisplayPos$LAYOUT.byteSize());
    }

    private static final GroupLayout DisplaySize$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("DisplaySize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 DisplaySize
     * }
     */
    public static final GroupLayout DisplaySize$layout() {
        return DisplaySize$LAYOUT;
    }

    private static final long DisplaySize$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 DisplaySize
     * }
     */
    public static final long DisplaySize$offset() {
        return DisplaySize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 DisplaySize
     * }
     */
    public static MemorySegment DisplaySize(MemorySegment struct) {
        return struct.asSlice(DisplaySize$OFFSET, DisplaySize$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 DisplaySize
     * }
     */
    public static void DisplaySize(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, DisplaySize$OFFSET, DisplaySize$LAYOUT.byteSize());
    }

    private static final GroupLayout FramebufferScale$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("FramebufferScale"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 FramebufferScale
     * }
     */
    public static final GroupLayout FramebufferScale$layout() {
        return FramebufferScale$LAYOUT;
    }

    private static final long FramebufferScale$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 FramebufferScale
     * }
     */
    public static final long FramebufferScale$offset() {
        return FramebufferScale$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 FramebufferScale
     * }
     */
    public static MemorySegment FramebufferScale(MemorySegment struct) {
        return struct.asSlice(FramebufferScale$OFFSET, FramebufferScale$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 FramebufferScale
     * }
     */
    public static void FramebufferScale(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, FramebufferScale$OFFSET, FramebufferScale$LAYOUT.byteSize());
    }

    private static final AddressLayout OwnerViewport$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("OwnerViewport"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiViewport *OwnerViewport
     * }
     */
    public static final AddressLayout OwnerViewport$layout() {
        return OwnerViewport$LAYOUT;
    }

    private static final long OwnerViewport$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiViewport *OwnerViewport
     * }
     */
    public static final long OwnerViewport$offset() {
        return OwnerViewport$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiViewport *OwnerViewport
     * }
     */
    public static MemorySegment OwnerViewport(MemorySegment struct) {
        return struct.get(OwnerViewport$LAYOUT, OwnerViewport$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiViewport *OwnerViewport
     * }
     */
    public static void OwnerViewport(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(OwnerViewport$LAYOUT, OwnerViewport$OFFSET, fieldValue);
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
