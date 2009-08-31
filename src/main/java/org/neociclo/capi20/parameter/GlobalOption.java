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
