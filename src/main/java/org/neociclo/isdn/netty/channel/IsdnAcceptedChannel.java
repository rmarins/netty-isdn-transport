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

import static org.jboss.netty.channel.Channels.*;
import static org.neociclo.isdn.netty.handler.IsdnHandlerFactory.getAcceptedChannelStateMachineHandler;

import net.sourceforge.jcapi.message.parameter.BearerCapability;
import net.sourceforge.jcapi.message.parameter.HighLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.LowLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.PLCI;
import net.sourceforge.jcapi.message.parameter.sub.B1Configuration;
import net.sourceforge.jcapi.message.parameter.sub.B2Configuration;
import net.sourceforge.jcapi.message.parameter.sub.B3Configuration;

import org.jboss.netty.channel.AbstractChannel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelSink;
import org.neociclo.capi20.Controller;
import org.neociclo.isdn.IsdnSocketAddress;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class IsdnAcceptedChannel extends AbstractChannel implements IsdnChannel {

    private final IsdnServerChannel parent;

    private final IsdnChannelConfig config;
    private final IsdnSocketAddress callingAddress;
    private final IsdnSocketAddress calledAddress;

    final PlciConnectionHandler plciHandler;
    final Object interestOpsLock = new Object();

    final IsdnAcceptedWorker worker;

    private boolean connected;

    public IsdnAcceptedChannel(
            IsdnServerChannel parent,
            ChannelFactory factory,
            ChannelPipeline pipeline,
            ChannelSink sink,
            IsdnSocketAddress calling,
            IsdnSocketAddress called,
            PlciConnectionHandler conn) {
            

        super(parent, factory, pipeline, sink);

        if (parent == null) {
            throw new NullPointerException("parent");
        }
        if (calling == null) {
            throw new NullPointerException("callingAddress");
        }
        if (called == null) {
            throw new NullPointerException("calledAddress");
        }

        this.parent = parent;
        this.config = new IsdnChannelConfig();
        this.callingAddress = calling;
        this.calledAddress = called;
        this.plciHandler = conn;

        this.worker = new IsdnAcceptedWorker(this);

        setupConfig(parent.getConfig());

        // let isdn server channel pipeline factory to create StateMachine
        // handler based on the protocol for accepted channels (incoming
        // connection)
        pipeline.addFirst("IsdnAcceptedChannelStateMachine", getAcceptedChannelStateMachineHandler(this,
                "IsdnAcceptedChannelStateMachine"));

        fireChannelOpen(this);
        fireChannelBound(this, getLocalAddress());
        fireChannelConnected(this, getRemoteAddress());
    }

    private void setupConfig(IsdnChannelConfig serverConfig) {

        // BProtocol
        config.setB1(serverConfig.getB1());
        config.setB2(serverConfig.getB2());
        config.setB3(serverConfig.getB3());
        if (serverConfig.getB1Config() != null) {
            config.setB1Config(new B1Configuration(serverConfig.getB1Config().getBytes(), serverConfig.getB1Config()
                    .getProtocol()));
        }
        if (serverConfig.getB2Config() != null) {
            config.setB2Config(new B2Configuration(serverConfig.getB2Config().getBytes(), serverConfig.getB2Config()
                    .getProtocol()));
        }
        if (serverConfig.getB3Config() != null) {
            config.setB3Config(new B3Configuration(serverConfig.getB3Config().getBytes(), serverConfig.getB3Config()
                    .getProtocol()));
        }
        config.setBChannelOperation(serverConfig.getBChannelOperation());

        // PLCI parameters
        config.setPlci(new PLCI(plciHandler.getPlci()));
        config.setCompatibilityInformationProfile(serverConfig.getCompatibilityInformationProfile());
        config.setAdditionalInfo(serverConfig.getAdditionalInfo());
        if (serverConfig.getBearerCapability() != null) {
            config.setBearerCapability(new BearerCapability(serverConfig.getBearerCapability().getBytes()));
        }
        if (serverConfig.getLowLayerCompatibility() != null) {
            config.setLowLayerCompatibility(LowLayerCompatibility.getCompatibility(serverConfig
                    .getLowLayerCompatibility().getBytes()));
        }
        if (serverConfig.getHighLayerCompatibility() != null) {
            config.setHighLayerCompatibility(HighLayerCompatibility.getCompatibility(serverConfig
                    .getHighLayerCompatibility().getBytes()));
        }
    }

    public IsdnChannelConfig getConfig() {
        return config;
    }

    public IsdnSocketAddress getLocalAddress() {
        return getCalledAddress();
    }

    public IsdnSocketAddress getRemoteAddress() {
        return getCallingAddress();
    }

    public boolean isBound() {
        return isOpen();
    }

    public boolean isConnected() {
        return isOpen() && connected;
    }

    public IsdnSocketAddress getCalledAddress() {
        return calledAddress;
    }

    public IsdnSocketAddress getCallingAddress() {
        return callingAddress;
    }

    public Controller getController() {
        return parent.getController();
    }

    public void setConnected() {
        this.connected = true;
    }

    @Override
    protected void setInterestOpsNow(int interestOps) {
        super.setInterestOpsNow(interestOps);
    }

}
