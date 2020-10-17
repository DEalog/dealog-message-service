package de.dealog.msg.service;

import de.dealog.msg.persistence.model.GeocodeEntity;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.persistence.repository.GeocodeRepository;
import de.dealog.msg.persistence.repository.MessageRepository;
import de.dealog.msg.rest.model.PagedList;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
@Slf4j
public class MessageService {

    public static final String POINT = "point";

    @Inject
    MessageRepository messageRepository;

    @Inject
    GeocodeRepository geocodeRepository;

    public Optional<Message> find(final String identifier, final MessageStatus status) {
        log.debug("Find message by identifier {} in status {}", identifier, status);
        final MessageEntity byIdentifier = messageRepository.findByIdentifierAndStatus(identifier, status);
        return Optional.ofNullable(byIdentifier);
    }

    public PagedList<? extends Message> find(final Point<G2D> point, final int page, final int size) {
        log.debug("List messages for page {}, size {} and point '{}' ...", page, size, point);
        final PanacheQuery<MessageEntity> messageQuery;
        final StringBuilder queryBuilder = new StringBuilder("status = :status");
        final Parameters parameters = Parameters.with("status", MessageStatus.Published);
        if (point != null) {
            parameters.and(POINT, point);
            queryBuilder.append(" AND within(:point, geocode.polygons) = true");
        }
        messageQuery = messageRepository.find(queryBuilder.toString(), Sort.descending("publishedAt"), parameters);

        final List<MessageEntity> list = messageQuery.page(page, size).list();

        log.debug("... return {} / {} messages ", list.size(), messageQuery.count());

        return PagedList.<Message>builder()
                .page(page)
                .pageSize(size)
                .content(list)
                .count(messageQuery.count())
                .pageCount(messageQuery.pageCount())
                .build();
    }

    @Transactional
    public void create(final Message message) {
        Validate.notNull(message, "The message should not be null");
        Validate.notEmpty(message.getIdentifier(), "The message identifier should not be empty");

        log.debug("Create message {}" , message);
        if (messageRepository.findByIdentifier(message.getIdentifier()) == null) {
            final MessageEntity messageEntity = (MessageEntity) message;
            final GeocodeEntity geocodeEntity =
                    Optional.ofNullable(geocodeRepository.findByHash(message.getGeocode().getHash()))
                    .orElse((GeocodeEntity) message.getGeocode());
            messageEntity.setGeocode(geocodeEntity);
            messageEntity.setStatus(MessageStatus.Published);
            try {
                messageRepository.persistAndFlush(messageEntity);
            } catch (final Exception e) {
                log.error("Create message {} failed!", message.getIdentifier(), e);
            }

        } else {
            log.error("Found duplicate identifier {}", message.getIdentifier());
        }
    }

    @Transactional
    public void update(final Message message) {
        Validate.notNull(message, "The message should not be null");
        Validate.notEmpty(message.getIdentifier(), "The message identifier should not be empty");

        log.debug("Update message {}" , message);
        final MessageEntity byIdentifier = messageRepository.findByIdentifier(message.getIdentifier());
        if (byIdentifier != null) {
            byIdentifier.setHeadline(message.getHeadline());
            byIdentifier.setDescription(message.getDescription());

            final GeocodeEntity geocodeEntity =
                    Optional.ofNullable(geocodeRepository.findByHash(message.getGeocode().getHash()))
                    .orElse((GeocodeEntity) message.getGeocode());
            byIdentifier.setGeocode(geocodeEntity);
            messageRepository.persistAndFlush(byIdentifier);
        } else {
            log.error("Message for identifier {} not found", message.getIdentifier());
        }
    }

    @Transactional
    public void updateStatus(final String identifier, final MessageStatus status) {
        Validate.notEmpty(identifier, "The message identifier should not be empty");
        Validate.notNull(status, "The message status should not be null");

        log.debug("Update status {} by identifier {}" , status, identifier);
        final MessageEntity byIdentifier = messageRepository.findByIdentifier(identifier);
        if (byIdentifier != null) {
            byIdentifier.setStatus(status);
            messageRepository.persistAndFlush(byIdentifier);

        }else {
            log.error("Message for identifier {} not found.", identifier);
        }
    }
}
