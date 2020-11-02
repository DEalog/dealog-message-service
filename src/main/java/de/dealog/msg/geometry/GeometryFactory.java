package de.dealog.msg.geometry;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

public class GeometryFactory {

    /**
     * Decode a Polygon or Multipolygon as WKT String to a {@link MultiPolygon} with predefined coordinate reference system
     * {@link CoordinateReferenceSystems#WGS84}
     *
     * @param geocode the WKT String to decode
     * @return the {@link MultiPolygon} object
     * @throws UnsupportedGeometryException if its neither a polygon nor a multipolygon
     */
    public static MultiPolygon<G2D> decodeWkt(final String geocode) {
        final MultiPolygon<G2D> multiPolygon;
        final Geometry<G2D> decode = Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(geocode, CoordinateReferenceSystems.WGS84);
        switch (decode.getGeometryType()) {
            case POLYGON:
                multiPolygon = DSL.multipolygon((Polygon<G2D>) decode);
                break;
            case MULTIPOLYGON:
                multiPolygon = (MultiPolygon<G2D>) decode;
                break;
            default:
                throw new UnsupportedGeometryException(String.format("Unsupported geometry %s", decode.getGeometryType()));
        }

        return multiPolygon;
    }

    /**
     * Creates a {@code Point} from a longitude and latitude with predefined coordinate reference system
     * {@link CoordinateReferenceSystems#WGS84}
     *
     * @param longitude the longitude value
     * @param latitude the latitude value
     * @return a {@link Point}
     */
    public static Point<G2D> mkPoint(final double longitude, final double latitude) {
        return Geometries.mkPoint(new G2D(longitude, latitude), CoordinateReferenceSystems.WGS84);
    }
}
