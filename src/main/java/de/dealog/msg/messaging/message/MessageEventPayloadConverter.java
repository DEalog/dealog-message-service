package de.dealog.msg.messaging.message;

import com.google.common.base.Converter;
import de.dealog.common.model.MessageEventPayload;
import de.dealog.msg.converter.UnsupportedConversionException;
import de.dealog.msg.geometry.GeometryFactory;
import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

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
            .polygons(GeometryFactory.decodeWkt(geocode))
            .build();
    }


    @Override
    protected MessageEventPayload doBackward(final MessageEntity message) {
        throw new UnsupportedConversionException(String.format("Unsupported conversion from %s", message));
    }
}
