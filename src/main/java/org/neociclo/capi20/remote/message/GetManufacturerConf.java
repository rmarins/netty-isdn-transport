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
import static org.neociclo.capi20.util.CapiBuffers.capiString;
import static org.neociclo.capi20.util.CapiBuffers.readStruct;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
public class GetManufacturerConf extends ControlMessage {

    private ChannelBuffer buffer;

    private String manufacturerInfo;

    public GetManufacturerConf(short appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.GET_MANUFACTURER_CONF, buffer);
    }

    public String getManufacturerInfo() {
        return manufacturerInfo;
    }

    @Override
    public String toString() {
        return format("%s(%s, appID: %d, %s)", getClass().getSimpleName(),
                getOperation(), getAppID(), getManufacturerInfo());
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public void parse(ChannelBuffer buf) {

        byte[] manInfoStruct = readStruct(buf);
        manufacturerInfo = capiString(manInfoStruct);

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
