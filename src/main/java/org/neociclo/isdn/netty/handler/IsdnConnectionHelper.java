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
package org.neociclo.isdn.netty.handler;

import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.isdn.netty.handler.ParameterBuilder.additionalInfo;
import static org.neociclo.isdn.netty.handler.ParameterBuilder.*;
import net.sourceforge.jcapi.message.parameter.BProtocol;
import net.sourceforge.jcapi.message.parameter.NCPI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.message.ConnectActiveInd;
import org.neociclo.capi20.message.ConnectActiveResp;
import org.neociclo.capi20.message.ConnectB3ActiveInd;
import org.neociclo.capi20.message.ConnectB3ActiveResp;
import org.neociclo.capi20.message.ConnectB3Req;
import org.neociclo.capi20.message.ConnectInd;
import org.neociclo.capi20.message.ConnectReq;
import org.neociclo.capi20.message.ConnectResp;
import org.neociclo.capi20.message.DataB3Ind;
import org.neociclo.capi20.message.DataB3Req;
import org.neociclo.capi20.message.DataB3Resp;
import org.neociclo.capi20.message.DisconnectB3Ind;
import org.neociclo.capi20.message.DisconnectB3Req;
import org.neociclo.capi20.message.DisconnectB3Resp;
import org.neociclo.capi20.message.DisconnectInd;
import org.neociclo.capi20.message.DisconnectReq;
import org.neociclo.capi20.message.DisconnectResp;
import org.neociclo.capi20.message.ListenReq;
import org.neociclo.capi20.parameter.Reject;
import org.neociclo.isdn.netty.channel.IsdnChannel;
import org.neociclo.isdn.netty.channel.IsdnChannelConfig;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class IsdnConnectionHelper {

    public static DataB3Req createDataB3Req(IsdnChannel channel, ChannelBuffer data) {
        IsdnChannelConfig config = channel.getConfig();

        byte[] b3Data = new byte[data.readableBytes()];
        data.readBytes(b3Data);

        DataB3Req req=  new DataB3Req();
        req.setNcci(config.getNcci());
        req.setB3Data(b3Data);

        return req;
    }

    public static DataB3Resp replyDataB3Resp(DataB3Ind ind) {
        DataB3Resp resp = new DataB3Resp();
        resp.setAppID(ind.getAppID());
        resp.setMessageID(ind.getMessageID());
        resp.setNcci(ind.getNcci());
        resp.setDataHandle(ind.getDataHandle());
        return resp;
    }

    public static DisconnectB3Resp replyDisconnectB3Resp(DisconnectB3Ind ind) {
        DisconnectB3Resp resp = new DisconnectB3Resp();
        resp.setAppID(ind.getAppID());
        resp.setMessageID(ind.getMessageID());
        resp.setNcci(ind.getNcci());
        return resp;
    }

    public static DisconnectB3Req createDisconnectB3Req(IsdnChannel channel) throws CapiException {
        
        IsdnChannelConfig config = channel.getConfig();

        int b3Protocol = b3Protocol(config.getB3());
        NCPI ncpi = createNcpi(channel, b3Protocol, DISCONNECT_B3_REQ);

        DisconnectB3Req disconB3Req = new DisconnectB3Req();
        disconB3Req.setNcci(config.getNcci());
        disconB3Req.setNcpi(ncpi);

        return disconB3Req;

    }

    public static ConnectB3ActiveResp replyConnectB3ActiveResp(ConnectB3ActiveInd ind) throws CapiException {
        ConnectB3ActiveResp resp = new ConnectB3ActiveResp();
        resp.setNcci(ind.getNcci());
        return resp;
    }

    public static ConnectB3Req createConnectB3Req(IsdnChannel channel) throws CapiException {

        IsdnChannelConfig config = channel.getConfig();

        int b3Protocol = b3Protocol(config.getB3());
        NCPI ncpi = createNcpi(channel, b3Protocol, CONNECT_B3_REQ);

        ConnectB3Req req = new ConnectB3Req();
        req.setPlci(config.getPlci());
        req.setNcpi(ncpi);

        return req;
    }

    public static DisconnectReq createDisconnectReq(IsdnChannel channel) throws CapiException {
        
        IsdnChannelConfig config = channel.getConfig();

        DisconnectReq disconnectReq = new DisconnectReq();
        disconnectReq.setPlci(config.getPlci());
        disconnectReq.setAdditionalInfo(additionalInfo(config.getAdditionalInfo()));

        return disconnectReq;

    }

    public static DisconnectResp replyDisconnectResp(DisconnectInd ind) {
        DisconnectResp resp = new DisconnectResp();
        resp.setAppID(ind.getAppID());
        resp.setMessageID(ind.getMessageID());
        resp.setPlci(ind.getPlci());
        return resp;
    }

    public static CapiMessage createConnectRequest(IsdnChannel channel) throws CapiException {
        IsdnChannelConfig config = channel.getConfig();
        ConnectReq req = new ConnectReq();
        req.setController(controller(channel.getController().getNumber()));
        req.setCipValue(config.getCompatibilityInformationProfile());
        req.setCalledPartyNumber(calledPartyNumber(channel.getCalledAddress()));
        req.setCallingPartyNumber(callingPartNumber(channel.getCallingAddress()));

        if (hasSubAddress(channel.getCalledAddress())) {
            req.setCalledPartySubAddress(calledPartySubAddress(channel.getCalledAddress()));
        }
        if (hasSubAddress(channel.getCallingAddress())) {
            req.setCallingPartySubAddress(callingPartySubAddress(channel.getCallingAddress()));
        }

        req.setBProtocol(bProtocol(config.getB1(), config.getB2(), config.getB3(), config.getB1Config(),
                config.getB2Config(), config.getB3Config(), config.getBChannelOperation()));

        if (config.getBearerCapability() != null) {
            req.setBearerCapability(bearerCapability(config.getBearerCapability()));
        }

        if (config.getLowLayerCompatibility() != null) {
            req.setLowLayerCompatibility(lowLayerCompatibility(config.getLowLayerCompatibility()));
        }

        if (config.getHighLayerCompatibility() != null) {
            req.setHighLayerCompatibility(highLayerCompatibility(config.getHighLayerCompatibility()));
        }

        if (config.getAdditionalInfo() != null) {
            req.setAdditionalInfo(additionalInfo(config.getAdditionalInfo()));
        }

        return req;
    }

    public static ConnectActiveResp replyConnectActiveResp(ConnectActiveInd activeInd) throws CapiException {

        ConnectActiveResp activeResp = new ConnectActiveResp();
        activeResp.setPlci(activeInd.getPlci());

        return activeResp;
    }

    public static ConnectResp replyConnectResp(ConnectInd ind, Reject response, IsdnChannel channel) throws CapiException {

        IsdnChannelConfig config = channel.getConfig();

        // TODO check CONNECT_IND properly and perform handshake of connection
        // parameters against the channel configuration

        ConnectResp resp = new ConnectResp();
        resp.setAppID(ind.getAppID());
        resp.setMessageID(ind.getMessageID());

        resp.setPlci(ind.getPlci());
        resp.setResponse(response);

        BProtocol bp = new BProtocol();
        bp.setB1Protocol(b1Protocol(config.getB1()));
        bp.setB2Protocol(b2Protocol(config.getB2()));
        bp.setB3Protocol(b3Protocol(config.getB3()));
        bp.setB3Configuration(config.getB3Config());
        resp.setBProtocol(bp);

        resp.setConnectedPartyNumber(null);
        resp.setConnectedPartySubAddress(null);

        resp.setLowLayerCompatibility(config.getLowLayerCompatibility());
        resp.setAdditionalInfo(ind.getAdditionalInfo());

        return resp;
    }

    public static ListenReq createListenReq(IsdnChannel channel) {
        // TODO do implementation
        return null;
    }

}
