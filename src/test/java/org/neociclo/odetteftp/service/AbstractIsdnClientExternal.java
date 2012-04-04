/**
 * Neociclo Accord, Open Source B2Bi Middleware
 * Copyright (C) 2005-2011 Neociclo, http://www.neociclo.com
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
package org.neociclo.odetteftp.service;

import static org.neociclo.capi20.parameter.B3Protocol.X25_DTE_DTE;
import static org.neociclo.capi20.parameter.CompatibilityInformationProfile.UNRESTRICTED_DIGITAL;
import static org.neociclo.odetteftp.util.IsdnConstants.*;
import static org.neociclo.odetteftp.util.OdetteFtpConstants.DEFAULT_OFTP_PORT;
import static org.neociclo.odetteftp.util.OdetteFtpConstants.DEFAULT_SECURE_OFTP_PORT;

import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.sub.B3Configuration;
import net.sourceforge.jcapi.message.parameter.sub.BChannelInformation;

import org.neociclo.isdn.CapiFactory;
import org.neociclo.isdn.RemoteCapiFactory;
import org.neociclo.isdn.netty.channel.IsdnChannel;
import org.neociclo.isdn.netty.channel.IsdnChannelConfig;
import org.neociclo.isdn.netty.channel.IsdnConfigurator;
import org.neociclo.odetteftp.oftplet.OftpletFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public abstract class AbstractIsdnClientExternal extends BaseClientExternalTestCase {

	@Override
	protected Client createClient(OftpletFactory factory) {

        String rcapiHost = System.getProperty("rcapi.server");
        int rcapiPort = Integer.valueOf(
        		System.getProperty("rcapi.port", String.valueOf(DEFAULT_RCAPI_PORT)));
        String rcapiUser = System.getProperty("rcapi.user");
        String rcapiPswd = System.getProperty("rcapi.password");

        if (rcapiHost == null || "".equals(rcapiHost.trim())) {
        	throw new IllegalArgumentException("rcapi.server");
        }

        // set up the CAPI intermediate layer
        CapiFactory capi = new RemoteCapiFactory(rcapiHost, rcapiPort, rcapiUser, rcapiPswd);

        // set up the OFTP Isdn client
        IsdnClient isdnClient = new IsdnClient(capi, factory);
        isdnClient.setIsdnConfigurator(new IsdnConfigurator() {
            public void configureChannel(IsdnChannel channel) {
                IsdnChannelConfig config = channel.getConfig();

                config.setMaxLogicalConnection(1);
                config.setMaxBDataBlocks(7);
                config.setMaxBDataLen(4096);

                config.setCompatibilityInformationProfile(UNRESTRICTED_DIGITAL);
                config.setB3(X25_DTE_DTE);

                B3Configuration b3config = new B3Configuration(X25_DTE_DTE.getBitField());
                config.setB3Config(b3config);

                AdditionalInfo info = new AdditionalInfo();
                BChannelInformation bChannelInfo = new BChannelInformation();
                info.setBinfo(bChannelInfo);
                config.setAdditionalInfo(info);
            }
        });

        return isdnClient;
	}

    protected void connect() throws Exception {
    	connect(true);
    }

    protected void connect(boolean await) throws Exception {

        String calledAddr = System.getProperty("oftp.isdn.called-addr");
        String callingAddr = System.getProperty("oftp.isdn.calling-addr");

        ((IsdnClient) client).connect(calledAddr, callingAddr, await);
    }


}
