package de.dealog.msg.persistence;

import org.geolatte.geom.Polygon;

import java.util.Date;

public interface Message {

    void setIdentifier(String identifier);

    void setHeadline(String headline);

    void setDescription(String description);

    void setGeocode(Polygon geocode);

    void setPublishedAt(Date publishedAd);

    String getIdentifier();

    String getHeadline();

    String getDescription();

    Polygon getGeocode();

    Date getPublishedAt();

    Date getCreatedAt();

    Date getModifiedAt();

    String toString();
}
