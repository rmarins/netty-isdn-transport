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

/**
 * @author Rafael Marins
 */
public class ControlConf extends ControlMessage {

    private ChannelBuffer buffer;

    private int controllerID;
    private ControlType type;
    private int info;
    private byte[] response;

    public ControlConf(int appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.CONTROL_CONF, buffer);
    }

    public int getControllerID() {
        return controllerID;
    }

    public ControlType getType() {
        return type;
    }

    public int getInfo() {
        return info;
    }

    public byte[] getResponse() {
        return response;
    }

    protected void setControllerID(int controllerID) {
        this.controllerID = controllerID;
    }

    protected void setType(ControlType type) {
        this.type = type;
    }

    protected void setInfo(int info) {
        this.info = info;
    }

    protected void setResponse(byte[] response) {
        this.response = response;
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public ChannelBuffer getBuffer() {
        buffer.readerIndex(SIZE_OF_MESSAGE_HEADER);
        return buffer;
    }

    @Override
    public int getTotalLength() {
        return buffer.capacity();
    }

    @Override
    public void parse(ChannelBuffer buf) {
        this.buffer = buf;

        setControllerID(readWord(buf));
        setType(ControlType.valueOf(readWord(buf)));
        setInfo(readWord(buf));
        if (buf.readable()) {
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            setResponse(data);
        } else {
            setResponse(null);
        }
        
    }

}
