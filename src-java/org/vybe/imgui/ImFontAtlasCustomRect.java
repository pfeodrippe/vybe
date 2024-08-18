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
 * struct ImFontAtlasCustomRect {
 *     unsigned short Width;
 *     unsigned short Height;
 *     unsigned short X;
 *     unsigned short Y;
 *     unsigned int GlyphID;
 *     float GlyphAdvanceX;
 *     ImVec2 GlyphOffset;
 *     ImFont *Font;
 * }
 * }
 */
public class ImFontAtlasCustomRect {

    ImFontAtlasCustomRect() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_SHORT.withName("Width"),
        imgui.C_SHORT.withName("Height"),
        imgui.C_SHORT.withName("X"),
        imgui.C_SHORT.withName("Y"),
        imgui.C_INT.withName("GlyphID"),
        imgui.C_FLOAT.withName("GlyphAdvanceX"),
        ImVec2.layout().withName("GlyphOffset"),
        imgui.C_POINTER.withName("Font")
    ).withName("ImFontAtlasCustomRect");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfShort Width$LAYOUT = (OfShort)$LAYOUT.select(groupElement("Width"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned short Width
     * }
     */
    public static final OfShort Width$layout() {
        return Width$LAYOUT;
    }

    private static final long Width$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned short Width
     * }
     */
    public static final long Width$offset() {
        return Width$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned short Width
     * }
     */
    public static short Width(MemorySegment struct) {
        return struct.get(Width$LAYOUT, Width$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned short Width
     * }
     */
    public static void Width(MemorySegment struct, short fieldValue) {
        struct.set(Width$LAYOUT, Width$OFFSET, fieldValue);
    }

    private static final OfShort Height$LAYOUT = (OfShort)$LAYOUT.select(groupElement("Height"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned short Height
     * }
     */
    public static final OfShort Height$layout() {
        return Height$LAYOUT;
    }

    private static final long Height$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned short Height
     * }
     */
    public static final long Height$offset() {
        return Height$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned short Height
     * }
     */
    public static short Height(MemorySegment struct) {
        return struct.get(Height$LAYOUT, Height$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned short Height
     * }
     */
    public static void Height(MemorySegment struct, short fieldValue) {
        struct.set(Height$LAYOUT, Height$OFFSET, fieldValue);
    }

    private static final OfShort X$LAYOUT = (OfShort)$LAYOUT.select(groupElement("X"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned short X
     * }
     */
    public static final OfShort X$layout() {
        return X$LAYOUT;
    }

    private static final long X$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned short X
     * }
     */
    public static final long X$offset() {
        return X$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned short X
     * }
     */
    public static short X(MemorySegment struct) {
        return struct.get(X$LAYOUT, X$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned short X
     * }
     */
    public static void X(MemorySegment struct, short fieldValue) {
        struct.set(X$LAYOUT, X$OFFSET, fieldValue);
    }

    private static final OfShort Y$LAYOUT = (OfShort)$LAYOUT.select(groupElement("Y"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned short Y
     * }
     */
    public static final OfShort Y$layout() {
        return Y$LAYOUT;
    }

    private static final long Y$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned short Y
     * }
     */
    public static final long Y$offset() {
        return Y$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned short Y
     * }
     */
    public static short Y(MemorySegment struct) {
        return struct.get(Y$LAYOUT, Y$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned short Y
     * }
     */
    public static void Y(MemorySegment struct, short fieldValue) {
        struct.set(Y$LAYOUT, Y$OFFSET, fieldValue);
    }

    private static final OfInt GlyphID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("GlyphID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int GlyphID
     * }
     */
    public static final OfInt GlyphID$layout() {
        return GlyphID$LAYOUT;
    }

    private static final long GlyphID$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int GlyphID
     * }
     */
    public static final long GlyphID$offset() {
        return GlyphID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int GlyphID
     * }
     */
    public static int GlyphID(MemorySegment struct) {
        return struct.get(GlyphID$LAYOUT, GlyphID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int GlyphID
     * }
     */
    public static void GlyphID(MemorySegment struct, int fieldValue) {
        struct.set(GlyphID$LAYOUT, GlyphID$OFFSET, fieldValue);
    }

    private static final OfFloat GlyphAdvanceX$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("GlyphAdvanceX"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float GlyphAdvanceX
     * }
     */
    public static final OfFloat GlyphAdvanceX$layout() {
        return GlyphAdvanceX$LAYOUT;
    }

    private static final long GlyphAdvanceX$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float GlyphAdvanceX
     * }
     */
    public static final long GlyphAdvanceX$offset() {
        return GlyphAdvanceX$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float GlyphAdvanceX
     * }
     */
    public static float GlyphAdvanceX(MemorySegment struct) {
        return struct.get(GlyphAdvanceX$LAYOUT, GlyphAdvanceX$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float GlyphAdvanceX
     * }
     */
    public static void GlyphAdvanceX(MemorySegment struct, float fieldValue) {
        struct.set(GlyphAdvanceX$LAYOUT, GlyphAdvanceX$OFFSET, fieldValue);
    }

    private static final GroupLayout GlyphOffset$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("GlyphOffset"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 GlyphOffset
     * }
     */
    public static final GroupLayout GlyphOffset$layout() {
        return GlyphOffset$LAYOUT;
    }

    private static final long GlyphOffset$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 GlyphOffset
     * }
     */
    public static final long GlyphOffset$offset() {
        return GlyphOffset$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 GlyphOffset
     * }
     */
    public static MemorySegment GlyphOffset(MemorySegment struct) {
        return struct.asSlice(GlyphOffset$OFFSET, GlyphOffset$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 GlyphOffset
     * }
     */
    public static void GlyphOffset(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, GlyphOffset$OFFSET, GlyphOffset$LAYOUT.byteSize());
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

    private static final long Font$OFFSET = 24;

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
