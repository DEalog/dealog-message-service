package de.dealog.msg.rest;

import com.google.common.collect.Iterators;
import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.rest.model.MessageRest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MessageConverterTest {

    @Inject
    MessageConverter messageConverter;

    @Test
    void doForward() {
        Message message = TestUtils.buildMessage("1", "This is the headline", "This is the description", "091790134134");
        MessageRest messageRest = messageConverter.doForward(message);
        assert messageRest != null;
        assertEquals(message.getIdentifier(), messageRest.getIdentifier());
        assertEquals(message.getHeadline(), messageRest.getHeadline());
        assertEquals(message.getDescription(), messageRest.getDescription());
        assertEquals(message.getCategory(), messageRest.getCategory());
        assertEquals(TestUtils.MY_ORG, messageRest.getOrganization());

        Message msg_one = TestUtils.buildMessage("1", "This is the headline", "This is the description", "091790134134");
        Message msg_two = TestUtils.buildMessage("10", "This is the second headline", "This is the second description", "091790134134");
        Message msg_three = TestUtils.buildMessage("100", "This is the third headline", "This is the third description", "091790134134");
        Iterable<MessageRest> messageRests = messageConverter.convertAll(Arrays.asList(msg_one, msg_two, msg_three));

        int size = Iterators.size(messageRests.iterator());
        assertEquals(3, size);
        MessageRest messageRest_three = Iterators.get(messageRests.iterator(), 2);

        assertEquals(msg_three.getIdentifier(), messageRest_three.getIdentifier());
        assertEquals(msg_three.getHeadline(), messageRest_three.getHeadline());
        assertEquals(msg_three.getDescription(), messageRest_three.getDescription());
        assertEquals(msg_three.getCategory(), messageRest_three.getCategory());
        assertEquals(TestUtils.MY_ORG, message.getOrganization());
    }

    @Test
    void doBackward() {
        MessageRest messageRest = TestUtils.buildMessageRest("1", "This is the headline", "This is the description");
        Message message = messageConverter.doBackward(messageRest);
        assert message != null;
        assertEquals(messageRest.getIdentifier(), message.getIdentifier());
        assertEquals(messageRest.getHeadline(), message.getHeadline());
        assertEquals(messageRest.getDescription(), message.getDescription());
        assertEquals(messageRest.getCategory(), message.getCategory());
        assertEquals(TestUtils.MY_ORG, message.getOrganization());
    }
}