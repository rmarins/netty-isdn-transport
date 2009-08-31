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
package org.neociclo.capi20.remote.message;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public enum CapiOperation {

    ALIVE_IND(0x00, 0xff), ALIVE_RESP(0x01, 0xff),

    GET_PROFILE_REQ(0xe0, 0xff), GET_PROFILE_CONF(0xe1, 0xff),

    REGISTER_REQ(0xf2, 0xff), REGISTER_CONF(0xf3, 0xff),

    GET_MANUFACTURER_REQ(0xfa, 0xff), GET_MANUFACTURER_CONF(0xfb, 0xff),

    GET_VERSION_REQ(0xfc, 0xff), GET_VERSION_CONF(0xfd, 0xff),

    GET_SERIALNUMBER_REQ(0xfe, 0xff), GET_SERIALNUMBER_CONF(0xff, 0xff),

    CONTROL_REQ(0xff, 0x00), CONTROL_CONF(0xff, 0x01), CONTROL_IND(0xff, 0x02), CONTROL_RESP(0xff, 0x03);

    public static CapiOperation valueOf(byte command, byte subCommand) {
        for (CapiOperation oper : CapiOperation.values()) {
            if (oper.getCommand() == command && oper.getSubCommand() == subCommand) {
                return oper;
            }
        }
        return null;
    }

    private byte command;
    private byte subCommand;

    private CapiOperation(int command, int subCommand) {
        this.command = (byte) (command & 0xff);
        this.subCommand = (byte) (subCommand & 0xff);
    }

    public byte getCommand() {
        return command;
    }

    public byte getSubCommand() {
        return subCommand;
    }

}
