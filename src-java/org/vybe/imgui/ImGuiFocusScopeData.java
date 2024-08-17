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
 * struct ImGuiFocusScopeData {
 *     ImGuiID ID;
 *     ImGuiID WindowID;
 * }
 * }
 */
public class ImGuiFocusScopeData {

    ImGuiFocusScopeData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("ID"),
        imgui.C_INT.withName("WindowID")
    ).withName("ImGuiFocusScopeData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
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

    private static final long ID$OFFSET = 0;

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

    private static final OfInt WindowID$LAYOUT = (OfInt)$LAYOUT.select(groupElement("WindowID"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID WindowID
     * }
     */
    public static final OfInt WindowID$layout() {
        return WindowID$LAYOUT;
    }

    private static final long WindowID$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID WindowID
     * }
     */
    public static final long WindowID$offset() {
        return WindowID$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID WindowID
     * }
     */
    public static int WindowID(MemorySegment struct) {
        return struct.get(WindowID$LAYOUT, WindowID$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID WindowID
     * }
     */
    public static void WindowID(MemorySegment struct, int fieldValue) {
        struct.set(WindowID$LAYOUT, WindowID$OFFSET, fieldValue);
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

