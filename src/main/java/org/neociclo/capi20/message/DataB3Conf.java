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
package org.neociclo.capi20.message;

import static org.neociclo.capi20.message.MessageType.*;
import static org.neociclo.capi20.parameter.ParameterBuffers.*;
import static org.neociclo.capi20.util.CapiBuffers.readWord;

import net.sourceforge.jcapi.message.parameter.NCCI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class DataB3Conf extends ReceiveMessage {

    private NCCI ncci;
    private int dataHandle;
    private Info info;

    public DataB3Conf(ChannelBuffer buffer) {
        super(DATA_B3_CONF, buffer);
    }

    protected void setNcci(NCCI ncci) {
        this.ncci = ncci;
    }

    protected void setDataHandle(int dataHandle) {
        this.dataHandle = dataHandle;
    }

    protected void setInfo(Info info) {
        this.info = info;
    }

    public NCCI getNcci() {
        return ncci;
    }

    public int getDataHandle() {
        return dataHandle;
    }

    public Info getInfo() {
        return info;
    }

    // -------------------------------------------------------------------------
    // ReceiveMessage implementation overriding
    // -------------------------------------------------------------------------

    @Override
    protected void setValues(ChannelBuffer buf) {
        setNcci(readNcci(buf));
        setDataHandle(readWord(buf));
        setInfo(readInfo(buf));
    }

}
