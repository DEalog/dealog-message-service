package de.dealog.msg.converter;

import de.dealog.msg.persistence.model.RegionType;
import de.dealog.msg.rest.model.RegionTypeRest;

import java.util.EnumMap;
import java.util.Map;

public class EnumConverter {

        public static final Map<RegionTypeRest, RegionType> regionTypeRestToRegionType;

        static {
            regionTypeRestToRegionType = new EnumMap<>(RegionTypeRest.class);
            regionTypeRestToRegionType.put(RegionTypeRest.COUNTRY, RegionType.COUNTRY);
            regionTypeRestToRegionType.put(RegionTypeRest.STATE, RegionType.STATE);
            regionTypeRestToRegionType.put(RegionTypeRest.COUNTY, RegionType.COUNTY);
            regionTypeRestToRegionType.put(RegionTypeRest.DISTRICT, RegionType.DISTRICT);
            regionTypeRestToRegionType.put(RegionTypeRest.MUNICIPALITY, RegionType.MUNICIPALITY);
            regionTypeRestToRegionType.put(RegionTypeRest.UNKNOWN, RegionType.UNKNOWN);
        }
}
