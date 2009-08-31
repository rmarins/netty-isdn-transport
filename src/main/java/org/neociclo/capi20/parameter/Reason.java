/**
 * Neociclo Accord, Open Source B2Bi Middleware
 * Copyright (C) 2005-2009 Neociclo, http://www.neociclo.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id$
 */
package org.neociclo.capi20.parameter;

import static java.lang.String.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public final class Reason {

    /** Normal clearing, no cause available */
    public static final Reason NORMAL_CLEARING = new Reason(0x00, "NORMAL_CLEARING");

    /** Protocol error, Layer 1 */
    public static final Reason PROTOCOL_ERROR_L1 = new Reason(0x3301, "PROTOCOL_ERROR_L1");

    /** Protocol error, Layer 2 */
    public static final Reason PROTOCOL_ERROR_L2 = new Reason(0x3302, "PROTOCOL_ERROR_L2");

    /** Protocol error, Layer 3 */
    public static final Reason PROTOCOL_ERROR_L3 = new Reason(0x3303, "PROTOCOL_ERROR_L3");

    /** The call was given to another application (see LISTEN_REQ) */
    public static final Reason CALL_GIVEN_TO_ANOTHER_APPLICATION = new Reason(0x3304,
            "CALL_GIVEN_TO_ANOTHER_APPLICATION");

    /** Cleared by Call Control Supervision (see Annex D.2) */
    public static final Reason CLEARED_BY_CALL_CONTROL_SUPERVISION = new Reason(0x3305,
            "CLEARED_BY_CALL_CONTROL_SUPERVISION");

    /**
     * Disconnect cause from the network in accordance with ETS 300 102-1 /
     * Q.850. The field 'xx' indicates the cause value received from the network
     * in a cause in- formation element (Octet 4).
     */
    public static final String NETWORK_DISCONNECT = "NETWORK_DISCONNECT";

    private static final Reason[] VALUES = { NORMAL_CLEARING, PROTOCOL_ERROR_L1, PROTOCOL_ERROR_L2, PROTOCOL_ERROR_L3,
            CALL_GIVEN_TO_ANOTHER_APPLICATION, CLEARED_BY_CALL_CONTROL_SUPERVISION };

    private static final Map<Integer, String> NETWORK_CAUSE = new HashMap<Integer, String>();

    static {
        NETWORK_CAUSE.put(0X00, "NORMAL TERMINATION, NO REASON AVAILABLE");
        NETWORK_CAUSE.put(0X80, "NORMAL TERMINATION");
        NETWORK_CAUSE.put(0X81, "UNALLOCATED (UNASSIGNED) NUMBER");
        NETWORK_CAUSE.put(0X82, "NO ROUTE TO SPECIFIED TRANSIT NETWORK");
        NETWORK_CAUSE.put(0X83, "NO ROUTE TO DESTINATION");
        NETWORK_CAUSE.put(0X86, "CHANNEL UNACCEPTABLE");
        NETWORK_CAUSE.put(0X87, "CALL AWARDED AND BEING DELIVERED IN AN ESTABLISHED CHANNEL");
        NETWORK_CAUSE.put(0X90, "NORMAL CALL CLEARING");
        NETWORK_CAUSE.put(0X91, "USER BUSY");
        NETWORK_CAUSE.put(0X92, "NO USER RESPONDING");
        NETWORK_CAUSE.put(0X93, "NO ANSWER FROM USER (USER ALERTED)");
        NETWORK_CAUSE.put(0X95, "CALL REJECTED");
        NETWORK_CAUSE.put(0X96, "NUMBER CHANGED");
        NETWORK_CAUSE.put(0X9A, "NON-SELECTED USER CLEARING");
        NETWORK_CAUSE.put(0X9B, "DESTINATION OUT OF ORDER");
        NETWORK_CAUSE.put(0X9C, "INVALID NUMBER FORMAT");
        NETWORK_CAUSE.put(0X9D, "FACILITY REJECTED");
        NETWORK_CAUSE.put(0X9E, "RESPONSE TO STATUS ENQUIRY");
        NETWORK_CAUSE.put(0X9F, "NORMAL, UNSPECIFIED");
        NETWORK_CAUSE.put(0XA2, "NO CIRCUIT / CHANNEL AVAILABLE");
        NETWORK_CAUSE.put(0XA6, "NETWORK OUT OF ORDER");
        NETWORK_CAUSE.put(0XA9, "TEMPORARY FAILURE");
        NETWORK_CAUSE.put(0XAA, "SWITCHING EQUIPMENT CONGESTION");
        NETWORK_CAUSE.put(0XAB, "ACCESS INFORMATION DISCARDED");
        NETWORK_CAUSE.put(0XAC, "REQUESTED CIRCUIT / CHANNEL NOT AVAILABLE");
        NETWORK_CAUSE.put(0XAF, "RESOURCES UNAVAILABLE, UNSPECIFIED");
        NETWORK_CAUSE.put(0XB1, "QUALITY OF SERVICE UNAVAILABLE");
        NETWORK_CAUSE.put(0XB2, "REQUESTED FACILITY NOT SUBSCRIBED");
        NETWORK_CAUSE.put(0XB9, "BEARER CAPABILITY NOT AUTHORIZED");
        NETWORK_CAUSE.put(0XBA, "BEARER CAPABILITY NOT PRESENTLY AVAILABLE");
        NETWORK_CAUSE.put(0XBF, "SERVICE OR OPTION NOT AVAILABLE, UNSPECIFIED");
        NETWORK_CAUSE.put(0XC1, "BEARER CAPABILITY NOT IMPLEMENTED");
        NETWORK_CAUSE.put(0XC2, "CHANNEL TYPE NOT IMPLEMENTED");
        NETWORK_CAUSE.put(0XC5, "REQUESTED FACILITY NOT IMPLEMENTED");
        NETWORK_CAUSE.put(0XC6, "ONLY RESTRICTED DIGITAL INFORMATION BEARER CAPABILITY IS AVAILABLE");
        NETWORK_CAUSE.put(0XCF, "SERVICE OR OPTION NOT IMPLEMENTED, UNSPECIFIED");
        NETWORK_CAUSE.put(0XD1, "INVALID CALL REFERENCE VALUE");
        NETWORK_CAUSE.put(0XD2, "IDENTIFIED CHANNEL DOES NOT EXIST");
        NETWORK_CAUSE.put(0XD3, "A SUSPENDED CALL EXISTS, BUT THIS CALL IDENTITY DOES NOT");
        NETWORK_CAUSE.put(0XD4, "CALL IDENTITY IN USE");
        NETWORK_CAUSE.put(0XD5, "NO CALL SUSPENDED");
        NETWORK_CAUSE.put(0XD6, "CALL HAVING THE REQUESTED CALL IDENTITY HAS BEEN CLEARED");
        NETWORK_CAUSE.put(0XD8, "INCOMPATIBLE DESTINATION");
        NETWORK_CAUSE.put(0XDB, "INVALID TRANSIT NETWORK SELECTION");
        NETWORK_CAUSE.put(0XDF, "INVALID MESSAGE, UNSPECIFIED");
        NETWORK_CAUSE.put(0XE0, "MANDATORY INFORMATION ELEMENT IS MISSING");
        NETWORK_CAUSE.put(0XE1, "MESSAGE TYPE NON-EXISTENT OR NOT IMPLEMENTED");
        NETWORK_CAUSE.put(0XE2, "MESSAGE NOT COMPATIBLE WITH CALL STATE OR MESSAGE TYPE NON-EXISTENT OR NOT IMPLEMENTED");
        NETWORK_CAUSE.put(0XE3, "INFORMATION ELEMENT NON-EXISTENT OR NOT IMPLEMENTED");
        NETWORK_CAUSE.put(0XE4, "INVALID INFORMATION ELEMENT CONTENTS");
        NETWORK_CAUSE.put(0XE5, "MESSAGE NOT COMPATIBLE WITH CALL STATE");
        NETWORK_CAUSE.put(0XE6, "RECOVERY ON TIMER EXPIRY");
        NETWORK_CAUSE.put(0XEF, "PROTOCOL ERROR, UNSPECIFIED");
        NETWORK_CAUSE.put(0XFF, "INTERWORKING, UNSPECIFIED");
    }

    public static Reason valueOf(int reasonCoded) {
        short codeShort = (short) (reasonCoded & 0xffff);
        if ((codeShort & 0xff00) == 0x3400) {
            return new Reason(codeShort, NETWORK_DISCONNECT);
        }
        for (Reason r : VALUES) {
            if (r.getCode() == codeShort) {
                return r;
            }
        }
        return null;
    }

    private short code;
    private String name;

    private Reason(int code, String name) {
        this.code = (short) (code & 0xffff);
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public short getNetworkCause() {
        if ((code & 0xff00) == 0x3400) {
            return (short) (code & 0xff);
        }
        return 0;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        short networkCause = getNetworkCause();
        if (networkCause > 0) {
            return format("%s(cause: 0x%02X, desc: %s)", name(), networkCause, NETWORK_CAUSE.get(networkCause & 0xff));
        } else {
            return name();
        }
    }

}
