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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
 */
public enum PacketSize {

    ENCODED_16 ((byte) 4),
    ENCODED_32 ((byte) 5),
    ENCODED_64 ((byte) 6),
    ENCODED_128 ((byte) 7),
    ENCODED_256 ((byte) 8),
    ENCODED_512 ((byte) 9),
    ENCODED_1024 ((byte) 10),
    ENCODED_2048 ((byte) 11),
    ENCODED_4096 ((byte) 12);

    public static PacketSize valueOf(byte encodedSize) {
        for (PacketSize p : PacketSize.values()) {
            if (p.getSizeCode() == encodedSize) {
                return p;
            }
        }
        return null;
    }

    private byte sizeCode;

    private PacketSize(byte code) {
        this.sizeCode = code;
    }

    public byte getSizeCode() {
        return sizeCode;
    }

}
