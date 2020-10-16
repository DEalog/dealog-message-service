package de.dealog.msg.rest;

import com.google.common.base.Converter;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.rest.model.MessageRest;

import javax.inject.Singleton;

@Singleton
public class MessageConverter extends Converter<Message, MessageRest> {

    @Override
    protected MessageRest doForward(final Message message) {
        final MessageRest messageRest= new MessageRest();
        messageRest.setIdentifier(message.getIdentifier());
        messageRest.setHeadline(message.getHeadline());
        messageRest.setDescription(message.getDescription());
        messageRest.setPublishedAt(message.getPublishedAt());
        return messageRest;
    }

    @Override
    protected Message doBackward(final MessageRest messageRest) {
        final MessageEntity message= new MessageEntity();
        message.setIdentifier(messageRest.getIdentifier());
        message.setHeadline(messageRest.getHeadline());
        message.setDescription(messageRest.getDescription());
        message.setPublishedAt(messageRest.getPublishedAt());
        return message;
    }
}
