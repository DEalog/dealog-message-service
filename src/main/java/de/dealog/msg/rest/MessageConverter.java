package de.dealog.msg.rest;

import com.google.common.base.Converter;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;

import javax.inject.Singleton;

@Singleton
public class MessageConverter extends Converter<Message, MessageRest> {

    @Override
    protected MessageRest doForward(final Message message) {
        MessageRest messageRest= new MessageRest();
        messageRest.setIdentifier(message.getIdentifier());
        messageRest.setHeadline(message.getHeadline());
        messageRest.setDescription(message.getDescription());
        messageRest.setCreatedDate(message.getCreatedDate());
        return messageRest;
    }

    @Override
    protected Message doBackward(final MessageRest messageRest) {
        Message message= new MessageEntity();
        message.setIdentifier(messageRest.getIdentifier());
        message.setHeadline(messageRest.getHeadline());
        message.setDescription(messageRest.getDescription());
        return message;
    }
}
