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
