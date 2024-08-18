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
 * struct ImDrawChannel {
 *     ImVector_ImDrawCmd _CmdBuffer;
 *     ImVector_ImDrawIdx _IdxBuffer;
 * }
 * }
 */
public class ImDrawChannel {

    ImDrawChannel() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ImVector_ImDrawCmd.layout().withName("_CmdBuffer"),
        ImVector_ImDrawIdx.layout().withName("_IdxBuffer")
    ).withName("ImDrawChannel");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout _CmdBuffer$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("_CmdBuffer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_ImDrawCmd _CmdBuffer
     * }
     */
    public static final GroupLayout _CmdBuffer$layout() {
        return _CmdBuffer$LAYOUT;
    }

    private static final long _CmdBuffer$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_ImDrawCmd _CmdBuffer
     * }
     */
    public static final long _CmdBuffer$offset() {
        return _CmdBuffer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_ImDrawCmd _CmdBuffer
     * }
     */
    public static MemorySegment _CmdBuffer(MemorySegment struct) {
        return struct.asSlice(_CmdBuffer$OFFSET, _CmdBuffer$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_ImDrawCmd _CmdBuffer
     * }
     */
    public static void _CmdBuffer(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, _CmdBuffer$OFFSET, _CmdBuffer$LAYOUT.byteSize());
    }

    private static final GroupLayout _IdxBuffer$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("_IdxBuffer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVector_ImDrawIdx _IdxBuffer
     * }
     */
    public static final GroupLayout _IdxBuffer$layout() {
        return _IdxBuffer$LAYOUT;
    }

    private static final long _IdxBuffer$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVector_ImDrawIdx _IdxBuffer
     * }
     */
    public static final long _IdxBuffer$offset() {
        return _IdxBuffer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVector_ImDrawIdx _IdxBuffer
     * }
     */
    public static MemorySegment _IdxBuffer(MemorySegment struct) {
        return struct.asSlice(_IdxBuffer$OFFSET, _IdxBuffer$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVector_ImDrawIdx _IdxBuffer
     * }
     */
    public static void _IdxBuffer(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, _IdxBuffer$OFFSET, _IdxBuffer$LAYOUT.byteSize());
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
