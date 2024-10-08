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
 * struct ImGuiLocEntry {
 *     ImGuiLocKey Key;
 *     const char *Text;
 * }
 * }
 */
public class ImGuiLocEntry {

    ImGuiLocEntry() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Key"),
        MemoryLayout.paddingLayout(4),
        imgui.C_POINTER.withName("Text")
    ).withName("ImGuiLocEntry");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt Key$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Key"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiLocKey Key
     * }
     */
    public static final OfInt Key$layout() {
        return Key$LAYOUT;
    }

    private static final long Key$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiLocKey Key
     * }
     */
    public static final long Key$offset() {
        return Key$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiLocKey Key
     * }
     */
    public static int Key(MemorySegment struct) {
        return struct.get(Key$LAYOUT, Key$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiLocKey Key
     * }
     */
    public static void Key(MemorySegment struct, int fieldValue) {
        struct.set(Key$LAYOUT, Key$OFFSET, fieldValue);
    }

    private static final AddressLayout Text$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Text"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *Text
     * }
     */
    public static final AddressLayout Text$layout() {
        return Text$LAYOUT;
    }

    private static final long Text$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *Text
     * }
     */
    public static final long Text$offset() {
        return Text$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *Text
     * }
     */
    public static MemorySegment Text(MemorySegment struct) {
        return struct.get(Text$LAYOUT, Text$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *Text
     * }
     */
    public static void Text(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Text$LAYOUT, Text$OFFSET, fieldValue);
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

