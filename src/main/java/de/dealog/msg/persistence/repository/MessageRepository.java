package de.dealog.msg.persistence.repository;

import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.common.model.Status;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<MessageEntity> {

    public MessageEntity findByIdentifier(final String identifier){
        return find("identifier", identifier).firstResult();
    }

    public MessageEntity findByIdentifierAndStatus(final String identifier, final Status status){
        return find("identifier = ?1 and status = ?2", identifier, status).firstResult();
    }
}
