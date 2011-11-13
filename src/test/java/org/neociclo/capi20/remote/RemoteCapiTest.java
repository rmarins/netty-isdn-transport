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
 * @version $Rev$ $Date$
 */
public class RemoteCapiTest {

    private RemoteCapi capi;

    @Before
    public void setUp() throws Exception {
        this.capi = new RemoteCapi(new InetSocketAddress("localhost", 2662));
    }

    @After
    public void tearDown() throws Exception {
        this.capi = null;
    }

//    @Test
//    public void testGetManufacturer() throws Exception {
//        String manufacturerInfo = capi.getManufacturer(0);
//        assertNotNull("No manufacturer info returned.", manufacturerInfo);
//    }
//
//    @Test
//    public void testGetProfile() throws Exception {
//        byte[] profile = capi.getProfile(0);
//        assertNotNull("Invalid Capi.getProfile() null response.", profile);
//        assertEquals("Invalid Capi.getProfile() response size.", 64, profile.length);
//    }
//
//    @Test
//    public void testGetSerialNumber() throws Exception {
//        String serialInfo = capi.getSerialNumber(0);
//        assertNotNull("No serial number returned.", serialInfo);
//    }
//
//    @Test
//    public void testGetVersion() throws Exception {
//        int version = capi.getVersion(0);
//        assertTrue("Bad version info: " + version, version != 0);
//    }
//
//    @Test
//    public void testIsInstalled() throws Exception {
//        boolean installed = capi.isInstalled();
//        assertTrue("Capi not installed.", installed);
//    }
//
//    @Test
//    public void testRegisterOnly() throws Exception {
//        int appID = capi.register(3072, 2, 7, 2048);
//        assertEquals("Initial AppID must be equal to ONE.", 1, appID);
//    }
//
//    @Test
//    public void testRegisterAndRelease() throws Exception {
//        int appID = capi.register(3072, 2, 7, 2048);
//        capi.release(appID);
//    }
//
//    @Test
//    public void testInfoMessageExchanges() throws Exception {
//
//        // CAPI_REGISTER
//        int appID = capi.register(3072, 2, 7, 2048);
//
//        // send INFO_REQ
//        InfoReq infoReq = new InfoReq();
//        infoReq.setPlci(new PLCI(1)); // controller 1
//        infoReq.setAppID(appID);
//        infoReq.setMessageID(1);
//        capi.putMessage(appID, infoReq.getRawOctets());
//
//        // get back INFO_CONF
//        capi.waitForSignal(appID);
//        byte[] rawConf = capi.getMessage(appID);
//        InfoConf infoConf = new InfoConf(wrappedBuffer(rawConf));
//
//        // CAPI_RELEASE
//        capi.release(appID);
//
//        assertEquals("INFO_CONF -> Message AppID mismatch.", appID, infoConf.getAppID());
//        assertEquals("INFO_CONF -> Message Number mismatch.", 1, infoConf.getMessageID());
//
//        assertNotNull("INFO_CONF -> Non null PLCI were expected.", infoConf.getPlci());
//        assertEquals("INFO_CONF -> PLCI controller were mismatch.", 1, infoConf.getPlci().getController().getController());
//
//        assertNotNull("INFO_CONF -> Non null INFO expected.", infoConf.getInfo());
//        assertEquals("INFO_CONF ->  Transmission of information initiated were expected.", Info.REQUEST_ACCEPTED,
//                infoConf.getInfo());
//    }

    @Test
    public void testDoNothing() {
    }
}
