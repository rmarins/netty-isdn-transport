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

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
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
