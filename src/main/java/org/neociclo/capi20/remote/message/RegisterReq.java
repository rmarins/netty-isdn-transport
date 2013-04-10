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
package org.neociclo.capi20.remote.message;

import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rafael Marins
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
