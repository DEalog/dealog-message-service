package de.dealog.msg.rest.data;

import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.persistence.repository.MessageRepository;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;

public interface MessageResource extends PanacheRepositoryResource<MessageRepository, MessageEntity, Long> {
}
