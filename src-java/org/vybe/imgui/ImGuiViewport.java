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
 * struct ImGuiViewport {
 *     ImGuiID ID;
 *     ImGuiViewportFlags Flags;
 *     ImVec2 Pos;
 *     ImVec2 Size;
 *     ImVec2 WorkPos;
 *     ImVec2 WorkSize;
 *     float DpiScale;
 *     ImGuiID ParentViewportId;
 *     ImDrawData *DrawData;
 *     void *RendererUserData;
 *     void *PlatformUserData;
 *     void *PlatformHandle;
 *     void *PlatformHandleRaw;
 *     bool PlatformWindowCreated;
 *     bool PlatformRequestMove;
 *     bool PlatformRequestResize;
 *     bool PlatformRequestClose;
 * }
 * }
 */
public class ImGuiViewport {

    ImGuiViewport() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("Flags"),
        ImVec2.layout().withName("Pos"),
        ImVec2.layout().withName("Size"),
        ImVec2.layout().withName("WorkPos"),
        ImVec2.layout().withName("WorkSize"),
        imgui.C_FLOAT.withName("DpiScale"),
        imgui.C_INT.withName("ParentViewportId"),
        imgui.C_POINTER.withName("DrawData"),
        imgui.C_POINTER.withName("RendererUserData"),
        imgui.C_POINTER.withName("PlatformUserData"),
        imgui.C_POINTER.withName("PlatformHandle"),
        imgui.C_POINTER.withName("PlatformHandleRaw"),
        imgui.C_BOOL.withName("PlatformWindowCreated"),
        imgui.C_BOOL.withName("PlatformRequestMove"),
        imgui.C_BOOL.withName("PlatformRequestResize"),
        imgui.C_BOOL.withName("PlatformRequestClose"),
        MemoryLayout.paddingLayout(4)
    ).withName("ImGuiViewport");

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
     * ImGuiViewportFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiViewportFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiViewportFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiViewportFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
    }

    private static final GroupLayout Pos$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("Pos"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 Pos
     * }
     */
    public static final GroupLayout Pos$layout() {
        return Pos$LAYOUT;
    }

    private static final long Pos$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 Pos
     * }
     */
    public static final long Pos$offset() {
        return Pos$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 Pos
     * }
     */
    public static MemorySegment Pos(MemorySegment struct) {
        return struct.asSlice(Pos$OFFSET, Pos$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 Pos
     * }
     */
    public static void Pos(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Pos$OFFSET, Pos$LAYOUT.byteSize());
    }

    private static final GroupLayout Size$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("Size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 Size
     * }
     */
    public static final GroupLayout Size$layout() {
        return Size$LAYOUT;
    }

    private static final long Size$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 Size
     * }
     */
    public static final long Size$offset() {
        return Size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 Size
     * }
     */
    public static MemorySegment Size(MemorySegment struct) {
        return struct.asSlice(Size$OFFSET, Size$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 Size
     * }
     */
    public static void Size(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Size$OFFSET, Size$LAYOUT.byteSize());
    }

    private static final GroupLayout WorkPos$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("WorkPos"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static final GroupLayout WorkPos$layout() {
        return WorkPos$LAYOUT;
    }

    private static final long WorkPos$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static final long WorkPos$offset() {
        return WorkPos$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static MemorySegment WorkPos(MemorySegment struct) {
        return struct.asSlice(WorkPos$OFFSET, WorkPos$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static void WorkPos(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, WorkPos$OFFSET, WorkPos$LAYOUT.byteSize());
    }

    private static final GroupLayout WorkSize$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("WorkSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static final GroupLayout WorkSize$layout() {
        return WorkSize$LAYOUT;
    }

    private static final long WorkSize$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static final long WorkSize$offset() {
        return WorkSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static MemorySegment WorkSize(MemorySegment struct) {
        return struct.asSlice(WorkSize$OFFSET, WorkSize$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static void WorkSize(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, WorkSize$OFFSET, WorkSize$LAYOUT.byteSize());
    }

    private static final OfFloat DpiScale$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("DpiScale"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static final OfFloat DpiScale$layout() {
        return DpiScale$LAYOUT;
    }

    private static final long DpiScale$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static final long DpiScale$offset() {
        return DpiScale$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static float DpiScale(MemorySegment struct) {
        return struct.get(DpiScale$LAYOUT, DpiScale$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static void DpiScale(MemorySegment struct, float fieldValue) {
        struct.set(DpiScale$LAYOUT, DpiScale$OFFSET, fieldValue);
    }

    private static final OfInt ParentViewportId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ParentViewportId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID ParentViewportId
     * }
     */
    public static final OfInt ParentViewportId$layout() {
        return ParentViewportId$LAYOUT;
    }

    private static final long ParentViewportId$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID ParentViewportId
     * }
     */
    public static final long ParentViewportId$offset() {
        return ParentViewportId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID ParentViewportId
     * }
     */
    public static int ParentViewportId(MemorySegment struct) {
        return struct.get(ParentViewportId$LAYOUT, ParentViewportId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID ParentViewportId
     * }
     */
    public static void ParentViewportId(MemorySegment struct, int fieldValue) {
        struct.set(ParentViewportId$LAYOUT, ParentViewportId$OFFSET, fieldValue);
    }

    private static final AddressLayout DrawData$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("DrawData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImDrawData *DrawData
     * }
     */
    public static final AddressLayout DrawData$layout() {
        return DrawData$LAYOUT;
    }

    private static final long DrawData$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImDrawData *DrawData
     * }
     */
    public static final long DrawData$offset() {
        return DrawData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImDrawData *DrawData
     * }
     */
    public static MemorySegment DrawData(MemorySegment struct) {
        return struct.get(DrawData$LAYOUT, DrawData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImDrawData *DrawData
     * }
     */
    public static void DrawData(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(DrawData$LAYOUT, DrawData$OFFSET, fieldValue);
    }

    private static final AddressLayout RendererUserData$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("RendererUserData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *RendererUserData
     * }
     */
    public static final AddressLayout RendererUserData$layout() {
        return RendererUserData$LAYOUT;
    }

    private static final long RendererUserData$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *RendererUserData
     * }
     */
    public static final long RendererUserData$offset() {
        return RendererUserData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *RendererUserData
     * }
     */
    public static MemorySegment RendererUserData(MemorySegment struct) {
        return struct.get(RendererUserData$LAYOUT, RendererUserData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *RendererUserData
     * }
     */
    public static void RendererUserData(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(RendererUserData$LAYOUT, RendererUserData$OFFSET, fieldValue);
    }

    private static final AddressLayout PlatformUserData$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("PlatformUserData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *PlatformUserData
     * }
     */
    public static final AddressLayout PlatformUserData$layout() {
        return PlatformUserData$LAYOUT;
    }

    private static final long PlatformUserData$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *PlatformUserData
     * }
     */
    public static final long PlatformUserData$offset() {
        return PlatformUserData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *PlatformUserData
     * }
     */
    public static MemorySegment PlatformUserData(MemorySegment struct) {
        return struct.get(PlatformUserData$LAYOUT, PlatformUserData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *PlatformUserData
     * }
     */
    public static void PlatformUserData(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(PlatformUserData$LAYOUT, PlatformUserData$OFFSET, fieldValue);
    }

    private static final AddressLayout PlatformHandle$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("PlatformHandle"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static final AddressLayout PlatformHandle$layout() {
        return PlatformHandle$LAYOUT;
    }

    private static final long PlatformHandle$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static final long PlatformHandle$offset() {
        return PlatformHandle$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static MemorySegment PlatformHandle(MemorySegment struct) {
        return struct.get(PlatformHandle$LAYOUT, PlatformHandle$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static void PlatformHandle(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(PlatformHandle$LAYOUT, PlatformHandle$OFFSET, fieldValue);
    }

    private static final AddressLayout PlatformHandleRaw$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("PlatformHandleRaw"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *PlatformHandleRaw
     * }
     */
    public static final AddressLayout PlatformHandleRaw$layout() {
        return PlatformHandleRaw$LAYOUT;
    }

    private static final long PlatformHandleRaw$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *PlatformHandleRaw
     * }
     */
    public static final long PlatformHandleRaw$offset() {
        return PlatformHandleRaw$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *PlatformHandleRaw
     * }
     */
    public static MemorySegment PlatformHandleRaw(MemorySegment struct) {
        return struct.get(PlatformHandleRaw$LAYOUT, PlatformHandleRaw$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *PlatformHandleRaw
     * }
     */
    public static void PlatformHandleRaw(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(PlatformHandleRaw$LAYOUT, PlatformHandleRaw$OFFSET, fieldValue);
    }

    private static final OfBoolean PlatformWindowCreated$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("PlatformWindowCreated"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool PlatformWindowCreated
     * }
     */
    public static final OfBoolean PlatformWindowCreated$layout() {
        return PlatformWindowCreated$LAYOUT;
    }

    private static final long PlatformWindowCreated$OFFSET = 88;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool PlatformWindowCreated
     * }
     */
    public static final long PlatformWindowCreated$offset() {
        return PlatformWindowCreated$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool PlatformWindowCreated
     * }
     */
    public static boolean PlatformWindowCreated(MemorySegment struct) {
        return struct.get(PlatformWindowCreated$LAYOUT, PlatformWindowCreated$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool PlatformWindowCreated
     * }
     */
    public static void PlatformWindowCreated(MemorySegment struct, boolean fieldValue) {
        struct.set(PlatformWindowCreated$LAYOUT, PlatformWindowCreated$OFFSET, fieldValue);
    }

    private static final OfBoolean PlatformRequestMove$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("PlatformRequestMove"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool PlatformRequestMove
     * }
     */
    public static final OfBoolean PlatformRequestMove$layout() {
        return PlatformRequestMove$LAYOUT;
    }

    private static final long PlatformRequestMove$OFFSET = 89;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool PlatformRequestMove
     * }
     */
    public static final long PlatformRequestMove$offset() {
        return PlatformRequestMove$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool PlatformRequestMove
     * }
     */
    public static boolean PlatformRequestMove(MemorySegment struct) {
        return struct.get(PlatformRequestMove$LAYOUT, PlatformRequestMove$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool PlatformRequestMove
     * }
     */
    public static void PlatformRequestMove(MemorySegment struct, boolean fieldValue) {
        struct.set(PlatformRequestMove$LAYOUT, PlatformRequestMove$OFFSET, fieldValue);
    }

    private static final OfBoolean PlatformRequestResize$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("PlatformRequestResize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool PlatformRequestResize
     * }
     */
    public static final OfBoolean PlatformRequestResize$layout() {
        return PlatformRequestResize$LAYOUT;
    }

    private static final long PlatformRequestResize$OFFSET = 90;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool PlatformRequestResize
     * }
     */
    public static final long PlatformRequestResize$offset() {
        return PlatformRequestResize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool PlatformRequestResize
     * }
     */
    public static boolean PlatformRequestResize(MemorySegment struct) {
        return struct.get(PlatformRequestResize$LAYOUT, PlatformRequestResize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool PlatformRequestResize
     * }
     */
    public static void PlatformRequestResize(MemorySegment struct, boolean fieldValue) {
        struct.set(PlatformRequestResize$LAYOUT, PlatformRequestResize$OFFSET, fieldValue);
    }

    private static final OfBoolean PlatformRequestClose$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("PlatformRequestClose"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool PlatformRequestClose
     * }
     */
    public static final OfBoolean PlatformRequestClose$layout() {
        return PlatformRequestClose$LAYOUT;
    }

    private static final long PlatformRequestClose$OFFSET = 91;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool PlatformRequestClose
     * }
     */
    public static final long PlatformRequestClose$offset() {
        return PlatformRequestClose$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool PlatformRequestClose
     * }
     */
    public static boolean PlatformRequestClose(MemorySegment struct) {
        return struct.get(PlatformRequestClose$LAYOUT, PlatformRequestClose$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool PlatformRequestClose
     * }
     */
    public static void PlatformRequestClose(MemorySegment struct, boolean fieldValue) {
        struct.set(PlatformRequestClose$LAYOUT, PlatformRequestClose$OFFSET, fieldValue);
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

