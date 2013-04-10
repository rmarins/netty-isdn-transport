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

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author Rafael Marins
 */
public class FacilitiesSupport {

    public static FacilitiesSupport parseBuffer(ByteBuffer buf) {

        FacilitiesSupport facilities = new FacilitiesSupport();

        // prepare regressive counter based on facilities length field
        byte count = (byte) (buf.get() - 1);

        while (count > 0) {
            byte encodedType = buf.get();
            FacilityType type = FacilityType.valueOf(encodedType);
            byte paramsLength = getParamsLength(type, buf);
            byte[] params = new byte[paramsLength];
            buf.get(params);

            // build and include the facility to results
            Facility obj = FacilityBuilder.createFacility(type, params);
            facilities.addFacility(obj);

            count -= paramsLength; // decrease the counter
        }

        return facilities;
    }

    private static byte getParamsLength(FacilityType type, ByteBuffer buf) {
        return (byte) (type.getClazz() == FacilityClass.DOUBLE ? 2 : 0);
    }

    private Map<FacilityType, Facility> facilities;

    public FacilitiesSupport() {
        super();
        this.facilities = new HashMap<FacilityType, Facility>();
    }

    public byte getBlockSize() {
        byte bsize = 0;
        Collection<Facility> col = facilities.values();
        for (Facility f : col) {
            bsize += f.getLength();
        }
        return bsize;
    }

    public void addFacility(Facility f) {
        facilities.put(f.getType(), f);
    }

    public Facility removeFacility(FacilityType type) {
        return facilities.remove(type);
    }

    public Set<FacilityType> getFacilities() {
        return facilities.keySet();
    }

    public Facility getFacility(FacilityType type) {
        return facilities.get(type);
    }

    public byte getCount() {
        return (byte) facilities.size();
    }

    public byte[] encoded() {

        byte[] block = new byte[getBlockSize() + 1];
        block[0] = (byte) block.length;

        byte counter = 0;
        Collection<Facility> col = facilities.values();
        for (Facility f : col) {
            byte[] fencode = f.encoded();
            System.arraycopy(fencode, 0, block, counter + 1, fencode.length);
            counter++;
        }

        return block;
    }

    public boolean hasFacilities() {
        return (getCount() > 0);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("FacilitiesSupport {");
        int toAdd = facilities.size();
        for (Facility fac : facilities.values()) {
            sb.append(fac.toString());
            toAdd--;
            if (toAdd > 0) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
