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
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rafael Marins
 */
public class GetSerialNumberReq extends ControlMessage {

    public static final int SIZE_OF_GET_SERIALNUMBER_REQ = SIZE_OF_MESSAGE_HEADER + WORD_SIZE;

    private int controllerID;

    public GetSerialNumberReq(int appID, int controllerID) {
        super(appID, CapiOperation.GET_SERIALNUMBER_REQ);
        this.controllerID = controllerID;
    }

    public int getControllerID() {
        return controllerID;
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public ChannelBuffer getBuffer() {
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer(SIZE_OF_GET_SERIALNUMBER_REQ);
        writeHeader(buf, this);
        writeWord(buf, getControllerID());
        return buf;
    }

    @Override
    public void parse(ChannelBuffer buffer) {
        // do nothing
    }

    @Override
    public int getTotalLength() {
        return SIZE_OF_GET_SERIALNUMBER_REQ;
    }

}
