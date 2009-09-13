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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.parameter.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PlciConnectionHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlciConnectionHandler.class);

    private BlockingQueue<CapiMessage> messageQueue = new LinkedBlockingQueue<CapiMessage>(7);
    private CapiMessage received;
    private Integer plci;

    public PlciConnectionHandler(Integer plci) {
        this.plci = plci;
    }

    public Integer getPlci() {
        return plci;
    }

    public void waitForSignal() throws CapiException {
        try {
            received = messageQueue.take();
        } catch (InterruptedException e) {
            throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, "interrupted blocking queue take()");
        }
    }

    public CapiMessage getMessage() {
        CapiMessage r = received;
        this.received = null;
        logger.trace("getMessage() :: PLCI = {}, message = {}", plci, r);
        return r;
    }

    public void offerReceived(CapiMessage message) throws CapiException {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new CapiException(Info.EXCHANGE_RESOURCE_ERROR, "interrupted blocking queue take()");
        }
    }

}