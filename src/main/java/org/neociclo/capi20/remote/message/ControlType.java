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
package org.neociclo.capi20.remote.message;

/**
 * @author Rafael Marins
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
