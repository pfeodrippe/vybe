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
 * struct ImSpan_ImGuiTableColumn {
 *     ImGuiTableColumn *Data;
 *     ImGuiTableColumn *DataEnd;
 * }
 * }
 */
public class ImSpan_ImGuiTableColumn {

    ImSpan_ImGuiTableColumn() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("Data"),
        imgui.C_POINTER.withName("DataEnd")
    ).withName("ImSpan_ImGuiTableColumn");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout Data$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *Data
     * }
     */
    public static final AddressLayout Data$layout() {
        return Data$LAYOUT;
    }

    private static final long Data$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *Data
     * }
     */
    public static final long Data$offset() {
        return Data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *Data
     * }
     */
    public static MemorySegment Data(MemorySegment struct) {
        return struct.get(Data$LAYOUT, Data$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *Data
     * }
     */
    public static void Data(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Data$LAYOUT, Data$OFFSET, fieldValue);
    }

    private static final AddressLayout DataEnd$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("DataEnd"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *DataEnd
     * }
     */
    public static final AddressLayout DataEnd$layout() {
        return DataEnd$LAYOUT;
    }

    private static final long DataEnd$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *DataEnd
     * }
     */
    public static final long DataEnd$offset() {
        return DataEnd$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *DataEnd
     * }
     */
    public static MemorySegment DataEnd(MemorySegment struct) {
        return struct.get(DataEnd$LAYOUT, DataEnd$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTableColumn *DataEnd
     * }
     */
    public static void DataEnd(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(DataEnd$LAYOUT, DataEnd$OFFSET, fieldValue);
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

