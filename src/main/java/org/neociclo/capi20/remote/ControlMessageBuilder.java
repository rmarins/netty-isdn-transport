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
package org.neociclo.capi20.remote;

import static org.neociclo.capi20.util.CapiBuffers.WORD_SIZE;
import static org.neociclo.capi20.util.CapiBuffers.readOctet;
import static org.neociclo.capi20.util.CapiBuffers.readWord;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.remote.message.CapiGetMessage;
import org.neociclo.capi20.remote.message.CapiOperation;
import org.neociclo.capi20.remote.message.ControlConf;
import org.neociclo.capi20.remote.message.ControlMessage;
import org.neociclo.capi20.remote.message.GetManufacturerConf;
import org.neociclo.capi20.remote.message.GetProfileConf;
import org.neociclo.capi20.remote.message.GetSerialNumberConf;
import org.neociclo.capi20.remote.message.GetVersionConf;
import org.neociclo.capi20.remote.message.RegisterConf;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class ControlMessageBuilder {

    public static ControlMessage build(ChannelBuffer messageBuffer) {

        // read the message header
        messageBuffer.skipBytes(WORD_SIZE); // skip the totalLength field
        short appID = readWord(messageBuffer);
        byte command = readOctet(messageBuffer);
        byte subCommand = readOctet(messageBuffer);
        messageBuffer.skipBytes(WORD_SIZE); // skip the messageID field

        // process narrow message type construction
        CapiOperation oper = CapiOperation.valueOf(command, subCommand);

        // it's a CAPI_GET_MESSAGE operation
        if (oper == null) {
            return new CapiGetMessage(messageBuffer);
        }

        switch (oper) {
        case REGISTER_CONF:
            return new RegisterConf(appID, messageBuffer);
        case GET_PROFILE_CONF:
            return new GetProfileConf(appID, messageBuffer);
        case GET_VERSION_CONF:
            return new GetVersionConf(appID, messageBuffer);
        case GET_SERIALNUMBER_CONF:
            return new GetSerialNumberConf(appID, messageBuffer);
        case GET_MANUFACTURER_CONF:
            return new GetManufacturerConf(appID, messageBuffer);
        case CONTROL_CONF:
            return new ControlConf(appID, messageBuffer);
        }

        return null;
    }

}
