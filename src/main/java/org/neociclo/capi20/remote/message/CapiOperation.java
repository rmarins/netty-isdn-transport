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

/**
 * @author Rafael Marins
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
