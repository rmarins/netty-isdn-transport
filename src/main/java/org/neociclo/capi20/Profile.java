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
package org.neociclo.capi20;

import static org.neociclo.capi20.util.Bits.*;

import java.util.ArrayList;

import org.neociclo.capi20.parameter.B1Protocol;
import org.neociclo.capi20.parameter.B2Protocol;
import org.neociclo.capi20.parameter.B3Protocol;
import org.neociclo.capi20.parameter.GlobalOption;


/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
