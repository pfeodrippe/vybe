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
 * struct ecs_query_t {
 *     ecs_header_t hdr;
 *     ecs_term_t terms[16];
 *     int32_t sizes[16];
 *     ecs_id_t ids[16];
 *     ecs_flags32_t flags;
 *     int8_t term_count;
 *     int8_t field_count;
 *     ecs_flags16_t fixed_fields;
 *     ecs_flags16_t data_fields;
 *     ecs_flags16_t write_fields;
 *     ecs_flags16_t read_fields;
 *     ecs_flags16_t shared_readonly_fields;
 *     ecs_flags16_t set_fields;
 *     ecs_query_cache_kind_t cache_kind;
 *     void *ctx;
 *     void *binding_ctx;
 *     ecs_entity_t entity;
 *     ecs_world_t *world;
 *     ecs_stage_t *stage;
 *     int32_t eval_count;
 * }
 * }
 */
public class ecs_query_t {

    ecs_query_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ecs_header_t.layout().withName("hdr"),
        MemoryLayout.sequenceLayout(16, ecs_term_t.layout()).withName("terms"),
        MemoryLayout.sequenceLayout(16, flecs.C_INT).withName("sizes"),
        MemoryLayout.sequenceLayout(16, flecs.C_LONG_LONG).withName("ids"),
        flecs.C_INT.withName("flags"),
        flecs.C_CHAR.withName("term_count"),
        flecs.C_CHAR.withName("field_count"),
        flecs.C_SHORT.withName("fixed_fields"),
        flecs.C_SHORT.withName("data_fields"),
        flecs.C_SHORT.withName("write_fields"),
        flecs.C_SHORT.withName("read_fields"),
        flecs.C_SHORT.withName("shared_readonly_fields"),
        flecs.C_SHORT.withName("set_fields"),
        MemoryLayout.paddingLayout(2),
        flecs.C_INT.withName("cache_kind"),
        flecs.C_POINTER.withName("ctx"),
        flecs.C_POINTER.withName("binding_ctx"),
        flecs.C_LONG_LONG.withName("entity"),
        flecs.C_POINTER.withName("world"),
        flecs.C_POINTER.withName("stage"),
        flecs.C_INT.withName("eval_count"),
        MemoryLayout.paddingLayout(4)
    ).withName("ecs_query_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout hdr$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("hdr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_header_t hdr
     * }
     */
    public static final GroupLayout hdr$layout() {
        return hdr$LAYOUT;
    }

    private static final long hdr$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_header_t hdr
     * }
     */
    public static final long hdr$offset() {
        return hdr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_header_t hdr
     * }
     */
    public static MemorySegment hdr(MemorySegment struct) {
        return struct.asSlice(hdr$OFFSET, hdr$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_header_t hdr
     * }
     */
    public static void hdr(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, hdr$OFFSET, hdr$LAYOUT.byteSize());
    }

    private static final SequenceLayout terms$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("terms"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static final SequenceLayout terms$layout() {
        return terms$LAYOUT;
    }

    private static final long terms$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static final long terms$offset() {
        return terms$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static MemorySegment terms(MemorySegment struct) {
        return struct.asSlice(terms$OFFSET, terms$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static void terms(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, terms$OFFSET, terms$LAYOUT.byteSize());
    }

    private static long[] terms$DIMS = { 16 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static long[] terms$dimensions() {
        return terms$DIMS;
    }
    private static final MethodHandle terms$ELEM_HANDLE = terms$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static MemorySegment terms(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)terms$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ecs_term_t terms[16]
     * }
     */
    public static void terms(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, terms(struct, index0), 0L, ecs_term_t.layout().byteSize());
    }

    private static final SequenceLayout sizes$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("sizes"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static final SequenceLayout sizes$layout() {
        return sizes$LAYOUT;
    }

    private static final long sizes$OFFSET = 1176;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static final long sizes$offset() {
        return sizes$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static MemorySegment sizes(MemorySegment struct) {
        return struct.asSlice(sizes$OFFSET, sizes$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static void sizes(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, sizes$OFFSET, sizes$LAYOUT.byteSize());
    }

    private static long[] sizes$DIMS = { 16 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static long[] sizes$dimensions() {
        return sizes$DIMS;
    }
    private static final VarHandle sizes$ELEM_HANDLE = sizes$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static int sizes(MemorySegment struct, long index0) {
        return (int)sizes$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * int32_t sizes[16]
     * }
     */
    public static void sizes(MemorySegment struct, long index0, int fieldValue) {
        sizes$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout ids$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("ids"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static final SequenceLayout ids$layout() {
        return ids$LAYOUT;
    }

    private static final long ids$OFFSET = 1240;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static final long ids$offset() {
        return ids$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static MemorySegment ids(MemorySegment struct) {
        return struct.asSlice(ids$OFFSET, ids$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static void ids(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, ids$OFFSET, ids$LAYOUT.byteSize());
    }

    private static long[] ids$DIMS = { 16 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static long[] ids$dimensions() {
        return ids$DIMS;
    }
    private static final VarHandle ids$ELEM_HANDLE = ids$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static long ids(MemorySegment struct, long index0) {
        return (long)ids$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * ecs_id_t ids[16]
     * }
     */
    public static void ids(MemorySegment struct, long index0, long fieldValue) {
        ids$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final OfInt flags$LAYOUT = (OfInt)$LAYOUT.select(groupElement("flags"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags32_t flags
     * }
     */
    public static final OfInt flags$layout() {
        return flags$LAYOUT;
    }

    private static final long flags$OFFSET = 1368;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags32_t flags
     * }
     */
    public static final long flags$offset() {
        return flags$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags32_t flags
     * }
     */
    public static int flags(MemorySegment struct) {
        return struct.get(flags$LAYOUT, flags$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags32_t flags
     * }
     */
    public static void flags(MemorySegment struct, int fieldValue) {
        struct.set(flags$LAYOUT, flags$OFFSET, fieldValue);
    }

    private static final OfByte term_count$LAYOUT = (OfByte)$LAYOUT.select(groupElement("term_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int8_t term_count
     * }
     */
    public static final OfByte term_count$layout() {
        return term_count$LAYOUT;
    }

    private static final long term_count$OFFSET = 1372;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int8_t term_count
     * }
     */
    public static final long term_count$offset() {
        return term_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int8_t term_count
     * }
     */
    public static byte term_count(MemorySegment struct) {
        return struct.get(term_count$LAYOUT, term_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int8_t term_count
     * }
     */
    public static void term_count(MemorySegment struct, byte fieldValue) {
        struct.set(term_count$LAYOUT, term_count$OFFSET, fieldValue);
    }

    private static final OfByte field_count$LAYOUT = (OfByte)$LAYOUT.select(groupElement("field_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int8_t field_count
     * }
     */
    public static final OfByte field_count$layout() {
        return field_count$LAYOUT;
    }

    private static final long field_count$OFFSET = 1373;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int8_t field_count
     * }
     */
    public static final long field_count$offset() {
        return field_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int8_t field_count
     * }
     */
    public static byte field_count(MemorySegment struct) {
        return struct.get(field_count$LAYOUT, field_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int8_t field_count
     * }
     */
    public static void field_count(MemorySegment struct, byte fieldValue) {
        struct.set(field_count$LAYOUT, field_count$OFFSET, fieldValue);
    }

    private static final OfShort fixed_fields$LAYOUT = (OfShort)$LAYOUT.select(groupElement("fixed_fields"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t fixed_fields
     * }
     */
    public static final OfShort fixed_fields$layout() {
        return fixed_fields$LAYOUT;
    }

    private static final long fixed_fields$OFFSET = 1374;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t fixed_fields
     * }
     */
    public static final long fixed_fields$offset() {
        return fixed_fields$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t fixed_fields
     * }
     */
    public static short fixed_fields(MemorySegment struct) {
        return struct.get(fixed_fields$LAYOUT, fixed_fields$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t fixed_fields
     * }
     */
    public static void fixed_fields(MemorySegment struct, short fieldValue) {
        struct.set(fixed_fields$LAYOUT, fixed_fields$OFFSET, fieldValue);
    }

    private static final OfShort data_fields$LAYOUT = (OfShort)$LAYOUT.select(groupElement("data_fields"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t data_fields
     * }
     */
    public static final OfShort data_fields$layout() {
        return data_fields$LAYOUT;
    }

    private static final long data_fields$OFFSET = 1376;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t data_fields
     * }
     */
    public static final long data_fields$offset() {
        return data_fields$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t data_fields
     * }
     */
    public static short data_fields(MemorySegment struct) {
        return struct.get(data_fields$LAYOUT, data_fields$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t data_fields
     * }
     */
    public static void data_fields(MemorySegment struct, short fieldValue) {
        struct.set(data_fields$LAYOUT, data_fields$OFFSET, fieldValue);
    }

    private static final OfShort write_fields$LAYOUT = (OfShort)$LAYOUT.select(groupElement("write_fields"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t write_fields
     * }
     */
    public static final OfShort write_fields$layout() {
        return write_fields$LAYOUT;
    }

    private static final long write_fields$OFFSET = 1378;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t write_fields
     * }
     */
    public static final long write_fields$offset() {
        return write_fields$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t write_fields
     * }
     */
    public static short write_fields(MemorySegment struct) {
        return struct.get(write_fields$LAYOUT, write_fields$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t write_fields
     * }
     */
    public static void write_fields(MemorySegment struct, short fieldValue) {
        struct.set(write_fields$LAYOUT, write_fields$OFFSET, fieldValue);
    }

    private static final OfShort read_fields$LAYOUT = (OfShort)$LAYOUT.select(groupElement("read_fields"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t read_fields
     * }
     */
    public static final OfShort read_fields$layout() {
        return read_fields$LAYOUT;
    }

    private static final long read_fields$OFFSET = 1380;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t read_fields
     * }
     */
    public static final long read_fields$offset() {
        return read_fields$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t read_fields
     * }
     */
    public static short read_fields(MemorySegment struct) {
        return struct.get(read_fields$LAYOUT, read_fields$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t read_fields
     * }
     */
    public static void read_fields(MemorySegment struct, short fieldValue) {
        struct.set(read_fields$LAYOUT, read_fields$OFFSET, fieldValue);
    }

    private static final OfShort shared_readonly_fields$LAYOUT = (OfShort)$LAYOUT.select(groupElement("shared_readonly_fields"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t shared_readonly_fields
     * }
     */
    public static final OfShort shared_readonly_fields$layout() {
        return shared_readonly_fields$LAYOUT;
    }

    private static final long shared_readonly_fields$OFFSET = 1382;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t shared_readonly_fields
     * }
     */
    public static final long shared_readonly_fields$offset() {
        return shared_readonly_fields$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t shared_readonly_fields
     * }
     */
    public static short shared_readonly_fields(MemorySegment struct) {
        return struct.get(shared_readonly_fields$LAYOUT, shared_readonly_fields$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t shared_readonly_fields
     * }
     */
    public static void shared_readonly_fields(MemorySegment struct, short fieldValue) {
        struct.set(shared_readonly_fields$LAYOUT, shared_readonly_fields$OFFSET, fieldValue);
    }

    private static final OfShort set_fields$LAYOUT = (OfShort)$LAYOUT.select(groupElement("set_fields"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_flags16_t set_fields
     * }
     */
    public static final OfShort set_fields$layout() {
        return set_fields$LAYOUT;
    }

    private static final long set_fields$OFFSET = 1384;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_flags16_t set_fields
     * }
     */
    public static final long set_fields$offset() {
        return set_fields$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_flags16_t set_fields
     * }
     */
    public static short set_fields(MemorySegment struct) {
        return struct.get(set_fields$LAYOUT, set_fields$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_flags16_t set_fields
     * }
     */
    public static void set_fields(MemorySegment struct, short fieldValue) {
        struct.set(set_fields$LAYOUT, set_fields$OFFSET, fieldValue);
    }

    private static final OfInt cache_kind$LAYOUT = (OfInt)$LAYOUT.select(groupElement("cache_kind"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_query_cache_kind_t cache_kind
     * }
     */
    public static final OfInt cache_kind$layout() {
        return cache_kind$LAYOUT;
    }

    private static final long cache_kind$OFFSET = 1388;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_query_cache_kind_t cache_kind
     * }
     */
    public static final long cache_kind$offset() {
        return cache_kind$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_query_cache_kind_t cache_kind
     * }
     */
    public static int cache_kind(MemorySegment struct) {
        return struct.get(cache_kind$LAYOUT, cache_kind$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_query_cache_kind_t cache_kind
     * }
     */
    public static void cache_kind(MemorySegment struct, int fieldValue) {
        struct.set(cache_kind$LAYOUT, cache_kind$OFFSET, fieldValue);
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

    private static final long ctx$OFFSET = 1392;

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

    private static final AddressLayout binding_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("binding_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *binding_ctx
     * }
     */
    public static final AddressLayout binding_ctx$layout() {
        return binding_ctx$LAYOUT;
    }

    private static final long binding_ctx$OFFSET = 1400;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *binding_ctx
     * }
     */
    public static final long binding_ctx$offset() {
        return binding_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *binding_ctx
     * }
     */
    public static MemorySegment binding_ctx(MemorySegment struct) {
        return struct.get(binding_ctx$LAYOUT, binding_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *binding_ctx
     * }
     */
    public static void binding_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(binding_ctx$LAYOUT, binding_ctx$OFFSET, fieldValue);
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

    private static final long entity$OFFSET = 1408;

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

    private static final AddressLayout world$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("world"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_world_t *world
     * }
     */
    public static final AddressLayout world$layout() {
        return world$LAYOUT;
    }

    private static final long world$OFFSET = 1416;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_world_t *world
     * }
     */
    public static final long world$offset() {
        return world$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_world_t *world
     * }
     */
    public static MemorySegment world(MemorySegment struct) {
        return struct.get(world$LAYOUT, world$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_world_t *world
     * }
     */
    public static void world(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(world$LAYOUT, world$OFFSET, fieldValue);
    }

    private static final AddressLayout stage$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("stage"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_stage_t *stage
     * }
     */
    public static final AddressLayout stage$layout() {
        return stage$LAYOUT;
    }

    private static final long stage$OFFSET = 1424;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_stage_t *stage
     * }
     */
    public static final long stage$offset() {
        return stage$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_stage_t *stage
     * }
     */
    public static MemorySegment stage(MemorySegment struct) {
        return struct.get(stage$LAYOUT, stage$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_stage_t *stage
     * }
     */
    public static void stage(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(stage$LAYOUT, stage$OFFSET, fieldValue);
    }

    private static final OfInt eval_count$LAYOUT = (OfInt)$LAYOUT.select(groupElement("eval_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int32_t eval_count
     * }
     */
    public static final OfInt eval_count$layout() {
        return eval_count$LAYOUT;
    }

    private static final long eval_count$OFFSET = 1432;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int32_t eval_count
     * }
     */
    public static final long eval_count$offset() {
        return eval_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int32_t eval_count
     * }
     */
    public static int eval_count(MemorySegment struct) {
        return struct.get(eval_count$LAYOUT, eval_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int32_t eval_count
     * }
     */
    public static void eval_count(MemorySegment struct, int fieldValue) {
        struct.set(eval_count$LAYOUT, eval_count$OFFSET, fieldValue);
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

