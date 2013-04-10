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
package org.neociclo.capi20.jcapi;

import static java.lang.String.*;

import java.util.Arrays;

import org.neociclo.capi20.Capi;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
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

    public boolean waitForSignal(int appID) throws CapiException {
        try {
            jcapi.waitForSignal(appID);
        } catch (org.capi.capi20.CapiException e) {
            exception(e);
        }
        return true;
    }

    public boolean waitForSignal(int appID, long timeoutMillis) throws CapiException {
    	return waitForSignal(appID);
    }

}
