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

/**
 * @author Rafael Marins
 */
public enum Flag {

    /** Qualifier bit. */
    QUALIFIER_BIT(0),

    /** More-data bit. */
    MORE_DATA_BIT(1),

    /** Delivery confirmation bit. */
    DELIVERY_CONFIRMATION_BIT(2),

    /** Expedited data bit. */
    EXPEDITED_DATA_BIT(3),

    /** Break / UI frame. */
    BREAK_UI_FRAME(4),

    /**
     * Framing error bit: data may be invalid (only with appropriate B2
     * protocol).
     */
    FRAMING_ERROR_BIT(15);

    private byte bitField;

    private Flag(int bitPosition) {
        this.bitField = (byte) (bitPosition & 0xff);
    }

    public int getBitField() {
        return (bitField & 0xff);
    }
}
