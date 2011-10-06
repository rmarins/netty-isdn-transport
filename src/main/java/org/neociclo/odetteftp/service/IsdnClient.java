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

import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.util.Timer;
import org.neociclo.isdn.CapiFactory;
import org.neociclo.isdn.IsdnSocketAddress;
import org.neociclo.isdn.netty.channel.ControllerSelector;
import org.neociclo.isdn.netty.channel.IsdnClientChannelFactory;
import org.neociclo.isdn.netty.channel.IsdnConfigurator;
import org.neociclo.odetteftp.EntityType;
import org.neociclo.odetteftp.oftplet.OftpletFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnClient extends Client {

	private IsdnSocketAddress calledAddress;
	private IsdnSocketAddress callingAddress;
	private CapiFactory capi;

	private IsdnConfigurator isdnConfigurator;
	private ControllerSelector controllerSelector;

	private Executor workerExecutor;

	public IsdnClient(String calledAddr, String callingAddr, CapiFactory capi, OftpletFactory factory) {
		this(new IsdnSocketAddress(calledAddr), new IsdnSocketAddress(callingAddr), capi, factory);
	}

	public IsdnClient(IsdnSocketAddress calledAddr, IsdnSocketAddress callingAddr, CapiFactory capi, OftpletFactory factory) {
		super(factory);

		if (calledAddr == null)
			throw new NullPointerException("calledAddress");
		else if (callingAddr == null)
			throw new NullPointerException("callingAddress");
		else if (capi == null)
			throw new NullPointerException("capi");

		this.calledAddress = calledAddr;
		this.callingAddress = callingAddr;
		this.capi = capi;
	}

	@Override
	protected SocketAddress getRemoteAddress() {
		return getCalledAddress();
	}

	@Override
	protected SocketAddress getLocalAddress() {
		return getCallingAddress();
	}

	@Override
	protected ChannelPipelineFactory getPipelineFactory(OftpletFactory factory, Timer timer) {

		IsdnPipelineFactory pipelineFactory = new IsdnPipelineFactory(EntityType.INITIATOR, factory, timer, null);

		return pipelineFactory;
	}

	@Override
	protected ChannelFactory createChannelFactory() {

        if (workerExecutor == null) {
        	workerExecutor = Executors.newCachedThreadPool();
            setManaged(workerExecutor);
        }

		ChannelFactory channelFactory = new IsdnClientChannelFactory(workerExecutor, getCapi(), getIsdnConfigurator(),
		        getControllerSelector());
		return channelFactory;
	}

	public final IsdnSocketAddress getCalledAddress() {
		return calledAddress;
	}

	public final IsdnSocketAddress getCallingAddress() {
		return callingAddress;
	}

	public final CapiFactory getCapi() {
		return capi;
	}

	public IsdnConfigurator getIsdnConfigurator() {
		return isdnConfigurator;
	}

	public void setIsdnConfigurator(IsdnConfigurator isdnConfigurator) {
		this.isdnConfigurator = isdnConfigurator;
	}

	public ControllerSelector getControllerSelector() {
		return controllerSelector;
	}

	public void setControllerSelector(ControllerSelector controllerSelector) {
		this.controllerSelector = controllerSelector;
	}

}
