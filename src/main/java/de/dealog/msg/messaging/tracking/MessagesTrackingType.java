package de.dealog.msg.messaging.tracking;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * The available message tracking types
 */
@RegisterForReflection
public enum MessagesTrackingType {

    /**
     * A single message is requested
     */
    SingleMessageRequested,

    /**
     * A list of messages are requested
     */
    ListRequested;
}
