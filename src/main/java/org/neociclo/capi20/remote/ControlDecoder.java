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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.neociclo.capi20.remote.message.ControlMessage;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
@ChannelPipelineCoverage("all")
class ControlDecoder extends FrameDecoder {

    public ControlDecoder() {
        super();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buf) throws Exception {

        if (buf == null || !buf.readable()) {
            return null;
        }

        buf.markReaderIndex();

        int totalLength = buf.readUnsignedShort();
        if (buf.readableBytes() < (totalLength - 2)) {
            buf.resetReaderIndex();
            return null;
        }

//        buf.discardReadBytes();
        ChannelBuffer messageBuf = buf.readSlice(totalLength - 2);

        // construct type-specific decoded object
        ControlMessage controlMsg = ControlMessageBuilder.build(messageBuf);

        return controlMsg;
    }

}
