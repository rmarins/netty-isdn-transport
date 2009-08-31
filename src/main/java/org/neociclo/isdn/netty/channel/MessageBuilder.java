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
package org.neociclo.isdn.netty.channel;

import static java.lang.String.format;
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.message.ConnectActiveInd;
import org.neociclo.capi20.message.ConnectB3ActiveInd;
import org.neociclo.capi20.message.ConnectB3Conf;
import org.neociclo.capi20.message.ConnectConf;
import org.neociclo.capi20.message.DataB3Conf;
import org.neociclo.capi20.message.DataB3Ind;
import org.neociclo.capi20.message.DisconnectB3Conf;
import org.neociclo.capi20.message.DisconnectB3Ind;
import org.neociclo.capi20.message.DisconnectConf;
import org.neociclo.capi20.message.DisconnectInd;
import org.neociclo.capi20.message.InfoConf;
import org.neociclo.capi20.message.MessageType;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class MessageBuilder {

    public static CapiMessage buildMessage(ChannelBuffer buffer, IsdnChannelConfig config) throws CapiException {

        buffer.skipBytes(WORD_SIZE * 2); // skip totalLength and appID header
        // fields
        byte command = readOctet(buffer);
        byte subCommand = readOctet(buffer);

        // rewind reader cursor to initial position before passing by constructor
        // argument
        buffer.readerIndex(0);

        // select case upon messageType
        MessageType type = MessageType.valueOf(command, subCommand);

        switch (type) {
        case DATA_B3_IND:
            return new DataB3Ind(buffer);
        case DATA_B3_CONF:
            return new DataB3Conf(buffer);
        case INFO_CONF:
            return new InfoConf(buffer);
        case CONNECT_CONF:
            return new ConnectConf(buffer);
        case DISCONNECT_IND:
            return new DisconnectInd(buffer);
        case DISCONNECT_CONF:
            return new DisconnectConf(buffer);
        case CONNECT_ACTIVE_IND:
            return new ConnectActiveInd(buffer);
        case CONNECT_B3_CONF:
            return new ConnectB3Conf(buffer);
        case CONNECT_B3_ACTIVE_IND:
            return new ConnectB3ActiveInd(buffer, config.getB3());
        case DISCONNECT_B3_CONF:
            return new DisconnectB3Conf(buffer);
        case DISCONNECT_B3_IND:
            return new DisconnectB3Ind(buffer, config.getB3());
        }

        throw new CapiException(Info.EXCHANGE_ILLEGAL_COMMAND, format(
                "Unknown message type: command = 0x%02X, subCommand = 0x%02X.", command, subCommand));

    }

}
