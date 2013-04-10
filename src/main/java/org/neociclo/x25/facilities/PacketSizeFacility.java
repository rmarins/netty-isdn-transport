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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
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
