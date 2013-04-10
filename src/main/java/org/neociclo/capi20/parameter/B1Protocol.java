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
public enum B1Protocol {

    /**
     * 64 kbit/s with HDLC framing, always set (default).
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

    public int intValue() {
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
