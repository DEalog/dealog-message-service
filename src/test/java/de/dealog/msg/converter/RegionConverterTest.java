package de.dealog.msg.converter;

import com.google.common.collect.Iterators;
import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.rest.model.RegionRest;
import de.dealog.msg.rest.model.RegionTypeRest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class RegionConverterTest {

    @Inject
    RegionConverter regionConverter;

    @Test
    void doForward() {
        final Region region = TestUtils.buildRegion("000000000000", "Deutschland", "Bundesrepublik");
        final RegionRest regionRest = regionConverter.convert(region);
        assert regionRest != null;
        assertEquals(regionRest.getArs(), region.getCode());
        assertEquals(regionRest.getName(), region.getName());
        assertThat(RegionTypeRest.COUNTRY.getIdentifiers(), hasItem(region.getDescription()));

        Region region_one = TestUtils.buildRegion("000000000000", "Deutschland", "Bundesrepublik");
        Region region_two = TestUtils.buildRegion("03355", "Lüneburg", "Landkreis");
        Region region_three = TestUtils.buildRegion("010510044044", "Heide", "Stadt");
        Iterable<RegionRest> districtRests = regionConverter.convertAll(Arrays.asList(region_one, region_two, region_three));

        int size = Iterators.size(districtRests.iterator());
        assertEquals(3, size);
        RegionRest regionRest_three = Iterators.get(districtRests.iterator(), 2);

        assertEquals(region_three.getCode(), regionRest_three.getArs());
        assertEquals(region_three.getName(), regionRest_three.getName());
        assertThat(regionRest_three.getType().getIdentifiers(), hasItem(region_three.getDescription()));
    }

    @Test
    void doBackward() {
        RegionRest regionRest = TestUtils.buildRegionRest("000000000000", "Deutschland", RegionTypeRest.COUNTRY);
        assertThrows(UnsupportedConversionException.class, () -> regionConverter.reverse().convert(regionRest));
    }
}