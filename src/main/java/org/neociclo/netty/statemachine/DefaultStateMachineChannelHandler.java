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
 * @version $Rev$ $Date$
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
