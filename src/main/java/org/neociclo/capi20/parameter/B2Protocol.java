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
 * @author Rafael Marins
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
