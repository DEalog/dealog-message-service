package de.dealog.msg.messaging.message;

import de.dealog.common.messaging.message.MessageEventPayload;
import de.dealog.common.model.Category;
import de.dealog.msg.TestUtils;
import de.dealog.msg.converter.UnsupportedConversionException;
import de.dealog.msg.persistence.model.MessageEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class MessageEventPayloadConverterTest {

    @Inject
    MessageEventPayloadConverter messageEventPayloadConverter;

    @Test
    void doForward() {
        final MessageEventPayload payload = new MessageEventPayload();
        payload.setIdentifier("identifier");
        payload.setDescription("description");
        payload.setHeadline("headline");
        payload.setArs("regionCode");
        payload.setOrganization("organization");

        final MessageEntity message = messageEventPayloadConverter.doForward(payload);
        assertEquals(payload.getIdentifier(), message.getIdentifier());
        assertEquals(payload.getDescription(), message.getDescription());
        assertEquals(payload.getArs(), message.getRegionCode());
        assertEquals(payload.getOrganization(), message.getOrganization());

        assertEquals(Category.Other, message.getCategory());
        assertEquals(null, message.getGeocode());

    }

    @Test
    void doBackward() {
        final MessageEntity message = TestUtils.buildMessage("1", "2", "3", "4");
        assertThrows(UnsupportedConversionException.class, () -> messageEventPayloadConverter.doBackward(message));
    }
}