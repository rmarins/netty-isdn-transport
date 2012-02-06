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
import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import java.util.Arrays;

import net.sourceforge.jcapi.message.parameter.NCCI;
import net.sourceforge.jcapi.message.parameter.NCPI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.B3Protocol;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class ConnectB3ActiveInd extends ReceiveMessage {

    private NCCI ncci;
    private NCPI ncpi;
    private B3Protocol b3Protocol;

    public ConnectB3ActiveInd(ChannelBuffer buffer, B3Protocol b3Protocol) {
        super(CONNECT_B3_ACTIVE_IND, buffer);
        this.b3Protocol = b3Protocol;
    }

    protected void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    protected void setNcpi(NCPI ncpi) {
        this.ncpi = ncpi;
    }

    public NCCI getNcci() {
        return ncci;
    }

    public NCPI getNcpi() {
        return ncpi;
    }

    public B3Protocol getB3Protocol() {
        return b3Protocol;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setNcci(readNcci(buf));
        setNcpi(readNcpi(buf, getB3Protocol(), getType()));
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, ncci: %d)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getNcci().getNcciValue());
    }
}
