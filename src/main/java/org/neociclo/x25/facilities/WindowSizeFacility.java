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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class WindowSizeFacility extends Facility {

    private byte transmitSize;
    private byte receiveSize;

    /**
     * Window sizes of 8 to 127 are only valid if extended sequence numbering is used.
     * 
     * @param receiveSize Usually values from 1 to 7. 
     * @param transmitSize Usually values from 1 to 7.
     */
    public WindowSizeFacility(byte receiveSize, byte transmitSize) {
        super(FacilityType.WINDOW_SIZE);
        setReceiveSize(receiveSize);
        setTransmitSize(transmitSize);
    }

    public WindowSizeFacility(byte[] buffer) {
        this(buffer[0], buffer[1]);
    }

    public byte getTransmitSize() {
        return transmitSize;
    }

    private void setTransmitSize(byte size) {
        this.transmitSize = (byte) (size & 0x7f);
    }

    public byte getReceiveSize() {
        return receiveSize;
    }

    private void setReceiveSize(byte size) {
        this.receiveSize = (byte) (size & 0x7f);
    }

    @Override
    public byte[] encoded() {
        byte[] encodedFacility = new byte[getLength()];
        encodedFacility[0] = FacilityType.WINDOW_SIZE.getCode();
        encodedFacility[1] = getReceiveSize();  // receive window size first
        encodedFacility[2] = getTransmitSize(); // transmit window size last
        return encodedFacility;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("WindowSizeFacility[out: ");
        sb.append((int) getTransmitSize()).append(", in: ").append((int) getReceiveSize()).append("]");
        return sb.toString();
    }

}
