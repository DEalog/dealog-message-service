package de.dealog.msg.rest.model;

import de.dealog.msg.geometry.GeometryFactory;
import de.dealog.msg.rest.validations.ValidGeoRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import javax.ws.rs.QueryParam;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GeoRequest {
    /**
     *  The name of the HTTP longitude query parameter
     */
    public static final String LONGITUDE = "long";

    /**
     *  The name of the HTTP latitude query parameter
     */
    public static final String LATITUDE = "lat";

    @QueryParam("long")
    private Double longitude;

    @QueryParam("lat")
    private Double latitude;

    /**
     * Returns a {@code Point}, if the longitude and latitude are present.
     *
     * @return the {@link Point} representing the long and lat exists.
     */
    public Point<G2D> getPoint() {
        Point<G2D>  point = null;
        if (longitude != null && latitude != null) {
           point = GeometryFactory.mkPoint(longitude, latitude);
        }
        return point;
    }
}
