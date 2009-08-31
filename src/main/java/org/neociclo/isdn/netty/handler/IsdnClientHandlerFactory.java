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
import org.neociclo.netty.statemachine.NettyEventInterceptor;
import org.neociclo.netty.statemachine.ChannelHandlerContextLookup;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnClientHandlerFactory {

    public static ChannelHandler getPhysicalLinkHandler(IsdnChannel channel, String handlerName) {

        StateMachine sm = StateMachineFactory.getInstance(Transition.class).create(IsdnConnectionHandler.PLCI_IDLE,
                new IsdnConnectionHandler());

        StateContextLookup stateContextLookup = new ChannelHandlerContextLookup(new DefaultStateContextFactory(),
                channel, handlerName);

        StateMachineProxyBuilder proxyBuilder = new StateMachineProxyBuilder();
        proxyBuilder.setName("PhysicalLinkStateMachine");
        proxyBuilder.setStateContextLookup(stateContextLookup);
        proxyBuilder.setEventArgumentsInterceptor(new NettyEventInterceptor());
        // proxyBuilder.setEventFactory(new MyEventFactory());

        IStateMachineChannelHandler engine = proxyBuilder.create(IStateMachineChannelHandler.class, sm);
        return new ChannelAllCoverageWrapper(new DefaultStateMachineChannelHandler(engine));

    }

    private IsdnClientHandlerFactory() { }

}
