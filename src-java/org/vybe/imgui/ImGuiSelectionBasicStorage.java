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
 * struct ImGuiSelectionBasicStorage {
 *     int Size;
 *     bool PreserveOrder;
 *     void *UserData;
 *     ImGuiID (*AdapterIndexToStorageId)(ImGuiSelectionBasicStorage *, int);
 *     int _SelectionOrder;
 *     ImGuiStorage _Storage;
 * }
 * }
 */
public class ImGuiSelectionBasicStorage {

    ImGuiSelectionBasicStorage() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_INT.withName("Size"),
        imgui.C_BOOL.withName("PreserveOrder"),
        MemoryLayout.paddingLayout(3),
        imgui.C_POINTER.withName("UserData"),
        imgui.C_POINTER.withName("AdapterIndexToStorageId"),
        imgui.C_INT.withName("_SelectionOrder"),
        MemoryLayout.paddingLayout(4),
        ImGuiStorage.layout().withName("_Storage")
    ).withName("ImGuiSelectionBasicStorage");

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

    private static final OfBoolean PreserveOrder$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("PreserveOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool PreserveOrder
     * }
     */
    public static final OfBoolean PreserveOrder$layout() {
        return PreserveOrder$LAYOUT;
    }

    private static final long PreserveOrder$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool PreserveOrder
     * }
     */
    public static final long PreserveOrder$offset() {
        return PreserveOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool PreserveOrder
     * }
     */
    public static boolean PreserveOrder(MemorySegment struct) {
        return struct.get(PreserveOrder$LAYOUT, PreserveOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool PreserveOrder
     * }
     */
    public static void PreserveOrder(MemorySegment struct, boolean fieldValue) {
        struct.set(PreserveOrder$LAYOUT, PreserveOrder$OFFSET, fieldValue);
    }

    private static final AddressLayout UserData$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("UserData"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static final AddressLayout UserData$layout() {
        return UserData$LAYOUT;
    }

    private static final long UserData$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static final long UserData$offset() {
        return UserData$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static MemorySegment UserData(MemorySegment struct) {
        return struct.get(UserData$LAYOUT, UserData$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *UserData
     * }
     */
    public static void UserData(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(UserData$LAYOUT, UserData$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * ImGuiID (*AdapterIndexToStorageId)(ImGuiSelectionBasicStorage *, int)
     * }
     */
    public static class AdapterIndexToStorageId {

        AdapterIndexToStorageId() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0, int _x1);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            imgui.C_INT,
            imgui.C_POINTER,
            imgui.C_INT
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(AdapterIndexToStorageId.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(AdapterIndexToStorageId.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0, int _x1) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0, _x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout AdapterIndexToStorageId$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("AdapterIndexToStorageId"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID (*AdapterIndexToStorageId)(ImGuiSelectionBasicStorage *, int)
     * }
     */
    public static final AddressLayout AdapterIndexToStorageId$layout() {
        return AdapterIndexToStorageId$LAYOUT;
    }

    private static final long AdapterIndexToStorageId$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID (*AdapterIndexToStorageId)(ImGuiSelectionBasicStorage *, int)
     * }
     */
    public static final long AdapterIndexToStorageId$offset() {
        return AdapterIndexToStorageId$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID (*AdapterIndexToStorageId)(ImGuiSelectionBasicStorage *, int)
     * }
     */
    public static MemorySegment AdapterIndexToStorageId(MemorySegment struct) {
        return struct.get(AdapterIndexToStorageId$LAYOUT, AdapterIndexToStorageId$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID (*AdapterIndexToStorageId)(ImGuiSelectionBasicStorage *, int)
     * }
     */
    public static void AdapterIndexToStorageId(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(AdapterIndexToStorageId$LAYOUT, AdapterIndexToStorageId$OFFSET, fieldValue);
    }

    private static final OfInt _SelectionOrder$LAYOUT = (OfInt)$LAYOUT.select(groupElement("_SelectionOrder"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int _SelectionOrder
     * }
     */
    public static final OfInt _SelectionOrder$layout() {
        return _SelectionOrder$LAYOUT;
    }

    private static final long _SelectionOrder$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int _SelectionOrder
     * }
     */
    public static final long _SelectionOrder$offset() {
        return _SelectionOrder$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int _SelectionOrder
     * }
     */
    public static int _SelectionOrder(MemorySegment struct) {
        return struct.get(_SelectionOrder$LAYOUT, _SelectionOrder$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int _SelectionOrder
     * }
     */
    public static void _SelectionOrder(MemorySegment struct, int fieldValue) {
        struct.set(_SelectionOrder$LAYOUT, _SelectionOrder$OFFSET, fieldValue);
    }

    private static final GroupLayout _Storage$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("_Storage"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiStorage _Storage
     * }
     */
    public static final GroupLayout _Storage$layout() {
        return _Storage$LAYOUT;
    }

    private static final long _Storage$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiStorage _Storage
     * }
     */
    public static final long _Storage$offset() {
        return _Storage$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiStorage _Storage
     * }
     */
    public static MemorySegment _Storage(MemorySegment struct) {
        return struct.asSlice(_Storage$OFFSET, _Storage$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiStorage _Storage
     * }
     */
    public static void _Storage(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, _Storage$OFFSET, _Storage$LAYOUT.byteSize());
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

