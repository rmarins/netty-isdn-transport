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
