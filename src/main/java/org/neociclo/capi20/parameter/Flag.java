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
