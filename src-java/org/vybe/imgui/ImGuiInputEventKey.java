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
 * struct ImGuiInputEventKey {
 *     ImGuiKey Key;
 *     bool Down;
 *     float AnalogValue;
 * }
 * }
 */
public class ImGuiInputEventKey {

    ImGuiInputEventKey() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Key"),
        imgui.C_BOOL.withName("Down"),
        MemoryLayout.paddingLayout(3),
        imgui.C_FLOAT.withName("AnalogValue")
    ).withName("ImGuiInputEventKey");

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
     * ImGuiKey Key
     * }
     */
    public static final OfInt Key$layout() {
        return Key$LAYOUT;
    }

    private static final long Key$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiKey Key
     * }
     */
    public static final long Key$offset() {
        return Key$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiKey Key
     * }
     */
    public static int Key(MemorySegment struct) {
        return struct.get(Key$LAYOUT, Key$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiKey Key
     * }
     */
    public static void Key(MemorySegment struct, int fieldValue) {
        struct.set(Key$LAYOUT, Key$OFFSET, fieldValue);
    }

    private static final OfBoolean Down$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("Down"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool Down
     * }
     */
    public static final OfBoolean Down$layout() {
        return Down$LAYOUT;
    }

    private static final long Down$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool Down
     * }
     */
    public static final long Down$offset() {
        return Down$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool Down
     * }
     */
    public static boolean Down(MemorySegment struct) {
        return struct.get(Down$LAYOUT, Down$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool Down
     * }
     */
    public static void Down(MemorySegment struct, boolean fieldValue) {
        struct.set(Down$LAYOUT, Down$OFFSET, fieldValue);
    }

    private static final OfFloat AnalogValue$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("AnalogValue"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float AnalogValue
     * }
     */
    public static final OfFloat AnalogValue$layout() {
        return AnalogValue$LAYOUT;
    }

    private static final long AnalogValue$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float AnalogValue
     * }
     */
    public static final long AnalogValue$offset() {
        return AnalogValue$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float AnalogValue
     * }
     */
    public static float AnalogValue(MemorySegment struct) {
        return struct.get(AnalogValue$LAYOUT, AnalogValue$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float AnalogValue
     * }
     */
    public static void AnalogValue(MemorySegment struct, float fieldValue) {
        struct.set(AnalogValue$LAYOUT, AnalogValue$OFFSET, fieldValue);
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

