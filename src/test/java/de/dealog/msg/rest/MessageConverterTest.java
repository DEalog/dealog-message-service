package de.dealog.msg.rest;

import com.google.common.collect.Iterators;
import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.Message;
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
        Message message = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        MessageRest messageRest = messageConverter.doForward(message);
        assert messageRest != null;
        assertEquals(message.getIdentifier(), messageRest.getIdentifier());
        assertEquals(message.getHeadline(), messageRest.getHeadline());
        assertEquals(message.getDescription(), messageRest.getDescription());

        Message msg_one = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        Message msg_two = TestUtils.buildMessage("10", "This is the second headline", "This is the second description");
        Message msg_three = TestUtils.buildMessage("100", "This is the third headline", "This is the third description");
        Iterable<MessageRest> messageRests = messageConverter.convertAll(Arrays.asList(msg_one, msg_two, msg_three));

        int size = Iterators.size(messageRests.iterator());
        assertEquals(3, size);
        MessageRest messageRest_three = Iterators.get(messageRests.iterator(), 2);

        assertEquals(msg_three.getIdentifier(), messageRest_three.getIdentifier());
        assertEquals(msg_three.getHeadline(), messageRest_three.getHeadline());
        assertEquals(msg_three.getDescription(), messageRest_three.getDescription());
    }

    @Test
    void doBackward() {
        MessageRest messageRest = TestUtils.buildMessageRest("1", "This is the headline", "This is the description");
        Message message = messageConverter.doBackward(messageRest);
        assert message != null;
        assertEquals(messageRest.getIdentifier(), message.getIdentifier());
        assertEquals(messageRest.getHeadline(), message.getHeadline());
        assertEquals(messageRest.getDescription(), message.getDescription());
    }
}