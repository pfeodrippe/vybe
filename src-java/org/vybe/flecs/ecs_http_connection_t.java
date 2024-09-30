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
 *     uint64_t id;
 *     ecs_http_server_t *server;
 *     char host[128];
 *     char port[16];
 * }
 * }
 */
public class ecs_http_connection_t {

    ecs_http_connection_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_LONG_LONG.withName("id"),
        flecs.C_POINTER.withName("server"),
        MemoryLayout.sequenceLayout(128, flecs.C_CHAR).withName("host"),
        MemoryLayout.sequenceLayout(16, flecs.C_CHAR).withName("port")
    ).withName("$anon$11206:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong id$LAYOUT = (OfLong)$LAYOUT.select(groupElement("id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static final OfLong id$layout() {
        return id$LAYOUT;
    }

    private static final long id$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static final long id$offset() {
        return id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static long id(MemorySegment struct) {
        return struct.get(id$LAYOUT, id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t id
     * }
     */
    public static void id(MemorySegment struct, long fieldValue) {
        struct.set(id$LAYOUT, id$OFFSET, fieldValue);
    }

    private static final AddressLayout server$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("server"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_http_server_t *server
     * }
     */
    public static final AddressLayout server$layout() {
        return server$LAYOUT;
    }

    private static final long server$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_http_server_t *server
     * }
     */
    public static final long server$offset() {
        return server$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_http_server_t *server
     * }
     */
    public static MemorySegment server(MemorySegment struct) {
        return struct.get(server$LAYOUT, server$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_http_server_t *server
     * }
     */
    public static void server(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(server$LAYOUT, server$OFFSET, fieldValue);
    }

    private static final SequenceLayout host$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("host"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static final SequenceLayout host$layout() {
        return host$LAYOUT;
    }

    private static final long host$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static final long host$offset() {
        return host$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static MemorySegment host(MemorySegment struct) {
        return struct.asSlice(host$OFFSET, host$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static void host(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, host$OFFSET, host$LAYOUT.byteSize());
    }

    private static long[] host$DIMS = { 128 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static long[] host$dimensions() {
        return host$DIMS;
    }
    private static final VarHandle host$ELEM_HANDLE = host$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static byte host(MemorySegment struct, long index0) {
        return (byte)host$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * char host[128]
     * }
     */
    public static void host(MemorySegment struct, long index0, byte fieldValue) {
        host$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout port$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("port"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static final SequenceLayout port$layout() {
        return port$LAYOUT;
    }

    private static final long port$OFFSET = 144;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static final long port$offset() {
        return port$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static MemorySegment port(MemorySegment struct) {
        return struct.asSlice(port$OFFSET, port$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static void port(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, port$OFFSET, port$LAYOUT.byteSize());
    }

    private static long[] port$DIMS = { 16 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static long[] port$dimensions() {
        return port$DIMS;
    }
    private static final VarHandle port$ELEM_HANDLE = port$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static byte port(MemorySegment struct, long index0) {
        return (byte)port$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * char port[16]
     * }
     */
    public static void port(MemorySegment struct, long index0, byte fieldValue) {
        port$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
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

