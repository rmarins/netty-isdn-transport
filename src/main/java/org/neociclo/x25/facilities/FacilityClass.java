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

public enum FacilityClass {

    /** ClasS A - For single octet parameter field */
    SINGLE,

    /** Class B - For double octet parameter field */
    DOUBLE,

    /** Class C - For triple octet parameter field */
    TRIPLE,

    /** Class C - For variable length parameter field */
    VARIABLE;

}