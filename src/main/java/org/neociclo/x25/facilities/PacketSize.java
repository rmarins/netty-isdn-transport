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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public enum PacketSize {

    ENCODED_16 ((byte) 4),
    ENCODED_32 ((byte) 5),
    ENCODED_64 ((byte) 6),
    ENCODED_128 ((byte) 7),
    ENCODED_256 ((byte) 8),
    ENCODED_512 ((byte) 9),
    ENCODED_1024 ((byte) 10),
    ENCODED_2048 ((byte) 11),
    ENCODED_4096 ((byte) 12);

    public static PacketSize valueOf(byte encodedSize) {
        for (PacketSize p : PacketSize.values()) {
            if (p.getSizeCode() == encodedSize) {
                return p;
            }
        }
        return null;
    }

    private byte sizeCode;

    private PacketSize(byte code) {
        this.sizeCode = code;
    }

    public byte getSizeCode() {
        return sizeCode;
    }

}
