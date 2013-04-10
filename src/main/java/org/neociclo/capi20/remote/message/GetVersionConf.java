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
package org.neociclo.capi20.remote.message;

import static java.lang.String.format;
import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
public class GetVersionConf extends ControlMessage {

    private ChannelBuffer buffer;

    private byte capiMajor;
    private byte capiMinor;
    private byte manufacturerMajor;
    private byte manufacturerMinor;
    private String versionInfo;

    public GetVersionConf(short appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.GET_VERSION_CONF, buffer);
    }

    public int intValue() {
        int version = (capiMajor << 24);
        version |= (capiMinor << 16);
        version |= (manufacturerMajor << 8);
        version |= (manufacturerMinor & 0xff);
        return version;
    }

    public byte getCapiMajor() {
        return capiMajor;
    }

    public byte getCapiMinor() {
        return capiMinor;
    }

    public byte getManufacturerMajor() {
        return manufacturerMajor;
    }

    public byte getManufacturerMinor() {
        return manufacturerMinor;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    @Override
    public String toString() {
        return format("%s(%s, appID: %d, capi: %d.%d, manufacturer: %d.%d, info: %s)", getClass().getSimpleName(),
                getOperation(), getAppID(), getCapiMajor(), getCapiMinor(), getManufacturerMajor(),
                getManufacturerMinor(), getVersionInfo());
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public void parse(ChannelBuffer buf) {

        capiMajor = readOctet(buf);
        capiMinor = readOctet(buf);
        manufacturerMajor = readOctet(buf);
        manufacturerMinor = readOctet(buf);

        byte[] infoStruct = readStruct(buf);
        versionInfo = capiString(infoStruct);

        this.buffer = buf;

    }

    @Override
    public ChannelBuffer getBuffer() {
        buffer.readerIndex(SIZE_OF_MESSAGE_HEADER);
        return buffer;
    }

    @Override
    public int getTotalLength() {
        return buffer.capacity();
    }

}
