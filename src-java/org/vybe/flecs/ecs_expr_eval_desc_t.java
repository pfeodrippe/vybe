// Generated by jextract

package org.vybe.flecs;

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
 * struct ecs_expr_eval_desc_t {
 *     const char *name;
 *     const char *expr;
 *     ecs_entity_t (*lookup_action)(const ecs_world_t *, const char *, void *);
 *     void *lookup_ctx;
 *     const ecs_script_vars_t *vars;
 *     ecs_entity_t type;
 *     bool disable_folding;
 *     bool allow_unresolved_identifiers;
 *     ecs_script_runtime_t *runtime;
 * }
 * }
 */
public class ecs_expr_eval_desc_t {

    ecs_expr_eval_desc_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("name"),
        flecs.C_POINTER.withName("expr"),
        flecs.C_POINTER.withName("lookup_action"),
        flecs.C_POINTER.withName("lookup_ctx"),
        flecs.C_POINTER.withName("vars"),
        flecs.C_LONG_LONG.withName("type"),
        flecs.C_BOOL.withName("disable_folding"),
        flecs.C_BOOL.withName("allow_unresolved_identifiers"),
        MemoryLayout.paddingLayout(6),
        flecs.C_POINTER.withName("runtime")
    ).withName("ecs_expr_eval_desc_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout name$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("name"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static final AddressLayout name$layout() {
        return name$LAYOUT;
    }

    private static final long name$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static final long name$offset() {
        return name$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static MemorySegment name(MemorySegment struct) {
        return struct.get(name$LAYOUT, name$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *name
     * }
     */
    public static void name(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(name$LAYOUT, name$OFFSET, fieldValue);
    }

    private static final AddressLayout expr$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("expr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *expr
     * }
     */
    public static final AddressLayout expr$layout() {
        return expr$LAYOUT;
    }

    private static final long expr$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *expr
     * }
     */
    public static final long expr$offset() {
        return expr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *expr
     * }
     */
    public static MemorySegment expr(MemorySegment struct) {
        return struct.get(expr$LAYOUT, expr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *expr
     * }
     */
    public static void expr(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(expr$LAYOUT, expr$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * ecs_entity_t (*lookup_action)(const ecs_world_t *, const char *, void *)
     * }
     */
    public static class lookup_action {

        lookup_action() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            long apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            flecs.C_LONG_LONG,
            flecs.C_POINTER,
            flecs.C_POINTER,
            flecs.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = flecs.upcallHandle(lookup_action.Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(lookup_action.Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static long invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2) {
            try {
                return (long) DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout lookup_action$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("lookup_action"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t (*lookup_action)(const ecs_world_t *, const char *, void *)
     * }
     */
    public static final AddressLayout lookup_action$layout() {
        return lookup_action$LAYOUT;
    }

    private static final long lookup_action$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t (*lookup_action)(const ecs_world_t *, const char *, void *)
     * }
     */
    public static final long lookup_action$offset() {
        return lookup_action$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t (*lookup_action)(const ecs_world_t *, const char *, void *)
     * }
     */
    public static MemorySegment lookup_action(MemorySegment struct) {
        return struct.get(lookup_action$LAYOUT, lookup_action$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t (*lookup_action)(const ecs_world_t *, const char *, void *)
     * }
     */
    public static void lookup_action(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(lookup_action$LAYOUT, lookup_action$OFFSET, fieldValue);
    }

    private static final AddressLayout lookup_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("lookup_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *lookup_ctx
     * }
     */
    public static final AddressLayout lookup_ctx$layout() {
        return lookup_ctx$LAYOUT;
    }

    private static final long lookup_ctx$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *lookup_ctx
     * }
     */
    public static final long lookup_ctx$offset() {
        return lookup_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *lookup_ctx
     * }
     */
    public static MemorySegment lookup_ctx(MemorySegment struct) {
        return struct.get(lookup_ctx$LAYOUT, lookup_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *lookup_ctx
     * }
     */
    public static void lookup_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(lookup_ctx$LAYOUT, lookup_ctx$OFFSET, fieldValue);
    }

    private static final AddressLayout vars$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("vars"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const ecs_script_vars_t *vars
     * }
     */
    public static final AddressLayout vars$layout() {
        return vars$LAYOUT;
    }

    private static final long vars$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const ecs_script_vars_t *vars
     * }
     */
    public static final long vars$offset() {
        return vars$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const ecs_script_vars_t *vars
     * }
     */
    public static MemorySegment vars(MemorySegment struct) {
        return struct.get(vars$LAYOUT, vars$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const ecs_script_vars_t *vars
     * }
     */
    public static void vars(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(vars$LAYOUT, vars$OFFSET, fieldValue);
    }

    private static final OfLong type$LAYOUT = (OfLong)$LAYOUT.select(groupElement("type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static final OfLong type$layout() {
        return type$LAYOUT;
    }

    private static final long type$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static final long type$offset() {
        return type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static long type(MemorySegment struct) {
        return struct.get(type$LAYOUT, type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t type
     * }
     */
    public static void type(MemorySegment struct, long fieldValue) {
        struct.set(type$LAYOUT, type$OFFSET, fieldValue);
    }

    private static final OfBoolean disable_folding$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("disable_folding"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool disable_folding
     * }
     */
    public static final OfBoolean disable_folding$layout() {
        return disable_folding$LAYOUT;
    }

    private static final long disable_folding$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool disable_folding
     * }
     */
    public static final long disable_folding$offset() {
        return disable_folding$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool disable_folding
     * }
     */
    public static boolean disable_folding(MemorySegment struct) {
        return struct.get(disable_folding$LAYOUT, disable_folding$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool disable_folding
     * }
     */
    public static void disable_folding(MemorySegment struct, boolean fieldValue) {
        struct.set(disable_folding$LAYOUT, disable_folding$OFFSET, fieldValue);
    }

    private static final OfBoolean allow_unresolved_identifiers$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("allow_unresolved_identifiers"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool allow_unresolved_identifiers
     * }
     */
    public static final OfBoolean allow_unresolved_identifiers$layout() {
        return allow_unresolved_identifiers$LAYOUT;
    }

    private static final long allow_unresolved_identifiers$OFFSET = 49;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool allow_unresolved_identifiers
     * }
     */
    public static final long allow_unresolved_identifiers$offset() {
        return allow_unresolved_identifiers$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool allow_unresolved_identifiers
     * }
     */
    public static boolean allow_unresolved_identifiers(MemorySegment struct) {
        return struct.get(allow_unresolved_identifiers$LAYOUT, allow_unresolved_identifiers$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool allow_unresolved_identifiers
     * }
     */
    public static void allow_unresolved_identifiers(MemorySegment struct, boolean fieldValue) {
        struct.set(allow_unresolved_identifiers$LAYOUT, allow_unresolved_identifiers$OFFSET, fieldValue);
    }

    private static final AddressLayout runtime$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("runtime"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_script_runtime_t *runtime
     * }
     */
    public static final AddressLayout runtime$layout() {
        return runtime$LAYOUT;
    }

    private static final long runtime$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_script_runtime_t *runtime
     * }
     */
    public static final long runtime$offset() {
        return runtime$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_script_runtime_t *runtime
     * }
     */
    public static MemorySegment runtime(MemorySegment struct) {
        return struct.get(runtime$LAYOUT, runtime$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_script_runtime_t *runtime
     * }
     */
    public static void runtime(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(runtime$LAYOUT, runtime$OFFSET, fieldValue);
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
