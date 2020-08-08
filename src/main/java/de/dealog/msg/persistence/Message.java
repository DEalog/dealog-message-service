package de.dealog.msg.persistence;

import java.util.Date;

public interface Message {

    void setIdentifier(String identifier);

    void setHeadline(String headline);

    void setDescription(String description);

    String getIdentifier();

    String getHeadline();

    String getDescription();

    Date getCreatedDate();

    Date getLastModifiedDate();

    String toString();
}
