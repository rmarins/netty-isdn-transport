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

import static java.lang.String.*;
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
