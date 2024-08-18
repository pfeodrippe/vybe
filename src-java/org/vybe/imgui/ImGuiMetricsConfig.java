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
 * struct ImGuiMetricsConfig {
 *     bool ShowDebugLog;
 *     bool ShowIDStackTool;
 *     bool ShowWindowsRects;
 *     bool ShowWindowsBeginOrder;
 *     bool ShowTablesRects;
 *     bool ShowDrawCmdMesh;
 *     bool ShowDrawCmdBoundingBoxes;
 *     bool ShowTextEncodingViewer;
 *     bool ShowAtlasTintedWithTextColor;
 *     bool ShowDockingNodes;
 *     int ShowWindowsRectsType;
 *     int ShowTablesRectsType;
 *     int HighlightMonitorIdx;
 *     ImGuiID HighlightViewportID;
 * }
 * }
 */
public class ImGuiMetricsConfig {

    ImGuiMetricsConfig() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_BOOL.withName("ShowDebugLog"),
        imgui.C_BOOL.withName("ShowIDStackTool"),
        imgui.C_BOOL.withName("ShowWindowsRects"),
        imgui.C_BOOL.withName("ShowWindowsBeginOrder"),
        imgui.C_BOOL.withName("ShowTablesRects"),
        imgui.C_BOOL.withName("ShowDrawCmdMesh"),
        imgui.C_BOOL.withName("ShowDrawCmdBoundingBoxes"),
        imgui.C_BOOL.withName("ShowTextEncodingViewer"),
        imgui.C_BOOL.withName("ShowAtlasTintedWithTextColor"),
        imgui.C_BOOL.withName("ShowDockingNodes"),
        MemoryLayout.paddingLayout(2),
        imgui.C_INT.withName("ShowWindowsRectsType"),
        imgui.C_INT.withName("ShowTablesRectsType"),
        imgui.C_INT.withName("HighlightMonitorIdx"),
        imgui.C_INT.withName("HighlightViewportID")
    ).withName("ImGuiMetricsConfig");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfBoolean ShowDebugLog$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowDebugLog"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowDebugLog
     * }
     */
    public static final OfBoolean ShowDebugLog$layout() {
        return ShowDebugLog$LAYOUT;
    }

    private static final long ShowDebugLog$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowDebugLog
     * }
     */
    public static final long ShowDebugLog$offset() {
        return ShowDebugLog$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowDebugLog
     * }
     */
    public static boolean ShowDebugLog(MemorySegment struct) {
        return struct.get(ShowDebugLog$LAYOUT, ShowDebugLog$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowDebugLog
     * }
     */
    public static void ShowDebugLog(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowDebugLog$LAYOUT, ShowDebugLog$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowIDStackTool$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowIDStackTool"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowIDStackTool
     * }
     */
    public static final OfBoolean ShowIDStackTool$layout() {
        return ShowIDStackTool$LAYOUT;
    }

    private static final long ShowIDStackTool$OFFSET = 1;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowIDStackTool
     * }
     */
    public static final long ShowIDStackTool$offset() {
        return ShowIDStackTool$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowIDStackTool
     * }
     */
    public static boolean ShowIDStackTool(MemorySegment struct) {
        return struct.get(ShowIDStackTool$LAYOUT, ShowIDStackTool$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowIDStackTool
     * }
     */
    public static void ShowIDStackTool(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowIDStackTool$LAYOUT, ShowIDStackTool$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowWindowsRects$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowWindowsRects"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowWindowsRects
     * }
     */
    public static final OfBoolean ShowWindowsRects$layout() {
        return ShowWindowsRects$LAYOUT;
    }

    private static final long ShowWindowsRects$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowWindowsRects
     * }
     */
    public static final long ShowWindowsRects$offset() {
        return ShowWindowsRects$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowWindowsRects
     * }
     */
    public static boolean ShowWindowsRects(MemorySegment struct) {
        return struct.get(ShowWindowsRects$LAYOUT, ShowWindowsRects$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowWindowsRects
     * }
     */
    public static void ShowWindowsRects(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowWindowsRects$LAYOUT, ShowWindowsRects$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowWindowsBeginOrder$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowWindowsBeginOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowWindowsBeginOrder
     * }
     */
    public static final OfBoolean ShowWindowsBeginOrder$layout() {
        return ShowWindowsBeginOrder$LAYOUT;
    }

    private static final long ShowWindowsBeginOrder$OFFSET = 3;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowWindowsBeginOrder
     * }
     */
    public static final long ShowWindowsBeginOrder$offset() {
        return ShowWindowsBeginOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowWindowsBeginOrder
     * }
     */
    public static boolean ShowWindowsBeginOrder(MemorySegment struct) {
        return struct.get(ShowWindowsBeginOrder$LAYOUT, ShowWindowsBeginOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowWindowsBeginOrder
     * }
     */
    public static void ShowWindowsBeginOrder(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowWindowsBeginOrder$LAYOUT, ShowWindowsBeginOrder$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowTablesRects$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowTablesRects"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowTablesRects
     * }
     */
    public static final OfBoolean ShowTablesRects$layout() {
        return ShowTablesRects$LAYOUT;
    }

    private static final long ShowTablesRects$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowTablesRects
     * }
     */
    public static final long ShowTablesRects$offset() {
        return ShowTablesRects$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowTablesRects
     * }
     */
    public static boolean ShowTablesRects(MemorySegment struct) {
        return struct.get(ShowTablesRects$LAYOUT, ShowTablesRects$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowTablesRects
     * }
     */
    public static void ShowTablesRects(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowTablesRects$LAYOUT, ShowTablesRects$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowDrawCmdMesh$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowDrawCmdMesh"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdMesh
     * }
     */
    public static final OfBoolean ShowDrawCmdMesh$layout() {
        return ShowDrawCmdMesh$LAYOUT;
    }

    private static final long ShowDrawCmdMesh$OFFSET = 5;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdMesh
     * }
     */
    public static final long ShowDrawCmdMesh$offset() {
        return ShowDrawCmdMesh$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdMesh
     * }
     */
    public static boolean ShowDrawCmdMesh(MemorySegment struct) {
        return struct.get(ShowDrawCmdMesh$LAYOUT, ShowDrawCmdMesh$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdMesh
     * }
     */
    public static void ShowDrawCmdMesh(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowDrawCmdMesh$LAYOUT, ShowDrawCmdMesh$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowDrawCmdBoundingBoxes$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowDrawCmdBoundingBoxes"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdBoundingBoxes
     * }
     */
    public static final OfBoolean ShowDrawCmdBoundingBoxes$layout() {
        return ShowDrawCmdBoundingBoxes$LAYOUT;
    }

    private static final long ShowDrawCmdBoundingBoxes$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdBoundingBoxes
     * }
     */
    public static final long ShowDrawCmdBoundingBoxes$offset() {
        return ShowDrawCmdBoundingBoxes$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdBoundingBoxes
     * }
     */
    public static boolean ShowDrawCmdBoundingBoxes(MemorySegment struct) {
        return struct.get(ShowDrawCmdBoundingBoxes$LAYOUT, ShowDrawCmdBoundingBoxes$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowDrawCmdBoundingBoxes
     * }
     */
    public static void ShowDrawCmdBoundingBoxes(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowDrawCmdBoundingBoxes$LAYOUT, ShowDrawCmdBoundingBoxes$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowTextEncodingViewer$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowTextEncodingViewer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowTextEncodingViewer
     * }
     */
    public static final OfBoolean ShowTextEncodingViewer$layout() {
        return ShowTextEncodingViewer$LAYOUT;
    }

    private static final long ShowTextEncodingViewer$OFFSET = 7;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowTextEncodingViewer
     * }
     */
    public static final long ShowTextEncodingViewer$offset() {
        return ShowTextEncodingViewer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowTextEncodingViewer
     * }
     */
    public static boolean ShowTextEncodingViewer(MemorySegment struct) {
        return struct.get(ShowTextEncodingViewer$LAYOUT, ShowTextEncodingViewer$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowTextEncodingViewer
     * }
     */
    public static void ShowTextEncodingViewer(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowTextEncodingViewer$LAYOUT, ShowTextEncodingViewer$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowAtlasTintedWithTextColor$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowAtlasTintedWithTextColor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowAtlasTintedWithTextColor
     * }
     */
    public static final OfBoolean ShowAtlasTintedWithTextColor$layout() {
        return ShowAtlasTintedWithTextColor$LAYOUT;
    }

    private static final long ShowAtlasTintedWithTextColor$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowAtlasTintedWithTextColor
     * }
     */
    public static final long ShowAtlasTintedWithTextColor$offset() {
        return ShowAtlasTintedWithTextColor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowAtlasTintedWithTextColor
     * }
     */
    public static boolean ShowAtlasTintedWithTextColor(MemorySegment struct) {
        return struct.get(ShowAtlasTintedWithTextColor$LAYOUT, ShowAtlasTintedWithTextColor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowAtlasTintedWithTextColor
     * }
     */
    public static void ShowAtlasTintedWithTextColor(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowAtlasTintedWithTextColor$LAYOUT, ShowAtlasTintedWithTextColor$OFFSET, fieldValue);
    }

    private static final OfBoolean ShowDockingNodes$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ShowDockingNodes"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ShowDockingNodes
     * }
     */
    public static final OfBoolean ShowDockingNodes$layout() {
        return ShowDockingNodes$LAYOUT;
    }

    private static final long ShowDockingNodes$OFFSET = 9;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ShowDockingNodes
     * }
     */
    public static final long ShowDockingNodes$offset() {
        return ShowDockingNodes$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ShowDockingNodes
     * }
     */
    public static boolean ShowDockingNodes(MemorySegment struct) {
        return struct.get(ShowDockingNodes$LAYOUT, ShowDockingNodes$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ShowDockingNodes
     * }
     */
    public static void ShowDockingNodes(MemorySegment struct, boolean fieldValue) {
        struct.set(ShowDockingNodes$LAYOUT, ShowDockingNodes$OFFSET, fieldValue);
    }

    private static final OfInt ShowWindowsRectsType$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ShowWindowsRectsType"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int ShowWindowsRectsType
     * }
     */
    public static final OfInt ShowWindowsRectsType$layout() {
        return ShowWindowsRectsType$LAYOUT;
    }

    private static final long ShowWindowsRectsType$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int ShowWindowsRectsType
     * }
     */
    public static final long ShowWindowsRectsType$offset() {
        return ShowWindowsRectsType$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int ShowWindowsRectsType
     * }
     */
    public static int ShowWindowsRectsType(MemorySegment struct) {
        return struct.get(ShowWindowsRectsType$LAYOUT, ShowWindowsRectsType$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int ShowWindowsRectsType
     * }
     */
    public static void ShowWindowsRectsType(MemorySegment struct, int fieldValue) {
        struct.set(ShowWindowsRectsType$LAYOUT, ShowWindowsRectsType$OFFSET, fieldValue);
    }

    private static final OfInt ShowTablesRectsType$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ShowTablesRectsType"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int ShowTablesRectsType
     * }
     */
    public static final OfInt ShowTablesRectsType$layout() {
        return ShowTablesRectsType$LAYOUT;
    }

    private static final long ShowTablesRectsType$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int ShowTablesRectsType
     * }
     */
    public static final long ShowTablesRectsType$offset() {
        return ShowTablesRectsType$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int ShowTablesRectsType
     * }
     */
    public static int ShowTablesRectsType(MemorySegment struct) {
        return struct.get(ShowTablesRectsType$LAYOUT, ShowTablesRectsType$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int ShowTablesRectsType
     * }
     */
    public static void ShowTablesRectsType(MemorySegment struct, int fieldValue) {
        struct.set(ShowTablesRectsType$LAYOUT, ShowTablesRectsType$OFFSET, fieldValue);
    }

    private static final OfInt HighlightMonitorIdx$LAYOUT = (OfInt)$LAYOUT.select(groupElement("HighlightMonitorIdx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int HighlightMonitorIdx
     * }
     */
    public static final OfInt HighlightMonitorIdx$layout() {
        return HighlightMonitorIdx$LAYOUT;
    }

    private static final long HighlightMonitorIdx$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int HighlightMonitorIdx
     * }
     */
    public static final long HighlightMonitorIdx$offset() {
        return HighlightMonitorIdx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int HighlightMonitorIdx
     * }
     */
    public static int HighlightMonitorIdx(MemorySegment struct) {
        return struct.get(HighlightMonitorIdx$LAYOUT, HighlightMonitorIdx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int HighlightMonitorIdx
     * }
     */
    public static void HighlightMonitorIdx(MemorySegment struct, int fieldValue) {
        struct.set(HighlightMonitorIdx$LAYOUT, HighlightMonitorIdx$OFFSET, fieldValue);
    }

    private static final OfInt HighlightViewportID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("HighlightViewportID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID HighlightViewportID
     * }
     */
    public static final OfInt HighlightViewportID$layout() {
        return HighlightViewportID$LAYOUT;
    }

    private static final long HighlightViewportID$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID HighlightViewportID
     * }
     */
    public static final long HighlightViewportID$offset() {
        return HighlightViewportID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID HighlightViewportID
     * }
     */
    public static int HighlightViewportID(MemorySegment struct) {
        return struct.get(HighlightViewportID$LAYOUT, HighlightViewportID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID HighlightViewportID
     * }
     */
    public static void HighlightViewportID(MemorySegment struct, int fieldValue) {
        struct.set(HighlightViewportID$LAYOUT, HighlightViewportID$OFFSET, fieldValue);
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
