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
package org.neociclo.capi20.parameter;

import org.neociclo.capi20.util.IBitType;

/**
 * Compatibility Information Profile.
 *
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public enum CompatibilityInformationProfile implements IBitType {

    NO_PREDEFINED_PROFILE (0),
    SPEECH (1),
    UNRESTRICTED_DIGITAL (2),
    RESTRICTED_DIGITAL (3),
    AUDIO_3DOT1_KHZ (4),
    AUDIO_7_KHZ (5),
    VIDEO (6),
    PACKET_MODE (7),
    ADAPTATION_56_KBPS (8),
    UNRESTRICTED_DIGITAL_WITH_TONES (9),
    TELEPHONY (16),
    GROUP_2OR3_FACSIMILE (17),
    GROUP_4_FACSIMILE_CLASS1 (18),
    TELEX_SERVICE_MIXED_GROUP4_FACSIMILE (19),
    TELEX_SERVICE_PROCESSABLE (20),
    TELEX_SERVICE_BASIC_MODE (21),
    INTERNATIONAL_INTERWORKING_VIDEOTEX (22),
    TELEX (23),
    MHS_X400 (24),
    OSI_X200 (25),
    TELEPHONY_7_KHZ (26),
    VIDEO_TELEPHONY_1ST_CONNECTION (27),
    VIDEO_TELEPHONY_2ND_CONNECTION (28);

    public static CompatibilityInformationProfile valueOf(int v) {
        for (CompatibilityInformationProfile cip : CompatibilityInformationProfile.values()) {
            if (cip.getBitField() == v) {
                return cip;
            }
        }
        return null;
    }

    private byte bitField;

    private CompatibilityInformationProfile(int v) {
        this.bitField = (byte) v;
    }

    public int getBitField() {
        return bitField;
    }
    
}
