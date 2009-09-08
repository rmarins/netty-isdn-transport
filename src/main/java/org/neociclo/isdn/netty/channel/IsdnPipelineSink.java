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

import static java.lang.Boolean.*;
import static org.jboss.netty.channel.Channels.*;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.AbstractChannelSink;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.util.ThreadRenamingRunnable;
import org.jboss.netty.util.internal.IoWorkerRunnable;
import org.neociclo.isdn.IsdnSocketAddress;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class IsdnPipelineSink extends AbstractChannelSink implements IsdnChannelSink {

//    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnPipelineSink.class);

    private Executor workerExecutor;

    public IsdnPipelineSink(Executor workerExecutor) {
        super();
        this.workerExecutor = workerExecutor;
    }

    public void eventSunk(ChannelPipeline pipeline, ChannelEvent e) throws Exception {

        IsdnClientChannel channel = (IsdnClientChannel) e.getChannel();
        ChannelFuture future = e.getFuture();
        if (e instanceof ChannelStateEvent) {
            ChannelStateEvent stateEvent = (ChannelStateEvent) e;
            ChannelState state = stateEvent.getState();
            Object value = stateEvent.getValue();
            switch (state) {
            case OPEN:
                if (FALSE.equals(value)) {
                    IsdnWorker.close(channel, future);
                }
                break;
            case BOUND:
                if (value != null) {
                    bind(channel, future, (IsdnSocketAddress) value);
                } else {
                    IsdnWorker.close(channel, future);
                }
                break;
            case CONNECTED:
                if (value != null) {
                    connect(channel, future, (IsdnSocketAddress) value);
                } else {
                    IsdnWorker.close(channel, future);
                }
                break;
            case INTEREST_OPS:
                IsdnWorker.setInterestOps(channel, future, ((Integer) value).intValue());
                break;
            }
        } else if (e instanceof MessageEvent) {
            IsdnWorker.write(channel, future, ((MessageEvent) e).getMessage());
        }

    }

    public void initialize(IsdnClientChannel channel) {
        try {
            // query CAPI controllers
            IsdnWorker.initDevice(channel);
        } catch (Throwable t) {
            fireExceptionCaught(channel, t);
        }
    }

    /**
     * Determine the <b>Calling Address</b> of ISDN channel when the
     * {@link ChannelState#BOUND} event is caught.
     * <p/>
     * Client binding procedure is generally started by the
     * {@link org.jboss.netty.bootstrap.ClientBootstrap} mechanism.
     * 
     * @param channel
     *            ISDN channel
     * @param future
     * @param callingAddress
     */
    private void bind(IsdnClientChannel channel, ChannelFuture future, IsdnSocketAddress callingAddress) {
        channel.setCallingAddress(callingAddress);
        channel.bound = true;
        fireChannelBound(channel, callingAddress);
        future.setSuccess();
    }

    private void connect(IsdnClientChannel channel, ChannelFuture future, IsdnSocketAddress calledAddress) {

        boolean workerStarted = false;

        channel.setCalledAddress(calledAddress);
        future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        try {

            int port = IsdnWorker.register(channel);
//            channel.connected = true;

            // fire events
            boolean bound = channel.isBound();
            if (!bound) {
                fireChannelBound(channel, channel.getLocalAddress());
            }

            fireChannelConnected(channel, channel.getRemoteAddress());

            // start IsdnWorker handle CAPI operations
            channel.worker = new IsdnWorker(channel, port, future);
            workerExecutor.execute(
                    new IoWorkerRunnable(
                            new ThreadRenamingRunnable(
                                    channel.worker,
                                    String.format("IsdnWorker(port %s): id %s, %s => %s",
                                            port,
                                            channel.getId(),
                                            channel.getLocalAddress(),
                                            channel.getRemoteAddress()))));

            workerStarted = true;


        } catch (Throwable t) {
            future.setFailure(t);
            fireExceptionCaught(channel, t);
        } finally {
            if (channel.isConnected() && !workerStarted) {
                IsdnWorker.close(channel, succeededFuture(channel));
            }
        }

    }

}
