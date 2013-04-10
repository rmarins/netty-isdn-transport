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
package org.neociclo.isdn;

import java.io.Serializable;

import org.neociclo.capi20.parameter.NumberType;
import org.neociclo.capi20.parameter.NumberingPlan;
import org.neociclo.capi20.parameter.PresentationIndicator;
import org.neociclo.capi20.parameter.ScreeningIndicator;
import org.neociclo.capi20.parameter.SubAddressType;

/**
 * @author Rafael Marins
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
