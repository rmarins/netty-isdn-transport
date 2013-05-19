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

import static org.jboss.netty.buffer.ChannelBuffers.*;
import static org.junit.Assert.*;

import java.net.InetSocketAddress;

import net.sourceforge.jcapi.message.parameter.PLCI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neociclo.capi20.message.InfoConf;
import org.neociclo.capi20.message.InfoReq;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 */
public class RemoteCapiTest {

	protected boolean runTests;

    private RemoteCapi capi;

    @Before
    public void setUp() throws Exception {

    	runTests = Boolean.parseBoolean(System.getProperty("capi.test", "false"));
        if (!runTests) {
            return;
        }

    	String capiServer = System.getProperty("capi.server", "localhost");
    	int capiPort = Integer.valueOf(System.getProperty("capi.port", "12662"));

    	this.capi = new RemoteCapi(new InetSocketAddress(capiServer, capiPort));
    }

    @After
    public void tearDown() throws Exception {
        this.capi = null;
    }

    @Test
    public void testGetManufacturer() throws Exception {
        if (!runTests) {
            return;
        }

        String manufacturerInfo = capi.getManufacturer(0);
        assertNotNull("No manufacturer info returned.", manufacturerInfo);
    }

    @Test
    public void testGetProfile() throws Exception {
        if (!runTests) {
            return;
        }

        byte[] profile = capi.getProfile(0);
        assertNotNull("Invalid Capi.getProfile() null response.", profile);
        assertEquals("Invalid Capi.getProfile() response size.", 64, profile.length);
    }

    @Test
    public void testGetSerialNumber() throws Exception {
        if (!runTests) {
            return;
        }

        String serialInfo = capi.getSerialNumber(0);
        assertNotNull("No serial number returned.", serialInfo);
    }

    @Test
    public void testGetVersion() throws Exception {
        if (!runTests) {
            return;
        }

        int version = capi.getVersion(0);
        assertTrue("Bad version info: " + version, version != 0);
    }

    @Test
    public void testIsInstalled() throws Exception {
        if (!runTests) {
            return;
        }

        boolean installed = capi.isInstalled();
        assertTrue("Capi not installed.", installed);
    }

    @Test
    public void testRegisterOnly() throws Exception {
        if (!runTests) {
            return;
        }

        int appID = capi.register(3072, 2, 7, 2048);
        assertEquals("Initial AppID must be equal to ONE.", 1, appID);
    }

    @Test
    public void testRegisterAndRelease() throws Exception {
        if (!runTests) {
            return;
        }

        int appID = capi.register(3072, 2, 7, 2048);
        capi.release(appID);
    }

    @Test
    public void testInfoMessageExchanges() throws Exception {
        if (!runTests) {
            return;
        }

        // CAPI_REGISTER
        int appID = capi.register(3072, 1, 7, 2048);

        // send INFO_REQ
        InfoReq infoReq = new InfoReq();
        infoReq.setPlci(new PLCI(0)); // controller 1
        infoReq.setAppID(appID);
        infoReq.setMessageID(1);
        capi.putMessage(appID, infoReq.getRawOctets());

        // get back INFO_CONF
        capi.waitForSignal(appID);
        byte[] rawConf = capi.getMessage(appID);
        InfoConf infoConf = new InfoConf(wrappedBuffer(rawConf));

        // CAPI_RELEASE
        capi.release(appID);

        assertEquals("INFO_CONF -> Message AppID mismatch.", appID, infoConf.getAppID());
        assertEquals("INFO_CONF -> Message Number mismatch.", 1, infoConf.getMessageID());

        assertNotNull("INFO_CONF -> Non null PLCI were expected.", infoConf.getPlci());
        assertEquals("INFO_CONF -> PLCI controller were mismatch.", 0, infoConf.getPlci().getController().getController());

        assertNotNull("INFO_CONF -> Non null INFO expected.", infoConf.getInfo());
        assertEquals("INFO_CONF ->  Transmission of information initiated were expected.", Info.REQUEST_ACCEPTED,
                infoConf.getInfo());
    }

}
