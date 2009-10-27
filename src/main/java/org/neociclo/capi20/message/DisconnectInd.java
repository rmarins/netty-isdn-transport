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
import static org.neociclo.capi20.message.MessageType.DISCONNECT_IND;
import static org.neociclo.capi20.parameter.ParameterBuffers.readPlci;
import static org.neociclo.capi20.parameter.ParameterBuffers.readReason;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Reason;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class DisconnectInd extends ReceiveMessage {

    private PLCI plci;
    private Reason reason;

    public DisconnectInd(ChannelBuffer buffer) {
        super(DISCONNECT_IND, buffer);
    }

    public PLCI getPlci() {
        return plci;
    }

    public Reason getReason() {
        return reason;
    }

    protected void setPlci(PLCI plci) {
        this.plci = plci;
    }

    protected void setReason(Reason reason) {
        this.reason = reason;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setPlci(readPlci(buf));
        setReason(readReason(buf));
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, plci: %d, reason: %s)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getPlci().getPlciValue(), getReason());
    }

}
