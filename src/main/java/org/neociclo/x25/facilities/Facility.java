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
package org.neociclo.x25.facilities;

/**
 * @author Rafael Marins
 */
public abstract class Facility {

    private FacilityType type;

    public Facility(FacilityType type) {
        super();
        this.type = type;
    }

    public abstract byte[] encoded();

    public FacilityType getType() {
        return type;
    }

    public byte getLength() {

        byte length = 0;
        FacilityClass fclazz = type.getClazz();

        /* consider the facility qualifier itself when computing length */
        if (fclazz == FacilityClass.DOUBLE) {
            length = 3;
        }

        // TODO add support for facilities with other different lengths

        return length;
    }

}
