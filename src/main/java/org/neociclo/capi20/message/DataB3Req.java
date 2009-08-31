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

import static org.neociclo.capi20.util.CapiBuffers.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import java.util.Arrays;

import net.sourceforge.jcapi.message.parameter.NCCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Flag;
import org.neociclo.capi20.util.Bits;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
