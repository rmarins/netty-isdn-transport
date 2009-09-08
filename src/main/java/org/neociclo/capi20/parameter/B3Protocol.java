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
package org.neociclo.capi20.parameter;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
