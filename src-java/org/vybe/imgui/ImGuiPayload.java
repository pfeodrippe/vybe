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
 * struct ImGuiPayload {
 *     void *Data;
 *     int DataSize;
 *     ImGuiID SourceId;
 *     ImGuiID SourceParentId;
 *     int DataFrameCount;
 *     char DataType[33];
 *     bool Preview;
 *     bool Delivery;
 * }
 * }
 */
public class ImGuiPayload {

    ImGuiPayload() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("Data"),
        imgui.C_INT.withName("DataSize"),
        imgui.C_INT.withName("SourceId"),
        imgui.C_INT.withName("SourceParentId"),
        imgui.C_INT.withName("DataFrameCount"),
        MemoryLayout.sequenceLayout(33, imgui.C_CHAR).withName("DataType"),
        imgui.C_BOOL.withName("Preview"),
        imgui.C_BOOL.withName("Delivery"),
        MemoryLayout.paddingLayout(5)
    ).withName("ImGuiPayload");

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
     * void *Data
     * }
     */
    public static final AddressLayout Data$layout() {
        return Data$LAYOUT;
    }

    private static final long Data$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *Data
     * }
     */
    public static final long Data$offset() {
        return Data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *Data
     * }
     */
    public static MemorySegment Data(MemorySegment struct) {
        return struct.get(Data$LAYOUT, Data$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *Data
     * }
     */
    public static void Data(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Data$LAYOUT, Data$OFFSET, fieldValue);
    }

    private static final OfInt DataSize$LAYOUT = (OfInt)$LAYOUT.select(groupElement("DataSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int DataSize
     * }
     */
    public static final OfInt DataSize$layout() {
        return DataSize$LAYOUT;
    }

    private static final long DataSize$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int DataSize
     * }
     */
    public static final long DataSize$offset() {
        return DataSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int DataSize
     * }
     */
    public static int DataSize(MemorySegment struct) {
        return struct.get(DataSize$LAYOUT, DataSize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int DataSize
     * }
     */
    public static void DataSize(MemorySegment struct, int fieldValue) {
        struct.set(DataSize$LAYOUT, DataSize$OFFSET, fieldValue);
    }

    private static final OfInt SourceId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SourceId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID SourceId
     * }
     */
    public static final OfInt SourceId$layout() {
        return SourceId$LAYOUT;
    }

    private static final long SourceId$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID SourceId
     * }
     */
    public static final long SourceId$offset() {
        return SourceId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID SourceId
     * }
     */
    public static int SourceId(MemorySegment struct) {
        return struct.get(SourceId$LAYOUT, SourceId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID SourceId
     * }
     */
    public static void SourceId(MemorySegment struct, int fieldValue) {
        struct.set(SourceId$LAYOUT, SourceId$OFFSET, fieldValue);
    }

    private static final OfInt SourceParentId$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SourceParentId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID SourceParentId
     * }
     */
    public static final OfInt SourceParentId$layout() {
        return SourceParentId$LAYOUT;
    }

    private static final long SourceParentId$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID SourceParentId
     * }
     */
    public static final long SourceParentId$offset() {
        return SourceParentId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID SourceParentId
     * }
     */
    public static int SourceParentId(MemorySegment struct) {
        return struct.get(SourceParentId$LAYOUT, SourceParentId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID SourceParentId
     * }
     */
    public static void SourceParentId(MemorySegment struct, int fieldValue) {
        struct.set(SourceParentId$LAYOUT, SourceParentId$OFFSET, fieldValue);
    }

    private static final OfInt DataFrameCount$LAYOUT = (OfInt)$LAYOUT.select(groupElement("DataFrameCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int DataFrameCount
     * }
     */
    public static final OfInt DataFrameCount$layout() {
        return DataFrameCount$LAYOUT;
    }

    private static final long DataFrameCount$OFFSET = 20;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int DataFrameCount
     * }
     */
    public static final long DataFrameCount$offset() {
        return DataFrameCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int DataFrameCount
     * }
     */
    public static int DataFrameCount(MemorySegment struct) {
        return struct.get(DataFrameCount$LAYOUT, DataFrameCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int DataFrameCount
     * }
     */
    public static void DataFrameCount(MemorySegment struct, int fieldValue) {
        struct.set(DataFrameCount$LAYOUT, DataFrameCount$OFFSET, fieldValue);
    }

    private static final SequenceLayout DataType$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("DataType"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static final SequenceLayout DataType$layout() {
        return DataType$LAYOUT;
    }

    private static final long DataType$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static final long DataType$offset() {
        return DataType$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static MemorySegment DataType(MemorySegment struct) {
        return struct.asSlice(DataType$OFFSET, DataType$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static void DataType(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, DataType$OFFSET, DataType$LAYOUT.byteSize());
    }

    private static long[] DataType$DIMS = { 33 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static long[] DataType$dimensions() {
        return DataType$DIMS;
    }
    private static final VarHandle DataType$ELEM_HANDLE = DataType$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static byte DataType(MemorySegment struct, long index0) {
        return (byte)DataType$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * char DataType[33]
     * }
     */
    public static void DataType(MemorySegment struct, long index0, byte fieldValue) {
        DataType$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final OfBoolean Preview$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("Preview"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool Preview
     * }
     */
    public static final OfBoolean Preview$layout() {
        return Preview$LAYOUT;
    }

    private static final long Preview$OFFSET = 57;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool Preview
     * }
     */
    public static final long Preview$offset() {
        return Preview$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool Preview
     * }
     */
    public static boolean Preview(MemorySegment struct) {
        return struct.get(Preview$LAYOUT, Preview$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool Preview
     * }
     */
    public static void Preview(MemorySegment struct, boolean fieldValue) {
        struct.set(Preview$LAYOUT, Preview$OFFSET, fieldValue);
    }

    private static final OfBoolean Delivery$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("Delivery"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool Delivery
     * }
     */
    public static final OfBoolean Delivery$layout() {
        return Delivery$LAYOUT;
    }

    private static final long Delivery$OFFSET = 58;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool Delivery
     * }
     */
    public static final long Delivery$offset() {
        return Delivery$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool Delivery
     * }
     */
    public static boolean Delivery(MemorySegment struct) {
        return struct.get(Delivery$LAYOUT, Delivery$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool Delivery
     * }
     */
    public static void Delivery(MemorySegment struct, boolean fieldValue) {
        struct.set(Delivery$LAYOUT, Delivery$OFFSET, fieldValue);
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

