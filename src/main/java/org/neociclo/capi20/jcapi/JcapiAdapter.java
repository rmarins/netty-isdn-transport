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
package org.neociclo.capi20.jcapi;

import static java.lang.String.*;

import java.util.Arrays;

import org.neociclo.capi20.Capi;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class JcapiAdapter implements Capi {

    private static void exception(org.capi.capi20.CapiException ce) throws CapiException {
        Info error = Info.valueOf(ce.getCapiCode());
        String msg = ce.getMessage();
        throw new CapiException(error, msg);
    }

    private org.capi.capi20.Capi jcapi;

    public JcapiAdapter(org.capi.capi20.Capi jcapi) {
        super();
        this.jcapi = jcapi;
    }

    public String getManufacturer(int controller) throws CapiException {
        try {
            return jcapi.getManufacturer(controller);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return null;
    }

    public byte[] getMessage(int appID) throws CapiException {
        try {
            org.capi.capi20.CapiMessage message = jcapi.getMessage(appID);
            if (message == null) {
                throw new CapiException(Info.EXCHANGE_QUEUE_EMPTY, "JCapi returned a null message.");
            }
            return message.getBytes();
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return null;
    }

    public byte[] getProfile(int controller) throws CapiException {
        try {
            return jcapi.getProfile(controller);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return null;
    }

    public String getSerialNumber(int controller) throws CapiException {
        try {
            return jcapi.getSerialNumber(controller);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return null;
    }

    public int getVersion(int controller) throws CapiException {
        try {
            int version = 0;
            int[] ret = jcapi.getVersion(controller);
            if (ret == null || ret.length != 4) {
                throw new CapiException(Info.ILLEGAL_MESSAGE_PARAMETER_CODING, format(
                        "Invalid JCapi version returned: {}.", Arrays.toString(ret)));
            }
            version |= ((ret[0] & 0xff) << 24);
            version |= ((ret[1] & 0xff) << 16);
            version |= ((ret[2] & 0xff) << 8);
            version |= (ret[3] & 0xff);
            return version;
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return 0;
    }

    public boolean isInstalled() throws CapiException {
        try {
            return jcapi.installed();
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return false;
    }

    public void putMessage(int appID, byte[] message) throws CapiException {
        try {
            jcapi.putMessage(new net.sourceforge.jcapi.JcapiMessage(message));
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
    }

    public int register(int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen)
            throws CapiException {
        try {
            return jcapi.register(messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return 0;
    }

    public void release(int appID) throws CapiException {
        try {
            jcapi.release(appID);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
    }

    public void waitForSignal(int appID) throws CapiException {
        try {
            jcapi.waitForSignal(appID);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
    }

}
