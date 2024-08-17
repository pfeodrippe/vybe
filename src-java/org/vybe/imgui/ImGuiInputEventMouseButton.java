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
 * struct ImGuiInputEventMouseButton {
 *     int Button;
 *     bool Down;
 *     ImGuiMouseSource MouseSource;
 * }
 * }
 */
public class ImGuiInputEventMouseButton {

    ImGuiInputEventMouseButton() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Button"),
        imgui.C_BOOL.withName("Down"),
        MemoryLayout.paddingLayout(3),
        imgui.C_INT.withName("MouseSource")
    ).withName("ImGuiInputEventMouseButton");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt Button$LAYOUT = (OfInt)$LAYOUT.select(groupElement("Button"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int Button
     * }
     */
    public static final OfInt Button$layout() {
        return Button$LAYOUT;
    }

    private static final long Button$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int Button
     * }
     */
    public static final long Button$offset() {
        return Button$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int Button
     * }
     */
    public static int Button(MemorySegment struct) {
        return struct.get(Button$LAYOUT, Button$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int Button
     * }
     */
    public static void Button(MemorySegment struct, int fieldValue) {
        struct.set(Button$LAYOUT, Button$OFFSET, fieldValue);
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

    private static final OfInt MouseSource$LAYOUT = (OfInt)$LAYOUT.select(groupElement("MouseSource"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiMouseSource MouseSource
     * }
     */
    public static final OfInt MouseSource$layout() {
        return MouseSource$LAYOUT;
    }

    private static final long MouseSource$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiMouseSource MouseSource
     * }
     */
    public static final long MouseSource$offset() {
        return MouseSource$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiMouseSource MouseSource
     * }
     */
    public static int MouseSource(MemorySegment struct) {
        return struct.get(MouseSource$LAYOUT, MouseSource$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiMouseSource MouseSource
     * }
     */
    public static void MouseSource(MemorySegment struct, int fieldValue) {
        struct.set(MouseSource$LAYOUT, MouseSource$OFFSET, fieldValue);
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

