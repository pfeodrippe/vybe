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
 * struct ImGuiStackSizes {
 *     short SizeOfIDStack;
 *     short SizeOfColorStack;
 *     short SizeOfStyleVarStack;
 *     short SizeOfFontStack;
 *     short SizeOfFocusScopeStack;
 *     short SizeOfGroupStack;
 *     short SizeOfItemFlagsStack;
 *     short SizeOfBeginPopupStack;
 *     short SizeOfDisabledStack;
 * }
 * }
 */
public class ImGuiStackSizes {

    ImGuiStackSizes() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_SHORT.withName("SizeOfIDStack"),
        imgui.C_SHORT.withName("SizeOfColorStack"),
        imgui.C_SHORT.withName("SizeOfStyleVarStack"),
        imgui.C_SHORT.withName("SizeOfFontStack"),
        imgui.C_SHORT.withName("SizeOfFocusScopeStack"),
        imgui.C_SHORT.withName("SizeOfGroupStack"),
        imgui.C_SHORT.withName("SizeOfItemFlagsStack"),
        imgui.C_SHORT.withName("SizeOfBeginPopupStack"),
        imgui.C_SHORT.withName("SizeOfDisabledStack")
    ).withName("ImGuiStackSizes");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfShort SizeOfIDStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfIDStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfIDStack
     * }
     */
    public static final OfShort SizeOfIDStack$layout() {
        return SizeOfIDStack$LAYOUT;
    }

    private static final long SizeOfIDStack$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfIDStack
     * }
     */
    public static final long SizeOfIDStack$offset() {
        return SizeOfIDStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfIDStack
     * }
     */
    public static short SizeOfIDStack(MemorySegment struct) {
        return struct.get(SizeOfIDStack$LAYOUT, SizeOfIDStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfIDStack
     * }
     */
    public static void SizeOfIDStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfIDStack$LAYOUT, SizeOfIDStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfColorStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfColorStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfColorStack
     * }
     */
    public static final OfShort SizeOfColorStack$layout() {
        return SizeOfColorStack$LAYOUT;
    }

    private static final long SizeOfColorStack$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfColorStack
     * }
     */
    public static final long SizeOfColorStack$offset() {
        return SizeOfColorStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfColorStack
     * }
     */
    public static short SizeOfColorStack(MemorySegment struct) {
        return struct.get(SizeOfColorStack$LAYOUT, SizeOfColorStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfColorStack
     * }
     */
    public static void SizeOfColorStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfColorStack$LAYOUT, SizeOfColorStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfStyleVarStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfStyleVarStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfStyleVarStack
     * }
     */
    public static final OfShort SizeOfStyleVarStack$layout() {
        return SizeOfStyleVarStack$LAYOUT;
    }

    private static final long SizeOfStyleVarStack$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfStyleVarStack
     * }
     */
    public static final long SizeOfStyleVarStack$offset() {
        return SizeOfStyleVarStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfStyleVarStack
     * }
     */
    public static short SizeOfStyleVarStack(MemorySegment struct) {
        return struct.get(SizeOfStyleVarStack$LAYOUT, SizeOfStyleVarStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfStyleVarStack
     * }
     */
    public static void SizeOfStyleVarStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfStyleVarStack$LAYOUT, SizeOfStyleVarStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfFontStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfFontStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfFontStack
     * }
     */
    public static final OfShort SizeOfFontStack$layout() {
        return SizeOfFontStack$LAYOUT;
    }

    private static final long SizeOfFontStack$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfFontStack
     * }
     */
    public static final long SizeOfFontStack$offset() {
        return SizeOfFontStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfFontStack
     * }
     */
    public static short SizeOfFontStack(MemorySegment struct) {
        return struct.get(SizeOfFontStack$LAYOUT, SizeOfFontStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfFontStack
     * }
     */
    public static void SizeOfFontStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfFontStack$LAYOUT, SizeOfFontStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfFocusScopeStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfFocusScopeStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfFocusScopeStack
     * }
     */
    public static final OfShort SizeOfFocusScopeStack$layout() {
        return SizeOfFocusScopeStack$LAYOUT;
    }

    private static final long SizeOfFocusScopeStack$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfFocusScopeStack
     * }
     */
    public static final long SizeOfFocusScopeStack$offset() {
        return SizeOfFocusScopeStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfFocusScopeStack
     * }
     */
    public static short SizeOfFocusScopeStack(MemorySegment struct) {
        return struct.get(SizeOfFocusScopeStack$LAYOUT, SizeOfFocusScopeStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfFocusScopeStack
     * }
     */
    public static void SizeOfFocusScopeStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfFocusScopeStack$LAYOUT, SizeOfFocusScopeStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfGroupStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfGroupStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfGroupStack
     * }
     */
    public static final OfShort SizeOfGroupStack$layout() {
        return SizeOfGroupStack$LAYOUT;
    }

    private static final long SizeOfGroupStack$OFFSET = 10;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfGroupStack
     * }
     */
    public static final long SizeOfGroupStack$offset() {
        return SizeOfGroupStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfGroupStack
     * }
     */
    public static short SizeOfGroupStack(MemorySegment struct) {
        return struct.get(SizeOfGroupStack$LAYOUT, SizeOfGroupStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfGroupStack
     * }
     */
    public static void SizeOfGroupStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfGroupStack$LAYOUT, SizeOfGroupStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfItemFlagsStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfItemFlagsStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfItemFlagsStack
     * }
     */
    public static final OfShort SizeOfItemFlagsStack$layout() {
        return SizeOfItemFlagsStack$LAYOUT;
    }

    private static final long SizeOfItemFlagsStack$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfItemFlagsStack
     * }
     */
    public static final long SizeOfItemFlagsStack$offset() {
        return SizeOfItemFlagsStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfItemFlagsStack
     * }
     */
    public static short SizeOfItemFlagsStack(MemorySegment struct) {
        return struct.get(SizeOfItemFlagsStack$LAYOUT, SizeOfItemFlagsStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfItemFlagsStack
     * }
     */
    public static void SizeOfItemFlagsStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfItemFlagsStack$LAYOUT, SizeOfItemFlagsStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfBeginPopupStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfBeginPopupStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfBeginPopupStack
     * }
     */
    public static final OfShort SizeOfBeginPopupStack$layout() {
        return SizeOfBeginPopupStack$LAYOUT;
    }

    private static final long SizeOfBeginPopupStack$OFFSET = 14;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfBeginPopupStack
     * }
     */
    public static final long SizeOfBeginPopupStack$offset() {
        return SizeOfBeginPopupStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfBeginPopupStack
     * }
     */
    public static short SizeOfBeginPopupStack(MemorySegment struct) {
        return struct.get(SizeOfBeginPopupStack$LAYOUT, SizeOfBeginPopupStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfBeginPopupStack
     * }
     */
    public static void SizeOfBeginPopupStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfBeginPopupStack$LAYOUT, SizeOfBeginPopupStack$OFFSET, fieldValue);
    }

    private static final OfShort SizeOfDisabledStack$LAYOUT = (OfShort)$LAYOUT.select(groupElement("SizeOfDisabledStack"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * short SizeOfDisabledStack
     * }
     */
    public static final OfShort SizeOfDisabledStack$layout() {
        return SizeOfDisabledStack$LAYOUT;
    }

    private static final long SizeOfDisabledStack$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * short SizeOfDisabledStack
     * }
     */
    public static final long SizeOfDisabledStack$offset() {
        return SizeOfDisabledStack$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * short SizeOfDisabledStack
     * }
     */
    public static short SizeOfDisabledStack(MemorySegment struct) {
        return struct.get(SizeOfDisabledStack$LAYOUT, SizeOfDisabledStack$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * short SizeOfDisabledStack
     * }
     */
    public static void SizeOfDisabledStack(MemorySegment struct, short fieldValue) {
        struct.set(SizeOfDisabledStack$LAYOUT, SizeOfDisabledStack$OFFSET, fieldValue);
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
