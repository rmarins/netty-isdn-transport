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

import static java.lang.String.*;
import static org.neociclo.capi20.remote.RemoteCapiHelper.*;
import static org.neociclo.capi20.util.CapiBuffers.readStruct;
import static org.neociclo.capi20.parameter.Info.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import net.sourceforge.jcapi.rcapi.message.parameter.UserData;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.parameter.Info;
import org.neociclo.capi20.remote.message.ControlConf;
import org.neociclo.capi20.remote.message.ControlMessage;
import org.neociclo.capi20.remote.message.CapiPutMessage;
import org.neociclo.capi20.remote.message.ControlReq;
import org.neociclo.capi20.remote.message.ControlType;
import org.neociclo.capi20.remote.message.RegisterConf;
import org.neociclo.capi20.remote.message.RegisterReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
class ApplicationController {

    private static final int CONTROL_APP_ID = 0xffff;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    @Sharable
    public class ApplicationHandler extends SimpleChannelHandler {

        private BlockingQueue<ControlMessage> incoming = new ArrayBlockingQueue<ControlMessage>(2);

        private ControlMessage received;

        private int msgNum = 1;

        public ApplicationHandler() {
            super();
        }

        public void send(Channel channel, ControlMessage message) throws CapiException {
            message.setMessageID(msgNum++);
            channel.write(message);

            if (LOGGER.isTraceEnabled()) {
            	LOGGER.trace("[appId={}, remote={}] send() out with the {}",
            			new Object[] { format("0x%04X", appID), remoteAddress, message });
            }
        }

        public ControlMessage receive() throws CapiException {
            if (received == null) {
            	if (LOGGER.isDebugEnabled()) {
            		LOGGER.debug("[appId={}, remote={}] receive() invoked but #message was null.",
            				new Object[] { format("0x%04X", appID), remoteAddress });
            	}
                throw new CapiException(Info.EXCHANGE_QUEUE_EMPTY, "CAPI_GET_MESSAGE failure. No incoming message in queue.");
            }

            ControlMessage message = received;
            if (LOGGER.isTraceEnabled()) {
            	LOGGER.trace("[appId={}, remote={}] receive() returned {}",
            			new Object[] { format("0x%04X", appID), remoteAddress, message });
            }

            received = null;
            return message;
        }

        public boolean lockUntilReceive() throws CapiException {
            try {
                received = incoming.take();
            } catch (InterruptedException e) {
                throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, "CAPI_WAIT_FOR_SIGNAL failure. Message receive await interrupted.", e);
            }
            return (received != null);
        }

        public boolean lockUntilReceive(long timeoutMillis) throws CapiException {
            try {
                received = incoming.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, "CAPI_WAIT_FOR_SIGNAL failure. Message receive await interrupted.", e);
            }
            return (received != null);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ControlMessage message = (ControlMessage) e.getMessage();
            incoming.put(message);
        }

        @Override
        public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        	LOGGER.trace("[appId={}, remote={}] channelUnbound()", new Object[] { format("0x%04X", appID),
        			remoteAddress });
        	super.channelUnbound(ctx, e);
        }
        
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        	LOGGER.trace("[appId={}, remote={}] channelClosed()", new Object[] { format("0x%04X", appID),
        			remoteAddress });
        	super.channelClosed(ctx, e);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        	LOGGER.trace("[appId={}, remote={}] channelDisconnected()", new Object[] { format("0x%04X", appID),
        			remoteAddress });
        	super.channelDisconnected(ctx, e);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        	LOGGER.error(format("[appId=0x%04X, remote=%s] %s - exceptionCaught()", appID, remoteAddress, channel), e);
        	super.exceptionCaught(ctx, e);
        }
    }

    private static void checkConnected(Channel channel) throws CapiException {
        if (!channel.isConnected()) {
            throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, format("Remote-CAPI not connected: %s.", channel));
        }
    }

    final Object readLock = new Object();
    final Object writeLock = new Object();

    private InetSocketAddress remoteAddress;
    private String user;
    private String passwd;
    private int appID;

    private ApplicationHandler handler;
    private Channel channel;

    public ApplicationController(InetSocketAddress remoteAddress, String user, String passwd, int appID) {
        super();
        this.remoteAddress = remoteAddress;
        this.user = user;
        this.passwd = passwd;
        this.appID = appID;
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

    public int getAppID() {
        return appID;
    }

    public void register(int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen)
            throws CapiException {

        initializeAndConnect();
        checkConnected(channel);

        capiAuthentication();

        RegisterReq req = new RegisterReq(getAppID(), messageBufferSize, maxLogicalConnection, maxBDataBlocks,
                maxBDataLen);
        handler.send(channel, req);

        handler.lockUntilReceive();
        RegisterConf conf = (RegisterConf) handler.receive();

        if (conf.getInfo() != Info.REQUEST_ACCEPTED) {
            throw new CapiException(conf.getInfo(), format("[appId=0x%04X, remote=%s] CAPI_REGISTER failure. " +
            		"Remote-CAPI reponse: %s", appID, remoteAddress, conf.getInfo()));
        }

    }

    private void capiAuthentication() throws CapiException {

        if (user == null || "".equals(user)) {
            return;
        }

        byte[] authChallenge = null;

        try{
            // send CONTROL_REQ [get_challenge]
            ControlReq cr = new ControlReq(CONTROL_APP_ID);
            cr.setType(ControlType.GET_CHALLENGE);
            handler.send(channel, cr);
            // receive CONTROL_CONF [get_challenge]
            handler.lockUntilReceive();
            ControlConf conf = (ControlConf) handler.receive();
            if (conf.getInfo() != 1) {
                throw new CapiException(REGISTER_UNAVAILABLE_RESOURCES_ERROR, "CONTROL -> get challenge failed.");
            }
            authChallenge = readStruct(ChannelBuffers.wrappedBuffer(conf.getResponse()));
        } catch (Throwable t) {
            throw new CapiException(REGISTER_UNAVAILABLE_RESOURCES_ERROR,
            		"CONTROL -> Unexpected error on get challenge.", t);
        }

        try {
            ControlReq req = new ControlReq(CONTROL_APP_ID);
            req.setType(ControlType.SET_USER);
            UserData ud = new UserData();
            ud.setUsername(user);
            ud.setChallenge(authChallenge);
            ud.setPassword(passwd);
            req.setRequest(ud.getBytes());
            handler.send(channel, req);
            // receive CONTROL_CONF [get_challenge]
            handler.lockUntilReceive();
            ControlConf conf = (ControlConf) handler.receive();
        } catch (Throwable t) {
            throw new CapiException(REGISTER_UNAVAILABLE_RESOURCES_ERROR,
            		"CONTROL -> Remote-CAPI authentication error.", t);
        }
        
    }

    public void putMessage(byte[] message) throws CapiException {
    	checkConnected(channel);

    	ChannelBuffer buf = ChannelBuffers.wrappedBuffer(message);
        CapiPutMessage req = new CapiPutMessage(buf);
        handler.send(channel, req);
    }

    public boolean waitForSignal() throws CapiException {
//    	checkConnected(channel);
    	return handler.lockUntilReceive();
    }

	public boolean waitForSignal(long timeoutMillis) throws CapiException {
//    	checkConnected(channel);
    	return handler.lockUntilReceive(timeoutMillis);
	}

    public byte[] getMessage() throws CapiException {
    	checkConnected(channel);
        ControlMessage message = handler.receive();
        return message.getOctets();
    }

    public void release() throws CapiException {

        remoteCapiClose(channel);

        handler = null;
        channel = null;

    }

    private void initializeAndConnect() throws CapiException {

        if (channel != null && channel.isConnected()) {
            throw new CapiException(Info.REGISTER_UNAVAILABLE_RESOURCES_ERROR, format(
                    "[appId=0x%04X, remote=%s] CAPI_REGISTER failure. Remote-CAPI already registered/connected: %s.",
                    appID, remoteAddress, channel));
        }

        handler = new ApplicationHandler();
        channel = remoteCapiConnect(remoteAddress, handler);
    }

}
