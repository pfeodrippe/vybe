// Generated by jextract

package org.vybe.imgui;

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
 * struct ImGuiPlatformMonitor {
 *     ImVec2 MainPos;
 *     ImVec2 MainSize;
 *     ImVec2 WorkPos;
 *     ImVec2 WorkSize;
 *     float DpiScale;
 *     void *PlatformHandle;
 * }
 * }
 */
public class ImGuiPlatformMonitor {

    ImGuiPlatformMonitor() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        ImVec2.layout().withName("MainPos"),
        ImVec2.layout().withName("MainSize"),
        ImVec2.layout().withName("WorkPos"),
        ImVec2.layout().withName("WorkSize"),
        imgui.C_FLOAT.withName("DpiScale"),
        MemoryLayout.paddingLayout(4),
        imgui.C_POINTER.withName("PlatformHandle")
    ).withName("ImGuiPlatformMonitor");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final GroupLayout MainPos$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("MainPos"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 MainPos
     * }
     */
    public static final GroupLayout MainPos$layout() {
        return MainPos$LAYOUT;
    }

    private static final long MainPos$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 MainPos
     * }
     */
    public static final long MainPos$offset() {
        return MainPos$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 MainPos
     * }
     */
    public static MemorySegment MainPos(MemorySegment struct) {
        return struct.asSlice(MainPos$OFFSET, MainPos$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 MainPos
     * }
     */
    public static void MainPos(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, MainPos$OFFSET, MainPos$LAYOUT.byteSize());
    }

    private static final GroupLayout MainSize$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("MainSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 MainSize
     * }
     */
    public static final GroupLayout MainSize$layout() {
        return MainSize$LAYOUT;
    }

    private static final long MainSize$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 MainSize
     * }
     */
    public static final long MainSize$offset() {
        return MainSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 MainSize
     * }
     */
    public static MemorySegment MainSize(MemorySegment struct) {
        return struct.asSlice(MainSize$OFFSET, MainSize$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 MainSize
     * }
     */
    public static void MainSize(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, MainSize$OFFSET, MainSize$LAYOUT.byteSize());
    }

    private static final GroupLayout WorkPos$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("WorkPos"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static final GroupLayout WorkPos$layout() {
        return WorkPos$LAYOUT;
    }

    private static final long WorkPos$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static final long WorkPos$offset() {
        return WorkPos$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static MemorySegment WorkPos(MemorySegment struct) {
        return struct.asSlice(WorkPos$OFFSET, WorkPos$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 WorkPos
     * }
     */
    public static void WorkPos(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, WorkPos$OFFSET, WorkPos$LAYOUT.byteSize());
    }

    private static final GroupLayout WorkSize$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("WorkSize"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static final GroupLayout WorkSize$layout() {
        return WorkSize$LAYOUT;
    }

    private static final long WorkSize$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static final long WorkSize$offset() {
        return WorkSize$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static MemorySegment WorkSize(MemorySegment struct) {
        return struct.asSlice(WorkSize$OFFSET, WorkSize$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ImVec2 WorkSize
     * }
     */
    public static void WorkSize(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, WorkSize$OFFSET, WorkSize$LAYOUT.byteSize());
    }

    private static final OfFloat DpiScale$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("DpiScale"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static final OfFloat DpiScale$layout() {
        return DpiScale$LAYOUT;
    }

    private static final long DpiScale$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static final long DpiScale$offset() {
        return DpiScale$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static float DpiScale(MemorySegment struct) {
        return struct.get(DpiScale$LAYOUT, DpiScale$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float DpiScale
     * }
     */
    public static void DpiScale(MemorySegment struct, float fieldValue) {
        struct.set(DpiScale$LAYOUT, DpiScale$OFFSET, fieldValue);
    }

    private static final AddressLayout PlatformHandle$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("PlatformHandle"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static final AddressLayout PlatformHandle$layout() {
        return PlatformHandle$LAYOUT;
    }

    private static final long PlatformHandle$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static final long PlatformHandle$offset() {
        return PlatformHandle$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static MemorySegment PlatformHandle(MemorySegment struct) {
        return struct.get(PlatformHandle$LAYOUT, PlatformHandle$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *PlatformHandle
     * }
     */
    public static void PlatformHandle(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(PlatformHandle$LAYOUT, PlatformHandle$OFFSET, fieldValue);
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

