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
 *     ecs_http_method_t method;
 *     char *path;
 *     char *body;
 *     ecs_http_key_value_t headers[32];
 *     ecs_http_key_value_t params[32];
 *     int32_t header_count;
 *     int32_t param_count;
 *     ecs_http_connection_t *conn;
 * }
 * }
 */
public class ecs_http_request_t {

    ecs_http_request_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_LONG_LONG.withName("id"),
        flecs.C_INT.withName("method"),
        MemoryLayout.paddingLayout(4),
        flecs.C_POINTER.withName("path"),
        flecs.C_POINTER.withName("body"),
        MemoryLayout.sequenceLayout(32, ecs_http_key_value_t.layout()).withName("headers"),
        MemoryLayout.sequenceLayout(32, ecs_http_key_value_t.layout()).withName("params"),
        flecs.C_INT.withName("header_count"),
        flecs.C_INT.withName("param_count"),
        flecs.C_POINTER.withName("conn")
    ).withName("$anon$11372:9");

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

    private static final OfInt method$LAYOUT = (OfInt)$LAYOUT.select(groupElement("method"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_http_method_t method
     * }
     */
    public static final OfInt method$layout() {
        return method$LAYOUT;
    }

    private static final long method$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_http_method_t method
     * }
     */
    public static final long method$offset() {
        return method$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_http_method_t method
     * }
     */
    public static int method(MemorySegment struct) {
        return struct.get(method$LAYOUT, method$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_http_method_t method
     * }
     */
    public static void method(MemorySegment struct, int fieldValue) {
        struct.set(method$LAYOUT, method$OFFSET, fieldValue);
    }

    private static final AddressLayout path$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("path"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char *path
     * }
     */
    public static final AddressLayout path$layout() {
        return path$LAYOUT;
    }

    private static final long path$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char *path
     * }
     */
    public static final long path$offset() {
        return path$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char *path
     * }
     */
    public static MemorySegment path(MemorySegment struct) {
        return struct.get(path$LAYOUT, path$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char *path
     * }
     */
    public static void path(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(path$LAYOUT, path$OFFSET, fieldValue);
    }

    private static final AddressLayout body$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("body"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * char *body
     * }
     */
    public static final AddressLayout body$layout() {
        return body$LAYOUT;
    }

    private static final long body$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * char *body
     * }
     */
    public static final long body$offset() {
        return body$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * char *body
     * }
     */
    public static MemorySegment body(MemorySegment struct) {
        return struct.get(body$LAYOUT, body$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * char *body
     * }
     */
    public static void body(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(body$LAYOUT, body$OFFSET, fieldValue);
    }

    private static final SequenceLayout headers$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("headers"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static final SequenceLayout headers$layout() {
        return headers$LAYOUT;
    }

    private static final long headers$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static final long headers$offset() {
        return headers$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static MemorySegment headers(MemorySegment struct) {
        return struct.asSlice(headers$OFFSET, headers$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static void headers(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, headers$OFFSET, headers$LAYOUT.byteSize());
    }

    private static long[] headers$DIMS = { 32 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static long[] headers$dimensions() {
        return headers$DIMS;
    }
    private static final MethodHandle headers$ELEM_HANDLE = headers$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static MemorySegment headers(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)headers$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t headers[32]
     * }
     */
    public static void headers(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, headers(struct, index0), 0L, ecs_http_key_value_t.layout().byteSize());
    }

    private static final SequenceLayout params$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("params"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static final SequenceLayout params$layout() {
        return params$LAYOUT;
    }

    private static final long params$OFFSET = 544;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static final long params$offset() {
        return params$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static MemorySegment params(MemorySegment struct) {
        return struct.asSlice(params$OFFSET, params$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static void params(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, params$OFFSET, params$LAYOUT.byteSize());
    }

    private static long[] params$DIMS = { 32 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static long[] params$dimensions() {
        return params$DIMS;
    }
    private static final MethodHandle params$ELEM_HANDLE = params$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static MemorySegment params(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)params$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ecs_http_key_value_t params[32]
     * }
     */
    public static void params(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, params(struct, index0), 0L, ecs_http_key_value_t.layout().byteSize());
    }

    private static final OfInt header_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("header_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t header_count
     * }
     */
    public static final OfInt header_count$layout() {
        return header_count$LAYOUT;
    }

    private static final long header_count$OFFSET = 1056;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t header_count
     * }
     */
    public static final long header_count$offset() {
        return header_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t header_count
     * }
     */
    public static int header_count(MemorySegment struct) {
        return struct.get(header_count$LAYOUT, header_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t header_count
     * }
     */
    public static void header_count(MemorySegment struct, int fieldValue) {
        struct.set(header_count$LAYOUT, header_count$OFFSET, fieldValue);
    }

    private static final OfInt param_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("param_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t param_count
     * }
     */
    public static final OfInt param_count$layout() {
        return param_count$LAYOUT;
    }

    private static final long param_count$OFFSET = 1060;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t param_count
     * }
     */
    public static final long param_count$offset() {
        return param_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t param_count
     * }
     */
    public static int param_count(MemorySegment struct) {
        return struct.get(param_count$LAYOUT, param_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t param_count
     * }
     */
    public static void param_count(MemorySegment struct, int fieldValue) {
        struct.set(param_count$LAYOUT, param_count$OFFSET, fieldValue);
    }

    private static final AddressLayout conn$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("conn"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_http_connection_t *conn
     * }
     */
    public static final AddressLayout conn$layout() {
        return conn$LAYOUT;
    }

    private static final long conn$OFFSET = 1064;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_http_connection_t *conn
     * }
     */
    public static final long conn$offset() {
        return conn$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_http_connection_t *conn
     * }
     */
    public static MemorySegment conn(MemorySegment struct) {
        return struct.get(conn$LAYOUT, conn$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_http_connection_t *conn
     * }
     */
    public static void conn(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(conn$LAYOUT, conn$OFFSET, fieldValue);
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

