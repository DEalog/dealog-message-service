package de.dealog.msg.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of a {@link MessageEntity} repository
 */
@ApplicationScoped
public class MessageRepository implements PanacheRepository<MessageEntity> {

    public MessageEntity findByIdentifier(String identifier){
        return find("identifier", identifier).firstResult();
    }
}
