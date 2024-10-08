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
 * struct ImGuiKeyOwnerData {
 *     ImGuiID OwnerCurr;
 *     ImGuiID OwnerNext;
 *     bool LockThisFrame;
 *     bool LockUntilRelease;
 * }
 * }
 */
public class ImGuiKeyOwnerData {

    ImGuiKeyOwnerData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("OwnerCurr"),
        imgui.C_INT.withName("OwnerNext"),
        imgui.C_BOOL.withName("LockThisFrame"),
        imgui.C_BOOL.withName("LockUntilRelease"),
        MemoryLayout.paddingLayout(2)
    ).withName("ImGuiKeyOwnerData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt OwnerCurr$LAYOUT = (OfInt)$LAYOUT.select(groupElement("OwnerCurr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID OwnerCurr
     * }
     */
    public static final OfInt OwnerCurr$layout() {
        return OwnerCurr$LAYOUT;
    }

    private static final long OwnerCurr$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID OwnerCurr
     * }
     */
    public static final long OwnerCurr$offset() {
        return OwnerCurr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID OwnerCurr
     * }
     */
    public static int OwnerCurr(MemorySegment struct) {
        return struct.get(OwnerCurr$LAYOUT, OwnerCurr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID OwnerCurr
     * }
     */
    public static void OwnerCurr(MemorySegment struct, int fieldValue) {
        struct.set(OwnerCurr$LAYOUT, OwnerCurr$OFFSET, fieldValue);
    }

    private static final OfInt OwnerNext$LAYOUT = (OfInt)$LAYOUT.select(groupElement("OwnerNext"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID OwnerNext
     * }
     */
    public static final OfInt OwnerNext$layout() {
        return OwnerNext$LAYOUT;
    }

    private static final long OwnerNext$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID OwnerNext
     * }
     */
    public static final long OwnerNext$offset() {
        return OwnerNext$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID OwnerNext
     * }
     */
    public static int OwnerNext(MemorySegment struct) {
        return struct.get(OwnerNext$LAYOUT, OwnerNext$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID OwnerNext
     * }
     */
    public static void OwnerNext(MemorySegment struct, int fieldValue) {
        struct.set(OwnerNext$LAYOUT, OwnerNext$OFFSET, fieldValue);
    }

    private static final OfBoolean LockThisFrame$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("LockThisFrame"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool LockThisFrame
     * }
     */
    public static final OfBoolean LockThisFrame$layout() {
        return LockThisFrame$LAYOUT;
    }

    private static final long LockThisFrame$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool LockThisFrame
     * }
     */
    public static final long LockThisFrame$offset() {
        return LockThisFrame$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool LockThisFrame
     * }
     */
    public static boolean LockThisFrame(MemorySegment struct) {
        return struct.get(LockThisFrame$LAYOUT, LockThisFrame$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool LockThisFrame
     * }
     */
    public static void LockThisFrame(MemorySegment struct, boolean fieldValue) {
        struct.set(LockThisFrame$LAYOUT, LockThisFrame$OFFSET, fieldValue);
    }

    private static final OfBoolean LockUntilRelease$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("LockUntilRelease"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool LockUntilRelease
     * }
     */
    public static final OfBoolean LockUntilRelease$layout() {
        return LockUntilRelease$LAYOUT;
    }

    private static final long LockUntilRelease$OFFSET = 9;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool LockUntilRelease
     * }
     */
    public static final long LockUntilRelease$offset() {
        return LockUntilRelease$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool LockUntilRelease
     * }
     */
    public static boolean LockUntilRelease(MemorySegment struct) {
        return struct.get(LockUntilRelease$LAYOUT, LockUntilRelease$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool LockUntilRelease
     * }
     */
    public static void LockUntilRelease(MemorySegment struct, boolean fieldValue) {
        struct.set(LockUntilRelease$LAYOUT, LockUntilRelease$OFFSET, fieldValue);
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

