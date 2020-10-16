package de.dealog.msg;

import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.rest.model.MessageRest;
import org.apache.commons.codec.digest.DigestUtils;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import java.util.Date;

public class TestUtils {

    public static MessageEntity buildMessage(final String identifier, final String headline, final String description) {
        MessageEntity message = new MessageEntity();
        message.setGeocode(buildGeocode());
        message.setIdentifier(identifier);
        message.setDescription(description);
        message.setHeadline(headline);
        message.setPublishedAt(new Date());
        return message;
    }

    public static GeocodeEntity buildGeocode () {
        String wkt = "MULTIPOLYGON (((11.795583 51.385388, 11.813135 51.398215, 11.827426 51.396073, 11.823821 51.391842, 11.795583 51.385388)))";
        return GeocodeEntity.builder()
                .polygons((MultiPolygon<G2D>) Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(wkt, CoordinateReferenceSystems.WGS84))
                .hash(DigestUtils.md5Hex(wkt))
                .build();
    }

    public static MessageRest buildMessageRest(final String identifier, final String headline, final String description) {
        MessageRest messageRest = new MessageRest();
        messageRest.setIdentifier(identifier);
        messageRest.setDescription(description);
        messageRest.setHeadline(headline);
        messageRest.setPublishedAt(new Date());
        return messageRest;
    }
}
