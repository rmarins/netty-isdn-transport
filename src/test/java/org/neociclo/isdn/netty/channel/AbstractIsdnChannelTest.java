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
package org.neociclo.isdn.netty.channel;

import static org.easymock.EasyMock.*;
import static org.neociclo.capi20.parameter.Info.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.jcapi.message.parameter.NCCI;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.CapiAdapter;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.MockCapiFactory;
import org.neociclo.capi20.SimpleCapi;
import org.neociclo.capi20.message.CapiMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public abstract class AbstractIsdnChannelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIsdnChannelTest.class);

    private static final int APP_ID = 0xaccd;

    /**
     * 64 octets answer to GET_PROFILE_REQ command.
     * 
     * <pre>
     *     Controller: 1
     *     Number of Supported B-channels: 2
     *     Global Options: [0x39 0x00 0x00 0x00]
     *     B1 Protocol Support: [0x1f 0x01 0x00 0x40]
     *     B2 Protocol Support: [0x1b 0x0b 0x00 0x00]
     *     B3 Protocol Support: [0xbf 0x00 0x00 0x80]
     *     Reserved: (24 times 0x00)
     *     Manufacturer Specific: (20 times 0x00)
     * </pre>
     */
    private static byte[] GET_PROFILE_CONF_CONTROLLER_ONE = new byte[] { 0x01, 0x00, 0x02, 0x00, // 4-octets
            0x39, 0x00, 0x00, 0x00, 0x1f, 0x01, 0x00, 0x40, 0x1b, 0x0b, // 10-octets
            0x00, 0x00, (byte) 0xbf, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, 0x00, 0x00, // 10-octets
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 10-octets
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 10-octets
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 10-octets
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }; // 10-octets

    private static PLCI PLCI_OBJ = new PLCI(0x01010000);

    private static NCCI NCCI_OBJ = new NCCI(0x01010100);

    protected MockCapiFactory capiFactory;

    private Object waitForSignalLock;

    private Queue<CapiMessage> inChannel;

    private AtomicInteger inMsgCounter;

    protected abstract void configure(IsdnChannel channel);

    protected abstract void mockTestRecording() throws Exception;

    @Before
    @SuppressWarnings("serial")
    public void setUp() throws Exception {

        this.capiFactory = new MockCapiFactory(false);

        this.waitForSignalLock = new Object();
        this.inMsgCounter = new AtomicInteger(0);

        this.inChannel = new LinkedList<CapiMessage>() {

            @Override
            public boolean offer(CapiMessage e) {
                LOGGER.trace("offer () :: message = {}", e);
                boolean ret = super.offer(e);
                synchronized (waitForSignalLock) {
                    waitForSignalLock.notify();
                }
                return ret;
            }

            @Override
            public CapiMessage poll() {
                CapiMessage p = super.poll();
                LOGGER.trace("poll() :: message = {}", p);
                return p;
            }
        };

        mockGeneralRecording();
        replay(mock());

    }

    @After
    public void tearDown() throws Exception {
        verify(mock());
    }

    protected Capi mock() {
        return capiFactory.getCapi();
    }

    protected SimpleCapi simpleCapi() {
        return capiFactory.getSimpleCapi();
    }

    protected int getAppId() {
        return APP_ID;
    }

    protected IsdnConfigurator getConfigurator() {
        return new IsdnConfigurator() {
            public void configureChannel(IsdnChannel channel) {
                configure(channel);
            }
        };
    }

    protected void mockGeneralRecording() throws Exception {

        stepGetProfile();

        stepRegister();

        stepWaitForSignal();
        stepGetMessageDrainQueue();

        mockTestRecording();

        stepRelease();

    }

    protected void stepRelease() throws Exception {

        // CAPI_RELEASE
        mock().release(eq(getAppId()));
        expectLastCall().once();

    }

    protected void stepRegister() throws Exception {

        // CAPI_REGISTER
        expect(mock().register(geq(2048), eq(2), and(geq(2), leq(7)), gt(0))).andReturn(getAppId()).once();

    }

    protected void stepGetMessageDrainQueue() throws Exception {

        // CAPI_GET_MESSAGE
        expect(mock().getMessage(eq(getAppId()))).andAnswer(new IAnswer<byte[]>() {
            public byte[] answer() throws Throwable {
                // sync & return messages from inChannel
                CapiMessage a = null;
                synchronized (inChannel) {
                    LOGGER.trace("Mock.getMessage() :: {}", inChannel);
                    if (inChannel.isEmpty()) {
                        throw new CapiException(EXCHANGE_QUEUE_EMPTY, "Empty inChannel queue.");
                    }
                    a = inChannel.poll();
                }
                return a.getRawOctets();
            }
        }).anyTimes();

    }

    protected void stepWaitForSignal() throws Exception {

        // CAPI_WAIT_FOR_SIGNAL
        mock().waitForSignal(eq(getAppId()));
        expectLastCall().andDelegateTo(new CapiAdapter() {
            @Override
            public boolean waitForSignal(int appID) throws CapiException {
                // release waitForSignal() method after lock notify
                if (inChannel.isEmpty()) {
                    LOGGER.trace("Mock.waitForSignal() :: Acquiring lock...");
                    try {
                        synchronized (waitForSignalLock) {
                            waitForSignalLock.wait();
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("waitForSignal() :: interrupted", e);
                        throw new CapiException(EXCHANGE_RESOURCE_ERROR, "interrupted exception");
                    }
                    LOGGER.trace("Mock.waitForSignal() :: Lock released");
                }
                return true;
            }
        }).anyTimes();

    }

    protected void stepGetProfile() throws Exception {

        // CAPI_GET_PROFILE: configure mock with one controller-only CAPI
//        expect(simpleCapi().getNumberOfControllers()).andReturn(1).anyTimes();
        expect(mock().getProfile(eq(1))).andReturn(GET_PROFILE_CONF_CONTROLLER_ONE).anyTimes();

    }

    protected int getMessageNumberAndIncrement() {
        return inMsgCounter.getAndIncrement();
    }

    protected void scheduleReceive(CapiMessage a) {
        inChannel.offer(a);
    }

    protected PLCI getPlci() {
        return PLCI_OBJ;
    }

    protected NCCI getNcci() {
        return NCCI_OBJ;
    }

}
