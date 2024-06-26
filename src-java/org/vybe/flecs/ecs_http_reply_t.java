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
 *     int code;
 *     ecs_strbuf_t body;
 *     const char *status;
 *     const char *content_type;
 *     ecs_strbuf_t headers;
 * }
 * }
 */
public class ecs_http_reply_t {

    ecs_http_reply_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_INT.withName("code"),
        MemoryLayout.paddingLayout(4),
        ecs_strbuf_t.layout().withName("body"),
        flecs.C_POINTER.withName("status"),
        flecs.C_POINTER.withName("content_type"),
        ecs_strbuf_t.layout().withName("headers")
    ).withName("$anon$10888:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt code$LAYOUT = (OfInt)$LAYOUT.select(groupElement("code"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static final OfInt code$layout() {
        return code$LAYOUT;
    }

    private static final long code$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static final long code$offset() {
        return code$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static int code(MemorySegment struct) {
        return struct.get(code$LAYOUT, code$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int code
     * }
     */
    public static void code(MemorySegment struct, int fieldValue) {
        struct.set(code$LAYOUT, code$OFFSET, fieldValue);
    }

    private static final GroupLayout body$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("body"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_strbuf_t body
     * }
     */
    public static final GroupLayout body$layout() {
        return body$LAYOUT;
    }

    private static final long body$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_strbuf_t body
     * }
     */
    public static final long body$offset() {
        return body$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_strbuf_t body
     * }
     */
    public static MemorySegment body(MemorySegment struct) {
        return struct.asSlice(body$OFFSET, body$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_strbuf_t body
     * }
     */
    public static void body(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, body$OFFSET, body$LAYOUT.byteSize());
    }

    private static final AddressLayout status$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("status"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *status
     * }
     */
    public static final AddressLayout status$layout() {
        return status$LAYOUT;
    }

    private static final long status$OFFSET = 1056;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *status
     * }
     */
    public static final long status$offset() {
        return status$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *status
     * }
     */
    public static MemorySegment status(MemorySegment struct) {
        return struct.get(status$LAYOUT, status$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *status
     * }
     */
    public static void status(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(status$LAYOUT, status$OFFSET, fieldValue);
    }

    private static final AddressLayout content_type$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("content_type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *content_type
     * }
     */
    public static final AddressLayout content_type$layout() {
        return content_type$LAYOUT;
    }

    private static final long content_type$OFFSET = 1064;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *content_type
     * }
     */
    public static final long content_type$offset() {
        return content_type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *content_type
     * }
     */
    public static MemorySegment content_type(MemorySegment struct) {
        return struct.get(content_type$LAYOUT, content_type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *content_type
     * }
     */
    public static void content_type(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(content_type$LAYOUT, content_type$OFFSET, fieldValue);
    }

    private static final GroupLayout headers$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("headers"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ecs_strbuf_t headers
     * }
     */
    public static final GroupLayout headers$layout() {
        return headers$LAYOUT;
    }

    private static final long headers$OFFSET = 1072;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ecs_strbuf_t headers
     * }
     */
    public static final long headers$offset() {
        return headers$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ecs_strbuf_t headers
     * }
     */
    public static MemorySegment headers(MemorySegment struct) {
        return struct.asSlice(headers$OFFSET, headers$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ecs_strbuf_t headers
     * }
     */
    public static void headers(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, headers$OFFSET, headers$LAYOUT.byteSize());
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

