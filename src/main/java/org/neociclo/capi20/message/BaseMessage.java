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

import static java.lang.String.format;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public abstract class BaseMessage implements CapiMessage {

    private MessageType type;
    private int appID;
    private int messageID;

    public BaseMessage(MessageType type) {
        this(0, type, 0);
    }

    public BaseMessage(int appID, MessageType type, int messageID) {
        super();
        if (type == null) {
            throw new NullPointerException("messageType");
        }
        this.type = type;
        this.appID = appID;
        this.messageID = messageID;
    }

    public abstract ChannelBuffer getBuffer();

    public abstract int getTotalLength();

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getRawOctets() {
        ChannelBuffer buf = getBuffer();
        buf.readerIndex(0);
        byte[] raw = new byte[buf.readableBytes()];
        buf.readBytes(raw);
        return raw;
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d)", getClass().getSimpleName(), getAppID(), getMessageID());
    }

}
