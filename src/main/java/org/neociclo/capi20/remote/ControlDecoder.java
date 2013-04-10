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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.neociclo.capi20.remote.message.ControlMessage;

/**
 * @author Rafael Marins
 */
@Sharable
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
