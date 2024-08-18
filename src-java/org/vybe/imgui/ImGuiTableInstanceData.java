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
 * struct ImGuiTableInstanceData {
 *     ImGuiID TableInstanceID;
 *     float LastOuterHeight;
 *     float LastTopHeadersRowHeight;
 *     float LastFrozenHeight;
 *     int HoveredRowLast;
 *     int HoveredRowNext;
 * }
 * }
 */
public class ImGuiTableInstanceData {

    ImGuiTableInstanceData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("TableInstanceID"),
        imgui.C_FLOAT.withName("LastOuterHeight"),
        imgui.C_FLOAT.withName("LastTopHeadersRowHeight"),
        imgui.C_FLOAT.withName("LastFrozenHeight"),
        imgui.C_INT.withName("HoveredRowLast"),
        imgui.C_INT.withName("HoveredRowNext")
    ).withName("ImGuiTableInstanceData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt TableInstanceID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("TableInstanceID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID TableInstanceID
     * }
     */
    public static final OfInt TableInstanceID$layout() {
        return TableInstanceID$LAYOUT;
    }

    private static final long TableInstanceID$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID TableInstanceID
     * }
     */
    public static final long TableInstanceID$offset() {
        return TableInstanceID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID TableInstanceID
     * }
     */
    public static int TableInstanceID(MemorySegment struct) {
        return struct.get(TableInstanceID$LAYOUT, TableInstanceID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID TableInstanceID
     * }
     */
    public static void TableInstanceID(MemorySegment struct, int fieldValue) {
        struct.set(TableInstanceID$LAYOUT, TableInstanceID$OFFSET, fieldValue);
    }

    private static final OfFloat LastOuterHeight$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("LastOuterHeight"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float LastOuterHeight
     * }
     */
    public static final OfFloat LastOuterHeight$layout() {
        return LastOuterHeight$LAYOUT;
    }

    private static final long LastOuterHeight$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float LastOuterHeight
     * }
     */
    public static final long LastOuterHeight$offset() {
        return LastOuterHeight$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float LastOuterHeight
     * }
     */
    public static float LastOuterHeight(MemorySegment struct) {
        return struct.get(LastOuterHeight$LAYOUT, LastOuterHeight$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float LastOuterHeight
     * }
     */
    public static void LastOuterHeight(MemorySegment struct, float fieldValue) {
        struct.set(LastOuterHeight$LAYOUT, LastOuterHeight$OFFSET, fieldValue);
    }

    private static final OfFloat LastTopHeadersRowHeight$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("LastTopHeadersRowHeight"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float LastTopHeadersRowHeight
     * }
     */
    public static final OfFloat LastTopHeadersRowHeight$layout() {
        return LastTopHeadersRowHeight$LAYOUT;
    }

    private static final long LastTopHeadersRowHeight$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float LastTopHeadersRowHeight
     * }
     */
    public static final long LastTopHeadersRowHeight$offset() {
        return LastTopHeadersRowHeight$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float LastTopHeadersRowHeight
     * }
     */
    public static float LastTopHeadersRowHeight(MemorySegment struct) {
        return struct.get(LastTopHeadersRowHeight$LAYOUT, LastTopHeadersRowHeight$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float LastTopHeadersRowHeight
     * }
     */
    public static void LastTopHeadersRowHeight(MemorySegment struct, float fieldValue) {
        struct.set(LastTopHeadersRowHeight$LAYOUT, LastTopHeadersRowHeight$OFFSET, fieldValue);
    }

    private static final OfFloat LastFrozenHeight$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("LastFrozenHeight"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float LastFrozenHeight
     * }
     */
    public static final OfFloat LastFrozenHeight$layout() {
        return LastFrozenHeight$LAYOUT;
    }

    private static final long LastFrozenHeight$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float LastFrozenHeight
     * }
     */
    public static final long LastFrozenHeight$offset() {
        return LastFrozenHeight$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float LastFrozenHeight
     * }
     */
    public static float LastFrozenHeight(MemorySegment struct) {
        return struct.get(LastFrozenHeight$LAYOUT, LastFrozenHeight$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float LastFrozenHeight
     * }
     */
    public static void LastFrozenHeight(MemorySegment struct, float fieldValue) {
        struct.set(LastFrozenHeight$LAYOUT, LastFrozenHeight$OFFSET, fieldValue);
    }

    private static final OfInt HoveredRowLast$LAYOUT = (OfInt)$LAYOUT.select(groupElement("HoveredRowLast"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int HoveredRowLast
     * }
     */
    public static final OfInt HoveredRowLast$layout() {
        return HoveredRowLast$LAYOUT;
    }

    private static final long HoveredRowLast$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int HoveredRowLast
     * }
     */
    public static final long HoveredRowLast$offset() {
        return HoveredRowLast$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int HoveredRowLast
     * }
     */
    public static int HoveredRowLast(MemorySegment struct) {
        return struct.get(HoveredRowLast$LAYOUT, HoveredRowLast$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int HoveredRowLast
     * }
     */
    public static void HoveredRowLast(MemorySegment struct, int fieldValue) {
        struct.set(HoveredRowLast$LAYOUT, HoveredRowLast$OFFSET, fieldValue);
    }

    private static final OfInt HoveredRowNext$LAYOUT = (OfInt)$LAYOUT.select(groupElement("HoveredRowNext"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int HoveredRowNext
     * }
     */
    public static final OfInt HoveredRowNext$layout() {
        return HoveredRowNext$LAYOUT;
    }

    private static final long HoveredRowNext$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int HoveredRowNext
     * }
     */
    public static final long HoveredRowNext$offset() {
        return HoveredRowNext$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int HoveredRowNext
     * }
     */
    public static int HoveredRowNext(MemorySegment struct) {
        return struct.get(HoveredRowNext$LAYOUT, HoveredRowNext$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int HoveredRowNext
     * }
     */
    public static void HoveredRowNext(MemorySegment struct, int fieldValue) {
        struct.set(HoveredRowNext$LAYOUT, HoveredRowNext$OFFSET, fieldValue);
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
