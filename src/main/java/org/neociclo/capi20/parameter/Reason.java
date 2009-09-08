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

import static java.lang.String.format;

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
            return format("%s(cause: 0x%02X, desc: %s)", name(), networkCause, NetworkCause.getDescription(networkCause & 0xff));
        } else {
            return name();
        }
    }

}
