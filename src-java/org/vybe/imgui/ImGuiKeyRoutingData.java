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
 * struct ImGuiKeyRoutingData {
 *     ImGuiKeyRoutingIndex NextEntryIndex;
 *     ImU16 Mods;
 *     ImU8 RoutingCurrScore;
 *     ImU8 RoutingNextScore;
 *     ImGuiID RoutingCurr;
 *     ImGuiID RoutingNext;
 * }
 * }
 */
public class ImGuiKeyRoutingData {

    ImGuiKeyRoutingData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_SHORT.withName("NextEntryIndex"),
        imgui.C_SHORT.withName("Mods"),
        imgui.C_CHAR.withName("RoutingCurrScore"),
        imgui.C_CHAR.withName("RoutingNextScore"),
        MemoryLayout.paddingLayout(2),
        imgui.C_INT.withName("RoutingCurr"),
        imgui.C_INT.withName("RoutingNext")
    ).withName("ImGuiKeyRoutingData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfShort NextEntryIndex$LAYOUT = (OfShort)$LAYOUT.select(groupElement("NextEntryIndex"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiKeyRoutingIndex NextEntryIndex
     * }
     */
    public static final OfShort NextEntryIndex$layout() {
        return NextEntryIndex$LAYOUT;
    }

    private static final long NextEntryIndex$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiKeyRoutingIndex NextEntryIndex
     * }
     */
    public static final long NextEntryIndex$offset() {
        return NextEntryIndex$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiKeyRoutingIndex NextEntryIndex
     * }
     */
    public static short NextEntryIndex(MemorySegment struct) {
        return struct.get(NextEntryIndex$LAYOUT, NextEntryIndex$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiKeyRoutingIndex NextEntryIndex
     * }
     */
    public static void NextEntryIndex(MemorySegment struct, short fieldValue) {
        struct.set(NextEntryIndex$LAYOUT, NextEntryIndex$OFFSET, fieldValue);
    }

    private static final OfShort Mods$LAYOUT = (OfShort)$LAYOUT.select(groupElement("Mods"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU16 Mods
     * }
     */
    public static final OfShort Mods$layout() {
        return Mods$LAYOUT;
    }

    private static final long Mods$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU16 Mods
     * }
     */
    public static final long Mods$offset() {
        return Mods$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU16 Mods
     * }
     */
    public static short Mods(MemorySegment struct) {
        return struct.get(Mods$LAYOUT, Mods$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU16 Mods
     * }
     */
    public static void Mods(MemorySegment struct, short fieldValue) {
        struct.set(Mods$LAYOUT, Mods$OFFSET, fieldValue);
    }

    private static final OfByte RoutingCurrScore$LAYOUT = (OfByte)$LAYOUT.select(groupElement("RoutingCurrScore"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU8 RoutingCurrScore
     * }
     */
    public static final OfByte RoutingCurrScore$layout() {
        return RoutingCurrScore$LAYOUT;
    }

    private static final long RoutingCurrScore$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU8 RoutingCurrScore
     * }
     */
    public static final long RoutingCurrScore$offset() {
        return RoutingCurrScore$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU8 RoutingCurrScore
     * }
     */
    public static byte RoutingCurrScore(MemorySegment struct) {
        return struct.get(RoutingCurrScore$LAYOUT, RoutingCurrScore$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU8 RoutingCurrScore
     * }
     */
    public static void RoutingCurrScore(MemorySegment struct, byte fieldValue) {
        struct.set(RoutingCurrScore$LAYOUT, RoutingCurrScore$OFFSET, fieldValue);
    }

    private static final OfByte RoutingNextScore$LAYOUT = (OfByte)$LAYOUT.select(groupElement("RoutingNextScore"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU8 RoutingNextScore
     * }
     */
    public static final OfByte RoutingNextScore$layout() {
        return RoutingNextScore$LAYOUT;
    }

    private static final long RoutingNextScore$OFFSET = 5;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU8 RoutingNextScore
     * }
     */
    public static final long RoutingNextScore$offset() {
        return RoutingNextScore$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU8 RoutingNextScore
     * }
     */
    public static byte RoutingNextScore(MemorySegment struct) {
        return struct.get(RoutingNextScore$LAYOUT, RoutingNextScore$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU8 RoutingNextScore
     * }
     */
    public static void RoutingNextScore(MemorySegment struct, byte fieldValue) {
        struct.set(RoutingNextScore$LAYOUT, RoutingNextScore$OFFSET, fieldValue);
    }

    private static final OfInt RoutingCurr$LAYOUT = (OfInt)$LAYOUT.select(groupElement("RoutingCurr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID RoutingCurr
     * }
     */
    public static final OfInt RoutingCurr$layout() {
        return RoutingCurr$LAYOUT;
    }

    private static final long RoutingCurr$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID RoutingCurr
     * }
     */
    public static final long RoutingCurr$offset() {
        return RoutingCurr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID RoutingCurr
     * }
     */
    public static int RoutingCurr(MemorySegment struct) {
        return struct.get(RoutingCurr$LAYOUT, RoutingCurr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID RoutingCurr
     * }
     */
    public static void RoutingCurr(MemorySegment struct, int fieldValue) {
        struct.set(RoutingCurr$LAYOUT, RoutingCurr$OFFSET, fieldValue);
    }

    private static final OfInt RoutingNext$LAYOUT = (OfInt)$LAYOUT.select(groupElement("RoutingNext"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID RoutingNext
     * }
     */
    public static final OfInt RoutingNext$layout() {
        return RoutingNext$LAYOUT;
    }

    private static final long RoutingNext$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID RoutingNext
     * }
     */
    public static final long RoutingNext$offset() {
        return RoutingNext$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID RoutingNext
     * }
     */
    public static int RoutingNext(MemorySegment struct) {
        return struct.get(RoutingNext$LAYOUT, RoutingNext$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID RoutingNext
     * }
     */
    public static void RoutingNext(MemorySegment struct, int fieldValue) {
        struct.set(RoutingNext$LAYOUT, RoutingNext$OFFSET, fieldValue);
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
