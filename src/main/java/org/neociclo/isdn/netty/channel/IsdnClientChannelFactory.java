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
package org.neociclo.isdn.netty.channel;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.util.internal.ExecutorUtil;
import org.neociclo.capi20.Capi;
import org.neociclo.isdn.CapiFactory;

/**
 * @author Rafael Marins
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
    	shutdown();
        capiFactory.releaseExternalResources();
    }

    public Executor executor() {
        return workerExecutor;
    }

	public void shutdown() {
        ExecutorUtil.terminate(workerExecutor);
	}

}
