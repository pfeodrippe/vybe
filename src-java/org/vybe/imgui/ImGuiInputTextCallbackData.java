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
 * struct ImGuiInputTextCallbackData {
 *     ImGuiContext *Ctx;
 *     ImGuiInputTextFlags EventFlag;
 *     ImGuiInputTextFlags Flags;
 *     void *UserData;
 *     ImWchar EventChar;
 *     ImGuiKey EventKey;
 *     char *Buf;
 *     int BufTextLen;
 *     int BufSize;
 *     bool BufDirty;
 *     int CursorPos;
 *     int SelectionStart;
 *     int SelectionEnd;
 * }
 * }
 */
public class ImGuiInputTextCallbackData {

    ImGuiInputTextCallbackData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("Ctx"),
        imgui.C_INT.withName("EventFlag"),
        imgui.C_INT.withName("Flags"),
        imgui.C_POINTER.withName("UserData"),
        imgui.C_SHORT.withName("EventChar"),
        MemoryLayout.paddingLayout(2),
        imgui.C_INT.withName("EventKey"),
        imgui.C_POINTER.withName("Buf"),
        imgui.C_INT.withName("BufTextLen"),
        imgui.C_INT.withName("BufSize"),
        imgui.C_BOOL.withName("BufDirty"),
        MemoryLayout.paddingLayout(3),
        imgui.C_INT.withName("CursorPos"),
        imgui.C_INT.withName("SelectionStart"),
        imgui.C_INT.withName("SelectionEnd")
    ).withName("ImGuiInputTextCallbackData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout Ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiContext *Ctx
     * }
     */
    public static final AddressLayout Ctx$layout() {
        return Ctx$LAYOUT;
    }

    private static final long Ctx$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiContext *Ctx
     * }
     */
    public static final long Ctx$offset() {
        return Ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiContext *Ctx
     * }
     */
    public static MemorySegment Ctx(MemorySegment struct) {
        return struct.get(Ctx$LAYOUT, Ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiContext *Ctx
     * }
     */
    public static void Ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Ctx$LAYOUT, Ctx$OFFSET, fieldValue);
    }

    private static final OfInt EventFlag$LAYOUT = (OfInt)$LAYOUT.select(groupElement("EventFlag"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags EventFlag
     * }
     */
    public static final OfInt EventFlag$layout() {
        return EventFlag$LAYOUT;
    }

    private static final long EventFlag$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags EventFlag
     * }
     */
    public static final long EventFlag$offset() {
        return EventFlag$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags EventFlag
     * }
     */
    public static int EventFlag(MemorySegment struct) {
        return struct.get(EventFlag$LAYOUT, EventFlag$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags EventFlag
     * }
     */
    public static void EventFlag(MemorySegment struct, int fieldValue) {
        struct.set(EventFlag$LAYOUT, EventFlag$OFFSET, fieldValue);
    }

    private static final OfInt Flags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Flags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiInputTextFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
    }

    private static final AddressLayout UserData$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("UserData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static final AddressLayout UserData$layout() {
        return UserData$LAYOUT;
    }

    private static final long UserData$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static final long UserData$offset() {
        return UserData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static MemorySegment UserData(MemorySegment struct) {
        return struct.get(UserData$LAYOUT, UserData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static void UserData(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(UserData$LAYOUT, UserData$OFFSET, fieldValue);
    }

    private static final OfShort EventChar$LAYOUT = (OfShort)$LAYOUT.select(groupElement("EventChar"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImWchar EventChar
     * }
     */
    public static final OfShort EventChar$layout() {
        return EventChar$LAYOUT;
    }

    private static final long EventChar$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImWchar EventChar
     * }
     */
    public static final long EventChar$offset() {
        return EventChar$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImWchar EventChar
     * }
     */
    public static short EventChar(MemorySegment struct) {
        return struct.get(EventChar$LAYOUT, EventChar$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImWchar EventChar
     * }
     */
    public static void EventChar(MemorySegment struct, short fieldValue) {
        struct.set(EventChar$LAYOUT, EventChar$OFFSET, fieldValue);
    }

    private static final OfInt EventKey$LAYOUT = (OfInt)$LAYOUT.select(groupElement("EventKey"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiKey EventKey
     * }
     */
    public static final OfInt EventKey$layout() {
        return EventKey$LAYOUT;
    }

    private static final long EventKey$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiKey EventKey
     * }
     */
    public static final long EventKey$offset() {
        return EventKey$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiKey EventKey
     * }
     */
    public static int EventKey(MemorySegment struct) {
        return struct.get(EventKey$LAYOUT, EventKey$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiKey EventKey
     * }
     */
    public static void EventKey(MemorySegment struct, int fieldValue) {
        struct.set(EventKey$LAYOUT, EventKey$OFFSET, fieldValue);
    }

    private static final AddressLayout Buf$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Buf"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char *Buf
     * }
     */
    public static final AddressLayout Buf$layout() {
        return Buf$LAYOUT;
    }

    private static final long Buf$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char *Buf
     * }
     */
    public static final long Buf$offset() {
        return Buf$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char *Buf
     * }
     */
    public static MemorySegment Buf(MemorySegment struct) {
        return struct.get(Buf$LAYOUT, Buf$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char *Buf
     * }
     */
    public static void Buf(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Buf$LAYOUT, Buf$OFFSET, fieldValue);
    }

    private static final OfInt BufTextLen$LAYOUT = (OfInt)$LAYOUT.select(groupElement("BufTextLen"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int BufTextLen
     * }
     */
    public static final OfInt BufTextLen$layout() {
        return BufTextLen$LAYOUT;
    }

    private static final long BufTextLen$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int BufTextLen
     * }
     */
    public static final long BufTextLen$offset() {
        return BufTextLen$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int BufTextLen
     * }
     */
    public static int BufTextLen(MemorySegment struct) {
        return struct.get(BufTextLen$LAYOUT, BufTextLen$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int BufTextLen
     * }
     */
    public static void BufTextLen(MemorySegment struct, int fieldValue) {
        struct.set(BufTextLen$LAYOUT, BufTextLen$OFFSET, fieldValue);
    }

    private static final OfInt BufSize$LAYOUT = (OfInt)$LAYOUT.select(groupElement("BufSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int BufSize
     * }
     */
    public static final OfInt BufSize$layout() {
        return BufSize$LAYOUT;
    }

    private static final long BufSize$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int BufSize
     * }
     */
    public static final long BufSize$offset() {
        return BufSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int BufSize
     * }
     */
    public static int BufSize(MemorySegment struct) {
        return struct.get(BufSize$LAYOUT, BufSize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int BufSize
     * }
     */
    public static void BufSize(MemorySegment struct, int fieldValue) {
        struct.set(BufSize$LAYOUT, BufSize$OFFSET, fieldValue);
    }

    private static final OfBoolean BufDirty$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("BufDirty"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool BufDirty
     * }
     */
    public static final OfBoolean BufDirty$layout() {
        return BufDirty$LAYOUT;
    }

    private static final long BufDirty$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool BufDirty
     * }
     */
    public static final long BufDirty$offset() {
        return BufDirty$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool BufDirty
     * }
     */
    public static boolean BufDirty(MemorySegment struct) {
        return struct.get(BufDirty$LAYOUT, BufDirty$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool BufDirty
     * }
     */
    public static void BufDirty(MemorySegment struct, boolean fieldValue) {
        struct.set(BufDirty$LAYOUT, BufDirty$OFFSET, fieldValue);
    }

    private static final OfInt CursorPos$LAYOUT = (OfInt)$LAYOUT.select(groupElement("CursorPos"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int CursorPos
     * }
     */
    public static final OfInt CursorPos$layout() {
        return CursorPos$LAYOUT;
    }

    private static final long CursorPos$OFFSET = 52;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int CursorPos
     * }
     */
    public static final long CursorPos$offset() {
        return CursorPos$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int CursorPos
     * }
     */
    public static int CursorPos(MemorySegment struct) {
        return struct.get(CursorPos$LAYOUT, CursorPos$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int CursorPos
     * }
     */
    public static void CursorPos(MemorySegment struct, int fieldValue) {
        struct.set(CursorPos$LAYOUT, CursorPos$OFFSET, fieldValue);
    }

    private static final OfInt SelectionStart$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SelectionStart"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int SelectionStart
     * }
     */
    public static final OfInt SelectionStart$layout() {
        return SelectionStart$LAYOUT;
    }

    private static final long SelectionStart$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int SelectionStart
     * }
     */
    public static final long SelectionStart$offset() {
        return SelectionStart$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int SelectionStart
     * }
     */
    public static int SelectionStart(MemorySegment struct) {
        return struct.get(SelectionStart$LAYOUT, SelectionStart$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int SelectionStart
     * }
     */
    public static void SelectionStart(MemorySegment struct, int fieldValue) {
        struct.set(SelectionStart$LAYOUT, SelectionStart$OFFSET, fieldValue);
    }

    private static final OfInt SelectionEnd$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SelectionEnd"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int SelectionEnd
     * }
     */
    public static final OfInt SelectionEnd$layout() {
        return SelectionEnd$LAYOUT;
    }

    private static final long SelectionEnd$OFFSET = 60;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int SelectionEnd
     * }
     */
    public static final long SelectionEnd$offset() {
        return SelectionEnd$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int SelectionEnd
     * }
     */
    public static int SelectionEnd(MemorySegment struct) {
        return struct.get(SelectionEnd$LAYOUT, SelectionEnd$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int SelectionEnd
     * }
     */
    public static void SelectionEnd(MemorySegment struct, int fieldValue) {
        struct.set(SelectionEnd$LAYOUT, SelectionEnd$OFFSET, fieldValue);
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

