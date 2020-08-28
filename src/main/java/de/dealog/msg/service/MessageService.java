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
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 *
 */
@ApplicationScoped
@NoArgsConstructor
@Slf4j
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    /**
     * Return a list of {@link Message}s
     * @param page
     * @param size
     * @return
     */
    public PagedList<? extends Message> list(final Point<G2D> point, final int page, final int size) {
        log.debug("List messages for page {}, size {} and point '{}' ...", page, size, point);
        PanacheQuery<MessageEntity> messageQuery;

        Sort sort = Sort.descending("publishedAt");
        StringBuilder queryBuilder = new StringBuilder("status = :status");
        Parameters parameters = Parameters.with("status", MessageStatus.Published);
        if (point != null) {
            parameters.and("point", point);
            queryBuilder.append(" within(:point, geocode) = true");
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

    /**
     * Create a new ...
     * @param message {@link MessageEntity}
     */
    public void create(final Message message) {
        log.debug("Create message {}" , message);
        if (messageRepository.findByIdentifier(message.getIdentifier()) == null) {
            message.setStatus(MessageStatus.Published);
            messageRepository.persistAndFlush((MessageEntity) message);
        } else {
            log.error("Found duplicate identifier {}", message.getIdentifier());
        }
    }

    /**
     * Message is updated
     * @param message The message
     */
    public void update(final Message message) {
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

    /**
     * Message is superseded so update state to {@link MessageStatus#Superseded}
     * @param message The message
     */
    public void supersede(final Message message) {
        log.debug("Supersede message {}" , message);
        MessageEntity byIdentifier = messageRepository.findByIdentifier(message.getIdentifier());
        if (byIdentifier != null) {
            byIdentifier.setStatus(MessageStatus.Superseded);
            messageRepository.persistAndFlush(byIdentifier);
        }else {
            log.error("Message for identifier {} not found", message.getIdentifier());
        }
    }
}
