package de.dealog.msg.persistence.model;

import de.dealog.common.model.Category;
import de.dealog.common.model.Status;

import java.util.Date;

public interface Message {

    String getIdentifier();

    String getOrganization();

    String getHeadline();

    String getDescription();

    Geocode getGeocode();

    String getRegionCode();

    Status getStatus();

    Category getCategory();

    Date getPublishedAt();

    String toString();
}
