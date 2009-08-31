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
import static org.neociclo.capi20.util.Bits.*;
import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import java.util.ArrayList;

import net.sourceforge.jcapi.message.parameter.NCCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Flag;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class DataB3Ind extends ReceiveMessage {

    private NCCI ncci;
    private int dataLength;
    private int dataHandle;
    private int flags;
    private byte[] b3Data;

    public DataB3Ind(ChannelBuffer buffer) {
        super(DATA_B3_IND, buffer);
    }

    public NCCI getNcci() {
        return ncci;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getDataHandle() {
        return dataHandle;
    }

    public Flag[] getFlags() {
        ArrayList<Flag> set = new ArrayList<Flag>();
        for (Flag o : Flag.values()) {
            if (hasFlag(o)) {
                set.add(o);
            }
        }
        if (set.isEmpty()) {
            return null;
        } else {
            return (set.toArray(new Flag[set.size()]));
        }
    }

    public boolean hasFlag(Flag flag) {
        return (isBitSet(flags, flag.getBitField()));
    }

    public byte[] getB3Data() {
        return b3Data;
    }

    protected void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    protected void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    protected void setDataHandle(int dataHandle) {
        this.dataHandle = dataHandle;
    }

    protected void setRawFlags(int value) {
        this.flags = value;
    }

    protected void setB3Data(byte[] b3Data) {
        this.b3Data = b3Data;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setNcci(readNcci(buf));
        buf.skipBytes(DWORD_SIZE); // skip 32-bits data pointer
        setDataLength(readWord(buf));
        setDataHandle(readWord(buf));
        setRawFlags(readWord(buf));
//        buf.skipBytes(QWORD_SIZE); // TODO skip 64-bits data pointer ???

        byte[] data = new byte[dataLength];
        buf.readBytes(data);
        setB3Data(data);
    }

}
