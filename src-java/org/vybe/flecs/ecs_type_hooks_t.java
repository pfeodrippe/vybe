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
 * struct ecs_type_hooks_t {
 *     ecs_xtor_t ctor;
 *     ecs_xtor_t dtor;
 *     ecs_copy_t copy;
 *     ecs_move_t move;
 *     ecs_copy_t copy_ctor;
 *     ecs_move_t move_ctor;
 *     ecs_move_t ctor_move_dtor;
 *     ecs_move_t move_dtor;
 *     ecs_iter_action_t on_add;
 *     ecs_iter_action_t on_set;
 *     ecs_iter_action_t on_remove;
 *     void *ctx;
 *     void *binding_ctx;
 *     ecs_ctx_free_t ctx_free;
 *     ecs_ctx_free_t binding_ctx_free;
 * }
 * }
 */
public class ecs_type_hooks_t {

    ecs_type_hooks_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("ctor"),
        flecs.C_POINTER.withName("dtor"),
        flecs.C_POINTER.withName("copy"),
        flecs.C_POINTER.withName("move"),
        flecs.C_POINTER.withName("copy_ctor"),
        flecs.C_POINTER.withName("move_ctor"),
        flecs.C_POINTER.withName("ctor_move_dtor"),
        flecs.C_POINTER.withName("move_dtor"),
        flecs.C_POINTER.withName("on_add"),
        flecs.C_POINTER.withName("on_set"),
        flecs.C_POINTER.withName("on_remove"),
        flecs.C_POINTER.withName("ctx"),
        flecs.C_POINTER.withName("binding_ctx"),
        flecs.C_POINTER.withName("ctx_free"),
        flecs.C_POINTER.withName("binding_ctx_free")
    ).withName("ecs_type_hooks_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout ctor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ctor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_xtor_t ctor
     * }
     */
    public static final AddressLayout ctor$layout() {
        return ctor$LAYOUT;
    }

    private static final long ctor$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_xtor_t ctor
     * }
     */
    public static final long ctor$offset() {
        return ctor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_xtor_t ctor
     * }
     */
    public static MemorySegment ctor(MemorySegment struct) {
        return struct.get(ctor$LAYOUT, ctor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_xtor_t ctor
     * }
     */
    public static void ctor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ctor$LAYOUT, ctor$OFFSET, fieldValue);
    }

    private static final AddressLayout dtor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("dtor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_xtor_t dtor
     * }
     */
    public static final AddressLayout dtor$layout() {
        return dtor$LAYOUT;
    }

    private static final long dtor$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_xtor_t dtor
     * }
     */
    public static final long dtor$offset() {
        return dtor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_xtor_t dtor
     * }
     */
    public static MemorySegment dtor(MemorySegment struct) {
        return struct.get(dtor$LAYOUT, dtor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_xtor_t dtor
     * }
     */
    public static void dtor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(dtor$LAYOUT, dtor$OFFSET, fieldValue);
    }

    private static final AddressLayout copy$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("copy"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_copy_t copy
     * }
     */
    public static final AddressLayout copy$layout() {
        return copy$LAYOUT;
    }

    private static final long copy$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_copy_t copy
     * }
     */
    public static final long copy$offset() {
        return copy$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_copy_t copy
     * }
     */
    public static MemorySegment copy(MemorySegment struct) {
        return struct.get(copy$LAYOUT, copy$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_copy_t copy
     * }
     */
    public static void copy(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(copy$LAYOUT, copy$OFFSET, fieldValue);
    }

    private static final AddressLayout move$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("move"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_move_t move
     * }
     */
    public static final AddressLayout move$layout() {
        return move$LAYOUT;
    }

    private static final long move$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_move_t move
     * }
     */
    public static final long move$offset() {
        return move$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_move_t move
     * }
     */
    public static MemorySegment move(MemorySegment struct) {
        return struct.get(move$LAYOUT, move$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_move_t move
     * }
     */
    public static void move(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(move$LAYOUT, move$OFFSET, fieldValue);
    }

    private static final AddressLayout copy_ctor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("copy_ctor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_copy_t copy_ctor
     * }
     */
    public static final AddressLayout copy_ctor$layout() {
        return copy_ctor$LAYOUT;
    }

    private static final long copy_ctor$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_copy_t copy_ctor
     * }
     */
    public static final long copy_ctor$offset() {
        return copy_ctor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_copy_t copy_ctor
     * }
     */
    public static MemorySegment copy_ctor(MemorySegment struct) {
        return struct.get(copy_ctor$LAYOUT, copy_ctor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_copy_t copy_ctor
     * }
     */
    public static void copy_ctor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(copy_ctor$LAYOUT, copy_ctor$OFFSET, fieldValue);
    }

    private static final AddressLayout move_ctor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("move_ctor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_move_t move_ctor
     * }
     */
    public static final AddressLayout move_ctor$layout() {
        return move_ctor$LAYOUT;
    }

    private static final long move_ctor$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_move_t move_ctor
     * }
     */
    public static final long move_ctor$offset() {
        return move_ctor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_move_t move_ctor
     * }
     */
    public static MemorySegment move_ctor(MemorySegment struct) {
        return struct.get(move_ctor$LAYOUT, move_ctor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_move_t move_ctor
     * }
     */
    public static void move_ctor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(move_ctor$LAYOUT, move_ctor$OFFSET, fieldValue);
    }

    private static final AddressLayout ctor_move_dtor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("ctor_move_dtor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_move_t ctor_move_dtor
     * }
     */
    public static final AddressLayout ctor_move_dtor$layout() {
        return ctor_move_dtor$LAYOUT;
    }

    private static final long ctor_move_dtor$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_move_t ctor_move_dtor
     * }
     */
    public static final long ctor_move_dtor$offset() {
        return ctor_move_dtor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_move_t ctor_move_dtor
     * }
     */
    public static MemorySegment ctor_move_dtor(MemorySegment struct) {
        return struct.get(ctor_move_dtor$LAYOUT, ctor_move_dtor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_move_t ctor_move_dtor
     * }
     */
    public static void ctor_move_dtor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(ctor_move_dtor$LAYOUT, ctor_move_dtor$OFFSET, fieldValue);
    }

    private static final AddressLayout move_dtor$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("move_dtor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_move_t move_dtor
     * }
     */
    public static final AddressLayout move_dtor$layout() {
        return move_dtor$LAYOUT;
    }

    private static final long move_dtor$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_move_t move_dtor
     * }
     */
    public static final long move_dtor$offset() {
        return move_dtor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_move_t move_dtor
     * }
     */
    public static MemorySegment move_dtor(MemorySegment struct) {
        return struct.get(move_dtor$LAYOUT, move_dtor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_move_t move_dtor
     * }
     */
    public static void move_dtor(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(move_dtor$LAYOUT, move_dtor$OFFSET, fieldValue);
    }

    private static final AddressLayout on_add$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("on_add"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_add
     * }
     */
    public static final AddressLayout on_add$layout() {
        return on_add$LAYOUT;
    }

    private static final long on_add$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_add
     * }
     */
    public static final long on_add$offset() {
        return on_add$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_add
     * }
     */
    public static MemorySegment on_add(MemorySegment struct) {
        return struct.get(on_add$LAYOUT, on_add$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_add
     * }
     */
    public static void on_add(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(on_add$LAYOUT, on_add$OFFSET, fieldValue);
    }

    private static final AddressLayout on_set$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("on_set"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_set
     * }
     */
    public static final AddressLayout on_set$layout() {
        return on_set$LAYOUT;
    }

    private static final long on_set$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_set
     * }
     */
    public static final long on_set$offset() {
        return on_set$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_set
     * }
     */
    public static MemorySegment on_set(MemorySegment struct) {
        return struct.get(on_set$LAYOUT, on_set$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_set
     * }
     */
    public static void on_set(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(on_set$LAYOUT, on_set$OFFSET, fieldValue);
    }

    private static final AddressLayout on_remove$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("on_remove"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_remove
     * }
     */
    public static final AddressLayout on_remove$layout() {
        return on_remove$LAYOUT;
    }

    private static final long on_remove$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_remove
     * }
     */
    public static final long on_remove$offset() {
        return on_remove$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_remove
     * }
     */
    public static MemorySegment on_remove(MemorySegment struct) {
        return struct.get(on_remove$LAYOUT, on_remove$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_iter_action_t on_remove
     * }
     */
    public static void on_remove(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(on_remove$LAYOUT, on_remove$OFFSET, fieldValue);
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

    private static final long ctx$OFFSET = 88;

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

    private static final long binding_ctx$OFFSET = 96;

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

    private static final long ctx_free$OFFSET = 104;

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

    private static final AddressLayout binding_ctx_free$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("binding_ctx_free"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t binding_ctx_free
     * }
     */
    public static final AddressLayout binding_ctx_free$layout() {
        return binding_ctx_free$LAYOUT;
    }

    private static final long binding_ctx_free$OFFSET = 112;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t binding_ctx_free
     * }
     */
    public static final long binding_ctx_free$offset() {
        return binding_ctx_free$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t binding_ctx_free
     * }
     */
    public static MemorySegment binding_ctx_free(MemorySegment struct) {
        return struct.get(binding_ctx_free$LAYOUT, binding_ctx_free$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_ctx_free_t binding_ctx_free
     * }
     */
    public static void binding_ctx_free(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(binding_ctx_free$LAYOUT, binding_ctx_free$OFFSET, fieldValue);
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

