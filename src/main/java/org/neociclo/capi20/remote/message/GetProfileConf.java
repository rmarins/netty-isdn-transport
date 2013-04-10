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

import static org.neociclo.capi20.util.CapiBuffers.*;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rafael Marins
 */
public class GetProfileConf extends ControlMessage {

    private ChannelBuffer buffer;

    public GetProfileConf(short appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.GET_PROFILE_CONF, buffer);
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public void parse(ChannelBuffer buf) {
        this.buffer = buf;
    }

    @Override
    public ChannelBuffer getBuffer() {
        // skip the control Info param return
        buffer.readerIndex(SIZE_OF_MESSAGE_HEADER + WORD_SIZE);
        return buffer;
    }

    @Override
    public int getTotalLength() {
        return buffer.capacity();
    }

}
