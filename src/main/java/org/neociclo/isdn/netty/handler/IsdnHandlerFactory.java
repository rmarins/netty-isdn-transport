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
package org.neociclo.isdn.netty.handler;

import org.apache.mina.statemachine.StateMachine;
import org.apache.mina.statemachine.StateMachineFactory;
import org.apache.mina.statemachine.StateMachineProxyBuilder;
import org.apache.mina.statemachine.annotation.Transition;
import org.apache.mina.statemachine.context.DefaultStateContextFactory;
import org.apache.mina.statemachine.context.StateContextLookup;
import org.jboss.netty.channel.ChannelHandler;
import org.neociclo.isdn.netty.channel.IsdnChannel;
import org.neociclo.netty.statemachine.ChannelAllCoverageWrapper;
import org.neociclo.netty.statemachine.IStateMachineChannelHandler;
import org.neociclo.netty.statemachine.DefaultStateMachineChannelHandler;
import org.neociclo.netty.statemachine.NettyEventFactory;
import org.neociclo.netty.statemachine.ChannelHandlerContextLookup;

/**
 * @author Rafael Marins
 */
public class IsdnHandlerFactory {

    public static ChannelHandler getIsdnClientStateMachineHandler(IsdnChannel channel, String handlerName) {

        StateMachine sm = StateMachineFactory.getInstance(Transition.class).create(IsdnConnectionHandler.PLCI_IDLE,
                new IsdnConnectionHandler());

        StateContextLookup stateContextLookup = new ChannelHandlerContextLookup(new DefaultStateContextFactory(),
                channel, handlerName);

        StateMachineProxyBuilder proxyBuilder = new StateMachineProxyBuilder();
        proxyBuilder.setName("IsdnClientChannelStateMachine");
        proxyBuilder.setStateContextLookup(stateContextLookup);
        // proxyBuilder.setEventArgumentsInterceptor(new
        // NettyEventInterceptor());
        proxyBuilder.setEventFactory(new NettyEventFactory());

        IStateMachineChannelHandler engine = proxyBuilder.create(IStateMachineChannelHandler.class, sm);
        return new ChannelAllCoverageWrapper(new DefaultStateMachineChannelHandler(engine));

    }

    public static ChannelHandler getAcceptedChannelStateMachineHandler(IsdnChannel channel, String handlerName) {

        StateMachine sm = StateMachineFactory.getInstance(Transition.class).create(
                IsdnConnectionHandler.P4_WF_CONNECT_ACTIVE_IND, new IsdnConnectionHandler());

        StateContextLookup stateContextLookup = new ChannelHandlerContextLookup(new DefaultStateContextFactory(),
                channel, handlerName);

        StateMachineProxyBuilder proxyBuilder = new StateMachineProxyBuilder();
        proxyBuilder.setName("IsdnAcceptedChannelStateMachine");
        proxyBuilder.setStateContextLookup(stateContextLookup);
        // proxyBuilder.setEventArgumentsInterceptor(new
        // NettyEventInterceptor());
        proxyBuilder.setEventFactory(new NettyEventFactory());

        IStateMachineChannelHandler engine = proxyBuilder.create(IStateMachineChannelHandler.class, sm);
        return new ChannelAllCoverageWrapper(new DefaultStateMachineChannelHandler(engine));

    }

    private IsdnHandlerFactory() {
    }

}
