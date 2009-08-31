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

import static java.lang.String.format;
import static org.jboss.netty.channel.Channels.*;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.createConnectB3Req;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.createConnectRequest;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.createDisconnectB3Req;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.createDisconnectReq;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.replyConnectActiveResp;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.replyConnectB3ActiveResp;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.replyDisconnectB3Resp;
import static org.neociclo.isdn.netty.handler.IsdnConnectionHelper.*;
import static org.jboss.netty.buffer.ChannelBuffers.*;

import java.io.UnsupportedEncodingException;

import org.apache.mina.statemachine.annotation.State;
import org.apache.mina.statemachine.annotation.Transition;
import org.apache.mina.statemachine.annotation.Transitions;
import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.event.Event;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.message.ConnectActiveInd;
import org.neociclo.capi20.message.ConnectB3ActiveInd;
import org.neociclo.capi20.message.ConnectB3Conf;
import org.neociclo.capi20.message.ConnectConf;
import org.neociclo.capi20.message.DataB3Conf;
import org.neociclo.capi20.message.DataB3Ind;
import org.neociclo.capi20.message.DisconnectB3Conf;
import org.neociclo.capi20.message.DisconnectB3Ind;
import org.neociclo.capi20.message.DisconnectConf;
import org.neociclo.capi20.message.DisconnectInd;
import org.neociclo.capi20.parameter.Info;
import org.neociclo.capi20.parameter.Reason;
import org.neociclo.isdn.netty.channel.IsdnChannel;
import org.neociclo.isdn.netty.channel.IsdnChannelConfig;
import org.neociclo.netty.statemachine.SimpleStateMachineHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnConnectionHandler extends SimpleStateMachineHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnConnectionHandler.class);

    public static final String ISDN_CONNECTED_EVENT_ATTR = "Isdn.channelConnectedEvent";
    public static final String ISDN_CLOSE_REQUESTED_EVENT_ATTR = "Isdn.closeRequestedEvent";

    /** General state of the protocol handler; the state it is initialized. */
    @State
    public static final String PLCI = "GENERAL";
    @State(PLCI)
    public static final String PLCI_IDLE = "P-0";
    @State(PLCI)
    public static final String WF_CONNECT_CONF = "P-0.1 [WF_CONNECT_CONF]";
    @State(PLCI)
    public static final String WF_CONNECT_ACTIVE_IND = "P-1.1";
    @State(PLCI)
    public static final String PLCI_ACTIVE = "P-ACT";
    @State(PLCI)
    public static final String WF_DISCONNECT_CONF = "P-5 [WF_DISCONNECT_CONF]";
    @State(PLCI)
    public static final String DO_CONNECT_B3_REQ = "P-1.2 [DO_CONNECT_B3_REQ]";

    @State(PLCI_ACTIVE)
    public static final String NCCI_IDLE = "N-0";
    @State(PLCI_ACTIVE)
    public static final String WF_CONNECT_B3_CONF = "N-0.2 [WF_CONNECT_B3_CONF]";
    @State(PLCI_ACTIVE)
    public static final String WF_CONNECT_B3_ACTIVE_IND = "N-2 [WF_CONNECT_B3_ACTIVE_IND]";
    @State(PLCI_ACTIVE)
    public static final String NCCI_ACTIVE = "N-ACT";
    @State(PLCI_ACTIVE)
    public static final String WF_DISCONNECT_B3_CONF = "N-4 [WF_CONNECT_B3_CONF]";

    public IsdnConnectionHandler() {
        super();
    }

    // -------------------------------------------------------------------------
    // Physical Link control
    // -------------------------------------------------------------------------

    @Transition(on = CHANNEL_CONNECTED, in = PLCI_IDLE, next = WF_CONNECT_CONF)
    public void plciConnectReq(IsdnChannel channel, StateContext stateCtx, ChannelStateEvent e) throws CapiException {

        LOGGER.trace("plciConnectReq()");

        CapiMessage connectReq = createConnectRequest(channel);
        channel.write(connectReq);

        // hold ChannelEvent#CONNECTED to sendUpstream() on CONNECT_ACTIVE_IND
        stateCtx.setAttribute(ISDN_CONNECTED_EVENT_ATTR, e);

    }

    @Transition(on = MESSAGE_RECEIVED, in = WF_CONNECT_CONF, next = WF_CONNECT_ACTIVE_IND)
    public void plciConnectConf(IsdnChannel channel, ConnectConf conf) throws CapiException {

        LOGGER.trace("plciConnectConf()");

        Info response = conf.getInfo();
        if (response != Info.REQUEST_ACCEPTED) {
            LOGGER.debug("PLCI connect failed. Connect Confirmation: info = {}.", response);
            throw new CapiException(conf.getInfo(), "PLCI connect failed.");
        }

        // keep the PLCI information on IsdnChannel
        IsdnChannelConfig config = channel.getConfig();
        config.setPlci(conf.getPlci());

    }

    @Transition(on = MESSAGE_RECEIVED, in = WF_CONNECT_ACTIVE_IND, next = DO_CONNECT_B3_REQ)
    public void plciConnectActiveInd(final IsdnChannel channel, final StateContext stateCtx,
            ConnectActiveInd activeInd, final ChannelHandlerContext ctx) throws CapiException {

        LOGGER.trace("plciConnectActiveInd()");

        // TODO shoud I keep the received LLC in channel config ?

        // reply with CONNECT_ACTIVE_RESP
        CapiMessage activeResp = replyConnectActiveResp(activeInd);
        channel.write(activeResp);

//        // fire connected after write completed
//        writeFuture.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                // trigger the ncciConnectB3Req manually
//                ncciConnectB3Req(channel, stateCtx);
//            }
//        });

    }

    @Transitions ({
        @Transition(on = MESSAGE_RECEIVED, in = WF_CONNECT_CONF, next = PLCI_IDLE),
        @Transition(on = MESSAGE_RECEIVED, in = PLCI_ACTIVE, next = PLCI_IDLE)
    })
    public void plciDisconnectInd(final IsdnChannel channel, DisconnectInd disconInd) throws CapiException {

        final Reason reason = disconInd.getReason();
        LOGGER.trace("plciDisconnectInd() :: reason = {}", reason);

        CapiMessage disconResp = replyDisconnectResp(disconInd);

        ChannelFuture writeFuture = channel.write(disconResp);
        writeFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!channel.isConnected()) {
                    channel.setConnectFailure(new Exception(format("DISCONNECT_IND - %s", reason)));
                }
                close(channel);
            }
        });

    }

    /**
     * Triggered on DISCONNECT_B3_CONF or DISCONNECT_B3_RESP (NCCI level)
     * handling.
     */
    public void plciDisconnectReq(IsdnChannel channel) throws CapiException {

        LOGGER.trace("plciDisconnectReq()");

        CapiMessage disconReq = createDisconnectReq(channel);
        channel.write(disconReq);

    }

    @Transition(on = MESSAGE_RECEIVED, in = WF_DISCONNECT_CONF, next = PLCI_IDLE)
    public void plciDisconnectConf(IsdnChannel channel, StateContext stateCtx, DisconnectConf conf,
            ChannelHandlerContext ctx) throws CapiException {

        LOGGER.trace("plciDisconnectConf()");

        Info response = conf.getInfo();
        if (response != Info.REQUEST_ACCEPTED) {
            LOGGER.debug("Disconnect failed. Confirmation: info = {}.", response);
            throw new CapiException(conf.getInfo(), "Disconnect failed.");
        }

        // forward the ChannelEvent#CLOSE_REQUESTED caught on
        // ncciDisconnectB3Req() to down layers with sendDownstream()
        ChannelStateEvent closeRequested = (ChannelStateEvent) stateCtx.getAttribute(ISDN_CLOSE_REQUESTED_EVENT_ATTR);
        stateCtx.setAttribute(ISDN_CLOSE_REQUESTED_EVENT_ATTR, null);
        ctx.sendDownstream(closeRequested);

    }

    // -------------------------------------------------------------------------
    // Logical Connection control
    // -------------------------------------------------------------------------

    /**
     * Triggered on CONNECT_ACTIVE_IND handling.
     */
    @Transition( on = WRITE_COMPLETE, in = DO_CONNECT_B3_REQ, next = WF_CONNECT_B3_CONF)
    public void ncciConnectB3Req(IsdnChannel channel, StateContext stateCtx) throws CapiException {

        LOGGER.trace("ncciConnectB3Req()");

        CapiMessage connectB3Req = createConnectB3Req(channel);
        channel.write(connectB3Req);

    }

    @Transition(on = MESSAGE_RECEIVED, in = WF_CONNECT_B3_CONF, next = WF_CONNECT_B3_ACTIVE_IND)
    public void ncciConnectB3Conf(IsdnChannel channel, ConnectB3Conf conf) throws CapiException {

        LOGGER.trace("ncciConnectB3Conf()");

        Info response = conf.getInfo();
        if (response != Info.REQUEST_ACCEPTED) {
            LOGGER.debug("NCCI connect B3 failed. Confirmation: info = {}.", response);
            throw new CapiException(conf.getInfo(), "NCCI connect B3 failed.");
        }

        // store the NCCI information on LogicalChannel
        IsdnChannelConfig config = channel.getConfig();
        config.setNcci(conf.getNcci());

    }

    @Transition(on = MESSAGE_RECEIVED, in = WF_CONNECT_B3_ACTIVE_IND, next = NCCI_ACTIVE)
    public void ncciConnectB3ActiveInd(final IsdnChannel channel, final StateContext stateCtx,
            ConnectB3ActiveInd conB3ActiveInd, final ChannelHandlerContext ctx) throws CapiException {

        LOGGER.trace("ncciConnectB3ActiveInd()");

        // TODO should I keep the received NCPI in logical channel (handshake)?

        // reply with CONNECT_B3_ACTIVE_RESP
        CapiMessage conB3ActiveResp = replyConnectB3ActiveResp(conB3ActiveInd);
        ChannelFuture writeFuture = channel.write(conB3ActiveResp);

        // set connected after write completed
        writeFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {

                // set channel connected
                channel.setConnected();

                // raise the ChannelEvent#CONNECTED caught on plciConnectReq()
                // to upper layers with sendUpstream()
                ChannelStateEvent channelConnected = (ChannelStateEvent) stateCtx
                        .getAttribute(ISDN_CONNECTED_EVENT_ATTR);
                stateCtx.setAttribute(ISDN_CONNECTED_EVENT_ATTR, null);
                ctx.sendUpstream(channelConnected);

            }
        });

    }

    @Transition(on = CLOSE_REQUESTED, in = NCCI_ACTIVE, next = WF_DISCONNECT_B3_CONF)
    public void ncciDisconnectB3Req(IsdnChannel channel, StateContext stateCtx, ChannelStateEvent e)
            throws CapiException {

        LOGGER.trace("ncciDisconnectB3Req()");

        CapiMessage disconnectB3Req = createDisconnectB3Req(channel);
        channel.write(disconnectB3Req);

        // hold ChannelEvent#CLOSE_REQUESTED to sendDownstream() on
        // DISCONNECT_CONF (PLCI level)
        stateCtx.setAttribute(ISDN_CLOSE_REQUESTED_EVENT_ATTR, e);

    }

    @Transition(on = MESSAGE_RECEIVED, in = WF_DISCONNECT_B3_CONF, next = NCCI_IDLE)
    public void ncciDisconnectB3Conf(IsdnChannel channel, DisconnectB3Conf conf) throws CapiException {

        LOGGER.trace("ncciDisconnectB3Conf()");

        try {
            Info response = conf.getInfo();
            if (response != Info.REQUEST_ACCEPTED) {
                LOGGER.debug("NCCI disconnect B3 failed. Confirmation: info = {}.", response);
                throw new CapiException(conf.getInfo(), "NCCI disconnect B3 failed.");
            }
        } finally {
            // trigger the plciDisconnectReq() manually
            plciDisconnectReq(channel);
        }

    }

    @Transitions ({
        @Transition(on = MESSAGE_RECEIVED, in = WF_CONNECT_B3_CONF, next = NCCI_IDLE),
        @Transition(on = MESSAGE_RECEIVED, in = NCCI_ACTIVE, next = NCCI_IDLE)
    })
    public void ncciDisconnectB3Ind(final IsdnChannel channel, DisconnectB3Ind disconB3Ind) throws CapiException {

        LOGGER.trace("ncciDisconnectB3Ind()");

        CapiMessage disconB3Resp = replyDisconnectB3Resp(disconB3Ind);
        channel.write(disconB3Resp);

    }

    // -------------------------------------------------------------------------
    // Packet Assembler/Disassembler control
    // -------------------------------------------------------------------------

    @Transition(on = MESSAGE_RECEIVED, in = NCCI_ACTIVE)
    public void ncciDataB3Ind(IsdnChannel channel, DataB3Ind dataInd, ChannelHandlerContext ctx) throws CapiException {

        try {
            LOGGER.trace("ncciDataB3Ind() :: data = {}", new String(dataInd.getB3Data(), "US-ASCII"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.trace("ncciDataB3Ind()");
        }

        CapiMessage dataResp = replyDataB3Resp(dataInd);
        channel.write(dataResp);

        // TODO send the complete DATA (more-data bit checked) to upper layer
        ChannelBuffer data = wrappedBuffer(dataInd.getB3Data());
        Channels.fireMessageReceived(channel, data);

    }

    @Transition (on = WRITE_REQUESTED, in = NCCI_ACTIVE)
    public void ncciDataB3Req(IsdnChannel channel, ChannelBuffer message, ChannelHandlerContext ctx, ChannelEvent channelEvent) throws CapiException {

        if (message == ChannelBuffers.EMPTY_BUFFER) {
            // send flush() signal downstream
            LOGGER.warn("ncciDataB3Req() :: empty buffer");
            handleEvent(WRITE_REQUESTED, ctx, channelEvent);
            return;
        }
        
        try {
            LOGGER.trace("ncciDataB3Req() :: data = {}", message.duplicate().toString("US-ASCII"));
        } catch (Throwable t) {
            LOGGER.trace("ncciDataB3Req()");
        }

        CapiMessage dataReq = createDataB3Req(channel, message);
        channel.write(dataReq);
    }

    @Transition(on = MESSAGE_RECEIVED, in = NCCI_ACTIVE)
    public void ncciDataB3Conf(IsdnChannel channel, DataB3Conf dataConf) throws CapiException {

        LOGGER.trace("ncciDataB3Conf()");

        // TODO process the dataCon.getInfo() accordingly
    }

    // -------------------------------------------------------------------------
    // Exception handling
    // -------------------------------------------------------------------------

    @Transition(on = EXCEPTION_CAUGHT, in = PLCI, next = PLCI_IDLE)
    public void error(IsdnChannel channel, ChannelHandlerContext ctx, ExceptionEvent event) {
        LOGGER.trace("error()", event.getCause());
        if (!channel.isConnected()) {
            channel.setConnectFailure(new Exception(format("ERROR - %s", event.getCause().getMessage())));
        }
        close(channel);
    }

    /**
     * Delegate unhandled events to Netty pipeline in the correct direction.
     */
    @Transition(on = ANY, in = PLCI)
    public void unhandledEvent(Event smEvent, ChannelHandlerContext ctx, ChannelEvent channelEvent) throws Exception {

        String name = (String) smEvent.getId();
        LOGGER.trace("unhandledEvent() :: on = {} , in = {}, event = {} ", new Object[] { name,
                getStateContext(ctx).getCurrentState().getId(), channelEvent });

        handleEvent(name, ctx, channelEvent);
    }

}
