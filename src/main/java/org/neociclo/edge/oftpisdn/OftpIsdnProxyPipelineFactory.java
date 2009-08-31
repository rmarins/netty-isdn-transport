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
package org.neociclo.edge.oftpisdn;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.neociclo.isdn.netty.channel.IsdnClientChannelFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class OftpIsdnProxyPipelineFactory implements ChannelPipelineFactory {

    private IsdnClientChannelFactory factory;
    private String calledAddress;
    private String callingAddress;

    public OftpIsdnProxyPipelineFactory(IsdnClientChannelFactory isdnFactory, String calledAddr, String callingAddr) {
        this.factory = isdnFactory;
        this.calledAddress = calledAddr;
        this.callingAddress = callingAddr;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = pipeline();

        // stream transmission buffer codec
        p.addLast("decoder", new StbDecoder());
        p.addLast("encoder", new StbEncoder());

        // proxy handler
        p.addLast("handler", new OftpIsdnProxyInboundHandler(factory, calledAddress, callingAddress));

        return p;
    }

}
