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

import java.util.ArrayList;

import org.neociclo.capi20.Controller;
import org.neociclo.capi20.SimpleCapi;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
interface IsdnChannelInternal extends IsdnChannel {

    SimpleCapi capi();

    IsdnWorker worker();

    void selectController(ArrayList<Controller> controllers);

    void setInitialized(boolean b);

    boolean isInitialized();

    boolean setClosed();

    void setInterestOpsNow(int interestOps);

    Object interestOpsLock();

}
