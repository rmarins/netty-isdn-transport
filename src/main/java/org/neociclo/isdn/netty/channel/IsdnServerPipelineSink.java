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

import static java.lang.String.format;
import static org.jboss.netty.channel.Channels.*;
import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.Info.*;
import static org.neociclo.capi20.parameter.Reject.*;
import static org.neociclo.isdn.netty.handler.ParameterHelper.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.jcapi.message.parameter.NCCI;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.channel.AbstractChannelSink;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.util.ThreadRenamingRunnable;
import org.jboss.netty.util.internal.IoWorkerRunnable;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.message.ConnectInd;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.message.ConnectResp;
import org.neociclo.capi20.message.DisconnectReq;
import org.neociclo.isdn.IsdnSocketAddress;
import org.neociclo.capi20.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
final class IsdnServerPipelineSink extends AbstractChannelSink {

    private static final Logger logger = LoggerFactory.getLogger(IsdnServerPipelineSink.class);

    private Executor workerExecutor;

    private Map<Integer, PlciConnectionHandler> plciHandlers = new HashMap<Integer, PlciConnectionHandler>();

    private AtomicInteger serverMessageCounter = new AtomicInteger(1);

    public IsdnServerPipelineSink(Executor workerExecutor) {
        super();
        this.workerExecutor = workerExecutor;
    }

    public void eventSunk(ChannelPipeline pipeline, ChannelEvent e) throws Exception {

        Channel channel = e.getChannel();
        if (channel instanceof IsdnServerChannel) {
            handleServerChannel(e);
        } else if (channel instanceof IsdnAcceptedChannel) {
            handleAcceptedChannel(e);
        }

    }

    private void handleAcceptedChannel(ChannelEvent e) {
        IsdnAcceptedChannel channel = (IsdnAcceptedChannel) e.getChannel();
        ChannelFuture future = e.getFuture();
        if (e instanceof ChannelStateEvent) {
            ChannelStateEvent event = (ChannelStateEvent) e;
            ChannelState state = event.getState();
            Object value = event.getValue();

            switch (state) {
            case OPEN:
                if (Boolean.FALSE.equals(value)) {
                    IsdnAcceptedWorker.close(channel, future);
                }
                break;
            case BOUND:
            case CONNECTED:
                if (value == null) {
                    IsdnAcceptedWorker.close(channel, future);
                }
                break;
            case INTEREST_OPS:
                IsdnAcceptedWorker.setInterestOps(channel, future, ((Integer) value).intValue());
                break;
            }

        } else if (e instanceof MessageEvent) {
            MessageEvent event = (MessageEvent) e;
            Object message = event.getMessage();
            IsdnAcceptedWorker.write(channel, future, message, serverMessageCounter);
        }
    }

    private void handleServerChannel(ChannelEvent e) {
        IsdnServerChannel channel = (IsdnServerChannel) e.getChannel();
        ChannelFuture future = e.getFuture();
        if (e instanceof ChannelStateEvent) {
            ChannelStateEvent stateEvent = (ChannelStateEvent) e;
            ChannelState state = stateEvent.getState();
            Object value = stateEvent.getValue();
            switch (state) {
            case OPEN:
                if (Boolean.FALSE.equals(value)) {
                    close(channel, future);
                }
                break;
            case BOUND:
                if (value != null) {
                    bind(channel, future, (IsdnSocketAddress) value);
                } else {
                    logger.warn("eventSunk() :: UNHANDLED (BOUND value=null) --> {}", e);
                    close(channel, future);
                }
                break;
            default:
                logger.warn("eventSunk() :: UNHANDLED --> {}", e);
            }
        }
    }

    private void close(IsdnServerChannel channel, ChannelFuture future) {
        logger.warn("CLOSE not implemented!!!");
    }

    private void bind(IsdnServerChannel channel, ChannelFuture future, IsdnSocketAddress localAddress) {

        logger.trace("bind()");

        boolean bound = false;
        boolean bossStarted = false;

        try {

            // CAPI application registering & setup listening
            int appId = IsdnWorker.register(channel);
            channel.setWorker(new IsdnWorker(channel, appId));
            boolean listening = IsdnWorker.listen(channel, future);
            bound = true;

            if (!listening) {
                // somehow LISTEN failed without raising an error
                future.setFailure(new CapiException(EXCHANGE_RESOURCE_ERROR, "LISTEN failed."));
            }

            /*
             * Indicate the success as BIND_REQUESTED event is almost complete,
             * and send upstream CHANNEL_CONNECTED event.
             */
            channel.setAppID(appId);
            channel.setCalledAddress(localAddress);
            channel.bound = true;
            future.setSuccess();
            fireChannelBound(channel, localAddress);

            Executor bossExecutor = ((IsdnServerChannelFactory) channel.getFactory()).bossExecutor();
            bossExecutor.execute(
                    new IoWorkerRunnable(
                            new ThreadRenamingRunnable(
                                    new Boss(channel, appId),
                                    format(
                                            "Isdn server boss (channelId: %d, %s)",
                                            channel.getId(),
                                            localAddress))));

            bossStarted = true;

        } catch (Throwable t) {
            future.setFailure(t);
            fireExceptionCaught(channel, t);
        } finally {
            if (!bossStarted && bound) {
                // close(channel, future);
            }
        }

    }

    public void initialize(IsdnChannelInternal channel) {
        try {
            // query CAPI controllers
            IsdnWorker.initDevice(channel);
        } catch (Throwable t) {
            fireExceptionCaught(channel, t);
        }
    }

    private static Integer getMessagePlci(CapiMessage message) {

        Integer plciValue = null;

        MessageType type = message.getType();
        switch (type) {
        case CONNECT_IND:
        case CONNECT_ACTIVE_IND:
        case DISCONNECT_IND:
        case DISCONNECT_CONF:
            try {
                Method mGetPlci = message.getClass().getMethod("getPlci", new Class[] {});
                PLCI plci = (PLCI) mGetPlci.invoke(message, new Object[] {});
                plciValue = plci.getRawValue() & 0xFFFF;
            } catch (Exception e) {
                // ignore
            }
            break;
        case CONNECT_B3_IND:
        case CONNECT_B3_ACTIVE_IND:
        case DATA_B3_IND:
        case DATA_B3_CONF:
        case DISCONNECT_B3_IND:
        case DISCONNECT_B3_CONF:
            try {
                Method mGetNcci = message.getClass().getMethod("getNcci", new Class[] {});
                NCCI ncci = (NCCI) mGetNcci.invoke(message, new Object[] {});
                plciValue = ncci.getRawValue() & 0xFFFF;
            } catch (Exception e) {
                // ignore
            }
            break;
        }

        return plciValue;
    }

    private final class Boss implements Runnable {

        private final IsdnServerChannel channel;
        private final int appID;

        Boss(IsdnServerChannel channel, int appID) {
            this.channel = channel;
            this.appID = appID;
        }

        public void run() {
            while (channel.isBound()) {
                try {

                    // wait for message
                    channel.capi().waitForSignal(appID);
                    CapiMessage message = IsdnWorker.getMessage(channel, appID);

                    // retrieve physical connection identifier from the message
                    Integer plci = getMessagePlci(message);

                    // TODO check if plci is null

                    MessageType type = message.getType();
                    if (type == CONNECT_IND) {

                        // incoming PLCI connection
                        ConnectInd connectInd = (ConnectInd) message;
                        
                        IsdnAcceptedChannel acceptedChannel = null;

                        try {
                            acceptedChannel = connectRespAndGetAccepted(channel, appID, plci, connectInd);
                        } catch (Exception e) {

                            logger.warn("Isdn incoming connection failed: {}.", connectInd);
                            logger.error("exception", e);

                            // accept failed - send DISCONNECT_REQ
                            DisconnectReq disconnectReq = createAcceptFailDisconnectReq(plci);
                            IsdnWorker.write(channel, future(channel), disconnectReq);

                            // the DISCONNECT_CONF response is discarded below
                            // on the 'else if (type == DISCONNECT_CONF) { }'
                            // in loop (a next time)

                        }

                        if (acceptedChannel == null) {
                            continue;
                        }

                        workerExecutor.execute(
                                new IoWorkerRunnable(
                                        new ThreadRenamingRunnable(
                                                acceptedChannel.worker,
                                                format("Isdn server worker (parentId: %d, channelId: %d, %s => %s)",
                                                        channel.getId(),
                                                        acceptedChannel.getId(),
                                                        acceptedChannel.getCallingAddress(),
                                                        acceptedChannel.getCalledAddress()))));
                        

                    } else {

                        // usual incoming message
                        PlciConnectionHandler ph = plciHandlers.get(plci);

                        if (ph != null ) {
                            // enqueue the message onto proper PLCI handler
                            ph.offerReceived(message);
                        } else if (type == DISCONNECT_CONF) {
                            // ignore/discard
                        } else {
                            // cannot dispatch the received message to proper channel
                            // through the PLCI connection handler
                            throw new CapiException(EXCHANGE_RESOURCE_ERROR, "No PLCI connection handler.");
                        }

                    }

                } catch (CapiException e) {
                    logger.warn("Failed to accept a connection.", e);
                }
            }
        }
    }

    private IsdnAcceptedChannel connectRespAndGetAccepted(IsdnServerChannel channel, int appID, Integer plci,
            ConnectInd connectInd) throws Exception {

        // prepare accepted channel
        IsdnSocketAddress callingAddress = callingAddress(connectInd.getCallingPartyNumber(), connectInd
                .getCallingPartySubAddress());
        IsdnSocketAddress calledAddress = calledAddress(connectInd.getCalledPartyNumber(), connectInd
                .getCalledPartySubAddress());

        PlciConnectionHandler ph = new PlciConnectionHandler(plci);

        ChannelPipeline pipeline = channel.getConfig().getPipelineFactory().getPipeline();
        IsdnAcceptedChannel accepted = new IsdnAcceptedChannel(channel, channel.getFactory(), pipeline, this,
                callingAddress, calledAddress, ph);

        // reply with connect resp
        ConnectResp resp = replyConnectResp(channel, connectInd);
        resp.setAppID(connectInd.getAppID());
        resp.setMessageID(connectInd.getMessageID());
        IsdnWorker.write(channel, future(channel), resp);

        // cache the plciHandler
        plciHandlers.put(plci, ph);

        return accepted;
    }

    private ConnectResp replyConnectResp(IsdnServerChannel channel, ConnectInd ind) {

        IsdnChannelConfig config = channel.getConfig();

        ConnectResp resp = new ConnectResp();
        resp.setPlci(ind.getPlci());
        resp.setResponse(ACCEPT_CALL);
        resp.setBProtocol(bProtocol(config));
        resp.setLowLayerCompatibility(ind.getLowLayerCompatibility());
        resp.setAdditionalInfo(ind.getAdditionalInfo());

        return resp;
    }

    private DisconnectReq createAcceptFailDisconnectReq(Integer plci) {
        DisconnectReq disconnectReq = new DisconnectReq();
        disconnectReq.setPlci(new PLCI(plci));
        return disconnectReq;
    }

}
