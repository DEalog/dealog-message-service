package de.dealog.msg.messaging.message;

import de.dealog.common.model.MessageEvent;
import de.dealog.msg.geometry.UnsupportedGeometryException;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.service.MessageService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class MessageEventConsumer {

    @Inject
    MessageEventPayloadConverter messageEventPayloadConverter;

    @Inject
    MessageService messageService;

    @Incoming("messages")
    @Blocking
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public void process(final MessageEvent messageEvent) {
        log.debug("Received message event type '{}'", messageEvent.getType());

        Message message = null;
        try {
            message = messageEventPayloadConverter.convert(messageEvent.getPayload());
        } catch (final UnsupportedGeometryException e) {
            log.error("Process of message with identifier '{}' failed.", messageEvent.getPayload().getIdentifier(), e);
        }

        if (message != null) {
            switch (messageEvent.getType()) {
                case Imported:
                case Created:
                case Updated:
                    handleCreateOrUpdateEvent(message);
                    break;
                case Superseded:
                    handleStatusUpdateEvent(message.getIdentifier(), MessageStatus.Superseded);
                    break;
                case Disposed:
                    handleStatusUpdateEvent(message.getIdentifier(), MessageStatus.Disposed);
                    break;
                default:
                    log.debug("Handler for message event type  '{}' is not implemented.", messageEvent.getType());
            }
        }
    }


    private void handleCreateOrUpdateEvent(final Message message) {
        messageService.createOrUpdate(message);
    }

    private void handleStatusUpdateEvent(final String identifier, final MessageStatus status) {
        Validate.notEmpty(identifier, "The message identifier should not be null");
        messageService.updateStatus(identifier, status);
    }
}
