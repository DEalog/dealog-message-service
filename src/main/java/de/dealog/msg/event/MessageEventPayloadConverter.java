package de.dealog.msg.event;

import com.google.common.base.Converter;
import de.dealog.common.model.MessageEventPayload;
import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.MessageEntity;
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
        message.setGeocode(Optional.ofNullable(payload.getGeocode()).map(this::convert).orElse(null));
        message.setPublishedAt(payload.getPublishedAt());
        return message;
    }

    private GeocodeEntity convert(final String geocode) {
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

        return GeocodeEntity.builder()
                .hash(DigestUtils.md5Hex(geocode))
                .polygons(multiPolygon)
                .build();
    }

    @Override
    protected MessageEventPayload doBackward(final MessageEntity message) {
        final MessageEventPayload payload= new MessageEventPayload();
        payload.setIdentifier(message.getIdentifier());
        payload.setHeadline(message.getHeadline());
        payload.setDescription(message.getDescription());
        payload.setGeocode(message.getGeocode().getPolygons().toString());
        payload.setPublishedAt(message.getPublishedAt());
        return payload;
    }
}
