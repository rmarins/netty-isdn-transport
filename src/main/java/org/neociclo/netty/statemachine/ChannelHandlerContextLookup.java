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

import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.context.StateContextFactory;
import org.apache.mina.statemachine.context.StateContextLookup;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.neociclo.isdn.netty.channel.IsdnChannel;

/**
 * @author Rafael Marins
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
