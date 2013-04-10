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

import static org.neociclo.capi20.parameter.ParameterBuffers.*;
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rafael Marins
 */
public class ControlReq extends ControlMessage {

    private int controllerID;
    private ControlType type;
    private byte[] request;

    public ControlReq(int appID) {
        super(appID, CapiOperation.CONTROL_REQ);
    }

    public int getControllerID() {
        return controllerID;
    }

    public void setControllerID(int controllerID) {
        this.controllerID = controllerID;
    }

    public ControlType getType() {
        return type;
    }

    public void setType(ControlType type) {
        this.type = type;
    }

    public byte[] getRequest() {
        return request;
    }

    public void setRequest(byte[] request) {
        this.request = request;
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public ChannelBuffer getBuffer() {
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer(getTotalLength());
        writeHeader(buf, this);
        writeWord(buf, getControllerID());
        writeWord(buf, getType().intValue());

        if (request != null) {
            buf.writeBytes(request);
        } else {
            writeOctet(buf, (byte) 0);
        }

        return buf;
    }

    @Override
    public int getTotalLength() {
        return 12 + (request == null ? 1 : request.length);
    }

    @Override
    public void parse(ChannelBuffer buffer) {
        // do nothing
    }

}
