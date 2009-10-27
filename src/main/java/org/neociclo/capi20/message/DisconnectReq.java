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

import static java.lang.String.format;
import static org.neociclo.capi20.parameter.ParameterBuffers.writeAdditionalInfo;
import static org.neociclo.capi20.parameter.ParameterBuffers.writePlci;
import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class DisconnectReq extends SendMessage {

    private PLCI plci;
    private AdditionalInfo additionalInfo;

    public DisconnectReq() {
        super(MessageType.DISCONNECT_REQ);
    }

    public PLCI getPlci() {
        return plci;
    }

    public void setPlci(PLCI plci) {
        this.plci = plci;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    // -------------------------------------------------------------------------
    // SendMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void writeValues(ChannelBuffer buf) {
        writePlci(buf, getPlci());
        writeAdditionalInfo(buf, getAdditionalInfo());
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, plci: %d)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getPlci().getPlciValue());
    }

}
