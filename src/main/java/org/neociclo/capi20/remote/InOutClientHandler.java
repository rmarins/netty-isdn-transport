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
 * @version $Rev$ $Date$
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
