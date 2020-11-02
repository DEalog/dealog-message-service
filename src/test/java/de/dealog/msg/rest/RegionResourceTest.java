package de.dealog.msg.rest;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedList;
import de.dealog.msg.rest.model.RegionTypeRest;
import de.dealog.msg.rest.validations.ValidGeoRequest;
import de.dealog.msg.service.RegionService;
import de.dealog.msg.service.model.QueryParams;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;

import static de.dealog.msg.TestUtils.SIZE_FAILS;
import static de.dealog.msg.rest.RegionResource.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
class RegionResourceTest {

    @Inject
    RegionService regionService;

    @Inject
    RegionConverter regionConverter;

    @BeforeAll
    public static void setup() {
        final Region region_one = TestUtils.buildRegion("03355", "Lüneburg", "Landkreis");
        final Region region_two = TestUtils.buildRegion("000000000000", "Deutschland", "Bundesrepublik");
        final Region region_three = TestUtils.buildRegion("010510044044", "Heide", "Stadt");
        final PagedList<? extends Region> pagedList = PagedList.<Region>builder()
                .page(0).pageSize(10).pageCount(1).count(666).content(Arrays.asList(region_one, region_two, region_three)).build();
        final RegionService regionService = Mockito.mock(RegionService.class);

        doReturn(pagedList).when(regionService).findHierachy(any(QueryParams.class), anyInt(), anyInt());

        QuarkusMock.installMockForType(regionService, RegionService.class);
    }

    @Test
    void findHierachy() {
       given()
               .param(PATH_ARS, "091790134135")
               .when().get(RESOURCE_PATH + "/" + PATH_HIERACHY)
               .then()
                .statusCode(200)
                .body(startsWith("[{\"ars\":\"000000000000\""))
                .body(containsString("\"type\":\""+ RegionTypeRest.COUNTRY.name() + "\""))
                .body(containsString("\"type\":\""+ RegionTypeRest.DISTRICT.name() + "\""))
                .body(containsString("\"type\":\""+ RegionTypeRest.MUNICIPALITY.name() + "\""))
                .body(endsWith("\"type\":\"MUNICIPALITY\"}]"));
    }

    @Test
    void findHierachy_ArsMinSizeFails() {
        given()
                .param("ars", "0")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(RESOURCE_PATH + "/" + PATH_HIERACHY)
                .then()
                .statusCode(400)
                .body(containsString(TestUtils.SIZE_FAILS));
    }

    @Test
    void findHierachy_ArsMaxSizeFails() {
        given()
                .param("ars", "0917901341345")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(MessageResource.RESOURCE_PATH)
                .then()
                .statusCode(400)
                .body(containsString(TestUtils.SIZE_FAILS));
    }

    @Test
    void findHierachy_ArsPatternFails() {
        given()
                .param("ars", "0a1b2c3d4f")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(RESOURCE_PATH + "/" + PATH_HIERACHY)
                .then()
                .statusCode(400)
                .body(containsString(TestUtils.PATTERN_FAILS));
    }

    @Test
    void findHierachy_LatAndLongFails() {
        given()
                .param(GeoRequest.LATITUDE, 48.21667)
                .when().get(RESOURCE_PATH + "/" + PATH_HIERACHY)
                .then()
                .statusCode(400)
                .body(containsString(ValidGeoRequest.MESSAGE));

        given()
                .param(GeoRequest.LONGITUDE, 11.26667)
                .when().get(RESOURCE_PATH + "/" + PATH_HIERACHY)
                .then()
                .statusCode(400)
                .body(containsString(ValidGeoRequest.MESSAGE));
    }
}