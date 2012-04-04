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
import org.neociclo.odetteftp.TransportType;
import org.neociclo.odetteftp.oftplet.OftpletFactory;
import org.neociclo.odetteftp.util.ExecutorUtil;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnClient extends Client {

	private CapiFactory capi;

	private IsdnConfigurator isdnConfigurator;
	private ControllerSelector controllerSelector;

	private Executor workerExecutor;

	public IsdnClient(CapiFactory capi, OftpletFactory factory) {
		super(factory);
		if (capi == null) {
			throw new NullPointerException("capi");
		}

		this.capi = capi;
	}

	public synchronized void connect(String calledAddress, String callingAddress, boolean await)
			throws Exception {
		connect(new IsdnSocketAddress(calledAddress), new IsdnSocketAddress(callingAddress), await);
	}

	@Override
	protected ChannelPipelineFactory getPipelineFactory(OftpletFactory factory, Timer timer) {

		IsdnPipelineFactory pipelineFactory = new IsdnPipelineFactory(EntityType.INITIATOR, factory, timer, null);

		return pipelineFactory;
	}

	@Override
	public void setChannelFactory(ChannelFactory channelFactory) {
		if (!(channelFactory instanceof IsdnClientChannelFactory)) {
			throw new IllegalArgumentException("Not an instance of IsdnClientChannelFactory");
		}
		super.setChannelFactory(channelFactory);
	}

	@Override
	public IsdnClientChannelFactory getChannelFactory() {

		// creates one channel-factory per object instance
        if (super.getChannelFactory() == null) {

            if (workerExecutor == null) {
            	workerExecutor = Executors.newCachedThreadPool();
                setManaged(workerExecutor);
            }

        	setChannelFactory(new IsdnClientChannelFactory(workerExecutor, getCapi(), getIsdnConfigurator(),
    		        getControllerSelector()));
        }

        return (IsdnClientChannelFactory) super.getChannelFactory();
	}

	public final IsdnSocketAddress getCalledAddress() {
		return (IsdnSocketAddress) getRemoteAddress();
	}

	public final IsdnSocketAddress getCallingAddress() {
		return (IsdnSocketAddress) getLocalAddress();
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

    @Override
    protected void releaseExternalResources() {
    	if (isManaged(workerExecutor)) {
    		ExecutorUtil.terminate(workerExecutor);
    	}
    	super.releaseExternalResources();
    }

	protected TransportType getTransportType() {
		return TransportType.ISDN_CAPI20;
	}

}
