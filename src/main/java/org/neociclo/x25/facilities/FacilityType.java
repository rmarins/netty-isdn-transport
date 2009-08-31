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
public enum FacilityType {

    WINDOW_SIZE (FacilityClass.DOUBLE, (byte) 0x43),

    PACKET_SIZE (FacilityClass.DOUBLE, (byte) 0x42);

    private FacilityClass clazz;
    private byte code;

    private FacilityType(FacilityClass clazz, byte code) {
        this.clazz = clazz;
        this.code = code;
    }

    public FacilityClass getClazz() {
        return clazz;
    }

    public byte getCode() {
        return code;
    }

    public static FacilityType valueOf(byte encodedType) {
        FacilityType found = null;
        for (FacilityType t : FacilityType.values()) {
            if (t.getCode() == encodedType) {
                found = t;
                break;
            }
        }
        return found;
    }
}
