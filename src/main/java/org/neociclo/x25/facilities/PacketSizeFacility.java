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
public class PacketSizeFacility extends Facility {

    private PacketSize receiveSize;
    private PacketSize transmitSize;

    public PacketSizeFacility(PacketSize receiveSize, PacketSize transmitSize) {
        super(FacilityType.PACKET_SIZE);
        this.receiveSize = receiveSize;
        this.transmitSize = transmitSize;
    }

    public PacketSizeFacility(byte[] buffer) {
        this(PacketSize.valueOf(buffer[0]), PacketSize.valueOf(buffer[1]));
    }

    public PacketSize getReceiveSize() {
        return receiveSize;
    }

    public PacketSize getTransmitSize() {
        return transmitSize;
    }

    @Override
    public byte[] encoded() {
        byte[] encodedFacility = new byte[getLength()];
        encodedFacility[0] = FacilityType.PACKET_SIZE.getCode();
        encodedFacility[1] = getReceiveSize().getSizeCode();  // receive packet size first
        encodedFacility[2] = getTransmitSize().getSizeCode(); // transmit window size last
        return encodedFacility;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("PacketSizeFacility[out: ");
        sb.append(getTransmitSize()).append(", in: ").append(getReceiveSize()).append("]");
        return sb.toString();
    }

}
