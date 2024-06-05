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
 * struct ecs_system_desc_t {
 *     int32_t _canary;
 *     ecs_entity_t entity;
 *     ecs_query_desc_t query;
 *     ecs_iter_action_t callback;
 *     ecs_run_action_t run;
 *     void *ctx;
 *     ecs_ctx_free_t ctx_free;
 *     void *callback_ctx;
 *     ecs_ctx_free_t callback_ctx_free;
 *     void *run_ctx;
 *     ecs_ctx_free_t run_ctx_free;
 *     float interval;
 *     int32_t rate;
 *     ecs_entity_t tick_source;
 *     bool multi_threaded;
 *     bool immediate;
 * }
 * }
 */
public class ecs_system_desc_t {

    ecs_system_desc_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_INT.withName("_canary"),
        MemoryLayout.paddingLayout(4),
        flecs.C_LONG_LONG.withName("entity"),
        ecs_query_desc_t.layout().withName("query"),
        flecs.C_POINTER.withName("callback"),
        flecs.C_POINTER.withName("run"),
        flecs.C_POINTER.withName("ctx"),
        flecs.C_POINTER.withName("ctx_free"),
        flecs.C_POINTER.withName("callback_ctx"),
        flecs.C_POINTER.withName("callback_ctx_free"),
        flecs.C_POINTER.withName("run_ctx"),
        flecs.C_POINTER.withName("run_ctx_free"),
        flecs.C_FLOAT.withName("interval"),
        flecs.C_INT.withName("rate"),
        flecs.C_LONG_LONG.withName("tick_source"),
        flecs.C_BOOL.withName("multi_threaded"),
        flecs.C_BOOL.withName("immediate"),
        MemoryLayout.paddingLayout(6)
    ).withName("ecs_system_desc_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt _canary$LAYOUT = (OfInt)$LAYOUT.select(groupElement("_canary"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static final OfInt _canary$layout() {
        return _canary$LAYOUT;
    }

    private static final long _canary$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static final long _canary$offset() {
        return _canary$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static int _canary(MemorySegment struct) {
        return struct.get(_canary$LAYOUT, _canary$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t _canary
     * }
     */
    public static void _canary(MemorySegment struct, int fieldValue) {
        struct.set(_canary$LAYOUT, _canary$OFFSET, fieldValue);
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

    private static final long entity$OFFSET = 8;

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

    private static final GroupLayout query$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("query"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_query_desc_t query
     * }
     */
    public static final GroupLayout query$layout() {
        return query$LAYOUT;
    }

    private static final long query$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_query_desc_t query
     * }
     */
    public static final long query$offset() {
        return query$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_query_desc_t query
     * }
     */
    public static MemorySegment query(MemorySegment struct) {
        return struct.asSlice(query$OFFSET, query$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_query_desc_t query
     * }
     */
    public static void query(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, query$OFFSET, query$LAYOUT.byteSize());
    }

    private static final AddressLayout callback$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("callback"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_iter_action_t callback
     * }
     */
    public static final AddressLayout callback$layout() {
        return callback$LAYOUT;
    }

    private static final long callback$OFFSET = 1304;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_iter_action_t callback
     * }
     */
    public static final long callback$offset() {
        return callback$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t callback
     * }
     */
    public static MemorySegment callback(MemorySegment struct) {
        return struct.get(callback$LAYOUT, callback$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t callback
     * }
     */
    public static void callback(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(callback$LAYOUT, callback$OFFSET, fieldValue);
    }

    private static final AddressLayout run$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("run"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_run_action_t run
     * }
     */
    public static final AddressLayout run$layout() {
        return run$LAYOUT;
    }

    private static final long run$OFFSET = 1312;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_run_action_t run
     * }
     */
    public static final long run$offset() {
        return run$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_run_action_t run
     * }
     */
    public static MemorySegment run(MemorySegment struct) {
        return struct.get(run$LAYOUT, run$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_run_action_t run
     * }
     */
    public static void run(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(run$LAYOUT, run$OFFSET, fieldValue);
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

    private static final long ctx$OFFSET = 1320;

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

    private static final AddressLayout ctx_free$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ctx_free"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t ctx_free
     * }
     */
    public static final AddressLayout ctx_free$layout() {
        return ctx_free$LAYOUT;
    }

    private static final long ctx_free$OFFSET = 1328;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t ctx_free
     * }
     */
    public static final long ctx_free$offset() {
        return ctx_free$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t ctx_free
     * }
     */
    public static MemorySegment ctx_free(MemorySegment struct) {
        return struct.get(ctx_free$LAYOUT, ctx_free$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t ctx_free
     * }
     */
    public static void ctx_free(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ctx_free$LAYOUT, ctx_free$OFFSET, fieldValue);
    }

    private static final AddressLayout callback_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("callback_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *callback_ctx
     * }
     */
    public static final AddressLayout callback_ctx$layout() {
        return callback_ctx$LAYOUT;
    }

    private static final long callback_ctx$OFFSET = 1336;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *callback_ctx
     * }
     */
    public static final long callback_ctx$offset() {
        return callback_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *callback_ctx
     * }
     */
    public static MemorySegment callback_ctx(MemorySegment struct) {
        return struct.get(callback_ctx$LAYOUT, callback_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *callback_ctx
     * }
     */
    public static void callback_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(callback_ctx$LAYOUT, callback_ctx$OFFSET, fieldValue);
    }

    private static final AddressLayout callback_ctx_free$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("callback_ctx_free"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t callback_ctx_free
     * }
     */
    public static final AddressLayout callback_ctx_free$layout() {
        return callback_ctx_free$LAYOUT;
    }

    private static final long callback_ctx_free$OFFSET = 1344;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t callback_ctx_free
     * }
     */
    public static final long callback_ctx_free$offset() {
        return callback_ctx_free$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t callback_ctx_free
     * }
     */
    public static MemorySegment callback_ctx_free(MemorySegment struct) {
        return struct.get(callback_ctx_free$LAYOUT, callback_ctx_free$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t callback_ctx_free
     * }
     */
    public static void callback_ctx_free(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(callback_ctx_free$LAYOUT, callback_ctx_free$OFFSET, fieldValue);
    }

    private static final AddressLayout run_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("run_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *run_ctx
     * }
     */
    public static final AddressLayout run_ctx$layout() {
        return run_ctx$LAYOUT;
    }

    private static final long run_ctx$OFFSET = 1352;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *run_ctx
     * }
     */
    public static final long run_ctx$offset() {
        return run_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *run_ctx
     * }
     */
    public static MemorySegment run_ctx(MemorySegment struct) {
        return struct.get(run_ctx$LAYOUT, run_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *run_ctx
     * }
     */
    public static void run_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(run_ctx$LAYOUT, run_ctx$OFFSET, fieldValue);
    }

    private static final AddressLayout run_ctx_free$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("run_ctx_free"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t run_ctx_free
     * }
     */
    public static final AddressLayout run_ctx_free$layout() {
        return run_ctx_free$LAYOUT;
    }

    private static final long run_ctx_free$OFFSET = 1360;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t run_ctx_free
     * }
     */
    public static final long run_ctx_free$offset() {
        return run_ctx_free$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t run_ctx_free
     * }
     */
    public static MemorySegment run_ctx_free(MemorySegment struct) {
        return struct.get(run_ctx_free$LAYOUT, run_ctx_free$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t run_ctx_free
     * }
     */
    public static void run_ctx_free(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(run_ctx_free$LAYOUT, run_ctx_free$OFFSET, fieldValue);
    }

    private static final OfFloat interval$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("interval"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float interval
     * }
     */
    public static final OfFloat interval$layout() {
        return interval$LAYOUT;
    }

    private static final long interval$OFFSET = 1368;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float interval
     * }
     */
    public static final long interval$offset() {
        return interval$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float interval
     * }
     */
    public static float interval(MemorySegment struct) {
        return struct.get(interval$LAYOUT, interval$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float interval
     * }
     */
    public static void interval(MemorySegment struct, float fieldValue) {
        struct.set(interval$LAYOUT, interval$OFFSET, fieldValue);
    }

    private static final OfInt rate$LAYOUT = (OfInt)$LAYOUT.select(groupElement("rate"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t rate
     * }
     */
    public static final OfInt rate$layout() {
        return rate$LAYOUT;
    }

    private static final long rate$OFFSET = 1372;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t rate
     * }
     */
    public static final long rate$offset() {
        return rate$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t rate
     * }
     */
    public static int rate(MemorySegment struct) {
        return struct.get(rate$LAYOUT, rate$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t rate
     * }
     */
    public static void rate(MemorySegment struct, int fieldValue) {
        struct.set(rate$LAYOUT, rate$OFFSET, fieldValue);
    }

    private static final OfLong tick_source$LAYOUT = (OfLong)$LAYOUT.select(groupElement("tick_source"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_entity_t tick_source
     * }
     */
    public static final OfLong tick_source$layout() {
        return tick_source$LAYOUT;
    }

    private static final long tick_source$OFFSET = 1376;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_entity_t tick_source
     * }
     */
    public static final long tick_source$offset() {
        return tick_source$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_entity_t tick_source
     * }
     */
    public static long tick_source(MemorySegment struct) {
        return struct.get(tick_source$LAYOUT, tick_source$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_entity_t tick_source
     * }
     */
    public static void tick_source(MemorySegment struct, long fieldValue) {
        struct.set(tick_source$LAYOUT, tick_source$OFFSET, fieldValue);
    }

    private static final OfBoolean multi_threaded$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("multi_threaded"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static final OfBoolean multi_threaded$layout() {
        return multi_threaded$LAYOUT;
    }

    private static final long multi_threaded$OFFSET = 1384;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static final long multi_threaded$offset() {
        return multi_threaded$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static boolean multi_threaded(MemorySegment struct) {
        return struct.get(multi_threaded$LAYOUT, multi_threaded$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool multi_threaded
     * }
     */
    public static void multi_threaded(MemorySegment struct, boolean fieldValue) {
        struct.set(multi_threaded$LAYOUT, multi_threaded$OFFSET, fieldValue);
    }

    private static final OfBoolean immediate$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("immediate"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static final OfBoolean immediate$layout() {
        return immediate$LAYOUT;
    }

    private static final long immediate$OFFSET = 1385;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static final long immediate$offset() {
        return immediate$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static boolean immediate(MemorySegment struct) {
        return struct.get(immediate$LAYOUT, immediate$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool immediate
     * }
     */
    public static void immediate(MemorySegment struct, boolean fieldValue) {
        struct.set(immediate$LAYOUT, immediate$OFFSET, fieldValue);
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

