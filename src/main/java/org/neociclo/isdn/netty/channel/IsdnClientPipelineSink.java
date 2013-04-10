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

import static java.lang.Boolean.FALSE;
import static org.jboss.netty.channel.Channels.fireChannelBound;
import static org.jboss.netty.channel.Channels.fireChannelConnected;
import static org.jboss.netty.channel.Channels.fireExceptionCaught;
import static org.jboss.netty.channel.Channels.succeededFuture;

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
import org.jboss.netty.util.internal.DeadLockProofWorker;
import org.neociclo.isdn.IsdnSocketAddress;

/**
 * @author Rafael Marins
 */
class IsdnClientPipelineSink extends AbstractChannelSink {

    private Executor workerExecutor;

    public IsdnClientPipelineSink(Executor workerExecutor) {
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

    public void initialize(IsdnChannelInternal channel) {
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
        channel.setBound(true);
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
            channel.setWorker(new IsdnWorker(channel, port));
            DeadLockProofWorker.start(
            		workerExecutor,
                    new ThreadRenamingRunnable(
                            channel.worker(),
                            String.format("CLIENT IsdnWorker(appId 0x%04X): id %s, %s => %s",
                                    port,
                                    channel.getId(),
                                    channel.getLocalAddress(),
                                    channel.getRemoteAddress())));

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
