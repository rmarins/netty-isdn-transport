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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
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
