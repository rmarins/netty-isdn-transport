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

import java.net.SocketAddress;
import java.util.ArrayList;

import org.jboss.netty.channel.AbstractServerChannel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.Controller;
import org.neociclo.capi20.SimpleCapi;
import org.neociclo.isdn.IsdnSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
final class IsdnServerChannel extends AbstractServerChannel implements IsdnChannelInternal {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnServerChannel.class);

    private final SimpleCapi capi;
    private final ControllerSelector selector;
    private final IsdnChannelConfig config;

    private boolean initialized;
    private Controller controller;

    private IsdnSocketAddress callingAddress;
    private IsdnSocketAddress calledAddress;

    private IsdnWorker worker;
    private Object interestOpsLock = new Object();

    private boolean connected;
    boolean bound;

    private int appID;

    public IsdnServerChannel(ChannelFactory factory, ChannelPipeline pipeline, IsdnServerPipelineSink sink, Capi capi,
            ControllerSelector controllerSelector, IsdnConfigurator configurator) {

        super(factory, pipeline, sink);

        LOGGER.trace("IsdnServerChannel()");

        if (capi == null) {
            throw new NullPointerException("capi");
        }

        this.capi = new SimpleCapi(capi);
        this.selector = controllerSelector;

        // setup channel configuration
        this.config = new IsdnChannelConfig();
        if (configurator != null) {
            configurator.configureChannel(this);
        }

        // use ChannelSink to init CAPI device and callback selectController()
        sink.initialize(this);

        // send upstream CHANNEL_OPEN event
        fireChannelOpen(this);

    }

    public SimpleCapi capi() {
        return capi;
    }

    public IsdnWorker worker() {
        return worker;
    }

    public IsdnSocketAddress getCalledAddress() {
        return calledAddress;
    }

    public IsdnSocketAddress getCallingAddress() {
        return callingAddress;
    }

    public SocketAddress getLocalAddress() {
        return getCalledAddress();
    }

    public SocketAddress getRemoteAddress() {
        return getCallingAddress();
    }

    public IsdnChannelConfig getConfig() {
        return config;
    }

    public Controller getController() {
        return controller;
    }

    public void setConnected() {
        this.connected = true;
    }

    public boolean isBound() {
        return isOpen() && bound;
    }

    public boolean isConnected() {
        return isOpen() && connected;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void selectController(ArrayList<Controller> controllers) {
        // do only once
        if (controller == null) {
            this.controller = selector.selectOne(this, controllers);
        }
    }

    public void setInitialized(boolean b) {
        this.initialized = b;
    }

    public Object interestOpsLock() {
        return interestOpsLock ;
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
        return String.format("%s(id %s, %s)", getClass().getSimpleName(), getId(), getLocalAddress());
    }

    void setCallingAddress(IsdnSocketAddress callingAddress) {
        this.callingAddress = callingAddress;
    }

    void setCalledAddress(IsdnSocketAddress calledAddress) {
        this.calledAddress = calledAddress;
    }

    void setWorker(IsdnWorker worker) {
        this.worker = worker;
    }

    public void setAppID(int id) {
        this.appID = id;
    }

    public int getAppID() {
        return appID;
    }

}
