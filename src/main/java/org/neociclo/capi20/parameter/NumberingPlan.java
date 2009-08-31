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

public enum NumberingPlan {

    UNKNOWN_NUMBER_PLAN (0x00),
    ISDN_PHONE_NUMBER_PLAN (0x01),
    DATA_NUMBER_PLAN (0x03),
    TELEX_NUMBER_PLAN (0x04),
    NATIONAL_STANDARD_NUMBER_PLAN (0x08),
    PRIVATE_NUMBER_PLAN (0x09),
    RESERVED_NUMBER_PLAN (0x0F);

    private short binValue;

    private NumberingPlan(int coded) {
        this.binValue = (short) coded;
    }

    public short getBinValue() {
        return binValue;
    }
}