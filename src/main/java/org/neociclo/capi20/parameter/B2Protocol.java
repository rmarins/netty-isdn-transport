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
public enum B2Protocol {

    /**
     * ISO 7776 (X.75 SLP), always set (default).
     */
    X75_SLP(0),

    /**
     * Transparent.
     */
    TRANSPARENT(1),

    /**
     * SDLC.
     */
    SDLC(2),

    /**
     * LAPD in accordance with Q.921 for D-channel X.25 (SAPI 16).
     */
    LAPD_WITH_Q921(3),

    /**
     * T.30 for Group 3 fax.
     */
    T30_GROUP3_FAX(4),

    /**
     * Point-to-Point Protocol (PPP).
     */
    PPP(5),

    /**
     * Transparent (ignoring framing errors of B1 protocol).
     */
    TRANSPARENT_IGNORE_FRAMING_ERRORS(6),

    /**
     * Modem error correction and compression (V.42 bis or MNP5).
     */
    MODEM_ERROR_CORRECTION_AND_COMPRESSION(7),

    /**
     * ISO 7776 (X.75 SLP) modified supporting V.42 bis compression.
     */
    X75_SLP_V42_BIS(8),

    /**
     * V.120 asynchronous mode.
     */
    V120_ASYNCHRONOUS(9),

    /**
     * V.120 asynchronous mode supporting V.42 bis.
     */
    V120_ASYNCHRONOUS_V42_BIS(10),

    /**
     * V.120 bit-transparent mode.
     */
    V120_BIT_TRANSPARENT(11),

    /**
     * LAPD in accordance with Q.921 including free SAPI selection.
     */
    LAPD_WITH_Q921_SAPI_SELECTION(12);

    private int bitField;

    private B2Protocol(int bitField) {
        this.bitField = bitField;
    }

    public int getBitField() {
        return bitField;
    }

    public int intValue() {
        return bitField;
    }

    @Override
    public String toString() {
        String s = String.format("%s(%s)", name(), getBitField());
        return s;
    }

}
