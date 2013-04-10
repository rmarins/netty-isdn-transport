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

import static org.neociclo.capi20.util.CapiBuffers.*;
import static org.jboss.netty.buffer.ChannelBuffers.*;
import static org.neociclo.capi20.util.CapiParameterBuilder.*;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
public class SimpleCapi implements Capi {

    public static final int DEFAULT_MAX_LOGICAL_CONNECTION = 2;

    public static final int DEFAULT_MAX_BDATA_BLOCKS = 2;

    public static final int DEFAULT_MAX_BDATA_LEN = 1024;

    /** CAPI implementation specific controller identification. */
    private static final int CAPI_IMPL_CONTROLLER = 0x0001;

    private static final int ONE_KB = 1024;

    private Capi capi;

    public SimpleCapi(Capi capi) {
        super();
        this.capi = capi;
    }

    public int getNumberOfControllers() throws CapiException {
        byte[] data = getProfile(CAPI_IMPL_CONTROLLER);
        ChannelBuffer buf = wrappedBuffer(data, 0, WORD_SIZE);
        return readWord(buf);
    }

    public Profile simpleGetProfile(int controller) throws CapiException {
        byte[] data = getProfile(controller);
        ChannelBuffer buf = wrappedBuffer(data);
        return buildProfile(buf);
    }

    public ChannelBuffer simpleGetMessage(int appID) throws CapiException {
        byte[] data = getMessage(appID);
        return wrappedBuffer(data);
    }

    public void simplePutMessage(int appID, ChannelBuffer message) throws CapiException {
        message.readerIndex(0);
        byte data[] = new byte[message.readableBytes()];
        message.readBytes(data);
        putMessage(appID, data);
    }

    public CapiVersion simpleGetVersion(int controller) throws CapiException {
        int version = getVersion(controller);
        return new CapiVersion(version);
    }

    public CapiVersion getImplVersion() throws CapiException {
        return simpleGetVersion(CAPI_IMPL_CONTROLLER);
    }

    public String getImplSerialNumber() throws CapiException {
        return getSerialNumber(CAPI_IMPL_CONTROLLER);
    }

    public String getImplManufacturer() throws CapiException {
        return getManufacturer(CAPI_IMPL_CONTROLLER);
    }

    public int register() throws CapiException {
        return register(DEFAULT_MAX_LOGICAL_CONNECTION, DEFAULT_MAX_BDATA_BLOCKS, DEFAULT_MAX_BDATA_LEN);
    }

    public int register(int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen) throws CapiException {
        int messageBufferSize = ONE_KB + (ONE_KB * maxLogicalConnection);
        return register(messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen);
    }

    // ------------------------------------------------------------------------
    // Wrapped CAPI delegated methods
    // ------------------------------------------------------------------------

    public byte[] getMessage(int appID) throws CapiException {
        return capi.getMessage(appID);
    }

    public byte[] getProfile(int controller) throws CapiException {
        return capi.getProfile(controller);
    }

    public void putMessage(int appID, byte[] message) throws CapiException {
        capi.putMessage(appID, message);
    }

    public String getManufacturer(int controller) throws CapiException {
        return capi.getManufacturer(controller);
    }

    public String getSerialNumber(int controller) throws CapiException {
        return capi.getSerialNumber(controller);
    }

    public int getVersion(int controller) throws CapiException {
        return capi.getVersion(controller);
    }

    public boolean isInstalled() throws CapiException {
        return capi.isInstalled();
    }

    public int register(int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen)
            throws CapiException {
        return capi.register(messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen);
    }

    public void release(int appID) throws CapiException {
        capi.release(appID);
    }

    public boolean waitForSignal(int appID) throws CapiException {
        return capi.waitForSignal(appID);
    }

    public boolean waitForSignal(int appID, long timeoutMillis) throws CapiException {
        return capi.waitForSignal(appID, timeoutMillis);
    }

    @Override
    public String toString() {

        String className = getClass().getSimpleName();
        Object[] args = null;

        try {
            String man = getImplManufacturer();
            String sn = getImplSerialNumber();
            CapiVersion ver = getImplVersion();
            args = new Object[] { className, ver.getCapiMajor(), ver.getCapiMinor(), man, ver.getManufacturerMajor(),
                    ver.getManufacturerMinor(), sn };
        } catch (Throwable t) {
            return className + "(capi error)";
        }

        return String.format("%s(capi %d.%d by %s, version: %d.%d, s/n: %s)", args);
    }

}
