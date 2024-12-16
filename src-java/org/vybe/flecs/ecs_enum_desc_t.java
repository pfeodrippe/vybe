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
 * struct ecs_enum_desc_t {
 *     ecs_entity_t entity;
 *     ecs_enum_constant_t constants[32];
 *     ecs_entity_t underlying_type;
 * }
 * }
 */
public class ecs_enum_desc_t {

    ecs_enum_desc_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_LONG_LONG.withName("entity"),
        MemoryLayout.sequenceLayout(32, ecs_enum_constant_t.layout()).withName("constants"),
        flecs.C_LONG_LONG.withName("underlying_type")
    ).withName("ecs_enum_desc_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong entity$LAYOUT = (OfLong)$LAYOUT.select(groupElement("entity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t entity
     * }
     */
    public static final OfLong entity$layout() {
        return entity$LAYOUT;
    }

    private static final long entity$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t entity
     * }
     */
    public static final long entity$offset() {
        return entity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t entity
     * }
     */
    public static long entity(MemorySegment struct) {
        return struct.get(entity$LAYOUT, entity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t entity
     * }
     */
    public static void entity(MemorySegment struct, long fieldValue) {
        struct.set(entity$LAYOUT, entity$OFFSET, fieldValue);
    }

    private static final SequenceLayout constants$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("constants"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static final SequenceLayout constants$layout() {
        return constants$LAYOUT;
    }

    private static final long constants$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static final long constants$offset() {
        return constants$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static MemorySegment constants(MemorySegment struct) {
        return struct.asSlice(constants$OFFSET, constants$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static void constants(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, constants$OFFSET, constants$LAYOUT.byteSize());
    }

    private static long[] constants$DIMS = { 32 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static long[] constants$dimensions() {
        return constants$DIMS;
    }
    private static final MethodHandle constants$ELEM_HANDLE = constants$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static MemorySegment constants(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)constants$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ecs_enum_constant_t constants[32]
     * }
     */
    public static void constants(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, constants(struct, index0), 0L, ecs_enum_constant_t.layout().byteSize());
    }

    private static final OfLong underlying_type$LAYOUT = (OfLong)$LAYOUT.select(groupElement("underlying_type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t underlying_type
     * }
     */
    public static final OfLong underlying_type$layout() {
        return underlying_type$LAYOUT;
    }

    private static final long underlying_type$OFFSET = 1032;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t underlying_type
     * }
     */
    public static final long underlying_type$offset() {
        return underlying_type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t underlying_type
     * }
     */
    public static long underlying_type(MemorySegment struct) {
        return struct.get(underlying_type$LAYOUT, underlying_type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t underlying_type
     * }
     */
    public static void underlying_type(MemorySegment struct, long fieldValue) {
        struct.set(underlying_type$LAYOUT, underlying_type$OFFSET, fieldValue);
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

