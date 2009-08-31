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
package org.neociclo.capi20.remote;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class ApplicationControllerFactory {

    private static final AtomicInteger nextAppID = new AtomicInteger(1);

    public static ApplicationController createApplicationController(RemoteCapi remoteCapi) {
        return new ApplicationController(remoteCapi.getRemoteAddress(), remoteCapi.getUser(), remoteCapi.getPasswd(),
                nextAppID.getAndIncrement());
    }

    private ApplicationControllerFactory() {
    }
}
