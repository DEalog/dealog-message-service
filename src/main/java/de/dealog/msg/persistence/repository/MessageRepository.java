package de.dealog.msg.persistence.repository;

import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.persistence.model.MessageStatus;
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

    public MessageEntity findByIdentifierAndStatus(final String identifier, final MessageStatus status){
        return find("identifier = ?1 and status = ?2", identifier, status).firstResult();
    }
}
