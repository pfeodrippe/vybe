// Generated by jextract

package org.vybe.jolt_cs;

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
 * typedef float (JPH_CollidePointCollector)(void *, JPH_CollidePointResult *)
 * }
 */
public class JPH_CollidePointCollector {

    JPH_CollidePointCollector() {
        // Should not be called directly
    }

    /**
     * The function pointer signature, expressed as a functional interface
     */
    public interface Function {
        float apply(MemorySegment context, MemorySegment result);
    }

    private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
        jolt_cs.C_FLOAT,
        jolt_cs.C_POINTER,
        jolt_cs.C_POINTER
    );

    /**
     * The descriptor of this function pointer
     */
    public static FunctionDescriptor descriptor() {
        return $DESC;
    }

    private static final MethodHandle UP$MH = jolt_cs.upcallHandle(JPH_CollidePointCollector.Function.class, "apply", $DESC);

    /**
     * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
     * The lifetime of the returned segment is managed by {@code arena}
     */
    public static MemorySegment allocate(JPH_CollidePointCollector.Function fi, Arena arena) {
        return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
    }

    private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

    /**
     * Invoke the upcall stub {@code funcPtr}, with given parameters
     */
    public static float invoke(MemorySegment funcPtr,MemorySegment context, MemorySegment result) {
        try {
            return (float) DOWN$MH.invokeExact(funcPtr, context, result);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}

