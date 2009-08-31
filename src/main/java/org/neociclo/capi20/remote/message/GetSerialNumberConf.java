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

import static java.lang.String.format;
import static org.neociclo.capi20.util.CapiBuffers.capiString;
import static org.neociclo.capi20.util.CapiBuffers.readStruct;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class GetSerialNumberConf extends ControlMessage {

    private ChannelBuffer buffer;

    private String serialNumber;

    public GetSerialNumberConf(short appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.GET_SERIALNUMBER_CONF, buffer);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return format("%s(%s, appID: %d, s/n: %s)", getClass().getSimpleName(),
                getOperation(), getAppID(), getSerialNumber());
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public void parse(ChannelBuffer buf) {

        byte[] serialStruct = readStruct(buf);
        serialNumber = capiString(serialStruct);

        this.buffer = buf;

    }

    @Override
    public ChannelBuffer getBuffer() {
        buffer.readerIndex(SIZE_OF_MESSAGE_HEADER);
        return buffer;
    }

    @Override
    public int getTotalLength() {
        return buffer.capacity();
    }

}
