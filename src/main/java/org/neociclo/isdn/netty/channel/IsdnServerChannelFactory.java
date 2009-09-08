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

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ServerChannel;
import org.jboss.netty.channel.ServerChannelFactory;
import org.jboss.netty.util.internal.ExecutorUtil;
import org.neociclo.capi20.Capi;
import org.neociclo.isdn.CapiFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnServerChannelFactory implements ServerChannelFactory {

    private Executor workerExecutor;
    private IsdnServerPipelineSink sink;
    private CapiFactory capiFactory;
    private IsdnConfigurator configurator;
    private ControllerSelector controllerSelector;

    /**
     * Creates a new instance.
     */
    public IsdnServerChannelFactory(Executor workerExecutor, CapiFactory capiFactory) {
        this(workerExecutor, capiFactory, null);
    }

    /**
     * Creates a new instance.
     */
    public IsdnServerChannelFactory(Executor workerExecutor, CapiFactory capiFactory, IsdnConfigurator configurator) {
        this(workerExecutor, capiFactory, configurator, null);
    }

    /**
     * Creates a new instance.
     */
    public IsdnServerChannelFactory(Executor workerExecutor, CapiFactory capiFactory, IsdnConfigurator configurator,
            ControllerSelector controllerSelector) {
        this.workerExecutor = workerExecutor;
        this.capiFactory = capiFactory;
        this.sink = new IsdnServerPipelineSink(workerExecutor);
        this.configurator = configurator;

        if (controllerSelector == null) {
            this.controllerSelector = new FirstControllerSelector();
        } else {
            this.controllerSelector = controllerSelector;
        }
    }

    public ServerChannel newChannel(ChannelPipeline pipeline) {
        Capi capi = capiFactory.getCapi();
        return new IsdnServerChannel(this, pipeline, sink, capi, controllerSelector, configurator);
//        return new IsdnCapiChannel(capi, false, this, pipeline, sink, controllerSelector, configurator);
    }

    public void releaseExternalResources() {
        ExecutorUtil.terminate(workerExecutor);
        capiFactory.releaseExternalResources();
    }

}
