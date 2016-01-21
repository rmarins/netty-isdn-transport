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
            	// setting the workerThread to null will prevent any channel
                // operations from interrupting this thread from now on.
                workerThread = null;
                logger.trace("Shouldn't receive NULL on PlciConnectionHandler.getMessage() since " +
                		"PlciConnectionHandler.waitForSignal() was released.");
                break;
            }

        }

        // setting the workerThread to null will prevent any channel
        // operations from interrupting this thread from now on.
        workerThread = null;
        
        // clean up
        channel.close();
        close(channel, succeededFuture(channel));
        channel=null;

    }

    public static void close(IsdnAcceptedChannel channel, ChannelFuture closeFuture) {

    	logger.trace("close() invoked on IsdnAcceptedChannel: {}", channel);

        fireChannelDisconnected(channel);
        fireChannelUnbound(channel);
        fireChannelClosed(channel);
        closeFuture.setSuccess();

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
