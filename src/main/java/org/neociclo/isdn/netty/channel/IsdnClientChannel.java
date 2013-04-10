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

import static org.neociclo.isdn.netty.handler.IsdnHandlerFactory.*;
import static org.jboss.netty.channel.Channels.*;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.jboss.netty.channel.AbstractChannel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.Controller;
import org.neociclo.capi20.SimpleCapi;
import org.neociclo.isdn.IsdnSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
final class IsdnClientChannel extends AbstractChannel implements IsdnChannelInternal {

    private Logger LOGGER = LoggerFactory.getLogger(IsdnClientChannel.class);

    private volatile IsdnSocketAddress callingAddress;
    private volatile IsdnSocketAddress calledAddress;

    private final IsdnChannelConfig config;
    private final ControllerSelector controllerSelector;

    private IsdnWorker worker;

    private boolean connected;
    private boolean initialized;
    private boolean bound;

    private final SimpleCapi capi;
    private final Object interestOpsLock = new Object();

    private Controller controller;

    IsdnClientChannel(Capi capi, ChannelFactory factory, ChannelPipeline pipeline, IsdnClientPipelineSink sink,
            ControllerSelector controllerSelector, IsdnConfigurator configurator) {

        super(null, factory, pipeline, sink);

        LOGGER.trace("IsdnCapiChannel()");

        if (capi == null) {
            throw new NullPointerException("capi");
        }

        this.capi = new SimpleCapi(capi);
        this.config = new IsdnChannelConfig();
        this.controllerSelector = controllerSelector;

        getPipeline().addFirst("IsdnClientChannelStateMachine",
                getIsdnClientStateMachineHandler(this, "IsdnClientChannelStateMachine"));

        if (configurator != null) {
            configurator.configureChannel(this);
        }

        // let ChannelSink to init and then fire channelOpen event up
        sink.initialize(this);
        fireChannelOpen(this);

    }

    public boolean isBound() {
        return isOpen() && bound;
    }

    public boolean isConnected() {
        return isOpen() && connected;
    }

    public void setConnected() {
        this.connected = true;
    }

    public IsdnSocketAddress getCalledAddress() {
        return calledAddress;
    }

    public IsdnSocketAddress getCallingAddress() {
        return callingAddress;
    }

    public IsdnChannelConfig getConfig() {
        return config;
    }

    public SocketAddress getLocalAddress() {
        return getCallingAddress();
    }

    public SocketAddress getRemoteAddress() {
        return getCalledAddress();
    }

    void setCallingAddress(IsdnSocketAddress callingAddress) {
        this.callingAddress = callingAddress;
    }

    void setCalledAddress(IsdnSocketAddress calledAddress) {
        this.calledAddress = calledAddress;
    }

    public void selectController(ArrayList<Controller> controllers) {
        // do only once
        if (controller == null) {
            this.controller = controllerSelector.selectOne(this, controllers);
        }
    }

    public Controller getController() {
        return controller;
    }

    public SimpleCapi capi() {
        return capi;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean b) {
        this.initialized = b;
    }

    @Override
    public void setInterestOpsNow(int interestOps) {
        super.setInterestOpsNow(interestOps);
    }

    @Override
    public boolean setClosed() {
        this.connected = false;
        return super.setClosed();
    }

    @Override
    public String toString() {
        return String.format("%s(id %s, %s => %s)", getClass().getSimpleName(), getId(), getCallingAddress(),
                getCalledAddress());
    }

    public IsdnWorker worker() {
        return worker;
    }

    void setWorker(IsdnWorker worker) {
        this.worker = worker;
    }

    void setBound(boolean bound) {
        this.bound = bound;
    }

    public Object interestOpsLock() {
        return interestOpsLock;
    }
}
