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
package org.neociclo.capi20.util;

import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.Profile;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class CapiParameterBuilder {

    private CapiParameterBuilder() { }

    public static Profile buildProfile(ChannelBuffer message) {

        // read controller identifier
        short controllerID = readWord(message);

        short bChannelsCount = readWord(message);
        int globalOptions = readDword(message);
        int b1 = readDword(message);
        int b2 = readDword(message);
        int b3 = readDword(message);

        // skip 24-octets reserved
        message.skipBytes(24);

        // read manufacturer specific octets
        byte[] extensions = new byte[20];
        message.readBytes(extensions);

        return new Profile(controllerID, bChannelsCount, globalOptions, b1, b2, b3, extensions);
    }

}
