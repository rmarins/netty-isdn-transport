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
package org.neociclo.capi20.parameter;

import org.neociclo.capi20.util.IBitType;

/**
 * Compatibility Information Profile.
 *
 * @author Rafael Marins
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
