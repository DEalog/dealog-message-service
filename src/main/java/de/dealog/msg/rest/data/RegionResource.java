package de.dealog.msg.rest.data;

import de.dealog.msg.persistence.model.RegionEntity;
import de.dealog.msg.persistence.repository.RegionRepository;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;

public interface RegionResource extends PanacheRepositoryResource<RegionRepository, RegionEntity, Long> {
}
