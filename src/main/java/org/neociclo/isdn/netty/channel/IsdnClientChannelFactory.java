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

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.util.internal.ExecutorUtil;
import org.neociclo.capi20.Capi;
import org.neociclo.isdn.CapiFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnClientChannelFactory implements ChannelFactory {

    private final CapiFactory capiFactory;
    private final IsdnClientPipelineSink sink;

    private ControllerSelector controllerSelector;
    private IsdnConfigurator configurator;

    private Executor workerExecutor;

    /**
     * Creates a new instance.
     */
    public IsdnClientChannelFactory(Executor workerExecutor, CapiFactory capiFactory) {
        this(workerExecutor, capiFactory, null);
    }

    /**
     * Creates a new instance.
     */
    public IsdnClientChannelFactory(Executor workerExecutor, CapiFactory capiFactory, IsdnConfigurator configurator) {
        this(workerExecutor, capiFactory, configurator, null);
    }

    /**
     * Creates a new instance.
     */
    public IsdnClientChannelFactory(Executor workerExecutor, CapiFactory capiFactory, IsdnConfigurator configurator,
            ControllerSelector controllerSelector) {

        super();

        this.workerExecutor = workerExecutor;
        this.capiFactory = capiFactory;
        this.sink = new IsdnClientPipelineSink(workerExecutor);
        this.configurator = configurator;

        if (controllerSelector == null) {
            this.controllerSelector = new FirstControllerSelector();
        } else {
            this.controllerSelector = controllerSelector;
        }
    }

    public IsdnClientChannel newChannel(ChannelPipeline pipeline) {
        Capi capi = capiFactory.getCapi();
        IsdnClientChannel channel = new IsdnClientChannel(capi, this, pipeline, sink, controllerSelector, configurator);
        return channel;
    }

    public void releaseExternalResources() {
        ExecutorUtil.terminate(workerExecutor);
        capiFactory.releaseExternalResources();
    }

    public Executor executor() {
        return workerExecutor;
    }

}
