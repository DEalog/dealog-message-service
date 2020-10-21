package de.dealog.msg.persistence.repository;

import de.dealog.msg.persistence.model.RegionEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegionRepository implements PanacheRepository<RegionEntity> {

    public RegionEntity findByArs(final String ars){
        return find("ars", ars).firstResult();
    }
}
