package de.dealog.msg.messaging.tracking;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * The payload contains the data of the message
 */
@Getter
@Setter
@ToString
@RegisterForReflection
public class MessagesTrackingPayload {

    /**
     * A list of tracked message ids
     */
    private List<String> ids;

    /**
     * The date and time when the message request was tracked in UTC
     */
    private Date trackedAt;
}


