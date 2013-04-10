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
package org.neociclo.capi20.remote;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Rafael Marins
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
