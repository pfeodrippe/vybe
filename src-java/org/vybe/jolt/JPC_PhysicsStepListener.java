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
 * typedef struct JPC_PhysicsStepListenerVTable {
 *     const void *__vtable_header[2];
 *     void (*OnStep)(float, JPC_PhysicsSystem *);
 * } JPC_PhysicsStepListener
 * }
 */
public class JPC_PhysicsStepListener extends JPC_PhysicsStepListenerVTable {

    JPC_PhysicsStepListener() {
        // Should not be called directly
    }
}

