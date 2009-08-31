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

import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
