package de.dealog.msg.service;

import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;
import de.dealog.msg.persistence.MessageRepository;
import de.dealog.msg.persistence.MessageStatus;
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
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
@Slf4j
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    public PagedList<? extends Message> list(final Point<G2D> point, final int page, final int size) {
        log.debug("List messages for page {}, size {} and point '{}' ...", page, size, point);
        PanacheQuery<MessageEntity> messageQuery;

        Sort sort = Sort.descending("publishedAt");
        StringBuilder queryBuilder = new StringBuilder("status = :status");
        Parameters parameters = Parameters.with("status", MessageStatus.Published);
        if (point != null) {
            parameters.and("point", point);
            queryBuilder.append(" AND within(:point, geocode) = true");
        }
        messageQuery = messageRepository.find(queryBuilder.toString(), sort, parameters);

        List<MessageEntity> list = messageQuery.page(page, size).list();
        long count = messageQuery.count();
        int pageCount = messageQuery.pageCount();

        log.debug("... return {} / {} messages ", list.size(), count);

        return PagedList.<Message>builder()
                .page(page)
                .pageSize(size)
                .content(list)
                .count(count)
                .pageCount(pageCount)
                .build();
    }

    public void create(final Message message) {
        Validate.notNull(message, "The message should not be null");
        Validate.notEmpty(message.getIdentifier(), "The message identifier should not be empty");

        log.debug("Create message {}" , message);
        if (messageRepository.findByIdentifier(message.getIdentifier()) == null) {
            message.setStatus(MessageStatus.Published);
            messageRepository.persistAndFlush((MessageEntity) message);
        } else {
            log.error("Found duplicate identifier {}", message.getIdentifier());
        }
    }

    public void update(final Message message) {
        Validate.notNull(message, "The message should not be null");
        Validate.notEmpty(message.getIdentifier(), "The message identifier should not be empty");

        log.debug("Update message {}" , message);
        MessageEntity byIdentifier = messageRepository.findByIdentifier(message.getIdentifier());
        if (byIdentifier != null) {
            byIdentifier.setHeadline(message.getHeadline());
            byIdentifier.setDescription(message.getDescription());
            byIdentifier.setGeocode(message.getGeocode());
            messageRepository.persistAndFlush(byIdentifier);
        } else {
            log.error("Message for identifier {} not found", message.getIdentifier());
        }
    }

    public void updateStatus(final String identifier, final MessageStatus status) {
        Validate.notEmpty(identifier, "The message identifier should not be empty");
        Validate.notNull(status, "The message status should not be null");

        log.debug("Update status {} by identifier {}" , status, identifier);
        MessageEntity byIdentifier = messageRepository.findByIdentifier(identifier);
        if (byIdentifier != null) {
            byIdentifier.setStatus(status);
            messageRepository.persistAndFlush(byIdentifier);

        }else {
            log.error("Message for identifier {} not found.", identifier);
        }
    }
}
