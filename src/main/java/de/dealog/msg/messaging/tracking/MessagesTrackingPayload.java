package de.dealog.msg.messaging.tracking;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * The payload contains the data of the message
 */
@Builder
@Getter
@ToString
@RegisterForReflection
public class MessagesTrackingPayload {

    /**
     * A list of tracked message ids
     */
    private final List<String> ids;

    /**
     * The date and time when the message request was tracked in UTC
     */
    private final Date trackedAt;
}


