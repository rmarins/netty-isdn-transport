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
package org.neociclo.capi20.remote.message;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public enum ControlType {

    GET_CHALLENGE(0x19),
    SET_USER(0x1A);

    public static ControlType valueOf(int value) {
        for (ControlType t : ControlType.values()) {
            if (t.intValue() == value) {
                return t;
            }
        }
        return null;
    }

    private int coded;

    private ControlType(int codedType) {
        this.coded = codedType;
    }

    public int intValue() {
        return coded;
    }
}
