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
package org.neociclo.isdn.netty.channel;

import java.net.InetSocketAddress;

import org.neociclo.capi20.Capi;
import org.neociclo.capi20.remote.RemoteCapi;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class RemoteCapiFactory implements CapiFactory {

    private String host;
    private int port;
    private String user;
    private String password;

    public RemoteCapiFactory(String host, int port) {
        this(host, port, null, null);
    }

    public RemoteCapiFactory(String host, int port, String user, String passwd) {
        super();
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = passwd;
    }

    public Capi getCapi() {
        return new RemoteCapi(new InetSocketAddress(host, port), user, password);
    }

    public void releaseExternalResources() {
        // do nothing
    }

}
