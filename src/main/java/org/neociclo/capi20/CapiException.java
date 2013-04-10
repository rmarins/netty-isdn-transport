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

import org.neociclo.capi20.parameter.Info;

/**
 * @author Rafael Marins
 */
public class CapiException extends Exception {

    private static final long serialVersionUID = 2850793514379709357L;

    private Info capiError;

    public CapiException(Info error) {
        super();
        this.capiError = error;
    }

    public CapiException(Info error, String message) {
        super(message);
        this.capiError = error;
    }

	public CapiException(Info error, String message, Throwable cause) {
		super(message, cause);
		this.capiError = error;
	}

    public Info getCapiError() {
        return capiError;
    }

}
