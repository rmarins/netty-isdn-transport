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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.event.DefaultEventFactory;
import org.apache.mina.statemachine.event.Event;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Rafael Marins
 */
public class NettyEventFactory extends DefaultEventFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyEventFactory.class);

    @Override
    public Event create(StateContext context, Method method, Object[] arguments) {
        List<Object> allArgs = new ArrayList<Object>(5);
        for (Object arg : arguments) {
            if (arg instanceof ChannelHandlerContext) {
                // Channel
                Channel channel = ((ChannelHandlerContext) arg).getChannel();
                allArgs.add(channel);

                // StateMachine context
                StateContext stateContext = (StateContext) ((ChannelHandlerContext) arg).getAttachment();
                if (stateContext != null) {
                    allArgs.add(stateContext);
                }

            } else if (arg instanceof MessageEvent) {
                // Message
                allArgs.add(((MessageEvent) arg).getMessage());
            }
        }

        if (allArgs.size() > 0) {
            Collections.addAll(allArgs, arguments);
            arguments = allArgs.toArray();
        }

        Event e = super.create(context, method, arguments);
        LOGGER.trace("Created: {}", e);
        return e;
    }

}
