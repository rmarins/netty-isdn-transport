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
import static org.neociclo.isdn.netty.channel.MessageBuilder.*;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.neociclo.capi20.Capi;
import org.neociclo.capi20.CapiException;
import org.neociclo.capi20.Controller;
import org.neociclo.capi20.Profile;
import org.neociclo.capi20.message.BaseMessage;
import org.neociclo.capi20.message.CapiMessage;
import org.neociclo.capi20.parameter.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class IsdnWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsdnWorker.class);

    /** 1 Kilobyte (1024 bytes). */
    private static final int KB = 1024;

    public static void initDevice(IsdnClientChannel channel) throws CapiException {

        // check if already initialized
        if (channel.initialized) {
            LOGGER.warn("IsdnCapiChannel already initialized. Skipping initDevice(): {}.", channel);
            return;
        }

        // retrieve number of controllers, first
        int numOfControllers = channel.capi.getNumberOfControllers();

        // query profile on the total of controllers
        ArrayList<Controller> controllers = new ArrayList<Controller>(numOfControllers);
        for (int i = 1; i <= numOfControllers; i++) {
            Controller ctrl = getProfile(channel, i);
            controllers.add(ctrl);
        }

        // select the channel controller
        channel.selectController(controllers);

        channel.initialized = true;

    }

    public static int register(IsdnClientChannel channel) throws CapiException {

        LOGGER.trace("register()");

        if (!channel.initialized) {
            throw new IllegalStateException("IsdnCapiChannel not initialized.");
        }

        IsdnChannelConfig config = channel.getConfig();

        int maxLogicalConnection = config.getMaxLogicalConnection();
        int messageBufferSize = KB + (KB * maxLogicalConnection);
        int maxBDataBlocks = config.getMaxBDataBlocks();
        int maxBDataLen = config.getMaxBDataLen();

        assert (maxLogicalConnection > 0) : "maxLogicalConnection";
        assert (maxBDataBlocks >= 2 && maxBDataBlocks <= 7) : "maxBDataBlocks (must be between 2 and 7)";
        assert (maxBDataLen > 0) : "maxBDataLen";

        LOGGER.trace("Capi.register() :: msgBufSize = {}, mxLogicalCon = {}, maxBDataBlocks = {}, maxBDataLen = {}",
                new Object[] { messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen });

        int appID = channel.capi.register(messageBufferSize, maxLogicalConnection, maxBDataBlocks, maxBDataLen);
        return appID;

    }

    public static void write(IsdnClientChannel channel, ChannelFuture future, Object message) {

        if (message == ChannelBuffers.EMPTY_BUFFER) {
            // set flush() signal complete
            future.setSuccess();
            return;
        } else if (!(message instanceof CapiMessage)) {
            // skip non-CapiMessage type
            LOGGER.warn("write() :: Non-CAPI message type: {}", message);
            return;
        }

        try {
            CapiMessage a = (CapiMessage) message;

            // write down the message into CAPI
            setMessageAppIdAndNumber(channel, a);

            LOGGER.trace("Capi.putMessage() :: {}", a);

            channel.capi.simplePutMessage(channel.worker.appID, a.getBuffer());

            // bytesWritten = general message format length + b3 data length
            int bytesWritten = a.getTotalLength();

            fireWriteComplete(channel, bytesWritten);
            future.setSuccess();

        } catch (Throwable t) {
            // convert any possible error of class 0x11xx concerning message
            // exchange functions into ClosedChannelException
            if (t instanceof CapiException) {
                LOGGER.trace("Converting CapiException into a ClosedChannelException.", t);
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

    private static void setMessageAppIdAndNumber(IsdnClientChannel channel, CapiMessage a) {
        BaseMessage b = (BaseMessage) a;

        b.setAppID(channel.worker.appID);

        // set message number for message REQ only
        int subCommand = (a.getType().getSubCommand() & 0xff);
        if (subCommand == 0x80) {
            b.setMessageID(channel.worker.messageCounter.getAndIncrement());
        }

    }

    public static void setInterestOps(IsdnClientChannel channel, ChannelFuture future, int interestOps) {

        LOGGER.trace("setInterestOps() :: interestOps = {}", interestOps);

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
                    Thread workerThread = channel.workerThread;
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

    public static void close(IsdnClientChannel channel, ChannelFuture future) {

        LOGGER.trace("close()");

        boolean connected = channel.isConnected();
        boolean bound = channel.isBound();

        try {
            channel.worker.release();
            if (channel.setClosed()) {
                future.setSuccess();
                if (connected) {
                    // interrupt the worker so it stops reading
                    Thread currentThread = Thread.currentThread();
                    Thread workerThread = channel.workerThread;
                    if (workerThread != null && currentThread != workerThread) {
                        workerThread.interrupt();
                    }
                    fireChannelDisconnected(channel);
                }
                if (bound) {
                    fireChannelUnbound(channel);
                }
                fireChannelClosed(channel);
            } else {
                future.setSuccess();
            }

        } catch (Throwable t) {
            future.setFailure(t);
            fireExceptionCaught(channel, t);
        }

    }

    private static Controller getProfile(IsdnClientChannel channel, final int controller) throws CapiException {

        final Profile profile = channel.capi.simpleGetProfile(controller);

        final Capi capi = channel.capi;

        return new Controller() {

            public String getSerialNumber() throws CapiException {
                // late method invocation
                return capi.getSerialNumber(controller);
            }

            public Profile getProfile() throws CapiException {
                return profile;
            }

            public int getNumber() {
                return controller;
            }

            public String getManufacturer() throws CapiException {
                // late method invocation
                return capi.getManufacturer(controller);
            }
        };

    }

    private final IsdnClientChannel channel;
    private final int appID;
    private final AtomicInteger messageCounter;

    private boolean released;

    private volatile ChannelFuture connectFuture;

    public IsdnWorker(IsdnClientChannel channel, int port, ChannelFuture connectFuture) {
        super();

        if (port <= 0) {
            throw new IllegalArgumentException(String.format("Invalid ISDN-port (appId). Port [%s] on Channel [%s].",
                    port, channel));
        }

        this.channel = channel;
        this.connectFuture = connectFuture;
        this.appID = port;
        this.messageCounter = new AtomicInteger(0);

    }

    public void release() throws CapiException {

        if (released) {
            // throw new
            // IllegalStateException(String.format("Channel already released. Port [%s] on Channel [%s].",
            // isdnPort, channel));
            LOGGER.warn("Channel already released: port = {}, channel = {}.", appID, channel);
            return;
        }

        LOGGER.trace("Capi.release()");
        channel.capi.release(appID);

        released = true;

    }

    public void run() {

        channel.workerThread = Thread.currentThread();

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
                LOGGER.trace("Capi.waitForSignal() :: locking... ");
                channel.capi.waitForSignal(appID);
                LOGGER.trace("Capi.waitForSignal() :: released!");
                
//                // retrieve message from CAPI queue
//                for (int i=0; i<3; i++) {
//                    LOGGER.trace("Capi.getMessage() :: {}", i);
                    message = getMessage();
//                    if (message != null) {
//                        break;
//                    }
//                }

            } catch (CapiException e) {

                // break the worker as this exception is caught on CAPI
                // innoperation condition
                fireExceptionCaught(channel, e);
                break;

            }

            if (message != null) {
                fireMessageReceived(channel, message);
            } else {
                LOGGER.warn("Shouldn't receive NULL on Capi.getMessage() since Capi.waitForSignal() was released.");
            }

        }

        // setting the workerThread to null will prevent any channel
        // operations from interrupting this thread from now on.
        channel.workerThread = null;

        // clean up
        ChannelFuture closeFuture = channel.getCloseFuture();
        if (!closeFuture.isDone()) {
            LOGGER.trace("Clean up");
            close(channel, succeededFuture(channel));
        }

    }

    private CapiMessage getMessage() throws CapiException {

        ChannelBuffer messageBuffer = channel.capi.simpleGetMessage(appID);
        

        if (messageBuffer == null) {
            return null;
        }

        CapiMessage message = buildMessage(messageBuffer, channel.getConfig());

        LOGGER.trace("Capi.getMessage() :: {}", message);

        return message;
    }

    public void setConnected() {
        if (connectFuture != null) {
            connectFuture.setSuccess();
            connectFuture = null;
        }
    }

    public void setConnectFailure(Throwable cause) {
        if (connectFuture != null) {
            connectFuture.setFailure(cause);
            connectFuture = null;
        }
    }

}
