/**
 * The Accord Project, http://accordproject.org
 * Copyright (C) 2005-2013 Rafael Marins, http://rafaelmarins.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neociclo.capi20.message;

/**
 * Definition of the CAPI message types following the <i>command</i> and
 * <i>subCommand</i> codes.
 * <p/>
 * Messages concerning the signaling protocol:
 * 
 * <pre>
 * CONNECT_REQ, CONNECT_CONF, CONNECT_IND, CONNECT_RESP, CONNECT_ACTIVE_IND,
 * CONNECT_ACTIVE_RESP, DISCONNECT_REQ, DISCONNECT_CONF, DISCONNECT_IND,
 * DISCONNECT_RESP, ALERT_REQ, ALERT_CONF, INFO_REQ, INFO_CONF, INFO_IND,
 * INFO_RESP.
 * </pre>
 * 
 * Messages concerning logical connections:
 * 
 * <pre>
 * CONNECT_B3_REQ, CONNECT_B3_CONF, CONNECT_B3_IND, CONNECT_B3_RESP,
 * CONNECT_B3_ACTIVE_IND, CONNECT_B3_ACTIVE_RESP, CONNECT_B3_T90_ACTIVE_IND,
 * CONNECT_B3_T90_ACTIVE_RESP, DISCONNECT_B3_REQ, DISCONNECT_B3_CONF,
 * DISCONNECT_B3_IND, DISCONNECT_B3_RESP, DATA_B3_REQ, DATA_B3_CONF,
 * DATA_B3_IND, DATA_B3_RESP, RESET_B3_REQ, RESET_B3_CONF, RESET_B3_IND,
 * RESET_B3_RESP.
 * </pre>
 * 
 * Administrative and other messages:
 * 
 * <pre>
 * LISTEN_REQ, LISTEN_CONF, FACILITY_REQ, FACILITY_CONF, FACILITY_IND,
 * FACILITY_RESP, SELECT_B_PROTOCOL_REQ, SELECT_B_PROTOCOL_CONF,
 * MANUFACTURER_REQ, MANUFACTURER_CONF, MANUFACTURER_IND, MANUFACTURER_RESP.
 * </pre>
 * 
 * @author Rafael Marins
 */
public enum MessageType {

    /** Signaling protocol: initiates an outgoing physical connection */
    CONNECT_REQ(0x02, 0x80),

    /** Signaling protocol: local confirmation of the request */
    CONNECT_CONF(0x02, 0x81),

    /** Signaling protocol: indicates an incoming physical connection */
    CONNECT_IND(0x02, 0x82),

    /** Signaling protocol: response to the indication */
    CONNECT_RESP(0x02, 0x83),

    /** Signaling protocol: indicates the activation of a physical connection */
    CONNECT_ACTIVE_IND(0x03, 0x82),

    /** Signaling protocol: response to the indication */
    CONNECT_ACTIVE_RESP(0x03, 0x83),

    /** Signaling protocol: initiates clearing down of a physical connection */
    DISCONNECT_REQ(0x04, 0x80),

    /** Signaling protocol: local confirmation of the request */
    DISCONNECT_CONF(0x04, 0x81),

    /** Signaling protocol: indicates the clearing of a physical connection */
    DISCONNECT_IND(0x04, 0x82),

    /** Signaling protocol: response to the indication */
    DISCONNECT_RESP(0x04, 0x83),

    /**
     * Signaling protocol: initiates sending of ALERT, i.e. compatibility with
     * call
     */
    ALERT_REQ(0x01, 0x80),

    /** Signaling protocol: local confirmation of the request */
    ALERT_CONF(0x01, 0x81),

    /** Signaling protocol: initiates sending of signaling information */
    INFO_REQ(0x08, 0x80),

    /** Signaling protocol: local confirmation of the request */
    INFO_CONF(0x08, 0x81),

    /** Signaling protocol: indicates specified signaling information */
    INFO_IND(0x08, 0x82),

    /** Signaling protocol: response to the indication */
    INFO_RESP(0x08, 0x83),

    /** Logical connections: initiates an outgoing logical connection */
    CONNECT_B3_REQ(0x82, 0x80),

    /** Logical connections: local confirmation of the request */
    CONNECT_B3_CONF(0x82, 0x81),

    /** Logical connections: indicates an incoming logical connection */
    CONNECT_B3_IND(0x82, 0x82),

    /** Logical connections: response to the indication */
    CONNECT_B3_RESP(0x82, 0x83),

    /** Logical connections: indicates the activation of a logical connection */
    CONNECT_B3_ACTIVE_IND(0x83, 0x82),

    /** Logical connections: response to the indication */
    CONNECT_B3_ACTIVE_RESP(0x83, 0x83),

    /** Logical connections: indicates switching from T.70NL to T.90NL */
    CONNECT_B3_T90_ACTIVE_IND(0x88, 0x82),

    /** Logical connections: response to the indication */
    CONNECT_B3_T90_ACTIVE_RESP(0x88, 0x83),

    /** Logical connections: initiates clearing down of a logical connection */
    DISCONNECT_B3_REQ(0x84, 0x80),

    /** Logical connections: local confirmation of the request */
    DISCONNECT_B3_CONF(0x84, 0x81),

    /** Logical connections: indicates the clearing down of a logical connection */
    DISCONNECT_B3_IND(0x84, 0x82),

    /** Logical connections: response to the indication */
    DISCONNECT_B3_RESP(0x84, 0x83),

    /** Logical connections: initiates sending of data over a logical connection */
    DATA_B3_REQ(0x86, 0x80),

    /** Logical connections: local confirmation of the request */
    DATA_B3_CONF(0x86, 0x81),

    /** Logical connections: indicates incoming data over a logical connection */
    DATA_B3_IND(0x86, 0x82),

    /** Logical connections: response to the indication */
    DATA_B3_RESP(0x86, 0x83),

    /** Logical connections: initiates the resetting of a logical connection */
    RESET_B3_REQ(0x87, 0x80),

    /** Logical connections: local confirmation of the request */
    RESET_B3_CONF(0x87, 0x81),

    /** Logical connections: indicates the resetting of a logical connection */
    RESET_B3_IND(0x87, 0x82),

    /** Logical connections: response to the indication */
    RESET_B3_RESP(0x87, 0x83),

    /** Administrative and others: activates call and info indications */
    LISTEN_REQ(0x05, 0x80),

    /** Administrative and others: local confirmation of the request */
    LISTEN_CONF(0x05, 0x81),

    /**
     * Administrative and others: requests additional facilities (e.g. ext.
     * equipment)
     */
    FACILITY_REQ(0x80, 0x80),

    /** Administrative and others: local confirmation of the request */
    FACILITY_CONF(0x80, 0x81),

    /**
     * Administrative and others: indicates additional facilities (e.g. ext.
     * equipment)
     */
    FACILITY_IND(0x80, 0x82),

    /** Administrative and others: response to the indication */
    FACILITY_RESP(0x80, 0x83),

    /**
     * Administrative and others: selects protocol stack used for a logical
     * connection
     */
    SELECT_B_PROTOCOL_REQ(0x41, 0x80),

    /** Administrative and others: local confirmation of the request */
    SELECT_B_PROTOCOL_CONF(0x41, 0x81),

    /** Administrative and others: manufacturer-specific operation */
    MANUFACTURER_REQ(0xFF, 0x80),

    /** Administrative and others: manufacturer-specific operation */
    MANUFACTURER_CONF(0xFF, 0x81),

    /** Administrative and others: manufacturer-specific operation */
    MANUFACTURER_IND(0xFF, 0x82),

    /** Administrative and others: manufacturer-specific operation */
    MANUFACTURER_RESP(0xFF, 0x83);

    public static MessageType valueOf(byte command, byte subCommand) {
        for (MessageType t : MessageType.values()) {
            if (t.getCommand() == command && t.getSubCommand() == subCommand) {
                return t;
            }
        }
        return null;
    }

    private byte command;
    private byte subCommand;

    private MessageType(int command, int subCommand) {
        this.command = (byte) (command & 0xff);
        this.subCommand = (byte) (subCommand & 0xff);
    }

    public byte getCommand() {
        return command;
    }

    public byte getSubCommand() {
        return subCommand;
    }

    public int intValue() {
        return (((command & 0xff) << 8) | (subCommand & 0xff));
    }

}
