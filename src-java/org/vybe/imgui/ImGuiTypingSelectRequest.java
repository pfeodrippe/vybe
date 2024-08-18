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
 * struct ImGuiTypingSelectRequest {
 *     ImGuiTypingSelectFlags Flags;
 *     int SearchBufferLen;
 *     const char *SearchBuffer;
 *     bool SelectRequest;
 *     bool SingleCharMode;
 *     ImS8 SingleCharSize;
 * }
 * }
 */
public class ImGuiTypingSelectRequest {

    ImGuiTypingSelectRequest() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Flags"),
        imgui.C_INT.withName("SearchBufferLen"),
        imgui.C_POINTER.withName("SearchBuffer"),
        imgui.C_BOOL.withName("SelectRequest"),
        imgui.C_BOOL.withName("SingleCharMode"),
        imgui.C_CHAR.withName("SingleCharSize"),
        MemoryLayout.paddingLayout(5)
    ).withName("ImGuiTypingSelectRequest");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt Flags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Flags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiTypingSelectFlags Flags
     * }
     */
    public static final OfInt Flags$layout() {
        return Flags$LAYOUT;
    }

    private static final long Flags$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiTypingSelectFlags Flags
     * }
     */
    public static final long Flags$offset() {
        return Flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiTypingSelectFlags Flags
     * }
     */
    public static int Flags(MemorySegment struct) {
        return struct.get(Flags$LAYOUT, Flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiTypingSelectFlags Flags
     * }
     */
    public static void Flags(MemorySegment struct, int fieldValue) {
        struct.set(Flags$LAYOUT, Flags$OFFSET, fieldValue);
    }

    private static final OfInt SearchBufferLen$LAYOUT = (OfInt)$LAYOUT.select(groupElement("SearchBufferLen"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int SearchBufferLen
     * }
     */
    public static final OfInt SearchBufferLen$layout() {
        return SearchBufferLen$LAYOUT;
    }

    private static final long SearchBufferLen$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int SearchBufferLen
     * }
     */
    public static final long SearchBufferLen$offset() {
        return SearchBufferLen$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int SearchBufferLen
     * }
     */
    public static int SearchBufferLen(MemorySegment struct) {
        return struct.get(SearchBufferLen$LAYOUT, SearchBufferLen$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int SearchBufferLen
     * }
     */
    public static void SearchBufferLen(MemorySegment struct, int fieldValue) {
        struct.set(SearchBufferLen$LAYOUT, SearchBufferLen$OFFSET, fieldValue);
    }

    private static final AddressLayout SearchBuffer$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("SearchBuffer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *SearchBuffer
     * }
     */
    public static final AddressLayout SearchBuffer$layout() {
        return SearchBuffer$LAYOUT;
    }

    private static final long SearchBuffer$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *SearchBuffer
     * }
     */
    public static final long SearchBuffer$offset() {
        return SearchBuffer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *SearchBuffer
     * }
     */
    public static MemorySegment SearchBuffer(MemorySegment struct) {
        return struct.get(SearchBuffer$LAYOUT, SearchBuffer$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *SearchBuffer
     * }
     */
    public static void SearchBuffer(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(SearchBuffer$LAYOUT, SearchBuffer$OFFSET, fieldValue);
    }

    private static final OfBoolean SelectRequest$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("SelectRequest"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool SelectRequest
     * }
     */
    public static final OfBoolean SelectRequest$layout() {
        return SelectRequest$LAYOUT;
    }

    private static final long SelectRequest$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool SelectRequest
     * }
     */
    public static final long SelectRequest$offset() {
        return SelectRequest$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool SelectRequest
     * }
     */
    public static boolean SelectRequest(MemorySegment struct) {
        return struct.get(SelectRequest$LAYOUT, SelectRequest$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool SelectRequest
     * }
     */
    public static void SelectRequest(MemorySegment struct, boolean fieldValue) {
        struct.set(SelectRequest$LAYOUT, SelectRequest$OFFSET, fieldValue);
    }

    private static final OfBoolean SingleCharMode$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("SingleCharMode"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool SingleCharMode
     * }
     */
    public static final OfBoolean SingleCharMode$layout() {
        return SingleCharMode$LAYOUT;
    }

    private static final long SingleCharMode$OFFSET = 17;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool SingleCharMode
     * }
     */
    public static final long SingleCharMode$offset() {
        return SingleCharMode$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool SingleCharMode
     * }
     */
    public static boolean SingleCharMode(MemorySegment struct) {
        return struct.get(SingleCharMode$LAYOUT, SingleCharMode$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool SingleCharMode
     * }
     */
    public static void SingleCharMode(MemorySegment struct, boolean fieldValue) {
        struct.set(SingleCharMode$LAYOUT, SingleCharMode$OFFSET, fieldValue);
    }

    private static final OfByte SingleCharSize$LAYOUT = (OfByte)$LAYOUT.select(groupElement("SingleCharSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImS8 SingleCharSize
     * }
     */
    public static final OfByte SingleCharSize$layout() {
        return SingleCharSize$LAYOUT;
    }

    private static final long SingleCharSize$OFFSET = 18;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImS8 SingleCharSize
     * }
     */
    public static final long SingleCharSize$offset() {
        return SingleCharSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImS8 SingleCharSize
     * }
     */
    public static byte SingleCharSize(MemorySegment struct) {
        return struct.get(SingleCharSize$LAYOUT, SingleCharSize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImS8 SingleCharSize
     * }
     */
    public static void SingleCharSize(MemorySegment struct, byte fieldValue) {
        struct.set(SingleCharSize$LAYOUT, SingleCharSize$OFFSET, fieldValue);
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
