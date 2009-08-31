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

import static org.neociclo.capi20.message.MessageType.CONNECT_ACTIVE_IND;
import static org.neociclo.capi20.parameter.ParameterBuffers.readConnectedNumber;
import static org.neociclo.capi20.parameter.ParameterBuffers.readConnectedSubAddress;
import static org.neociclo.capi20.parameter.ParameterBuffers.readLowLayerCompatibility;
import static org.neociclo.capi20.parameter.ParameterBuffers.readPlci;
import net.sourceforge.jcapi.message.parameter.ConnectedNumber;
import net.sourceforge.jcapi.message.parameter.ConnectedSubAddress;
import net.sourceforge.jcapi.message.parameter.LowLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class ConnectActiveInd extends ReceiveMessage {

    private PLCI plci;
    private ConnectedNumber connectedNumber;
    private ConnectedSubAddress connectedSubAddress;
    private LowLayerCompatibility lowLayerCompatibility;

    public ConnectActiveInd(ChannelBuffer buffer) {
        super(CONNECT_ACTIVE_IND, buffer);
    }

    public PLCI getPlci() {
        return plci;
    }

    public ConnectedNumber getConnectedNumber() {
        return connectedNumber;
    }

    public ConnectedSubAddress getConnectedSubAddress() {
        return connectedSubAddress;
    }

    public LowLayerCompatibility getLowLayerCompatibility() {
        return lowLayerCompatibility;
    }

    protected void setPlci(PLCI plci) {
        this.plci = plci;
    }

    protected void setConnectedNumber(ConnectedNumber connectedNumber) {
        this.connectedNumber = connectedNumber;
    }

    protected void setConnectedSubAddress(ConnectedSubAddress connectedSubAddress) {
        this.connectedSubAddress = connectedSubAddress;
    }

    protected void setLowLayerCompatibility(LowLayerCompatibility lowLayerCompatibility) {
        this.lowLayerCompatibility = lowLayerCompatibility;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setPlci(readPlci(buf));
        setConnectedNumber(readConnectedNumber(buf));
        setConnectedSubAddress(readConnectedSubAddress(buf));
        setLowLayerCompatibility(readLowLayerCompatibility(buf));
    }

}
