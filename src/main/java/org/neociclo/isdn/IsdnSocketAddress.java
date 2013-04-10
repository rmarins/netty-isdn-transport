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
package org.neociclo.isdn;

import java.net.SocketAddress;

/**
 * @author Rafael Marins
 */
public class IsdnSocketAddress extends SocketAddress {

    private static final long serialVersionUID = -838183852292629142L;

    private IsdnAddress address;

    public IsdnSocketAddress(IsdnAddress address) {
        super();
        this.address = address;
    }
    
    public IsdnSocketAddress(String isdnNumber) {
        this(isdnNumber, null);
    }

    public IsdnSocketAddress(String isdnNumber, String isdnSubAddress) {
        super();
        this.address = new IsdnAddress(isdnNumber, isdnSubAddress);
    }

    public IsdnAddress getAddress() {
        return address;
    }

    public String getNumber() {
        return address.getNumber();
    }

    public String getSubAddress() {
        return address.getSubAddress();
    }

    @Override
    public String toString() {
        return getNumber() + (getSubAddress() == null ? "" : ':' + getSubAddress());
    }

	/**
     * @param msn
     * @return
     */
    public static IsdnSocketAddress valueOf(String msn) {
    	if (msn == null) {
    		return null;
    	} else {
    		return new IsdnSocketAddress(msn);
    	}
    }
}
