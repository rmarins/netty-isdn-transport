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
package org.neociclo.capi20.parameter;

/**
 * The purpose of the parameter Info is to provide error information to the
 * application. A unique code is defined for each error which can be detected by
 * the controller. This code is independent of the error context.
 * <p/>
 * <b>COMMON-ISDN-API</b> shall not generate other information values than those
 * defined below. In case additional information values are defined in future,
 * however, an application should interpret any information value except class
 * 0x00xx as an indication that the corresponding request was rejected by
 * <b>COMMON-ISDN-API</b>. <i>Class 0x00xx</i> indicates successful handling of
 * the corresponding request and returns additional information.
 * 
 * <ul>
 * <li><b>Class 0x00xx:</b> Informative values (the corresponding request
 * message was processed)</li>
 * <li><b>Class 0x10xx:</b> Error information concerning CAPI_REGISTER</i>
 * <li><b>Class 0x11xx:</b> Error information concerning message exchange
 * functions</li>
 * <li><b>Class 0x20xx:</b> Error information concerning resource/coding
 * problems</li>
 * <li><b>Class 0x30xx:</b> Error information concerning requested services</li>
 * </ul>
 * 
 * This information element appears in:
 * 
 * <pre>
 * CONNECT_B3_CONF 
 * CONNECT_CONF 
 * INFO_CONF 
 * DATA_B3_CONF 
 * DISCONNECT_B3_CONF 
 * DISCONNECT_CONF 
 * LISTEN_CONF 
 * RESET_B3_CONF 
 * SELECT_B_PROTOCOL_CONF
 * </pre>
 * 
 * @author Rafael Marins
 */
public enum Info {

    REQUEST_ACCEPTED(0x0000),
    /** NCPI not supported by current protocol, NCPI ignored */
    NCPI_NOT_SUPPORTED(0x0001),
    /** Flags not supported by current protocol, flags ignored */
    FLAGS_NOT_SUPPORTED(0x0002),
    /** Alert already sent by another application */
    ALERT_ALREADY_SENT(0x0003),

    /** Too many applications */
    REGISTER_TOO_MANY_APPLICATIONS (0x1001),
    /** Logical block size too small; must be at least 128 bytes */
    REGISTER_LOGICAL_BLOCK_SIZE_TOO_SMALL (0x1002),
    /** Buffer exceeds 64 kbytes */
    REGISTER_BUFFER_EXCEEDED (0x1003),
    /** Message buffer size too small, must be at least 1024 bytes */
    REGISTER_MESSAGE_BUFFER_SIZE_TOO_SMALL (0x1004),
    /** Max. number of logical connections not supported */
    REGISTER_ILLEGAL_MAX_LOGICAL_CONNECTION (0x1005),
    /** The message could not be accepted because of an internal busy condition */
    REGISTER_INTERNAL_BUSY_CONDITION (0x1007),
    /** OS resource error (e.g. no memory) */
    REGISTER_UNAVAILABLE_RESOURCES_ERROR (0x1008),
    /** COMMON-ISDN-API not installed */
    REGISTER_CAPI_NOT_INSTALLED (0x1009),
    /** Controller does not support external equipment */
    REGISTER_EXTERNAL_EQUIPMENT_NOT_SUPPORTED (0x100a),
    /** Controller does only support external equipment */
    REGISTER_EXTERNAL_EQUIPMENT_REQUIRED (0x100b),

    /** Illegal application number. */
    EXCHANGE_ILLEGAL_APPLICATION_NUMBER (0x1101),
    /** Illegal command or subcommand, or message length less than 12 octets. */
    EXCHANGE_ILLEGAL_COMMAND (0x1102),
    /**
     * The message could not be accepted because of a queue full condition. The error 
     * code does not imply that COMMON-ISDN-API cannot receive messages directed to 
     * another controller, PLCI or NCCI.
     */
    EXCHANGE_QUEUE_FULL (0x1103),
    /** Queue is empty. */
    EXCHANGE_QUEUE_EMPTY (0x1104),
    /**
     * Queue overflow: a message was lost. This indicates a configuration error. The only 
     * recovery from this error is to do the CAPI_RELEASE operation.
     */
    EXCHANGE_QUEUE_OVERFLOW (0x1105),
    /** Unknown notification parameter. */
    EXCHANGE_UNKOWN_NOTIFICATION_PARAM (0x1106),
    /** The message could not be accepted because of an internal busy condition. */
    EXCHANGE_INTERNAL_BUSY (0x1107),
    /** OS resource error (e.g. no memory). */
    EXCHANGE_RESOURCE_ERROR (0x1108),
    /** COMMON-ISDN-API not installed. */
    EXCHANGE_CAPI_NOT_INSTALLED (0x1109),
    /** Controller does not support external equipment. */
    EXCHANGE_EXTERNAL_EQUIPMENT_NOT_SUPPORTED (0x110a),
    /** Controller supports only external equipment. */
    EXCHANGE_EXTERNAL_EQUIPMENT_REQUIRED (0x110b),

    /** Message not supported in current state. */
    MESSAGE_NOT_SUPPORTED (0x2001),
    /** Illegal Controller/PLCI/NCCI. */
    ILLEGAL_CONTROLLER_PLCI_NCCI (0x2002),
    /** No PLCI available. */
    NO_PLCI_AVAILABLE (0x2003),
    /** No NCCI available. */
    NO_NCCI_AVAILABLE (0x2004),
    /** No Listen resources available. */
    NO_LISTEN_RESOURCES_AVAILABLE (0x2005),
    /** No fax resources available (protocol T.30). */
    NO_FAX_RESOURCES_AVAILABLE (0x2006),
    /** Illegal message parameter coding. */
    ILLEGAL_MESSAGE_PARAMETER_CODING (0x2007),
    /** No interconnection resources available. */
    NO_INTERCONNECTION_RESOURCES_AVAILABLE (0x2008)
    ;

    private static final int CLASS_TYPE_BIT_MASK = 0xFF00;

    public static Info valueOf(int value) {
        return valueOf((short) (value & 0xffff));
    }

    public static Info valueOf(short value) {
        Info found = null;
        for (Info i : Info.values()) {
            if (i.getCode() == value) {
                found = i;
                break;
            }
        }
        return found;
    }

    private short code;

    private Info(int code) {
        this.code = (short) (code & 0xffff);
    }

    public short getCode() {
        return code;
    }

    public boolean isClassZero() {
        return ((getCode() & CLASS_TYPE_BIT_MASK) == 0x0000);
    }

    public boolean isClassTen() {
        return ((getCode() & CLASS_TYPE_BIT_MASK) == 0x1000);
    }

    public boolean isClassEleven() {
        return ((getCode() & CLASS_TYPE_BIT_MASK) == 0x1100);
    }

    public boolean isClassTwenty() {
        return ((getCode() & CLASS_TYPE_BIT_MASK) == 0x2000);
    }

    public boolean isClassThirty() {
        return ((getCode() & CLASS_TYPE_BIT_MASK) == 0x3000);
    }

}
