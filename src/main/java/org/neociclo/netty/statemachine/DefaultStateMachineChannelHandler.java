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

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.WriteCompletionEvent;

/**
 * @author Rafael Marins
 */
public class DefaultStateMachineChannelHandler implements ChannelDownstreamHandler, ChannelUpstreamHandler {

    private IStateMachineChannelHandler stateHandler;

    public DefaultStateMachineChannelHandler(IStateMachineChannelHandler stateHandler) {
        super();
        this.stateHandler = stateHandler;
    }

    /**
     * {@inheritDoc}  Down-casts the received upstream event into more
     * meaningful sub-type event and calls an appropriate handler method with
     * the down-casted event.
     */
    public void handleUpstream(
            ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        if (e instanceof MessageEvent) {
            stateHandler.messageReceived(ctx, (MessageEvent) e);
        } else if (e instanceof WriteCompletionEvent) {
            WriteCompletionEvent evt = (WriteCompletionEvent) e;
            stateHandler.writeComplete(ctx, evt);
        } else if (e instanceof ChildChannelStateEvent) {
            ChildChannelStateEvent evt = (ChildChannelStateEvent) e;
            if (evt.getChildChannel().isOpen()) {
                stateHandler.childChannelOpen(ctx, evt);
            } else {
                stateHandler.childChannelClosed(ctx, evt);
            }
        } else if (e instanceof ChannelStateEvent) {
            ChannelStateEvent evt = (ChannelStateEvent) e;
            switch (evt.getState()) {
            case OPEN:
                if (Boolean.TRUE.equals(evt.getValue())) {
                    stateHandler.channelOpen(ctx, evt);
                } else {
                    stateHandler.channelClosed(ctx, evt);
                }
                break;
            case BOUND:
                if (evt.getValue() != null) {
                    stateHandler.channelBound(ctx, evt);
                } else {
                    stateHandler.channelUnbound(ctx, evt);
                }
                break;
            case CONNECTED:
                if (evt.getValue() != null) {
                    stateHandler.channelConnected(ctx, evt);
                } else {
                    stateHandler.channelDisconnected(ctx, evt);
                }
                break;
            case INTEREST_OPS:
                stateHandler.channelInterestChanged(ctx, evt);
                break;
            default:
                ctx.sendDownstream(e);
            }
        } else if (e instanceof ExceptionEvent) {
            stateHandler.exceptionCaught(ctx, (ExceptionEvent) e);
        } else {
            ctx.sendUpstream(e);
        }
    }

    /**
     * {@inheritDoc}  Down-casts the received downstream event into more
     * meaningful sub-type event and calls an appropriate handler method with
     * the down-casted event.
     */
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {

        if (e instanceof MessageEvent) {
            stateHandler.writeRequested(ctx, (MessageEvent) e);
        } else if (e instanceof ChannelStateEvent) {
            ChannelStateEvent evt = (ChannelStateEvent) e;
            switch (evt.getState()) {
            case OPEN:
                if (!Boolean.TRUE.equals(evt.getValue())) {
                    stateHandler.closeRequested(ctx, evt);
                }
                break;
            case BOUND:
                if (evt.getValue() != null) {
                    stateHandler.bindRequested(ctx, evt);
                } else {
                    stateHandler.unbindRequested(ctx, evt);
                }
                break;
            case CONNECTED:
                if (evt.getValue() != null) {
                    stateHandler.connectRequested(ctx, evt);
                } else {
                    stateHandler.disconnectRequested(ctx, evt);
                }
                break;
            case INTEREST_OPS:
                stateHandler.setInterestOpsRequested(ctx, evt);
                break;
            default:
                ctx.sendDownstream(e);
            }
        } else {
            ctx.sendDownstream(e);
        }
    }

}
