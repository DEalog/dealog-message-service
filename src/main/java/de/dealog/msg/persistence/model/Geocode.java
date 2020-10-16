package de.dealog.msg.persistence.model;

import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;

public interface Geocode {
    MultiPolygon<G2D> getPolygons();

    String getHash();
}
