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
 * struct ImFontGlyph {
 *     unsigned int Colored : 1;
 *     unsigned int Visible : 1;
 *     unsigned int Codepoint : 30;
 *     float AdvanceX;
 *     float X0;
 *     float Y0;
 *     float X1;
 *     float Y1;
 *     float U0;
 *     float V0;
 *     float U1;
 *     float V1;
 * }
 * }
 */
public class ImFontGlyph {

    ImFontGlyph() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.paddingLayout(4),
        imgui.C_FLOAT.withName("AdvanceX"),
        imgui.C_FLOAT.withName("X0"),
        imgui.C_FLOAT.withName("Y0"),
        imgui.C_FLOAT.withName("X1"),
        imgui.C_FLOAT.withName("Y1"),
        imgui.C_FLOAT.withName("U0"),
        imgui.C_FLOAT.withName("V0"),
        imgui.C_FLOAT.withName("U1"),
        imgui.C_FLOAT.withName("V1")
    ).withName("ImFontGlyph");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfFloat AdvanceX$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("AdvanceX"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float AdvanceX
     * }
     */
    public static final OfFloat AdvanceX$layout() {
        return AdvanceX$LAYOUT;
    }

    private static final long AdvanceX$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float AdvanceX
     * }
     */
    public static final long AdvanceX$offset() {
        return AdvanceX$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float AdvanceX
     * }
     */
    public static float AdvanceX(MemorySegment struct) {
        return struct.get(AdvanceX$LAYOUT, AdvanceX$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float AdvanceX
     * }
     */
    public static void AdvanceX(MemorySegment struct, float fieldValue) {
        struct.set(AdvanceX$LAYOUT, AdvanceX$OFFSET, fieldValue);
    }

    private static final OfFloat X0$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("X0"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float X0
     * }
     */
    public static final OfFloat X0$layout() {
        return X0$LAYOUT;
    }

    private static final long X0$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float X0
     * }
     */
    public static final long X0$offset() {
        return X0$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float X0
     * }
     */
    public static float X0(MemorySegment struct) {
        return struct.get(X0$LAYOUT, X0$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float X0
     * }
     */
    public static void X0(MemorySegment struct, float fieldValue) {
        struct.set(X0$LAYOUT, X0$OFFSET, fieldValue);
    }

    private static final OfFloat Y0$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("Y0"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float Y0
     * }
     */
    public static final OfFloat Y0$layout() {
        return Y0$LAYOUT;
    }

    private static final long Y0$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float Y0
     * }
     */
    public static final long Y0$offset() {
        return Y0$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float Y0
     * }
     */
    public static float Y0(MemorySegment struct) {
        return struct.get(Y0$LAYOUT, Y0$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float Y0
     * }
     */
    public static void Y0(MemorySegment struct, float fieldValue) {
        struct.set(Y0$LAYOUT, Y0$OFFSET, fieldValue);
    }

    private static final OfFloat X1$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("X1"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float X1
     * }
     */
    public static final OfFloat X1$layout() {
        return X1$LAYOUT;
    }

    private static final long X1$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float X1
     * }
     */
    public static final long X1$offset() {
        return X1$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float X1
     * }
     */
    public static float X1(MemorySegment struct) {
        return struct.get(X1$LAYOUT, X1$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float X1
     * }
     */
    public static void X1(MemorySegment struct, float fieldValue) {
        struct.set(X1$LAYOUT, X1$OFFSET, fieldValue);
    }

    private static final OfFloat Y1$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("Y1"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float Y1
     * }
     */
    public static final OfFloat Y1$layout() {
        return Y1$LAYOUT;
    }

    private static final long Y1$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float Y1
     * }
     */
    public static final long Y1$offset() {
        return Y1$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float Y1
     * }
     */
    public static float Y1(MemorySegment struct) {
        return struct.get(Y1$LAYOUT, Y1$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float Y1
     * }
     */
    public static void Y1(MemorySegment struct, float fieldValue) {
        struct.set(Y1$LAYOUT, Y1$OFFSET, fieldValue);
    }

    private static final OfFloat U0$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("U0"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float U0
     * }
     */
    public static final OfFloat U0$layout() {
        return U0$LAYOUT;
    }

    private static final long U0$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float U0
     * }
     */
    public static final long U0$offset() {
        return U0$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float U0
     * }
     */
    public static float U0(MemorySegment struct) {
        return struct.get(U0$LAYOUT, U0$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float U0
     * }
     */
    public static void U0(MemorySegment struct, float fieldValue) {
        struct.set(U0$LAYOUT, U0$OFFSET, fieldValue);
    }

    private static final OfFloat V0$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("V0"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float V0
     * }
     */
    public static final OfFloat V0$layout() {
        return V0$LAYOUT;
    }

    private static final long V0$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float V0
     * }
     */
    public static final long V0$offset() {
        return V0$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float V0
     * }
     */
    public static float V0(MemorySegment struct) {
        return struct.get(V0$LAYOUT, V0$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float V0
     * }
     */
    public static void V0(MemorySegment struct, float fieldValue) {
        struct.set(V0$LAYOUT, V0$OFFSET, fieldValue);
    }

    private static final OfFloat U1$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("U1"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float U1
     * }
     */
    public static final OfFloat U1$layout() {
        return U1$LAYOUT;
    }

    private static final long U1$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float U1
     * }
     */
    public static final long U1$offset() {
        return U1$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float U1
     * }
     */
    public static float U1(MemorySegment struct) {
        return struct.get(U1$LAYOUT, U1$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float U1
     * }
     */
    public static void U1(MemorySegment struct, float fieldValue) {
        struct.set(U1$LAYOUT, U1$OFFSET, fieldValue);
    }

    private static final OfFloat V1$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("V1"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float V1
     * }
     */
    public static final OfFloat V1$layout() {
        return V1$LAYOUT;
    }

    private static final long V1$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float V1
     * }
     */
    public static final long V1$offset() {
        return V1$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float V1
     * }
     */
    public static float V1(MemorySegment struct) {
        return struct.get(V1$LAYOUT, V1$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float V1
     * }
     */
    public static void V1(MemorySegment struct, float fieldValue) {
        struct.set(V1$LAYOUT, V1$OFFSET, fieldValue);
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
