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

import static java.lang.String.*;
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
public abstract class ControlMessage {

    public static final int SIZE_OF_MESSAGE_HEADER = 8;

    protected static void writeHeader(ChannelBuffer buf, ControlMessage msg) {
        writeWord(buf, msg.getTotalLength());
        writeWord(buf, msg.getAppID());
        writeOctet(buf, msg.getOperation().getCommand());
        writeOctet(buf, msg.getOperation().getSubCommand());
        writeWord(buf, msg.getMessageID());
    }

    private CapiOperation operation;
    private int appID;
    private int messageID;

    public ControlMessage(int appID, CapiOperation oper) {
        super();
        this.appID = (short) (appID & 0xffff);
        this.operation = oper;
    }

    public ControlMessage(int appID, CapiOperation oper, ChannelBuffer buffer) {
        super();
        this.appID = (short) (appID & 0xffff);
        this.operation = oper;
        parse(buffer);
    }

    public abstract int getTotalLength();

    public abstract ChannelBuffer getBuffer();

    public abstract void parse(ChannelBuffer buffer);

    public CapiOperation getOperation() {
        return operation;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getAppID() {
        return appID;
    }

    public byte[] getOctets() {
        ChannelBuffer buffer = getBuffer();
        buffer.readerIndex(0);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        return data;
    }

    @Override
    public String toString() {
        return format("%s(%s, appID: %d)", getClass().getSimpleName(), getOperation(), getAppID());
    }

}
