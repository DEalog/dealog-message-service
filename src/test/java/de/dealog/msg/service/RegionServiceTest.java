package de.dealog.msg.service;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.persistence.repository.RegionRepository;
import de.dealog.msg.service.model.PagedList;
import de.dealog.msg.service.model.QueryParams;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class RegionServiceTest {

    public static final int PAGE_NUMBER_ONE = 1;
    public static final int PAGE_SIZE_3 = 3;
    public static final long TOTAL_COUNT = Long.MAX_VALUE;

    @Inject
    RegionService regionService;

    @InjectMock
    RegionRepository regionRepository;

    @Test
    void findRegions() {
        Region region_one = TestUtils.buildRegion("000000000000", "Deutschland", "Bundesrepublik");
        Region region_two = TestUtils.buildRegion("03355", "Lüneburg", "Landkreis");
        Region region_three = TestUtils.buildRegion("010510044044", "Heide", "Stadt");

        List<Region> regions = Arrays.asList(region_one, region_two, region_three);

        PanacheQuery query = Mockito.mock(PanacheQuery.class);
        PanacheQuery queryPage = Mockito.mock(PanacheQuery.class);
        when(queryPage.list()).thenReturn(regions);

        int pageCount = (int) (TOTAL_COUNT / PAGE_SIZE_3);
        when(query.pageCount()).thenReturn(pageCount);
        when(query.page(1, PAGE_SIZE_3)).thenReturn(queryPage);
        when(query.count()).thenReturn(TOTAL_COUNT);
        when(regionRepository.find(anyString(), any(Parameters.class))).thenReturn(query);

        PagedList<? extends Region> list = regionService.findHierachy(QueryParams.builder().build(), PAGE_NUMBER_ONE, PAGE_SIZE_3);

        assertThat(PAGE_NUMBER_ONE, is(list.getPage()));
        assertThat(regions.size(), is(list.getPageSize()));
        assertThat(TOTAL_COUNT, is(list.getCount()));
        assertThat(pageCount, is(list.getPageCount()));
    }
}