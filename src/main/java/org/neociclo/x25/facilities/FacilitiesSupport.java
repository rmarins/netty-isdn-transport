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
package org.neociclo.x25.facilities;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
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
