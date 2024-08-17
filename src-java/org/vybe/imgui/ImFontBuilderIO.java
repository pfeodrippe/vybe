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
 * struct ImFontBuilderIO {
 *     bool (*FontBuilder_Build)(ImFontAtlas *);
 * }
 * }
 */
public class ImFontBuilderIO {

    ImFontBuilderIO() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        imgui.C_POINTER.withName("FontBuilder_Build")
    ).withName("ImFontBuilderIO");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    /**
     * {@snippet lang=c :
     * bool (*FontBuilder_Build)(ImFontAtlas *)
     * }
     */
    public static class FontBuilder_Build {

        FontBuilder_Build() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            boolean apply(MemorySegment _x0);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            imgui.C_BOOL,
            imgui.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = imgui.upcallHandle(FontBuilder_Build.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(FontBuilder_Build.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static boolean invoke(MemorySegment funcPtr,MemorySegment _x0) {
            try {
                return (boolean) DOWN$MH.invokeExact(funcPtr, _x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout FontBuilder_Build$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("FontBuilder_Build"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool (*FontBuilder_Build)(ImFontAtlas *)
     * }
     */
    public static final AddressLayout FontBuilder_Build$layout() {
        return FontBuilder_Build$LAYOUT;
    }

    private static final long FontBuilder_Build$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool (*FontBuilder_Build)(ImFontAtlas *)
     * }
     */
    public static final long FontBuilder_Build$offset() {
        return FontBuilder_Build$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool (*FontBuilder_Build)(ImFontAtlas *)
     * }
     */
    public static MemorySegment FontBuilder_Build(MemorySegment struct) {
        return struct.get(FontBuilder_Build$LAYOUT, FontBuilder_Build$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool (*FontBuilder_Build)(ImFontAtlas *)
     * }
     */
    public static void FontBuilder_Build(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(FontBuilder_Build$LAYOUT, FontBuilder_Build$OFFSET, fieldValue);
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

