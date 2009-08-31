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
package org.neociclo.capi20;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class CapiVersion {

    private byte capiMajor;
    private byte capiMinor;
    private byte manufacturerMajor;
    private byte manufacturerMinor;

    /**
     * 
     * @param version the version struct param.
     */
    public CapiVersion(int version) {
        capiMajor = (byte) ((version >> 24) & 0xff);
        capiMinor = (byte) ((version >> 16) & 0xff);
        manufacturerMajor = (byte) ((version >> 8) & 0xff);
        manufacturerMinor = (byte) (version & 0xff);
    }

    public byte getCapiMajor() {
        return capiMajor;
    }

    public byte getCapiMinor() {
        return capiMinor;
    }

    public byte getManufacturerMajor() {
        return manufacturerMajor;
    }

    public byte getManufacturerMinor() {
        return manufacturerMinor;
    }

    
}
