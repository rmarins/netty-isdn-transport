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

import static org.neociclo.capi20.util.CapiBuffers.OCTET_SIZE;
import static org.neociclo.capi20.util.CapiBuffers.readWord;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
abstract class ReceiveMessage extends BaseMessage {

    private ChannelBuffer buffer;
    private int totalLength;

    public ReceiveMessage(MessageType type, ChannelBuffer buffer) {
        super(type);
        this.buffer = buffer;
        setHeaderValues(buffer);
        setValues(buffer);
    }

    protected abstract void setValues(ChannelBuffer buf);

    protected void setHeaderValues(ChannelBuffer buf) {
        setTotalLength(readWord(buf));
        setAppID(readWord(buf));
        buf.skipBytes(OCTET_SIZE * 2); // skip command and subCommand fields
        setMessageID(readWord(buf));
    }

    private void setTotalLength(int length) {
        this.totalLength = length;
    }

    // -------------------------------------------------------------------------
    // BaseMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public ChannelBuffer getBuffer() {
        return buffer;
    }

    @Override
    public int getTotalLength() {
        return totalLength;
    }
}
