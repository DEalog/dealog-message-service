package de.dealog.msg.event;

import com.google.common.base.Converter;
import de.dealog.common.model.MessageEventPayload;
import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.converter.UnsupportedConversionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.codec.WktDecoder;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import javax.inject.Singleton;
import java.util.Optional;

/**
 *
 */
@Singleton
@Slf4j
public class MessageEventPayloadConverter extends Converter<MessageEventPayload, MessageEntity> {

    @Override
    protected MessageEntity doForward(final MessageEventPayload payload) {
        final MessageEntity message= new MessageEntity();
        message.setIdentifier(payload.getIdentifier());
        message.setHeadline(payload.getHeadline());
        message.setDescription(payload.getDescription());
        message.setRegionCode(payload.getArs());
        message.setGeocode(Optional.ofNullable(payload.getGeocode()).map(this::accept).orElse(null));
        message.setPublishedAt(payload.getPublishedAt());
        return message;
    }

    private GeocodeEntity accept(final String geocode) {
        return GeocodeEntity.builder()
            .hash(DigestUtils.md5Hex(geocode))
            .polygons(convert(geocode))
            .build();
    }

    /**
     * Convert a
     * @param geocode ... WKT String
     * @return ... a {@link MultiPolygon} object
     * @throws UnsupportedGeometryException if its neither a polygon nor a multipolygon
     */
    private MultiPolygon<G2D> convert(final String geocode) {
        final MultiPolygon<G2D> multiPolygon;
        final WktDecoder decoder = Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1);
        final Geometry<G2D> decode = decoder.decode(geocode, CoordinateReferenceSystems.WGS84);
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

    @Override
    protected MessageEventPayload doBackward(final MessageEntity message) {
        throw new UnsupportedConversionException(String.format("Unsupported conversion from %s", message));
    }
}
