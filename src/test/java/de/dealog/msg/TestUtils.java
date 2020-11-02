package de.dealog.msg;

import de.dealog.msg.persistence.model.RegionEntity;
import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.rest.model.RegionRest;
import de.dealog.msg.rest.model.MessageRest;
import de.dealog.msg.rest.model.RegionTypeRest;
import org.apache.commons.codec.digest.DigestUtils;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import java.util.Date;

public class TestUtils {

    public static final String SIZE_FAILS = "size must be between";

    public static final String PATTERN_FAILS = "must match";

    public static MessageEntity buildMessage(final String identifier, final String headline, final String description, final String regionCode) {
        final MessageEntity message = new MessageEntity();
        message.setIdentifier(identifier);
        message.setDescription(description);
        message.setHeadline(headline);
        message.setRegionCode(regionCode);
        message.setGeocode(buildGeocode());
        message.setPublishedAt(new Date());
        return message;
    }

    public static GeocodeEntity buildGeocode () {
        final String wkt = "MULTIPOLYGON (((11.795583 51.385388, 11.813135 51.398215, 11.827426 51.396073, 11.823821 51.391842, 11.795583 51.385388)))";
        return GeocodeEntity.builder()
                .polygons((MultiPolygon<G2D>) Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(wkt, CoordinateReferenceSystems.WGS84))
                .hash(DigestUtils.md5Hex(wkt))
                .build();
    }

    public static MessageRest buildMessageRest(final String identifier, final String headline, final String description) {
        final MessageRest messageRest = new MessageRest();
        messageRest.setIdentifier(identifier);
        messageRest.setDescription(description);
        messageRest.setHeadline(headline);
        messageRest.setPublishedAt(new Date());
        return messageRest;
    }

    public static RegionRest buildRegionRest(final String ars, final String name, final RegionTypeRest regionTypeRest) {
        final RegionRest regionRest = new RegionRest();
        regionRest.setArs(ars);
        regionRest.setName(name);
        regionRest.setType(regionTypeRest);
        return regionRest;
    }

    public static RegionEntity buildRegion(final String code, final String name, final String description) {
        final RegionEntity regionEntity = new RegionEntity();
        regionEntity.setCode(code);
        regionEntity.setName(name);
        regionEntity.setDescription(description);
        return regionEntity;
    }
}
