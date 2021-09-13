package de.dealog.msg.messaging.message;

import de.dealog.common.messaging.message.MessageEvent;
import de.dealog.common.model.Status;
import de.dealog.msg.converter.MessageEventPayloadConverter;
import de.dealog.msg.geometry.UnsupportedGeometryException;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.service.MessageService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
@Slf4j
public class MessageEventConsumer {

    MessageEventPayloadConverter messageEventPayloadConverter;

    MessageService messageService;

    @Inject
    public MessageEventConsumer(final MessageEventPayloadConverter messageEventPayloadConverter,
                                final MessageService messageService) {
        this.messageEventPayloadConverter = messageEventPayloadConverter;
        this.messageService = messageService;
    }

    @Incoming("messages")
    @Blocking
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public void process(final JsonObject eventJson) {

        MessageEvent messageEvent = eventJson.mapTo(MessageEvent.class);
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
                    handleStatusUpdateEvent(message.getIdentifier(), Status.Superseded);
                    break;
                case Disposed:
                    handleStatusUpdateEvent(message.getIdentifier(), Status.Disposed);
                    break;
                default:
                    log.debug("Handler for message event type  '{}' is not implemented.", messageEvent.getType());
            }
        }
    }


    private void handleCreateOrUpdateEvent(final Message message) {
        messageService.createOrUpdate(message);
    }

    private void handleStatusUpdateEvent(final String identifier, final Status status) {
        Validate.notEmpty(identifier, "The message identifier should not be null");
        messageService.updateStatus(identifier, status);
    }
}
