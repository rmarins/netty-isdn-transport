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
package org.neociclo.capi20;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public interface Capi {

    /**
     * This is the function the application uses to announce its presence to
     * <b>COMMON-ISDN-API</b>. The application describes its needs by passing
     * the four parameters <i>messageBufferSize</i>,
     * <i>maxLogicalConnection</i>, <i>maxBDataBlocks</i> and
     * <i>maxBDataLen</i>. For a typical application, the amount of memory
     * required should be calculated by the following formula:
     * 
     * <pre>
     * messageBufferSize = 1024 + (1024 * maxLogicalConnection)
     * </pre>
     * 
     * @param messageBufferSize
     *            specifies the size of message buffer.
     * @param maxLogicalConnection
     *            specifies the maximum number of logical connections this
     *            application can maintain concurrently. Any attempt by the
     *            application to exceed the logical connection count by
     *            accepting or initiating additional connections will result in
     *            a connection set-up failure and an error indication from
     *            <b>COMMON-ISDN-API</b>.
     * @param maxBDataBlocks
     *            specifies the maximum number of received data blocks that can
     *            be reported to the application simultaneously for each logical
     *            connection. The number of simultaneously available data blocks
     *            has a decisive effect on the data throughput in the system and
     *            should be between 2 and 7. At least two data blocks must be
     *            specified.
     * @param maxBDataLen
     *            specifies the maximum size of the application data block to be
     *            transmitted and received. Selection of a protocol that
     *            requires larger data units, or attempts to transmit or receive
     *            larger data units, will result in an error indication from
     *            <b>COMMON-ISDN-API</b>. The default value for the protocol ISO
     *            7776 (X.75) is 128 octets. <b>COMMON-ISDN-API</b> is able to
     *            support at least up to 2048 octets.
     * @return the registered application ID.
     * @throws CapiException
     */
    public int register(int messageBufferSize, int maxLogicalConnection, int maxBDataBlocks, int maxBDataLen)
            throws CapiException;

    /**
     * The application uses this operation to log off from
     * <b>COMMON-ISDN-API</b>. <b>COMMON-ISDN-API</b> will release all resources
     * that have been allocated.
     * <p/>
     * The application is identified by the application identification number
     * assigned in the earlier {@link #register(int, int, int, int)} operation.
     * 
     * @param appID
     *            the application identification number assigned by the function
     *            CAPI_REGISTER
     * @throws CapiException
     */
    public void release(int appID) throws CapiException;

    /**
     * With this operation the application transfers a message to
     * <b>COMMON-ISDN-API</b>. The application identifies itself with an
     * application identification number.
     * 
     * @param appID
     *            the application identification number assigned by the function
     *            CAPI_REGISTER
     * @param message
     *            the message being passed to <b>COMMON-ISDN-API</b>.
     * @throws CapiException
     */
    public void putMessage(int appID, byte[] message) throws CapiException;

    /**
     * With this operation the application retrieves a message from
     * <b>COMMON-ISDN-API</b>. The application can only retrieve those messages
     * intended for the stipulated application identification number. If there
     * is no message waiting for retrieval, the function returns immediately
     * with an error code.
     * 
     * @param appID
     *            the application identification number assigned by the function
     *            CAPI_REGISTER
     * @return
     * @throws CapiException
     */
    public byte[] getMessage(int appID) throws CapiException;

    /**
     * This operation is used by the application to wait for an asynchronous
     * event from <b>COMMON-ISDN-API</b>.
     * <p/>
     * This function returns as soon as a message from <b>COMMON-ISDN-API</b> is
     * available.
     * <p/>
     * This function also returns as soon as the application calls CAPI_RELEASE,
     * even if no pending <b>COMMON-ISDN-API</b> message is available in the
     * <b>COMMON-ISDN-API</b> message queue. The <b>COMMON-ISDN-API</b>
     * application shall not destroy the thread while CAPI_WAIT_FOR_SIGNAL is in
     * the blocking state.
     * 
     * @param appID
     *            the application identification number assigned by the function
     *            CAPI_REGISTER
     * @return signal received
     * @throws CapiException
     */
    public boolean waitForSignal(int appID) throws CapiException;

    /**
     * This function can be used by an application to determine whether the ISDN
     * hardware and necessary drivers are installed.
     * 
     * @return
     * @throws CapiException
     */
    public boolean isInstalled() throws CapiException;

    /**
     * With this operation the application determines the manufacturer
     * identification of kernel-mode <b>COMMON-ISDN-API</b> or of the
     * controller(s).
     * 
     * @param controller
     *            the number of the controller. If 0, the manufacturer
     *            identification of the kernel driver is given to the
     *            application.
     * @return
     * @throws CapiException
     */
    public String getManufacturer(int controller) throws CapiException;

    /**
     * With this function the application obtains the version of
     * <b>COMMON-ISDN-API</b>, as well as an internal revision number.
     * 
     * @param controller
     *            the number of the controller. If 0, the revision number of the
     *            kernel driver is given to the application.
     * @return <pre>
     * struct capi_version_params { 
     *  word CAPIMajor; 
     *  word CAPIMinor; 
     *  word ManufacturerMajor; 
     *  word ManufacturerMinor; 
     * };
     * </pre>
     * @throws CapiException
     */
    public int getVersion(int controller) throws CapiException;

    /**
     * With this operation the application determines the (optional) serial
     * number of kernel-mode <b>COMMON-ISDN-API</b> or of the controller(s).
     * 
     * @param controller
     *            the number of the controller. If 0, the serial number of the
     *            kernel driver is given to the application.
     * @return
     * @throws CapiException
     */
    public String getSerialNumber(int controller) throws CapiException;

    /**
     * The application uses this function to get the capabilities from <b>COMMON-ISDN-API</b>. 
     * @param controller
     *            the number of Controller. If 0, only the number of installed
     *            controllers is returned to the application.
     * @return
     * @throws CapiException
     */
    public byte[] getProfile(int controller) throws CapiException;

	/**
	 * @see Capi#waitForSignal(int)
	 */
	public boolean waitForSignal(int appID, long timeoutMillis) throws CapiException;

}
