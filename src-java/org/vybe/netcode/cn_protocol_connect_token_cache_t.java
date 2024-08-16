// Generated by jextract

package org.vybe.netcode;

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
 * struct cn_protocol_connect_token_cache_t {
 *     int capacity;
 *     cn_hashtable_t table;
 *     cn_list_t list;
 *     cn_list_t free_list;
 *     cn_protocol_connect_token_cache_node_t *node_memory;
 *     void *mem_ctx;
 * }
 * }
 */
public class cn_protocol_connect_token_cache_t {

    cn_protocol_connect_token_cache_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_INT.withName("capacity"),
        MemoryLayout.paddingLayout(4),
        cn_hashtable_t.layout().withName("table"),
        cn_list_t.layout().withName("list"),
        cn_list_t.layout().withName("free_list"),
        netcode.C_POINTER.withName("node_memory"),
        netcode.C_POINTER.withName("mem_ctx")
    ).withName("cn_protocol_connect_token_cache_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt capacity$LAYOUT = (OfInt)$LAYOUT.select(groupElement("capacity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int capacity
     * }
     */
    public static final OfInt capacity$layout() {
        return capacity$LAYOUT;
    }

    private static final long capacity$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int capacity
     * }
     */
    public static final long capacity$offset() {
        return capacity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int capacity
     * }
     */
    public static int capacity(MemorySegment struct) {
        return struct.get(capacity$LAYOUT, capacity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int capacity
     * }
     */
    public static void capacity(MemorySegment struct, int fieldValue) {
        struct.set(capacity$LAYOUT, capacity$OFFSET, fieldValue);
    }

    private static final GroupLayout table$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("table"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_hashtable_t table
     * }
     */
    public static final GroupLayout table$layout() {
        return table$LAYOUT;
    }

    private static final long table$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_hashtable_t table
     * }
     */
    public static final long table$offset() {
        return table$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_hashtable_t table
     * }
     */
    public static MemorySegment table(MemorySegment struct) {
        return struct.asSlice(table$OFFSET, table$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_hashtable_t table
     * }
     */
    public static void table(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, table$OFFSET, table$LAYOUT.byteSize());
    }

    private static final GroupLayout list$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("list"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_list_t list
     * }
     */
    public static final GroupLayout list$layout() {
        return list$LAYOUT;
    }

    private static final long list$OFFSET = 120;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_list_t list
     * }
     */
    public static final long list$offset() {
        return list$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_list_t list
     * }
     */
    public static MemorySegment list(MemorySegment struct) {
        return struct.asSlice(list$OFFSET, list$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_list_t list
     * }
     */
    public static void list(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, list$OFFSET, list$LAYOUT.byteSize());
    }

    private static final GroupLayout free_list$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("free_list"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_list_t free_list
     * }
     */
    public static final GroupLayout free_list$layout() {
        return free_list$LAYOUT;
    }

    private static final long free_list$OFFSET = 136;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_list_t free_list
     * }
     */
    public static final long free_list$offset() {
        return free_list$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_list_t free_list
     * }
     */
    public static MemorySegment free_list(MemorySegment struct) {
        return struct.asSlice(free_list$OFFSET, free_list$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_list_t free_list
     * }
     */
    public static void free_list(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, free_list$OFFSET, free_list$LAYOUT.byteSize());
    }

    private static final AddressLayout node_memory$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("node_memory"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_cache_node_t *node_memory
     * }
     */
    public static final AddressLayout node_memory$layout() {
        return node_memory$LAYOUT;
    }

    private static final long node_memory$OFFSET = 152;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_cache_node_t *node_memory
     * }
     */
    public static final long node_memory$offset() {
        return node_memory$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_cache_node_t *node_memory
     * }
     */
    public static MemorySegment node_memory(MemorySegment struct) {
        return struct.get(node_memory$LAYOUT, node_memory$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_cache_node_t *node_memory
     * }
     */
    public static void node_memory(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(node_memory$LAYOUT, node_memory$OFFSET, fieldValue);
    }

    private static final AddressLayout mem_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("mem_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static final AddressLayout mem_ctx$layout() {
        return mem_ctx$LAYOUT;
    }

    private static final long mem_ctx$OFFSET = 160;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static final long mem_ctx$offset() {
        return mem_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static MemorySegment mem_ctx(MemorySegment struct) {
        return struct.get(mem_ctx$LAYOUT, mem_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static void mem_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(mem_ctx$LAYOUT, mem_ctx$OFFSET, fieldValue);
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
