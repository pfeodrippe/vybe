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

public class flecs extends flecs_1 {

    flecs() {
        // Should not be called directly
    }
    private static final int ECS_STRBUF_MAX_LIST_DEPTH = (int)32L;
    /**
     * {@snippet lang=c :
     * #define ECS_STRBUF_MAX_LIST_DEPTH 32
     * }
     */
    public static int ECS_STRBUF_MAX_LIST_DEPTH() {
        return ECS_STRBUF_MAX_LIST_DEPTH;
    }
    private static final int EWOULDBLOCK = (int)35L;
    /**
     * {@snippet lang=c :
     * #define EWOULDBLOCK 35
     * }
     */
    public static int EWOULDBLOCK() {
        return EWOULDBLOCK;
    }
    private static final int EOF = (int)-1L;
    /**
     * {@snippet lang=c :
     * #define EOF -1
     * }
     */
    public static int EOF() {
        return EOF;
    }
    /**
     * {@snippet lang=c :
     * #define P_tmpdir "/var/tmp/"
     * }
     */
    public static MemorySegment P_tmpdir() {
        class Holder {
            static final MemorySegment P_tmpdir
                = flecs.LIBRARY_ARENA.allocateFrom("/var/tmp/");
        }
        return Holder.P_tmpdir;
    }
    private static final long EcsSelf = -9223372036854775808L;
    /**
     * {@snippet lang=c :
     * #define EcsSelf -9223372036854775808
     * }
     */
    public static long EcsSelf() {
        return EcsSelf;
    }
    private static final long EcsUp = 4611686018427387904L;
    /**
     * {@snippet lang=c :
     * #define EcsUp 4611686018427387904
     * }
     */
    public static long EcsUp() {
        return EcsUp;
    }
    private static final long EcsTrav = 2305843009213693952L;
    /**
     * {@snippet lang=c :
     * #define EcsTrav 2305843009213693952
     * }
     */
    public static long EcsTrav() {
        return EcsTrav;
    }
    private static final long EcsCascade = 1152921504606846976L;
    /**
     * {@snippet lang=c :
     * #define EcsCascade 1152921504606846976
     * }
     */
    public static long EcsCascade() {
        return EcsCascade;
    }
    private static final long EcsDesc = 576460752303423488L;
    /**
     * {@snippet lang=c :
     * #define EcsDesc 576460752303423488
     * }
     */
    public static long EcsDesc() {
        return EcsDesc;
    }
    private static final long EcsIsVariable = 288230376151711744L;
    /**
     * {@snippet lang=c :
     * #define EcsIsVariable 288230376151711744
     * }
     */
    public static long EcsIsVariable() {
        return EcsIsVariable;
    }
    private static final long EcsIsEntity = 144115188075855872L;
    /**
     * {@snippet lang=c :
     * #define EcsIsEntity 144115188075855872
     * }
     */
    public static long EcsIsEntity() {
        return EcsIsEntity;
    }
    private static final long EcsIsName = 72057594037927936L;
    /**
     * {@snippet lang=c :
     * #define EcsIsName 72057594037927936
     * }
     */
    public static long EcsIsName() {
        return EcsIsName;
    }
    private static final long EcsTraverseFlags = -576460752303423488L;
    /**
     * {@snippet lang=c :
     * #define EcsTraverseFlags -576460752303423488
     * }
     */
    public static long EcsTraverseFlags() {
        return EcsTraverseFlags;
    }
    private static final long EcsTermRefFlags = -72057594037927936L;
    /**
     * {@snippet lang=c :
     * #define EcsTermRefFlags -72057594037927936
     * }
     */
    public static long EcsTermRefFlags() {
        return EcsTermRefFlags;
    }
    private static final int flecs_iter_cache_ids = (int)1L;
    /**
     * {@snippet lang=c :
     * #define flecs_iter_cache_ids 1
     * }
     */
    public static int flecs_iter_cache_ids() {
        return flecs_iter_cache_ids;
    }
    private static final int flecs_iter_cache_trs = (int)2L;
    /**
     * {@snippet lang=c :
     * #define flecs_iter_cache_trs 2
     * }
     */
    public static int flecs_iter_cache_trs() {
        return flecs_iter_cache_trs;
    }
    private static final int flecs_iter_cache_sources = (int)4L;
    /**
     * {@snippet lang=c :
     * #define flecs_iter_cache_sources 4
     * }
     */
    public static int flecs_iter_cache_sources() {
        return flecs_iter_cache_sources;
    }
    private static final int flecs_iter_cache_ptrs = (int)8L;
    /**
     * {@snippet lang=c :
     * #define flecs_iter_cache_ptrs 8
     * }
     */
    public static int flecs_iter_cache_ptrs() {
        return flecs_iter_cache_ptrs;
    }
    private static final int flecs_iter_cache_variables = (int)16L;
    /**
     * {@snippet lang=c :
     * #define flecs_iter_cache_variables 16
     * }
     */
    public static int flecs_iter_cache_variables() {
        return flecs_iter_cache_variables;
    }
    private static final int flecs_iter_cache_all = (int)255L;
    /**
     * {@snippet lang=c :
     * #define flecs_iter_cache_all 255
     * }
     */
    public static int flecs_iter_cache_all() {
        return flecs_iter_cache_all;
    }
    private static final int ECS_MAX_COMPONENT_ID = (int)268435455L;
    /**
     * {@snippet lang=c :
     * #define ECS_MAX_COMPONENT_ID 268435455
     * }
     */
    public static int ECS_MAX_COMPONENT_ID() {
        return ECS_MAX_COMPONENT_ID;
    }
    private static final int ECS_MAX_RECURSION = (int)512L;
    /**
     * {@snippet lang=c :
     * #define ECS_MAX_RECURSION 512
     * }
     */
    public static int ECS_MAX_RECURSION() {
        return ECS_MAX_RECURSION;
    }
    private static final int ECS_MAX_TOKEN_SIZE = (int)256L;
    /**
     * {@snippet lang=c :
     * #define ECS_MAX_TOKEN_SIZE 256
     * }
     */
    public static int ECS_MAX_TOKEN_SIZE() {
        return ECS_MAX_TOKEN_SIZE;
    }
    private static final int EcsQueryMatchPrefab = (int)2L;
    /**
     * {@snippet lang=c :
     * #define EcsQueryMatchPrefab 2
     * }
     */
    public static int EcsQueryMatchPrefab() {
        return EcsQueryMatchPrefab;
    }
    private static final int EcsQueryMatchDisabled = (int)4L;
    /**
     * {@snippet lang=c :
     * #define EcsQueryMatchDisabled 4
     * }
     */
    public static int EcsQueryMatchDisabled() {
        return EcsQueryMatchDisabled;
    }
    private static final int EcsQueryMatchEmptyTables = (int)8L;
    /**
     * {@snippet lang=c :
     * #define EcsQueryMatchEmptyTables 8
     * }
     */
    public static int EcsQueryMatchEmptyTables() {
        return EcsQueryMatchEmptyTables;
    }
    private static final int EcsQueryAllowUnresolvedByName = (int)64L;
    /**
     * {@snippet lang=c :
     * #define EcsQueryAllowUnresolvedByName 64
     * }
     */
    public static int EcsQueryAllowUnresolvedByName() {
        return EcsQueryAllowUnresolvedByName;
    }
    private static final int EcsQueryTableOnly = (int)128L;
    /**
     * {@snippet lang=c :
     * #define EcsQueryTableOnly 128
     * }
     */
    public static int EcsQueryTableOnly() {
        return EcsQueryTableOnly;
    }
    private static final int EcsFirstUserComponentId = (int)8L;
    /**
     * {@snippet lang=c :
     * #define EcsFirstUserComponentId 8
     * }
     */
    public static int EcsFirstUserComponentId() {
        return EcsFirstUserComponentId;
    }
    private static final int EcsFirstUserEntityId = (int)384L;
    /**
     * {@snippet lang=c :
     * #define EcsFirstUserEntityId 384
     * }
     */
    public static int EcsFirstUserEntityId() {
        return EcsFirstUserEntityId;
    }
    private static final int ECS_INVALID_OPERATION = (int)1L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_OPERATION 1
     * }
     */
    public static int ECS_INVALID_OPERATION() {
        return ECS_INVALID_OPERATION;
    }
    private static final int ECS_INVALID_PARAMETER = (int)2L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_PARAMETER 2
     * }
     */
    public static int ECS_INVALID_PARAMETER() {
        return ECS_INVALID_PARAMETER;
    }
    private static final int ECS_CONSTRAINT_VIOLATED = (int)3L;
    /**
     * {@snippet lang=c :
     * #define ECS_CONSTRAINT_VIOLATED 3
     * }
     */
    public static int ECS_CONSTRAINT_VIOLATED() {
        return ECS_CONSTRAINT_VIOLATED;
    }
    private static final int ECS_OUT_OF_MEMORY = (int)4L;
    /**
     * {@snippet lang=c :
     * #define ECS_OUT_OF_MEMORY 4
     * }
     */
    public static int ECS_OUT_OF_MEMORY() {
        return ECS_OUT_OF_MEMORY;
    }
    private static final int ECS_OUT_OF_RANGE = (int)5L;
    /**
     * {@snippet lang=c :
     * #define ECS_OUT_OF_RANGE 5
     * }
     */
    public static int ECS_OUT_OF_RANGE() {
        return ECS_OUT_OF_RANGE;
    }
    private static final int ECS_UNSUPPORTED = (int)6L;
    /**
     * {@snippet lang=c :
     * #define ECS_UNSUPPORTED 6
     * }
     */
    public static int ECS_UNSUPPORTED() {
        return ECS_UNSUPPORTED;
    }
    private static final int ECS_INTERNAL_ERROR = (int)7L;
    /**
     * {@snippet lang=c :
     * #define ECS_INTERNAL_ERROR 7
     * }
     */
    public static int ECS_INTERNAL_ERROR() {
        return ECS_INTERNAL_ERROR;
    }
    private static final int ECS_ALREADY_DEFINED = (int)8L;
    /**
     * {@snippet lang=c :
     * #define ECS_ALREADY_DEFINED 8
     * }
     */
    public static int ECS_ALREADY_DEFINED() {
        return ECS_ALREADY_DEFINED;
    }
    private static final int ECS_MISSING_OS_API = (int)9L;
    /**
     * {@snippet lang=c :
     * #define ECS_MISSING_OS_API 9
     * }
     */
    public static int ECS_MISSING_OS_API() {
        return ECS_MISSING_OS_API;
    }
    private static final int ECS_OPERATION_FAILED = (int)10L;
    /**
     * {@snippet lang=c :
     * #define ECS_OPERATION_FAILED 10
     * }
     */
    public static int ECS_OPERATION_FAILED() {
        return ECS_OPERATION_FAILED;
    }
    private static final int ECS_INVALID_CONVERSION = (int)11L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_CONVERSION 11
     * }
     */
    public static int ECS_INVALID_CONVERSION() {
        return ECS_INVALID_CONVERSION;
    }
    private static final int ECS_ID_IN_USE = (int)12L;
    /**
     * {@snippet lang=c :
     * #define ECS_ID_IN_USE 12
     * }
     */
    public static int ECS_ID_IN_USE() {
        return ECS_ID_IN_USE;
    }
    private static final int ECS_CYCLE_DETECTED = (int)13L;
    /**
     * {@snippet lang=c :
     * #define ECS_CYCLE_DETECTED 13
     * }
     */
    public static int ECS_CYCLE_DETECTED() {
        return ECS_CYCLE_DETECTED;
    }
    private static final int ECS_LEAK_DETECTED = (int)14L;
    /**
     * {@snippet lang=c :
     * #define ECS_LEAK_DETECTED 14
     * }
     */
    public static int ECS_LEAK_DETECTED() {
        return ECS_LEAK_DETECTED;
    }
    private static final int ECS_DOUBLE_FREE = (int)15L;
    /**
     * {@snippet lang=c :
     * #define ECS_DOUBLE_FREE 15
     * }
     */
    public static int ECS_DOUBLE_FREE() {
        return ECS_DOUBLE_FREE;
    }
    private static final int ECS_INCONSISTENT_NAME = (int)20L;
    /**
     * {@snippet lang=c :
     * #define ECS_INCONSISTENT_NAME 20
     * }
     */
    public static int ECS_INCONSISTENT_NAME() {
        return ECS_INCONSISTENT_NAME;
    }
    private static final int ECS_NAME_IN_USE = (int)21L;
    /**
     * {@snippet lang=c :
     * #define ECS_NAME_IN_USE 21
     * }
     */
    public static int ECS_NAME_IN_USE() {
        return ECS_NAME_IN_USE;
    }
    private static final int ECS_NOT_A_COMPONENT = (int)22L;
    /**
     * {@snippet lang=c :
     * #define ECS_NOT_A_COMPONENT 22
     * }
     */
    public static int ECS_NOT_A_COMPONENT() {
        return ECS_NOT_A_COMPONENT;
    }
    private static final int ECS_INVALID_COMPONENT_SIZE = (int)23L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_COMPONENT_SIZE 23
     * }
     */
    public static int ECS_INVALID_COMPONENT_SIZE() {
        return ECS_INVALID_COMPONENT_SIZE;
    }
    private static final int ECS_INVALID_COMPONENT_ALIGNMENT = (int)24L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_COMPONENT_ALIGNMENT 24
     * }
     */
    public static int ECS_INVALID_COMPONENT_ALIGNMENT() {
        return ECS_INVALID_COMPONENT_ALIGNMENT;
    }
    private static final int ECS_COMPONENT_NOT_REGISTERED = (int)25L;
    /**
     * {@snippet lang=c :
     * #define ECS_COMPONENT_NOT_REGISTERED 25
     * }
     */
    public static int ECS_COMPONENT_NOT_REGISTERED() {
        return ECS_COMPONENT_NOT_REGISTERED;
    }
    private static final int ECS_INCONSISTENT_COMPONENT_ID = (int)26L;
    /**
     * {@snippet lang=c :
     * #define ECS_INCONSISTENT_COMPONENT_ID 26
     * }
     */
    public static int ECS_INCONSISTENT_COMPONENT_ID() {
        return ECS_INCONSISTENT_COMPONENT_ID;
    }
    private static final int ECS_INCONSISTENT_COMPONENT_ACTION = (int)27L;
    /**
     * {@snippet lang=c :
     * #define ECS_INCONSISTENT_COMPONENT_ACTION 27
     * }
     */
    public static int ECS_INCONSISTENT_COMPONENT_ACTION() {
        return ECS_INCONSISTENT_COMPONENT_ACTION;
    }
    private static final int ECS_MODULE_UNDEFINED = (int)28L;
    /**
     * {@snippet lang=c :
     * #define ECS_MODULE_UNDEFINED 28
     * }
     */
    public static int ECS_MODULE_UNDEFINED() {
        return ECS_MODULE_UNDEFINED;
    }
    private static final int ECS_MISSING_SYMBOL = (int)29L;
    /**
     * {@snippet lang=c :
     * #define ECS_MISSING_SYMBOL 29
     * }
     */
    public static int ECS_MISSING_SYMBOL() {
        return ECS_MISSING_SYMBOL;
    }
    private static final int ECS_ALREADY_IN_USE = (int)30L;
    /**
     * {@snippet lang=c :
     * #define ECS_ALREADY_IN_USE 30
     * }
     */
    public static int ECS_ALREADY_IN_USE() {
        return ECS_ALREADY_IN_USE;
    }
    private static final int ECS_ACCESS_VIOLATION = (int)40L;
    /**
     * {@snippet lang=c :
     * #define ECS_ACCESS_VIOLATION 40
     * }
     */
    public static int ECS_ACCESS_VIOLATION() {
        return ECS_ACCESS_VIOLATION;
    }
    private static final int ECS_COLUMN_INDEX_OUT_OF_RANGE = (int)41L;
    /**
     * {@snippet lang=c :
     * #define ECS_COLUMN_INDEX_OUT_OF_RANGE 41
     * }
     */
    public static int ECS_COLUMN_INDEX_OUT_OF_RANGE() {
        return ECS_COLUMN_INDEX_OUT_OF_RANGE;
    }
    private static final int ECS_COLUMN_IS_NOT_SHARED = (int)42L;
    /**
     * {@snippet lang=c :
     * #define ECS_COLUMN_IS_NOT_SHARED 42
     * }
     */
    public static int ECS_COLUMN_IS_NOT_SHARED() {
        return ECS_COLUMN_IS_NOT_SHARED;
    }
    private static final int ECS_COLUMN_IS_SHARED = (int)43L;
    /**
     * {@snippet lang=c :
     * #define ECS_COLUMN_IS_SHARED 43
     * }
     */
    public static int ECS_COLUMN_IS_SHARED() {
        return ECS_COLUMN_IS_SHARED;
    }
    private static final int ECS_COLUMN_TYPE_MISMATCH = (int)45L;
    /**
     * {@snippet lang=c :
     * #define ECS_COLUMN_TYPE_MISMATCH 45
     * }
     */
    public static int ECS_COLUMN_TYPE_MISMATCH() {
        return ECS_COLUMN_TYPE_MISMATCH;
    }
    private static final int ECS_INVALID_WHILE_READONLY = (int)70L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_WHILE_READONLY 70
     * }
     */
    public static int ECS_INVALID_WHILE_READONLY() {
        return ECS_INVALID_WHILE_READONLY;
    }
    private static final int ECS_LOCKED_STORAGE = (int)71L;
    /**
     * {@snippet lang=c :
     * #define ECS_LOCKED_STORAGE 71
     * }
     */
    public static int ECS_LOCKED_STORAGE() {
        return ECS_LOCKED_STORAGE;
    }
    private static final int ECS_INVALID_FROM_WORKER = (int)72L;
    /**
     * {@snippet lang=c :
     * #define ECS_INVALID_FROM_WORKER 72
     * }
     */
    public static int ECS_INVALID_FROM_WORKER() {
        return ECS_INVALID_FROM_WORKER;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_BLACK "[1;30m"
     * }
     */
    public static MemorySegment ECS_BLACK() {
        class Holder {
            static final MemorySegment ECS_BLACK
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[1;30m");
        }
        return Holder.ECS_BLACK;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_RED "[0;31m"
     * }
     */
    public static MemorySegment ECS_RED() {
        class Holder {
            static final MemorySegment ECS_RED
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;31m");
        }
        return Holder.ECS_RED;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_GREEN "[0;32m"
     * }
     */
    public static MemorySegment ECS_GREEN() {
        class Holder {
            static final MemorySegment ECS_GREEN
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;32m");
        }
        return Holder.ECS_GREEN;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_YELLOW "[0;33m"
     * }
     */
    public static MemorySegment ECS_YELLOW() {
        class Holder {
            static final MemorySegment ECS_YELLOW
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;33m");
        }
        return Holder.ECS_YELLOW;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_BLUE "[0;34m"
     * }
     */
    public static MemorySegment ECS_BLUE() {
        class Holder {
            static final MemorySegment ECS_BLUE
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;34m");
        }
        return Holder.ECS_BLUE;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_MAGENTA "[0;35m"
     * }
     */
    public static MemorySegment ECS_MAGENTA() {
        class Holder {
            static final MemorySegment ECS_MAGENTA
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;35m");
        }
        return Holder.ECS_MAGENTA;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_CYAN "[0;36m"
     * }
     */
    public static MemorySegment ECS_CYAN() {
        class Holder {
            static final MemorySegment ECS_CYAN
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;36m");
        }
        return Holder.ECS_CYAN;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_WHITE "[1;37m"
     * }
     */
    public static MemorySegment ECS_WHITE() {
        class Holder {
            static final MemorySegment ECS_WHITE
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[1;37m");
        }
        return Holder.ECS_WHITE;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_GREY "[0;37m"
     * }
     */
    public static MemorySegment ECS_GREY() {
        class Holder {
            static final MemorySegment ECS_GREY
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;37m");
        }
        return Holder.ECS_GREY;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_NORMAL "[0;49m"
     * }
     */
    public static MemorySegment ECS_NORMAL() {
        class Holder {
            static final MemorySegment ECS_NORMAL
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[0;49m");
        }
        return Holder.ECS_NORMAL;
    }
    /**
     * {@snippet lang=c :
     * #define ECS_BOLD "[1;49m"
     * }
     */
    public static MemorySegment ECS_BOLD() {
        class Holder {
            static final MemorySegment ECS_BOLD
                = flecs.LIBRARY_ARENA.allocateFrom("\u001b[1;49m");
        }
        return Holder.ECS_BOLD;
    }
    private static final int ECS_HTTP_HEADER_COUNT_MAX = (int)32L;
    /**
     * {@snippet lang=c :
     * #define ECS_HTTP_HEADER_COUNT_MAX 32
     * }
     */
    public static int ECS_HTTP_HEADER_COUNT_MAX() {
        return ECS_HTTP_HEADER_COUNT_MAX;
    }
    private static final int ECS_HTTP_QUERY_PARAM_COUNT_MAX = (int)32L;
    /**
     * {@snippet lang=c :
     * #define ECS_HTTP_QUERY_PARAM_COUNT_MAX 32
     * }
     */
    public static int ECS_HTTP_QUERY_PARAM_COUNT_MAX() {
        return ECS_HTTP_QUERY_PARAM_COUNT_MAX;
    }
    private static final int ECS_REST_DEFAULT_PORT = (int)27750L;
    /**
     * {@snippet lang=c :
     * #define ECS_REST_DEFAULT_PORT 27750
     * }
     */
    public static int ECS_REST_DEFAULT_PORT() {
        return ECS_REST_DEFAULT_PORT;
    }
    private static final int ECS_STAT_WINDOW = (int)60L;
    /**
     * {@snippet lang=c :
     * #define ECS_STAT_WINDOW 60
     * }
     */
    public static int ECS_STAT_WINDOW() {
        return ECS_STAT_WINDOW;
    }
    private static final int ECS_ALERT_MAX_SEVERITY_FILTERS = (int)4L;
    /**
     * {@snippet lang=c :
     * #define ECS_ALERT_MAX_SEVERITY_FILTERS 4
     * }
     */
    public static int ECS_ALERT_MAX_SEVERITY_FILTERS() {
        return ECS_ALERT_MAX_SEVERITY_FILTERS;
    }
    private static final int ECS_MEMBER_DESC_CACHE_SIZE = (int)32L;
    /**
     * {@snippet lang=c :
     * #define ECS_MEMBER_DESC_CACHE_SIZE 32
     * }
     */
    public static int ECS_MEMBER_DESC_CACHE_SIZE() {
        return ECS_MEMBER_DESC_CACHE_SIZE;
    }
    private static final int ECS_META_MAX_SCOPE_DEPTH = (int)32L;
    /**
     * {@snippet lang=c :
     * #define ECS_META_MAX_SCOPE_DEPTH 32
     * }
     */
    public static int ECS_META_MAX_SCOPE_DEPTH() {
        return ECS_META_MAX_SCOPE_DEPTH;
    }
}

