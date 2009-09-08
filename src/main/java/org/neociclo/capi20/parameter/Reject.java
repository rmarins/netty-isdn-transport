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
public final class Reject {

    /** Accept the call. */
    public static final Reject ACCEPT_CALL = new Reject(0x00, "ACCEPTED_CALL");

    /** Ignore the call. */
    public static final Reject IGNORE_CALL = new Reject(0x01, "IGNORE_CALL");

    /** Reject call, normal call clearing. */
    public static final Reject REJECT_NORMAL_CLEARING = new Reject(0x02, "REJECT_NORMAL_CLEARING");

    /** Reject call, user busy. */
    public static final Reject REJECT_USER_BUSY = new Reject(0x03, "REJECT_USER_BUSY");

    /** Reject call, requested circuit/channel not available. */
    public static final Reject REJECT_CIRCUIT_NOT_AVAILABLE = new Reject(0x04, "REJECT_CIRCUIT_NOT_AVAILABLE");

    /** Reject call, facility rejected. */
    public static final Reject REJECT_FACILITY_NOT_SUPPORTED = new Reject(0x05, "REJECT_FACILITY_NOT_SUPPORTED");

    /** Reject call, channel unacceptable. */
    public static final Reject REJECT_CHANNEL_UNACCEPTABLE = new Reject(0x06, "REJECT_CHANNEL_UNACCEPTABLE");

    /** Reject call, incompatible destination. */
    public static final Reject REJECT_INCOMPATIBLE_DESTINATION = new Reject(0x07, "REJECT_INCOMPATIBLE_DESTINATION");

    /** Reject call, destination out of order. */
    public static final Reject REJECT_DESTINATION_OUT_OF_ORDER = new Reject(0x08, "REJECT_DESTINATION_OUT_OF_ORDER");

    /**
     * Disconnect cause from the network in accordance with ETS 300 102-1 /
     * Q.850. The field 'xx' indicates the cause value received from the network
     * in a cause in- formation element (Octet 4).
     */
    public static final String NETWORK_DISCONNECT = "NETWORK_DISCONNECT";

    private static final Reject[] VALUES = { ACCEPT_CALL, IGNORE_CALL, REJECT_NORMAL_CLEARING, REJECT_USER_BUSY,
            REJECT_CIRCUIT_NOT_AVAILABLE, REJECT_FACILITY_NOT_SUPPORTED, REJECT_CHANNEL_UNACCEPTABLE,
            REJECT_INCOMPATIBLE_DESTINATION, REJECT_DESTINATION_OUT_OF_ORDER };

    public static Reject valueOf(int rejectValue) {
        short code = (short) (rejectValue & 0xFFFF);
        if ((code & 0xFF00) == 0x3400) {
            return new Reject(code, NETWORK_DISCONNECT);
        }
        for (Reject j : VALUES) {
            if (j.value == code) {
                return j;
            }
        }
        return null;
    }

    private int value;
    private String name;

    private Reject(int val, String name) {
        this.value = val;
        this.name = name;
    }

    public int intValue() {
        return value;
    }

    public short getNetworkCause() {
        if ((value & 0xff00) == 0x3400) {
            return (short) (value & 0xff);
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
            return format("%s(cause: 0x%02X, desc: %s)", name(), networkCause, NetworkCause
                    .getDescription(networkCause & 0xFF));
        } else {
            return name();
        }
    }

}
