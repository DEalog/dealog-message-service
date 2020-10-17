package de.dealog.msg.rest;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedList;
import de.dealog.msg.service.MessageService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
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
        Message msg_one = TestUtils.buildMessage(UUID_ONE, "This is the headline", "This is the description");
        Message msg_two = TestUtils.buildMessage(UUID_TWO, "This is the second headline", "This is the second description");
        Message msg_three = TestUtils.buildMessage(UUID_THREE, "This is the third headline", "This is the third description");
        PagedList<? extends Message> pagedList = PagedList.<Message>builder()
                .page(0).pageSize(10).pageCount(1).count(666).content(Arrays.asList(msg_one, msg_two, msg_three)).build();
        MessageService messageService = Mockito.mock(MessageService.class);

        doReturn(pagedList).when(messageService).list(null, 0, 10);
        QuarkusMock.installMockForType(messageService, MessageService.class);
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