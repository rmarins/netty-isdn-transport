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
import static org.neociclo.capi20.message.MessageType.DISCONNECT_IND;
import static org.neociclo.capi20.parameter.ParameterBuffers.readPlci;
import static org.neociclo.capi20.parameter.ParameterBuffers.readReason;
import net.sourceforge.jcapi.message.parameter.PLCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Reason;

/**
 * @author Rafael Marins
 */
public class DisconnectInd extends ReceiveMessage {

    private PLCI plci;
    private Reason reason;

    public DisconnectInd(ChannelBuffer buffer) {
        super(DISCONNECT_IND, buffer);
    }

    public PLCI getPlci() {
        return plci;
    }

    public Reason getReason() {
        return reason;
    }

    protected void setPlci(PLCI plci) {
        this.plci = plci;
    }

    protected void setReason(Reason reason) {
        this.reason = reason;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setPlci(readPlci(buf));
        setReason(readReason(buf));
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, plci: %d, reason: %s)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getPlci().getPlciValue(), getReason());
    }

}
