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
 * struct cn_protocol_connect_token_t {
 *     uint64_t creation_timestamp;
 *     cn_crypto_key_t client_to_server_key;
 *     cn_crypto_key_t server_to_client_key;
 *     uint64_t expiration_timestamp;
 *     uint32_t handshake_timeout;
 *     uint16_t endpoint_count;
 *     cn_endpoint_t endpoints[32];
 * }
 * }
 */
public class cn_protocol_connect_token_t {

    cn_protocol_connect_token_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_LONG_LONG.withName("creation_timestamp"),
        cn_crypto_key_t.layout().withName("client_to_server_key"),
        cn_crypto_key_t.layout().withName("server_to_client_key"),
        netcode.C_LONG_LONG.withName("expiration_timestamp"),
        netcode.C_INT.withName("handshake_timeout"),
        netcode.C_SHORT.withName("endpoint_count"),
        MemoryLayout.paddingLayout(2),
        MemoryLayout.sequenceLayout(32, cn_endpoint_t.layout()).withName("endpoints")
    ).withName("cn_protocol_connect_token_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfLong creation_timestamp$LAYOUT = (OfLong)$LAYOUT.select(groupElement("creation_timestamp"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t creation_timestamp
     * }
     */
    public static final OfLong creation_timestamp$layout() {
        return creation_timestamp$LAYOUT;
    }

    private static final long creation_timestamp$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t creation_timestamp
     * }
     */
    public static final long creation_timestamp$offset() {
        return creation_timestamp$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t creation_timestamp
     * }
     */
    public static long creation_timestamp(MemorySegment struct) {
        return struct.get(creation_timestamp$LAYOUT, creation_timestamp$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t creation_timestamp
     * }
     */
    public static void creation_timestamp(MemorySegment struct, long fieldValue) {
        struct.set(creation_timestamp$LAYOUT, creation_timestamp$OFFSET, fieldValue);
    }

    private static final GroupLayout client_to_server_key$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("client_to_server_key"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_crypto_key_t client_to_server_key
     * }
     */
    public static final GroupLayout client_to_server_key$layout() {
        return client_to_server_key$LAYOUT;
    }

    private static final long client_to_server_key$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_crypto_key_t client_to_server_key
     * }
     */
    public static final long client_to_server_key$offset() {
        return client_to_server_key$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_crypto_key_t client_to_server_key
     * }
     */
    public static MemorySegment client_to_server_key(MemorySegment struct) {
        return struct.asSlice(client_to_server_key$OFFSET, client_to_server_key$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_crypto_key_t client_to_server_key
     * }
     */
    public static void client_to_server_key(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, client_to_server_key$OFFSET, client_to_server_key$LAYOUT.byteSize());
    }

    private static final GroupLayout server_to_client_key$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("server_to_client_key"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_crypto_key_t server_to_client_key
     * }
     */
    public static final GroupLayout server_to_client_key$layout() {
        return server_to_client_key$LAYOUT;
    }

    private static final long server_to_client_key$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_crypto_key_t server_to_client_key
     * }
     */
    public static final long server_to_client_key$offset() {
        return server_to_client_key$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_crypto_key_t server_to_client_key
     * }
     */
    public static MemorySegment server_to_client_key(MemorySegment struct) {
        return struct.asSlice(server_to_client_key$OFFSET, server_to_client_key$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_crypto_key_t server_to_client_key
     * }
     */
    public static void server_to_client_key(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, server_to_client_key$OFFSET, server_to_client_key$LAYOUT.byteSize());
    }

    private static final OfLong expiration_timestamp$LAYOUT = (OfLong)$LAYOUT.select(groupElement("expiration_timestamp"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t expiration_timestamp
     * }
     */
    public static final OfLong expiration_timestamp$layout() {
        return expiration_timestamp$LAYOUT;
    }

    private static final long expiration_timestamp$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t expiration_timestamp
     * }
     */
    public static final long expiration_timestamp$offset() {
        return expiration_timestamp$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t expiration_timestamp
     * }
     */
    public static long expiration_timestamp(MemorySegment struct) {
        return struct.get(expiration_timestamp$LAYOUT, expiration_timestamp$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t expiration_timestamp
     * }
     */
    public static void expiration_timestamp(MemorySegment struct, long fieldValue) {
        struct.set(expiration_timestamp$LAYOUT, expiration_timestamp$OFFSET, fieldValue);
    }

    private static final OfInt handshake_timeout$LAYOUT = (OfInt)$LAYOUT.select(groupElement("handshake_timeout"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint32_t handshake_timeout
     * }
     */
    public static final OfInt handshake_timeout$layout() {
        return handshake_timeout$LAYOUT;
    }

    private static final long handshake_timeout$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint32_t handshake_timeout
     * }
     */
    public static final long handshake_timeout$offset() {
        return handshake_timeout$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint32_t handshake_timeout
     * }
     */
    public static int handshake_timeout(MemorySegment struct) {
        return struct.get(handshake_timeout$LAYOUT, handshake_timeout$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint32_t handshake_timeout
     * }
     */
    public static void handshake_timeout(MemorySegment struct, int fieldValue) {
        struct.set(handshake_timeout$LAYOUT, handshake_timeout$OFFSET, fieldValue);
    }

    private static final OfShort endpoint_count$LAYOUT = (OfShort)$LAYOUT.select(groupElement("endpoint_count"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint16_t endpoint_count
     * }
     */
    public static final OfShort endpoint_count$layout() {
        return endpoint_count$LAYOUT;
    }

    private static final long endpoint_count$OFFSET = 84;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint16_t endpoint_count
     * }
     */
    public static final long endpoint_count$offset() {
        return endpoint_count$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint16_t endpoint_count
     * }
     */
    public static short endpoint_count(MemorySegment struct) {
        return struct.get(endpoint_count$LAYOUT, endpoint_count$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint16_t endpoint_count
     * }
     */
    public static void endpoint_count(MemorySegment struct, short fieldValue) {
        struct.set(endpoint_count$LAYOUT, endpoint_count$OFFSET, fieldValue);
    }

    private static final SequenceLayout endpoints$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("endpoints"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static final SequenceLayout endpoints$layout() {
        return endpoints$LAYOUT;
    }

    private static final long endpoints$OFFSET = 88;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static final long endpoints$offset() {
        return endpoints$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static MemorySegment endpoints(MemorySegment struct) {
        return struct.asSlice(endpoints$OFFSET, endpoints$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static void endpoints(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, endpoints$OFFSET, endpoints$LAYOUT.byteSize());
    }

    private static long[] endpoints$DIMS = { 32 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static long[] endpoints$dimensions() {
        return endpoints$DIMS;
    }
    private static final MethodHandle endpoints$ELEM_HANDLE = endpoints$LAYOUT.sliceHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static MemorySegment endpoints(MemorySegment struct, long index0) {
        try {
            return (MemorySegment)endpoints$ELEM_HANDLE.invokeExact(struct, 0L, index0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * cn_endpoint_t endpoints[32]
     * }
     */
    public static void endpoints(MemorySegment struct, long index0, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, endpoints(struct, index0), 0L, cn_endpoint_t.layout().byteSize());
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

