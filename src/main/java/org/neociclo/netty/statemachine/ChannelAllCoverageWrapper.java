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
package org.neociclo.netty.statemachine;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelUpstreamHandler;

/**
 * @author Rafael Marins
 */
@ChannelPipelineCoverage ("all")
public class ChannelAllCoverageWrapper implements ChannelDownstreamHandler, ChannelUpstreamHandler {

    private ChannelHandler handler;
    private final boolean downstream;
    private final boolean upstream;

    public ChannelAllCoverageWrapper(ChannelHandler handler) {
        super();
        this.handler = handler;
        this.downstream = (handler instanceof ChannelDownstreamHandler);
        this.upstream = (handler instanceof ChannelUpstreamHandler);
    }

    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (downstream) {
            ((ChannelDownstreamHandler) handler).handleDownstream(ctx, e);
        }
    }

    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (upstream) {
            ((ChannelUpstreamHandler) handler).handleUpstream(ctx, e);
        }
    }

}
