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
