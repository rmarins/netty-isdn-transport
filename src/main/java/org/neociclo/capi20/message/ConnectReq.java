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
 * @version $Rev$ $Date$
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
