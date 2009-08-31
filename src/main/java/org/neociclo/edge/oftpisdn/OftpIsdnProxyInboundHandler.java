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
package org.neociclo.edge.oftpisdn;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.ThreadRenamingRunnable;
import org.neociclo.isdn.netty.channel.IsdnClientChannelFactory;
import org.neociclo.isdn.netty.channel.IsdnSocketAddress;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
@ChannelPipelineCoverage("one")
public class OftpIsdnProxyInboundHandler extends SimpleChannelUpstreamHandler {

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @ChannelPipelineCoverage("one")
    private class OutboundHandler extends SimpleChannelUpstreamHandler {

        private final Channel inboundChannel;

        OutboundHandler(Channel inboundChannel) {
            this.inboundChannel = inboundChannel;
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ChannelBuffer msg = (ChannelBuffer) e.getMessage();
            inboundChannel.write(msg);
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            closeOnFlush(inboundChannel);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            closeOnFlush(e.getChannel());
        }
    }

    private IsdnClientChannelFactory factory;
    private String calledAddress;
    private String callingAddress;

    private volatile Channel outboundChannel;

    public OftpIsdnProxyInboundHandler(IsdnClientChannelFactory factory, String calledAddress, String callingAddress) {
        this.factory = factory;
        this.calledAddress = calledAddress;
        this.callingAddress = callingAddress;
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        // Suspend incoming traffic until connected to the remote host.
        final Channel inboundChannel = e.getChannel();
        inboundChannel.setReadable(false);

        Runnable r = new Runnable() {
            public void run() {
                // Start the connection attempt.
                ClientBootstrap client = new ClientBootstrap(factory);
                client.getPipeline().addLast("handler", new OutboundHandler(inboundChannel));
                ChannelFuture f = client.connect(new IsdnSocketAddress(calledAddress), new IsdnSocketAddress(
                        callingAddress));

                outboundChannel = f.getChannel();
                f.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            // connection attempt succeed:
                            // begin to accept incoming traffic.
                            inboundChannel.setReadable(true);
                        } else {
                            // close the connection if the connection attempt
                            // has failed.
                            inboundChannel.close();
                        }
                    }
                });
            }
        };

        Runnable outboundWorker = new ThreadRenamingRunnable(r, String.format("%s :: %s --> %s",
                OftpIsdnProxyInboundHandler.class.getSimpleName(), calledAddress, callingAddress));
        
        factory.executor().execute(outboundWorker);

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        outboundChannel.write(msg);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        closeOnFlush(e.getChannel());
    }

}
