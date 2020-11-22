package de.dealog.msg.messaging.tracking;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Model of the message tracking
 */
@Builder
@Getter
@RegisterForReflection
public class MessagesTracking implements Serializable {

    private static final long serialVersionUID = 123L;

    /**
     * The {@link MessagesTrackingType} of the message tracking
     */
    private final MessagesTrackingType type;

    /**
     * The payload containing the message tracking
     */
    private final MessagesTrackingPayload payload;

}
