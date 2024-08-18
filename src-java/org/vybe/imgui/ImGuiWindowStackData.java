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
 * struct ImGuiWindowStackData {
 *     ImGuiWindow *Window;
 *     ImGuiLastItemData ParentLastItemDataBackup;
 *     ImGuiStackSizes StackSizesOnBegin;
 *     bool DisabledOverrideReenable;
 * }
 * }
 */
public class ImGuiWindowStackData {

    ImGuiWindowStackData() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("Window"),
        ImGuiLastItemData.layout().withName("ParentLastItemDataBackup"),
        ImGuiStackSizes.layout().withName("StackSizesOnBegin"),
        imgui.C_BOOL.withName("DisabledOverrideReenable"),
        MemoryLayout.paddingLayout(5)
    ).withName("ImGuiWindowStackData");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout Window$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("Window"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static final AddressLayout Window$layout() {
        return Window$LAYOUT;
    }

    private static final long Window$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static final long Window$offset() {
        return Window$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static MemorySegment Window(MemorySegment struct) {
        return struct.get(Window$LAYOUT, Window$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiWindow *Window
     * }
     */
    public static void Window(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(Window$LAYOUT, Window$OFFSET, fieldValue);
    }

    private static final GroupLayout ParentLastItemDataBackup$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("ParentLastItemDataBackup"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiLastItemData ParentLastItemDataBackup
     * }
     */
    public static final GroupLayout ParentLastItemDataBackup$layout() {
        return ParentLastItemDataBackup$LAYOUT;
    }

    private static final long ParentLastItemDataBackup$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiLastItemData ParentLastItemDataBackup
     * }
     */
    public static final long ParentLastItemDataBackup$offset() {
        return ParentLastItemDataBackup$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiLastItemData ParentLastItemDataBackup
     * }
     */
    public static MemorySegment ParentLastItemDataBackup(MemorySegment struct) {
        return struct.asSlice(ParentLastItemDataBackup$OFFSET, ParentLastItemDataBackup$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiLastItemData ParentLastItemDataBackup
     * }
     */
    public static void ParentLastItemDataBackup(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ParentLastItemDataBackup$OFFSET, ParentLastItemDataBackup$LAYOUT.byteSize());
    }

    private static final GroupLayout StackSizesOnBegin$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("StackSizesOnBegin"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiStackSizes StackSizesOnBegin
     * }
     */
    public static final GroupLayout StackSizesOnBegin$layout() {
        return StackSizesOnBegin$LAYOUT;
    }

    private static final long StackSizesOnBegin$OFFSET = 88;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiStackSizes StackSizesOnBegin
     * }
     */
    public static final long StackSizesOnBegin$offset() {
        return StackSizesOnBegin$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiStackSizes StackSizesOnBegin
     * }
     */
    public static MemorySegment StackSizesOnBegin(MemorySegment struct) {
        return struct.asSlice(StackSizesOnBegin$OFFSET, StackSizesOnBegin$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiStackSizes StackSizesOnBegin
     * }
     */
    public static void StackSizesOnBegin(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, StackSizesOnBegin$OFFSET, StackSizesOnBegin$LAYOUT.byteSize());
    }

    private static final OfBoolean DisabledOverrideReenable$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("DisabledOverrideReenable"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool DisabledOverrideReenable
     * }
     */
    public static final OfBoolean DisabledOverrideReenable$layout() {
        return DisabledOverrideReenable$LAYOUT;
    }

    private static final long DisabledOverrideReenable$OFFSET = 106;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool DisabledOverrideReenable
     * }
     */
    public static final long DisabledOverrideReenable$offset() {
        return DisabledOverrideReenable$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool DisabledOverrideReenable
     * }
     */
    public static boolean DisabledOverrideReenable(MemorySegment struct) {
        return struct.get(DisabledOverrideReenable$LAYOUT, DisabledOverrideReenable$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool DisabledOverrideReenable
     * }
     */
    public static void DisabledOverrideReenable(MemorySegment struct, boolean fieldValue) {
        struct.set(DisabledOverrideReenable$LAYOUT, DisabledOverrideReenable$OFFSET, fieldValue);
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
