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
package org.neociclo.capi20.remote;

import static org.neociclo.capi20.remote.RemoteCapiHelper.*;
import static java.lang.String.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.parameter.Info;
import org.neociclo.capi20.remote.message.GetManufacturerConf;
import org.neociclo.capi20.remote.message.GetManufacturerReq;
import org.neociclo.capi20.remote.message.GetProfileConf;
import org.neociclo.capi20.remote.message.GetProfileReq;
import org.neociclo.capi20.remote.message.GetSerialNumberConf;
import org.neociclo.capi20.remote.message.GetSerialNumberReq;
import org.neociclo.capi20.remote.message.GetVersionConf;
import org.neociclo.capi20.remote.message.GetVersionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
public class RemoteCapi implements Capi {

    /**
     * Remote-CAPI implementation specific Application ID: 0xaccd.
     */
    public static final int CONTROL_APP_ID = 0xcdac;

    private static final Map<Integer, ApplicationController> cachedApps = new HashMap<Integer, ApplicationController>();

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteCapi.class);

    private static ApplicationController getRegisteredAppId(int appID) throws CapiException {
        ApplicationController appCtrl = cachedApps.get(appID);
        if (appCtrl == null) {
            throw new CapiException(Info.EXCHANGE_ILLEGAL_APPLICATION_NUMBER, format(
                    "Application Controller not found. No registered application: 0x%04X", appID));
        }
        return appCtrl;
    }

    private InetSocketAddress remoteAddress;
    private String user;
    private String passwd;

    public RemoteCapi(InetSocketAddress address) {
        this(address, null, null);
    }

    public RemoteCapi(InetSocketAddress address, String user, String passwd) {
        super();
        if (address == null) {
            throw new NullPointerException("address");
        }
        this.remoteAddress = address;
        this.user = user;
        this.passwd = passwd;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public String getUser() {
        return user;
    }

    public String getPasswd() {
        return passwd;
    }

    // -------------------------------------------------------------------------
    // CAPI specific methods implementations
    // -------------------------------------------------------------------------

    public String getManufacturer(int controller) throws CapiException {
        LOGGER.trace("getManufacturer() :: controller = {}", controller);
        GetManufacturerReq req = new GetManufacturerReq(CONTROL_APP_ID, controller);
        GetManufacturerConf conf = (GetManufacturerConf) capiInvoke(this, req);
        return conf.getManufacturerInfo();
    }

    public byte[] getProfile(int controller) throws CapiException {
        LOGGER.trace("getProfile() :: controller = {}", controller);
        GetProfileReq req = new GetProfileReq(CONTROL_APP_ID, controller);
        GetProfileConf conf = (GetProfileConf) capiInvoke(this, req);
        ChannelBuffer buf = conf.getBuffer();
        byte[] profile = new byte[buf.readableBytes()];
        buf.readBytes(profile);
        return profile;
    }

    public String getSerialNumber(int controller) throws CapiException {
        LOGGER.trace("getSerialNumber() :: controller = {}", controller);
        GetSerialNumberReq req = new GetSerialNumberReq(CONTROL_APP_ID, controller);
        GetSerialNumberConf conf = (GetSerialNumberConf) capiInvoke(this, req);
        return conf.getSerialNumber();
    }

    public int getVersion(int controller) throws CapiException {
        LOGGER.trace("getVersion() :: controller = {}", controller);
        GetVersionReq req = new GetVersionReq(CONTROL_APP_ID, controller);
        GetVersionConf conf = (GetVersionConf) capiInvoke(this, req);
        return conf.intValue();
    }

    public boolean isInstalled() throws CapiException {
        LOGGER.trace("isInstalled() :: always true");
        return true;
    }

    public int register(int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen)
            throws CapiException {

        LOGGER.trace("register() :: msgBufSize = {}, maxConn = {}, maxBDataBlocks = {}, maxBDataLen = {}",
                new Object[] { messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen });

        ApplicationController appCtrl = ApplicationControllerFactory.createApplicationController(this);

        int appID = appCtrl.getAppID();
        cachedApps.put(appID, appCtrl);

        synchronized (appCtrl) {
            appCtrl.register(messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen);
        }

        LOGGER.trace("register() :: Registered. AppID = {}.", appID);

        return appID;
    }

    public void putMessage(int appID, byte[] message) throws CapiException {

        ApplicationController appCtrl = getRegisteredAppId(appID);

        synchronized (appCtrl.writeLock) {
            appCtrl.putMessage(message);
        }

    }

    public boolean waitForSignal(int appID) throws CapiException {
        ApplicationController appCtrl = getRegisteredAppId(appID);
        boolean signal = false;
        synchronized (appCtrl.readLock) {
            signal = appCtrl.waitForSignal();
        }
        return signal;
    }

	public boolean waitForSignal(int appID, long timeoutMillis) throws CapiException {
        ApplicationController appCtrl = getRegisteredAppId(appID);
        boolean signal = false;
        synchronized (appCtrl.readLock) {
            signal = appCtrl.waitForSignal(timeoutMillis);
        }
        return signal;
	}

    public byte[] getMessage(int appID) throws CapiException {

        ApplicationController appCtrl = getRegisteredAppId(appID);

        byte[] message = null;
        synchronized (appCtrl.readLock) {
            message = appCtrl.getMessage();
        }

        return message;
    }

    public void release(int appID) throws CapiException {

        LOGGER.trace("release() :: appID = {}.", appID);
        
        ApplicationController appCtrl = getRegisteredAppId(appID);

        synchronized (appCtrl) {
            appCtrl.release();
            cachedApps.remove(appID);
        }

    }

}
