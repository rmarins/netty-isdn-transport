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
package org.neociclo.isdn.netty.handler;

import static net.sourceforge.jcapi.message.parameter.IPartyNumberConstants.*;
import static org.neociclo.capi20.parameter.CompatibilityInformationProfile.*;
import static org.neociclo.capi20.parameter.SubAddressType.*;

import org.apache.commons.lang.ArrayUtils;

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
import net.sourceforge.jcapi.message.parameter.NCPI;
import net.sourceforge.jcapi.message.parameter.sub.B1Configuration;
import net.sourceforge.jcapi.message.parameter.sub.B2Configuration;
import net.sourceforge.jcapi.message.parameter.sub.B3Configuration;

import org.neociclo.capi20.message.MessageType;
import org.neociclo.capi20.parameter.B1Protocol;
import org.neociclo.capi20.parameter.B2Protocol;
import org.neociclo.capi20.parameter.B3Protocol;
import org.neociclo.capi20.parameter.BChannelOperation;
import org.neociclo.capi20.parameter.CompatibilityInformationProfile;
import org.neociclo.capi20.parameter.Info;
import org.neociclo.capi20.parameter.NumberType;
import org.neociclo.capi20.parameter.NumberingPlan;
import org.neociclo.capi20.parameter.PresentationIndicator;
import org.neociclo.capi20.parameter.ScreeningIndicator;
import org.neociclo.capi20.parameter.SubAddressType;
import org.neociclo.isdn.IsdnAddress;
import org.neociclo.isdn.IsdnSocketAddress;
import org.neociclo.isdn.netty.channel.IsdnChannel;
import org.neociclo.isdn.netty.channel.IsdnChannelConfig;

/**
 * @author Rafael Marins
 */
public class ParameterHelper {

    public static NCPI createNcpi(IsdnChannel channel, int b3Protocol, MessageType messageType) {

        IsdnChannelConfig config = channel.getConfig();

        int lcn = config.getLogicalChannelIdentifier();
        boolean x25Dbit = config.isDeliveryConfirmationEnabled();
        int x25Group = x25LogicalChannelGroup(lcn);
        int x25Channel = x25Channel(lcn);

        byte[] x25AddressBlock = null;
        if (!isEmpty(config.getX25Nua())) {
        	x25AddressBlock = encodeAddressBlock(config.getX25Nua(), null);
        } else {
			x25AddressBlock = encodeAddressBlock(channel.getCalledAddress().getNumber(), channel.getCallingAddress()
					.getNumber());
        }
        byte[] facilities = (config.getFacilities() != null && config.getFacilities().hasFacilities() ? config
                .getFacilities().encoded() : new byte[] {});
        
        //x25 userdata
        byte[] userdata = new byte[] {};
        if (! ArrayUtils.isEmpty(config.getX25UserData())) {
        	userdata=config.getX25UserData();
        }

        byte[] x25Contents = new byte[x25AddressBlock.length + facilities.length + userdata.length];
        System.arraycopy(x25AddressBlock, 0, x25Contents, 0, x25AddressBlock.length);
        System.arraycopy(facilities, 0, x25Contents, x25AddressBlock.length, facilities.length);
        System.arraycopy(userdata, 0, x25Contents, x25AddressBlock.length + facilities.length, userdata.length);
        
        NCPI ncpi = new NCPI(b3Protocol, messageType.intValue());
        ncpi.setDeliveryConfirmationEnabled(x25Dbit);
        ncpi.setX25LogicalChannelGroup(x25Group);
        ncpi.setX25Channel(x25Channel);
        ncpi.setX25Contents(x25Contents);

        return ncpi;

    }

    public static byte[] encodeAddressBlock(IsdnAddress calledAddress, IsdnAddress callingAddress) {
    	return encodeAddressBlock(calledAddress.getNumber(), callingAddress.getNumber());
    }

    public static byte[] encodeAddressBlock(String calledAddress, String callingAddress) {

        byte callingAddressLength = (isEmpty(callingAddress) ? 0 : (byte) callingAddress.length());
        byte calledAddressLength = (isEmpty(calledAddress) ? 0 : (byte) calledAddress.length());

        byte digits = (byte) (callingAddressLength + calledAddressLength);
        byte totalLength = (byte) (1 + (digits / 2) + (digits % 2));

        byte[] addressBlock = new byte[totalLength];

        // addresses length octet
        short encodedAddressesLength = 0;
        encodedAddressesLength = (short) ((encodedAddressesLength | callingAddressLength) << 4);
        encodedAddressesLength = (short) (encodedAddressesLength | (calledAddressLength & 0x0f));
        addressBlock[0] = (byte) encodedAddressesLength;

        encodeAddress(calledAddress, calledAddressLength, addressBlock, 1, true);
        encodeAddress(callingAddress, callingAddressLength, addressBlock, 1 + (calledAddressLength / 2),
                isEven(calledAddressLength));

        return addressBlock;
    }

    private static void encodeAddress(String address, byte length, byte[] block, int offset, boolean aligned) {

        if (length == 0) {
            return;
        }

        char[] ca = address.toCharArray();
        byte[] addressDigits = new byte[ca.length];
        for (byte i = 0; i < ca.length; i++) {
            addressDigits[i] = (byte) (ca[i] - '0');
        }

        byte idx = 0;
        while (idx < length) {
            byte octet = block[offset];
            if (aligned) {
                octet = (byte) (octet | (addressDigits[idx] << 4));
                block[offset] = octet;
            } else {
                octet = (byte) (octet | (addressDigits[idx] & 0x0f));
                block[offset] = octet;
                offset++;
            }
            aligned = !aligned;
            idx++;
        }

    }

    private static boolean isEven(int number) {
        return (!isOdd(number));
    }

    private static boolean isEmpty(String text) {
        return (text == null || "".equals(text));
    }

    public static int x25Flags(boolean dBit) {
        return (dBit ? 0x01 : 0x00);
    }

    public static int x25Channel(int logicalChannelNumber) {
        return (logicalChannelNumber & 0xff);
    }

    public static int x25LogicalChannelGroup(int logicalChannelNumber) {
        return ((logicalChannelNumber & 0x0f00) >> 8);
    }

    public static Info info(int info) {
        return Info.valueOf(info);
    }

    public static AdditionalInfo additionalInfo(AdditionalInfo info) {
        return (info == null ? new AdditionalInfo() : info);
    }

    public static HighLayerCompatibility highLayerCompatibility(HighLayerCompatibility hlc) {
        return (hlc == null ? new HighLayerCompatibility() : hlc);
    }

    public static LowLayerCompatibility lowLayerCompatibility(LowLayerCompatibility llc) {
        return (llc == null ? new LowLayerCompatibility() : llc);
    }

    public static BearerCapability bearerCapability(BearerCapability bc) {
        if (bc == null) {
            bc = new BearerCapability();
        }
        return bc;
    }

    public static BProtocol bProtocol(IsdnChannelConfig c) {
        return bProtocol(c.getB1(), c.getB2(), c.getB3(), c.getB1Config(), c.getB2Config(), c.getB3Config(), c
                .getBChannelOperation());
    }

    public static BProtocol bProtocol(B1Protocol b1, B2Protocol b2, B3Protocol b3, B1Configuration b1Config,
            B2Configuration b2Config, B3Configuration b3Config, BChannelOperation oper) {

        int b1Protocol = b1Protocol(b1);
        int b2Protocol = b2Protocol(b2);
        int b3Protocol = b3Protocol(b3);
        int bChannelOper = bChannelOperation(oper); 
        
        BProtocol bProtocol = new BProtocol();
        bProtocol.setB1Protocol(b1Protocol);
        bProtocol.setB2Protocol(b2Protocol);
        bProtocol.setB3Protocol(b3Protocol);
        bProtocol.setB1Configuration(b1Config);
        bProtocol.setB2Configuration(b2Config);
        bProtocol.setB3Configuration(b3Config);
        bProtocol.setBChannelOperation(bChannelOper);

        return bProtocol;
    }

    public static int bChannelOperation(BChannelOperation oper) {
        if (oper == null) {
            oper = BChannelOperation.DEFAULT;
        }
        return oper.getValue();
    }

    public static int b3Protocol(B3Protocol b3) {
        if (b3 == null) {
            b3 = B3Protocol.TRANSPARENT;
        }
        return b3.getBitField();
    }

    public static int b2Protocol(B2Protocol b2) {
        if (b2 == null) {
            b2 = B2Protocol.X75_SLP;
        }
        return b2.getBitField();
    }

    public static int b1Protocol(B1Protocol b1) {
        if (b1 == null) {
            b1 = B1Protocol.HDLC_FRAMING_64KBITPS;
        }
        return b1.getBitField();
    }

    public static boolean isOdd(int number) {
        return (number % 2 != 0);
    }

    public static boolean hasSubAddress(IsdnSocketAddress address) {
        String subAddress = address.getSubAddress();
        return (subAddress != null && !("".equals(subAddress)));
    }

    public static CallingPartySubAddress callingPartySubAddress(IsdnSocketAddress calling) {
        
        IsdnAddress address = calling.getAddress();
   
        String subAddress = allowEmptyParam(address.getSubAddress());

        int type = subAddressType(address.getSubAddressType());
    
        CallingPartySubAddress sa = new CallingPartySubAddress();
        sa.setNumber(subAddress);
        sa.setSubaddressType(type);
        sa.setOddEvenIndicator(isOdd(subAddress.length()));

        return sa;
    }

    public static CalledPartySubAddress calledPartySubAddress(IsdnSocketAddress called) {
        
        IsdnAddress address = called.getAddress();
    
        String subAddress = allowEmptyParam(address.getSubAddress());

        int type = subAddressType(address.getSubAddressType());
    
        CalledPartySubAddress sa = new CalledPartySubAddress();
        sa.setNumber(subAddress);
        sa.setSubaddressType(type);
        sa.setOddEvenIndicator(isOdd(subAddress.length()));

        return sa;
    }

    public static int subAddressType(SubAddressType type) {
        return (type == null ? ((NSAP.intValue() & 0x70) >> 4) : ((type.intValue() & 0x70) >> 4));
    }

    public static SubAddressType subAddressType(int type, boolean oddIndicator) {
        int oddEven = (oddIndicator ? 0x08 : 0x00);
        return (type == 0 ? NSAP : SubAddressType.valueOf(((type & 0x07) << 4) | oddEven));
    }

    public static int cipValue(CompatibilityInformationProfile cip) {
        return (cip == null ? NO_PREDEFINED_PROFILE.getBitField() : cip.getBitField());
    }

    public static Controller controller(int number) {
        return new Controller(number);
    }
    
    public static CallingPartyNumber callingPartNumber(IsdnSocketAddress calling) {
    
        IsdnAddress address = calling.getAddress();
    
        String number = allowEmptyParam(address.getNumber());

        int numberType = numberType(address.getNumberType());
        int numberingPlan = numberingPlan(address.getNumberingPlan());
        int presentationIndicator = presentationIndicator(address.getPresentation());
    
        CallingPartyNumber num = new CallingPartyNumber(numberType, numberingPlan, presentationIndicator, number);
        num.setScreeningIndicator(screeningIndicator(address.getScreening()));
        return num;
    }

    private static String allowEmptyParam(String text) {
        return (text == null ? "" : text);
    }

    public static int numberingPlan(NumberingPlan plan) {
        return (plan == null ? NUMBERINGPLAN_UNKNOWN : plan.intValue());
    }

    public static NumberingPlan numberingPlan(int plan) {
        return (plan == 0 ? NumberingPlan.UNKNOWN_NUMBER_PLAN : NumberingPlan.valueOf(plan & 0x0F));
    }

    public static int numberType(NumberType type) {
        return (type == null ? NUMBERTYPE_UNKNOWN : (type.intValue() >> 4));
    }

    public static NumberType numberType(int type) {
        return (type == 0 ? NumberType.UNKOWN_NUMBER_TYPE : NumberType.valueOf((type & 0x0F) << 4));
    }

    public static int presentationIndicator(PresentationIndicator ind) {
        return (ind == null ? PRESENTATION_ALLOWED : ((ind.intValue() & 0x60) >> 5));
    }

    public static PresentationIndicator presentationIndicator(int ind) {
        return (ind == 0 ? PresentationIndicator.ALLOWED : PresentationIndicator.valueOf((ind & 0x06) << 5));
    }

    public static ScreeningIndicator screeningIndicator(int ind) {
        return (ind == 0 ? ScreeningIndicator.NOT_SCREENED : ScreeningIndicator.valueOf(ind & 0x03));
    }

    public static int screeningIndicator(ScreeningIndicator ind) {
        return (ind == null ? SCREENING_USER_PROVIDED_NOT_SCREENED : (ind.intValue() & 0x03));
    }

    public static IsdnSocketAddress callingAddress(CallingPartyNumber number, CallingPartySubAddress subAddress) {

        IsdnAddress ia = new IsdnAddress(number.getNumber(), (subAddress == null ? null : subAddress.getNumber()));
        ia.setNumberType(numberType(number.getNumberType()));
        ia.setNumberingPlan(numberingPlan(number.getNumberingPlan()));
        ia.setPresentation(presentationIndicator(number.getPresentationIndicator()));
        ia.setScreening(screeningIndicator(number.getScreeningIndicator()));

        if (subAddress != null) {
            ia.setSubAddressType(subAddressType(subAddress.getSubaddressType(), subAddress.isOddEvenIndicator()));
        }

        return new IsdnSocketAddress(ia);
    }

    public static IsdnSocketAddress calledAddress(CalledPartyNumber number, CalledPartySubAddress subAddress) {

        IsdnAddress ia = new IsdnAddress(number.getNumber(), (subAddress == null ? null : subAddress.getNumber()));
        ia.setNumberType(numberType(number.getNumberType()));
        ia.setNumberingPlan(numberingPlan(number.getNumberingPlan()));

        if (subAddress != null) {
            ia.setSubAddressType(subAddressType(subAddress.getSubaddressType(), subAddress.isOddEvenIndicator()));
        }

        return new IsdnSocketAddress(ia);
    }

    public static CalledPartyNumber calledPartyNumber(IsdnSocketAddress called) {
    
        IsdnAddress address = called.getAddress();
    
        String number = allowEmptyParam(address.getNumber());

        int numberType = numberType(address.getNumberType());
        int numberingPlan = numberingPlan(address.getNumberingPlan());
    
        CalledPartyNumber num = new CalledPartyNumber(numberType, numberingPlan, number);
        return num;
    }

    /**
     * Instance construction not allowed. An utility class.
     */
    private ParameterHelper() { }

}
