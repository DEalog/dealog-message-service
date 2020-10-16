package de.dealog.msg.persistence.model;

import java.util.Date;

public interface Message {

    String getIdentifier();

    String getHeadline();

    String getDescription();

    Geocode getGeocode();

    MessageStatus getStatus();

    Date getPublishedAt();

    String toString();
}
