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
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class RegisterConf extends ControlMessage {

    public static final int SIZE_OF_REGISTER_CONF = SIZE_OF_MESSAGE_HEADER + WORD_SIZE;

    private ChannelBuffer buffer;
    private Info info;

    public RegisterConf(int appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.REGISTER_CONF, buffer);
    }

    public Info getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return format("%s(%s, appID: %d, info: %s)", getClass().getSimpleName(),
                getOperation(), getAppID(), getInfo());
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public void parse(ChannelBuffer buf) {

        info = Info.valueOf(readWord(buf));
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
