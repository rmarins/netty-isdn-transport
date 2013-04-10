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
public enum GlobalOption {

    /**
     * Internal controller supported.
     */
    INTERNAL(0),

    /**
     * External equipment supported.
     */
    EXTERNAL(1),

    /**
     * Handset supported (external equipment must also be set).
     */
    HANDSET(2),

    /**
     * DTMF supported.
     */
    DTMF(3),

    /**
     *Supplementary Services (see Part III).
     */
    SUPPLEMENTARY_SERVICES(4),

    /**
     * Channel allocation supported (leased lines).
     */
    LEASED_LINES(5),

    /**
     * Parameter B channel operation supported.
     */
    PARAMETER_B(6),

    /**
     * Line Interconnect supported.
     */
    LINE_INTERCONNECT(7);

    private int bitField;

    private GlobalOption(int bitField) {
        this.bitField = bitField;
    }

    public int getBitField() {
        return bitField;
    }

    @Override
    public String toString() {
        String s = String.format("%s(%s)", name(), getBitField());
        return s;
    }

}
