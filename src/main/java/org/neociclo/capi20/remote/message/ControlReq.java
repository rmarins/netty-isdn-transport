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

import static org.neociclo.capi20.parameter.ParameterBuffers.*;
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
