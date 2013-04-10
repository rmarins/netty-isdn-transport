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

public enum NumberingPlan {

    UNKNOWN_NUMBER_PLAN (0x00),
    ISDN_PHONE_NUMBER_PLAN (0x01),
    DATA_NUMBER_PLAN (0x03),
    TELEX_NUMBER_PLAN (0x04),
    NATIONAL_STANDARD_NUMBER_PLAN (0x08),
    PRIVATE_NUMBER_PLAN (0x09),
    RESERVED_NUMBER_PLAN (0x0F);

    public static NumberingPlan valueOf(int plan) {
        for (NumberingPlan a : values()) {
            if (a.intValue() == plan) {
                return a;
            }
        }
        return null;
    }

    private byte octetCode;

    private NumberingPlan(int coded) {
        this.octetCode = (byte) (coded & 0xFF);
    }

    public int intValue() {
        return (octetCode & 0xFF);
    }
}