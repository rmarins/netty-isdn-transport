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

import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.context.StateContextFactory;
import org.apache.mina.statemachine.context.StateContextLookup;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.neociclo.isdn.netty.channel.IsdnChannel;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class ChannelHandlerContextLookup implements StateContextLookup {

    private StateContextFactory contextFactory;
    private IsdnChannel channel;
    private String handlerName;

    public ChannelHandlerContextLookup(StateContextFactory contextFactory, IsdnChannel channel, String handlerName) {
        super();
        this.contextFactory = contextFactory;
        this.channel = channel;
        this.handlerName = handlerName;
    }

    public StateContext lookup(Object[] eventArgs) {

        if (channelContext() == null) {
            return null;
        }

        StateContext ctx = getAttachment();
        if (ctx == null) {
            ctx = contextFactory.create();
            setAttachment(ctx);
        }

        return ctx;
    }

    private ChannelHandlerContext channelContext() {
        return channel.getPipeline().getContext(handlerName);
    }

    private void setAttachment(StateContext ctx) {
        channelContext().setAttachment(ctx);
    }

    private StateContext getAttachment() {
        return (StateContext) channelContext().getAttachment();
    }

}
