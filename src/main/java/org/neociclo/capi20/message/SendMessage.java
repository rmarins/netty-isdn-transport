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
package org.neociclo.capi20.message;

import static org.jboss.netty.buffer.ChannelBuffers.*;
import static org.neociclo.capi20.util.CapiBuffers.writeOctet;
import static org.neociclo.capi20.util.CapiBuffers.writeWord;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
