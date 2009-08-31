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

import java.net.SocketAddress;
import java.util.ArrayList;

import org.jboss.netty.channel.AbstractChannel;
import org.jboss.netty.channel.ChannelPipeline;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.Controller;
import org.neociclo.capi20.SimpleCapi;
import org.neociclo.isdn.netty.handler.IsdnClientHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
final class IsdnCapiChannel extends AbstractChannel implements IsdnChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnCapiChannel.class);

    private volatile IsdnSocketAddress callingAddress;
    private volatile IsdnSocketAddress calledAddress;

    private final IsdnChannelConfig config;
    private final ControllerSelector controllerSelector;
    private final boolean initiator;

    volatile Thread workerThread;
    IsdnWorker worker;

    private volatile boolean connected;
    volatile boolean initialized;
    volatile boolean bound;

    final SimpleCapi capi;
    final Object interestOpsLock = new Object();

    private Controller controller;

    IsdnCapiChannel(Capi capi, boolean initiator, IsdnClientChannelFactory factory, ChannelPipeline pipeline,
            IsdnPipelineSink sink, ControllerSelector controllerSelector, IsdnConfigurator configurator) {

        super(null, factory, pipeline, sink);

        LOGGER.trace("IsdnCapiChannel()");

        if (capi == null) {
            throw new NullPointerException("capi");
        }

        this.capi = new SimpleCapi(capi);
        this.config = new IsdnChannelConfig();
        this.initiator = initiator;
        this.controllerSelector = controllerSelector;

        getPipeline().addFirst("plciHandler", IsdnClientHandlerFactory.getPhysicalLinkHandler(this, "plciHandler"));

//        getPipeline().addAfter("plciHandler", "ncciHandler",
//                IsdnClientHandlerFactory.getLogicalConnectionHandler(this, "ncciHandler"));

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
        worker.setConnected();
    }

    public void setConnectFailure(Throwable cause) {
        this.connected = false;
        worker.setConnectFailure(cause);
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

    public boolean isInitiator() {
        return initiator;
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

    @Override
    protected void setInterestOpsNow(int interestOps) {
        super.setInterestOpsNow(interestOps);
    }

    @Override
    protected boolean setClosed() {
        this.connected = false;
        return super.setClosed();
    }

    @Override
    public String toString() {
        return String.format("%s(id %s, %s => %s)", getClass().getSimpleName(), getId(), getCallingAddress(),
                getCalledAddress());
    }
}
