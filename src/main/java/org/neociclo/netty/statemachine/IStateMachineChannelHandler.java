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
package org.neociclo.netty.statemachine;

import java.net.SocketAddress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.WriteCompletionEvent;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public interface IStateMachineChannelHandler {

    public static final String ANY = "*";
    public static final String CHANNEL_OPEN = "channelOpen";
    public static final String EXCEPTION_CAUGHT = "exceptionCaught";
    public static final String MESSAGE_RECEIVED = "messageReceived";
    public static final String CHANNEL_BOUND = "channelBound";
    public static final String CHANNEL_CONNECTED = "channelConnected";
    public static final String INTEREST_CHANGED = "channelInterestChanged";
    public static final String CHANNEL_DISCONNECTED = "channelDisconnected";
    public static final String CHANNEL_UNBOUND = "channelUnbound";
    public static final String CHANNEL_CLOSED = "channelClosed";
    public static final String WRITE_COMPLETE = "writeComplete";
    public static final String CHILD_OPEN = "childChannelOpen";
    public static final String CHILD_CLOSED = "childChannelClosed";
    public static final String WRITE_REQUESTED = "writeRequested";
    public static final String BIND_REQUESTED = "bindRequested";
    public static final String CONNECT_REQUESTED = "connectRequested";
    public static final String SET_INTEREST_OPS_REQUESTED = "setInterestOpsRequested";
    public static final String UNBIND_REQUESTED = "unbindRequested";
    public static final String CLOSE_REQUESTED = "closeRequested";

    /**
     * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
     * from a remote peer.
     */
    void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception;

    /**
     * Invoked when an exception was raised by an I/O thread or a
     * {@link ChannelHandler}.
     */
    void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel} is open, but not bound nor connected.
     */
    void channelOpen(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel} is open and bound to a local address,
     * but not connected.
     */
    void channelBound(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel} is open, bound to a local address, and
     * connected to a remote address.
     */
    void channelConnected(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel}'s {@link Channel#getInterestOps() interestOps}
     * was changed.
     */
    void channelInterestChanged(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel} was disconnected from its remote peer.
     */
    void channelDisconnected(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel} was unbound from the current local address.
     */
    void channelUnbound(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when a {@link Channel} was closed and all its related resources
     * were released.
     */
    void channelClosed(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when something was written into a {@link Channel}.
     */
    void writeComplete(ChannelHandlerContext context, WriteCompletionEvent event) throws Exception;

    /**
     * Invoked when a child {@link Channel} was open.
     * (e.g. a server channel accepted a connection)
     */
    void childChannelOpen(ChannelHandlerContext context, ChildChannelStateEvent event) throws Exception;

    /**
     * Invoked when a child {@link Channel} was closed.
     * (e.g. the accepted connection was closed)
     */
    void childChannelClosed(ChannelHandlerContext context, ChildChannelStateEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#write(Object)} is called.
     */
    void writeRequested(ChannelHandlerContext context, MessageEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#bind(SocketAddress)} was called.
     */
    void bindRequested(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#connect(SocketAddress)} was called.
     */
    void connectRequested(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#setInterestOps(int)} was called.
     */
    void setInterestOpsRequested(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#disconnect()} was called.
     */
    void disconnectRequested(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#unbind()} was called.
     */
    void unbindRequested(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

    /**
     * Invoked when {@link Channel#close()} was called.
     */
    void closeRequested(ChannelHandlerContext context, ChannelStateEvent event) throws Exception;

}
