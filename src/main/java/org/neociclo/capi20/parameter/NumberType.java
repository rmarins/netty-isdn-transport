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