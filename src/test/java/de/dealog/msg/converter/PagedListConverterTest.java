package de.dealog.msg.converter;

import com.google.common.base.Converter;
import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.rest.model.PagedListRest;
import de.dealog.msg.rest.model.RegionRest;
import de.dealog.msg.service.model.PagedList;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class PagedListConverterTest {

    @Inject
    RegionConverter regionConverter;

    @Test
    void doForward() {
        final Region region_one = TestUtils.buildRegion("03355", "Lüneburg", "Landkreis");
        final Region region_two = TestUtils.buildRegion("000000000000", "Deutschland", "Bundesrepublik");
        final Region region_three = TestUtils.buildRegion("010510044044", "Heide", "Stadt");
        final PagedList<? extends Region> pagedList = PagedList.<Region>builder()
                .page(0).pageSize(10).pageCount(1).count(666).content(Arrays.asList(region_one, region_two, region_three)).build();

        final PagedListConverter<Region, RegionRest> pagedListConverter = getPagedListConverter();
        final PagedListRest<RegionRest> convert = pagedListConverter.convert(pagedList);
        assertEquals(pagedList.getPage(), convert.getNumber());
        assertEquals(pagedList.getPageSize(), convert.getSize());
        assertEquals(pagedList.getCount(), convert.getTotalElements());
        assertEquals(pagedList.getPageCount(), convert.getTotalPages());
        assertEquals(pagedList.getContent().size(), convert.getContent().size());
    }

    @Test
    void doBackward() {
        final PagedListConverter<Region, RegionRest> pagedListConverter = getPagedListConverter();
        final PagedListRest<RegionRest> pagedList = new PagedListRest<>();
        assertThrows(UnsupportedConversionException.class, () -> pagedListConverter.reverse().convert(pagedList));
    }

    private PagedListConverter<Region, RegionRest> getPagedListConverter() {
        final PagedListConverter<Region, RegionRest> pagedListConverter = new PagedListConverter<>() {
            @Override
            public void setContentConverter(final Converter<Region, RegionRest> regionConverter) {
                super.setContentConverter(regionConverter);
            }
        };
        pagedListConverter.setContentConverter(regionConverter);
        return pagedListConverter;
    }
}