package de.dealog.msg.messaging.tracking;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * The message tracking
 */
@Getter
@Setter
public class MessageTracking implements Serializable {

    private static final long serialVersionUID = 123L;

    /**
     * The {@link MessageTrackingType} of the message tracking
     */
    private MessageTrackingType type;

    /**
     * The payload containing the message tracking
     */
    private MessageTrackingPayload payload;

}
