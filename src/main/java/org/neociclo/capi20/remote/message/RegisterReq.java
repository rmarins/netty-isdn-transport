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
package org.neociclo.capi20.remote.message;

import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class RegisterReq extends ControlMessage {

    public static final int SIZE_OF_REGISTER_REQ = SIZE_OF_MESSAGE_HEADER + DWORD_SIZE + (WORD_SIZE * 4) + OCTET_SIZE;

    public static final byte CAPI_VERSION = 2;

    private int messageBufferSize;
    private int maxLogicalConnection;
    private int maxBDataBlocks;
    private int maxBDataLen;

    public RegisterReq(int appID, int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen) {
        super(appID, CapiOperation.REGISTER_REQ);
        this.messageBufferSize = messageBufferSize;
        this.maxLogicalConnection = maxLogicalConnection;
        this.maxBDataBlocks = maxBDataBlocks;
        this.maxBDataLen = maxBDataLen;
    }

    public int getMessageBufferSize() {
        return messageBufferSize;
    }

    public int getMaxLogicalConnection() {
        return maxLogicalConnection;
    }

    public int getMaxBDataBlocks() {
        return maxBDataBlocks;
    }

    public int getMaxBDataLen() {
        return maxBDataLen;
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public ChannelBuffer getBuffer() {
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer(SIZE_OF_REGISTER_REQ);
        writeHeader(buf, this);
        writeDword(buf, 0); // null buffer pointer
        writeWord(buf, getMessageBufferSize());
        writeWord(buf, getMaxLogicalConnection());
        writeWord(buf, getMaxBDataBlocks());
        writeWord(buf, getMaxBDataLen());
        writeOctet(buf, CAPI_VERSION);
        return buf;
    }

    @Override
    public void parse(ChannelBuffer buffer) {
        // do nothing
    }

    @Override
    public int getTotalLength() {
        return SIZE_OF_REGISTER_REQ;
    }

}
