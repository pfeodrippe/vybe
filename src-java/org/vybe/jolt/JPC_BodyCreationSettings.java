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
 * struct JPC_BodyCreationSettings {
 *     JPC_Real position[4];
 *     float rotation[4];
 *     float linear_velocity[4];
 *     float angular_velocity[4];
 *     uint64_t user_data;
 *     JPC_ObjectLayer object_layer;
 *     JPC_CollisionGroup collision_group;
 *     JPC_MotionType motion_type;
 *     bool allow_dynamic_or_kinematic;
 *     bool is_sensor;
 *     bool use_manifold_reduction;
 *     JPC_MotionQuality motion_quality;
 *     bool allow_sleeping;
 *     float friction;
 *     float restitution;
 *     float linear_damping;
 *     float angular_damping;
 *     float max_linear_velocity;
 *     float max_angular_velocity;
 *     float gravity_factor;
 *     JPC_OverrideMassProperties override_mass_properties;
 *     float inertia_multiplier;
 *     JPC_MassProperties mass_properties_override;
 *     const void *reserved;
 *     const JPC_Shape *shape;
 * }
 * }
 */
public class JPC_BodyCreationSettings {

    JPC_BodyCreationSettings() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("position"),
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("rotation"),
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("linear_velocity"),
        MemoryLayout.sequenceLayout(4, jolt.C_FLOAT).withName("angular_velocity"),
        jolt.C_LONG_LONG.withName("user_data"),
        jolt.C_SHORT.withName("object_layer"),
        MemoryLayout.paddingLayout(6),
        JPC_CollisionGroup.layout().withName("collision_group"),
        jolt.C_CHAR.withName("motion_type"),
        jolt.C_BOOL.withName("allow_dynamic_or_kinematic"),
        jolt.C_BOOL.withName("is_sensor"),
        jolt.C_BOOL.withName("use_manifold_reduction"),
        jolt.C_CHAR.withName("motion_quality"),
        jolt.C_BOOL.withName("allow_sleeping"),
        MemoryLayout.paddingLayout(2),
        jolt.C_FLOAT.withName("friction"),
        jolt.C_FLOAT.withName("restitution"),
        jolt.C_FLOAT.withName("linear_damping"),
        jolt.C_FLOAT.withName("angular_damping"),
        jolt.C_FLOAT.withName("max_linear_velocity"),
        jolt.C_FLOAT.withName("max_angular_velocity"),
        jolt.C_FLOAT.withName("gravity_factor"),
        jolt.C_CHAR.withName("override_mass_properties"),
        MemoryLayout.paddingLayout(3),
        jolt.C_FLOAT.withName("inertia_multiplier"),
        MemoryLayout.paddingLayout(4),
        JPC_MassProperties.layout().withName("mass_properties_override"),
        jolt.C_POINTER.withName("reserved"),
        jolt.C_POINTER.withName("shape")
    ).withName("JPC_BodyCreationSettings");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final SequenceLayout position$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("position"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static final SequenceLayout position$layout() {
        return position$LAYOUT;
    }

    private static final long position$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static final long position$offset() {
        return position$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static MemorySegment position(MemorySegment struct) {
        return struct.asSlice(position$OFFSET, position$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static void position(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, position$OFFSET, position$LAYOUT.byteSize());
    }

    private static long[] position$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static long[] position$dimensions() {
        return position$DIMS;
    }
    private static final VarHandle position$ELEM_HANDLE = position$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static float position(MemorySegment struct, long index0) {
        return (float)position$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * JPC_Real position[4]
     * }
     */
    public static void position(MemorySegment struct, long index0, float fieldValue) {
        position$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout rotation$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("rotation"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static final SequenceLayout rotation$layout() {
        return rotation$LAYOUT;
    }

    private static final long rotation$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static final long rotation$offset() {
        return rotation$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static MemorySegment rotation(MemorySegment struct) {
        return struct.asSlice(rotation$OFFSET, rotation$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static void rotation(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, rotation$OFFSET, rotation$LAYOUT.byteSize());
    }

    private static long[] rotation$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static long[] rotation$dimensions() {
        return rotation$DIMS;
    }
    private static final VarHandle rotation$ELEM_HANDLE = rotation$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static float rotation(MemorySegment struct, long index0) {
        return (float)rotation$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float rotation[4]
     * }
     */
    public static void rotation(MemorySegment struct, long index0, float fieldValue) {
        rotation$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout linear_velocity$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("linear_velocity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static final SequenceLayout linear_velocity$layout() {
        return linear_velocity$LAYOUT;
    }

    private static final long linear_velocity$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static final long linear_velocity$offset() {
        return linear_velocity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static MemorySegment linear_velocity(MemorySegment struct) {
        return struct.asSlice(linear_velocity$OFFSET, linear_velocity$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static void linear_velocity(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, linear_velocity$OFFSET, linear_velocity$LAYOUT.byteSize());
    }

    private static long[] linear_velocity$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static long[] linear_velocity$dimensions() {
        return linear_velocity$DIMS;
    }
    private static final VarHandle linear_velocity$ELEM_HANDLE = linear_velocity$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static float linear_velocity(MemorySegment struct, long index0) {
        return (float)linear_velocity$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float linear_velocity[4]
     * }
     */
    public static void linear_velocity(MemorySegment struct, long index0, float fieldValue) {
        linear_velocity$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout angular_velocity$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("angular_velocity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static final SequenceLayout angular_velocity$layout() {
        return angular_velocity$LAYOUT;
    }

    private static final long angular_velocity$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static final long angular_velocity$offset() {
        return angular_velocity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static MemorySegment angular_velocity(MemorySegment struct) {
        return struct.asSlice(angular_velocity$OFFSET, angular_velocity$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static void angular_velocity(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, angular_velocity$OFFSET, angular_velocity$LAYOUT.byteSize());
    }

    private static long[] angular_velocity$DIMS = { 4 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static long[] angular_velocity$dimensions() {
        return angular_velocity$DIMS;
    }
    private static final VarHandle angular_velocity$ELEM_HANDLE = angular_velocity$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static float angular_velocity(MemorySegment struct, long index0) {
        return (float)angular_velocity$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * float angular_velocity[4]
     * }
     */
    public static void angular_velocity(MemorySegment struct, long index0, float fieldValue) {
        angular_velocity$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final OfLong user_data$LAYOUT = (OfLong)$LAYOUT.select(groupElement("user_data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t user_data
     * }
     */
    public static final OfLong user_data$layout() {
        return user_data$LAYOUT;
    }

    private static final long user_data$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t user_data
     * }
     */
    public static final long user_data$offset() {
        return user_data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t user_data
     * }
     */
    public static long user_data(MemorySegment struct) {
        return struct.get(user_data$LAYOUT, user_data$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t user_data
     * }
     */
    public static void user_data(MemorySegment struct, long fieldValue) {
        struct.set(user_data$LAYOUT, user_data$OFFSET, fieldValue);
    }

    private static final OfShort object_layer$LAYOUT = (OfShort)$LAYOUT.select(groupElement("object_layer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer object_layer
     * }
     */
    public static final OfShort object_layer$layout() {
        return object_layer$LAYOUT;
    }

    private static final long object_layer$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer object_layer
     * }
     */
    public static final long object_layer$offset() {
        return object_layer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer object_layer
     * }
     */
    public static short object_layer(MemorySegment struct) {
        return struct.get(object_layer$LAYOUT, object_layer$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_ObjectLayer object_layer
     * }
     */
    public static void object_layer(MemorySegment struct, short fieldValue) {
        struct.set(object_layer$LAYOUT, object_layer$OFFSET, fieldValue);
    }

    private static final GroupLayout collision_group$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("collision_group"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_CollisionGroup collision_group
     * }
     */
    public static final GroupLayout collision_group$layout() {
        return collision_group$LAYOUT;
    }

    private static final long collision_group$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_CollisionGroup collision_group
     * }
     */
    public static final long collision_group$offset() {
        return collision_group$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_CollisionGroup collision_group
     * }
     */
    public static MemorySegment collision_group(MemorySegment struct) {
        return struct.asSlice(collision_group$OFFSET, collision_group$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_CollisionGroup collision_group
     * }
     */
    public static void collision_group(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, collision_group$OFFSET, collision_group$LAYOUT.byteSize());
    }

    private static final OfByte motion_type$LAYOUT = (OfByte)$LAYOUT.select(groupElement("motion_type"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_MotionType motion_type
     * }
     */
    public static final OfByte motion_type$layout() {
        return motion_type$LAYOUT;
    }

    private static final long motion_type$OFFSET = 96;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_MotionType motion_type
     * }
     */
    public static final long motion_type$offset() {
        return motion_type$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_MotionType motion_type
     * }
     */
    public static byte motion_type(MemorySegment struct) {
        return struct.get(motion_type$LAYOUT, motion_type$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_MotionType motion_type
     * }
     */
    public static void motion_type(MemorySegment struct, byte fieldValue) {
        struct.set(motion_type$LAYOUT, motion_type$OFFSET, fieldValue);
    }

    private static final OfBoolean allow_dynamic_or_kinematic$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("allow_dynamic_or_kinematic"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool allow_dynamic_or_kinematic
     * }
     */
    public static final OfBoolean allow_dynamic_or_kinematic$layout() {
        return allow_dynamic_or_kinematic$LAYOUT;
    }

    private static final long allow_dynamic_or_kinematic$OFFSET = 97;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool allow_dynamic_or_kinematic
     * }
     */
    public static final long allow_dynamic_or_kinematic$offset() {
        return allow_dynamic_or_kinematic$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool allow_dynamic_or_kinematic
     * }
     */
    public static boolean allow_dynamic_or_kinematic(MemorySegment struct) {
        return struct.get(allow_dynamic_or_kinematic$LAYOUT, allow_dynamic_or_kinematic$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool allow_dynamic_or_kinematic
     * }
     */
    public static void allow_dynamic_or_kinematic(MemorySegment struct, boolean fieldValue) {
        struct.set(allow_dynamic_or_kinematic$LAYOUT, allow_dynamic_or_kinematic$OFFSET, fieldValue);
    }

    private static final OfBoolean is_sensor$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("is_sensor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool is_sensor
     * }
     */
    public static final OfBoolean is_sensor$layout() {
        return is_sensor$LAYOUT;
    }

    private static final long is_sensor$OFFSET = 98;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool is_sensor
     * }
     */
    public static final long is_sensor$offset() {
        return is_sensor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool is_sensor
     * }
     */
    public static boolean is_sensor(MemorySegment struct) {
        return struct.get(is_sensor$LAYOUT, is_sensor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool is_sensor
     * }
     */
    public static void is_sensor(MemorySegment struct, boolean fieldValue) {
        struct.set(is_sensor$LAYOUT, is_sensor$OFFSET, fieldValue);
    }

    private static final OfBoolean use_manifold_reduction$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("use_manifold_reduction"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool use_manifold_reduction
     * }
     */
    public static final OfBoolean use_manifold_reduction$layout() {
        return use_manifold_reduction$LAYOUT;
    }

    private static final long use_manifold_reduction$OFFSET = 99;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool use_manifold_reduction
     * }
     */
    public static final long use_manifold_reduction$offset() {
        return use_manifold_reduction$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool use_manifold_reduction
     * }
     */
    public static boolean use_manifold_reduction(MemorySegment struct) {
        return struct.get(use_manifold_reduction$LAYOUT, use_manifold_reduction$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool use_manifold_reduction
     * }
     */
    public static void use_manifold_reduction(MemorySegment struct, boolean fieldValue) {
        struct.set(use_manifold_reduction$LAYOUT, use_manifold_reduction$OFFSET, fieldValue);
    }

    private static final OfByte motion_quality$LAYOUT = (OfByte)$LAYOUT.select(groupElement("motion_quality"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_MotionQuality motion_quality
     * }
     */
    public static final OfByte motion_quality$layout() {
        return motion_quality$LAYOUT;
    }

    private static final long motion_quality$OFFSET = 100;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_MotionQuality motion_quality
     * }
     */
    public static final long motion_quality$offset() {
        return motion_quality$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_MotionQuality motion_quality
     * }
     */
    public static byte motion_quality(MemorySegment struct) {
        return struct.get(motion_quality$LAYOUT, motion_quality$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_MotionQuality motion_quality
     * }
     */
    public static void motion_quality(MemorySegment struct, byte fieldValue) {
        struct.set(motion_quality$LAYOUT, motion_quality$OFFSET, fieldValue);
    }

    private static final OfBoolean allow_sleeping$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("allow_sleeping"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool allow_sleeping
     * }
     */
    public static final OfBoolean allow_sleeping$layout() {
        return allow_sleeping$LAYOUT;
    }

    private static final long allow_sleeping$OFFSET = 101;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool allow_sleeping
     * }
     */
    public static final long allow_sleeping$offset() {
        return allow_sleeping$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool allow_sleeping
     * }
     */
    public static boolean allow_sleeping(MemorySegment struct) {
        return struct.get(allow_sleeping$LAYOUT, allow_sleeping$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool allow_sleeping
     * }
     */
    public static void allow_sleeping(MemorySegment struct, boolean fieldValue) {
        struct.set(allow_sleeping$LAYOUT, allow_sleeping$OFFSET, fieldValue);
    }

    private static final OfFloat friction$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("friction"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static final OfFloat friction$layout() {
        return friction$LAYOUT;
    }

    private static final long friction$OFFSET = 104;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static final long friction$offset() {
        return friction$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static float friction(MemorySegment struct) {
        return struct.get(friction$LAYOUT, friction$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float friction
     * }
     */
    public static void friction(MemorySegment struct, float fieldValue) {
        struct.set(friction$LAYOUT, friction$OFFSET, fieldValue);
    }

    private static final OfFloat restitution$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("restitution"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float restitution
     * }
     */
    public static final OfFloat restitution$layout() {
        return restitution$LAYOUT;
    }

    private static final long restitution$OFFSET = 108;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float restitution
     * }
     */
    public static final long restitution$offset() {
        return restitution$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float restitution
     * }
     */
    public static float restitution(MemorySegment struct) {
        return struct.get(restitution$LAYOUT, restitution$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float restitution
     * }
     */
    public static void restitution(MemorySegment struct, float fieldValue) {
        struct.set(restitution$LAYOUT, restitution$OFFSET, fieldValue);
    }

    private static final OfFloat linear_damping$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("linear_damping"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float linear_damping
     * }
     */
    public static final OfFloat linear_damping$layout() {
        return linear_damping$LAYOUT;
    }

    private static final long linear_damping$OFFSET = 112;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float linear_damping
     * }
     */
    public static final long linear_damping$offset() {
        return linear_damping$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float linear_damping
     * }
     */
    public static float linear_damping(MemorySegment struct) {
        return struct.get(linear_damping$LAYOUT, linear_damping$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float linear_damping
     * }
     */
    public static void linear_damping(MemorySegment struct, float fieldValue) {
        struct.set(linear_damping$LAYOUT, linear_damping$OFFSET, fieldValue);
    }

    private static final OfFloat angular_damping$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("angular_damping"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float angular_damping
     * }
     */
    public static final OfFloat angular_damping$layout() {
        return angular_damping$LAYOUT;
    }

    private static final long angular_damping$OFFSET = 116;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float angular_damping
     * }
     */
    public static final long angular_damping$offset() {
        return angular_damping$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float angular_damping
     * }
     */
    public static float angular_damping(MemorySegment struct) {
        return struct.get(angular_damping$LAYOUT, angular_damping$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float angular_damping
     * }
     */
    public static void angular_damping(MemorySegment struct, float fieldValue) {
        struct.set(angular_damping$LAYOUT, angular_damping$OFFSET, fieldValue);
    }

    private static final OfFloat max_linear_velocity$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("max_linear_velocity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float max_linear_velocity
     * }
     */
    public static final OfFloat max_linear_velocity$layout() {
        return max_linear_velocity$LAYOUT;
    }

    private static final long max_linear_velocity$OFFSET = 120;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float max_linear_velocity
     * }
     */
    public static final long max_linear_velocity$offset() {
        return max_linear_velocity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float max_linear_velocity
     * }
     */
    public static float max_linear_velocity(MemorySegment struct) {
        return struct.get(max_linear_velocity$LAYOUT, max_linear_velocity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float max_linear_velocity
     * }
     */
    public static void max_linear_velocity(MemorySegment struct, float fieldValue) {
        struct.set(max_linear_velocity$LAYOUT, max_linear_velocity$OFFSET, fieldValue);
    }

    private static final OfFloat max_angular_velocity$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("max_angular_velocity"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float max_angular_velocity
     * }
     */
    public static final OfFloat max_angular_velocity$layout() {
        return max_angular_velocity$LAYOUT;
    }

    private static final long max_angular_velocity$OFFSET = 124;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float max_angular_velocity
     * }
     */
    public static final long max_angular_velocity$offset() {
        return max_angular_velocity$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float max_angular_velocity
     * }
     */
    public static float max_angular_velocity(MemorySegment struct) {
        return struct.get(max_angular_velocity$LAYOUT, max_angular_velocity$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float max_angular_velocity
     * }
     */
    public static void max_angular_velocity(MemorySegment struct, float fieldValue) {
        struct.set(max_angular_velocity$LAYOUT, max_angular_velocity$OFFSET, fieldValue);
    }

    private static final OfFloat gravity_factor$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("gravity_factor"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static final OfFloat gravity_factor$layout() {
        return gravity_factor$LAYOUT;
    }

    private static final long gravity_factor$OFFSET = 128;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static final long gravity_factor$offset() {
        return gravity_factor$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static float gravity_factor(MemorySegment struct) {
        return struct.get(gravity_factor$LAYOUT, gravity_factor$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float gravity_factor
     * }
     */
    public static void gravity_factor(MemorySegment struct, float fieldValue) {
        struct.set(gravity_factor$LAYOUT, gravity_factor$OFFSET, fieldValue);
    }

    private static final OfByte override_mass_properties$LAYOUT = (OfByte)$LAYOUT.select(groupElement("override_mass_properties"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_OverrideMassProperties override_mass_properties
     * }
     */
    public static final OfByte override_mass_properties$layout() {
        return override_mass_properties$LAYOUT;
    }

    private static final long override_mass_properties$OFFSET = 132;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_OverrideMassProperties override_mass_properties
     * }
     */
    public static final long override_mass_properties$offset() {
        return override_mass_properties$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_OverrideMassProperties override_mass_properties
     * }
     */
    public static byte override_mass_properties(MemorySegment struct) {
        return struct.get(override_mass_properties$LAYOUT, override_mass_properties$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_OverrideMassProperties override_mass_properties
     * }
     */
    public static void override_mass_properties(MemorySegment struct, byte fieldValue) {
        struct.set(override_mass_properties$LAYOUT, override_mass_properties$OFFSET, fieldValue);
    }

    private static final OfFloat inertia_multiplier$LAYOUT = (OfFloat)$LAYOUT.select(groupElement("inertia_multiplier"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * float inertia_multiplier
     * }
     */
    public static final OfFloat inertia_multiplier$layout() {
        return inertia_multiplier$LAYOUT;
    }

    private static final long inertia_multiplier$OFFSET = 136;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * float inertia_multiplier
     * }
     */
    public static final long inertia_multiplier$offset() {
        return inertia_multiplier$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * float inertia_multiplier
     * }
     */
    public static float inertia_multiplier(MemorySegment struct) {
        return struct.get(inertia_multiplier$LAYOUT, inertia_multiplier$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * float inertia_multiplier
     * }
     */
    public static void inertia_multiplier(MemorySegment struct, float fieldValue) {
        struct.set(inertia_multiplier$LAYOUT, inertia_multiplier$OFFSET, fieldValue);
    }

    private static final GroupLayout mass_properties_override$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("mass_properties_override"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * JPC_MassProperties mass_properties_override
     * }
     */
    public static final GroupLayout mass_properties_override$layout() {
        return mass_properties_override$LAYOUT;
    }

    private static final long mass_properties_override$OFFSET = 144;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * JPC_MassProperties mass_properties_override
     * }
     */
    public static final long mass_properties_override$offset() {
        return mass_properties_override$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * JPC_MassProperties mass_properties_override
     * }
     */
    public static MemorySegment mass_properties_override(MemorySegment struct) {
        return struct.asSlice(mass_properties_override$OFFSET, mass_properties_override$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * JPC_MassProperties mass_properties_override
     * }
     */
    public static void mass_properties_override(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, mass_properties_override$OFFSET, mass_properties_override$LAYOUT.byteSize());
    }

    private static final AddressLayout reserved$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("reserved"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const void *reserved
     * }
     */
    public static final AddressLayout reserved$layout() {
        return reserved$LAYOUT;
    }

    private static final long reserved$OFFSET = 224;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const void *reserved
     * }
     */
    public static final long reserved$offset() {
        return reserved$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const void *reserved
     * }
     */
    public static MemorySegment reserved(MemorySegment struct) {
        return struct.get(reserved$LAYOUT, reserved$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const void *reserved
     * }
     */
    public static void reserved(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(reserved$LAYOUT, reserved$OFFSET, fieldValue);
    }

    private static final AddressLayout shape$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("shape"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const JPC_Shape *shape
     * }
     */
    public static final AddressLayout shape$layout() {
        return shape$LAYOUT;
    }

    private static final long shape$OFFSET = 232;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const JPC_Shape *shape
     * }
     */
    public static final long shape$offset() {
        return shape$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const JPC_Shape *shape
     * }
     */
    public static MemorySegment shape(MemorySegment struct) {
        return struct.get(shape$LAYOUT, shape$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const JPC_Shape *shape
     * }
     */
    public static void shape(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(shape$LAYOUT, shape$OFFSET, fieldValue);
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

