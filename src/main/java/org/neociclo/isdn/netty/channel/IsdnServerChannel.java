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

import static org.jboss.netty.channel.Channels.fireChannelOpen;
import static org.neociclo.isdn.netty.handler.IsdnClientHandlerFactory.*;

import java.net.SocketAddress;

import org.jboss.netty.channel.AbstractServerChannel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelSink;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.Controller;
import org.neociclo.capi20.SimpleCapi;
import org.neociclo.isdn.IsdnSocketAddress;
import org.neociclo.isdn.netty.handler.IsdnClientHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnServerChannel extends AbstractServerChannel implements IsdnChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnServerChannel.class);

    public static final String HANDLER_NAME = "IsdnServerChannelStateMachine";

    private final SimpleCapi capi;
    private final ControllerSelector selector;
    private final IsdnChannelConfig config;

    public IsdnServerChannel(ChannelFactory factory, ChannelPipeline pipeline, IsdnChannelSink sink, Capi capi,
            ControllerSelector controllerSelector, IsdnConfigurator configurator) {

        super(factory, pipeline, sink);

        LOGGER.trace("IsdnServerChannel()");

        if (capi == null) {
            throw new NullPointerException("capi");
        }

        this.capi = new SimpleCapi(capi);
        this.selector = controllerSelector;

        // setup isdn server channel handler StateMachine based on the protocol
        getPipeline().addFirst(HANDLER_NAME, getIsdnServerStateMachineHandler(this, HANDLER_NAME));

        // setup channel configuration
        this.config = new IsdnChannelConfig();
        if (configurator != null) {
            configurator.configureChannel(this);
        }

        // use ChannelSink to init CAPI device and callback selectController()
//        sink.initialize(this);

        // send upstream CHANNEL_OPEN event
        fireChannelOpen(this);

    }

    public IsdnSocketAddress getCalledAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public IsdnSocketAddress getCallingAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public IsdnChannelConfig getConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    public Controller getController() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setConnectFailure(Throwable cause) {
        // TODO Auto-generated method stub

    }

    public void setConnected() {
        // TODO Auto-generated method stub

    }

    public SocketAddress getLocalAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public SocketAddress getRemoteAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isBound() {
        // TODO Auto-generated method stub
        return false;
    }

}
