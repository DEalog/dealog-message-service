package de.dealog.msg.rest;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedList;
import de.dealog.msg.service.MessageService;
import de.dealog.msg.service.model.QueryParams;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
class MessageResourceTest {

    @Inject
    MessageService messageService;

    @Inject
    MessageConverter messageConverter;

    private final static String UUID_ONE = UUID.randomUUID().toString();
    private final static String UUID_TWO = UUID.randomUUID().toString();
    private final static String UUID_THREE = UUID.randomUUID().toString();

    @BeforeAll
    public static void setup() {
        Message msg_one = TestUtils.buildMessage(UUID_ONE, "This is the headline", "This is the description", "091790134134");
        Message msg_two = TestUtils.buildMessage(UUID_TWO, "This is the second headline", "This is the second description", "091790134134");
        Message msg_three = TestUtils.buildMessage(UUID_THREE, "This is the third headline", "This is the third description", "091790134134");
        PagedList<? extends Message> pagedList = PagedList.<Message>builder()
                .page(0).pageSize(10).pageCount(1).count(666).content(Arrays.asList(msg_one, msg_two, msg_three)).build();
        MessageService messageService = Mockito.mock(MessageService.class);

        doReturn(pagedList).when(messageService).findAll(any(QueryParams.class), anyInt(), anyInt());

        doReturn(Optional.of(msg_one)).when(messageService).findOne(UUID_ONE, MessageStatus.Published);

        QuarkusMock.installMockForType(messageService, MessageService.class);
    }

    @Test
    void find() {
        given()
                .pathParam(MessageResource.PATH_IDENTIFIER, UUID_ONE)
                .when().get(MessageResource.RESOURCE_PATH + "/{" + MessageResource.PATH_IDENTIFIER + "}")
                .then()
                .statusCode(200)
                .body(containsString("\"identifier\":\"" + UUID_ONE + "\""))
                .body(containsString("\"headline\":\"This is the headline\""))
                .body(containsString("\"description\":\"This is the description\""));
    }

    @Test
    void findAll() {
        given()
            .param(PageRequest.PAGE, 0)
            .param(PageRequest.SIZE, 10)
            .when().get(MessageResource.RESOURCE_PATH)
            .then()
            .statusCode(200)
            .body(containsString("\"identifier\":\"" + UUID_ONE + "\""))
            .body(containsString("\"headline\":\"This is the headline\""))
            .body(containsString("\"description\":\"This is the description\""));
    }
}