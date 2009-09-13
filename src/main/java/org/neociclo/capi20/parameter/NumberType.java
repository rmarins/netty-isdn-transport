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

public enum NumberType {

    UNKOWN_NUMBER_TYPE (0x00),
    INTERNATIONAL_NUMBER_TYPE (0x10),
    NATIONAL_NUMBER_TYPE (0x20),
    NETWORK_NUMBER_TYPE (0x30),
    SUBSCRIBER_NUMBER_TYPE (0x40),
    ABBREVIATED_NUMBER_TYPE (0x60),
    RESERVED_NUMBER_TYPE (0x70);

    public static NumberType valueOf(int type) {
        for (NumberType a : values()) {
            if (a.intValue() == type) {
                return a;
            }
        }
        return null;
    }

    private byte octetCode;

    private NumberType(int coded) {
        this.octetCode = (byte) (coded & 0xFF);
    }

    public int intValue() {
        return (octetCode & 0xFF);
    }
}