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
package org.neociclo.edge.oftpisdn;

import static org.neociclo.capi20.parameter.B3Protocol.X25_DTE_DTE;
import static org.neociclo.capi20.parameter.CompatibilityInformationProfile.UNRESTRICTED_DIGITAL;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.sourceforge.jcapi.message.parameter.AdditionalInfo;
import net.sourceforge.jcapi.message.parameter.sub.B3Configuration;
import net.sourceforge.jcapi.message.parameter.sub.BChannelInformation;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.neociclo.isdn.netty.channel.CapiFactory;
import org.neociclo.isdn.netty.channel.IsdnChannel;
import org.neociclo.isdn.netty.channel.IsdnChannelConfig;
import org.neociclo.isdn.netty.channel.IsdnClientChannelFactory;
import org.neociclo.isdn.netty.channel.IsdnConfigurator;
import org.neociclo.isdn.netty.channel.JCapiFactory;
import org.neociclo.isdn.netty.channel.RemoteCapiFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class OftpIsdnProxy {

    public static void main(String[] args) {
        // Validate command line options.
        if (args.length != 7) {
            System.err.println(
                    "Usage: " + OftpIsdnProxy.class.getSimpleName() +
                    " <local port> <rcapi host> <rcapi port> <rcapi user> <rcapi passwd>" +
                    " <called address> <calling address>");
            return;
        }

        // parse command line options
        int localPort = Integer.parseInt(args[0]);

        String rcapiHost = args[1];
        int rcapiPort = Integer.parseInt(args[2]);
        String rcapiUser = args[3];
        String rcapiPswd = args[4];

        String calledAddr = args[5];
        String callingAddr = args[6];

        // configure bootstrap
        Executor executor = Executors.newCachedThreadPool();
        ServerBootstrap sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executor));

        // set up the event pipeline factory
        CapiFactory cf = new RemoteCapiFactory(rcapiHost, rcapiPort, rcapiUser, rcapiPswd);
//        CapiFactory cf = new JCapiFactory();
        IsdnClientChannelFactory isdnFactory = new IsdnClientChannelFactory(executor, cf, new IsdnConfigurator() {
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

        sb.setPipelineFactory(new OftpIsdnProxyPipelineFactory(isdnFactory, calledAddr, callingAddr));

        // start up the server
        sb.bind(new InetSocketAddress(localPort));
    }

}
