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

/**
 * @author Rafael Marins
 */
public class CapiVersion {

    private byte capiMajor;
    private byte capiMinor;
    private byte manufacturerMajor;
    private byte manufacturerMinor;

    /**
     * 
     * @param version the version struct param.
     */
    public CapiVersion(int version) {
        capiMajor = (byte) ((version >> 24) & 0xff);
        capiMinor = (byte) ((version >> 16) & 0xff);
        manufacturerMajor = (byte) ((version >> 8) & 0xff);
        manufacturerMinor = (byte) (version & 0xff);
    }

    public byte getCapiMajor() {
        return capiMajor;
    }

    public byte getCapiMinor() {
        return capiMinor;
    }

    public byte getManufacturerMajor() {
        return manufacturerMajor;
    }

    public byte getManufacturerMinor() {
        return manufacturerMinor;
    }

    
}
