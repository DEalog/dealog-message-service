package de.dealog.msg.service;

import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;
import de.dealog.msg.persistence.MessageRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

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
    public PagedList<? extends Message> list(final int page, final int size) {
        log.debug("List messages for page {} and size {} ...", page, size);
        PanacheQuery<MessageEntity> messageQuery = messageRepository.findAll();
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
     *
     * @param message
     */
    public void create(final Message message) {
        log.debug("Create new message {}" , message);
        if (messageRepository.findByIdentifier(message.getIdentifier()) == null) {
            messageRepository.persistAndFlush((MessageEntity) message);
        } else {
            log.error("Found duplicate identifier {}", message.getIdentifier());
        }
    }
}
