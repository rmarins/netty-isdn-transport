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

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.SimpleCapi;
import org.neociclo.capi20.message.BaseMessage;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.parameter.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class IsdnAcceptedWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(IsdnAcceptedWorker.class);

    private IsdnAcceptedChannel channel;
    private Thread workerThread;

    public IsdnAcceptedWorker(IsdnAcceptedChannel acceptedChannel) {
        this.channel = acceptedChannel;
    }

    public void run() {

        workerThread = Thread.currentThread();

        while (channel.isOpen()) {

            synchronized (channel.interestOpsLock) {
                while (!channel.isReadable()) {
                    try {
                        // notify() is not called at all.
                        // close() and setInterestOps() calls Thread.interrupt()
                        channel.interestOpsLock.wait();
                    } catch (InterruptedException e) {
                        if (!channel.isOpen()) {
                            break;
                        }
                    }
                }
            }

            CapiMessage message = null;
            try {
                // asynchronous waiting uninterruptably
                logger.trace("PlciConnectionHandler.waitForSignal() :: locking... ");
                channel.plciHandler.waitForSignal();
                logger.trace("PlciConnectionHandler.waitForSignal() :: released!");
                
                message = channel.plciHandler.getMessage();

            } catch (CapiException e) {

                // break the worker as this exception is caught on CAPI
                // innoperation condition
                fireExceptionCaught(channel, e);
                break;
            }

            if (message != null) {
                fireMessageReceived(channel, message);
            } else {
                logger.warn("Shouldn't receive NULL on PlciConnectionHandler.getMessage() since " +
                		"PlciConnectionHandler.waitForSignal() was released.");
            }

        }

        // setting the workerThread to null will prevent any channel
        // operations from interrupting this thread from now on.
        workerThread = null;

        // clean up
        ChannelFuture closeFuture = channel.getCloseFuture();
        if (!closeFuture.isDone()) {
            logger.trace("Clean up");
            close(channel, succeededFuture(channel));
        }

    }

    public static void close(IsdnAcceptedChannel channel2, ChannelFuture succeededFuture) {
        logger.warn("close() not yet implemented");
    }

    public static void write(IsdnAcceptedChannel channel, ChannelFuture future, Object message, AtomicInteger messageCounter) {

        if (message == ChannelBuffers.EMPTY_BUFFER) {
            // set flush() signal complete
            future.setSuccess();
            return;
        } else if (!(message instanceof CapiMessage)) {
            // skip non-CapiMessage type
            logger.warn("write() :: Non-CAPI message type: {}", message);
            return;
        }

        IsdnServerChannel serverChannel = (IsdnServerChannel) channel.getParent();
        SimpleCapi capi = serverChannel.capi();
        int appID = serverChannel.getAppID();

        try {
            CapiMessage a = (CapiMessage) message;

            // write down the message into CAPI
            setMessageAppIdAndNumber(a, serverChannel.getAppID(), messageCounter.getAndIncrement());

            logger.trace("Capi.putMessage() :: {}", a);
            capi.simplePutMessage(appID, a.getBuffer());

            // bytesWritten = general message format length + b3 data length
            int bytesWritten = a.getTotalLength();

            fireWriteComplete(channel, bytesWritten);
            future.setSuccess();

        } catch (Throwable t) {
            // convert any possible error of class 0x11xx concerning message
            // exchange functions into ClosedChannelException
            if (t instanceof CapiException) {
                logger.trace("Converting CapiException into a ClosedChannelException.", t);
                CapiException ce = (CapiException) t;
                Info error = ce.getCapiError();
                if (error.isClassEleven()) {
                    t = new ClosedChannelException();
                }
            }
            future.setFailure(t);
            fireExceptionCaught(channel, t);
        }

        
        
    }

    private static void setMessageAppIdAndNumber(CapiMessage a, int appID, int number) {

        BaseMessage b = (BaseMessage) a;
        b.setAppID(appID);

        // set message number for message REQ only
        int subCommand = (a.getType().getSubCommand() & 0xff);
        if (subCommand == 0x80) {
            b.setMessageID(number);
        }

    }

    public static void setInterestOps(IsdnAcceptedChannel channel, ChannelFuture future, int interestOps) {

        // Override OP_WRITE flag - a user cannot change this flag.
        interestOps &= ~Channel.OP_WRITE;
        interestOps |= channel.getInterestOps() & Channel.OP_WRITE;

        boolean changed = false;
        try {
            if (channel.getInterestOps() != interestOps) {
                if ((interestOps & Channel.OP_READ) != 0) {
                    channel.setInterestOpsNow(Channel.OP_READ);
                } else {
                    channel.setInterestOpsNow(Channel.OP_NONE);
                }
                changed = true;
            }

            future.setSuccess();
            if (changed) {
                synchronized (channel.interestOpsLock) {
                    channel.setInterestOpsNow(interestOps);

                    // Notify the worker so it stops or continues reading.
                    Thread currentThread = Thread.currentThread();
                    Thread workerThread = channel.worker.workerThread;
                    if (workerThread != null && currentThread != workerThread) {
                        workerThread.interrupt();
                    }
                }

                fireChannelInterestChanged(channel);
            }
        } catch (Throwable t) {
            future.setFailure(t);
            fireExceptionCaught(channel, t);
        }
        
    }

}
