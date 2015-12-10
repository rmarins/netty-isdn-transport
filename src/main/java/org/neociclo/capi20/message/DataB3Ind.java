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

import static java.lang.String.format;
import static org.neociclo.capi20.util.CapiBuffers.*;
import static org.neociclo.capi20.util.Bits.*;
import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import java.util.ArrayList;
import java.util.Arrays;

import net.sourceforge.jcapi.message.parameter.NCCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Flag;

/**
 * @author Rafael Marins
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
        buf.skipBytes(QWORD_SIZE); // skip 64-bits data pointer

        byte[] data = new byte[dataLength];
        buf.readBytes(data);
        setB3Data(data);
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, ncci: %d, handle: %d, length: %d, flags: %s)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getNcci().getNcciValue(), getDataHandle(), getDataLength(), Arrays.toString(getFlags()));
    }

}
