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
package org.neociclo.capi20.message;

import static java.lang.String.format;
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

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, plci: %d)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getPlci().getPlciValue());
    }

}
