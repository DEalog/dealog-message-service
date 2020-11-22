package de.dealog.msg.rest;

import de.dealog.common.model.Category;
import de.dealog.common.model.Status;
import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.validations.ValidGeoRequest;
import de.dealog.msg.service.MessageService;
import de.dealog.msg.service.model.PagedList;
import de.dealog.msg.service.model.QueryParams;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@Slf4j
class MessageResourceTest {

    private final static String UUID_ONE = UUID.randomUUID().toString();
    private final static String UUID_TWO = UUID.randomUUID().toString();
    private final static String UUID_THREE = UUID.randomUUID().toString();

    @BeforeAll
    public static void setup() {
        final Message msg_one = TestUtils.buildMessage(UUID_ONE,
                "This is the headline",
                "This is the description",
                "091790134134");
        final Message msg_two = TestUtils.buildMessage(UUID_TWO,
                "This is the second headline",
                "This is the second description",
                "091790134134");
        final Message msg_three = TestUtils.buildMessage(UUID_THREE,
                "This is the third headline",
                "This is the third description",
                "091790134134");
        final PagedList<? extends Message> pagedList = PagedList.<Message>builder()
                .page(0).pageSize(3).pageCount(3).count(9).content(Arrays.asList(msg_one, msg_two, msg_three)).build();
        final MessageService messageService = Mockito.mock(MessageService.class);

        doReturn(pagedList).when(messageService).findAll(any(QueryParams.class), anyInt(), anyInt());
        doReturn(Optional.of(msg_one)).when(messageService).findOne(UUID_ONE, Status.Published);

        QuarkusMock.installMockForType(messageService, MessageService.class);
    }

    @Test
    void find() {
        given()
                .pathParam(MessageConstants.PATH__PARAM_IDENTIFIER, UUID_ONE)
                .when().get(MessageConstants.RESOURCE_PATH + "/{" + MessageConstants.PATH__PARAM_IDENTIFIER + "}")
                .then()
                .statusCode(200)
                .body(containsString("\"identifier\":\"" + UUID_ONE + "\""))
                .body(containsString("\"category\":\"" + Category.Health.name() + "\""))
                .body(containsString("\"organization\":\"" + TestUtils.MY_ORG + "\""))
                .body(containsString("\"headline\":\"This is the headline\""))
                .body(containsString("\"description\":\"This is the description\""));
    }

    @Test
    void findAll() {
        given()
            .param(PageRequest.PAGE, 0)
            .param(PageRequest.SIZE, 10)
            .when().get(MessageConstants.RESOURCE_PATH)
            .then()
            .statusCode(200)
            .body(containsString("\"identifier\":\"" + UUID_ONE + "\""))
            .body(containsString("\"category\":\"" + Category.Health.name() + "\""))
            .body(containsString("\"organization\":\"" + TestUtils.MY_ORG + "\""))
            .body(containsString("\"headline\":\"This is the headline\""))
            .body(containsString("\"description\":\"This is the description\""))
            .body(containsString("\"meta\":{\"size\":3,\"number\":0,\"totalElements\":9,\"totalPages\":3}"));
    }

    @Test
    void findAllByArs() {
        given()
                .param("ars", "091790134134")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(MessageConstants.RESOURCE_PATH)
                .then()
                .statusCode(200);
    }

    @Test
    void findAll_ArsMinSizeFails() {
        given()
                .param("ars", "0")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(MessageConstants.RESOURCE_PATH)
                .then()
                .statusCode(400)
                .body(containsString(TestUtils.SIZE_FAILS));
    }

    @Test
    void findAll_ArsMaxSizeFails() {
        given()
                .param("ars", "0917901341345")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(MessageConstants.RESOURCE_PATH)
                .then()
                .statusCode(400)
                .body(containsString(TestUtils.SIZE_FAILS));
    }

    @Test
    void findAll_ArsPatternFails() {
        given()
                .param("ars", "0a1b2c3d4f")
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .when().get(MessageConstants.RESOURCE_PATH)
                .then()
                .statusCode(400)
                .body(containsString(TestUtils.PATTERN_FAILS));
    }

    @Test
    void findAll_LatAndLongFails() {
        given()
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .param(GeoRequest.LATITUDE, 48.21667)
                .when().get(MessageConstants.RESOURCE_PATH)
                .then()
                .statusCode(400)
                .body(containsString(ValidGeoRequest.MESSAGE));

        given()
                .param(PageRequest.PAGE, 0)
                .param(PageRequest.SIZE, 10)
                .param(GeoRequest.LONGITUDE, 11.26667)
                .when().get(MessageConstants.RESOURCE_PATH)
                .then()
                .statusCode(400)
                .body(containsString(ValidGeoRequest.MESSAGE));
    }
}