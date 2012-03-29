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
 * @version $Rev$ $Date$
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
