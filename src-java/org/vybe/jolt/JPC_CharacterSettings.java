// Generated by jextract

package org.vybe.jolt;

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
 * struct JPC_CharacterSettings {
 *     JPC_CharacterBaseSettings base;
 *     JPC_ObjectLayer layer;
 *     float mass;
 *     float friction;
 *     float gravity_factor;
 * }
 * }
 */
public class JPC_CharacterSettings {

    JPC_CharacterSettings() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        JPC_CharacterBaseSettings.layout().withName("base"),
        jolt.C_SHORT.withName("layer"),
        MemoryLayout.paddingLayout(2),
        jolt.C_FLOAT.withName("mass"),
        jolt.C_FLOAT.withName("friction"),
        jolt.C_FLOAT.withName("gravity_factor")
    ).withName("JPC_CharacterSettings");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout base$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("base"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_CharacterBaseSettings base
     * }
     */
    public static final GroupLayout base$layout() {
        return base$LAYOUT;
    }

    private static final long base$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_CharacterBaseSettings base
     * }
     */
    public static final long base$offset() {
        return base$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_CharacterBaseSettings base
     * }
     */
    public static MemorySegment base(MemorySegment struct) {
        return struct.asSlice(base$OFFSET, base$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_CharacterBaseSettings base
     * }
     */
    public static void base(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, base$OFFSET, base$LAYOUT.byteSize());
    }

    private static final OfShort layer$LAYOUT = (OfShort)$LAYOUT.select(groupElement("layer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer layer
     * }
     */
    public static final OfShort layer$layout() {
        return layer$LAYOUT;
    }

    private static final long layer$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer layer
     * }
     */
    public static final long layer$offset() {
        return layer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer layer
     * }
     */
    public static short layer(MemorySegment struct) {
        return struct.get(layer$LAYOUT, layer$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer layer
     * }
     */
    public static void layer(MemorySegment struct, short fieldValue) {
        struct.set(layer$LAYOUT, layer$OFFSET, fieldValue);
    }

    private static final OfFloat mass$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("mass"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float mass
     * }
     */
    public static final OfFloat mass$layout() {
        return mass$LAYOUT;
    }

    private static final long mass$OFFSET = 68;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float mass
     * }
     */
    public static final long mass$offset() {
        return mass$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float mass
     * }
     */
    public static float mass(MemorySegment struct) {
        return struct.get(mass$LAYOUT, mass$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float mass
     * }
     */
    public static void mass(MemorySegment struct, float fieldValue) {
        struct.set(mass$LAYOUT, mass$OFFSET, fieldValue);
    }

    private static final OfFloat friction$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("friction"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static final OfFloat friction$layout() {
        return friction$LAYOUT;
    }

    private static final long friction$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static final long friction$offset() {
        return friction$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static float friction(MemorySegment struct) {
        return struct.get(friction$LAYOUT, friction$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static void friction(MemorySegment struct, float fieldValue) {
        struct.set(friction$LAYOUT, friction$OFFSET, fieldValue);
    }

    private static final OfFloat gravity_factor$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("gravity_factor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static final OfFloat gravity_factor$layout() {
        return gravity_factor$LAYOUT;
    }

    private static final long gravity_factor$OFFSET = 76;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static final long gravity_factor$offset() {
        return gravity_factor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static float gravity_factor(MemorySegment struct) {
        return struct.get(gravity_factor$LAYOUT, gravity_factor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static void gravity_factor(MemorySegment struct, float fieldValue) {
        struct.set(gravity_factor$LAYOUT, gravity_factor$OFFSET, fieldValue);
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
