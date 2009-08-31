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

import static org.neociclo.capi20.util.CapiBuffers.*;
import static org.jboss.netty.buffer.ChannelBuffers.*;
import static org.neociclo.capi20.util.CapiParameterBuilder.*;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class SimpleCapi implements Capi {

    public static final int DEFAULT_MAX_LOGICAL_CONNECTION = 2;

    public static final int DEFAULT_MAX_BDATA_BLOCKS = 2;

    public static final int DEFAULT_MAX_BDATA_LEN = 1024;

    /** CAPI implementation specific controller identification. */
    private static final int CAPI_IMPL_CONTROLLER = 0x0000;

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

    public void waitForSignal(int appID) throws CapiException {
        capi.waitForSignal(appID);
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
