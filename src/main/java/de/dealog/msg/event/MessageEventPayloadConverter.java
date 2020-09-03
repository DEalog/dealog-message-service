package de.dealog.msg.event;

import com.google.common.base.Converter;
import de.dealog.common.model.MessageEventPayload;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.*;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.codec.WktDecoder;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import javax.inject.Singleton;

/**
 *
 */
@Singleton
@Slf4j
public class MessageEventPayloadConverter extends Converter<MessageEventPayload, Message> {

    @Override
    protected Message doForward(final MessageEventPayload payload) {
        Message message= new MessageEntity();
        message.setIdentifier(payload.getIdentifier());
        message.setHeadline(payload.getHeadline());
        message.setDescription(payload.getDescription());
        message.setGeocode(convert(payload.getGeocode()));
        message.setPublishedAt(payload.getPublishedAt());
        return message;
    }

    private MultiPolygon<G2D> convert(final String geocode) {
        MultiPolygon<G2D> multiPolygon;
        WktDecoder decoder = Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1);
        Geometry<G2D> decode = decoder.decode(geocode, CoordinateReferenceSystems.WGS84);
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
    protected MessageEventPayload doBackward(final Message message) {
        MessageEventPayload payload= new MessageEventPayload();
        payload.setIdentifier(message.getIdentifier());
        payload.setHeadline(message.getHeadline());
        payload.setDescription(message.getDescription());
        payload.setGeocode(message.getGeocode().toString());
        payload.setPublishedAt(message.getPublishedAt());
        return payload;
    }
}
