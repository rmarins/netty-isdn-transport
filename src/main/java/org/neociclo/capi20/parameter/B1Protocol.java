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

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public enum B1Protocol {

    /**
     * 64 kbit/s with HDLC framing, always set (defaul).
     */
    HDLC_FRAMING_64KBITPS(0),

    /**
     * 64 kbit/s bit-transparent operation with byte framing from the network.
     */
    BIT_TRANSPARENT_64KBITPS(1),

    /**
     * V.110 asynchronous operation with start/stop byte framing.
     */
    V110_ASYNCHRONOUS(2),

    /**
     * V.110 synchronous operation with HDLC framing.
     */
    V110_SYNCHRONOUS(3),

    /**
     * T.30 modem for Group 3 fax.
     */
    T30_MODEM(4),

    /**
     * 64 kbit/s inverted with HDLC framing.
     */
    INVERTED_64KBITPS(5),

    /**
     * 56 kbit/s bit-transparent operation with byte framing from the network.
     */
    BIT_TRANSPARENT_56KBITPS(6),

    /**
     * Modem with full negotiation (B2 Protocol must be 7).
     */
    MODEM_WITH_FULL_NEGOTIATION(7),

    /**
     * Modem asynchronous operation with start/stop byte framing.
     */
    MODEM_ASYNCHRONOUS(8),

    /**
     * Modem synchronous operation with HDLC framing.
     */
    MODEM_SYNCHRONOUS(9);

    private int bitField;

    private B1Protocol(int bitField) {
        this.bitField = bitField;
    }

    public int getBitField() {
        return bitField;
    }

    @Override
    public String toString() {
        String s = String.format("%s(%s)",
                name(),
                getBitField());
        return s;
    }

}
