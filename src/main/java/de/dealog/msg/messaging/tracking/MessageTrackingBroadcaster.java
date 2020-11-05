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

    public static final String MESSAGE_SINGLE_REQUEST = "message-single-request";

    public static final String MESSAGE_LIST_REQUEST = "message-list-request";

    @Inject
    @Channel("message-tracking")
    Emitter<MessageTracking> viewEmitter;

    @ConsumeEvent(MESSAGE_SINGLE_REQUEST)
    public void consume(final String id) {
        if (StringUtils.isNotEmpty(id)) {
            broadcast(Collections.singletonList(id), MessageTrackingType.SingleMessageRequested);
        }
    }

    @ConsumeEvent(MESSAGE_LIST_REQUEST)
    public void consume(final JsonArray json) {
        if (json != null) {
            final List ids = json.getList();
            broadcast(ids, MessageTrackingType.ListRequested);
        }
    }

    private void broadcast(final List<String> ids, final MessageTrackingType type) {
        final MessageTrackingPayload payload = new MessageTrackingPayload();
        payload.setIds(ids);
        payload.setTrackedAt(new Date());

        final MessageTracking messageTracking = new MessageTracking();
        messageTracking.setType(type);
        messageTracking.setPayload(payload);
        log.debug("Send message tracking event '{}'", messageTracking.getType());
        viewEmitter.send(messageTracking);
    }
}
