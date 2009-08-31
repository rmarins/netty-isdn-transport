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
package org.neociclo.capi20;

import static org.easymock.EasyMock.*;

import org.neociclo.isdn.netty.channel.CapiFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class MockCapiFactory implements CapiFactory {

    private final Capi mock;

    private final SimpleCapi simpleCapi;

    public MockCapiFactory(boolean threadSafe) {
        super();
        this.mock = createMock(Capi.class);
        this.simpleCapi = new SimpleCapi(mock);
        makeThreadSafe(mock, threadSafe);
    }

    public Capi getCapi() {
        return mock;
    }

    public SimpleCapi getSimpleCapi() {
        return simpleCapi;
    }

    public void releaseExternalResources() {
        // do nothing
    }

}
