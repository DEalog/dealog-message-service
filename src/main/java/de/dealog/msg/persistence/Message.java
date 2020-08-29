package de.dealog.msg.persistence;

import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;

import java.util.Date;

public interface Message {

    void setIdentifier(String identifier);

    void setHeadline(String headline);

    void setDescription(String description);

    void setGeocode(MultiPolygon<G2D> geocode);

    void setStatus(MessageStatus status);

    void setPublishedAt(Date publishedAd);

    String getIdentifier();

    String getHeadline();

    String getDescription();

    MultiPolygon getGeocode();

    MessageStatus getStatus();

    Date getPublishedAt();

    Date getCreatedAt();

    Date getModifiedAt();

    String toString();
}
