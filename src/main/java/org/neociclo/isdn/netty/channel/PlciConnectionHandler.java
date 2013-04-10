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