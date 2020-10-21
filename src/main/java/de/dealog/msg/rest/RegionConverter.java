package de.dealog.msg.rest;

import com.google.common.base.Converter;
import de.dealog.msg.converter.UnsupportedConversionException;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.rest.model.RegionRest;
import de.dealog.msg.rest.model.RegionTypeRest;

import javax.inject.Singleton;

@Singleton
public class RegionConverter extends Converter<Region, RegionRest> {

    @Override
    protected RegionRest doForward(final Region region) {
        final RegionRest regionRest = new RegionRest();
        regionRest.setArs(region.getCode());
        regionRest.setName(region.getName());
        regionRest.setType(RegionTypeRest.findByIdentifier(region.getDescription()).orElse(RegionTypeRest.UNKNOWN));
        return regionRest;
    }

    @Override
    protected Region doBackward(final RegionRest regionRest) {
        throw new UnsupportedConversionException(String.format("Unsupported conversion from %s", regionRest));
    }
}
