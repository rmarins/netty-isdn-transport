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

import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.BProtocol;
import net.sourceforge.jcapi.message.parameter.BearerCapability;
import net.sourceforge.jcapi.message.parameter.CalledPartyNumber;
import net.sourceforge.jcapi.message.parameter.CalledPartySubAddress;
import net.sourceforge.jcapi.message.parameter.CallingPartyNumber;
import net.sourceforge.jcapi.message.parameter.CallingPartySubAddress;
import net.sourceforge.jcapi.message.parameter.Controller;
import net.sourceforge.jcapi.message.parameter.HighLayerCompatibility;
import net.sourceforge.jcapi.message.parameter.LowLayerCompatibility;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.CompatibilityInformationProfile;

/**
 * @author Rafael Marins
 */
public class ConnectReq extends SendMessage {

    private Controller controller;
    private CompatibilityInformationProfile cipValue;
    private CalledPartyNumber calledPartyNumber;
    private CallingPartyNumber callingPartyNumber;
    private CalledPartySubAddress calledPartySubAddress;
    private CallingPartySubAddress callingPartySubAddress;
    private BProtocol bProtocol;
    private BearerCapability bearerCapability;
    private LowLayerCompatibility lowLayerCompatibility;
    private HighLayerCompatibility highLayerCompatibility;
    private AdditionalInfo additionalInfo;

    public ConnectReq() {
        super(MessageType.CONNECT_REQ);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public CompatibilityInformationProfile getCipValue() {
        return cipValue;
    }

    public void setCipValue(CompatibilityInformationProfile cipValue) {
        this.cipValue = cipValue;
    }

    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }

    public CalledPartySubAddress getCalledPartySubAddress() {
        return calledPartySubAddress;
    }

    public void setCalledPartySubAddress(CalledPartySubAddress calledPartySubAddress) {
        this.calledPartySubAddress = calledPartySubAddress;
    }

    public CallingPartySubAddress getCallingPartySubAddress() {
        return callingPartySubAddress;
    }

    public void setCallingPartySubAddress(CallingPartySubAddress callingPartySubAddress) {
        this.callingPartySubAddress = callingPartySubAddress;
    }

    public BProtocol getBProtocol() {
        return bProtocol;
    }

    public void setBProtocol(BProtocol bProtocol) {
        this.bProtocol = bProtocol;
    }

    public BearerCapability getBearerCapability() {
        return bearerCapability;
    }

    public void setBearerCapability(BearerCapability bearerCapability) {
        this.bearerCapability = bearerCapability;
    }

    public LowLayerCompatibility getLowLayerCompatibility() {
        return lowLayerCompatibility;
    }

    public void setLowLayerCompatibility(LowLayerCompatibility lowLayerCompatibility) {
        this.lowLayerCompatibility = lowLayerCompatibility;
    }

    public HighLayerCompatibility getHighLayerCompatibility() {
        return highLayerCompatibility;
    }

    public void setHighLayerCompatibility(HighLayerCompatibility highLayerCompatibility) {
        this.highLayerCompatibility = highLayerCompatibility;
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
        writeController(buf, getController());
        writeCipValue(buf, getCipValue());
        writeCalledPartyNumber(buf, getCalledPartyNumber());
        writeCallingPartyNumber(buf, getCallingPartyNumber());
        writeCalledPartySubAddress(buf, getCalledPartySubAddress());
        writeCallingPartySubAddress(buf, getCallingPartySubAddress());
        writeBProtocol(buf, getBProtocol());
        writeBearerCapability(buf, getBearerCapability());
        writeLowLayerCompatibility(buf, getLowLayerCompatibility());
        writeHighLayerCompatibility(buf, getHighLayerCompatibility());
        writeAdditionalInfo(buf, getAdditionalInfo());
    }

}
