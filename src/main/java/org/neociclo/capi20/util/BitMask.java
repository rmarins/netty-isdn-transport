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
package org.neociclo.capi20.util;

import static org.neociclo.capi20.util.Bits.*;

import java.util.ArrayList;

/**
 * @author Rafael Marins
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
