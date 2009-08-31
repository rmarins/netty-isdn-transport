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

import static org.neociclo.capi20.parameter.ParameterBuffers.*;
import net.sourceforge.jcapi.message.parameter.NCCI;
import net.sourceforge.jcapi.message.parameter.NCPI;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class DisconnectB3Req extends SendMessage {

    private NCCI ncci;
    private NCPI ncpi;

    public DisconnectB3Req() {
        super(MessageType.DISCONNECT_B3_REQ);
    }

    public NCCI getNcci() {
        return ncci;
    }

    public void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    public NCPI getNcpi() {
        return ncpi;
    }

    public void setNcpi(NCPI ncpi) {
        this.ncpi = ncpi;
    }

    // -------------------------------------------------------------------------
    // SendMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void writeValues(ChannelBuffer buf) {
        writeNcci(buf, ncci);
        writeNcpi(buf, getNcpi());
    }

}
