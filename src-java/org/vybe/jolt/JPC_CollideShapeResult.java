// Generated by jextract

package org.vybe.jolt;

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
 * struct JPC_CollideShapeResult {
 *     float shape1_contact_point[4];
 *     float shape2_contact_point[4];
 *     float penetration_axis[4];
 *     float penetration_depth;
 *     JPC_SubShapeID shape1_sub_shape_id;
 *     JPC_SubShapeID shape2_sub_shape_id;
 *     JPC_BodyID body2_id;
 *     struct {
 *         uint32_t num_points;
 *         float points[32][4];
 *     } shape1_face;
 *     struct {
 *         uint32_t num_points;
 *         float points[32][4];
 *     } shape2_face;
 * }
 * }
 */
public class JPC_CollideShapeResult {

    JPC_CollideShapeResult() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("shape1_contact_point"),
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("shape2_contact_point"),
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("penetration_axis"),
        jolt.C_FLOAT.withName("penetration_depth"),
        jolt.C_INT.withName("shape1_sub_shape_id"),
        jolt.C_INT.withName("shape2_sub_shape_id"),
        jolt.C_INT.withName("body2_id"),
        JPC_CollideShapeResult.shape1_face.layout().withName("shape1_face"),
        JPC_CollideShapeResult.shape2_face.layout().withName("shape2_face")
    ).withName("JPC_CollideShapeResult");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final SequenceLayout shape1_contact_point$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("shape1_contact_point"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static final SequenceLayout shape1_contact_point$layout() {
        return shape1_contact_point$LAYOUT;
    }

    private static final long shape1_contact_point$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static final long shape1_contact_point$offset() {
        return shape1_contact_point$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static MemorySegment shape1_contact_point(MemorySegment struct) {
        return struct.asSlice(shape1_contact_point$OFFSET, shape1_contact_point$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static void shape1_contact_point(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, shape1_contact_point$OFFSET, shape1_contact_point$LAYOUT.byteSize());
    }

    private static long[] shape1_contact_point$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static long[] shape1_contact_point$dimensions() {
        return shape1_contact_point$DIMS;
    }
    private static final VarHandle shape1_contact_point$ELEM_HANDLE = shape1_contact_point$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static float shape1_contact_point(MemorySegment struct, long index0) {
        return (float)shape1_contact_point$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float shape1_contact_point[4]
     * }
     */
    public static void shape1_contact_point(MemorySegment struct, long index0, float fieldValue) {
        shape1_contact_point$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout shape2_contact_point$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("shape2_contact_point"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static final SequenceLayout shape2_contact_point$layout() {
        return shape2_contact_point$LAYOUT;
    }

    private static final long shape2_contact_point$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static final long shape2_contact_point$offset() {
        return shape2_contact_point$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static MemorySegment shape2_contact_point(MemorySegment struct) {
        return struct.asSlice(shape2_contact_point$OFFSET, shape2_contact_point$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static void shape2_contact_point(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, shape2_contact_point$OFFSET, shape2_contact_point$LAYOUT.byteSize());
    }

    private static long[] shape2_contact_point$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static long[] shape2_contact_point$dimensions() {
        return shape2_contact_point$DIMS;
    }
    private static final VarHandle shape2_contact_point$ELEM_HANDLE = shape2_contact_point$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static float shape2_contact_point(MemorySegment struct, long index0) {
        return (float)shape2_contact_point$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float shape2_contact_point[4]
     * }
     */
    public static void shape2_contact_point(MemorySegment struct, long index0, float fieldValue) {
        shape2_contact_point$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout penetration_axis$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("penetration_axis"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static final SequenceLayout penetration_axis$layout() {
        return penetration_axis$LAYOUT;
    }

    private static final long penetration_axis$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static final long penetration_axis$offset() {
        return penetration_axis$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static MemorySegment penetration_axis(MemorySegment struct) {
        return struct.asSlice(penetration_axis$OFFSET, penetration_axis$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static void penetration_axis(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, penetration_axis$OFFSET, penetration_axis$LAYOUT.byteSize());
    }

    private static long[] penetration_axis$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static long[] penetration_axis$dimensions() {
        return penetration_axis$DIMS;
    }
    private static final VarHandle penetration_axis$ELEM_HANDLE = penetration_axis$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static float penetration_axis(MemorySegment struct, long index0) {
        return (float)penetration_axis$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float penetration_axis[4]
     * }
     */
    public static void penetration_axis(MemorySegment struct, long index0, float fieldValue) {
        penetration_axis$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final OfFloat penetration_depth$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("penetration_depth"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float penetration_depth
     * }
     */
    public static final OfFloat penetration_depth$layout() {
        return penetration_depth$LAYOUT;
    }

    private static final long penetration_depth$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float penetration_depth
     * }
     */
    public static final long penetration_depth$offset() {
        return penetration_depth$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float penetration_depth
     * }
     */
    public static float penetration_depth(MemorySegment struct) {
        return struct.get(penetration_depth$LAYOUT, penetration_depth$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float penetration_depth
     * }
     */
    public static void penetration_depth(MemorySegment struct, float fieldValue) {
        struct.set(penetration_depth$LAYOUT, penetration_depth$OFFSET, fieldValue);
    }

    private static final OfInt shape1_sub_shape_id$LAYOUT = (OfInt)$LAYOUT.select(groupElement("shape1_sub_shape_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape1_sub_shape_id
     * }
     */
    public static final OfInt shape1_sub_shape_id$layout() {
        return shape1_sub_shape_id$LAYOUT;
    }

    private static final long shape1_sub_shape_id$OFFSET = 52;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape1_sub_shape_id
     * }
     */
    public static final long shape1_sub_shape_id$offset() {
        return shape1_sub_shape_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape1_sub_shape_id
     * }
     */
    public static int shape1_sub_shape_id(MemorySegment struct) {
        return struct.get(shape1_sub_shape_id$LAYOUT, shape1_sub_shape_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape1_sub_shape_id
     * }
     */
    public static void shape1_sub_shape_id(MemorySegment struct, int fieldValue) {
        struct.set(shape1_sub_shape_id$LAYOUT, shape1_sub_shape_id$OFFSET, fieldValue);
    }

    private static final OfInt shape2_sub_shape_id$LAYOUT = (OfInt)$LAYOUT.select(groupElement("shape2_sub_shape_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape2_sub_shape_id
     * }
     */
    public static final OfInt shape2_sub_shape_id$layout() {
        return shape2_sub_shape_id$LAYOUT;
    }

    private static final long shape2_sub_shape_id$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape2_sub_shape_id
     * }
     */
    public static final long shape2_sub_shape_id$offset() {
        return shape2_sub_shape_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape2_sub_shape_id
     * }
     */
    public static int shape2_sub_shape_id(MemorySegment struct) {
        return struct.get(shape2_sub_shape_id$LAYOUT, shape2_sub_shape_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_SubShapeID shape2_sub_shape_id
     * }
     */
    public static void shape2_sub_shape_id(MemorySegment struct, int fieldValue) {
        struct.set(shape2_sub_shape_id$LAYOUT, shape2_sub_shape_id$OFFSET, fieldValue);
    }

    private static final OfInt body2_id$LAYOUT = (OfInt)$LAYOUT.select(groupElement("body2_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_BodyID body2_id
     * }
     */
    public static final OfInt body2_id$layout() {
        return body2_id$LAYOUT;
    }

    private static final long body2_id$OFFSET = 60;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_BodyID body2_id
     * }
     */
    public static final long body2_id$offset() {
        return body2_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_BodyID body2_id
     * }
     */
    public static int body2_id(MemorySegment struct) {
        return struct.get(body2_id$LAYOUT, body2_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_BodyID body2_id
     * }
     */
    public static void body2_id(MemorySegment struct, int fieldValue) {
        struct.set(body2_id$LAYOUT, body2_id$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * }
     * }
     */
    public static class shape1_face {

        shape1_face() {
            // Should not be called directly
        }

        private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
            jolt.C_INT.withName("num_points"),
            MemoryLayout.paddingLayout(12),
            MemoryLayout.sequenceLayout(32, MemoryLayout.sequenceLayout(4, jolt.C_FLOAT)).withName("points")
        ).withName("$anon$568:5");

        /**
         * The layout of this struct
         */
        public static final GroupLayout layout() {
            return $LAYOUT;
        }

        private static final OfInt num_points$LAYOUT = (OfInt)$LAYOUT.select(groupElement("num_points"));

        /**
         * Layout for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static final OfInt num_points$layout() {
            return num_points$LAYOUT;
        }

        private static final long num_points$OFFSET = 0;

        /**
         * Offset for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static final long num_points$offset() {
            return num_points$OFFSET;
        }

        /**
         * Getter for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static int num_points(MemorySegment struct) {
            return struct.get(num_points$LAYOUT, num_points$OFFSET);
        }

        /**
         * Setter for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static void num_points(MemorySegment struct, int fieldValue) {
            struct.set(num_points$LAYOUT, num_points$OFFSET, fieldValue);
        }

        private static final SequenceLayout points$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("points"));

        /**
         * Layout for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static final SequenceLayout points$layout() {
            return points$LAYOUT;
        }

        private static final long points$OFFSET = 16;

        /**
         * Offset for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static final long points$offset() {
            return points$OFFSET;
        }

        /**
         * Getter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static MemorySegment points(MemorySegment struct) {
            return struct.asSlice(points$OFFSET, points$LAYOUT.byteSize());
        }

        /**
         * Setter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static void points(MemorySegment struct, MemorySegment fieldValue) {
            MemorySegment.copy(fieldValue, 0L, struct, points$OFFSET, points$LAYOUT.byteSize());
        }

        private static long[] points$DIMS = { 32, 4 };

        /**
         * Dimensions for array field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static long[] points$dimensions() {
            return points$DIMS;
        }
        private static final VarHandle points$ELEM_HANDLE = points$LAYOUT.varHandle(sequenceElement(), sequenceElement());

        /**
         * Indexed getter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static float points(MemorySegment struct, long index0, long index1) {
            return (float)points$ELEM_HANDLE.get(struct, 0L, index0, index1);
        }

        /**
         * Indexed setter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static void points(MemorySegment struct, long index0, long index1, float fieldValue) {
            points$ELEM_HANDLE.set(struct, 0L, index0, index1, fieldValue);
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

    private static final GroupLayout shape1_face$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("shape1_face"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape1_face
     * }
     */
    public static final GroupLayout shape1_face$layout() {
        return shape1_face$LAYOUT;
    }

    private static final long shape1_face$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape1_face
     * }
     */
    public static final long shape1_face$offset() {
        return shape1_face$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape1_face
     * }
     */
    public static MemorySegment shape1_face(MemorySegment struct) {
        return struct.asSlice(shape1_face$OFFSET, shape1_face$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape1_face
     * }
     */
    public static void shape1_face(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, shape1_face$OFFSET, shape1_face$LAYOUT.byteSize());
    }

    /**
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * }
     * }
     */
    public static class shape2_face {

        shape2_face() {
            // Should not be called directly
        }

        private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
            jolt.C_INT.withName("num_points"),
            MemoryLayout.paddingLayout(12),
            MemoryLayout.sequenceLayout(32, MemoryLayout.sequenceLayout(4, jolt.C_FLOAT)).withName("points")
        ).withName("$anon$572:5");

        /**
         * The layout of this struct
         */
        public static final GroupLayout layout() {
            return $LAYOUT;
        }

        private static final OfInt num_points$LAYOUT = (OfInt)$LAYOUT.select(groupElement("num_points"));

        /**
         * Layout for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static final OfInt num_points$layout() {
            return num_points$LAYOUT;
        }

        private static final long num_points$OFFSET = 0;

        /**
         * Offset for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static final long num_points$offset() {
            return num_points$OFFSET;
        }

        /**
         * Getter for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static int num_points(MemorySegment struct) {
            return struct.get(num_points$LAYOUT, num_points$OFFSET);
        }

        /**
         * Setter for field:
         * {@snippet lang=c :
         * uint32_t num_points
         * }
         */
        public static void num_points(MemorySegment struct, int fieldValue) {
            struct.set(num_points$LAYOUT, num_points$OFFSET, fieldValue);
        }

        private static final SequenceLayout points$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("points"));

        /**
         * Layout for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static final SequenceLayout points$layout() {
            return points$LAYOUT;
        }

        private static final long points$OFFSET = 16;

        /**
         * Offset for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static final long points$offset() {
            return points$OFFSET;
        }

        /**
         * Getter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static MemorySegment points(MemorySegment struct) {
            return struct.asSlice(points$OFFSET, points$LAYOUT.byteSize());
        }

        /**
         * Setter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static void points(MemorySegment struct, MemorySegment fieldValue) {
            MemorySegment.copy(fieldValue, 0L, struct, points$OFFSET, points$LAYOUT.byteSize());
        }

        private static long[] points$DIMS = { 32, 4 };

        /**
         * Dimensions for array field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static long[] points$dimensions() {
            return points$DIMS;
        }
        private static final VarHandle points$ELEM_HANDLE = points$LAYOUT.varHandle(sequenceElement(), sequenceElement());

        /**
         * Indexed getter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static float points(MemorySegment struct, long index0, long index1) {
            return (float)points$ELEM_HANDLE.get(struct, 0L, index0, index1);
        }

        /**
         * Indexed setter for field:
         * {@snippet lang=c :
         * float points[32][4]
         * }
         */
        public static void points(MemorySegment struct, long index0, long index1, float fieldValue) {
            points$ELEM_HANDLE.set(struct, 0L, index0, index1, fieldValue);
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

    private static final GroupLayout shape2_face$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("shape2_face"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape2_face
     * }
     */
    public static final GroupLayout shape2_face$layout() {
        return shape2_face$LAYOUT;
    }

    private static final long shape2_face$OFFSET = 592;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape2_face
     * }
     */
    public static final long shape2_face$offset() {
        return shape2_face$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape2_face
     * }
     */
    public static MemorySegment shape2_face(MemorySegment struct) {
        return struct.asSlice(shape2_face$OFFSET, shape2_face$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * struct {
     *     uint32_t num_points;
     *     float points[32][4];
     * } shape2_face
     * }
     */
    public static void shape2_face(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, shape2_face$OFFSET, shape2_face$LAYOUT.byteSize());
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

