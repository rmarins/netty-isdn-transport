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

import static org.neociclo.capi20.message.MessageType.LISTEN_REQ;
import static org.neociclo.capi20.parameter.ParameterBuffers.writeCallingPartyNumber;
import static org.neociclo.capi20.parameter.ParameterBuffers.writeCallingPartySubAddress;
import static org.neociclo.capi20.parameter.ParameterBuffers.writeController;
import static org.neociclo.capi20.util.CapiBuffers.writeDword;
import net.sourceforge.jcapi.message.parameter.CallingPartyNumber;
import net.sourceforge.jcapi.message.parameter.CallingPartySubAddress;
import net.sourceforge.jcapi.message.parameter.Controller;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.CompatibilityInformationProfile;
import org.neociclo.capi20.parameter.InformationType;
import org.neociclo.capi20.util.BitMask;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class ListenReq extends SendMessage {

    private Controller controller;
    private int cipMask2;
    private CallingPartyNumber callingPartyNumber;
    private CallingPartySubAddress callingPartySubAddress;

    private BitMask<InformationType> infoMask = new BitMask<InformationType>(InformationType.values());

    private BitMask<CompatibilityInformationProfile> cipMask = new BitMask<CompatibilityInformationProfile>(
            CompatibilityInformationProfile.values());

    public ListenReq() {
        super(LISTEN_REQ);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    public void setCallingPartyNumber(CallingPartyNumber callingNumber) {
        this.callingPartyNumber = callingNumber;
    }

    public CallingPartySubAddress getCallingPartySubAddress() {
        return callingPartySubAddress;
    }

    public void setCallingPartySubAddress(CallingPartySubAddress callingSubAddress) {
        this.callingPartySubAddress = callingSubAddress;
    }

    public int getInfoMask() {
        return infoMask.getBitMask();
    }

    public int getCipMask() {
        return cipMask.getBitMask();
    }

    public int getCipMask2() {
        return cipMask2;
    }

    public void unsetInfoType(InformationType t) {
        infoMask.unsetOption(t);
    }

    public InformationType[] getAllInfoType() {
        return infoMask.getAllOptions();
    }

    public boolean hasInfoType(InformationType t) {
        return infoMask.hasOption(t);
    }

    public void setInfoType(InformationType t) {
        infoMask.setOption(t);
    }

    public void unsetCip(CompatibilityInformationProfile t) {
        cipMask.unsetOption(t);
    }

    public CompatibilityInformationProfile[] getAllCip() {
        return cipMask.getAllOptions();
    }

    public boolean hasCip(CompatibilityInformationProfile t) {
        return cipMask.hasOption(t);
    }

    public void setCip(CompatibilityInformationProfile t) {
        cipMask.setOption(t);
    }

    // -------------------------------------------------------------------------
    // SendMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void writeValues(ChannelBuffer buf) {
        writeController(buf, getController());
        writeDword(buf, getInfoMask());
        writeDword(buf, getCipMask());
        writeDword(buf, getCipMask2());
        writeCallingPartyNumber(buf, getCallingPartyNumber());
        writeCallingPartySubAddress(buf, getCallingPartySubAddress());
    }

}
