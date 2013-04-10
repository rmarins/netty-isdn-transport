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

import static org.easymock.EasyMock.*;

import org.neociclo.isdn.CapiFactory;

/**
 * @author Rafael Marins
 */
public class MockCapiFactory implements CapiFactory {

    private static SimpleCapi createSimpleCapi(Capi mock) {
    	return new SimpleCapi(mock) {
        	@Override
        	public int getNumberOfControllers() throws CapiException {
        	    return 1;
        	}
        };
    }

    private final Capi mock;

    private final SimpleCapi simpleCapi;

    public MockCapiFactory(boolean threadSafe) {
        super();
        this.mock = createMock(Capi.class);
        this.simpleCapi = createSimpleCapi(mock);
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
