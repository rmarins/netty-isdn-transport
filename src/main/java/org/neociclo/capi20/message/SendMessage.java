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
package org.neociclo.capi20.message;

import static org.jboss.netty.buffer.ChannelBuffers.*;
import static org.neociclo.capi20.util.CapiBuffers.writeOctet;
import static org.neociclo.capi20.util.CapiBuffers.writeWord;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
abstract class SendMessage extends BaseMessage {

    public ChannelBuffer buffer;

    public SendMessage(MessageType type) {
        super(type);
    }

    protected abstract void writeValues(ChannelBuffer buf);

    private ChannelBuffer createBuffer() {
        ChannelBuffer buf = dynamicBuffer();
        writeHeader(buf);
        writeValues(buf);
        writeTotalLength(buf);
        return buf;
    }

    protected void writeTotalLength(ChannelBuffer buf) {
        buf.markWriterIndex();
        int length = buf.writerIndex();
        buf.writerIndex(0);
        writeWord(buf, length);
        buf.resetWriterIndex();
    }

    protected void writeHeader(ChannelBuffer buf) {
        writeWord(buf, 0); // totalLength field is set later
        writeWord(buf, getAppID());
        writeOctet(buf, getType().getCommand());
        writeOctet(buf, getType().getSubCommand());
        writeWord(buf, getMessageID());
    }

    public void clearBuffer() {
        this.buffer = null;
    }

    // -------------------------------------------------------------------------
    // BaseMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public ChannelBuffer getBuffer() {
        if (buffer == null) {
            buffer = createBuffer();
        }
        return buffer;
    }

    @Override
    public int getTotalLength() {
        ChannelBuffer buf = getBuffer();
        return buf.writerIndex();
    }

}
