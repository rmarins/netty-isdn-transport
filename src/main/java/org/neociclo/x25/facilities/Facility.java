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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public abstract class Facility {

    private FacilityType type;

    public Facility(FacilityType type) {
        super();
        this.type = type;
    }

    public abstract byte[] encoded();

    public FacilityType getType() {
        return type;
    }

    public byte getLength() {

        byte length = 0;
        FacilityClass fclazz = type.getClazz();

        /* consider the facility qualifier itself when computing length */
        if (fclazz == FacilityClass.DOUBLE) {
            length = 3;
        }

        // TODO add support for facilities with other different lengths

        return length;
    }

}
