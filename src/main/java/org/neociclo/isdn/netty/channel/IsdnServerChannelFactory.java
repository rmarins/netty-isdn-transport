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

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ServerChannel;
import org.jboss.netty.channel.ServerChannelFactory;
import org.jboss.netty.util.internal.ExecutorUtil;
import org.neociclo.capi20.Capi;
import org.neociclo.isdn.CapiFactory;

/**
 * @author Rafael Marins
 */
public class IsdnServerChannelFactory implements ServerChannelFactory {

    private Executor bossExecutor;
    private Executor workerExecutor;
    private IsdnServerPipelineSink sink;
    private CapiFactory capiFactory;
    private IsdnConfigurator configurator;
    private ControllerSelector controllerSelector;

    /**
     * Creates a new instance.
     */
    public IsdnServerChannelFactory(Executor bossExecutor, Executor workerExecutor, CapiFactory capiFactory) {
        this(bossExecutor, workerExecutor, capiFactory, null);
    }

    /**
     * Creates a new instance.
     */
    public IsdnServerChannelFactory(Executor bossExecutor, Executor workerExecutor, CapiFactory capiFactory, IsdnConfigurator configurator) {
        this(bossExecutor, workerExecutor, capiFactory, configurator, null);
    }

    /**
     * Creates a new instance.
     */
    public IsdnServerChannelFactory(Executor bossExecutor, Executor workerExecutor, CapiFactory capiFactory, IsdnConfigurator configurator,
            ControllerSelector controllerSelector) {

        this.bossExecutor = bossExecutor;
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
    	shutdown();
        capiFactory.releaseExternalResources();
    }

    Executor bossExecutor() {
        return bossExecutor;
    }

	public void shutdown() {
        ExecutorUtil.terminate(workerExecutor);
	}

}
