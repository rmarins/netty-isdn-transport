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
package org.neociclo.capi20.message;

import static java.lang.String.format;
import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;

import java.util.Arrays;

import net.sourceforge.jcapi.message.parameter.NCCI;
import net.sourceforge.jcapi.message.parameter.NCPI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.B3Protocol;

/**
 * @author Rafael Marins
 */
public class ConnectB3ActiveInd extends ReceiveMessage {

    private NCCI ncci;
    private NCPI ncpi;
    private B3Protocol b3Protocol;

    public ConnectB3ActiveInd(ChannelBuffer buffer, B3Protocol b3Protocol) {
        super(CONNECT_B3_ACTIVE_IND, buffer);
        this.b3Protocol = b3Protocol;
    }

    protected void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    protected void setNcpi(NCPI ncpi) {
        this.ncpi = ncpi;
    }

    public NCCI getNcci() {
        return ncci;
    }

    public NCPI getNcpi() {
        return ncpi;
    }

    public B3Protocol getB3Protocol() {
        return b3Protocol;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setNcci(readNcci(buf));
        setNcpi(readNcpi(buf, getB3Protocol(), getType()));
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, ncci: %d)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getNcci().getNcciValue());
    }
}
