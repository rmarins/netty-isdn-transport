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
package org.neociclo.isdn.netty.channel;

import java.io.Serializable;

import org.neociclo.capi20.parameter.NumberType;
import org.neociclo.capi20.parameter.NumberingPlan;
import org.neociclo.capi20.parameter.PresentationIndicator;
import org.neociclo.capi20.parameter.ScreeningIndicator;
import org.neociclo.capi20.parameter.SubAddressType;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnAddress implements Serializable {

    private static final long serialVersionUID = -2806698211720974450L;

    private String number;

    private NumberType numberType;

    private NumberingPlan numberingPlan;

    private String subAddress;

    private SubAddressType subAddressType;

    private PresentationIndicator presentation;

    private ScreeningIndicator screening;

    /**
     * Creates a new instance.
     */
    public IsdnAddress(String isdnNumber) {
        this(isdnNumber, null);
    }

    public IsdnAddress(String isdnNumber, String isdnSubAddress) {
        super();
        if (isdnNumber == null) {
            throw new NullPointerException("isdnNumber");
        }
        this.number = isdnNumber;
        this.subAddress = isdnSubAddress;
    }

    public String getNumber() {
        return number;
    }

    public String getSubAddress() {
        return subAddress;
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public void setNumberType(NumberType numberType) {
        this.numberType = numberType;
    }

    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    public SubAddressType getSubAddressType() {
        return subAddressType;
    }

    public void setSubAddressType(SubAddressType subAddressType) {
        this.subAddressType = subAddressType;
    }

    public PresentationIndicator getPresentation() {
        return presentation;
    }

    public void setPresentation(PresentationIndicator presentation) {
        this.presentation = presentation;
    }

    public ScreeningIndicator getScreening() {
        return screening;
    }

    public void setScreening(ScreeningIndicator screening) {
        this.screening = screening;
    }

}
