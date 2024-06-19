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
 * typedef struct __darwin_sigaltstack {
 *     void *ss_sp;
 *     __darwin_size_t ss_size;
 *     int ss_flags;
 * } stack_t
 * }
 */
public class stack_t extends __darwin_sigaltstack {

    stack_t() {
        // Should not be called directly
    }
}
