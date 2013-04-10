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
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 */
public class RegisterConf extends ControlMessage {

    public static final int SIZE_OF_REGISTER_CONF = SIZE_OF_MESSAGE_HEADER + WORD_SIZE;

    private ChannelBuffer buffer;
    private Info info;

    public RegisterConf(int appID, ChannelBuffer buffer) {
        super(appID, CapiOperation.REGISTER_CONF, buffer);
    }

    public Info getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return format("%s(%s, appID: %d, info: %s)", getClass().getSimpleName(),
                getOperation(), getAppID(), getInfo());
    }

    // -------------------------------------------------------------------------
    // ControlMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    public void parse(ChannelBuffer buf) {

        info = Info.valueOf(readWord(buf));
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
