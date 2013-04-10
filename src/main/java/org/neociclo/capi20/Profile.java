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
package org.neociclo.capi20;

import static org.neociclo.capi20.util.Bits.*;

import java.util.ArrayList;

import org.neociclo.capi20.parameter.B1Protocol;
import org.neociclo.capi20.parameter.B2Protocol;
import org.neociclo.capi20.parameter.B3Protocol;
import org.neociclo.capi20.parameter.GlobalOption;


/**
 * @author Rafael Marins
 */
public class Profile {

    private short controllerID;

    private short numberOfBChannels;

    private int options;

    private int b1Support;

    private int b2Support;

    private int b3Support;

    private byte[] ext;

    public Profile(short controllerID, short numberOfBChannels, int globalOptions, int b1, int b2, int b3, byte[] extensions) {
        super();
        this.controllerID = controllerID;
        this.numberOfBChannels = numberOfBChannels;
        this.options = globalOptions;
        this.b1Support = b1;
        this.b2Support = b2;
        this.b3Support = b3;
        this.ext = extensions;
    }

    public short getControllerID() {
        return controllerID;
    }

    public short getNumberOfBChannels() {
        return numberOfBChannels;
    }

    public int options() {
        return options;
    }

    public int b1() {
        return b1Support;
    }

    public int b2() {
        return b2Support;
    }

    public int b3() {
        return b3Support;
    }

    public byte[] ext() {
        return ext;
    }

    private <T> boolean checkedIsSupported(T feature) {

        if (feature instanceof B1Protocol) {
            return isSupported((B1Protocol) feature);
        } else if (feature instanceof B2Protocol) {
            return isSupported((B2Protocol) feature);
        } else if (feature instanceof B3Protocol) {
            return isSupported((B3Protocol) feature);
        } else if (feature instanceof GlobalOption) {
            return hasOption((GlobalOption) feature);
        }

        return false;
    }

    public boolean hasOption(GlobalOption globalOption) {
        if (globalOption == null) {
            throw new NullPointerException("globalOption");
        }
        return isBitSet(options(), globalOption.getBitField());
    }

    public boolean isSupported(B1Protocol capability) {
        if (capability == null) {
            throw new NullPointerException("capability");
        }
        return isBitSet(b1(), capability.getBitField());
    }

    public boolean isSupported(B2Protocol capability) {
        if (capability == null) {
            throw new NullPointerException("capability");
        }
        return isBitSet(b2(), capability.getBitField());
    }

    public boolean isSupported(B3Protocol capability) {
        if (capability == null) {
            throw new NullPointerException("capability");
        }
        return isBitSet(b3(), capability.getBitField());
    }

    public GlobalOption[] getOptionsAvailable() {
        return getAllProtocolsSupported(GlobalOption.values());
    }

    public B1Protocol[] getAllSupportedB1() {
        return getAllProtocolsSupported(B1Protocol.values());
    }

    public B2Protocol[] getAllSupportedB2() {
        return getAllProtocolsSupported(B2Protocol.values());
    }

    public B3Protocol[] getAllSupportedB3() {
        return getAllProtocolsSupported(B3Protocol.values());
    }

    @SuppressWarnings("unchecked")
    private <T> T[] getAllProtocolsSupported(T[] values) {
        ArrayList<T> result = new ArrayList<T>();
        for (T cap : values) {
            if (checkedIsSupported(cap)) {
                result.add(cap);
            }
        }
        return (T[]) result.toArray();
    }

}
