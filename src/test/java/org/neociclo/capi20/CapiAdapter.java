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
package org.neociclo.capi20;

/**
 * @author Rafael Marins
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

    public boolean waitForSignal(int appID) throws org.neociclo.capi20.CapiException {
    	return false;
    }

    public boolean waitForSignal(int appID, long timeoutMillis) throws org.neociclo.capi20.CapiException {
    	return false;
    }

}
