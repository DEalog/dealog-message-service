package de.dealog.msg.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeocodeRepository implements PanacheRepository<GeocodeEntity> {

    public GeocodeEntity findByHash(String hash){
        return find("hash", hash).firstResult();
    }
}
