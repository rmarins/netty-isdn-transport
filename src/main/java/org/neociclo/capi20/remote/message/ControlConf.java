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

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
