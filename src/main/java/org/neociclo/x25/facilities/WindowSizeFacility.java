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
