pub const __builtin_bswap16 = @import("std").zig.c_builtins.__builtin_bswap16;
pub const __builtin_bswap32 = @import("std").zig.c_builtins.__builtin_bswap32;
pub const __builtin_bswap64 = @import("std").zig.c_builtins.__builtin_bswap64;
pub const __builtin_signbit = @import("std").zig.c_builtins.__builtin_signbit;
pub const __builtin_signbitf = @import("std").zig.c_builtins.__builtin_signbitf;
pub const __builtin_popcount = @import("std").zig.c_builtins.__builtin_popcount;
pub const __builtin_ctz = @import("std").zig.c_builtins.__builtin_ctz;
pub const __builtin_clz = @import("std").zig.c_builtins.__builtin_clz;
pub const __builtin_sqrt = @import("std").zig.c_builtins.__builtin_sqrt;
pub const __builtin_sqrtf = @import("std").zig.c_builtins.__builtin_sqrtf;
pub const __builtin_sin = @import("std").zig.c_builtins.__builtin_sin;
pub const __builtin_sinf = @import("std").zig.c_builtins.__builtin_sinf;
pub const __builtin_cos = @import("std").zig.c_builtins.__builtin_cos;
pub const __builtin_cosf = @import("std").zig.c_builtins.__builtin_cosf;
pub const __builtin_exp = @import("std").zig.c_builtins.__builtin_exp;
pub const __builtin_expf = @import("std").zig.c_builtins.__builtin_expf;
pub const __builtin_exp2 = @import("std").zig.c_builtins.__builtin_exp2;
pub const __builtin_exp2f = @import("std").zig.c_builtins.__builtin_exp2f;
pub const __builtin_log = @import("std").zig.c_builtins.__builtin_log;
pub const __builtin_logf = @import("std").zig.c_builtins.__builtin_logf;
pub const __builtin_log2 = @import("std").zig.c_builtins.__builtin_log2;
pub const __builtin_log2f = @import("std").zig.c_builtins.__builtin_log2f;
pub const __builtin_log10 = @import("std").zig.c_builtins.__builtin_log10;
pub const __builtin_log10f = @import("std").zig.c_builtins.__builtin_log10f;
pub const __builtin_abs = @import("std").zig.c_builtins.__builtin_abs;
pub const __builtin_labs = @import("std").zig.c_builtins.__builtin_labs;
pub const __builtin_llabs = @import("std").zig.c_builtins.__builtin_llabs;
pub const __builtin_fabs = @import("std").zig.c_builtins.__builtin_fabs;
pub const __builtin_fabsf = @import("std").zig.c_builtins.__builtin_fabsf;
pub const __builtin_floor = @import("std").zig.c_builtins.__builtin_floor;
pub const __builtin_floorf = @import("std").zig.c_builtins.__builtin_floorf;
pub const __builtin_ceil = @import("std").zig.c_builtins.__builtin_ceil;
pub const __builtin_ceilf = @import("std").zig.c_builtins.__builtin_ceilf;
pub const __builtin_trunc = @import("std").zig.c_builtins.__builtin_trunc;
pub const __builtin_truncf = @import("std").zig.c_builtins.__builtin_truncf;
pub const __builtin_round = @import("std").zig.c_builtins.__builtin_round;
pub const __builtin_roundf = @import("std").zig.c_builtins.__builtin_roundf;
pub const __builtin_strlen = @import("std").zig.c_builtins.__builtin_strlen;
pub const __builtin_strcmp = @import("std").zig.c_builtins.__builtin_strcmp;
pub const __builtin_object_size = @import("std").zig.c_builtins.__builtin_object_size;
pub const __builtin___memset_chk = @import("std").zig.c_builtins.__builtin___memset_chk;
pub const __builtin_memset = @import("std").zig.c_builtins.__builtin_memset;
pub const __builtin___memcpy_chk = @import("std").zig.c_builtins.__builtin___memcpy_chk;
pub const __builtin_memcpy = @import("std").zig.c_builtins.__builtin_memcpy;
pub const __builtin_expect = @import("std").zig.c_builtins.__builtin_expect;
pub const __builtin_nanf = @import("std").zig.c_builtins.__builtin_nanf;
pub const __builtin_huge_valf = @import("std").zig.c_builtins.__builtin_huge_valf;
pub const __builtin_inff = @import("std").zig.c_builtins.__builtin_inff;
pub const __builtin_isnan = @import("std").zig.c_builtins.__builtin_isnan;
pub const __builtin_isinf = @import("std").zig.c_builtins.__builtin_isinf;
pub const __builtin_isinf_sign = @import("std").zig.c_builtins.__builtin_isinf_sign;
pub const __has_builtin = @import("std").zig.c_builtins.__has_builtin;
pub const __builtin_assume = @import("std").zig.c_builtins.__builtin_assume;
pub const __builtin_unreachable = @import("std").zig.c_builtins.__builtin_unreachable;
pub const __builtin_constant_p = @import("std").zig.c_builtins.__builtin_constant_p;
pub const __builtin_mul_overflow = @import("std").zig.c_builtins.__builtin_mul_overflow;
pub extern fn __assert_rtn([*c]const u8, [*c]const u8, c_int, [*c]const u8) noreturn;
pub const __builtin_va_list = [*c]u8;
pub const __gnuc_va_list = __builtin_va_list;
pub const va_list = __builtin_va_list;
pub const __int8_t = i8;
pub const __uint8_t = u8;
pub const __int16_t = c_short;
pub const __uint16_t = c_ushort;
pub const __int32_t = c_int;
pub const __uint32_t = c_uint;
pub const __int64_t = c_longlong;
pub const __uint64_t = c_ulonglong;
pub const __darwin_intptr_t = c_long;
pub const __darwin_natural_t = c_uint;
pub const __darwin_ct_rune_t = c_int;
pub const __mbstate_t = extern union {
    __mbstate8: [128]u8,
    _mbstateL: c_longlong,
};
pub const __darwin_mbstate_t = __mbstate_t;
pub const __darwin_ptrdiff_t = c_long;
pub const __darwin_size_t = c_ulong;
pub const __darwin_va_list = __builtin_va_list;
pub const __darwin_wchar_t = c_int;
pub const __darwin_rune_t = __darwin_wchar_t;
pub const __darwin_wint_t = c_int;
pub const __darwin_clock_t = c_ulong;
pub const __darwin_socklen_t = __uint32_t;
pub const __darwin_ssize_t = c_long;
pub const __darwin_time_t = c_long;
pub const __darwin_blkcnt_t = __int64_t;
pub const __darwin_blksize_t = __int32_t;
pub const __darwin_dev_t = __int32_t;
pub const __darwin_fsblkcnt_t = c_uint;
pub const __darwin_fsfilcnt_t = c_uint;
pub const __darwin_gid_t = __uint32_t;
pub const __darwin_id_t = __uint32_t;
pub const __darwin_ino64_t = __uint64_t;
pub const __darwin_ino_t = __darwin_ino64_t;
pub const __darwin_mach_port_name_t = __darwin_natural_t;
pub const __darwin_mach_port_t = __darwin_mach_port_name_t;
pub const __darwin_mode_t = __uint16_t;
pub const __darwin_off_t = __int64_t;
pub const __darwin_pid_t = __int32_t;
pub const __darwin_sigset_t = __uint32_t;
pub const __darwin_suseconds_t = __int32_t;
pub const __darwin_uid_t = __uint32_t;
pub const __darwin_useconds_t = __uint32_t;
pub const __darwin_uuid_t = [16]u8;
pub const __darwin_uuid_string_t = [37]u8;
pub const struct___darwin_pthread_handler_rec = extern struct {
    __routine: ?*const fn (?*anyopaque) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque) callconv(.C) void),
    __arg: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    __next: [*c]struct___darwin_pthread_handler_rec = @import("std").mem.zeroes([*c]struct___darwin_pthread_handler_rec),
};
pub const struct__opaque_pthread_attr_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [56]u8 = @import("std").mem.zeroes([56]u8),
};
pub const struct__opaque_pthread_cond_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [40]u8 = @import("std").mem.zeroes([40]u8),
};
pub const struct__opaque_pthread_condattr_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [8]u8 = @import("std").mem.zeroes([8]u8),
};
pub const struct__opaque_pthread_mutex_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [56]u8 = @import("std").mem.zeroes([56]u8),
};
pub const struct__opaque_pthread_mutexattr_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [8]u8 = @import("std").mem.zeroes([8]u8),
};
pub const struct__opaque_pthread_once_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [8]u8 = @import("std").mem.zeroes([8]u8),
};
pub const struct__opaque_pthread_rwlock_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [192]u8 = @import("std").mem.zeroes([192]u8),
};
pub const struct__opaque_pthread_rwlockattr_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __opaque: [16]u8 = @import("std").mem.zeroes([16]u8),
};
pub const struct__opaque_pthread_t = extern struct {
    __sig: c_long = @import("std").mem.zeroes(c_long),
    __cleanup_stack: [*c]struct___darwin_pthread_handler_rec = @import("std").mem.zeroes([*c]struct___darwin_pthread_handler_rec),
    __opaque: [8176]u8 = @import("std").mem.zeroes([8176]u8),
};
pub const __darwin_pthread_attr_t = struct__opaque_pthread_attr_t;
pub const __darwin_pthread_cond_t = struct__opaque_pthread_cond_t;
pub const __darwin_pthread_condattr_t = struct__opaque_pthread_condattr_t;
pub const __darwin_pthread_key_t = c_ulong;
pub const __darwin_pthread_mutex_t = struct__opaque_pthread_mutex_t;
pub const __darwin_pthread_mutexattr_t = struct__opaque_pthread_mutexattr_t;
pub const __darwin_pthread_once_t = struct__opaque_pthread_once_t;
pub const __darwin_pthread_rwlock_t = struct__opaque_pthread_rwlock_t;
pub const __darwin_pthread_rwlockattr_t = struct__opaque_pthread_rwlockattr_t;
pub const __darwin_pthread_t = [*c]struct__opaque_pthread_t;
pub const __darwin_nl_item = c_int;
pub const __darwin_wctrans_t = c_int;
pub const __darwin_wctype_t = __uint32_t;
pub extern fn memchr(__s: ?*const anyopaque, __c: c_int, __n: c_ulong) ?*anyopaque;
pub extern fn memcmp(__s1: ?*const anyopaque, __s2: ?*const anyopaque, __n: c_ulong) c_int;
pub extern fn memcpy(__dst: ?*anyopaque, __src: ?*const anyopaque, __n: c_ulong) ?*anyopaque;
pub extern fn memmove(__dst: ?*anyopaque, __src: ?*const anyopaque, __len: c_ulong) ?*anyopaque;
pub extern fn memset(__b: ?*anyopaque, __c: c_int, __len: c_ulong) ?*anyopaque;
pub extern fn strcat(__s1: [*c]u8, __s2: [*c]const u8) [*c]u8;
pub extern fn strchr(__s: [*c]const u8, __c: c_int) [*c]u8;
pub extern fn strcmp(__s1: [*c]const u8, __s2: [*c]const u8) c_int;
pub extern fn strcoll(__s1: [*c]const u8, __s2: [*c]const u8) c_int;
pub extern fn strcpy(__dst: [*c]u8, __src: [*c]const u8) [*c]u8;
pub extern fn strcspn(__s: [*c]const u8, __charset: [*c]const u8) c_ulong;
pub extern fn strerror(__errnum: c_int) [*c]u8;
pub extern fn strlen(__s: [*c]const u8) c_ulong;
pub extern fn strncat(__s1: [*c]u8, __s2: [*c]const u8, __n: c_ulong) [*c]u8;
pub extern fn strncmp(__s1: [*c]const u8, __s2: [*c]const u8, __n: c_ulong) c_int;
pub extern fn strncpy(__dst: [*c]u8, __src: [*c]const u8, __n: c_ulong) [*c]u8;
pub extern fn strpbrk(__s: [*c]const u8, __charset: [*c]const u8) [*c]u8;
pub extern fn strrchr(__s: [*c]const u8, __c: c_int) [*c]u8;
pub extern fn strspn(__s: [*c]const u8, __charset: [*c]const u8) c_ulong;
pub extern fn strstr(__big: [*c]const u8, __little: [*c]const u8) [*c]u8;
pub extern fn strtok(__str: [*c]u8, __sep: [*c]const u8) [*c]u8;
pub extern fn strxfrm(__s1: [*c]u8, __s2: [*c]const u8, __n: c_ulong) c_ulong;
pub extern fn strtok_r(__str: [*c]u8, __sep: [*c]const u8, __lasts: [*c][*c]u8) [*c]u8;
pub extern fn strerror_r(__errnum: c_int, __strerrbuf: [*c]u8, __buflen: usize) c_int;
pub extern fn strdup(__s1: [*c]const u8) [*c]u8;
pub extern fn memccpy(__dst: ?*anyopaque, __src: ?*const anyopaque, __c: c_int, __n: c_ulong) ?*anyopaque;
pub extern fn stpcpy(__dst: [*c]u8, __src: [*c]const u8) [*c]u8;
pub extern fn stpncpy(__dst: [*c]u8, __src: [*c]const u8, __n: c_ulong) [*c]u8;
pub extern fn strndup(__s1: [*c]const u8, __n: c_ulong) [*c]u8;
pub extern fn strnlen(__s1: [*c]const u8, __n: usize) usize;
pub extern fn strsignal(__sig: c_int) [*c]u8;
pub const u_int8_t = u8;
pub const u_int16_t = c_ushort;
pub const u_int32_t = c_uint;
pub const u_int64_t = c_ulonglong;
pub const register_t = i64;
pub const user_addr_t = u_int64_t;
pub const user_size_t = u_int64_t;
pub const user_ssize_t = i64;
pub const user_long_t = i64;
pub const user_ulong_t = u_int64_t;
pub const user_time_t = i64;
pub const user_off_t = i64;
pub const syscall_arg_t = u_int64_t;
pub const rsize_t = __darwin_size_t;
pub const errno_t = c_int;
pub extern fn memset_s(__s: ?*anyopaque, __smax: rsize_t, __c: c_int, __n: rsize_t) errno_t;
pub extern fn memmem(__big: ?*const anyopaque, __big_len: usize, __little: ?*const anyopaque, __little_len: usize) ?*anyopaque;
pub extern fn memset_pattern4(__b: ?*anyopaque, __pattern4: ?*const anyopaque, __len: usize) void;
pub extern fn memset_pattern8(__b: ?*anyopaque, __pattern8: ?*const anyopaque, __len: usize) void;
pub extern fn memset_pattern16(__b: ?*anyopaque, __pattern16: ?*const anyopaque, __len: usize) void;
pub extern fn strcasestr(__big: [*c]const u8, __little: [*c]const u8) [*c]u8;
pub extern fn strnstr(__big: [*c]const u8, __little: [*c]const u8, __len: usize) [*c]u8;
pub extern fn strlcat(__dst: [*c]u8, __source: [*c]const u8, __size: c_ulong) c_ulong;
pub extern fn strlcpy(__dst: [*c]u8, __source: [*c]const u8, __size: c_ulong) c_ulong;
pub extern fn strmode(__mode: c_int, __bp: [*c]u8) void;
pub extern fn strsep(__stringp: [*c][*c]u8, __delim: [*c]const u8) [*c]u8;
pub extern fn swab(noalias ?*const anyopaque, noalias ?*anyopaque, isize) void;
pub extern fn timingsafe_bcmp(__b1: ?*const anyopaque, __b2: ?*const anyopaque, __len: usize) c_int;
pub extern fn strsignal_r(__sig: c_int, __strsignalbuf: [*c]u8, __buflen: usize) c_int;
pub extern fn bcmp(?*const anyopaque, ?*const anyopaque, c_ulong) c_int;
pub extern fn bcopy(?*const anyopaque, ?*anyopaque, c_ulong) void;
pub extern fn bzero(?*anyopaque, c_ulong) void;
pub extern fn index([*c]const u8, c_int) [*c]u8;
pub extern fn rindex([*c]const u8, c_int) [*c]u8;
pub extern fn ffs(c_int) c_int;
pub extern fn strcasecmp([*c]const u8, [*c]const u8) c_int;
pub extern fn strncasecmp([*c]const u8, [*c]const u8, c_ulong) c_int;
pub extern fn ffsl(c_long) c_int;
pub extern fn ffsll(c_longlong) c_int;
pub extern fn fls(c_int) c_int;
pub extern fn flsl(c_long) c_int;
pub extern fn flsll(c_longlong) c_int;
pub const int_least8_t = i8;
pub const int_least16_t = i16;
pub const int_least32_t = i32;
pub const int_least64_t = i64;
pub const uint_least8_t = u8;
pub const uint_least16_t = u16;
pub const uint_least32_t = u32;
pub const uint_least64_t = u64;
pub const int_fast8_t = i8;
pub const int_fast16_t = i16;
pub const int_fast32_t = i32;
pub const int_fast64_t = i64;
pub const uint_fast8_t = u8;
pub const uint_fast16_t = u16;
pub const uint_fast32_t = u32;
pub const uint_fast64_t = u64;
pub const intmax_t = c_long;
pub const uintmax_t = c_ulong;
pub const ecs_flags8_t = u8;
pub const ecs_flags16_t = u16;
pub const ecs_flags32_t = u32;
pub const ecs_flags64_t = u64;
pub const ecs_size_t = i32;
pub const struct_ecs_block_allocator_chunk_header_t = extern struct {
    next: [*c]struct_ecs_block_allocator_chunk_header_t = @import("std").mem.zeroes([*c]struct_ecs_block_allocator_chunk_header_t),
};
pub const ecs_block_allocator_chunk_header_t = struct_ecs_block_allocator_chunk_header_t;
pub const struct_ecs_block_allocator_block_t = extern struct {
    memory: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    next: [*c]struct_ecs_block_allocator_block_t = @import("std").mem.zeroes([*c]struct_ecs_block_allocator_block_t),
};
pub const ecs_block_allocator_block_t = struct_ecs_block_allocator_block_t;
pub const struct_ecs_block_allocator_t = extern struct {
    head: [*c]ecs_block_allocator_chunk_header_t = @import("std").mem.zeroes([*c]ecs_block_allocator_chunk_header_t),
    block_head: [*c]ecs_block_allocator_block_t = @import("std").mem.zeroes([*c]ecs_block_allocator_block_t),
    block_tail: [*c]ecs_block_allocator_block_t = @import("std").mem.zeroes([*c]ecs_block_allocator_block_t),
    chunk_size: i32 = @import("std").mem.zeroes(i32),
    data_size: i32 = @import("std").mem.zeroes(i32),
    chunks_per_block: i32 = @import("std").mem.zeroes(i32),
    block_size: i32 = @import("std").mem.zeroes(i32),
    alloc_count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_block_allocator_t = struct_ecs_block_allocator_t;
pub const struct_ecs_vec_t = extern struct {
    array: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    count: i32 = @import("std").mem.zeroes(i32),
    size: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_vec_t = struct_ecs_vec_t;
pub const struct_ecs_sparse_t = extern struct {
    dense: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    pages: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    count: i32 = @import("std").mem.zeroes(i32),
    max_id: u64 = @import("std").mem.zeroes(u64),
    allocator: [*c]struct_ecs_allocator_t = @import("std").mem.zeroes([*c]struct_ecs_allocator_t),
    page_allocator: [*c]struct_ecs_block_allocator_t = @import("std").mem.zeroes([*c]struct_ecs_block_allocator_t),
};
pub const struct_ecs_allocator_t = extern struct {
    chunks: ecs_block_allocator_t = @import("std").mem.zeroes(ecs_block_allocator_t),
    sizes: struct_ecs_sparse_t = @import("std").mem.zeroes(struct_ecs_sparse_t),
};
pub const ecs_allocator_t = struct_ecs_allocator_t;
pub extern fn ecs_vec_init(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) void;
pub extern fn ecs_vec_init_if(vec: [*c]ecs_vec_t, size: ecs_size_t) void;
pub extern fn ecs_vec_fini(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t) void;
pub extern fn ecs_vec_reset(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t) [*c]ecs_vec_t;
pub extern fn ecs_vec_clear(vec: [*c]ecs_vec_t) void;
pub extern fn ecs_vec_append(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t) ?*anyopaque;
pub extern fn ecs_vec_remove(vec: [*c]ecs_vec_t, size: ecs_size_t, elem: i32) void;
pub extern fn ecs_vec_remove_last(vec: [*c]ecs_vec_t) void;
pub extern fn ecs_vec_copy(allocator: [*c]struct_ecs_allocator_t, vec: [*c]const ecs_vec_t, size: ecs_size_t) ecs_vec_t;
pub extern fn ecs_vec_copy_shrink(allocator: [*c]struct_ecs_allocator_t, vec: [*c]const ecs_vec_t, size: ecs_size_t) ecs_vec_t;
pub extern fn ecs_vec_reclaim(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t) void;
pub extern fn ecs_vec_set_size(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) void;
pub extern fn ecs_vec_set_min_size(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) void;
pub extern fn ecs_vec_set_min_count(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) void;
pub extern fn ecs_vec_set_min_count_zeromem(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) void;
pub extern fn ecs_vec_set_count(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) void;
pub extern fn ecs_vec_grow(allocator: [*c]struct_ecs_allocator_t, vec: [*c]ecs_vec_t, size: ecs_size_t, elem_count: i32) ?*anyopaque;
pub extern fn ecs_vec_count(vec: [*c]const ecs_vec_t) i32;
pub extern fn ecs_vec_size(vec: [*c]const ecs_vec_t) i32;
pub extern fn ecs_vec_get(vec: [*c]const ecs_vec_t, size: ecs_size_t, index: i32) ?*anyopaque;
pub extern fn ecs_vec_first(vec: [*c]const ecs_vec_t) ?*anyopaque;
pub extern fn ecs_vec_last(vec: [*c]const ecs_vec_t, size: ecs_size_t) ?*anyopaque;
pub const ecs_sparse_t = struct_ecs_sparse_t;
pub extern fn flecs_sparse_init(result: [*c]ecs_sparse_t, allocator: [*c]struct_ecs_allocator_t, page_allocator: [*c]struct_ecs_block_allocator_t, size: ecs_size_t) void;
pub extern fn flecs_sparse_fini(sparse: [*c]ecs_sparse_t) void;
pub extern fn flecs_sparse_clear(sparse: [*c]ecs_sparse_t) void;
pub extern fn flecs_sparse_add(sparse: [*c]ecs_sparse_t, elem_size: ecs_size_t) ?*anyopaque;
pub extern fn flecs_sparse_last_id(sparse: [*c]const ecs_sparse_t) u64;
pub extern fn flecs_sparse_new_id(sparse: [*c]ecs_sparse_t) u64;
pub extern fn flecs_sparse_remove(sparse: [*c]ecs_sparse_t, elem_size: ecs_size_t, id: u64) void;
pub extern fn flecs_sparse_remove_fast(sparse: [*c]ecs_sparse_t, size: ecs_size_t, index: u64) ?*anyopaque;
pub extern fn flecs_sparse_is_alive(sparse: [*c]const ecs_sparse_t, id: u64) bool;
pub extern fn flecs_sparse_get_dense(sparse: [*c]const ecs_sparse_t, elem_size: ecs_size_t, index: i32) ?*anyopaque;
pub extern fn flecs_sparse_count(sparse: [*c]const ecs_sparse_t) i32;
pub extern fn flecs_sparse_get(sparse: [*c]const ecs_sparse_t, elem_size: ecs_size_t, id: u64) ?*anyopaque;
pub extern fn flecs_sparse_try(sparse: [*c]const ecs_sparse_t, elem_size: ecs_size_t, id: u64) ?*anyopaque;
pub extern fn flecs_sparse_get_any(sparse: [*c]const ecs_sparse_t, elem_size: ecs_size_t, id: u64) ?*anyopaque;
pub extern fn flecs_sparse_ensure(sparse: [*c]ecs_sparse_t, elem_size: ecs_size_t, id: u64) ?*anyopaque;
pub extern fn flecs_sparse_ensure_fast(sparse: [*c]ecs_sparse_t, elem_size: ecs_size_t, id: u64) ?*anyopaque;
pub extern fn flecs_sparse_ids(sparse: [*c]const ecs_sparse_t) [*c]const u64;
pub extern fn ecs_sparse_init(sparse: [*c]ecs_sparse_t, elem_size: ecs_size_t) void;
pub extern fn ecs_sparse_add(sparse: [*c]ecs_sparse_t, elem_size: ecs_size_t) ?*anyopaque;
pub extern fn ecs_sparse_last_id(sparse: [*c]const ecs_sparse_t) u64;
pub extern fn ecs_sparse_count(sparse: [*c]const ecs_sparse_t) i32;
pub extern fn ecs_sparse_get_dense(sparse: [*c]const ecs_sparse_t, elem_size: ecs_size_t, index: i32) ?*anyopaque;
pub extern fn ecs_sparse_get(sparse: [*c]const ecs_sparse_t, elem_size: ecs_size_t, id: u64) ?*anyopaque;
pub extern fn flecs_ballocator_init(ba: [*c]ecs_block_allocator_t, size: ecs_size_t) void;
pub extern fn flecs_ballocator_new(size: ecs_size_t) [*c]ecs_block_allocator_t;
pub extern fn flecs_ballocator_fini(ba: [*c]ecs_block_allocator_t) void;
pub extern fn flecs_ballocator_free(ba: [*c]ecs_block_allocator_t) void;
pub extern fn flecs_balloc(allocator: [*c]ecs_block_allocator_t) ?*anyopaque;
pub extern fn flecs_bcalloc(allocator: [*c]ecs_block_allocator_t) ?*anyopaque;
pub extern fn flecs_bfree(allocator: [*c]ecs_block_allocator_t, memory: ?*anyopaque) void;
pub extern fn flecs_bfree_w_dbg_info(allocator: [*c]ecs_block_allocator_t, memory: ?*anyopaque, type_name: [*c]const u8) void;
pub extern fn flecs_brealloc(dst: [*c]ecs_block_allocator_t, src: [*c]ecs_block_allocator_t, memory: ?*anyopaque) ?*anyopaque;
pub extern fn flecs_bdup(ba: [*c]ecs_block_allocator_t, memory: ?*anyopaque) ?*anyopaque;
pub const struct_ecs_stack_page_t = extern struct {
    data: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    next: [*c]struct_ecs_stack_page_t = @import("std").mem.zeroes([*c]struct_ecs_stack_page_t),
    sp: i16 = @import("std").mem.zeroes(i16),
    id: u32 = @import("std").mem.zeroes(u32),
};
pub const ecs_stack_page_t = struct_ecs_stack_page_t;
pub const ecs_stack_cursor_t = struct_ecs_stack_cursor_t;
pub const struct_ecs_stack_t = extern struct {
    first: ecs_stack_page_t = @import("std").mem.zeroes(ecs_stack_page_t),
    tail_page: [*c]ecs_stack_page_t = @import("std").mem.zeroes([*c]ecs_stack_page_t),
    tail_cursor: [*c]ecs_stack_cursor_t = @import("std").mem.zeroes([*c]ecs_stack_cursor_t),
    cursor_count: i32 = @import("std").mem.zeroes(i32),
};
pub const struct_ecs_stack_cursor_t = extern struct {
    prev: [*c]struct_ecs_stack_cursor_t = @import("std").mem.zeroes([*c]struct_ecs_stack_cursor_t),
    page: [*c]struct_ecs_stack_page_t = @import("std").mem.zeroes([*c]struct_ecs_stack_page_t),
    sp: i16 = @import("std").mem.zeroes(i16),
    is_free: bool = @import("std").mem.zeroes(bool),
    owner: [*c]struct_ecs_stack_t = @import("std").mem.zeroes([*c]struct_ecs_stack_t),
};
pub const ecs_stack_t = struct_ecs_stack_t;
pub extern fn flecs_stack_init(stack: [*c]ecs_stack_t) void;
pub extern fn flecs_stack_fini(stack: [*c]ecs_stack_t) void;
pub extern fn flecs_stack_alloc(stack: [*c]ecs_stack_t, size: ecs_size_t, @"align": ecs_size_t) ?*anyopaque;
pub extern fn flecs_stack_calloc(stack: [*c]ecs_stack_t, size: ecs_size_t, @"align": ecs_size_t) ?*anyopaque;
pub extern fn flecs_stack_free(ptr: ?*anyopaque, size: ecs_size_t) void;
pub extern fn flecs_stack_reset(stack: [*c]ecs_stack_t) void;
pub extern fn flecs_stack_get_cursor(stack: [*c]ecs_stack_t) [*c]ecs_stack_cursor_t;
pub extern fn flecs_stack_restore_cursor(stack: [*c]ecs_stack_t, cursor: [*c]ecs_stack_cursor_t) void;
pub const ecs_map_data_t = u64;
pub const ecs_map_key_t = ecs_map_data_t;
pub const ecs_map_val_t = ecs_map_data_t;
pub const struct_ecs_bucket_entry_t = extern struct {
    key: ecs_map_key_t = @import("std").mem.zeroes(ecs_map_key_t),
    value: ecs_map_val_t = @import("std").mem.zeroes(ecs_map_val_t),
    next: [*c]struct_ecs_bucket_entry_t = @import("std").mem.zeroes([*c]struct_ecs_bucket_entry_t),
};
pub const ecs_bucket_entry_t = struct_ecs_bucket_entry_t;
pub const struct_ecs_bucket_t = extern struct {
    first: [*c]ecs_bucket_entry_t = @import("std").mem.zeroes([*c]ecs_bucket_entry_t),
};
pub const ecs_bucket_t = struct_ecs_bucket_t;
pub const struct_ecs_map_t = extern struct {
    bucket_shift: u8 = @import("std").mem.zeroes(u8),
    shared_allocator: bool = @import("std").mem.zeroes(bool),
    buckets: [*c]ecs_bucket_t = @import("std").mem.zeroes([*c]ecs_bucket_t),
    bucket_count: i32 = @import("std").mem.zeroes(i32),
    count: i32 = @import("std").mem.zeroes(i32),
    entry_allocator: [*c]struct_ecs_block_allocator_t = @import("std").mem.zeroes([*c]struct_ecs_block_allocator_t),
    allocator: [*c]struct_ecs_allocator_t = @import("std").mem.zeroes([*c]struct_ecs_allocator_t),
};
pub const ecs_map_t = struct_ecs_map_t;
pub const struct_ecs_map_iter_t = extern struct {
    map: [*c]const ecs_map_t = @import("std").mem.zeroes([*c]const ecs_map_t),
    bucket: [*c]ecs_bucket_t = @import("std").mem.zeroes([*c]ecs_bucket_t),
    entry: [*c]ecs_bucket_entry_t = @import("std").mem.zeroes([*c]ecs_bucket_entry_t),
    res: [*c]ecs_map_data_t = @import("std").mem.zeroes([*c]ecs_map_data_t),
};
pub const ecs_map_iter_t = struct_ecs_map_iter_t;
pub const struct_ecs_map_params_t = extern struct {
    allocator: [*c]struct_ecs_allocator_t = @import("std").mem.zeroes([*c]struct_ecs_allocator_t),
    entry_allocator: struct_ecs_block_allocator_t = @import("std").mem.zeroes(struct_ecs_block_allocator_t),
};
pub const ecs_map_params_t = struct_ecs_map_params_t;
pub extern fn ecs_map_params_init(params: [*c]ecs_map_params_t, allocator: [*c]struct_ecs_allocator_t) void;
pub extern fn ecs_map_params_fini(params: [*c]ecs_map_params_t) void;
pub extern fn ecs_map_init(map: [*c]ecs_map_t, allocator: [*c]struct_ecs_allocator_t) void;
pub extern fn ecs_map_init_w_params(map: [*c]ecs_map_t, params: [*c]ecs_map_params_t) void;
pub extern fn ecs_map_init_if(map: [*c]ecs_map_t, allocator: [*c]struct_ecs_allocator_t) void;
pub extern fn ecs_map_init_w_params_if(result: [*c]ecs_map_t, params: [*c]ecs_map_params_t) void;
pub extern fn ecs_map_fini(map: [*c]ecs_map_t) void;
pub extern fn ecs_map_get(map: [*c]const ecs_map_t, key: ecs_map_key_t) [*c]ecs_map_val_t;
pub extern fn ecs_map_get_deref_(map: [*c]const ecs_map_t, key: ecs_map_key_t) ?*anyopaque;
pub extern fn ecs_map_ensure(map: [*c]ecs_map_t, key: ecs_map_key_t) [*c]ecs_map_val_t;
pub extern fn ecs_map_ensure_alloc(map: [*c]ecs_map_t, elem_size: ecs_size_t, key: ecs_map_key_t) ?*anyopaque;
pub extern fn ecs_map_insert(map: [*c]ecs_map_t, key: ecs_map_key_t, value: ecs_map_val_t) void;
pub extern fn ecs_map_insert_alloc(map: [*c]ecs_map_t, elem_size: ecs_size_t, key: ecs_map_key_t) ?*anyopaque;
pub extern fn ecs_map_remove(map: [*c]ecs_map_t, key: ecs_map_key_t) ecs_map_val_t;
pub extern fn ecs_map_remove_free(map: [*c]ecs_map_t, key: ecs_map_key_t) void;
pub extern fn ecs_map_clear(map: [*c]ecs_map_t) void;
pub extern fn ecs_map_iter(map: [*c]const ecs_map_t) ecs_map_iter_t;
pub extern fn ecs_map_next(iter: [*c]ecs_map_iter_t) bool;
pub extern fn ecs_map_copy(dst: [*c]ecs_map_t, src: [*c]const ecs_map_t) void;
pub const struct_ecs_switch_node_t = extern struct {
    next: u32 = @import("std").mem.zeroes(u32),
    prev: u32 = @import("std").mem.zeroes(u32),
};
pub const ecs_switch_node_t = struct_ecs_switch_node_t;
pub const struct_ecs_switch_page_t = extern struct {
    nodes: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    values: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
};
pub const ecs_switch_page_t = struct_ecs_switch_page_t;
pub const struct_ecs_switch_t = extern struct {
    hdrs: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
    pages: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
};
pub const ecs_switch_t = struct_ecs_switch_t;
pub extern fn flecs_switch_init(sw: [*c]ecs_switch_t, allocator: [*c]ecs_allocator_t) void;
pub extern fn flecs_switch_fini(sw: [*c]ecs_switch_t) void;
pub extern fn flecs_switch_set(sw: [*c]ecs_switch_t, element: u32, value: u64) bool;
pub extern fn flecs_switch_reset(sw: [*c]ecs_switch_t, element: u32) bool;
pub extern fn flecs_switch_get(sw: [*c]const ecs_switch_t, element: u32) u64;
pub extern fn flecs_switch_first(sw: [*c]const ecs_switch_t, value: u64) u32;
pub extern fn flecs_switch_next(sw: [*c]const ecs_switch_t, previous: u32) u32;
pub extern fn flecs_switch_targets(sw: [*c]const ecs_switch_t) ecs_map_iter_t;
pub extern var ecs_block_allocator_alloc_count: i64;
pub extern var ecs_block_allocator_free_count: i64;
pub extern var ecs_stack_allocator_alloc_count: i64;
pub extern var ecs_stack_allocator_free_count: i64;
pub extern fn flecs_allocator_init(a: [*c]ecs_allocator_t) void;
pub extern fn flecs_allocator_fini(a: [*c]ecs_allocator_t) void;
pub extern fn flecs_allocator_get(a: [*c]ecs_allocator_t, size: ecs_size_t) [*c]ecs_block_allocator_t;
pub extern fn flecs_strdup(a: [*c]ecs_allocator_t, str: [*c]const u8) [*c]u8;
pub extern fn flecs_strfree(a: [*c]ecs_allocator_t, str: [*c]u8) void;
pub extern fn flecs_dup(a: [*c]ecs_allocator_t, size: ecs_size_t, src: ?*const anyopaque) ?*anyopaque;
pub const struct_ecs_strbuf_list_elem = extern struct {
    count: i32 = @import("std").mem.zeroes(i32),
    separator: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_strbuf_list_elem = struct_ecs_strbuf_list_elem;
pub const struct_ecs_strbuf_t = extern struct {
    content: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    length: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    list_stack: [32]ecs_strbuf_list_elem = @import("std").mem.zeroes([32]ecs_strbuf_list_elem),
    list_sp: i32 = @import("std").mem.zeroes(i32),
    small_string: [512]u8 = @import("std").mem.zeroes([512]u8),
};
pub const ecs_strbuf_t = struct_ecs_strbuf_t;
pub extern fn ecs_strbuf_append(buffer: [*c]ecs_strbuf_t, fmt: [*c]const u8, ...) void;
pub extern fn ecs_strbuf_vappend(buffer: [*c]ecs_strbuf_t, fmt: [*c]const u8, args: va_list) void;
pub extern fn ecs_strbuf_appendstr(buffer: [*c]ecs_strbuf_t, str: [*c]const u8) void;
pub extern fn ecs_strbuf_appendch(buffer: [*c]ecs_strbuf_t, ch: u8) void;
pub extern fn ecs_strbuf_appendint(buffer: [*c]ecs_strbuf_t, v: i64) void;
pub extern fn ecs_strbuf_appendflt(buffer: [*c]ecs_strbuf_t, v: f64, nan_delim: u8) void;
pub extern fn ecs_strbuf_appendbool(buffer: [*c]ecs_strbuf_t, v: bool) void;
pub extern fn ecs_strbuf_mergebuff(dst_buffer: [*c]ecs_strbuf_t, src_buffer: [*c]ecs_strbuf_t) void;
pub extern fn ecs_strbuf_appendstrn(buffer: [*c]ecs_strbuf_t, str: [*c]const u8, n: i32) void;
pub extern fn ecs_strbuf_get(buffer: [*c]ecs_strbuf_t) [*c]u8;
pub extern fn ecs_strbuf_get_small(buffer: [*c]ecs_strbuf_t) [*c]u8;
pub extern fn ecs_strbuf_reset(buffer: [*c]ecs_strbuf_t) void;
pub extern fn ecs_strbuf_list_push(buffer: [*c]ecs_strbuf_t, list_open: [*c]const u8, separator: [*c]const u8) void;
pub extern fn ecs_strbuf_list_pop(buffer: [*c]ecs_strbuf_t, list_close: [*c]const u8) void;
pub extern fn ecs_strbuf_list_next(buffer: [*c]ecs_strbuf_t) void;
pub extern fn ecs_strbuf_list_appendch(buffer: [*c]ecs_strbuf_t, ch: u8) void;
pub extern fn ecs_strbuf_list_append(buffer: [*c]ecs_strbuf_t, fmt: [*c]const u8, ...) void;
pub extern fn ecs_strbuf_list_appendstr(buffer: [*c]ecs_strbuf_t, str: [*c]const u8) void;
pub extern fn ecs_strbuf_list_appendstrn(buffer: [*c]ecs_strbuf_t, str: [*c]const u8, n: i32) void;
pub extern fn ecs_strbuf_written(buffer: [*c]const ecs_strbuf_t) i32;
pub extern fn __error() [*c]c_int;
pub extern fn renameat(c_int, [*c]const u8, c_int, [*c]const u8) c_int;
pub extern fn renamex_np([*c]const u8, [*c]const u8, c_uint) c_int;
pub extern fn renameatx_np(c_int, [*c]const u8, c_int, [*c]const u8, c_uint) c_int;
pub const fpos_t = __darwin_off_t;
pub const struct___sbuf = extern struct {
    _base: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    _size: c_int = @import("std").mem.zeroes(c_int),
};
pub const struct___sFILEX = opaque {};
pub const struct___sFILE = extern struct {
    _p: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    _r: c_int = @import("std").mem.zeroes(c_int),
    _w: c_int = @import("std").mem.zeroes(c_int),
    _flags: c_short = @import("std").mem.zeroes(c_short),
    _file: c_short = @import("std").mem.zeroes(c_short),
    _bf: struct___sbuf = @import("std").mem.zeroes(struct___sbuf),
    _lbfsize: c_int = @import("std").mem.zeroes(c_int),
    _cookie: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    _close: ?*const fn (?*anyopaque) callconv(.C) c_int = @import("std").mem.zeroes(?*const fn (?*anyopaque) callconv(.C) c_int),
    _read: ?*const fn (?*anyopaque, [*c]u8, c_int) callconv(.C) c_int = @import("std").mem.zeroes(?*const fn (?*anyopaque, [*c]u8, c_int) callconv(.C) c_int),
    _seek: ?*const fn (?*anyopaque, fpos_t, c_int) callconv(.C) fpos_t = @import("std").mem.zeroes(?*const fn (?*anyopaque, fpos_t, c_int) callconv(.C) fpos_t),
    _write: ?*const fn (?*anyopaque, [*c]const u8, c_int) callconv(.C) c_int = @import("std").mem.zeroes(?*const fn (?*anyopaque, [*c]const u8, c_int) callconv(.C) c_int),
    _ub: struct___sbuf = @import("std").mem.zeroes(struct___sbuf),
    _extra: ?*struct___sFILEX = @import("std").mem.zeroes(?*struct___sFILEX),
    _ur: c_int = @import("std").mem.zeroes(c_int),
    _ubuf: [3]u8 = @import("std").mem.zeroes([3]u8),
    _nbuf: [1]u8 = @import("std").mem.zeroes([1]u8),
    _lb: struct___sbuf = @import("std").mem.zeroes(struct___sbuf),
    _blksize: c_int = @import("std").mem.zeroes(c_int),
    _offset: fpos_t = @import("std").mem.zeroes(fpos_t),
};
pub const FILE = struct___sFILE;
pub extern var __stdinp: [*c]FILE;
pub extern var __stdoutp: [*c]FILE;
pub extern var __stderrp: [*c]FILE;
pub extern fn clearerr([*c]FILE) void;
pub extern fn fclose([*c]FILE) c_int;
pub extern fn feof([*c]FILE) c_int;
pub extern fn ferror([*c]FILE) c_int;
pub extern fn fflush([*c]FILE) c_int;
pub extern fn fgetc([*c]FILE) c_int;
pub extern fn fgetpos(noalias [*c]FILE, [*c]fpos_t) c_int;
pub extern fn fgets(noalias [*c]u8, c_int, [*c]FILE) [*c]u8;
pub extern fn fopen(__filename: [*c]const u8, __mode: [*c]const u8) [*c]FILE;
pub extern fn fprintf([*c]FILE, [*c]const u8, ...) c_int;
pub extern fn fputc(c_int, [*c]FILE) c_int;
pub extern fn fputs(noalias [*c]const u8, noalias [*c]FILE) c_int;
pub extern fn fread(__ptr: ?*anyopaque, __size: c_ulong, __nitems: c_ulong, __stream: [*c]FILE) c_ulong;
pub extern fn freopen(noalias [*c]const u8, noalias [*c]const u8, noalias [*c]FILE) [*c]FILE;
pub extern fn fscanf(noalias [*c]FILE, noalias [*c]const u8, ...) c_int;
pub extern fn fseek([*c]FILE, c_long, c_int) c_int;
pub extern fn fsetpos([*c]FILE, [*c]const fpos_t) c_int;
pub extern fn ftell([*c]FILE) c_long;
pub extern fn fwrite(__ptr: ?*const anyopaque, __size: c_ulong, __nitems: c_ulong, __stream: [*c]FILE) c_ulong;
pub extern fn getc([*c]FILE) c_int;
pub extern fn getchar() c_int;
pub extern fn gets([*c]u8) [*c]u8;
pub extern fn perror([*c]const u8) void;
pub extern fn printf([*c]const u8, ...) c_int;
pub extern fn putc(c_int, [*c]FILE) c_int;
pub extern fn putchar(c_int) c_int;
pub extern fn puts([*c]const u8) c_int;
pub extern fn remove([*c]const u8) c_int;
pub extern fn rename(__old: [*c]const u8, __new: [*c]const u8) c_int;
pub extern fn rewind([*c]FILE) void;
pub extern fn scanf(noalias [*c]const u8, ...) c_int;
pub extern fn setbuf(noalias [*c]FILE, noalias [*c]u8) void;
pub extern fn setvbuf(noalias [*c]FILE, noalias [*c]u8, c_int, usize) c_int;
pub extern fn sprintf([*c]u8, [*c]const u8, ...) c_int;
pub extern fn sscanf(noalias [*c]const u8, noalias [*c]const u8, ...) c_int;
pub extern fn tmpfile() [*c]FILE;
pub extern fn tmpnam([*c]u8) [*c]u8;
pub extern fn ungetc(c_int, [*c]FILE) c_int;
pub extern fn vfprintf([*c]FILE, [*c]const u8, __builtin_va_list) c_int;
pub extern fn vprintf([*c]const u8, __builtin_va_list) c_int;
pub extern fn vsprintf([*c]u8, [*c]const u8, __builtin_va_list) c_int;
pub extern fn ctermid([*c]u8) [*c]u8;
pub extern fn fdopen(c_int, [*c]const u8) [*c]FILE;
pub extern fn fileno([*c]FILE) c_int;
pub extern fn pclose([*c]FILE) c_int;
pub extern fn popen([*c]const u8, [*c]const u8) [*c]FILE;
pub extern fn __srget([*c]FILE) c_int;
pub extern fn __svfscanf([*c]FILE, [*c]const u8, va_list) c_int;
pub extern fn __swbuf(c_int, [*c]FILE) c_int;
pub inline fn __sputc(arg__c: c_int, arg__p: [*c]FILE) c_int {
    var _c = arg__c;
    _ = &_c;
    var _p = arg__p;
    _ = &_p;
    if (((blk: {
        const ref = &_p.*._w;
        ref.* -= 1;
        break :blk ref.*;
    }) >= @as(c_int, 0)) or ((_p.*._w >= _p.*._lbfsize) and (@as(c_int, @bitCast(@as(c_uint, @as(u8, @bitCast(@as(i8, @truncate(_c))))))) != @as(c_int, '\n')))) return @as(c_int, @bitCast(@as(c_uint, blk: {
        const tmp = @as(u8, @bitCast(@as(i8, @truncate(_c))));
        (blk_1: {
            const ref = &_p.*._p;
            const tmp_2 = ref.*;
            ref.* += 1;
            break :blk_1 tmp_2;
        }).* = tmp;
        break :blk tmp;
    }))) else return __swbuf(_c, _p);
    return 0;
}
pub extern fn flockfile([*c]FILE) void;
pub extern fn ftrylockfile([*c]FILE) c_int;
pub extern fn funlockfile([*c]FILE) void;
pub extern fn getc_unlocked([*c]FILE) c_int;
pub extern fn getchar_unlocked() c_int;
pub extern fn putc_unlocked(c_int, [*c]FILE) c_int;
pub extern fn putchar_unlocked(c_int) c_int;
pub extern fn getw([*c]FILE) c_int;
pub extern fn putw(c_int, [*c]FILE) c_int;
pub extern fn tempnam(__dir: [*c]const u8, __prefix: [*c]const u8) [*c]u8;
pub const off_t = __darwin_off_t;
pub extern fn fseeko(__stream: [*c]FILE, __offset: off_t, __whence: c_int) c_int;
pub extern fn ftello(__stream: [*c]FILE) off_t;
pub extern fn snprintf(__str: [*c]u8, __size: c_ulong, __format: [*c]const u8, ...) c_int;
pub extern fn vfscanf(noalias __stream: [*c]FILE, noalias __format: [*c]const u8, __builtin_va_list) c_int;
pub extern fn vscanf(noalias __format: [*c]const u8, __builtin_va_list) c_int;
pub extern fn vsnprintf(__str: [*c]u8, __size: c_ulong, __format: [*c]const u8, __builtin_va_list) c_int;
pub extern fn vsscanf(noalias __str: [*c]const u8, noalias __format: [*c]const u8, __builtin_va_list) c_int;
pub extern fn dprintf(c_int, noalias [*c]const u8, ...) c_int;
pub extern fn vdprintf(c_int, noalias [*c]const u8, va_list) c_int;
pub extern fn getdelim(noalias __linep: [*c][*c]u8, noalias __linecapp: [*c]usize, __delimiter: c_int, noalias __stream: [*c]FILE) isize;
pub extern fn getline(noalias __linep: [*c][*c]u8, noalias __linecapp: [*c]usize, noalias __stream: [*c]FILE) isize;
pub extern fn fmemopen(noalias __buf: ?*anyopaque, __size: usize, noalias __mode: [*c]const u8) [*c]FILE;
pub extern fn open_memstream(__bufp: [*c][*c]u8, __sizep: [*c]usize) [*c]FILE;
pub extern const sys_nerr: c_int;
pub const sys_errlist: [*c]const [*c]const u8 = @extern([*c]const [*c]const u8, .{
    .name = "sys_errlist",
});
pub extern fn asprintf(noalias [*c][*c]u8, noalias [*c]const u8, ...) c_int;
pub extern fn ctermid_r([*c]u8) [*c]u8;
pub extern fn fgetln([*c]FILE, [*c]usize) [*c]u8;
pub extern fn fmtcheck([*c]const u8, [*c]const u8) [*c]const u8;
pub extern fn fpurge([*c]FILE) c_int;
pub extern fn setbuffer([*c]FILE, [*c]u8, c_int) void;
pub extern fn setlinebuf([*c]FILE) c_int;
pub extern fn vasprintf(noalias [*c][*c]u8, noalias [*c]const u8, va_list) c_int;
pub extern fn funopen(?*const anyopaque, ?*const fn (?*anyopaque, [*c]u8, c_int) callconv(.C) c_int, ?*const fn (?*anyopaque, [*c]const u8, c_int) callconv(.C) c_int, ?*const fn (?*anyopaque, fpos_t, c_int) callconv(.C) fpos_t, ?*const fn (?*anyopaque) callconv(.C) c_int) [*c]FILE;
pub extern fn __sprintf_chk(noalias [*c]u8, c_int, usize, noalias [*c]const u8, ...) c_int;
pub extern fn __snprintf_chk(noalias [*c]u8, usize, c_int, usize, noalias [*c]const u8, ...) c_int;
pub extern fn __vsprintf_chk(noalias [*c]u8, c_int, usize, noalias [*c]const u8, va_list) c_int;
pub extern fn __vsnprintf_chk(noalias [*c]u8, usize, c_int, usize, noalias [*c]const u8, va_list) c_int;
pub extern fn alloca(c_ulong) ?*anyopaque;
pub const struct_ecs_time_t = extern struct {
    sec: u32 = @import("std").mem.zeroes(u32),
    nanosec: u32 = @import("std").mem.zeroes(u32),
};
pub const ecs_time_t = struct_ecs_time_t;
pub extern var ecs_os_api_malloc_count: i64;
pub extern var ecs_os_api_realloc_count: i64;
pub extern var ecs_os_api_calloc_count: i64;
pub extern var ecs_os_api_free_count: i64;
pub const ecs_os_thread_t = usize;
pub const ecs_os_cond_t = usize;
pub const ecs_os_mutex_t = usize;
pub const ecs_os_dl_t = usize;
pub const ecs_os_sock_t = usize;
pub const ecs_os_thread_id_t = u64;
pub const ecs_os_proc_t = ?*const fn () callconv(.C) void;
pub const ecs_os_api_init_t = ?*const fn () callconv(.C) void;
pub const ecs_os_api_fini_t = ?*const fn () callconv(.C) void;
pub const ecs_os_api_malloc_t = ?*const fn (ecs_size_t) callconv(.C) ?*anyopaque;
pub const ecs_os_api_free_t = ?*const fn (?*anyopaque) callconv(.C) void;
pub const ecs_os_api_realloc_t = ?*const fn (?*anyopaque, ecs_size_t) callconv(.C) ?*anyopaque;
pub const ecs_os_api_calloc_t = ?*const fn (ecs_size_t) callconv(.C) ?*anyopaque;
pub const ecs_os_api_strdup_t = ?*const fn ([*c]const u8) callconv(.C) [*c]u8;
pub const ecs_os_thread_callback_t = ?*const fn (?*anyopaque) callconv(.C) ?*anyopaque;
pub const ecs_os_api_thread_new_t = ?*const fn (ecs_os_thread_callback_t, ?*anyopaque) callconv(.C) ecs_os_thread_t;
pub const ecs_os_api_thread_join_t = ?*const fn (ecs_os_thread_t) callconv(.C) ?*anyopaque;
pub const ecs_os_api_thread_self_t = ?*const fn () callconv(.C) ecs_os_thread_id_t;
pub const ecs_os_api_task_new_t = ?*const fn (ecs_os_thread_callback_t, ?*anyopaque) callconv(.C) ecs_os_thread_t;
pub const ecs_os_api_task_join_t = ?*const fn (ecs_os_thread_t) callconv(.C) ?*anyopaque;
pub const ecs_os_api_ainc_t = ?*const fn ([*c]i32) callconv(.C) i32;
pub const ecs_os_api_lainc_t = ?*const fn ([*c]i64) callconv(.C) i64;
pub const ecs_os_api_mutex_new_t = ?*const fn () callconv(.C) ecs_os_mutex_t;
pub const ecs_os_api_mutex_lock_t = ?*const fn (ecs_os_mutex_t) callconv(.C) void;
pub const ecs_os_api_mutex_unlock_t = ?*const fn (ecs_os_mutex_t) callconv(.C) void;
pub const ecs_os_api_mutex_free_t = ?*const fn (ecs_os_mutex_t) callconv(.C) void;
pub const ecs_os_api_cond_new_t = ?*const fn () callconv(.C) ecs_os_cond_t;
pub const ecs_os_api_cond_free_t = ?*const fn (ecs_os_cond_t) callconv(.C) void;
pub const ecs_os_api_cond_signal_t = ?*const fn (ecs_os_cond_t) callconv(.C) void;
pub const ecs_os_api_cond_broadcast_t = ?*const fn (ecs_os_cond_t) callconv(.C) void;
pub const ecs_os_api_cond_wait_t = ?*const fn (ecs_os_cond_t, ecs_os_mutex_t) callconv(.C) void;
pub const ecs_os_api_sleep_t = ?*const fn (i32, i32) callconv(.C) void;
pub const ecs_os_api_enable_high_timer_resolution_t = ?*const fn (bool) callconv(.C) void;
pub const ecs_os_api_get_time_t = ?*const fn ([*c]ecs_time_t) callconv(.C) void;
pub const ecs_os_api_now_t = ?*const fn () callconv(.C) u64;
pub const ecs_os_api_log_t = ?*const fn (i32, [*c]const u8, i32, [*c]const u8) callconv(.C) void;
pub const ecs_os_api_abort_t = ?*const fn () callconv(.C) void;
pub const ecs_os_api_dlopen_t = ?*const fn ([*c]const u8) callconv(.C) ecs_os_dl_t;
pub const ecs_os_api_dlproc_t = ?*const fn (ecs_os_dl_t, [*c]const u8) callconv(.C) ecs_os_proc_t;
pub const ecs_os_api_dlclose_t = ?*const fn (ecs_os_dl_t) callconv(.C) void;
pub const ecs_os_api_module_to_path_t = ?*const fn ([*c]const u8) callconv(.C) [*c]u8;
pub const ecs_os_api_perf_trace_t = ?*const fn ([*c]const u8, usize, [*c]const u8) callconv(.C) void;
pub const struct_ecs_os_api_t = extern struct {
    init_: ecs_os_api_init_t = @import("std").mem.zeroes(ecs_os_api_init_t),
    fini_: ecs_os_api_fini_t = @import("std").mem.zeroes(ecs_os_api_fini_t),
    malloc_: ecs_os_api_malloc_t = @import("std").mem.zeroes(ecs_os_api_malloc_t),
    realloc_: ecs_os_api_realloc_t = @import("std").mem.zeroes(ecs_os_api_realloc_t),
    calloc_: ecs_os_api_calloc_t = @import("std").mem.zeroes(ecs_os_api_calloc_t),
    free_: ecs_os_api_free_t = @import("std").mem.zeroes(ecs_os_api_free_t),
    strdup_: ecs_os_api_strdup_t = @import("std").mem.zeroes(ecs_os_api_strdup_t),
    thread_new_: ecs_os_api_thread_new_t = @import("std").mem.zeroes(ecs_os_api_thread_new_t),
    thread_join_: ecs_os_api_thread_join_t = @import("std").mem.zeroes(ecs_os_api_thread_join_t),
    thread_self_: ecs_os_api_thread_self_t = @import("std").mem.zeroes(ecs_os_api_thread_self_t),
    task_new_: ecs_os_api_thread_new_t = @import("std").mem.zeroes(ecs_os_api_thread_new_t),
    task_join_: ecs_os_api_thread_join_t = @import("std").mem.zeroes(ecs_os_api_thread_join_t),
    ainc_: ecs_os_api_ainc_t = @import("std").mem.zeroes(ecs_os_api_ainc_t),
    adec_: ecs_os_api_ainc_t = @import("std").mem.zeroes(ecs_os_api_ainc_t),
    lainc_: ecs_os_api_lainc_t = @import("std").mem.zeroes(ecs_os_api_lainc_t),
    ladec_: ecs_os_api_lainc_t = @import("std").mem.zeroes(ecs_os_api_lainc_t),
    mutex_new_: ecs_os_api_mutex_new_t = @import("std").mem.zeroes(ecs_os_api_mutex_new_t),
    mutex_free_: ecs_os_api_mutex_free_t = @import("std").mem.zeroes(ecs_os_api_mutex_free_t),
    mutex_lock_: ecs_os_api_mutex_lock_t = @import("std").mem.zeroes(ecs_os_api_mutex_lock_t),
    mutex_unlock_: ecs_os_api_mutex_lock_t = @import("std").mem.zeroes(ecs_os_api_mutex_lock_t),
    cond_new_: ecs_os_api_cond_new_t = @import("std").mem.zeroes(ecs_os_api_cond_new_t),
    cond_free_: ecs_os_api_cond_free_t = @import("std").mem.zeroes(ecs_os_api_cond_free_t),
    cond_signal_: ecs_os_api_cond_signal_t = @import("std").mem.zeroes(ecs_os_api_cond_signal_t),
    cond_broadcast_: ecs_os_api_cond_broadcast_t = @import("std").mem.zeroes(ecs_os_api_cond_broadcast_t),
    cond_wait_: ecs_os_api_cond_wait_t = @import("std").mem.zeroes(ecs_os_api_cond_wait_t),
    sleep_: ecs_os_api_sleep_t = @import("std").mem.zeroes(ecs_os_api_sleep_t),
    now_: ecs_os_api_now_t = @import("std").mem.zeroes(ecs_os_api_now_t),
    get_time_: ecs_os_api_get_time_t = @import("std").mem.zeroes(ecs_os_api_get_time_t),
    log_: ecs_os_api_log_t = @import("std").mem.zeroes(ecs_os_api_log_t),
    abort_: ecs_os_api_abort_t = @import("std").mem.zeroes(ecs_os_api_abort_t),
    dlopen_: ecs_os_api_dlopen_t = @import("std").mem.zeroes(ecs_os_api_dlopen_t),
    dlproc_: ecs_os_api_dlproc_t = @import("std").mem.zeroes(ecs_os_api_dlproc_t),
    dlclose_: ecs_os_api_dlclose_t = @import("std").mem.zeroes(ecs_os_api_dlclose_t),
    module_to_dl_: ecs_os_api_module_to_path_t = @import("std").mem.zeroes(ecs_os_api_module_to_path_t),
    module_to_etc_: ecs_os_api_module_to_path_t = @import("std").mem.zeroes(ecs_os_api_module_to_path_t),
    perf_trace_push_: ecs_os_api_perf_trace_t = @import("std").mem.zeroes(ecs_os_api_perf_trace_t),
    perf_trace_pop_: ecs_os_api_perf_trace_t = @import("std").mem.zeroes(ecs_os_api_perf_trace_t),
    log_level_: i32 = @import("std").mem.zeroes(i32),
    log_indent_: i32 = @import("std").mem.zeroes(i32),
    log_last_error_: i32 = @import("std").mem.zeroes(i32),
    log_last_timestamp_: i64 = @import("std").mem.zeroes(i64),
    flags_: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    log_out_: [*c]FILE = @import("std").mem.zeroes([*c]FILE),
};
pub const ecs_os_api_t = struct_ecs_os_api_t;
pub extern var ecs_os_api: ecs_os_api_t;
pub extern fn ecs_os_init() void;
pub extern fn ecs_os_fini() void;
pub extern fn ecs_os_set_api(os_api: [*c]ecs_os_api_t) void;
pub extern fn ecs_os_get_api() ecs_os_api_t;
pub extern fn ecs_os_set_api_defaults() void;
pub extern fn ecs_os_dbg(file: [*c]const u8, line: i32, msg: [*c]const u8) void;
pub extern fn ecs_os_trace(file: [*c]const u8, line: i32, msg: [*c]const u8) void;
pub extern fn ecs_os_warn(file: [*c]const u8, line: i32, msg: [*c]const u8) void;
pub extern fn ecs_os_err(file: [*c]const u8, line: i32, msg: [*c]const u8) void;
pub extern fn ecs_os_fatal(file: [*c]const u8, line: i32, msg: [*c]const u8) void;
pub extern fn ecs_os_strerror(err: c_int) [*c]const u8;
pub extern fn ecs_os_strset(str: [*c][*c]u8, value: [*c]const u8) void;
pub extern fn ecs_os_perf_trace_push_(file: [*c]const u8, line: usize, name: [*c]const u8) void;
pub extern fn ecs_os_perf_trace_pop_(file: [*c]const u8, line: usize, name: [*c]const u8) void;
pub extern fn ecs_sleepf(t: f64) void;
pub extern fn ecs_time_measure(start: [*c]ecs_time_t) f64;
pub extern fn ecs_time_sub(t1: ecs_time_t, t2: ecs_time_t) ecs_time_t;
pub extern fn ecs_time_to_double(t: ecs_time_t) f64;
pub extern fn ecs_os_memdup(src: ?*const anyopaque, size: ecs_size_t) ?*anyopaque;
pub extern fn ecs_os_has_heap() bool;
pub extern fn ecs_os_has_threading() bool;
pub extern fn ecs_os_has_task_support() bool;
pub extern fn ecs_os_has_time() bool;
pub extern fn ecs_os_has_logging() bool;
pub extern fn ecs_os_has_dl() bool;
pub extern fn ecs_os_has_modules() bool;
pub const ecs_id_t = u64;
pub const ecs_entity_t = ecs_id_t;
pub const ecs_type_t = extern struct {
    array: [*c]ecs_id_t = @import("std").mem.zeroes([*c]ecs_id_t),
    count: i32 = @import("std").mem.zeroes(i32),
};
pub const struct_ecs_world_t = opaque {};
pub const ecs_world_t = struct_ecs_world_t;
pub const struct_ecs_stage_t = opaque {};
pub const ecs_stage_t = struct_ecs_stage_t;
pub const struct_ecs_table_t = opaque {};
pub const ecs_table_t = struct_ecs_table_t;
pub const struct_ecs_term_ref_t = extern struct {
    id: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_term_ref_t = struct_ecs_term_ref_t;
pub const struct_ecs_term_t = extern struct {
    id: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    src: ecs_term_ref_t = @import("std").mem.zeroes(ecs_term_ref_t),
    first: ecs_term_ref_t = @import("std").mem.zeroes(ecs_term_ref_t),
    second: ecs_term_ref_t = @import("std").mem.zeroes(ecs_term_ref_t),
    trav: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    inout: i16 = @import("std").mem.zeroes(i16),
    oper: i16 = @import("std").mem.zeroes(i16),
    field_index: i8 = @import("std").mem.zeroes(i8),
    flags_: ecs_flags16_t = @import("std").mem.zeroes(ecs_flags16_t),
};
pub const ecs_term_t = struct_ecs_term_t;
pub const struct_ecs_mixins_t = opaque {};
pub const ecs_mixins_t = struct_ecs_mixins_t;
pub const struct_ecs_header_t = extern struct {
    magic: i32 = @import("std").mem.zeroes(i32),
    type: i32 = @import("std").mem.zeroes(i32),
    refcount: i32 = @import("std").mem.zeroes(i32),
    mixins: ?*ecs_mixins_t = @import("std").mem.zeroes(?*ecs_mixins_t),
};
pub const ecs_header_t = struct_ecs_header_t;
pub const EcsQueryCacheDefault: c_int = 0;
pub const EcsQueryCacheAuto: c_int = 1;
pub const EcsQueryCacheAll: c_int = 2;
pub const EcsQueryCacheNone: c_int = 3;
pub const enum_ecs_query_cache_kind_t = c_uint;
pub const ecs_query_cache_kind_t = enum_ecs_query_cache_kind_t;
pub const struct_ecs_query_t = extern struct {
    hdr: ecs_header_t = @import("std").mem.zeroes(ecs_header_t),
    terms: [32]ecs_term_t = @import("std").mem.zeroes([32]ecs_term_t),
    sizes: [32]i32 = @import("std").mem.zeroes([32]i32),
    ids: [32]ecs_id_t = @import("std").mem.zeroes([32]ecs_id_t),
    flags: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    var_count: i8 = @import("std").mem.zeroes(i8),
    term_count: i8 = @import("std").mem.zeroes(i8),
    field_count: i8 = @import("std").mem.zeroes(i8),
    fixed_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    var_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    static_id_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    data_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    write_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    read_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    row_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    shared_readonly_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    set_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    cache_kind: ecs_query_cache_kind_t = @import("std").mem.zeroes(ecs_query_cache_kind_t),
    vars: [*c][*c]u8 = @import("std").mem.zeroes([*c][*c]u8),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    binding_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    real_world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    eval_count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_query_t = struct_ecs_query_t;
pub const struct_ecs_table_range_t = extern struct {
    table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    offset: i32 = @import("std").mem.zeroes(i32),
    count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_table_range_t = struct_ecs_table_range_t;
pub const struct_ecs_var_t = extern struct {
    range: ecs_table_range_t = @import("std").mem.zeroes(ecs_table_range_t),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_var_t = struct_ecs_var_t;
pub const struct_ecs_table_cache_t_1 = opaque {};
pub const struct_ecs_table_cache_hdr_t = extern struct {
    cache: ?*struct_ecs_table_cache_t_1 = @import("std").mem.zeroes(?*struct_ecs_table_cache_t_1),
    table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    prev: [*c]struct_ecs_table_cache_hdr_t = @import("std").mem.zeroes([*c]struct_ecs_table_cache_hdr_t),
    next: [*c]struct_ecs_table_cache_hdr_t = @import("std").mem.zeroes([*c]struct_ecs_table_cache_hdr_t),
    empty: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_table_cache_hdr_t = struct_ecs_table_cache_hdr_t;
pub const struct_ecs_table_record_t = extern struct {
    hdr: ecs_table_cache_hdr_t = @import("std").mem.zeroes(ecs_table_cache_hdr_t),
    index: i16 = @import("std").mem.zeroes(i16),
    count: i16 = @import("std").mem.zeroes(i16),
    column: i16 = @import("std").mem.zeroes(i16),
};
pub const ecs_table_record_t = struct_ecs_table_record_t;
pub const struct_ecs_query_var_t_3 = opaque {};
pub const struct_ecs_query_op_t_4 = opaque {};
pub const struct_ecs_query_op_ctx_t_5 = opaque {};
pub const struct_ecs_query_cache_table_match_t = opaque {};
pub const ecs_query_cache_table_match_t = struct_ecs_query_cache_table_match_t;
pub const struct_ecs_query_op_profile_t = extern struct {
    count: [2]i32 = @import("std").mem.zeroes([2]i32),
};
pub const ecs_query_op_profile_t = struct_ecs_query_op_profile_t;
pub const struct_ecs_query_iter_t = extern struct {
    query: [*c]const ecs_query_t = @import("std").mem.zeroes([*c]const ecs_query_t),
    vars: [*c]struct_ecs_var_t = @import("std").mem.zeroes([*c]struct_ecs_var_t),
    query_vars: ?*const struct_ecs_query_var_t_3 = @import("std").mem.zeroes(?*const struct_ecs_query_var_t_3),
    ops: ?*const struct_ecs_query_op_t_4 = @import("std").mem.zeroes(?*const struct_ecs_query_op_t_4),
    op_ctx: ?*struct_ecs_query_op_ctx_t_5 = @import("std").mem.zeroes(?*struct_ecs_query_op_ctx_t_5),
    node: ?*ecs_query_cache_table_match_t = @import("std").mem.zeroes(?*ecs_query_cache_table_match_t),
    prev: ?*ecs_query_cache_table_match_t = @import("std").mem.zeroes(?*ecs_query_cache_table_match_t),
    last: ?*ecs_query_cache_table_match_t = @import("std").mem.zeroes(?*ecs_query_cache_table_match_t),
    written: [*c]u64 = @import("std").mem.zeroes([*c]u64),
    skip_count: i32 = @import("std").mem.zeroes(i32),
    profile: [*c]ecs_query_op_profile_t = @import("std").mem.zeroes([*c]ecs_query_op_profile_t),
    op: i16 = @import("std").mem.zeroes(i16),
    sp: i16 = @import("std").mem.zeroes(i16),
};
pub const ecs_query_iter_t = struct_ecs_query_iter_t;
pub const struct_ecs_page_iter_t = extern struct {
    offset: i32 = @import("std").mem.zeroes(i32),
    limit: i32 = @import("std").mem.zeroes(i32),
    remaining: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_page_iter_t = struct_ecs_page_iter_t;
pub const struct_ecs_worker_iter_t = extern struct {
    index: i32 = @import("std").mem.zeroes(i32),
    count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_worker_iter_t = struct_ecs_worker_iter_t;
pub const struct_ecs_table_cache_iter_t = extern struct {
    cur: [*c]struct_ecs_table_cache_hdr_t = @import("std").mem.zeroes([*c]struct_ecs_table_cache_hdr_t),
    next: [*c]struct_ecs_table_cache_hdr_t = @import("std").mem.zeroes([*c]struct_ecs_table_cache_hdr_t),
    next_list: [*c]struct_ecs_table_cache_hdr_t = @import("std").mem.zeroes([*c]struct_ecs_table_cache_hdr_t),
};
pub const ecs_table_cache_iter_t = struct_ecs_table_cache_iter_t;
pub const struct_ecs_each_iter_t = extern struct {
    it: ecs_table_cache_iter_t = @import("std").mem.zeroes(ecs_table_cache_iter_t),
    ids: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    sources: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    sizes: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    columns: i32 = @import("std").mem.zeroes(i32),
    trs: [*c]const ecs_table_record_t = @import("std").mem.zeroes([*c]const ecs_table_record_t),
};
pub const ecs_each_iter_t = struct_ecs_each_iter_t;
const union_unnamed_2 = extern union {
    query: ecs_query_iter_t,
    page: ecs_page_iter_t,
    worker: ecs_worker_iter_t,
    each: ecs_each_iter_t,
};
pub const struct_ecs_iter_cache_t = extern struct {
    stack_cursor: [*c]ecs_stack_cursor_t = @import("std").mem.zeroes([*c]ecs_stack_cursor_t),
    used: ecs_flags8_t = @import("std").mem.zeroes(ecs_flags8_t),
    allocated: ecs_flags8_t = @import("std").mem.zeroes(ecs_flags8_t),
};
pub const ecs_iter_cache_t = struct_ecs_iter_cache_t;
pub const struct_ecs_iter_private_t = extern struct {
    iter: union_unnamed_2 = @import("std").mem.zeroes(union_unnamed_2),
    entity_iter: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    cache: ecs_iter_cache_t = @import("std").mem.zeroes(ecs_iter_cache_t),
};
pub const ecs_iter_private_t = struct_ecs_iter_private_t;
pub const ecs_iter_next_action_t = ?*const fn ([*c]ecs_iter_t) callconv(.C) bool;
pub const ecs_iter_fini_action_t = ?*const fn ([*c]ecs_iter_t) callconv(.C) void;
pub const struct_ecs_iter_t = extern struct {
    world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    real_world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    entities: [*c]const ecs_entity_t = @import("std").mem.zeroes([*c]const ecs_entity_t),
    sizes: [*c]const ecs_size_t = @import("std").mem.zeroes([*c]const ecs_size_t),
    table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    other_table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    ids: [*c]ecs_id_t = @import("std").mem.zeroes([*c]ecs_id_t),
    variables: [*c]ecs_var_t = @import("std").mem.zeroes([*c]ecs_var_t),
    trs: [*c][*c]const ecs_table_record_t = @import("std").mem.zeroes([*c][*c]const ecs_table_record_t),
    sources: [*c]ecs_entity_t = @import("std").mem.zeroes([*c]ecs_entity_t),
    constrained_vars: ecs_flags64_t = @import("std").mem.zeroes(ecs_flags64_t),
    group_id: u64 = @import("std").mem.zeroes(u64),
    set_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    ref_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    row_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    up_fields: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    system: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    event: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    event_id: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    event_cur: i32 = @import("std").mem.zeroes(i32),
    field_count: i8 = @import("std").mem.zeroes(i8),
    term_index: i8 = @import("std").mem.zeroes(i8),
    variable_count: i8 = @import("std").mem.zeroes(i8),
    query: [*c]const ecs_query_t = @import("std").mem.zeroes([*c]const ecs_query_t),
    variable_names: [*c][*c]u8 = @import("std").mem.zeroes([*c][*c]u8),
    param: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    binding_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    callback_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    run_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    delta_time: f32 = @import("std").mem.zeroes(f32),
    delta_system_time: f32 = @import("std").mem.zeroes(f32),
    frame_offset: i32 = @import("std").mem.zeroes(i32),
    offset: i32 = @import("std").mem.zeroes(i32),
    count: i32 = @import("std").mem.zeroes(i32),
    flags: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    interrupted_by: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    priv_: ecs_iter_private_t = @import("std").mem.zeroes(ecs_iter_private_t),
    next: ecs_iter_next_action_t = @import("std").mem.zeroes(ecs_iter_next_action_t),
    callback: ?*const fn ([*c]ecs_iter_t) callconv(.C) void = @import("std").mem.zeroes(?*const fn ([*c]ecs_iter_t) callconv(.C) void),
    fini: ecs_iter_fini_action_t = @import("std").mem.zeroes(ecs_iter_fini_action_t),
    chain_it: [*c]ecs_iter_t = @import("std").mem.zeroes([*c]ecs_iter_t),
};
pub const ecs_iter_t = struct_ecs_iter_t;
pub const ecs_iter_action_t = ?*const fn ([*c]ecs_iter_t) callconv(.C) void;
pub const ecs_run_action_t = ?*const fn ([*c]ecs_iter_t) callconv(.C) void;
pub const ecs_ctx_free_t = ?*const fn (?*anyopaque) callconv(.C) void;
pub const struct_ecs_event_id_record_t_6 = opaque {};
pub const struct_ecs_event_record_t = extern struct {
    any: ?*struct_ecs_event_id_record_t_6 = @import("std").mem.zeroes(?*struct_ecs_event_id_record_t_6),
    wildcard: ?*struct_ecs_event_id_record_t_6 = @import("std").mem.zeroes(?*struct_ecs_event_id_record_t_6),
    wildcard_pair: ?*struct_ecs_event_id_record_t_6 = @import("std").mem.zeroes(?*struct_ecs_event_id_record_t_6),
    event_ids: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
    event: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_event_record_t = struct_ecs_event_record_t;
pub const struct_ecs_observable_t = extern struct {
    on_add: ecs_event_record_t = @import("std").mem.zeroes(ecs_event_record_t),
    on_remove: ecs_event_record_t = @import("std").mem.zeroes(ecs_event_record_t),
    on_set: ecs_event_record_t = @import("std").mem.zeroes(ecs_event_record_t),
    on_wildcard: ecs_event_record_t = @import("std").mem.zeroes(ecs_event_record_t),
    events: ecs_sparse_t = @import("std").mem.zeroes(ecs_sparse_t),
};
pub const ecs_observable_t = struct_ecs_observable_t;
pub const struct_ecs_observer_t = extern struct {
    hdr: ecs_header_t = @import("std").mem.zeroes(ecs_header_t),
    query: [*c]ecs_query_t = @import("std").mem.zeroes([*c]ecs_query_t),
    events: [8]ecs_entity_t = @import("std").mem.zeroes([8]ecs_entity_t),
    event_count: i32 = @import("std").mem.zeroes(i32),
    callback: ecs_iter_action_t = @import("std").mem.zeroes(ecs_iter_action_t),
    run: ecs_run_action_t = @import("std").mem.zeroes(ecs_run_action_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    callback_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    run_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    callback_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    run_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    observable: [*c]ecs_observable_t = @import("std").mem.zeroes([*c]ecs_observable_t),
    world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_observer_t = struct_ecs_observer_t;
pub const struct_ecs_id_record_t = opaque {};
pub const ecs_id_record_t = struct_ecs_id_record_t;
pub const struct_ecs_record_t = extern struct {
    idr: ?*ecs_id_record_t = @import("std").mem.zeroes(?*ecs_id_record_t),
    table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    row: u32 = @import("std").mem.zeroes(u32),
    dense: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_record_t = struct_ecs_record_t;
pub const struct_ecs_ref_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    id: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    table_id: u64 = @import("std").mem.zeroes(u64),
    tr: [*c]struct_ecs_table_record_t = @import("std").mem.zeroes([*c]struct_ecs_table_record_t),
    record: [*c]ecs_record_t = @import("std").mem.zeroes([*c]ecs_record_t),
};
pub const ecs_ref_t = struct_ecs_ref_t;
pub const ecs_type_hooks_t = struct_ecs_type_hooks_t;
pub const struct_ecs_type_info_t = extern struct {
    size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    alignment: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    hooks: ecs_type_hooks_t = @import("std").mem.zeroes(ecs_type_hooks_t),
    component: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_type_info_t = struct_ecs_type_info_t;
pub const ecs_xtor_t = ?*const fn (?*anyopaque, i32, [*c]const ecs_type_info_t) callconv(.C) void;
pub const ecs_copy_t = ?*const fn (?*anyopaque, ?*const anyopaque, i32, [*c]const ecs_type_info_t) callconv(.C) void;
pub const ecs_move_t = ?*const fn (?*anyopaque, ?*anyopaque, i32, [*c]const ecs_type_info_t) callconv(.C) void;
pub const struct_ecs_type_hooks_t = extern struct {
    ctor: ecs_xtor_t = @import("std").mem.zeroes(ecs_xtor_t),
    dtor: ecs_xtor_t = @import("std").mem.zeroes(ecs_xtor_t),
    copy: ecs_copy_t = @import("std").mem.zeroes(ecs_copy_t),
    move: ecs_move_t = @import("std").mem.zeroes(ecs_move_t),
    copy_ctor: ecs_copy_t = @import("std").mem.zeroes(ecs_copy_t),
    move_ctor: ecs_move_t = @import("std").mem.zeroes(ecs_move_t),
    ctor_move_dtor: ecs_move_t = @import("std").mem.zeroes(ecs_move_t),
    move_dtor: ecs_move_t = @import("std").mem.zeroes(ecs_move_t),
    on_add: ecs_iter_action_t = @import("std").mem.zeroes(ecs_iter_action_t),
    on_set: ecs_iter_action_t = @import("std").mem.zeroes(ecs_iter_action_t),
    on_remove: ecs_iter_action_t = @import("std").mem.zeroes(ecs_iter_action_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    binding_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    lifecycle_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    binding_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    lifecycle_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
};
pub const ecs_poly_t = anyopaque;
pub const ecs_order_by_action_t = ?*const fn (ecs_entity_t, ?*const anyopaque, ecs_entity_t, ?*const anyopaque) callconv(.C) c_int;
pub const ecs_sort_table_action_t = ?*const fn (?*ecs_world_t, ?*ecs_table_t, [*c]ecs_entity_t, ?*anyopaque, i32, i32, i32, ecs_order_by_action_t) callconv(.C) void;
pub const ecs_group_by_action_t = ?*const fn (?*ecs_world_t, ?*ecs_table_t, ecs_id_t, ?*anyopaque) callconv(.C) u64;
pub const ecs_group_create_action_t = ?*const fn (?*ecs_world_t, u64, ?*anyopaque) callconv(.C) ?*anyopaque;
pub const ecs_group_delete_action_t = ?*const fn (?*ecs_world_t, u64, ?*anyopaque, ?*anyopaque) callconv(.C) void;
pub const ecs_module_action_t = ?*const fn (?*ecs_world_t) callconv(.C) void;
pub const ecs_fini_action_t = ?*const fn (?*ecs_world_t, ?*anyopaque) callconv(.C) void;
pub const ecs_compare_action_t = ?*const fn (?*const anyopaque, ?*const anyopaque) callconv(.C) c_int;
pub const ecs_hash_value_action_t = ?*const fn (?*const anyopaque) callconv(.C) u64;
pub const flecs_poly_dtor_t = ?*const fn (?*ecs_poly_t) callconv(.C) void;
pub const EcsInOutDefault: c_int = 0;
pub const EcsInOutNone: c_int = 1;
pub const EcsInOutFilter: c_int = 2;
pub const EcsInOut: c_int = 3;
pub const EcsIn: c_int = 4;
pub const EcsOut: c_int = 5;
pub const enum_ecs_inout_kind_t = c_uint;
pub const ecs_inout_kind_t = enum_ecs_inout_kind_t;
pub const EcsAnd: c_int = 0;
pub const EcsOr: c_int = 1;
pub const EcsNot: c_int = 2;
pub const EcsOptional: c_int = 3;
pub const EcsAndFrom: c_int = 4;
pub const EcsOrFrom: c_int = 5;
pub const EcsNotFrom: c_int = 6;
pub const enum_ecs_oper_kind_t = c_uint;
pub const ecs_oper_kind_t = enum_ecs_oper_kind_t;
pub const struct_ecs_data_t = opaque {};
pub const ecs_data_t = struct_ecs_data_t;
pub extern fn flecs_module_path_from_c(c_name: [*c]const u8) [*c]u8;
pub extern fn flecs_identifier_is_0(id: [*c]const u8) bool;
pub extern fn flecs_default_ctor(ptr: ?*anyopaque, count: i32, ctx: [*c]const ecs_type_info_t) void;
pub extern fn flecs_vasprintf(fmt: [*c]const u8, args: va_list) [*c]u8;
pub extern fn flecs_asprintf(fmt: [*c]const u8, ...) [*c]u8;
pub extern fn flecs_chresc(out: [*c]u8, in: u8, delimiter: u8) [*c]u8;
pub extern fn flecs_chrparse(in: [*c]const u8, out: [*c]u8) [*c]const u8;
pub extern fn flecs_stresc(out: [*c]u8, size: ecs_size_t, delimiter: u8, in: [*c]const u8) ecs_size_t;
pub extern fn flecs_astresc(delimiter: u8, in: [*c]const u8) [*c]u8;
pub extern fn flecs_parse_ws_eol(ptr: [*c]const u8) [*c]const u8;
pub extern fn flecs_parse_digit(ptr: [*c]const u8, token: [*c]u8) [*c]const u8;
pub extern fn flecs_to_snake_case(str: [*c]const u8) [*c]u8;
pub extern fn flecs_table_observed_count(table: ?*const ecs_table_t) i32;
pub extern fn flecs_dump_backtrace(stream: ?*anyopaque) void;
pub const struct_ecs_suspend_readonly_state_t = extern struct {
    is_readonly: bool = @import("std").mem.zeroes(bool),
    is_deferred: bool = @import("std").mem.zeroes(bool),
    defer_count: i32 = @import("std").mem.zeroes(i32),
    scope: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    with: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    commands: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    defer_stack: ecs_stack_t = @import("std").mem.zeroes(ecs_stack_t),
    stage: ?*ecs_stage_t = @import("std").mem.zeroes(?*ecs_stage_t),
};
pub const ecs_suspend_readonly_state_t = struct_ecs_suspend_readonly_state_t;
pub extern fn flecs_suspend_readonly(world: ?*const ecs_world_t, state: [*c]ecs_suspend_readonly_state_t) ?*ecs_world_t;
pub extern fn flecs_resume_readonly(world: ?*ecs_world_t, state: [*c]ecs_suspend_readonly_state_t) void;
pub extern fn flecs_poly_claim_(poly: ?*ecs_poly_t) i32;
pub extern fn flecs_poly_release_(poly: ?*ecs_poly_t) i32;
pub extern fn flecs_poly_refcount(poly: ?*ecs_poly_t) i32;
pub const ecs_hm_bucket_t = extern struct {
    keys: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    values: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
};
pub const ecs_hashmap_t = extern struct {
    hash: ecs_hash_value_action_t = @import("std").mem.zeroes(ecs_hash_value_action_t),
    compare: ecs_compare_action_t = @import("std").mem.zeroes(ecs_compare_action_t),
    key_size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    value_size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    hashmap_allocator: [*c]ecs_block_allocator_t = @import("std").mem.zeroes([*c]ecs_block_allocator_t),
    bucket_allocator: ecs_block_allocator_t = @import("std").mem.zeroes(ecs_block_allocator_t),
    impl: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
};
pub const flecs_hashmap_iter_t = extern struct {
    it: ecs_map_iter_t = @import("std").mem.zeroes(ecs_map_iter_t),
    bucket: [*c]ecs_hm_bucket_t = @import("std").mem.zeroes([*c]ecs_hm_bucket_t),
    index: i32 = @import("std").mem.zeroes(i32),
};
pub const flecs_hashmap_result_t = extern struct {
    key: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    value: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    hash: u64 = @import("std").mem.zeroes(u64),
};
pub extern fn flecs_hashmap_init_(hm: [*c]ecs_hashmap_t, key_size: ecs_size_t, value_size: ecs_size_t, hash: ecs_hash_value_action_t, compare: ecs_compare_action_t, allocator: [*c]ecs_allocator_t) void;
pub extern fn flecs_hashmap_fini(map: [*c]ecs_hashmap_t) void;
pub extern fn flecs_hashmap_get_(map: [*c]const ecs_hashmap_t, key_size: ecs_size_t, key: ?*const anyopaque, value_size: ecs_size_t) ?*anyopaque;
pub extern fn flecs_hashmap_ensure_(map: [*c]ecs_hashmap_t, key_size: ecs_size_t, key: ?*const anyopaque, value_size: ecs_size_t) flecs_hashmap_result_t;
pub extern fn flecs_hashmap_set_(map: [*c]ecs_hashmap_t, key_size: ecs_size_t, key: ?*anyopaque, value_size: ecs_size_t, value: ?*const anyopaque) void;
pub extern fn flecs_hashmap_remove_(map: [*c]ecs_hashmap_t, key_size: ecs_size_t, key: ?*const anyopaque, value_size: ecs_size_t) void;
pub extern fn flecs_hashmap_remove_w_hash_(map: [*c]ecs_hashmap_t, key_size: ecs_size_t, key: ?*const anyopaque, value_size: ecs_size_t, hash: u64) void;
pub extern fn flecs_hashmap_get_bucket(map: [*c]const ecs_hashmap_t, hash: u64) [*c]ecs_hm_bucket_t;
pub extern fn flecs_hm_bucket_remove(map: [*c]ecs_hashmap_t, bucket: [*c]ecs_hm_bucket_t, hash: u64, index: i32) void;
pub extern fn flecs_hashmap_copy(dst: [*c]ecs_hashmap_t, src: [*c]const ecs_hashmap_t) void;
pub extern fn flecs_hashmap_iter(map: [*c]ecs_hashmap_t) flecs_hashmap_iter_t;
pub extern fn flecs_hashmap_next_(it: [*c]flecs_hashmap_iter_t, key_size: ecs_size_t, key_out: ?*anyopaque, value_size: ecs_size_t) ?*anyopaque;
pub const struct_ecs_value_t = extern struct {
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    ptr: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
};
pub const ecs_value_t = struct_ecs_value_t;
pub const struct_ecs_entity_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    id: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    parent: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    sep: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    root_sep: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    symbol: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    use_low_id: bool = @import("std").mem.zeroes(bool),
    add: ?[*:0]const ecs_id_t = @import("std").mem.zeroes([*c]const ecs_id_t),
    set: [*c]const ecs_value_t = @import("std").mem.zeroes([*c]const ecs_value_t),
    add_expr: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_entity_desc_t = struct_ecs_entity_desc_t;
pub const struct_ecs_bulk_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    entities: [*c]ecs_entity_t = @import("std").mem.zeroes([*c]ecs_entity_t),
    count: i32 = @import("std").mem.zeroes(i32),
    ids: [32]ecs_id_t = @import("std").mem.zeroes([32]ecs_id_t),
    data: [*c]?*anyopaque = @import("std").mem.zeroes([*c]?*anyopaque),
    table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
};
pub const ecs_bulk_desc_t = struct_ecs_bulk_desc_t;
pub const struct_ecs_component_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    type: ecs_type_info_t = @import("std").mem.zeroes(ecs_type_info_t),
};
pub const ecs_component_desc_t = struct_ecs_component_desc_t;
pub const struct_ecs_query_desc_t = extern struct {
    _canary: i32 = 0,
    terms: [FLECS_TERM_COUNT_MAX]ecs_term_t = [_]ecs_term_t{.{}} ** FLECS_TERM_COUNT_MAX,
    expr: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    cache_kind: ecs_query_cache_kind_t = EcsQueryCacheNone,
    flags: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    order_by_callback: ecs_order_by_action_t = @import("std").mem.zeroes(ecs_order_by_action_t),
    order_by_table_callback: ecs_sort_table_action_t = @import("std").mem.zeroes(ecs_sort_table_action_t),
    order_by: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    group_by: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    group_by_callback: ecs_group_by_action_t = @import("std").mem.zeroes(ecs_group_by_action_t),
    on_group_create: ecs_group_create_action_t = @import("std").mem.zeroes(ecs_group_create_action_t),
    on_group_delete: ecs_group_delete_action_t = @import("std").mem.zeroes(ecs_group_delete_action_t),
    group_by_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    group_by_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    binding_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    binding_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_query_desc_t = struct_ecs_query_desc_t;
pub const struct_ecs_observer_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    query: ecs_query_desc_t = @import("std").mem.zeroes(ecs_query_desc_t),
    events: [8]ecs_entity_t = @import("std").mem.zeroes([8]ecs_entity_t),
    yield_existing: bool = @import("std").mem.zeroes(bool),
    callback: ecs_iter_action_t = @import("std").mem.zeroes(ecs_iter_action_t),
    run: ecs_run_action_t = @import("std").mem.zeroes(ecs_run_action_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    callback_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    callback_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    run_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    run_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    observable: ?*ecs_poly_t = @import("std").mem.zeroes(?*ecs_poly_t),
    last_event_id: [*c]i32 = @import("std").mem.zeroes([*c]i32),
    term_index_: i8 = @import("std").mem.zeroes(i8),
    flags_: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
};
pub const ecs_observer_desc_t = struct_ecs_observer_desc_t;
pub const struct_ecs_event_desc_t = extern struct {
    event: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    ids: [*c]const ecs_type_t = @import("std").mem.zeroes([*c]const ecs_type_t),
    table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    other_table: ?*ecs_table_t = @import("std").mem.zeroes(?*ecs_table_t),
    offset: i32 = @import("std").mem.zeroes(i32),
    count: i32 = @import("std").mem.zeroes(i32),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    param: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    const_param: ?*const anyopaque = @import("std").mem.zeroes(?*const anyopaque),
    observable: ?*ecs_poly_t = @import("std").mem.zeroes(?*ecs_poly_t),
    flags: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
};
pub const ecs_event_desc_t = struct_ecs_event_desc_t;
pub const struct_ecs_build_info_t = extern struct {
    compiler: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    addons: [*c][*c]const u8 = @import("std").mem.zeroes([*c][*c]const u8),
    version: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    version_major: i16 = @import("std").mem.zeroes(i16),
    version_minor: i16 = @import("std").mem.zeroes(i16),
    version_patch: i16 = @import("std").mem.zeroes(i16),
    debug: bool = @import("std").mem.zeroes(bool),
    sanitize: bool = @import("std").mem.zeroes(bool),
    perf_trace: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_build_info_t = struct_ecs_build_info_t;
const struct_unnamed_7 = extern struct {
    add_count: i64 = @import("std").mem.zeroes(i64),
    remove_count: i64 = @import("std").mem.zeroes(i64),
    delete_count: i64 = @import("std").mem.zeroes(i64),
    clear_count: i64 = @import("std").mem.zeroes(i64),
    set_count: i64 = @import("std").mem.zeroes(i64),
    ensure_count: i64 = @import("std").mem.zeroes(i64),
    modified_count: i64 = @import("std").mem.zeroes(i64),
    discard_count: i64 = @import("std").mem.zeroes(i64),
    event_count: i64 = @import("std").mem.zeroes(i64),
    other_count: i64 = @import("std").mem.zeroes(i64),
    batched_entity_count: i64 = @import("std").mem.zeroes(i64),
    batched_command_count: i64 = @import("std").mem.zeroes(i64),
};
pub const struct_ecs_world_info_t = extern struct {
    last_component_id: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    min_id: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    max_id: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    delta_time_raw: f32 = @import("std").mem.zeroes(f32),
    delta_time: f32 = @import("std").mem.zeroes(f32),
    time_scale: f32 = @import("std").mem.zeroes(f32),
    target_fps: f32 = @import("std").mem.zeroes(f32),
    frame_time_total: f32 = @import("std").mem.zeroes(f32),
    system_time_total: f32 = @import("std").mem.zeroes(f32),
    emit_time_total: f32 = @import("std").mem.zeroes(f32),
    merge_time_total: f32 = @import("std").mem.zeroes(f32),
    rematch_time_total: f32 = @import("std").mem.zeroes(f32),
    world_time_total: f64 = @import("std").mem.zeroes(f64),
    world_time_total_raw: f64 = @import("std").mem.zeroes(f64),
    frame_count_total: i64 = @import("std").mem.zeroes(i64),
    merge_count_total: i64 = @import("std").mem.zeroes(i64),
    eval_comp_monitors_total: i64 = @import("std").mem.zeroes(i64),
    rematch_count_total: i64 = @import("std").mem.zeroes(i64),
    id_create_total: i64 = @import("std").mem.zeroes(i64),
    id_delete_total: i64 = @import("std").mem.zeroes(i64),
    table_create_total: i64 = @import("std").mem.zeroes(i64),
    table_delete_total: i64 = @import("std").mem.zeroes(i64),
    pipeline_build_count_total: i64 = @import("std").mem.zeroes(i64),
    systems_ran_frame: i64 = @import("std").mem.zeroes(i64),
    observers_ran_frame: i64 = @import("std").mem.zeroes(i64),
    tag_id_count: i32 = @import("std").mem.zeroes(i32),
    component_id_count: i32 = @import("std").mem.zeroes(i32),
    pair_id_count: i32 = @import("std").mem.zeroes(i32),
    table_count: i32 = @import("std").mem.zeroes(i32),
    empty_table_count: i32 = @import("std").mem.zeroes(i32),
    cmd: struct_unnamed_7 = @import("std").mem.zeroes(struct_unnamed_7),
    name_prefix: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_world_info_t = struct_ecs_world_info_t;
pub const struct_ecs_query_group_info_t = extern struct {
    match_count: i32 = @import("std").mem.zeroes(i32),
    table_count: i32 = @import("std").mem.zeroes(i32),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
};
pub const ecs_query_group_info_t = struct_ecs_query_group_info_t;
pub const struct_EcsIdentifier = extern struct {
    value: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    length: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    hash: u64 = @import("std").mem.zeroes(u64),
    index_hash: u64 = @import("std").mem.zeroes(u64),
    index: [*c]ecs_hashmap_t = @import("std").mem.zeroes([*c]ecs_hashmap_t),
};
pub const EcsIdentifier = struct_EcsIdentifier;
pub const struct_EcsComponent = extern struct {
    size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    alignment: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
};
pub const EcsComponent = struct_EcsComponent;
pub const struct_EcsPoly = extern struct {
    poly: ?*ecs_poly_t = @import("std").mem.zeroes(?*ecs_poly_t),
};
pub const EcsPoly = struct_EcsPoly;
pub const struct_EcsDefaultChildComponent = extern struct {
    component: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
};
pub const EcsDefaultChildComponent = struct_EcsDefaultChildComponent;
pub extern const ECS_PAIR: ecs_id_t;
pub extern const ECS_AUTO_OVERRIDE: ecs_id_t;
pub extern const ECS_TOGGLE: ecs_id_t;
pub extern const FLECS_IDEcsComponentID_: ecs_entity_t;
pub extern const FLECS_IDEcsIdentifierID_: ecs_entity_t;
pub extern const FLECS_IDEcsPolyID_: ecs_entity_t;
pub extern const FLECS_IDEcsDefaultChildComponentID_: ecs_entity_t;
pub extern const EcsQuery: ecs_entity_t;
pub extern const EcsObserver: ecs_entity_t;
pub extern const EcsSystem: ecs_entity_t;
pub extern const FLECS_IDEcsTickSourceID_: ecs_entity_t;
pub extern const FLECS_IDEcsPipelineQueryID_: ecs_entity_t;
pub extern const FLECS_IDEcsTimerID_: ecs_entity_t;
pub extern const FLECS_IDEcsRateFilterID_: ecs_entity_t;
pub extern const EcsFlecs: ecs_entity_t;
pub extern const EcsFlecsCore: ecs_entity_t;
pub extern const EcsWorld: ecs_entity_t;
pub extern const EcsWildcard: ecs_entity_t;
pub extern const EcsAny: ecs_entity_t;
pub extern const EcsThis: ecs_entity_t;
pub extern const EcsVariable: ecs_entity_t;
pub extern const EcsTransitive: ecs_entity_t;
pub extern const EcsReflexive: ecs_entity_t;
pub extern const EcsFinal: ecs_entity_t;
pub extern const EcsOnInstantiate: ecs_entity_t;
pub extern const EcsOverride: ecs_entity_t;
pub extern const EcsInherit: ecs_entity_t;
pub extern const EcsDontInherit: ecs_entity_t;
pub extern const EcsSymmetric: ecs_entity_t;
pub extern const EcsExclusive: ecs_entity_t;
pub extern const EcsAcyclic: ecs_entity_t;
pub extern const EcsTraversable: ecs_entity_t;
pub extern const EcsWith: ecs_entity_t;
pub extern const EcsOneOf: ecs_entity_t;
pub extern const EcsCanToggle: ecs_entity_t;
pub extern const EcsTrait: ecs_entity_t;
pub extern const EcsRelationship: ecs_entity_t;
pub extern const EcsTarget: ecs_entity_t;
pub extern const EcsPairIsTag: ecs_entity_t;
pub extern const EcsName: ecs_entity_t;
pub extern const EcsSymbol: ecs_entity_t;
pub extern const EcsAlias: ecs_entity_t;
pub extern const EcsChildOf: ecs_entity_t;
pub extern const EcsIsA: ecs_entity_t;
pub extern const EcsDependsOn: ecs_entity_t;
pub extern const EcsSlotOf: ecs_entity_t;
pub extern const EcsModule: ecs_entity_t;
pub extern const EcsPrivate: ecs_entity_t;
pub extern const EcsPrefab: ecs_entity_t;
pub extern const EcsDisabled: ecs_entity_t;
pub extern const EcsNotQueryable: ecs_entity_t;
pub extern const EcsOnAdd: ecs_entity_t;
pub extern const EcsOnRemove: ecs_entity_t;
pub extern const EcsOnSet: ecs_entity_t;
pub extern const EcsMonitor: ecs_entity_t;
pub extern const EcsOnTableCreate: ecs_entity_t;
pub extern const EcsOnTableDelete: ecs_entity_t;
pub extern const EcsOnTableEmpty: ecs_entity_t;
pub extern const EcsOnTableFill: ecs_entity_t;
pub extern const EcsOnDelete: ecs_entity_t;
pub extern const EcsOnDeleteTarget: ecs_entity_t;
pub extern const EcsRemove: ecs_entity_t;
pub extern const EcsDelete: ecs_entity_t;
pub extern const EcsPanic: ecs_entity_t;
pub extern const EcsSparse: ecs_entity_t;
pub extern const EcsUnion: ecs_entity_t;
pub extern const EcsPredEq: ecs_entity_t;
pub extern const EcsPredMatch: ecs_entity_t;
pub extern const EcsPredLookup: ecs_entity_t;
pub extern const EcsScopeOpen: ecs_entity_t;
pub extern const EcsScopeClose: ecs_entity_t;
pub extern const EcsEmpty: ecs_entity_t;
pub extern const FLECS_IDEcsPipelineID_: ecs_entity_t;
pub extern const EcsOnStart: ecs_entity_t;
pub extern const EcsPreFrame: ecs_entity_t;
pub extern const EcsOnLoad: ecs_entity_t;
pub extern const EcsPostLoad: ecs_entity_t;
pub extern const EcsPreUpdate: ecs_entity_t;
pub extern const EcsOnUpdate: ecs_entity_t;
pub extern const EcsOnValidate: ecs_entity_t;
pub extern const EcsPostUpdate: ecs_entity_t;
pub extern const EcsPreStore: ecs_entity_t;
pub extern const EcsOnStore: ecs_entity_t;
pub extern const EcsPostFrame: ecs_entity_t;
pub extern const EcsPhase: ecs_entity_t;
pub extern fn ecs_init() ?*ecs_world_t;
pub extern fn ecs_mini() ?*ecs_world_t;
pub extern fn ecs_init_w_args(argc: c_int, argv: [*c][*c]u8) ?*ecs_world_t;
pub extern fn ecs_fini(world: ?*ecs_world_t) c_int;
pub extern fn ecs_is_fini(world: ?*const ecs_world_t) bool;
pub extern fn ecs_atfini(world: ?*ecs_world_t, action: ecs_fini_action_t, ctx: ?*anyopaque) void;
pub const struct_ecs_entities_t = extern struct {
    ids: [*c]const ecs_entity_t = @import("std").mem.zeroes([*c]const ecs_entity_t),
    count: i32 = @import("std").mem.zeroes(i32),
    alive_count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_entities_t = struct_ecs_entities_t;
pub extern fn ecs_get_entities(world: ?*const ecs_world_t) ecs_entities_t;
pub extern fn ecs_world_get_flags(world: ?*const ecs_world_t) ecs_flags32_t;
pub extern fn ecs_frame_begin(world: ?*ecs_world_t, delta_time: f32) f32;
pub extern fn ecs_frame_end(world: ?*ecs_world_t) void;
pub extern fn ecs_run_post_frame(world: ?*ecs_world_t, action: ecs_fini_action_t, ctx: ?*anyopaque) void;
pub extern fn ecs_quit(world: ?*ecs_world_t) void;
pub extern fn ecs_should_quit(world: ?*const ecs_world_t) bool;
pub extern fn ecs_measure_frame_time(world: ?*ecs_world_t, enable: bool) void;
pub extern fn ecs_measure_system_time(world: ?*ecs_world_t, enable: bool) void;
pub extern fn ecs_set_target_fps(world: ?*ecs_world_t, fps: f32) void;
pub extern fn ecs_set_default_query_flags(world: ?*ecs_world_t, flags: ecs_flags32_t) void;
pub extern fn ecs_readonly_begin(world: ?*ecs_world_t, multi_threaded: bool) bool;
pub extern fn ecs_readonly_end(world: ?*ecs_world_t) void;
pub extern fn ecs_merge(world: ?*ecs_world_t) void;
pub extern fn ecs_defer_begin(world: ?*ecs_world_t) bool;
pub extern fn ecs_is_deferred(world: ?*const ecs_world_t) bool;
pub extern fn ecs_defer_end(world: ?*ecs_world_t) bool;
pub extern fn ecs_defer_suspend(world: ?*ecs_world_t) void;
pub extern fn ecs_defer_resume(world: ?*ecs_world_t) void;
pub extern fn ecs_set_stage_count(world: ?*ecs_world_t, stages: i32) void;
pub extern fn ecs_get_stage_count(world: ?*const ecs_world_t) i32;
pub extern fn ecs_get_stage(world: ?*const ecs_world_t, stage_id: i32) ?*ecs_world_t;
pub extern fn ecs_stage_is_readonly(world: ?*const ecs_world_t) bool;
pub extern fn ecs_stage_new(world: ?*ecs_world_t) ?*ecs_world_t;
pub extern fn ecs_stage_free(stage: ?*ecs_world_t) void;
pub extern fn ecs_stage_get_id(world: ?*const ecs_world_t) i32;
pub extern fn ecs_set_ctx(world: ?*ecs_world_t, ctx: ?*anyopaque, ctx_free: ecs_ctx_free_t) void;
pub extern fn ecs_set_binding_ctx(world: ?*ecs_world_t, ctx: ?*anyopaque, ctx_free: ecs_ctx_free_t) void;
pub extern fn ecs_get_ctx(world: ?*const ecs_world_t) ?*anyopaque;
pub extern fn ecs_get_binding_ctx(world: ?*const ecs_world_t) ?*anyopaque;
pub extern fn ecs_get_build_info() [*c]const ecs_build_info_t;
pub extern fn ecs_get_world_info(world: ?*const ecs_world_t) [*c]const ecs_world_info_t;
pub extern fn ecs_dim(world: ?*ecs_world_t, entity_count: i32) void;
pub extern fn ecs_set_entity_range(world: ?*ecs_world_t, id_start: ecs_entity_t, id_end: ecs_entity_t) void;
pub extern fn ecs_enable_range_check(world: ?*ecs_world_t, enable: bool) bool;
pub extern fn ecs_get_max_id(world: ?*const ecs_world_t) ecs_entity_t;
pub extern fn ecs_run_aperiodic(world: ?*ecs_world_t, flags: ecs_flags32_t) void;
pub extern fn ecs_delete_empty_tables(world: ?*ecs_world_t, id: ecs_id_t, clear_generation: u16, delete_generation: u16, min_id_count: i32, time_budget_seconds: f64) i32;
pub extern fn ecs_get_world(poly: ?*const ecs_poly_t) ?*const ecs_world_t;
pub extern fn ecs_get_entity(poly: ?*const ecs_poly_t) ecs_entity_t;
pub extern fn flecs_poly_is_(object: ?*const ecs_poly_t, @"type": i32) bool;
pub extern fn ecs_make_pair(first: ecs_entity_t, second: ecs_entity_t) ecs_id_t;
pub extern fn ecs_new(world: ?*ecs_world_t) ecs_entity_t;
pub extern fn ecs_new_low_id(world: ?*ecs_world_t) ecs_entity_t;
pub extern fn ecs_new_w_id(world: ?*ecs_world_t, id: ecs_id_t) ecs_entity_t;
pub extern fn ecs_new_w_table(world: ?*ecs_world_t, table: ?*ecs_table_t) ecs_entity_t;
pub extern fn ecs_entity_init(world: ?*ecs_world_t, desc: [*c]const ecs_entity_desc_t) ecs_entity_t;
pub extern fn ecs_bulk_init(world: ?*ecs_world_t, desc: [*c]const ecs_bulk_desc_t) [*c]const ecs_entity_t;
pub extern fn ecs_bulk_new_w_id(world: ?*ecs_world_t, id: ecs_id_t, count: i32) [*c]const ecs_entity_t;
pub extern fn ecs_clone(world: ?*ecs_world_t, dst: ecs_entity_t, src: ecs_entity_t, copy_value: bool) ecs_entity_t;
pub extern fn ecs_delete(world: ?*ecs_world_t, entity: ecs_entity_t) void;
pub extern fn ecs_delete_with(world: ?*ecs_world_t, id: ecs_id_t) void;
pub extern fn ecs_add_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) void;
pub extern fn ecs_remove_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) void;
pub extern fn ecs_auto_override_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) void;
pub extern fn ecs_clear(world: ?*ecs_world_t, entity: ecs_entity_t) void;
pub extern fn ecs_remove_all(world: ?*ecs_world_t, id: ecs_id_t) void;
pub extern fn ecs_set_with(world: ?*ecs_world_t, id: ecs_id_t) ecs_entity_t;
pub extern fn ecs_get_with(world: ?*const ecs_world_t) ecs_id_t;
pub extern fn ecs_enable(world: ?*ecs_world_t, entity: ecs_entity_t, enabled: bool) void;
pub extern fn ecs_enable_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t, enable: bool) void;
pub extern fn ecs_is_enabled_id(world: ?*const ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) bool;
pub extern fn ecs_get_id(world: ?*const ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) ?*const anyopaque;
pub extern fn ecs_get_mut_id(world: ?*const ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) ?*anyopaque;
pub extern fn ecs_ensure_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) ?*anyopaque;
pub extern fn ecs_ensure_modified_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) ?*anyopaque;
pub extern fn ecs_ref_init_id(world: ?*const ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) ecs_ref_t;
pub extern fn ecs_ref_get_id(world: ?*const ecs_world_t, ref: [*c]ecs_ref_t, id: ecs_id_t) ?*anyopaque;
pub extern fn ecs_ref_update(world: ?*const ecs_world_t, ref: [*c]ecs_ref_t) void;
pub extern fn ecs_record_find(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]ecs_record_t;
pub extern fn ecs_write_begin(world: ?*ecs_world_t, entity: ecs_entity_t) [*c]ecs_record_t;
pub extern fn ecs_write_end(record: [*c]ecs_record_t) void;
pub extern fn ecs_read_begin(world: ?*ecs_world_t, entity: ecs_entity_t) [*c]const ecs_record_t;
pub extern fn ecs_read_end(record: [*c]const ecs_record_t) void;
pub extern fn ecs_record_get_entity(record: [*c]const ecs_record_t) ecs_entity_t;
pub extern fn ecs_record_get_id(world: ?*const ecs_world_t, record: [*c]const ecs_record_t, id: ecs_id_t) ?*const anyopaque;
pub extern fn ecs_record_ensure_id(world: ?*ecs_world_t, record: [*c]ecs_record_t, id: ecs_id_t) ?*anyopaque;
pub extern fn ecs_record_has_id(world: ?*ecs_world_t, record: [*c]const ecs_record_t, id: ecs_id_t) bool;
pub extern fn ecs_record_get_by_column(record: [*c]const ecs_record_t, column: i32, size: usize) ?*anyopaque;
pub extern fn ecs_emplace_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t, is_new: [*c]bool) ?*anyopaque;
pub extern fn ecs_modified_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) void;
pub extern fn ecs_set_id(world: ?*ecs_world_t, entity: ecs_entity_t, id: ecs_id_t, size: usize, ptr: ?*const anyopaque) void;
pub extern fn ecs_is_valid(world: ?*const ecs_world_t, e: ecs_entity_t) bool;
pub extern fn ecs_is_alive(world: ?*const ecs_world_t, e: ecs_entity_t) bool;
pub extern fn ecs_strip_generation(e: ecs_entity_t) ecs_id_t;
pub extern fn ecs_get_alive(world: ?*const ecs_world_t, e: ecs_entity_t) ecs_entity_t;
pub extern fn ecs_make_alive(world: ?*ecs_world_t, entity: ecs_entity_t) void;
pub extern fn ecs_make_alive_id(world: ?*ecs_world_t, id: ecs_id_t) void;
pub extern fn ecs_exists(world: ?*const ecs_world_t, entity: ecs_entity_t) bool;
pub extern fn ecs_set_version(world: ?*ecs_world_t, entity: ecs_entity_t) void;
pub extern fn ecs_get_type(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const ecs_type_t;
pub extern fn ecs_get_table(world: ?*const ecs_world_t, entity: ecs_entity_t) ?*ecs_table_t;
pub extern fn ecs_type_str(world: ?*const ecs_world_t, @"type": [*c]const ecs_type_t) [*c]u8;
pub extern fn ecs_table_str(world: ?*const ecs_world_t, table: ?*const ecs_table_t) [*c]u8;
pub extern fn ecs_entity_str(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]u8;
pub extern fn ecs_has_id(world: ?*const ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) bool;
pub extern fn ecs_owns_id(world: ?*const ecs_world_t, entity: ecs_entity_t, id: ecs_id_t) bool;
pub extern fn ecs_get_target(world: ?*const ecs_world_t, entity: ecs_entity_t, rel: ecs_entity_t, index: i32) ecs_entity_t;
pub extern fn ecs_get_parent(world: ?*const ecs_world_t, entity: ecs_entity_t) ecs_entity_t;
pub extern fn ecs_get_target_for_id(world: ?*const ecs_world_t, entity: ecs_entity_t, rel: ecs_entity_t, id: ecs_id_t) ecs_entity_t;
pub extern fn ecs_get_depth(world: ?*const ecs_world_t, entity: ecs_entity_t, rel: ecs_entity_t) i32;
pub extern fn ecs_count_id(world: ?*const ecs_world_t, entity: ecs_id_t) i32;
pub extern fn ecs_get_name(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn ecs_get_symbol(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn ecs_set_name(world: ?*ecs_world_t, entity: ecs_entity_t, name: [*c]const u8) ecs_entity_t;
pub extern fn ecs_set_symbol(world: ?*ecs_world_t, entity: ecs_entity_t, symbol: [*c]const u8) ecs_entity_t;
pub extern fn ecs_set_alias(world: ?*ecs_world_t, entity: ecs_entity_t, alias: [*c]const u8) void;
pub extern fn ecs_lookup(world: ?*const ecs_world_t, path: [*c]const u8) ecs_entity_t;
pub extern fn ecs_lookup_child(world: ?*const ecs_world_t, parent: ecs_entity_t, name: [*c]const u8) ecs_entity_t;
pub extern fn ecs_lookup_path_w_sep(world: ?*const ecs_world_t, parent: ecs_entity_t, path: [*c]const u8, sep: [*c]const u8, prefix: [*c]const u8, recursive: bool) ecs_entity_t;
pub extern fn ecs_lookup_symbol(world: ?*const ecs_world_t, symbol: [*c]const u8, lookup_as_path: bool, recursive: bool) ecs_entity_t;
pub extern fn ecs_get_path_w_sep(world: ?*const ecs_world_t, parent: ecs_entity_t, child: ecs_entity_t, sep: [*c]const u8, prefix: [*c]const u8) [*c]u8;
pub extern fn ecs_get_path_w_sep_buf(world: ?*const ecs_world_t, parent: ecs_entity_t, child: ecs_entity_t, sep: [*c]const u8, prefix: [*c]const u8, buf: [*c]ecs_strbuf_t) void;
pub extern fn ecs_new_from_path_w_sep(world: ?*ecs_world_t, parent: ecs_entity_t, path: [*c]const u8, sep: [*c]const u8, prefix: [*c]const u8) ecs_entity_t;
pub extern fn ecs_add_path_w_sep(world: ?*ecs_world_t, entity: ecs_entity_t, parent: ecs_entity_t, path: [*c]const u8, sep: [*c]const u8, prefix: [*c]const u8) ecs_entity_t;
pub extern fn ecs_set_scope(world: ?*ecs_world_t, scope: ecs_entity_t) ecs_entity_t;
pub extern fn ecs_get_scope(world: ?*const ecs_world_t) ecs_entity_t;
pub extern fn ecs_set_name_prefix(world: ?*ecs_world_t, prefix: [*c]const u8) [*c]const u8;
pub extern fn ecs_set_lookup_path(world: ?*ecs_world_t, lookup_path: [*c]const ecs_entity_t) [*c]ecs_entity_t;
pub extern fn ecs_get_lookup_path(world: ?*const ecs_world_t) [*c]ecs_entity_t;
pub extern fn ecs_component_init(world: ?*ecs_world_t, desc: [*c]const ecs_component_desc_t) ecs_entity_t;
pub extern fn ecs_get_type_info(world: ?*const ecs_world_t, id: ecs_id_t) [*c]const ecs_type_info_t;
pub extern fn ecs_set_hooks_id(world: ?*ecs_world_t, id: ecs_entity_t, hooks: [*c]const ecs_type_hooks_t) void;
pub extern fn ecs_get_hooks_id(world: ?*const ecs_world_t, id: ecs_entity_t) [*c]const ecs_type_hooks_t;
pub extern fn ecs_id_is_tag(world: ?*const ecs_world_t, id: ecs_id_t) bool;
pub extern fn ecs_id_in_use(world: ?*const ecs_world_t, id: ecs_id_t) bool;
pub extern fn ecs_get_typeid(world: ?*const ecs_world_t, id: ecs_id_t) ecs_entity_t;
pub extern fn ecs_id_match(id: ecs_id_t, pattern: ecs_id_t) bool;
pub extern fn ecs_id_is_pair(id: ecs_id_t) bool;
pub extern fn ecs_id_is_wildcard(id: ecs_id_t) bool;
pub extern fn ecs_id_is_valid(world: ?*const ecs_world_t, id: ecs_id_t) bool;
pub extern fn ecs_id_get_flags(world: ?*const ecs_world_t, id: ecs_id_t) ecs_flags32_t;
pub extern fn ecs_id_flag_str(id_flags: ecs_id_t) [*c]const u8;
pub extern fn ecs_id_str(world: ?*const ecs_world_t, id: ecs_id_t) [*c]u8;
pub extern fn ecs_id_str_buf(world: ?*const ecs_world_t, id: ecs_id_t, buf: [*c]ecs_strbuf_t) void;
pub extern fn ecs_id_from_str(world: ?*const ecs_world_t, expr: [*c]const u8) ecs_id_t;
pub extern fn ecs_term_ref_is_set(id: [*c]const ecs_term_ref_t) bool;
pub extern fn ecs_term_is_initialized(term: [*c]const ecs_term_t) bool;
pub extern fn ecs_term_match_this(term: [*c]const ecs_term_t) bool;
pub extern fn ecs_term_match_0(term: [*c]const ecs_term_t) bool;
pub extern fn ecs_term_str(world: ?*const ecs_world_t, term: [*c]const ecs_term_t) [*c]u8;
pub extern fn ecs_query_str(query: [*c]const ecs_query_t) [*c]u8;
pub extern fn ecs_each_id(world: ?*const ecs_world_t, id: ecs_id_t) ecs_iter_t;
pub extern fn ecs_each_next(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_children(world: ?*const ecs_world_t, parent: ecs_entity_t) ecs_iter_t;
pub extern fn ecs_children_next(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_query_init(world: ?*ecs_world_t, desc: [*c]const ecs_query_desc_t) [*c]ecs_query_t;
pub extern fn ecs_query_fini(query: [*c]ecs_query_t) void;
pub extern fn ecs_query_find_var(query: [*c]const ecs_query_t, name: [*c]const u8) i32;
pub extern fn ecs_query_var_name(query: [*c]const ecs_query_t, var_id: i32) [*c]const u8;
pub extern fn ecs_query_var_is_entity(query: [*c]const ecs_query_t, var_id: i32) bool;
pub extern fn ecs_query_iter(world: ?*const ecs_world_t, query: [*c]const ecs_query_t) ecs_iter_t;
pub extern fn ecs_query_next(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_query_has(query: [*c]ecs_query_t, entity: ecs_entity_t, it: [*c]ecs_iter_t) bool;
pub extern fn ecs_query_has_table(query: [*c]ecs_query_t, table: ?*ecs_table_t, it: [*c]ecs_iter_t) bool;
pub extern fn ecs_query_has_range(query: [*c]ecs_query_t, range: [*c]ecs_table_range_t, it: [*c]ecs_iter_t) bool;
pub extern fn ecs_query_match_count(query: [*c]const ecs_query_t) i32;
pub extern fn ecs_query_plan(query: [*c]const ecs_query_t) [*c]u8;
pub extern fn ecs_query_plan_w_profile(query: [*c]const ecs_query_t, it: [*c]const ecs_iter_t) [*c]u8;
pub extern fn ecs_query_args_parse(query: [*c]ecs_query_t, it: [*c]ecs_iter_t, expr: [*c]const u8) [*c]const u8;
pub extern fn ecs_query_changed(query: [*c]ecs_query_t) bool;
pub extern fn ecs_iter_skip(it: [*c]ecs_iter_t) void;
pub extern fn ecs_iter_set_group(it: [*c]ecs_iter_t, group_id: u64) void;
pub extern fn ecs_query_get_group_ctx(query: [*c]const ecs_query_t, group_id: u64) ?*anyopaque;
pub extern fn ecs_query_get_group_info(query: [*c]const ecs_query_t, group_id: u64) [*c]const ecs_query_group_info_t;
pub const struct_ecs_query_count_t = extern struct {
    results: i32 = @import("std").mem.zeroes(i32),
    entities: i32 = @import("std").mem.zeroes(i32),
    tables: i32 = @import("std").mem.zeroes(i32),
    empty_tables: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_query_count_t = struct_ecs_query_count_t;
pub extern fn ecs_query_count(query: [*c]const ecs_query_t) ecs_query_count_t;
pub extern fn ecs_query_is_true(query: [*c]const ecs_query_t) bool;
pub extern fn ecs_emit(world: ?*ecs_world_t, desc: [*c]ecs_event_desc_t) void;
pub extern fn ecs_enqueue(world: ?*ecs_world_t, desc: [*c]ecs_event_desc_t) void;
pub extern fn ecs_observer_init(world: ?*ecs_world_t, desc: [*c]const ecs_observer_desc_t) ecs_entity_t;
pub extern fn ecs_observer_get(world: ?*const ecs_world_t, observer: ecs_entity_t) [*c]const ecs_observer_t;
pub extern fn ecs_iter_next(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_iter_fini(it: [*c]ecs_iter_t) void;
pub extern fn ecs_iter_count(it: [*c]ecs_iter_t) i32;
pub extern fn ecs_iter_is_true(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_iter_first(it: [*c]ecs_iter_t) ecs_entity_t;
pub extern fn ecs_iter_set_var(it: [*c]ecs_iter_t, var_id: i32, entity: ecs_entity_t) void;
pub extern fn ecs_iter_set_var_as_table(it: [*c]ecs_iter_t, var_id: i32, table: ?*const ecs_table_t) void;
pub extern fn ecs_iter_set_var_as_range(it: [*c]ecs_iter_t, var_id: i32, range: [*c]const ecs_table_range_t) void;
pub extern fn ecs_iter_get_var(it: [*c]ecs_iter_t, var_id: i32) ecs_entity_t;
pub extern fn ecs_iter_get_var_as_table(it: [*c]ecs_iter_t, var_id: i32) ?*ecs_table_t;
pub extern fn ecs_iter_get_var_as_range(it: [*c]ecs_iter_t, var_id: i32) ecs_table_range_t;
pub extern fn ecs_iter_var_is_constrained(it: [*c]ecs_iter_t, var_id: i32) bool;
pub extern fn ecs_iter_changed(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_iter_str(it: [*c]const ecs_iter_t) [*c]u8;
pub extern fn ecs_page_iter(it: [*c]const ecs_iter_t, offset: i32, limit: i32) ecs_iter_t;
pub extern fn ecs_page_next(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_worker_iter(it: [*c]const ecs_iter_t, index: i32, count: i32) ecs_iter_t;
pub extern fn ecs_worker_next(it: [*c]ecs_iter_t) bool;
pub extern fn ecs_field_w_size(it: [*c]const ecs_iter_t, size: usize, index: i8) ?*anyopaque;
pub extern fn ecs_field_at_w_size(it: [*c]const ecs_iter_t, size: usize, index: i8, row: i32) ?*anyopaque;
pub extern fn ecs_field_is_readonly(it: [*c]const ecs_iter_t, index: i8) bool;
pub extern fn ecs_field_is_writeonly(it: [*c]const ecs_iter_t, index: i8) bool;
pub extern fn ecs_field_is_set(it: [*c]const ecs_iter_t, index: i8) bool;
pub extern fn ecs_field_id(it: [*c]const ecs_iter_t, index: i8) ecs_id_t;
pub extern fn ecs_field_column(it: [*c]const ecs_iter_t, index: i8) i32;
pub extern fn ecs_field_src(it: [*c]const ecs_iter_t, index: i8) ecs_entity_t;
pub extern fn ecs_field_size(it: [*c]const ecs_iter_t, index: i8) usize;
pub extern fn ecs_field_is_self(it: [*c]const ecs_iter_t, index: i8) bool;
pub extern fn ecs_table_get_type(table: ?*const ecs_table_t) [*c]const ecs_type_t;
pub extern fn ecs_table_get_type_index(world: ?*const ecs_world_t, table: ?*const ecs_table_t, id: ecs_id_t) i32;
pub extern fn ecs_table_get_column_index(world: ?*const ecs_world_t, table: ?*const ecs_table_t, id: ecs_id_t) i32;
pub extern fn ecs_table_column_count(table: ?*const ecs_table_t) i32;
pub extern fn ecs_table_type_to_column_index(table: ?*const ecs_table_t, index: i32) i32;
pub extern fn ecs_table_column_to_type_index(table: ?*const ecs_table_t, index: i32) i32;
pub extern fn ecs_table_get_column(table: ?*const ecs_table_t, index: i32, offset: i32) ?*anyopaque;
pub extern fn ecs_table_get_id(world: ?*const ecs_world_t, table: ?*const ecs_table_t, id: ecs_id_t, offset: i32) ?*anyopaque;
pub extern fn ecs_table_get_column_size(table: ?*const ecs_table_t, index: i32) usize;
pub extern fn ecs_table_count(table: ?*const ecs_table_t) i32;
pub extern fn ecs_table_size(table: ?*const ecs_table_t) i32;
pub extern fn ecs_table_entities(table: ?*const ecs_table_t) [*c]const ecs_entity_t;
pub extern fn ecs_table_has_id(world: ?*const ecs_world_t, table: ?*const ecs_table_t, id: ecs_id_t) bool;
pub extern fn ecs_table_get_depth(world: ?*const ecs_world_t, table: ?*const ecs_table_t, rel: ecs_entity_t) i32;
pub extern fn ecs_table_add_id(world: ?*ecs_world_t, table: ?*ecs_table_t, id: ecs_id_t) ?*ecs_table_t;
pub extern fn ecs_table_find(world: ?*ecs_world_t, ids: [*c]const ecs_id_t, id_count: i32) ?*ecs_table_t;
pub extern fn ecs_table_remove_id(world: ?*ecs_world_t, table: ?*ecs_table_t, id: ecs_id_t) ?*ecs_table_t;
pub extern fn ecs_table_lock(world: ?*ecs_world_t, table: ?*ecs_table_t) void;
pub extern fn ecs_table_unlock(world: ?*ecs_world_t, table: ?*ecs_table_t) void;
pub extern fn ecs_table_has_flags(table: ?*ecs_table_t, flags: ecs_flags32_t) bool;
pub extern fn ecs_table_swap_rows(world: ?*ecs_world_t, table: ?*ecs_table_t, row_1: i32, row_2: i32) void;
pub extern fn ecs_commit(world: ?*ecs_world_t, entity: ecs_entity_t, record: [*c]ecs_record_t, table: ?*ecs_table_t, added: [*c]const ecs_type_t, removed: [*c]const ecs_type_t) bool;
pub extern fn ecs_search(world: ?*const ecs_world_t, table: ?*const ecs_table_t, id: ecs_id_t, id_out: [*c]ecs_id_t) i32;
pub extern fn ecs_search_offset(world: ?*const ecs_world_t, table: ?*const ecs_table_t, offset: i32, id: ecs_id_t, id_out: [*c]ecs_id_t) i32;
pub extern fn ecs_search_relation(world: ?*const ecs_world_t, table: ?*const ecs_table_t, offset: i32, id: ecs_id_t, rel: ecs_entity_t, flags: ecs_flags64_t, subject_out: [*c]ecs_entity_t, id_out: [*c]ecs_id_t, tr_out: [*c][*c]struct_ecs_table_record_t) i32;
pub extern fn ecs_value_init(world: ?*const ecs_world_t, @"type": ecs_entity_t, ptr: ?*anyopaque) c_int;
pub extern fn ecs_value_init_w_type_info(world: ?*const ecs_world_t, ti: [*c]const ecs_type_info_t, ptr: ?*anyopaque) c_int;
pub extern fn ecs_value_new(world: ?*ecs_world_t, @"type": ecs_entity_t) ?*anyopaque;
pub extern fn ecs_value_new_w_type_info(world: ?*ecs_world_t, ti: [*c]const ecs_type_info_t) ?*anyopaque;
pub extern fn ecs_value_fini_w_type_info(world: ?*const ecs_world_t, ti: [*c]const ecs_type_info_t, ptr: ?*anyopaque) c_int;
pub extern fn ecs_value_fini(world: ?*const ecs_world_t, @"type": ecs_entity_t, ptr: ?*anyopaque) c_int;
pub extern fn ecs_value_free(world: ?*ecs_world_t, @"type": ecs_entity_t, ptr: ?*anyopaque) c_int;
pub extern fn ecs_value_copy_w_type_info(world: ?*const ecs_world_t, ti: [*c]const ecs_type_info_t, dst: ?*anyopaque, src: ?*const anyopaque) c_int;
pub extern fn ecs_value_copy(world: ?*const ecs_world_t, @"type": ecs_entity_t, dst: ?*anyopaque, src: ?*const anyopaque) c_int;
pub extern fn ecs_value_move_w_type_info(world: ?*const ecs_world_t, ti: [*c]const ecs_type_info_t, dst: ?*anyopaque, src: ?*anyopaque) c_int;
pub extern fn ecs_value_move(world: ?*const ecs_world_t, @"type": ecs_entity_t, dst: ?*anyopaque, src: ?*anyopaque) c_int;
pub extern fn ecs_value_move_ctor_w_type_info(world: ?*const ecs_world_t, ti: [*c]const ecs_type_info_t, dst: ?*anyopaque, src: ?*anyopaque) c_int;
pub extern fn ecs_value_move_ctor(world: ?*const ecs_world_t, @"type": ecs_entity_t, dst: ?*anyopaque, src: ?*anyopaque) c_int;
pub extern fn ecs_deprecated_(file: [*c]const u8, line: i32, msg: [*c]const u8) void;
pub extern fn ecs_log_push_(level: i32) void;
pub extern fn ecs_log_pop_(level: i32) void;
pub extern fn ecs_should_log(level: i32) bool;
pub extern fn ecs_strerror(error_code: i32) [*c]const u8;
pub extern fn ecs_print_(level: i32, file: [*c]const u8, line: i32, fmt: [*c]const u8, ...) void;
pub extern fn ecs_printv_(level: c_int, file: [*c]const u8, line: i32, fmt: [*c]const u8, args: va_list) void;
pub extern fn ecs_log_(level: i32, file: [*c]const u8, line: i32, fmt: [*c]const u8, ...) void;
pub extern fn ecs_logv_(level: c_int, file: [*c]const u8, line: i32, fmt: [*c]const u8, args: va_list) void;
pub extern fn ecs_abort_(error_code: i32, file: [*c]const u8, line: i32, fmt: [*c]const u8, ...) void;
pub extern fn ecs_assert_log_(error_code: i32, condition_str: [*c]const u8, file: [*c]const u8, line: i32, fmt: [*c]const u8, ...) void;
pub extern fn ecs_parser_error_(name: [*c]const u8, expr: [*c]const u8, column: i64, fmt: [*c]const u8, ...) void;
pub extern fn ecs_parser_errorv_(name: [*c]const u8, expr: [*c]const u8, column: i64, fmt: [*c]const u8, args: va_list) void;
pub extern fn ecs_log_set_level(level: c_int) c_int;
pub extern fn ecs_log_get_level() c_int;
pub extern fn ecs_log_enable_colors(enabled: bool) bool;
pub extern fn ecs_log_enable_timestamp(enabled: bool) bool;
pub extern fn ecs_log_enable_timedelta(enabled: bool) bool;
pub extern fn ecs_log_last_error() c_int;
pub const ecs_app_init_action_t = ?*const fn (?*ecs_world_t) callconv(.C) c_int;
pub const struct_ecs_app_desc_t = extern struct {
    target_fps: f32 = @import("std").mem.zeroes(f32),
    delta_time: f32 = @import("std").mem.zeroes(f32),
    threads: i32 = @import("std").mem.zeroes(i32),
    frames: i32 = @import("std").mem.zeroes(i32),
    enable_rest: bool = @import("std").mem.zeroes(bool),
    enable_stats: bool = @import("std").mem.zeroes(bool),
    port: u16 = @import("std").mem.zeroes(u16),
    init: ecs_app_init_action_t = @import("std").mem.zeroes(ecs_app_init_action_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
};
pub const ecs_app_desc_t = struct_ecs_app_desc_t;
pub const ecs_app_run_action_t = ?*const fn (?*ecs_world_t, [*c]ecs_app_desc_t) callconv(.C) c_int;
pub const ecs_app_frame_action_t = ?*const fn (?*ecs_world_t, [*c]const ecs_app_desc_t) callconv(.C) c_int;
pub extern fn ecs_app_run(world: ?*ecs_world_t, desc: [*c]ecs_app_desc_t) c_int;
pub extern fn ecs_app_run_frame(world: ?*ecs_world_t, desc: [*c]const ecs_app_desc_t) c_int;
pub extern fn ecs_app_set_run_action(callback: ecs_app_run_action_t) c_int;
pub extern fn ecs_app_set_frame_action(callback: ecs_app_frame_action_t) c_int;
pub const struct_ecs_http_server_t = opaque {};
pub const ecs_http_server_t = struct_ecs_http_server_t;
pub const ecs_http_connection_t = extern struct {
    id: u64 = @import("std").mem.zeroes(u64),
    server: ?*ecs_http_server_t = @import("std").mem.zeroes(?*ecs_http_server_t),
    host: [128]u8 = @import("std").mem.zeroes([128]u8),
    port: [16]u8 = @import("std").mem.zeroes([16]u8),
};
pub const ecs_http_key_value_t = extern struct {
    key: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    value: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const EcsHttpGet: c_int = 0;
pub const EcsHttpPost: c_int = 1;
pub const EcsHttpPut: c_int = 2;
pub const EcsHttpDelete: c_int = 3;
pub const EcsHttpOptions: c_int = 4;
pub const EcsHttpMethodUnsupported: c_int = 5;
pub const ecs_http_method_t = c_uint;
pub const ecs_http_request_t = extern struct {
    id: u64 = @import("std").mem.zeroes(u64),
    method: ecs_http_method_t = @import("std").mem.zeroes(ecs_http_method_t),
    path: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    body: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    headers: [32]ecs_http_key_value_t = @import("std").mem.zeroes([32]ecs_http_key_value_t),
    params: [32]ecs_http_key_value_t = @import("std").mem.zeroes([32]ecs_http_key_value_t),
    header_count: i32 = @import("std").mem.zeroes(i32),
    param_count: i32 = @import("std").mem.zeroes(i32),
    conn: [*c]ecs_http_connection_t = @import("std").mem.zeroes([*c]ecs_http_connection_t),
};
pub const ecs_http_reply_t = extern struct {
    code: c_int = @import("std").mem.zeroes(c_int),
    body: ecs_strbuf_t = @import("std").mem.zeroes(ecs_strbuf_t),
    status: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    content_type: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    headers: ecs_strbuf_t = @import("std").mem.zeroes(ecs_strbuf_t),
};
pub extern var ecs_http_request_received_count: i64;
pub extern var ecs_http_request_invalid_count: i64;
pub extern var ecs_http_request_handled_ok_count: i64;
pub extern var ecs_http_request_handled_error_count: i64;
pub extern var ecs_http_request_not_handled_count: i64;
pub extern var ecs_http_request_preflight_count: i64;
pub extern var ecs_http_send_ok_count: i64;
pub extern var ecs_http_send_error_count: i64;
pub extern var ecs_http_busy_count: i64;
pub const ecs_http_reply_action_t = ?*const fn ([*c]const ecs_http_request_t, [*c]ecs_http_reply_t, ?*anyopaque) callconv(.C) bool;
pub const ecs_http_server_desc_t = extern struct {
    callback: ecs_http_reply_action_t = @import("std").mem.zeroes(ecs_http_reply_action_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    port: u16 = @import("std").mem.zeroes(u16),
    ipaddr: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    send_queue_wait_ms: i32 = @import("std").mem.zeroes(i32),
    cache_timeout: f64 = @import("std").mem.zeroes(f64),
    cache_purge_timeout: f64 = @import("std").mem.zeroes(f64),
};
pub extern fn ecs_http_server_init(desc: [*c]const ecs_http_server_desc_t) ?*ecs_http_server_t;
pub extern fn ecs_http_server_fini(server: ?*ecs_http_server_t) void;
pub extern fn ecs_http_server_start(server: ?*ecs_http_server_t) c_int;
pub extern fn ecs_http_server_dequeue(server: ?*ecs_http_server_t, delta_time: f32) void;
pub extern fn ecs_http_server_stop(server: ?*ecs_http_server_t) void;
pub extern fn ecs_http_server_http_request(srv: ?*ecs_http_server_t, req: [*c]const u8, len: ecs_size_t, reply_out: [*c]ecs_http_reply_t) c_int;
pub extern fn ecs_http_server_request(srv: ?*ecs_http_server_t, method: [*c]const u8, req: [*c]const u8, reply_out: [*c]ecs_http_reply_t) c_int;
pub extern fn ecs_http_server_ctx(srv: ?*ecs_http_server_t) ?*anyopaque;
pub extern fn ecs_http_get_header(req: [*c]const ecs_http_request_t, name: [*c]const u8) [*c]const u8;
pub extern fn ecs_http_get_param(req: [*c]const ecs_http_request_t, name: [*c]const u8) [*c]const u8;
pub extern const FLECS_IDEcsRestID_: ecs_entity_t;
pub const EcsRest = extern struct {
    port: u16 = @import("std").mem.zeroes(u16),
    ipaddr: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    impl: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
};
pub extern fn ecs_rest_server_init(world: ?*ecs_world_t, desc: [*c]const ecs_http_server_desc_t) ?*ecs_http_server_t;
pub extern fn ecs_rest_server_fini(srv: ?*ecs_http_server_t) void;
pub extern fn FlecsRestImport(world: ?*ecs_world_t) void;
pub const struct_EcsTimer = extern struct {
    timeout: f32 = @import("std").mem.zeroes(f32),
    time: f32 = @import("std").mem.zeroes(f32),
    overshoot: f32 = @import("std").mem.zeroes(f32),
    fired_count: i32 = @import("std").mem.zeroes(i32),
    active: bool = @import("std").mem.zeroes(bool),
    single_shot: bool = @import("std").mem.zeroes(bool),
};
pub const EcsTimer = struct_EcsTimer;
pub const struct_EcsRateFilter = extern struct {
    src: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    rate: i32 = @import("std").mem.zeroes(i32),
    tick_count: i32 = @import("std").mem.zeroes(i32),
    time_elapsed: f32 = @import("std").mem.zeroes(f32),
};
pub const EcsRateFilter = struct_EcsRateFilter;
pub extern fn ecs_set_timeout(world: ?*ecs_world_t, tick_source: ecs_entity_t, timeout: f32) ecs_entity_t;
pub extern fn ecs_get_timeout(world: ?*const ecs_world_t, tick_source: ecs_entity_t) f32;
pub extern fn ecs_set_interval(world: ?*ecs_world_t, tick_source: ecs_entity_t, interval: f32) ecs_entity_t;
pub extern fn ecs_get_interval(world: ?*const ecs_world_t, tick_source: ecs_entity_t) f32;
pub extern fn ecs_start_timer(world: ?*ecs_world_t, tick_source: ecs_entity_t) void;
pub extern fn ecs_stop_timer(world: ?*ecs_world_t, tick_source: ecs_entity_t) void;
pub extern fn ecs_reset_timer(world: ?*ecs_world_t, tick_source: ecs_entity_t) void;
pub extern fn ecs_randomize_timers(world: ?*ecs_world_t) void;
pub extern fn ecs_set_rate(world: ?*ecs_world_t, tick_source: ecs_entity_t, rate: i32, source: ecs_entity_t) ecs_entity_t;
pub extern fn ecs_set_tick_source(world: ?*ecs_world_t, system: ecs_entity_t, tick_source: ecs_entity_t) void;
pub extern fn FlecsTimerImport(world: ?*ecs_world_t) void;
pub const struct_ecs_pipeline_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    query: ecs_query_desc_t = @import("std").mem.zeroes(ecs_query_desc_t),
};
pub const ecs_pipeline_desc_t = struct_ecs_pipeline_desc_t;
pub extern fn ecs_pipeline_init(world: ?*ecs_world_t, desc: [*c]const ecs_pipeline_desc_t) ecs_entity_t;
pub extern fn ecs_set_pipeline(world: ?*ecs_world_t, pipeline: ecs_entity_t) void;
pub extern fn ecs_get_pipeline(world: ?*const ecs_world_t) ecs_entity_t;
pub extern fn ecs_progress(world: ?*ecs_world_t, delta_time: f32) bool;
pub extern fn ecs_set_time_scale(world: ?*ecs_world_t, scale: f32) void;
pub extern fn ecs_reset_clock(world: ?*ecs_world_t) void;
pub extern fn ecs_run_pipeline(world: ?*ecs_world_t, pipeline: ecs_entity_t, delta_time: f32) void;
pub extern fn ecs_set_threads(world: ?*ecs_world_t, threads: i32) void;
pub extern fn ecs_set_task_threads(world: ?*ecs_world_t, task_threads: i32) void;
pub extern fn ecs_using_task_threads(world: ?*ecs_world_t) bool;
pub extern fn FlecsPipelineImport(world: ?*ecs_world_t) void;
pub const struct_EcsTickSource = extern struct {
    tick: bool = @import("std").mem.zeroes(bool),
    time_elapsed: f32 = @import("std").mem.zeroes(f32),
};
pub const EcsTickSource = struct_EcsTickSource;
pub const struct_ecs_system_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    query: ecs_query_desc_t = @import("std").mem.zeroes(ecs_query_desc_t),
    callback: ?*const fn (*ecs_iter_t) callconv(.C) void = @import("std").mem.zeroes(?*const fn (*ecs_iter_t) callconv(.C) void),
    run: ecs_run_action_t = @import("std").mem.zeroes(ecs_run_action_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    callback_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    callback_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    run_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    run_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    interval: f32 = @import("std").mem.zeroes(f32),
    rate: i32 = @import("std").mem.zeroes(i32),
    tick_source: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    multi_threaded: bool = @import("std").mem.zeroes(bool),
    immediate: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_system_desc_t = struct_ecs_system_desc_t;
pub extern fn ecs_system_init(world: ?*ecs_world_t, desc: [*c]const ecs_system_desc_t) ecs_entity_t;
pub const struct_ecs_system_t = extern struct {
    hdr: ecs_header_t = @import("std").mem.zeroes(ecs_header_t),
    run: ecs_run_action_t = @import("std").mem.zeroes(ecs_run_action_t),
    action: ecs_iter_action_t = @import("std").mem.zeroes(ecs_iter_action_t),
    query: [*c]ecs_query_t = @import("std").mem.zeroes([*c]ecs_query_t),
    query_entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    tick_source: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    multi_threaded: bool = @import("std").mem.zeroes(bool),
    immediate: bool = @import("std").mem.zeroes(bool),
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    callback_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    run_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    callback_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    run_ctx_free: ecs_ctx_free_t = @import("std").mem.zeroes(ecs_ctx_free_t),
    time_spent: f32 = @import("std").mem.zeroes(f32),
    time_passed: f32 = @import("std").mem.zeroes(f32),
    last_frame: i64 = @import("std").mem.zeroes(i64),
    world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    dtor: flecs_poly_dtor_t = @import("std").mem.zeroes(flecs_poly_dtor_t),
};
pub const ecs_system_t = struct_ecs_system_t;
pub extern fn ecs_system_get(world: ?*const ecs_world_t, system: ecs_entity_t) [*c]const ecs_system_t;
pub extern fn ecs_run(world: ?*ecs_world_t, system: ecs_entity_t, delta_time: f32, param: ?*anyopaque) ecs_entity_t;
pub extern fn ecs_run_worker(world: ?*ecs_world_t, system: ecs_entity_t, stage_current: i32, stage_count: i32, delta_time: f32, param: ?*anyopaque) ecs_entity_t;
pub extern fn FlecsSystemImport(world: ?*ecs_world_t) void;
pub const struct_ecs_gauge_t = extern struct {
    avg: [60]f32 = @import("std").mem.zeroes([60]f32),
    min: [60]f32 = @import("std").mem.zeroes([60]f32),
    max: [60]f32 = @import("std").mem.zeroes([60]f32),
};
pub const ecs_gauge_t = struct_ecs_gauge_t;
pub const struct_ecs_counter_t = extern struct {
    rate: ecs_gauge_t = @import("std").mem.zeroes(ecs_gauge_t),
    value: [60]f64 = @import("std").mem.zeroes([60]f64),
};
pub const ecs_counter_t = struct_ecs_counter_t;
pub const union_ecs_metric_t = extern union {
    gauge: ecs_gauge_t,
    counter: ecs_counter_t,
};
pub const ecs_metric_t = union_ecs_metric_t;
const struct_unnamed_8 = extern struct {
    count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    not_alive_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_9 = extern struct {
    tag_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    component_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    pair_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    type_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    create_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    delete_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_10 = extern struct {
    count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    empty_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    create_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    delete_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_11 = extern struct {
    query_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    observer_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    system_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_12 = extern struct {
    add_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    remove_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    delete_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    clear_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    set_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    ensure_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    modified_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    other_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    discard_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    batched_entity_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    batched_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_13 = extern struct {
    frame_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    merge_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    rematch_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    pipeline_build_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    systems_ran: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    observers_ran: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    event_emit_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_14 = extern struct {
    world_time_raw: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    world_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    frame_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    system_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    emit_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    merge_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    rematch_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    fps: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    delta_time: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_15 = extern struct {
    alloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    realloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    free_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    outstanding_alloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    block_alloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    block_free_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    block_outstanding_alloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    stack_alloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    stack_free_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    stack_outstanding_alloc_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
const struct_unnamed_16 = extern struct {
    request_received_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    request_invalid_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    request_handled_ok_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    request_handled_error_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    request_not_handled_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    request_preflight_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    send_ok_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    send_error_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    busy_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
};
pub const struct_ecs_world_stats_t = extern struct {
    first_: i64 = @import("std").mem.zeroes(i64),
    entities: struct_unnamed_8 = @import("std").mem.zeroes(struct_unnamed_8),
    components: struct_unnamed_9 = @import("std").mem.zeroes(struct_unnamed_9),
    tables: struct_unnamed_10 = @import("std").mem.zeroes(struct_unnamed_10),
    queries: struct_unnamed_11 = @import("std").mem.zeroes(struct_unnamed_11),
    commands: struct_unnamed_12 = @import("std").mem.zeroes(struct_unnamed_12),
    frame: struct_unnamed_13 = @import("std").mem.zeroes(struct_unnamed_13),
    performance: struct_unnamed_14 = @import("std").mem.zeroes(struct_unnamed_14),
    memory: struct_unnamed_15 = @import("std").mem.zeroes(struct_unnamed_15),
    http: struct_unnamed_16 = @import("std").mem.zeroes(struct_unnamed_16),
    last_: i64 = @import("std").mem.zeroes(i64),
    t: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_world_stats_t = struct_ecs_world_stats_t;
pub const struct_ecs_query_stats_t = extern struct {
    first_: i64 = @import("std").mem.zeroes(i64),
    result_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    matched_table_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    matched_entity_count: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    last_: i64 = @import("std").mem.zeroes(i64),
    t: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_query_stats_t = struct_ecs_query_stats_t;
pub const struct_ecs_system_stats_t = extern struct {
    first_: i64 = @import("std").mem.zeroes(i64),
    time_spent: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    last_: i64 = @import("std").mem.zeroes(i64),
    task: bool = @import("std").mem.zeroes(bool),
    query: ecs_query_stats_t = @import("std").mem.zeroes(ecs_query_stats_t),
};
pub const ecs_system_stats_t = struct_ecs_system_stats_t;
pub const struct_ecs_sync_stats_t = extern struct {
    first_: i64 = @import("std").mem.zeroes(i64),
    time_spent: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    commands_enqueued: ecs_metric_t = @import("std").mem.zeroes(ecs_metric_t),
    last_: i64 = @import("std").mem.zeroes(i64),
    system_count: i32 = @import("std").mem.zeroes(i32),
    multi_threaded: bool = @import("std").mem.zeroes(bool),
    immediate: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_sync_stats_t = struct_ecs_sync_stats_t;
pub const struct_ecs_pipeline_stats_t = extern struct {
    canary_: i8 = @import("std").mem.zeroes(i8),
    systems: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    sync_points: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    t: i32 = @import("std").mem.zeroes(i32),
    system_count: i32 = @import("std").mem.zeroes(i32),
    active_system_count: i32 = @import("std").mem.zeroes(i32),
    rebuild_count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_pipeline_stats_t = struct_ecs_pipeline_stats_t;
pub extern fn ecs_world_stats_get(world: ?*const ecs_world_t, stats: [*c]ecs_world_stats_t) void;
pub extern fn ecs_world_stats_reduce(dst: [*c]ecs_world_stats_t, src: [*c]const ecs_world_stats_t) void;
pub extern fn ecs_world_stats_reduce_last(stats: [*c]ecs_world_stats_t, old: [*c]const ecs_world_stats_t, count: i32) void;
pub extern fn ecs_world_stats_repeat_last(stats: [*c]ecs_world_stats_t) void;
pub extern fn ecs_world_stats_copy_last(dst: [*c]ecs_world_stats_t, src: [*c]const ecs_world_stats_t) void;
pub extern fn ecs_world_stats_log(world: ?*const ecs_world_t, stats: [*c]const ecs_world_stats_t) void;
pub extern fn ecs_query_stats_get(world: ?*const ecs_world_t, query: [*c]const ecs_query_t, stats: [*c]ecs_query_stats_t) void;
pub extern fn ecs_query_cache_stats_reduce(dst: [*c]ecs_query_stats_t, src: [*c]const ecs_query_stats_t) void;
pub extern fn ecs_query_cache_stats_reduce_last(stats: [*c]ecs_query_stats_t, old: [*c]const ecs_query_stats_t, count: i32) void;
pub extern fn ecs_query_cache_stats_repeat_last(stats: [*c]ecs_query_stats_t) void;
pub extern fn ecs_query_cache_stats_copy_last(dst: [*c]ecs_query_stats_t, src: [*c]const ecs_query_stats_t) void;
pub extern fn ecs_system_stats_get(world: ?*const ecs_world_t, system: ecs_entity_t, stats: [*c]ecs_system_stats_t) bool;
pub extern fn ecs_system_stats_reduce(dst: [*c]ecs_system_stats_t, src: [*c]const ecs_system_stats_t) void;
pub extern fn ecs_system_stats_reduce_last(stats: [*c]ecs_system_stats_t, old: [*c]const ecs_system_stats_t, count: i32) void;
pub extern fn ecs_system_stats_repeat_last(stats: [*c]ecs_system_stats_t) void;
pub extern fn ecs_system_stats_copy_last(dst: [*c]ecs_system_stats_t, src: [*c]const ecs_system_stats_t) void;
pub extern fn ecs_pipeline_stats_get(world: ?*ecs_world_t, pipeline: ecs_entity_t, stats: [*c]ecs_pipeline_stats_t) bool;
pub extern fn ecs_pipeline_stats_fini(stats: [*c]ecs_pipeline_stats_t) void;
pub extern fn ecs_pipeline_stats_reduce(dst: [*c]ecs_pipeline_stats_t, src: [*c]const ecs_pipeline_stats_t) void;
pub extern fn ecs_pipeline_stats_reduce_last(stats: [*c]ecs_pipeline_stats_t, old: [*c]const ecs_pipeline_stats_t, count: i32) void;
pub extern fn ecs_pipeline_stats_repeat_last(stats: [*c]ecs_pipeline_stats_t) void;
pub extern fn ecs_pipeline_stats_copy_last(dst: [*c]ecs_pipeline_stats_t, src: [*c]const ecs_pipeline_stats_t) void;
pub extern fn ecs_metric_reduce(dst: [*c]ecs_metric_t, src: [*c]const ecs_metric_t, t_dst: i32, t_src: i32) void;
pub extern fn ecs_metric_reduce_last(m: [*c]ecs_metric_t, t: i32, count: i32) void;
pub extern fn ecs_metric_copy(m: [*c]ecs_metric_t, dst: i32, src: i32) void;
pub extern var FLECS_IDFlecsStatsID_: ecs_entity_t;
pub extern var FLECS_IDEcsWorldStatsID_: ecs_entity_t;
pub extern var FLECS_IDEcsWorldSummaryID_: ecs_entity_t;
pub extern var FLECS_IDEcsSystemStatsID_: ecs_entity_t;
pub extern var FLECS_IDEcsPipelineStatsID_: ecs_entity_t;
pub extern var EcsPeriod1s: ecs_entity_t;
pub extern var EcsPeriod1m: ecs_entity_t;
pub extern var EcsPeriod1h: ecs_entity_t;
pub extern var EcsPeriod1d: ecs_entity_t;
pub extern var EcsPeriod1w: ecs_entity_t;
pub const EcsStatsHeader = extern struct {
    elapsed: f32 = @import("std").mem.zeroes(f32),
    reduce_count: i32 = @import("std").mem.zeroes(i32),
};
pub const EcsWorldStats = extern struct {
    hdr: EcsStatsHeader = @import("std").mem.zeroes(EcsStatsHeader),
    stats: ecs_world_stats_t = @import("std").mem.zeroes(ecs_world_stats_t),
};
pub const EcsSystemStats = extern struct {
    hdr: EcsStatsHeader = @import("std").mem.zeroes(EcsStatsHeader),
    stats: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
};
pub const EcsPipelineStats = extern struct {
    hdr: EcsStatsHeader = @import("std").mem.zeroes(EcsStatsHeader),
    stats: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
};
pub const EcsWorldSummary = extern struct {
    target_fps: f64 = @import("std").mem.zeroes(f64),
    time_scale: f64 = @import("std").mem.zeroes(f64),
    frame_time_total: f64 = @import("std").mem.zeroes(f64),
    system_time_total: f64 = @import("std").mem.zeroes(f64),
    merge_time_total: f64 = @import("std").mem.zeroes(f64),
    frame_time_last: f64 = @import("std").mem.zeroes(f64),
    system_time_last: f64 = @import("std").mem.zeroes(f64),
    merge_time_last: f64 = @import("std").mem.zeroes(f64),
    frame_count: i64 = @import("std").mem.zeroes(i64),
    command_count: i64 = @import("std").mem.zeroes(i64),
    build_info: ecs_build_info_t = @import("std").mem.zeroes(ecs_build_info_t),
};
pub extern fn FlecsStatsImport(world: ?*ecs_world_t) void;
pub extern var FLECS_IDFlecsMetricsID_: ecs_entity_t;
pub extern var EcsMetric: ecs_entity_t;
pub extern var FLECS_IDEcsMetricID_: ecs_entity_t;
pub extern var EcsCounter: ecs_entity_t;
pub extern var FLECS_IDEcsCounterID_: ecs_entity_t;
pub extern var EcsCounterIncrement: ecs_entity_t;
pub extern var FLECS_IDEcsCounterIncrementID_: ecs_entity_t;
pub extern var EcsCounterId: ecs_entity_t;
pub extern var FLECS_IDEcsCounterIdID_: ecs_entity_t;
pub extern var EcsGauge: ecs_entity_t;
pub extern var FLECS_IDEcsGaugeID_: ecs_entity_t;
pub extern var EcsMetricInstance: ecs_entity_t;
pub extern var FLECS_IDEcsMetricInstanceID_: ecs_entity_t;
pub extern var FLECS_IDEcsMetricValueID_: ecs_entity_t;
pub extern var FLECS_IDEcsMetricSourceID_: ecs_entity_t;
pub const struct_EcsMetricValue = extern struct {
    value: f64 = @import("std").mem.zeroes(f64),
};
pub const EcsMetricValue = struct_EcsMetricValue;
pub const struct_EcsMetricSource = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const EcsMetricSource = struct_EcsMetricSource;
pub const struct_ecs_metric_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    member: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    dotmember: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    id: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    targets: bool = @import("std").mem.zeroes(bool),
    kind: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    brief: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_metric_desc_t = struct_ecs_metric_desc_t;
pub extern fn ecs_metric_init(world: ?*ecs_world_t, desc: [*c]const ecs_metric_desc_t) ecs_entity_t;
pub extern fn FlecsMetricsImport(world: ?*ecs_world_t) void;
pub extern var FLECS_IDFlecsAlertsID_: ecs_entity_t;
pub extern var FLECS_IDEcsAlertID_: ecs_entity_t;
pub extern var FLECS_IDEcsAlertInstanceID_: ecs_entity_t;
pub extern var FLECS_IDEcsAlertsActiveID_: ecs_entity_t;
pub extern var FLECS_IDEcsAlertTimeoutID_: ecs_entity_t;
pub extern var EcsAlertInfo: ecs_entity_t;
pub extern var FLECS_IDEcsAlertInfoID_: ecs_entity_t;
pub extern var EcsAlertWarning: ecs_entity_t;
pub extern var FLECS_IDEcsAlertWarningID_: ecs_entity_t;
pub extern var EcsAlertError: ecs_entity_t;
pub extern var FLECS_IDEcsAlertErrorID_: ecs_entity_t;
pub extern var EcsAlertCritical: ecs_entity_t;
pub extern var FLECS_IDEcsAlertCriticalID_: ecs_entity_t;
pub const struct_EcsAlertInstance = extern struct {
    message: [*c]u8 = @import("std").mem.zeroes([*c]u8),
};
pub const EcsAlertInstance = struct_EcsAlertInstance;
pub const struct_EcsAlertsActive = extern struct {
    info_count: i32 = @import("std").mem.zeroes(i32),
    warning_count: i32 = @import("std").mem.zeroes(i32),
    error_count: i32 = @import("std").mem.zeroes(i32),
    alerts: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
};
pub const EcsAlertsActive = struct_EcsAlertsActive;
pub const struct_ecs_alert_severity_filter_t = extern struct {
    severity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    with: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    @"var": [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    _var_index: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_alert_severity_filter_t = struct_ecs_alert_severity_filter_t;
pub const struct_ecs_alert_desc_t = extern struct {
    _canary: i32 = @import("std").mem.zeroes(i32),
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    query: ecs_query_desc_t = @import("std").mem.zeroes(ecs_query_desc_t),
    message: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    doc_name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    brief: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    severity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    severity_filters: [4]ecs_alert_severity_filter_t = @import("std").mem.zeroes([4]ecs_alert_severity_filter_t),
    retain_period: f32 = @import("std").mem.zeroes(f32),
    member: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    id: ecs_id_t = @import("std").mem.zeroes(ecs_id_t),
    @"var": [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_alert_desc_t = struct_ecs_alert_desc_t;
pub extern fn ecs_alert_init(world: ?*ecs_world_t, desc: [*c]const ecs_alert_desc_t) ecs_entity_t;
pub extern fn ecs_get_alert_count(world: ?*const ecs_world_t, entity: ecs_entity_t, alert: ecs_entity_t) i32;
pub extern fn ecs_get_alert(world: ?*const ecs_world_t, entity: ecs_entity_t, alert: ecs_entity_t) ecs_entity_t;
pub extern fn FlecsAlertsImport(world: ?*ecs_world_t) void;
pub const struct_ecs_from_json_desc_t = extern struct {
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    expr: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    lookup_action: ?*const fn (?*const ecs_world_t, [*c]const u8, ?*anyopaque) callconv(.C) ecs_entity_t = @import("std").mem.zeroes(?*const fn (?*const ecs_world_t, [*c]const u8, ?*anyopaque) callconv(.C) ecs_entity_t),
    lookup_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    strict: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_from_json_desc_t = struct_ecs_from_json_desc_t;
pub extern fn ecs_ptr_from_json(world: ?*const ecs_world_t, @"type": ecs_entity_t, ptr: ?*anyopaque, json: [*c]const u8, desc: [*c]const ecs_from_json_desc_t) [*c]const u8;
pub extern fn ecs_entity_from_json(world: ?*ecs_world_t, entity: ecs_entity_t, json: [*c]const u8, desc: [*c]const ecs_from_json_desc_t) [*c]const u8;
pub extern fn ecs_world_from_json(world: ?*ecs_world_t, json: [*c]const u8, desc: [*c]const ecs_from_json_desc_t) [*c]const u8;
pub extern fn ecs_world_from_json_file(world: ?*ecs_world_t, filename: [*c]const u8, desc: [*c]const ecs_from_json_desc_t) [*c]const u8;
pub extern fn ecs_array_to_json(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque, count: i32) [*c]u8;
pub extern fn ecs_array_to_json_buf(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque, count: i32, buf_out: [*c]ecs_strbuf_t) c_int;
pub extern fn ecs_ptr_to_json(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque) [*c]u8;
pub extern fn ecs_ptr_to_json_buf(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque, buf_out: [*c]ecs_strbuf_t) c_int;
pub extern fn ecs_type_info_to_json(world: ?*const ecs_world_t, @"type": ecs_entity_t) [*c]u8;
pub extern fn ecs_type_info_to_json_buf(world: ?*const ecs_world_t, @"type": ecs_entity_t, buf_out: [*c]ecs_strbuf_t) c_int;
pub const struct_ecs_entity_to_json_desc_t = extern struct {
    serialize_entity_id: bool = @import("std").mem.zeroes(bool),
    serialize_doc: bool = @import("std").mem.zeroes(bool),
    serialize_full_paths: bool = @import("std").mem.zeroes(bool),
    serialize_inherited: bool = @import("std").mem.zeroes(bool),
    serialize_values: bool = @import("std").mem.zeroes(bool),
    serialize_builtin: bool = @import("std").mem.zeroes(bool),
    serialize_type_info: bool = @import("std").mem.zeroes(bool),
    serialize_alerts: bool = @import("std").mem.zeroes(bool),
    serialize_refs: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    serialize_matches: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_entity_to_json_desc_t = struct_ecs_entity_to_json_desc_t;
pub extern fn ecs_entity_to_json(world: ?*const ecs_world_t, entity: ecs_entity_t, desc: [*c]const ecs_entity_to_json_desc_t) [*c]u8;
pub extern fn ecs_entity_to_json_buf(world: ?*const ecs_world_t, entity: ecs_entity_t, buf_out: [*c]ecs_strbuf_t, desc: [*c]const ecs_entity_to_json_desc_t) c_int;
pub const struct_ecs_iter_to_json_desc_t = extern struct {
    serialize_entity_ids: bool = @import("std").mem.zeroes(bool),
    serialize_values: bool = @import("std").mem.zeroes(bool),
    serialize_builtin: bool = @import("std").mem.zeroes(bool),
    serialize_doc: bool = @import("std").mem.zeroes(bool),
    serialize_full_paths: bool = @import("std").mem.zeroes(bool),
    serialize_fields: bool = @import("std").mem.zeroes(bool),
    serialize_inherited: bool = @import("std").mem.zeroes(bool),
    serialize_table: bool = @import("std").mem.zeroes(bool),
    serialize_type_info: bool = @import("std").mem.zeroes(bool),
    serialize_field_info: bool = @import("std").mem.zeroes(bool),
    serialize_query_info: bool = @import("std").mem.zeroes(bool),
    serialize_query_plan: bool = @import("std").mem.zeroes(bool),
    serialize_query_profile: bool = @import("std").mem.zeroes(bool),
    dont_serialize_results: bool = @import("std").mem.zeroes(bool),
    serialize_alerts: bool = @import("std").mem.zeroes(bool),
    serialize_refs: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    serialize_matches: bool = @import("std").mem.zeroes(bool),
    query: ?*ecs_poly_t = @import("std").mem.zeroes(?*ecs_poly_t),
};
pub const ecs_iter_to_json_desc_t = struct_ecs_iter_to_json_desc_t;
pub extern fn ecs_iter_to_json(iter: [*c]ecs_iter_t, desc: [*c]const ecs_iter_to_json_desc_t) [*c]u8;
pub extern fn ecs_iter_to_json_buf(iter: [*c]ecs_iter_t, buf_out: [*c]ecs_strbuf_t, desc: [*c]const ecs_iter_to_json_desc_t) c_int;
pub const struct_ecs_world_to_json_desc_t = extern struct {
    serialize_builtin: bool = @import("std").mem.zeroes(bool),
    serialize_modules: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_world_to_json_desc_t = struct_ecs_world_to_json_desc_t;
pub extern fn ecs_world_to_json(world: ?*ecs_world_t, desc: [*c]const ecs_world_to_json_desc_t) [*c]u8;
pub extern fn ecs_world_to_json_buf(world: ?*ecs_world_t, buf_out: [*c]ecs_strbuf_t, desc: [*c]const ecs_world_to_json_desc_t) c_int;
pub extern var EcsUnitPrefixes: ecs_entity_t;
pub extern var EcsYocto: ecs_entity_t;
pub extern var EcsZepto: ecs_entity_t;
pub extern var EcsAtto: ecs_entity_t;
pub extern var EcsFemto: ecs_entity_t;
pub extern var EcsPico: ecs_entity_t;
pub extern var EcsNano: ecs_entity_t;
pub extern var EcsMicro: ecs_entity_t;
pub extern var EcsMilli: ecs_entity_t;
pub extern var EcsCenti: ecs_entity_t;
pub extern var EcsDeci: ecs_entity_t;
pub extern var EcsDeca: ecs_entity_t;
pub extern var EcsHecto: ecs_entity_t;
pub extern var EcsKilo: ecs_entity_t;
pub extern var EcsMega: ecs_entity_t;
pub extern var EcsGiga: ecs_entity_t;
pub extern var EcsTera: ecs_entity_t;
pub extern var EcsPeta: ecs_entity_t;
pub extern var EcsExa: ecs_entity_t;
pub extern var EcsZetta: ecs_entity_t;
pub extern var EcsYotta: ecs_entity_t;
pub extern var EcsKibi: ecs_entity_t;
pub extern var EcsMebi: ecs_entity_t;
pub extern var EcsGibi: ecs_entity_t;
pub extern var EcsTebi: ecs_entity_t;
pub extern var EcsPebi: ecs_entity_t;
pub extern var EcsExbi: ecs_entity_t;
pub extern var EcsZebi: ecs_entity_t;
pub extern var EcsYobi: ecs_entity_t;
pub extern var EcsDuration: ecs_entity_t;
pub extern var EcsPicoSeconds: ecs_entity_t;
pub extern var EcsNanoSeconds: ecs_entity_t;
pub extern var EcsMicroSeconds: ecs_entity_t;
pub extern var EcsMilliSeconds: ecs_entity_t;
pub extern var EcsSeconds: ecs_entity_t;
pub extern var EcsMinutes: ecs_entity_t;
pub extern var EcsHours: ecs_entity_t;
pub extern var EcsDays: ecs_entity_t;
pub extern var EcsTime: ecs_entity_t;
pub extern var EcsDate: ecs_entity_t;
pub extern var EcsMass: ecs_entity_t;
pub extern var EcsGrams: ecs_entity_t;
pub extern var EcsKiloGrams: ecs_entity_t;
pub extern var EcsElectricCurrent: ecs_entity_t;
pub extern var EcsAmpere: ecs_entity_t;
pub extern var EcsAmount: ecs_entity_t;
pub extern var EcsMole: ecs_entity_t;
pub extern var EcsLuminousIntensity: ecs_entity_t;
pub extern var EcsCandela: ecs_entity_t;
pub extern var EcsForce: ecs_entity_t;
pub extern var EcsNewton: ecs_entity_t;
pub extern var EcsLength: ecs_entity_t;
pub extern var EcsMeters: ecs_entity_t;
pub extern var EcsPicoMeters: ecs_entity_t;
pub extern var EcsNanoMeters: ecs_entity_t;
pub extern var EcsMicroMeters: ecs_entity_t;
pub extern var EcsMilliMeters: ecs_entity_t;
pub extern var EcsCentiMeters: ecs_entity_t;
pub extern var EcsKiloMeters: ecs_entity_t;
pub extern var EcsMiles: ecs_entity_t;
pub extern var EcsPixels: ecs_entity_t;
pub extern var EcsPressure: ecs_entity_t;
pub extern var EcsPascal: ecs_entity_t;
pub extern var EcsBar: ecs_entity_t;
pub extern var EcsSpeed: ecs_entity_t;
pub extern var EcsMetersPerSecond: ecs_entity_t;
pub extern var EcsKiloMetersPerSecond: ecs_entity_t;
pub extern var EcsKiloMetersPerHour: ecs_entity_t;
pub extern var EcsMilesPerHour: ecs_entity_t;
pub extern var EcsTemperature: ecs_entity_t;
pub extern var EcsKelvin: ecs_entity_t;
pub extern var EcsCelsius: ecs_entity_t;
pub extern var EcsFahrenheit: ecs_entity_t;
pub extern var EcsData: ecs_entity_t;
pub extern var EcsBits: ecs_entity_t;
pub extern var EcsKiloBits: ecs_entity_t;
pub extern var EcsMegaBits: ecs_entity_t;
pub extern var EcsGigaBits: ecs_entity_t;
pub extern var EcsBytes: ecs_entity_t;
pub extern var EcsKiloBytes: ecs_entity_t;
pub extern var EcsMegaBytes: ecs_entity_t;
pub extern var EcsGigaBytes: ecs_entity_t;
pub extern var EcsKibiBytes: ecs_entity_t;
pub extern var EcsMebiBytes: ecs_entity_t;
pub extern var EcsGibiBytes: ecs_entity_t;
pub extern var EcsDataRate: ecs_entity_t;
pub extern var EcsBitsPerSecond: ecs_entity_t;
pub extern var EcsKiloBitsPerSecond: ecs_entity_t;
pub extern var EcsMegaBitsPerSecond: ecs_entity_t;
pub extern var EcsGigaBitsPerSecond: ecs_entity_t;
pub extern var EcsBytesPerSecond: ecs_entity_t;
pub extern var EcsKiloBytesPerSecond: ecs_entity_t;
pub extern var EcsMegaBytesPerSecond: ecs_entity_t;
pub extern var EcsGigaBytesPerSecond: ecs_entity_t;
pub extern var EcsAngle: ecs_entity_t;
pub extern var EcsRadians: ecs_entity_t;
pub extern var EcsDegrees: ecs_entity_t;
pub extern var EcsFrequency: ecs_entity_t;
pub extern var EcsHertz: ecs_entity_t;
pub extern var EcsKiloHertz: ecs_entity_t;
pub extern var EcsMegaHertz: ecs_entity_t;
pub extern var EcsGigaHertz: ecs_entity_t;
pub extern var EcsUri: ecs_entity_t;
pub extern var EcsUriHyperlink: ecs_entity_t;
pub extern var EcsUriImage: ecs_entity_t;
pub extern var EcsUriFile: ecs_entity_t;
pub extern var EcsColor: ecs_entity_t;
pub extern var EcsColorRgb: ecs_entity_t;
pub extern var EcsColorHsl: ecs_entity_t;
pub extern var EcsColorCss: ecs_entity_t;
pub extern var EcsAcceleration: ecs_entity_t;
pub extern var EcsPercentage: ecs_entity_t;
pub extern var EcsBel: ecs_entity_t;
pub extern var EcsDeciBel: ecs_entity_t;
pub extern fn FlecsUnitsImport(world: ?*ecs_world_t) void;
pub extern var FLECS_IDEcsScriptID_: ecs_entity_t;
pub const struct_ecs_script_template_t = opaque {};
pub const ecs_script_template_t = struct_ecs_script_template_t;
pub const struct_ecs_script_var_t = extern struct {
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    value: ecs_value_t = @import("std").mem.zeroes(ecs_value_t),
    type_info: [*c]const ecs_type_info_t = @import("std").mem.zeroes([*c]const ecs_type_info_t),
};
pub const ecs_script_var_t = struct_ecs_script_var_t;
pub const struct_ecs_script_vars_t = extern struct {
    parent: [*c]struct_ecs_script_vars_t = @import("std").mem.zeroes([*c]struct_ecs_script_vars_t),
    var_index: ecs_hashmap_t = @import("std").mem.zeroes(ecs_hashmap_t),
    vars: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
    world: ?*const ecs_world_t = @import("std").mem.zeroes(?*const ecs_world_t),
    stack: [*c]struct_ecs_stack_t = @import("std").mem.zeroes([*c]struct_ecs_stack_t),
    cursor: [*c]ecs_stack_cursor_t = @import("std").mem.zeroes([*c]ecs_stack_cursor_t),
    allocator: [*c]ecs_allocator_t = @import("std").mem.zeroes([*c]ecs_allocator_t),
};
pub const ecs_script_vars_t = struct_ecs_script_vars_t;
pub const struct_ecs_script_t = extern struct {
    world: ?*ecs_world_t = @import("std").mem.zeroes(?*ecs_world_t),
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    code: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_script_t = struct_ecs_script_t;
pub const struct_EcsScript = extern struct {
    script: [*c]ecs_script_t = @import("std").mem.zeroes([*c]ecs_script_t),
    template_: ?*ecs_script_template_t = @import("std").mem.zeroes(?*ecs_script_template_t),
};
pub const EcsScript = struct_EcsScript;
pub extern fn ecs_script_parse(world: ?*ecs_world_t, name: [*c]const u8, code: [*c]const u8) [*c]ecs_script_t;
pub extern fn ecs_script_eval(script: [*c]ecs_script_t) c_int;
pub extern fn ecs_script_free(script: [*c]ecs_script_t) void;
pub extern fn ecs_script_run(world: ?*ecs_world_t, name: [*c]const u8, code: [*c]const u8) c_int;
pub extern fn ecs_script_run_file(world: ?*ecs_world_t, filename: [*c]const u8) c_int;
pub extern fn ecs_script_ast_to_buf(script: [*c]ecs_script_t, buf: [*c]ecs_strbuf_t) c_int;
pub extern fn ecs_script_ast_to_str(script: [*c]ecs_script_t) [*c]u8;
pub const struct_ecs_script_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    filename: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    code: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
};
pub const ecs_script_desc_t = struct_ecs_script_desc_t;
pub extern fn ecs_script_init(world: ?*ecs_world_t, desc: [*c]const ecs_script_desc_t) ecs_entity_t;
pub extern fn ecs_script_update(world: ?*ecs_world_t, script: ecs_entity_t, instance: ecs_entity_t, code: [*c]const u8) c_int;
pub extern fn ecs_script_clear(world: ?*ecs_world_t, script: ecs_entity_t, instance: ecs_entity_t) void;
pub extern fn ecs_script_vars_init(world: ?*ecs_world_t) [*c]ecs_script_vars_t;
pub extern fn ecs_script_vars_fini(vars: [*c]ecs_script_vars_t) void;
pub extern fn ecs_script_vars_push(parent: [*c]ecs_script_vars_t) [*c]ecs_script_vars_t;
pub extern fn ecs_script_vars_pop(vars: [*c]ecs_script_vars_t) [*c]ecs_script_vars_t;
pub extern fn ecs_script_vars_declare(vars: [*c]ecs_script_vars_t, name: [*c]const u8) [*c]ecs_script_var_t;
pub extern fn ecs_script_vars_define_id(vars: [*c]ecs_script_vars_t, name: [*c]const u8, @"type": ecs_entity_t) [*c]ecs_script_var_t;
pub extern fn ecs_script_vars_lookup(vars: [*c]const ecs_script_vars_t, name: [*c]const u8) [*c]ecs_script_var_t;
pub extern fn ecs_script_vars_from_iter(it: [*c]const ecs_iter_t, vars: [*c]ecs_script_vars_t, offset: c_int) void;
pub const struct_ecs_script_expr_run_desc_t = extern struct {
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    expr: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    lookup_action: ?*const fn (?*const ecs_world_t, [*c]const u8, ?*anyopaque) callconv(.C) ecs_entity_t = @import("std").mem.zeroes(?*const fn (?*const ecs_world_t, [*c]const u8, ?*anyopaque) callconv(.C) ecs_entity_t),
    lookup_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    vars: [*c]ecs_script_vars_t = @import("std").mem.zeroes([*c]ecs_script_vars_t),
};
pub const ecs_script_expr_run_desc_t = struct_ecs_script_expr_run_desc_t;
pub extern fn ecs_script_expr_run(world: ?*ecs_world_t, ptr: [*c]const u8, value: [*c]ecs_value_t, desc: [*c]const ecs_script_expr_run_desc_t) [*c]const u8;
pub extern fn ecs_script_string_interpolate(world: ?*ecs_world_t, str: [*c]const u8, vars: [*c]const ecs_script_vars_t) [*c]u8;
pub extern fn ecs_ptr_to_expr(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque) [*c]u8;
pub extern fn ecs_ptr_to_expr_buf(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque, buf: [*c]ecs_strbuf_t) c_int;
pub extern fn ecs_ptr_to_str(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque) [*c]u8;
pub extern fn ecs_ptr_to_str_buf(world: ?*const ecs_world_t, @"type": ecs_entity_t, data: ?*const anyopaque, buf: [*c]ecs_strbuf_t) c_int;
pub extern fn FlecsScriptImport(world: ?*ecs_world_t) void;
pub extern const FLECS_IDEcsDocDescriptionID_: ecs_entity_t;
pub extern const EcsDocBrief: ecs_entity_t;
pub extern const EcsDocDetail: ecs_entity_t;
pub extern const EcsDocLink: ecs_entity_t;
pub extern const EcsDocColor: ecs_entity_t;
pub const struct_EcsDocDescription = extern struct {
    value: [*c]u8 = @import("std").mem.zeroes([*c]u8),
};
pub const EcsDocDescription = struct_EcsDocDescription;
pub extern fn ecs_doc_set_name(world: ?*ecs_world_t, entity: ecs_entity_t, name: [*c]const u8) void;
pub extern fn ecs_doc_set_brief(world: ?*ecs_world_t, entity: ecs_entity_t, description: [*c]const u8) void;
pub extern fn ecs_doc_set_detail(world: ?*ecs_world_t, entity: ecs_entity_t, description: [*c]const u8) void;
pub extern fn ecs_doc_set_link(world: ?*ecs_world_t, entity: ecs_entity_t, link: [*c]const u8) void;
pub extern fn ecs_doc_set_color(world: ?*ecs_world_t, entity: ecs_entity_t, color: [*c]const u8) void;
pub extern fn ecs_doc_get_name(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn ecs_doc_get_brief(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn ecs_doc_get_detail(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn ecs_doc_get_link(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn ecs_doc_get_color(world: ?*const ecs_world_t, entity: ecs_entity_t) [*c]const u8;
pub extern fn FlecsDocImport(world: ?*ecs_world_t) void;
pub const ptrdiff_t = c_long;
pub const wchar_t = c_int;
pub const max_align_t = c_longdouble;
pub const ecs_bool_t = bool;
pub const ecs_char_t = u8;
pub const ecs_byte_t = u8;
pub const ecs_u8_t = u8;
pub const ecs_u16_t = u16;
pub const ecs_u32_t = u32;
pub const ecs_u64_t = u64;
pub const ecs_uptr_t = usize;
pub const ecs_i8_t = i8;
pub const ecs_i16_t = i16;
pub const ecs_i32_t = i32;
pub const ecs_i64_t = i64;
pub const ecs_iptr_t = isize;
pub const ecs_f32_t = f32;
pub const ecs_f64_t = f64;
pub const ecs_string_t = [*c]u8;
pub extern const FLECS_IDEcsTypeID_: ecs_entity_t;
pub extern const FLECS_IDEcsTypeSerializerID_: ecs_entity_t;
pub extern const FLECS_IDEcsPrimitiveID_: ecs_entity_t;
pub extern const FLECS_IDEcsEnumID_: ecs_entity_t;
pub extern const FLECS_IDEcsBitmaskID_: ecs_entity_t;
pub extern const FLECS_IDEcsMemberID_: ecs_entity_t;
pub extern const FLECS_IDEcsMemberRangesID_: ecs_entity_t;
pub extern const FLECS_IDEcsStructID_: ecs_entity_t;
pub extern const FLECS_IDEcsArrayID_: ecs_entity_t;
pub extern const FLECS_IDEcsVectorID_: ecs_entity_t;
pub extern const FLECS_IDEcsOpaqueID_: ecs_entity_t;
pub extern const FLECS_IDEcsUnitID_: ecs_entity_t;
pub extern const FLECS_IDEcsUnitPrefixID_: ecs_entity_t;
pub extern const EcsConstant: ecs_entity_t;
pub extern const EcsQuantity: ecs_entity_t;
pub extern const FLECS_IDecs_bool_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_char_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_byte_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_u8_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_u16_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_u32_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_u64_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_uptr_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_i8_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_i16_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_i32_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_i64_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_iptr_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_f32_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_f64_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_string_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_entity_tID_: ecs_entity_t;
pub extern const FLECS_IDecs_id_tID_: ecs_entity_t;
pub const EcsPrimitiveType: c_int = 0;
pub const EcsBitmaskType: c_int = 1;
pub const EcsEnumType: c_int = 2;
pub const EcsStructType: c_int = 3;
pub const EcsArrayType: c_int = 4;
pub const EcsVectorType: c_int = 5;
pub const EcsOpaqueType: c_int = 6;
pub const EcsTypeKindLast: c_int = 6;
pub const enum_ecs_type_kind_t = c_uint;
pub const ecs_type_kind_t = enum_ecs_type_kind_t;
pub const struct_EcsType = extern struct {
    kind: ecs_type_kind_t = @import("std").mem.zeroes(ecs_type_kind_t),
    existing: bool = @import("std").mem.zeroes(bool),
    partial: bool = @import("std").mem.zeroes(bool),
};
pub const EcsType = struct_EcsType;
pub const EcsBool: c_int = 1;
pub const EcsChar: c_int = 2;
pub const EcsByte: c_int = 3;
pub const EcsU8: c_int = 4;
pub const EcsU16: c_int = 5;
pub const EcsU32: c_int = 6;
pub const EcsU64: c_int = 7;
pub const EcsI8: c_int = 8;
pub const EcsI16: c_int = 9;
pub const EcsI32: c_int = 10;
pub const EcsI64: c_int = 11;
pub const EcsF32: c_int = 12;
pub const EcsF64: c_int = 13;
pub const EcsUPtr: c_int = 14;
pub const EcsIPtr: c_int = 15;
pub const EcsString: c_int = 16;
pub const EcsEntity: c_int = 17;
pub const EcsId: c_int = 18;
pub const EcsPrimitiveKindLast: c_int = 18;
pub const enum_ecs_primitive_kind_t = c_uint;
pub const ecs_primitive_kind_t = enum_ecs_primitive_kind_t;
pub const struct_EcsPrimitive = extern struct {
    kind: ecs_primitive_kind_t = @import("std").mem.zeroes(ecs_primitive_kind_t),
};
pub const EcsPrimitive = struct_EcsPrimitive;
pub const struct_EcsMember = extern struct {
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    count: i32 = @import("std").mem.zeroes(i32),
    unit: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    offset: i32 = @import("std").mem.zeroes(i32),
    use_offset: bool = @import("std").mem.zeroes(bool),
};
pub const EcsMember = struct_EcsMember;
pub const struct_ecs_member_value_range_t = extern struct {
    min: f64 = @import("std").mem.zeroes(f64),
    max: f64 = @import("std").mem.zeroes(f64),
};
pub const ecs_member_value_range_t = struct_ecs_member_value_range_t;
pub const struct_EcsMemberRanges = extern struct {
    value: ecs_member_value_range_t = @import("std").mem.zeroes(ecs_member_value_range_t),
    warning: ecs_member_value_range_t = @import("std").mem.zeroes(ecs_member_value_range_t),
    @"error": ecs_member_value_range_t = @import("std").mem.zeroes(ecs_member_value_range_t),
};
pub const EcsMemberRanges = struct_EcsMemberRanges;
pub const struct_ecs_member_t = extern struct {
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    count: i32 = @import("std").mem.zeroes(i32),
    offset: i32 = @import("std").mem.zeroes(i32),
    unit: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    use_offset: bool = @import("std").mem.zeroes(bool),
    range: ecs_member_value_range_t = @import("std").mem.zeroes(ecs_member_value_range_t),
    error_range: ecs_member_value_range_t = @import("std").mem.zeroes(ecs_member_value_range_t),
    warning_range: ecs_member_value_range_t = @import("std").mem.zeroes(ecs_member_value_range_t),
    size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    member: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_member_t = struct_ecs_member_t;
pub const struct_EcsStruct = extern struct {
    members: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
};
pub const EcsStruct = struct_EcsStruct;
pub const struct_ecs_enum_constant_t = extern struct {
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    value: i32 = @import("std").mem.zeroes(i32),
    constant: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_enum_constant_t = struct_ecs_enum_constant_t;
pub const struct_EcsEnum = extern struct {
    constants: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
};
pub const EcsEnum = struct_EcsEnum;
pub const struct_ecs_bitmask_constant_t = extern struct {
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    value: ecs_flags32_t = @import("std").mem.zeroes(ecs_flags32_t),
    constant: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_bitmask_constant_t = struct_ecs_bitmask_constant_t;
pub const struct_EcsBitmask = extern struct {
    constants: ecs_map_t = @import("std").mem.zeroes(ecs_map_t),
};
pub const EcsBitmask = struct_EcsBitmask;
pub const struct_EcsArray = extern struct {
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    count: i32 = @import("std").mem.zeroes(i32),
};
pub const EcsArray = struct_EcsArray;
pub const struct_EcsVector = extern struct {
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const EcsVector = struct_EcsVector;
pub const struct_ecs_serializer_t = extern struct {
    value: ?*const fn ([*c]const struct_ecs_serializer_t, ecs_entity_t, ?*const anyopaque) callconv(.C) c_int = @import("std").mem.zeroes(?*const fn ([*c]const struct_ecs_serializer_t, ecs_entity_t, ?*const anyopaque) callconv(.C) c_int),
    member: ?*const fn ([*c]const struct_ecs_serializer_t, [*c]const u8) callconv(.C) c_int = @import("std").mem.zeroes(?*const fn ([*c]const struct_ecs_serializer_t, [*c]const u8) callconv(.C) c_int),
    world: ?*const ecs_world_t = @import("std").mem.zeroes(?*const ecs_world_t),
    ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
};
pub const ecs_serializer_t = struct_ecs_serializer_t;
pub const ecs_meta_serialize_t = ?*const fn ([*c]const ecs_serializer_t, ?*const anyopaque) callconv(.C) c_int;
pub const struct_EcsOpaque = extern struct {
    as_type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    serialize: ecs_meta_serialize_t = @import("std").mem.zeroes(ecs_meta_serialize_t),
    assign_bool: ?*const fn (?*anyopaque, bool) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, bool) callconv(.C) void),
    assign_char: ?*const fn (?*anyopaque, u8) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, u8) callconv(.C) void),
    assign_int: ?*const fn (?*anyopaque, i64) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, i64) callconv(.C) void),
    assign_uint: ?*const fn (?*anyopaque, u64) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, u64) callconv(.C) void),
    assign_float: ?*const fn (?*anyopaque, f64) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, f64) callconv(.C) void),
    assign_string: ?*const fn (?*anyopaque, [*c]const u8) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, [*c]const u8) callconv(.C) void),
    assign_entity: ?*const fn (?*anyopaque, ?*ecs_world_t, ecs_entity_t) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, ?*ecs_world_t, ecs_entity_t) callconv(.C) void),
    assign_id: ?*const fn (?*anyopaque, ?*ecs_world_t, ecs_id_t) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, ?*ecs_world_t, ecs_id_t) callconv(.C) void),
    assign_null: ?*const fn (?*anyopaque) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque) callconv(.C) void),
    clear: ?*const fn (?*anyopaque) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque) callconv(.C) void),
    ensure_element: ?*const fn (?*anyopaque, usize) callconv(.C) ?*anyopaque = @import("std").mem.zeroes(?*const fn (?*anyopaque, usize) callconv(.C) ?*anyopaque),
    ensure_member: ?*const fn (?*anyopaque, [*c]const u8) callconv(.C) ?*anyopaque = @import("std").mem.zeroes(?*const fn (?*anyopaque, [*c]const u8) callconv(.C) ?*anyopaque),
    count: ?*const fn (?*const anyopaque) callconv(.C) usize = @import("std").mem.zeroes(?*const fn (?*const anyopaque) callconv(.C) usize),
    resize: ?*const fn (?*anyopaque, usize) callconv(.C) void = @import("std").mem.zeroes(?*const fn (?*anyopaque, usize) callconv(.C) void),
};
pub const EcsOpaque = struct_EcsOpaque;
pub const struct_ecs_unit_translation_t = extern struct {
    factor: i32 = @import("std").mem.zeroes(i32),
    power: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_unit_translation_t = struct_ecs_unit_translation_t;
pub const struct_EcsUnit = extern struct {
    symbol: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    prefix: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    base: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    over: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    translation: ecs_unit_translation_t = @import("std").mem.zeroes(ecs_unit_translation_t),
};
pub const EcsUnit = struct_EcsUnit;
pub const struct_EcsUnitPrefix = extern struct {
    symbol: [*c]u8 = @import("std").mem.zeroes([*c]u8),
    translation: ecs_unit_translation_t = @import("std").mem.zeroes(ecs_unit_translation_t),
};
pub const EcsUnitPrefix = struct_EcsUnitPrefix;
pub const EcsOpArray: c_int = 0;
pub const EcsOpVector: c_int = 1;
pub const EcsOpOpaque: c_int = 2;
pub const EcsOpPush: c_int = 3;
pub const EcsOpPop: c_int = 4;
pub const EcsOpScope: c_int = 5;
pub const EcsOpEnum: c_int = 6;
pub const EcsOpBitmask: c_int = 7;
pub const EcsOpPrimitive: c_int = 8;
pub const EcsOpBool: c_int = 9;
pub const EcsOpChar: c_int = 10;
pub const EcsOpByte: c_int = 11;
pub const EcsOpU8: c_int = 12;
pub const EcsOpU16: c_int = 13;
pub const EcsOpU32: c_int = 14;
pub const EcsOpU64: c_int = 15;
pub const EcsOpI8: c_int = 16;
pub const EcsOpI16: c_int = 17;
pub const EcsOpI32: c_int = 18;
pub const EcsOpI64: c_int = 19;
pub const EcsOpF32: c_int = 20;
pub const EcsOpF64: c_int = 21;
pub const EcsOpUPtr: c_int = 22;
pub const EcsOpIPtr: c_int = 23;
pub const EcsOpString: c_int = 24;
pub const EcsOpEntity: c_int = 25;
pub const EcsOpId: c_int = 26;
pub const EcsMetaTypeOpKindLast: c_int = 26;
pub const enum_ecs_meta_type_op_kind_t = c_uint;
pub const ecs_meta_type_op_kind_t = enum_ecs_meta_type_op_kind_t;
pub const struct_ecs_meta_type_op_t = extern struct {
    kind: ecs_meta_type_op_kind_t = @import("std").mem.zeroes(ecs_meta_type_op_kind_t),
    offset: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    count: i32 = @import("std").mem.zeroes(i32),
    name: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    op_count: i32 = @import("std").mem.zeroes(i32),
    size: ecs_size_t = @import("std").mem.zeroes(ecs_size_t),
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    member_index: i32 = @import("std").mem.zeroes(i32),
    members: [*c]ecs_hashmap_t = @import("std").mem.zeroes([*c]ecs_hashmap_t),
};
pub const ecs_meta_type_op_t = struct_ecs_meta_type_op_t;
pub const struct_EcsTypeSerializer = extern struct {
    ops: ecs_vec_t = @import("std").mem.zeroes(ecs_vec_t),
};
pub const EcsTypeSerializer = struct_EcsTypeSerializer;
pub const struct_ecs_meta_scope_t = extern struct {
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    ops: [*c]ecs_meta_type_op_t = @import("std").mem.zeroes([*c]ecs_meta_type_op_t),
    op_count: i32 = @import("std").mem.zeroes(i32),
    op_cur: i32 = @import("std").mem.zeroes(i32),
    elem_cur: i32 = @import("std").mem.zeroes(i32),
    prev_depth: i32 = @import("std").mem.zeroes(i32),
    ptr: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
    comp: [*c]const EcsComponent = @import("std").mem.zeroes([*c]const EcsComponent),
    @"opaque": [*c]const EcsOpaque = @import("std").mem.zeroes([*c]const EcsOpaque),
    vector: [*c]ecs_vec_t = @import("std").mem.zeroes([*c]ecs_vec_t),
    members: [*c]ecs_hashmap_t = @import("std").mem.zeroes([*c]ecs_hashmap_t),
    is_collection: bool = @import("std").mem.zeroes(bool),
    is_inline_array: bool = @import("std").mem.zeroes(bool),
    is_empty_scope: bool = @import("std").mem.zeroes(bool),
};
pub const ecs_meta_scope_t = struct_ecs_meta_scope_t;
pub const struct_ecs_meta_cursor_t = extern struct {
    world: ?*const ecs_world_t = @import("std").mem.zeroes(?*const ecs_world_t),
    scope: [32]ecs_meta_scope_t = @import("std").mem.zeroes([32]ecs_meta_scope_t),
    depth: i32 = @import("std").mem.zeroes(i32),
    valid: bool = @import("std").mem.zeroes(bool),
    is_primitive_scope: bool = @import("std").mem.zeroes(bool),
    lookup_action: ?*const fn (?*const ecs_world_t, [*c]const u8, ?*anyopaque) callconv(.C) ecs_entity_t = @import("std").mem.zeroes(?*const fn (?*const ecs_world_t, [*c]const u8, ?*anyopaque) callconv(.C) ecs_entity_t),
    lookup_ctx: ?*anyopaque = @import("std").mem.zeroes(?*anyopaque),
};
pub const ecs_meta_cursor_t = struct_ecs_meta_cursor_t;
pub extern fn ecs_meta_cursor(world: ?*const ecs_world_t, @"type": ecs_entity_t, ptr: ?*anyopaque) ecs_meta_cursor_t;
pub extern fn ecs_meta_get_ptr(cursor: [*c]ecs_meta_cursor_t) ?*anyopaque;
pub extern fn ecs_meta_next(cursor: [*c]ecs_meta_cursor_t) c_int;
pub extern fn ecs_meta_elem(cursor: [*c]ecs_meta_cursor_t, elem: i32) c_int;
pub extern fn ecs_meta_member(cursor: [*c]ecs_meta_cursor_t, name: [*c]const u8) c_int;
pub extern fn ecs_meta_dotmember(cursor: [*c]ecs_meta_cursor_t, name: [*c]const u8) c_int;
pub extern fn ecs_meta_push(cursor: [*c]ecs_meta_cursor_t) c_int;
pub extern fn ecs_meta_pop(cursor: [*c]ecs_meta_cursor_t) c_int;
pub extern fn ecs_meta_is_collection(cursor: [*c]const ecs_meta_cursor_t) bool;
pub extern fn ecs_meta_get_type(cursor: [*c]const ecs_meta_cursor_t) ecs_entity_t;
pub extern fn ecs_meta_get_unit(cursor: [*c]const ecs_meta_cursor_t) ecs_entity_t;
pub extern fn ecs_meta_get_member(cursor: [*c]const ecs_meta_cursor_t) [*c]const u8;
pub extern fn ecs_meta_get_member_id(cursor: [*c]const ecs_meta_cursor_t) ecs_entity_t;
pub extern fn ecs_meta_set_bool(cursor: [*c]ecs_meta_cursor_t, value: bool) c_int;
pub extern fn ecs_meta_set_char(cursor: [*c]ecs_meta_cursor_t, value: u8) c_int;
pub extern fn ecs_meta_set_int(cursor: [*c]ecs_meta_cursor_t, value: i64) c_int;
pub extern fn ecs_meta_set_uint(cursor: [*c]ecs_meta_cursor_t, value: u64) c_int;
pub extern fn ecs_meta_set_float(cursor: [*c]ecs_meta_cursor_t, value: f64) c_int;
pub extern fn ecs_meta_set_string(cursor: [*c]ecs_meta_cursor_t, value: [*c]const u8) c_int;
pub extern fn ecs_meta_set_string_literal(cursor: [*c]ecs_meta_cursor_t, value: [*c]const u8) c_int;
pub extern fn ecs_meta_set_entity(cursor: [*c]ecs_meta_cursor_t, value: ecs_entity_t) c_int;
pub extern fn ecs_meta_set_id(cursor: [*c]ecs_meta_cursor_t, value: ecs_id_t) c_int;
pub extern fn ecs_meta_set_null(cursor: [*c]ecs_meta_cursor_t) c_int;
pub extern fn ecs_meta_set_value(cursor: [*c]ecs_meta_cursor_t, value: [*c]const ecs_value_t) c_int;
pub extern fn ecs_meta_get_bool(cursor: [*c]const ecs_meta_cursor_t) bool;
pub extern fn ecs_meta_get_char(cursor: [*c]const ecs_meta_cursor_t) u8;
pub extern fn ecs_meta_get_int(cursor: [*c]const ecs_meta_cursor_t) i64;
pub extern fn ecs_meta_get_uint(cursor: [*c]const ecs_meta_cursor_t) u64;
pub extern fn ecs_meta_get_float(cursor: [*c]const ecs_meta_cursor_t) f64;
pub extern fn ecs_meta_get_string(cursor: [*c]const ecs_meta_cursor_t) [*c]const u8;
pub extern fn ecs_meta_get_entity(cursor: [*c]const ecs_meta_cursor_t) ecs_entity_t;
pub extern fn ecs_meta_get_id(cursor: [*c]const ecs_meta_cursor_t) ecs_id_t;
pub extern fn ecs_meta_ptr_to_float(type_kind: ecs_primitive_kind_t, ptr: ?*const anyopaque) f64;
pub const struct_ecs_primitive_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    kind: ecs_primitive_kind_t = @import("std").mem.zeroes(ecs_primitive_kind_t),
};
pub const ecs_primitive_desc_t = struct_ecs_primitive_desc_t;
pub extern fn ecs_primitive_init(world: ?*ecs_world_t, desc: [*c]const ecs_primitive_desc_t) ecs_entity_t;
pub const struct_ecs_enum_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    constants: [32]ecs_enum_constant_t = @import("std").mem.zeroes([32]ecs_enum_constant_t),
};
pub const ecs_enum_desc_t = struct_ecs_enum_desc_t;
pub extern fn ecs_enum_init(world: ?*ecs_world_t, desc: [*c]const ecs_enum_desc_t) ecs_entity_t;
pub const struct_ecs_bitmask_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    constants: [32]ecs_bitmask_constant_t = @import("std").mem.zeroes([32]ecs_bitmask_constant_t),
};
pub const ecs_bitmask_desc_t = struct_ecs_bitmask_desc_t;
pub extern fn ecs_bitmask_init(world: ?*ecs_world_t, desc: [*c]const ecs_bitmask_desc_t) ecs_entity_t;
pub const struct_ecs_array_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    count: i32 = @import("std").mem.zeroes(i32),
};
pub const ecs_array_desc_t = struct_ecs_array_desc_t;
pub extern fn ecs_array_init(world: ?*ecs_world_t, desc: [*c]const ecs_array_desc_t) ecs_entity_t;
pub const struct_ecs_vector_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    type: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_vector_desc_t = struct_ecs_vector_desc_t;
pub extern fn ecs_vector_init(world: ?*ecs_world_t, desc: [*c]const ecs_vector_desc_t) ecs_entity_t;
pub const struct_ecs_struct_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    members: [32]ecs_member_t = @import("std").mem.zeroes([32]ecs_member_t),
};
pub const ecs_struct_desc_t = struct_ecs_struct_desc_t;
pub extern fn ecs_struct_init(world: ?*ecs_world_t, desc: [*c]const ecs_struct_desc_t) ecs_entity_t;
pub const struct_ecs_opaque_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    type: EcsOpaque = @import("std").mem.zeroes(EcsOpaque),
};
pub const ecs_opaque_desc_t = struct_ecs_opaque_desc_t;
pub extern fn ecs_opaque_init(world: ?*ecs_world_t, desc: [*c]const ecs_opaque_desc_t) ecs_entity_t;
pub const struct_ecs_unit_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    symbol: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    quantity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    base: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    over: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    translation: ecs_unit_translation_t = @import("std").mem.zeroes(ecs_unit_translation_t),
    prefix: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
};
pub const ecs_unit_desc_t = struct_ecs_unit_desc_t;
pub extern fn ecs_unit_init(world: ?*ecs_world_t, desc: [*c]const ecs_unit_desc_t) ecs_entity_t;
pub const struct_ecs_unit_prefix_desc_t = extern struct {
    entity: ecs_entity_t = @import("std").mem.zeroes(ecs_entity_t),
    symbol: [*c]const u8 = @import("std").mem.zeroes([*c]const u8),
    translation: ecs_unit_translation_t = @import("std").mem.zeroes(ecs_unit_translation_t),
};
pub const ecs_unit_prefix_desc_t = struct_ecs_unit_prefix_desc_t;
pub extern fn ecs_unit_prefix_init(world: ?*ecs_world_t, desc: [*c]const ecs_unit_prefix_desc_t) ecs_entity_t;
pub extern fn ecs_quantity_init(world: ?*ecs_world_t, desc: [*c]const ecs_entity_desc_t) ecs_entity_t;
pub extern fn FlecsMetaImport(world: ?*ecs_world_t) void;
pub extern fn ecs_meta_from_desc(world: ?*ecs_world_t, component: ecs_entity_t, kind: ecs_type_kind_t, desc: [*c]const u8) c_int;
pub extern fn ecs_set_os_api_impl() void;
pub extern fn ecs_import(world: ?*ecs_world_t, module: ecs_module_action_t, module_name: [*c]const u8) ecs_entity_t;
pub extern fn ecs_import_c(world: ?*ecs_world_t, module: ecs_module_action_t, module_name_c: [*c]const u8) ecs_entity_t;
pub extern fn ecs_import_from_library(world: ?*ecs_world_t, library_name: [*c]const u8, module_name: [*c]const u8) ecs_entity_t;
pub extern fn ecs_module_init(world: ?*ecs_world_t, c_name: [*c]const u8, desc: [*c]const ecs_component_desc_t) ecs_entity_t;
pub extern fn ecs_cpp_get_type_name(type_name: [*c]u8, func_name: [*c]const u8, len: usize, front_len: usize) [*c]u8;
pub extern fn ecs_cpp_get_symbol_name(symbol_name: [*c]u8, type_name: [*c]const u8, len: usize) [*c]u8;
pub extern fn ecs_cpp_get_constant_name(constant_name: [*c]u8, func_name: [*c]const u8, len: usize, back_len: usize) [*c]u8;
pub extern fn ecs_cpp_trim_module(world: ?*ecs_world_t, type_name: [*c]const u8) [*c]const u8;
pub extern fn ecs_cpp_component_validate(world: ?*ecs_world_t, id: ecs_entity_t, name: [*c]const u8, symbol: [*c]const u8, size: usize, alignment: usize, implicit_name: bool) void;
pub extern fn ecs_cpp_component_register(world: ?*ecs_world_t, id: ecs_entity_t, name: [*c]const u8, symbol: [*c]const u8, size: ecs_size_t, alignment: ecs_size_t, implicit_name: bool, existing_out: [*c]bool) ecs_entity_t;
pub extern fn ecs_cpp_component_register_explicit(world: ?*ecs_world_t, s_id: ecs_entity_t, id: ecs_entity_t, name: [*c]const u8, type_name: [*c]const u8, symbol: [*c]const u8, size: usize, alignment: usize, is_component: bool, existing_out: [*c]bool) ecs_entity_t;
pub extern fn ecs_cpp_enum_init(world: ?*ecs_world_t, id: ecs_entity_t) void;
pub extern fn ecs_cpp_enum_constant_register(world: ?*ecs_world_t, parent: ecs_entity_t, id: ecs_entity_t, name: [*c]const u8, value: c_int) ecs_entity_t;
pub extern fn ecs_cpp_reset_count_get() i32;
pub extern fn ecs_cpp_reset_count_inc() i32;
pub extern fn ecs_cpp_last_member(world: ?*const ecs_world_t, @"type": ecs_entity_t) [*c]const ecs_member_t;
pub const __llvm__ = @as(c_int, 1);
pub const __clang__ = @as(c_int, 1);
pub const __clang_major__ = @as(c_int, 18);
pub const __clang_minor__ = @as(c_int, 1);
pub const __clang_patchlevel__ = @as(c_int, 6);
pub const __clang_version__ = "18.1.6 ";
pub const __GNUC__ = @as(c_int, 4);
pub const __GNUC_MINOR__ = @as(c_int, 2);
pub const __GNUC_PATCHLEVEL__ = @as(c_int, 1);
pub const __GXX_ABI_VERSION = @as(c_int, 1002);
pub const __ATOMIC_RELAXED = @as(c_int, 0);
pub const __ATOMIC_CONSUME = @as(c_int, 1);
pub const __ATOMIC_ACQUIRE = @as(c_int, 2);
pub const __ATOMIC_RELEASE = @as(c_int, 3);
pub const __ATOMIC_ACQ_REL = @as(c_int, 4);
pub const __ATOMIC_SEQ_CST = @as(c_int, 5);
pub const __MEMORY_SCOPE_SYSTEM = @as(c_int, 0);
pub const __MEMORY_SCOPE_DEVICE = @as(c_int, 1);
pub const __MEMORY_SCOPE_WRKGRP = @as(c_int, 2);
pub const __MEMORY_SCOPE_WVFRNT = @as(c_int, 3);
pub const __MEMORY_SCOPE_SINGLE = @as(c_int, 4);
pub const __OPENCL_MEMORY_SCOPE_WORK_ITEM = @as(c_int, 0);
pub const __OPENCL_MEMORY_SCOPE_WORK_GROUP = @as(c_int, 1);
pub const __OPENCL_MEMORY_SCOPE_DEVICE = @as(c_int, 2);
pub const __OPENCL_MEMORY_SCOPE_ALL_SVM_DEVICES = @as(c_int, 3);
pub const __OPENCL_MEMORY_SCOPE_SUB_GROUP = @as(c_int, 4);
pub const __FPCLASS_SNAN = @as(c_int, 0x0001);
pub const __FPCLASS_QNAN = @as(c_int, 0x0002);
pub const __FPCLASS_NEGINF = @as(c_int, 0x0004);
pub const __FPCLASS_NEGNORMAL = @as(c_int, 0x0008);
pub const __FPCLASS_NEGSUBNORMAL = @as(c_int, 0x0010);
pub const __FPCLASS_NEGZERO = @as(c_int, 0x0020);
pub const __FPCLASS_POSZERO = @as(c_int, 0x0040);
pub const __FPCLASS_POSSUBNORMAL = @as(c_int, 0x0080);
pub const __FPCLASS_POSNORMAL = @as(c_int, 0x0100);
pub const __FPCLASS_POSINF = @as(c_int, 0x0200);
pub const __PRAGMA_REDEFINE_EXTNAME = @as(c_int, 1);
pub const __VERSION__ = "Homebrew Clang 18.1.6";
pub const __OBJC_BOOL_IS_BOOL = @as(c_int, 1);
pub const __CONSTANT_CFSTRINGS__ = @as(c_int, 1);
pub const __block = @compileError("unable to translate macro: undefined identifier `__blocks__`");
// (no file):42:9
pub const __BLOCKS__ = @as(c_int, 1);
pub const __clang_literal_encoding__ = "UTF-8";
pub const __clang_wide_literal_encoding__ = "UTF-32";
pub const __ORDER_LITTLE_ENDIAN__ = @as(c_int, 1234);
pub const __ORDER_BIG_ENDIAN__ = @as(c_int, 4321);
pub const __ORDER_PDP_ENDIAN__ = @as(c_int, 3412);
pub const __BYTE_ORDER__ = __ORDER_LITTLE_ENDIAN__;
pub const __LITTLE_ENDIAN__ = @as(c_int, 1);
pub const _LP64 = @as(c_int, 1);
pub const __LP64__ = @as(c_int, 1);
pub const __CHAR_BIT__ = @as(c_int, 8);
pub const __BOOL_WIDTH__ = @as(c_int, 8);
pub const __SHRT_WIDTH__ = @as(c_int, 16);
pub const __INT_WIDTH__ = @as(c_int, 32);
pub const __LONG_WIDTH__ = @as(c_int, 64);
pub const __LLONG_WIDTH__ = @as(c_int, 64);
pub const __BITINT_MAXWIDTH__ = @as(c_int, 128);
pub const __SCHAR_MAX__ = @as(c_int, 127);
pub const __SHRT_MAX__ = @as(c_int, 32767);
pub const __INT_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __LONG_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_long, 9223372036854775807, .decimal);
pub const __LONG_LONG_MAX__ = @as(c_longlong, 9223372036854775807);
pub const __WCHAR_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __WCHAR_WIDTH__ = @as(c_int, 32);
pub const __WINT_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __WINT_WIDTH__ = @as(c_int, 32);
pub const __INTMAX_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_long, 9223372036854775807, .decimal);
pub const __INTMAX_WIDTH__ = @as(c_int, 64);
pub const __SIZE_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_ulong, 18446744073709551615, .decimal);
pub const __SIZE_WIDTH__ = @as(c_int, 64);
pub const __UINTMAX_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_ulong, 18446744073709551615, .decimal);
pub const __UINTMAX_WIDTH__ = @as(c_int, 64);
pub const __PTRDIFF_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_long, 9223372036854775807, .decimal);
pub const __PTRDIFF_WIDTH__ = @as(c_int, 64);
pub const __INTPTR_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_long, 9223372036854775807, .decimal);
pub const __INTPTR_WIDTH__ = @as(c_int, 64);
pub const __UINTPTR_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_ulong, 18446744073709551615, .decimal);
pub const __UINTPTR_WIDTH__ = @as(c_int, 64);
pub const __SIZEOF_DOUBLE__ = @as(c_int, 8);
pub const __SIZEOF_FLOAT__ = @as(c_int, 4);
pub const __SIZEOF_INT__ = @as(c_int, 4);
pub const __SIZEOF_LONG__ = @as(c_int, 8);
pub const __SIZEOF_LONG_DOUBLE__ = @as(c_int, 8);
pub const __SIZEOF_LONG_LONG__ = @as(c_int, 8);
pub const __SIZEOF_POINTER__ = @as(c_int, 8);
pub const __SIZEOF_SHORT__ = @as(c_int, 2);
pub const __SIZEOF_PTRDIFF_T__ = @as(c_int, 8);
pub const __SIZEOF_SIZE_T__ = @as(c_int, 8);
pub const __SIZEOF_WCHAR_T__ = @as(c_int, 4);
pub const __SIZEOF_WINT_T__ = @as(c_int, 4);
pub const __SIZEOF_INT128__ = @as(c_int, 16);
pub const __INTMAX_TYPE__ = c_long;
pub const __INTMAX_FMTd__ = "ld";
pub const __INTMAX_FMTi__ = "li";
pub const __INTMAX_C_SUFFIX__ = @compileError("unable to translate macro: undefined identifier `L`");
// (no file):97:9
pub const __UINTMAX_TYPE__ = c_ulong;
pub const __UINTMAX_FMTo__ = "lo";
pub const __UINTMAX_FMTu__ = "lu";
pub const __UINTMAX_FMTx__ = "lx";
pub const __UINTMAX_FMTX__ = "lX";
pub const __UINTMAX_C_SUFFIX__ = @compileError("unable to translate macro: undefined identifier `UL`");
// (no file):103:9
pub const __PTRDIFF_TYPE__ = c_long;
pub const __PTRDIFF_FMTd__ = "ld";
pub const __PTRDIFF_FMTi__ = "li";
pub const __INTPTR_TYPE__ = c_long;
pub const __INTPTR_FMTd__ = "ld";
pub const __INTPTR_FMTi__ = "li";
pub const __SIZE_TYPE__ = c_ulong;
pub const __SIZE_FMTo__ = "lo";
pub const __SIZE_FMTu__ = "lu";
pub const __SIZE_FMTx__ = "lx";
pub const __SIZE_FMTX__ = "lX";
pub const __WCHAR_TYPE__ = c_int;
pub const __WINT_TYPE__ = c_int;
pub const __SIG_ATOMIC_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __SIG_ATOMIC_WIDTH__ = @as(c_int, 32);
pub const __CHAR16_TYPE__ = c_ushort;
pub const __CHAR32_TYPE__ = c_uint;
pub const __UINTPTR_TYPE__ = c_ulong;
pub const __UINTPTR_FMTo__ = "lo";
pub const __UINTPTR_FMTu__ = "lu";
pub const __UINTPTR_FMTx__ = "lx";
pub const __UINTPTR_FMTX__ = "lX";
pub const __FLT16_DENORM_MIN__ = @as(f16, 5.9604644775390625e-8);
pub const __FLT16_HAS_DENORM__ = @as(c_int, 1);
pub const __FLT16_DIG__ = @as(c_int, 3);
pub const __FLT16_DECIMAL_DIG__ = @as(c_int, 5);
pub const __FLT16_EPSILON__ = @as(f16, 9.765625e-4);
pub const __FLT16_HAS_INFINITY__ = @as(c_int, 1);
pub const __FLT16_HAS_QUIET_NAN__ = @as(c_int, 1);
pub const __FLT16_MANT_DIG__ = @as(c_int, 11);
pub const __FLT16_MAX_10_EXP__ = @as(c_int, 4);
pub const __FLT16_MAX_EXP__ = @as(c_int, 16);
pub const __FLT16_MAX__ = @as(f16, 6.5504e+4);
pub const __FLT16_MIN_10_EXP__ = -@as(c_int, 4);
pub const __FLT16_MIN_EXP__ = -@as(c_int, 13);
pub const __FLT16_MIN__ = @as(f16, 6.103515625e-5);
pub const __FLT_DENORM_MIN__ = @as(f32, 1.40129846e-45);
pub const __FLT_HAS_DENORM__ = @as(c_int, 1);
pub const __FLT_DIG__ = @as(c_int, 6);
pub const __FLT_DECIMAL_DIG__ = @as(c_int, 9);
pub const __FLT_EPSILON__ = @as(f32, 1.19209290e-7);
pub const __FLT_HAS_INFINITY__ = @as(c_int, 1);
pub const __FLT_HAS_QUIET_NAN__ = @as(c_int, 1);
pub const __FLT_MANT_DIG__ = @as(c_int, 24);
pub const __FLT_MAX_10_EXP__ = @as(c_int, 38);
pub const __FLT_MAX_EXP__ = @as(c_int, 128);
pub const __FLT_MAX__ = @as(f32, 3.40282347e+38);
pub const __FLT_MIN_10_EXP__ = -@as(c_int, 37);
pub const __FLT_MIN_EXP__ = -@as(c_int, 125);
pub const __FLT_MIN__ = @as(f32, 1.17549435e-38);
pub const __DBL_DENORM_MIN__ = @as(f64, 4.9406564584124654e-324);
pub const __DBL_HAS_DENORM__ = @as(c_int, 1);
pub const __DBL_DIG__ = @as(c_int, 15);
pub const __DBL_DECIMAL_DIG__ = @as(c_int, 17);
pub const __DBL_EPSILON__ = @as(f64, 2.2204460492503131e-16);
pub const __DBL_HAS_INFINITY__ = @as(c_int, 1);
pub const __DBL_HAS_QUIET_NAN__ = @as(c_int, 1);
pub const __DBL_MANT_DIG__ = @as(c_int, 53);
pub const __DBL_MAX_10_EXP__ = @as(c_int, 308);
pub const __DBL_MAX_EXP__ = @as(c_int, 1024);
pub const __DBL_MAX__ = @as(f64, 1.7976931348623157e+308);
pub const __DBL_MIN_10_EXP__ = -@as(c_int, 307);
pub const __DBL_MIN_EXP__ = -@as(c_int, 1021);
pub const __DBL_MIN__ = @as(f64, 2.2250738585072014e-308);
pub const __LDBL_DENORM_MIN__ = @as(c_longdouble, 4.9406564584124654e-324);
pub const __LDBL_HAS_DENORM__ = @as(c_int, 1);
pub const __LDBL_DIG__ = @as(c_int, 15);
pub const __LDBL_DECIMAL_DIG__ = @as(c_int, 17);
pub const __LDBL_EPSILON__ = @as(c_longdouble, 2.2204460492503131e-16);
pub const __LDBL_HAS_INFINITY__ = @as(c_int, 1);
pub const __LDBL_HAS_QUIET_NAN__ = @as(c_int, 1);
pub const __LDBL_MANT_DIG__ = @as(c_int, 53);
pub const __LDBL_MAX_10_EXP__ = @as(c_int, 308);
pub const __LDBL_MAX_EXP__ = @as(c_int, 1024);
pub const __LDBL_MAX__ = @as(c_longdouble, 1.7976931348623157e+308);
pub const __LDBL_MIN_10_EXP__ = -@as(c_int, 307);
pub const __LDBL_MIN_EXP__ = -@as(c_int, 1021);
pub const __LDBL_MIN__ = @as(c_longdouble, 2.2250738585072014e-308);
pub const __POINTER_WIDTH__ = @as(c_int, 64);
pub const __BIGGEST_ALIGNMENT__ = @as(c_int, 8);
pub const __INT8_TYPE__ = i8;
pub const __INT8_FMTd__ = "hhd";
pub const __INT8_FMTi__ = "hhi";
pub const __INT8_C_SUFFIX__ = "";
pub const __INT16_TYPE__ = c_short;
pub const __INT16_FMTd__ = "hd";
pub const __INT16_FMTi__ = "hi";
pub const __INT16_C_SUFFIX__ = "";
pub const __INT32_TYPE__ = c_int;
pub const __INT32_FMTd__ = "d";
pub const __INT32_FMTi__ = "i";
pub const __INT32_C_SUFFIX__ = "";
pub const __INT64_TYPE__ = c_longlong;
pub const __INT64_FMTd__ = "lld";
pub const __INT64_FMTi__ = "lli";
pub const __INT64_C_SUFFIX__ = @compileError("unable to translate macro: undefined identifier `LL`");
// (no file):199:9
pub const __UINT8_TYPE__ = u8;
pub const __UINT8_FMTo__ = "hho";
pub const __UINT8_FMTu__ = "hhu";
pub const __UINT8_FMTx__ = "hhx";
pub const __UINT8_FMTX__ = "hhX";
pub const __UINT8_C_SUFFIX__ = "";
pub const __UINT8_MAX__ = @as(c_int, 255);
pub const __INT8_MAX__ = @as(c_int, 127);
pub const __UINT16_TYPE__ = c_ushort;
pub const __UINT16_FMTo__ = "ho";
pub const __UINT16_FMTu__ = "hu";
pub const __UINT16_FMTx__ = "hx";
pub const __UINT16_FMTX__ = "hX";
pub const __UINT16_C_SUFFIX__ = "";
pub const __UINT16_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 65535, .decimal);
pub const __INT16_MAX__ = @as(c_int, 32767);
pub const __UINT32_TYPE__ = c_uint;
pub const __UINT32_FMTo__ = "o";
pub const __UINT32_FMTu__ = "u";
pub const __UINT32_FMTx__ = "x";
pub const __UINT32_FMTX__ = "X";
pub const __UINT32_C_SUFFIX__ = @compileError("unable to translate macro: undefined identifier `U`");
// (no file):221:9
pub const __UINT32_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_uint, 4294967295, .decimal);
pub const __INT32_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __UINT64_TYPE__ = c_ulonglong;
pub const __UINT64_FMTo__ = "llo";
pub const __UINT64_FMTu__ = "llu";
pub const __UINT64_FMTx__ = "llx";
pub const __UINT64_FMTX__ = "llX";
pub const __UINT64_C_SUFFIX__ = @compileError("unable to translate macro: undefined identifier `ULL`");
// (no file):229:9
pub const __UINT64_MAX__ = @as(c_ulonglong, 18446744073709551615);
pub const __INT64_MAX__ = @as(c_longlong, 9223372036854775807);
pub const __INT_LEAST8_TYPE__ = i8;
pub const __INT_LEAST8_MAX__ = @as(c_int, 127);
pub const __INT_LEAST8_WIDTH__ = @as(c_int, 8);
pub const __INT_LEAST8_FMTd__ = "hhd";
pub const __INT_LEAST8_FMTi__ = "hhi";
pub const __UINT_LEAST8_TYPE__ = u8;
pub const __UINT_LEAST8_MAX__ = @as(c_int, 255);
pub const __UINT_LEAST8_FMTo__ = "hho";
pub const __UINT_LEAST8_FMTu__ = "hhu";
pub const __UINT_LEAST8_FMTx__ = "hhx";
pub const __UINT_LEAST8_FMTX__ = "hhX";
pub const __INT_LEAST16_TYPE__ = c_short;
pub const __INT_LEAST16_MAX__ = @as(c_int, 32767);
pub const __INT_LEAST16_WIDTH__ = @as(c_int, 16);
pub const __INT_LEAST16_FMTd__ = "hd";
pub const __INT_LEAST16_FMTi__ = "hi";
pub const __UINT_LEAST16_TYPE__ = c_ushort;
pub const __UINT_LEAST16_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 65535, .decimal);
pub const __UINT_LEAST16_FMTo__ = "ho";
pub const __UINT_LEAST16_FMTu__ = "hu";
pub const __UINT_LEAST16_FMTx__ = "hx";
pub const __UINT_LEAST16_FMTX__ = "hX";
pub const __INT_LEAST32_TYPE__ = c_int;
pub const __INT_LEAST32_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __INT_LEAST32_WIDTH__ = @as(c_int, 32);
pub const __INT_LEAST32_FMTd__ = "d";
pub const __INT_LEAST32_FMTi__ = "i";
pub const __UINT_LEAST32_TYPE__ = c_uint;
pub const __UINT_LEAST32_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_uint, 4294967295, .decimal);
pub const __UINT_LEAST32_FMTo__ = "o";
pub const __UINT_LEAST32_FMTu__ = "u";
pub const __UINT_LEAST32_FMTx__ = "x";
pub const __UINT_LEAST32_FMTX__ = "X";
pub const __INT_LEAST64_TYPE__ = c_longlong;
pub const __INT_LEAST64_MAX__ = @as(c_longlong, 9223372036854775807);
pub const __INT_LEAST64_WIDTH__ = @as(c_int, 64);
pub const __INT_LEAST64_FMTd__ = "lld";
pub const __INT_LEAST64_FMTi__ = "lli";
pub const __UINT_LEAST64_TYPE__ = c_ulonglong;
pub const __UINT_LEAST64_MAX__ = @as(c_ulonglong, 18446744073709551615);
pub const __UINT_LEAST64_FMTo__ = "llo";
pub const __UINT_LEAST64_FMTu__ = "llu";
pub const __UINT_LEAST64_FMTx__ = "llx";
pub const __UINT_LEAST64_FMTX__ = "llX";
pub const __INT_FAST8_TYPE__ = i8;
pub const __INT_FAST8_MAX__ = @as(c_int, 127);
pub const __INT_FAST8_WIDTH__ = @as(c_int, 8);
pub const __INT_FAST8_FMTd__ = "hhd";
pub const __INT_FAST8_FMTi__ = "hhi";
pub const __UINT_FAST8_TYPE__ = u8;
pub const __UINT_FAST8_MAX__ = @as(c_int, 255);
pub const __UINT_FAST8_FMTo__ = "hho";
pub const __UINT_FAST8_FMTu__ = "hhu";
pub const __UINT_FAST8_FMTx__ = "hhx";
pub const __UINT_FAST8_FMTX__ = "hhX";
pub const __INT_FAST16_TYPE__ = c_short;
pub const __INT_FAST16_MAX__ = @as(c_int, 32767);
pub const __INT_FAST16_WIDTH__ = @as(c_int, 16);
pub const __INT_FAST16_FMTd__ = "hd";
pub const __INT_FAST16_FMTi__ = "hi";
pub const __UINT_FAST16_TYPE__ = c_ushort;
pub const __UINT_FAST16_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 65535, .decimal);
pub const __UINT_FAST16_FMTo__ = "ho";
pub const __UINT_FAST16_FMTu__ = "hu";
pub const __UINT_FAST16_FMTx__ = "hx";
pub const __UINT_FAST16_FMTX__ = "hX";
pub const __INT_FAST32_TYPE__ = c_int;
pub const __INT_FAST32_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const __INT_FAST32_WIDTH__ = @as(c_int, 32);
pub const __INT_FAST32_FMTd__ = "d";
pub const __INT_FAST32_FMTi__ = "i";
pub const __UINT_FAST32_TYPE__ = c_uint;
pub const __UINT_FAST32_MAX__ = @import("std").zig.c_translation.promoteIntLiteral(c_uint, 4294967295, .decimal);
pub const __UINT_FAST32_FMTo__ = "o";
pub const __UINT_FAST32_FMTu__ = "u";
pub const __UINT_FAST32_FMTx__ = "x";
pub const __UINT_FAST32_FMTX__ = "X";
pub const __INT_FAST64_TYPE__ = c_longlong;
pub const __INT_FAST64_MAX__ = @as(c_longlong, 9223372036854775807);
pub const __INT_FAST64_WIDTH__ = @as(c_int, 64);
pub const __INT_FAST64_FMTd__ = "lld";
pub const __INT_FAST64_FMTi__ = "lli";
pub const __UINT_FAST64_TYPE__ = c_ulonglong;
pub const __UINT_FAST64_MAX__ = @as(c_ulonglong, 18446744073709551615);
pub const __UINT_FAST64_FMTo__ = "llo";
pub const __UINT_FAST64_FMTu__ = "llu";
pub const __UINT_FAST64_FMTx__ = "llx";
pub const __UINT_FAST64_FMTX__ = "llX";
pub const __USER_LABEL_PREFIX__ = @compileError("unable to translate macro: undefined identifier `_`");
// (no file):320:9
pub const __NO_MATH_ERRNO__ = @as(c_int, 1);
pub const __FINITE_MATH_ONLY__ = @as(c_int, 0);
pub const __GNUC_STDC_INLINE__ = @as(c_int, 1);
pub const __GCC_ATOMIC_TEST_AND_SET_TRUEVAL = @as(c_int, 1);
pub const __CLANG_ATOMIC_BOOL_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_CHAR_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_CHAR16_T_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_CHAR32_T_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_WCHAR_T_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_SHORT_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_INT_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_LONG_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_LLONG_LOCK_FREE = @as(c_int, 2);
pub const __CLANG_ATOMIC_POINTER_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_BOOL_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_CHAR_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_CHAR16_T_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_CHAR32_T_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_WCHAR_T_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_SHORT_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_INT_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_LONG_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_LLONG_LOCK_FREE = @as(c_int, 2);
pub const __GCC_ATOMIC_POINTER_LOCK_FREE = @as(c_int, 2);
pub const __NO_INLINE__ = @as(c_int, 1);
pub const __PIC__ = @as(c_int, 2);
pub const __pic__ = @as(c_int, 2);
pub const __FLT_RADIX__ = @as(c_int, 2);
pub const __DECIMAL_DIG__ = __LDBL_DECIMAL_DIG__;
pub const __SSP_STRONG__ = @as(c_int, 2);
pub const __nonnull = @compileError("unable to translate macro: undefined identifier `_Nonnull`");
// (no file):351:9
pub const __null_unspecified = @compileError("unable to translate macro: undefined identifier `_Null_unspecified`");
// (no file):352:9
pub const __nullable = @compileError("unable to translate macro: undefined identifier `_Nullable`");
// (no file):353:9
pub const TARGET_OS_WIN32 = @as(c_int, 0);
pub const TARGET_OS_WINDOWS = @as(c_int, 0);
pub const TARGET_OS_LINUX = @as(c_int, 0);
pub const TARGET_OS_UNIX = @as(c_int, 0);
pub const TARGET_OS_MAC = @as(c_int, 1);
pub const TARGET_OS_OSX = @as(c_int, 1);
pub const TARGET_OS_IPHONE = @as(c_int, 0);
pub const TARGET_OS_IOS = @as(c_int, 0);
pub const TARGET_OS_TV = @as(c_int, 0);
pub const TARGET_OS_WATCH = @as(c_int, 0);
pub const TARGET_OS_DRIVERKIT = @as(c_int, 0);
pub const TARGET_OS_MACCATALYST = @as(c_int, 0);
pub const TARGET_OS_SIMULATOR = @as(c_int, 0);
pub const TARGET_OS_EMBEDDED = @as(c_int, 0);
pub const TARGET_OS_NANO = @as(c_int, 0);
pub const TARGET_IPHONE_SIMULATOR = @as(c_int, 0);
pub const TARGET_OS_UIKITFORMAC = @as(c_int, 0);
pub const __AARCH64EL__ = @as(c_int, 1);
pub const __aarch64__ = @as(c_int, 1);
pub const __GCC_ASM_FLAG_OUTPUTS__ = @as(c_int, 1);
pub const __AARCH64_CMODEL_SMALL__ = @as(c_int, 1);
pub const __ARM_ACLE = @as(c_int, 200);
pub const __ARM_ARCH = @as(c_int, 8);
pub const __ARM_ARCH_PROFILE = 'A';
pub const __ARM_64BIT_STATE = @as(c_int, 1);
pub const __ARM_PCS_AAPCS64 = @as(c_int, 1);
pub const __ARM_ARCH_ISA_A64 = @as(c_int, 1);
pub const __ARM_FEATURE_CLZ = @as(c_int, 1);
pub const __ARM_FEATURE_FMA = @as(c_int, 1);
pub const __ARM_FEATURE_LDREX = @as(c_int, 0xF);
pub const __ARM_FEATURE_IDIV = @as(c_int, 1);
pub const __ARM_FEATURE_DIV = @as(c_int, 1);
pub const __ARM_FEATURE_NUMERIC_MAXMIN = @as(c_int, 1);
pub const __ARM_FEATURE_DIRECTED_ROUNDING = @as(c_int, 1);
pub const __ARM_ALIGN_MAX_STACK_PWR = @as(c_int, 4);
pub const __ARM_STATE_ZA = @as(c_int, 1);
pub const __ARM_STATE_ZT0 = @as(c_int, 1);
pub const __ARM_FP = @as(c_int, 0xE);
pub const __ARM_FP16_FORMAT_IEEE = @as(c_int, 1);
pub const __ARM_FP16_ARGS = @as(c_int, 1);
pub const __ARM_SIZEOF_WCHAR_T = @as(c_int, 4);
pub const __ARM_SIZEOF_MINIMAL_ENUM = @as(c_int, 4);
pub const __ARM_NEON = @as(c_int, 1);
pub const __ARM_NEON_FP = @as(c_int, 0xE);
pub const __ARM_FEATURE_UNALIGNED = @as(c_int, 1);
pub const __GCC_HAVE_SYNC_COMPARE_AND_SWAP_1 = @as(c_int, 1);
pub const __GCC_HAVE_SYNC_COMPARE_AND_SWAP_2 = @as(c_int, 1);
pub const __GCC_HAVE_SYNC_COMPARE_AND_SWAP_4 = @as(c_int, 1);
pub const __GCC_HAVE_SYNC_COMPARE_AND_SWAP_8 = @as(c_int, 1);
pub const __GCC_HAVE_SYNC_COMPARE_AND_SWAP_16 = @as(c_int, 1);
pub const __FP_FAST_FMA = @as(c_int, 1);
pub const __FP_FAST_FMAF = @as(c_int, 1);
pub const __AARCH64_SIMD__ = @as(c_int, 1);
pub const __ARM64_ARCH_8__ = @as(c_int, 1);
pub const __ARM_NEON__ = @as(c_int, 1);
pub const __REGISTER_PREFIX__ = "";
pub const __arm64 = @as(c_int, 1);
pub const __arm64__ = @as(c_int, 1);
pub const __APPLE_CC__ = @as(c_int, 6000);
pub const __APPLE__ = @as(c_int, 1);
pub const __STDC_NO_THREADS__ = @as(c_int, 1);
pub const __weak = @compileError("unable to translate macro: undefined identifier `objc_gc`");
// (no file):415:9
pub const __strong = "";
pub const __unsafe_unretained = "";
pub const __DYNAMIC__ = @as(c_int, 1);
pub const __ENVIRONMENT_MAC_OS_X_VERSION_MIN_REQUIRED__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140100, .decimal);
pub const __ENVIRONMENT_OS_VERSION_MIN_REQUIRED__ = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140100, .decimal);
pub const __MACH__ = @as(c_int, 1);
pub const __STDC__ = @as(c_int, 1);
pub const __STDC_HOSTED__ = @as(c_int, 1);
pub const __STDC_VERSION__ = @as(c_long, 201710);
pub const __STDC_UTF_16__ = @as(c_int, 1);
pub const __STDC_UTF_32__ = @as(c_int, 1);
pub const _DEBUG = @as(c_int, 1);
pub const __GCC_HAVE_DWARF2_CFI_ASM = @as(c_int, 1);
pub const flecs_STATIC = "";
pub const FLECS_H = "";
pub const FLECS_VERSION_MAJOR = @as(c_int, 4);
pub const FLECS_VERSION_MINOR = @as(c_int, 0);
pub const FLECS_VERSION_PATCH = @as(c_int, 1);
pub const FLECS_VERSION = FLECS_VERSION_IMPL(FLECS_VERSION_MAJOR, FLECS_VERSION_MINOR, FLECS_VERSION_PATCH);
pub const ecs_float_t = f32;
pub const ecs_ftime_t = ecs_float_t;
pub const FLECS_DEBUG = "";
pub const FLECS_CPP = "";
pub const FLECS_MODULE = "";
pub const FLECS_SCRIPT = "";
pub const FLECS_STATS = "";
pub const FLECS_METRICS = "";
pub const FLECS_ALERTS = "";
pub const FLECS_SYSTEM = "";
pub const FLECS_PIPELINE = "";
pub const FLECS_TIMER = "";
pub const FLECS_META = "";
pub const FLECS_UNITS = "";
pub const FLECS_JSON = "";
pub const FLECS_DOC = "";
pub const FLECS_LOG = "";
pub const FLECS_APP = "";
pub const FLECS_OS_API_IMPL = "";
pub const FLECS_HTTP = "";
pub const FLECS_REST = "";
pub const FLECS_HI_COMPONENT_ID = @as(c_int, 256);
pub const FLECS_HI_ID_RECORD_ID = @as(c_int, 1024);
pub const FLECS_SPARSE_PAGE_BITS = @as(c_int, 12);
pub const FLECS_ENTITY_PAGE_BITS = @as(c_int, 12);
pub const FLECS_ID_DESC_MAX = @as(c_int, 32);
pub const FLECS_EVENT_DESC_MAX = @as(c_int, 8);
pub const FLECS_VARIABLE_COUNT_MAX = @as(c_int, 64);
pub const FLECS_TERM_COUNT_MAX = @as(c_int, 32);
pub const FLECS_TERM_ARG_COUNT_MAX = @as(c_int, 16);
pub const FLECS_QUERY_VARIABLE_COUNT_MAX = @as(c_int, 64);
pub const FLECS_QUERY_SCOPE_NESTING_MAX = @as(c_int, 8);
pub const FLECS_API_DEFINES_H = "";
pub const FLECS_API_FLAGS_H = "";
pub const EcsWorldQuitWorkers = @as(c_uint, 1) << @as(c_int, 0);
pub const EcsWorldReadonly = @as(c_uint, 1) << @as(c_int, 1);
pub const EcsWorldInit = @as(c_uint, 1) << @as(c_int, 2);
pub const EcsWorldQuit = @as(c_uint, 1) << @as(c_int, 3);
pub const EcsWorldFini = @as(c_uint, 1) << @as(c_int, 4);
pub const EcsWorldMeasureFrameTime = @as(c_uint, 1) << @as(c_int, 5);
pub const EcsWorldMeasureSystemTime = @as(c_uint, 1) << @as(c_int, 6);
pub const EcsWorldMultiThreaded = @as(c_uint, 1) << @as(c_int, 7);
pub const EcsWorldFrameInProgress = @as(c_uint, 1) << @as(c_int, 8);
pub const EcsOsApiHighResolutionTimer = @as(c_uint, 1) << @as(c_int, 0);
pub const EcsOsApiLogWithColors = @as(c_uint, 1) << @as(c_int, 1);
pub const EcsOsApiLogWithTimeStamp = @as(c_uint, 1) << @as(c_int, 2);
pub const EcsOsApiLogWithTimeDelta = @as(c_uint, 1) << @as(c_int, 3);
pub const EcsEntityIsId = @as(c_uint, 1) << @as(c_int, 31);
pub const EcsEntityIsTarget = @as(c_uint, 1) << @as(c_int, 30);
pub const EcsEntityIsTraversable = @as(c_uint, 1) << @as(c_int, 29);
pub const EcsIdOnDeleteRemove = @as(c_uint, 1) << @as(c_int, 0);
pub const EcsIdOnDeleteDelete = @as(c_uint, 1) << @as(c_int, 1);
pub const EcsIdOnDeletePanic = @as(c_uint, 1) << @as(c_int, 2);
pub const EcsIdOnDeleteMask = (EcsIdOnDeletePanic | EcsIdOnDeleteRemove) | EcsIdOnDeleteDelete;
pub const EcsIdOnDeleteObjectRemove = @as(c_uint, 1) << @as(c_int, 3);
pub const EcsIdOnDeleteObjectDelete = @as(c_uint, 1) << @as(c_int, 4);
pub const EcsIdOnDeleteObjectPanic = @as(c_uint, 1) << @as(c_int, 5);
pub const EcsIdOnDeleteObjectMask = (EcsIdOnDeleteObjectPanic | EcsIdOnDeleteObjectRemove) | EcsIdOnDeleteObjectDelete;
pub const EcsIdOnInstantiateOverride = @as(c_uint, 1) << @as(c_int, 6);
pub const EcsIdOnInstantiateInherit = @as(c_uint, 1) << @as(c_int, 7);
pub const EcsIdOnInstantiateDontInherit = @as(c_uint, 1) << @as(c_int, 8);
pub const EcsIdOnInstantiateMask = (EcsIdOnInstantiateOverride | EcsIdOnInstantiateInherit) | EcsIdOnInstantiateDontInherit;
pub const EcsIdExclusive = @as(c_uint, 1) << @as(c_int, 9);
pub const EcsIdTraversable = @as(c_uint, 1) << @as(c_int, 10);
pub const EcsIdTag = @as(c_uint, 1) << @as(c_int, 11);
pub const EcsIdWith = @as(c_uint, 1) << @as(c_int, 12);
pub const EcsIdCanToggle = @as(c_uint, 1) << @as(c_int, 13);
pub const EcsIdIsTransitive = @as(c_uint, 1) << @as(c_int, 14);
pub const EcsIdHasOnAdd = @as(c_uint, 1) << @as(c_int, 16);
pub const EcsIdHasOnRemove = @as(c_uint, 1) << @as(c_int, 17);
pub const EcsIdHasOnSet = @as(c_uint, 1) << @as(c_int, 18);
pub const EcsIdHasOnTableFill = @as(c_uint, 1) << @as(c_int, 19);
pub const EcsIdHasOnTableEmpty = @as(c_uint, 1) << @as(c_int, 20);
pub const EcsIdHasOnTableCreate = @as(c_uint, 1) << @as(c_int, 21);
pub const EcsIdHasOnTableDelete = @as(c_uint, 1) << @as(c_int, 22);
pub const EcsIdIsSparse = @as(c_uint, 1) << @as(c_int, 23);
pub const EcsIdIsUnion = @as(c_uint, 1) << @as(c_int, 24);
pub const EcsIdEventMask = (((((((EcsIdHasOnAdd | EcsIdHasOnRemove) | EcsIdHasOnSet) | EcsIdHasOnTableFill) | EcsIdHasOnTableEmpty) | EcsIdHasOnTableCreate) | EcsIdHasOnTableDelete) | EcsIdIsSparse) | EcsIdIsUnion;
pub const EcsIdMarkedForDelete = @as(c_uint, 1) << @as(c_int, 30);
pub const ECS_ID_ON_DELETE = @compileError("unable to translate C expr: expected ')' instead got '['");
// ../flecs/distr/flecs.h:417:9
pub inline fn ECS_ID_ON_DELETE_TARGET(flags: anytype) @TypeOf(ECS_ID_ON_DELETE(flags >> @as(c_int, 3))) {
    _ = &flags;
    return ECS_ID_ON_DELETE(flags >> @as(c_int, 3));
}
pub inline fn ECS_ID_ON_DELETE_FLAG(id: anytype) @TypeOf(@as(c_uint, 1) << (id - EcsRemove)) {
    _ = &id;
    return @as(c_uint, 1) << (id - EcsRemove);
}
pub inline fn ECS_ID_ON_DELETE_TARGET_FLAG(id: anytype) @TypeOf(@as(c_uint, 1) << (@as(c_int, 3) + (id - EcsRemove))) {
    _ = &id;
    return @as(c_uint, 1) << (@as(c_int, 3) + (id - EcsRemove));
}
pub const ECS_ID_ON_INSTANTIATE = @compileError("unable to translate C expr: expected ')' instead got '['");
// ../flecs/distr/flecs.h:425:9
pub inline fn ECS_ID_ON_INSTANTIATE_FLAG(id: anytype) @TypeOf(@as(c_uint, 1) << (@as(c_int, 6) + (id - EcsOverride))) {
    _ = &id;
    return @as(c_uint, 1) << (@as(c_int, 6) + (id - EcsOverride));
}
pub const EcsIterIsValid = @as(c_uint, 1) << @as(c_uint, 0);
pub const EcsIterNoData = @as(c_uint, 1) << @as(c_uint, 1);
pub const EcsIterNoResults = @as(c_uint, 1) << @as(c_uint, 3);
pub const EcsIterIgnoreThis = @as(c_uint, 1) << @as(c_uint, 4);
pub const EcsIterHasCondSet = @as(c_uint, 1) << @as(c_uint, 6);
pub const EcsIterProfile = @as(c_uint, 1) << @as(c_uint, 7);
pub const EcsIterTrivialSearch = @as(c_uint, 1) << @as(c_uint, 8);
pub const EcsIterTrivialTest = @as(c_uint, 1) << @as(c_uint, 11);
pub const EcsIterTrivialCached = @as(c_uint, 1) << @as(c_uint, 14);
pub const EcsIterCacheSearch = @as(c_uint, 1) << @as(c_uint, 15);
pub const EcsIterFixedInChangeComputed = @as(c_uint, 1) << @as(c_uint, 16);
pub const EcsIterFixedInChanged = @as(c_uint, 1) << @as(c_uint, 17);
pub const EcsIterSkip = @as(c_uint, 1) << @as(c_uint, 18);
pub const EcsIterCppEach = @as(c_uint, 1) << @as(c_uint, 19);
pub const EcsIterTableOnly = @as(c_uint, 1) << @as(c_uint, 20);
pub const EcsEventTableOnly = @as(c_uint, 1) << @as(c_uint, 20);
pub const EcsEventNoOnSet = @as(c_uint, 1) << @as(c_uint, 16);
pub const EcsQueryMatchThis = @as(c_uint, 1) << @as(c_uint, 11);
pub const EcsQueryMatchOnlyThis = @as(c_uint, 1) << @as(c_uint, 12);
pub const EcsQueryMatchOnlySelf = @as(c_uint, 1) << @as(c_uint, 13);
pub const EcsQueryMatchWildcards = @as(c_uint, 1) << @as(c_uint, 14);
pub const EcsQueryMatchNothing = @as(c_uint, 1) << @as(c_uint, 15);
pub const EcsQueryHasCondSet = @as(c_uint, 1) << @as(c_uint, 16);
pub const EcsQueryHasPred = @as(c_uint, 1) << @as(c_uint, 17);
pub const EcsQueryHasScopes = @as(c_uint, 1) << @as(c_uint, 18);
pub const EcsQueryHasRefs = @as(c_uint, 1) << @as(c_uint, 19);
pub const EcsQueryHasOutTerms = @as(c_uint, 1) << @as(c_uint, 20);
pub const EcsQueryHasNonThisOutTerms = @as(c_uint, 1) << @as(c_uint, 21);
pub const EcsQueryHasMonitor = @as(c_uint, 1) << @as(c_uint, 22);
pub const EcsQueryIsTrivial = @as(c_uint, 1) << @as(c_uint, 23);
pub const EcsQueryHasCacheable = @as(c_uint, 1) << @as(c_uint, 24);
pub const EcsQueryIsCacheable = @as(c_uint, 1) << @as(c_uint, 25);
pub const EcsQueryHasTableThisVar = @as(c_uint, 1) << @as(c_uint, 26);
pub const EcsQueryCacheYieldEmptyTables = @as(c_uint, 1) << @as(c_uint, 27);
pub const EcsTermMatchAny = @as(c_uint, 1) << @as(c_int, 0);
pub const EcsTermMatchAnySrc = @as(c_uint, 1) << @as(c_int, 1);
pub const EcsTermTransitive = @as(c_uint, 1) << @as(c_int, 2);
pub const EcsTermReflexive = @as(c_uint, 1) << @as(c_int, 3);
pub const EcsTermIdInherited = @as(c_uint, 1) << @as(c_int, 4);
pub const EcsTermIsTrivial = @as(c_uint, 1) << @as(c_int, 5);
pub const EcsTermIsCacheable = @as(c_uint, 1) << @as(c_int, 7);
pub const EcsTermIsScope = @as(c_uint, 1) << @as(c_int, 8);
pub const EcsTermIsMember = @as(c_uint, 1) << @as(c_int, 9);
pub const EcsTermIsToggle = @as(c_uint, 1) << @as(c_int, 10);
pub const EcsTermKeepAlive = @as(c_uint, 1) << @as(c_int, 11);
pub const EcsTermIsSparse = @as(c_uint, 1) << @as(c_int, 12);
pub const EcsTermIsUnion = @as(c_uint, 1) << @as(c_int, 13);
pub const EcsTermIsOr = @as(c_uint, 1) << @as(c_int, 14);
pub const EcsObserverIsMulti = @as(c_uint, 1) << @as(c_uint, 1);
pub const EcsObserverIsMonitor = @as(c_uint, 1) << @as(c_uint, 2);
pub const EcsObserverIsDisabled = @as(c_uint, 1) << @as(c_uint, 3);
pub const EcsObserverIsParentDisabled = @as(c_uint, 1) << @as(c_uint, 4);
pub const EcsObserverBypassQuery = @as(c_uint, 1) << @as(c_uint, 5);
pub const EcsObserverYieldOnCreate = @as(c_uint, 1) << @as(c_uint, 6);
pub const EcsObserverYieldOnDelete = @as(c_uint, 1) << @as(c_uint, 7);
pub const EcsTableHasBuiltins = @as(c_uint, 1) << @as(c_uint, 1);
pub const EcsTableIsPrefab = @as(c_uint, 1) << @as(c_uint, 2);
pub const EcsTableHasIsA = @as(c_uint, 1) << @as(c_uint, 3);
pub const EcsTableHasChildOf = @as(c_uint, 1) << @as(c_uint, 4);
pub const EcsTableHasName = @as(c_uint, 1) << @as(c_uint, 5);
pub const EcsTableHasPairs = @as(c_uint, 1) << @as(c_uint, 6);
pub const EcsTableHasModule = @as(c_uint, 1) << @as(c_uint, 7);
pub const EcsTableIsDisabled = @as(c_uint, 1) << @as(c_uint, 8);
pub const EcsTableNotQueryable = @as(c_uint, 1) << @as(c_uint, 9);
pub const EcsTableHasCtors = @as(c_uint, 1) << @as(c_uint, 10);
pub const EcsTableHasDtors = @as(c_uint, 1) << @as(c_uint, 11);
pub const EcsTableHasCopy = @as(c_uint, 1) << @as(c_uint, 12);
pub const EcsTableHasMove = @as(c_uint, 1) << @as(c_uint, 13);
pub const EcsTableHasToggle = @as(c_uint, 1) << @as(c_uint, 14);
pub const EcsTableHasOverrides = @as(c_uint, 1) << @as(c_uint, 15);
pub const EcsTableHasOnAdd = @as(c_uint, 1) << @as(c_uint, 16);
pub const EcsTableHasOnRemove = @as(c_uint, 1) << @as(c_uint, 17);
pub const EcsTableHasOnSet = @as(c_uint, 1) << @as(c_uint, 18);
pub const EcsTableHasOnTableFill = @as(c_uint, 1) << @as(c_uint, 19);
pub const EcsTableHasOnTableEmpty = @as(c_uint, 1) << @as(c_uint, 20);
pub const EcsTableHasOnTableCreate = @as(c_uint, 1) << @as(c_uint, 21);
pub const EcsTableHasOnTableDelete = @as(c_uint, 1) << @as(c_uint, 22);
pub const EcsTableHasSparse = @as(c_uint, 1) << @as(c_uint, 23);
pub const EcsTableHasUnion = @as(c_uint, 1) << @as(c_uint, 24);
pub const EcsTableHasTraversable = @as(c_uint, 1) << @as(c_uint, 26);
pub const EcsTableMarkedForDelete = @as(c_uint, 1) << @as(c_uint, 30);
pub const EcsTableHasLifecycle = EcsTableHasCtors | EcsTableHasDtors;
pub const EcsTableIsComplex = (EcsTableHasLifecycle | EcsTableHasToggle) | EcsTableHasSparse;
pub const EcsTableHasAddActions = ((EcsTableHasIsA | EcsTableHasCtors) | EcsTableHasOnAdd) | EcsTableHasOnSet;
pub const EcsTableHasRemoveActions = (EcsTableHasIsA | EcsTableHasDtors) | EcsTableHasOnRemove;
pub const EcsTableEdgeFlags = ((EcsTableHasOnAdd | EcsTableHasOnRemove) | EcsTableHasSparse) | EcsTableHasUnion;
pub const EcsTableAddEdgeFlags = (EcsTableHasOnAdd | EcsTableHasSparse) | EcsTableHasUnion;
pub const EcsTableRemoveEdgeFlags = (EcsTableHasOnRemove | EcsTableHasSparse) | EcsTableHasUnion;
pub const EcsAperiodicEmptyTables = @as(c_uint, 1) << @as(c_uint, 1);
pub const EcsAperiodicComponentMonitors = @as(c_uint, 1) << @as(c_uint, 2);
pub const EcsAperiodicEmptyQueries = @as(c_uint, 1) << @as(c_uint, 4);
pub const ECS_TARGET_DARWIN = "";
pub const ECS_TARGET_POSIX = "";
pub const ECS_TARGET_CLANG = "";
pub const ECS_TARGET_GNU = "";
pub const ECS_CLANG_VERSION = __clang_major__;
pub const _CDEFS_H_ = "";
pub const __BEGIN_DECLS = "";
pub const __END_DECLS = "";
pub inline fn __P(protos: anytype) @TypeOf(protos) {
    _ = &protos;
    return protos;
}
pub const __CONCAT = @compileError("unable to translate C expr: unexpected token '##'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:113:9
pub const __STRING = @compileError("unable to translate C expr: unexpected token '#'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:114:9
pub const __const = @compileError("unable to translate C expr: unexpected token 'const'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:116:9
pub const __signed = c_int;
pub const __volatile = @compileError("unable to translate C expr: unexpected token 'volatile'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:118:9
pub const __dead2 = @compileError("unable to translate macro: undefined identifier `__noreturn__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:162:9
pub const __pure2 = @compileError("unable to translate C expr: unexpected token '__attribute__'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:163:9
pub const __stateful_pure = @compileError("unable to translate macro: undefined identifier `__pure__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:164:9
pub const __unused = @compileError("unable to translate macro: undefined identifier `__unused__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:169:9
pub const __used = @compileError("unable to translate macro: undefined identifier `__used__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:174:9
pub const __cold = @compileError("unable to translate macro: undefined identifier `__cold__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:180:9
pub const __returns_nonnull = @compileError("unable to translate macro: undefined identifier `returns_nonnull`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:187:9
pub const __exported = @compileError("unable to translate macro: undefined identifier `__visibility__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:197:9
pub const __exported_push = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:198:9
pub const __exported_pop = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:199:9
pub const __deprecated = @compileError("unable to translate macro: undefined identifier `__deprecated__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:211:9
pub const __deprecated_msg = @compileError("unable to translate macro: undefined identifier `__deprecated__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:215:10
pub inline fn __deprecated_enum_msg(_msg: anytype) @TypeOf(__deprecated_msg(_msg)) {
    _ = &_msg;
    return __deprecated_msg(_msg);
}
pub const __kpi_deprecated = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:226:9
pub const __unavailable = @compileError("unable to translate macro: undefined identifier `__unavailable__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:232:9
pub const __kpi_unavailable = "";
pub const __kpi_deprecated_arm64_macos_unavailable = "";
pub const __dead = "";
pub const __pure = "";
pub const __restrict = @compileError("unable to translate C expr: unexpected token 'restrict'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:254:9
pub const __disable_tail_calls = @compileError("unable to translate macro: undefined identifier `__disable_tail_calls__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:287:9
pub const __not_tail_called = @compileError("unable to translate macro: undefined identifier `__not_tail_called__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:299:9
pub const __result_use_check = @compileError("unable to translate macro: undefined identifier `__warn_unused_result__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:310:9
pub const __swift_unavailable = @compileError("unable to translate macro: undefined identifier `__availability__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:320:9
pub const __abortlike = __dead2 ++ __cold ++ __not_tail_called;
pub const __header_inline = @compileError("unable to translate C expr: unexpected token 'inline'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:354:10
pub const __header_always_inline = @compileError("unable to translate macro: undefined identifier `__always_inline__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:367:10
pub const __unreachable_ok_push = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:380:10
pub const __unreachable_ok_pop = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:383:10
pub const __printflike = @compileError("unable to translate macro: undefined identifier `__format__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:404:9
pub const __printf0like = @compileError("unable to translate macro: undefined identifier `__format__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:406:9
pub const __scanflike = @compileError("unable to translate macro: undefined identifier `__format__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:408:9
pub const __osloglike = @compileError("unable to translate macro: undefined identifier `__format__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:410:9
pub const __IDSTRING = @compileError("unable to translate C expr: unexpected token 'static'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:413:9
pub const __COPYRIGHT = @compileError("unable to translate macro: undefined identifier `copyright`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:416:9
pub const __RCSID = @compileError("unable to translate macro: undefined identifier `rcsid`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:420:9
pub const __SCCSID = @compileError("unable to translate macro: undefined identifier `sccsid`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:424:9
pub const __PROJECT_VERSION = @compileError("unable to translate macro: undefined identifier `project_version`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:428:9
pub const __FBSDID = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:433:9
pub const __DECONST = @compileError("unable to translate C expr: unexpected token 'const'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:437:9
pub const __DEVOLATILE = @compileError("unable to translate C expr: unexpected token 'volatile'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:441:9
pub const __DEQUALIFY = @compileError("unable to translate C expr: unexpected token 'const'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:445:9
pub const __alloc_align = @compileError("unable to translate macro: undefined identifier `alloc_align`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:454:9
pub const __alloc_size = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:475:9
pub const __DARWIN_ONLY_64_BIT_INO_T = @as(c_int, 1);
pub const __DARWIN_ONLY_UNIX_CONFORMANCE = @as(c_int, 1);
pub const __DARWIN_ONLY_VERS_1050 = @as(c_int, 1);
pub const __DARWIN_UNIX03 = @as(c_int, 1);
pub const __DARWIN_64_BIT_INO_T = @as(c_int, 1);
pub const __DARWIN_VERS_1050 = @as(c_int, 1);
pub const __DARWIN_NON_CANCELABLE = @as(c_int, 0);
pub const __DARWIN_SUF_UNIX03 = "";
pub const __DARWIN_SUF_64_BIT_INO_T = "";
pub const __DARWIN_SUF_1050 = "";
pub const __DARWIN_SUF_NON_CANCELABLE = "";
pub const __DARWIN_SUF_EXTSN = "$DARWIN_EXTSN";
pub const __DARWIN_ALIAS = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:660:9
pub const __DARWIN_ALIAS_C = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:661:9
pub const __DARWIN_ALIAS_I = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:662:9
pub const __DARWIN_NOCANCEL = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:663:9
pub const __DARWIN_INODE64 = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:664:9
pub const __DARWIN_1050 = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:666:9
pub const __DARWIN_1050ALIAS = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:667:9
pub const __DARWIN_1050ALIAS_C = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:668:9
pub const __DARWIN_1050ALIAS_I = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:669:9
pub const __DARWIN_1050INODE64 = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:670:9
pub const __DARWIN_EXTSN = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:672:9
pub const __DARWIN_EXTSN_C = @compileError("unable to translate C expr: unexpected token '__asm'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:673:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_2_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:35:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_2_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:41:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_2_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:47:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_3_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:53:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_3_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:59:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_3_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:65:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_4_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:71:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_4_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:77:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_4_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:83:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_4_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:89:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_5_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:95:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_5_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:101:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_6_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:107:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_6_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:113:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_7_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:119:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_7_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:125:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_8_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:131:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_8_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:137:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_8_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:143:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_8_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:149:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_8_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:155:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_9_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:161:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_9_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:167:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_9_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:173:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_9_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:179:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_10_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:185:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_10_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:191:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_10_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:197:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_10_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:203:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_11_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:209:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_11_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:215:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_11_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:221:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_11_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:227:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_11_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:233:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_12_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:239:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_12_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:245:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_12_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:251:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_12_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:257:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_12_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:263:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:269:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:275:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:281:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:287:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:293:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_5 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:299:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_6 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:305:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_13_7 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:311:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:317:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:323:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:329:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:335:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_5 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:341:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:347:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_6 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:359:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_7 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:365:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_14_8 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:371:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:377:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:383:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:389:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:395:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:401:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_5 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:407:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_15_6 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:413:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:419:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:425:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:431:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_3 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:437:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_4 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:443:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_5 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:449:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_6 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:455:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_16_7 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:461:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_17_0 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:467:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_17_1 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:473:9
pub const __DARWIN_ALIAS_STARTING_IPHONE___IPHONE_17_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:479:9
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_0(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_3(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_5(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_6(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_7(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_8(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_9(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_10(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_10_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_10_3(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_11(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_11_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_11_3(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_11_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_12(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_12_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_12_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_12_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_13(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_13_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_13_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_13_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_14(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_14_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_14_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_14_5(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_14_6(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_15(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_15_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_15_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_10_16(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_11_0(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_11_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_11_3(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_11_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_11_5(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_11_6(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_0(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_3(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_5(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_6(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_12_7(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_0(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_2(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_3(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_4(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_5(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_13_6(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_14_0(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub inline fn __DARWIN_ALIAS_STARTING_MAC___MAC_14_1(x: anytype) @TypeOf(x) {
    _ = &x;
    return x;
}
pub const __DARWIN_ALIAS_STARTING_MAC___MAC_14_2 = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_symbol_aliasing.h:833:9
pub const __DARWIN_ALIAS_STARTING = @compileError("unable to translate macro: undefined identifier `__DARWIN_ALIAS_STARTING_MAC_`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:683:9
pub const ___POSIX_C_DEPRECATED_STARTING_198808L = "";
pub const ___POSIX_C_DEPRECATED_STARTING_199009L = "";
pub const ___POSIX_C_DEPRECATED_STARTING_199209L = "";
pub const ___POSIX_C_DEPRECATED_STARTING_199309L = "";
pub const ___POSIX_C_DEPRECATED_STARTING_199506L = "";
pub const ___POSIX_C_DEPRECATED_STARTING_200112L = "";
pub const ___POSIX_C_DEPRECATED_STARTING_200809L = "";
pub const __POSIX_C_DEPRECATED = @compileError("unable to translate macro: undefined identifier `___POSIX_C_DEPRECATED_STARTING_`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:746:9
pub const __DARWIN_C_ANSI = @as(c_long, 0o10000);
pub const __DARWIN_C_FULL = @as(c_long, 900000);
pub const __DARWIN_C_LEVEL = __DARWIN_C_FULL;
pub const __STDC_WANT_LIB_EXT1__ = @as(c_int, 1);
pub const __DARWIN_NO_LONG_LONG = @as(c_int, 0);
pub const _DARWIN_FEATURE_64_BIT_INODE = @as(c_int, 1);
pub const _DARWIN_FEATURE_ONLY_64_BIT_INODE = @as(c_int, 1);
pub const _DARWIN_FEATURE_ONLY_VERS_1050 = @as(c_int, 1);
pub const _DARWIN_FEATURE_ONLY_UNIX_CONFORMANCE = @as(c_int, 1);
pub const _DARWIN_FEATURE_UNIX_CONFORMANCE = @as(c_int, 3);
pub const __CAST_AWAY_QUALIFIER = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:844:9
pub const __XNU_PRIVATE_EXTERN = @compileError("unable to translate macro: undefined identifier `visibility`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:858:9
pub const __has_ptrcheck = @as(c_int, 0);
pub const __single = "";
pub const __unsafe_indexable = "";
pub const __counted_by = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:872:9
pub const __sized_by = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:873:9
pub const __ended_by = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:874:9
pub const __terminated_by = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:875:9
pub const __null_terminated = "";
pub const __ptrcheck_abi_assume_single = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:885:9
pub const __ptrcheck_abi_assume_unsafe_indexable = @compileError("unable to translate C expr: unexpected token ''");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:886:9
pub inline fn __unsafe_forge_bidi_indexable(T: anytype, P: anytype, S: anytype) @TypeOf(T(P)) {
    _ = &T;
    _ = &P;
    _ = &S;
    return T(P);
}
pub const __unsafe_forge_single = @import("std").zig.c_translation.Macros.CAST_OR_CALL;
pub inline fn __terminated_by_to_indexable(P: anytype) @TypeOf(P) {
    _ = &P;
    return P;
}
pub inline fn __unsafe_terminated_by_to_indexable(P: anytype) @TypeOf(P) {
    _ = &P;
    return P;
}
pub inline fn __null_terminated_to_indexable(P: anytype) @TypeOf(P) {
    _ = &P;
    return P;
}
pub inline fn __unsafe_null_terminated_to_indexable(P: anytype) @TypeOf(P) {
    _ = &P;
    return P;
}
pub const __unsafe_terminated_by_from_indexable = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:895:9
pub const __unsafe_null_terminated_from_indexable = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:896:9
pub const __array_decay_dicards_count_in_parameters = "";
pub const __unsafe_late_const = "";
pub const __ASSUME_PTR_ABI_SINGLE_BEGIN = __ptrcheck_abi_assume_single();
pub const __ASSUME_PTR_ABI_SINGLE_END = __ptrcheck_abi_assume_unsafe_indexable();
pub const __header_indexable = "";
pub const __header_bidi_indexable = "";
pub const __compiler_barrier = @compileError("unable to translate C expr: unexpected token '__asm__'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:930:9
pub const __enum_open = @compileError("unable to translate macro: undefined identifier `__enum_extensibility__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:933:9
pub const __enum_closed = @compileError("unable to translate macro: undefined identifier `__enum_extensibility__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:934:9
pub const __enum_options = @compileError("unable to translate macro: undefined identifier `__flag_enum__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:941:9
pub const __enum_decl = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:954:9
pub const __enum_closed_decl = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:956:9
pub const __options_decl = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:958:9
pub const __options_closed_decl = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/cdefs.h:960:9
pub const __kernel_ptr_semantics = "";
pub const __kernel_data_semantics = "";
pub const __kernel_dual_semantics = "";
pub const __ASSERT_FILE_NAME = @compileError("unable to translate macro: undefined identifier `__FILE_NAME__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/assert.h:60:9
pub const __assert = @compileError("unable to translate C expr: expected ')' instead got 'line'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/assert.h:93:9
pub const assert = @compileError("unable to translate macro: undefined identifier `__LINE__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/assert.h:98:9
pub const _ASSERT_H_ = "";
pub const static_assert = @compileError("unable to translate C expr: unexpected token '_Static_assert'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/assert.h:113:9
pub const __STDARG_H = "";
pub const __need___va_list = "";
pub const __need_va_list = "";
pub const __need_va_arg = "";
pub const __need___va_copy = "";
pub const __need_va_copy = "";
pub const __GNUC_VA_LIST = "";
pub const _VA_LIST = "";
pub const va_start = @compileError("unable to translate macro: undefined identifier `__builtin_va_start`");
// /opt/homebrew/Cellar/zig/0.13.0/lib/zig/include/__stdarg_va_arg.h:17:9
pub const va_end = @compileError("unable to translate macro: undefined identifier `__builtin_va_end`");
// /opt/homebrew/Cellar/zig/0.13.0/lib/zig/include/__stdarg_va_arg.h:19:9
pub const va_arg = @compileError("unable to translate C expr: unexpected token 'an identifier'");
// /opt/homebrew/Cellar/zig/0.13.0/lib/zig/include/__stdarg_va_arg.h:20:9
pub const __va_copy = @compileError("unable to translate macro: undefined identifier `__builtin_va_copy`");
// /opt/homebrew/Cellar/zig/0.13.0/lib/zig/include/__stdarg___va_copy.h:11:9
pub const va_copy = @compileError("unable to translate macro: undefined identifier `__builtin_va_copy`");
// /opt/homebrew/Cellar/zig/0.13.0/lib/zig/include/__stdarg_va_copy.h:11:9
pub const _STRING_H_ = "";
pub const __TYPES_H_ = "";
pub const _SYS__TYPES_H_ = "";
pub const _BSD_MACHINE__TYPES_H_ = "";
pub const _BSD_ARM__TYPES_H_ = "";
pub const __DARWIN_NULL = @import("std").zig.c_translation.cast(?*anyopaque, @as(c_int, 0));
pub const _SYS__PTHREAD_TYPES_H_ = "";
pub const __PTHREAD_SIZE__ = @as(c_int, 8176);
pub const __PTHREAD_ATTR_SIZE__ = @as(c_int, 56);
pub const __PTHREAD_MUTEXATTR_SIZE__ = @as(c_int, 8);
pub const __PTHREAD_MUTEX_SIZE__ = @as(c_int, 56);
pub const __PTHREAD_CONDATTR_SIZE__ = @as(c_int, 8);
pub const __PTHREAD_COND_SIZE__ = @as(c_int, 40);
pub const __PTHREAD_ONCE_SIZE__ = @as(c_int, 8);
pub const __PTHREAD_RWLOCK_SIZE__ = @as(c_int, 192);
pub const __PTHREAD_RWLOCKATTR_SIZE__ = @as(c_int, 16);
pub const __offsetof = @compileError("unable to translate C expr: unexpected token 'an identifier'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/sys/_types.h:83:9
pub const __strfmonlike = @compileError("unable to translate macro: undefined identifier `__format__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/_types.h:31:9
pub const __strftimelike = @compileError("unable to translate macro: undefined identifier `__format__`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/_types.h:33:9
pub const __DARWIN_WCHAR_MAX = __WCHAR_MAX__;
pub const __DARWIN_WCHAR_MIN = -@import("std").zig.c_translation.promoteIntLiteral(c_int, 0x7fffffff, .hex) - @as(c_int, 1);
pub const __DARWIN_WEOF = @import("std").zig.c_translation.cast(__darwin_wint_t, -@as(c_int, 1));
pub const _FORTIFY_SOURCE = @as(c_int, 2);
pub const __AVAILABILITY__ = "";
pub const __API_TO_BE_DEPRECATED = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_MACOS = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_IOS = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_MACCATALYST = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_WATCHOS = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_TVOS = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_DRIVERKIT = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __API_TO_BE_DEPRECATED_VISIONOS = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __AVAILABILITY_VERSIONS__ = "";
pub const __MAC_10_0 = @as(c_int, 1000);
pub const __MAC_10_1 = @as(c_int, 1010);
pub const __MAC_10_2 = @as(c_int, 1020);
pub const __MAC_10_3 = @as(c_int, 1030);
pub const __MAC_10_4 = @as(c_int, 1040);
pub const __MAC_10_5 = @as(c_int, 1050);
pub const __MAC_10_6 = @as(c_int, 1060);
pub const __MAC_10_7 = @as(c_int, 1070);
pub const __MAC_10_8 = @as(c_int, 1080);
pub const __MAC_10_9 = @as(c_int, 1090);
pub const __MAC_10_10 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101000, .decimal);
pub const __MAC_10_10_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101002, .decimal);
pub const __MAC_10_10_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101003, .decimal);
pub const __MAC_10_11 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101100, .decimal);
pub const __MAC_10_11_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101102, .decimal);
pub const __MAC_10_11_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101103, .decimal);
pub const __MAC_10_11_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101104, .decimal);
pub const __MAC_10_12 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101200, .decimal);
pub const __MAC_10_12_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101201, .decimal);
pub const __MAC_10_12_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101202, .decimal);
pub const __MAC_10_12_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101204, .decimal);
pub const __MAC_10_13 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101300, .decimal);
pub const __MAC_10_13_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101301, .decimal);
pub const __MAC_10_13_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101302, .decimal);
pub const __MAC_10_13_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101304, .decimal);
pub const __MAC_10_14 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101400, .decimal);
pub const __MAC_10_14_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101401, .decimal);
pub const __MAC_10_14_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101404, .decimal);
pub const __MAC_10_14_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101405, .decimal);
pub const __MAC_10_14_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101406, .decimal);
pub const __MAC_10_15 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101500, .decimal);
pub const __MAC_10_15_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101501, .decimal);
pub const __MAC_10_15_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101504, .decimal);
pub const __MAC_10_16 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 101600, .decimal);
pub const __MAC_11_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110000, .decimal);
pub const __MAC_11_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110100, .decimal);
pub const __MAC_11_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110300, .decimal);
pub const __MAC_11_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110400, .decimal);
pub const __MAC_11_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110500, .decimal);
pub const __MAC_11_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110600, .decimal);
pub const __MAC_12_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120000, .decimal);
pub const __MAC_12_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120100, .decimal);
pub const __MAC_12_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120200, .decimal);
pub const __MAC_12_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120300, .decimal);
pub const __MAC_12_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120400, .decimal);
pub const __MAC_12_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120500, .decimal);
pub const __MAC_12_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120600, .decimal);
pub const __MAC_12_7 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120700, .decimal);
pub const __MAC_13_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130000, .decimal);
pub const __MAC_13_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130100, .decimal);
pub const __MAC_13_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130200, .decimal);
pub const __MAC_13_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130300, .decimal);
pub const __MAC_13_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130400, .decimal);
pub const __MAC_13_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130500, .decimal);
pub const __MAC_13_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130600, .decimal);
pub const __MAC_14_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140000, .decimal);
pub const __MAC_14_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140100, .decimal);
pub const __MAC_14_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140200, .decimal);
pub const __IPHONE_2_0 = @as(c_int, 20000);
pub const __IPHONE_2_1 = @as(c_int, 20100);
pub const __IPHONE_2_2 = @as(c_int, 20200);
pub const __IPHONE_3_0 = @as(c_int, 30000);
pub const __IPHONE_3_1 = @as(c_int, 30100);
pub const __IPHONE_3_2 = @as(c_int, 30200);
pub const __IPHONE_4_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40000, .decimal);
pub const __IPHONE_4_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40100, .decimal);
pub const __IPHONE_4_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40200, .decimal);
pub const __IPHONE_4_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40300, .decimal);
pub const __IPHONE_5_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50000, .decimal);
pub const __IPHONE_5_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50100, .decimal);
pub const __IPHONE_6_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60000, .decimal);
pub const __IPHONE_6_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60100, .decimal);
pub const __IPHONE_7_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70000, .decimal);
pub const __IPHONE_7_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70100, .decimal);
pub const __IPHONE_8_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80000, .decimal);
pub const __IPHONE_8_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80100, .decimal);
pub const __IPHONE_8_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80200, .decimal);
pub const __IPHONE_8_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80300, .decimal);
pub const __IPHONE_8_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80400, .decimal);
pub const __IPHONE_9_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90000, .decimal);
pub const __IPHONE_9_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90100, .decimal);
pub const __IPHONE_9_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90200, .decimal);
pub const __IPHONE_9_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90300, .decimal);
pub const __IPHONE_10_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __IPHONE_10_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100100, .decimal);
pub const __IPHONE_10_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100200, .decimal);
pub const __IPHONE_10_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100300, .decimal);
pub const __IPHONE_11_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110000, .decimal);
pub const __IPHONE_11_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110100, .decimal);
pub const __IPHONE_11_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110200, .decimal);
pub const __IPHONE_11_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110300, .decimal);
pub const __IPHONE_11_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110400, .decimal);
pub const __IPHONE_12_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120000, .decimal);
pub const __IPHONE_12_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120100, .decimal);
pub const __IPHONE_12_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120200, .decimal);
pub const __IPHONE_12_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120300, .decimal);
pub const __IPHONE_12_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120400, .decimal);
pub const __IPHONE_13_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130000, .decimal);
pub const __IPHONE_13_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130100, .decimal);
pub const __IPHONE_13_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130200, .decimal);
pub const __IPHONE_13_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130300, .decimal);
pub const __IPHONE_13_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130400, .decimal);
pub const __IPHONE_13_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130500, .decimal);
pub const __IPHONE_13_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130600, .decimal);
pub const __IPHONE_13_7 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130700, .decimal);
pub const __IPHONE_14_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140000, .decimal);
pub const __IPHONE_14_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140100, .decimal);
pub const __IPHONE_14_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140200, .decimal);
pub const __IPHONE_14_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140300, .decimal);
pub const __IPHONE_14_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140500, .decimal);
pub const __IPHONE_14_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140400, .decimal);
pub const __IPHONE_14_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140600, .decimal);
pub const __IPHONE_14_7 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140700, .decimal);
pub const __IPHONE_14_8 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140800, .decimal);
pub const __IPHONE_15_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150000, .decimal);
pub const __IPHONE_15_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150100, .decimal);
pub const __IPHONE_15_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150200, .decimal);
pub const __IPHONE_15_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150300, .decimal);
pub const __IPHONE_15_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150400, .decimal);
pub const __IPHONE_15_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150500, .decimal);
pub const __IPHONE_15_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150600, .decimal);
pub const __IPHONE_16_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160000, .decimal);
pub const __IPHONE_16_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160100, .decimal);
pub const __IPHONE_16_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160200, .decimal);
pub const __IPHONE_16_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160300, .decimal);
pub const __IPHONE_16_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160400, .decimal);
pub const __IPHONE_16_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160500, .decimal);
pub const __IPHONE_16_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160600, .decimal);
pub const __IPHONE_16_7 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160700, .decimal);
pub const __IPHONE_17_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 170000, .decimal);
pub const __IPHONE_17_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 170100, .decimal);
pub const __IPHONE_17_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 170200, .decimal);
pub const __WATCHOS_1_0 = @as(c_int, 10000);
pub const __WATCHOS_2_0 = @as(c_int, 20000);
pub const __WATCHOS_2_1 = @as(c_int, 20100);
pub const __WATCHOS_2_2 = @as(c_int, 20200);
pub const __WATCHOS_3_0 = @as(c_int, 30000);
pub const __WATCHOS_3_1 = @as(c_int, 30100);
pub const __WATCHOS_3_1_1 = @as(c_int, 30101);
pub const __WATCHOS_3_2 = @as(c_int, 30200);
pub const __WATCHOS_4_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40000, .decimal);
pub const __WATCHOS_4_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40100, .decimal);
pub const __WATCHOS_4_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40200, .decimal);
pub const __WATCHOS_4_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40300, .decimal);
pub const __WATCHOS_5_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50000, .decimal);
pub const __WATCHOS_5_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50100, .decimal);
pub const __WATCHOS_5_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50200, .decimal);
pub const __WATCHOS_5_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50300, .decimal);
pub const __WATCHOS_6_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60000, .decimal);
pub const __WATCHOS_6_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60100, .decimal);
pub const __WATCHOS_6_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60200, .decimal);
pub const __WATCHOS_7_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70000, .decimal);
pub const __WATCHOS_7_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70100, .decimal);
pub const __WATCHOS_7_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70200, .decimal);
pub const __WATCHOS_7_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70300, .decimal);
pub const __WATCHOS_7_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70400, .decimal);
pub const __WATCHOS_7_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70500, .decimal);
pub const __WATCHOS_7_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70600, .decimal);
pub const __WATCHOS_8_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80000, .decimal);
pub const __WATCHOS_8_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80100, .decimal);
pub const __WATCHOS_8_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80300, .decimal);
pub const __WATCHOS_8_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80400, .decimal);
pub const __WATCHOS_8_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80500, .decimal);
pub const __WATCHOS_8_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80600, .decimal);
pub const __WATCHOS_8_7 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80700, .decimal);
pub const __WATCHOS_9_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90000, .decimal);
pub const __WATCHOS_9_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90100, .decimal);
pub const __WATCHOS_9_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90200, .decimal);
pub const __WATCHOS_9_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90300, .decimal);
pub const __WATCHOS_9_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90400, .decimal);
pub const __WATCHOS_9_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90500, .decimal);
pub const __WATCHOS_9_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90600, .decimal);
pub const __WATCHOS_10_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __WATCHOS_10_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100100, .decimal);
pub const __WATCHOS_10_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100200, .decimal);
pub const __TVOS_9_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90000, .decimal);
pub const __TVOS_9_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90100, .decimal);
pub const __TVOS_9_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 90200, .decimal);
pub const __TVOS_10_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100000, .decimal);
pub const __TVOS_10_0_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100001, .decimal);
pub const __TVOS_10_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100100, .decimal);
pub const __TVOS_10_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 100200, .decimal);
pub const __TVOS_11_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110000, .decimal);
pub const __TVOS_11_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110100, .decimal);
pub const __TVOS_11_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110200, .decimal);
pub const __TVOS_11_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110300, .decimal);
pub const __TVOS_11_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 110400, .decimal);
pub const __TVOS_12_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120000, .decimal);
pub const __TVOS_12_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120100, .decimal);
pub const __TVOS_12_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120200, .decimal);
pub const __TVOS_12_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120300, .decimal);
pub const __TVOS_12_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 120400, .decimal);
pub const __TVOS_13_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130000, .decimal);
pub const __TVOS_13_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130200, .decimal);
pub const __TVOS_13_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130300, .decimal);
pub const __TVOS_13_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 130400, .decimal);
pub const __TVOS_14_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140000, .decimal);
pub const __TVOS_14_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140100, .decimal);
pub const __TVOS_14_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140200, .decimal);
pub const __TVOS_14_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140300, .decimal);
pub const __TVOS_14_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140500, .decimal);
pub const __TVOS_14_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140600, .decimal);
pub const __TVOS_14_7 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 140700, .decimal);
pub const __TVOS_15_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150000, .decimal);
pub const __TVOS_15_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150100, .decimal);
pub const __TVOS_15_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150200, .decimal);
pub const __TVOS_15_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150300, .decimal);
pub const __TVOS_15_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150400, .decimal);
pub const __TVOS_15_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150500, .decimal);
pub const __TVOS_15_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 150600, .decimal);
pub const __TVOS_16_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160000, .decimal);
pub const __TVOS_16_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160100, .decimal);
pub const __TVOS_16_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160200, .decimal);
pub const __TVOS_16_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160300, .decimal);
pub const __TVOS_16_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160400, .decimal);
pub const __TVOS_16_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160500, .decimal);
pub const __TVOS_16_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 160600, .decimal);
pub const __TVOS_17_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 170000, .decimal);
pub const __TVOS_17_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 170100, .decimal);
pub const __TVOS_17_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 170200, .decimal);
pub const __BRIDGEOS_2_0 = @as(c_int, 20000);
pub const __BRIDGEOS_3_0 = @as(c_int, 30000);
pub const __BRIDGEOS_3_1 = @as(c_int, 30100);
pub const __BRIDGEOS_3_4 = @as(c_int, 30400);
pub const __BRIDGEOS_4_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40000, .decimal);
pub const __BRIDGEOS_4_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 40100, .decimal);
pub const __BRIDGEOS_5_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50000, .decimal);
pub const __BRIDGEOS_5_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50100, .decimal);
pub const __BRIDGEOS_5_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 50300, .decimal);
pub const __BRIDGEOS_6_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60000, .decimal);
pub const __BRIDGEOS_6_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60200, .decimal);
pub const __BRIDGEOS_6_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60400, .decimal);
pub const __BRIDGEOS_6_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60500, .decimal);
pub const __BRIDGEOS_6_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 60600, .decimal);
pub const __BRIDGEOS_7_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70000, .decimal);
pub const __BRIDGEOS_7_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70100, .decimal);
pub const __BRIDGEOS_7_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70200, .decimal);
pub const __BRIDGEOS_7_3 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70300, .decimal);
pub const __BRIDGEOS_7_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70400, .decimal);
pub const __BRIDGEOS_7_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 70600, .decimal);
pub const __BRIDGEOS_8_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80000, .decimal);
pub const __BRIDGEOS_8_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80100, .decimal);
pub const __BRIDGEOS_8_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 80200, .decimal);
pub const __DRIVERKIT_19_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 190000, .decimal);
pub const __DRIVERKIT_20_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 200000, .decimal);
pub const __DRIVERKIT_21_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 210000, .decimal);
pub const __DRIVERKIT_22_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 220000, .decimal);
pub const __DRIVERKIT_22_4 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 220400, .decimal);
pub const __DRIVERKIT_22_5 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 220500, .decimal);
pub const __DRIVERKIT_22_6 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 220600, .decimal);
pub const __DRIVERKIT_23_0 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 230000, .decimal);
pub const __DRIVERKIT_23_1 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 230100, .decimal);
pub const __DRIVERKIT_23_2 = @import("std").zig.c_translation.promoteIntLiteral(c_int, 230200, .decimal);
pub const __VISIONOS_1_0 = @as(c_int, 10000);
pub const MAC_OS_X_VERSION_10_0 = __MAC_10_0;
pub const MAC_OS_X_VERSION_10_1 = __MAC_10_1;
pub const MAC_OS_X_VERSION_10_2 = __MAC_10_2;
pub const MAC_OS_X_VERSION_10_3 = __MAC_10_3;
pub const MAC_OS_X_VERSION_10_4 = __MAC_10_4;
pub const MAC_OS_X_VERSION_10_5 = __MAC_10_5;
pub const MAC_OS_X_VERSION_10_6 = __MAC_10_6;
pub const MAC_OS_X_VERSION_10_7 = __MAC_10_7;
pub const MAC_OS_X_VERSION_10_8 = __MAC_10_8;
pub const MAC_OS_X_VERSION_10_9 = __MAC_10_9;
pub const MAC_OS_X_VERSION_10_10 = __MAC_10_10;
pub const MAC_OS_X_VERSION_10_10_2 = __MAC_10_10_2;
pub const MAC_OS_X_VERSION_10_10_3 = __MAC_10_10_3;
pub const MAC_OS_X_VERSION_10_11 = __MAC_10_11;
pub const MAC_OS_X_VERSION_10_11_2 = __MAC_10_11_2;
pub const MAC_OS_X_VERSION_10_11_3 = __MAC_10_11_3;
pub const MAC_OS_X_VERSION_10_11_4 = __MAC_10_11_4;
pub const MAC_OS_X_VERSION_10_12 = __MAC_10_12;
pub const MAC_OS_X_VERSION_10_12_1 = __MAC_10_12_1;
pub const MAC_OS_X_VERSION_10_12_2 = __MAC_10_12_2;
pub const MAC_OS_X_VERSION_10_12_4 = __MAC_10_12_4;
pub const MAC_OS_X_VERSION_10_13 = __MAC_10_13;
pub const MAC_OS_X_VERSION_10_13_1 = __MAC_10_13_1;
pub const MAC_OS_X_VERSION_10_13_2 = __MAC_10_13_2;
pub const MAC_OS_X_VERSION_10_13_4 = __MAC_10_13_4;
pub const MAC_OS_X_VERSION_10_14 = __MAC_10_14;
pub const MAC_OS_X_VERSION_10_14_1 = __MAC_10_14_1;
pub const MAC_OS_X_VERSION_10_14_4 = __MAC_10_14_4;
pub const MAC_OS_X_VERSION_10_14_5 = __MAC_10_14_5;
pub const MAC_OS_X_VERSION_10_14_6 = __MAC_10_14_6;
pub const MAC_OS_X_VERSION_10_15 = __MAC_10_15;
pub const MAC_OS_X_VERSION_10_15_1 = __MAC_10_15_1;
pub const MAC_OS_X_VERSION_10_15_4 = __MAC_10_15_4;
pub const MAC_OS_X_VERSION_10_16 = __MAC_10_16;
pub const MAC_OS_VERSION_11_0 = __MAC_11_0;
pub const MAC_OS_VERSION_11_1 = __MAC_11_1;
pub const MAC_OS_VERSION_11_3 = __MAC_11_3;
pub const MAC_OS_VERSION_11_4 = __MAC_11_4;
pub const MAC_OS_VERSION_11_5 = __MAC_11_5;
pub const MAC_OS_VERSION_11_6 = __MAC_11_6;
pub const MAC_OS_VERSION_12_0 = __MAC_12_0;
pub const MAC_OS_VERSION_12_1 = __MAC_12_1;
pub const MAC_OS_VERSION_12_2 = __MAC_12_2;
pub const MAC_OS_VERSION_12_3 = __MAC_12_3;
pub const MAC_OS_VERSION_12_4 = __MAC_12_4;
pub const MAC_OS_VERSION_12_5 = __MAC_12_5;
pub const MAC_OS_VERSION_12_6 = __MAC_12_6;
pub const MAC_OS_VERSION_12_7 = __MAC_12_7;
pub const MAC_OS_VERSION_13_0 = __MAC_13_0;
pub const MAC_OS_VERSION_13_1 = __MAC_13_1;
pub const MAC_OS_VERSION_13_2 = __MAC_13_2;
pub const MAC_OS_VERSION_13_3 = __MAC_13_3;
pub const MAC_OS_VERSION_13_4 = __MAC_13_4;
pub const MAC_OS_VERSION_13_5 = __MAC_13_5;
pub const MAC_OS_VERSION_13_6 = __MAC_13_6;
pub const MAC_OS_VERSION_14_0 = __MAC_14_0;
pub const MAC_OS_VERSION_14_1 = __MAC_14_1;
pub const MAC_OS_VERSION_14_2 = __MAC_14_2;
pub const __AVAILABILITY_INTERNAL__ = "";
pub const __MAC_OS_X_VERSION_MIN_REQUIRED = __ENVIRONMENT_OS_VERSION_MIN_REQUIRED__;
pub const __MAC_OS_X_VERSION_MAX_ALLOWED = __MAC_14_2;
pub const __AVAILABILITY_INTERNAL_DEPRECATED = @compileError("unable to translate macro: undefined identifier `deprecated`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:160:9
pub const __AVAILABILITY_INTERNAL_DEPRECATED_MSG = @compileError("unable to translate macro: undefined identifier `deprecated`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:163:17
pub const __AVAILABILITY_INTERNAL_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `unavailable`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:172:9
pub const __AVAILABILITY_INTERNAL_WEAK_IMPORT = @compileError("unable to translate macro: undefined identifier `weak_import`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:173:9
pub const __AVAILABILITY_INTERNAL_REGULAR = "";
pub const __AVAILABILITY_INTERNAL_LEGACY__ = "";
pub const __ENABLE_LEGACY_MAC_AVAILABILITY = @as(c_int, 1);
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2831:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2832:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2833:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2835:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2839:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2841:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2846:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2850:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2851:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2853:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2857:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2859:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2863:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2865:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2870:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2874:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2875:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2877:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2881:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2883:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2887:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2889:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2894:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2899:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2903:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2905:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2909:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2911:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2915:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2917:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2921:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_5_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2923:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2927:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2929:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2933:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2935:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2939:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2941:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2945:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2947:25
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2951:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2952:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2953:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2954:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2955:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2956:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2958:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2962:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2964:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2969:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2973:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2974:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2976:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2980:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2982:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2986:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2988:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2993:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2997:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:2998:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3000:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3004:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3006:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3010:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3012:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3017:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3021:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3022:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3024:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3028:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3030:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3034:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3036:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3040:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_5_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3042:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3046:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3048:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3052:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3054:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3058:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3060:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3064:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3066:25
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3070:21
pub const __AVAILABILITY_INTERNAL__MAC_10_2_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3071:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3072:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3073:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3074:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3075:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3077:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3081:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3083:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3088:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3092:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3093:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3095:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3099:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3101:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3105:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3107:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3112:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3116:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3117:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3119:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3123:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3125:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3129:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3131:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3136:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3140:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3141:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3143:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3147:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3149:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3153:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_5_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3155:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3159:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3161:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3165:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3167:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3171:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3173:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3177:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3179:25
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3183:21
pub const __AVAILABILITY_INTERNAL__MAC_10_3_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3184:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3185:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3186:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3187:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3188:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3190:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3194:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3196:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3201:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3205:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3206:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3208:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3212:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3214:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3218:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3220:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3225:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3229:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3230:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3232:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3236:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3238:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3242:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3244:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3249:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3253:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3254:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3256:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3260:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_5_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3262:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3266:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3268:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3272:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3274:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3278:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3280:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3284:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3286:25
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3290:21
pub const __AVAILABILITY_INTERNAL__MAC_10_4_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3291:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3292:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEPRECATED__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3293:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3294:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3295:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3296:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3298:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3302:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3304:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3309:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3313:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3314:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3316:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3320:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3322:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3326:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3328:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3333:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3337:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3338:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3340:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3344:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3346:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3350:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3352:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3357:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3361:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_5_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3363:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3367:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3369:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3373:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3375:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3379:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3381:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3385:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3387:25
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3391:21
pub const __AVAILABILITY_INTERNAL__MAC_10_5_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3392:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3393:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3394:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3395:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3396:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3398:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3402:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3404:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3409:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3413:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3414:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3416:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3420:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3422:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3426:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3428:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3433:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3437:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3438:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3440:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3444:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3446:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3450:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3452:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3457:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3461:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3462:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3464:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3468:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3470:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3474:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3476:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3480:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3482:25
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3486:21
pub const __AVAILABILITY_INTERNAL__MAC_10_6_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3487:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3488:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3489:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3490:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3491:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3493:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3497:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3499:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3504:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3508:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3509:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3511:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3515:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3517:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3521:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3523:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3528:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3532:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3533:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3535:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3539:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3541:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3545:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3547:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3552:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_13_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3556:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3557:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3559:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3563:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3565:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3569:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3571:25
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3575:21
pub const __AVAILABILITY_INTERNAL__MAC_10_7_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3576:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3577:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3578:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3579:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3580:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3582:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3586:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3588:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3593:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3597:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3598:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3600:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3604:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3606:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3610:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3612:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3617:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3621:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3622:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3624:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3628:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3630:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3634:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3636:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3641:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3645:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3646:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3648:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3652:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3654:25
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3658:21
pub const __AVAILABILITY_INTERNAL__MAC_10_8_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3659:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3660:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3661:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3662:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3663:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3665:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3669:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3671:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3676:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3680:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3681:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3683:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3687:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3689:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3693:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3695:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3700:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3704:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3705:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3707:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3711:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3713:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3717:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3719:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3724:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3728:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_14 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3729:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3730:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3732:25
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3736:21
pub const __AVAILABILITY_INTERNAL__MAC_10_9_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3737:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3738:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_0 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3739:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_0_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3741:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3745:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3746:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3747:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3749:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3753:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3755:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3760:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3764:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3765:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3767:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3771:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3773:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3777:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3779:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3784:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3788:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3789:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3791:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3795:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3797:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3801:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3803:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3808:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3812:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3814:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3818:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3820:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3824:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3826:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3830:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3832:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_5 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3836:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_5_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3838:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_6 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3842:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_6_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3844:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_7 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3848:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_7_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3850:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_8 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3854:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_8_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3856:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_9 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3860:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_9_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3862:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_10_13_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3867:25
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3871:21
pub const __AVAILABILITY_INTERNAL__MAC_10_0_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3872:21
pub const __AVAILABILITY_INTERNAL__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3873:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3874:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3875:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3876:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3878:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3882:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3884:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3888:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3889:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3891:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3895:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3897:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3901:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3903:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3908:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3912:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3913:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3915:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3919:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3921:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3925:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3927:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3932:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3936:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_2_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3937:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3938:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3939:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3941:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3945:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3946:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3948:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3952:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3954:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3958:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3960:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3965:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3969:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3970:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3972:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3976:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3978:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3982:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3984:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3989:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3993:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_3_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3994:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3995:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_10 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3996:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_10_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3997:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_10_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:3999:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_10_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4003:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_10_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4005:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_10_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4010:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4014:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4015:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4017:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4021:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4023:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4027:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4029:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4034:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4038:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4039:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4041:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4045:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4047:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4051:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4053:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4058:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4062:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_13_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4064:25
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_10_13_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4068:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4069:21
pub const __AVAILABILITY_INTERNAL__MAC_10_10_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4070:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4071:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4072:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4073:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4075:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4079:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4081:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4085:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4087:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4091:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4092:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4094:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4098:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4100:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4104:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4106:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4111:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4115:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_2_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4116:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4117:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4118:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4120:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4124:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4126:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4130:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4131:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4133:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4137:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4139:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4143:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4145:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4150:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4154:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_3_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4155:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4156:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4157:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4159:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4163:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4164:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4166:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4170:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4172:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4176:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4178:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4183:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4187:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_4_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4188:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4189:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4190:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4191:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4193:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_3 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4197:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_3_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4199:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4203:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4205:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_11_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4210:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4214:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4215:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4217:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4221:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4223:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4227:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4229:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4234:25
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4238:21
pub const __AVAILABILITY_INTERNAL__MAC_10_11_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4239:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4240:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4241:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4242:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4244:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4248:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4250:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4254:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4256:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4260:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_1_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4261:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4262:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4263:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4265:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4269:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4271:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4275:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_2_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4276:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4277:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_4_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4278:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_4_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4280:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_4_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4284:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_4_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4285:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4286:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_1 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4287:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_1_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4289:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_2 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4293:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_2_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4295:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4299:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_4_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4301:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_12_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4306:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4310:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_13_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4312:25
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_13_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4316:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_10_14 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4317:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4318:21
pub const __AVAILABILITY_INTERNAL__MAC_10_12_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4319:21
pub const __AVAILABILITY_INTERNAL__MAC_10_13 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4320:21
pub const __AVAILABILITY_INTERNAL__MAC_10_13_4 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4321:21
pub const __AVAILABILITY_INTERNAL__MAC_10_14 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4322:21
pub const __AVAILABILITY_INTERNAL__MAC_10_14_DEP__MAC_10_14 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4323:21
pub const __AVAILABILITY_INTERNAL__MAC_10_15 = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4324:21
pub const __AVAILABILITY_INTERNAL__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4326:21
pub const __AVAILABILITY_INTERNAL__MAC_NA_DEP__MAC_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4327:21
pub const __AVAILABILITY_INTERNAL__MAC_NA_DEP__MAC_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4328:21
pub const __AVAILABILITY_INTERNAL__IPHONE_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4330:21
pub const __AVAILABILITY_INTERNAL__IPHONE_NA__IPHONE_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4331:21
pub const __AVAILABILITY_INTERNAL__IPHONE_NA_DEP__IPHONE_NA = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4332:21
pub const __AVAILABILITY_INTERNAL__IPHONE_NA_DEP__IPHONE_NA_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4333:21
pub const __AVAILABILITY_INTERNAL__IPHONE_COMPAT_VERSION = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4336:22
pub const __AVAILABILITY_INTERNAL__IPHONE_COMPAT_VERSION_DEP__IPHONE_COMPAT_VERSION = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4337:22
pub const __AVAILABILITY_INTERNAL__IPHONE_COMPAT_VERSION_DEP__IPHONE_COMPAT_VERSION_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternalLegacy.h:4338:22
pub const __API_AVAILABLE_PLATFORM_macos = @compileError("unable to translate macro: undefined identifier `macos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:180:12
pub const __API_DEPRECATED_PLATFORM_macos = @compileError("unable to translate macro: undefined identifier `macos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:181:12
pub const __API_UNAVAILABLE_PLATFORM_macos = @compileError("unable to translate macro: undefined identifier `macos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:182:12
pub const __API_AVAILABLE_PLATFORM_macosx = @compileError("unable to translate macro: undefined identifier `macos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:183:12
pub const __API_DEPRECATED_PLATFORM_macosx = @compileError("unable to translate macro: undefined identifier `macos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:184:12
pub const __API_UNAVAILABLE_PLATFORM_macosx = @compileError("unable to translate macro: undefined identifier `macos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:185:12
pub const __API_AVAILABLE_PLATFORM_ios = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:186:12
pub const __API_DEPRECATED_PLATFORM_ios = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:187:12
pub const __API_UNAVAILABLE_PLATFORM_ios = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:188:12
pub const __API_AVAILABLE_PLATFORM_macCatalyst = @compileError("unable to translate macro: undefined identifier `macCatalyst`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:189:12
pub const __API_DEPRECATED_PLATFORM_macCatalyst = @compileError("unable to translate macro: undefined identifier `macCatalyst`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:190:12
pub const __API_UNAVAILABLE_PLATFORM_macCatalyst = @compileError("unable to translate macro: undefined identifier `macCatalyst`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:191:12
pub const __API_AVAILABLE_PLATFORM_watchos = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:195:12
pub const __API_DEPRECATED_PLATFORM_watchos = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:196:12
pub const __API_UNAVAILABLE_PLATFORM_watchos = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:197:12
pub const __API_AVAILABLE_PLATFORM_tvos = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:198:12
pub const __API_DEPRECATED_PLATFORM_tvos = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:199:12
pub const __API_UNAVAILABLE_PLATFORM_tvos = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:200:12
pub const __API_AVAILABLE_PLATFORM_driverkit = @compileError("unable to translate macro: undefined identifier `driverkit`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:204:12
pub const __API_DEPRECATED_PLATFORM_driverkit = @compileError("unable to translate macro: undefined identifier `driverkit`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:205:12
pub const __API_UNAVAILABLE_PLATFORM_driverkit = @compileError("unable to translate macro: undefined identifier `driverkit`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:206:12
pub const __API_AVAILABLE_PLATFORM_visionos = @compileError("unable to translate macro: undefined identifier `visionos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:207:12
pub const __API_DEPRECATED_PLATFORM_visionos = @compileError("unable to translate macro: undefined identifier `visionos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:208:12
pub const __API_UNAVAILABLE_PLATFORM_visionos = @compileError("unable to translate macro: undefined identifier `visionos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:209:12
pub const __API_AVAILABLE_PLATFORM_xros = @compileError("unable to translate macro: undefined identifier `visionos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:210:12
pub const __API_DEPRECATED_PLATFORM_xros = @compileError("unable to translate macro: undefined identifier `visionos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:211:12
pub const __API_UNAVAILABLE_PLATFORM_xros = @compileError("unable to translate macro: undefined identifier `visionos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:212:12
pub const __API_APPLY_TO = @compileError("unable to translate macro: undefined identifier `any`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:218:11
pub inline fn __API_RANGE_STRINGIFY(x: anytype) @TypeOf(__API_RANGE_STRINGIFY2(x)) {
    _ = &x;
    return __API_RANGE_STRINGIFY2(x);
}
pub const __API_RANGE_STRINGIFY2 = @compileError("unable to translate C expr: unexpected token '#'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:220:11
pub const __API_A = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:234:13
pub inline fn __API_AVAILABLE0(arg0: anytype) @TypeOf(__API_A(arg0)) {
    _ = &arg0;
    return __API_A(arg0);
}
pub inline fn __API_AVAILABLE1(arg0: anytype, arg1: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1)) {
    _ = &arg0;
    _ = &arg1;
    return __API_A(arg0) ++ __API_A(arg1);
}
pub inline fn __API_AVAILABLE2(arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2);
}
pub inline fn __API_AVAILABLE3(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3);
}
pub inline fn __API_AVAILABLE4(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4);
}
pub inline fn __API_AVAILABLE5(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4) ++ __API_A(arg5)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4) ++ __API_A(arg5);
}
pub inline fn __API_AVAILABLE6(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4) ++ __API_A(arg5) ++ __API_A(arg6)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4) ++ __API_A(arg5) ++ __API_A(arg6);
}
pub inline fn __API_AVAILABLE7(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4) ++ __API_A(arg5) ++ __API_A(arg6) ++ __API_A(arg7)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_A(arg0) ++ __API_A(arg1) ++ __API_A(arg2) ++ __API_A(arg3) ++ __API_A(arg4) ++ __API_A(arg5) ++ __API_A(arg6) ++ __API_A(arg7);
}
pub const __API_AVAILABLE_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:244:13
pub const __API_A_BEGIN = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:246:13
pub inline fn __API_AVAILABLE_BEGIN0(arg0: anytype) @TypeOf(__API_A_BEGIN(arg0)) {
    _ = &arg0;
    return __API_A_BEGIN(arg0);
}
pub inline fn __API_AVAILABLE_BEGIN1(arg0: anytype, arg1: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1)) {
    _ = &arg0;
    _ = &arg1;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1);
}
pub inline fn __API_AVAILABLE_BEGIN2(arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2);
}
pub inline fn __API_AVAILABLE_BEGIN3(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3);
}
pub inline fn __API_AVAILABLE_BEGIN4(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4);
}
pub inline fn __API_AVAILABLE_BEGIN5(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4) ++ __API_A_BEGIN(arg5)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4) ++ __API_A_BEGIN(arg5);
}
pub inline fn __API_AVAILABLE_BEGIN6(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4) ++ __API_A_BEGIN(arg5) ++ __API_A_BEGIN(arg6)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4) ++ __API_A_BEGIN(arg5) ++ __API_A_BEGIN(arg6);
}
pub inline fn __API_AVAILABLE_BEGIN7(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4) ++ __API_A_BEGIN(arg5) ++ __API_A_BEGIN(arg6) ++ __API_A_BEGIN(arg7)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_A_BEGIN(arg0) ++ __API_A_BEGIN(arg1) ++ __API_A_BEGIN(arg2) ++ __API_A_BEGIN(arg3) ++ __API_A_BEGIN(arg4) ++ __API_A_BEGIN(arg5) ++ __API_A_BEGIN(arg6) ++ __API_A_BEGIN(arg7);
}
pub const __API_AVAILABLE_BEGIN_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:256:13
pub const __API_D = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:260:13
pub inline fn __API_DEPRECATED_MSG0(msg: anytype, arg0: anytype) @TypeOf(__API_D(msg, arg0)) {
    _ = &msg;
    _ = &arg0;
    return __API_D(msg, arg0);
}
pub inline fn __API_DEPRECATED_MSG1(msg: anytype, arg0: anytype, arg1: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1);
}
pub inline fn __API_DEPRECATED_MSG2(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2);
}
pub inline fn __API_DEPRECATED_MSG3(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3);
}
pub inline fn __API_DEPRECATED_MSG4(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4);
}
pub inline fn __API_DEPRECATED_MSG5(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4) ++ __API_D(msg, arg5)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4) ++ __API_D(msg, arg5);
}
pub inline fn __API_DEPRECATED_MSG6(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4) ++ __API_D(msg, arg5) ++ __API_D(msg, arg6)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4) ++ __API_D(msg, arg5) ++ __API_D(msg, arg6);
}
pub inline fn __API_DEPRECATED_MSG7(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4) ++ __API_D(msg, arg5) ++ __API_D(msg, arg6) ++ __API_D(msg, arg7)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_D(msg, arg0) ++ __API_D(msg, arg1) ++ __API_D(msg, arg2) ++ __API_D(msg, arg3) ++ __API_D(msg, arg4) ++ __API_D(msg, arg5) ++ __API_D(msg, arg6) ++ __API_D(msg, arg7);
}
pub const __API_DEPRECATED_MSG_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:270:13
pub const __API_D_BEGIN = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:272:13
pub inline fn __API_DEPRECATED_BEGIN0(msg: anytype, arg0: anytype) @TypeOf(__API_D_BEGIN(msg, arg0)) {
    _ = &msg;
    _ = &arg0;
    return __API_D_BEGIN(msg, arg0);
}
pub inline fn __API_DEPRECATED_BEGIN1(msg: anytype, arg0: anytype, arg1: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1);
}
pub inline fn __API_DEPRECATED_BEGIN2(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2);
}
pub inline fn __API_DEPRECATED_BEGIN3(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3);
}
pub inline fn __API_DEPRECATED_BEGIN4(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4);
}
pub inline fn __API_DEPRECATED_BEGIN5(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4) ++ __API_D_BEGIN(msg, arg5)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4) ++ __API_D_BEGIN(msg, arg5);
}
pub inline fn __API_DEPRECATED_BEGIN6(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4) ++ __API_D_BEGIN(msg, arg5) ++ __API_D_BEGIN(msg, arg6)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4) ++ __API_D_BEGIN(msg, arg5) ++ __API_D_BEGIN(msg, arg6);
}
pub inline fn __API_DEPRECATED_BEGIN7(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4) ++ __API_D_BEGIN(msg, arg5) ++ __API_D_BEGIN(msg, arg6) ++ __API_D_BEGIN(msg, arg7)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_D_BEGIN(msg, arg0) ++ __API_D_BEGIN(msg, arg1) ++ __API_D_BEGIN(msg, arg2) ++ __API_D_BEGIN(msg, arg3) ++ __API_D_BEGIN(msg, arg4) ++ __API_D_BEGIN(msg, arg5) ++ __API_D_BEGIN(msg, arg6) ++ __API_D_BEGIN(msg, arg7);
}
pub const __API_DEPRECATED_BEGIN_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:282:13
pub const __API_R = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:285:17
pub inline fn __API_DEPRECATED_REP0(msg: anytype, arg0: anytype) @TypeOf(__API_R(msg, arg0)) {
    _ = &msg;
    _ = &arg0;
    return __API_R(msg, arg0);
}
pub inline fn __API_DEPRECATED_REP1(msg: anytype, arg0: anytype, arg1: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1);
}
pub inline fn __API_DEPRECATED_REP2(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2);
}
pub inline fn __API_DEPRECATED_REP3(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3);
}
pub inline fn __API_DEPRECATED_REP4(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4);
}
pub inline fn __API_DEPRECATED_REP5(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4) ++ __API_R(msg, arg5)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4) ++ __API_R(msg, arg5);
}
pub inline fn __API_DEPRECATED_REP6(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4) ++ __API_R(msg, arg5) ++ __API_R(msg, arg6)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4) ++ __API_R(msg, arg5) ++ __API_R(msg, arg6);
}
pub inline fn __API_DEPRECATED_REP7(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4) ++ __API_R(msg, arg5) ++ __API_R(msg, arg6) ++ __API_R(msg, arg7)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_R(msg, arg0) ++ __API_R(msg, arg1) ++ __API_R(msg, arg2) ++ __API_R(msg, arg3) ++ __API_R(msg, arg4) ++ __API_R(msg, arg5) ++ __API_R(msg, arg6) ++ __API_R(msg, arg7);
}
pub const __API_DEPRECATED_REP_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:298:13
pub const __API_R_BEGIN = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:301:17
pub inline fn __API_DEPRECATED_BEGIN_REP0(msg: anytype, arg0: anytype) @TypeOf(__API_R_BEGIN(msg, arg0)) {
    _ = &msg;
    _ = &arg0;
    return __API_R_BEGIN(msg, arg0);
}
pub inline fn __API_DEPRECATED_BEGIN_REP1(msg: anytype, arg0: anytype, arg1: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1);
}
pub inline fn __API_DEPRECATED_BEGIN_REP2(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2);
}
pub inline fn __API_DEPRECATED_BEGIN_REP3(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3);
}
pub inline fn __API_DEPRECATED_BEGIN_REP4(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4);
}
pub inline fn __API_DEPRECATED_BEGIN_REP5(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4) ++ __API_R_BEGIN(msg, arg5)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4) ++ __API_R_BEGIN(msg, arg5);
}
pub inline fn __API_DEPRECATED_BEGIN_REP6(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4) ++ __API_R_BEGIN(msg, arg5) ++ __API_R_BEGIN(msg, arg6)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4) ++ __API_R_BEGIN(msg, arg5) ++ __API_R_BEGIN(msg, arg6);
}
pub inline fn __API_DEPRECATED_BEGIN_REP7(msg: anytype, arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4) ++ __API_R_BEGIN(msg, arg5) ++ __API_R_BEGIN(msg, arg6) ++ __API_R_BEGIN(msg, arg7)) {
    _ = &msg;
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_R_BEGIN(msg, arg0) ++ __API_R_BEGIN(msg, arg1) ++ __API_R_BEGIN(msg, arg2) ++ __API_R_BEGIN(msg, arg3) ++ __API_R_BEGIN(msg, arg4) ++ __API_R_BEGIN(msg, arg5) ++ __API_R_BEGIN(msg, arg6) ++ __API_R_BEGIN(msg, arg7);
}
pub const __API_DEPRECATED_BEGIN_REP_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:314:13
pub const __API_U = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:325:13
pub inline fn __API_UNAVAILABLE0(arg0: anytype) @TypeOf(__API_U(arg0)) {
    _ = &arg0;
    return __API_U(arg0);
}
pub inline fn __API_UNAVAILABLE1(arg0: anytype, arg1: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1)) {
    _ = &arg0;
    _ = &arg1;
    return __API_U(arg0) ++ __API_U(arg1);
}
pub inline fn __API_UNAVAILABLE2(arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2);
}
pub inline fn __API_UNAVAILABLE3(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3);
}
pub inline fn __API_UNAVAILABLE4(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4);
}
pub inline fn __API_UNAVAILABLE5(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4) ++ __API_U(arg5)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4) ++ __API_U(arg5);
}
pub inline fn __API_UNAVAILABLE6(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4) ++ __API_U(arg5) ++ __API_U(arg6)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4) ++ __API_U(arg5) ++ __API_U(arg6);
}
pub inline fn __API_UNAVAILABLE7(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4) ++ __API_U(arg5) ++ __API_U(arg6) ++ __API_U(arg7)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_U(arg0) ++ __API_U(arg1) ++ __API_U(arg2) ++ __API_U(arg3) ++ __API_U(arg4) ++ __API_U(arg5) ++ __API_U(arg6) ++ __API_U(arg7);
}
pub const __API_UNAVAILABLE_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:335:13
pub const __API_U_BEGIN = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:337:13
pub inline fn __API_UNAVAILABLE_BEGIN0(arg0: anytype) @TypeOf(__API_U_BEGIN(arg0)) {
    _ = &arg0;
    return __API_U_BEGIN(arg0);
}
pub inline fn __API_UNAVAILABLE_BEGIN1(arg0: anytype, arg1: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1)) {
    _ = &arg0;
    _ = &arg1;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1);
}
pub inline fn __API_UNAVAILABLE_BEGIN2(arg0: anytype, arg1: anytype, arg2: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2);
}
pub inline fn __API_UNAVAILABLE_BEGIN3(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3);
}
pub inline fn __API_UNAVAILABLE_BEGIN4(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4);
}
pub inline fn __API_UNAVAILABLE_BEGIN5(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4) ++ __API_U_BEGIN(arg5)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4) ++ __API_U_BEGIN(arg5);
}
pub inline fn __API_UNAVAILABLE_BEGIN6(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4) ++ __API_U_BEGIN(arg5) ++ __API_U_BEGIN(arg6)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4) ++ __API_U_BEGIN(arg5) ++ __API_U_BEGIN(arg6);
}
pub inline fn __API_UNAVAILABLE_BEGIN7(arg0: anytype, arg1: anytype, arg2: anytype, arg3: anytype, arg4: anytype, arg5: anytype, arg6: anytype, arg7: anytype) @TypeOf(__API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4) ++ __API_U_BEGIN(arg5) ++ __API_U_BEGIN(arg6) ++ __API_U_BEGIN(arg7)) {
    _ = &arg0;
    _ = &arg1;
    _ = &arg2;
    _ = &arg3;
    _ = &arg4;
    _ = &arg5;
    _ = &arg6;
    _ = &arg7;
    return __API_U_BEGIN(arg0) ++ __API_U_BEGIN(arg1) ++ __API_U_BEGIN(arg2) ++ __API_U_BEGIN(arg3) ++ __API_U_BEGIN(arg4) ++ __API_U_BEGIN(arg5) ++ __API_U_BEGIN(arg6) ++ __API_U_BEGIN(arg7);
}
pub const __API_UNAVAILABLE_BEGIN_GET_MACRO = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:347:13
pub const __swift_compiler_version_at_least = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:369:13
pub const __SPI_AVAILABLE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/AvailabilityInternal.h:377:11
pub const __OSX_AVAILABLE_STARTING = @compileError("unable to translate macro: undefined identifier `__AVAILABILITY_INTERNAL`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:209:17
pub const __OSX_AVAILABLE_BUT_DEPRECATED = @compileError("unable to translate macro: undefined identifier `__AVAILABILITY_INTERNAL`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:210:17
pub const __OSX_AVAILABLE_BUT_DEPRECATED_MSG = @compileError("unable to translate macro: undefined identifier `__AVAILABILITY_INTERNAL`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:212:17
pub const __OS_AVAILABILITY = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:235:13
pub const __OS_AVAILABILITY_MSG = @compileError("unable to translate macro: undefined identifier `availability`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:236:13
pub const __OSX_EXTENSION_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `macosx_app_extension`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:253:13
pub const __IOS_EXTENSION_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `ios_app_extension`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:254:13
pub inline fn __OS_EXTENSION_UNAVAILABLE(_msg: anytype) @TypeOf(__OSX_EXTENSION_UNAVAILABLE(_msg) ++ __IOS_EXTENSION_UNAVAILABLE(_msg)) {
    _ = &_msg;
    return __OSX_EXTENSION_UNAVAILABLE(_msg) ++ __IOS_EXTENSION_UNAVAILABLE(_msg);
}
pub const __OSX_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `macosx`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:271:13
pub const __OSX_AVAILABLE = @compileError("unable to translate macro: undefined identifier `macosx`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:272:13
pub const __OSX_DEPRECATED = @compileError("unable to translate macro: undefined identifier `macosx`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:273:13
pub const __IOS_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:297:13
pub const __IOS_PROHIBITED = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:299:15
pub const __IOS_AVAILABLE = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:301:13
pub const __IOS_DEPRECATED = @compileError("unable to translate macro: undefined identifier `ios`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:302:13
pub const __TVOS_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:326:13
pub const __TVOS_PROHIBITED = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:328:15
pub const __TVOS_AVAILABLE = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:330:13
pub const __TVOS_DEPRECATED = @compileError("unable to translate macro: undefined identifier `tvos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:331:13
pub const __WATCHOS_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:355:13
pub const __WATCHOS_PROHIBITED = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:357:15
pub const __WATCHOS_AVAILABLE = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:359:13
pub const __WATCHOS_DEPRECATED = @compileError("unable to translate macro: undefined identifier `watchos`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:360:13
pub const __SWIFT_UNAVAILABLE = @compileError("unable to translate macro: undefined identifier `swift`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:383:13
pub const __SWIFT_UNAVAILABLE_MSG = @compileError("unable to translate macro: undefined identifier `swift`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:384:13
pub const __API_AVAILABLE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:427:13
pub const __API_AVAILABLE_BEGIN = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:429:13
pub const __API_AVAILABLE_END = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:430:13
pub const __API_DEPRECATED = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:448:13
pub const __API_DEPRECATED_WITH_REPLACEMENT = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:449:13
pub const __API_DEPRECATED_BEGIN = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:451:13
pub const __API_DEPRECATED_END = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:452:13
pub const __API_DEPRECATED_WITH_REPLACEMENT_BEGIN = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:454:13
pub const __API_DEPRECATED_WITH_REPLACEMENT_END = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:455:13
pub const __API_UNAVAILABLE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:465:13
pub const __API_UNAVAILABLE_BEGIN = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:467:13
pub const __API_UNAVAILABLE_END = @compileError("unable to translate macro: undefined identifier `_Pragma`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:468:13
pub const __SPI_DEPRECATED = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:533:11
pub const __SPI_DEPRECATED_WITH_REPLACEMENT = @compileError("unable to translate C expr: expected ')' instead got '...'");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/Availability.h:537:11
pub const _SIZE_T = "";
pub const NULL = __DARWIN_NULL;
pub const _RSIZE_T = "";
pub const _BSD_MACHINE_TYPES_H_ = "";
pub const _ARM_MACHTYPES_H_ = "";
pub const _MACHTYPES_H_ = "";
pub const _INT8_T = "";
pub const _INT16_T = "";
pub const _INT32_T = "";
pub const _INT64_T = "";
pub const _U_INT8_T = "";
pub const _U_INT16_T = "";
pub const _U_INT32_T = "";
pub const _U_INT64_T = "";
pub const _INTPTR_T = "";
pub const _UINTPTR_T = "";
pub const USER_ADDR_NULL = @import("std").zig.c_translation.cast(user_addr_t, @as(c_int, 0));
pub inline fn CAST_USER_ADDR_T(a_ptr: anytype) user_addr_t {
    _ = &a_ptr;
    return @import("std").zig.c_translation.cast(user_addr_t, @import("std").zig.c_translation.cast(usize, a_ptr));
}
pub const _ERRNO_T = "";
pub const _SSIZE_T = "";
pub const _STRINGS_H_ = "";
pub const _SECURE__STRINGS_H_ = "";
pub const _SECURE__COMMON_H_ = "";
pub const _USE_FORTIFY_LEVEL = @as(c_int, 2);
pub inline fn __darwin_obsz0(object: anytype) @TypeOf(__builtin_object_size(object, @as(c_int, 0))) {
    _ = &object;
    return __builtin_object_size(object, @as(c_int, 0));
}
pub inline fn __darwin_obsz(object: anytype) @TypeOf(__builtin_object_size(object, if (_USE_FORTIFY_LEVEL > @as(c_int, 1)) @as(c_int, 1) else @as(c_int, 0))) {
    _ = &object;
    return __builtin_object_size(object, if (_USE_FORTIFY_LEVEL > @as(c_int, 1)) @as(c_int, 1) else @as(c_int, 0));
}
pub const _SECURE__STRING_H_ = "";
pub const __HAS_FIXED_CHK_PROTOTYPES = @as(c_int, 1);
pub const __CLANG_STDINT_H = "";
pub const _STDINT_H_ = "";
pub const __WORDSIZE = @as(c_int, 64);
pub const _UINT8_T = "";
pub const _UINT16_T = "";
pub const _UINT32_T = "";
pub const _UINT64_T = "";
pub const _INTMAX_T = "";
pub const _UINTMAX_T = "";
pub inline fn INT8_C(v: anytype) @TypeOf(v) {
    _ = &v;
    return v;
}
pub inline fn INT16_C(v: anytype) @TypeOf(v) {
    _ = &v;
    return v;
}
pub inline fn INT32_C(v: anytype) @TypeOf(v) {
    _ = &v;
    return v;
}
pub const INT64_C = @import("std").zig.c_translation.Macros.LL_SUFFIX;
pub inline fn UINT8_C(v: anytype) @TypeOf(v) {
    _ = &v;
    return v;
}
pub inline fn UINT16_C(v: anytype) @TypeOf(v) {
    _ = &v;
    return v;
}
pub const UINT32_C = @import("std").zig.c_translation.Macros.U_SUFFIX;
pub const UINT64_C = @import("std").zig.c_translation.Macros.ULL_SUFFIX;
pub const INTMAX_C = @import("std").zig.c_translation.Macros.L_SUFFIX;
pub const UINTMAX_C = @import("std").zig.c_translation.Macros.UL_SUFFIX;
pub const INT8_MAX = @as(c_int, 127);
pub const INT16_MAX = @as(c_int, 32767);
pub const INT32_MAX = @import("std").zig.c_translation.promoteIntLiteral(c_int, 2147483647, .decimal);
pub const INT64_MAX = @as(c_longlong, 9223372036854775807);
pub const INT8_MIN = -@as(c_int, 128);
pub const INT16_MIN = -@import("std").zig.c_translation.promoteIntLiteral(c_int, 32768, .decimal);
pub const INT32_MIN = -INT32_MAX - @as(c_int, 1);
pub const INT64_MIN = -INT64_MAX - @as(c_int, 1);
pub const UINT8_MAX = @as(c_int, 255);
pub const UINT16_MAX = @import("std").zig.c_translation.promoteIntLiteral(c_int, 65535, .decimal);
pub const UINT32_MAX = @import("std").zig.c_translation.promoteIntLiteral(c_uint, 4294967295, .decimal);
pub const UINT64_MAX = @as(c_ulonglong, 18446744073709551615);
pub const INT_LEAST8_MIN = INT8_MIN;
pub const INT_LEAST16_MIN = INT16_MIN;
pub const INT_LEAST32_MIN = INT32_MIN;
pub const INT_LEAST64_MIN = INT64_MIN;
pub const INT_LEAST8_MAX = INT8_MAX;
pub const INT_LEAST16_MAX = INT16_MAX;
pub const INT_LEAST32_MAX = INT32_MAX;
pub const INT_LEAST64_MAX = INT64_MAX;
pub const UINT_LEAST8_MAX = UINT8_MAX;
pub const UINT_LEAST16_MAX = UINT16_MAX;
pub const UINT_LEAST32_MAX = UINT32_MAX;
pub const UINT_LEAST64_MAX = UINT64_MAX;
pub const INT_FAST8_MIN = INT8_MIN;
pub const INT_FAST16_MIN = INT16_MIN;
pub const INT_FAST32_MIN = INT32_MIN;
pub const INT_FAST64_MIN = INT64_MIN;
pub const INT_FAST8_MAX = INT8_MAX;
pub const INT_FAST16_MAX = INT16_MAX;
pub const INT_FAST32_MAX = INT32_MAX;
pub const INT_FAST64_MAX = INT64_MAX;
pub const UINT_FAST8_MAX = UINT8_MAX;
pub const UINT_FAST16_MAX = UINT16_MAX;
pub const UINT_FAST32_MAX = UINT32_MAX;
pub const UINT_FAST64_MAX = UINT64_MAX;
pub const INTPTR_MAX = @import("std").zig.c_translation.promoteIntLiteral(c_long, 9223372036854775807, .decimal);
pub const INTPTR_MIN = -INTPTR_MAX - @as(c_int, 1);
pub const UINTPTR_MAX = @import("std").zig.c_translation.promoteIntLiteral(c_ulong, 18446744073709551615, .decimal);
pub const INTMAX_MAX = INTMAX_C(@import("std").zig.c_translation.promoteIntLiteral(c_int, 9223372036854775807, .decimal));
pub const UINTMAX_MAX = UINTMAX_C(@import("std").zig.c_translation.promoteIntLiteral(c_int, 18446744073709551615, .decimal));
pub const INTMAX_MIN = -INTMAX_MAX - @as(c_int, 1);
pub const PTRDIFF_MIN = INTMAX_MIN;
pub const PTRDIFF_MAX = INTMAX_MAX;
pub const SIZE_MAX = UINTPTR_MAX;
pub const RSIZE_MAX = SIZE_MAX >> @as(c_int, 1);
pub const WCHAR_MAX = __WCHAR_MAX__;
pub const WCHAR_MIN = -WCHAR_MAX - @as(c_int, 1);
pub const WINT_MIN = INT32_MIN;
pub const WINT_MAX = INT32_MAX;
pub const SIG_ATOMIC_MIN = INT32_MIN;
pub const SIG_ATOMIC_MAX = INT32_MAX;
pub const FLECS_BAKE_CONFIG_H = "";
pub const FLECS_API = "";
pub const FLECS_DBG_API = "";
pub const __STDBOOL_H = "";
pub const __bool_true_false_are_defined = @as(c_int, 1);
pub const @"bool" = bool;
pub const @"true" = @as(c_int, 1);
pub const @"false" = @as(c_int, 0);
pub const ecs_flagsn_t_ = @compileError("unable to translate macro: undefined identifier `ecs_flags`");
// ../flecs/distr/flecs.h:808:9
pub inline fn ecs_flagsn_t(bits: anytype) @TypeOf(ecs_flagsn_t_(bits)) {
    _ = &bits;
    return ecs_flagsn_t_(bits);
}
pub const ecs_termset_t = ecs_flagsn_t(FLECS_TERM_COUNT_MAX);
pub const ECS_TERMSET_SET = @compileError("unable to translate C expr: expected ')' instead got '|='");
// ../flecs/distr/flecs.h:815:9
pub const ECS_TERMSET_CLEAR = @compileError("unable to translate C expr: expected ')' instead got '&='");
// ../flecs/distr/flecs.h:816:9
pub inline fn ECS_TERMSET_COND(set: anytype, flag: anytype, cond: anytype) @TypeOf(if (cond) ECS_TERMSET_SET(set, flag) else ECS_TERMSET_CLEAR(set, flag)) {
    _ = &set;
    _ = &flag;
    _ = &cond;
    return if (cond) ECS_TERMSET_SET(set, flag) else ECS_TERMSET_CLEAR(set, flag);
}
pub inline fn ECS_SIZEOF(T: anytype) @TypeOf(ECS_CAST(ecs_size_t, @import("std").zig.c_translation.sizeof(T))) {
    _ = &T;
    return ECS_CAST(ecs_size_t, @import("std").zig.c_translation.sizeof(T));
}
pub const ECS_ALIGNOF = @compileError("unable to translate C expr: unexpected token '__alignof__'");
// ../flecs/distr/flecs.h:835:9
pub const ECS_DEPRECATED = @compileError("unable to translate macro: undefined identifier `deprecated`");
// ../flecs/distr/flecs.h:844:9
pub inline fn ECS_ALIGN(size: anytype, alignment: anytype) ecs_size_t {
    _ = &size;
    _ = &alignment;
    return @import("std").zig.c_translation.cast(ecs_size_t, (@import("std").zig.c_translation.MacroArithmetic.div(@import("std").zig.c_translation.cast(usize, size) - @as(c_int, 1), @import("std").zig.c_translation.cast(usize, alignment)) + @as(c_int, 1)) * @import("std").zig.c_translation.cast(usize, alignment));
}
pub inline fn ECS_MAX(a: anytype, b: anytype) @TypeOf(if (a > b) a else b) {
    _ = &a;
    _ = &b;
    return if (a > b) a else b;
}
pub inline fn ECS_MIN(a: anytype, b: anytype) @TypeOf(if (a < b) a else b) {
    _ = &a;
    _ = &b;
    return if (a < b) a else b;
}
pub const ECS_CAST = @import("std").zig.c_translation.Macros.CAST_OR_CALL;
pub inline fn ECS_CONST_CAST(@"type": anytype, value: anytype) @TypeOf(@"type"(usize)(value)) {
    _ = &@"type";
    _ = &value;
    return @"type"(usize)(value);
}
pub inline fn ECS_PTR_CAST(@"type": anytype, value: anytype) @TypeOf(@"type"(usize)(value)) {
    _ = &@"type";
    _ = &value;
    return @"type"(usize)(value);
}
pub inline fn ECS_EQ(a: anytype, b: anytype) @TypeOf(ecs_os_memcmp(&a, &b, @import("std").zig.c_translation.sizeof(a)) == @as(c_int, 0)) {
    _ = &a;
    _ = &b;
    return ecs_os_memcmp(&a, &b, @import("std").zig.c_translation.sizeof(a)) == @as(c_int, 0);
}
pub inline fn ECS_NEQ(a: anytype, b: anytype) @TypeOf(!(ECS_EQ(a, b) != 0)) {
    _ = &a;
    _ = &b;
    return !(ECS_EQ(a, b) != 0);
}
pub inline fn ECS_EQZERO(a: anytype) @TypeOf(ECS_EQ(a, @import("std").mem.zeroInit(u64, .{@as(c_int, 0)}))) {
    _ = &a;
    return ECS_EQ(a, @import("std").mem.zeroInit(u64, .{@as(c_int, 0)}));
}
pub inline fn ECS_NEQZERO(a: anytype) @TypeOf(ECS_NEQ(a, @import("std").mem.zeroInit(u64, .{@as(c_int, 0)}))) {
    _ = &a;
    return ECS_NEQ(a, @import("std").mem.zeroInit(u64, .{@as(c_int, 0)}));
}
pub const FLECS_VERSION_IMPLSTR = @compileError("unable to translate C expr: unexpected token '#'");
// ../flecs/distr/flecs.h:889:9
pub inline fn FLECS_VERSION_IMPL(major: anytype, minor: anytype, patch: anytype) @TypeOf(FLECS_VERSION_IMPLSTR(major, minor, patch)) {
    _ = &major;
    _ = &minor;
    _ = &patch;
    return FLECS_VERSION_IMPLSTR(major, minor, patch);
}
pub const ECS_CONCAT = @compileError("unable to translate C expr: unexpected token '##'");
// ../flecs/distr/flecs.h:893:9
pub const ecs_world_t_magic = @import("std").zig.c_translation.promoteIntLiteral(c_int, 0x65637377, .hex);
pub const ecs_stage_t_magic = @import("std").zig.c_translation.promoteIntLiteral(c_int, 0x65637373, .hex);
pub const ecs_query_t_magic = @import("std").zig.c_translation.promoteIntLiteral(c_int, 0x65637375, .hex);
pub const ecs_observer_t_magic = @import("std").zig.c_translation.promoteIntLiteral(c_int, 0x65637362, .hex);
pub const ECS_ROW_MASK = @import("std").zig.c_translation.promoteIntLiteral(c_uint, 0x0FFFFFFF, .hex);
pub const ECS_ROW_FLAGS_MASK = ~ECS_ROW_MASK;
pub inline fn ECS_RECORD_TO_ROW(v: anytype) @TypeOf(ECS_CAST(i32, ECS_CAST(u32, v) & ECS_ROW_MASK)) {
    _ = &v;
    return ECS_CAST(i32, ECS_CAST(u32, v) & ECS_ROW_MASK);
}
pub inline fn ECS_RECORD_TO_ROW_FLAGS(v: anytype) @TypeOf(ECS_CAST(u32, v) & ECS_ROW_FLAGS_MASK) {
    _ = &v;
    return ECS_CAST(u32, v) & ECS_ROW_FLAGS_MASK;
}
pub inline fn ECS_ROW_TO_RECORD(row: anytype, flags: anytype) @TypeOf(ECS_CAST(u32, ECS_CAST(u32, row) | flags)) {
    _ = &row;
    _ = &flags;
    return ECS_CAST(u32, ECS_CAST(u32, row) | flags);
}
pub const ECS_ID_FLAGS_MASK = @as(c_ulonglong, 0xFF) << @as(c_int, 60);
pub const ECS_ENTITY_MASK = @as(c_ulonglong, 0xFFFFFFFF);
pub const ECS_GENERATION_MASK = @as(c_ulonglong, 0xFFFF) << @as(c_int, 32);
pub inline fn ECS_GENERATION(e: anytype) @TypeOf((e & ECS_GENERATION_MASK) >> @as(c_int, 32)) {
    _ = &e;
    return (e & ECS_GENERATION_MASK) >> @as(c_int, 32);
}
pub inline fn ECS_GENERATION_INC(e: anytype) @TypeOf((e & ~ECS_GENERATION_MASK) | ((@import("std").zig.c_translation.promoteIntLiteral(c_int, 0xFFFF, .hex) & (ECS_GENERATION(e) + @as(c_int, 1))) << @as(c_int, 32))) {
    _ = &e;
    return (e & ~ECS_GENERATION_MASK) | ((@import("std").zig.c_translation.promoteIntLiteral(c_int, 0xFFFF, .hex) & (ECS_GENERATION(e) + @as(c_int, 1))) << @as(c_int, 32));
}
pub const ECS_COMPONENT_MASK = ~ECS_ID_FLAGS_MASK;
pub const ECS_HAS_ID_FLAG = @compileError("unable to translate macro: undefined identifier `ECS_`");
// ../flecs/distr/flecs.h:922:9
pub inline fn ECS_IS_PAIR(id: anytype) @TypeOf((id & ECS_ID_FLAGS_MASK) == ECS_PAIR) {
    _ = &id;
    return (id & ECS_ID_FLAGS_MASK) == ECS_PAIR;
}
pub inline fn ECS_PAIR_FIRST(e: anytype) @TypeOf(ecs_entity_t_hi(e & ECS_COMPONENT_MASK)) {
    _ = &e;
    return ecs_entity_t_hi(e & ECS_COMPONENT_MASK);
}
pub inline fn ECS_PAIR_SECOND(e: anytype) @TypeOf(ecs_entity_t_lo(e)) {
    _ = &e;
    return ecs_entity_t_lo(e);
}
pub const ECS_HAS_RELATION = @compileError("unable to translate macro: undefined identifier `PAIR`");
// ../flecs/distr/flecs.h:926:9
pub inline fn ECS_TERM_REF_FLAGS(ref: anytype) @TypeOf(ref.*.id & EcsTermRefFlags) {
    _ = &ref;
    return ref.*.id & EcsTermRefFlags;
}
pub inline fn ECS_TERM_REF_ID(ref: anytype) @TypeOf(ref.*.id & ~EcsTermRefFlags) {
    _ = &ref;
    return ref.*.id & ~EcsTermRefFlags;
}
pub const ecs_id = @compileError("unable to translate macro: undefined identifier `FLECS_ID`");
// ../flecs/distr/flecs.h:936:9
pub inline fn ecs_entity_t_lo(value: anytype) @TypeOf(ECS_CAST(u32, value)) {
    _ = &value;
    return ECS_CAST(u32, value);
}
pub inline fn ecs_entity_t_hi(value: anytype) @TypeOf(ECS_CAST(u32, value >> @as(c_int, 32))) {
    _ = &value;
    return ECS_CAST(u32, value >> @as(c_int, 32));
}
pub inline fn ecs_entity_t_comb(lo: anytype, hi: anytype) @TypeOf((ECS_CAST(u64, hi) << @as(c_int, 32)) + ECS_CAST(u32, lo)) {
    _ = &lo;
    _ = &hi;
    return (ECS_CAST(u64, hi) << @as(c_int, 32)) + ECS_CAST(u32, lo);
}
pub inline fn ecs_pair(pred: anytype, obj: anytype) @TypeOf(ECS_PAIR | ecs_entity_t_comb(obj, pred)) {
    _ = &pred;
    _ = &obj;
    return ECS_PAIR | ecs_entity_t_comb(obj, pred);
}
pub inline fn ecs_pair_t(pred: anytype, obj: anytype) @TypeOf(ECS_PAIR | ecs_entity_t_comb(obj, ecs_id(pred))) {
    _ = &pred;
    _ = &obj;
    return ECS_PAIR | ecs_entity_t_comb(obj, ecs_id(pred));
}
pub inline fn ecs_pair_first(world: anytype, pair: anytype) @TypeOf(ecs_get_alive(world, ECS_PAIR_FIRST(pair))) {
    _ = &world;
    _ = &pair;
    return ecs_get_alive(world, ECS_PAIR_FIRST(pair));
}
pub inline fn ecs_pair_second(world: anytype, pair: anytype) @TypeOf(ecs_get_alive(world, ECS_PAIR_SECOND(pair))) {
    _ = &world;
    _ = &pair;
    return ecs_get_alive(world, ECS_PAIR_SECOND(pair));
}
pub const ecs_pair_relation = ecs_pair_first;
pub const ecs_pair_target = ecs_pair_second;
pub inline fn flecs_poly_id(tag: anytype) @TypeOf(ecs_pair(ecs_id(EcsPoly), tag)) {
    _ = &tag;
    return ecs_pair(ecs_id(EcsPoly), tag);
}
pub inline fn ECS_TABLE_LOCK(world: anytype, table: anytype) @TypeOf(ecs_table_lock(world, table)) {
    _ = &world;
    _ = &table;
    return ecs_table_lock(world, table);
}
pub inline fn ECS_TABLE_UNLOCK(world: anytype, table: anytype) @TypeOf(ecs_table_unlock(world, table)) {
    _ = &world;
    _ = &table;
    return ecs_table_unlock(world, table);
}
pub const EcsIterNextYield = @as(c_int, 0);
pub const EcsIterYield = -@as(c_int, 1);
pub const EcsIterNext = @as(c_int, 1);
pub const ECS_XTOR_IMPL = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:985:9
pub const ECS_COPY_IMPL = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:1002:9
pub const ECS_MOVE_IMPL = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:1023:9
pub const ECS_HOOK_IMPL = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:1043:9
pub const FLECS_VEC_H = "";
pub inline fn ecs_vec_init_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_init(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_init(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub inline fn ecs_vec_init_if_t(vec: anytype, T: anytype) @TypeOf(ecs_vec_init_if(vec, ECS_SIZEOF(T))) {
    _ = &vec;
    _ = &T;
    return ecs_vec_init_if(vec, ECS_SIZEOF(T));
}
pub inline fn ecs_vec_fini_t(allocator: anytype, vec: anytype, T: anytype) @TypeOf(ecs_vec_fini(allocator, vec, ECS_SIZEOF(T))) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    return ecs_vec_fini(allocator, vec, ECS_SIZEOF(T));
}
pub inline fn ecs_vec_reset_t(allocator: anytype, vec: anytype, T: anytype) @TypeOf(ecs_vec_reset(allocator, vec, ECS_SIZEOF(T))) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    return ecs_vec_reset(allocator, vec, ECS_SIZEOF(T));
}
pub const ecs_vec_append_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1132:9
pub inline fn ecs_vec_remove_t(vec: anytype, T: anytype, elem: anytype) @TypeOf(ecs_vec_remove(vec, ECS_SIZEOF(T), elem)) {
    _ = &vec;
    _ = &T;
    _ = &elem;
    return ecs_vec_remove(vec, ECS_SIZEOF(T), elem);
}
pub inline fn ecs_vec_copy_t(allocator: anytype, vec: anytype, T: anytype) @TypeOf(ecs_vec_copy(allocator, vec, ECS_SIZEOF(T))) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    return ecs_vec_copy(allocator, vec, ECS_SIZEOF(T));
}
pub inline fn ecs_vec_copy_shrink_t(allocator: anytype, vec: anytype, T: anytype) @TypeOf(ecs_vec_copy_shrink(allocator, vec, ECS_SIZEOF(T))) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    return ecs_vec_copy_shrink(allocator, vec, ECS_SIZEOF(T));
}
pub inline fn ecs_vec_reclaim_t(allocator: anytype, vec: anytype, T: anytype) @TypeOf(ecs_vec_reclaim(allocator, vec, ECS_SIZEOF(T))) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    return ecs_vec_reclaim(allocator, vec, ECS_SIZEOF(T));
}
pub inline fn ecs_vec_set_size_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_set_size(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_set_size(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub inline fn ecs_vec_set_min_size_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_set_min_size(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_set_min_size(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub inline fn ecs_vec_set_min_count_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_set_min_count(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_set_min_count(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub inline fn ecs_vec_set_min_count_zeromem_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_set_min_count_zeromem(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_set_min_count_zeromem(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub inline fn ecs_vec_set_count_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_set_count(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_set_count(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub inline fn ecs_vec_grow_t(allocator: anytype, vec: anytype, T: anytype, elem_count: anytype) @TypeOf(ecs_vec_grow(allocator, vec, ECS_SIZEOF(T), elem_count)) {
    _ = &allocator;
    _ = &vec;
    _ = &T;
    _ = &elem_count;
    return ecs_vec_grow(allocator, vec, ECS_SIZEOF(T), elem_count);
}
pub const ecs_vec_get_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1249:9
pub const ecs_vec_first_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1256:9
pub const ecs_vec_last_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1264:9
pub const FLECS_SPARSE_H = "";
pub const FLECS_SPARSE_PAGE_SIZE = @as(c_int, 1) << FLECS_SPARSE_PAGE_BITS;
pub inline fn FLECS_SPARSE_PAGE(index_1: anytype) i32 {
    _ = &index_1;
    return @import("std").zig.c_translation.cast(i32, @import("std").zig.c_translation.cast(u32, index_1) >> FLECS_SPARSE_PAGE_BITS);
}
pub inline fn FLECS_SPARSE_OFFSET(index_1: anytype) @TypeOf(@import("std").zig.c_translation.cast(i32, index_1) & (FLECS_SPARSE_PAGE_SIZE - @as(c_int, 1))) {
    _ = &index_1;
    return @import("std").zig.c_translation.cast(i32, index_1) & (FLECS_SPARSE_PAGE_SIZE - @as(c_int, 1));
}
pub inline fn flecs_sparse_init_t(result: anytype, allocator: anytype, page_allocator: anytype, T: anytype) @TypeOf(flecs_sparse_init(result, allocator, page_allocator, ECS_SIZEOF(T))) {
    _ = &result;
    _ = &allocator;
    _ = &page_allocator;
    _ = &T;
    return flecs_sparse_init(result, allocator, page_allocator, ECS_SIZEOF(T));
}
pub const flecs_sparse_add_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1335:9
pub inline fn flecs_sparse_remove_t(sparse: anytype, T: anytype, id: anytype) @TypeOf(flecs_sparse_remove(sparse, ECS_SIZEOF(T), id)) {
    _ = &sparse;
    _ = &T;
    _ = &id;
    return flecs_sparse_remove(sparse, ECS_SIZEOF(T), id);
}
pub const flecs_sparse_get_dense_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1379:9
pub const flecs_sparse_get_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1395:9
pub const flecs_sparse_try_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1405:9
pub const flecs_sparse_get_any_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1415:9
pub const flecs_sparse_ensure_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1425:9
pub const flecs_sparse_ensure_fast_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1435:9
pub inline fn ecs_sparse_init_t(sparse: anytype, T: anytype) @TypeOf(ecs_sparse_init(sparse, ECS_SIZEOF(T))) {
    _ = &sparse;
    _ = &T;
    return ecs_sparse_init(sparse, ECS_SIZEOF(T));
}
pub const ecs_sparse_add_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1460:9
pub const ecs_sparse_get_dense_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1477:9
pub const ecs_sparse_get_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1486:9
pub const FLECS_BLOCK_ALLOCATOR_H = "";
pub inline fn flecs_ballocator_init_t(ba: anytype, T: anytype) @TypeOf(flecs_ballocator_init(ba, ECS_SIZEOF(T))) {
    _ = &ba;
    _ = &T;
    return flecs_ballocator_init(ba, ECS_SIZEOF(T));
}
pub inline fn flecs_ballocator_init_n(ba: anytype, T: anytype, count: anytype) @TypeOf(flecs_ballocator_init(ba, ECS_SIZEOF(T) * count)) {
    _ = &ba;
    _ = &T;
    _ = &count;
    return flecs_ballocator_init(ba, ECS_SIZEOF(T) * count);
}
pub inline fn flecs_ballocator_new_t(T: anytype) @TypeOf(flecs_ballocator_new(ECS_SIZEOF(T))) {
    _ = &T;
    return flecs_ballocator_new(ECS_SIZEOF(T));
}
pub inline fn flecs_ballocator_new_n(T: anytype, count: anytype) @TypeOf(flecs_ballocator_new(ECS_SIZEOF(T) * count)) {
    _ = &T;
    _ = &count;
    return flecs_ballocator_new(ECS_SIZEOF(T) * count);
}
pub const FLECS_STACK_ALLOCATOR_H = "";
pub const ECS_STACK_PAGE_SIZE = @as(c_int, 4096);
pub inline fn flecs_stack_alloc_t(stack: anytype, T: anytype) @TypeOf(flecs_stack_alloc(stack, ECS_SIZEOF(T), ECS_ALIGNOF(T))) {
    _ = &stack;
    _ = &T;
    return flecs_stack_alloc(stack, ECS_SIZEOF(T), ECS_ALIGNOF(T));
}
pub inline fn flecs_stack_alloc_n(stack: anytype, T: anytype, count: anytype) @TypeOf(flecs_stack_alloc(stack, ECS_SIZEOF(T) * count, ECS_ALIGNOF(T))) {
    _ = &stack;
    _ = &T;
    _ = &count;
    return flecs_stack_alloc(stack, ECS_SIZEOF(T) * count, ECS_ALIGNOF(T));
}
pub inline fn flecs_stack_calloc_t(stack: anytype, T: anytype) @TypeOf(flecs_stack_calloc(stack, ECS_SIZEOF(T), ECS_ALIGNOF(T))) {
    _ = &stack;
    _ = &T;
    return flecs_stack_calloc(stack, ECS_SIZEOF(T), ECS_ALIGNOF(T));
}
pub inline fn flecs_stack_calloc_n(stack: anytype, T: anytype, count: anytype) @TypeOf(flecs_stack_calloc(stack, ECS_SIZEOF(T) * count, ECS_ALIGNOF(T))) {
    _ = &stack;
    _ = &T;
    _ = &count;
    return flecs_stack_calloc(stack, ECS_SIZEOF(T) * count, ECS_ALIGNOF(T));
}
pub inline fn flecs_stack_free_t(ptr: anytype, T: anytype) @TypeOf(flecs_stack_free(ptr, ECS_SIZEOF(T))) {
    _ = &ptr;
    _ = &T;
    return flecs_stack_free(ptr, ECS_SIZEOF(T));
}
pub inline fn flecs_stack_free_n(ptr: anytype, T: anytype, count: anytype) @TypeOf(flecs_stack_free(ptr, ECS_SIZEOF(T) * count)) {
    _ = &ptr;
    _ = &T;
    _ = &count;
    return flecs_stack_free(ptr, ECS_SIZEOF(T) * count);
}
pub const FLECS_MAP_H = "";
pub inline fn ecs_map_count(map: anytype) @TypeOf(if (map) map.*.count else @as(c_int, 0)) {
    _ = &map;
    return if (map) map.*.count else @as(c_int, 0);
}
pub inline fn ecs_map_is_init(map: anytype) @TypeOf(if (map) map.*.bucket_shift != @as(c_int, 0) else @"false") {
    _ = &map;
    return if (map) map.*.bucket_shift != @as(c_int, 0) else @"false";
}
pub const ecs_map_get_ref = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1850:9
pub const ecs_map_get_deref = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1851:9
pub const ecs_map_ensure_ref = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1852:9
pub inline fn ecs_map_insert_ptr(m: anytype, k: anytype, v: anytype) @TypeOf(ecs_map_insert(m, k, ECS_CAST(ecs_map_val_t, ECS_PTR_CAST(usize, v)))) {
    _ = &m;
    _ = &k;
    _ = &v;
    return ecs_map_insert(m, k, ECS_CAST(ecs_map_val_t, ECS_PTR_CAST(usize, v)));
}
pub const ecs_map_insert_alloc_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1855:9
pub const ecs_map_ensure_alloc_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1856:9
pub inline fn ecs_map_remove_ptr(m: anytype, k: anytype) @TypeOf(ECS_PTR_CAST(?*anyopaque, ECS_CAST(usize, ecs_map_remove(m, k)))) {
    _ = &m;
    _ = &k;
    return ECS_PTR_CAST(?*anyopaque, ECS_CAST(usize, ecs_map_remove(m, k)));
}
pub inline fn ecs_map_key(it: anytype) @TypeOf(it.*.res[@as(usize, @intCast(@as(c_int, 0)))]) {
    _ = &it;
    return it.*.res[@as(usize, @intCast(@as(c_int, 0)))];
}
pub inline fn ecs_map_value(it: anytype) @TypeOf(it.*.res[@as(usize, @intCast(@as(c_int, 1)))]) {
    _ = &it;
    return it.*.res[@as(usize, @intCast(@as(c_int, 1)))];
}
pub inline fn ecs_map_ptr(it: anytype) @TypeOf(ECS_PTR_CAST(?*anyopaque, ECS_CAST(usize, ecs_map_value(it)))) {
    _ = &it;
    return ECS_PTR_CAST(?*anyopaque, ECS_CAST(usize, ecs_map_value(it)));
}
pub const ecs_map_ref = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:1862:9
pub const FLECS_SWITCH_LIST_H = "";
pub const FLECS_ALLOCATOR_H = "";
pub inline fn flecs_allocator(obj: anytype) @TypeOf(&obj.*.allocators.dyn) {
    _ = &obj;
    return &obj.*.allocators.dyn;
}
pub inline fn flecs_alloc(a: anytype, size: anytype) @TypeOf(flecs_balloc(flecs_allocator_get(a, size))) {
    _ = &a;
    _ = &size;
    return flecs_balloc(flecs_allocator_get(a, size));
}
pub inline fn flecs_alloc_t(a: anytype, T: anytype) @TypeOf(flecs_alloc(a, ECS_SIZEOF(T))) {
    _ = &a;
    _ = &T;
    return flecs_alloc(a, ECS_SIZEOF(T));
}
pub inline fn flecs_alloc_n(a: anytype, T: anytype, count: anytype) @TypeOf(flecs_alloc(a, ECS_SIZEOF(T) * count)) {
    _ = &a;
    _ = &T;
    _ = &count;
    return flecs_alloc(a, ECS_SIZEOF(T) * count);
}
pub inline fn flecs_calloc(a: anytype, size: anytype) @TypeOf(flecs_bcalloc(flecs_allocator_get(a, size))) {
    _ = &a;
    _ = &size;
    return flecs_bcalloc(flecs_allocator_get(a, size));
}
pub inline fn flecs_calloc_t(a: anytype, T: anytype) @TypeOf(flecs_calloc(a, ECS_SIZEOF(T))) {
    _ = &a;
    _ = &T;
    return flecs_calloc(a, ECS_SIZEOF(T));
}
pub inline fn flecs_calloc_n(a: anytype, T: anytype, count: anytype) @TypeOf(flecs_calloc(a, ECS_SIZEOF(T) * count)) {
    _ = &a;
    _ = &T;
    _ = &count;
    return flecs_calloc(a, ECS_SIZEOF(T) * count);
}
pub inline fn flecs_free(a: anytype, size: anytype, ptr: anytype) @TypeOf(flecs_bfree(if (ptr) flecs_allocator_get(a, size) else NULL, ptr)) {
    _ = &a;
    _ = &size;
    _ = &ptr;
    return flecs_bfree(if (ptr) flecs_allocator_get(a, size) else NULL, ptr);
}
pub const flecs_free_t = @compileError("unable to translate C expr: unexpected token '#'");
// ../flecs/distr/flecs.h:2011:9
pub const flecs_free_n = @compileError("unable to translate C expr: unexpected token '#'");
// ../flecs/distr/flecs.h:2013:9
pub inline fn flecs_realloc(a: anytype, size_dst: anytype, size_src: anytype, ptr: anytype) @TypeOf(flecs_brealloc(flecs_allocator_get(a, size_dst), flecs_allocator_get(a, size_src), ptr)) {
    _ = &a;
    _ = &size_dst;
    _ = &size_src;
    _ = &ptr;
    return flecs_brealloc(flecs_allocator_get(a, size_dst), flecs_allocator_get(a, size_src), ptr);
}
pub inline fn flecs_realloc_n(a: anytype, T: anytype, count_dst: anytype, count_src: anytype, ptr: anytype) @TypeOf(flecs_realloc(a, ECS_SIZEOF(T) * count_dst, ECS_SIZEOF(T) * count_src, ptr)) {
    _ = &a;
    _ = &T;
    _ = &count_dst;
    _ = &count_src;
    _ = &ptr;
    return flecs_realloc(a, ECS_SIZEOF(T) * count_dst, ECS_SIZEOF(T) * count_src, ptr);
}
pub inline fn flecs_dup_n(a: anytype, T: anytype, count: anytype, ptr: anytype) @TypeOf(flecs_dup(a, ECS_SIZEOF(T) * count, ptr)) {
    _ = &a;
    _ = &T;
    _ = &count;
    _ = &ptr;
    return flecs_dup(a, ECS_SIZEOF(T) * count, ptr);
}
pub const FLECS_STRBUF_H_ = "";
pub const ECS_STRBUF_INIT = @import("std").mem.zeroInit(ecs_strbuf_t, .{@as(c_int, 0)});
pub const ECS_STRBUF_SMALL_STRING_SIZE = @as(c_int, 512);
pub const ECS_STRBUF_MAX_LIST_DEPTH = @as(c_int, 32);
pub inline fn ecs_strbuf_appendlit(buf: anytype, str: anytype) @TypeOf(ecs_strbuf_appendstrn(buf, str, @import("std").zig.c_translation.cast(i32, @import("std").zig.c_translation.sizeof(str) - @as(c_int, 1)))) {
    _ = &buf;
    _ = &str;
    return ecs_strbuf_appendstrn(buf, str, @import("std").zig.c_translation.cast(i32, @import("std").zig.c_translation.sizeof(str) - @as(c_int, 1)));
}
pub inline fn ecs_strbuf_list_appendlit(buf: anytype, str: anytype) @TypeOf(ecs_strbuf_list_appendstrn(buf, str, @import("std").zig.c_translation.cast(i32, @import("std").zig.c_translation.sizeof(str) - @as(c_int, 1)))) {
    _ = &buf;
    _ = &str;
    return ecs_strbuf_list_appendstrn(buf, str, @import("std").zig.c_translation.cast(i32, @import("std").zig.c_translation.sizeof(str) - @as(c_int, 1)));
}
pub const FLECS_OS_API_H = "";
pub const _SYS_ERRNO_H_ = "";
pub const errno = __error().*;
pub const EPERM = @as(c_int, 1);
pub const ENOENT = @as(c_int, 2);
pub const ESRCH = @as(c_int, 3);
pub const EINTR = @as(c_int, 4);
pub const EIO = @as(c_int, 5);
pub const ENXIO = @as(c_int, 6);
pub const E2BIG = @as(c_int, 7);
pub const ENOEXEC = @as(c_int, 8);
pub const EBADF = @as(c_int, 9);
pub const ECHILD = @as(c_int, 10);
pub const EDEADLK = @as(c_int, 11);
pub const ENOMEM = @as(c_int, 12);
pub const EACCES = @as(c_int, 13);
pub const EFAULT = @as(c_int, 14);
pub const ENOTBLK = @as(c_int, 15);
pub const EBUSY = @as(c_int, 16);
pub const EEXIST = @as(c_int, 17);
pub const EXDEV = @as(c_int, 18);
pub const ENODEV = @as(c_int, 19);
pub const ENOTDIR = @as(c_int, 20);
pub const EISDIR = @as(c_int, 21);
pub const EINVAL = @as(c_int, 22);
pub const ENFILE = @as(c_int, 23);
pub const EMFILE = @as(c_int, 24);
pub const ENOTTY = @as(c_int, 25);
pub const ETXTBSY = @as(c_int, 26);
pub const EFBIG = @as(c_int, 27);
pub const ENOSPC = @as(c_int, 28);
pub const ESPIPE = @as(c_int, 29);
pub const EROFS = @as(c_int, 30);
pub const EMLINK = @as(c_int, 31);
pub const EPIPE = @as(c_int, 32);
pub const EDOM = @as(c_int, 33);
pub const ERANGE = @as(c_int, 34);
pub const EAGAIN = @as(c_int, 35);
pub const EWOULDBLOCK = EAGAIN;
pub const EINPROGRESS = @as(c_int, 36);
pub const EALREADY = @as(c_int, 37);
pub const ENOTSOCK = @as(c_int, 38);
pub const EDESTADDRREQ = @as(c_int, 39);
pub const EMSGSIZE = @as(c_int, 40);
pub const EPROTOTYPE = @as(c_int, 41);
pub const ENOPROTOOPT = @as(c_int, 42);
pub const EPROTONOSUPPORT = @as(c_int, 43);
pub const ESOCKTNOSUPPORT = @as(c_int, 44);
pub const ENOTSUP = @as(c_int, 45);
pub const EPFNOSUPPORT = @as(c_int, 46);
pub const EAFNOSUPPORT = @as(c_int, 47);
pub const EADDRINUSE = @as(c_int, 48);
pub const EADDRNOTAVAIL = @as(c_int, 49);
pub const ENETDOWN = @as(c_int, 50);
pub const ENETUNREACH = @as(c_int, 51);
pub const ENETRESET = @as(c_int, 52);
pub const ECONNABORTED = @as(c_int, 53);
pub const ECONNRESET = @as(c_int, 54);
pub const ENOBUFS = @as(c_int, 55);
pub const EISCONN = @as(c_int, 56);
pub const ENOTCONN = @as(c_int, 57);
pub const ESHUTDOWN = @as(c_int, 58);
pub const ETOOMANYREFS = @as(c_int, 59);
pub const ETIMEDOUT = @as(c_int, 60);
pub const ECONNREFUSED = @as(c_int, 61);
pub const ELOOP = @as(c_int, 62);
pub const ENAMETOOLONG = @as(c_int, 63);
pub const EHOSTDOWN = @as(c_int, 64);
pub const EHOSTUNREACH = @as(c_int, 65);
pub const ENOTEMPTY = @as(c_int, 66);
pub const EPROCLIM = @as(c_int, 67);
pub const EUSERS = @as(c_int, 68);
pub const EDQUOT = @as(c_int, 69);
pub const ESTALE = @as(c_int, 70);
pub const EREMOTE = @as(c_int, 71);
pub const EBADRPC = @as(c_int, 72);
pub const ERPCMISMATCH = @as(c_int, 73);
pub const EPROGUNAVAIL = @as(c_int, 74);
pub const EPROGMISMATCH = @as(c_int, 75);
pub const EPROCUNAVAIL = @as(c_int, 76);
pub const ENOLCK = @as(c_int, 77);
pub const ENOSYS = @as(c_int, 78);
pub const EFTYPE = @as(c_int, 79);
pub const EAUTH = @as(c_int, 80);
pub const ENEEDAUTH = @as(c_int, 81);
pub const EPWROFF = @as(c_int, 82);
pub const EDEVERR = @as(c_int, 83);
pub const EOVERFLOW = @as(c_int, 84);
pub const EBADEXEC = @as(c_int, 85);
pub const EBADARCH = @as(c_int, 86);
pub const ESHLIBVERS = @as(c_int, 87);
pub const EBADMACHO = @as(c_int, 88);
pub const ECANCELED = @as(c_int, 89);
pub const EIDRM = @as(c_int, 90);
pub const ENOMSG = @as(c_int, 91);
pub const EILSEQ = @as(c_int, 92);
pub const ENOATTR = @as(c_int, 93);
pub const EBADMSG = @as(c_int, 94);
pub const EMULTIHOP = @as(c_int, 95);
pub const ENODATA = @as(c_int, 96);
pub const ENOLINK = @as(c_int, 97);
pub const ENOSR = @as(c_int, 98);
pub const ENOSTR = @as(c_int, 99);
pub const EPROTO = @as(c_int, 100);
pub const ETIME = @as(c_int, 101);
pub const EOPNOTSUPP = @as(c_int, 102);
pub const ENOPOLICY = @as(c_int, 103);
pub const ENOTRECOVERABLE = @as(c_int, 104);
pub const EOWNERDEAD = @as(c_int, 105);
pub const EQFULL = @as(c_int, 106);
pub const ELAST = @as(c_int, 106);
pub const _STDIO_H_ = "";
pub const __STDIO_H_ = "";
pub const _VA_LIST_T = "";
pub const _SYS_STDIO_H_ = "";
pub const RENAME_SECLUDE = @as(c_int, 0x00000001);
pub const RENAME_SWAP = @as(c_int, 0x00000002);
pub const RENAME_EXCL = @as(c_int, 0x00000004);
pub const RENAME_RESERVED1 = @as(c_int, 0x00000008);
pub const RENAME_NOFOLLOW_ANY = @as(c_int, 0x00000010);
pub const _FSTDIO = "";
pub const _SEEK_SET_H_ = "";
pub const SEEK_SET = @as(c_int, 0);
pub const SEEK_CUR = @as(c_int, 1);
pub const SEEK_END = @as(c_int, 2);
pub const SEEK_HOLE = @as(c_int, 3);
pub const SEEK_DATA = @as(c_int, 4);
pub const __SLBF = @as(c_int, 0x0001);
pub const __SNBF = @as(c_int, 0x0002);
pub const __SRD = @as(c_int, 0x0004);
pub const __SWR = @as(c_int, 0x0008);
pub const __SRW = @as(c_int, 0x0010);
pub const __SEOF = @as(c_int, 0x0020);
pub const __SERR = @as(c_int, 0x0040);
pub const __SMBF = @as(c_int, 0x0080);
pub const __SAPP = @as(c_int, 0x0100);
pub const __SSTR = @as(c_int, 0x0200);
pub const __SOPT = @as(c_int, 0x0400);
pub const __SNPT = @as(c_int, 0x0800);
pub const __SOFF = @as(c_int, 0x1000);
pub const __SMOD = @as(c_int, 0x2000);
pub const __SALC = @as(c_int, 0x4000);
pub const __SIGN = @import("std").zig.c_translation.promoteIntLiteral(c_int, 0x8000, .hex);
pub const _IOFBF = @as(c_int, 0);
pub const _IOLBF = @as(c_int, 1);
pub const _IONBF = @as(c_int, 2);
pub const BUFSIZ = @as(c_int, 1024);
pub const EOF = -@as(c_int, 1);
pub const FOPEN_MAX = @as(c_int, 20);
pub const FILENAME_MAX = @as(c_int, 1024);
pub const P_tmpdir = "/var/tmp/";
pub const L_tmpnam = @as(c_int, 1024);
pub const TMP_MAX = @import("std").zig.c_translation.promoteIntLiteral(c_int, 308915776, .decimal);
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/stdio.h:119:9: warning: macro 'stdin' contains a runtime value, translated to function
pub inline fn stdin() @TypeOf(__stdinp) {
    return __stdinp;
}
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/stdio.h:120:9: warning: macro 'stdout' contains a runtime value, translated to function
pub inline fn stdout() @TypeOf(__stdoutp) {
    return __stdoutp;
}
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/stdio.h:121:9: warning: macro 'stderr' contains a runtime value, translated to function
pub inline fn stderr() @TypeOf(__stderrp) {
    return __stderrp;
}
pub const L_ctermid = @as(c_int, 1024);
pub const _CTERMID_H_ = "";
pub const __sgetc = @compileError("TODO unary inc/dec expr");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/stdio.h:261:9
pub inline fn __sfeof(p: anytype) @TypeOf((p.*._flags & __SEOF) != @as(c_int, 0)) {
    _ = &p;
    return (p.*._flags & __SEOF) != @as(c_int, 0);
}
pub inline fn __sferror(p: anytype) @TypeOf((p.*._flags & __SERR) != @as(c_int, 0)) {
    _ = &p;
    return (p.*._flags & __SERR) != @as(c_int, 0);
}
pub const __sclearerr = @compileError("unable to translate C expr: expected ')' instead got '&='");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/stdio.h:285:9
pub inline fn __sfileno(p: anytype) @TypeOf(p.*._file) {
    _ = &p;
    return p.*._file;
}
pub const _OFF_T = "";
pub inline fn fropen(cookie: anytype, @"fn": anytype) @TypeOf(funopen(cookie, @"fn", @as(c_int, 0), @as(c_int, 0), @as(c_int, 0))) {
    _ = &cookie;
    _ = &@"fn";
    return funopen(cookie, @"fn", @as(c_int, 0), @as(c_int, 0), @as(c_int, 0));
}
pub inline fn fwopen(cookie: anytype, @"fn": anytype) @TypeOf(funopen(cookie, @as(c_int, 0), @"fn", @as(c_int, 0), @as(c_int, 0))) {
    _ = &cookie;
    _ = &@"fn";
    return funopen(cookie, @as(c_int, 0), @"fn", @as(c_int, 0), @as(c_int, 0));
}
pub inline fn feof_unlocked(p: anytype) @TypeOf(__sfeof(p)) {
    _ = &p;
    return __sfeof(p);
}
pub inline fn ferror_unlocked(p: anytype) @TypeOf(__sferror(p)) {
    _ = &p;
    return __sferror(p);
}
pub inline fn clearerr_unlocked(p: anytype) @TypeOf(__sclearerr(p)) {
    _ = &p;
    return __sclearerr(p);
}
pub inline fn fileno_unlocked(p: anytype) @TypeOf(__sfileno(p)) {
    _ = &p;
    return __sfileno(p);
}
pub const _SECURE__STDIO_H_ = "";
pub const _ALLOCA_H_ = "";
pub const __alloca = @compileError("unable to translate macro: undefined identifier `__builtin_alloca`");
// /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.2.sdk/usr/include/alloca.h:40:9
pub inline fn ecs_os_malloc(size: anytype) @TypeOf(ecs_os_api.malloc_(size)) {
    _ = &size;
    return ecs_os_api.malloc_(size);
}
pub inline fn ecs_os_free(ptr: anytype) @TypeOf(ecs_os_api.free_(ptr)) {
    _ = &ptr;
    return ecs_os_api.free_(ptr);
}
pub inline fn ecs_os_realloc(ptr: anytype, size: anytype) @TypeOf(ecs_os_api.realloc_(ptr, size)) {
    _ = &ptr;
    _ = &size;
    return ecs_os_api.realloc_(ptr, size);
}
pub inline fn ecs_os_calloc(size: anytype) @TypeOf(ecs_os_api.calloc_(size)) {
    _ = &size;
    return ecs_os_api.calloc_(size);
}
pub inline fn ecs_os_alloca(size: anytype) @TypeOf(alloca(@import("std").zig.c_translation.cast(usize, size))) {
    _ = &size;
    return alloca(@import("std").zig.c_translation.cast(usize, size));
}
pub const ecs_os_malloc_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2624:9
pub const ecs_os_malloc_n = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2625:9
pub const ecs_os_calloc_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2626:9
pub const ecs_os_calloc_n = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2627:9
pub const ecs_os_realloc_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2629:9
pub const ecs_os_realloc_n = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2630:9
pub const ecs_os_alloca_t = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2631:9
pub const ecs_os_alloca_n = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2632:9
pub inline fn ecs_os_strdup(str: anytype) @TypeOf(ecs_os_api.strdup_(str)) {
    _ = &str;
    return ecs_os_api.strdup_(str);
}
pub inline fn ecs_os_strlen(str: anytype) ecs_size_t {
    _ = &str;
    return @import("std").zig.c_translation.cast(ecs_size_t, strlen(str));
}
pub inline fn ecs_os_strncmp(str1: anytype, str2: anytype, num: anytype) @TypeOf(strncmp(str1, str2, @import("std").zig.c_translation.cast(usize, num))) {
    _ = &str1;
    _ = &str2;
    _ = &num;
    return strncmp(str1, str2, @import("std").zig.c_translation.cast(usize, num));
}
pub inline fn ecs_os_memcmp(ptr1: anytype, ptr2: anytype, num: anytype) @TypeOf(memcmp(ptr1, ptr2, @import("std").zig.c_translation.cast(usize, num))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &num;
    return memcmp(ptr1, ptr2, @import("std").zig.c_translation.cast(usize, num));
}
pub inline fn ecs_os_memcpy(ptr1: anytype, ptr2: anytype, num: anytype) @TypeOf(memcpy(ptr1, ptr2, @import("std").zig.c_translation.cast(usize, num))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &num;
    return memcpy(ptr1, ptr2, @import("std").zig.c_translation.cast(usize, num));
}
pub inline fn ecs_os_memset(ptr: anytype, value: anytype, num: anytype) @TypeOf(memset(ptr, value, @import("std").zig.c_translation.cast(usize, num))) {
    _ = &ptr;
    _ = &value;
    _ = &num;
    return memset(ptr, value, @import("std").zig.c_translation.cast(usize, num));
}
pub inline fn ecs_os_memmove(dst: anytype, src: anytype, size: anytype) @TypeOf(memmove(dst, src, @import("std").zig.c_translation.cast(usize, size))) {
    _ = &dst;
    _ = &src;
    _ = &size;
    return memmove(dst, src, @import("std").zig.c_translation.cast(usize, size));
}
pub inline fn ecs_os_memcpy_t(ptr1: anytype, ptr2: anytype, T: anytype) @TypeOf(ecs_os_memcpy(ptr1, ptr2, ECS_SIZEOF(T))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &T;
    return ecs_os_memcpy(ptr1, ptr2, ECS_SIZEOF(T));
}
pub inline fn ecs_os_memcpy_n(ptr1: anytype, ptr2: anytype, T: anytype, count: anytype) @TypeOf(ecs_os_memcpy(ptr1, ptr2, ECS_SIZEOF(T) * @import("std").zig.c_translation.cast(usize, count))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &T;
    _ = &count;
    return ecs_os_memcpy(ptr1, ptr2, ECS_SIZEOF(T) * @import("std").zig.c_translation.cast(usize, count));
}
pub inline fn ecs_os_memcmp_t(ptr1: anytype, ptr2: anytype, T: anytype) @TypeOf(ecs_os_memcmp(ptr1, ptr2, ECS_SIZEOF(T))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &T;
    return ecs_os_memcmp(ptr1, ptr2, ECS_SIZEOF(T));
}
pub inline fn ecs_os_memmove_t(ptr1: anytype, ptr2: anytype, T: anytype) @TypeOf(ecs_os_memmove(ptr1, ptr2, ECS_SIZEOF(T))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &T;
    return ecs_os_memmove(ptr1, ptr2, ECS_SIZEOF(T));
}
pub inline fn ecs_os_memmove_n(ptr1: anytype, ptr2: anytype, T: anytype, count: anytype) @TypeOf(ecs_os_memmove(ptr1, ptr2, ECS_SIZEOF(T) * @import("std").zig.c_translation.cast(usize, count))) {
    _ = &ptr1;
    _ = &ptr2;
    _ = &T;
    _ = &count;
    return ecs_os_memmove(ptr1, ptr2, ECS_SIZEOF(T) * @import("std").zig.c_translation.cast(usize, count));
}
pub inline fn ecs_os_strcmp(str1: anytype, str2: anytype) @TypeOf(strcmp(str1, str2)) {
    _ = &str1;
    _ = &str2;
    return strcmp(str1, str2);
}
pub inline fn ecs_os_memset_t(ptr: anytype, value: anytype, T: anytype) @TypeOf(ecs_os_memset(ptr, value, ECS_SIZEOF(T))) {
    _ = &ptr;
    _ = &value;
    _ = &T;
    return ecs_os_memset(ptr, value, ECS_SIZEOF(T));
}
pub inline fn ecs_os_memset_n(ptr: anytype, value: anytype, T: anytype, count: anytype) @TypeOf(ecs_os_memset(ptr, value, ECS_SIZEOF(T) * @import("std").zig.c_translation.cast(usize, count))) {
    _ = &ptr;
    _ = &value;
    _ = &T;
    _ = &count;
    return ecs_os_memset(ptr, value, ECS_SIZEOF(T) * @import("std").zig.c_translation.cast(usize, count));
}
pub inline fn ecs_os_zeromem(ptr: anytype) @TypeOf(ecs_os_memset(ptr, @as(c_int, 0), ECS_SIZEOF(ptr.*))) {
    _ = &ptr;
    return ecs_os_memset(ptr, @as(c_int, 0), ECS_SIZEOF(ptr.*));
}
pub inline fn ecs_os_memdup_t(ptr: anytype, T: anytype) @TypeOf(ecs_os_memdup(ptr, ECS_SIZEOF(T))) {
    _ = &ptr;
    _ = &T;
    return ecs_os_memdup(ptr, ECS_SIZEOF(T));
}
pub inline fn ecs_os_memdup_n(ptr: anytype, T: anytype, count: anytype) @TypeOf(ecs_os_memdup(ptr, ECS_SIZEOF(T) * count)) {
    _ = &ptr;
    _ = &T;
    _ = &count;
    return ecs_os_memdup(ptr, ECS_SIZEOF(T) * count);
}
pub const ecs_offset = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:2671:9
pub inline fn ecs_os_strcat(str1: anytype, str2: anytype) @TypeOf(strcat(str1, str2)) {
    _ = &str1;
    _ = &str2;
    return strcat(str1, str2);
}
pub const ecs_os_snprintf = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:2682:9
pub inline fn ecs_os_vsnprintf(ptr: anytype, len: anytype, fmt: anytype, args: anytype) @TypeOf(vsnprintf(ptr, ECS_CAST(usize, len), fmt, args)) {
    _ = &ptr;
    _ = &len;
    _ = &fmt;
    _ = &args;
    return vsnprintf(ptr, ECS_CAST(usize, len), fmt, args);
}
pub inline fn ecs_os_strcpy(str1: anytype, str2: anytype) @TypeOf(strcpy(str1, str2)) {
    _ = &str1;
    _ = &str2;
    return strcpy(str1, str2);
}
pub inline fn ecs_os_strncpy(str1: anytype, str2: anytype, len: anytype) @TypeOf(strncpy(str1, str2, ECS_CAST(usize, len))) {
    _ = &str1;
    _ = &str2;
    _ = &len;
    return strncpy(str1, str2, ECS_CAST(usize, len));
}
pub const ecs_os_fopen = @compileError("unable to translate C expr: unexpected token '='");
// ../flecs/distr/flecs.h:2692:9
pub inline fn ecs_os_thread_new(callback: anytype, param: anytype) @TypeOf(ecs_os_api.thread_new_(callback, param)) {
    _ = &callback;
    _ = &param;
    return ecs_os_api.thread_new_(callback, param);
}
pub inline fn ecs_os_thread_join(thread: anytype) @TypeOf(ecs_os_api.thread_join_(thread)) {
    _ = &thread;
    return ecs_os_api.thread_join_(thread);
}
pub inline fn ecs_os_thread_self() @TypeOf(ecs_os_api.thread_self_()) {
    return ecs_os_api.thread_self_();
}
pub inline fn ecs_os_task_new(callback: anytype, param: anytype) @TypeOf(ecs_os_api.task_new_(callback, param)) {
    _ = &callback;
    _ = &param;
    return ecs_os_api.task_new_(callback, param);
}
pub inline fn ecs_os_task_join(thread: anytype) @TypeOf(ecs_os_api.task_join_(thread)) {
    _ = &thread;
    return ecs_os_api.task_join_(thread);
}
pub inline fn ecs_os_ainc(value: anytype) @TypeOf(ecs_os_api.ainc_(value)) {
    _ = &value;
    return ecs_os_api.ainc_(value);
}
pub inline fn ecs_os_adec(value: anytype) @TypeOf(ecs_os_api.adec_(value)) {
    _ = &value;
    return ecs_os_api.adec_(value);
}
pub inline fn ecs_os_lainc(value: anytype) @TypeOf(ecs_os_api.lainc_(value)) {
    _ = &value;
    return ecs_os_api.lainc_(value);
}
pub inline fn ecs_os_ladec(value: anytype) @TypeOf(ecs_os_api.ladec_(value)) {
    _ = &value;
    return ecs_os_api.ladec_(value);
}
pub inline fn ecs_os_mutex_new() @TypeOf(ecs_os_api.mutex_new_()) {
    return ecs_os_api.mutex_new_();
}
pub inline fn ecs_os_mutex_free(mutex: anytype) @TypeOf(ecs_os_api.mutex_free_(mutex)) {
    _ = &mutex;
    return ecs_os_api.mutex_free_(mutex);
}
pub inline fn ecs_os_mutex_lock(mutex: anytype) @TypeOf(ecs_os_api.mutex_lock_(mutex)) {
    _ = &mutex;
    return ecs_os_api.mutex_lock_(mutex);
}
pub inline fn ecs_os_mutex_unlock(mutex: anytype) @TypeOf(ecs_os_api.mutex_unlock_(mutex)) {
    _ = &mutex;
    return ecs_os_api.mutex_unlock_(mutex);
}
pub inline fn ecs_os_cond_new() @TypeOf(ecs_os_api.cond_new_()) {
    return ecs_os_api.cond_new_();
}
pub inline fn ecs_os_cond_free(cond: anytype) @TypeOf(ecs_os_api.cond_free_(cond)) {
    _ = &cond;
    return ecs_os_api.cond_free_(cond);
}
pub inline fn ecs_os_cond_signal(cond: anytype) @TypeOf(ecs_os_api.cond_signal_(cond)) {
    _ = &cond;
    return ecs_os_api.cond_signal_(cond);
}
pub inline fn ecs_os_cond_broadcast(cond: anytype) @TypeOf(ecs_os_api.cond_broadcast_(cond)) {
    _ = &cond;
    return ecs_os_api.cond_broadcast_(cond);
}
pub inline fn ecs_os_cond_wait(cond: anytype, mutex: anytype) @TypeOf(ecs_os_api.cond_wait_(cond, mutex)) {
    _ = &cond;
    _ = &mutex;
    return ecs_os_api.cond_wait_(cond, mutex);
}
pub inline fn ecs_os_sleep(sec: anytype, nanosec: anytype) @TypeOf(ecs_os_api.sleep_(sec, nanosec)) {
    _ = &sec;
    _ = &nanosec;
    return ecs_os_api.sleep_(sec, nanosec);
}
pub inline fn ecs_os_now() @TypeOf(ecs_os_api.now_()) {
    return ecs_os_api.now_();
}
pub inline fn ecs_os_get_time(time_out: anytype) @TypeOf(ecs_os_api.get_time_(time_out)) {
    _ = &time_out;
    return ecs_os_api.get_time_(time_out);
}
pub const ecs_os_inc = @compileError("TODO unary inc/dec expr");
// ../flecs/distr/flecs.h:2735:9
pub const ecs_os_linc = @compileError("TODO unary inc/dec expr");
// ../flecs/distr/flecs.h:2736:9
pub const ecs_os_dec = @compileError("TODO unary inc/dec expr");
// ../flecs/distr/flecs.h:2737:9
pub const ecs_os_ldec = @compileError("TODO unary inc/dec expr");
// ../flecs/distr/flecs.h:2738:9
pub const ecs_os_isnan = @compileError("unable to translate macro: undefined identifier `isnan`");
// ../flecs/distr/flecs.h:2754:9
pub const ecs_os_isinf = @compileError("unable to translate macro: undefined identifier `isinf`");
// ../flecs/distr/flecs.h:2755:9
pub inline fn ecs_os_abort() @TypeOf(ecs_os_api.abort_()) {
    return ecs_os_api.abort_();
}
pub inline fn ecs_os_dlopen(libname: anytype) @TypeOf(ecs_os_api.dlopen_(libname)) {
    _ = &libname;
    return ecs_os_api.dlopen_(libname);
}
pub inline fn ecs_os_dlproc(lib: anytype, procname: anytype) @TypeOf(ecs_os_api.dlproc_(lib, procname)) {
    _ = &lib;
    _ = &procname;
    return ecs_os_api.dlproc_(lib, procname);
}
pub inline fn ecs_os_dlclose(lib: anytype) @TypeOf(ecs_os_api.dlclose_(lib)) {
    _ = &lib;
    return ecs_os_api.dlclose_(lib);
}
pub inline fn ecs_os_module_to_dl(lib: anytype) @TypeOf(ecs_os_api.module_to_dl_(lib)) {
    _ = &lib;
    return ecs_os_api.module_to_dl_(lib);
}
pub inline fn ecs_os_module_to_etc(lib: anytype) @TypeOf(ecs_os_api.module_to_etc_(lib)) {
    _ = &lib;
    return ecs_os_api.module_to_etc_(lib);
}
pub const ecs_os_perf_trace_push = @compileError("unable to translate C expr: unexpected token ''");
// ../flecs/distr/flecs.h:2862:9
pub const ecs_os_perf_trace_pop = @compileError("unable to translate C expr: unexpected token ''");
// ../flecs/distr/flecs.h:2863:9
pub const EcsSelf = @as(c_ulonglong, 1) << @as(c_int, 63);
pub const EcsUp = @as(c_ulonglong, 1) << @as(c_int, 62);
pub const EcsTrav = @as(c_ulonglong, 1) << @as(c_int, 61);
pub const EcsCascade = @as(c_ulonglong, 1) << @as(c_int, 60);
pub const EcsDesc = @as(c_ulonglong, 1) << @as(c_int, 59);
pub const EcsIsVariable = @as(c_ulonglong, 1) << @as(c_int, 58);
pub const EcsIsEntity = @as(c_ulonglong, 1) << @as(c_int, 57);
pub const EcsIsName = @as(c_ulonglong, 1) << @as(c_int, 56);
pub const EcsTraverseFlags = (((EcsSelf | EcsUp) | EcsTrav) | EcsCascade) | EcsDesc;
pub const EcsTermRefFlags = ((EcsTraverseFlags | EcsIsVariable) | EcsIsEntity) | EcsIsName;
pub const FLECS_API_TYPES_H = "";
pub const flecs_iter_cache_ids = @as(c_uint, 1) << @as(c_uint, 0);
pub const flecs_iter_cache_trs = @as(c_uint, 1) << @as(c_uint, 1);
pub const flecs_iter_cache_sources = @as(c_uint, 1) << @as(c_uint, 2);
pub const flecs_iter_cache_ptrs = @as(c_uint, 1) << @as(c_uint, 3);
pub const flecs_iter_cache_variables = @as(c_uint, 1) << @as(c_uint, 4);
pub const flecs_iter_cache_all = @as(c_int, 255);
pub const FLECS_API_SUPPORT_H = "";
pub const ECS_MAX_COMPONENT_ID = ~@import("std").zig.c_translation.cast(u32, ECS_ID_FLAGS_MASK >> @as(c_int, 32));
pub const ECS_MAX_RECURSION = @as(c_int, 512);
pub const ECS_MAX_TOKEN_SIZE = @as(c_int, 256);
pub const flecs_poly_claim = @compileError("unable to translate macro: undefined identifier `reinterpret_cast`");
// ../flecs/distr/flecs.h:3934:9
pub const flecs_poly_release = @compileError("unable to translate macro: undefined identifier `reinterpret_cast`");
// ../flecs/distr/flecs.h:3936:9
pub inline fn ECS_OFFSET(o: anytype, offset: anytype) ?*anyopaque {
    _ = &o;
    _ = &offset;
    return @import("std").zig.c_translation.cast(?*anyopaque, @import("std").zig.c_translation.cast(usize, o) + @import("std").zig.c_translation.cast(usize, offset));
}
pub inline fn ECS_OFFSET_T(o: anytype, T: anytype) @TypeOf(ECS_OFFSET(o, ECS_SIZEOF(T))) {
    _ = &o;
    _ = &T;
    return ECS_OFFSET(o, ECS_SIZEOF(T));
}
pub inline fn ECS_ELEM(ptr: anytype, size: anytype, index_1: anytype) @TypeOf(ECS_OFFSET(ptr, size * index_1)) {
    _ = &ptr;
    _ = &size;
    _ = &index_1;
    return ECS_OFFSET(ptr, size * index_1);
}
pub inline fn ECS_ELEM_T(o: anytype, T: anytype, index_1: anytype) @TypeOf(ECS_ELEM(o, ECS_SIZEOF(T), index_1)) {
    _ = &o;
    _ = &T;
    _ = &index_1;
    return ECS_ELEM(o, ECS_SIZEOF(T), index_1);
}
pub const ECS_BIT_SET = @compileError("unable to translate C expr: unexpected token '|='");
// ../flecs/distr/flecs.h:3952:9
pub const ECS_BIT_CLEAR = @compileError("unable to translate C expr: unexpected token '&='");
// ../flecs/distr/flecs.h:3953:9
pub inline fn ECS_BIT_COND(flags: anytype, bit: anytype, cond: anytype) @TypeOf(if (cond) ECS_BIT_SET(flags, bit) else ECS_BIT_CLEAR(flags, bit)) {
    _ = &flags;
    _ = &bit;
    _ = &cond;
    return if (cond) ECS_BIT_SET(flags, bit) else ECS_BIT_CLEAR(flags, bit);
}
pub const ECS_BIT_CLEAR16 = @compileError("unable to translate C expr: unexpected token '&='");
// ../flecs/distr/flecs.h:3958:9
pub inline fn ECS_BIT_COND16(flags: anytype, bit: anytype, cond: anytype) @TypeOf(if (cond) ECS_BIT_SET(flags, bit) else ECS_BIT_CLEAR16(flags, bit)) {
    _ = &flags;
    _ = &bit;
    _ = &cond;
    return if (cond) ECS_BIT_SET(flags, bit) else ECS_BIT_CLEAR16(flags, bit);
}
pub inline fn ECS_BIT_IS_SET(flags: anytype, bit: anytype) @TypeOf(flags & bit) {
    _ = &flags;
    _ = &bit;
    return flags & bit;
}
pub inline fn ECS_BIT_SETN(flags: anytype, n: anytype) @TypeOf(ECS_BIT_SET(flags, @as(c_ulonglong, 1) << n)) {
    _ = &flags;
    _ = &n;
    return ECS_BIT_SET(flags, @as(c_ulonglong, 1) << n);
}
pub inline fn ECS_BIT_CLEARN(flags: anytype, n: anytype) @TypeOf(ECS_BIT_CLEAR(flags, @as(c_ulonglong, 1) << n)) {
    _ = &flags;
    _ = &n;
    return ECS_BIT_CLEAR(flags, @as(c_ulonglong, 1) << n);
}
pub inline fn ECS_BIT_CONDN(flags: anytype, n: anytype, cond: anytype) @TypeOf(ECS_BIT_COND(flags, @as(c_ulonglong, 1) << n, cond)) {
    _ = &flags;
    _ = &n;
    _ = &cond;
    return ECS_BIT_COND(flags, @as(c_ulonglong, 1) << n, cond);
}
pub const FLECS_HASHMAP_H = "";
pub inline fn flecs_hashmap_init(hm: anytype, K: anytype, V: anytype, hash: anytype, compare: anytype, allocator: anytype) @TypeOf(flecs_hashmap_init_(hm, ECS_SIZEOF(K), ECS_SIZEOF(V), hash, compare, allocator)) {
    _ = &hm;
    _ = &K;
    _ = &V;
    _ = &hash;
    _ = &compare;
    _ = &allocator;
    return flecs_hashmap_init_(hm, ECS_SIZEOF(K), ECS_SIZEOF(V), hash, compare, allocator);
}
pub const flecs_hashmap_get = @compileError("unable to translate C expr: unexpected token ')'");
// ../flecs/distr/flecs.h:4038:9
pub inline fn flecs_hashmap_ensure(map: anytype, key: anytype, V: anytype) @TypeOf(flecs_hashmap_ensure_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(V))) {
    _ = &map;
    _ = &key;
    _ = &V;
    return flecs_hashmap_ensure_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(V));
}
pub inline fn flecs_hashmap_set(map: anytype, key: anytype, value: anytype) @TypeOf(flecs_hashmap_set_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(value.*), value)) {
    _ = &map;
    _ = &key;
    _ = &value;
    return flecs_hashmap_set_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(value.*), value);
}
pub inline fn flecs_hashmap_remove(map: anytype, key: anytype, V: anytype) @TypeOf(flecs_hashmap_remove_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(V))) {
    _ = &map;
    _ = &key;
    _ = &V;
    return flecs_hashmap_remove_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(V));
}
pub inline fn flecs_hashmap_remove_w_hash(map: anytype, key: anytype, V: anytype, hash: anytype) @TypeOf(flecs_hashmap_remove_w_hash_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(V), hash)) {
    _ = &map;
    _ = &key;
    _ = &V;
    _ = &hash;
    return flecs_hashmap_remove_w_hash_(map, ECS_SIZEOF(key.*), key, ECS_SIZEOF(V), hash);
}
pub const flecs_hashmap_next = @compileError("unable to translate C expr: unexpected token ')'");
// ../flecs/distr/flecs.h:4111:9
pub const flecs_hashmap_next_w_key = @compileError("unable to translate C expr: unexpected token ')'");
// ../flecs/distr/flecs.h:4114:9
pub const EcsQueryMatchPrefab = @as(c_uint, 1) << @as(c_uint, 1);
pub const EcsQueryMatchDisabled = @as(c_uint, 1) << @as(c_uint, 2);
pub const EcsQueryMatchEmptyTables = @as(c_uint, 1) << @as(c_uint, 3);
pub const EcsQueryAllowUnresolvedByName = @as(c_uint, 1) << @as(c_uint, 6);
pub const EcsQueryTableOnly = @as(c_uint, 1) << @as(c_uint, 7);
pub const EcsSingleton = EcsVariable;
pub const EcsLastInternalComponentId = ecs_id(EcsPoly);
pub const EcsFirstUserComponentId = @as(c_int, 8);
pub const EcsFirstUserEntityId = FLECS_HI_COMPONENT_ID + @as(c_int, 128);
pub const flecs_poly_is = @compileError("unable to translate macro: undefined identifier `_magic`");
// ../flecs/distr/flecs.h:5832:9
pub const FLECS_C_ = "";
pub inline fn ECS_DECLARE(id: anytype) @TypeOf(ecs_id(id)) {
    _ = &id;
    return blk: {
        _ = ecs_entity_t ++ id;
        break :blk ecs_id(id);
    };
}
pub const ECS_ENTITY_DECLARE = ECS_DECLARE;
pub const ECS_ENTITY_DEFINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9436:9
pub const ECS_ENTITY = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9457:9
pub const ECS_TAG_DECLARE = ECS_DECLARE;
pub inline fn ECS_TAG_DEFINE(world: anytype, id: anytype) @TypeOf(ECS_ENTITY_DEFINE(world, id, @as(c_int, 0))) {
    _ = &world;
    _ = &id;
    return ECS_ENTITY_DEFINE(world, id, @as(c_int, 0));
}
pub inline fn ECS_TAG(world: anytype, id: anytype) @TypeOf(ECS_ENTITY(world, id, @as(c_int, 0))) {
    _ = &world;
    _ = &id;
    return ECS_ENTITY(world, id, @as(c_int, 0));
}
pub const ECS_PREFAB_DECLARE = ECS_DECLARE;
pub const ECS_PREFAB_DEFINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9496:9
pub const ECS_PREFAB = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9506:9
pub inline fn ECS_COMPONENT_DECLARE(id: anytype) @TypeOf(ecs_entity_t ++ ecs_id(id)) {
    _ = &id;
    return ecs_entity_t ++ ecs_id(id);
}
pub const ECS_COMPONENT_DEFINE = @compileError("unable to translate macro: undefined identifier `desc`");
// ../flecs/distr/flecs.h:9519:9
pub const ECS_COMPONENT = @compileError("unable to translate C expr: unexpected token '='");
// ../flecs/distr/flecs.h:9542:9
pub inline fn ECS_OBSERVER_DECLARE(id: anytype) @TypeOf(ecs_entity_t ++ ecs_id(id)) {
    _ = &id;
    return ecs_entity_t ++ ecs_id(id);
}
pub const ECS_OBSERVER_DEFINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9558:9
pub const ECS_OBSERVER = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9580:9
pub inline fn ECS_QUERY_DECLARE(name: anytype) @TypeOf(ecs_query_t * name) {
    _ = &name;
    return ecs_query_t * name;
}
pub const ECS_QUERY_DEFINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9598:9
pub const ECS_QUERY = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9617:9
pub const ecs_entity = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9632:9
pub const ecs_component = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9646:9
pub const ecs_component_t = @compileError("unable to translate C expr: unexpected token '{'");
// ../flecs/distr/flecs.h:9657:9
pub const ecs_query = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9675:9
pub const ecs_observer = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9690:9
pub inline fn ecs_new_w(world: anytype, T: anytype) @TypeOf(ecs_new_w_id(world, ecs_id(T))) {
    _ = &world;
    _ = &T;
    return ecs_new_w_id(world, ecs_id(T));
}
pub inline fn ecs_new_w_pair(world: anytype, first: anytype, second: anytype) @TypeOf(ecs_new_w_id(world, ecs_pair(first, second))) {
    _ = &world;
    _ = &first;
    _ = &second;
    return ecs_new_w_id(world, ecs_pair(first, second));
}
pub inline fn ecs_bulk_new(world: anytype, component: anytype, count: anytype) @TypeOf(ecs_bulk_new_w_id(world, ecs_id(component), count)) {
    _ = &world;
    _ = &component;
    _ = &count;
    return ecs_bulk_new_w_id(world, ecs_id(component), count);
}
pub inline fn ecs_add(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_add_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_add_id(world, entity, ecs_id(T));
}
pub inline fn ecs_add_pair(world: anytype, subject: anytype, first: anytype, second: anytype) @TypeOf(ecs_add_id(world, subject, ecs_pair(first, second))) {
    _ = &world;
    _ = &subject;
    _ = &first;
    _ = &second;
    return ecs_add_id(world, subject, ecs_pair(first, second));
}
pub inline fn ecs_remove(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_remove_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_remove_id(world, entity, ecs_id(T));
}
pub inline fn ecs_remove_pair(world: anytype, subject: anytype, first: anytype, second: anytype) @TypeOf(ecs_remove_id(world, subject, ecs_pair(first, second))) {
    _ = &world;
    _ = &subject;
    _ = &first;
    _ = &second;
    return ecs_remove_id(world, subject, ecs_pair(first, second));
}
pub inline fn ecs_auto_override(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_auto_override_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_auto_override_id(world, entity, ecs_id(T));
}
pub inline fn ecs_auto_override_pair(world: anytype, subject: anytype, first: anytype, second: anytype) @TypeOf(ecs_auto_override_id(world, subject, ecs_pair(first, second))) {
    _ = &world;
    _ = &subject;
    _ = &first;
    _ = &second;
    return ecs_auto_override_id(world, subject, ecs_pair(first, second));
}
pub const ecs_insert = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9755:9
pub inline fn ecs_set_ptr(world: anytype, entity: anytype, component: anytype, ptr: anytype) @TypeOf(ecs_set_id(world, entity, ecs_id(component), @import("std").zig.c_translation.sizeof(component), ptr)) {
    _ = &world;
    _ = &entity;
    _ = &component;
    _ = &ptr;
    return ecs_set_id(world, entity, ecs_id(component), @import("std").zig.c_translation.sizeof(component), ptr);
}
pub const ecs_set = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9763:9
pub const ecs_set_pair = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9766:9
pub const ecs_set_pair_second = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9771:9
pub const ecs_set_override = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9776:9
pub const ecs_emplace = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9782:9
pub const ecs_emplace_pair = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9785:9
pub const ecs_get = @compileError("unable to translate C expr: unexpected token 'const'");
// ../flecs/distr/flecs.h:9790:9
pub const ecs_get_pair = @compileError("unable to translate C expr: unexpected token 'const'");
// ../flecs/distr/flecs.h:9793:9
pub const ecs_get_pair_second = @compileError("unable to translate C expr: unexpected token 'const'");
// ../flecs/distr/flecs.h:9797:9
pub const ecs_get_mut = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9803:9
pub const ecs_get_mut_pair = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9806:9
pub const ecs_get_mut_pair_second = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9810:9
pub const ecs_ensure = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9819:9
pub const ecs_ensure_pair = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9822:9
pub const ecs_ensure_pair_second = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9826:9
pub inline fn ecs_modified(world: anytype, entity: anytype, component: anytype) @TypeOf(ecs_modified_id(world, entity, ecs_id(component))) {
    _ = &world;
    _ = &entity;
    _ = &component;
    return ecs_modified_id(world, entity, ecs_id(component));
}
pub inline fn ecs_modified_pair(world: anytype, subject: anytype, first: anytype, second: anytype) @TypeOf(ecs_modified_id(world, subject, ecs_pair(first, second))) {
    _ = &world;
    _ = &subject;
    _ = &first;
    _ = &second;
    return ecs_modified_id(world, subject, ecs_pair(first, second));
}
pub const ecs_record_get = @compileError("unable to translate C expr: unexpected token 'const'");
// ../flecs/distr/flecs.h:9851:9
pub inline fn ecs_record_has(world: anytype, record: anytype, T: anytype) @TypeOf(ecs_record_has_id(world, record, ecs_id(T))) {
    _ = &world;
    _ = &record;
    _ = &T;
    return ecs_record_has_id(world, record, ecs_id(T));
}
pub const ecs_record_get_pair = @compileError("unable to translate C expr: unexpected token 'const'");
// ../flecs/distr/flecs.h:9857:9
pub const ecs_record_get_pair_second = @compileError("unable to translate C expr: unexpected token 'const'");
// ../flecs/distr/flecs.h:9861:9
pub const ecs_record_ensure = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9865:9
pub const ecs_record_ensure_pair = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9868:9
pub const ecs_record_ensure_pair_second = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9872:9
pub inline fn ecs_ref_init(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_ref_init_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_ref_init_id(world, entity, ecs_id(T));
}
pub const ecs_ref_get = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:9879:9
pub inline fn ecs_singleton_add(world: anytype, comp: anytype) @TypeOf(ecs_add(world, ecs_id(comp), comp)) {
    _ = &world;
    _ = &comp;
    return ecs_add(world, ecs_id(comp), comp);
}
pub inline fn ecs_singleton_remove(world: anytype, comp: anytype) @TypeOf(ecs_remove(world, ecs_id(comp), comp)) {
    _ = &world;
    _ = &comp;
    return ecs_remove(world, ecs_id(comp), comp);
}
pub inline fn ecs_singleton_get(world: anytype, comp: anytype) @TypeOf(ecs_get(world, ecs_id(comp), comp)) {
    _ = &world;
    _ = &comp;
    return ecs_get(world, ecs_id(comp), comp);
}
pub inline fn ecs_singleton_set_ptr(world: anytype, comp: anytype, ptr: anytype) @TypeOf(ecs_set_ptr(world, ecs_id(comp), comp, ptr)) {
    _ = &world;
    _ = &comp;
    _ = &ptr;
    return ecs_set_ptr(world, ecs_id(comp), comp, ptr);
}
pub const ecs_singleton_set = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9901:9
pub inline fn ecs_singleton_ensure(world: anytype, comp: anytype) @TypeOf(ecs_ensure(world, ecs_id(comp), comp)) {
    _ = &world;
    _ = &comp;
    return ecs_ensure(world, ecs_id(comp), comp);
}
pub inline fn ecs_singleton_modified(world: anytype, comp: anytype) @TypeOf(ecs_modified(world, ecs_id(comp), comp)) {
    _ = &world;
    _ = &comp;
    return ecs_modified(world, ecs_id(comp), comp);
}
pub inline fn ecs_has(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_has_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_has_id(world, entity, ecs_id(T));
}
pub inline fn ecs_has_pair(world: anytype, entity: anytype, first: anytype, second: anytype) @TypeOf(ecs_has_id(world, entity, ecs_pair(first, second))) {
    _ = &world;
    _ = &entity;
    _ = &first;
    _ = &second;
    return ecs_has_id(world, entity, ecs_pair(first, second));
}
pub inline fn ecs_owns_pair(world: anytype, entity: anytype, first: anytype, second: anytype) @TypeOf(ecs_owns_id(world, entity, ecs_pair(first, second))) {
    _ = &world;
    _ = &entity;
    _ = &first;
    _ = &second;
    return ecs_owns_id(world, entity, ecs_pair(first, second));
}
pub inline fn ecs_owns(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_owns_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_owns_id(world, entity, ecs_id(T));
}
pub inline fn ecs_shares_id(world: anytype, entity: anytype, id: anytype) @TypeOf(ecs_search_relation(world, ecs_get_table(world, entity), @as(c_int, 0), ecs_id(id), EcsIsA, @as(c_int, 1), @as(c_int, 0), @as(c_int, 0), @as(c_int, 0), @as(c_int, 0)) != -@as(c_int, 1)) {
    _ = &world;
    _ = &entity;
    _ = &id;
    return ecs_search_relation(world, ecs_get_table(world, entity), @as(c_int, 0), ecs_id(id), EcsIsA, @as(c_int, 1), @as(c_int, 0), @as(c_int, 0), @as(c_int, 0), @as(c_int, 0)) != -@as(c_int, 1);
}
pub inline fn ecs_shares_pair(world: anytype, entity: anytype, first: anytype, second: anytype) @TypeOf(ecs_shares_id(world, entity, ecs_pair(first, second))) {
    _ = &world;
    _ = &entity;
    _ = &first;
    _ = &second;
    return ecs_shares_id(world, entity, ecs_pair(first, second));
}
pub inline fn ecs_shares(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_shares_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_shares_id(world, entity, ecs_id(T));
}
pub inline fn ecs_get_target_for(world: anytype, entity: anytype, rel: anytype, T: anytype) @TypeOf(ecs_get_target_for_id(world, entity, rel, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &rel;
    _ = &T;
    return ecs_get_target_for_id(world, entity, rel, ecs_id(T));
}
pub inline fn ecs_enable_component(world: anytype, entity: anytype, T: anytype, enable: anytype) @TypeOf(ecs_enable_id(world, entity, ecs_id(T), enable)) {
    _ = &world;
    _ = &entity;
    _ = &T;
    _ = &enable;
    return ecs_enable_id(world, entity, ecs_id(T), enable);
}
pub inline fn ecs_is_enabled(world: anytype, entity: anytype, T: anytype) @TypeOf(ecs_is_enabled_id(world, entity, ecs_id(T))) {
    _ = &world;
    _ = &entity;
    _ = &T;
    return ecs_is_enabled_id(world, entity, ecs_id(T));
}
pub inline fn ecs_enable_pair(world: anytype, entity: anytype, First: anytype, second: anytype, enable: anytype) @TypeOf(ecs_enable_id(world, entity, ecs_pair(ecs_id(First), second), enable)) {
    _ = &world;
    _ = &entity;
    _ = &First;
    _ = &second;
    _ = &enable;
    return ecs_enable_id(world, entity, ecs_pair(ecs_id(First), second), enable);
}
pub inline fn ecs_is_enabled_pair(world: anytype, entity: anytype, First: anytype, second: anytype) @TypeOf(ecs_is_enabled_id(world, entity, ecs_pair(ecs_id(First), second))) {
    _ = &world;
    _ = &entity;
    _ = &First;
    _ = &second;
    return ecs_is_enabled_id(world, entity, ecs_pair(ecs_id(First), second));
}
pub inline fn ecs_lookup_from(world: anytype, parent: anytype, path: anytype) @TypeOf(ecs_lookup_path_w_sep(world, parent, path, ".", NULL, @"true")) {
    _ = &world;
    _ = &parent;
    _ = &path;
    return ecs_lookup_path_w_sep(world, parent, path, ".", NULL, @"true");
}
pub inline fn ecs_get_path_from(world: anytype, parent: anytype, child: anytype) @TypeOf(ecs_get_path_w_sep(world, parent, child, ".", NULL)) {
    _ = &world;
    _ = &parent;
    _ = &child;
    return ecs_get_path_w_sep(world, parent, child, ".", NULL);
}
pub inline fn ecs_get_path(world: anytype, child: anytype) @TypeOf(ecs_get_path_w_sep(world, @as(c_int, 0), child, ".", NULL)) {
    _ = &world;
    _ = &child;
    return ecs_get_path_w_sep(world, @as(c_int, 0), child, ".", NULL);
}
pub inline fn ecs_get_path_buf(world: anytype, child: anytype, buf: anytype) @TypeOf(ecs_get_path_w_sep_buf(world, @as(c_int, 0), child, ".", NULL, buf)) {
    _ = &world;
    _ = &child;
    _ = &buf;
    return ecs_get_path_w_sep_buf(world, @as(c_int, 0), child, ".", NULL, buf);
}
pub inline fn ecs_new_from_path(world: anytype, parent: anytype, path: anytype) @TypeOf(ecs_new_from_path_w_sep(world, parent, path, ".", NULL)) {
    _ = &world;
    _ = &parent;
    _ = &path;
    return ecs_new_from_path_w_sep(world, parent, path, ".", NULL);
}
pub inline fn ecs_add_path(world: anytype, entity: anytype, parent: anytype, path: anytype) @TypeOf(ecs_add_path_w_sep(world, entity, parent, path, ".", NULL)) {
    _ = &world;
    _ = &entity;
    _ = &parent;
    _ = &path;
    return ecs_add_path_w_sep(world, entity, parent, path, ".", NULL);
}
pub inline fn ecs_add_fullpath(world: anytype, entity: anytype, path: anytype) @TypeOf(ecs_add_path_w_sep(world, entity, @as(c_int, 0), path, ".", NULL)) {
    _ = &world;
    _ = &entity;
    _ = &path;
    return ecs_add_path_w_sep(world, entity, @as(c_int, 0), path, ".", NULL);
}
pub const ecs_set_hooks = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:9998:9
pub const ecs_get_hooks = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10001:9
pub const ECS_CTOR = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10011:9
pub const ECS_DTOR = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10021:9
pub const ECS_COPY = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10031:9
pub const ECS_MOVE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10041:9
pub const ECS_ON_ADD = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10051:9
pub const ECS_ON_REMOVE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10053:9
pub const ECS_ON_SET = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10055:9
pub const ecs_ctor = @compileError("unable to translate macro: undefined identifier `_ctor`");
// ../flecs/distr/flecs.h:10059:9
pub const ecs_dtor = @compileError("unable to translate macro: undefined identifier `_dtor`");
// ../flecs/distr/flecs.h:10060:9
pub const ecs_copy = @compileError("unable to translate macro: undefined identifier `_copy`");
// ../flecs/distr/flecs.h:10061:9
pub const ecs_move = @compileError("unable to translate macro: undefined identifier `_move`");
// ../flecs/distr/flecs.h:10062:9
pub const ecs_on_set = @compileError("unable to translate macro: undefined identifier `_on_set`");
// ../flecs/distr/flecs.h:10063:9
pub const ecs_on_add = @compileError("unable to translate macro: undefined identifier `_on_add`");
// ../flecs/distr/flecs.h:10064:9
pub const ecs_on_remove = @compileError("unable to translate macro: undefined identifier `_on_remove`");
// ../flecs/distr/flecs.h:10065:9
pub inline fn ecs_count(world: anytype, @"type": anytype) @TypeOf(ecs_count_id(world, ecs_id(@"type"))) {
    _ = &world;
    _ = &@"type";
    return ecs_count_id(world, ecs_id(@"type"));
}
pub const ecs_field = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:10084:9
pub const ecs_field_self = @compileError("unable to translate macro: undefined identifier `ecs_field_self_w_size`");
// ../flecs/distr/flecs.h:10087:9
pub const ecs_field_at = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:10090:9
pub const ecs_table_get = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:10100:9
pub const ecs_table_get_pair = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:10103:9
pub const ecs_table_get_pair_second = @compileError("unable to translate C expr: unexpected token ','");
// ../flecs/distr/flecs.h:10106:9
pub const ecs_ids = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10117:9
pub const ecs_values = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10120:9
pub inline fn ecs_value_ptr(T: anytype, ptr: anytype) ecs_value_t {
    _ = &T;
    _ = &ptr;
    return @import("std").mem.zeroInit(ecs_value_t, .{ ecs_id(T), ptr });
}
pub const ecs_value_pair = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10126:9
pub const ecs_value_pair_2nd = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10129:9
pub inline fn ecs_value_new_t(world: anytype, T: anytype) @TypeOf(ecs_value_new(world, ecs_id(T))) {
    _ = &world;
    _ = &T;
    return ecs_value_new(world, ecs_id(T));
}
pub const ecs_value = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10135:9
pub const ecs_sort_table = @compileError("unable to translate macro: undefined identifier `_sort_table`");
// ../flecs/distr/flecs.h:10147:9
pub const ecs_compare = @compileError("unable to translate macro: undefined identifier `_compare_fn`");
// ../flecs/distr/flecs.h:10149:9
pub const ECS_SORT_TABLE_WITH_COMPARE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10174:9
pub const ECS_SORT_TABLE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10244:9
pub const ECS_COMPARE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10257:9
pub inline fn ecs_isa(e: anytype) @TypeOf(ecs_pair(EcsIsA, e)) {
    _ = &e;
    return ecs_pair(EcsIsA, e);
}
pub inline fn ecs_childof(e: anytype) @TypeOf(ecs_pair(EcsChildOf, e)) {
    _ = &e;
    return ecs_pair(EcsChildOf, e);
}
pub inline fn ecs_dependson(e: anytype) @TypeOf(ecs_pair(EcsDependsOn, e)) {
    _ = &e;
    return ecs_pair(EcsDependsOn, e);
}
pub inline fn ecs_with(e: anytype) @TypeOf(ecs_pair(EcsWith, e)) {
    _ = &e;
    return ecs_pair(EcsWith, e);
}
pub inline fn ecs_each(world: anytype, id: anytype) @TypeOf(ecs_each_id(world, ecs_id(id))) {
    _ = &world;
    _ = &id;
    return ecs_each_id(world, ecs_id(id));
}
pub inline fn ecs_each_pair(world: anytype, r: anytype, t: anytype) @TypeOf(ecs_each_id(world, ecs_pair(r, t))) {
    _ = &world;
    _ = &r;
    _ = &t;
    return ecs_each_id(world, ecs_pair(r, t));
}
pub inline fn ecs_each_pair_t(world: anytype, R: anytype, t: anytype) @TypeOf(ecs_each_id(world, ecs_pair(ecs_id(R), t))) {
    _ = &world;
    _ = &R;
    _ = &t;
    return ecs_each_id(world, ecs_pair(ecs_id(R), t));
}
pub const FLECS_ADDONS_H = "";
pub const flecs_journal_begin = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10428:9
pub const flecs_journal_end = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10429:9
pub const flecs_journal = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10430:9
pub const FLECS_LOG_H = "";
pub const ecs_print = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10624:9
pub const ecs_printv = @compileError("unable to translate macro: undefined identifier `__FILE__`");
// ../flecs/distr/flecs.h:10627:9
pub const ecs_log = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10630:9
pub const ecs_logv = @compileError("unable to translate macro: undefined identifier `__FILE__`");
// ../flecs/distr/flecs.h:10633:9
pub const ecs_trace_ = @compileError("unable to translate C expr: expected ')' instead got 'line'");
// ../flecs/distr/flecs.h:10637:9
pub const ecs_trace = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10638:9
pub const ecs_warn_ = @compileError("unable to translate C expr: expected ')' instead got 'line'");
// ../flecs/distr/flecs.h:10641:9
pub const ecs_warn = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10642:9
pub const ecs_err_ = @compileError("unable to translate C expr: expected ')' instead got 'line'");
// ../flecs/distr/flecs.h:10645:9
pub const ecs_err = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10646:9
pub const ecs_fatal_ = @compileError("unable to translate C expr: expected ')' instead got 'line'");
// ../flecs/distr/flecs.h:10649:9
pub const ecs_fatal = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10650:9
pub const ecs_deprecated = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10654:9
pub const FLECS_LOG_3 = "";
pub const ecs_dbg_1 = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10674:9
pub const ecs_dbg_2 = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10675:9
pub const ecs_dbg_3 = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10676:9
pub const ecs_log_push_1 = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10678:9
pub const ecs_log_push_2 = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10679:9
pub const ecs_log_push_3 = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10680:9
pub const ecs_log_pop_1 = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10682:9
pub const ecs_log_pop_2 = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10683:9
pub const ecs_log_pop_3 = @compileError("unable to translate C expr: unexpected token ';'");
// ../flecs/distr/flecs.h:10684:9
pub inline fn ecs_should_log_1() @TypeOf(ecs_should_log(@as(c_int, 1))) {
    return ecs_should_log(@as(c_int, 1));
}
pub inline fn ecs_should_log_2() @TypeOf(ecs_should_log(@as(c_int, 2))) {
    return ecs_should_log(@as(c_int, 2));
}
pub inline fn ecs_should_log_3() @TypeOf(ecs_should_log(@as(c_int, 3))) {
    return ecs_should_log(@as(c_int, 3));
}
pub const FLECS_LOG_2 = "";
pub const FLECS_LOG_1 = "";
pub const FLECS_LOG_0 = "";
pub const ecs_dbg = ecs_dbg_1;
pub inline fn ecs_log_push() @TypeOf(ecs_log_push_(@as(c_int, 0))) {
    return ecs_log_push_(@as(c_int, 0));
}
pub inline fn ecs_log_pop() @TypeOf(ecs_log_pop_(@as(c_int, 0))) {
    return ecs_log_pop_(@as(c_int, 0));
}
pub const ecs_abort = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10776:9
pub const ecs_assert = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10785:9
pub const ecs_assert_var = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10793:9
pub const ecs_dbg_assert = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10800:9
pub const ecs_san_assert = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10810:9
pub const ecs_dummy_check = @compileError("unable to translate C expr: unexpected token 'if'");
// ../flecs/distr/flecs.h:10815:9
pub const ecs_check = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10832:9
pub const ecs_throw = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10848:9
pub const ecs_parser_error = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:10855:9
pub inline fn ecs_parser_errorv(name: anytype, expr: anytype, column: anytype, fmt: anytype, args: anytype) @TypeOf(ecs_parser_errorv_(name, expr, column, fmt, args)) {
    _ = &name;
    _ = &expr;
    _ = &column;
    _ = &fmt;
    _ = &args;
    return ecs_parser_errorv_(name, expr, column, fmt, args);
}
pub const ECS_INVALID_OPERATION = @as(c_int, 1);
pub const ECS_INVALID_PARAMETER = @as(c_int, 2);
pub const ECS_CONSTRAINT_VIOLATED = @as(c_int, 3);
pub const ECS_OUT_OF_MEMORY = @as(c_int, 4);
pub const ECS_OUT_OF_RANGE = @as(c_int, 5);
pub const ECS_UNSUPPORTED = @as(c_int, 6);
pub const ECS_INTERNAL_ERROR = @as(c_int, 7);
pub const ECS_ALREADY_DEFINED = @as(c_int, 8);
pub const ECS_MISSING_OS_API = @as(c_int, 9);
pub const ECS_OPERATION_FAILED = @as(c_int, 10);
pub const ECS_INVALID_CONVERSION = @as(c_int, 11);
pub const ECS_ID_IN_USE = @as(c_int, 12);
pub const ECS_CYCLE_DETECTED = @as(c_int, 13);
pub const ECS_LEAK_DETECTED = @as(c_int, 14);
pub const ECS_DOUBLE_FREE = @as(c_int, 15);
pub const ECS_INCONSISTENT_NAME = @as(c_int, 20);
pub const ECS_NAME_IN_USE = @as(c_int, 21);
pub const ECS_NOT_A_COMPONENT = @as(c_int, 22);
pub const ECS_INVALID_COMPONENT_SIZE = @as(c_int, 23);
pub const ECS_INVALID_COMPONENT_ALIGNMENT = @as(c_int, 24);
pub const ECS_COMPONENT_NOT_REGISTERED = @as(c_int, 25);
pub const ECS_INCONSISTENT_COMPONENT_ID = @as(c_int, 26);
pub const ECS_INCONSISTENT_COMPONENT_ACTION = @as(c_int, 27);
pub const ECS_MODULE_UNDEFINED = @as(c_int, 28);
pub const ECS_MISSING_SYMBOL = @as(c_int, 29);
pub const ECS_ALREADY_IN_USE = @as(c_int, 30);
pub const ECS_ACCESS_VIOLATION = @as(c_int, 40);
pub const ECS_COLUMN_INDEX_OUT_OF_RANGE = @as(c_int, 41);
pub const ECS_COLUMN_IS_NOT_SHARED = @as(c_int, 42);
pub const ECS_COLUMN_IS_SHARED = @as(c_int, 43);
pub const ECS_COLUMN_TYPE_MISMATCH = @as(c_int, 45);
pub const ECS_INVALID_WHILE_READONLY = @as(c_int, 70);
pub const ECS_LOCKED_STORAGE = @as(c_int, 71);
pub const ECS_INVALID_FROM_WORKER = @as(c_int, 72);
pub const ECS_BLACK = "\x1b[1;30m";
pub const ECS_RED = "\x1b[0;31m";
pub const ECS_GREEN = "\x1b[0;32m";
pub const ECS_YELLOW = "\x1b[0;33m";
pub const ECS_BLUE = "\x1b[0;34m";
pub const ECS_MAGENTA = "\x1b[0;35m";
pub const ECS_CYAN = "\x1b[0;36m";
pub const ECS_WHITE = "\x1b[1;37m";
pub const ECS_GREY = "\x1b[0;37m";
pub const ECS_NORMAL = "\x1b[0;49m";
pub const ECS_BOLD = "\x1b[1;49m";
pub const FLECS_APP_H = "";
pub const FLECS_HTTP_H = "";
pub const ECS_HTTP_HEADER_COUNT_MAX = @as(c_int, 32);
pub const ECS_HTTP_QUERY_PARAM_COUNT_MAX = @as(c_int, 32);
pub const ECS_HTTP_REPLY_INIT = @import("std").mem.zeroInit(ecs_http_reply_t, .{ @as(c_int, 200), ECS_STRBUF_INIT, "OK", "application/json", ECS_STRBUF_INIT });
pub const FLECS_REST_H = "";
pub const ECS_REST_DEFAULT_PORT = @as(c_int, 27750);
pub const FLECS_TIMER_H = "";
pub const FLECS_PIPELINE_H = "";
pub const ECS_PIPELINE_DEFINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:11837:9
pub const ECS_PIPELINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:11857:9
pub const ecs_pipeline = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:11865:9
pub const FLECS_SYSTEM_H = "";
pub inline fn ECS_SYSTEM_DECLARE(id: anytype) @TypeOf(ecs_entity_t ++ ecs_id(id)) {
    _ = &id;
    return ecs_entity_t ++ ecs_id(id);
}
pub const ECS_SYSTEM_DEFINE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:12273:9
pub const ECS_SYSTEM = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:12300:9
pub const ecs_system = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:12324:9
pub const FLECS_STATS_H = "";
pub const ECS_STAT_WINDOW = @as(c_int, 60);
pub const FLECS_METRICS_H = "";
pub const ecs_metric = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:13070:9
pub const FLECS_ALERTS_H = "";
pub const ECS_ALERT_MAX_SEVERITY_FILTERS = @as(c_int, 4);
pub const ecs_alert = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:13268:9
pub const FLECS_JSON_H = "";
pub const ECS_ENTITY_TO_JSON_INIT = @compileError("unable to translate C expr: expected '.' instead got '}'");
// ../flecs/distr/flecs.h:13560:9
pub const ECS_ITER_TO_JSON_INIT = @compileError("unable to translate C expr: expected '.' instead got '}'");
// ../flecs/distr/flecs.h:13626:9
pub const FLECS_UNITS_H = "";
pub const FLECS_SCRIPT_H = "";
pub const ecs_script = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:14288:9
pub inline fn ecs_script_vars_define(vars: anytype, name: anytype, @"type": anytype) @TypeOf(ecs_script_vars_define_id(vars, name, ecs_id(@"type"))) {
    _ = &vars;
    _ = &name;
    _ = &@"type";
    return ecs_script_vars_define_id(vars, name, ecs_id(@"type"));
}
pub const FLECS_DOC_H = "";
pub const __STDDEF_H = "";
pub const __need_ptrdiff_t = "";
pub const __need_size_t = "";
pub const __need_rsize_t = "";
pub const __need_wchar_t = "";
pub const __need_NULL = "";
pub const __need_max_align_t = "";
pub const __need_offsetof = "";
pub const _PTRDIFF_T = "";
pub const _WCHAR_T = "";
pub const __CLANG_MAX_ALIGN_T_DEFINED = "";
pub const offsetof = @compileError("unable to translate C expr: unexpected token 'an identifier'");
// /opt/homebrew/Cellar/zig/0.13.0/lib/zig/include/__stddef_offsetof.h:16:9
pub const FLECS_META_H = "";
pub const ECS_MEMBER_DESC_CACHE_SIZE = @as(c_int, 32);
pub const ECS_META_MAX_SCOPE_DEPTH = @as(c_int, 32);
pub const ecs_primitive = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16046:9
pub const ecs_enum = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16050:9
pub const ecs_bitmask = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16054:9
pub const ecs_array = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16058:9
pub const ecs_vector = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16062:9
pub const ecs_opaque = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16066:9
pub const ecs_struct = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16070:9
pub const ecs_unit = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16074:9
pub const ecs_unit_prefix = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16078:9
pub const ecs_quantity = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16082:9
pub const FLECS_META_C_H = "";
pub const ECS_META_COMPONENT = @compileError("unable to translate macro: undefined identifier `FLECS__`");
// ../flecs/distr/flecs.h:16141:9
pub const ECS_STRUCT = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16147:9
pub const ECS_ENUM = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16152:9
pub const ECS_BITMASK = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16157:9
pub const ECS_PRIVATE = "";
pub const ECS_META_IMPL_CALL_INNER = @compileError("unable to translate C expr: unexpected token '##'");
// ../flecs/distr/flecs.h:16177:9
pub inline fn ECS_META_IMPL_CALL(base: anytype, impl: anytype, name: anytype, type_desc: anytype) @TypeOf(ECS_META_IMPL_CALL_INNER(base, impl, name, type_desc)) {
    _ = &base;
    _ = &impl;
    _ = &name;
    _ = &type_desc;
    return ECS_META_IMPL_CALL_INNER(base, impl, name, type_desc);
}
pub const ECS_STRUCT_TYPE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16184:9
pub const ECS_STRUCT_ECS_META_IMPL = ECS_STRUCT_IMPL;
pub const ECS_STRUCT_IMPL = @compileError("unable to translate macro: undefined identifier `FLECS__`");
// ../flecs/distr/flecs.h:16189:9
pub const ECS_STRUCT_DECLARE = @compileError("unable to translate C expr: unexpected token 'extern'");
// ../flecs/distr/flecs.h:16195:9
pub const ECS_STRUCT_EXTERN = @compileError("unable to translate C expr: unexpected token 'extern'");
// ../flecs/distr/flecs.h:16199:9
pub const ECS_ENUM_TYPE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16204:9
pub const ECS_ENUM_ECS_META_IMPL = ECS_ENUM_IMPL;
pub const ECS_ENUM_IMPL = @compileError("unable to translate macro: undefined identifier `FLECS__`");
// ../flecs/distr/flecs.h:16209:9
pub const ECS_ENUM_DECLARE = @compileError("unable to translate C expr: unexpected token 'extern'");
// ../flecs/distr/flecs.h:16215:9
pub const ECS_ENUM_EXTERN = @compileError("unable to translate C expr: unexpected token 'extern'");
// ../flecs/distr/flecs.h:16219:9
pub const ECS_BITMASK_TYPE = @compileError("unable to translate C expr: expected ')' instead got '...'");
// ../flecs/distr/flecs.h:16224:9
pub const ECS_BITMASK_ECS_META_IMPL = ECS_BITMASK_IMPL;
pub const ECS_BITMASK_IMPL = @compileError("unable to translate macro: undefined identifier `FLECS__`");
// ../flecs/distr/flecs.h:16229:9
pub const ECS_BITMASK_DECLARE = @compileError("unable to translate C expr: unexpected token 'extern'");
// ../flecs/distr/flecs.h:16235:9
pub const ECS_BITMASK_EXTERN = @compileError("unable to translate C expr: unexpected token 'extern'");
// ../flecs/distr/flecs.h:16239:9
pub const FLECS_OS_API_IMPL_H = "";
pub const FLECS_MODULE_H = "";
pub const ECS_MODULE_DEFINE = @compileError("unable to translate macro: undefined identifier `desc`");
// ../flecs/distr/flecs.h:16405:9
pub const ECS_MODULE = @compileError("unable to translate C expr: unexpected token '='");
// ../flecs/distr/flecs.h:16414:9
pub const ECS_IMPORT = @compileError("unable to translate macro: undefined identifier `Import`");
// ../flecs/distr/flecs.h:16426:9
pub const FLECS_CPP_H = "";
pub const __darwin_pthread_handler_rec = struct___darwin_pthread_handler_rec;
pub const _opaque_pthread_attr_t = struct__opaque_pthread_attr_t;
pub const _opaque_pthread_cond_t = struct__opaque_pthread_cond_t;
pub const _opaque_pthread_condattr_t = struct__opaque_pthread_condattr_t;
pub const _opaque_pthread_mutex_t = struct__opaque_pthread_mutex_t;
pub const _opaque_pthread_mutexattr_t = struct__opaque_pthread_mutexattr_t;
pub const _opaque_pthread_once_t = struct__opaque_pthread_once_t;
pub const _opaque_pthread_rwlock_t = struct__opaque_pthread_rwlock_t;
pub const _opaque_pthread_rwlockattr_t = struct__opaque_pthread_rwlockattr_t;
pub const _opaque_pthread_t = struct__opaque_pthread_t;
pub const __sbuf = struct___sbuf;
pub const __sFILEX = struct___sFILEX;
pub const __sFILE = struct___sFILE;
