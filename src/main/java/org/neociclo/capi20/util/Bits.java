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
package org.neociclo.capi20.util;

/**
 * @author The ISDN-CAPI-For-Java Project at http://code.google.com/p/isdn-capi-for-java/
 * @version http://code.google.com/p/isdn-capi-for-java/source/browse/trunk/capi20-library/src/main/java/de/ubschmidt/capi/util/Bits.java?r=18
 */
public final class Bits {

    private Bits() { }

    /**
     * 
     * @param position
     * @return
     */
    public static int getBitValue(final int position) {
        return (1 << position);
    }

    /**
     * 
     * @param value
     * @param position
     * @return
     */
    public static boolean isBitSet(final int value, final int position) {
        return (value & getBitValue(position)) != 0;
    }

    /**
     * 
     * @param value
     * @param position
     * @return
     */
    public static int setBit(final int value, final int position) {
        return (value | getBitValue(position));
    }

    /**
     * 
     * @param value
     * @param position
     * @return
     */
    public static int clearBit(final int value, final int position) {
        return (value & (0xffffffff ^ getBitValue(position)));
    }

}
