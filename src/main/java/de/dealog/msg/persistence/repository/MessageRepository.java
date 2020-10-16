package de.dealog.msg.persistence.repository;

import de.dealog.msg.persistence.model.MessageEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of a {@link MessageEntity} repository
 */
@ApplicationScoped
public class MessageRepository implements PanacheRepository<MessageEntity> {
    public MessageEntity findByIdentifier(final String identifier){
        return find("identifier", identifier).firstResult();
    }
}
