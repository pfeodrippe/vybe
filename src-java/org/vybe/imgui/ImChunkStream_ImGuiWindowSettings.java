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
 * struct ImChunkStream_ImGuiWindowSettings {
 *     ImVector_char Buf;
 * }
 * }
 */
public class ImChunkStream_ImGuiWindowSettings {

    ImChunkStream_ImGuiWindowSettings() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ImVector_char.layout().withName("Buf")
    ).withName("ImChunkStream_ImGuiWindowSettings");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout Buf$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("Buf"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_char Buf
     * }
     */
    public static final GroupLayout Buf$layout() {
        return Buf$LAYOUT;
    }

    private static final long Buf$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_char Buf
     * }
     */
    public static final long Buf$offset() {
        return Buf$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_char Buf
     * }
     */
    public static MemorySegment Buf(MemorySegment struct) {
        return struct.asSlice(Buf$OFFSET, Buf$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_char Buf
     * }
     */
    public static void Buf(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, Buf$OFFSET, Buf$LAYOUT.byteSize());
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

