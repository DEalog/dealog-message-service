package de.dealog.msg.messaging.tracking;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Slf4j
public class MessageTrackingBroadcaster {

    /**
     * Single message request event
     */
    public static final String MESSAGE_SINGLE_REQUEST = "message-single-request";

    /**
     * List message request event
     */
    public static final String MESSAGE_LIST_REQUEST = "message-list-request";

    private final Emitter<MessagesTracking> viewEmitter;

    @Inject
    public MessageTrackingBroadcaster(@Channel("messages-tracking") final Emitter<MessagesTracking> viewEmitter) {
        this.viewEmitter = viewEmitter;
    }

    @ConsumeEvent(MESSAGE_SINGLE_REQUEST)
    public void consume(final String id) {
        if (StringUtils.isNotEmpty(id)) {
            broadcast(Collections.singletonList(id), MessagesTrackingType.SingleMessageRequested);
        }
    }

    @ConsumeEvent(MESSAGE_LIST_REQUEST)
    public void consume(final JsonArray json) {
        if (json != null && json.getList().size() > 0) {
            broadcast(json.getList(), MessagesTrackingType.ListRequested);
        }
    }

    private void broadcast(final List<String> ids, final MessagesTrackingType type) {
        final MessagesTrackingPayload payload = MessagesTrackingPayload.builder()
                .ids(ids)
                .trackedAt(new Date())
                .build();

        final MessagesTracking messageTracking = MessagesTracking.builder()
                .type(type)
                .payload(payload)
                .build();
        log.debug("Broadcast messages tracking event '{}'", messageTracking.getType());
        viewEmitter.send(messageTracking);
    }
}
