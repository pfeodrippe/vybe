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
 *     ecs_http_reply_action_t callback;
 *     void *ctx;
 *     uint16_t port;
 *     const char *ipaddr;
 *     int32_t send_queue_wait_ms;
 *     double cache_timeout;
 *     double cache_purge_timeout;
 * }
 * }
 */
public class ecs_http_server_desc_t {

    ecs_http_server_desc_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("callback"),
        flecs.C_POINTER.withName("ctx"),
        flecs.C_SHORT.withName("port"),
        MemoryLayout.paddingLayout(6),
        flecs.C_POINTER.withName("ipaddr"),
        flecs.C_INT.withName("send_queue_wait_ms"),
        MemoryLayout.paddingLayout(4),
        flecs.C_DOUBLE.withName("cache_timeout"),
        flecs.C_DOUBLE.withName("cache_purge_timeout")
    ).withName("$anon$10921:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout callback$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("callback"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_http_reply_action_t callback
     * }
     */
    public static final AddressLayout callback$layout() {
        return callback$LAYOUT;
    }

    private static final long callback$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_http_reply_action_t callback
     * }
     */
    public static final long callback$offset() {
        return callback$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_http_reply_action_t callback
     * }
     */
    public static MemorySegment callback(MemorySegment struct) {
        return struct.get(callback$LAYOUT, callback$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_http_reply_action_t callback
     * }
     */
    public static void callback(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(callback$LAYOUT, callback$OFFSET, fieldValue);
    }

    private static final AddressLayout ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *ctx
     * }
     */
    public static final AddressLayout ctx$layout() {
        return ctx$LAYOUT;
    }

    private static final long ctx$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *ctx
     * }
     */
    public static final long ctx$offset() {
        return ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *ctx
     * }
     */
    public static MemorySegment ctx(MemorySegment struct) {
        return struct.get(ctx$LAYOUT, ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *ctx
     * }
     */
    public static void ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ctx$LAYOUT, ctx$OFFSET, fieldValue);
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

    private static final long port$OFFSET = 16;

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
     * const char *ipaddr
     * }
     */
    public static final AddressLayout ipaddr$layout() {
        return ipaddr$LAYOUT;
    }

    private static final long ipaddr$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *ipaddr
     * }
     */
    public static final long ipaddr$offset() {
        return ipaddr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *ipaddr
     * }
     */
    public static MemorySegment ipaddr(MemorySegment struct) {
        return struct.get(ipaddr$LAYOUT, ipaddr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *ipaddr
     * }
     */
    public static void ipaddr(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ipaddr$LAYOUT, ipaddr$OFFSET, fieldValue);
    }

    private static final OfInt send_queue_wait_ms$LAYOUT = (OfInt)$LAYOUT.select(groupElement("send_queue_wait_ms"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t send_queue_wait_ms
     * }
     */
    public static final OfInt send_queue_wait_ms$layout() {
        return send_queue_wait_ms$LAYOUT;
    }

    private static final long send_queue_wait_ms$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t send_queue_wait_ms
     * }
     */
    public static final long send_queue_wait_ms$offset() {
        return send_queue_wait_ms$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t send_queue_wait_ms
     * }
     */
    public static int send_queue_wait_ms(MemorySegment struct) {
        return struct.get(send_queue_wait_ms$LAYOUT, send_queue_wait_ms$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t send_queue_wait_ms
     * }
     */
    public static void send_queue_wait_ms(MemorySegment struct, int fieldValue) {
        struct.set(send_queue_wait_ms$LAYOUT, send_queue_wait_ms$OFFSET, fieldValue);
    }

    private static final OfDouble cache_timeout$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("cache_timeout"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double cache_timeout
     * }
     */
    public static final OfDouble cache_timeout$layout() {
        return cache_timeout$LAYOUT;
    }

    private static final long cache_timeout$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double cache_timeout
     * }
     */
    public static final long cache_timeout$offset() {
        return cache_timeout$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double cache_timeout
     * }
     */
    public static double cache_timeout(MemorySegment struct) {
        return struct.get(cache_timeout$LAYOUT, cache_timeout$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double cache_timeout
     * }
     */
    public static void cache_timeout(MemorySegment struct, double fieldValue) {
        struct.set(cache_timeout$LAYOUT, cache_timeout$OFFSET, fieldValue);
    }

    private static final OfDouble cache_purge_timeout$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("cache_purge_timeout"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double cache_purge_timeout
     * }
     */
    public static final OfDouble cache_purge_timeout$layout() {
        return cache_purge_timeout$LAYOUT;
    }

    private static final long cache_purge_timeout$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double cache_purge_timeout
     * }
     */
    public static final long cache_purge_timeout$offset() {
        return cache_purge_timeout$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double cache_purge_timeout
     * }
     */
    public static double cache_purge_timeout(MemorySegment struct) {
        return struct.get(cache_purge_timeout$LAYOUT, cache_purge_timeout$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double cache_purge_timeout
     * }
     */
    public static void cache_purge_timeout(MemorySegment struct, double fieldValue) {
        struct.set(cache_purge_timeout$LAYOUT, cache_purge_timeout$OFFSET, fieldValue);
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

