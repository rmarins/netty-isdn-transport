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

import org.neociclo.capi20.util.IBitType;

/**
 * @author Rafael Marins
 */
public enum InformationType implements IBitType {

    /** Cause information given by the network during disconnection. */
    CAUSE(0),

    /** Date/time information indicated by the network. */
    DATETIME(1),

    /**
     * Information to be displayed to the user.
     */
    DISPLAY(2),

    /** User-user information that is carried transparently by the network. */
    USERUSER_INFORMATION(3),

    /**
     * Information regarding to the progress of the call. There are five
     * different <b>INFO_IND</b> messages that correspond to this information
     * type, each with a unique info number.
     * <p/>
     * The other four messages indicate the occurrence of the network events
     * <b>SETUP ACKNOWLEDGE</b>, <b>CALL PROCEEDING</b>, <b>ALERTING</b> and
     * <code>PROGRESS</code>. In these cases, the Info number parameInfo element
     * is an empty <b>COMMON-ISDN-API</b> struct.
     */
    CALL_PROGRESS(4),

    /**
     * Facility information to indicate the invocation and operation of
     * supplementary services.
     */
    FACILITY(5),

    /**
     * Connection-oriented charge information provided by the network. There are
     * two different <b>INFO_IND</b> messages, with unique Info number values,
     * that correspond to this information type. The first shows the total
     * charge units indicated by the network up to this moment; the second shows
     * the total charges in the national currency indicated by the network up to
     * this moment. In both cases, the Info element parameter is coded as a
     * <b>COMMON-ISDN-API</b> struct containing a dword. It is highly
     * recommended that only one of these two types of charge information be
     * supplied to the user, and that the application convert one type to the
     * other. However, in some networks this might be impossible due to
     * ambiguous information provided by the network. In such cases it is not
     * defined whether the current charges are represented by only one or by
     * both types of information, or by the sum of the two.
     */
    CHARGING(6),

    /** Identifies the destination of a call. */
    CALLED_PARTY_NUMBER(7),

    /** Identifies the used channel of a call. */
    CHANNEL_INFORMATION(8),

    /**
     * Enables 'early B3 connect' (see note). When this bit is set to 1, a
     * B-channel connection (NCCI) may be established based on a D-channel
     * connection (PLCI) which has not yet been established. Additional
     * information regarding the progress of the call is sent to the
     * application. There are two different <b>INFO_IND</b> messages that
     * correspond to this information type, each with a unique info number:
     * <p/>
     * The first <b>INFO_IND</b> contains the progress indicator information
     * element as defined in <b>ETS 300 102-1</b> and <b>Q.931</b>. The other
     * indicates the occurrence of the network event <code>DISC</code>. In this
     * case, the Info number parameter indicates the corresponding event and the
     * Info element is an empty <b>COMMON-ISDN-API</b> struct.
     */
    EARLY_B3_CONNECT(9),

    /**
     * Redirecting/redirection information indicated by the network.
     */
    REDIRECTION_INFORMATION(10),

    /**
     * Indicates the completion of the called party number. The <i>sending
     * complete</i> information element may be sent by the network after
     * completion of the called party number; this can be helpful especially for
     * the overlap receiving (direct dial in, DDI) case, where the called party
     * number may be spread over multiple <b>INFO_IND</b> messages containing
     * the called party number information element (see bit 7).
     */
    SENDING_COMPLETE(12);

    private byte bitField;

    private InformationType(int bitPos) {
        this.bitField = (byte) (bitPos & 0xFF);
    }

    public int getBitField() {
        return (bitField & 0xFF);
    }

}
