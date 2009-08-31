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

import static org.neociclo.capi20.util.CapiBuffers.OCTET_SIZE;
import static org.neociclo.capi20.util.CapiBuffers.readWord;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
