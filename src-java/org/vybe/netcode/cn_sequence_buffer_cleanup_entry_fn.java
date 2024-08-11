// Generated by jextract

package org.vybe.netcode;

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
 * typedef void (cn_sequence_buffer_cleanup_entry_fn)(void *, uint16_t, void *, void *)
 * }
 */
public class cn_sequence_buffer_cleanup_entry_fn {

    cn_sequence_buffer_cleanup_entry_fn() {
        // Should not be called directly
    }

    /**
     * The function pointer signature, expressed as a functional interface
     */
    public interface Function {
        void apply(MemorySegment data, short sequence, MemorySegment udata, MemorySegment mem_ctx);
    }

    private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
        netcode.C_POINTER,
        netcode.C_SHORT,
        netcode.C_POINTER,
        netcode.C_POINTER
    );

    /**
     * The descriptor of this function pointer
     */
    public static FunctionDescriptor descriptor() {
        return $DESC;
    }

    private static final MethodHandle UP$MH = netcode.upcallHandle(cn_sequence_buffer_cleanup_entry_fn.Function.class, "apply", $DESC);

    /**
     * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
     * The lifetime of the returned segment is managed by {@code arena}
     */
    public static MemorySegment allocate(cn_sequence_buffer_cleanup_entry_fn.Function fi, Arena arena) {
        return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
    }

    private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

    /**
     * Invoke the upcall stub {@code funcPtr}, with given parameters
     */
    public static void invoke(MemorySegment funcPtr,MemorySegment data, short sequence, MemorySegment udata, MemorySegment mem_ctx) {
        try {
             DOWN$MH.invokeExact(funcPtr, data, sequence, udata, mem_ctx);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}

