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
package org.neociclo.isdn.netty.handler;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.event.DefaultEventFactory;
import org.apache.mina.statemachine.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public class MyEventFactory extends DefaultEventFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyEventFactory.class);

    @Override
    public Event create(StateContext context, Method method, Object[] arguments) {
        LOGGER.trace("create() :: method = {}, arguments = {}, context = {}",
                new Object[] {
                    method.getName(),
                    Arrays.toString(arguments),
                    context });
        return super.create(context, method, arguments);
    }

}
