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
 * struct {
 *     uint16_t port;
 *     char *ipaddr;
 *     void *impl;
 * }
 * }
 */
public class EcsRest {

    EcsRest() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_SHORT.withName("port"),
        MemoryLayout.paddingLayout(6),
        flecs.C_POINTER.withName("ipaddr"),
        flecs.C_POINTER.withName("impl")
    ).withName("$anon$11095:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfShort port$LAYOUT = (OfShort)$LAYOUT.select(groupElement("port"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static final OfShort port$layout() {
        return port$LAYOUT;
    }

    private static final long port$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static final long port$offset() {
        return port$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static short port(MemorySegment struct) {
        return struct.get(port$LAYOUT, port$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static void port(MemorySegment struct, short fieldValue) {
        struct.set(port$LAYOUT, port$OFFSET, fieldValue);
    }

    private static final AddressLayout ipaddr$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ipaddr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char *ipaddr
     * }
     */
    public static final AddressLayout ipaddr$layout() {
        return ipaddr$LAYOUT;
    }

    private static final long ipaddr$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char *ipaddr
     * }
     */
    public static final long ipaddr$offset() {
        return ipaddr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char *ipaddr
     * }
     */
    public static MemorySegment ipaddr(MemorySegment struct) {
        return struct.get(ipaddr$LAYOUT, ipaddr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char *ipaddr
     * }
     */
    public static void ipaddr(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ipaddr$LAYOUT, ipaddr$OFFSET, fieldValue);
    }

    private static final AddressLayout impl$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("impl"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *impl
     * }
     */
    public static final AddressLayout impl$layout() {
        return impl$LAYOUT;
    }

    private static final long impl$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *impl
     * }
     */
    public static final long impl$offset() {
        return impl$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *impl
     * }
     */
    public static MemorySegment impl(MemorySegment struct) {
        return struct.get(impl$LAYOUT, impl$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *impl
     * }
     */
    public static void impl(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(impl$LAYOUT, impl$OFFSET, fieldValue);
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

