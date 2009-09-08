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

import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.BProtocol;
import net.sourceforge.jcapi.message.parameter.ConnectedNumber;
import net.sourceforge.jcapi.message.parameter.ConnectedSubAddress;
import net.sourceforge.jcapi.message.parameter.LowLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Reject;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class ConnectResp extends SendMessage {

    private PLCI plci;
    private Reject response;
    private BProtocol bProtocol;
    private ConnectedNumber connectedPartyNumber;
    private ConnectedSubAddress connectedPartySubAddress;
    private LowLayerCompatibility lowLayerCompatibility;
    private AdditionalInfo additionalInfo;

    public ConnectResp() {
        super(CONNECT_RESP);
    }

    public PLCI getPlci() {
        return plci;
    }

    public void setPlci(PLCI plci) {
        this.plci = plci;
    }

    public Reject getResponse() {
        return response;
    }

    public void setResponse(Reject response) {
        this.response = response;
    }

    public BProtocol getBProtocol() {
        return bProtocol;
    }

    public void setBProtocol(BProtocol bProtocol) {
        this.bProtocol = bProtocol;
    }

    public ConnectedNumber getConnectedPartyNumber() {
        return connectedPartyNumber;
    }

    public void setConnectedPartyNumber(ConnectedNumber connectedPartyNumber) {
        this.connectedPartyNumber = connectedPartyNumber;
    }

    public ConnectedSubAddress getConnectedPartySubAddress() {
        return connectedPartySubAddress;
    }

    public void setConnectedPartySubAddress(ConnectedSubAddress connectedPartySubAddress) {
        this.connectedPartySubAddress = connectedPartySubAddress;
    }

    public LowLayerCompatibility getLowLayerCompatibility() {
        return lowLayerCompatibility;
    }

    public void setLowLayerCompatibility(LowLayerCompatibility lowLayerCompatibility) {
        this.lowLayerCompatibility = lowLayerCompatibility;
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
        writeReject(buf, getResponse());
        writeBProtocol(buf, getBProtocol());
        writeConnectedNumber(buf, getConnectedPartyNumber());
        writeConnectedSubAddress(buf, getConnectedPartySubAddress());
        writeLowLayerCompatibility(buf, getLowLayerCompatibility());
        writeAdditionalInfo(buf, getAdditionalInfo());
    }

}
