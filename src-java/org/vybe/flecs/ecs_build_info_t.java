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
 * struct ecs_build_info_t {
 *     const char *compiler;
 *     const char **addons;
 *     const char *version;
 *     int16_t version_major;
 *     int16_t version_minor;
 *     int16_t version_patch;
 *     bool debug;
 *     bool sanitize;
 *     bool perf_trace;
 * }
 * }
 */
public class ecs_build_info_t {

    ecs_build_info_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        flecs.C_POINTER.withName("compiler"),
        flecs.C_POINTER.withName("addons"),
        flecs.C_POINTER.withName("version"),
        flecs.C_SHORT.withName("version_major"),
        flecs.C_SHORT.withName("version_minor"),
        flecs.C_SHORT.withName("version_patch"),
        flecs.C_BOOL.withName("debug"),
        flecs.C_BOOL.withName("sanitize"),
        flecs.C_BOOL.withName("perf_trace"),
        MemoryLayout.paddingLayout(7)
    ).withName("ecs_build_info_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout compiler$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("compiler"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *compiler
     * }
     */
    public static final AddressLayout compiler$layout() {
        return compiler$LAYOUT;
    }

    private static final long compiler$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *compiler
     * }
     */
    public static final long compiler$offset() {
        return compiler$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *compiler
     * }
     */
    public static MemorySegment compiler(MemorySegment struct) {
        return struct.get(compiler$LAYOUT, compiler$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *compiler
     * }
     */
    public static void compiler(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(compiler$LAYOUT, compiler$OFFSET, fieldValue);
    }

    private static final AddressLayout addons$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("addons"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char **addons
     * }
     */
    public static final AddressLayout addons$layout() {
        return addons$LAYOUT;
    }

    private static final long addons$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char **addons
     * }
     */
    public static final long addons$offset() {
        return addons$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char **addons
     * }
     */
    public static MemorySegment addons(MemorySegment struct) {
        return struct.get(addons$LAYOUT, addons$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char **addons
     * }
     */
    public static void addons(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(addons$LAYOUT, addons$OFFSET, fieldValue);
    }

    private static final AddressLayout version$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("version"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *version
     * }
     */
    public static final AddressLayout version$layout() {
        return version$LAYOUT;
    }

    private static final long version$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *version
     * }
     */
    public static final long version$offset() {
        return version$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *version
     * }
     */
    public static MemorySegment version(MemorySegment struct) {
        return struct.get(version$LAYOUT, version$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *version
     * }
     */
    public static void version(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(version$LAYOUT, version$OFFSET, fieldValue);
    }

    private static final OfShort version_major$LAYOUT = (OfShort)$LAYOUT.select(groupElement("version_major"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int16_t version_major
     * }
     */
    public static final OfShort version_major$layout() {
        return version_major$LAYOUT;
    }

    private static final long version_major$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int16_t version_major
     * }
     */
    public static final long version_major$offset() {
        return version_major$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int16_t version_major
     * }
     */
    public static short version_major(MemorySegment struct) {
        return struct.get(version_major$LAYOUT, version_major$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int16_t version_major
     * }
     */
    public static void version_major(MemorySegment struct, short fieldValue) {
        struct.set(version_major$LAYOUT, version_major$OFFSET, fieldValue);
    }

    private static final OfShort version_minor$LAYOUT = (OfShort)$LAYOUT.select(groupElement("version_minor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int16_t version_minor
     * }
     */
    public static final OfShort version_minor$layout() {
        return version_minor$LAYOUT;
    }

    private static final long version_minor$OFFSET = 26;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int16_t version_minor
     * }
     */
    public static final long version_minor$offset() {
        return version_minor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int16_t version_minor
     * }
     */
    public static short version_minor(MemorySegment struct) {
        return struct.get(version_minor$LAYOUT, version_minor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int16_t version_minor
     * }
     */
    public static void version_minor(MemorySegment struct, short fieldValue) {
        struct.set(version_minor$LAYOUT, version_minor$OFFSET, fieldValue);
    }

    private static final OfShort version_patch$LAYOUT = (OfShort)$LAYOUT.select(groupElement("version_patch"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int16_t version_patch
     * }
     */
    public static final OfShort version_patch$layout() {
        return version_patch$LAYOUT;
    }

    private static final long version_patch$OFFSET = 28;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int16_t version_patch
     * }
     */
    public static final long version_patch$offset() {
        return version_patch$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int16_t version_patch
     * }
     */
    public static short version_patch(MemorySegment struct) {
        return struct.get(version_patch$LAYOUT, version_patch$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int16_t version_patch
     * }
     */
    public static void version_patch(MemorySegment struct, short fieldValue) {
        struct.set(version_patch$LAYOUT, version_patch$OFFSET, fieldValue);
    }

    private static final OfBoolean debug$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("debug"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool debug
     * }
     */
    public static final OfBoolean debug$layout() {
        return debug$LAYOUT;
    }

    private static final long debug$OFFSET = 30;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool debug
     * }
     */
    public static final long debug$offset() {
        return debug$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool debug
     * }
     */
    public static boolean debug(MemorySegment struct) {
        return struct.get(debug$LAYOUT, debug$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool debug
     * }
     */
    public static void debug(MemorySegment struct, boolean fieldValue) {
        struct.set(debug$LAYOUT, debug$OFFSET, fieldValue);
    }

    private static final OfBoolean sanitize$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("sanitize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool sanitize
     * }
     */
    public static final OfBoolean sanitize$layout() {
        return sanitize$LAYOUT;
    }

    private static final long sanitize$OFFSET = 31;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool sanitize
     * }
     */
    public static final long sanitize$offset() {
        return sanitize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool sanitize
     * }
     */
    public static boolean sanitize(MemorySegment struct) {
        return struct.get(sanitize$LAYOUT, sanitize$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool sanitize
     * }
     */
    public static void sanitize(MemorySegment struct, boolean fieldValue) {
        struct.set(sanitize$LAYOUT, sanitize$OFFSET, fieldValue);
    }

    private static final OfBoolean perf_trace$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("perf_trace"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool perf_trace
     * }
     */
    public static final OfBoolean perf_trace$layout() {
        return perf_trace$LAYOUT;
    }

    private static final long perf_trace$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool perf_trace
     * }
     */
    public static final long perf_trace$offset() {
        return perf_trace$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool perf_trace
     * }
     */
    public static boolean perf_trace(MemorySegment struct) {
        return struct.get(perf_trace$LAYOUT, perf_trace$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool perf_trace
     * }
     */
    public static void perf_trace(MemorySegment struct, boolean fieldValue) {
        struct.set(perf_trace$LAYOUT, perf_trace$OFFSET, fieldValue);
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

