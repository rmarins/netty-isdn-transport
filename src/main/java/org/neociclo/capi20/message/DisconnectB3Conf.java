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

import static org.neociclo.capi20.message.MessageType.DISCONNECT_B3_CONF;
import static org.neociclo.capi20.parameter.ParameterBuffers.readInfo;
import static org.neociclo.capi20.parameter.ParameterBuffers.readNcci;
import net.sourceforge.jcapi.message.parameter.NCCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class DisconnectB3Conf extends ReceiveMessage {

    private NCCI ncci;
    private Info info;

    public DisconnectB3Conf(ChannelBuffer buffer) {
        super(DISCONNECT_B3_CONF, buffer);
    }

    public NCCI getNcci() {
        return ncci;
    }

    public Info getInfo() {
        return info;
    }

    protected void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    protected void setInfo(Info info) {
        this.info = info;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setNcci(readNcci(buf));
        setInfo(readInfo(buf));
    }

}
