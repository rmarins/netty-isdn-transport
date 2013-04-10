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
