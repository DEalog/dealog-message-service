package de.dealog.msg.converter;

import com.google.common.base.Converter;
import de.dealog.common.converter.UnsupportedConversionException;
import de.dealog.common.messaging.message.MessageEventPayload;
import de.dealog.common.model.Category;
import de.dealog.msg.geometry.GeometryFactory;
import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

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
        message.setOrganization(payload.getOrganization());
        message.setHeadline(payload.getHeadline());
        message.setDescription(payload.getDescription());
        message.setRegionCode(payload.getArs());
        message.setCategory(Optional.ofNullable(payload.getCategory()).orElse(Category.Other));
        message.setGeocode(Optional.ofNullable(payload.getGeocode()).map(this::accept).orElse(null));
        message.setPublishedAt(payload.getPublishedAt());
        return message;
    }

    private GeocodeEntity accept(final String geocode) {
        return StringUtils.isNotEmpty(geocode) ? GeocodeEntity.builder()
            .hash(DigestUtils.md5Hex(geocode))
            .polygons(GeometryFactory.decodeWkt(geocode))
            .build() : null;
    }

    @Override
    protected MessageEventPayload doBackward(final MessageEntity message) {
        throw new UnsupportedConversionException(String.format("Unsupported conversion from %s", message));
    }
}
