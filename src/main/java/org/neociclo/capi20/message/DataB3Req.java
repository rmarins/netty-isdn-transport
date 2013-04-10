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

import static org.neociclo.capi20.util.CapiBuffers.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import java.util.Arrays;

import net.sourceforge.jcapi.message.parameter.NCCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Flag;
import org.neociclo.capi20.util.Bits;

/**
 * @author Rafael Marins
 */
public class DataB3Req extends SendMessage {

    public static final int SIZEOF_DATA_B3_REQ = 0x16;

    private NCCI ncci;
    private int dataLength;
    private int dataHandle;
    private int flags;
    private byte[] b3Data;

    public DataB3Req() {
        super(MessageType.DATA_B3_REQ);
    }

    public NCCI getNcci() {
        return ncci;
    }

    public void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getDataHandle() {
        return (dataHandle == 0 ? hashCode() : dataHandle);
    }

    public void setDataHandle(int dataHandle) {
        this.dataHandle = dataHandle;
    }

    public int getRawFlags() {
        return flags;
    }

    public void setFlag(Flag f) {
        this.flags = Bits.setBit(flags, f.getBitField());
    }

    public void unsetFlag(Flag f) {
        this.flags = Bits.clearBit(flags, f.getBitField());
    }

    public byte[] getB3Data() {
        return b3Data;
    }

    public void setB3Data(byte[] b3Data) {
        this.b3Data = b3Data;
    }

    // -------------------------------------------------------------------------
    // SendMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void writeValues(ChannelBuffer buf) {
        byte[] data = getB3Data();
        writeNcci(buf, getNcci());
        writeDword(buf, (data == null ? 0 : data.hashCode())); // data pointer 
        writeWord(buf, (data == null ? getDataLength() : data.length)); // data length
        writeWord(buf, getDataHandle()); // data handle
        writeWord(buf, getRawFlags());
        buf.writeBytes(data);
    }

    @Override
    protected void writeTotalLength(ChannelBuffer buf) {
        buf.markWriterIndex();
        buf.writerIndex(0);
        writeWord(buf, SIZEOF_DATA_B3_REQ);
        buf.resetWriterIndex();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(b3Data);
        result = prime * result + dataHandle;
        result = prime * result + dataLength;
        result = prime * result + flags;
        result = prime * result + ((ncci == null) ? 0 : ncci.hashCode());
        return result;
    }

}
