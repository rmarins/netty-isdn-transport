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
public class CapiAdapter implements Capi {

    public String getManufacturer(int controller) throws org.neociclo.capi20.CapiException {
        return null;
    }

    public byte[] getMessage(int appID) throws org.neociclo.capi20.CapiException {
        return null;
    }

    public byte[] getProfile(int controller) throws org.neociclo.capi20.CapiException {
        return null;
    }

    public String getSerialNumber(int controller) throws org.neociclo.capi20.CapiException {
        return null;
    }

    public int getVersion(int controller) throws org.neociclo.capi20.CapiException {
        return 0;
    }

    public boolean isInstalled() throws org.neociclo.capi20.CapiException {
        return false;
    }

    public void putMessage(int appID, byte[] message) throws org.neociclo.capi20.CapiException { }

    public int register(int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen)
            throws org.neociclo.capi20.CapiException {
        return 0;
    }

    public void release(int appID) throws org.neociclo.capi20.CapiException { }

    public void waitForSignal(int appID) throws org.neociclo.capi20.CapiException { }

}
