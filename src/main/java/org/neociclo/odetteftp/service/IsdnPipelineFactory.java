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

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.util.Timer;
import org.neociclo.odetteftp.EntityType;
import org.neociclo.odetteftp.netty.OdetteFtpChannelHandler;
import org.neociclo.odetteftp.netty.codec.OdetteFtpDecoder;
import org.neociclo.odetteftp.netty.codec.OdetteFtpEncoder;
import org.neociclo.odetteftp.netty.codec.ProtocolLoggingHandler;
import org.neociclo.odetteftp.oftplet.OftpletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class IsdnPipelineFactory implements ChannelPipelineFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(IsdnPipelineFactory.class);

	private EntityType entityType;
	private OftpletFactory factory;
	private Timer timer;
	private ChannelGroup channelGroup;

	private boolean loggingEnabled = true;

	/**
	 * @param entityType
	 * @param factory
	 * @param timer
	 * @param channelGroup
	 */
	public IsdnPipelineFactory(EntityType entityType, OftpletFactory factory, Timer timer, ChannelGroup channelGroup) {
		super();
		this.entityType = entityType;
		this.factory = factory;
		this.timer = timer;
		this.channelGroup = channelGroup;
	}

	public ChannelPipeline getPipeline() throws Exception {

        final ChannelPipeline p = pipeline();

        p.addLast("oftpPipelineSetupWhenConnected", new SimpleChannelHandler() {
        	@Override
        	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        		keepChannelOpenEvent(ctx, e);
        	    super.channelOpen(ctx, e);
        	}
			@Override
        	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
                configureOdetteFtpHandler(p);
                p.remove("oftpPipelineSetupWhenConnected");

                ChannelStateEvent channelOpenEvent = restoreChannelOpenEvent(ctx);
                super.channelOpen(ctx, channelOpenEvent);
                channelOpenEvent = null;

                super.channelConnected(ctx, e);
            }
        });

		return p;
	}

	private void keepChannelOpenEvent(ChannelHandlerContext ctx, ChannelStateEvent e) {
		ctx.setAttachment(e);
    }

	private ChannelStateEvent restoreChannelOpenEvent(ChannelHandlerContext ctx) {
		ChannelStateEvent e = (ChannelStateEvent) ctx.getAttachment();
		ctx.setAttachment(null);
        return e;
    }

    private void configureOdetteFtpHandler(ChannelPipeline p) {

    	// add odette-ftp exchange buffer codecs
        p.addLast("OdetteExchangeBuffer-DECODER", new OdetteFtpDecoder());
        p.addLast("OdetteExchangeBuffer-ENCODER", new OdetteFtpEncoder());
        LOGGER.debug("Added Odette Exchange Buffer codecs to channel pipeline.");

        if (isLoggingEnabled()) {
	        p.addLast("OdetteFtp-LOGGING", new ProtocolLoggingHandler());
	        LOGGER.debug("Added Odette FTP protocol logging handler to channel pipeline.");
        }

        // add odette-ftp handler
        p.addLast("OdetteFtp-HANDLER", new OdetteFtpChannelHandler(entityType, factory, timer, channelGroup));
		LOGGER.debug("Added Odette FTP handler to channel pipeline (oftpletFactory={}, timer={}, channelGroup={}).",
		        new Object[] { factory, timer, channelGroup });

    }

	public void disableLogging() {
		this.loggingEnabled = false;
	}

	public void enableLogging() {
		this.loggingEnabled = true;
	}

	public boolean isLoggingEnabled() {
		return loggingEnabled;
	}

}
