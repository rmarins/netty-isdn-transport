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

import static org.jboss.netty.channel.Channels.*;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.AbstractChannelSink;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnServerPipelineSink extends AbstractChannelSink implements IsdnChannelSink {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnServerPipelineSink.class);
        
    private Executor workerExecutor;

    public IsdnServerPipelineSink(Executor workerExecutor) {
        this.workerExecutor = workerExecutor;
    }

    public void eventSunk(ChannelPipeline pipeline, ChannelEvent e) throws Exception {

        LOGGER.trace(e.toString());

    }

    public void initialize(IsdnClientChannel channel) {
        try {
            // query CAPI controllers
            IsdnWorker.initDevice(channel);
        } catch (Throwable t) {
            fireExceptionCaught(channel, t);
        }
    }

}
