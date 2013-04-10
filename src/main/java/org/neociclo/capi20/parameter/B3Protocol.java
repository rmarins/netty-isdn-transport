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
public enum B3Protocol {

    /**
     * Transparent, always set (default).
     */
    TRANSPARENT(0),

    /**
     * T.90NL with compatibility to T.70NL in accordance to T.90 Appendix II.
     */
    T90NL_WITH_T70NL_COMPATIBILITY(1),

    /**
     * ISO 8208 (X.25 DTE-DTE).
     */
    X25_DTE_DTE(2),

    /**
     * X.25 DCE.
     */
    X25_DCE(3),

    /**
     * T.30 for Group 3 fax.
     */
    T30_GROUP3_FAX(4),

    /**
     * T.30 for Group 3 fax with extensions.
     */
    T30_GROUP3_FAX_EXTENSIONS(5),

    /**
     * Modem.
     */
    MODEM(6);

    private int bitField;

    private B3Protocol(int bitField) {
        this.bitField = bitField;
    }

    public int getBitField() {
        return bitField;
    }

    public int intValue() {
        return bitField;
    }

    @Override
    public String toString() {
        String s = String.format("%s(%s)", name(), getBitField());
        return s;
    }

}
