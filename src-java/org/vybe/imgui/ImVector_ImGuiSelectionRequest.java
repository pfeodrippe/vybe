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
 * struct ImVector_ImGuiSelectionRequest {
 *     int Size;
 *     int Capacity;
 *     ImGuiSelectionRequest *Data;
 * }
 * }
 */
public class ImVector_ImGuiSelectionRequest {

    ImVector_ImGuiSelectionRequest() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Size"),
        imgui.C_INT.withName("Capacity"),
        imgui.C_POINTER.withName("Data")
    ).withName("ImVector_ImGuiSelectionRequest");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt Size$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Size"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int Size
     * }
     */
    public static final OfInt Size$layout() {
        return Size$LAYOUT;
    }

    private static final long Size$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int Size
     * }
     */
    public static final long Size$offset() {
        return Size$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int Size
     * }
     */
    public static int Size(MemorySegment struct) {
        return struct.get(Size$LAYOUT, Size$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int Size
     * }
     */
    public static void Size(MemorySegment struct, int fieldValue) {
        struct.set(Size$LAYOUT, Size$OFFSET, fieldValue);
    }

    private static final OfInt Capacity$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Capacity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int Capacity
     * }
     */
    public static final OfInt Capacity$layout() {
        return Capacity$LAYOUT;
    }

    private static final long Capacity$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int Capacity
     * }
     */
    public static final long Capacity$offset() {
        return Capacity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int Capacity
     * }
     */
    public static int Capacity(MemorySegment struct) {
        return struct.get(Capacity$LAYOUT, Capacity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int Capacity
     * }
     */
    public static void Capacity(MemorySegment struct, int fieldValue) {
        struct.set(Capacity$LAYOUT, Capacity$OFFSET, fieldValue);
    }

    private static final AddressLayout Data$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiSelectionRequest *Data
     * }
     */
    public static final AddressLayout Data$layout() {
        return Data$LAYOUT;
    }

    private static final long Data$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiSelectionRequest *Data
     * }
     */
    public static final long Data$offset() {
        return Data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiSelectionRequest *Data
     * }
     */
    public static MemorySegment Data(MemorySegment struct) {
        return struct.get(Data$LAYOUT, Data$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiSelectionRequest *Data
     * }
     */
    public static void Data(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Data$LAYOUT, Data$OFFSET, fieldValue);
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
