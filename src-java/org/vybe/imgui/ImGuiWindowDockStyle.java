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
 * struct ImGuiWindowDockStyle {
 *     ImU32 Colors[8];
 * }
 * }
 */
public class ImGuiWindowDockStyle {

    ImGuiWindowDockStyle() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(8, imgui.C_INT).withName("Colors")
    ).withName("ImGuiWindowDockStyle");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final SequenceLayout Colors$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("Colors"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static final SequenceLayout Colors$layout() {
        return Colors$LAYOUT;
    }

    private static final long Colors$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static final long Colors$offset() {
        return Colors$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static MemorySegment Colors(MemorySegment struct) {
        return struct.asSlice(Colors$OFFSET, Colors$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static void Colors(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Colors$OFFSET, Colors$LAYOUT.byteSize());
    }

    private static long[] Colors$DIMS = { 8 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static long[] Colors$dimensions() {
        return Colors$DIMS;
    }
    private static final VarHandle Colors$ELEM_HANDLE = Colors$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static int Colors(MemorySegment struct, long index0) {
        return (int)Colors$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ImU32 Colors[8]
     * }
     */
    public static void Colors(MemorySegment struct, long index0, int fieldValue) {
        Colors$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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

