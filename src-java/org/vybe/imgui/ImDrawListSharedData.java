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
 * struct ImDrawListSharedData {
 *     ImVec2 TexUvWhitePixel;
 *     ImFont *Font;
 *     float FontSize;
 *     float FontScale;
 *     float CurveTessellationTol;
 *     float CircleSegmentMaxError;
 *     ImVec4 ClipRectFullscreen;
 *     ImDrawListFlags InitialFlags;
 *     ImVector_ImVec2 TempBuffer;
 *     ImVec2 ArcFastVtx[48];
 *     float ArcFastRadiusCutoff;
 *     ImU8 CircleSegmentCounts[64];
 *     const ImVec4 *TexUvLines;
 * }
 * }
 */
public class ImDrawListSharedData {

    ImDrawListSharedData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ImVec2.layout().withName("TexUvWhitePixel"),
        imgui.C_POINTER.withName("Font"),
        imgui.C_FLOAT.withName("FontSize"),
        imgui.C_FLOAT.withName("FontScale"),
        imgui.C_FLOAT.withName("CurveTessellationTol"),
        imgui.C_FLOAT.withName("CircleSegmentMaxError"),
        ImVec4.layout().withName("ClipRectFullscreen"),
        imgui.C_INT.withName("InitialFlags"),
        MemoryLayout.paddingLayout(4),
        ImVector_ImVec2.layout().withName("TempBuffer"),
        MemoryLayout.sequenceLayout(48, ImVec2.layout()).withName("ArcFastVtx"),
        imgui.C_FLOAT.withName("ArcFastRadiusCutoff"),
        MemoryLayout.sequenceLayout(64, imgui.C_CHAR).withName("CircleSegmentCounts"),
        MemoryLayout.paddingLayout(4),
        imgui.C_POINTER.withName("TexUvLines")
    ).withName("ImDrawListSharedData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout TexUvWhitePixel$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("TexUvWhitePixel"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 TexUvWhitePixel
     * }
     */
    public static final GroupLayout TexUvWhitePixel$layout() {
        return TexUvWhitePixel$LAYOUT;
    }

    private static final long TexUvWhitePixel$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 TexUvWhitePixel
     * }
     */
    public static final long TexUvWhitePixel$offset() {
        return TexUvWhitePixel$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 TexUvWhitePixel
     * }
     */
    public static MemorySegment TexUvWhitePixel(MemorySegment struct) {
        return struct.asSlice(TexUvWhitePixel$OFFSET, TexUvWhitePixel$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 TexUvWhitePixel
     * }
     */
    public static void TexUvWhitePixel(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, TexUvWhitePixel$OFFSET, TexUvWhitePixel$LAYOUT.byteSize());
    }

    private static final AddressLayout Font$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Font"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImFont *Font
     * }
     */
    public static final AddressLayout Font$layout() {
        return Font$LAYOUT;
    }

    private static final long Font$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImFont *Font
     * }
     */
    public static final long Font$offset() {
        return Font$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImFont *Font
     * }
     */
    public static MemorySegment Font(MemorySegment struct) {
        return struct.get(Font$LAYOUT, Font$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImFont *Font
     * }
     */
    public static void Font(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Font$LAYOUT, Font$OFFSET, fieldValue);
    }

    private static final OfFloat FontSize$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("FontSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float FontSize
     * }
     */
    public static final OfFloat FontSize$layout() {
        return FontSize$LAYOUT;
    }

    private static final long FontSize$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float FontSize
     * }
     */
    public static final long FontSize$offset() {
        return FontSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float FontSize
     * }
     */
    public static float FontSize(MemorySegment struct) {
        return struct.get(FontSize$LAYOUT, FontSize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float FontSize
     * }
     */
    public static void FontSize(MemorySegment struct, float fieldValue) {
        struct.set(FontSize$LAYOUT, FontSize$OFFSET, fieldValue);
    }

    private static final OfFloat FontScale$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("FontScale"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float FontScale
     * }
     */
    public static final OfFloat FontScale$layout() {
        return FontScale$LAYOUT;
    }

    private static final long FontScale$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float FontScale
     * }
     */
    public static final long FontScale$offset() {
        return FontScale$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float FontScale
     * }
     */
    public static float FontScale(MemorySegment struct) {
        return struct.get(FontScale$LAYOUT, FontScale$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float FontScale
     * }
     */
    public static void FontScale(MemorySegment struct, float fieldValue) {
        struct.set(FontScale$LAYOUT, FontScale$OFFSET, fieldValue);
    }

    private static final OfFloat CurveTessellationTol$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("CurveTessellationTol"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float CurveTessellationTol
     * }
     */
    public static final OfFloat CurveTessellationTol$layout() {
        return CurveTessellationTol$LAYOUT;
    }

    private static final long CurveTessellationTol$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float CurveTessellationTol
     * }
     */
    public static final long CurveTessellationTol$offset() {
        return CurveTessellationTol$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float CurveTessellationTol
     * }
     */
    public static float CurveTessellationTol(MemorySegment struct) {
        return struct.get(CurveTessellationTol$LAYOUT, CurveTessellationTol$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float CurveTessellationTol
     * }
     */
    public static void CurveTessellationTol(MemorySegment struct, float fieldValue) {
        struct.set(CurveTessellationTol$LAYOUT, CurveTessellationTol$OFFSET, fieldValue);
    }

    private static final OfFloat CircleSegmentMaxError$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("CircleSegmentMaxError"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float CircleSegmentMaxError
     * }
     */
    public static final OfFloat CircleSegmentMaxError$layout() {
        return CircleSegmentMaxError$LAYOUT;
    }

    private static final long CircleSegmentMaxError$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float CircleSegmentMaxError
     * }
     */
    public static final long CircleSegmentMaxError$offset() {
        return CircleSegmentMaxError$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float CircleSegmentMaxError
     * }
     */
    public static float CircleSegmentMaxError(MemorySegment struct) {
        return struct.get(CircleSegmentMaxError$LAYOUT, CircleSegmentMaxError$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float CircleSegmentMaxError
     * }
     */
    public static void CircleSegmentMaxError(MemorySegment struct, float fieldValue) {
        struct.set(CircleSegmentMaxError$LAYOUT, CircleSegmentMaxError$OFFSET, fieldValue);
    }

    private static final GroupLayout ClipRectFullscreen$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("ClipRectFullscreen"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec4 ClipRectFullscreen
     * }
     */
    public static final GroupLayout ClipRectFullscreen$layout() {
        return ClipRectFullscreen$LAYOUT;
    }

    private static final long ClipRectFullscreen$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec4 ClipRectFullscreen
     * }
     */
    public static final long ClipRectFullscreen$offset() {
        return ClipRectFullscreen$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec4 ClipRectFullscreen
     * }
     */
    public static MemorySegment ClipRectFullscreen(MemorySegment struct) {
        return struct.asSlice(ClipRectFullscreen$OFFSET, ClipRectFullscreen$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec4 ClipRectFullscreen
     * }
     */
    public static void ClipRectFullscreen(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ClipRectFullscreen$OFFSET, ClipRectFullscreen$LAYOUT.byteSize());
    }

    private static final OfInt InitialFlags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("InitialFlags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImDrawListFlags InitialFlags
     * }
     */
    public static final OfInt InitialFlags$layout() {
        return InitialFlags$LAYOUT;
    }

    private static final long InitialFlags$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImDrawListFlags InitialFlags
     * }
     */
    public static final long InitialFlags$offset() {
        return InitialFlags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImDrawListFlags InitialFlags
     * }
     */
    public static int InitialFlags(MemorySegment struct) {
        return struct.get(InitialFlags$LAYOUT, InitialFlags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImDrawListFlags InitialFlags
     * }
     */
    public static void InitialFlags(MemorySegment struct, int fieldValue) {
        struct.set(InitialFlags$LAYOUT, InitialFlags$OFFSET, fieldValue);
    }

    private static final GroupLayout TempBuffer$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("TempBuffer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_ImVec2 TempBuffer
     * }
     */
    public static final GroupLayout TempBuffer$layout() {
        return TempBuffer$LAYOUT;
    }

    private static final long TempBuffer$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_ImVec2 TempBuffer
     * }
     */
    public static final long TempBuffer$offset() {
        return TempBuffer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_ImVec2 TempBuffer
     * }
     */
    public static MemorySegment TempBuffer(MemorySegment struct) {
        return struct.asSlice(TempBuffer$OFFSET, TempBuffer$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_ImVec2 TempBuffer
     * }
     */
    public static void TempBuffer(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, TempBuffer$OFFSET, TempBuffer$LAYOUT.byteSize());
    }

    private static final SequenceLayout ArcFastVtx$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("ArcFastVtx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static final SequenceLayout ArcFastVtx$layout() {
        return ArcFastVtx$LAYOUT;
    }

    private static final long ArcFastVtx$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static final long ArcFastVtx$offset() {
        return ArcFastVtx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static MemorySegment ArcFastVtx(MemorySegment struct) {
        return struct.asSlice(ArcFastVtx$OFFSET, ArcFastVtx$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static void ArcFastVtx(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ArcFastVtx$OFFSET, ArcFastVtx$LAYOUT.byteSize());
    }

    private static long[] ArcFastVtx$DIMS = { 48 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static long[] ArcFastVtx$dimensions() {
        return ArcFastVtx$DIMS;
    }
    private static final MethodHandle ArcFastVtx$ELEM_HANDLE = ArcFastVtx$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static MemorySegment ArcFastVtx(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)ArcFastVtx$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ImVec2 ArcFastVtx[48]
     * }
     */
    public static void ArcFastVtx(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, ArcFastVtx(struct, index0), 0L, ImVec2.layout().byteSize());
    }

    private static final OfFloat ArcFastRadiusCutoff$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("ArcFastRadiusCutoff"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float ArcFastRadiusCutoff
     * }
     */
    public static final OfFloat ArcFastRadiusCutoff$layout() {
        return ArcFastRadiusCutoff$LAYOUT;
    }

    private static final long ArcFastRadiusCutoff$OFFSET = 456;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float ArcFastRadiusCutoff
     * }
     */
    public static final long ArcFastRadiusCutoff$offset() {
        return ArcFastRadiusCutoff$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float ArcFastRadiusCutoff
     * }
     */
    public static float ArcFastRadiusCutoff(MemorySegment struct) {
        return struct.get(ArcFastRadiusCutoff$LAYOUT, ArcFastRadiusCutoff$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float ArcFastRadiusCutoff
     * }
     */
    public static void ArcFastRadiusCutoff(MemorySegment struct, float fieldValue) {
        struct.set(ArcFastRadiusCutoff$LAYOUT, ArcFastRadiusCutoff$OFFSET, fieldValue);
    }

    private static final SequenceLayout CircleSegmentCounts$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("CircleSegmentCounts"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static final SequenceLayout CircleSegmentCounts$layout() {
        return CircleSegmentCounts$LAYOUT;
    }

    private static final long CircleSegmentCounts$OFFSET = 460;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static final long CircleSegmentCounts$offset() {
        return CircleSegmentCounts$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static MemorySegment CircleSegmentCounts(MemorySegment struct) {
        return struct.asSlice(CircleSegmentCounts$OFFSET, CircleSegmentCounts$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static void CircleSegmentCounts(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, CircleSegmentCounts$OFFSET, CircleSegmentCounts$LAYOUT.byteSize());
    }

    private static long[] CircleSegmentCounts$DIMS = { 64 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static long[] CircleSegmentCounts$dimensions() {
        return CircleSegmentCounts$DIMS;
    }
    private static final VarHandle CircleSegmentCounts$ELEM_HANDLE = CircleSegmentCounts$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static byte CircleSegmentCounts(MemorySegment struct, long index0) {
        return (byte)CircleSegmentCounts$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ImU8 CircleSegmentCounts[64]
     * }
     */
    public static void CircleSegmentCounts(MemorySegment struct, long index0, byte fieldValue) {
        CircleSegmentCounts$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final AddressLayout TexUvLines$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("TexUvLines"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const ImVec4 *TexUvLines
     * }
     */
    public static final AddressLayout TexUvLines$layout() {
        return TexUvLines$LAYOUT;
    }

    private static final long TexUvLines$OFFSET = 528;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const ImVec4 *TexUvLines
     * }
     */
    public static final long TexUvLines$offset() {
        return TexUvLines$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const ImVec4 *TexUvLines
     * }
     */
    public static MemorySegment TexUvLines(MemorySegment struct) {
        return struct.get(TexUvLines$LAYOUT, TexUvLines$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const ImVec4 *TexUvLines
     * }
     */
    public static void TexUvLines(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(TexUvLines$LAYOUT, TexUvLines$OFFSET, fieldValue);
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

