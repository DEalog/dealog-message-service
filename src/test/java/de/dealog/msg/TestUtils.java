package de.dealog.msg;

import de.dealog.msg.persistence.MessageEntity;
import de.dealog.msg.rest.MessageRest;

import java.util.Date;

public class TestUtils {

    public static MessageEntity buildMessage(final String identifier, final String headline, final String description) {
        MessageEntity message = new MessageEntity();
        message.setIdentifier(identifier);
        message.setDescription(description);
        message.setHeadline(headline);
        message.setPublishedAt(new Date());
        return message;
    }

    public static MessageRest buildMessageRest(final String identifier, final String headline, final String description) {
        MessageRest messageRest = new MessageRest();
        messageRest.setIdentifier(identifier);
        messageRest.setDescription(description);
        messageRest.setHeadline(headline);
        messageRest.setPublishedAt(new Date());
        return messageRest;
    }
}
