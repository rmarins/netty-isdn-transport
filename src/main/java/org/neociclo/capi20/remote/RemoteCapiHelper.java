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

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.parameter.Info;
import org.neociclo.capi20.remote.message.ControlMessage;

/**
 * @author Rafael Marins
 */
class RemoteCapiHelper {

    /**
     * Maximum period of time (seconds) to wait for completing the
     * Request-Response CAPI Control operation.
     */
    public static final int IN_OUT_TIMEOUT_SECONDS = 30;

    public static final int CONNECT_TIMEOUT_SECONDS = 15;

    public static final int DISCONNECT_TIMEOUT_SECONDS = 15;


    public static ControlMessage capiInvoke(RemoteCapi remoteCapi, ControlMessage request) throws CapiException {

        InOutClientHandler inOut = null;
        ClientBootstrap bootstrap = null;

        try {
            // configure client bootstrap
            Executor executor = Executors.newCachedThreadPool();
            bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(executor, executor));

            // use Request-Response handler business logic
            inOut = new InOutClientHandler(request);
            bootstrap.setPipelineFactory(new RemoteCapiPipelineFactory(inOut));

            // start the connection attempt
            ChannelFuture future = bootstrap.connect(remoteCapi.getRemoteAddress());

            // wait until the connection is closed or the connection attempt
            // fails
            future.getChannel().getCloseFuture().await(IN_OUT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            // in event of connection failure, throw an exception
            if (future.getCause() != null) {
                throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, future.getCause().getMessage());
            }

        } catch (InterruptedException e) {
            return null;
        } finally {
            // shut down thread pools to exit
            bootstrap.releaseExternalResources();
        }

        return inOut.getOut();
    }

    public static void remoteCapiClose(Channel channel) {
        try {
            channel.close().await(DISCONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public static Channel remoteCapiConnect(InetSocketAddress remoteAddress, ChannelHandler handler) {

        ChannelFuture connectFuture = null;

        // configure client bootstrap
        final ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors
                .newCachedThreadPool(), Executors.newCachedThreadPool()));

        try {

            // use Request-Response handler business logic
            bootstrap.setPipelineFactory(new RemoteCapiPipelineFactory(handler));

            // start the connection attempt
            connectFuture = bootstrap.connect(remoteAddress);

            connectFuture.await(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            return null;
        } finally {
            if (connectFuture.isSuccess()) {
                // shut down thread pools on channelClosed
                connectFuture.getChannel().getCloseFuture().addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture closeFuture) throws Exception {
                    	new Thread() {
                    		@Override
                    		public void run() {
                                bootstrap.releaseExternalResources();
                    		}
                    	}.start();
                    }
                });
            } else {
                // shut down thread pools due to connection failure
                bootstrap.releaseExternalResources();

                return null;
            }
        }

        return connectFuture.getChannel();
    }

    private RemoteCapiHelper() {
    }

}
