package de.dealog.msg.event;

import com.google.common.base.Converter;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;
import de.dealog.common.model.MessageEventPayload;

import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class MessageEventPayloadConverter extends Converter<MessageEventPayload, Message> {

    @Override
    protected Message doForward(final MessageEventPayload payload) {
        Message message= new MessageEntity();
        message.setIdentifier(payload.getIdentifier());
        message.setHeadline(payload.getHeadline());
        message.setDescription(payload.getDescription());
        return message;
    }

    @Override
    protected MessageEventPayload doBackward(final Message message) {
        MessageEventPayload payload= new MessageEventPayload();
        payload.setIdentifier(message.getIdentifier());
        payload.setHeadline(message.getHeadline());
        payload.setDescription(message.getDescription());
        return payload;
    }
}
