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
 * struct ImGuiSettingsHandler {
 *     const char *TypeName;
 *     ImGuiID TypeHash;
 *     void (*ClearAllFn)(ImGuiContext *, ImGuiSettingsHandler *);
 *     void (*ReadInitFn)(ImGuiContext *, ImGuiSettingsHandler *);
 *     void *(*ReadOpenFn)(ImGuiContext *, ImGuiSettingsHandler *, const char *);
 *     void (*ReadLineFn)(ImGuiContext *, ImGuiSettingsHandler *, void *, const char *);
 *     void (*ApplyAllFn)(ImGuiContext *, ImGuiSettingsHandler *);
 *     void (*WriteAllFn)(ImGuiContext *, ImGuiSettingsHandler *, ImGuiTextBuffer *);
 *     void *UserData;
 * }
 * }
 */
public class ImGuiSettingsHandler {

    ImGuiSettingsHandler() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("TypeName"),
        imgui.C_INT.withName("TypeHash"),
        MemoryLayout.paddingLayout(4),
        imgui.C_POINTER.withName("ClearAllFn"),
        imgui.C_POINTER.withName("ReadInitFn"),
        imgui.C_POINTER.withName("ReadOpenFn"),
        imgui.C_POINTER.withName("ReadLineFn"),
        imgui.C_POINTER.withName("ApplyAllFn"),
        imgui.C_POINTER.withName("WriteAllFn"),
        imgui.C_POINTER.withName("UserData")
    ).withName("ImGuiSettingsHandler");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout TypeName$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("TypeName"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *TypeName
     * }
     */
    public static final AddressLayout TypeName$layout() {
        return TypeName$LAYOUT;
    }

    private static final long TypeName$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *TypeName
     * }
     */
    public static final long TypeName$offset() {
        return TypeName$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *TypeName
     * }
     */
    public static MemorySegment TypeName(MemorySegment struct) {
        return struct.get(TypeName$LAYOUT, TypeName$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *TypeName
     * }
     */
    public static void TypeName(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(TypeName$LAYOUT, TypeName$OFFSET, fieldValue);
    }

    private static final OfInt TypeHash$LAYOUT = (OfInt)$LAYOUT.select(groupElement("TypeHash"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImGuiID TypeHash
     * }
     */
    public static final OfInt TypeHash$layout() {
        return TypeHash$LAYOUT;
    }

    private static final long TypeHash$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImGuiID TypeHash
     * }
     */
    public static final long TypeHash$offset() {
        return TypeHash$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImGuiID TypeHash
     * }
     */
    public static int TypeHash(MemorySegment struct) {
        return struct.get(TypeHash$LAYOUT, TypeHash$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImGuiID TypeHash
     * }
     */
    public static void TypeHash(MemorySegment struct, int fieldValue) {
        struct.set(TypeHash$LAYOUT, TypeHash$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*ClearAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static class ClearAllFn {

        ClearAllFn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            imgui.C_POINTER,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(ClearAllFn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(ClearAllFn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout ClearAllFn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ClearAllFn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*ClearAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static final AddressLayout ClearAllFn$layout() {
        return ClearAllFn$LAYOUT;
    }

    private static final long ClearAllFn$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*ClearAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static final long ClearAllFn$offset() {
        return ClearAllFn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*ClearAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static MemorySegment ClearAllFn(MemorySegment struct) {
        return struct.get(ClearAllFn$LAYOUT, ClearAllFn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*ClearAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static void ClearAllFn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ClearAllFn$LAYOUT, ClearAllFn$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*ReadInitFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static class ReadInitFn {

        ReadInitFn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            imgui.C_POINTER,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(ReadInitFn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(ReadInitFn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout ReadInitFn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ReadInitFn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*ReadInitFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static final AddressLayout ReadInitFn$layout() {
        return ReadInitFn$LAYOUT;
    }

    private static final long ReadInitFn$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*ReadInitFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static final long ReadInitFn$offset() {
        return ReadInitFn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*ReadInitFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static MemorySegment ReadInitFn(MemorySegment struct) {
        return struct.get(ReadInitFn$LAYOUT, ReadInitFn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*ReadInitFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static void ReadInitFn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ReadInitFn$LAYOUT, ReadInitFn$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void *(*ReadOpenFn)(ImGuiContext *, ImGuiSettingsHandler *, const char *)
     * }
     */
    public static class ReadOpenFn {

        ReadOpenFn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            MemorySegment apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            imgui.C_POINTER,
            imgui.C_POINTER,
            imgui.C_POINTER,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(ReadOpenFn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(ReadOpenFn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static MemorySegment invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2) {
            try {
                return (MemorySegment) DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout ReadOpenFn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ReadOpenFn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *(*ReadOpenFn)(ImGuiContext *, ImGuiSettingsHandler *, const char *)
     * }
     */
    public static final AddressLayout ReadOpenFn$layout() {
        return ReadOpenFn$LAYOUT;
    }

    private static final long ReadOpenFn$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *(*ReadOpenFn)(ImGuiContext *, ImGuiSettingsHandler *, const char *)
     * }
     */
    public static final long ReadOpenFn$offset() {
        return ReadOpenFn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *(*ReadOpenFn)(ImGuiContext *, ImGuiSettingsHandler *, const char *)
     * }
     */
    public static MemorySegment ReadOpenFn(MemorySegment struct) {
        return struct.get(ReadOpenFn$LAYOUT, ReadOpenFn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *(*ReadOpenFn)(ImGuiContext *, ImGuiSettingsHandler *, const char *)
     * }
     */
    public static void ReadOpenFn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ReadOpenFn$LAYOUT, ReadOpenFn$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*ReadLineFn)(ImGuiContext *, ImGuiSettingsHandler *, void *, const char *)
     * }
     */
    public static class ReadLineFn {

        ReadLineFn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2, MemorySegment _x3);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            imgui.C_POINTER,
            imgui.C_POINTER,
            imgui.C_POINTER,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(ReadLineFn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(ReadLineFn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2, MemorySegment _x3) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2, _x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout ReadLineFn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ReadLineFn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*ReadLineFn)(ImGuiContext *, ImGuiSettingsHandler *, void *, const char *)
     * }
     */
    public static final AddressLayout ReadLineFn$layout() {
        return ReadLineFn$LAYOUT;
    }

    private static final long ReadLineFn$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*ReadLineFn)(ImGuiContext *, ImGuiSettingsHandler *, void *, const char *)
     * }
     */
    public static final long ReadLineFn$offset() {
        return ReadLineFn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*ReadLineFn)(ImGuiContext *, ImGuiSettingsHandler *, void *, const char *)
     * }
     */
    public static MemorySegment ReadLineFn(MemorySegment struct) {
        return struct.get(ReadLineFn$LAYOUT, ReadLineFn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*ReadLineFn)(ImGuiContext *, ImGuiSettingsHandler *, void *, const char *)
     * }
     */
    public static void ReadLineFn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ReadLineFn$LAYOUT, ReadLineFn$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*ApplyAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static class ApplyAllFn {

        ApplyAllFn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            imgui.C_POINTER,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(ApplyAllFn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(ApplyAllFn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout ApplyAllFn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ApplyAllFn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*ApplyAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static final AddressLayout ApplyAllFn$layout() {
        return ApplyAllFn$LAYOUT;
    }

    private static final long ApplyAllFn$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*ApplyAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static final long ApplyAllFn$offset() {
        return ApplyAllFn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*ApplyAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static MemorySegment ApplyAllFn(MemorySegment struct) {
        return struct.get(ApplyAllFn$LAYOUT, ApplyAllFn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*ApplyAllFn)(ImGuiContext *, ImGuiSettingsHandler *)
     * }
     */
    public static void ApplyAllFn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ApplyAllFn$LAYOUT, ApplyAllFn$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * void (*WriteAllFn)(ImGuiContext *, ImGuiSettingsHandler *, ImGuiTextBuffer *)
     * }
     */
    public static class WriteAllFn {

        WriteAllFn() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            void apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
            imgui.C_POINTER,
            imgui.C_POINTER,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(WriteAllFn.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(WriteAllFn.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static void invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2) {
            try {
                 DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout WriteAllFn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("WriteAllFn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void (*WriteAllFn)(ImGuiContext *, ImGuiSettingsHandler *, ImGuiTextBuffer *)
     * }
     */
    public static final AddressLayout WriteAllFn$layout() {
        return WriteAllFn$LAYOUT;
    }

    private static final long WriteAllFn$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void (*WriteAllFn)(ImGuiContext *, ImGuiSettingsHandler *, ImGuiTextBuffer *)
     * }
     */
    public static final long WriteAllFn$offset() {
        return WriteAllFn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void (*WriteAllFn)(ImGuiContext *, ImGuiSettingsHandler *, ImGuiTextBuffer *)
     * }
     */
    public static MemorySegment WriteAllFn(MemorySegment struct) {
        return struct.get(WriteAllFn$LAYOUT, WriteAllFn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void (*WriteAllFn)(ImGuiContext *, ImGuiSettingsHandler *, ImGuiTextBuffer *)
     * }
     */
    public static void WriteAllFn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(WriteAllFn$LAYOUT, WriteAllFn$OFFSET, fieldValue);
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

    private static final long UserData$OFFSET = 64;

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

