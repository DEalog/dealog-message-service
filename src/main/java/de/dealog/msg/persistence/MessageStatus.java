package de.dealog.msg.persistence;

/**
 * The status of the message
 */
public enum MessageStatus {

    /**
     * The message was created within e.g. the backoffice or imported e.g Mowas
     */
    Published,

    /**
     * The message is superseded
     */
    Superseded;
}