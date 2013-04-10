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

import net.sourceforge.jcapi.Jcapi;

import org.neociclo.capi20.Capi;
import org.neociclo.capi20.jcapi.JcapiAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
public class JCapiFactory implements CapiFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JCapiFactory.class);

    private static Capi jCapiSingleton;

    public Capi getCapi() {

        if (jCapiSingleton == null) {
            try {
                jCapiSingleton = new JcapiAdapter(new Jcapi());
            } catch (IllegalStateException ise) {
                LOGGER.error("Cannot create the Capi instance. JCapi initialization failed.", ise);
            }
        }
        return jCapiSingleton;
    }

    public void releaseExternalResources() {
        // TODO Auto-generated method stub
    }

}
