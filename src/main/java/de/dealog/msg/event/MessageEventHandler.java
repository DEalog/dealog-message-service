package de.dealog.msg.event;

import de.dealog.common.model.MessageEvent;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.service.MessageService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Slf4j
public class MessageEventHandler {

    @Inject
    MessageEventPayloadConverter messageEventPayloadConverter;

    @Inject
    MessageService messageService;

    @Incoming("messages")
    @Blocking
    @Transactional
    public void process(final MessageEvent messageEvent) {
        log.debug("Received message event type '{}'", messageEvent.getType());
        Message message = messageEventPayloadConverter.convert(messageEvent.getPayload());
        switch (messageEvent.getType()) {
            case Created:
                handlePublishEvent(message);
                break;
            case Imported:
            case Updated:
            case Superseded:
            default:
                log.debug("Handler for message event type  '{}' is not implemented.", messageEvent.getType());
        }
    }

    private void handlePublishEvent(final Message message) {
        messageService.create(message);
    }

}
