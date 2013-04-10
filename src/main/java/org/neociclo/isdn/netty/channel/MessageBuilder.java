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

import static java.lang.String.format;
import static org.neociclo.capi20.message.MessageType.CONNECT_B3_REQ;
import static org.neociclo.capi20.message.MessageType.DISCONNECT_B3_REQ;
import static org.neociclo.capi20.message.MessageType.RESET_B3_REQ;
import static org.neociclo.capi20.util.CapiBuffers.*;
import static org.neociclo.isdn.netty.handler.ParameterHelper.additionalInfo;
import static org.neociclo.isdn.netty.handler.ParameterHelper.b1Protocol;
import static org.neociclo.isdn.netty.handler.ParameterHelper.b2Protocol;
import static org.neociclo.isdn.netty.handler.ParameterHelper.b3Protocol;
import static org.neociclo.isdn.netty.handler.ParameterHelper.bProtocol;
import static org.neociclo.isdn.netty.handler.ParameterHelper.bearerCapability;
import static org.neociclo.isdn.netty.handler.ParameterHelper.calledPartyNumber;
import static org.neociclo.isdn.netty.handler.ParameterHelper.calledPartySubAddress;
import static org.neociclo.isdn.netty.handler.ParameterHelper.callingPartNumber;
import static org.neociclo.isdn.netty.handler.ParameterHelper.callingPartySubAddress;
import static org.neociclo.isdn.netty.handler.ParameterHelper.controller;
import static org.neociclo.isdn.netty.handler.ParameterHelper.createNcpi;
import static org.neociclo.isdn.netty.handler.ParameterHelper.hasSubAddress;
import static org.neociclo.isdn.netty.handler.ParameterHelper.highLayerCompatibility;
import static org.neociclo.isdn.netty.handler.ParameterHelper.lowLayerCompatibility;

import net.sourceforge.jcapi.message.parameter.BProtocol;
import net.sourceforge.jcapi.message.parameter.NCPI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.message.ConnectActiveInd;
import org.neociclo.capi20.message.ConnectActiveResp;
import org.neociclo.capi20.message.ConnectB3ActiveInd;
import org.neociclo.capi20.message.ConnectB3ActiveResp;
import org.neociclo.capi20.message.ConnectB3Conf;
import org.neociclo.capi20.message.ConnectB3Ind;
import org.neociclo.capi20.message.ConnectB3Req;
import org.neociclo.capi20.message.ConnectB3Resp;
import org.neociclo.capi20.message.ConnectConf;
import org.neociclo.capi20.message.ConnectInd;
import org.neociclo.capi20.message.ConnectReq;
import org.neociclo.capi20.message.ConnectResp;
import org.neociclo.capi20.message.DataB3Conf;
import org.neociclo.capi20.message.DataB3Ind;
import org.neociclo.capi20.message.DataB3Req;
import org.neociclo.capi20.message.DataB3Resp;
import org.neociclo.capi20.message.DisconnectB3Conf;
import org.neociclo.capi20.message.DisconnectB3Ind;
import org.neociclo.capi20.message.DisconnectB3Req;
import org.neociclo.capi20.message.DisconnectB3Resp;
import org.neociclo.capi20.message.DisconnectConf;
import org.neociclo.capi20.message.DisconnectInd;
import org.neociclo.capi20.message.DisconnectReq;
import org.neociclo.capi20.message.DisconnectResp;
import org.neociclo.capi20.message.InfoConf;
import org.neociclo.capi20.message.InfoReq;
import org.neociclo.capi20.message.ListenConf;
import org.neociclo.capi20.message.MessageType;
import org.neociclo.capi20.message.ResetB3Conf;
import org.neociclo.capi20.message.ResetB3Ind;
import org.neociclo.capi20.message.ResetB3Req;
import org.neociclo.capi20.message.ResetB3Resp;
import org.neociclo.capi20.parameter.Info;
import org.neociclo.capi20.parameter.Reject;

/**
 * @author Rafael Marins
 */
public class MessageBuilder {

    public static CapiMessage buildMessage(ChannelBuffer buffer, IsdnChannelConfig config) throws CapiException {

        buffer.skipBytes(WORD_SIZE * 2); // skip totalLength and appID header
        // fields
        byte command = readOctet(buffer);
        byte subCommand = readOctet(buffer);

        // rewind reader cursor to initial position before passing by constructor
        // argument
        buffer.readerIndex(0);

        // select case upon messageType
        MessageType type = MessageType.valueOf(command, subCommand);

        switch (type) {
        case DATA_B3_IND:
            return new DataB3Ind(buffer);
        case DATA_B3_CONF:
            return new DataB3Conf(buffer);
        case INFO_CONF:
            return new InfoConf(buffer);
        case CONNECT_CONF:
            return new ConnectConf(buffer);
        case CONNECT_IND:
            return new ConnectInd(buffer);
        case DISCONNECT_IND:
            return new DisconnectInd(buffer);
        case DISCONNECT_CONF:
            return new DisconnectConf(buffer);
        case CONNECT_ACTIVE_IND:
            return new ConnectActiveInd(buffer);
        case CONNECT_B3_CONF:
            return new ConnectB3Conf(buffer);
        case CONNECT_B3_IND:
            return new ConnectB3Ind(buffer, config.getB3());
        case CONNECT_B3_ACTIVE_IND:
            return new ConnectB3ActiveInd(buffer, config.getB3());
        case DISCONNECT_B3_CONF:
            return new DisconnectB3Conf(buffer);
        case DISCONNECT_B3_IND:
            return new DisconnectB3Ind(buffer, config.getB3());
        case LISTEN_CONF:
            return new ListenConf(buffer);
        case RESET_B3_IND:
            return new ResetB3Ind(buffer, config.getB3());
        case RESET_B3_CONF:
            return new ResetB3Conf(buffer);
        }

        throw new CapiException(Info.EXCHANGE_ILLEGAL_COMMAND, format(
                "Unknown message type: command = 0x%02X, subCommand = 0x%02X.", command, subCommand));

    }

	public static ConnectB3Resp replyConnectB3Ind(ConnectB3Ind ind, Reject response) {
	    ConnectB3Resp resp = new ConnectB3Resp();
	    resp.setAppID(ind.getAppID());
	    resp.setMessageID(ind.getMessageID());
	    resp.setNcci(ind.getNcci());
	    resp.setNcpi(ind.getNcpi());
	    resp.setResponse(response);
	    return resp;
	}

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

	public static ResetB3Req createResetB3Req(IsdnChannel channel) throws CapiException {
	    
	    IsdnChannelConfig config = channel.getConfig();
	
	    int b3Protocol = b3Protocol(config.getB3());
	    NCPI ncpi = createNcpi(channel, b3Protocol, RESET_B3_REQ);
	
	    ResetB3Req reset = new ResetB3Req();
	    reset.setNcci(config.getNcci());
	    reset.setNcpi(ncpi);
	
	    return reset;
	
	}

	public static ResetB3Resp createResetB3Resp(IsdnChannel channel) throws CapiException {
	    
	    IsdnChannelConfig config = channel.getConfig();
	
//	    int b3Protocol = b3Protocol(config.getB3());
//	    NCPI ncpi = createNcpi(channel, b3Protocol, RESET_B3_REQ);
	
	    ResetB3Resp resp = new ResetB3Resp();
	    resp.setNcci(config.getNcci());
	
	    return resp;
	
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

	public static InfoReq createInfoReq(IsdnChannel channel) throws CapiException {
		IsdnChannelConfig config = channel.getConfig();

		InfoReq req = new InfoReq();
	    req.setPlci(config.getPlci());
	    req.setAdditionalInfo(additionalInfo(config.getAdditionalInfo()));
	    req.setCalledPartyNumber(calledPartyNumber(channel.getCalledAddress()));

		return req;
	}

}
