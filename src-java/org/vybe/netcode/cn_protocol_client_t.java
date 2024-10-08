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
 * struct cn_protocol_client_t {
 *     bool use_ipv6;
 *     uint16_t port;
 *     cn_protocol_client_state_t state;
 *     double last_packet_recieved_time;
 *     double last_packet_sent_time;
 *     uint64_t application_id;
 *     uint64_t current_time;
 *     uint64_t client_id;
 *     int max_clients;
 *     double connection_timeout;
 *     int has_sent_disconnect_packets;
 *     cn_protocol_connect_token_t connect_token;
 *     uint64_t challenge_nonce;
 *     uint8_t challenge_data[256];
 *     int goto_next_server;
 *     cn_protocol_client_state_t goto_next_server_tentative_state;
 *     int server_endpoint_index;
 *     cn_endpoint_t web_service_endpoint;
 *     cn_socket_t socket;
 *     uint64_t sequence;
 *     cn_circular_buffer_t packet_queue;
 *     cn_protocol_replay_buffer_t replay_buffer;
 *     cn_simulator_t *sim;
 *     uint8_t buffer[1280];
 *     uint8_t connect_token_packet[1024];
 *     void *mem_ctx;
 * }
 * }
 */
public class cn_protocol_client_t {

    cn_protocol_client_t() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        netcode.C_BOOL.withName("use_ipv6"),
        MemoryLayout.paddingLayout(1),
        netcode.C_SHORT.withName("port"),
        netcode.C_INT.withName("state"),
        netcode.C_DOUBLE.withName("last_packet_recieved_time"),
        netcode.C_DOUBLE.withName("last_packet_sent_time"),
        netcode.C_LONG_LONG.withName("application_id"),
        netcode.C_LONG_LONG.withName("current_time"),
        netcode.C_LONG_LONG.withName("client_id"),
        netcode.C_INT.withName("max_clients"),
        MemoryLayout.paddingLayout(4),
        netcode.C_DOUBLE.withName("connection_timeout"),
        netcode.C_INT.withName("has_sent_disconnect_packets"),
        MemoryLayout.paddingLayout(4),
        cn_protocol_connect_token_t.layout().withName("connect_token"),
        netcode.C_LONG_LONG.withName("challenge_nonce"),
        MemoryLayout.sequenceLayout(256, netcode.C_CHAR).withName("challenge_data"),
        netcode.C_INT.withName("goto_next_server"),
        netcode.C_INT.withName("goto_next_server_tentative_state"),
        netcode.C_INT.withName("server_endpoint_index"),
        cn_endpoint_t.layout().withName("web_service_endpoint"),
        cn_socket_t.layout().withName("socket"),
        netcode.C_LONG_LONG.withName("sequence"),
        cn_circular_buffer_t.layout().withName("packet_queue"),
        cn_protocol_replay_buffer_t.layout().withName("replay_buffer"),
        netcode.C_POINTER.withName("sim"),
        MemoryLayout.sequenceLayout(1280, netcode.C_CHAR).withName("buffer"),
        MemoryLayout.sequenceLayout(1024, netcode.C_CHAR).withName("connect_token_packet"),
        netcode.C_POINTER.withName("mem_ctx")
    ).withName("cn_protocol_client_t");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfBoolean use_ipv6$LAYOUT = (OfBoolean)$LAYOUT.select(groupElement("use_ipv6"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * bool use_ipv6
     * }
     */
    public static final OfBoolean use_ipv6$layout() {
        return use_ipv6$LAYOUT;
    }

    private static final long use_ipv6$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * bool use_ipv6
     * }
     */
    public static final long use_ipv6$offset() {
        return use_ipv6$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * bool use_ipv6
     * }
     */
    public static boolean use_ipv6(MemorySegment struct) {
        return struct.get(use_ipv6$LAYOUT, use_ipv6$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * bool use_ipv6
     * }
     */
    public static void use_ipv6(MemorySegment struct, boolean fieldValue) {
        struct.set(use_ipv6$LAYOUT, use_ipv6$OFFSET, fieldValue);
    }

    private static final OfShort port$LAYOUT = (OfShort)$LAYOUT.select(groupElement("port"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static final OfShort port$layout() {
        return port$LAYOUT;
    }

    private static final long port$OFFSET = 2;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static final long port$offset() {
        return port$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static short port(MemorySegment struct) {
        return struct.get(port$LAYOUT, port$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint16_t port
     * }
     */
    public static void port(MemorySegment struct, short fieldValue) {
        struct.set(port$LAYOUT, port$OFFSET, fieldValue);
    }

    private static final OfInt state$LAYOUT = (OfInt)$LAYOUT.select(groupElement("state"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t state
     * }
     */
    public static final OfInt state$layout() {
        return state$LAYOUT;
    }

    private static final long state$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t state
     * }
     */
    public static final long state$offset() {
        return state$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t state
     * }
     */
    public static int state(MemorySegment struct) {
        return struct.get(state$LAYOUT, state$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t state
     * }
     */
    public static void state(MemorySegment struct, int fieldValue) {
        struct.set(state$LAYOUT, state$OFFSET, fieldValue);
    }

    private static final OfDouble last_packet_recieved_time$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("last_packet_recieved_time"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double last_packet_recieved_time
     * }
     */
    public static final OfDouble last_packet_recieved_time$layout() {
        return last_packet_recieved_time$LAYOUT;
    }

    private static final long last_packet_recieved_time$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double last_packet_recieved_time
     * }
     */
    public static final long last_packet_recieved_time$offset() {
        return last_packet_recieved_time$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double last_packet_recieved_time
     * }
     */
    public static double last_packet_recieved_time(MemorySegment struct) {
        return struct.get(last_packet_recieved_time$LAYOUT, last_packet_recieved_time$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double last_packet_recieved_time
     * }
     */
    public static void last_packet_recieved_time(MemorySegment struct, double fieldValue) {
        struct.set(last_packet_recieved_time$LAYOUT, last_packet_recieved_time$OFFSET, fieldValue);
    }

    private static final OfDouble last_packet_sent_time$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("last_packet_sent_time"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double last_packet_sent_time
     * }
     */
    public static final OfDouble last_packet_sent_time$layout() {
        return last_packet_sent_time$LAYOUT;
    }

    private static final long last_packet_sent_time$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double last_packet_sent_time
     * }
     */
    public static final long last_packet_sent_time$offset() {
        return last_packet_sent_time$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double last_packet_sent_time
     * }
     */
    public static double last_packet_sent_time(MemorySegment struct) {
        return struct.get(last_packet_sent_time$LAYOUT, last_packet_sent_time$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double last_packet_sent_time
     * }
     */
    public static void last_packet_sent_time(MemorySegment struct, double fieldValue) {
        struct.set(last_packet_sent_time$LAYOUT, last_packet_sent_time$OFFSET, fieldValue);
    }

    private static final OfLong application_id$LAYOUT = (OfLong)$LAYOUT.select(groupElement("application_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t application_id
     * }
     */
    public static final OfLong application_id$layout() {
        return application_id$LAYOUT;
    }

    private static final long application_id$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t application_id
     * }
     */
    public static final long application_id$offset() {
        return application_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t application_id
     * }
     */
    public static long application_id(MemorySegment struct) {
        return struct.get(application_id$LAYOUT, application_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t application_id
     * }
     */
    public static void application_id(MemorySegment struct, long fieldValue) {
        struct.set(application_id$LAYOUT, application_id$OFFSET, fieldValue);
    }

    private static final OfLong current_time$LAYOUT = (OfLong)$LAYOUT.select(groupElement("current_time"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t current_time
     * }
     */
    public static final OfLong current_time$layout() {
        return current_time$LAYOUT;
    }

    private static final long current_time$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t current_time
     * }
     */
    public static final long current_time$offset() {
        return current_time$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t current_time
     * }
     */
    public static long current_time(MemorySegment struct) {
        return struct.get(current_time$LAYOUT, current_time$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t current_time
     * }
     */
    public static void current_time(MemorySegment struct, long fieldValue) {
        struct.set(current_time$LAYOUT, current_time$OFFSET, fieldValue);
    }

    private static final OfLong client_id$LAYOUT = (OfLong)$LAYOUT.select(groupElement("client_id"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t client_id
     * }
     */
    public static final OfLong client_id$layout() {
        return client_id$LAYOUT;
    }

    private static final long client_id$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t client_id
     * }
     */
    public static final long client_id$offset() {
        return client_id$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t client_id
     * }
     */
    public static long client_id(MemorySegment struct) {
        return struct.get(client_id$LAYOUT, client_id$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t client_id
     * }
     */
    public static void client_id(MemorySegment struct, long fieldValue) {
        struct.set(client_id$LAYOUT, client_id$OFFSET, fieldValue);
    }

    private static final OfInt max_clients$LAYOUT = (OfInt)$LAYOUT.select(groupElement("max_clients"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int max_clients
     * }
     */
    public static final OfInt max_clients$layout() {
        return max_clients$LAYOUT;
    }

    private static final long max_clients$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int max_clients
     * }
     */
    public static final long max_clients$offset() {
        return max_clients$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int max_clients
     * }
     */
    public static int max_clients(MemorySegment struct) {
        return struct.get(max_clients$LAYOUT, max_clients$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int max_clients
     * }
     */
    public static void max_clients(MemorySegment struct, int fieldValue) {
        struct.set(max_clients$LAYOUT, max_clients$OFFSET, fieldValue);
    }

    private static final OfDouble connection_timeout$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("connection_timeout"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double connection_timeout
     * }
     */
    public static final OfDouble connection_timeout$layout() {
        return connection_timeout$LAYOUT;
    }

    private static final long connection_timeout$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double connection_timeout
     * }
     */
    public static final long connection_timeout$offset() {
        return connection_timeout$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double connection_timeout
     * }
     */
    public static double connection_timeout(MemorySegment struct) {
        return struct.get(connection_timeout$LAYOUT, connection_timeout$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double connection_timeout
     * }
     */
    public static void connection_timeout(MemorySegment struct, double fieldValue) {
        struct.set(connection_timeout$LAYOUT, connection_timeout$OFFSET, fieldValue);
    }

    private static final OfInt has_sent_disconnect_packets$LAYOUT = (OfInt)$LAYOUT.select(groupElement("has_sent_disconnect_packets"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int has_sent_disconnect_packets
     * }
     */
    public static final OfInt has_sent_disconnect_packets$layout() {
        return has_sent_disconnect_packets$LAYOUT;
    }

    private static final long has_sent_disconnect_packets$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int has_sent_disconnect_packets
     * }
     */
    public static final long has_sent_disconnect_packets$offset() {
        return has_sent_disconnect_packets$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int has_sent_disconnect_packets
     * }
     */
    public static int has_sent_disconnect_packets(MemorySegment struct) {
        return struct.get(has_sent_disconnect_packets$LAYOUT, has_sent_disconnect_packets$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int has_sent_disconnect_packets
     * }
     */
    public static void has_sent_disconnect_packets(MemorySegment struct, int fieldValue) {
        struct.set(has_sent_disconnect_packets$LAYOUT, has_sent_disconnect_packets$OFFSET, fieldValue);
    }

    private static final GroupLayout connect_token$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("connect_token"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_t connect_token
     * }
     */
    public static final GroupLayout connect_token$layout() {
        return connect_token$LAYOUT;
    }

    private static final long connect_token$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_t connect_token
     * }
     */
    public static final long connect_token$offset() {
        return connect_token$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_t connect_token
     * }
     */
    public static MemorySegment connect_token(MemorySegment struct) {
        return struct.asSlice(connect_token$OFFSET, connect_token$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_protocol_connect_token_t connect_token
     * }
     */
    public static void connect_token(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, connect_token$OFFSET, connect_token$LAYOUT.byteSize());
    }

    private static final OfLong challenge_nonce$LAYOUT = (OfLong)$LAYOUT.select(groupElement("challenge_nonce"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static final OfLong challenge_nonce$layout() {
        return challenge_nonce$LAYOUT;
    }

    private static final long challenge_nonce$OFFSET = 928;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static final long challenge_nonce$offset() {
        return challenge_nonce$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static long challenge_nonce(MemorySegment struct) {
        return struct.get(challenge_nonce$LAYOUT, challenge_nonce$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t challenge_nonce
     * }
     */
    public static void challenge_nonce(MemorySegment struct, long fieldValue) {
        struct.set(challenge_nonce$LAYOUT, challenge_nonce$OFFSET, fieldValue);
    }

    private static final SequenceLayout challenge_data$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("challenge_data"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static final SequenceLayout challenge_data$layout() {
        return challenge_data$LAYOUT;
    }

    private static final long challenge_data$OFFSET = 936;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static final long challenge_data$offset() {
        return challenge_data$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static MemorySegment challenge_data(MemorySegment struct) {
        return struct.asSlice(challenge_data$OFFSET, challenge_data$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static void challenge_data(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, challenge_data$OFFSET, challenge_data$LAYOUT.byteSize());
    }

    private static long[] challenge_data$DIMS = { 256 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static long[] challenge_data$dimensions() {
        return challenge_data$DIMS;
    }
    private static final VarHandle challenge_data$ELEM_HANDLE = challenge_data$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static byte challenge_data(MemorySegment struct, long index0) {
        return (byte)challenge_data$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * uint8_t challenge_data[256]
     * }
     */
    public static void challenge_data(MemorySegment struct, long index0, byte fieldValue) {
        challenge_data$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final OfInt goto_next_server$LAYOUT = (OfInt)$LAYOUT.select(groupElement("goto_next_server"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int goto_next_server
     * }
     */
    public static final OfInt goto_next_server$layout() {
        return goto_next_server$LAYOUT;
    }

    private static final long goto_next_server$OFFSET = 1192;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int goto_next_server
     * }
     */
    public static final long goto_next_server$offset() {
        return goto_next_server$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int goto_next_server
     * }
     */
    public static int goto_next_server(MemorySegment struct) {
        return struct.get(goto_next_server$LAYOUT, goto_next_server$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int goto_next_server
     * }
     */
    public static void goto_next_server(MemorySegment struct, int fieldValue) {
        struct.set(goto_next_server$LAYOUT, goto_next_server$OFFSET, fieldValue);
    }

    private static final OfInt goto_next_server_tentative_state$LAYOUT = (OfInt)$LAYOUT.select(groupElement("goto_next_server_tentative_state"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t goto_next_server_tentative_state
     * }
     */
    public static final OfInt goto_next_server_tentative_state$layout() {
        return goto_next_server_tentative_state$LAYOUT;
    }

    private static final long goto_next_server_tentative_state$OFFSET = 1196;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t goto_next_server_tentative_state
     * }
     */
    public static final long goto_next_server_tentative_state$offset() {
        return goto_next_server_tentative_state$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t goto_next_server_tentative_state
     * }
     */
    public static int goto_next_server_tentative_state(MemorySegment struct) {
        return struct.get(goto_next_server_tentative_state$LAYOUT, goto_next_server_tentative_state$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_protocol_client_state_t goto_next_server_tentative_state
     * }
     */
    public static void goto_next_server_tentative_state(MemorySegment struct, int fieldValue) {
        struct.set(goto_next_server_tentative_state$LAYOUT, goto_next_server_tentative_state$OFFSET, fieldValue);
    }

    private static final OfInt server_endpoint_index$LAYOUT = (OfInt)$LAYOUT.select(groupElement("server_endpoint_index"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int server_endpoint_index
     * }
     */
    public static final OfInt server_endpoint_index$layout() {
        return server_endpoint_index$LAYOUT;
    }

    private static final long server_endpoint_index$OFFSET = 1200;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int server_endpoint_index
     * }
     */
    public static final long server_endpoint_index$offset() {
        return server_endpoint_index$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int server_endpoint_index
     * }
     */
    public static int server_endpoint_index(MemorySegment struct) {
        return struct.get(server_endpoint_index$LAYOUT, server_endpoint_index$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int server_endpoint_index
     * }
     */
    public static void server_endpoint_index(MemorySegment struct, int fieldValue) {
        struct.set(server_endpoint_index$LAYOUT, server_endpoint_index$OFFSET, fieldValue);
    }

    private static final GroupLayout web_service_endpoint$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("web_service_endpoint"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_endpoint_t web_service_endpoint
     * }
     */
    public static final GroupLayout web_service_endpoint$layout() {
        return web_service_endpoint$LAYOUT;
    }

    private static final long web_service_endpoint$OFFSET = 1204;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_endpoint_t web_service_endpoint
     * }
     */
    public static final long web_service_endpoint$offset() {
        return web_service_endpoint$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_endpoint_t web_service_endpoint
     * }
     */
    public static MemorySegment web_service_endpoint(MemorySegment struct) {
        return struct.asSlice(web_service_endpoint$OFFSET, web_service_endpoint$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_endpoint_t web_service_endpoint
     * }
     */
    public static void web_service_endpoint(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, web_service_endpoint$OFFSET, web_service_endpoint$LAYOUT.byteSize());
    }

    private static final GroupLayout socket$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("socket"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_socket_t socket
     * }
     */
    public static final GroupLayout socket$layout() {
        return socket$LAYOUT;
    }

    private static final long socket$OFFSET = 1228;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_socket_t socket
     * }
     */
    public static final long socket$offset() {
        return socket$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_socket_t socket
     * }
     */
    public static MemorySegment socket(MemorySegment struct) {
        return struct.asSlice(socket$OFFSET, socket$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_socket_t socket
     * }
     */
    public static void socket(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, socket$OFFSET, socket$LAYOUT.byteSize());
    }

    private static final OfLong sequence$LAYOUT = (OfLong)$LAYOUT.select(groupElement("sequence"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint64_t sequence
     * }
     */
    public static final OfLong sequence$layout() {
        return sequence$LAYOUT;
    }

    private static final long sequence$OFFSET = 1256;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint64_t sequence
     * }
     */
    public static final long sequence$offset() {
        return sequence$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint64_t sequence
     * }
     */
    public static long sequence(MemorySegment struct) {
        return struct.get(sequence$LAYOUT, sequence$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint64_t sequence
     * }
     */
    public static void sequence(MemorySegment struct, long fieldValue) {
        struct.set(sequence$LAYOUT, sequence$OFFSET, fieldValue);
    }

    private static final GroupLayout packet_queue$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("packet_queue"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_circular_buffer_t packet_queue
     * }
     */
    public static final GroupLayout packet_queue$layout() {
        return packet_queue$LAYOUT;
    }

    private static final long packet_queue$OFFSET = 1264;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_circular_buffer_t packet_queue
     * }
     */
    public static final long packet_queue$offset() {
        return packet_queue$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_circular_buffer_t packet_queue
     * }
     */
    public static MemorySegment packet_queue(MemorySegment struct) {
        return struct.asSlice(packet_queue$OFFSET, packet_queue$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_circular_buffer_t packet_queue
     * }
     */
    public static void packet_queue(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, packet_queue$OFFSET, packet_queue$LAYOUT.byteSize());
    }

    private static final GroupLayout replay_buffer$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("replay_buffer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_protocol_replay_buffer_t replay_buffer
     * }
     */
    public static final GroupLayout replay_buffer$layout() {
        return replay_buffer$LAYOUT;
    }

    private static final long replay_buffer$OFFSET = 1296;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_protocol_replay_buffer_t replay_buffer
     * }
     */
    public static final long replay_buffer$offset() {
        return replay_buffer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_protocol_replay_buffer_t replay_buffer
     * }
     */
    public static MemorySegment replay_buffer(MemorySegment struct) {
        return struct.asSlice(replay_buffer$OFFSET, replay_buffer$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_protocol_replay_buffer_t replay_buffer
     * }
     */
    public static void replay_buffer(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, replay_buffer$OFFSET, replay_buffer$LAYOUT.byteSize());
    }

    private static final AddressLayout sim$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("sim"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * cn_simulator_t *sim
     * }
     */
    public static final AddressLayout sim$layout() {
        return sim$LAYOUT;
    }

    private static final long sim$OFFSET = 3352;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * cn_simulator_t *sim
     * }
     */
    public static final long sim$offset() {
        return sim$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * cn_simulator_t *sim
     * }
     */
    public static MemorySegment sim(MemorySegment struct) {
        return struct.get(sim$LAYOUT, sim$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * cn_simulator_t *sim
     * }
     */
    public static void sim(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(sim$LAYOUT, sim$OFFSET, fieldValue);
    }

    private static final SequenceLayout buffer$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("buffer"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static final SequenceLayout buffer$layout() {
        return buffer$LAYOUT;
    }

    private static final long buffer$OFFSET = 3360;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static final long buffer$offset() {
        return buffer$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static MemorySegment buffer(MemorySegment struct) {
        return struct.asSlice(buffer$OFFSET, buffer$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static void buffer(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, buffer$OFFSET, buffer$LAYOUT.byteSize());
    }

    private static long[] buffer$DIMS = { 1280 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static long[] buffer$dimensions() {
        return buffer$DIMS;
    }
    private static final VarHandle buffer$ELEM_HANDLE = buffer$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static byte buffer(MemorySegment struct, long index0) {
        return (byte)buffer$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * uint8_t buffer[1280]
     * }
     */
    public static void buffer(MemorySegment struct, long index0, byte fieldValue) {
        buffer$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final SequenceLayout connect_token_packet$LAYOUT = (SequenceLayout)$LAYOUT.select(groupElement("connect_token_packet"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static final SequenceLayout connect_token_packet$layout() {
        return connect_token_packet$LAYOUT;
    }

    private static final long connect_token_packet$OFFSET = 4640;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static final long connect_token_packet$offset() {
        return connect_token_packet$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static MemorySegment connect_token_packet(MemorySegment struct) {
        return struct.asSlice(connect_token_packet$OFFSET, connect_token_packet$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static void connect_token_packet(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, connect_token_packet$OFFSET, connect_token_packet$LAYOUT.byteSize());
    }

    private static long[] connect_token_packet$DIMS = { 1024 };

    /**
     * Dimensions for array field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static long[] connect_token_packet$dimensions() {
        return connect_token_packet$DIMS;
    }
    private static final VarHandle connect_token_packet$ELEM_HANDLE = connect_token_packet$LAYOUT.varHandle(sequenceElement());

    /**
     * Indexed getter for field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static byte connect_token_packet(MemorySegment struct, long index0) {
        return (byte)connect_token_packet$ELEM_HANDLE.get(struct, 0L, index0);
    }

    /**
     * Indexed setter for field:
     * {@snippet lang=c :
     * uint8_t connect_token_packet[1024]
     * }
     */
    public static void connect_token_packet(MemorySegment struct, long index0, byte fieldValue) {
        connect_token_packet$ELEM_HANDLE.set(struct, 0L, index0, fieldValue);
    }

    private static final AddressLayout mem_ctx$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("mem_ctx"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static final AddressLayout mem_ctx$layout() {
        return mem_ctx$LAYOUT;
    }

    private static final long mem_ctx$OFFSET = 5664;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static final long mem_ctx$offset() {
        return mem_ctx$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static MemorySegment mem_ctx(MemorySegment struct) {
        return struct.get(mem_ctx$LAYOUT, mem_ctx$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *mem_ctx
     * }
     */
    public static void mem_ctx(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(mem_ctx$LAYOUT, mem_ctx$OFFSET, fieldValue);
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

