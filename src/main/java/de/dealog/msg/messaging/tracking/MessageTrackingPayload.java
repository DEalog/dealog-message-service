package de.dealog.msg.messaging.tracking;

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
public class MessageTrackingPayload {

    /**
     * A list of tracked message ids
     */
    private List<String> ids;

    /**
     * The date and time when the message request was tracked in UTC
     */
    private Date trackedAt;
}


