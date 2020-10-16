package de.dealog.msg.persistence.repository;

import de.dealog.msg.persistence.model.GeocodeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeocodeRepository implements PanacheRepository<GeocodeEntity> {

    public GeocodeEntity findByHash(final String hash){
        return find("hash", hash).firstResult();
    }
}
