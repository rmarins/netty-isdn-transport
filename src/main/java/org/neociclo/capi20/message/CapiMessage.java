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
package org.neociclo.capi20.message;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public interface CapiMessage {

    /**
     * Type definition of this message, compound of a command and subCommand
     * code pair.
     * 
     * @return
     */
    MessageType getType();

    /**
     * Unique identification number of the application. The application ID is
     * assigned to the application by <b>COMMON-ISDN-API</b> in the
     * CAPI_REGISTER operation.
     * 
     * @return
     */
    int getAppID();

    /**
     * The message sequence number identification.
     * @return
     */
    int getMessageID();

    ChannelBuffer getBuffer();

    byte[] getRawOctets();

    int getTotalLength();

}
