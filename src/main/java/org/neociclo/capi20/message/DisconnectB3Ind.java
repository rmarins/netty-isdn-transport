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

import net.sourceforge.jcapi.message.parameter.NCCI;
import net.sourceforge.jcapi.message.parameter.NCPI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.B3Protocol;
import org.neociclo.capi20.parameter.Reason;

/**
 * @author Rafael Marins
 */
public class DisconnectB3Ind extends ReceiveMessage {

    private NCCI ncci;
    private Reason reasonB3;
    private NCPI ncpi;
    private B3Protocol b3;

    public DisconnectB3Ind(ChannelBuffer buffer, B3Protocol b3) {
        super(DISCONNECT_B3_IND, buffer);
        this.b3 = b3;
    }

    public NCCI getNcci() {
        return ncci;
    }

    public Reason getReasonB3() {
        return reasonB3;
    }

    public NCPI getNcpi() {
        return ncpi;
    }

    protected void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    protected void setReasonB3(Reason reasonB3) {
        this.reasonB3 = reasonB3;
    }

    protected void setNcpi(NCPI ncpi) {
        this.ncpi = ncpi;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setNcci(readNcci(buf));
        setReasonB3(readReason(buf));
        setNcpi(readNcpi(buf, b3, DISCONNECT_B3_IND));
    }

    @Override
    public String toString() {
        return format("%s(appID: %d, msgNum: %d, ncci: %d, reason: %s)", getClass().getSimpleName(), getAppID(),
                getMessageID(), getNcci().getNcciValue(), getReasonB3());
    }
}
