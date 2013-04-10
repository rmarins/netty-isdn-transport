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
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.neociclo.capi20.remote.message.ControlMessage;

/**
 * @author Rafael Marins
 */
@Sharable
class ControlEncoder extends OneToOneEncoder {

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

        // narrow the message object
        ControlMessage controlMsg = (ControlMessage) msg;

        // construct a message buffer
        ChannelBuffer buf = controlMsg.getBuffer();

        int frameLength = buf.capacity() + 2;
        ChannelBuffer encoded = ChannelBuffers.dynamicBuffer(frameLength);
        encoded.writeShort((short) (frameLength & 0xffff));
        encoded.writeBytes(buf);

        return encoded;
    }

}
