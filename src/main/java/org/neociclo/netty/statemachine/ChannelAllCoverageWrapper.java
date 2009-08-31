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
package org.neociclo.netty.statemachine;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelUpstreamHandler;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
