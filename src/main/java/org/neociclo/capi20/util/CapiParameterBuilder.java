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
package org.neociclo.capi20.util;

import static org.neociclo.capi20.util.CapiBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.Profile;

/**
 * @author Rafael Marins
 */
public class CapiParameterBuilder {

    private CapiParameterBuilder() { }

    public static Profile buildProfile(ChannelBuffer message) {

        // read controller identifier
        short controllerID = readWord(message);

        short bChannelsCount = readWord(message);
        int globalOptions = readDword(message);
        int b1 = readDword(message);
        int b2 = readDword(message);
        int b3 = readDword(message);

        // skip 24-octets reserved
        message.skipBytes(24);

        // read manufacturer specific octets
        byte[] extensions = new byte[20];
        message.readBytes(extensions);

        return new Profile(controllerID, bChannelsCount, globalOptions, b1, b2, b3, extensions);
    }

}
