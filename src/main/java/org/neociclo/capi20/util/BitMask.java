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

import static org.neociclo.capi20.util.Bits.*;

import java.util.ArrayList;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class BitMask<T extends Enum<T>> {

    private int mask;

    private T[] values;

    public BitMask(T[] values) {
        super();
        this.values = values;
    }

    public void setOption(IBitType t) {
        this.mask = setBit(mask, t.getBitField());
    }

    public boolean hasOption(IBitType t) {
        return isBitSet(mask, t.getBitField());
    }

    public void unsetOption(IBitType t) {
        this.mask = clearBit(mask, t.getBitField());
    }

    @SuppressWarnings("unchecked")
    public T[] getAllOptions() {
        ArrayList<T> result = new ArrayList<T>();
        for (T cap : values) {
            if (hasOption((IBitType) cap)) {
                result.add(cap);
            }
        }
        return (T[]) result.toArray();
    }

    public int getBitMask() {
        return mask;
    }
    
}
