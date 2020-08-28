package de.dealog.msg.event;

import com.google.common.base.Converter;
import de.dealog.common.model.MessageEventPayload;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.codec.WktDecoder;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class MessageEventPayloadConverter extends Converter<MessageEventPayload, Message> {

    @Override
    protected Message doForward(final MessageEventPayload payload) {
        Message message= new MessageEntity();
        message.setIdentifier(payload.getIdentifier());
        message.setHeadline(payload.getHeadline());
        message.setDescription(payload.getDescription());
        WktDecoder decoder = Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1);
        Polygon<?> polygon = (Polygon<?>) decoder.decode(payload.getGeocode(), CoordinateReferenceSystems.WGS84);
        message.setGeocode(polygon);
        message.setPublishedAt(payload.getPublishedAt());
        return message;
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
