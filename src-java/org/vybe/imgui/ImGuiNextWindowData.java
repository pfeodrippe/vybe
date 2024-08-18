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
 * struct ImGuiNextWindowData {
 *     ImGuiNextWindowDataFlags Flags;
 *     ImGuiCond PosCond;
 *     ImGuiCond SizeCond;
 *     ImGuiCond CollapsedCond;
 *     ImGuiCond DockCond;
 *     ImVec2 PosVal;
 *     ImVec2 PosPivotVal;
 *     ImVec2 SizeVal;
 *     ImVec2 ContentSizeVal;
 *     ImVec2 ScrollVal;
 *     ImGuiChildFlags ChildFlags;
 *     bool PosUndock;
 *     bool CollapsedVal;
 *     ImRect SizeConstraintRect;
 *     ImGuiSizeCallback SizeCallback;
 *     void *SizeCallbackUserData;
 *     float BgAlphaVal;
 *     ImGuiID ViewportId;
 *     ImGuiID DockId;
 *     ImGuiWindowClass WindowClass;
 *     ImVec2 MenuBarOffsetMinVal;
 *     ImGuiWindowRefreshFlags RefreshFlagsVal;
 * }
 * }
 */
public class ImGuiNextWindowData {

    ImGuiNextWindowData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Flags"),
        imgui.C_INT.withName("PosCond"),
        imgui.C_INT.withName("SizeCond"),
        imgui.C_INT.withName("CollapsedCond"),
        imgui.C_INT.withName("DockCond"),
        ImVec2.layout().withName("PosVal"),
        ImVec2.layout().withName("PosPivotVal"),
        ImVec2.layout().withName("SizeVal"),
        ImVec2.layout().withName("ContentSizeVal"),
        ImVec2.layout().withName("ScrollVal"),
        imgui.C_INT.withName("ChildFlags"),
        imgui.C_BOOL.withName("PosUndock"),
        imgui.C_BOOL.withName("CollapsedVal"),
        MemoryLayout.paddingLayout(2),
        ImRect.layout().withName("SizeConstraintRect"),
        MemoryLayout.paddingLayout(4),
        imgui.C_POINTER.withName("SizeCallback"),
        imgui.C_POINTER.withName("SizeCallbackUserData"),
        imgui.C_FLOAT.withName("BgAlphaVal"),
        imgui.C_INT.withName("ViewportId"),
        imgui.C_INT.withName("DockId"),
        ImGuiWindowClass.layout().withName("WindowClass"),
        ImVec2.layout().withName("MenuBarOffsetMinVal"),
        imgui.C_INT.withName("RefreshFlagsVal")
    ).withName("ImGuiNextWindowData");

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
     * ImGuiNextWindowDataFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiNextWindowDataFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiNextWindowDataFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiNextWindowDataFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
    }

    private static final OfInt PosCond$LAYOUT = (OfInt)$LAYOUT.select(groupElement("PosCond"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiCond PosCond
     * }
     */
    public static final OfInt PosCond$layout() {
        return PosCond$LAYOUT;
    }

    private static final long PosCond$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiCond PosCond
     * }
     */
    public static final long PosCond$offset() {
        return PosCond$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiCond PosCond
     * }
     */
    public static int PosCond(MemorySegment struct) {
        return struct.get(PosCond$LAYOUT, PosCond$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiCond PosCond
     * }
     */
    public static void PosCond(MemorySegment struct, int fieldValue) {
        struct.set(PosCond$LAYOUT, PosCond$OFFSET, fieldValue);
    }

    private static final OfInt SizeCond$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SizeCond"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiCond SizeCond
     * }
     */
    public static final OfInt SizeCond$layout() {
        return SizeCond$LAYOUT;
    }

    private static final long SizeCond$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiCond SizeCond
     * }
     */
    public static final long SizeCond$offset() {
        return SizeCond$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiCond SizeCond
     * }
     */
    public static int SizeCond(MemorySegment struct) {
        return struct.get(SizeCond$LAYOUT, SizeCond$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiCond SizeCond
     * }
     */
    public static void SizeCond(MemorySegment struct, int fieldValue) {
        struct.set(SizeCond$LAYOUT, SizeCond$OFFSET, fieldValue);
    }

    private static final OfInt CollapsedCond$LAYOUT = (OfInt)$LAYOUT.select(groupElement("CollapsedCond"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiCond CollapsedCond
     * }
     */
    public static final OfInt CollapsedCond$layout() {
        return CollapsedCond$LAYOUT;
    }

    private static final long CollapsedCond$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiCond CollapsedCond
     * }
     */
    public static final long CollapsedCond$offset() {
        return CollapsedCond$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiCond CollapsedCond
     * }
     */
    public static int CollapsedCond(MemorySegment struct) {
        return struct.get(CollapsedCond$LAYOUT, CollapsedCond$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiCond CollapsedCond
     * }
     */
    public static void CollapsedCond(MemorySegment struct, int fieldValue) {
        struct.set(CollapsedCond$LAYOUT, CollapsedCond$OFFSET, fieldValue);
    }

    private static final OfInt DockCond$LAYOUT = (OfInt)$LAYOUT.select(groupElement("DockCond"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiCond DockCond
     * }
     */
    public static final OfInt DockCond$layout() {
        return DockCond$LAYOUT;
    }

    private static final long DockCond$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiCond DockCond
     * }
     */
    public static final long DockCond$offset() {
        return DockCond$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiCond DockCond
     * }
     */
    public static int DockCond(MemorySegment struct) {
        return struct.get(DockCond$LAYOUT, DockCond$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiCond DockCond
     * }
     */
    public static void DockCond(MemorySegment struct, int fieldValue) {
        struct.set(DockCond$LAYOUT, DockCond$OFFSET, fieldValue);
    }

    private static final GroupLayout PosVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("PosVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 PosVal
     * }
     */
    public static final GroupLayout PosVal$layout() {
        return PosVal$LAYOUT;
    }

    private static final long PosVal$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 PosVal
     * }
     */
    public static final long PosVal$offset() {
        return PosVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 PosVal
     * }
     */
    public static MemorySegment PosVal(MemorySegment struct) {
        return struct.asSlice(PosVal$OFFSET, PosVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 PosVal
     * }
     */
    public static void PosVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, PosVal$OFFSET, PosVal$LAYOUT.byteSize());
    }

    private static final GroupLayout PosPivotVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("PosPivotVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 PosPivotVal
     * }
     */
    public static final GroupLayout PosPivotVal$layout() {
        return PosPivotVal$LAYOUT;
    }

    private static final long PosPivotVal$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 PosPivotVal
     * }
     */
    public static final long PosPivotVal$offset() {
        return PosPivotVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 PosPivotVal
     * }
     */
    public static MemorySegment PosPivotVal(MemorySegment struct) {
        return struct.asSlice(PosPivotVal$OFFSET, PosPivotVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 PosPivotVal
     * }
     */
    public static void PosPivotVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, PosPivotVal$OFFSET, PosPivotVal$LAYOUT.byteSize());
    }

    private static final GroupLayout SizeVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("SizeVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 SizeVal
     * }
     */
    public static final GroupLayout SizeVal$layout() {
        return SizeVal$LAYOUT;
    }

    private static final long SizeVal$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 SizeVal
     * }
     */
    public static final long SizeVal$offset() {
        return SizeVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 SizeVal
     * }
     */
    public static MemorySegment SizeVal(MemorySegment struct) {
        return struct.asSlice(SizeVal$OFFSET, SizeVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 SizeVal
     * }
     */
    public static void SizeVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, SizeVal$OFFSET, SizeVal$LAYOUT.byteSize());
    }

    private static final GroupLayout ContentSizeVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("ContentSizeVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 ContentSizeVal
     * }
     */
    public static final GroupLayout ContentSizeVal$layout() {
        return ContentSizeVal$LAYOUT;
    }

    private static final long ContentSizeVal$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 ContentSizeVal
     * }
     */
    public static final long ContentSizeVal$offset() {
        return ContentSizeVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 ContentSizeVal
     * }
     */
    public static MemorySegment ContentSizeVal(MemorySegment struct) {
        return struct.asSlice(ContentSizeVal$OFFSET, ContentSizeVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 ContentSizeVal
     * }
     */
    public static void ContentSizeVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ContentSizeVal$OFFSET, ContentSizeVal$LAYOUT.byteSize());
    }

    private static final GroupLayout ScrollVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("ScrollVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 ScrollVal
     * }
     */
    public static final GroupLayout ScrollVal$layout() {
        return ScrollVal$LAYOUT;
    }

    private static final long ScrollVal$OFFSET = 52;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 ScrollVal
     * }
     */
    public static final long ScrollVal$offset() {
        return ScrollVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 ScrollVal
     * }
     */
    public static MemorySegment ScrollVal(MemorySegment struct) {
        return struct.asSlice(ScrollVal$OFFSET, ScrollVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 ScrollVal
     * }
     */
    public static void ScrollVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ScrollVal$OFFSET, ScrollVal$LAYOUT.byteSize());
    }

    private static final OfInt ChildFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ChildFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiChildFlags ChildFlags
     * }
     */
    public static final OfInt ChildFlags$layout() {
        return ChildFlags$LAYOUT;
    }

    private static final long ChildFlags$OFFSET = 60;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiChildFlags ChildFlags
     * }
     */
    public static final long ChildFlags$offset() {
        return ChildFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiChildFlags ChildFlags
     * }
     */
    public static int ChildFlags(MemorySegment struct) {
        return struct.get(ChildFlags$LAYOUT, ChildFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiChildFlags ChildFlags
     * }
     */
    public static void ChildFlags(MemorySegment struct, int fieldValue) {
        struct.set(ChildFlags$LAYOUT, ChildFlags$OFFSET, fieldValue);
    }

    private static final OfBoolean PosUndock$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("PosUndock"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool PosUndock
     * }
     */
    public static final OfBoolean PosUndock$layout() {
        return PosUndock$LAYOUT;
    }

    private static final long PosUndock$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool PosUndock
     * }
     */
    public static final long PosUndock$offset() {
        return PosUndock$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool PosUndock
     * }
     */
    public static boolean PosUndock(MemorySegment struct) {
        return struct.get(PosUndock$LAYOUT, PosUndock$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool PosUndock
     * }
     */
    public static void PosUndock(MemorySegment struct, boolean fieldValue) {
        struct.set(PosUndock$LAYOUT, PosUndock$OFFSET, fieldValue);
    }

    private static final OfBoolean CollapsedVal$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("CollapsedVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool CollapsedVal
     * }
     */
    public static final OfBoolean CollapsedVal$layout() {
        return CollapsedVal$LAYOUT;
    }

    private static final long CollapsedVal$OFFSET = 65;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool CollapsedVal
     * }
     */
    public static final long CollapsedVal$offset() {
        return CollapsedVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool CollapsedVal
     * }
     */
    public static boolean CollapsedVal(MemorySegment struct) {
        return struct.get(CollapsedVal$LAYOUT, CollapsedVal$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool CollapsedVal
     * }
     */
    public static void CollapsedVal(MemorySegment struct, boolean fieldValue) {
        struct.set(CollapsedVal$LAYOUT, CollapsedVal$OFFSET, fieldValue);
    }

    private static final GroupLayout SizeConstraintRect$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("SizeConstraintRect"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImRect SizeConstraintRect
     * }
     */
    public static final GroupLayout SizeConstraintRect$layout() {
        return SizeConstraintRect$LAYOUT;
    }

    private static final long SizeConstraintRect$OFFSET = 68;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImRect SizeConstraintRect
     * }
     */
    public static final long SizeConstraintRect$offset() {
        return SizeConstraintRect$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImRect SizeConstraintRect
     * }
     */
    public static MemorySegment SizeConstraintRect(MemorySegment struct) {
        return struct.asSlice(SizeConstraintRect$OFFSET, SizeConstraintRect$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImRect SizeConstraintRect
     * }
     */
    public static void SizeConstraintRect(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, SizeConstraintRect$OFFSET, SizeConstraintRect$LAYOUT.byteSize());
    }

    private static final AddressLayout SizeCallback$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("SizeCallback"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiSizeCallback SizeCallback
     * }
     */
    public static final AddressLayout SizeCallback$layout() {
        return SizeCallback$LAYOUT;
    }

    private static final long SizeCallback$OFFSET = 88;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiSizeCallback SizeCallback
     * }
     */
    public static final long SizeCallback$offset() {
        return SizeCallback$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiSizeCallback SizeCallback
     * }
     */
    public static MemorySegment SizeCallback(MemorySegment struct) {
        return struct.get(SizeCallback$LAYOUT, SizeCallback$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiSizeCallback SizeCallback
     * }
     */
    public static void SizeCallback(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(SizeCallback$LAYOUT, SizeCallback$OFFSET, fieldValue);
    }

    private static final AddressLayout SizeCallbackUserData$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("SizeCallbackUserData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *SizeCallbackUserData
     * }
     */
    public static final AddressLayout SizeCallbackUserData$layout() {
        return SizeCallbackUserData$LAYOUT;
    }

    private static final long SizeCallbackUserData$OFFSET = 96;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *SizeCallbackUserData
     * }
     */
    public static final long SizeCallbackUserData$offset() {
        return SizeCallbackUserData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *SizeCallbackUserData
     * }
     */
    public static MemorySegment SizeCallbackUserData(MemorySegment struct) {
        return struct.get(SizeCallbackUserData$LAYOUT, SizeCallbackUserData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *SizeCallbackUserData
     * }
     */
    public static void SizeCallbackUserData(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(SizeCallbackUserData$LAYOUT, SizeCallbackUserData$OFFSET, fieldValue);
    }

    private static final OfFloat BgAlphaVal$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("BgAlphaVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float BgAlphaVal
     * }
     */
    public static final OfFloat BgAlphaVal$layout() {
        return BgAlphaVal$LAYOUT;
    }

    private static final long BgAlphaVal$OFFSET = 104;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float BgAlphaVal
     * }
     */
    public static final long BgAlphaVal$offset() {
        return BgAlphaVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float BgAlphaVal
     * }
     */
    public static float BgAlphaVal(MemorySegment struct) {
        return struct.get(BgAlphaVal$LAYOUT, BgAlphaVal$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float BgAlphaVal
     * }
     */
    public static void BgAlphaVal(MemorySegment struct, float fieldValue) {
        struct.set(BgAlphaVal$LAYOUT, BgAlphaVal$OFFSET, fieldValue);
    }

    private static final OfInt ViewportId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ViewportId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID ViewportId
     * }
     */
    public static final OfInt ViewportId$layout() {
        return ViewportId$LAYOUT;
    }

    private static final long ViewportId$OFFSET = 108;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID ViewportId
     * }
     */
    public static final long ViewportId$offset() {
        return ViewportId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID ViewportId
     * }
     */
    public static int ViewportId(MemorySegment struct) {
        return struct.get(ViewportId$LAYOUT, ViewportId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID ViewportId
     * }
     */
    public static void ViewportId(MemorySegment struct, int fieldValue) {
        struct.set(ViewportId$LAYOUT, ViewportId$OFFSET, fieldValue);
    }

    private static final OfInt DockId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("DockId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID DockId
     * }
     */
    public static final OfInt DockId$layout() {
        return DockId$LAYOUT;
    }

    private static final long DockId$OFFSET = 112;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID DockId
     * }
     */
    public static final long DockId$offset() {
        return DockId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID DockId
     * }
     */
    public static int DockId(MemorySegment struct) {
        return struct.get(DockId$LAYOUT, DockId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID DockId
     * }
     */
    public static void DockId(MemorySegment struct, int fieldValue) {
        struct.set(DockId$LAYOUT, DockId$OFFSET, fieldValue);
    }

    private static final GroupLayout WindowClass$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("WindowClass"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiWindowClass WindowClass
     * }
     */
    public static final GroupLayout WindowClass$layout() {
        return WindowClass$LAYOUT;
    }

    private static final long WindowClass$OFFSET = 116;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiWindowClass WindowClass
     * }
     */
    public static final long WindowClass$offset() {
        return WindowClass$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiWindowClass WindowClass
     * }
     */
    public static MemorySegment WindowClass(MemorySegment struct) {
        return struct.asSlice(WindowClass$OFFSET, WindowClass$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiWindowClass WindowClass
     * }
     */
    public static void WindowClass(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, WindowClass$OFFSET, WindowClass$LAYOUT.byteSize());
    }

    private static final GroupLayout MenuBarOffsetMinVal$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("MenuBarOffsetMinVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 MenuBarOffsetMinVal
     * }
     */
    public static final GroupLayout MenuBarOffsetMinVal$layout() {
        return MenuBarOffsetMinVal$LAYOUT;
    }

    private static final long MenuBarOffsetMinVal$OFFSET = 148;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 MenuBarOffsetMinVal
     * }
     */
    public static final long MenuBarOffsetMinVal$offset() {
        return MenuBarOffsetMinVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 MenuBarOffsetMinVal
     * }
     */
    public static MemorySegment MenuBarOffsetMinVal(MemorySegment struct) {
        return struct.asSlice(MenuBarOffsetMinVal$OFFSET, MenuBarOffsetMinVal$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 MenuBarOffsetMinVal
     * }
     */
    public static void MenuBarOffsetMinVal(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, MenuBarOffsetMinVal$OFFSET, MenuBarOffsetMinVal$LAYOUT.byteSize());
    }

    private static final OfInt RefreshFlagsVal$LAYOUT = (OfInt)$LAYOUT.select(groupElement("RefreshFlagsVal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiWindowRefreshFlags RefreshFlagsVal
     * }
     */
    public static final OfInt RefreshFlagsVal$layout() {
        return RefreshFlagsVal$LAYOUT;
    }

    private static final long RefreshFlagsVal$OFFSET = 156;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiWindowRefreshFlags RefreshFlagsVal
     * }
     */
    public static final long RefreshFlagsVal$offset() {
        return RefreshFlagsVal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiWindowRefreshFlags RefreshFlagsVal
     * }
     */
    public static int RefreshFlagsVal(MemorySegment struct) {
        return struct.get(RefreshFlagsVal$LAYOUT, RefreshFlagsVal$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiWindowRefreshFlags RefreshFlagsVal
     * }
     */
    public static void RefreshFlagsVal(MemorySegment struct, int fieldValue) {
        struct.set(RefreshFlagsVal$LAYOUT, RefreshFlagsVal$OFFSET, fieldValue);
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
