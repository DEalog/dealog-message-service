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
        try {
            Message message = messageEventPayloadConverter.convert(messageEvent.getPayload());
            switch (messageEvent.getType()) {
                case Imported:
                case Created:
                    handleCreateEvent(message);
                    break;
                case Updated:
                    handleUpdateEvent(message);
                    break;
                case Superseded:
                    handleSupersedeEvent(message);
                    break;
                default:
                    log.debug("Handler for message event type  '{}' is not implemented.", messageEvent.getType());
            }
        } catch (UnsupportedGeometryException e) {
            log.error("Process of message with identifier '{}' failed.", messageEvent.getPayload().getIdentifier(), e);
        }
    }

    private void handleSupersedeEvent(Message message) { messageService.supersede(message);}

    private void handleCreateEvent(final Message message) {
        messageService.create(message);
    }

    private void handleUpdateEvent(final Message message) {
        messageService.update(message);
    }

}
