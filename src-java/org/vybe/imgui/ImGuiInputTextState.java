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
 * struct ImGuiInputTextState {
 *     ImGuiContext *Ctx;
 *     ImGuiID ID;
 *     int CurLenW;
 *     int CurLenA;
 *     ImVector_ImWchar TextW;
 *     ImVector_char TextA;
 *     ImVector_char InitialTextA;
 *     bool TextAIsValid;
 *     int BufCapacityA;
 *     float ScrollX;
 *     STB_TexteditState Stb;
 *     float CursorAnim;
 *     bool CursorFollow;
 *     bool SelectedAllMouseLock;
 *     bool Edited;
 *     ImGuiInputTextFlags Flags;
 *     bool ReloadUserBuf;
 *     int ReloadSelectionStart;
 *     int ReloadSelectionEnd;
 * }
 * }
 */
public class ImGuiInputTextState {

    ImGuiInputTextState() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("Ctx"),
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("CurLenW"),
        imgui.C_INT.withName("CurLenA"),
        MemoryLayout.paddingLayout(4),
        ImVector_ImWchar.layout().withName("TextW"),
        ImVector_char.layout().withName("TextA"),
        ImVector_char.layout().withName("InitialTextA"),
        imgui.C_BOOL.withName("TextAIsValid"),
        MemoryLayout.paddingLayout(3),
        imgui.C_INT.withName("BufCapacityA"),
        imgui.C_FLOAT.withName("ScrollX"),
        STB_TexteditState.layout().withName("Stb"),
        imgui.C_FLOAT.withName("CursorAnim"),
        imgui.C_BOOL.withName("CursorFollow"),
        imgui.C_BOOL.withName("SelectedAllMouseLock"),
        imgui.C_BOOL.withName("Edited"),
        MemoryLayout.paddingLayout(1),
        imgui.C_INT.withName("Flags"),
        imgui.C_BOOL.withName("ReloadUserBuf"),
        MemoryLayout.paddingLayout(3),
        imgui.C_INT.withName("ReloadSelectionStart"),
        imgui.C_INT.withName("ReloadSelectionEnd")
    ).withName("ImGuiInputTextState");

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

    private static final OfInt CurLenW$LAYOUT = (OfInt)$LAYOUT.select(groupElement("CurLenW"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int CurLenW
     * }
     */
    public static final OfInt CurLenW$layout() {
        return CurLenW$LAYOUT;
    }

    private static final long CurLenW$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int CurLenW
     * }
     */
    public static final long CurLenW$offset() {
        return CurLenW$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int CurLenW
     * }
     */
    public static int CurLenW(MemorySegment struct) {
        return struct.get(CurLenW$LAYOUT, CurLenW$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int CurLenW
     * }
     */
    public static void CurLenW(MemorySegment struct, int fieldValue) {
        struct.set(CurLenW$LAYOUT, CurLenW$OFFSET, fieldValue);
    }

    private static final OfInt CurLenA$LAYOUT = (OfInt)$LAYOUT.select(groupElement("CurLenA"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int CurLenA
     * }
     */
    public static final OfInt CurLenA$layout() {
        return CurLenA$LAYOUT;
    }

    private static final long CurLenA$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int CurLenA
     * }
     */
    public static final long CurLenA$offset() {
        return CurLenA$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int CurLenA
     * }
     */
    public static int CurLenA(MemorySegment struct) {
        return struct.get(CurLenA$LAYOUT, CurLenA$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int CurLenA
     * }
     */
    public static void CurLenA(MemorySegment struct, int fieldValue) {
        struct.set(CurLenA$LAYOUT, CurLenA$OFFSET, fieldValue);
    }

    private static final GroupLayout TextW$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("TextW"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_ImWchar TextW
     * }
     */
    public static final GroupLayout TextW$layout() {
        return TextW$LAYOUT;
    }

    private static final long TextW$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_ImWchar TextW
     * }
     */
    public static final long TextW$offset() {
        return TextW$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_ImWchar TextW
     * }
     */
    public static MemorySegment TextW(MemorySegment struct) {
        return struct.asSlice(TextW$OFFSET, TextW$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_ImWchar TextW
     * }
     */
    public static void TextW(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, TextW$OFFSET, TextW$LAYOUT.byteSize());
    }

    private static final GroupLayout TextA$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("TextA"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_char TextA
     * }
     */
    public static final GroupLayout TextA$layout() {
        return TextA$LAYOUT;
    }

    private static final long TextA$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_char TextA
     * }
     */
    public static final long TextA$offset() {
        return TextA$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_char TextA
     * }
     */
    public static MemorySegment TextA(MemorySegment struct) {
        return struct.asSlice(TextA$OFFSET, TextA$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_char TextA
     * }
     */
    public static void TextA(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, TextA$OFFSET, TextA$LAYOUT.byteSize());
    }

    private static final GroupLayout InitialTextA$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("InitialTextA"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_char InitialTextA
     * }
     */
    public static final GroupLayout InitialTextA$layout() {
        return InitialTextA$LAYOUT;
    }

    private static final long InitialTextA$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_char InitialTextA
     * }
     */
    public static final long InitialTextA$offset() {
        return InitialTextA$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_char InitialTextA
     * }
     */
    public static MemorySegment InitialTextA(MemorySegment struct) {
        return struct.asSlice(InitialTextA$OFFSET, InitialTextA$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_char InitialTextA
     * }
     */
    public static void InitialTextA(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, InitialTextA$OFFSET, InitialTextA$LAYOUT.byteSize());
    }

    private static final OfBoolean TextAIsValid$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("TextAIsValid"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool TextAIsValid
     * }
     */
    public static final OfBoolean TextAIsValid$layout() {
        return TextAIsValid$LAYOUT;
    }

    private static final long TextAIsValid$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool TextAIsValid
     * }
     */
    public static final long TextAIsValid$offset() {
        return TextAIsValid$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool TextAIsValid
     * }
     */
    public static boolean TextAIsValid(MemorySegment struct) {
        return struct.get(TextAIsValid$LAYOUT, TextAIsValid$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool TextAIsValid
     * }
     */
    public static void TextAIsValid(MemorySegment struct, boolean fieldValue) {
        struct.set(TextAIsValid$LAYOUT, TextAIsValid$OFFSET, fieldValue);
    }

    private static final OfInt BufCapacityA$LAYOUT = (OfInt)$LAYOUT.select(groupElement("BufCapacityA"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int BufCapacityA
     * }
     */
    public static final OfInt BufCapacityA$layout() {
        return BufCapacityA$LAYOUT;
    }

    private static final long BufCapacityA$OFFSET = 76;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int BufCapacityA
     * }
     */
    public static final long BufCapacityA$offset() {
        return BufCapacityA$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int BufCapacityA
     * }
     */
    public static int BufCapacityA(MemorySegment struct) {
        return struct.get(BufCapacityA$LAYOUT, BufCapacityA$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int BufCapacityA
     * }
     */
    public static void BufCapacityA(MemorySegment struct, int fieldValue) {
        struct.set(BufCapacityA$LAYOUT, BufCapacityA$OFFSET, fieldValue);
    }

    private static final OfFloat ScrollX$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("ScrollX"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float ScrollX
     * }
     */
    public static final OfFloat ScrollX$layout() {
        return ScrollX$LAYOUT;
    }

    private static final long ScrollX$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float ScrollX
     * }
     */
    public static final long ScrollX$offset() {
        return ScrollX$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float ScrollX
     * }
     */
    public static float ScrollX(MemorySegment struct) {
        return struct.get(ScrollX$LAYOUT, ScrollX$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float ScrollX
     * }
     */
    public static void ScrollX(MemorySegment struct, float fieldValue) {
        struct.set(ScrollX$LAYOUT, ScrollX$OFFSET, fieldValue);
    }

    private static final GroupLayout Stb$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("Stb"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * STB_TexteditState Stb
     * }
     */
    public static final GroupLayout Stb$layout() {
        return Stb$LAYOUT;
    }

    private static final long Stb$OFFSET = 84;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * STB_TexteditState Stb
     * }
     */
    public static final long Stb$offset() {
        return Stb$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * STB_TexteditState Stb
     * }
     */
    public static MemorySegment Stb(MemorySegment struct) {
        return struct.asSlice(Stb$OFFSET, Stb$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * STB_TexteditState Stb
     * }
     */
    public static void Stb(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Stb$OFFSET, Stb$LAYOUT.byteSize());
    }

    private static final OfFloat CursorAnim$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("CursorAnim"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float CursorAnim
     * }
     */
    public static final OfFloat CursorAnim$layout() {
        return CursorAnim$LAYOUT;
    }

    private static final long CursorAnim$OFFSET = 3712;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float CursorAnim
     * }
     */
    public static final long CursorAnim$offset() {
        return CursorAnim$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float CursorAnim
     * }
     */
    public static float CursorAnim(MemorySegment struct) {
        return struct.get(CursorAnim$LAYOUT, CursorAnim$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float CursorAnim
     * }
     */
    public static void CursorAnim(MemorySegment struct, float fieldValue) {
        struct.set(CursorAnim$LAYOUT, CursorAnim$OFFSET, fieldValue);
    }

    private static final OfBoolean CursorFollow$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("CursorFollow"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool CursorFollow
     * }
     */
    public static final OfBoolean CursorFollow$layout() {
        return CursorFollow$LAYOUT;
    }

    private static final long CursorFollow$OFFSET = 3716;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool CursorFollow
     * }
     */
    public static final long CursorFollow$offset() {
        return CursorFollow$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool CursorFollow
     * }
     */
    public static boolean CursorFollow(MemorySegment struct) {
        return struct.get(CursorFollow$LAYOUT, CursorFollow$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool CursorFollow
     * }
     */
    public static void CursorFollow(MemorySegment struct, boolean fieldValue) {
        struct.set(CursorFollow$LAYOUT, CursorFollow$OFFSET, fieldValue);
    }

    private static final OfBoolean SelectedAllMouseLock$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("SelectedAllMouseLock"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool SelectedAllMouseLock
     * }
     */
    public static final OfBoolean SelectedAllMouseLock$layout() {
        return SelectedAllMouseLock$LAYOUT;
    }

    private static final long SelectedAllMouseLock$OFFSET = 3717;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool SelectedAllMouseLock
     * }
     */
    public static final long SelectedAllMouseLock$offset() {
        return SelectedAllMouseLock$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool SelectedAllMouseLock
     * }
     */
    public static boolean SelectedAllMouseLock(MemorySegment struct) {
        return struct.get(SelectedAllMouseLock$LAYOUT, SelectedAllMouseLock$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool SelectedAllMouseLock
     * }
     */
    public static void SelectedAllMouseLock(MemorySegment struct, boolean fieldValue) {
        struct.set(SelectedAllMouseLock$LAYOUT, SelectedAllMouseLock$OFFSET, fieldValue);
    }

    private static final OfBoolean Edited$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("Edited"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool Edited
     * }
     */
    public static final OfBoolean Edited$layout() {
        return Edited$LAYOUT;
    }

    private static final long Edited$OFFSET = 3718;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool Edited
     * }
     */
    public static final long Edited$offset() {
        return Edited$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool Edited
     * }
     */
    public static boolean Edited(MemorySegment struct) {
        return struct.get(Edited$LAYOUT, Edited$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool Edited
     * }
     */
    public static void Edited(MemorySegment struct, boolean fieldValue) {
        struct.set(Edited$LAYOUT, Edited$OFFSET, fieldValue);
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

    private static final long Flags$OFFSET = 3720;

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

    private static final OfBoolean ReloadUserBuf$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("ReloadUserBuf"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool ReloadUserBuf
     * }
     */
    public static final OfBoolean ReloadUserBuf$layout() {
        return ReloadUserBuf$LAYOUT;
    }

    private static final long ReloadUserBuf$OFFSET = 3724;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool ReloadUserBuf
     * }
     */
    public static final long ReloadUserBuf$offset() {
        return ReloadUserBuf$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool ReloadUserBuf
     * }
     */
    public static boolean ReloadUserBuf(MemorySegment struct) {
        return struct.get(ReloadUserBuf$LAYOUT, ReloadUserBuf$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool ReloadUserBuf
     * }
     */
    public static void ReloadUserBuf(MemorySegment struct, boolean fieldValue) {
        struct.set(ReloadUserBuf$LAYOUT, ReloadUserBuf$OFFSET, fieldValue);
    }

    private static final OfInt ReloadSelectionStart$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ReloadSelectionStart"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int ReloadSelectionStart
     * }
     */
    public static final OfInt ReloadSelectionStart$layout() {
        return ReloadSelectionStart$LAYOUT;
    }

    private static final long ReloadSelectionStart$OFFSET = 3728;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int ReloadSelectionStart
     * }
     */
    public static final long ReloadSelectionStart$offset() {
        return ReloadSelectionStart$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int ReloadSelectionStart
     * }
     */
    public static int ReloadSelectionStart(MemorySegment struct) {
        return struct.get(ReloadSelectionStart$LAYOUT, ReloadSelectionStart$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int ReloadSelectionStart
     * }
     */
    public static void ReloadSelectionStart(MemorySegment struct, int fieldValue) {
        struct.set(ReloadSelectionStart$LAYOUT, ReloadSelectionStart$OFFSET, fieldValue);
    }

    private static final OfInt ReloadSelectionEnd$LAYOUT = (OfInt)$LAYOUT.select(groupElement("ReloadSelectionEnd"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int ReloadSelectionEnd
     * }
     */
    public static final OfInt ReloadSelectionEnd$layout() {
        return ReloadSelectionEnd$LAYOUT;
    }

    private static final long ReloadSelectionEnd$OFFSET = 3732;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int ReloadSelectionEnd
     * }
     */
    public static final long ReloadSelectionEnd$offset() {
        return ReloadSelectionEnd$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int ReloadSelectionEnd
     * }
     */
    public static int ReloadSelectionEnd(MemorySegment struct) {
        return struct.get(ReloadSelectionEnd$LAYOUT, ReloadSelectionEnd$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int ReloadSelectionEnd
     * }
     */
    public static void ReloadSelectionEnd(MemorySegment struct, int fieldValue) {
        struct.set(ReloadSelectionEnd$LAYOUT, ReloadSelectionEnd$OFFSET, fieldValue);
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

