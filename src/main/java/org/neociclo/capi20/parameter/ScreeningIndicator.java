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

public enum ScreeningIndicator {

    NOT_SCREENED (0x00),
    VERIFIED_AND_PASSED (0x01),
    VERIFIED_AND_FAILED (0x02),
    NETWORK_PROVIDED (0x03);

    private short binValue;

    private ScreeningIndicator(int coded) {
        this.binValue = (short) coded;
    }

    public short getBinValue() {
        return binValue;
    }

}