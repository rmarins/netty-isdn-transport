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

import static java.lang.String.*;
import static org.neociclo.capi20.remote.RemoteCapiHelper.*;
import static org.neociclo.capi20.util.CapiBuffers.readStruct;
import static org.neociclo.capi20.parameter.Info.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sourceforge.jcapi.rcapi.message.parameter.UserData;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
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

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class ApplicationController {

    private static final int CONTROL_APP_ID = 0xffff;

    @ChannelPipelineCoverage("all")
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
        }

        public ControlMessage receive() throws CapiException {
            if (received == null) {
                throw new CapiException(Info.EXCHANGE_QUEUE_EMPTY, "CAPI_GET_MESSAGE failure. No incoming message in queue.");
            }
            ControlMessage message = received;
            received = null;
            return message;
        }

        public void lockUntilReceive() throws CapiException {
            try {
                received = incoming.take();
            } catch (InterruptedException e) {
                throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, "CAPI_WAIT_FOR_SIGNAL failure. Message receive await interrupted.");
            }
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ControlMessage message = (ControlMessage) e.getMessage();
            incoming.put(message);
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
            throw new CapiException(conf.getInfo(), format("CAPI_REGISTER failure. Remote-CAPI reponse: %s", conf
                    .getInfo()));
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
            throw new CapiException(REGISTER_UNAVAILABLE_RESOURCES_ERROR, "CONTROL -> Unexpected error on get challenge.");
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
            t.printStackTrace();
            throw new CapiException(REGISTER_UNAVAILABLE_RESOURCES_ERROR, "CONTROL -> Remote-CAPI authentication error.");
        }
        
    }

    public void putMessage(byte[] message) throws CapiException {
        ChannelBuffer buf = ChannelBuffers.wrappedBuffer(message);
        CapiPutMessage req = new CapiPutMessage(buf);
        handler.send(channel, req);
    }

    public void waitForSignal() throws CapiException {
        handler.lockUntilReceive();
    }

    public byte[] getMessage() throws CapiException {

        ControlMessage message;
        message = handler.receive();

        return message.getOctets();
    }

    public void release() throws CapiException {

        checkConnected(channel);
        remoteCapiClose(channel);

        handler = null;
        channel = null;

    }

    private void initializeAndConnect() throws CapiException {

        if (channel != null && channel.isConnected()) {
            throw new CapiException(Info.REGISTER_UNAVAILABLE_RESOURCES_ERROR, format(
                    "CAPI_REGISTER failure. Remote-CAPI already registered/connected: %s.", channel));
        }

        handler = new ApplicationHandler();
        channel = remoteCapiConnect(remoteAddress, handler);
    }

}
