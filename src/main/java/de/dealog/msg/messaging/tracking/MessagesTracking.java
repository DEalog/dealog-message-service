package de.dealog.msg.messaging.tracking;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * The message tracking
 */
@Getter
@Setter
@RegisterForReflection
public class MessagesTracking implements Serializable {

    private static final long serialVersionUID = 123L;

    /**
     * The {@link MessagesTrackingType} of the message tracking
     */
    private MessagesTrackingType type;

    /**
     * The payload containing the message tracking
     */
    private MessagesTrackingPayload payload;

}
