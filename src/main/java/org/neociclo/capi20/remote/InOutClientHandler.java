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
package org.neociclo.capi20.remote;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.neociclo.capi20.remote.message.ControlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
@ChannelPipelineCoverage("all")
class InOutClientHandler extends SimpleChannelHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InOutClientHandler.class);

    private ControlMessage in;
    private ControlMessage out;

    public InOutClientHandler(ControlMessage inMessage) {
        super();
        this.in = inMessage;
    }

    public ControlMessage getIn() {
        return in;
    }

    public ControlMessage getOut() {
        return out;
    }

    // -------------------------------------------------------------------------
    //   SimpleChannelHandler overriding
    // -------------------------------------------------------------------------

    /**
     * Send the inMessage as CAPI Control request.
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        e.getChannel().write(in);
    }

    /**
     * Store the received message. Expected only once.
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        // cast message already decoded
        this.out = (ControlMessage) e.getMessage();
        // so close it upon receivement
        e.getChannel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        LOGGER.warn("Unexpected exception.", e.getCause());
    }
    

}
