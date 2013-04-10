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
package org.neociclo.isdn.netty.channel;

import static org.easymock.EasyMock.*;
import static org.neociclo.capi20.parameter.B3Protocol.X25_DTE_DTE;
import static org.neociclo.capi20.parameter.CompatibilityInformationProfile.UNRESTRICTED_DIGITAL;
import static org.neociclo.capi20.parameter.Info.REQUEST_ACCEPTED;

import java.util.concurrent.Executors;

import net.sourceforge.jcapi.message.ConnectActiveIndication;
import net.sourceforge.jcapi.message.ConnectActiveResponse;
import net.sourceforge.jcapi.message.ConnectB3ActiveIndication;
import net.sourceforge.jcapi.message.ConnectB3ActiveResponse;
import net.sourceforge.jcapi.message.ConnectB3Confirmation;
import net.sourceforge.jcapi.message.ConnectB3Request;
import net.sourceforge.jcapi.message.ConnectConfirmation;
import net.sourceforge.jcapi.message.ConnectRequest;
import net.sourceforge.jcapi.message.DisconnectB3Confirmation;
import net.sourceforge.jcapi.message.DisconnectB3Request;
import net.sourceforge.jcapi.message.DisconnectConfirmation;
import net.sourceforge.jcapi.message.DisconnectRequest;
import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.ConnectedNumber;
import net.sourceforge.jcapi.message.parameter.ConnectedSubAddress;
import net.sourceforge.jcapi.message.parameter.sub.B3Configuration;
import net.sourceforge.jcapi.message.parameter.sub.BChannelInformation;

import org.capi.capi20.CapiException;
import org.capi.capi20.CapiMessage;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.junit.Test;
import org.neociclo.capi20.CapiAdapter;
import org.neociclo.isdn.IsdnSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
public class IsdnClientChannelTest extends AbstractIsdnChannelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnClientChannelTest.class);

    private static final long AWAIT_TIMEOUT = 5000L;

    private IsdnSocketAddress calledAddress = new IsdnSocketAddress("55552222");

    private IsdnSocketAddress callingAddress = new IsdnSocketAddress("55551111");

    @Override
    protected void mockTestRecording() throws Exception {

//        // rec: CONNECT_REQ, send: CONNECT_CONF and CONNECT_ACTIVE_IND
//        expectConnectReqOnceAndReply();
//        // rec: CONNET_ACTIVE_RESP
//        expectConnectActiveRespOnceAndIgnore();
//        // rec: CONNECT_B3_REQ, send: CONNECT_B3_CONF and CONNECT_B3_ACTIVE_IND
//        expectConnectB3ReqOnceAndReply();
//        // rec: CONNECT_B3_ACTIVE_RESP
//        expectConnectB3ActiveRespOnceAndIgnore();
//        // rec: DISCONNECT_B3_REQ, send: DISCONNECT_B3_CONF
//        expectDisconnectB3ReqOnceAndReply();
//        // rec: DISCONNECT_REQ, send: DISCONNECT_CONF
//        expectDisconnectReqOnceAndReply();

    }

    @Override
    protected void configure(IsdnChannel channel) {

        IsdnChannelConfig config = channel.getConfig();

        config.setMaxLogicalConnection(2);
        config.setMaxBDataBlocks(7);
        config.setMaxBDataLen(2048);

        config.setCompatibilityInformationProfile(UNRESTRICTED_DIGITAL);
        config.setB3(X25_DTE_DTE);

        B3Configuration b3config = new B3Configuration(X25_DTE_DTE.getBitField());
        config.setB3Config(b3config);

        AdditionalInfo info = new AdditionalInfo();
        BChannelInformation bChannelInfo = new BChannelInformation();
        info.setBinfo(bChannelInfo);
        config.setAdditionalInfo(info);
        
    }

    @Test
    public void testChannelConnectAndDisconnect() throws Exception {

        IsdnClientChannelFactory isdnClientFactory = new IsdnClientChannelFactory(Executors.newCachedThreadPool(),
                capiFactory, getConfigurator());
        ClientBootstrap client = new ClientBootstrap(isdnClientFactory);

        // CONNECT
        long c0 = System.currentTimeMillis();
        ChannelFuture channelFuture = client.connect(calledAddress, callingAddress);
        channelFuture.await(AWAIT_TIMEOUT);
        long c1 = System.currentTimeMillis();
        LOGGER.debug("testChannelConnectAndDisconnect() :: Connect performed in {}ms", (c1 - c0));

        // few delay to simulate a network application activity
//        Thread.sleep(10);

        // DISCONNECT
        long d0 = System.currentTimeMillis();
        ChannelFuture closeFuture = channelFuture.getChannel().close();
        closeFuture.await(AWAIT_TIMEOUT);
        long d1 = System.currentTimeMillis();
        LOGGER.debug("testChannelConnectAndDisconnect() :: Disconnect performed in {}ms", (d1 - d0));

    }

//    private void expectConnectReqOnceAndReply() throws Exception {
//
//        // CONNECT_REQ: handle and reply with CONNECT_CONF
//        mock().send(isA(ConnectRequest.class));
//        expectLastCall().andDelegateTo(new CapiAdapter() {
//            @Override
//            public void putMessage(CapiMessage msg) throws CapiException {
//
//                ConnectRequest conReq = (ConnectRequest) msg;
//
//                // enqueue CONNECT_CONF
//                ConnectConfirmation conConf = replyConnectConf(conReq);
//                scheduleReceive(conConf);
//
//                // enqueue CONNECT_ACTIVE_IND
//                ConnectActiveIndication activeInd = replyConnectActiveInd(conReq);
//                scheduleReceive(activeInd);
//            }
//        }).once();
//
//    }
//
//    private void expectConnectActiveRespOnceAndIgnore() throws Exception {
//
//        // CONNECT_ACTIVE_RESP: configure mock but does nothing
//        // handle CONNECT_B3_REQ later
//        mock().send(isA(ConnectActiveResponse.class));
//        expectLastCall().once();
//
//    }
//
//    private void expectConnectB3ReqOnceAndReply() throws Exception {
//
//        // CONNECT_B3_REQ: handle and reply with CONNECT_B3_CONF
//        mock().send(isA(ConnectB3Request.class));
//        expectLastCall().andDelegateTo(new CapiAdapter() {
//            @Override
//            public void putMessage(CapiMessage msg) throws CapiException {
//
//                ConnectB3Request conB3Req = (ConnectB3Request) msg;
//
//                // enqueue CONNECT_B3_CONF
//                ConnectB3Confirmation conConf = replyConnectB3Conf(conB3Req);
//                scheduleReceive(conConf);
//
//                // enqueue CONNECT_B3_ACTIVE_IND
//                ConnectB3ActiveIndication activeInd = replyConnectB3ActiveInd(conB3Req);
//                scheduleReceive(activeInd);
//            }
//
//        }).once();
//
//    }
//
//    private void expectConnectB3ActiveRespOnceAndIgnore() throws Exception {
//
//        // CONNECT_B3_ACTIVE_RESP: configure mock but does nothing
//        mock().send(isA(ConnectB3ActiveResponse.class));
//        expectLastCall().once();
//
//    }
//
//    private void expectDisconnectB3ReqOnceAndReply() throws Exception {
//
//        // DISCONNECT_B3_REQ: handle and reply with DISCONNECT_B3_CONF
//        mock().send(isA(DisconnectB3Request.class));
//        expectLastCall().andDelegateTo(new CapiAdapter() {
//            @Override
//            public void putMessage(CapiMessage msg) throws CapiException {
//
//                DisconnectB3Request disB3Req = (DisconnectB3Request) msg;
//
//                // enqueue DISCONNECT_B3_CONF
//                DisconnectB3Confirmation disConf = replyDisconnectB3Conf(disB3Req);
//                scheduleReceive(disConf);
//
//            }
//
//        }).once();
//
//    }
//
//    private void expectDisconnectReqOnceAndReply() throws Exception {
//
//        // DISCONNECT_REQ: handle and reply with DISCONNECT_CONF
//        mock().send(isA(DisconnectRequest.class));
//        expectLastCall().andDelegateTo(new CapiAdapter() {
//            @Override
//            public void putMessage(CapiMessage msg) throws CapiException {
//
//                DisconnectRequest disReq = (DisconnectRequest) msg;
//
//                // enqueue DISCONNECT_CONF
//                DisconnectConfirmation disConf = replyDisconnectConf(disReq);
//                scheduleReceive(disConf);
//
//            }
//
//        }).once();
//
//    }

    private DisconnectConfirmation replyDisconnectConf(DisconnectRequest disReq) throws CapiException {
        DisconnectConfirmation disConf = new DisconnectConfirmation(disReq.getAppID(), disReq.getMessageID());
        disConf.setPlci(disReq.getPlci());
        disConf.setInfo(REQUEST_ACCEPTED.getCode());
        return disConf;
    }

    private DisconnectB3Confirmation replyDisconnectB3Conf(DisconnectB3Request disB3Req) throws CapiException {
        DisconnectB3Confirmation disConf = new DisconnectB3Confirmation(disB3Req.getAppID(), disB3Req.getMessageID());
        disConf.setNcci(disB3Req.getNcci());
        disConf.setInfo(REQUEST_ACCEPTED.getCode());
        return disConf;
    }

    private ConnectConfirmation replyConnectConf(ConnectRequest conReq) throws CapiException {
        ConnectConfirmation conConf = new ConnectConfirmation(conReq.getAppID(), conReq.getMessageID());
        conConf.setPlci(getPlci());
        conConf.setInfo(REQUEST_ACCEPTED.getCode());
        return conConf;
    }

    private ConnectActiveIndication replyConnectActiveInd(ConnectRequest conReq) throws CapiException {

        ConnectActiveIndication activeInd = new ConnectActiveIndication(getAppId(), getMessageNumberAndIncrement());
        activeInd.setPlci(getPlci());

        if (conReq.getLowLayerCompatibility() != null) {
            activeInd.setLowLayerCompatibility(conReq.getLowLayerCompatibility());
        }

        ConnectedNumber connectedNumber = new ConnectedNumber();
        connectedNumber.setNumber(conReq.getCalledPartyNumber().getNumber());
        connectedNumber.setNumberingPlan(conReq.getCalledPartyNumber().getNumberingPlan());
        connectedNumber.setNumberType(conReq.getCalledPartyNumber().getNumberType());
        connectedNumber.setPresentationIndicator(conReq.getCallingPartyNumber().getPresentationIndicator());
        connectedNumber.setScreeningIndicator(conReq.getCallingPartyNumber().getScreeningIndicator());
        activeInd.setConnectedPartyNumber(connectedNumber);

        if (conReq.getCalledPartySubAddress() != null) {
            ConnectedSubAddress connectedSubAddress = new ConnectedSubAddress();
            connectedSubAddress.setNumber(conReq.getCalledPartySubAddress().getNumber());
            connectedSubAddress.setOddEvenIndicator(conReq.getCalledPartySubAddress().isOddEvenIndicator());
            connectedSubAddress.setSubaddressType(conReq.getCalledPartySubAddress().getSubaddressType());
            activeInd.setConnectedPartySubAddress(connectedSubAddress);
        }

        return activeInd;
    }

    private ConnectB3Confirmation replyConnectB3Conf(ConnectB3Request req) throws CapiException {
        ConnectB3Confirmation conf = new ConnectB3Confirmation(req.getAppID(), req.getMessageID());
        conf.setNcci(getNcci());
        conf.setInfo(REQUEST_ACCEPTED.getCode());
        return conf;
    }

    private ConnectB3ActiveIndication replyConnectB3ActiveInd(ConnectB3Request req) throws CapiException {
        ConnectB3ActiveIndication ind = new ConnectB3ActiveIndication(req.getAppID(), getMessageNumberAndIncrement(),
                req.getB3Protocol());
        ind.setNcci(getNcci());
        ind.setNcpi(req.getNcpi());
        return ind;
    }

}
