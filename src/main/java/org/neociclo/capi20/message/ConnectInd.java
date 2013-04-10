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

import static org.neociclo.capi20.message.MessageType.CONNECT_IND;
import static org.neociclo.capi20.parameter.ParameterBuffers.readAdditionalInfo;
import static org.neociclo.capi20.parameter.ParameterBuffers.readBearerCapability;
import static org.neociclo.capi20.parameter.ParameterBuffers.readCalledPartyNumber;
import static org.neociclo.capi20.parameter.ParameterBuffers.readCalledPartySubAddress;
import static org.neociclo.capi20.parameter.ParameterBuffers.readCallingPartyNumber;
import static org.neociclo.capi20.parameter.ParameterBuffers.readCallingPartySubAddress;
import static org.neociclo.capi20.parameter.ParameterBuffers.readCipValue;
import static org.neociclo.capi20.parameter.ParameterBuffers.readHighLayerCompatibility;
import static org.neociclo.capi20.parameter.ParameterBuffers.readLowLayerCompatibility;
import static org.neociclo.capi20.parameter.ParameterBuffers.readPlci;
import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.BearerCapability;
import net.sourceforge.jcapi.message.parameter.CalledPartyNumber;
import net.sourceforge.jcapi.message.parameter.CalledPartySubAddress;
import net.sourceforge.jcapi.message.parameter.CallingPartyNumber;
import net.sourceforge.jcapi.message.parameter.CallingPartySubAddress;
import net.sourceforge.jcapi.message.parameter.HighLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.LowLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.CompatibilityInformationProfile;

/**
 * @author Rafael Marins
 */
public class ConnectInd extends ReceiveMessage {

    private PLCI plci;
    private CompatibilityInformationProfile cipValue;
    private CalledPartyNumber calledPartyNumber;
    private CallingPartyNumber callingPartyNumber;
    private CalledPartySubAddress calledPartySubAddress;
    private CallingPartySubAddress callingPartySubAddress;
    private BearerCapability bearerCapability;
    private LowLayerCompatibility lowLayerCompatibility;
    private HighLayerCompatibility highLayerCompatibility;
    private AdditionalInfo additionalInfo;

    public ConnectInd(ChannelBuffer buffer) {
        super(CONNECT_IND, buffer);
    }

    public PLCI getPlci() {
        return plci;
    }

    public CompatibilityInformationProfile getCipValue() {
        return cipValue;
    }

    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    public CalledPartySubAddress getCalledPartySubAddress() {
        return calledPartySubAddress;
    }

    public CallingPartySubAddress getCallingPartySubAddress() {
        return callingPartySubAddress;
    }

    public BearerCapability getBearerCapability() {
        return bearerCapability;
    }

    public LowLayerCompatibility getLowLayerCompatibility() {
        return lowLayerCompatibility;
    }

    public HighLayerCompatibility getHighLayerCompatibility() {
        return highLayerCompatibility;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    protected void setPlci(PLCI plci) {
        this.plci = plci;
    }

    protected void setCipValue(CompatibilityInformationProfile value) {
        this.cipValue = value;
    }

    protected void setCalledPartyNumber(CalledPartyNumber number) {
        this.calledPartyNumber = number;
    }

    protected void setCallingPartyNumber(CallingPartyNumber number) {
        this.callingPartyNumber = number;
    }

    protected void setCalledPartySubAddress(CalledPartySubAddress subAddress) {
        this.calledPartySubAddress = subAddress;
    }

    protected void setCallingPartySubAddress(CallingPartySubAddress subAddress) {
        this.callingPartySubAddress = subAddress;
    }

    protected void setBearerCapability(BearerCapability bc) {
        this.bearerCapability = bc;
    }

    protected void setLowLayerCompatibility(LowLayerCompatibility llc) {
        this.lowLayerCompatibility = llc;
    }

    protected void setHighLayerCompatibility(HighLayerCompatibility hlc) {
        this.highLayerCompatibility = hlc;
    }

    protected void setAdditionalInfo(AdditionalInfo ai) {
        this.additionalInfo = ai;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setPlci(readPlci(buf));
        setCipValue(readCipValue(buf));
        setCalledPartyNumber(readCalledPartyNumber(buf));
        setCallingPartyNumber(readCallingPartyNumber(buf));
        setCalledPartySubAddress(readCalledPartySubAddress(buf));
        setCallingPartySubAddress(readCallingPartySubAddress(buf));
        setBearerCapability(readBearerCapability(buf));
        setLowLayerCompatibility(readLowLayerCompatibility(buf));
        setHighLayerCompatibility(readHighLayerCompatibility(buf));
        setAdditionalInfo(readAdditionalInfo(buf));
    }

}
